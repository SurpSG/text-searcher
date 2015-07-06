package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ITextPreprocessor;

import java.io.File;
import java.util.List;

/**
 * Created by surop on 06.07.15.
 */
public class FilterFileNameExclude extends FilterFileName {

    public FilterFileNameExclude(List<String> keywords) {
        super(keywords);
    }

    public FilterFileNameExclude(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    @Override
    public boolean doFilter(File file) {
        return !super.doFilter(file);
    }
}
