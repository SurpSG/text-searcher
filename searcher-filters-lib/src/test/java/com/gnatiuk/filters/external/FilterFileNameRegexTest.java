package com.gnatiuk.filters.external;

import com.gnatiuk.filters.ATextFilterTest;
import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Created by Батинчук on 29.10.2015.
 */
public class FilterFileNameRegexTest extends ATextFilterTest {
    private FilterFileNameRegex filterFileNameRegex;
    private List<String> filesPatterns;

    private String[] keywords = new String[]{
            "[A-Za-z]*\\..*",
            ".*",
            "^.*\\.txt$"
    };
    private String[] badKeywords = new String[]{
            "[0-9]{1,}\\..*",
    };

    @Override
    protected ATextFilter getTextFilter() {
        return filterFileNameRegex;
    }

    public FilterFileNameRegexTest(){
        filesPatterns = Arrays.asList(keywords);
        filterFileNameRegex = new FilterFileNameRegex(filesPatterns);
    }


    @Test
    public void doFilterTest(){
        FilterFileNameRegex filterFileNameRegexSaved = filterFileNameRegex;
        for (String keyword : keywords) {
            filterFileNameRegex = new FilterFileNameRegex(Arrays.asList(keyword));
            super.doFilterTest();
        }

        for (String keyword : badKeywords) {
            filterFileNameRegex = new FilterFileNameRegex(Arrays.asList(keyword));
            assertFalse(getFilter().doFilter(testFile).getFilePath().equals(testFile.toPath()));
        }

        filterFileNameRegex = filterFileNameRegexSaved;
    }
}
