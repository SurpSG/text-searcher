package com.gnatiuk.filters.external;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.external.FilterFileName;
import com.gnatiuk.searcher.filters.external.FilterFileNameExclude;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Батинчук on 29.10.2015.
 */
public class FilterFileNameExcludeTest extends FilterFileNameTest{
    private FilterFileNameExclude filterFileNameExclude = new FilterFileNameExclude(Arrays.asList(TEST_FILE_NAME));
    @Override
    protected IFilter getFilter() {
        return filterFileNameExclude;
    }
    @Test
    public void doFilterTest(){
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int lastIndex = random.nextInt(TEST_FILE_NAME.length() - 1) + 1;
            int firstIndex = random.nextInt(lastIndex);
            String partitionTestFileName = TEST_FILE_NAME.substring(firstIndex, lastIndex);
            FilterFileNameExclude filterFileNameExclude = new FilterFileNameExclude(Arrays.asList(partitionTestFileName));
            Assert.assertFalse(filterFileNameExclude.doFilter(testFile).getFilePath().equals(testFile.toPath()));
        }
    }
}
