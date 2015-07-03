package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */

import com.gnatiuk.searcher.core.filters.*;
import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;

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

}
