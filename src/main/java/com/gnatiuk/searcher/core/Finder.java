package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
import com.gnatiuk.searcher.core.utils.FinderType;
import com.gnatiuk.searcher.core.utils.IWorkCompleteListener;
import com.gnatiuk.searcher.core.utils.WorkCompleteEvent;

import java.util.*;
import java.util.regex.Pattern;

public class Finder {

    private List<String> textsToFind;
    private List<String> filePaths;
    private List<Pattern> fileFilterPatterns;

    public static void main(String[] args) {

        String textToFind = "new PurchaseProblems\\(.*,.*,.*\\)";
        textToFind = "!(obj instanceof EqualsTest";

        String[] filePaths = {
                    "E:\\downloads",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/druid",
        };

        List<String> fileFilters = Arrays.asList(/*"\\.java$"/*,"\\.c$"*/);



        Finder finder = new Finder(textToFind, Arrays.asList(filePaths), fileFilters);
//        finder = new FinderPatternBased(textToFind, filePaths, fileFilters);

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                System.out.println("done!!!!!");
            }
        });

        finder.start();
    }

    public Finder(String textToFind, List<String> filePaths, List<String> fileFilters){
        this(Arrays.asList(textToFind), filePaths, fileFilters);
    }

    public Finder(List<String> textsToFind, List<String> filePaths, List<String> fileFilters){
        this.textsToFind = textsToFind;
        this.filePaths = filePaths;
        fileFilterPatterns = createPatterns(fileFilters);
    }

    public void start(){
        ThreadController.getInstance().registerThread(new SearchHierarchyRunnable(textsToFind, filePaths, fileFilterPatterns));
    }

    protected List<Pattern> createPatterns(List<String> stringPatterns){
        List<Pattern> patterns = new ArrayList<>();

        for (String filter : stringPatterns) {
            patterns.add(createPattern(filter));
        }

        return patterns;
    }

    public static Pattern createPattern(String stringPatterns){
        return Pattern.compile(stringPatterns);
    }

    public static Finder build(FinderType finderType, String textToFind, List<String> filePaths, List<String> fileFilters){
        switch (finderType){
            case PATTERN_BASED:
//                return new FinderPatternBased(textToFind, filePaths, fileFilters);
            default:
                return new Finder(textToFind, filePaths, fileFilters);
        }
    }

}
