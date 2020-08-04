package org.projectomandacaru.xls2arff.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UtilsTest {
    @Test
    public void test() {
        assertThat(Utils.getAbsolutePath("C:\\a\\b\\c\\d\\teste.txt", '\\'), is("C:\\a\\b\\c\\d"));
        assertThat(Utils.getAbsolutePath("/a/b/c/d/teste.txt", '/'), is("/a/b/c/d"));

    }
}