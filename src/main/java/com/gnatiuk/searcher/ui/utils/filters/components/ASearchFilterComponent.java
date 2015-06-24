package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.core.filters.AFilter;

import javax.swing.*;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public abstract class ASearchFilterComponent {

    public abstract AFilter buildFilter();
    public abstract JPanel getSearchCriteriaComponentsPanel();

    protected static JPanel layoutComponents(List<JComponent> component, String title, int axis) {
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createTitledBorder(title));
        BoxLayout layout = new BoxLayout(container, axis);
        container.setLayout(layout);
        for (JComponent jComponent : component) {
            container.add(jComponent);
        }
        return container;
    }


}
