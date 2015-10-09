package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileNameRegex extends ATextFilter implements ExternalFilterMarker {


    private FileSearchEvent fileSearchEvent;

    private List<Pattern> filesPatterns;

    public FilterFileNameRegex(){
        super();
    }

    public FilterFileNameRegex(List<String> keywords) {
        this(keywords, ITextPreprocessor.NONE_PROCESSOR);
    }

    public FilterFileNameRegex(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
        initPatternsList(this.keywords);
    }

    private void initPatternsList(List<String> patterns){
        filesPatterns = new ArrayList<>();
        for (String pattern : patterns) {
            filesPatterns.add(Pattern.compile(pattern));
        }
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        for (Pattern filePattern : filesPatterns) {
            if(filePattern.matcher(textPreprocessor.process(file.getName())).find()){
                buildFileFoundEvent(file);
                return new FileSearchEvent(file);
            }
        }
        return FileSearchEvent.NOT_FOUND;
    }

    private void buildFileFoundEvent(File file){
        fileSearchEvent = new FileSearchEvent(file);
    }

    @Override
    public String toString() {
        return "FilterFileNameRegex{" +
                "filesPatterns=" + filesPatterns +
                '}';
    }
}
