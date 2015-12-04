package com.gnatiuk.searcher.ui.utils;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sgnatiuk on 12/4/15.
 */
public final class ImageLoader {
    public static String IMAGE_RESOURCES = "searcher-ui/src/main/resources/img";
    private static final Map<String, Image> imageMap = new HashMap<>();

    private static Image BROKEN_IMAGE;
    static {
        try {
            BROKEN_IMAGE = new Image(getImageUrl(Paths.get(IMAGE_RESOURCES, "broken_image.gif")).toString());
        } catch (FileNotFoundException|MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static Image loadImageByPath(Path imagePath){
        Image loadedImage = imageMap.get(imagePath.toString());
        if(loadedImage != null){
            return loadedImage;
        }

        try {
            loadedImage = getImage(getImageUrl(imagePath));
        } catch (FileNotFoundException|MalformedURLException e) {
            loadedImage = BROKEN_IMAGE;
            System.err.println(e.getMessage());
        }finally {
            imageMap.put(imagePath.toString(), loadedImage);
        }

        return loadedImage;
    }

    private static Image getImage(URL imageUrl){
        return new Image(imageUrl.toString());
    }

    private static URL getImageUrl(Path filePath) throws FileNotFoundException, MalformedURLException {
        if(Files.exists(filePath)){
            return filePath.toUri().toURL();
        }else{
            throw new FileNotFoundException("Image does not exists. Please, check path: "+filePath);
        }
    }


    private ImageLoader(){}
}
