package com.gnatiuk.filters;

import com.gnatiuk.searcher.filters.IFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by sgnatiuk on 10/23/15.
 */
public abstract class AFilterTest {

    protected final String TEST_FILE_NAME = "Test.txt";

    protected File testFile;

    protected abstract IFilter getFilter();

    @Before
    public final void init() {
        testFile = new File(TEST_FILE_NAME);
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doFilterTest(){
        assertEquals(getFilter().doFilter(testFile).getFilePath(), testFile.toPath());
    }

    @After
    public void destroy() {
        if(!testFile.delete()){
            System.err.println("Cannot remove file: " + testFile.getAbsolutePath());
        }
    }
}
