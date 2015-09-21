package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private List<Runnable> scheduledTasks;

    private final ThreadPoolExecutor executorService;

    private ControllerState controllerState;


    private ThreadController(){
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
        controllerState = ControllerState.STOPPED;
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
                if(executorService.getQueue().isEmpty()
                        && scheduledTasks.isEmpty()
                        && executorService.getActiveCount() == 0){
                    System.out.println("time: " + (System.currentTimeMillis() - Finder.t1));
                    if(workCompleteListener != null){
                        workCompleteListener.actionPerformed(new WorkCompleteEvent());
                        controllerState = ControllerState.STOPPED;
                    }
                }
            }
        }).start();
    }

    public void registerThread(SearchRunnable searchThread){
        searchThread.addTaskStartedListener(taskStartedListener);
        searchThread.addTaskCompleteListener(taskCompleteListener);
        searchThread.addFileFoundListener(fileFoundListener);

        if(controllerState == ControllerState.STARTED){
            executorService.execute(searchThread);
        }else {
            scheduledTasks.add(searchThread);
        }
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

    public void start(){
        if(controllerState == ControllerState.STARTED){
            throw new UnsupportedOperationException("Controller already started. Current state = "+controllerState);
        }
        synchronized (scheduledTasks) {
            controllerState = ControllerState.STARTED;
            scheduledTasks.forEach(executorService::execute);
            scheduledTasks.clear();
        }
    }

    public void stop(){
        controllerState = ControllerState.STOPPED;
        scheduledTasks.clear();
        executorService.getQueue().clear();
    }

    public void pause(){
        if(controllerState != ControllerState.STARTED){
            throw new UnsupportedOperationException("Controller is not started. Current state = "+controllerState);
        }
        synchronized (scheduledTasks){
            controllerState = ControllerState.PAUSED;
            scheduledTasks.clear();
            scheduledTasks.addAll(executorService.getQueue());
            executorService.getQueue().clear();
        }
    }

    public void resume(){
        if(controllerState != ControllerState.PAUSED){
            throw new UnsupportedOperationException("Controller is not paused. Current state = "+controllerState);
        }
        start();
    }

    public void shutdown(){
        executorService.shutdown();
    }

    private enum ControllerState{
        PAUSED, STARTED, STOPPED;
    }
}
