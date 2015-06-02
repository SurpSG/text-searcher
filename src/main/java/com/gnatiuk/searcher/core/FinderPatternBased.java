package com.gnatiuk.searcher.core;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 5/27/15.
 */
public class FinderPatternBased extends Finder {

    public FinderPatternBased(String textToFind, List<String> filePaths, List<String> fileFilters) {
        super(textToFind, filePaths, fileFilters);
    }

    public FinderPatternBased(List<String> textsToFind, List<String> filePaths, List<String> fileFilters) {
        super(textsToFind, filePaths, fileFilters);
    }

    @Override
    protected boolean isLineContainsKeyword(String line, String keyword){
        return createPattern(keyword).matcher(line).find();
    }
}
