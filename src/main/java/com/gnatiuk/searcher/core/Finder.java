package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */

import com.gnatiuk.searcher.core.filters.*;
import com.gnatiuk.searcher.core.filters.internal_file_filter.*;
import com.gnatiuk.searcher.core.filters.external_file_filter.*;
import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Finder {

    private List<String> filePaths;

    public static long t1;

    private AFilter filter;

    public Finder(List<String> filePaths, AFilter filter){
        this.filter = filter;
        this.filePaths = filePaths;
    }

    public void start(){
        ThreadController.getInstance().registerThread(new SearcherHierarchyRunnable(filePaths, filter));
    }

    public static void main(String[] args) {

        String[] filePaths = {
//                "/cryptfs/builds/sh20/root_pac/build_mips",
//                "/cryptfs/builds/sh20/scripts/",

//                "/home/sgnatiuk/Downloads",
//                "/home/sgnatiuk/Documents",

//                "/cryptfs/builds/sh10/build_directv_sh10/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local/bist/boot/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local/",
                "/cryptfs/builds/sh20/root_pac/build_mips",
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
                "segn11111111111",
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


        FilterFileName filterFileName = new FilterFileName(fileFiltersKeywords);
        FilterFileNameRegex filterFileNameRegex = new FilterFileNameRegex(fileFiltersRegex);
        FilterFileNameRegexExclude filterFileNameRegexExclude = new FilterFileNameRegexExclude(fileFiltersRegexExclude);


        FilterFileReader filterFileKeywordIgnoreCase = new FilterFileKeywordIgnoreCase(filterKeywords);
        FilterFileReader filterFileKeywordCaseSensitive = new FilterFileKeywordCaseSensitive(filterKeywords);
        FilterFileReader filterFileKeywordRegexIgnoreCase = new FilterFileKeywordRegexIgnoreCase(filterKeywordsRegex);
        FilterFileReader filterFileKeywordRegexCaseSensitive = new FilterFileKeywordRegexCaseSensitive(filterKeywordsRegex);

        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(filterFileName);
//        searchFilter.addFilter(filterFileNameRegex);
//        searchFilter.addFilter(filterFileNameRegexExclude);

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

        t1 = System.currentTimeMillis();
        finder.start();
    }
}
