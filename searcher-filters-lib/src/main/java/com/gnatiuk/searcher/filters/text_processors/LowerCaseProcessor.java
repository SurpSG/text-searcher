package com.gnatiuk.searcher.filters.text_processors;

/**
 * Created by sgnatiuk on 9/7/15.
 */
class LowerCaseProcessor  implements ITextPreprocessor {
    @Override
    public String process(String row) {
        return row.toLowerCase();
    }

    @Override
    public String toString() {
        return "LowerCaseProcessor";
    }
}
