package net.zdsoft.syncdata.entity;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;

/**
 * 
 * @author weixh
 * @since 2017-11-28 下午3:01:59
 */
public class EdqOrg {
	private String zzmc;// 组织名称
	private String zzjc;// 组织简称
	private String zzdm;// 组织代码
	private String sjzzdm;// 上级组织代码
	private String zzlx;// 组织类型，1：部门/科室2：学校3：教育局4：学区
	private int px;// 排序
	private int operationType;// 操作类型，1新增2修改3删除
	
	public void convertToUnit(Unit unit){
		unit.setUnitName(this.zzmc);
		unit.setDisplayOrder(this.px+"");
		
	}
	
	public void convertToDept(Dept dept){
		dept.setDeptName(zzmc);
		dept.setDeptShortName(zzjc);
		dept.setDisplayOrder(px);
		
	}

	public String getZzmc() {
		return zzmc;
	}

	public void setZzmc(String zzmc) {
		this.zzmc = zzmc;
	}

	public String getZzjc() {
		return zzjc;
	}

	public void setZzjc(String zzjc) {
		this.zzjc = zzjc;
	}

	public String getZzdm() {
		return zzdm;
	}

	public void setZzdm(String zzdm) {
		this.zzdm = zzdm;
	}

	public String getSjzzdm() {
		return sjzzdm;
	}

	public void setSjzzdm(String sjzzdm) {
		this.sjzzdm = sjzzdm;
	}

	public String getZzlx() {
		return zzlx;
	}

	public void setZzlx(String zzlx) {
		this.zzlx = zzlx;
	}

	public int getPx() {
		return px;
	}

	public void setPx(int px) {
		this.px = px;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

}
