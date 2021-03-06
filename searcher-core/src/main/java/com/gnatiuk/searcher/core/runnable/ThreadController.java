package com.gnatiuk.searcher.core.runnable;

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
final class ThreadController {

    private static final int CPU_UNITS = Runtime.getRuntime().availableProcessors();

    private ITaskCompleteListener taskCompleteListener;
    private ITaskCompleteListener externalTaskCompleteListener;
    private ITaskStartedListener taskStartedListener;
    private IWorkCompleteListener workCompleteListener;
    private IFileFoundListener fileFoundListener;

    private List<Runnable> scheduledTasks;

    private final ThreadPoolExecutor executorService;

    private volatile ControllerState controllerState;


    private ThreadController(){
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
        controllerState = ControllerState.STOPPED;
        taskCompleteListener = createTaskCompleteListener();
        executorService = buildThreadPoolExecutor();
    }

    static class ThreadControllerHolder {
        public static final ThreadController HOLDER_INSTANCE = new ThreadController();
    }

    static ThreadController getInstance(){
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

    private void alertTasksFinished() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (controllerState) {
                    if (controllerState != ControllerState.STOPPED) {
                        if (isWorkFinished()) {
                            controllerState = ControllerState.STOPPED;
                            notifyWorkFinished();
                        }
                    }
                }
            }
        }).start();
    }

    private void notifyWorkFinished() {
        if (workCompleteListener != null) {
            workCompleteListener.actionPerformed(new WorkCompleteEvent());
        }
    }

    private boolean isWorkFinished() {
        return executorService.getQueue().isEmpty()
                && scheduledTasks.isEmpty()
                && executorService.getActiveCount() == 0;
    }

    void registerThread(SearchRunnable searchThread){
        searchThread.addTaskStartedListener(taskStartedListener);
        searchThread.addTaskCompleteListener(taskCompleteListener);
        searchThread.addFileFoundListener(fileFoundListener);

        if(controllerState == ControllerState.STARTED){
            executorService.execute(searchThread);
        }else {
            scheduledTasks.add(searchThread);
        }
    }

    void registerWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        this.workCompleteListener = workCompleteListener;
    }


    void registerTaskCompleteListener(ITaskCompleteListener taskCompleteListener){
        this.externalTaskCompleteListener = taskCompleteListener;
    }

    void registerTaskStartedListener(ITaskStartedListener taskStartedListener){
        this.taskStartedListener = taskStartedListener;
    }

    void registerFileFoundListener(IFileFoundListener fileFoundListener){
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

    void start(){
        if(controllerState == ControllerState.STARTED){
            throw new UnsupportedOperationException("Controller already started. Current state = "+controllerState);
        }
        synchronized (scheduledTasks) {
            controllerState = ControllerState.STARTED;
            scheduledTasks.forEach(executorService::execute);
            scheduledTasks.clear();
        }
    }

    void stop(){
        controllerState = ControllerState.STOPPED;
        scheduledTasks.clear();
        executorService.getQueue().clear();
    }

    void pause(){
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

    void resume(){
        if(controllerState != ControllerState.PAUSED){
            throw new UnsupportedOperationException("Controller is not paused. Current state = "+controllerState);
        }
        start();
    }

    ControllerState getControllerState() {
        return controllerState;
    }

    void shutdown(){
        executorService.shutdown();
    }

    enum ControllerState{
        PAUSED, STARTED, STOPPED;
    }
}
