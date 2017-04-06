package han.androidterminator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import han.androidterminator.view.Info;
import han.androidterminator.view.PhotoView;

/**
 * Created by hs on 2017/4/6.
 */

public class ActivityPhoto extends AppCompatActivity {
    PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.photo_main);
        Intent intent = getIntent();
        String path =  intent.getStringExtra("path");
        String name =   intent.getStringExtra("name");
        if(path.startsWith("file://")){
            path = path.replace("file://","");
        }
        photoView =  (PhotoView) findViewById(R.id.photo_image);
        photoView.enable();
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Bitmap bm = BitmapFactory.decodeFile(path);
        photoView.setImageBitmap(bm);

    }
}
