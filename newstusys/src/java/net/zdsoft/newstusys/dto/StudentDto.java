package net.zdsoft.newstusys.dto;

import java.util.Date;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.newstusys.constants.BaseStudentConstants;

/**
 * Created by Administrator on 2018/8/30.
 */
public class StudentDto {

    private String studentName;
    private String oldName;
    private String className;
    private String studentCode;
    private String classInnerCode;
    private String nativePlace;
    private String sex;
    private String birthday;
    private String nation;
    private String background;
    private String country;
    private String compatriots;
    private String identitycardType;
    private String identityCard;
    private String studentType;
    private String cardNumber;
    private String oldSchoolName;
    private String toSchoolDate;
    private String registerPlace;

    private String registerStreet;
    private String homeAddress;
    private String postalcode;
    private String familyMobile;
    private String strong;
    private String rewardRemark;

    private String gRealName;
    private String gRelation;
    private String gMobilePhone;
    private String fRealName;
    private String fMobilePhone;
    private String fPoliticalStatus;
    private String fCulture;
    private String fCompany;
    private String fDuty;
    private String fIdcardType;
    private String fIdcard;
    private String fcountry;
    private Date fBirthdayDate;
    private String fBirthdayDateStr;
    private String mRealName;
    private String mMobilePhone;
    private String mPoliticalStatus;
    private String mCulture;
    private String mCompany;
    private String mDuty;
    private String mIdcardType;
    private String mIdcard;
    private String mcountry;
    private Date mBirthdayDate;
    private String mBirthdayDateStr;

    private Date birthdayDate;
    private Date toSchDate;
    private String id;
    private String classId;
    
    private String studentResume;

    public void entityToDto(Student stu) {
    	if(stu == null) {
    		return;
    	}
    	this.setId(stu.getId());
    	this.setClassId(stu.getClassId());
    	this.setStudentName(stu.getStudentName());
    	this.setBackground(stu.getBackground());
    	this.setBirthdayDate(stu.getBirthday());
    	this.setCardNumber(stu.getCardNumber());
    	this.setClassInnerCode(stu.getClassInnerCode());
    	this.setClassName(stu.getClassName());
    	this.setCompatriots(stu.getCompatriots()==null?"":stu.getCompatriots().toString());
    	this.setCountry(stu.getCountry());
    	this.setFamilyMobile(stu.getFamilyMobile());
    	this.setToSchDate(stu.getToSchoolDate());
    	this.setIdentityCard(stu.getIdentityCard());
    	this.setIdentitycardType(stu.getIdentitycardType());
    	this.setNation(stu.getNation());
    	this.setNativePlace(stu.getNativePlace());
    	this.setOldName(stu.getOldName());
    	this.setOldSchoolName(stu.getOldSchoolName());
    	this.setStudentType(stu.getStudentType());
    	this.setStudentCode(stu.getStudentCode());
    	this.setHomeAddress(stu.getHomeAddress());
    	this.setPostalcode(stu.getPostalcode());
    	this.setRegisterPlace(stu.getRegisterPlace());
    	this.setRegisterStreet(stu.getRegisterStreet());
    	this.setRewardRemark(stu.getRewardRemark());
    	this.setStrong(stu.getStrong());
    	this.setSex(stu.getSex() != null?stu.getSex().toString():"");
    }
    
    public void famToDto(Family fam) {
    	if(fam == null || (fam.getIsDeleted() != null && fam.getIsDeleted() == 1)) {
    		return;
    	}
    	if(fam.getIsGuardian() != null && fam.getIsGuardian() == 1) {
    		this.setgRelation(fam.getRelation());
    		this.setgMobilePhone(fam.getMobilePhone());
    		this.setgRealName(fam.getRealName());
    	}
    	if(BaseStudentConstants.RELATION_FATHER.equals(fam.getRelation())) {
    		this.setfCompany(fam.getCompany());
    		this.setfCulture(fam.getCulture());
    		this.setfDuty(fam.getDuty());
    		this.setfMobilePhone(fam.getMobilePhone());
    		this.setfPoliticalStatus(fam.getPoliticalStatus());
    		this.setfRealName(fam.getRealName());
    		this.setfIdcardType(fam.getIdentitycardType());
    		this.setFcountry(fam.getCountry());
    		this.setfBirthdayDate(fam.getBirthday());
    	} else if(BaseStudentConstants.RELATION_MOTHER.equals(fam.getRelation())) {
    		this.setmCompany(fam.getCompany());
    		this.setmCulture(fam.getCulture());
    		this.setmDuty(fam.getDuty());
    		this.setmMobilePhone(fam.getMobilePhone());
    		this.setmPoliticalStatus(fam.getPoliticalStatus());
    		this.setmRealName(fam.getRealName());
    		this.setmIdcardType(fam.getIdentitycardType());
    		this.setMcountry(fam.getCountry());
    		this.setmBirthdayDate(fam.getBirthday());
    	}
    }
    
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getClassInnerCode() {
        return classInnerCode;
    }

