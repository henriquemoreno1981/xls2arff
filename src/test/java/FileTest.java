import org.junit.jupiter.api.Test;
import org.projectomandacaru.xls2arrf.utils.Utils;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class FileTest {
    @Test
    public void test() throws Exception {
        String filename = "../resources/tmp.txt";
        File file = new File(filename);
        assertThat(file.getCanonicalPath(), containsString(Utils.getPath(filename)));
    }
}
