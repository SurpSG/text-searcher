package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.filters.FiltersContainer;
import com.gnatiuk.searcher.filters.external.*;
import com.gnatiuk.searcher.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.filters.internal.FilterFileReader;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import utils.SizeMeasure;

import javax.swing.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Sergiy on 7/4/2015.
 */
public class FinderTest {

    public static void main(String[] args) {

        String[] filePaths = {
                "/home",
        };

        String[] fileFiltersKeywordsArray = new String[]{
                ".java"
        };

        String[] fileFiltersRegexArray = new String[]{
//                "\\.java$",
                "\\.c$",
                "\\.cpp$",
                "\\.cc$",
                "\\.h$",
        };

        String[] fileFiltersRegexExcludeArray = new String[]{
//                "\\.txt$",
//                "\\.java$",
//                "\\.h$",
//                "\\.c$",
//                "\\.cc$",
//                "\\.cpp$",
        };

        String[] filterKeywordsArray = new String[]{
        };

        String[] filterKeywordsRegexArray = new String[]{
                "regex",
        };





        List<String> fileFiltersKeywords = Arrays.asList(fileFiltersKeywordsArray);
        List<String> fileFiltersRegex = Arrays.asList(fileFiltersRegexArray);
        List<String> fileFiltersRegexExclude = Arrays.asList(fileFiltersRegexExcludeArray);
        List<String> filterKeywords = Arrays.asList(filterKeywordsArray);
        List<String> filterKeywordsRegex = Arrays.asList(filterKeywordsRegexArray);






        FilterFileName filterFileNameCaseSensitive = new FilterFileName(fileFiltersKeywords);
        FilterFileName filterFileNameIgnoreCase = new FilterFileName(fileFiltersKeywords, ITextPreprocessor.LOWERCASE_PROCESSOR);

        FilterFileNameRegex filterFileNameRegexIgnoreCase = new FilterFileNameRegex(fileFiltersRegex,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegex filterFileNameRegexCaseSensitive = new FilterFileNameRegex(fileFiltersRegex);

        FilterFileNameRegexExclude filterFileNameRegexExcludeIgnoreCase = new FilterFileNameRegexExclude(fileFiltersRegexExclude,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegexExclude filterFileNameRegexExcludeCaseSensitive = new FilterFileNameRegexExclude(fileFiltersRegexExclude);



        FilterFileReader filterFileKeywordIgnoreCase = new FilterFileKeyword(filterKeywords, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordCaseSensitive = new FilterFileKeyword(filterKeywords);

        FilterFileReader filterFileKeywordRegexIgnoreCase = new FilterFileKeywordRegex(filterKeywordsRegex, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordRegexCaseSensitive = new FilterFileKeywordRegex(filterKeywordsRegex);
        FilterFileSize filterFileSize = new FilterFileSize(0, 1, SizeMeasure.MB);
        FilterFileDate filterFileDate = new FilterFileDate(new Date(99,1,1), new Date(System.currentTimeMillis()));

        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(filterFileNameCaseSensitive);
//        searchFilter.addFilter(filterFileNameIgnoreCase);

        searchFilter.addFilter(filterFileNameRegexIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexCaseSensitive);

//        searchFilter.addFilter(filterFileNameRegexExcludeIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexExcludeCaseSensitive);


        searchFilter.addFilter(filterFileKeywordIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordCaseSensitive);

//        searchFilter.addFilter(filterFileKeywordRegexIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordRegexCaseSensitive);
        searchFilter.addFilter(filterFileSize);

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                JOptionPane.showMessageDialog(null, "Done!");

                ThreadController.getInstance().shutdown();
            }
        });

        ThreadController.getInstance().registerThread(new SearcherHierarchyRunnable(Arrays.asList(filePaths), searchFilter));
        ThreadController.getInstance().start();
    }
}
