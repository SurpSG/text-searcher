package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.runnable.SearchCotroller;
import com.gnatiuk.searcher.core.utils.IFileFoundListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import com.gnatiuk.searcher.ui.utils.FilesTreePanel;
import com.gnatiuk.searcher.ui.utils.FloatPanelWrapper;
import com.gnatiuk.searcher.ui.utils.FoundTreePanel;
import com.gnatiuk.searcher.ui.utils.OnBootManager;
import com.gnatiuk.searcher.ui.utils.filters.components.SearchFiltersContainerComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.RecentPathsManager;

import java.util.List;


/**
 * Created by surop on 08.09.15.
 */
public class SearcherApplication extends Application{


    private Button runButton;
    private Button pauseButton;
    private Button stopButton;
    private Button resumeButton;
    private FilesTreePanel filesTreePanel;
    private Pane leftPanel;
    private Pane rightPanel;
    private FloatPanelWrapper foundFloatPanel;
    private StackPane layeredPane;


    private SplitPane splitPane;
    private ToolBar toolBar;

    private VBox root;


    private SearchFiltersContainerComponent filtersContainer;
    private FoundTreePanel foundListViewPanel;


    @Override
    public void start(Stage primaryStage) throws Exception {

        createRootPane();
        createLayeredPane();
        createToolBar();

        createSplitPane();
        createFilesTreePane();
        createFiltersPane();
        createHandleSearchButtons();
        createFoundPane();

        createScene(primaryStage);

        createOnSearchCompleteAction();
        createOnFileFoundAction();
    }

    private void createScene(Stage primaryStage){
        Scene scene = new Scene(root,800,600);
        scene.setFill(Color.GHOSTWHITE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("");
        primaryStage.show();
    }

    private void createOnFileFoundAction(){
        SearchCotroller.getInstance().registerFileFoundListener(new IFileFoundListener() {
            @Override
            public void alertFileFound(FileSearchEvent fileFoundEvent) {
                if (fileFoundEvent != FileSearchEvent.NOT_FOUND) {
                    foundListViewPanel.addItem(fileFoundEvent);
                    if (!foundFloatPanel.isVisible()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                foundFloatPanel.show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void createOnSearchCompleteAction(){
        SearchCotroller.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Searching is finished");
                        alert.setHeaderText(null);
                        alert.show();

                        runButton.setDisable(false);
                        resumeButton.setDisable(true);
                        stopButton.setDisable(true);
                        pauseButton.setDisable(true);
                    }
                });

            }
        });
    }

    private void createFiltersPane(){
        filtersContainer = new SearchFiltersContainerComponent();
        filtersContainer.addFilterComponents(OnBootManager.getOnLoadSearchComponents());
        Node searchCriteriaComponentsPane = filtersContainer.getFiltersContainer();
        leftPanel.getChildren().addAll(searchCriteriaComponentsPane);
    }

    private void createHandleSearchButtons(){
        runButton = new Button("Run");
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                foundListViewPanel.clear();

                IFilter filter = filtersContainer.buildFilter();
                if(filter == IFilter.NONE_FILTER){
                    //TODO inform user that there is no filter
                    System.err.println("there is no filter");
                    return;
                }

                List<String> pathsToSearch = filesTreePanel.getSelectedPaths();
                if(pathsToSearch.isEmpty()){
                    //TODO inform user that there is no path is selected
                    System.err.println("there is no path is selected");
                    return;
                }

                RecentPathsManager.getInstance().saveWords(pathsToSearch);

                SearchCotroller.getInstance().setPathsToSearch(pathsToSearch);
                SearchCotroller.getInstance().setFilter(filter);
                SearchCotroller.getInstance().startSearching();
                pauseButton.setDisable(false);
                stopButton.setDisable(false);
                runButton.setDisable(true);
                resumeButton.setDisable(true);
            }
        });

        resumeButton = new Button("Resume");
        resumeButton.setDisable(true);
        resumeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchCotroller.getInstance().resumeSearching();
                runButton.setDisable(true);
                resumeButton.setDisable(true);
                pauseButton.setDisable(false);
                stopButton.setDisable(false);
            }
        });

        stopButton = new Button("Stop");
        stopButton.setDisable(true);
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchCotroller.getInstance().stopSearching();
                runButton.setDisable(false);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
            }
        });
        pauseButton = new Button("Pause");
        pauseButton.setDisable(true);
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchCotroller.getInstance().pauseSearching();

                runButton.setDisable(false);
                stopButton.setDisable(false);
                pauseButton.setDisable(true);
                resumeButton.setDisable(false);
            }
        });

        HBox handleButtonsPane = new HBox();
        handleButtonsPane.getChildren().addAll(runButton, pauseButton, resumeButton, stopButton);
        leftPanel.getChildren().add(handleButtonsPane);
    }

    private void createFoundPane(){
        foundListViewPanel = new FoundTreePanel();
        foundFloatPanel = new FloatPanelWrapper(foundListViewPanel.getTreeView());

        Pane floatPanel = foundFloatPanel.getFloatPanel();
        floatPanel.setMaxHeight(FloatPanelWrapper.DEFAULT_HEIGHT);
        StackPane.setAlignment(floatPanel, Pos.BOTTOM_CENTER);

        layeredPane.getChildren().add(floatPanel);
        foundFloatPanel.hide();
    }

    private void createSplitPane(){
        leftPanel = new VBox();
        rightPanel = new VBox();

        splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPanel, rightPanel);
        layeredPane.getChildren().add(splitPane);
    }

    private void createFilesTreePane(){
        filesTreePanel = new FilesTreePanel();
        Node filesTree = filesTreePanel.getTreeNode();
        VBox.setVgrow(filesTree, Priority.ALWAYS);
        rightPanel.getChildren().add(filesTree);
    }

    private void createLayeredPane(){
        layeredPane = new StackPane();
        VBox.setVgrow(layeredPane, Priority.ALWAYS);
        root.getChildren().add(layeredPane);
    }

    private void createRootPane(){
        root = new VBox();
    }

    private void createToolBar(){
        toolBar = new ToolBar();
        createToolBarButtons();
        root.getChildren().add(toolBar);
    }

    private void createToolBarButtons(){

        Button resultPaneHide = new Button("Show results");
        resultPaneHide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (foundFloatPanel.isVisible()) {
                    foundFloatPanel.hide();
                } else {
                    foundFloatPanel.show();
                }
            }
        });
        toolBar.getItems().addAll(resultPaneHide);
    }

    @Override
    public void stop(){
        SearchCotroller.getInstance().shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}