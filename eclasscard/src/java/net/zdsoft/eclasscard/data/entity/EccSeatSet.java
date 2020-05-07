package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_class_seat_set")
public class EccSeatSet extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	/**
	 *  class_id varchar2(32),
      class_type varchar2(1),
      row_number number,---行数
      col_number number,--列数
      space_num varchar2(32),
	 */
	private String classId;
	private String classType;
	private Integer rowNumber;
	private Integer colNumber;
	private String spaceNum;//value:2，4   代表（2、3之间过道 4，5之间过道）
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	@Override
	public String fetchCacheEntitName() {
		return "eccSeatSet";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Integer getColNumber() {
		return colNumber;
	}

	public void setColNumber(Integer colNumber) {
		this.colNumber = colNumber;
	}

	public String getSpaceNum() {
		return spaceNum;
	}

	public void setSpaceNum(String spaceNum) {
		this.spaceNum = spaceNum;
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
	

}
