package com.gnatiuk.searcher.core.filters;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.FileSearchStatus;
import com.gnatiuk.searcher.core.utils.TaskCompleteEvent;
import com.gnatiuk.searcher.core.utils.TaskStartedEvent;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterExecutor extends SearchRunnable {

    private IFilter filter;
    private File file;
    private FileSearchStatus fileSearchStatus;

    public FilterExecutor(IFilter filter, File file) {
        this.filter = filter;
        this.file = file;
    }

    @Override
    protected void doWork() {
        if(filter.doFilter(file)){
            System.out.println("Found! "+file.getName());
            alertFileFound(file.getAbsolutePath(), "");
            fileSearchStatus = FileSearchStatus.PROCESSED_FOUND;
        }
        fileSearchStatus = FileSearchStatus.PROCESSED_NOT_FOUND;
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
        return new TaskStartedEvent(getId(), Arrays.asList(file.getAbsolutePath()));
    }

    @Override
    protected TaskCompleteEvent createTaskCompleteEvent() {
        return new TaskCompleteEvent(Arrays.asList(file.getAbsolutePath()), fileSearchStatus);
    }
}
