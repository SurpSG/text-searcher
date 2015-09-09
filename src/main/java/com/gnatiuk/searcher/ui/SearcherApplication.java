package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.ui.utils.FilesTreePanel;
import com.gnatiuk.searcher.ui.utils.FloatPanelWrapper;
import com.gnatiuk.searcher.ui.utils.FoundTreePanel;
import com.gnatiuk.searcher.ui.utils.filters.components.SearchFiltersContainerComponent;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Created by surop on 08.09.15.
 */
public class SearcherApplication extends Application{


    private Button runButton;
    private FilesTreePanel filesTreePanel;
    private FoundTreePanel foundListViewPanel;
    private Pane leftPanel;
    private Pane rightPanel;
    private FloatPanelWrapper foundFloatPanel;
    private StackPane layeredPane;

    private SplitPane splitPane;


    private SearchFiltersContainerComponent filtersContainer;


    @Override
    public void start(Stage primaryStage) throws Exception {

        filtersContainer = new SearchFiltersContainerComponent();

        filesTreePanel = new FilesTreePanel();

        runButton = new Button("Run");

        leftPanel = new VBox();
        Pane searchCriteriaComponentsPane = filtersContainer.getSearchCriteriaComponentsPane();
        searchCriteriaComponentsPane.setMaxWidth(Double.MAX_VALUE);
        leftPanel.getChildren().addAll(searchCriteriaComponentsPane, runButton);

        rightPanel = new VBox();
        rightPanel.getChildren().add(filesTreePanel.getTreeNode());

        splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPanel, rightPanel);

        foundListViewPanel = new FoundTreePanel();
        foundFloatPanel = new FloatPanelWrapper(
                foundListViewPanel.getTreeView());

        layeredPane = new StackPane();
        Pane floatPanel = foundFloatPanel.getFloatPanel();
        floatPanel.setMaxHeight(150);
        StackPane.setAlignment(floatPanel, Pos.BOTTOM_CENTER);
        layeredPane.getChildren().add(splitPane);
        layeredPane.getChildren().add(floatPanel);


        Scene scene = new Scene(layeredPane,300,300);
        scene.setFill(Color.GHOSTWHITE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}