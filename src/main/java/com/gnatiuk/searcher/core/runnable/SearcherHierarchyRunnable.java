package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.utils.TaskStartedEvent;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class SearcherHierarchyRunnable extends SearchRunnable {

    private List<String> filePaths;
    protected List<Pattern> fileFilterPatterns;

    public SearcherHierarchyRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
        super(textsToFind);
        this.filePaths = filePaths;
        fileFilterPatterns = fileFilters;
    }

    protected void performSearch() {
        for (String filePath : filePaths) {
            processFile(new File(filePath));
        }
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
        List<String> fileNames = new ArrayList<>();
        for (String filePath : filePaths) {
            fileNames.add(new File(filePath).getAbsolutePath());
        }
        return new TaskStartedEvent(getId(), fileNames);//TODO it seems that is should be a Path
    }

    protected void processFile(File file){
        if (file.isDirectory()) {
            invokeNewHierarchyThread(file);
        } else {
            invokeFileReadThread(file);
        }
    }

    private List<String> buildFullPathForChildren(String parentAbsolutePath, String[] children){
        List<String> fullPathChildren = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < children.length; j++) {

            stringBuilder.append(parentAbsolutePath)
                    .append("/").append(children[j]);

            fullPathChildren.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        return fullPathChildren;
    }

    protected void invokeNewHierarchyThread(File file){
        if(Files.isSymbolicLink(file.toPath())){
            return;
        }
        String[] files = file.list();
        if (files == null) {
            return;
        }

        invokeNewHierarchyThread(buildFullPathForChildren(file.getAbsolutePath(), files));
    }

    protected void invokeNewHierarchyThread(List<String> filePaths){
        ThreadController.getInstance().registerThread(SearcherHierarchyRunnable.build(textsToFind, filePaths,
                fileFilterPatterns));
    }


    protected void invokeFileReadThread(File file) {
        if(fileFilterPatterns.isEmpty()){
            ThreadController.getInstance().registerThread(new SearcherFileRunnable(textsToFind, file));
        }else{
            for (Pattern fileFilterPattern : fileFilterPatterns) {
                if (fileFilterPattern.matcher(file.getName()).find()) {
                    ThreadController.getInstance().registerThread(new SearcherFileRunnable(textsToFind, file));
                    return;
                }
            }
        }
    }

    public static SearcherHierarchyRunnable build(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters){
        return new SearcherHierarchyRunnable(textsToFind, filePaths, fileFilters);
    }
}
