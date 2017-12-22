package rxjava.bwei.com.renzhijun20171211.http;

import android.os.Handler;

/**
 * Created by Zhijun on 2017/12/11.
 */

public class OkhttpUtils {
    private static final String TAG = "HttpUtils";
    private static volatile OkhttpUtils instance;

    private static Handler handler = new Handler();

    private OkhttpUtils() {

    }

    public static OkhttpUtils getInstance() {
        if (null == instance) {
            synchronized (OkhttpUtils.class) {
                if (instance == null) {
                    instance = new OkhttpUtils();
                }
            }
        }
        return instance;
    }
}
