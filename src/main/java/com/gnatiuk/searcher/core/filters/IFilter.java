package com.gnatiuk.searcher.core.filters;


import com.gnatiuk.searcher.core.utils.FileFoundEvent;

import java.io.File;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public interface IFilter {

    FileFoundEvent doFilter(File file);

    IFilter NONE_FILTER = file -> new FileFoundEvent(file);

}
