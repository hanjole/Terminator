package han.androidterminator.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.io.File;

import han.androidterminator.MaApplication;

/**
 * Created by hs on 2017/4/17.
 */

public class AudioService extends Service {

    private MediaPlayer mp;
    private String path;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        path =  intent.getStringExtra("path");
        mp  = MediaPlayer.create(MaApplication.getContext(), Uri.fromFile(new File(path)));

        mp.start();
        // 音乐播放完毕的事件处理
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                // 不循环播放
                try {
                    // mp.start();
                    System.out.println("stopped");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        // 播放音乐时发生错误的事件处理
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 释放资源
                try {
                    mp.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }
}
