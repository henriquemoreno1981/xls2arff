package org.projectomandacaru.xls2arrf.model;

import java.io.IOException;

public interface SpreadSheetToCsv {
    String SEPARATOR = ",";
    String BREAK_LINE = "\r\n";

    String[] readFileToCsv(String filename, String outputPath, String[] sheetNames) throws IOException;
}
