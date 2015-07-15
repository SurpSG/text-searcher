package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.core.utils.FileFoundEvent;
import com.gnatiuk.searcher.core.utils.FoundOption;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundTreePanel extends JFXPanel {

    private TreeItem<String> rootNode;
    private TreeView<String> treeView;

    private ListView<String> listView;
    private ObservableList<String> items;
    private FileOpener fileOpener;

    public FoundTreePanel(){
        rootNode = new TreeItem<>("Results");
        rootNode.setExpanded(true);
        treeView = new TreeView<>(rootNode);



//        listView = new ListView<>();
        fileOpener = new FileOpener();
//        listView.setOrientation(Orientation.VERTICAL);
//        items = FXCollections.observableArrayList();
//        listView.setItems(items);
//        addDoubleClickListener();

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

    public synchronized void addItem(FileFoundEvent item){
        TreeItem<String> treeItem = new TreeItem<>(item.getFilePath().toString());
        Map<FoundOption, String> foundOption = item.getSearchOptions();
        System.out.println(foundOption);
        for (FoundOption option : foundOption.keySet()) {
            System.out.println(option+" "+foundOption.get(option));
            treeItem.getChildren().add(new TreeItem<>(foundOption.get(option)));
        }
        rootNode.getChildren().add(treeItem);
    }

    public void clear(){
        rootNode.getChildren().clear();
    }

    private void addDoubleClickListener(){
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    File file = new File(listView.getSelectionModel().getSelectedItem());

                    if (fileOpener.isSupported()) {
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
