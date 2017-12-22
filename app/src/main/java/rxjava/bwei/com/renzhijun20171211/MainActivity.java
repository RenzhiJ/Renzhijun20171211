package rxjava.bwei.com.renzhijun20171211;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rxjava.bwei.com.renzhijun20171211.bean.News;
import rxjava.bwei.com.renzhijun20171211.presenter.NewsPresenter;
import rxjava.bwei.com.renzhijun20171211.view.IView;

public class MainActivity extends AppCompatActivity implements IView {

    private static final String TAG = "MainActivity";
    private NewsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> map = new HashMap<>();
        //拼接字符串
        map.put("uid", "72");
        map.put("pid", "1");

        presenter = new NewsPresenter();
        presenter.attachView(this);
        presenter.getData(map);
    }

    @Override
    public void onSuccess(Object o) {
        if (o instanceof List) {
            //打印集合数据
            List<News> news = (List<News>) o;
            Log.i("TAG", "onSuccess: " + news.size());
        }
    }

    @Override
    public void onFailed(Exception e) {
        Log.e(TAG, "onFailed: " + e.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}