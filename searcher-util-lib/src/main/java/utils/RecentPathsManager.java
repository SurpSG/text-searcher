package utils;

/**
 * Created by sgnatiuk on 11/9/15.
 */
public class RecentPathsManager extends LibManager {

    private RecentPathsManager(String libFilePath) {
        super(libFilePath);
    }

    @Override
    protected int getMaxWordsToSave() {
        return 10;
    }

    static class InstanceContainer{
        public static final LibManager instance = new RecentPathsManager(AppOptions.RECENT_PATHS_FILE_PATH);
    }

    public static LibManager getInstance(){
        return InstanceContainer.instance;
    }
}
