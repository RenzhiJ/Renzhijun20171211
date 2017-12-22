package rxjava.bwei.com.renzhijun20171211.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Zhijun on 2017/12/11.
 */

public class RetrofitUtils {
    private static volatile RetrofitUtils instance;

    private ApiService apiService;

    private RetrofitUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://120.27.23.105/")
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (null == instance) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}