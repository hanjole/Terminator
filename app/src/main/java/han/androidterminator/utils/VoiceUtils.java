package han.androidterminator.utils;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import han.androidterminator.MaApplication;

/**
 * Created by hs on 2017/4/17.
 */

public class VoiceUtils {

    public static boolean isMediaFile(String word) {
        File mediaStorageDir = new File(
                MaApplication.getContext().getExternalCacheDir(),
                "voiceCache");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }


        for (File f:  mediaStorageDir.listFiles()) {
            if(("Voice_" + word + ".mp3").equals(f.getName())){
                return true;
            }

        }

        return false;
    }


    public static String[] getOutputMediaFile(String word) {
        String[] voiceInfo ={"",""};
        File mediaStorageDir = new File(
                MaApplication.getContext().getExternalCacheDir(),
                "voiceCache");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")
//                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "Voice_" + word + ".mp3");

        voiceInfo[0] = mediaStorageDir.getPath();
        voiceInfo[1] =File.separator + "Voice_" + word + ".mp3";
//        Log.e("getOutputMediaFile",voiceInfo[0]+voiceInfo[1]);
        return voiceInfo;
    }
}
