package helloworld.tumingzhi.com.mapdemo;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by DELL on 2018/3/1.
 */

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
