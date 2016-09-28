package com.gnatiuk.searcher.filters.external;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 9/28/16.
 */
public class FilterPathExclude extends ATextFilter implements ExternalFilterMarker{


    public FilterPathExclude(List<String> keywords) {
        super(keywords);
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        for (String keyword : keywords) {
            if(file.getAbsolutePath().startsWith(keyword)){
                return FileSearchEvent.NOT_FOUND;
            }
        }
        return new FileSearchEvent(file);
    }
}
