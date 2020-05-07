/* 
 * @(#)InterfaceDto.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午02:21:02 $
 */
public class InterfaceDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;// 接口类别
    private String typeName;
    private int sensitiveNum;// 敏感字段数
    private int commonNum; // 普通字段数
    private List<EntityDto> entitys;//
    private int count; //接口次数
    private Integer maxNumDay;  //每天的最大调用次数
    private Integer limitEveryTime;  //获取接口最大数量
    private String id;
    private String url;
    private String applyId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSensitiveNum() {
        return sensitiveNum;
    }

    public void setSensitiveNum(int sensitiveNum) {
        this.sensitiveNum = sensitiveNum;
    }

    public List<EntityDto> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<EntityDto> entitys) {
        this.entitys = entitys;
    }

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Integer getMaxNumDay() {
		return maxNumDay;
	}

	public void setMaxNumDay(Integer maxNumDay) {
		this.maxNumDay = maxNumDay;
	}

	public Integer getLimitEveryTime() {
		return limitEveryTime;
	}

	public void setLimitEveryTime(Integer limitEveryTime) {
		this.limitEveryTime = limitEveryTime;
	}

	public int getCommonNum() {
		return commonNum;
	}

	public void setCommonNum(int commonNum) {
		this.commonNum = commonNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
}
