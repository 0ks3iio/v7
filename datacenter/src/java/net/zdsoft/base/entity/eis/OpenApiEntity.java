package net.zdsoft.base.entity.eis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eis_openapi_entity")
public class OpenApiEntity extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    @Override
    public String fetchCacheEntitName() {
        return "openapientity";
    }
    @Column
    private String type;
    @Column(name = "entity_name")
    private String entityName;
    @Column(name = "entity_type")
    private String entityType;
    @Column(name = "entity_column_name")
    private String entityColumnName;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "entity_comment")
    private String entityComment;
    @Column(name = "mcode_id")
    private String mcodeId;
    @Column
    private int mandatory;
    @Column(name = "display_order")
    private int displayOrder;
    @Column(name = "is_using")
    private Integer isUsing;
    private Integer columnProp;//字段特性 0.默认 1.主键  2.删除字段
    @Column(name = "is_sensitive")
    private int isSensitive;
    
    private int entityLength;
    private String relationColumn;//字段关联哪个模型的主键，教师的单位id,为school.edu；学生班级id,为class
    private Integer isDefault;//是否有默认值
    
    public static final int TRUE_IS_USING  = 1;  //启用
	public static final int FALSE_IS_USING = 0;  //不启用

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMcodeId() {
        return mcodeId;
    }

    public void setMcodeId(String mcodeId) {
        this.mcodeId = mcodeId;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getEntityComment() {
        return entityComment;
    }

    public void setEntityComment(String entityComment) {
        this.entityComment = entityComment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }

    public void setEntityColumnName(String entityColumnName) {
        this.entityColumnName = entityColumnName;
    }

    public int getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(int isUsing) {
        this.isUsing = isUsing;
    }

    public Integer getColumnProp() {
		return columnProp;
	}

	public void setColumnProp(Integer columnProp) {
		this.columnProp = columnProp;
	}

	public int getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(int isSensitive) {
        this.isSensitive = isSensitive;
    }
    
	public int getEntityLength() {
		return entityLength;
	}

	public void setEntityLength(int entityLength) {
		this.entityLength = entityLength;
	}

	public String getRelationColumn() {
		return relationColumn;
	}

	public void setRelationColumn(String relationColumn) {
		this.relationColumn = relationColumn;
	}

	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
