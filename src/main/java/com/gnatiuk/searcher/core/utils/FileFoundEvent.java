package com.gnatiuk.searcher.core.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FileFoundEvent {

    public static final FileFoundEvent NOT_FOUND = null;//TODO attention! NullPointerException

    private Map<FoundOption, String> searchOptions;
    private Path filePath;

    public FileFoundEvent(File file) {
        searchOptions = new LinkedHashMap<>();
        filePath = file.toPath();
    }

    public void addFoundOption(FoundOption optionName, String optionValue){

        searchOptions.put(optionName, optionValue);
    }

    public void addFoundOptions(Map<FoundOption, String> searchOptions){
        this.searchOptions.putAll(searchOptions);
    }

    public void mergeFoundOptions(FileFoundEvent fileFoundEvent){
        if(!this.equals(fileFoundEvent)){
            throw new IllegalArgumentException(String.format("It is not the same file: current fileFoundEvent: %s, " +
                    "new fileFoundEvent: %s", this, fileFoundEvent));
        }

        addFoundOptions(fileFoundEvent.searchOptions);
    }

    public Map<FoundOption, String> getSearchOptions() {
        return searchOptions;
    }

    public Path getFilePath() {
        return filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileFoundEvent that = (FileFoundEvent) o;

        return !(filePath != null ? !filePath.equals(that.filePath) : that.filePath != null);

    }



    @Override
    public String toString() {
        return "FileFoundEvent{" +
                "searchOptions=" + searchOptions +
                ", filePath=" + filePath +
                '}';
    }
}
