package han.androidterminator.obj;


/**
 * Created by hs on 2017/4/11.
 */

public class WordObj {

    String name;
    String soundmark;
    String translation;
    String voice;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

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


}
