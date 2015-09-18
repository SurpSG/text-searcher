package com.gnatiuk.searcher.utils.json;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 9/7/15.
 */
public class FilterJsonProcessor {

    private static ObjectMapper mapper = createObjectMapper();

    public static String serializeFilterToJson(IFilter filter) throws IOException {
        return mapper.writeValueAsString(filter);
    }

    public static void serializeFilterToFile(String path, IFilter filter) throws IOException {
        FileUtils.writeToFile(new File(path), Arrays.asList(mapper.writeValueAsString(filter)));
    }

    public static IFilter deserializeFilterFromJson(String json) throws IOException {
        return mapper.readValue(json, IFilter.class);
    }

    public static IFilter deserializeFilterFromFile(File file) throws IOException {
        return mapper.readValue(new FileInputStream(file), IFilter.class);
    }

    private static ObjectMapper createObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
        return mapper;
    }

}
