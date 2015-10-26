package com.gnatiuk.searcher.filters.util;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FileSearchEvent {

    public static final FileSearchEvent NOT_FOUND = null;//TODO attention! NullPointerException

    private List<SearchOption> searchOptions;
    private Path filePath;

    public FileSearchEvent(File file) {
        searchOptions = new LinkedList<>();
        filePath = file.toPath();
    }

    public void addFoundOption(FoundOption optionName, String optionValue){

        searchOptions.add(new SearchOption(optionName, optionValue));
    }

    public void addFoundOptions(List<SearchOption> searchOptions){
        this.searchOptions.addAll(searchOptions);
    }

    public void mergeFoundOptions(FileSearchEvent fileSearchEvent){
        if(!this.equals(fileSearchEvent)){
            throw new IllegalArgumentException(String.format("It is not the same file: current fileSearchEvent: %s, " +
                    "new fileSearchEvent: %s", this, fileSearchEvent));
        }

        addFoundOptions(fileSearchEvent.searchOptions);
    }

    public List<SearchOption> getSearchOptions() {
        return searchOptions;
    }

    public int getOptionsCount(){
        return searchOptions.size();
    }

    public Path getFilePath() {
        return filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileSearchEvent that = (FileSearchEvent) o;

        return !(filePath != null ? !filePath.equals(that.filePath) : that.filePath != null);

    }



    @Override
    public String toString() {
        return "FileSearchEvent{" +
                "searchOptions=" + searchOptions +
                ", filePath=" + filePath +
                '}';
    }
}
