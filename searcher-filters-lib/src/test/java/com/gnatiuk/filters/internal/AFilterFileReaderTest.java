package com.gnatiuk.filters.internal;

import com.gnatiuk.filters.ATextFilterTest;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import org.junit.Before;
import utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Батинчук on 28.10.2015.
 */
public abstract class AFilterFileReaderTest extends ATextFilterTest {
    @Before
    public void initFilterFileReaderTest() {
        try {
            FileUtils.writeToFile(testFile, keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
