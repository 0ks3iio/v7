package net.zdsoft.szxy.operation.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author shenke
 * @since 2019/4/11 上午11:12
 */
public final class PinyinUtils {

    /**
     * 将中文转为拼音
     * @param chineseStr 汉字字符串（可包含数字、英文等）
     * @return
     */
    public static String toHanyuPinyi(String chineseStr) {
        if (StringUtils.isBlank(chineseStr)) {
            return StringUtils.EMPTY;
        }
        char[] cs = chineseStr.toCharArray();
        StringBuilder names = new StringBuilder();
        for(char c : cs){
            names.append(toHanyuPinyinForChar(c));
        }
        return names.toString();
    }

    public static String toHanyuPinyinForChar(char chineseChar) {
        if (CharUtils.isAscii(chineseChar)) {
            return String.valueOf(chineseChar);
        }
        String[] py = PinyinHelper.toHanyuPinyinStringArray(chineseChar);
        if (py != null){
            String t = py[0];
            if (NumberUtils.isCreatable(t.substring(t.length() - 1))){
                t = t.substring(0, t.length() - 1);
            }
            return t;
        }
        return "";
    }

    /**
     * 获取汉字的拼音的首字母
     * @param chineseChar
     * @return
     */
    public static String toHanyuPinyinForFirstLetter(char chineseChar) {
        if (CharUtils.isAscii(chineseChar)) {
            return String.valueOf(chineseChar);
        }
        String[] py = PinyinHelper.toHanyuPinyinStringArray(chineseChar);
        if (py != null){
            return py[0].substring(0, 1);
        }
        return "";
    }
}
