package net.zdsoft.syncdata.entity;

import java.util.Date;

import javax.persistence.Entity;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.syncdata.constant.JledqConstant;

/**
 * 
 * @author weixh
 * @since 2017-11-28 下午2:58:12
 */
@SuppressWarnings("serial")
@Entity
public class EdqUser extends BaseEntity<String>{
	private String zzdm;// 组织代码
	private int yhlx;// 用户类型 1-管理员2-教师3-学生4-家长5-教育局6-学区7-学校
	private String userId;
	private String yhzh;
	private int zt;// 1正常2冻结
	private String yhm;// 用户姓名
	private String sjhm;// 手机号
	private int px;// 排序
	private int operationType;// 操作类型，1新增2修改3删除
	
	private String unitId;
	private String deptId;
	private String classId;

	public void toUser(User uu) {
		uu.setCreationTime(new Date());
		uu.setUnitId(unitId);
		uu.setUsername(yhzh);
		if (JledqConstant.USER_YHLX_FAM == yhlx) {
			uu.setOwnerType(User.OWNER_TYPE_FAMILY);
		} else if(JledqConstant.USER_YHLX_STU == yhlx) {
			uu.setOwnerType(User.OWNER_TYPE_STUDENT);
		} else {
			uu.setOwnerType(User.OWNER_TYPE_TEACHER);
		}
		uu.setRealName(yhm);
		uu.setSex(0);
		uu.setUserType(User.USER_TYPE_COMMON_USER);
        uu.setUserState(User.USER_MARK_NORMAL);
        uu.setMobilePhone(sjhm);
        uu.setDisplayOrder(px);
	}
	
	public String getZzdm() {
		return zzdm;
	}

	public void setZzdm(String zzdm) {
		this.zzdm = zzdm;
	}

	public int getYhlx() {
		return yhlx;
	}

	public void setYhlx(int yhlx) {
		this.yhlx = yhlx;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getYhzh() {
		return yhzh;
	}

	public void setYhzh(String yhzh) {
		this.yhzh = yhzh;
	}

	public int getZt() {
		return zt;
	}

	public void setZt(int zt) {
		this.zt = zt;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "edqUser";
	}

}
