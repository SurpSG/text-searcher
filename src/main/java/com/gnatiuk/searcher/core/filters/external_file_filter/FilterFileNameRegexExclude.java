package com.gnatiuk.searcher.core.filters.external_file_filter;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileNameRegexExclude extends FilterFileNameRegex {
    public FilterFileNameRegexExclude(List<String> filesPatterns) {
        super(filesPatterns);
    }

    @Override
    public boolean doFilter(File file) {
        return !super.doFilter(file);
    }
}
