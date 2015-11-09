package utils;

/**
 * Created by sgnatiuk on 11/9/15.
 */
public class KeywordsLibManager extends  LibManager{
    private KeywordsLibManager() {
        super(AppOptions.KEY_ASSIST_FILE_PATH);
    }

    @Override
    protected int getMaxWordsToSave() {
        return 100;
    }

    static class InstanceContainer{
        public static final LibManager instance = new KeywordsLibManager();
    }

    public static LibManager getInstance(){
        return InstanceContainer.instance;
    }
}
