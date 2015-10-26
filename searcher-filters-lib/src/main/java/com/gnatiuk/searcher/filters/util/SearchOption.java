package com.gnatiuk.searcher.filters.util;

/**
 * Created by sgnatiuk on 7/17/15.
 */
public class SearchOption {
    private FoundOption foundOption;
    private String foundValue;

    public SearchOption(FoundOption foundOption, String foundValue) {
        this.foundOption = foundOption;
        this.foundValue = foundValue;
    }

    public FoundOption getFoundOption() {
        return foundOption;
    }

    public String getFoundValue() {
        return foundValue;
    }
}
