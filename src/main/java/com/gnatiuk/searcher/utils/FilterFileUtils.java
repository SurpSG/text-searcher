package com.gnatiuk.searcher.utils;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.json.FilterJsonProcessor;

import java.io.File;
import java.io.IOException;

/**
 * Created by sgnatiuk on 9/30/15.
 */
public class FilterFileUtils {

    public static IFilter loadFilterByName(String filterName){
        try {
            String filterFileName = getFilterFileByName(filterName);
            if(filterFileName == null){
                return IFilter.NONE_FILTER;
            }
            return FilterJsonProcessor.deserializeFilterFromFile(buildFilterFilePathByName(filterFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IFilter.NONE_FILTER;
    }

    public static void saveFilterWithName(String filterName, IFilter filter){
        try {
            System.out.println("saveFilterWithName");
            System.out.println("filter="+filter);
            System.out.println("filter.hash="+filter.filterHash());
            String hashedName = String.format("[%s]%s%s",filter.filterHash(), filterName, AppOptions.FILTER_FILE_EXTENSION);
            FilterJsonProcessor.serializeFilterToFile(buildFilterFilePathByName(hashedName), filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildFilterFilePathByName(String filterFileName){
        return AppOptions.FILTERS_SAVE_DIR_PATH + File.separator + filterFileName;
    }

    public static boolean isExists(String filterName){
        return getFilterFileByName(filterName) != null;
    }

    public static boolean isExists(IFilter filter){
        return isExists(filter.filterHash());
    }

    public static String getFilterFileByName(String filterName){
        String[] filtersFilesNames = new File(AppOptions.FILTERS_SAVE_DIR_PATH).list();

        for (String filtersFilesName : filtersFilesNames) {
            if(!filtersFilesName.endsWith(AppOptions.FILTER_FILE_EXTENSION)){
                System.err.println(String.format("'%s' is outsider file.", filtersFilesName));
                continue;
            }
            String extractedFilterName = extractFilterName(filtersFilesName);
            String extractedFilterHashName = extractHashedName(filtersFilesName);
            if(extractedFilterName != null && extractedFilterName.equals(filterName)
                    || extractedFilterHashName != null && extractedFilterHashName.equals(filterName)){
                return filtersFilesName;
            }
        }
        return null;
    }

    public static String extractHashedName(String filterFileName){
        try{
            return filterFileName.substring(filterFileName.indexOf("[")+1, filterFileName.indexOf("]"));
        }catch (Exception e){
            System.err.println(filterFileName + " outside standart.");
            return null;
        }
    }

    public static String extractFilterName(String filterFileName){
        try{
            return filterFileName.substring(filterFileName.indexOf("]") + 1, filterFileName.indexOf(AppOptions.FILTER_FILE_EXTENSION));
        }catch (Exception e){
            return null;
        }
    }
}
