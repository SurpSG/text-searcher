package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.ThreadController;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sergiy on 6/4/2015.
 */
public class SearcherByFileNameRunnable extends SearcherHierarchyRunnable {
    public SearcherByFileNameRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
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
        ThreadController.getInstance().registerThread(SearcherByFileNameRunnable.build(textsToFind, filePaths,
                fileFilterPatterns));
    }

    public static SearcherByFileNameRunnable build(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters){
        return new SearcherByFileNameRunnable(textsToFind, filePaths, fileFilters);
    }
}
