package com.gnatiuk.searcher.ui.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundListViewPanel extends JFXPanel {

    private ListView<String> listView;
    private ObservableList<String> items;
    private FileOpener fileOpener;

    public FoundListViewPanel(){
        listView = new ListView<>();
        fileOpener = new FileOpener();
        listView.setOrientation(Orientation.VERTICAL);
        items = FXCollections.observableArrayList();
        listView.setItems(items);
        addDoubleClickListener();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                StackPane root = new StackPane();
                root.getChildren().add(listView);
                Scene scene = new Scene(root, 400, 300);
                setScene(scene);
            }
        });
    }

    public void addItem(Object item){
        synchronized (items) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(++counter).append(") ").append(item.toString());
//            items.add(stringBuilder.toString().toString());
            items.add(item.toString());
        }
    }

    public void clear(){
        items.clear();
    }

    private void addDoubleClickListener(){
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    File file = new File(listView.getSelectionModel().getSelectedItem());

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
