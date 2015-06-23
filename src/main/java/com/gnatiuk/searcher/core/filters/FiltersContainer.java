package com.gnatiuk.searcher.core.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FiltersContainer extends AFilter {

    private List<AFilter> filters;

    public FiltersContainer(){
        this(new ArrayList<AFilter>());
    }

    public FiltersContainer(List<AFilter> filters){
        this.filters = filters;
    }

    public void addFilter(AFilter filter){
        filters.add(filter);
    }

    @Override
    public boolean doFilter(File file) {
        for (AFilter filter : filters) {
            if(!filter.doFilter(file)){
                return false;
            }
        }
        return true;
    }


}
