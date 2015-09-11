package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.runnable.SearcherHierarchyRunnable;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;
import com.gnatiuk.searcher.core.utils.IFileFoundListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.ui.utils.FilesTreePanel;
import com.gnatiuk.searcher.ui.utils.FloatPanelWrapper;
import com.gnatiuk.searcher.ui.utils.FoundTreePanel;
import com.gnatiuk.searcher.ui.utils.filters.components.SearchFiltersContainerComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.List;


/**
 * Created by surop on 08.09.15.
 */
public class SearcherApplication extends Application{


    private Button runButton;
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
        ThreadController.getInstance().registerFileFoundListener(new IFileFoundListener() {
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
        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                JOptionPane.showMessageDialog(null, "Done!");
            }
        });
    }

    private void createFiltersPane(){
        filtersContainer = new SearchFiltersContainerComponent();
        Node searchCriteriaComponentsPane = filtersContainer.getSearchCriteriaComponentsPane();
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
                System.out.println(filter);
                SearcherHierarchyRunnable searcherHierarchyRunnable = new SearcherHierarchyRunnable(pathsToSearch, filter);
                ThreadController.getInstance().registerThread(searcherHierarchyRunnable);
            }
        });
        leftPanel.getChildren().addAll(runButton);
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
        ThreadController.getInstance().shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}