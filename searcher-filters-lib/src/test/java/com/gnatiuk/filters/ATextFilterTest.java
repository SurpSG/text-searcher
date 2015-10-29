package com.gnatiuk.filters;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Батинчук on 28.10.2015.
 */
public abstract class ATextFilterTest extends AFilterTest {
    protected ITextPreprocessor textPreprocessor;
    protected List<String> keywords;
    @Before
    public void initTextFilterTest() {
        keywords = Arrays.asList("abc","asdsdf");
        textPreprocessor = ITextPreprocessor.NONE_PROCESSOR;
    }


}
