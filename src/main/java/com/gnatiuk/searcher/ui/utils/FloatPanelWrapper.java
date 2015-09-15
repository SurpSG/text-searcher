package com.gnatiuk.searcher.ui.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * Created by sgnatiuk on 7/27/15.
 */
public class FloatPanelWrapper {

    public static final int DEFAULT_HEIGHT = 300;

    private Node foundContainer;

    private VBox floatPane;
    private HBox toolPanel;
    private Button hideButton;

    private Parent parent;
    private boolean visible;

    public FloatPanelWrapper(Node foundComponent){

        visible = false;
        this.foundContainer = foundComponent;
        VBox.setVgrow(foundComponent,Priority.ALWAYS);

        initFloatPanel();
        initToolPanel();
        floatPane.getChildren().add(toolPanel);
        floatPane.getChildren().add(foundComponent);

        foundComponent.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    hide();
                }
            }
        });
    }

    private void initFloatPanel(){
        floatPane = new VBox();
    }

    private void initToolPanel(){
        toolPanel = new HBox();
        toolPanel.setMaxHeight(20);
        toolPanel.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));

        initHideButton();

        toolPanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            private double currentY = Double.MIN_VALUE;

            @Override
            public void handle(MouseEvent event) {
                if (currentY == Double.MIN_VALUE) {
                    currentY = event.getSceneY();
                }

                double diff = event.getSceneY() - currentY;
                double currentHeight = floatPane.getMaxHeight() - diff;
                if(currentHeight > 0){
                    floatPane.setMaxHeight(currentHeight);
                    currentY = event.getSceneY();
                }
            }
        });

        toolPanel.getChildren().add(hideButton);
    }

    private void initHideButton(){
        hideButton = new Button("Hide");
        hideButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hide();
            }
        });
    }

    public Pane getFloatPanel() {
        return floatPane;
    }

    public void show(){
        if(parent != null && parent instanceof Pane){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Pane) parent).getChildren().add(floatPane);
                }
            });
        }else{
            throw new RuntimeException("floatPane parent is null or parent.class is "+parent.getClass()+" that is not instance of Pane class.");
        }
        visible = true;
    }

    public void hide(){
        if(parent == null){
            parent = floatPane.getParent();
        }

        if(parent instanceof Pane){
            ((Pane)parent).getChildren().remove(floatPane);
        }else{
            throw new RuntimeException("floatPane parent.class is "+parent.getClass()+" that is not instance of Pane class.");
        }
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
