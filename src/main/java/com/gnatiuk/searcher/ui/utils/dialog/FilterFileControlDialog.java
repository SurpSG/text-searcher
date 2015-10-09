package com.gnatiuk.searcher.ui.utils.dialog;

import com.gnatiuk.searcher.ui.utils.AutoCompleteTextField;
import com.gnatiuk.searcher.utils.AppOptions;
import com.gnatiuk.searcher.utils.FilterFileUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public abstract class FilterFileControlDialog extends FilesControlDialog {

    protected Button fileControlButton;
    protected TextField textField;

    public FilterFileControlDialog(){
        initControlButton();
        textField = new AutoCompleteTextField(filesListView.getItems());
//        filesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    protected void buildDialogGraphics(){
        rootPane.getChildren().add(textField);
        super.buildDialogGraphics();
        rootPane.getChildren().add(fileControlButton);
    }

    private void initControlButton(){
        fileControlButton = new Button(getControlButtonName());
        fileControlButton.setMaxWidth(Double.MAX_VALUE);

        fileControlButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onControlButtonAction();
                dialog.close();
            }
        });
    }

    @Override
    protected String processFileName(String fileName) {
        String processedFileName = FilterFileUtils.extractFilterName(fileName);
        return processedFileName != null ? processedFileName : fileName;
    }

    @Override
    protected List<String> getAllowedFileExtensions() {
        return Arrays.asList(AppOptions.FILTER_FILE_EXTENSION);
    }

    @Override
    protected String getFilesSaveDir() {
        return AppOptions.FILTERS_SAVE_DIR_PATH;
    }

    protected abstract String getControlButtonName();
    protected abstract void onControlButtonAction();
}
