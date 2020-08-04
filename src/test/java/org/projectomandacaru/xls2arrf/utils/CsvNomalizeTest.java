package org.projectomandacaru.xls2arrf.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CsvNomalizeTest {
    @Test
    public void normalize() {
        assertThat(CsvNomalize.normalize("Áéíóú 1"), is("aeiou-1"));
        assertThat(CsvNomalize.normalize("Áéíóú          1"), is("aeiou-1"));
        assertThat(CsvNomalize.normalize("Áéíóúd'oeste     1"), is("aeioudoeste-1"));
    }
}