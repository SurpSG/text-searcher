package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.core.filters.internal.FilterFileReader;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergiy on 7/4/2015.
 */
public class FinderTest {

    public static void main(String[] args) {

        String[] filePaths = {
//                "/cryptfs/builds/sh20/root_pac/build_mips",
//                "/cryptfs/builds/sh20/scripts/",

//                "/home/sgnatiuk/Downloads",
//                "/home/sgnatiuk/Documents",

//                "/cryptfs/builds/sh10/build_directv_sh10/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local/bist/boot/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local/",
//                "/cryptfs/builds/sh20/root_pac/build_mips",
                "E:\\study",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/hddvr_image/tmp",
//                "/cryptfs/builds/sh10/build_directv_sh10/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/hddvr_image/hdstb_hum_sh10"
//                "/home/sgnatiuk/Downloads/logs",
        };

        String[] fileFiltersKeywordsArray = new String[]{
                "."
        };

        String[] fileFiltersRegexArray = new String[]{
//                "flash.*\\.c$"
//                ".*java$",
//                "\\.c$",
//                "\\.cpp$",
//                "\\.o$"
                "\\.txt$",
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
                "\\.f$",
                "\\.dat$",
                "\\.d$",
                "\\.1$",
                "\\.tcc$",
                "\\.pl$",
                "\\.ada$",
                "\\.gch$",
                "\\.eps$",
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
//                "bcdi",
//                "audioconnection",
//                "de guia",
                "LABEL",
//                "S0710_3",
//                "+=9",
//                "+= 9",
//                "Nas N Did Wevay",
//                "Nas N Did",
//                "kernel",
//                "exception"
        };

        String[] filterKeywordsRegexArray = new String[]{
//                "(Invalid.*argument)*.*failed:.*(Invalid.*argument)*",
//                "(\"Running[ ])[ ]*(%s){1,}.*(test)*",
//                "\"Mounting[ ]*(%s)*(writable)*.*(flash)*"
                "kernel.*panic"
        };





        List<String> fileFiltersKeywords = Arrays.asList(fileFiltersKeywordsArray);
        List<String> fileFiltersRegex = Arrays.asList(fileFiltersRegexArray);
        List<String> fileFiltersRegexExclude = Arrays.asList(fileFiltersRegexExcludeArray);
        List<String> filterKeywords = Arrays.asList(filterKeywordsArray);
        List<String> filterKeywordsRegex = Arrays.asList(filterKeywordsRegexArray);






        FilterFileName filterFileNameCaseSensitive = new FilterFileName(fileFiltersKeywords);
        FilterFileName filterFileNameIgnoreCase = new FilterFileName(fileFiltersKeywords,ITextPreprocessor.LOWERCASE_PROCESSOR);

        FilterFileNameRegex filterFileNameRegexIgnoreCase = new FilterFileNameRegex(fileFiltersRegex,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegex filterFileNameRegexCaseSensitive = new FilterFileNameRegex(fileFiltersRegex);

        FilterFileNameRegexExclude filterFileNameRegexExcludeIgnoreCase = new FilterFileNameRegexExclude(fileFiltersRegexExclude,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegexExclude filterFileNameRegexExcludeCaseSensitive = new FilterFileNameRegexExclude(fileFiltersRegexExclude);



        FilterFileReader filterFileKeywordIgnoreCase = new FilterFileKeyword(filterKeywords, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordCaseSensitive = new FilterFileKeyword(filterKeywords);

        FilterFileReader filterFileKeywordRegexIgnoreCase = new FilterFileKeywordRegex(filterKeywordsRegex, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader filterFileKeywordRegexCaseSensitive = new FilterFileKeywordRegex(filterKeywordsRegex);

        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(filterFileNameCaseSensitive);
//        searchFilter.addFilter(filterFileNameIgnoreCase);

//        searchFilter.addFilter(filterFileNameRegexIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexCaseSensitive);

//        searchFilter.addFilter(filterFileNameRegexExcludeIgnoreCase);
//        searchFilter.addFilter(filterFileNameRegexExcludeCaseSensitive);


        searchFilter.addFilter(filterFileKeywordIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordCaseSensitive);

//        searchFilter.addFilter(filterFileKeywordRegexIgnoreCase);
//        searchFilter.addFilter(filterFileKeywordRegexCaseSensitive);

        Finder finder = new Finder(Arrays.asList(filePaths), searchFilter);

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                System.out.println("done!!!!!");
                JOptionPane.showMessageDialog(null, "Done!");

            }
        });

        Finder.t1 = System.currentTimeMillis();
        finder.start();
    }
}