    public void setClassInnerCode(String classInnerCode) {
        this.classInnerCode = classInnerCode;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompatriots() {
        return compatriots;
    }

    public void setCompatriots(String compatriots) {
        this.compatriots = compatriots;
    }

    public String getIdentitycardType() {
        return identitycardType;
    }

    public void setIdentitycardType(String identitycardType) {
        this.identitycardType = identitycardType;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getOldSchoolName() {
        return oldSchoolName;
    }

    public void setOldSchoolName(String oldSchoolName) {
        this.oldSchoolName = oldSchoolName;
    }

    public String getToSchoolDate() {
        return toSchoolDate;
    }

    public void setToSchoolDate(String toSchoolDate) {
        this.toSchoolDate = toSchoolDate;
    }

    public String getRegisterPlace() {
        return registerPlace;
    }

    public void setRegisterPlace(String registerPlace) {
        this.registerPlace = registerPlace;
    }

    public String getRegisterStreet() {
        return registerStreet;
    }

    public void setRegisterStreet(String registerStreet) {
        this.registerStreet = registerStreet;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getFamilyMobile() {
        return familyMobile;
    }

    public void setFamilyMobile(String familyMobile) {
        this.familyMobile = familyMobile;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
    }

    public String getRewardRemark() {
        return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
        this.rewardRemark = rewardRemark;
    }

    public String getgRealName() {
        return gRealName;
    }

    public void setgRealName(String gRealName) {
        this.gRealName = gRealName;
    }

    public String getgRelation() {
        return gRelation;
    }

    public void setgRelation(String gRelation) {
        this.gRelation = gRelation;
    }

    public String getgMobilePhone() {
        return gMobilePhone;
    }

    public void setgMobilePhone(String gMobilePhone) {
        this.gMobilePhone = gMobilePhone;
    }

    public String getfRealName() {
        return fRealName;
    }

    public void setfRealName(String fRealName) {
        this.fRealName = fRealName;
    }

    public String getfMobilePhone() {
        return fMobilePhone;
    }

    public void setfMobilePhone(String fMobilePhone) {
        this.fMobilePhone = fMobilePhone;
    }

    public String getfPoliticalStatus() {
        return fPoliticalStatus;
    }

    public void setfPoliticalStatus(String fPoliticalStatus) {
        this.fPoliticalStatus = fPoliticalStatus;
    }

    public String getfCulture() {
        return fCulture;
    }

    public void setfCulture(String fCulture) {
        this.fCulture = fCulture;
    }

    public String getfCompany() {
        return fCompany;
    }

    public void setfCompany(String fCompany) {
        this.fCompany = fCompany;
    }

    public String getfDuty() {
        return fDuty;
    }

    public void setfDuty(String fDuty) {
        this.fDuty = fDuty;
    }

    public String getmRealName() {
        return mRealName;
    }

    public void setmRealName(String mRealName) {
        this.mRealName = mRealName;
    }

    public String getmMobilePhone() {
        return mMobilePhone;
    }

    public void setmMobilePhone(String mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public String getmPoliticalStatus() {
        return mPoliticalStatus;
    }

    public void setmPoliticalStatus(String mPoliticalStatus) {
        this.mPoliticalStatus = mPoliticalStatus;
    }

    public String getmCulture() {
        return mCulture;
    }

    public void setmCulture(String mCulture) {
        this.mCulture = mCulture;
    }

    public String getmCompany() {
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public String getmDuty() {
        return mDuty;
    }

    public void setmDuty(String mDuty) {
        this.mDuty = mDuty;
    }

    public String getStudentResume() {
        return studentResume;
    }

    public void setStudentResume(String studentResume) {
        this.studentResume = studentResume;
    }

	public Date getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public Date getToSchDate() {
		return toSchDate;
	}

	public void setToSchDate(Date toSchDate) {
		this.toSchDate = toSchDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getfIdcard() {
		return fIdcard;
	}

	public void setfIdcard(String fIdcard) {
		this.fIdcard = fIdcard;
	}

	public String getmIdcard() {
		return mIdcard;
	}

	public void setmIdcard(String mIdcard) {
		this.mIdcard = mIdcard;
	}

	public String getfIdcardType() {
		return fIdcardType;
	}

	public void setfIdcardType(String fIdcardType) {
		this.fIdcardType = fIdcardType;
	}

	public String getFcountry() {
		return fcountry;
	}

	public void setFcountry(String fcountry) {
		this.fcountry = fcountry;
	}

	public Date getfBirthdayDate() {
		return fBirthdayDate;
	}

	public void setfBirthdayDate(Date fBirthdayDate) {
		this.fBirthdayDate = fBirthdayDate;
	}

	public String getmIdcardType() {
		return mIdcardType;
	}

	public void setmIdcardType(String mIdcardType) {
		this.mIdcardType = mIdcardType;
	}

	public String getMcountry() {
		return mcountry;
	}

	public void setMcountry(String mcountry) {
		this.mcountry = mcountry;
	}

	public Date getmBirthdayDate() {
		return mBirthdayDate;
	}

	public void setmBirthdayDate(Date mBirthdayDate) {
		this.mBirthdayDate = mBirthdayDate;
	}

    public String getfBirthdayDateStr() {
        return fBirthdayDateStr;
    }

    public void setfBirthdayDateStr(String fBirthdayDateStr) {
        this.fBirthdayDateStr = fBirthdayDateStr;
    }

    public String getmBirthdayDateStr() {
        return mBirthdayDateStr;
    }

    public void setmBirthdayDateStr(String mBirthdayDateStr) {
        this.mBirthdayDateStr = mBirthdayDateStr;
    }
}
