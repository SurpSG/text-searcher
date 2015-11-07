package com.gnatiuk.filters;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Батинчук on 28.10.2015.
 */
public abstract class ATextFilterTest extends AFilterTest {

    protected ITextPreprocessor textPreprocessor;
    protected List<String> keywords;

    protected abstract ATextFilter getTextFilter();

    public ATextFilterTest() {
        keywords = Arrays.asList("abc","asdsdf");
        textPreprocessor = ITextPreprocessor.NONE_PROCESSOR;
    }

    @Test
    public void testTextProcessor() {

        List<String> keywordsToTest = Arrays.asList(
                "AAAAA",
                "aaaaa",
                "AaAaA"
        );

        ATextFilter textFilter = getTextFilter();
        //save filter keywords
        List<String> savedKeywords = textFilter.getKeywords();
        textFilter.setKeywords(keywordsToTest);

        //Test lowerCaseProcessor
        textFilter.setTextPreprocessor(ITextPreprocessor.NONE_PROCESSOR);
        for (int i = 0; i < keywordsToTest.size(); i++) {
            Assert.assertEquals(keywordsToTest.get(i), textFilter.getKeywords().get(i));
        }

        //Test lowerCaseProcessor
        textFilter.setTextPreprocessor(ITextPreprocessor.LOWERCASE_PROCESSOR);
        for (int i = 0; i < keywordsToTest.size(); i++) {
            Assert.assertEquals(textFilter.getKeywords().get(i), ITextPreprocessor.LOWERCASE_PROCESSOR.process(keywordsToTest.get(i)));
        }

        //restore filter keywords
        textFilter.setKeywords(savedKeywords);
    }


    @Override
    protected final ATextFilter getFilter() {
        return getTextFilter();
    }

}
