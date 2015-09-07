package com.gnatiuk.searcher.utils.json;

import com.gnatiuk.searcher.core.filters.IFilter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by sgnatiuk on 9/7/15.
 */
public class FilterJsonProcessor {


    public static String serializeFilterToJson(IFilter filter) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
        return mapper.writeValueAsString(filter);
    }

    public static IFilter deserializeFilterToJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
        return mapper.readValue(json, IFilter.class);
    }
}
