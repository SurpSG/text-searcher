package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */

import com.gnatiuk.searcher.core.filters.FilterFileName;
import com.gnatiuk.searcher.core.filters.FilterKeyword;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.FiltersContainer;
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
//                    "E:\\downloads",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/druid",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips",
//                "/home/sgnatiuk/Documents/temp",
//                "/cryptfs/builds/sh20/scripts/",
//                "/cryptfs/builds/sh20/root_pac/build_mips",
                "/home/sgnatiuk/Downloads",
//                "/home/sgnatiuk/Documents",
        };

        List<String> fileFilters = Arrays.asList(".*Test.*java$"/*,"\\.java$"/*,"\\.c$"*/);


        String fileNameKeyword = "flash";

        FilterFileName filterFileName = new FilterFileName(Arrays.asList(fileNameKeyword));
        FilterKeyword filterKeyword = new FilterKeyword(Arrays.asList("flash1"));

        FiltersContainer searchFilter = new FiltersContainer();
        searchFilter.addFilter(filterFileName);
        searchFilter.addFilter(filterKeyword);

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
