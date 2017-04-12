package han.androidterminator;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import han.androidterminator.utils.ImageLoaderUtils;
import han.androidterminator.utils.ImageUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentPhoto fragmentPhoto;
    FragmentWebUrl fragmentH5Url;
    FragmentWord fragmentWord;
    FloatingActionButton fab;
    PopupWindow photoPopup;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (!picturePath.startsWith("file://")) {
                picturePath = "file://" + picturePath;
            }
            Log.i("picturePath",picturePath);
            showPopupPhoto(picturePath);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        JsonUtils jsonUtils = new JsonUtils(MainActivity.this,Constant.SO_WORD);
//        SharedPreferences.Editor edits =  jsonUtils.preferences.edit();
//        if(jsonUtils.getArrayJson()!=null){
//            Log.e("SO_WORD", jsonUtils.getArrayJson().toString());
//        }
//        edits.clear();
//        edits.apply();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 当前显示页面
                switch (Constant.showF){
                    case 1:
                        chosePicture();
                        break;
                    case 2:
                        showPopupWord();
                        break;
                    case 3:

                        break;
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (fragmentPhoto == null) {
            fragmentPhoto = new FragmentPhoto();
        }
        transaction.add(R.id.f_photo, fragmentPhoto, "fragmentPhoto");
        transaction.show(fragmentPhoto);
        transaction.commit();

    }

    private void showPopupWord() {



    }


    private void showPopupPhoto(final String path) {

        final View inflaterView = this.getLayoutInflater().inflate(R.layout.popup_photo, null);
        ImageView image = (ImageView) inflaterView.findViewById(R.id.popup_image);
        ImageLoaderUtils.displayImage(path,image,false);

        final EditText edit = (EditText) inflaterView.findViewById(R.id.popup_describe_et);
        Button b1 = (Button) inflaterView.findViewById(R.id.popup_b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPopup.dismiss();
            }
        });
        Button b2 = (Button) inflaterView.findViewById(R.id.popup_b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtils jsonUtils = new JsonUtils(MainActivity.this, Constant.SO_PHOTO);
                jsonUtils.photoWriteJson(path,edit.getText().toString());
                Log.e("SO_PHOTO", jsonUtils.getArrayJson().toString());

                if(fragmentPhoto!=null){
                    fragmentPhoto.refreshRecycler();
                }

                photoPopup.dismiss();
            }
        });
        photoPopup = new PopupWindow(inflaterView);
        photoPopup.setOutsideTouchable(false);
        photoPopup.setFocusable(true);
        photoPopup.setBackgroundDrawable(new BitmapDrawable());
        photoPopup.setWidth(700);
        photoPopup.setHeight(1100);
        photoPopup.showAtLocation(fab, Gravity.CENTER, 0, 0);




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action


        } else if (id == R.id.nav_photo) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragmentPhoto == null) {
                fragmentPhoto = new FragmentPhoto();
                transaction.add(R.id.f_photo, fragmentPhoto, "fragmentPhoto");
            }

            hideFragment(transaction);
            transaction.show(fragmentPhoto);
            transaction.commit();
            Constant.showF = 1;

        } else if (id == R.id.nav_word) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragmentWord == null) {
                fragmentWord = new FragmentWord();
                transaction.add(R.id.f_photo, fragmentWord, "fragmentWord");
            }

            hideFragment(transaction);
            transaction.show(fragmentWord);
            transaction.commit();
            Constant.showF = 2;
        } else if (id == R.id.nav_weburl) {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragmentH5Url == null) {
                fragmentH5Url = new FragmentWebUrl();
                transaction.add(R.id.f_photo, fragmentH5Url, "fragmentH5Url");
            }

            hideFragment(transaction);
            transaction.show(fragmentH5Url);
            transaction.commit();
            Constant.showF = 3;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentPhoto != null) {
            transaction.hide(fragmentPhoto);
        }
        if (fragmentH5Url != null) {
            transaction.hide(fragmentH5Url);
        }
        if (fragmentWord != null) {
            transaction.hide(fragmentWord);
        }
    }

    private void chosePicture() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }
}
