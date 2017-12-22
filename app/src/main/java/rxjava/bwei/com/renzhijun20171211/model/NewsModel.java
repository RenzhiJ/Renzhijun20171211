package rxjava.bwei.com.renzhijun20171211.model;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import rxjava.bwei.com.renzhijun20171211.bean.MessageBean;
import rxjava.bwei.com.renzhijun20171211.bean.News;
import rxjava.bwei.com.renzhijun20171211.http.RetrofitUtils;
import rxjava.bwei.com.renzhijun20171211.presenter.NewsPresenter;

/**
 * Created by Zhijun on 2017/12/11.
 */

public class NewsModel implements IModel {
    private NewsPresenter presenter;
    public NewsModel(NewsPresenter presenter){
        this.presenter = presenter;
    }
    @Override
    public void getData(Map<String, String> map) {
        Flowable<MessageBean<List<News>>> flowable = RetrofitUtils.getInstance().getApiService()
                .getNews(map);
        presenter.getNews(flowable);
    }
}