package gorden.album.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import gorden.album.demo.util.CrashHandler;
import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;

/**
 * Created by Gorden on 2017/4/5.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this, true);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
