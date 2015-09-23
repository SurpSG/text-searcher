package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.filters.components.builders.ASearchComponentBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by surop on 04.08.15.
 */
public class CreatorSearchFilterComponent extends ASearchFilterComponent {

    public static final String NAME = "Create new filter";

    private static final ComboBox<Pair<Class<? extends ASearchFilterComponent>, String>>
            FILTERS_COMBO_BOX = createFiltersComboBox();

    private final Button addFiltersButton = createAddFilterButton();
    private CreateButtonListener buttonListener;

    private final Pane searchCriteriaComponents;

    public CreatorSearchFilterComponent() {
        super();
        searchCriteriaComponents = createSearchCriteriaComponentsPane();
    }

    private Button createAddFilterButton() {
        Button button = new Button("+");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pair<Class<? extends ASearchFilterComponent>, String> selectedItem =
                        FILTERS_COMBO_BOX.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    return;
                }

                Class<? extends ASearchFilterComponent> classObj = selectedItem.getKey();
                if (buttonListener != null) {
                    buttonListener.actionPerformed(new CreateButtonEvent(ASearchComponentBuilder.build(classObj)));
                }
            }
        });
        return button;
    }

    @Override
    public IFilter buildFilter() {
        return IFilter.NONE_FILTER;
    }

    private Pane createSearchCriteriaComponentsPane() {

        Pane box = new HBox(ITEMS_HORIZONTAL_PADDING);
        box.setPadding(new Insets(COMPONENT_PADDING));
        box.getChildren().add(addFiltersButton);
        box.getChildren().add(FILTERS_COMBO_BOX);
        return box;
    }

    @Override
    public Pane getSearchCriteriaComponentsPane() {
        return searchCriteriaComponents;
    }

    @Override
    public String getName() {
        return NAME;
    }


    public void setCreateButtonListener(CreateButtonListener createButtonListener) {
        buttonListener = createButtonListener;
    }

    private static ComboBox<Pair<Class<? extends ASearchFilterComponent>, String>> createFiltersComboBox() {
        ComboBox<Pair<Class<? extends ASearchFilterComponent>, String>> comboBox = new ComboBox<>();

        comboBox.setCellFactory(new Callback<ListView<Pair<Class<? extends ASearchFilterComponent>, String>>, ListCell<Pair<Class<? extends ASearchFilterComponent>, String>>>() {
            @Override
            public ListCell<Pair<Class<? extends ASearchFilterComponent>, String>> call(ListView<Pair<Class<? extends ASearchFilterComponent>, String>> param) {
                return new ListCell<Pair<Class<? extends ASearchFilterComponent>, String>>() {
                    @Override
                    protected void updateItem(Pair<Class<? extends ASearchFilterComponent>, String> item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            return;
                        }

                        setText(item.getValue());
                    }

                };
            }
        });

        comboBox.setButtonCell(new ListCell<Pair<Class<? extends ASearchFilterComponent>, String>>() {
            @Override
            protected void updateItem(Pair<Class<? extends ASearchFilterComponent>, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getValue());
                }
            }

        });


        List<Pair<Class<? extends ASearchFilterComponent>, String>> elements = Arrays.asList(
                new Pair<>(FileNameFilterComponent.class, FileNameFilterComponent.NAME),
                new Pair<>(KeywordFilterComponent.class, KeywordFilterComponent.NAME),
                new Pair<>(FileNameExcludeFilterComponent.class, FileNameExcludeFilterComponent.NAME)
        );
        comboBox.getItems().addAll(elements);


        return comboBox;

    }

    public interface CreateButtonListener {
        void actionPerformed(CreateButtonEvent createButtonEvent);
    }

    public static class CreateButtonEvent {
        private ASearchFilterComponent searchFilterComponent;

        public CreateButtonEvent(ASearchFilterComponent searchFilterComponent) {
            this.searchFilterComponent = searchFilterComponent;
        }

        public ASearchFilterComponent getSearchFilterComponent() {
            return searchFilterComponent;
        }
    }
}
