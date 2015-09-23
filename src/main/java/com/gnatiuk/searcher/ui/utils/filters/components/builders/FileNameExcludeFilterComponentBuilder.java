package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.ui.utils.filters.components.ASearchTextFilterComponent;
import com.gnatiuk.searcher.ui.utils.filters.components.FileNameExcludeFilterComponent;

/**
 * Created by sgnatiuk on 9/23/15.
 */
public class FileNameExcludeFilterComponentBuilder extends TextSearchComponentBuilder {

    @Override
    protected ASearchTextFilterComponent buildDefault() {
        return new FileNameExcludeFilterComponent();
    }
}
