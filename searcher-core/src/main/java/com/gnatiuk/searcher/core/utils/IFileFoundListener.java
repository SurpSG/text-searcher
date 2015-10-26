package com.gnatiuk.searcher.core.utils;

import com.gnatiuk.searcher.filters.util.FileSearchEvent;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public interface IFileFoundListener {

    void alertFileFound(FileSearchEvent fileSearchEvent);
}
