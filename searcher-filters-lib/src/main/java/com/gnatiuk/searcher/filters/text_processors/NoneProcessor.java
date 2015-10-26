package com.gnatiuk.searcher.filters.text_processors;

/**
 * Created by sgnatiuk on 9/7/15.
 */
class NoneProcessor implements ITextPreprocessor{
    @Override
    public String process(String row) {
        return row;
    }

    @Override
    public String toString() {
        return "NoneProcessor";
    }
}
