/*
 * @(#)Pagination.java Created on 2004-6-7
 * Copyright (c) 2004 ZDSoft Networks, Inc. All rights reserved.
 * $Id: Pagination.java,v 1.11 2008/07/31 11:29:10 huangwj Exp $
 */
package net.zdsoft.framework.entity;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;


/**
 * 分页类.
 *
 * @author liangxiao
 * @author wangjw
 * @author yukh
 * @author huangwj
 * @version $Revision: 1.11 $, $Date: 2008/07/31 11:29:10 $
 */
public class Pagination {

    private static Logger logger = LoggerFactory.getLogger(Pagination.class);

    private String id = "";// 分页信息的标识，会作为参数的后缀，在多个pagination共存的时候有用

    private int pageIndex; // 当前是第几页
    private int maxPageIndex; // 一共有多少页
    private int maxRowCount; // 一共有多少行
    private int pageSize; // 每页有多少行
    private int curRowNum; // 当前起始记录序号
    private boolean isUseCursor = false;
    private boolean isUseDBPage = true;// 是否使用数据库分页算法
    private String sort = null;
    private boolean desc = false;

    private Map<String, String> params = new HashMap<String, String>();
    private String uri;

    // 每页默认显示20条
    public static final int DEFAULT_PAGE_SIZE = 20;

    public Pagination() {

    }

    /**
     * 构造方法
     *
     * @param pageSize    每页多少行
     * @param isUseCursor 是否使用游标
     */
    public Pagination(int pageSize, boolean isUseCursor) {
        this(1, pageSize, isUseCursor);
    }

    /**
     * 构造方法
     *
     * @param pageIndex   当前是第几页
     * @param pageSize    每页多少行
     * @param isUseCursor 是否使用游标
     */
    public Pagination(int pageIndex, int pageSize, boolean isUseCursor) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.isUseCursor = isUseCursor;
    }

    /**
     * 初始化，计算出一共有多少页和当前起始记录序号
     */
    public void initialize() {
        // 得到总共页数
        if (maxRowCount % pageSize == 0) {
            maxPageIndex = maxRowCount / pageSize;
        } else {
            maxPageIndex = maxRowCount / pageSize + 1;
        }
        // 校验当前页参数
        if (pageIndex > maxPageIndex) {
            pageIndex = maxPageIndex;
        } else if (pageIndex < 1) {
            pageIndex = 1;
        }
        // 得到当前起始记录序号
        if (maxRowCount == 0) {
            curRowNum = 0;
            pageIndex = 0;
        } else {
            curRowNum = (pageIndex - 1) * pageSize + 1;
        }

        if (logger.isInfoEnabled()) {
            logger.info("page: " + pageIndex + "/" + maxPageIndex + " row: " + curRowNum + "/"
                    + maxRowCount + " per: " + pageSize + " cursor: " + isUseCursor);
        }
    }

    /**
     * 取得分页对象的id
     *
     * @return 分页对象的id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置分页对象的id
     *
     * @param id 分页对象的id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 设置每页多少行
     *
     * @param pageSize 每页多少行
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置当前是第几页
     *
     * @param pageIndex 当前是第几页
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 判断是否降序排列
     *
     * @return 是true，否则false
     */
    public boolean isDesc() {
        return desc;
    }

    /**
     * 设置是否降序排列
     *
     * @param desc 是否降序排列
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    /**
     * 取得排序字段
     *
     * @return 排序字段
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置排序字段
     *
     * @param sort 排序字段
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 取得当前是第几页
     *
     * @return 当前是第几页
     */
    public int getPageIndex() {
        if (pageIndex <= 0)
            pageIndex = 1;
        return pageIndex;
    }

    /**
     * 取得当前起始记录序号
     *
     * @return 当前起始记录序号
     */
    public int getCurRowNum() {
        return curRowNum;
    }

    /**
     * 设置是否使用游标
     *
     * @param isUseCursor 是否使用游标
     */
    public void setUseCursor(boolean isUseCursor) {
        this.isUseCursor = isUseCursor;
    }

    /**
     * 判断是否使用游标
     *
     * @return 是true，否则false
     */
    public boolean isUseCursor() {
        return isUseCursor;
    }

    /**
     * 取得一共有多少页
     *
     * @return 一共有多少页
     */
    public int getMaxPageIndex() {
        if (maxPageIndex == 0 && pageSize > 0) {
            maxPageIndex = maxRowCount / pageSize;
            if (maxRowCount % pageSize != 0)
                maxPageIndex++;
        }
        return maxPageIndex;
    }

    /**
     * 取得一共有多少行
     *
     * @return 一共有多少行
     */
    public int getMaxRowCount() {
        return maxRowCount;
    }

    /**
     * 取得每页有多少行
     *
     * @return 每页有多少行
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置一共有多少行
     *
     * @param maxRowCount 一共有多少行
     */
    public void setMaxRowCount(int maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public boolean isUseDBPage() {
        return isUseDBPage;
    }

    public void setUseDBPage(boolean isUseDBPage) {
        this.isUseDBPage = isUseDBPage;
    }

    public static Pageable toPageable(Pagination page) {
        return new PageRequest(page.getPageIndex() - 1, page.getPageSize());
    }

    public static Pageable toPageable(String pagestr) {
        Pagination page = dc(pagestr);
        return new PageRequest(page.getPageIndex() - 1, page.getPageSize());
    }

    public Pageable toPageable() {
        return new PageRequest(this.getPageIndex() - 1, this.getPageSize());
    }

    public static Pagination toPagination(Pageable page) {
        return new Pagination(page.getPageNumber() + 1, page.getPageSize(), false);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static Pagination dc(String data) {
        return JSON.parseObject(data, Pagination.class, Feature.IgnoreNotMatch);
    }

}
