package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by luf on 2018/12/14.
 */
@Entity
@Table(name="studevelop_template_item")
public class StudevelopTemplateItem extends BaseEntity<String> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 100001L;

	private String  itemName;

    private String isClosed;

    private String templateId;
    private String objectId;
    private String objectType;
    private String singleOrInput;
    private Integer sortNumber;
    private String unitId;
    private Date creationTime;
    private Date modifyTime;
    @Transient
    private String optionNames;

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Transient
    private List<StudevelopTemplateOptions> templateOptions;
    @Transient
    private StudevelopTemplateResult templateResult;
    @Transient
    private String acadyear;
    @Transient
    private String semester;
    @Transient
    private String section;
    @Transient
    private String gradeId;
    @Transient
    private String code;

    public StudevelopTemplateResult getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(StudevelopTemplateResult templateResult) {
        this.templateResult = templateResult;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getSingleOrInput() {
        return singleOrInput;
    }

    public void setSingleOrInput(String singleOrInput) {
        this.singleOrInput = singleOrInput;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public List<StudevelopTemplateOptions> getTemplateOptions() {
        return templateOptions;
    }

    public void setTemplateOptions(List<StudevelopTemplateOptions> templateOptions) {
        this.templateOptions = templateOptions;
    }

    public String getOptionNames() {
		return optionNames;
	}

	public void setOptionNames(String optionNames) {
		this.optionNames = optionNames;
	}

	@Override
    public String fetchCacheEntitName() {
        return "studevelopTemplateItme";
    }
}
