package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.dialog.FilterSaveDialog;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchFilterComponent;
import com.gnatiuk.searcher.utils.FilterFileUtils;
import utils.AppOptions;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sgnatiuk on 9/29/15.
 */
public class OnBootManager {

    public static String ON_BOOT_FILTERS_FILE_NAME = "on_boot_filters";
    public static String ON_BOOT_PATHS_FILE_NAME = "on_boot_paths";

    public static List<ASearchFilterComponent> getOnLoadSearchComponents(){
        File onBootFiltersFile = new File(buildOnBootFilePath(ON_BOOT_FILTERS_FILE_NAME));

        if(!onBootFiltersFile.exists()){
           return Collections.emptyList();
        }

        try {
            List<ASearchFilterComponent> filters = new ArrayList<>();
            for (String filterToLoad : FileUtils.readFile(onBootFiltersFile)) {
                IFilter filter = FilterFileUtils.loadFilterByName(filterToLoad);
                if (filter == IFilter.NONE_FILTER){
                    unregisterOnBootFilter(filterToLoad);
                    continue;
                }
                List<ASearchFilterComponent> searchFilterComponents = ASearchFilterComponent.build(filter);
                for (ASearchFilterComponent filterComponent : searchFilterComponents) {
                    filterComponent.setAutoLoad(true);
                }
                filters.addAll(searchFilterComponents);
            }
            return filters;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public static void registerOnBootFilter(IFilter filter){
        if(!FilterFileUtils.isExists(filter)){
            FilterSaveDialog filterSaveDialog = new FilterSaveDialog(filter);
            filterSaveDialog.show();
            filterSaveDialog.getSavedFilterName();
        }

        try {
            File onBootFiltersFile = new File(buildOnBootFilePath(ON_BOOT_FILTERS_FILE_NAME));
            if(!onBootFiltersFile.exists()){
                onBootFiltersFile.createNewFile();
            }
            Set<String> onBootFilters = new HashSet<>(FileUtils.readFile(onBootFiltersFile));
            onBootFilters.add(filter.filterHash());
            FileUtils.writeToFile(onBootFiltersFile, onBootFilters, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerOnBootFilter(String filterName){
        if(!FilterFileUtils.isExists(filterName)){
            throw new RuntimeException("Filter with name '"+filterName+"' does not exist.");
        }

        try {
            File onBootFiltersFile = new File(buildOnBootFilePath(ON_BOOT_FILTERS_FILE_NAME));
            Set<String> onBootFilters = new HashSet<>(FileUtils.readFile(onBootFiltersFile));
            onBootFilters.add(filterName);
            FileUtils.writeToFile(onBootFiltersFile, onBootFilters, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterOnBootFilter(IFilter filter){
        try {
            File onBootFiltersFile = new File(buildOnBootFilePath(ON_BOOT_FILTERS_FILE_NAME));
            List<String> onBootFilters = FileUtils.readFile(onBootFiltersFile);
            System.out.println("unregisterOnBootFilter");
            System.out.println("filter="+filter);
            System.out.println("onBootFilters="+onBootFilters);
            System.out.println("filterHash()="+filter.filterHash());
            onBootFilters.remove(filter.filterHash()+"");
            FileUtils.writeToFile(onBootFiltersFile, onBootFilters, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterOnBootFilter(String filterName){
        try {
            File onBootFiltersFile = new File(buildOnBootFilePath(ON_BOOT_FILTERS_FILE_NAME));
            List<String> onBootFilters = FileUtils.readFile(onBootFiltersFile);
            onBootFilters.remove(filterName);
            FileUtils.writeToFile(onBootFiltersFile, onBootFilters, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildOnBootFilePath(String fileName){
        return AppOptions.ON_BOOT_DIR_PATH + File.separator + fileName;
    }

    public static Collection<String> getRecentPaths(){
        try {
            return FileUtils.readFile(new File(buildOnBootFilePath(ON_BOOT_PATHS_FILE_NAME)), true);
        } catch (IOException e) {
            e.printStackTrace();
           return Collections.emptyList();
        }
    }

    public static void addRecentPaths(Collection<String> paths){
        try {
            FileUtils.writeToFile(new File(buildOnBootFilePath(ON_BOOT_PATHS_FILE_NAME)), paths, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
