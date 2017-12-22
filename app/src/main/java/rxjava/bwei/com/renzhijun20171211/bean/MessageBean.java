package rxjava.bwei.com.renzhijun20171211.bean;

/**
 * Created by Zhijun on 2017/12/11.
 */

public class MessageBean<T> {
    private String result;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
