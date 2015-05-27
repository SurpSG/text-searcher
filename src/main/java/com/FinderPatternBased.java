package com;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 5/27/15.
 */
public class FinderPatternBased extends Finder {

    private Pattern textToFindPattern;

    public FinderPatternBased(String textToFind, String[] filePaths, List<String> fileFilters) {
        super(textToFind, filePaths, fileFilters);
        textToFindPattern = createPattern(textToFind);
    }

    @Override
    protected boolean checkRow(String row) {
        return textToFindPattern.matcher(row).find();
    }
}
