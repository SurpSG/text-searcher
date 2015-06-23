package com.gnatiuk.searcher.core.filters.external_file_filter;

import com.gnatiuk.searcher.core.filters.AFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileNameRegex extends AFilter {

    private List<Pattern> filesPatterns;

    public FilterFileNameRegex(List<String> filesPatterns) {
        initPatternsList(filesPatterns);
    }

    private void initPatternsList(List<String> patterns){
        filesPatterns = new ArrayList<>();
        for (String pattern : patterns) {
            filesPatterns.add(createPattern(pattern));
        }
    }

    private Pattern createPattern(String pattern){
        return Pattern.compile(pattern);
    }

    @Override
    public boolean doFilter(File file) {
        for (Pattern filePattern : filesPatterns) {
            if(filePattern.matcher(file.getName()).find()){
                return true;
            }
        }
        return false;
    }
}
