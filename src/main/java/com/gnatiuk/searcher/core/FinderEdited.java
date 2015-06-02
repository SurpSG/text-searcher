package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class FinderEdited {

    private static volatile int currentSearchThreads = 0;

    private static int filesNumber = 0;

    private static synchronized int getCurrentSearchThreads(){
        return currentSearchThreads;
    }


    private List<String> textsToFind;
    private List<String> filePaths;
    private List<Pattern> fileFilterPatterns;

    public static void main(String[] args) {

        String textToFind = "new PurchaseProblems\\(.*,.*,.*\\)";
        textToFind = "public PurchaseProblems\\(";
        textToFind = "new PurchaseProblems(";
        textToFind = "Print.out(Print.FATAL, \"surop caStatus=\"+caStatus);";
        textToFind = "createPurchaseProblems";
        textToFind = "surop";

        String[] filePaths = {
                    "/cryptfs/builds/shr26-700/root_pac/build_mips",
//                    "/cryptfs/logs",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/druid",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/middleware_core",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/centrals",
        };

        List<String> fileFilters = Arrays.asList(/**/"\\.java$"/*,"\\.c$"*/);



        FinderEdited finder = new FinderEdited(textToFind, Arrays.asList(filePaths), fileFilters);
//        finder = new FinderPatternBased(textToFind, filePaths, fileFilters);
        finder.start();

    }

    public FinderEdited(String textToFind, List<String> filePaths, List<String> fileFilters){
        this(Arrays.asList(textToFind), filePaths, fileFilters);
    }

    public FinderEdited(List<String> textsToFind, List<String> filePaths, List<String> fileFilters){
        this.textsToFind = textsToFind;
        this.filePaths = filePaths;
        fileFilterPatterns = createPatterns(fileFilters);
    }

    public void start(){
        final long t1 = System.currentTimeMillis();


            ThreadController.getInstance().registerThread(new SearchRunnable(textsToFind, filePaths, fileFilterPatterns));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentSearchThreads;

                    while( (currentSearchThreads = getCurrentSearchThreads()) > 0){

                        System.err.println("threads: " + currentSearchThreads);
                        System.err.println("FilesNumber="+filesNumber);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    long t2 = System.currentTimeMillis() - t1;

                    System.out.println("time: "+(t2));
                }
            }).start();
    }

    protected List<Pattern> createPatterns(List<String> stringPatterns){
        List<Pattern> patterns = new ArrayList<>();

        for (String filter : stringPatterns) {
            patterns.add(createPattern(filter));
        }

        return patterns;
    }

    protected Pattern createPattern(String stringPatterns){
        return Pattern.compile(stringPatterns);
    }

}
