/* 
 * @(#)SQLUtils.java    Created on 2008-12-31
 * Copyright (c) 2008 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.utils;

import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;

/**
 * SQL工具类
 */
public class SQLUtils {

    public final static String FROM = " from ";
    public final static String SELECT = "select ";
    public final static String WHERE = " where ";
    public final static String DISTINCT = " distinct ";
    public final static String HAVING = " having ";
    public final static String GROUP_BY = " group by ";
    public final static String ORDER_BY = " order by ";
    public final static String AND = " and ";
    public final static String OR = " or ";
    public final static String IN = " in ";
    public final static String COUNT = " count ";
    public final static String SUM = " sum ";
    public final static String SUBSTR = " substr ";
    public final static String AS = " as ";
    
    /**
	 * 常用的符号
	 */
	public static final String COMMA =",";//
	public static final String LEFT_BRACKETS  ="( ";//
	public static final String RIGHT_BRACKETS =" )";//
	public static final String SINGLE_QUOTATION ="'";//
	public static final String QUESTION_MARK =" ? ";//
	
	
    /**
     * 
     * @param cond
     * @param first
     * @param second
     * @return
     */
    public static String buildIfExists(String cond, String first, String second) {
        StringBuffer sb = new StringBuffer();
        sb.append("declare");
        sb.append("\n");
        sb.append("row_num number;");
        sb.append("\n");
        sb.append("begin");
        sb.append("\n");
        sb.append("select count(*) into row_num ");
        sb.append(cond);
        sb.append(";\n");
        sb.append("if(row_num > 0) then ");
        sb.append(first);
        sb.append(";");
        sb.append("\n");
        sb.append("else");
        sb.append("\n");
        sb.append(second);
        sb.append(";");
        sb.append("\n");
        sb.append("end if;");
        sb.append("\n");
        sb.append("end;");

        return sb.toString();

    }

    /**
     * SELECT * FROM eduadm_subject WHERE unit_id IN ? ORDER BY order_id ASC
     * 把args变成('111','222')后，加入第一个IN后的?中
     * 
     * @param sql
     * @param args
     * @return
     */
    public static String addParmToIn(String sql, String[] args) {
        return addParmToIn(sql, toSQLInString(args));
    }

    /**
     * SELECT * FROM eduadm_subject WHERE unit_id IN ? ORDER BY order_id ASC
     * 把inStr加入第一个IN后的?中
     * 
     * @param sql
     * @param inStr
     * @return
     */
    private static String addParmToIn(String sql, String inStr) {
        Stack<Character> stack = new Stack<Character>();
        char c;
        int repStart;

        // 找到IN后的？
        for (int i = 0; i < sql.length(); i++) {

            c = sql.charAt(i);
            // 若为对应的符号
            if (stack.isEmpty() != true && stack.peek().equals(c)) {
                stack.pop();
                continue;
            }
            // 忽略单引号和双引号中的内容
            if (stack.isEmpty() != true) {
                continue;
            }
            // 若为单引号或双引号则记录
            if (c == '\'' || c == '"') {
                stack.push(c);
                continue;
            }

            // 若找到IN
            if ((c == 'i' || c == 'I') && (sql.charAt(i - 1) == ' ')
                    && (sql.charAt(i + 1) == 'n' || sql.charAt(i + 1) == 'N')
                    && (i + 2 >= sql.length() || sql.charAt(i + 2) == ' ')) {
                // 若到了末尾
                if (i + 2 >= sql.length()) {
                    sql += inStr;
                } else {
                    // in后面?开始的位置
                    repStart = sql.indexOf('?', i + 2);
                    // 若没有?就继续往后面找
                    if (repStart < 0)
                        continue;

                    sql = sql.substring(0, repStart) + inStr + sql.substring(repStart + 1);

                }
                break;
            }
        }
        return sql;
    }

