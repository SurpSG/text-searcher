package com.gnatiuk.searcher.utils;

import java.io.File;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class AppOptions {

    public static final String APP_ON_DUTY_DIR_NAME = ".text-searcher";
    public static final String APP_ON_DUTY_DIR_PATH = createOnDutyDirIfNotExists();

    public static final String FILTERS_SAVE_DIR_NAME = "filters";
    public static final String FILTERS_SAVE_DIR_PATH = createNestedDirIfNotExists(FILTERS_SAVE_DIR_NAME);

    public static final String ON_BOOT_DIR_NAME = "on-boot";
    public static final String ON_BOOT_DIR_PATH = createNestedDirIfNotExists(ON_BOOT_DIR_NAME);

    public static final String FILTER_FILE_EXTENSION = ".json";
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
     * @return path to filters save directory
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

}
