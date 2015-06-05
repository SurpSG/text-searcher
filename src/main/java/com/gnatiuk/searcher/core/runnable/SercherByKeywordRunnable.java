package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.ThreadController;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/5/15.
 */
public class SercherByKeywordRunnable extends SearcherHierarchyRunnable {
    public SercherByKeywordRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
        super(textsToFind, filePaths, fileFilters);
    }

    protected void invokeFileReadThread(File file) {
        ThreadController.getInstance().registerThread(new SearcherFileRunnable(textsToFind, file));
    }
}
