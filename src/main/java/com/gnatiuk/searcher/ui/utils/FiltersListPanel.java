package com.gnatiuk.searcher.ui.utils;


import com.gnatiuk.searcher.core.filters.IFilter;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

/**
 * Created by sgnatiuk on 7/20/15.
 */
public class FiltersListPanel extends JFXPanel {

    private ListView<String> filtersList;

    public FiltersListPanel(){
        filtersList = new ListView<>();

        Platform.runLater(() -> {
            StackPane root = new StackPane();
            root.getChildren().add(filtersList);
            Scene scene = new Scene(root, 400, 200);
            setScene(scene);
        });
    }

    public void addFilter(IFilter filter){
        Platform.runLater(() -> filtersList.getItems().add(filter.toString()));
    }

    public void clearFilters(){
        Platform.runLater(() -> filtersList.getItems().clear());
    }
}
