/*
 * @(#)JdbcUtils.java Created on 2004-10-22
 * Copyright (c) 2004 ZDSoft Networks, Inc. All rights reserved.
 * $Id: JdbcUtils.java,v 1.14 2008/07/31 11:25:05 huangwj Exp $
 */
package net.zdsoft.framework.utils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * JDBC工具类.
 * 
 * @author liangxiao
 * @author huangwj
 * @author yukh
 * @version $Revision: 1.14 $, $Date: 2008/07/31 11:25:05 $
 */
public final class JdbcUtils {

    private JdbcUtils() {
    }

    /**
     * 获得成对出现的第一个关键字对应的关键字的位置
     * 
     * @param str
     * @param keyword
     *            关键字，例如：select
     * @param oppositeKeyword
     *            对应的关键字，例如：from
     * @return 第一个关键字对应的关键字的位置
     */
    public static int getFirstPairIndex(String str, String keyword, String oppositeKeyword) {
        ArrayList<PairKeyword> keywordArray = new ArrayList<PairKeyword>();
        int index = -1;
        while ((index = str.indexOf(keyword, index)) != -1) {
            keywordArray.add(new PairKeyword(keyword, index));
            index += keyword.length();
        }

        index = -1;
        while ((index = str.indexOf(oppositeKeyword, index)) != -1) {
            keywordArray.add(new PairKeyword(oppositeKeyword, index));
            index += oppositeKeyword.length();
        }

        if (keywordArray.size() < 2) {
            return -1;
        }

        Collections.sort(keywordArray, new PairKeywordComparator());

        PairKeyword firstKeyword = (PairKeyword) keywordArray.get(0);
        if (!firstKeyword.getName().equals(keyword)) {
            return -1;
        }

        while (keywordArray.size() > 2) {
            boolean hasOpposite = false;
            for (int i = 2; i < keywordArray.size(); i++) {
                PairKeyword keyword0 = (PairKeyword) keywordArray.get(i - 1);
                PairKeyword keyword1 = (PairKeyword) keywordArray.get(i);
                if (keyword0.getName().equals(keyword)
                        && keyword1.getName().equals(oppositeKeyword)) {
                    keywordArray.remove(i);
                    keywordArray.remove(i - 1);
                    hasOpposite = true;
                    break;
                }
            }
            if (!hasOpposite) {
                return -1;
            }
        }

        if (keywordArray.size() != 2) {
            return -1;
        }

        PairKeyword lastKeyword = (PairKeyword) keywordArray.get(1);
        if (!lastKeyword.getName().equals(oppositeKeyword)) {
            return -1;
        }

        return lastKeyword.getIndex();
    }

