package com.example.thaib.testtab.album;

import com.example.thaib.testtab.Utils.ImageFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

/**
 * Created by thaib on 17/04/2018.
 */

public class Album {
    private File file;
    private LinkedList<File> images;
    private static FileFilter imageFilter = new ImageFileFilter();

    public Album(File file) {
        this.file = file;
        images=new LinkedList<>();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        loadImagesFile();
    }

    public LinkedList<File> getImages() {
        return images;
    }

    public void loadImagesFile() {
        images = new LinkedList<>();
        if(file != null && file.exists()){
            File[] files = file.listFiles(imageFilter);
        }
    }



    public String getName() {
        if (file != null)
            return file.getName();
        return null;
    }

    public int getSize() {
        if (images != null)
            return images.size();
        return 0;
    }

    public File getImageAt(int index) {
        return images.get(index);
    }

    public File getLastestImage() {
        if(images.size() > 0)
            return images.get(0);
        return null;
    }

    public void addImage(File img){
        images.add(img);
    }

    public void clearAlbum(){
        this.images.clear();
    }

    public void setImages(LinkedList<File> images) {
        this.images = images;
    }
}
