package com.gnatiuk.searcher.core.utils;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class TaskCompleteEvent {
    private int threadId;

    public TaskCompleteEvent(int threadId) {
        this.threadId = threadId;
    }

    public int getThreadId() {
        return threadId;
    }
}
