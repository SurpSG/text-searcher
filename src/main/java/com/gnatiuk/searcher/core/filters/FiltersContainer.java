package com.gnatiuk.searcher.core.filters;

import com.gnatiuk.searcher.core.utils.FileSearchEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FiltersContainer implements IFilter {

    private List<IFilter> filters;

    public FiltersContainer(){
        this(new ArrayList<IFilter>());
    }

    public FiltersContainer(List<IFilter> filters){
        this.filters = filters;
    }

    public FiltersContainer addFilter(IFilter filter){
        if(filter != IFilter.NONE_FILTER){
            filters.add(filter);
        }
        return this;
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        FileSearchEvent foundEvent = new FileSearchEvent(file);
        for (IFilter filter : filters) {
            FileSearchEvent fileSearchEvent = filter.doFilter(file);
            if(fileSearchEvent == FileSearchEvent.NOT_FOUND){
                return FileSearchEvent.NOT_FOUND;
            }
            foundEvent.mergeFoundOptions(fileSearchEvent);
        }
        return foundEvent;
    }

    @Override
    public String toString() {
        return "FiltersContainer{" +
                "filters=" + filters +
                '}';
    }
}
