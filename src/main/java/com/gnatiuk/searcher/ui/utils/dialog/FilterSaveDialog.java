package com.gnatiuk.searcher.ui.utils.dialog;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.FilterFileUtils;
import javafx.scene.control.SelectionMode;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class FilterSaveDialog extends FilterFileControlDialog {

    private static final String CONTROL_BUTTON_NAME = "Save";

    private IFilter filter;
    private String savedFilterName;

    public FilterSaveDialog(IFilter filter) {
        this.filter = filter;
        filesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    protected String getControlButtonName() {
        return CONTROL_BUTTON_NAME;
    }

    @Override
    protected void onControlButtonAction() {
        savedFilterName = textField.getText();
        FilterFileUtils.saveFilterWithName(savedFilterName, filter);
    }

    public String getSavedFilterName() {
        return savedFilterName;
    }
}
