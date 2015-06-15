package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.*;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public abstract class SearchRunnable implements Runnable {


    private static int ID_SOURCE = 0;

    private final int id;

    private ITaskStartedListener taskStartedListener;
    private ITaskCompleteListener taskCompleteListener;
    protected IFileFoundListener fileFoundListener;

    public SearchRunnable() {
        id = ID_SOURCE++;
    }

    @Override
    public void run() {
        alertTaskStarted();
        try {
            doWork();
        } finally {
            alertTaskCompleted();
        }
    }

    private void alertTaskStarted(){
        if (taskStartedListener != null) {
            taskStartedListener.actionPerformed(createTaskStartedEvent());
        }
    }

    private void alertTaskCompleted(){
        if (taskCompleteListener != null) {
            taskCompleteListener.actionPerformed(createTaskCompleteEvent());
        }
    }

    protected abstract void doWork();

    protected abstract TaskStartedEvent createTaskStartedEvent();

    protected abstract TaskCompleteEvent createTaskCompleteEvent();

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