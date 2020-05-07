package net.zdsoft.basedata.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Entity
@Table(name = "base_user")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends BaseEntity<String>{
	
	public static final String CACHE_NAMES_USERNAME = "Username";
	public static final String MAN_AVATARURL = "desktop/images/user-male.png";
	public static final String WOMAN_AVATARURL = "desktop/images/user-female.png";
	private static final long serialVersionUID = 1L;
	public static final int OWNER_TYPE_STUDENT = 1;
	public static final int OWNER_TYPE_TEACHER = 2;
	public static final int OWNER_TYPE_FAMILY = 3;
	public static final int OWNER_TYPE_SUPER = 9;

	public static final int USER_TYPE_TOP_ADMIN = 0;
	public static final int USER_TYPE_UNIT_ADMIN = 1;
	public static final int USER_TYPE_COMMON_USER = 2;
	
	public static final int USER_WOMAN_SEX = 2;

	public static final String ADMIN_USER_ID = "00000000000000000000000000000000";
	public static final String ADMIN_USER_NAME = "系统管理员";
	public static final int USER_MARK_NORMAL = 1;
	public static final int USER_MARK_LOCK = 2;

	private String unitId;
	private Integer sequence;
	private String accountId;
	@Column(updatable = false)
	private String ownerId;
	private Integer ownerType;
	@ColumnInfo(displayName = "用户名")
	private String username;
	private String realName;
	private Integer userState;
	private Integer userType;
	private String email;
	private String regionCode;
	private Integer displayOrder;
	private Integer sex;
	private String chargeNumber;
	private Integer chargeNumberType;
	private Integer orderStatus;
	@ColumnInfo(displayName = "昵称")
	private String nickName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted = 0;
	private Integer eventSource;
	@ColumnInfo(displayName = "密码", vtype = "password")
	private String password;
	private Integer subsystemAdmin;
	private String deptId;
	private Integer roleType;
	private Integer userRole;
	private String classId;
	private Integer iconIndex;
	private Integer authProperty;
	private String signature;
	private Date birthday;
	private Integer enrollYear;
	private String webpage;
	private String summary;
	private String mobilePhone;
	private String address;
	private String zipCode;
	private String officeTel;
	private String homeTel;
	private String qq;
	private String msn;
	private String orgVisual;
	private String bgImg;
	@Column(length=2000)
	private String pinyinAll;
	@Transient
	private Long sequenceIntId;
	
	private String identityCard;
	private String identityType;
//	@Transient
	private String dingdingId;
	private Date expireDate;// 到期时间
	
	private Date loginDate;
	private Date upPwdDate;
	
	private String userIdCode;  //第三方 用户数据之间的关联字段
	@Transient
	private String ahUserId;
	/**	 如果是不带http的，则是系统参数中file.url + 这个值，如果是http开头的完整地址，则直接显示*/
	@Column(length = 500)
	@ColumnInfo(displayName = "头像")
	private String avatarUrl;
	
	@Transient
	private List<UserDept> deptList;
	
	public String getAhUserId() {
		return ahUserId;
	}

	public void setAhUserId(String ahUserId) {
		this.ahUserId = ahUserId;
	}

	public static List<User> dt(String data) {
		List<User> ts = SUtils.dt(data, new TypeReference<List<User>>() {
		});
		if (ts == null) {
			ts = new ArrayList<User>();
		}
		return ts;

	}
	
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getUpPwdDate() {
		return upPwdDate;
	}

	public void setUpPwdDate(Date upPwdDate) {
		this.upPwdDate = upPwdDate;
	}

	public static List<User> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<User> ts = SUtils.dt(json.getString("data"), new TypeReference<List<User>>() {
		});
		if (ts == null) {
			ts = new ArrayList<User>();
		}
		if (json.containsKey("count")) {
			page.setMaxRowCount(json.getInteger("count"));
		}
		return ts;

	}

	public static User dc(String data) {
		return SUtils.dc(data, User.class);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "user";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getChargeNumber() {
		return chargeNumber;
	}

	public void setChargeNumber(String chargeNumber) {
		this.chargeNumber = chargeNumber;
	}

	public Integer getChargeNumberType() {
		return chargeNumberType;
	}

	public void setChargeNumberType(Integer chargeNumberType) {
		this.chargeNumberType = chargeNumberType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public Integer getSubsystemAdmin() {
		return subsystemAdmin;
	}

	public void setSubsystemAdmin(Integer subsystemAdmin) {
		this.subsystemAdmin = subsystemAdmin;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Integer getIconIndex() {
		return iconIndex;
	}

	public void setIconIndex(Integer iconIndex) {
		this.iconIndex = iconIndex;
	}

	public Integer getAuthProperty() {
		return authProperty;
	}

	public void setAuthProperty(Integer authProperty) {
		this.authProperty = authProperty;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getEnrollYear() {
		return enrollYear;
	}

	public void setEnrollYear(Integer enrollYear) {
		this.enrollYear = enrollYear;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getOrgVisual() {
		return orgVisual;
	}

	public void setOrgVisual(String orgVisual) {
		this.orgVisual = orgVisual;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public String getPinyinAll() {
		return pinyinAll;
	}

	public void setPinyinAll(String pinyinAll) {
		this.pinyinAll = pinyinAll;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public String getAvatarUrl() {
		if (StringUtils.isBlank(avatarUrl)) {
			// 性别为女的用user-female 其他的全部用默认的user-male
			if (Objects.equals(sex, USER_WOMAN_SEX)) {
				avatarUrl=File.separator + "portrait" + File.separator + "user-female.png";
			} else {
				avatarUrl=File.separator + "portrait" + File.separator + "user-male.png";
			}
		} 
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getDingdingId() {
		return dingdingId;
	}

	public void setDingdingId(String dingdingId) {
		this.dingdingId = dingdingId;
	}

	public List<UserDept> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<UserDept> deptList) {
		this.deptList = deptList;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getUserIdCode() {
		return userIdCode;
	}

	public void setUserIdCode(String userIdCode) {
		this.userIdCode = userIdCode;
	}
}
