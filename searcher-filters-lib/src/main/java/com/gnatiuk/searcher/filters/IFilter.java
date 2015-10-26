package com.gnatiuk.searcher.filters;


import com.gnatiuk.searcher.filters.external.FilterFileName;
import com.gnatiuk.searcher.filters.external.FilterFileNameExclude;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegexExclude;
import com.gnatiuk.searcher.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.filters.internal.FilterFileKeywordRegex;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.File;

/**
 * Created by sgnatiuk on 6/15/15.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = FilterFileName.class, name = "FilterFileName"),
        @JsonSubTypes.Type(value = FilterFileNameExclude.class, name = "FilterFileNameExclude"),
        @JsonSubTypes.Type(value = FilterFileNameRegex.class, name = "FilterFileNameRegex"),
        @JsonSubTypes.Type(value = FilterFileNameRegexExclude.class, name = "FilterFileNameRegexExclude"),

        @JsonSubTypes.Type(value = FilterFileKeyword.class, name = "FilterFileKeyword"),
        @JsonSubTypes.Type(value = FilterFileKeywordRegex.class, name = "FilterFileKeywordRegex"),

        @JsonSubTypes.Type(value = FiltersContainer.class, name = "FiltersContainer"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IFilter {

    FileSearchEvent doFilter(File file);
    String filterHash();


    IFilter NONE_FILTER = new IFilter() {
        @Override
        public FileSearchEvent doFilter(File file) {
            return new FileSearchEvent(file);
        }

        @Override
        public String filterHash() {
            return "0";
        }
    };
}