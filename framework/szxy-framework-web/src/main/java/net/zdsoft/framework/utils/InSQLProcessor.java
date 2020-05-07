/*
 * @(#)InSQLProcessor.java Created on 2005-5-11
 * Copyright (c) 2005 ZDSoft Networks, Inc. All rights reserved.
 * $Id: InSQLProcessor.java,v 1.4 2008/07/31 11:22:05 huangwj Exp $
 */
package net.zdsoft.framework.utils;

/**
 * SQL执行处理器接口, 用于处理带IN子句的SQL语句中IN中参数过长的问题.
 * 
 * @author huangwj
 * @version $Revision: 1.4 $, $Date: 2008/07/31 11:22:05 $
 */
public interface InSQLProcessor {

    /**
     * 执行SQL的方法.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            语句中的参数
     */
    public void executeSQL(String sql, Object[] args);

}
