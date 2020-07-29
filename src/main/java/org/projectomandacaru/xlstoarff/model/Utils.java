package org.projectomandacaru.xlstoarff.model;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String getPath(File file) throws IOException {
        if (file.isFile()) {
            return file.getParentFile().getCanonicalPath();
        } else {
            return file.getCanonicalPath();
        }
    }

    public static String getPath(String filename) throws IOException {
        File file = new File(filename);
        return getPath(file);
    }
}
