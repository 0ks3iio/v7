/*
 * @(#)HibernateDataSource.java Created on 2006-12-21
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/keel/src/net/zdsoft/keelcnet/dao/HibernateDataSource.java,v
 * 1.5 2008/10/21 02:03:34 zhangza Exp $
 */
package net.zdsoft.framework.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class HibernateDataSource extends BasicDataSource {
    // private final Logger log =
    // LoggerFactory.getLogger(HibernateDataSource.class);
    // private Properties properties;

    public HibernateDataSource() {
    }

    @Override
    public Connection getConnection() throws SQLException {
        // TODO Auto-generated method stub
        return super.getConnection();
    }

    public void setProperties(Properties properties) {
        // this.properties = properties;
        String value;
        setDriverClassName(properties.getProperty("hibernate.connection.driver_class"));
        setUrl(properties.getProperty("hibernate.connection.url"));
        setUsername(properties.getProperty("hibernate.connection.username"));
        setPassword(properties.getProperty("hibernate.connection.password"));

        value = properties.getProperty("hibernate.dbcp.maxActive");
        if (null != value && !value.trim().equals("")) {
            setMaxActive(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.maxWait");
        if (null != value && !value.trim().equals("")) {
            setMaxWait(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.maxIdle");
        if (null != value && !value.trim().equals("")) {
            setMaxIdle(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.minIdle");
        if (null != value && !value.trim().equals("")) {
            setMinIdle(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.autoCommit");
        if (null != value && !value.trim().equals("")) {
            setDefaultAutoCommit(Boolean.parseBoolean(value));
        }

        value = properties.getProperty("hibernate.dbcp.validationQuery");
        if (null != value && !value.trim().equals("")) {
            setValidationQuery(value);
        }

        value = properties.getProperty("hibernate.dbcp.testOnBorrow");
        if (null != value && !value.trim().equals("")) {
            setTestOnBorrow(Boolean.parseBoolean(value));
        }

        value = properties.getProperty("hibernate.dbcp.testOnReturn");
        if (null != value && !value.trim().equals("")) {
            setTestOnReturn(Boolean.parseBoolean(value));
        }

        value = properties.getProperty("hibernate.dbcp.TestWhileIdle");
        if (null != value && !value.trim().equals("")) {
            setTestWhileIdle(Boolean.parseBoolean(value));
        }

        value = properties.getProperty("hibernate.dbcp.timeBetweenEvictionRunsMillis");
        if (null != value && !value.trim().equals("")) {
            setTimeBetweenEvictionRunsMillis(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.numTestsPerEvictionRun");
        if (null != value && !value.trim().equals("")) {
            setNumTestsPerEvictionRun(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.minEvictableIdleTimeMillis");
        if (null != value && !value.trim().equals("")) {
            setMinEvictableIdleTimeMillis(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.removeAbandoned");
        if (null != value && !value.trim().equals("")) {
            setRemoveAbandoned(Boolean.parseBoolean(value));
        }

        value = properties.getProperty("hibernate.dbcp.removeAbandonedTimeout");
        if (null != value && !value.trim().equals("")) {
            setRemoveAbandonedTimeout(Integer.parseInt(value));
        }

        value = properties.getProperty("hibernate.dbcp.logAbandoned");
        if (null != value && !value.trim().equals("")) {
            setLogAbandoned(Boolean.parseBoolean(value));
        }
    }

}
