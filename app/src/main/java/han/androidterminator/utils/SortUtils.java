package han.androidterminator.utils;

import java.util.List;

import han.androidterminator.obj.WordObj;

/**
 * Created by hs on 2017/4/19.
 */

public class SortUtils {

    // 根据最后添加时间排序
    public static void jsonSort(List<WordObj> objList) {
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
