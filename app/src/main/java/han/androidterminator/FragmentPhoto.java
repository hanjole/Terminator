package han.androidterminator;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import han.androidterminator.obj.PhotoObj;

/**
 * Created by hs on 2017/4/1.
 */

public class FragmentPhoto extends Fragment {
    JSONArray array;
    List<PhotoObj> list = new ArrayList<>();
    RecyclerView recyclerView;

    SharedPreferences preferences;
//    RecyclerAdapterPhoto adapter;
    RecyclerAdapterPhotoNew adapterNew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_phpto);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        adapter = new RecyclerAdapterPhoto(getActivity(), list);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(onItemClickListener);
        adapterNew = new RecyclerAdapterPhotoNew(getActivity(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterNew);
        adapterNew.setOnItemClickListener(onItemClickListener);
        refreshRecycler();

        return v;
    }

    BaseRecyclerAdapter.OnItemClickListener onItemClickListener = new BaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {


            Intent intent = new Intent();
            intent.setClass(getActivity(), ActivityPhoto.class);
//            intent.putExtra("path",adapter.list.get(position).getImg());
//            intent.putExtra("name",adapter.list.get(position).getTitle());
            intent.putExtra("path", adapterNew.list.get(position).getImg());
            intent.putExtra("name", adapterNew.list.get(position).getTitle());
            startActivity(intent);


        }
    };


    public void refreshRecycler() {
        preferences = getActivity().getSharedPreferences(Constant.SO_PHOTO, 1);
//      SharedPreferences.Editor a =  preferences.edit();
//        a.clear();
//        a.apply();
        try {
            if (preferences.getString("jsonData", null) == null) {
                array = new JSONArray();
            } else {
                array = new JSONArray(preferences.getString("jsonData", null));
            }

            if (array != null) {
                list.clear();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject json = (JSONObject) array.get(i);
                        PhotoObj photo = new PhotoObj();
                        photo.setTitle(json.optString("name"));
                        photo.setTime(json.optString("date"));
                        photo.setImg(json.optString("path"));
                        list.add(photo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                array = new JSONArray();
                Log.e("SO_PHOTO", "null");
            }

        } catch (Exception e) {

        }

        sort(list);
        adapterNew.setList(list);
        for (int i = 0; i < list.size(); i++) {
            Log.e("list", list.get(i).getTitle().toString());
        }

    }

    // 根据最后添加时间排序
    private void sort(List<PhotoObj> objList) {
        for (int i = 0; i < objList.size(); i++) {
            for (int j = i + 1; j < objList.size(); j++) {
                int intTemp = objList.get(i).getTime().compareToIgnoreCase(objList.get(j).getTime());
                PhotoObj strTemp;
                if (intTemp < 0) {
                    strTemp = objList.get(j);
                    objList.set(j, objList.get(i));
                    objList.set(i, strTemp);
                }
            }
        }
    }


}

