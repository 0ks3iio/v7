/* 
 * @(#)EntityDto.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.dto;

import java.io.Serializable;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 下午09:05:18 $
 */
public class EntityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String columnName;// 字段名
    private String displayName;// 字段中文名
    private int isAutho;// 用户是否有权限用；1：是；0：否
    private int isSensitive; //是否是敏感字段； 1：是；0：否
    private String entityId;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getIsAutho() {
        return isAutho;
    }

    public void setIsAutho(int isAutho) {
        this.isAutho = isAutho;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

	public int getIsSensitive() {
		return isSensitive;
	}

	public void setIsSensitive(int isSensitive) {
		this.isSensitive = isSensitive;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
}
