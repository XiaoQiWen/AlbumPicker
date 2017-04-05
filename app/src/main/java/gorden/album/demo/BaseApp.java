package gorden.album.demo;

import android.app.Application;

import gorden.album.demo.util.CrashHandler;

/**
 * Created by Gorden on 2017/4/5.
 */

public class BaseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this,true);
    }
}
