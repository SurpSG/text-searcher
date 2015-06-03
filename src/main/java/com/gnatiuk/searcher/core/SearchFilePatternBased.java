package com.gnatiuk.searcher.core;

import java.io.File;
import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class SearchFilePatternBased extends SearchFileRunnable {
    public SearchFilePatternBased(List<String> textsToFind, File fileToRead) {
        super(textsToFind, fileToRead);
    }

    @Override
    protected boolean isLineContainsKeyword(String line, String keyword){
        return Finder.createPattern(keyword).matcher(line).find();
    }
}
