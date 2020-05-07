package net.zdsoft.bigdata.datav;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shenke
 * @since 2018/10/14 11:06
 */
public class DataVStringUtil {

    private static Pattern compress = Pattern.compile("\\s*");
    private static Pattern enter = Pattern.compile("\\n|\\r");

    /**
     * 剔除 回车、换行、空格以达到压缩的目的
     * @param src
     * @return
     */
    public static String compressOfBlank(String src) {
        if (src == null) {
            return null;
        }
        Matcher matcher = compress.matcher(src);
        return matcher.replaceAll("");
    }

    public static String compressOfEnterLine(String src) {
        if (src == null) {
            return null;
        }
        return enter.matcher(src).replaceAll(" ");
    }

}
