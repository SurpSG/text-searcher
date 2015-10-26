package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.OnBootManager;
import com.gnatiuk.searcher.ui.utils.dialog.FilterSaveDialog;
import com.gnatiuk.searcher.ui.utils.filters.components.builders.FilterToComponentMap;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
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

    private CheckBox autoLoad;

    public ASearchFilterComponent() {
        autoLoad = new CheckBox();
        titledPane = new OptionedTitledPane(getName());

        enableBox = new CheckBox("enable");
        enableBox.setSelected(true);
        enableBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (enableBox.isSelected()) {
                    enableFilterComponent();
                } else {
                    disableFilterComponent();
                }
            }
        });

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


        initFilterComponentRootBox();
        createMenuItems();

    }

    public abstract String getName();
    protected abstract IFilter buildFilter();
    public abstract Node getSearchCriteriaComponentsPane();

    public boolean isAutoLoad() {
        return autoLoad.isSelected();
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad .setSelected(autoLoad);
    }

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
        loadOnAppStart.setGraphic(autoLoad);
        autoLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(autoLoad.isSelected()){
                    OnBootManager.registerOnBootFilter(buildFilter());
                }else{
                    OnBootManager.unregisterOnBootFilter(buildFilter());
                }
            }
        });

        titledPane.addMenuItems(loadOnAppStart, save);
    }

    protected Node layoutComponents(List<Node> components) {
        if(concreteFilterComponents != null){
            return filterComponentRootBox;
        }

        concreteFilterComponents = components;
        filterComponentRootBox.getChildren().addAll(removeFilterComponent, enableBox);

        for (Node node : components) {
            filterComponentRootBox.getChildren().add(node);
        }

        titledPane.setPaneContent(filterComponentRootBox);
        return titledPane;
    }

    public static List<ASearchFilterComponent> build(IFilter filter) {
        return FilterToComponentMap.buildFilterComponent(filter);
    }

    public static List<ASearchFilterComponent> build(Collection<IFilter> filters) {
        List<ASearchFilterComponent> searchFilterComponents = new ArrayList<>();
        for (IFilter filter : filters) {
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

    private void setDisableAll(boolean disable, Background background) {
        filterComponentRootBox.setBackground(background);
        for (Node concreteFilterComponent : concreteFilterComponents) {
            concreteFilterComponent.setDisable(disable);
        }
    }
}
