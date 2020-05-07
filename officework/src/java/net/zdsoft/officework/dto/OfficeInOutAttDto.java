package net.zdsoft.officework.dto;

import java.util.Date;

public class OfficeInOutAttDto {
	public static final String TO_OFFICE_MACHINENUM_IN = "J0001";
	public static final String TO_OFFICE_MACHINENUM_OUT = "C0001";
	private String unitId;
	private String attendancenum;
	private String machineNum = TO_OFFICE_MACHINENUM_IN;
	private String mark;
	private Date attendanceTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAttendancenum() {
		return attendancenum;
	}

	public void setAttendancenum(String attendancenum) {
		this.attendancenum = attendancenum;
	}

	public String getMachineNum() {
		return machineNum;
	}

	public void setMachineNum(String machineNum) {
		this.machineNum = machineNum;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

}
