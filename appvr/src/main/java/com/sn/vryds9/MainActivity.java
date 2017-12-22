package com.sn.vryds9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

/*      0.0 在项目里新建一个资产目录assets,把图片放入资产目录下,
        1.0 在清单文件下Application节点中加入android:largeHeap="true"的属下节点.
        2.0 导入VR需要依赖的library库,以导model的方式去导入:Common,Commonwidge,Panowidget
        3.0 在Module的build.gradle文件里dependencies,添加:compile 'com.google.protobuf.nano:protobuf-javanano:3.0.0-alpha-7'
        4.0 完成项目XML布局,VrPanoramaView
        5.0 由于VR资源数据量大,获取需要时间,故把加载图片放到子线程中进行,主线程来显示图片,可以使用一个异步线程AsyncTask或EventBus技术完成
        6.0 因为VR很占用内存,所以当界面进入onPause状态,暂停VR视图显示,进入onResume状态,继续VR视图显示,进入onDestroy状态,杀死VR,关闭异步任务
        7.0 设置对VR运行状态的监听,如果VR运行出现错误,可以及时的处理.
        8.0 播放VR效果,只需执行异步任务即可.
*/
public class MainActivity extends AppCompatActivity {

    private VrPanoramaView mVrPanoramaView;
    private ImagerLoaderTask mImagerLoaderTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //A.对VR控件初始化
        mVrPanoramaView = (VrPanoramaView) findViewById(R.id.vp_view);

        //隐藏掉VR效果左下角的信息按钮显示
        mVrPanoramaView.setInfoButtonEnabled(false);
        //隐藏掉VR效果右下角全屏显示按钮
        mVrPanoramaView.setFullscreenButtonEnabled(false);
        //切换VR的模式   参数: VrWidgetView.DisplayMode.FULLSCREEN_STEREO设备模式(手机横着放试试)   VrWidgetView.DisplayMode.FULLSCREEN_MONO手机模式
        mVrPanoramaView.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);

        //D.设置对VR运行状态的监听,如果VR运行出现错误,可以及时处了.
        mVrPanoramaView.setEventListener(new MyVREventListener());

        //B.使用自定义的AsyncTask,播放VR效果
        mImagerLoaderTask = new ImagerLoaderTask();
        mImagerLoaderTask.execute();

    }

    /**
     * B.自定义一个类继承AsyncTask,只使用我们需要的方法.
     * 由于VR资源数据量大,获取需要时间,故把加载图片放到子线程中进行,主线程来显示图片,故可以使用一个异步线程AsyncTask或EventBus来处理.
     */
    private class ImagerLoaderTask extends AsyncTask<Void , Void ,Bitmap>{
        //B.该方法在子线程运行,从本地文件中把资源加载到内存中.
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                //从资产目录拿到资源,返回结果是字节流
                InputStream inputStream = getAssets().open("andes.jpg");
                //把字节流转换成Bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //该方法在主线程运行
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //创建bVrPanoramaView.Options,去决定显示VR是普通效果,还是立体效果
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            //TYPE_STEREO_OVER_UNDER立体效果:图片的上半部分放在左眼显示,下半部分放在右眼显示     TYPE_MONO:普通效果
            options.inputType=VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            //使用VR控件对象,显示效果  参数:1.Bitmap对象      2.VrPanoramaView.Options对象,决定显示的效果
            mVrPanoramaView.loadImageFromBitmap(bitmap, options);
            super.onPostExecute(bitmap);
        }
    }

    //C.因为VR很占用内存,所以当界面进入onPause状态,暂停VR视图显示,进入onResume状态,继续VR视图显示,进入onDestroy,杀死VR,关闭异步任务

    //当失去焦点时,回调
    @Override
    protected void onPause() {
        //暂停渲染和显示
        mVrPanoramaView.pauseRendering();
        super.onPause();
    }

    //当重新获取到焦点时,回调
    @Override
    protected void onResume() {
        super.onResume();
        //继续渲染和显示
        mVrPanoramaView.resumeRendering();
    }

    //当Activity销毁时,回调
    @Override
    protected void onDestroy() {
        //关闭渲染视图
        mVrPanoramaView.shutdown();
        if(mImagerLoaderTask != null){
            //在退出activity时,如果异步任务没有取消,就取消
            if(!mImagerLoaderTask.isCancelled()){
                mImagerLoaderTask.cancel(true);
            }
        }
        super.onDestroy();
    }

    //VR运行状态监听类,自定义一个类继承VrPanoramaEventListener,复写里面的两个方法
    private class MyVREventListener extends VrPanoramaEventListener{
        //当VR视图加载成功的时候回调
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Toast.makeText(MainActivity.this, "加载成功,么么哒", Toast.LENGTH_SHORT).show();

        }

        //当VR视图加载失败的时候回调
        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            Toast.makeText(MainActivity.this, "加载失败,不好意思,因为易大师太帅影响", Toast.LENGTH_SHORT).show();
        }
    }


}
