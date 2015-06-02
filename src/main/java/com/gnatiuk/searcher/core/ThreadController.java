package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.utils.IThreadCompleteListener;
import com.gnatiuk.searcher.core.utils.ThreadCompleteEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class ThreadController {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private final SynchronousQueue<SearchRunnable> threadsQueue;
    private final List<SearchRunnable> runningThreads;

    private ThreadCompleteListener threadCompleteListener;

    private ThreadController(){
        threadsQueue = new SynchronousQueue<>();
        runningThreads = Collections.synchronizedList(new CopyOnWriteArrayList<SearchRunnable>());
        threadCompleteListener = new ThreadCompleteListener();
    }

    public static class ThreadControllerHolder {
        public static final ThreadController HOLDER_INSTANCE = new ThreadController();
    }

    public static ThreadController getInstance(){
        return ThreadControllerHolder.HOLDER_INSTANCE;
    }

    public void registerThread(SearchRunnable searchThread){
        threadsQueue.add(searchThread);
        invokeNext();
    }

    private synchronized void invokeNext(){
        while (runningThreads.size() < CPU_UNITS && threadsQueue.size() > 0){
            synchronized (threadsQueue){
                if(threadsQueue.size() > 0){
                    SearchRunnable searchRunnable = threadsQueue.poll();
                    searchRunnable.addThreadCompleteListener(threadCompleteListener);
                    runningThreads.add(searchRunnable);
                    invoke(searchRunnable);
                }
            }
        }
    }

    private void invoke(SearchRunnable runnable){
        new Thread(runnable).start();
    }

    private void clear(int threadId){
        for (SearchRunnable runningThread : runningThreads) {
            if(runningThread.getId() == threadId){
                runningThreads.remove(runningThread);
                invokeNext();
                return;
            }
        }
    }

    private class ThreadCompleteListener implements IThreadCompleteListener{

        @Override
        public void actionPerformed(ThreadCompleteEvent event) {
            clear(event.getThreadId());
        }
    }
}
