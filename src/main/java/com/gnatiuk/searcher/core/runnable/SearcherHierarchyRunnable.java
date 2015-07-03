package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.filters.FilterExecutor;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.utils.FileSearchStatus;
import com.gnatiuk.searcher.core.utils.TaskCompleteEvent;
import com.gnatiuk.searcher.core.utils.TaskStartedEvent;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class SearcherHierarchyRunnable extends SearchRunnable {

    private List<String> filePaths;
    private IFilter filter;

    public SearcherHierarchyRunnable(List<String> filePaths, IFilter filter) {
        this(filePaths, filter, SearchRunnable.NORMAL_PRIORITY);
    }

    public SearcherHierarchyRunnable(List<String> filePaths, IFilter filter, int priority) {
        super(priority);
        this.filter = filter;
        this.filePaths = filePaths;
    }

    protected void doWork() {
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
        return new TaskStartedEvent(getId(), fileNames);
    }

    @Override
    protected TaskCompleteEvent createTaskCompleteEvent() {
        return new TaskCompleteEvent(filePaths, FileSearchStatus.IN_PROGRESS);
    }

    protected void processFile(File file){
        if (file.isDirectory()) {
            invokeNewHierarchyThread(file);
        } else {
            invokeNewFilterThread(file);
        }
    }

    private List<String> buildFullPathForChildren(String parentAbsolutePath, String[] children){
        List<String> fullPathChildren = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < children.length; j++) {

            stringBuilder.append(parentAbsolutePath)
                    .append(File.separator).append(children[j]);

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
        ThreadController.getInstance().registerThread(new SearcherHierarchyRunnable(filePaths, filter,
                SearchRunnable.LOW_PRIORITY));
    }

    protected void invokeNewFilterThread(File file) {
        ThreadController.getInstance().registerThread(new FilterExecutor(filter, file, SearchRunnable.HIGH_PRIORITY));
    }
}
