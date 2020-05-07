package net.zdsoft.bigdata.extend.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_user_profile")
public class UserProfile extends BaseEntity<String> {

	private static final long serialVersionUID = -7032251753732076305L;

	private String name;

    private String code;
    
    private String aggEngine;
    
    private String projectName;

    private String indexName;

    private String typeName;
    
    private String mainDbType;
    private String mainTableName;
    private String foreignKey;
    private String primaryKey;
    private String basicColumns;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAggEngine() {
		return aggEngine;
	}

	public void setAggEngine(String aggEngine) {
		this.aggEngine = aggEngine;
	}

	public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	

	public String getMainDbType() {
		return mainDbType;
	}

	public void setMainDbType(String mainDbType) {
		this.mainDbType = mainDbType;
	}

	public String getMainTableName() {
		return mainTableName;
	}

	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}
	
	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
    public String fetchCacheEntitName() {
        return "userProfile";
    }

	public String getBasicColumns() {
		return basicColumns;
	}

	public void setBasicColumns(String basicColumns) {
		this.basicColumns = basicColumns;
	}
	
	
}
