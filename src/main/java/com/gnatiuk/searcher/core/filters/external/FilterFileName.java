package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.utils.FileFoundEvent;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileName extends ATextFilter {

    public FilterFileName(List<String> keywords) {
        super(keywords);
    }

    public FilterFileName(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    @Override
    public FileFoundEvent doFilter(File file) {
        for (String targetFileName : keywords) {
            if(textPreprocessor.process(file.getName()).contains(targetFileName)){
                return new FileFoundEvent(file);
            }
        }
        return FileFoundEvent.NOT_FOUND;
    }
}
