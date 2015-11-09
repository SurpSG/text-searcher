package utils;

import java.io.File;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class AppOptions {

    public static final String APP_ON_DUTY_DIR_NAME = ".text-searcher";
    public static final String APP_ON_DUTY_DIR_PATH = createOnDutyDirIfNotExists();

    public static final String FILTER_FILE_EXTENSION = ".json";
    public static final String FILTERS_SAVE_DIR_NAME = "com.gnatiuk.filters";
    public static final String FILTERS_SAVE_DIR_PATH = createNestedDirIfNotExists(FILTERS_SAVE_DIR_NAME);

    public static final String ON_BOOT_DIR_NAME = "on-boot";
    public static final String ON_BOOT_DIR_PATH = createNestedDirIfNotExists(ON_BOOT_DIR_NAME);

    public static final String KEY_ASSIST_DIR_NAME = "key-assist";
    public static final String KEY_ASSIST_DIR_PATH = createNestedDirIfNotExists(KEY_ASSIST_DIR_NAME);
    public static final String KEY_ASSIST_FILE_NAME = "keywords-lib";
    public static final String KEY_ASSIST_FILE_PATH = KEY_ASSIST_DIR_PATH + File.separator + KEY_ASSIST_FILE_NAME;

    public static String RECENT_PATHS_FILE_NAME = "on_boot_paths";
    public static final String RECENT_PATHS_FILE_PATH = buildOnBootFilePath(ON_BOOT_DIR_PATH, RECENT_PATHS_FILE_NAME);


    /**
     *
     * @return path to on-duty directory
     */
    private static String createOnDutyDirIfNotExists(){
        String onDutyDirPath = System.getProperty("user.home") + File.separator + APP_ON_DUTY_DIR_NAME;
        createDirIfNotExists(new File(onDutyDirPath));
        return onDutyDirPath;
    }

    /**
     *
     * @return path to com.gnatiuk.filters save directory
     */
    private static String createNestedDirIfNotExists(String dirName){
        String filtersSaveDir = APP_ON_DUTY_DIR_PATH + File.separator + dirName;
        createDirIfNotExists(new File(filtersSaveDir));
        return filtersSaveDir;
    }

    private static void createDirIfNotExists(File file){
        if(!file.exists()){
            file.mkdir();
        }
    }

    private static String buildOnBootFilePath(String dirPath, String fileName){
        return dirPath + File.separator + fileName;
    }

}
