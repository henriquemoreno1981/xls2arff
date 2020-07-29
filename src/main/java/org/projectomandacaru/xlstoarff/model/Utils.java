package org.projectomandacaru.xlstoarff.model;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String getPath(File file) throws IOException {
        return file.getParentFile().getCanonicalPath();
    }

    public static String getPath(String filename) throws IOException {
        File file = new File(filename);
        return getPath(file);
    }
}
