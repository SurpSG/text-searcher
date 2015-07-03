package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ITextPreprocessor;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileNameRegexExclude extends FilterFileNameRegex {
    public FilterFileNameRegexExclude(List<String> filesPatterns) {
        super(filesPatterns);
    }

    public FilterFileNameRegexExclude(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    @Override
    public boolean doFilter(File file) {
        return !super.doFilter(file);
    }
}
