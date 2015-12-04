package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.external.FilterFileDate;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by sgnatiuk on 11/12/15.
 */
public class DateFilterComponent extends ASearchFilterComponent {

    public static final String NAME = "Last modified filter";

    private DatePicker datePickerFrom;
    private DatePicker datePickerTo;

    public DateFilterComponent(){
        datePickerFrom = new DatePicker();
        datePickerTo = new DatePicker();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected IFilter buildFilter() {
        return new FilterFileDate(getDate(datePickerFrom), getDate(datePickerTo));
    }

    private Date getDate(DatePicker datePicker){
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    @Override
    protected Node layoutComponents(List<Node> components) {
        components.add(new Label("From:"));
        components.add(datePickerFrom);
        components.add(new Label("To:"));
        components.add(datePickerTo);
        return super.layoutComponents(components);
    }
}
