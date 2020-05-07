package net.zdsoft.datareport.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="datareport_results")
public class DataReportResults extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String reportId;
	private String unitId;
	private String ownerId;
	private String taskId;
	private Integer rowIndex;
	private Integer type;
	private String data;
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;
	private String data6;
	private String data7;
	private String data8;
	private String data9;
	private String data10;
	private String data11;
	private String data12;
	private String data13;
	private String data14;
	private String data15;
	private String data16;
	private String data17;
	private String data18;
	private String data19;
	private String data20;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String[] dataArray;
	
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public String getData6() {
		return data6;
	}

	public void setData6(String data6) {
		this.data6 = data6;
	}

	public String getData7() {
		return data7;
	}

	public void setData7(String data7) {
		this.data7 = data7;
	}

	public String getData8() {
		return data8;
	}

	public void setData8(String data8) {
		this.data8 = data8;
	}

	public String getData9() {
		return data9;
	}

	public void setData9(String data9) {
		this.data9 = data9;
	}

	public String getData10() {
		return data10;
	}

	public void setData10(String data10) {
		this.data10 = data10;
	}

	public String getData11() {
		return data11;
	}

	public void setData11(String data11) {
		this.data11 = data11;
	}

	public String getData12() {
		return data12;
	}

	public void setData12(String data12) {
		this.data12 = data12;
	}

	public String getData13() {
		return data13;
	}

	public void setData13(String data13) {
		this.data13 = data13;
	}

	public String getData14() {
		return data14;
	}

	public void setData14(String data14) {
		this.data14 = data14;
	}

	public String getData15() {
		return data15;
	}

	public void setData15(String data15) {
		this.data15 = data15;
	}

	public String getData16() {
		return data16;
	}

	public void setData16(String data16) {
		this.data16 = data16;
	}

	public String getData17() {
		return data17;
	}

	public void setData17(String data17) {
		this.data17 = data17;
	}

	public String getData18() {
		return data18;
	}

	public void setData18(String data18) {
		this.data18 = data18;
	}

	public String getData19() {
		return data19;
	}

	public void setData19(String data19) {
		this.data19 = data19;
	}

	public String getData20() {
		return data20;
	}

	public void setData20(String data20) {
		this.data20 = data20;
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

	public String[] getDataArray() {
		return dataArray;
	}

	public void setDataArray(String[] dataArray) {
		this.dataArray = dataArray;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dataReportResults";
	}
}
