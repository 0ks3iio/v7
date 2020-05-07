/**
 * FileName: ChartCategoryUtilsTest.java
 * Author:   shenke
 * Date:     2018/5/28 下午1:41
 * Descriptor:
 */
package net.zdsoft;

import net.zdsoft.bigdata.data.code.ChartClassification;
import net.zdsoft.bigdata.data.code.ChartClassificationUtils;
import org.junit.Test;

import java.util.List;

/**
 * @author shenke
 * @since 2018/5/28 下午1:41
 */
public class ChartCategoryUtilsTest {

    @Test
    public void _getEnables() {
        List<ChartClassification> c = ChartClassificationUtils.getEnableClassifications("http://localhost:8081/");
        c.forEach(e -> System.out.println(e.toString()));
    }
}
