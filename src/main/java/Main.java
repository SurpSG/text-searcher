import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 5/12/15.
 */
public class Main {
    private static int[][] res = null;

    public static void main(String[] args) {

        System.out.println(0X10);
//        int[][] matA = {
//                { 1, 1, 1 },
//                { 1, 1, 1 },
//                { 1, 1, 1 }
//        };
//
//        int[][] matB = {
//                { 1, 1, 1 },
//                { 1, 1, 1 },
//                { 1, 1, 1 }
//        };
//
//        int n = Runtime.getRuntime().availableProcessors();
//        ThreadMat[] threads = new ThreadMat[n];
//        for (int i = 0; i < n; i++) {
//            threads[i] = new ThreadMat();
//        }
//        try {
//                for (int i = 0; i < n; i++) {
//                    threads[i].add(matA, matB, res);
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println(e);
//        }
//        for (int i = 0; i < res.length; i++) {
//            for (int j = 0; j < res.length - 1; j++) {
//                System.out.format("%6d ", res[i][j]);
//            }
//            System.out.println();
//        }
    }


    public static int getFilesCount(Path dir) throws IOException{
        int c = 0;
        if(Files.isDirectory(dir)) {
            try(DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
                for(Path file : files) {
                    if(Files.isRegularFile(file) || Files.isSymbolicLink(file)) {
                        // symbolic link also looks like file
                        c++;
                    }
                }
            }
        }

        return c;
    }

    public static int filesNmb = 0;

    public static void calcFiles(File directory){

        if(directory.isDirectory()){
            String[] files = directory.list();
            if(files != null){
                filesNmb += files.length;
                System.out.println(filesNmb);
                for (String fileName : files) {
                    File file = new File(directory.getAbsolutePath()+"/"+fileName);
                    if(file.isDirectory() && !Files.isSymbolicLink(file.toPath())){

                        calcFiles(file);
                    }
                }
            }
        }
    }

}