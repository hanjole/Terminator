package han.androidterminator;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import han.androidterminator.obj.CollectionObj;
import han.androidterminator.obj.WordObj;
import han.androidterminator.utils.SortUtils;

/**
 * Created by hs on 2017/4/1.
 */

public class FragmentWebUrl extends Fragment {
    JSONArray array;
    List<CollectionObj> list = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences preferences;
    RecyclerAdapterH5 adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_h5url, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.url_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterH5(getActivity(), null);
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setOnItemLongClickListener(onItemLongClickListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        refreshRecycler();
        return v;
    }

    BaseRecyclerAdapter.OnItemClickListener onItemClickListener = new BaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }
    };

    BaseRecyclerAdapter.OnItemLongClickListener onItemLongClickListener = new BaseRecyclerAdapter.OnItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int position) {

        }
    };


    public void refreshRecycler() {
        preferences = getActivity().getSharedPreferences(Constant.SO_WORD, 1);
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
                        CollectionObj photo = new CollectionObj();
                        photo.url = json.optString("url");
                        photo.name = json.optString("name");
                        photo.date = json.optString("date");
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
        adapter.setList(list);
        for (int i = 0; i < list.size(); i++) {
            Log.e("list",
                            (list.get(i).name==null?"":list.get(i).name)
                            +"---"
                            +(list.get(i).date==null?"":list.get(i).date)
                            +"---"
                            +(list.get(i).url==null?"":list.get(i).url)
            );
        }

    }
    // 根据最后添加时间排序
    private void sort(List<CollectionObj> objList) {
        for (int i = 0; i < objList.size(); i++) {
            for (int j = i + 1; j < objList.size(); j++) {
                if (objList.get(i).date==null){

                    continue;
                }
                int intTemp = objList.get(i).date.compareToIgnoreCase(objList.get(j).date);
                CollectionObj strTemp;
                if (intTemp < 0) {
                    strTemp = objList.get(j);
                    objList.set(j, objList.get(i));
                    objList.set(i, strTemp);
                }
            }
        }
    }



}
