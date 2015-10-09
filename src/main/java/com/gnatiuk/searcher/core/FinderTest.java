package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.core.filters.internal.FilterFileReader;
import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.utils.WordsLibManager;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergiy on 7/4/2015.
 */
public class FinderTest {

    public static void main(String[] args) {

        String[] filePaths = {
                "/cryptfs/builds/sh10/487/buildroot/build_mips",
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
//                "\\.o$"
//                "\\.txt$",
//                "\\.prop"
        };

        String[] fileFiltersRegexExcludeArray = new String[]{
                "\\.o$",
//                "\\.txt$",
//                "\\.h$",
//                "\\.java$",
                "\\.patch$",
                "\\.xml$",
                "\\.class$",
                "\\.bz2$",
                "\\.lzma$",
                "\\.gz$",
                "\\.tgz$",
                "\\.a$",
//                "\\.c$",
//                "\\.cc$",
//                "\\.cpp$",
                "\\.so$",
                "\\.ko$",
                "\\.f$",
                "\\.dat$",
                "\\.d$",
                "\\.1$",
                "\\.tcc$",
                "\\.pl$",
                "\\.ada$",
                "\\.gch$",
                "\\.eps$",
                "\\.a_shipped",
                "\\.exp$",
                "\\.s$",
                "\\.m4a$",
                "\\.test$",
                "\\.dia$",
                "\\.squashfs$",
                "\\.a$",
                "\\.so$",
                "\\.status$",
                "\\.log$",
//                "\\.doc$",
//                "^[0-9]$"
        };

        String[] filterKeywordsArray = new String[]{
                "keyreaderthread",
        };

        String[] filterKeywordsArray1 = new String[]{
                "OSD62"
        };

        String[] filterKeywordsRegexArray = new String[]{
                "PLUGIN*.*MIN.*SW.*VERSION",
        };





        List<String> fileFiltersKeywords = Arrays.asList(fileFiltersKeywordsArray);
        List<String> fileFiltersRegex = Arrays.asList(fileFiltersRegexArray);
        List<String> fileFiltersRegexExclude = Arrays.asList(fileFiltersRegexExcludeArray);
        List<String> filterKeywords = Arrays.asList(filterKeywordsArray);
        List<String> filterKeywords1 = Arrays.asList(filterKeywordsArray1);
        List<String> filterKeywordsRegex = Arrays.asList(filterKeywordsRegexArray);






        FilterFileName filterFileNameCaseSensitive = new FilterFileName(fileFiltersKeywords);
        FilterFileName filterFileNameIgnoreCase = new FilterFileName(fileFiltersKeywords,ITextPreprocessor.LOWERCASE_PROCESSOR);

        FilterFileNameRegex filterFileNameRegexIgnoreCase = new FilterFileNameRegex(fileFiltersRegex,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegex filterFileNameRegexCaseSensitive = new FilterFileNameRegex(fileFiltersRegex);

        FilterFileNameRegexExclude filterFileNameRegexExcludeIgnoreCase = new FilterFileNameRegexExclude(fileFiltersRegexExclude,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegexExclude filterFileNameRegexExcludeCaseSensitive = new FilterFileNameRegexExclude(fileFiltersRegexExclude);



        FilterFileReader filterFileKeywordIgnoreCase = new FilterFileKeyword(filterKeywords, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordIgnoreCase1 = new FilterFileKeyword(filterKeywords1, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordCaseSensitive = new FilterFileKeyword(filterKeywords);
        FilterFileReader filterFileKeywordCaseSensitive1 = new FilterFileKeyword(filterKeywords1);

        FilterFileReader filterFileKeywordRegexIgnoreCase = new FilterFileKeywordRegex(filterKeywordsRegex, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordRegexCaseSensitive = new FilterFileKeywordRegex(filterKeywordsRegex);

        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(filterFileNameCaseSensitive);
//        searchFilter.addFilter(filterFileNameIgnoreCase);

        searchFilter.addFilter(filterFileNameRegexIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexCaseSensitive);

//        searchFilter.addFilter(filterFileNameRegexExcludeIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexExcludeCaseSensitive);


        searchFilter.addFilter(filterFileKeywordIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordCaseSensitive);
//        searchFilter.addFilter(filterFileKeywordIgnoreCase1);

//        searchFilter.addFilter(filterFileKeywordRegexIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordRegexCaseSensitive);

        System.out.println(Arrays.asList(filePaths));
        for (IFilter filter : searchFilter.getFilters()) {
            System.out.println(filter);
        }

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                JOptionPane.showMessageDialog(null, "Done!");

                ThreadController.getInstance().shutdown();
            }
        });

        ThreadController.getInstance().registerThread(new SearcherHierarchyRunnable(Arrays.asList(filePaths), searchFilter));
//        ThreadController.getInstance().start();

        WordsLibManager.getInstance().getKeywords();
    }
}
