package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.IFileFoundListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.filters.CompareStatus;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 11/19/15.
 */
public final class SearchController {

    private List<Path> foundFiles;
    private SearchOption previousSearchOption;
    private SearchOption currentSearchOption;


    private SearchController(){
        foundFiles = new ArrayList<>();
        currentSearchOption = new SearchOption();
    }

    public static SearchController getInstance(){
        return SearchCotrollerHolder.HOLDER_INSTANCE;
    }


    static class SearchCotrollerHolder {
        static final SearchController HOLDER_INSTANCE = new SearchController();
    }

    public void setPathsToSearch(List<String> paths){
        currentSearchOption.paths = paths;
    }

    public void setFilter(IFilter filter){
        if(ThreadController.getInstance().getControllerState() != ThreadController.ControllerState.STOPPED){
            throw new RuntimeException("Setting of new filters is not allowed when controller is not stooped.");
        }
        currentSearchOption.filter = filter;
    }

    private boolean reusePreviousSearchResults(){
        if(previousSearchOption == null || !currentSearchOption.paths.equals(previousSearchOption.paths)){
            return false;
        }
        return currentSearchOption.filter.compareToFilter(previousSearchOption.filter) != CompareStatus.NOT_EQUALS;
    }


    public void startSearching(){
        if(reusePreviousSearchResults()){
            System.err.println("reuse");
            List<Path> prevFoundFiles = new ArrayList<>(foundFiles);
            System.out.println(prevFoundFiles);
            for (Path prevFoundFile : prevFoundFiles) {
                ThreadController.getInstance().registerThread(
                        new FilterExecutor(currentSearchOption.filter, prevFoundFile.toFile(), SearchRunnable.HIGH_PRIORITY)
                );
            }
        }else{
            ThreadController.getInstance().registerThread(
                    new SearcherHierarchyRunnable(currentSearchOption.paths, currentSearchOption.filter)
            );
        }
        foundFiles.clear();
        ThreadController.getInstance().start();
    }

    public void stopSearching(){
        foundFiles.clear();
        ThreadController.getInstance().stop();
    }

    public void resumeSearching(){
        ThreadController.getInstance().resume();
    }

    public void pauseSearching(){
        ThreadController.getInstance().pause();
    }

    public void registerWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        ThreadController.getInstance().registerWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                previousSearchOption = currentSearchOption;
                workCompleteListener.actionPerformed(event);
            }
        });
    }

    public void registerFileFoundListener(IFileFoundListener fileFoundListener){
        ThreadController.getInstance().registerFileFoundListener(
                new IFileFoundListener() {
                    @Override
                    public void alertFileFound(FileSearchEvent fileSearchEvent) {
                        if(fileSearchEvent != FileSearchEvent.NOT_FOUND){
                            foundFiles.add(fileSearchEvent.getFilePath());
                            fileFoundListener.alertFileFound(fileSearchEvent);
                        }
                    }
                }
        );
    }

    public void shutdown(){
        ThreadController.getInstance().shutdown();
    }


    private static class SearchOption{
        List<String> paths;
        IFilter filter;

        @Override
        public String toString() {
            return "SearchOption{" +
                    "paths=" + paths +
                    ", filter=" + filter +
                    '}';
        }
    }
}
