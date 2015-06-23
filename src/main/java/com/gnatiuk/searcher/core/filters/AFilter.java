package com.gnatiuk.searcher.core.filters;


import java.io.File;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public abstract class AFilter {

    public abstract boolean  doFilter(File file);

    public static AFilter build(){
        return null;
    }
}
