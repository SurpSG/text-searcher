package com.gnatiuk.searcher.core.filters;

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
    public boolean doFilter(File file) {
        for (IFilter filter : filters) {
            if(!filter.doFilter(file)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltersContainer{" +
                "filters=" + filters +
                '}';
    }
}
