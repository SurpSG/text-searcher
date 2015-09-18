package com.gnatiuk.searcher.ui.utils.dialog;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public abstract class FilesControlDialog {

    protected Dialog<String> dialog;
    protected ListView<String> filesListView;
    protected Pane rootPane;

    public FilesControlDialog(){
        rootPane = buildRootPane();
        filesListView = new ListView<>();
        loadSavedFiles();
    }

    protected Pane buildRootPane(){
        return new VBox();
    }

    protected void buildDialogGraphics(){
        rootPane.getChildren().add(filesListView);
    }

    public void show(){
        buildDialogObject();
        dialog.show();
    }

    public void showAndWait(){
        buildDialogObject();
        dialog.showAndWait();
    }

    private void buildDialogObject(){
        buildDialogGraphics();
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.getDialogPane().lookupButton(closeButton).setDisable(false);

        dialog.setGraphic(rootPane);
        dialog.initStyle(StageStyle.UTILITY);
    }

    protected void loadSavedFiles(){
        List<String> allowedFileExtensions = getAllowedFileExtensions();
        for (String file :new File(getFilesSaveDir()).list() ) {
            for (String allowedFileExtension : allowedFileExtensions) {
                if(file.endsWith(allowedFileExtension)){
                    filesListView.getItems().add(file);
                }
            }
        }
    }

    protected abstract List<String> getAllowedFileExtensions();
    protected abstract String getFilesSaveDir();
}
