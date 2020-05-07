package net.zdsoft.bigdata.data.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.zdsoft.bigdata.data.utils.JacksonDateDeserializer;
import net.zdsoft.framework.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
/**
 * 组合报表（包括看板和报告）
 * @author user
 *
 */
@Entity
@Table(name = "bg_multi_report")
public class MultiReport extends BaseEntity<String> {

	private static final long serialVersionUID = 7929408922173587799L;
	
	public static final int BOARD=6;
	public static final int REPORT=7;

	private String unitId;
	
	private String name;
	
	private int type;//1 看板 2报告
	
	private int orderType;
	
	private int orderId;
	
	private String remark;
	
	private String creatorUserId;
	
    @JsonDeserialize(using = JacksonDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;
    @JsonDeserialize(using = JacksonDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

	// 文件夹字段
	@Transient
	private String folderId;
	// 标签
	@Transient
	private List<String> tagNameList;
	
	@Override
	public String fetchCacheEntitName() {
		return "multiReport";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int  getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public List<String> getTagNameList() {
		return tagNameList;
	}

	public void setTagNameList(List<String> tagNameList) {
		this.tagNameList = tagNameList;
	}

	public String toJSONString() {
		return JSONObject.toJSONString(this);
	}

}
