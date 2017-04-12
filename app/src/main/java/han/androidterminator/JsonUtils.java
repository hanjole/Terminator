package han.androidterminator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hs on 2017/4/5.
 */

public class JsonUtils {
    Activity mActivity;
    List<JSONArray> JSONArrayList;
    SharedPreferences preferences;
    JSONArray array;
    String type;

    public JsonUtils(Activity activity, String type) {
        mActivity = activity;
        this.type = type;
        preferences = mActivity.getSharedPreferences(type, 1);
    }

    public void photoReadJson(String path, String name) {
        try {
            JSONArray array = new JSONArray();
            if (JSONArrayList == null) {
                JSONArrayList = new ArrayList<>();
            }
            //  {    }
//            JSONArrayList.get(JSONArrayList.size()).
            JSONObject j = new JSONObject();
            j.put("name", name);
            j.put("path", path);
            j.put("date", new Date().getTime());
            array.put(j);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void photoWriteJson(String path, String name) {
        try {
            if (preferences.getString("jsonData", null) == null) {
                array = new JSONArray();
            } else {
                array = new JSONArray(preferences.getString("jsonData", null));
            }

            //  {    }
//            JSONArrayList.get(JSONArrayList.size()).
            JSONObject j = new JSONObject();
            j.put("name", name);
            j.put("path", path);

            SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            j.put("date", formatter.format(new Date().getTime()));

            array.put(array.length(), j);

            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("jsonData", array.toString());
            edit.apply();
//            Log.e("preferences", preferences.getAll().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void wordWriteJson(String name, String soundmark,String translation) {

        try {
            if (preferences.getString("jsonData", null) == null) {
                array = new JSONArray();
            } else {
                array = new JSONArray(preferences.getString("jsonData", null));
            }

            //  {    }
//            JSONArrayList.get(JSONArrayList.size()).
            JSONObject j = new JSONObject();
            j.put("name", name);
            j.put("soundmark", soundmark);
            j.put("translation", translation);
            SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            j.put("date", formatter.format(new Date().getTime()));

            array.put(array.length(), j);

            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("jsonData", array.toString());
            edit.apply();
//            Log.e("preferences", preferences.getAll().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public JSONArray getArrayJson() {
//        try {
//            if (preferences.getString("jsonData", null) == null) {
//                array = new JSONArray();
//            } else {
//                array = new JSONArray(preferences.getString("jsonData", null));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return array;
    }


}
