package han.androidterminator.obj;


/**
 * Created by hs on 2017/4/11.
 */

public class WordObj {

    String name;
    String soundmark;

    public void setName(String name) {
        this.name = name;
    }

    public void setSoundmark(String soundmark) {
        this.soundmark = soundmark;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getName() {
        return name;
    }

    public String getSoundmark() {
        return soundmark;
    }

    public String getTranslation() {
        return translation;
    }

    String translation;
}
