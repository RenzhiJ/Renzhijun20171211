package rxjava.bwei.com.renzhijun20171211.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rxjava.bwei.com.renzhijun20171211.bean.MessageBean;
import rxjava.bwei.com.renzhijun20171211.bean.News;

/**
 * Created by Zhijun on 2017/12/11.
 */

public interface ApiService {
    @GET("product/addCart")
    Flowable<MessageBean<List<News>>> getNews(@QueryMap Map<String, String> map);
}