package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.AFilterTest;
import com.gnatiuk.searcher.core.filters.IFilter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by sgnatiuk on 10/23/15.
 */
public class FilterFileNameTest extends AFilterTest {

    private FilterFileName filterFileName = new FilterFileName(Arrays.asList(TEST_FILE_NAME));

    @Override
    protected IFilter getFilter() {
        return filterFileName;
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
            assertEquals(filterFileName.doFilter(testFile).getFilePath(), testFile.toPath());
        }
    }
}
