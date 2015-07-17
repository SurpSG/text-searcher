package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.core.utils.FileSearchEvent;
import com.gnatiuk.searcher.core.utils.FoundOption;
import com.gnatiuk.searcher.core.utils.SearchOption;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundTreePanel extends JFXPanel {

    private TreeItem<String> rootNode;
    private TreeView<String> treeView;

    private FileOpener fileOpener;

    public FoundTreePanel(){
        rootNode = new TreeItem<>("Results");
        rootNode.setExpanded(true);
        treeView = new TreeView<>(rootNode);

        fileOpener = new FileOpener();
        addDoubleClickListener();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                StackPane root = new StackPane();
                root.getChildren().add(treeView);
                Scene scene = new Scene(root, 400, 300);
                setScene(scene);
            }
        });
    }

    public synchronized void addItem(FileSearchEvent item){

        TreeItem<String> foundFile = new TreeItem<>(item.getFilePath().toString());
        for (SearchOption searchOption : item.getSearchOptions()) {
            foundFile.getChildren().add(new TreeItem<>(searchOption.getFoundOption()+": "+searchOption.getFoundValue()));
        }
        rootNode.getChildren().add(foundFile);
    }

    public void clear(){
        rootNode.getChildren().clear();
    }

    private void addDoubleClickListener(){
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
                    if(treeItem.getParent() != rootNode){
                        return;
                    }
                    File file = new File(treeItem.getValue());

                    if(fileOpener.isSupported()){
                        fileOpener.openFile(file);
                    }
                }
            }
        });
    }


    private class FileOpener{

        private Desktop desktop;
        private boolean supported;

        public FileOpener(){
            desktop = Desktop.getDesktop();
            supported = Desktop.isDesktopSupported();
        }

        public boolean isSupported(){
            return supported;
        }

        public void openFile(File file){

            if(!supported){
                return;
            }

            if(!file.exists()){
                throw new RuntimeException(String.format("File %s does not exist. check data that comes from listView!!!"));
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        desktop.open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
