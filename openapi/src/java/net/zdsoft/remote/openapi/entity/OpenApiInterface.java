package net.zdsoft.remote.openapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_openapi_interface")
public class OpenApiInterface extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String type;
    private String uri;
    @Column(name = "method_type")
    private String methodType;
    private String description;
    @Column(name = "display_order")
    private int displayOrder;
    @Column(name = "result_type")
    private String resultType;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "fpk_column_name")
    private String fpkColumnName;
    @Column(name = "data_type")
    private Integer dataType;
    @Column(name = "is_using")
    private Integer isUsing;
    @Column(name = "unit_column_name")
    private String unitColumnName;      //基础表的单位字段    in_  代表基础表中没有单位id
    private String typeName;
    
    private String sourceType; // 数据来源  1：base+eis  2： center 库
    
    public static final int TRUE_IS_USING  = 1;  //启用
	public static final int FALSE_IS_USING = 0;  //不启用
	
	public static final int BASE_DATE_TYPE = 1;  //基础数据接口
	public static final int BUSINESS_DATE_TYPE = 2;  //业务接口
	public static final int PUSH_DATE_TYPE = 3;  //推送数据接口
	public static final int UPDATE_DATE_TYPE = 4;  //更新数据接口
    @Transient
    private String newUri;// 2.1uri

    public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getNewUri() {
        return newUri;
    }

    public void setNewUri(String newUri) {
        this.newUri = newUri;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Integer isUsing) {
        this.isUsing = isUsing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFpkColumnName() {
        return fpkColumnName;
    }

    public void setFpkColumnName(String fpkColumnName) {
        this.fpkColumnName = fpkColumnName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "openapiinterface";
    }

	public String getUnitColumnName() {
		return unitColumnName;
	}

	public void setUnitColumnName(String unitColumnName) {
		this.unitColumnName = unitColumnName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
