package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.*;
import com.gnatiuk.searcher.ui.utils.FileSearchStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public abstract class SearchRunnable implements Runnable {


    private static int ID_SOURCE = 0;

    private final int id;
    private ITaskStartedListener taskStartedListener;
    private ITaskCompleteListener taskCompleteListener;
    protected IFileFoundListener fileFoundListener;

    protected List<String> textsToFind;


    public SearchRunnable(List<String> textsToFind) {
        this.textsToFind = textsToFind;
        id = ID_SOURCE++;
    }

    @Override
    public void run() {
        if (taskStartedListener != null) {
            taskStartedListener.actionPerformed(createTaskStartedEvent());
        }
        try {
            performSearch();
        } finally {
            if (taskCompleteListener != null) {
                taskCompleteListener.actionPerformed(new TaskCompleteEvent(getProcessedFiles(), FileSearchStatus.FILTERED_COLOR));
            }
        }
    }

    protected abstract List<String> getProcessedFiles();

    protected abstract void performSearch();

    protected abstract TaskStartedEvent createTaskStartedEvent();

    protected void alertFileFound(String filePath, String fileRow){
        if(fileFoundListener != null){
            fileFoundListener.alertFileFound(new FileFoundEvent(filePath, fileRow));
        }
    }

    public void addTaskCompleteListener(ITaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    public void addTaskStartedListener(ITaskStartedListener taskStartedListener) {
        this.taskStartedListener = taskStartedListener;
    }

    public void addFileFoundListener(IFileFoundListener fileFoundListener) {
        this.fileFoundListener = fileFoundListener;
    }

    public int getId() {
        return id;
    }
}