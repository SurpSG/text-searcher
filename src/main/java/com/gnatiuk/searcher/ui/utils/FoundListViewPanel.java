package com.gnatiuk.searcher.ui.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundListViewPanel extends JFXPanel {

    private int counter;

    private ListView<String> listView;
    private ObservableList<String> items;

    public FoundListViewPanel(){
        counter = 0;
        listView = new ListView<>();
        listView.setOrientation(Orientation.VERTICAL);
        items = FXCollections.observableArrayList();
        listView.setItems(items);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                StackPane root = new StackPane();
                root.getChildren().add(listView);
                Scene scene = new Scene(root, 400, 400);
                setScene(scene);
            }
        });
    }

    public void addItem(Object item){
        synchronized (items) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(++counter).append(") ").append(item.toString());
            items.add(stringBuilder.toString().toString());
        }
    }

    public void clear(){
        counter = 0;
        items.clear();
    }
}
