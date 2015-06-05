package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.Finder;
import com.gnatiuk.searcher.core.ThreadController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/5/15.
 */
public class SearcherKeywortPatternBased extends SearcherHierarchyRunnable {
    public SearcherKeywortPatternBased(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters) {
        super(textsToFind, filePaths, fileFilters);
    }

    protected void invokeFileReadThread(File file) {
        ThreadController.getInstance().registerThread(new SearcherFileKeywordPattern(textsToFind, file));
    }

    private class SearcherFileKeywordPattern extends SearcherFileRunnable{

        List<Pattern> keywordsPatterns;


        public SearcherFileKeywordPattern(List<String> textsToFind, File fileToRead) {
            super(textsToFind, fileToRead);
            keywordsPatterns = createKeywordsPatterns(textsToFind);
        }

        private List<Pattern> createKeywordsPatterns(List<String> keywords){
            List<Pattern> patterns = new ArrayList<>();
            for (String keyword : keywords) {
                patterns.add(Finder.createPattern(keyword));
            }
            return patterns;
        }

        protected boolean isLineContainsKeywords(String line) {
            for (Pattern keywordsPattern : keywordsPatterns) {
                if(keywordsPattern.matcher(line).find()){
                    return true;
                }
            }
            return false;
        }
    }
}
