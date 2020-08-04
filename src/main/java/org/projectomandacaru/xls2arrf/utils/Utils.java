package org.projectomandacaru.xls2arrf.utils;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String getPath(File file) throws IOException {
        if (file.isFile()) {
            String absolutePath;
            if (file.getParentFile() == null) {
                absolutePath = getAbsolutePath(file.getAbsolutePath(), File.separatorChar);
            } else {
                absolutePath = file.getParentFile().getCanonicalPath();
            }
            return absolutePath;
        } else {
            return file.getCanonicalPath();
        }
    }

    public static String getAbsolutePath(String absolutePath, char separatorChar) {
        return absolutePath.substring(0, absolutePath.lastIndexOf(separatorChar));
    }

    public static String getPath(String filename) throws IOException {
        File file = new File(filename);
        return getPath(file);
    }
}
