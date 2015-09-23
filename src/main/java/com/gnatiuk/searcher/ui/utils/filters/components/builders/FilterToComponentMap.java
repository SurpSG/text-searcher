package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameExclude;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchFilterComponent;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchTextFilterComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 9/23/15.
 */
public enum FilterToComponentMap {

    FILE_NAME(FilterFileName.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new FileNameFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },
    FILE_NAME_REGEX(FilterFileNameRegex.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new FileNameFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    FILE_NAME_EXCLUDE(FilterFileNameExclude.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new FileNameExcludeFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },
    FILE_NAME_REGEX_EXCLUDE(FilterFileNameRegexExclude.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new FileNameExcludeFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    KEYWORD_FILTER(FilterFileKeyword.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new KeywordFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(false);
            return Arrays.asList(filterComponent);
        }
    },

    KEYWORD_REGEX_FILTER(FilterFileKeywordRegex.class) {
        @Override
        protected List<ASearchFilterComponent> build(IFilter filter) {
            ASearchTextFilterComponent filterComponent = new KeywordFilterComponentBuilder().buildByFilter(filter);
            filterComponent.setRegexCheck(true);
            return Arrays.asList(filterComponent);
        }
    },

    FILTERS_CONTAINER(FiltersContainer.class) {
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

    FilterToComponentMap(Class<? extends IFilter> filterClass) {
        this.filterClass = filterClass;
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
