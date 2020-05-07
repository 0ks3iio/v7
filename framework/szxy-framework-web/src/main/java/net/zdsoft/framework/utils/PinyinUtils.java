package net.zdsoft.framework.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class PinyinUtils {

    /**
     * 取得汉语拼音字母，如果是不可转化的中文，如全角符号，则会自动过滤
     * @param chineseStr 要转化的字符串（含中文、英语、数字等）
     * @param filterStrs 要过滤的字符
     * @param firstLetter 是否取首字母
     * @return
     */
    public static String toHanyuPinyin(String chineseStr, String[] filterStrs, boolean firstLetter){
        if (StringUtils.isBlank(chineseStr))
            return "";
        char[] cs = chineseStr.toCharArray();
        String names = "";
        for(char c : cs){
            if (filterStrs != null && ArrayUtils.contains(filterStrs, c))
                continue;
            if (CharUtils.isAscii(c)){
                names += c;
            }
            else{
                String[] py = PinyinHelper.toHanyuPinyinStringArray(c);
                if (py != null){
                    if (firstLetter){
                        names += PinyinHelper.toHanyuPinyinStringArray(c)[0].substring(0, 1);
                    }
                    else{
                        String t = PinyinHelper.toHanyuPinyinStringArray(c)[0];
                        if (NumberUtils.isNumber(t.substring(t.length() - 1))){
                            t = t.substring(0, t.length() - 1);
                        }
                        names += t;
                    }
                }
            }
        }
        return names;
    }
    
    /**
     * 取得汉语拼音首字母，如果是不可转化的中文，如全角符号，则会自动过滤
     * @param chineseStr 要转化的字符串（含中文、英语、数字等）
     * @param firstLetter 是否取首字母
     * @return
     */
    public static String toHanyuPinyin(String chineseStr, boolean firstLetter){
        return toHanyuPinyin(chineseStr, null, firstLetter);
    }
}
