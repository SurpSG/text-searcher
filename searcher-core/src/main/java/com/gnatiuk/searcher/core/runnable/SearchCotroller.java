package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.utils.IFileFoundListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 11/19/15.
 */
public final class SearchCotroller {

    private List<Path> foundFiles;
    private List<String> pathsToSearch;
    private IFilter filtersContainer;

    private SearchCotroller(){
        foundFiles = new ArrayList<>();
    }

    public static SearchCotroller getInstance(){
        return SearchCotrollerHolder.HOLDER_INSTANCE;
    }


    static class SearchCotrollerHolder {
        static final SearchCotroller HOLDER_INSTANCE = new SearchCotroller();
    }

    public void setPathsToSearch(List<String> paths){
        pathsToSearch = paths;
    }

    public void setFilter(IFilter filtersContainer){
        if(ThreadController.getInstance().getControllerState() != ThreadController.ControllerState.STOPPED){
            throw new RuntimeException("Setting of new filters is not allowed when controller is not stooped.");
        }
        this.filtersContainer = filtersContainer;
    }

    private boolean reusePreviuosSearchResults(){
        return false;
    }


    public void startSearching(){
        if(reusePreviuosSearchResults()){
            //TODO reuse
        }else{
            foundFiles.clear();
            ThreadController.getInstance().registerThread(new SearcherHierarchyRunnable(pathsToSearch, filtersContainer));
        }
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

    public void addWorkCompleteListener(IWorkCompleteListener workCompleteListener){
        ThreadController.getInstance().addWorkCompleteListener(workCompleteListener);
    }

    public void registerFileFoundListener(IFileFoundListener fileFoundListener){
        ThreadController.getInstance().registerFileFoundListener(
                new IFileFoundListener() {
                    @Override
                    public void alertFileFound(FileSearchEvent fileSearchEvent) {
                        foundFiles.add(fileSearchEvent.getFilePath());
                        fileFoundListener.alertFileFound(fileSearchEvent);
                    }
                }
        );
    }

    public void shutdown(){
        ThreadController.getInstance().shutdown();
    }
}
