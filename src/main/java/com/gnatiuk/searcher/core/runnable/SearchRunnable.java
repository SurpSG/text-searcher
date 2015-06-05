package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.ITaskCompleteListener;
import com.gnatiuk.searcher.core.utils.TaskCompleteEvent;

import java.util.List;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public abstract class SearchRunnable implements Runnable {


    private static int ID_SOURCE = 0;

    private final int id;
    private ITaskCompleteListener taskCompleteListener;

    protected List<String> textsToFind;


    public SearchRunnable(List<String> textsToFind) {
        this.textsToFind = textsToFind;
        id = ID_SOURCE++;
    }

    @Override
    public void run() {
        try {
            performSearch();
        } finally {
            if (taskCompleteListener != null) {
                taskCompleteListener.actionPerformed(new TaskCompleteEvent(id));
            }
        }
    }

    protected abstract void performSearch();


    public void addTaskCompleteListener(ITaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    public int getId() {
        return id;
    }
}