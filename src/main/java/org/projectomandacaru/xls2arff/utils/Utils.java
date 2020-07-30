package org.projectomandacaru.xls2arff.utils;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String getPath(File file) throws IOException {
        if (file.isFile()) {
            String absolutePath = "";
            if ( file.getParentFile() == null ) {
                absolutePath = file.getAbsolutePath();
                absolutePath = absolutePath.substring(absolutePath.lastIndexOf(File.separatorChar));
            } else {
                absolutePath = file.getParentFile().getCanonicalPath();
            }
            return absolutePath;
        } else {
            return file.getCanonicalPath();
        }
    }

    public static String getPath(String filename) throws IOException {
        File file = new File(filename);
        return getPath(file);
    }
}
