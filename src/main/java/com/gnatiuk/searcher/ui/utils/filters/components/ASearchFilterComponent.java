package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.core.filters.IFilter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public abstract class ASearchFilterComponent {

    public static final int ITEMS_HORIZONTAL_PADDING = 10;
    public static final int ITEMS_VERTICAL_PADDING = 10;

    public abstract String getName();
    public abstract IFilter buildFilter();

    public abstract Pane getSearchCriteriaComponentsPane();

    public abstract Control getControl();

    private boolean enable = true;

    private CheckBox enableBox;
    private Button removeFilterComponent;

    private FilterRemovedListener filterRemovedListener;

    public ASearchFilterComponent() {
        enableBox = new CheckBox("enable");
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

    public boolean isEnable() {
        return enable;
    }

    protected Pane layoutComponents(List<Control> components, String title, int axis) {
        HBox box = new HBox(ITEMS_HORIZONTAL_PADDING);
        box.setStyle(
                "-fx-border-style: solid;"
                        + "-fx-border-width: 0.5;"
                        + "-fx-border-color: lightgray"
        );
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(removeFilterComponent);
        box.getChildren().add(enableBox);
        for (Control control : components) {
            box.getChildren().add(control);
        }
        return box;
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

    public void setFilterRemovedListener(FilterRemovedListener filterRemovedListener) {
        this.filterRemovedListener = filterRemovedListener;
    }

}
