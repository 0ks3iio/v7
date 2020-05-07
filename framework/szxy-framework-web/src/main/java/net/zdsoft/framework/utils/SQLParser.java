/* 
 * @(#)SQLParser.java    Created on Sep 27, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.utils;

/**
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Sep 27, 2010 3:22:24 PM $
 */
public class SQLParser {

    /**
     * SQL中是否包含子句
     * 
     * @param sql
     * @param key
     */
    public static boolean isContailClause(String sql, String clause) {
        int clauseLen = clause.length();
        sql = sql.toLowerCase();
        
        boolean contail = false;
        int index = -1;
        while ((index = sql.indexOf(clause)) > -1) {
            int nextPos = index + clauseLen;
            
            char[] arr = sql.substring(nextPos).trim().toCharArray();
            int quotesCnt = 0;// 单引号个数            
            for (int i = arr.length - 1; i >= 0; i--) {
                char c = arr[i];
                if (c == '\'') {
                    quotesCnt ++;                   
                }                
            }
            if(quotesCnt % 2 != 0){//不在字符串内              
                sql = sql.substring(nextPos);
            } else {
                char beginChar = sql.charAt(index -1);
                char endChar = sql.charAt(index + clauseLen );
                // 判断是连在一起的orderby（即可能是字段或别名），还是真正的order by子句
                if (beginChar <= ' ' && endChar <= ' ') {
                    contail = true;
                    break;
                }else{
                    sql = sql.substring(nextPos);
                }

            }
        }
        return contail;
    }

    public static void main(String[] args) {
        System.out.println("============");
        String sql = "select id ,'a order by ' from SEPTEST\r\norder\r\nby\r\ntype asc";
//        String sql = "SELECT * FROM gz_eduadm_subject WHERE (unit_id = ? OR (subject_type='1' and is_using='1')) order by subject_type,unit_id,order_id";
        System.out.println(isContailClause(sql, "order"));
        System.out.println("============");
    }
}
