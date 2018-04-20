package com.example.thaib.testtab.Utils;

import java.io.File;
import java.io.FileFilter;

public class FolderFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory() && !file.getName().startsWith(".");
    }
}
