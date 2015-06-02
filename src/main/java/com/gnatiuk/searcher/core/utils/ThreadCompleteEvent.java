package com.gnatiuk.searcher.core.utils;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class ThreadCompleteEvent {
    private int threadId;

    public ThreadCompleteEvent(int threadId) {
        this.threadId = threadId;
    }

    public int getThreadId() {
        return threadId;
    }

}
