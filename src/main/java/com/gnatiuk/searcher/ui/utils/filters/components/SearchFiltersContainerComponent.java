package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 8/9/2015.
 */
public class SearchFiltersContainerComponent extends ASearchFilterComponent {

    public static final String NAME = "Search filters";

    private List<ASearchFilterComponent> filtersComponents;

    private CreatorSearchFilterComponent creatorSearchFilterComponent;


    private VBox vbox;

    public SearchFiltersContainerComponent() {
        super();
        filtersComponents = new ArrayList<>();
        createCreatorSearchFilterComponent();

        vbox = new VBox(ITEMS_VERTICAL_PADDING);
        Text title = new Text("Crete new filter");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        vbox.getChildren().add(creatorSearchFilterComponent.getSearchCriteriaComponentsPane());
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
        return filter;
    }

    public void addFilterComponent(ASearchFilterComponent filterComponent) {
        Pane pane = filterComponent.getSearchCriteriaComponentsPane();
        filterComponent.setFilterRemovedListener(new FilterRemovedListener() {
            @Override
            public void notifyFilterRemoved() {
                filtersComponents.remove(filterComponent);
                vbox.getChildren().remove(pane);
            }
        });
        filtersComponents.add(filterComponent);
        vbox.getChildren().add(pane);
        update();
    }

    public void removeFilterComponent(ASearchFilterComponent filterComponent) {
        filtersComponents.remove(filterComponent);
        update();
    }

    @Override
    public Pane getSearchCriteriaComponentsPane() {
        return vbox;
    }

    public void update() {
//        panelsContainerBox.remove(creatorSearchFilterComponent.getSearchCriteriaComponentsPane());
//        panelsContainerBox.remove
//        for (ASearchFilterComponent filtersComponent : filtersComponents) {
//            panelsContainerBox.add(filtersComponent.getSearchCriteriaComponentsPane());
//        }
//        panelsContainerBox.add(creatorSearchFilterComponent.getSearchCriteriaComponentsPane());
//        panelsContainerBox.updateUI();
//        panelsContainerBox.repaint();
    }


    @Override
    public Control getControl() {
        return null;
    }
}
