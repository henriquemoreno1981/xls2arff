package org.projectomandacaru.xls2arrf.utils;

import org.apache.commons.io.FilenameUtils;
import org.projectomandacaru.xls2arrf.model.SpreadSheetToCsv;
import org.projectomandacaru.xls2arrf.model.XslToCsv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UnknownFormatConversionException;

public class SpreadSheetToCsvFactory {

    private static Logger logger = LoggerFactory.getLogger(SpreadSheetToCsvFactory.class);

    public static SpreadSheetToCsv factory(String filename) throws UnknownFormatConversionException {
        String ext = FilenameUtils.getExtension(filename).toLowerCase();
        logger.trace(String.format("Extensão '%s' do arquivo '%s'", ext, filename));
        switch ( ext ) {
            case "xsl":
            case "xlsx":
                return new XslToCsv();
            default:
                throw new UnknownFormatConversionException(String.format("Extensão %s não foi reconhecido", ext));
        }
    }
}
