package net.zdsoft.framework.utils;

import java.util.HashMap;
import java.util.Map;

/* 
 * 产生班级名称和年级名称的基类
 * 
 * <p>ZDSoft学籍系统（stusys）V3.5</p>
 * @author Dongzk
 * @since 1.0
 * @version $Id: NameFactory.java,v 1.3 2006/11/03 03:26:09 dongzk Exp $
 */
public class NameFactory {
    public static final Map<Integer, String> sectionMap;
    public static final Map<Integer, String> yearMap;
    public static final Map<String, Integer> yearStrMap;

    public static final Map<String, String> sectionIdMap;
    public static final Map<String, String> yearIdMap;

    // 初始化静态变量及学段对应关系
    static {
        sectionMap = new HashMap<Integer, String>();
        sectionMap.put(0, "幼");
        sectionMap.put(1, "小");
        sectionMap.put(2, "初");
        sectionMap.put(3, "高");

        yearMap = new HashMap<Integer, String>();
        yearMap.put(new Integer(1), "一");
        yearMap.put(new Integer(2), "二");
        yearMap.put(new Integer(3), "三");
        yearMap.put(new Integer(4), "四");
        yearMap.put(new Integer(5), "五");
        yearMap.put(new Integer(6), "六");
        yearMap.put(new Integer(7), "七");
        yearMap.put(new Integer(8), "八");
        yearMap.put(new Integer(9), "九");

        yearStrMap = new HashMap<String, Integer>();
        yearStrMap.put("一", new Integer(1));
        yearStrMap.put("二", new Integer(2));
        yearStrMap.put("三", new Integer(3));
        yearStrMap.put("四", new Integer(4));
        yearStrMap.put("五", new Integer(5));
        yearStrMap.put("六", new Integer(6));
        yearStrMap.put("七", new Integer(7));
        yearStrMap.put("八", new Integer(8));
        yearStrMap.put("九", new Integer(9));

        sectionIdMap = new HashMap<String, String>();
        sectionIdMap.put("幼", "0");
        sectionIdMap.put("小", "1");
        sectionIdMap.put("初", "2");
        sectionIdMap.put("高", "3");

        yearIdMap = new HashMap<String, String>();
        yearIdMap.put("一", "1");
        yearIdMap.put("二", "2");
        yearIdMap.put("三", "3");
        yearIdMap.put("四", "4");
        yearIdMap.put("五", "5");
        yearIdMap.put("六", "6");
        yearIdMap.put("七", "7");
        yearIdMap.put("八", "8");
        yearIdMap.put("九", "9");


    }

}
