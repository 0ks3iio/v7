package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;


@Entity
@Table(name = "bg_openapi_interface")
public class ApiInterface extends BaseEntity<String>{
    private static final long serialVersionUID = 1L;
    @Override
    public String fetchCacheEntitName() {
        return "apiinterface";
    }
    private String type;
    private String uri;
    @Column(name = "method_type")
    private String methodType;
    private String description;
    @Column(name = "display_order")
    private int displayOrder;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "is_using")
    private Integer isUsing;
    @Column(name = "data_type")
    private Integer dataType;
    @Column(name = "result_type")
    private String resultType;
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "fpk_column_name")
    private String fpkColumnName;
    @Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private int isDeleted;
    
    public static final int TRUE_IS_USING  = 1;  //启用
	public static final int FALSE_IS_USING = 0;  //不启用
	
	public static final int BASE_DATE_TYPE = 1;  //基础数据接口
	public static final int BUSINESS_DATE_TYPE = 2;  //业务接口
	public static final int PUSH_DATE_TYPE = 3;  //推送数据接口
	public static final int UPDATE_DATE_TYPE = 4;  //更新数据接口
    @Transient
    private String newUri;// 2.1uri
    @Transient
    private String resultTypeName;
    
    private String metadataId;  //元数据的主键id

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getFpkColumnName() {
		return fpkColumnName;
	}

	public void setFpkColumnName(String fpkColumnName) {
		this.fpkColumnName = fpkColumnName;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
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

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getResultTypeName() {
		return resultTypeName;
	}

	public void setResultTypeName(String resultTypeName) {
		this.resultTypeName = resultTypeName;
	}
}
