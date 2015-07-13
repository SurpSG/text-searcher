package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileNameRegex extends ATextFilter {

    private List<Pattern> filesPatterns;

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
    public boolean doFilter(File file) {
        for (Pattern filePattern : filesPatterns) {
            if(filePattern.matcher(textPreprocessor.process(file.getName())).find()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FilterFileNameRegex{" +
                "filesPatterns=" + filesPatterns +
                '}';
    }
}
