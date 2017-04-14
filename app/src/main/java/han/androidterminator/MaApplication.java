package han.androidterminator;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import han.androidterminator.utils.ImageLoaderUtils;
import okhttp3.OkHttpClient;

/**
 * Created by hs on 2017/4/5.
 */

public class MaApplication extends Application {

   static Context mContext;
    public static Handler nonUiHandler;
    private static Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtils.init(this);
        mContext = this;
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .addInterceptor(new LoggerInterceptor("TAG"))
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        mHandler = new Handler();
        new Thread() {
            public void run() {
                Looper.prepare();
                nonUiHandler = new Handler();
                Looper.loop();
            }
        }.start();

    }

    public static void post(Runnable runnable) {
        mHandler.post(runnable);
    }


    public static void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }


    public static Context getContext() {
        return mContext;
    }
}
