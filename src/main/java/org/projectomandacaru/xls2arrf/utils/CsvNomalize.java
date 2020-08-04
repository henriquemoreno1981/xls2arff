package org.projectomandacaru.xls2arrf.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class CsvNomalize {
    public static String normalizeSymbolsAndAccents(String str) {
        str = org.apache.commons.lang3.StringUtils.defaultString(str);
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        nfdNormalizedString = pattern.matcher(nfdNormalizedString).replaceAll("");
        nfdNormalizedString = nfdNormalizedString.replaceAll("[^a-zA-Z0-9\\s]", "");
        return nfdNormalizedString;
    }

    public static String normalize(String val) {
        val = normalizeSymbolsAndAccents(val).replaceAll("\\s+", "-").toLowerCase();
        return val;
    }
}
