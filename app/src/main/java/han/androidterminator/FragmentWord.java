package han.androidterminator;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import han.androidterminator.obj.PhotoObj;
import han.androidterminator.obj.WordObj;
import han.androidterminator.service.AudioService;
import han.androidterminator.utils.VoiceUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by hs on 2017/4/1.
 */

public class FragmentWord extends Fragment {

    JSONArray array;
    List<WordObj> list = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences preferences;
    RecyclerAdapterWord adapter;
//    private MediaPlayer mp;
    private String query;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_word, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_Word);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterWord(getActivity(), null);
        adapter.setOnItemClickListener(onItemClickListener);

        adapter.setOnItemLongClickListener(onItemLongClickListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
//
//        JsonUtils jsonUtils = new JsonUtils(getActivity(),Constant.SO_WORD);
//        jsonUtils.wordWriteJson("aa","saaaaa","aaaaaaa");
//        jsonUtils.wordWriteJson("bbbbb","sabbbbaa","abbbbbaaaa");
//        jsonUtils.wordWriteJson("cccc","saaccccca","dcccccc");
//        jsonUtils.wordWriteJson("ddddd","dddddd","ddddddddddddddd");
        refreshRecycler();
        return v;
    }
    BaseRecyclerAdapter.OnItemLongClickListener onItemLongClickListener = new BaseRecyclerAdapter.OnItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int position) {

            if( adapter.deleteShow==true){
                return;
            }
            Log.e("OnItemLongClickListener",position +"onItemClickListener");
            adapter.deleteShow = true;
            adapter.notifyDataSetChanged();

        }
    };

    BaseRecyclerAdapter.OnItemClickListener onItemClickListener = new BaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            query = adapter.list.get(position).getName();
            String[] info =   VoiceUtils.getOutputMediaFile(query);

            if(VoiceUtils.isMediaFile(query)){
                String path = info[0]+info[1];
                //如果本地存在
                Log.e("isMediaFile",info[0]+info[1]);
                Intent intent = new Intent(getActivity(),AudioService.class);
                intent.putExtra("path",path);
                getActivity().startService(intent);
            }else {

                OkHttpUtils.get()
                        .url("http://dict.youdao.com/dictvoice?audio=" + query)
                        .build()
                        .execute(new FileCallBack(info[0],info[1]) {
                            @Override
                            public File saveFile(Response response, int id) throws IOException {
                                return super.saveFile(response, id);
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(File response, int id) {
                                Intent intent = new Intent(getActivity(),AudioService.class);
                                intent.putExtra("path",response.toString());
                                getActivity().startService(intent);
                            }
                        });
            }

//            Uri location = Uri.parse("http://dict.youdao.com/dictvoice?audio=" + adapter.list.get(position));
            System.out.println("http://dict.youdao.com/dictvoice?audio=" + adapter.list.get(position).getName());



//            mp.start();





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
                        WordObj photo = new WordObj();
                        photo.setName(json.optString("name"));
                        photo.setSoundmark(json.optString("soundmark"));
                        photo.setTranslation(json.optString("translation"));
                        photo.setDate(json.optString("date"));
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
                    (list.get(i).getName()==null?"":list.get(i).getName().toString())
                    +"---"
                    +(list.get(i).getSoundmark()==null?"":list.get(i).getSoundmark().toString())
                    +"---"
                    +(list.get(i).getTranslation()==null?"":list.get(i).getTranslation().toString())
                    +"---"
                    +(list.get(i).getDate()==null?"":list.get(i).getDate().toString())
                    +"---"
                    +(list.get(i).getVoice()==null?"":list.get(i).getVoice().toString())
            );
        }

    }


    // 根据最后添加时间排序
    private void sort(List<WordObj> objList) {
        for (int i = 0; i < objList.size(); i++) {
            for (int j = i + 1; j < objList.size(); j++) {
                if (objList.get(i).getDate()==null){

                    continue;
                }
                int intTemp = objList.get(i).getDate().compareToIgnoreCase(objList.get(j).getDate());
                WordObj strTemp;
                if (intTemp < 0) {
                    strTemp = objList.get(j);
                    objList.set(j, objList.get(i));
                    objList.set(i, strTemp);
                }
            }
        }
    }
}


/**
 API key：1990392990
 keyfrom：myTestVoiceV
 创建时间：2017-04-11
 网站名称：myTestVoiceV
 网站地址：https://msdn.microsoft.com/en-us/library/ff512419.aspx

 使用API
 数据接口
 http://fanyi.youdao.com/openapi.do?keyfrom=myTestVoiceV&key=1990392990&type=data&doctype=<doctype>&version=1.1&q=要翻译的文本
 版本：1.1，请求方式：get，编码方式：utf-8
 主要功能：中英互译，同时获得有道翻译结果和有道词典结果（可能没有）
 参数说明：
 　type - 返回结果的类型，固定为data
 　doctype - 返回结果的数据格式，xml或json或jsonp
 　version - 版本，当前最新版本为1.1
 　q - 要翻译的文本，必须是UTF-8编码，字符长度不能超过200个字符，需要进行urlencode编码
 　only - 可选参数，dict表示只获取词典数据，translate表示只获取翻译数据，默认为都获取
 　注： 词典结果只支持中英互译，翻译结果支持英日韩法俄西到中文的翻译以及中文到英语的翻译
 errorCode：
 　0 - 正常
 　20 - 要翻译的文本过长
 　30 - 无法进行有效的翻译
 　40 - 不支持的语言类型
 　50 - 无效的key
 　60 - 无词典结果，仅在获取词典结果生效
 xml数据格式举例
 http://fanyi.youdao.com/openapi.do?keyfrom=myTestVoiceV&key=1990392990&type=data&doctype=xml&version=1.1&q=这里是有道翻译API
 <youdao-fanyi>
 <errorCode>0</errorCode>
 <query><![CDATA[这里是有道翻译API]]></query>
 <translation>
 <paragraph><![CDATA[Here is the youdao translation API]]></paragraph>
 </translation>
 </youdao-fanyi>
 json数据格式举例
 http://fanyi.youdao.com/openapi.do?keyfrom=myTestVoiceV&key=1990392990&type=data&doctype=json&version=1.1&q=good
 {
 "errorCode":0
 "query":"good",
 "translation":["好"], // 有道翻译
 "basic":{ // 有道词典-基本词典
 "phonetic":"gʊd"
 "uk-phonetic":"gʊd" //英式发音
 "us-phonetic":"ɡʊd" //美式发音
 "explains":[
 "好处",
 "好的"
 "好"
 ]
 },
 "web":[ // 有道词典-网络释义
 {
 "key":"good",
 "value":["良好","善","美好"]
 },
 {...}
 ]
 }
 jsonp数据格式举例
 http://fanyi.youdao.com/openapi.do?keyfrom=myTestVoiceV&key=1990392990&type=data&doctype=jsonp&callback=show&version=1.1&q=API
 show({
 "errorCode":0
 "query":"API",
 "translation":["API"], // 有道翻译
 "basic":{ // 有道词典-基本词典
 "explains":[
 "abbr. 应用程序界面（Application Program Interface）；..."
 ]
 },
 "web":[ // 有道词典-网络释义
 {
 "key":"API",
 "value":["应用程序接口(Application Programming Interface)","应用编程接口","应用程序编程接口","美国石油协会"]
 },
 {...}
 ]
 });



 * */