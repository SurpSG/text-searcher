package com.gnatiuk.searcher.core.filters.external_file_filter;

import com.gnatiuk.searcher.core.filters.AFilter;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterFileName extends AFilter {

    private List<String> targetFileNames;

    public FilterFileName(List<String> targetFileNames) {
        this.targetFileNames = targetFileNames;
    }

    @Override
    public boolean doFilter(File file) {
        for (String targetFileName : targetFileNames) {
            if(file.getName().contains(targetFileName)){
                return true;
            }
        }
        return false;
    }
}
