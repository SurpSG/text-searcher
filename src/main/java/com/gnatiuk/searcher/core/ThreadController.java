package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.*;

import java.util.concurrent.*;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class ThreadController {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private ITaskCompleteListener taskCompleteListener;
    private ITaskCompleteListener externalTaskCompleteListener;
    private ITaskStartedListener taskStartedListener;
    private IWorkCompleteListener workCompleteListener;
    private IFileFoundListener fileFoundListener;

    private final ThreadPoolExecutor executorService;


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
        System.out.println("dich");
        ITaskCompleteListener taskCompleteListener = new ITaskCompleteListener() {
            @Override
            public void actionPerformed(TaskCompleteEvent event) {
                if(externalTaskCompleteListener != null){
                    externalTaskCompleteListener.actionPerformed(event);
                }

                System.out.println(executorService.getQueue().size());
                if(executorService.getQueue().isEmpty()){
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
                    System.out.println("time: " + (System.currentTimeMillis() - Finder.t1));
                    if(workCompleteListener != null){
                        workCompleteListener.actionPerformed(new WorkCompleteEvent());
                    }
                    executorService.shutdown();
                }
            }
        }).start();
    }

    public void registerThread(SearchRunnable searchThread){
        searchThread.addTaskStartedListener(taskStartedListener);
        searchThread.addTaskCompleteListener(taskCompleteListener);
        searchThread.addFileFoundListener(fileFoundListener);
        executorService.execute(searchThread);
    }

    public void addWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        this.workCompleteListener = workCompleteListener;
    }


    public void registerTaskCompleteListener(ITaskCompleteListener taskCompleteListener){
        this.externalTaskCompleteListener = taskCompleteListener;
    }

    public void registerTaskStartedListener(ITaskStartedListener taskStartedListener){
        this.taskStartedListener = taskStartedListener;
    }

    public void registerFileFoundListener(IFileFoundListener fileFoundListener){
        this.fileFoundListener = fileFoundListener;
    }
}
