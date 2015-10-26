package com.gnatiuk.searcher.filters;

import com.gnatiuk.searcher.filters.external.ExternalFilterMarker;
import com.gnatiuk.searcher.filters.internal.InternalFilterMarker;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.File;
import java.util.*;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FiltersContainer implements IFilter, ExternalFilterMarker {

    @JsonProperty("filters")
    private Set<IFilter> filters;

    public FiltersContainer(){
        this(new ArrayList<IFilter>());
    }

    public FiltersContainer(Collection<IFilter> filters){
        this.filters = new TreeSet<>(new FilterScopeComparator());
        this.filters.addAll(filters);
    }

    public FiltersContainer addFilter(IFilter filter){
        if(filter != NONE_FILTER){
            filters.add(filter);
        }
        return this;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return filters.isEmpty();
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
                "com.gnatiuk.filters=" + filters +
                '}';
    }

    @Override
    public String filterHash() {
        StringBuilder result = new StringBuilder();
        for (IFilter filter : filters) {
            result.append(filter.filterHash());
        }
        return result.toString();
    }

    public Collection<IFilter> getFilters() {
        return filters;
    }

    private static class FilterScopeComparator implements Comparator<IFilter>{

        @Override
        public int compare(IFilter filter1, IFilter filter2) {
            if(getWeight(filter1) - getWeight(filter2) >= 0){
                return 1;
            }
            return -1;
        }

        private int getWeight(IFilter filter){
            if (filter instanceof InternalFilterMarker){
                return InternalFilterMarker.WEIGHT;
            }else if(filter instanceof ExternalFilterMarker){
                return ExternalFilterMarker.WEIGHT;
            }else {
                return Integer.MAX_VALUE;
            }
        }
    }
}
