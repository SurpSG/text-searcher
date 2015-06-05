package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.ITaskCompleteListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.TaskCompleteEvent;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;

import java.util.concurrent.*;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class ThreadController {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private final ITaskCompleteListener taskCompleteListener;

    private final ThreadPoolExecutor executorService;

    private IWorkCompleteListener workCompleteListener;

    private ThreadController(){
        taskCompleteListener = createTaskCompleteListener();
        executorService =(ThreadPoolExecutor) Executors.newFixedThreadPool(CPU_UNITS);
    }

    public static class ThreadControllerHolder {
        public static final ThreadController HOLDER_INSTANCE = new ThreadController();
    }

    public static ThreadController getInstance(){
        return ThreadControllerHolder.HOLDER_INSTANCE;
    }

    private ITaskCompleteListener createTaskCompleteListener(){
        ITaskCompleteListener taskCompleteListener = new ITaskCompleteListener() {
            @Override
            public void actionPerformed(TaskCompleteEvent event) {
                if(workCompleteListener != null && executorService.getQueue().isEmpty()){
                    alertTasksFinished();
                }
            }
        };
        return taskCompleteListener;
    }

    private synchronized void alertTasksFinished(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(executorService.getQueue().isEmpty() && executorService.getActiveCount() == 0){
                    System.out.println("time: "+(System.currentTimeMillis() - Finder.t1));
                    workCompleteListener.actionPerformed(new WorkCompleteEvent());
                    executorService.shutdown();
                }
            }
        }).start();
    }

    public void registerThread(SearchRunnable searchThread){
        searchThread.addTaskCompleteListener(taskCompleteListener);
        executorService.execute(searchThread);
    }

    public void addWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        this.workCompleteListener = workCompleteListener;
    }

    public void removeWorkCompleteListener(){
        workCompleteListener = null;
    }

    void shutdown(){
        executorService.shutdown();
    }
}
