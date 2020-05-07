package net.zdsoft.api.base.entity.eis;

import net.zdsoft.api.base.entity.base.DataCenterBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "bg_openapi_developer")
public class ApiDeveloper extends DataCenterBaseEntity {
    private static final long serialVersionUID = 1L;
    public static final int IS_SUPER_TYPE = 0;
    public static final int IS_AP_ADMID_TYPE = 1;
    public static final int IS_COMMON_TYPE = 2;
    @Override
    public String fetchCacheEntitName() {
        return "developer";
    }
    private String apKey;
    private String ips;
    private String username;
    private String realName;
    private String password;
    private int userType; 
    private String description;
    private String mobilePhone;
    private String address;
    private String email;
    private String unitName;

    @Transient
	private Long countNum;  //调用接口的次数

    public String getApKey() {
		return apKey;
	}

	public void setApKey(String apKey) {
		this.apKey = apKey;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public Long getCountNum() {
		return countNum;
	}

	public void setCountNum(Long countNum) {
		this.countNum = countNum;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
