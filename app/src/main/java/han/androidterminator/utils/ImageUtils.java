package han.androidterminator.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by hs on 2017/4/5.
 */

public class ImageUtils {

    public static  void setImage(ImageView view ,String url) {
        Bitmap bitmap = getLoacalBitmap(url); //从本地取图片(在cdcard中获取)  //
        view.setImageBitmap(bitmap);
    }

    public static  Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
