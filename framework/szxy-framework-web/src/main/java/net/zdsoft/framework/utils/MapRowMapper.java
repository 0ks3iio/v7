/*
 * @(#)MapRowMapper.java  1.0 2004-12-13
 * Copyright (c) 2004 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/keel/src/net/zdsoft/keel/dao/MapRowMapper.java,v 1.3 2005/01/17 08:56:19 yukh Exp $
 */
package net.zdsoft.framework.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 此接口用于要将结果集以map的形式存放的情况.
 * 
 * @author huangwj
 * @version $Revision: 1.3 $, $Date: 2005/01/17 08:56:19 $
 */
public interface MapRowMapper<K, V> {

    /**
     * 产生要放入map中的可以标识这条记录的某个key, <br>
     * 例如可以以这条记录中的某个字段的值作为key等等.
     * 
     * @param rs
     *            结果集
     * @param rowNum
     *            当前记录行号
     * @return 放入map的关键字
     */
    public abstract K mapRowKey(ResultSet rs, int rowNum)
            throws SQLException;

    /**
     * 产生要放入map中的以 <code>mapKey()</code> 方法的返回值为key的某个value, <br>
     * 例如可以以这条记录中的某个字段的值作为value, 或者一个值对象等等.
     * 
     * @param rs
     *            结果集
     * @param rowNum
     *            当前记录行号
     * @return 放入map的值
     */
    public abstract V mapRowValue(ResultSet rs, int rowNum)
            throws SQLException;

}
