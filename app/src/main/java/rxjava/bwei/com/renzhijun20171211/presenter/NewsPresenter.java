package rxjava.bwei.com.renzhijun20171211.presenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import rxjava.bwei.com.renzhijun20171211.bean.MessageBean;
import rxjava.bwei.com.renzhijun20171211.bean.News;
import rxjava.bwei.com.renzhijun20171211.model.NewsModel;
import rxjava.bwei.com.renzhijun20171211.view.IView;

/**
 * Created by Zhijun on 2017/12/11.
 */

public class NewsPresenter implements BasePresenter{

    private IView iv;
    private DisposableSubscriber subscriber;

    public void attachView(IView iv) {
        this.iv = iv;
    }

    public void detachView() {
        if (iv != null) {
            iv = null;
        }
        if (subscriber != null) {
            if (!subscriber.isDisposed()) {
                subscriber.dispose();
            }
        }
    }
    @Override
    public void getData(Map<String, String> map) {
        //多态
        final NewsModel model = new NewsModel(this);
        model.getData(map);

    }
    public void getNews(Flowable<MessageBean<List<News>>> flowable) {
        subscriber = (DisposableSubscriber) flowable.subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<MessageBean<List<News>>>() {
                    @Override
                    public void onNext(MessageBean<List<News>> listMessageBean) {
                        if (listMessageBean != null) {
                            List<News> list = listMessageBean.getData();
                            if (list != null) {
                                iv.onSuccess(list);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed(new Exception(t));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}