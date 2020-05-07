/* 
 * @(#)RowMapper.java    Created on 2005-9-28
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/keel/src/net/zdsoft/keel/dao/MixedRowMapper.java,v 1.2 2007/01/11 09:15:14 liangxiao Exp $
 */
package net.zdsoft.framework.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 混合型RowMapper，把MapRowMapper、MultiRowMapper、SingleRowMapper的方法集中到一起
 * 
 * @author liangxiao
 * @version $Revision: 1.2 $, $Date: 2007/01/11 09:15:14 $
 */
public abstract class MixedRowMapper<K, T> {

    /**
     * 映射单行记录
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    public abstract T mapSingleRow(ResultSet rs) throws SQLException;

    /**
     * 映射多行记录
     * 
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    public abstract T mapMultiRow(ResultSet rs, int rowNum)
            throws SQLException;

    /**
     * 以Map方式映射记录的键
     * 
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    public abstract K  mapRowKey(ResultSet rs, int rowNum)
            throws SQLException;

    /**
     * 以Map方式映射记录对象
     * 
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    public abstract T mapRowValue(ResultSet rs, int rowNum)
            throws SQLException;

    /**
     * 转换成SingleRowMapper
     * 
     * @return
     */
    public SingleRowMapper<T> beSingle() {
        return new SingleRowMapper<T>() {

            public T mapRow(ResultSet rs) throws SQLException {
                return mapSingleRow(rs);
            }

        };
    }

    /**
     * 转换成MultiRowMapper
     * 
     * @return
     */
    public MultiRowMapper<T> beMulti() {
        return new MultiRowMapper<T>() {

            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapMultiRow(rs, rowNum);
            }

        };
    }

    /**
     * 转换成MapRowMapper
     * 
     * @return
     */
    public MapRowMapper<K, T> beMap() {
        return new MapRowMapper<K, T>() {

            public K mapRowKey(ResultSet rs, int rowNum)
                    throws SQLException {
                return mapRowKey(rs, rowNum);
            }

            public T mapRowValue(ResultSet rs, int rowNum)
                    throws SQLException {
                return mapRowValue(rs, rowNum);
            }

        };
    }

}
