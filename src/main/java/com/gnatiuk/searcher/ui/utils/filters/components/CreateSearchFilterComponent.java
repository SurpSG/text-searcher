package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.IFilter;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;

/**
 * Created by surop on 04.08.15.
 */
public class CreateSearchFilterComponent extends ASearchFilterComponent {

    public static final String NAME = "Create new filter";

    @Override
    public IFilter buildFilter() {
        return null;
    }

    @Override
    public Pane getSearchCriteriaComponentsPane() {
        return null;
    }

    @Override
    public Control getControl() {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
