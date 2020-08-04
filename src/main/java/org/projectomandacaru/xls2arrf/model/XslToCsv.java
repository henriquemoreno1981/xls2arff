package org.projectomandacaru.xls2arrf.model;

import org.apache.poi.ss.usermodel.*;
import org.projectomandacaru.xls2arrf.utils.CsvNomalize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.AbstractInstance;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XslToCsv implements SpreadSheetToCsv {
    private static Logger LOGGER = LoggerFactory.getLogger(XslToCsv.class);

    private String convertExcelToCSV(Sheet sheet, String sheetName, String path) {
        String resultPath = null;
        StringBuilder data = new StringBuilder();
        try {
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                boolean firstCell = true;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (!firstCell) {
                        data.append(SEPARATOR);
                    }
                    CellType type = cell.getCellTypeEnum();
                    switch (type) {
                        case BOOLEAN:
                            data.append(cell.getBooleanCellValue());
                            break;
                        case NUMERIC:
                            data.append(BigDecimal.valueOf(cell.getNumericCellValue()) //
                                    .setScale(AbstractInstance.s_numericAfterDecimalPoint, RoundingMode.HALF_UP) //
                                    .doubleValue());
                            break;
                        case STRING:
                            String val = CsvNomalize.normalize(cell.getStringCellValue());
                            data.append(val);
                            break;
                        case BLANK:
                            break;
                        default:
                            data.append(cell + "");
                    }
                    firstCell = false;
                }
                data.append(SpreadSheetToCsv.BREAK_LINE);
            }
            resultPath = path + File.separatorChar + sheetName + ".csv";
            Files.write(Paths.get(resultPath),
                    data.toString().getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return resultPath;
    }

    @Override
    public String[] readFileToCsv(String filename, String outputPath, String[] sheetNames) throws IOException {
        InputStream inp = null;
        List<String> files = new ArrayList<>();
        try {
            inp = new FileInputStream(filename);
            Workbook wb = WorkbookFactory.create(inp);

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                String sheetName = wb.getSheetAt(i).getSheetName();
                boolean hasSheet = false;
                if (sheetNames != null && sheetName.length() > 0) {
                    for (String name : sheetNames) {
                        if ( sheetName.equalsIgnoreCase(name)) {
                            hasSheet = true;
                        }
                    }
                }
                if (hasSheet) {
                    LOGGER.info(String.format("Gerando arquivo CSV: '%s.csv'", sheetName));
                    this.convertExcelToCSV(wb.getSheetAt(i), wb.getSheetAt(i).getSheetName(), outputPath);
                    files.add(outputPath + File.separatorChar + sheetName + ".csv");
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return files.toArray(new String[]{});
    }
}
