package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.utils.FileFoundEvent;

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
    public FileFoundEvent doFilter(File file) {
        FileFoundEvent foundEvent = super.doFilter(file);
        if(foundEvent == FileFoundEvent.NOT_FOUND){
            return new FileFoundEvent(file);
        }else {
            return FileFoundEvent.NOT_FOUND;
        }
    }
}
