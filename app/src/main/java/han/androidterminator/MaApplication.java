package han.androidterminator;

import android.app.Application;

import han.androidterminator.utils.ImageLoaderUtils;

/**
 * Created by hs on 2017/4/5.
 */

public class MaApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtils.init(this);
        getContext();
    }


    public MaApplication getContext() {
        return this;
    }
}
