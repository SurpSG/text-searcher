package com.gnatiuk;

import com.gnatiuk.searcher.core.runnable.SearchController;
import com.gnatiuk.searcher.core.utils.IFileFoundListener;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by sgnatiuk on 10/23/15.
 */
public class SearcherControllerTest {


    @Before
    public final void init() {

    }

    @Test
    public synchronized void doFilterTest() {
        FilterFileNameRegex filterFileNameRegex = new FilterFileNameRegex(Arrays.asList("\\.java$"));
        List<String> filePaths = Arrays.asList(new File(".").getAbsolutePath());

        SearchController.getInstance().setPathsToSearch(filePaths);
        SearchController.getInstance().setFilter(filterFileNameRegex);

        List<Path> firstTimeFound = new ArrayList<>();
        List<Path> found = new ArrayList<>();
        SearchController.getInstance().registerFileFoundListener(new IFileFoundListener() {
            @Override
            public void alertFileFound(FileSearchEvent fileSearchEvent) {
                found.add(fileSearchEvent.getFilePath());
            }
        });
        Thread currentThread = Thread.currentThread();
        Object sync = new Object();
        SearchController.getInstance().registerWorkCompleteListener(new IWorkCompleteListener() {
                                                                        @Override
                                                                        public void actionPerformed(WorkCompleteEvent event) {
                                                                            firstTimeFound.addAll(found);
                                                                            found.clear();
                                                                            SearchController.getInstance().registerWorkCompleteListener(new IWorkCompleteListener() {
                                                                                @Override
                                                                                public void actionPerformed(WorkCompleteEvent event) {
//                                                                                    synchronized (currentThread){
//                                                                                        currentThread.notify();
//
//                                                                                    }
                                                                                }
                                                                            });
                                                                            SearchController.getInstance().startSearching();
                                                                        }
                                                                    }
        );
        SearchController.getInstance().startSearching();

        try {
            synchronized (this){
                wait(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(found.size() == 0);

        for (Path path : found) {
            assertTrue(firstTimeFound.contains(path));
        }

    }

    @After
    public void destroy() {
        SearchController.getInstance().shutdown();
    }
}
