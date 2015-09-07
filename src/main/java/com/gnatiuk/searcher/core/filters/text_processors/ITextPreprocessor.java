package com.gnatiuk.searcher.core.filters.text_processors;

import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Created by Sergiy on 7/4/2015.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoneProcessor.class, name = "NoneProcessor"),
        @JsonSubTypes.Type(value = LowerCaseProcessor.class, name = "LowerCaseProcessor")
})
public interface ITextPreprocessor {

    String process(String row);

    ITextPreprocessor NONE_PROCESSOR = new NoneProcessor();
    ITextPreprocessor LOWERCASE_PROCESSOR = new LowerCaseProcessor();
}