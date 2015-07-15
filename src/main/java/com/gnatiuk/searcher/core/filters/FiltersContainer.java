package com.gnatiuk.searcher.core.filters;

import com.gnatiuk.searcher.core.utils.FileFoundEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FiltersContainer implements IFilter {

    private List<IFilter> filters;

    public FiltersContainer(){
        this(new ArrayList<>());
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
    public FileFoundEvent doFilter(File file) {
        FileFoundEvent foundEvent = new FileFoundEvent(file);
        for (IFilter filter : filters) {
            FileFoundEvent fileFoundEvent = filter.doFilter(file);
            if(fileFoundEvent == FileFoundEvent.NOT_FOUND){
                return FileFoundEvent.NOT_FOUND;
            }
            foundEvent.mergeFoundOptions(fileFoundEvent);
        }
        System.out.println(foundEvent);
        return foundEvent;
    }

    @Override
    public String toString() {
        return "FiltersContainer{" +
                "filters=" + filters +
                '}';
    }
}
