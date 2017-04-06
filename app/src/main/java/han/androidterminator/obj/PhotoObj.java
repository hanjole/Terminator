package han.androidterminator.obj;

import java.util.Date;
import java.util.List;

/**
 * Created by hs on 2017/4/1.
 */

public class PhotoObj {

    List<PhotoObj> list;
    private String img;
    private String title;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PhotoObj() {
    }

    public PhotoObj(String img, String title,String time) {
        this.img = img;
        this.title = title;
       this. time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PhotoObj> parsePhotoObj(String json) {

        return null;
    }


}
