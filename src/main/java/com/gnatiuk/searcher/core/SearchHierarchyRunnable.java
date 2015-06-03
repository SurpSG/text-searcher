package com.gnatiuk.searcher.core;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class SearchHierarchyRunnable extends SearchRunnable {

    private List<String> filePaths;
    private List<Pattern> fileFilterPatterns;

    public SearchHierarchyRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
        super(textsToFind);
        this.filePaths = filePaths;
        fileFilterPatterns = fileFilters;
    }

    protected void performSearch() {
        for (String filePath : filePaths) {
            File file = new File(filePath);

            if (file.isDirectory() && !Files.isSymbolicLink(file.toPath())) {
                String[] files = file.list();
                if (files == null) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < files.length; j++) {

                    stringBuilder.append(file.getAbsolutePath())
                            .append("/")
                            .append(files[j]);

                    files[j] = stringBuilder.toString();
                    stringBuilder.setLength(0);
                }

                ThreadController.getInstance().registerThread(new SearchHierarchyRunnable(textsToFind, Arrays.asList(files), fileFilterPatterns));
            } else {
                invokeFileReadThread(file);
            }
        }
    }

    private void invokeFileReadThread(File file) {
        if(fileFilterPatterns.isEmpty()){
            ThreadController.getInstance().registerThread(new SearchFileRunnable(textsToFind, file));
        }else{
            for (Pattern fileFilterPattern : fileFilterPatterns) {
                if (fileFilterPattern.matcher(file.getName()).find()) {
                    ThreadController.getInstance().registerThread(new SearchFileRunnable(textsToFind, file));
                    return;
                }
            }
        }
    }
}
