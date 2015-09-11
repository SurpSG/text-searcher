package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
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
public class SearchFiltersContainerComponent extends ASearchFilterComponent {

    public static final String NAME = "Search filters";

    private List<ASearchFilterComponent> filtersComponents;

    private CreatorSearchFilterComponent creatorSearchFilterComponent;

    private VBox rootBox;
    private VBox newFiltersBox;

    public SearchFiltersContainerComponent() {
        super();
        filtersComponents = new ArrayList<>();
        createCreatorSearchFilterComponent();

        rootBox = new VBox(ITEMS_VERTICAL_PADDING);
        newFiltersBox = new VBox(ITEMS_VERTICAL_PADDING);

        rootBox.getChildren().add(creatorSearchFilterComponent.getSearchCriteriaComponentsPane());

        titledPane.setPaneContent(newFiltersBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(titledPane);

        rootBox.getChildren().add(scrollPane);
    }

    protected void createMenuItems(){
        super.createMenuItems();
        MenuItem load = new MenuItem("Load");
        MenuItem clearAll = new MenuItem("Clear All");
        clearAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filtersComponents.clear();
                newFiltersBox.getChildren().clear();
            }
        });
        titledPane.addMenuItems(load, clearAll);
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

    @Override
    public String getName() {
        return NAME;
    }

    @Override
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

    @Override
    public Pane getSearchCriteriaComponentsPane() {
        return rootBox;
    }

}