    /**
     * 取得填充参数后的sql
     * 
     * @param preparedSQL
     *            预编译sql
     * @param args
     *            参数数组
     * @return 填充参数后的sql
     */
    public static String getSQL(String preparedSQL, Object[] args) {
        if (args == null || args.length == 0) {
            return preparedSQL;
        }

        StringBuilder sql = new StringBuilder();

        int index = 0;
        int parameterIndex = 0;

        while ((index = preparedSQL.indexOf('?')) > 0) {

            // 判断?是否是字符串中的字符，而非占位符---------------------
            boolean sign = false;
            char[] arr = preparedSQL.substring(0, index).trim().toCharArray();
            for (int i = arr.length - 1; i >= 0; i--) {
                char c = arr[i];
                if (c == '\'') {
                    int commaIndex = preparedSQL.substring(index + 1).indexOf("'") + 2;
                    sql.append(preparedSQL.substring(0, index + commaIndex));
                    preparedSQL = preparedSQL.substring(index + commaIndex);
                    sign = true;
                    break;
                }
            }

            if (sign)
                continue;
            // -----------------------------------------------------

            sql.append(preparedSQL.substring(0, index));
            preparedSQL = preparedSQL.substring(index + 1);

            Object arg = args[parameterIndex++];

            if (arg == null) {
                sql.append("null");
            }
            else if (arg instanceof String) {
                sql.append("'");
                sql.append(arg);
                sql.append("'");
            }
            else if (arg instanceof java.util.Date) {
                sql.append("'");
                sql.append(new SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) arg));
                sql.append("'");
            }
            else {
                sql.append(arg);
            }
        }

        sql.append(preparedSQL);

        return sql.toString();
    }

    /**
     * 取得执行count的sql
     * 
     * @param sql
     *            执行查询的sql
     * @return 执行count的sql
     */
    public static String getCountSQL(String sql) {
        String lowerCaseSQL = sql.toLowerCase();
        int index = lowerCaseSQL.lastIndexOf(" order ");
        int index_from = lowerCaseSQL.lastIndexOf(" from ");
        if (index != -1 && index > index_from) {
            sql = sql.substring(0, index);
        }

        int fromIndex = getFirstPairIndex(lowerCaseSQL, "select ", " from ");
        if (fromIndex == -1) {
            throw new IllegalArgumentException("Could not getCountSQL[" + sql + "]");
        }

        return "SELECT COUNT(1)" + sql.substring(fromIndex);

        // return "SELECT COUNT(1)"
        // + sql.substring(lowerCaseSQL.indexOf(" from "));
    }

    /**
     * 根据参数个数生成IN括弧里面的部分sql，包含括弧
     * 
     * @param size
     *            参数个数
     * @return IN括弧里面的部分sql
     */
    public static String getInSQL(int size) {
        StringBuffer inSQL = new StringBuffer();

        inSQL.append("(");
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                inSQL.append("?");
            }
            else {
                inSQL.append(",?");
            }
        }
        inSQL.append(")");

        return inSQL.toString();
    }

    /**
     * 控制带IN子句的SQL语句中IN子句参数数目最多为300个(ASE最大限制数), 若超出就分批执行.
     * (用于解决带IN子句的SQL中IN子句参数数目超出最大限制数时出错的问题.)
     * 
     * @param inSQL
     *            带"IN"的sql语句, e.g. SELECT * FROM table_name WHERE field_name IN
     * @param inArgs
     *            IN子句中的所有参数
     * @param otherArgs
     *            其他参数
     * @param processor
     *            处理每次SQL执行结果的接口, 例如实现中可获取每次查询结果, 然后将它们累加
     */
    public static void executeInSQL(String inSQL, Object[] inArgs, Object[] otherArgs,
            InSQLProcessor processor) {
        if (inArgs == null || inArgs.length == 0) {
            return;
        }

        // IN子句中最多允许的参数数目
        int inArgsMaxNum = 300;

        // 其他参数数目
        int otherArgsNum = (otherArgs == null) ? 0 : otherArgs.length;

        // 查询执行的次数
        int execNum = (inArgs.length % inArgsMaxNum == 0) ? inArgs.length / inArgsMaxNum
                : inArgs.length / inArgsMaxNum + 1;

        // 分批执行SQL
        for (int i = 0; i < execNum; i++) {
            // 每次执行SQL时IN子句中的参数数目
            // 如果是最后一次执行的SQL, 参数计算有区别
            int inArgsNum = ((i + 1) == execNum) ? inArgs.length - inArgsMaxNum * i : inArgsMaxNum;

            String sql = inSQL + JdbcUtils.getInSQL(inArgsNum); // 产生sql语句

            int count = inArgsNum + otherArgsNum; // 每次执行时总的参数数目
            Object[] args = new Object[count]; // 总的参数

            // 初始化其他参数
            for (int j = 0; j < otherArgsNum; j++) {
                args[j] = otherArgs[j];
            }

            // 每次执行时IN子句中第一个参数在数组中的索引
            int startParamIndex = inArgsMaxNum * i;

            // 初始化IN子句参数
            for (int j = otherArgsNum; j < count; j++) {
                // 注意索引
                args[j] = inArgs[startParamIndex + j - otherArgsNum];
            }

            processor.executeSQL(sql, args); // Call back
        }
    }

    /**
     * 将参数以Object类型填入预制式sql语句中.
     * 
     * @param args
     *            参数
     * @param ps
     *            预制式sql语句对象
     * @throws SQLException
     */
    public static void setParamsToStatement(Object[] args, PreparedStatement ps)
            throws SQLException {
        // Set the parameters
        for (int i = 0, index; i < args.length; i++) {
            index = i + 1;
            if (args[i] instanceof java.util.Date) {
                args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
            }
            ps.setObject(index, args[i]);
        }
    }

    /**
     * 将参数以合适的类型填入预制式sql语句中.
     * 
     * @param args
     *            参数
     * @param argTypes
     *            参数类型
     * @param ps
     *            预制式sql语句对象
     * @throws SQLException
     */
    public static void setSuitedParamsToStatement(Object[] args, int[] argTypes,
            PreparedStatement ps) throws SQLException {
        for (int i = 0, index; i < args.length; i++) {
            index = i + 1;

            if (args[i] == null) {
                ps.setNull(index, argTypes[i]);
                continue;
            }

            switch (argTypes[i]) {

                case Types.INTEGER:
                    ps.setInt(index, ((Integer) args[i]).intValue());
                    break;
                case Types.BOOLEAN:
                    ps.setBoolean(index, ((Boolean) args[i]).booleanValue());
                    break;
                case Types.FLOAT:
                    ps.setFloat(index, NumberUtils.toFloat(args[i].toString()));
                    break;
                case Types.DOUBLE:
                    ps.setDouble(index, NumberUtils.toDouble(args[i].toString()));
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                    ps.setString(index, (String) args[i]);
                    break;
                case Types.DATE:
                    ps.setDate(index, new Date(((java.util.Date) args[i]).getTime()));
                    break;
                case Types.TIMESTAMP:
                    ps.setTimestamp(index, new Timestamp(((java.util.Date) args[i]).getTime()));
                    break;

                default:
                    ps.setObject(index, args[i]);
                    break;
            }
        }
    }

    /**
     * 从记录集中获得指定列的值
     * 
     * @param columnIndex
     *            列序号，从1开始
     * @param argType
     *            列的类型
     * @param rs
     *            记录集
     * @return 指定列的值
     * @throws SQLException
     */
    public static Object getColumnValueFromResultSet(int columnIndex, int argType, ResultSet rs)
            throws SQLException {
        switch (argType) {
            case Types.INTEGER:
                return new Integer(rs.getInt(columnIndex));
            case Types.BOOLEAN:
                return new Boolean(rs.getBoolean(columnIndex));
            case Types.FLOAT:
                return new Float(rs.getFloat(columnIndex));
            case Types.DOUBLE:
                return new Double(rs.getDouble(columnIndex));
            case Types.CHAR:
            case Types.VARCHAR:
                return rs.getString(columnIndex);
            case Types.DATE:
                return rs.getDate(columnIndex);
            case Types.TIMESTAMP:
                return rs.getTimestamp(columnIndex);
            default:
                return rs.getObject(columnIndex);
        }
    }

    public static void main(String[] args) {
        // System.out
        // .println(ArrayUtils
        // .toString(getSQLArgNames("select * from sys_account where username
        // like ? and password >=?")));

        // System.out
        // .println(getCountSQL("select id,stuid,unitivecode,examcode,stuname,is_allocate,(select result from jw_audit_result where audit_unit_level='4'and business_type=2 and apply_id = a.id ) as borough_state,(select result from jw_audit_result where audit_unit_level='3'and business_type=2 and apply_id = a.id ) as city_state from recruit_allocate as a where a.section = ? and a.rst_subject_id = ? and exists(select 1 from jw_audit_result where apply_id = a.id and audit_unit_level='6' and result in ('2','3') and operate_unit = ? )"));

        System.out.println(getSQL(
                "SELECT * FROM sys_model WHERE unitclass=? AND substr(url,0,instr(url||'?','?') -1)=?",
                new Object[] { 1, "a.action" }));
    }

}
