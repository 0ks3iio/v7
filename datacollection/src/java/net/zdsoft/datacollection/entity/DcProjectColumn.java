package net.zdsoft.datacollection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_project_column")
public class DcProjectColumn extends BaseEntity<String> {

	@Column(length=32)
	private String projectId = "";
	@Column(length=32)
	private String userId = "";
	@Column(length=32)
	private String unitId = "";
	@Column(length=1)
	private Integer state = 0;
	private Integer rowIndex = 0;
	@Column(length=1)
	private Integer isDataRow = 0;
	@Column(length=1)
	private Integer isTemplate = 0;
	
	@Column(name = "column_1", length=1000)
	private String column1;
	@Column(name = "column_2", length=1000)
	private String column2;
	@Column(name = "column_3", length=1000)
	private String column3;
	@Column(name = "column_4", length=1000)
	private String column4;
	@Column(name = "column_5", length=1000)
	private String column5;
	@Column(name = "column_6", length=1000)
	private String column6;
	@Column(name = "column_7", length=1000)
	private String column7;
	@Column(name = "column_8", length=1000)
	private String column8;
	@Column(name = "column_9", length=1000)
	private String column9;
	@Column(name = "column_10", length=1000)
	private String column10;
	@Column(name = "column_11", length=1000)
	private String column11;
	@Column(name = "column_12", length=1000)
	private String column12;
	@Column(name = "column_13", length=1000)
	private String column13;
	@Column(name = "column_14", length=1000)
	private String column14;
	@Column(name = "column_15", length=1000)
	private String column15;
	@Column(name = "column_16", length=1000)
	private String column16;
	@Column(name = "column_17", length=1000)
	private String column17;
	@Column(name = "column_18", length=1000)
	private String column18;
	@Column(name = "column_19", length=1000)
	private String column19;
	@Column(name = "column_20", length=1000)
	private String column20;
	@Column(name = "column_21", length=1000)
	private String column21;
	@Column(name = "column_22", length=1000)
	private String column22;
	@Column(name = "column_23", length=1000)
	private String column23;
	@Column(name = "column_24", length=1000)
	private String column24;
	@Column(name = "column_25", length=1000)
	private String column25;
	@Column(name = "column_26", length=1000)
	private String column26;
	@Column(name = "column_27", length=1000)
	private String column27;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3(String column3) {
		this.column3 = column3;
	}

	public String getColumn4() {
		return column4;
	}

	public void setColumn4(String column4) {
		this.column4 = column4;
	}

	public String getColumn5() {
		return column5;
	}

	public void setColumn5(String column5) {
		this.column5 = column5;
	}

	public String getColumn6() {
		return column6;
	}

	public void setColumn6(String column6) {
		this.column6 = column6;
	}

	public String getColumn7() {
		return column7;
	}

	public void setColumn7(String column7) {
		this.column7 = column7;
	}

	public String getColumn8() {
		return column8;
	}

	public void setColumn8(String column8) {
		this.column8 = column8;
	}

	public String getColumn9() {
		return column9;
	}

	public void setColumn9(String column9) {
		this.column9 = column9;
	}

	public String getColumn10() {
		return column10;
	}

	public void setColumn10(String column10) {
		this.column10 = column10;
	}

	public String getColumn11() {
		return column11;
	}

	public void setColumn11(String column11) {
		this.column11 = column11;
	}

	public String getColumn12() {
		return column12;
	}

	public void setColumn12(String column12) {
		this.column12 = column12;
	}

	public String getColumn13() {
		return column13;
	}

	public void setColumn13(String column13) {
		this.column13 = column13;
	}

	public String getColumn14() {
		return column14;
	}

	public void setColumn14(String column14) {
		this.column14 = column14;
	}

	public String getColumn15() {
		return column15;
	}

	public void setColumn15(String column15) {
		this.column15 = column15;
	}

	public String getColumn16() {
		return column16;
	}

	public void setColumn16(String column16) {
		this.column16 = column16;
	}

	public String getColumn17() {
		return column17;
	}

	public void setColumn17(String column17) {
		this.column17 = column17;
	}

	public String getColumn18() {
		return column18;
	}

	public void setColumn18(String column18) {
		this.column18 = column18;
	}

	public String getColumn19() {
		return column19;
	}

	public void setColumn19(String column19) {
		this.column19 = column19;
	}

	public String getColumn20() {
		return column20;
	}

	public void setColumn20(String column20) {
		this.column20 = column20;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Integer getIsDataRow() {
		return isDataRow;
	}

	public void setIsDataRow(Integer isDataRow) {
		this.isDataRow = isDataRow;
	}

	public Integer getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Integer isTemplate) {
		this.isTemplate = isTemplate;
	}

	public String getColumn21() {
		return column21;
	}

	public void setColumn21(String column21) {
		this.column21 = column21;
	}

	public String getColumn22() {
		return column22;
	}

	public void setColumn22(String column22) {
		this.column22 = column22;
	}

	public String getColumn23() {
		return column23;
	}

	public void setColumn23(String column23) {
		this.column23 = column23;
	}

	public String getColumn24() {
		return column24;
	}

	public void setColumn24(String column24) {
		this.column24 = column24;
	}

	public String getColumn25() {
		return column25;
	}

	public void setColumn25(String column25) {
		this.column25 = column25;
	}

	public String getColumn26() {
		return column26;
	}

	public void setColumn26(String column26) {
		this.column26 = column26;
	}

	public String getColumn27() {
		return column27;
	}

	public void setColumn27(String column27) {
		this.column27 = column27;
	}
}
