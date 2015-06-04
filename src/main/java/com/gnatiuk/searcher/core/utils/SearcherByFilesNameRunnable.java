package com.gnatiuk.searcher.core.utils;

import com.gnatiuk.searcher.core.SearchHierarchyRunnable;
import com.gnatiuk.searcher.core.SearchRunnable;
import com.gnatiuk.searcher.core.ThreadController;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sergiy on 6/4/2015.
 */
public class SearcherByFilesNameRunnable extends SearchHierarchyRunnable {
    public SearcherByFilesNameRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
        super(textsToFind, filePaths, fileFilters);
    }

    @Override
    protected void processFile(File file){
        checkFileMatchWithFileFilters(file.getName());
        if (file.isDirectory()) {
            invokeNewHierarchyThread(file);
        }
    }

    private void checkFileMatchWithFileFilters(String filePath){
        for (Pattern fileFilterPattern : fileFilterPatterns) {
            if(fileFilterPattern.matcher(filePath).find()){
                System.out.println("\tFound! file: "+filePath);
            }
        }
    }

    @Override
    protected void invokeNewHierarchyThread(List<String> filePaths){
        ThreadController.getInstance().registerThread(SearcherByFilesNameRunnable.build(textsToFind, filePaths,
                fileFilterPatterns));
    }

    public static SearcherByFilesNameRunnable build(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters){
        return new SearcherByFilesNameRunnable(textsToFind, filePaths, fileFilters);
    }
}
