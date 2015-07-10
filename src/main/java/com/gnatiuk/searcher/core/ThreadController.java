package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.*;

import java.util.Comparator;
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
        executorService = buildThreadPoolExecutor();
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
                if(externalTaskCompleteListener != null){
                    externalTaskCompleteListener.actionPerformed(event);
                }

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

    private ThreadPoolExecutor buildThreadPoolExecutor(){
        return new ThreadPoolExecutor(CPU_UNITS, CPU_UNITS, 0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(CPU_UNITS, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                if(o1 instanceof SearchRunnable
                        && o2 instanceof SearchRunnable){
                    SearchRunnable obj1 = (SearchRunnable) o1;
                    SearchRunnable obj2 = (SearchRunnable) o2;
                    return obj2.getPriority() - obj1.getPriority();
                }
                return 0;
            }
        }));
    }

    public void shutdown(){
        executorService.shutdown();
    }
}
