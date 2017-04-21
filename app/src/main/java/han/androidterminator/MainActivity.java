package han.androidterminator;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import han.androidterminator.utils.ImageLoaderUtils;
import han.androidterminator.utils.ImageUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentPhoto fragmentPhoto;
    FragmentWebUrl fragmentH5Url;
    FragmentWord fragmentWord;
    FloatingActionButton fab;
    PopupWindow photoPopup, wordPhoto,collection;
    final int PAGE_WORD = 2;
    final int PAGE_URL = 3;
    final int PAGE_PHOTO = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (!picturePath.startsWith("file://")) {
                picturePath = "file://" + picturePath;
            }
            Log.i("picturePath", picturePath);
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
                switch (Constant.showF) {
                    case PAGE_PHOTO:
                        chosePicture();
                        break;
                    case PAGE_WORD:
                        showPopupWord();
                        break;
                    case PAGE_URL:
                        showPopupCollection();
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

    private void showPopupCollection() {
        final View inflaterView = this.getLayoutInflater().inflate(R.layout.popup_collection, null);

        final EditText name = (EditText) inflaterView.findViewById(R.id.edit_collection_name);
        final EditText url = (EditText) inflaterView.findViewById(R.id.edit_collection_url);
        Button add = (Button) inflaterView.findViewById(R.id.b_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtils jsonUtils = new JsonUtils(Constant.SO_URL);
                jsonUtils.collectionWriteJson(name.getText()+"",url.getText()+"");
                collection.dismiss();
                fragmentH5Url.refreshRecycler();

            }
        });

        collection = new PopupWindow(inflaterView);
        collection.setOutsideTouchable(false);
        collection.setFocusable(true);
        collection.setWidth(700);
        collection.setHeight(1100);
        collection.showAtLocation(fab, Gravity.CENTER, 0, 0);


    }

    private void showPopupWord() {
        final View inflaterView = this.getLayoutInflater().inflate(R.layout.popup_word, null);

        final EditText wordName = (EditText) inflaterView.findViewById(R.id.edit_word_name);
        final TextView soundmark = (TextView) inflaterView.findViewById(R.id.word_soundmark_tv);
        final EditText wordTranslation = (EditText) inflaterView.findViewById(R.id.edit_word_translation);
        Button importTranslation = (Button) inflaterView.findViewById(R.id.b_import);
        Button add = (Button) inflaterView.findViewById(R.id.b_add);
        importTranslation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!wordName.getText().toString().isEmpty()) {

                    if (!match("^[A-Za-z]+$", wordName.getText().toString())) {
                        Toast.makeText(MainActivity.this, "只能输入英文", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    getTranslationAndSoundmark(wordName.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "请输入需要翻译的名称", Toast.LENGTH_SHORT).show();
                }

//                wordPhoto.dismiss();
            }

            private void getTranslationAndSoundmark(String name) {


                OkHttpUtils.get()
                        .url("http://fanyi.youdao.com/openapi.do?keyfrom=myTestVoiceV&key=1990392990&type=data&doctype=json&version=1.1&q=" + name)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {


                                try {
                                    Log.i("onResponse", response);

                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONObject basicObj = jsonObj.optJSONObject("basic");
                                    if (basicObj == null) {
                                        MaApplication.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                soundmark.setText("");
                                                wordTranslation.setText("");
                                            }
                                        });
                                        return;

                                    }
                                    String explains = "";
                                    String phonetic = "";
                                    phonetic = basicObj.optString("uk-phonetic");
                                    JSONArray explainsArray = basicObj.getJSONArray("explains");
                                    if (explainsArray.get(0) != null) {
                                        explains = explainsArray.get(0).toString();
                                    }


                                    final String finalExplains = explains;
                                    final String finalPhonetic = phonetic;
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            soundmark.setText(finalPhonetic);
                                            wordTranslation.setText(finalExplains);
                                        }
                                    });

                                } catch (JSONException e) {
                                    System.out.println("youdaoJson解析失败");
                                    e.printStackTrace();
                                }

                            }
                        });

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtils jsonUtils = new JsonUtils(Constant.SO_WORD);
                jsonUtils.wordWriteJson(
                        wordName.getText() == null ? "" : wordName.getText().toString(),
                        soundmark.getText() == null ? "" : soundmark.getText().toString(),
                        wordTranslation.getText() == null ? "" : wordTranslation.getText().toString()
                );

//                Log.e("SO_WORD", jsonUtils.getArrayJson().toString());

                if (fragmentWord != null) {
                    fragmentWord.refreshRecycler();
                }


                wordPhoto.dismiss();
            }
        });
        wordPhoto = new PopupWindow(inflaterView);
        wordPhoto.setOutsideTouchable(false);
        wordPhoto.setFocusable(true);
        wordPhoto.setWidth(700);
        wordPhoto.setHeight(1100);
        wordPhoto.showAtLocation(fab, Gravity.CENTER, 0, 0);


    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    View.OnClickListener clickListenerImport = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            wordPhoto.dismiss();
        }
    };


    private void showPopupPhoto(final String path) {

        final View inflaterView = this.getLayoutInflater().inflate(R.layout.popup_photo, null);
        ImageView image = (ImageView) inflaterView.findViewById(R.id.popup_image);
        ImageLoaderUtils.displayImage(path, image, false);

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
                jsonUtils.photoWriteJson(path, edit.getText().toString());
                Log.e("SO_PHOTO", jsonUtils.getArrayJson().toString());

                if (fragmentPhoto != null) {
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

            switch (Constant.showF) {
                case PAGE_PHOTO:
                    super.onBackPressed();
                    break;
                case PAGE_WORD:
                    if (fragmentWord.adapter.deleteShow) {
                        fragmentWord.adapter.deleteShow = false;
                        fragmentWord.adapter.notifyDataSetChanged();
                    } else
                        super.onBackPressed();
                    break;

                case PAGE_URL:
                    super.onBackPressed();
                    break;
                default:

                    super.onBackPressed();


            }
//            super.onBackPressed();
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
            Constant.showF = PAGE_PHOTO;

        } else if (id == R.id.nav_word) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragmentWord == null) {
                fragmentWord = new FragmentWord();
                transaction.add(R.id.f_photo, fragmentWord, "fragmentWord");
            }

            hideFragment(transaction);
            transaction.show(fragmentWord);
            transaction.commit();
            Constant.showF = PAGE_WORD;
        } else if (id == R.id.nav_weburl) {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragmentH5Url == null) {
                fragmentH5Url = new FragmentWebUrl();
                transaction.add(R.id.f_photo, fragmentH5Url, "fragmentH5Url");
            }

            hideFragment(transaction);
            transaction.show(fragmentH5Url);
            transaction.commit();
            Constant.showF = PAGE_URL;
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
