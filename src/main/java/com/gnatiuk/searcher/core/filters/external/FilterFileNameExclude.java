package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;

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
    public FileSearchEvent doFilter(File file) {
        FileSearchEvent foundEvent = super.doFilter(file);
        if(foundEvent == FileSearchEvent.NOT_FOUND){
            return new FileSearchEvent(file);
        }else {
            return FileSearchEvent.NOT_FOUND;
        }
    }
}
