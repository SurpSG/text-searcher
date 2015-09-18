package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.dialog.FilterLoadDialog;
import com.gnatiuk.searcher.ui.utils.dialog.FilterSaveDialog;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.OptionedTitledPane;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.FilterRemovedListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 8/9/2015.
 */
//public class SearchFiltersContainerComponent extends ASearchFilterComponent {
public class SearchFiltersContainerComponent {

    public static final String NAME = "Search filters";

    private List<ASearchFilterComponent> filtersComponents;

    private CreatorSearchFilterComponent creatorSearchFilterComponent;

    private VBox rootBox;
    private VBox newFiltersBox;
    protected OptionedTitledPane titledPane;


    public SearchFiltersContainerComponent() {
        titledPane = new OptionedTitledPane(getName());
        filtersComponents = new ArrayList<>();
        createCreatorSearchFilterComponent();
        createMenuItems();
        rootBox = new VBox();
        newFiltersBox = new VBox();

        rootBox.getChildren().add(creatorSearchFilterComponent.getSearchCriteriaComponentsPane());

        titledPane.setPaneContent(newFiltersBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(titledPane);

        rootBox.getChildren().add(scrollPane);
    }

    protected void createMenuItems(){

        MenuItem saveAll = new MenuItem("Save All");
        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FilterSaveDialog(buildFilter()).show();
            }
        });

        MenuItem load = new MenuItem("Load");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                titledPane.hideMenuItems();
                FilterLoadDialog filterLoadDialog = new FilterLoadDialog();
                filterLoadDialog.showAndWait();
                addFilterComponents(ASearchFilterComponent.build(filterLoadDialog.getFilter()));
            }
        });

        MenuItem clearAll = new MenuItem("Clear All");
        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filtersComponents.clear();
                newFiltersBox.getChildren().clear();
            }
        });
        MenuItem loadAndReplace = new MenuItem("Load and replace");
        loadAndReplace.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearAll.getOnAction().handle(event);
                load.getOnAction().handle(event);
            }
        });
        titledPane.addMenuItems(saveAll, load, loadAndReplace, clearAll);
    }

    private CreatorSearchFilterComponent createCreatorSearchFilterComponent() {
        creatorSearchFilterComponent = new CreatorSearchFilterComponent();

        creatorSearchFilterComponent.setCreateButtonListener(new CreatorSearchFilterComponent.CreateButtonListener() {
            @Override
            public void actionPerformed(CreatorSearchFilterComponent.CreateButtonEvent createButtonEvent) {
                addFilterComponent(createButtonEvent.getSearchFilterComponent());
            }
        });

        return creatorSearchFilterComponent;
    }

    public String getName() {
        return NAME;
    }

    public IFilter buildFilter() {
        FiltersContainer filter = new FiltersContainer();
        for (ASearchFilterComponent filterComponent : filtersComponents) {
            filter.addFilter(filterComponent.buildFilter());
        }
        if (filter.isEmpty()) {
            return IFilter.NONE_FILTER;
        }
        return filter;
    }

    public void addFilterComponent(final ASearchFilterComponent filterComponent) {
        final Node pane = filterComponent.getSearchCriteriaComponentsPane();
        filterComponent.setFilterRemovedListener(new FilterRemovedListener() {
            @Override
            public void notifyFilterRemoved() {
                filtersComponents.remove(filterComponent);
                newFiltersBox.getChildren().remove(pane);
            }
        });
        filtersComponents.add(filterComponent);
        newFiltersBox.getChildren().add(pane);
    }

    public void addFilterComponents(List<ASearchFilterComponent> filterComponents) {
        for (ASearchFilterComponent filterComponent : filterComponents) {
            addFilterComponent(filterComponent);
        }
    }

    public Pane getFiltersContainer() {
        return rootBox;
    }

}
