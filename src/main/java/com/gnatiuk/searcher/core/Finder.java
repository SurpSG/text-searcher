package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */

import com.gnatiuk.searcher.core.filters.*;
import com.gnatiuk.searcher.core.filters.internal_file_filter.FilterFileKeywordIgnoreCase;
import com.gnatiuk.searcher.core.filters.internal_file_filter.FilterFileKeywordRegexIgnoreCase;
import com.gnatiuk.searcher.core.filters.internal_file_filter.FilterFileReader;
import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;

import java.util.Arrays;
import java.util.List;

public class Finder {

    private List<String> filePaths;

    public static long t1;

    private IFilter filter;

    public Finder(List<String> filePaths, IFilter filter){
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

                "/home/sgnatiuk/Downloads",
//                "/home/sgnatiuk/Documents",

//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local/bist/boot/",
//                "/cryptfs/builds/sh10/build_directv_sh10/",
                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/local",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/",
//                "/cryptfs/builds/sh10/build_directv_sh10/",
//                "/cryptfs/builds/sh10/build_directv_sh10/buildroot/hddvr_image/hdstb_hum_sh10"
//                "/home/sgnatiuk/Downloads/logs",
        };

        List<String> fileFiltersKeywords = Arrays.asList("");
        List<String> fileFiltersRegex = Arrays.asList("\\.c$"/*".*java$","\\.c$","\\.cpp$","\\.txt$","\\.prop"*/);

        List<String> filterKeywords = Arrays.asList("bist_main:");
        List<String> filterKeywordsRegex = Arrays.asList("Failed.*to.*mount.*flash.*retrying");


        FilterFileName filterFileName = new FilterFileName(fileFiltersKeywords);
        FilterFileNameRegex filterFileNameRegex = new FilterFileNameRegex(fileFiltersRegex);
        FilterFileReader filterKeyword = new FilterFileKeywordIgnoreCase(filterKeywords);
        FilterFileReader filterFileKeywordRegexIgnoreCase = new FilterFileKeywordRegexIgnoreCase(filterKeywordsRegex);

        FiltersContainer searchFilter = new FiltersContainer();
//        searchFilter.addFilter(filterFileNameRegex);
//        searchFilter.addFilter(filterFileName);
//        searchFilter.addFilter(filterKeyword);
        searchFilter.addFilter(filterFileKeywordRegexIgnoreCase);

        Finder finder = new Finder(Arrays.asList(filePaths), searchFilter);

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                System.out.println("done!!!!!");

            }
        });

        t1 = System.currentTimeMillis();
        finder.start();
    }
}