    /**
     * 将字符串数组转化为sql中in条件所需格式的字符串 包括括号
     * 
     * @param strs 字符串数组
     * @return
     */
    public static String toSQLInString(String[] strs) {
        String str = "";
        StringBuilder sb = new StringBuilder();
        if (null != strs && strs.length > 0) {
            for (int i = 0; i < strs.length; i++) {
                if (null == strs[i]) {
                    continue;
                } else {
                    sb.append(",'").append(strs[i]).append("'");
                }
            }
            str = sb.toString();
            if (!"".equals(str)) {
                str = str.substring(1);
            }
        }

        if ("".equals(str)) {
            str = "null";
        }

        str = " (" + str + ") ";
        return str;
    }
    
    /**
     * 将字符串数组转化为sql中in条件所需格式的字符串 包括括号
     * 
     * @param strs 字符串数组
     * @param column 字段
     * @param isNotIn 是否not in
     * @return
     */
    public static String toSQLInString(String[] strs, String column,boolean isNotIn){
        StringBuffer str = new StringBuffer(); 
        if (null != strs && strs.length > 0) {
     	   if(strs.length<=500){
     		  if(isNotIn){
     			  str.append(column).append(" not in ").append(toSQLInString(strs));
     		  }else{
     			  str.append(column).append(" in ").append(toSQLInString(strs));
     		  }
     	   }else{
     		   int length = strs.length;
     		   int a = length/500;
     		   int b = length%500;
     		   String[] tempStrs = null;
     		   str.append("(");
     		   for (int i = 0; i < a; i++) {
     			  if(isNotIn){
     				  str.append(column).append(" not in ");
     			  }else{
     				  str.append(column).append(" in ");
     			  }
     			   tempStrs = (String[])ArrayUtils.subarray(strs, i*500, (i+1)*500);
     			   str.append(toSQLInString(tempStrs));
     			   if(i!=a-1){
     				   if(isNotIn){
     					  str.append(" and ");
     				   }else{
     					   str.append(" or ");
     				   }
     			   }
     		   }
     		   if(b!=0){
     			  if(isNotIn){
 					  str.append(" and ").append(column).append(" not in ");
 				   }else{
 					   str.append(" or ").append(column).append(" in ");
 				   }
     			   tempStrs = (String[])ArrayUtils.subarray(strs, a*500, length);
     			   str.append(toSQLInString(tempStrs));
     		   }
     		   str.append(")");
     	   }
         }
        if (str.length()==0) {
     	   str.append(column).append(" in ").append("(null)");
        }
        return str.toString();
 	}

    public static void main(String[] args) {
        System.out.println("=====");

        String cond = " from gz_exam_max_id where type_code = 'TKCJBHZ'";
        String first = "update gz_exam_max_id set max_id = max_id + 1 where type_code = 'TKCJBHZ'";
        String second = "insert into gz_exam_max_id (id, type_code, max_id) values ('4028808F1E8C25AE011E8C336A740025', 'TKCJBHZ', 1)";
        System.out.println(buildIfExists(cond, first, second));

        String sql = "SELECT 'abc' FROM gz_eduadm_subject ? WHERE \"abcv\"unit_id IN ?\" ordby";
        sql = addParmToIn(sql, "(123)");
        System.out.println(sql);
    }

    /**
     * 根据hql查询语句得到count查询语句，分页查询的辅助方法
     * <p>
     * 不支持 distinct & group by
     * 
     * @param hql
     * @return
     */
    public static String CountSQLString(String hql) {
        String hqllower = hql.toLowerCase();
        int fromIndex = hqllower.indexOf(FROM);
        if(fromIndex<0)
        	fromIndex = hqllower.indexOf(FROM.trim());
        int distinctIndex = hqllower.indexOf(DISTINCT);
        int groupbyIndex = hqllower.indexOf(GROUP_BY);
        int orderIndex = hqllower.indexOf(ORDER_BY);
        if (-1 != fromIndex && distinctIndex == -1 && groupbyIndex == -1) {
            StringBuffer countHql = new StringBuffer("select count(*) ")
                    .append((-1 == orderIndex) ? hql.substring(fromIndex) : hql.substring(
                            fromIndex, orderIndex));
            return countHql.toString();
        }
        return null;
    }

}
