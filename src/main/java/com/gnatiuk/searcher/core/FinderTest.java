package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.core.filters.internal.FilterFileReader;
import com.gnatiuk.searcher.core.utils.FileFoundEvent;
import com.gnatiuk.searcher.core.utils.IFileFoundListener;
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

//                "/home/sgnatiuk/Downloads",
//                "/home/sgnatiuk/Documents",
//                "/cryptfs/builds/sh20/root_pac/build_mips",
//                "/cryptfs/builds/sh10/sh10_back/build_directv_sh10/buildroot/build_mips",
                "/cryptfs/builds/sh10/sh10_back/build_directv_sh10/buildroot/build_mips/middleware_core",

        };

        String[] fileNameFilter = new String[]{
                "UpdateListObject.cpp",
//                "UpdateListO"
        };

        String[] fileNameFilterRegex = new String[]{
//                "flash.*\\.c$"
                "\\.c$",
                "\\.cpp$",
                "\\.cc$",
//                "\\.log",
//                "\\.o$"
//                "\\.txt$",
//                "\\.prop"
        };

        String[] fileNameFilterRegexExclude = new String[]{
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

        String[] keywordsFilter = new String[]{
//                "bcdi",
//                "audioconnection",
//                "de guia",
                "dst",
//                " Off-air scan",
//                "UpdateListObject",
//                "S0710_3",
//                "+=9",
//                "+= 9",
//                "Nas N Did Wevay",
//                "Nas N Did",
//                "kernel",
//                "exception"
        };

        String[] keywordsFilterRegex = new String[]{
//                "(Invalid.*argument)*.*failed:.*(Invalid.*argument)*",
//                "(\"Running[ ])[ ]*(%s){1,}.*(test)*",
//                "\"Mounting[ ]*(%s)*(writable)*.*(flash)*"
                "dst"
        };





        List<String> fileFiltersKeywords = Arrays.asList(fileNameFilter);
        List<String> fileFiltersRegex = Arrays.asList(fileNameFilterRegex);
        List<String> fileFiltersRegexExclude = Arrays.asList(fileNameFilterRegexExclude);
        List<String> filterKeywords = Arrays.asList(keywordsFilter);
        List<String> filterKeywordsRegex = Arrays.asList(keywordsFilterRegex);






        FilterFileName fileNameCS = new FilterFileName(fileFiltersKeywords);
        FilterFileName fileNameIC = new FilterFileName(fileFiltersKeywords,ITextPreprocessor.LOWERCASE_PROCESSOR);

        FilterFileNameRegex fileNameRegexIC = new FilterFileNameRegex(fileFiltersRegex,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegex fileNameRegexCS = new FilterFileNameRegex(fileFiltersRegex);

        FilterFileNameRegexExclude fileNameRegexExcludeIC = new FilterFileNameRegexExclude(fileFiltersRegexExclude,ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileNameRegexExclude fileNameRegexExcludeCS = new FilterFileNameRegexExclude(fileFiltersRegexExclude);



        FilterFileReader keywordIC = new FilterFileKeyword(filterKeywords, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader keywordCS = new FilterFileKeyword(filterKeywords);

        FilterFileReader keywordRegexIC = new FilterFileKeywordRegex(filterKeywordsRegex, ITextPreprocessor.LOWERCASE_PROCESSOR);
        FilterFileReader keywordRegexCS = new FilterFileKeywordRegex(filterKeywordsRegex);





        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(fileNameCS);
//        searchFilter.addFilter(fileNameIC);

//        searchFilter.addFilter(fileNameRegexIC);
        searchFilter.addFilter(fileNameRegexCS);

//        searchFilter.addFilter(fileNameRegexExcludeIC);
//        searchFilter.addFilter(fileNameRegexExcludeCS);


        searchFilter.addFilter(keywordIC);
//        searchFilter.addFilter(keywordCS);

//        searchFilter.addFilter(keywordRegexIC);
//        searchFilter.addFilter(keywordRegexCS);

        Finder finder = new Finder(Arrays.asList(filePaths), searchFilter);

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                System.out.println("done!!!!!");
                JOptionPane.showMessageDialog(null, "Done!");
                ThreadController.getInstance().shutdown();
            }
        });

        ThreadController.getInstance().registerFileFoundListener(new IFileFoundListener() {
            @Override
            public void alertFileFound(FileFoundEvent fileFoundEvent) {
                if (fileFoundEvent != FileFoundEvent.NOT_FOUND) {
//                    System.out.println(fileFoundEvent);
                }
            }
        });

        Finder.t1 = System.currentTimeMillis();
        finder.start();
    }
}
