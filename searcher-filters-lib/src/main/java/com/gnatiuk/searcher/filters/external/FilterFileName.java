package com.gnatiuk.searcher.filters.external;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileName extends ATextFilter implements ExternalFilterMarker {

    private FileSearchEvent fileSearchEvent;

    public FilterFileName() {
        this(new ArrayList<>());
    }
    public FilterFileName(List<String> keywords) {
        super(keywords);
    }

    public FilterFileName(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        for (String targetFileName : keywords) {
            if(textPreprocessor.process(file.getName()).contains(targetFileName)){
                buildFileFoundEvent(file);
                return new FileSearchEvent(file);
            }
        }
        return FileSearchEvent.NOT_FOUND;
    }

    private void buildFileFoundEvent(File file){
        fileSearchEvent = new FileSearchEvent(file);
    }

}
