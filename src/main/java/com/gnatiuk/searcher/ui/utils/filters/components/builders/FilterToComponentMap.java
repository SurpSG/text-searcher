package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameExclude;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.ui.utils.filters.components.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 9/23/15.
 */
public enum FilterToComponentMap {

    FILE_NAME(FilterFileName.class, FileNameFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },
    FILE_NAME_REGEX(FilterFileNameRegex.class, FileNameFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    FILE_NAME_EXCLUDE(FilterFileNameExclude.class, FileNameExcludeFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },
    FILE_NAME_REGEX_EXCLUDE(FilterFileNameRegexExclude.class, FileNameExcludeFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    KEYWORD_FILTER(FilterFileKeyword.class, KeywordFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },

    KEYWORD_REGEX_FILTER(FilterFileKeywordRegex.class, KeywordFilterComponent.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new TextSearchComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    FILTERS_CONTAINER(FiltersContainer.class, null) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            FiltersContainer filtersContainer = (FiltersContainer) filter;
            List<ASearchFilterComponent> searchFiltersComponents = new ArrayList<>();
            for (IFilter iFilter : filtersContainer.getFilters()) {
                searchFiltersComponents.addAll(buildFilterComponent(iFilter));
            }
            return searchFiltersComponents;
        }
    };



    private Class<? extends IFilter> filterClass;
    private Class<? extends ASearchFilterComponent> filterComponentClass;

    FilterToComponentMap(Class<? extends IFilter> filterClass, Class<? extends ASearchFilterComponent> filterComponentClass) {
        this.filterClass = filterClass;
        this.filterComponentClass = filterComponentClass;
    }

    public static Class<? extends ASearchFilterComponent> getFilterComponentByFilterClass(IFilter filter){
        for (FilterToComponentMap filterToComponentMap : values()) {
            if(filter.getClass() == filterToComponentMap.filterClass){
                return filterToComponentMap.filterComponentClass;
            }
        }
        return null;
    }

    public static List<ASearchFilterComponent> buildFilterComponent(IFilter filter){
        for (FilterToComponentMap filterToComponentMap : values()) {
            if(filter.getClass() == filterToComponentMap.filterClass){
                return filterToComponentMap.build(filter);
            }
        }
        throw new IllegalArgumentException(String.format("'%s' class is not registered.", filter.getClass().getName()));
    }

    protected abstract List<ASearchFilterComponent> build(IFilter filter);
}
