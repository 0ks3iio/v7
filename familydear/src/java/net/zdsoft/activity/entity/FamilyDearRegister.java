package net.zdsoft.activity.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="famdear_register")
public class FamilyDearRegister extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String activityId;//活动Id
	private String arrangeId;//安排Id
	private String teacherId;//报名教师Id
	private String teaUserId;//用户id
	private String unitId;
	private int status;//状态
	private String remark;//审核说明
	private String auditUserId;//审核人
	private Date applyTime;//报名时间
	private Date auditTime;//审核时间
	@Transient
	private String arriveTimeStr;
	@Transient
	private String returnTimeStr;
	@Transient
	private String contrysSub;
	
	@Transient
	private String teacherName;
	@Transient
	private String teacherPhone;
	@Transient
	private String sex;
	@Transient
	private String deptName;
	@Transient
	private String contry;//结亲对象村
	@Transient
	private String contrySub;//结亲对象村
	@Transient
	private String batchType;//批次
	@Transient
	private String batchTypeSub;//批次
	@Transient
	private String contrys;//结亲村
	@Transient
	private String auditUserName;
	@Transient
	private String cadreType;//职务
	@Transient
	private String nationName;//民族
	@Transient
	private String phone;//手机号码
	@Transient
	private String objectName;//亲戚姓名
	@Transient
	private String objectNation;//亲戚民族
	@Transient
	private String objectCard;//亲戚身份证
	@Transient
	private String objectType;//亲戚身份类型
	@Transient
	private String objectDizhi;//亲戚地址
	@Transient
	private String objectPhone;//联系电话
	@Transient
	private String activityName;//报名轮次
	@Transient
	private String arriveDate;//访亲时间
	@Transient
	private String upload;//上传照片
	@Transient
	private String remarkNew;//备注
	
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getArrangeId() {
		return arrangeId;
	}

	public void setArrangeId(String arrangeId) {
		this.arrangeId = arrangeId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeaUserId() {
		return teaUserId;
	}

	public void setTeaUserId(String teaUserId) {
		this.teaUserId = teaUserId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getContry() {
		return contry;
	}

	public void setContry(String contry) {
		this.contry = contry;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}



	public String getContrys() {
		return contrys;
	}

	public void setContrys(String contrys) {
		this.contrys = contrys;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "getFamilyDearRegister";
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getCadreType() {
		return cadreType;
	}

	public void setCadreType(String cadreType) {
		this.cadreType = cadreType;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectNation() {
		return objectNation;
	}

	public void setObjectNation(String objectNation) {
		this.objectNation = objectNation;
	}

	public String getObjectCard() {
		return objectCard;
	}

	public void setObjectCard(String objectCard) {
		this.objectCard = objectCard;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectDizhi() {
		return objectDizhi;
	}

	public void setObjectDizhi(String objectDizhi) {
		this.objectDizhi = objectDizhi;
	}

	public String getObjectPhone() {
		return objectPhone;
	}

	public void setObjectPhone(String objectPhone) {
		this.objectPhone = objectPhone;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getRemarkNew() {
		return remarkNew;
	}

	public void setRemarkNew(String remarkNew) {
		this.remarkNew = remarkNew;
	}

	public String getTeacherPhone() {
		return teacherPhone;
	}

	public void setTeacherPhone(String teacherPhone) {
		this.teacherPhone = teacherPhone;
	}

	public String getContrysSub() {
		return contrysSub;
	}

	public void setContrysSub(String contrysSub) {
		this.contrysSub = contrysSub;
	}

	public String getArriveTimeStr() {
		return arriveTimeStr;
	}

	public void setArriveTimeStr(String arriveTimeStr) {
		this.arriveTimeStr = arriveTimeStr;
	}

	public String getReturnTimeStr() {
		return returnTimeStr;
	}

	public void setReturnTimeStr(String returnTimeStr) {
		this.returnTimeStr = returnTimeStr;
	}

	public String getBatchTypeSub() {
		return batchTypeSub;
	}

	public void setBatchTypeSub(String batchTypeSub) {
		this.batchTypeSub = batchTypeSub;
	}

	public String getContrySub() {
		return contrySub;
	}

	public void setContrySub(String contrySub) {
		this.contrySub = contrySub;
	}
}
