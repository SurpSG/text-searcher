package com.gnatiuk.searcher.core.filters;


import java.io.File;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public interface IFilter {

    public boolean  doFilter(File file);

    IFilter NONE_FILTER = file -> true;

}
