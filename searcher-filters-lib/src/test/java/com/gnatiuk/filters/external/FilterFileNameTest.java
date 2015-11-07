package com.gnatiuk.filters.external;

import com.gnatiuk.filters.ATextFilterTest;
import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.external.FilterFileName;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by sgnatiuk on 10/23/15.
 */
public class FilterFileNameTest extends ATextFilterTest {

    private FilterFileName filterFileName;

    @Override
    protected ATextFilter getTextFilter() {
        return filterFileName;
    }

    @Before
    public void initTextFilterTest() {
        filterFileName = new FilterFileName(Arrays.asList(TEST_FILE_NAME), textPreprocessor);
    }


    @Test
    public void doFilterTest(){
        super.doFilterTest();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int lastIndex = random.nextInt(TEST_FILE_NAME.length() - 1) + 1;
            int firstIndex = random.nextInt(lastIndex);
            String partitionTestFileName = TEST_FILE_NAME.substring(firstIndex, lastIndex);
            FilterFileName filterFileName = new FilterFileName(Arrays.asList(partitionTestFileName));
            Assert.assertEquals(filterFileName.doFilter(testFile).getFilePath(), testFile.toPath());
        }
    }
}
