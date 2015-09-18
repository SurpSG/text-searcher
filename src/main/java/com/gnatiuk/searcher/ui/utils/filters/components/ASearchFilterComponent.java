package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.dialog.FilterSaveDialog;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.OptionedTitledPane;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.FilterRemovedListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public abstract class ASearchFilterComponent {

    public static final int ITEMS_HORIZONTAL_PADDING = 10;
    public static final int COMPONENT_PADDING = 10;
    public static final int ITEMS_VERTICAL_PADDING = 10;

    private static final Background ENABLE_BACKGROUND;
    private static final Background DISABLE_BACKGROUND;

    static {
        ENABLE_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
        DISABLE_BACKGROUND = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
    }

    private FilterRemovedListener filterRemovedListener;


    protected OptionedTitledPane titledPane;
    private HBox filterComponentRootBox;
    private CheckBox enableBox;
    private Button removeFilterComponent;

    private List<Node> concreteFilterComponents;

    public ASearchFilterComponent() {

        initFilterComponentRootBox();
        titledPane = new OptionedTitledPane(getName());
        createMenuItems();
        enableBox = new CheckBox("enable");
        enableBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(enableBox.isSelected()){
                    enableFilterComponent();
                }else{
                    disableFilterComponent();
                }
            }
        });
        enableBox.setSelected(true);

        removeFilterComponent = new Button("-");

        removeFilterComponent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (filterRemovedListener != null) {
                    filterRemovedListener.notifyFilterRemoved();
                } else {
                    throw new RuntimeException("No listener on remove filter button.");
                }
            }
        });
    }

    public abstract String getName();
    protected abstract IFilter buildFilter();
    public abstract Node getSearchCriteriaComponentsPane();

    public IFilter getFilter(){
        if(enableBox.isSelected()){
            return buildFilter();
        }else {
            return IFilter.NONE_FILTER;
        }
    }

    protected void createMenuItems(){
        MenuItem save = new MenuItem("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FilterSaveDialog(buildFilter()).show();
            }
        });
        MenuItem loadOnAppStart = new MenuItem("Load on start");
        loadOnAppStart.setGraphic(new CheckBox());


        titledPane.addMenuItems(loadOnAppStart, save);
    }

    protected Node layoutComponents(List<Node> components) {
        if(concreteFilterComponents != null){
            return filterComponentRootBox;
        }

        concreteFilterComponents = components;
        filterComponentRootBox.getChildren().add(removeFilterComponent);
        filterComponentRootBox.getChildren().add(enableBox);

        for (Node node : components) {
            HBox.setHgrow(node, Priority.ALWAYS);
            filterComponentRootBox.getChildren().add(node);
        }

        titledPane.setPaneContent(filterComponentRootBox);
        return titledPane;
    }


    public static ASearchFilterComponent build(Class<? extends ASearchFilterComponent> classObj) {
        try {
            //get default constructor
            Constructor<? extends ASearchFilterComponent> constructor = classObj.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(String.format("Problems during creating an instance using" +
                " '%s' Class object", classObj));
    }

    public static List<ASearchFilterComponent> build(IFilter filter) {
        return Collections.EMPTY_LIST;//TODO create builder for ASearchFilterComponent from IFilter object
    }

    public static List<ASearchFilterComponent> build(Collection<IFilter> filters) {
        List<ASearchFilterComponent> searchFilterComponents = new ArrayList<>();
        for (IFilter filter : filters) {
            System.out.println(filter);
            searchFilterComponents.addAll(build(filter));
        }
        return searchFilterComponents;
    }

    public void setFilterRemovedListener(FilterRemovedListener filterRemovedListener) {
        this.filterRemovedListener = filterRemovedListener;
    }

    private void initFilterComponentRootBox(){
        filterComponentRootBox = new HBox(ITEMS_HORIZONTAL_PADDING);
        filterComponentRootBox.setPadding(new Insets(COMPONENT_PADDING));
        filterComponentRootBox.setStyle(
                "-fx-border-style: solid;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-color: gray;"
        );
        filterComponentRootBox.setAlignment(Pos.CENTER_LEFT);
    }

    private void disableFilterComponent(){
        setDisableAll(true,DISABLE_BACKGROUND);
    }

    private void enableFilterComponent(){
        setDisableAll(false, ENABLE_BACKGROUND);
    }

    private void setDisableAll(boolean disable, Background background){
        filterComponentRootBox.setBackground(background);
        for (Node concreteFilterComponent : concreteFilterComponents) {
            concreteFilterComponent.setDisable(disable);
        }
    }
}
