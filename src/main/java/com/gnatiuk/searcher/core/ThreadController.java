package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.*;

import java.util.concurrent.*;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class ThreadController {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private final ITaskCompleteListener taskCompleteListener;
    private ITaskStartedListener taskStartedListener;

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
//                    executorService.shutdown();
                }
            }
        }).start();
    }

    public void registerThread(SearchRunnable searchThread){
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        searchThread.addTaskStartedListener(taskStartedListener);
        searchThread.addTaskCompleteListener(taskCompleteListener);
        executorService.execute(searchThread);
    }

    public void addWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        this.workCompleteListener = workCompleteListener;
    }

    public void addTaskStartedListener(ITaskStartedListener taskStartedListener){
        this.taskStartedListener = taskStartedListener;
    }
}
