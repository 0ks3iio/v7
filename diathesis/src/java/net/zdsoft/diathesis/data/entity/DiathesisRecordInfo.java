package net.zdsoft.diathesis.data.entity;

import net.zdsoft.diathesis.data.dto.FileDto;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 
 * @author niuchao
 * @since  2019年4月1日
 */
@Entity
@Table(name="newdiathesis_record_info")
public class DiathesisRecordInfo extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
    private String recordId;
    private String structureId;
    private String contentTxt;
    @Transient
    private String dataType;
    @Transient
    private String stuCode;
    @Transient
    private String resultTxt;//附件名称
	@Transient
	private List<FileDto> fileList;

	public List<FileDto> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileDto> fileList) {
		this.fileList = fileList;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getStructureId() {
		return structureId;
	}

	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}

	public String getContentTxt() {
		return contentTxt;
	}

	public void setContentTxt(String contentTxt) {
		this.contentTxt = contentTxt;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getResultTxt() {
		return resultTxt;
	}

	public void setResultTxt(String resultTxt) {
		this.resultTxt = resultTxt;
	}

	@Override
    public String fetchCacheEntitName() {
        return "diathesisRecordInfo";
    }
}
