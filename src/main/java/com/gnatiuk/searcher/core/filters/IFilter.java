package com.gnatiuk.searcher.core.filters;


import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;
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
@JsonSubTypes({
        @JsonSubTypes.Type(value = FilterFileKeyword.class, name = "FilterFileKeyword"),
        @JsonSubTypes.Type(value = FilterFileName.class, name = "FilterFileName"),
        @JsonSubTypes.Type(value = FiltersContainer.class, name = "FiltersContainer"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IFilter {

    FileSearchEvent doFilter(File file);

    IFilter NONE_FILTER = (file) -> new FileSearchEvent(file);

}