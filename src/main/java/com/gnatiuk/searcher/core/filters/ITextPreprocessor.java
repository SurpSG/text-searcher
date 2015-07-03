package com.gnatiuk.searcher.core.filters;

/**
 * Created by Sergiy on 7/4/2015.
 */
public interface ITextPreprocessor {


    String process(String row);

    static final ITextPreprocessor NONE_PROCESSOR = new ITextPreprocessor() {
        @Override
        public String process(String row) {
            return row;
        }
    };

    static final ITextPreprocessor LOWERCASE_PROCESSOR = new ITextPreprocessor() {
        @Override
        public String process(String row) {
            return row.toLowerCase();
        }
    };
}
