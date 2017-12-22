package rxjava.bwei.com.renzhijun20171211.view;

/**
 * Created by Zhijun on 2017/12/11.
 */

public interface IView {
    //返回成功或失败的数据
    void onSuccess(Object o);
    void onFailed(Exception e);
}