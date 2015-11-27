package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.runnable.SearchController;
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
//                "/cryptfs/builds/sh10/490/buildroot/build_mips",
//                "/cryptfs/builds/sh10/490_v4/buildroot/build_mips/",
//                "/cryptfs/builds/sh10/490_v4/scripts/",
//                "/cryptfs/builds/sh10/486/buildroot/build_mips/",
//
//
//                "/cryptfs/builds/sh20-merge/original/scripts/",
//                "/cryptfs/builds/sh20-merge/original/buildroot/build_mips",
//                "/cryptfs/builds/sh20-merge/clean_3/buildroot",

//                "/cryptfs/builds/sh10/490_v1_orig/buildroot/dl",





//                "/cryptfs/builds/sh10/490_v1_clean/build_scripts/root_pac/build_mips/",
//                "/cryptfs/builds/sh10/490_v1_orig",


//                "/cryptfs/builds/sh10/490_v1_orig/buildroot/",
//                "/cryptfs/builds/sh10/490_v1_orig/scripts/",
//                "/cryptfs/builds/sh10/490_v1_orig/buildroot/package",
//                "/cryptfs/builds/sh10/490_v1_orig/buildroot/build_mips/",

//                "/cryptfs/builds/sh10/root20scriptMerge/root_pac/build_mips",
//

//                "/cryptfs/builds/sh10/490_v1_clean/build_scripts/root_pac/build_mips",
//                "/cryptfs/builds/sh10/490_v1_clean/build_scripts/scripts_sh10",
//                "/cryptfs/builds/sh10/490_v1_clean/build_scripts/scripts_sh20",
                "/cryptfs/builds/sh10/root20scriptMerge/merge-buildroot/10root_pac"

        };

        String[] fileFiltersKeywordsArray = new String[]{
//                "IDtcpInfo.h",
                "sh-10",
                "sh10",
        };

        String[] fileFiltersRegexArray = new String[]{
//                "\\.log",
//                "\\.xml$",
//                "\\.sh",
//                "\\.sh$",
//                "\\.c$",
//                "\\.sh$",
//                "\\.cpp$",
//                "\\.cc$",
//                "\\.h$",
//                "\\.xml$",
//                "^ProgressTestRegistrator.java$",
//                "^EventManager.java$",
        };

        String[] fileFiltersRegexExcludeArray = new String[]{
//                "\\.so$",
//                "\\.so.1$",
//                "\\.cache$",
//                "\\.o$",
//                "\\.log$",
//                "\\.a$",
//                "\\.tgz$",
//                "\\.ko$",
//                "\\.jar$",
//                "\\.patch$",
//                "\\.i$",
                "\\.bz2$",
                "\\.gz$",
                "Tag$",
                "Entries$",
                "Repository$",
//                "\\.txt$",
//                "\\.java$",
//                "\\.h$",
//                "\\.c$",
//                "\\.cc$",
//                "\\.cpp$",
        };

        String[] filterKeywordsArray = new String[]{
//                "Parallel mksquashfs",
//                "Use fakeroot to pretend to create all needed device nodes",
//                "se fakeroot to munge permissions and do root-like things",
//                "se fakeroot to munge permissions and do root-like things",
                "sh10",
                "sh-10",
                "h20",
        };

        String[] filterKeywordsRegexArray = new String[]{
//                "[^A-Za-z]dlSurfaceRequest[^A-Za-z]",
//                "#define.*[^A-Z]shr2",
//                "#define.*[^A-Z]shr",
                "sh10",
                "sh-10",
                "[^a-z]h20",
//                "#define.*h10",
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

//        searchFilter.addFilter(filterFileNameRegexIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexCaseSensitive);

//        searchFilter.addFilter(filterFileNameRegexExcludeIgnoreCase);
        searchFilter.addFilter(filterFileNameRegexExcludeCaseSensitive);


//        searchFilter.addFilter(filterFileKeywordIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordCaseSensitive);

        searchFilter.addFilter(filterFileKeywordRegexIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordRegexCaseSensitive);
//        searchFilter.addFilter(filterFileSize);

        System.out.println(Arrays.toString(filePaths));
        System.out.println(searchFilter);

        SearchController.getInstance().registerWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                SearchController.getInstance().shutdown();
                JOptionPane.showMessageDialog(null, "Done");
            }
        });

        SearchController.getInstance().setPathsToSearch(Arrays.asList(filePaths));
        SearchController.getInstance().setFilter(searchFilter);
        SearchController.getInstance().startSearching();
    }
}


/*


 */