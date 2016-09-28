package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.TaskCompleteEvent;
import com.gnatiuk.searcher.core.utils.TaskStartedEvent;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import com.gnatiuk.searcher.filters.util.SearchOption;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterExecutor extends SearchRunnable {

    private IFilter filter;
    private File file;

    public FilterExecutor(IFilter filter, File file, int priority) {
        super(priority);
        this.filter = filter;
        this.file = file;
    }
    private static Object sync = new Object();
    @Override
    protected void doWork() {
        FileSearchEvent fileSearchEvent;
        if((fileSearchEvent = filter.doFilter(file)) != FileSearchEvent.NOT_FOUND){
            synchronized (sync){
                System.out.println("gedit " + file.getAbsolutePath());
                for (SearchOption searchOption : fileSearchEvent.getSearchOptions()) {
                    System.out.println("\t\t"+searchOption.getFoundOption()+": "+searchOption.getFoundValue());
                }
            }
            alertFileFound(fileSearchEvent);
        }
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
        return new TaskStartedEvent(getId(), Arrays.asList(file.getAbsolutePath()));
    }

    @Override
    protected TaskCompleteEvent createTaskCompleteEvent() {
        return new TaskCompleteEvent(Arrays.asList(file.getAbsolutePath()));
    }

    @Override
    public String toString() {
        return "FilterExecutor{" +
                "file=" + file +
                '}';
    }
}
