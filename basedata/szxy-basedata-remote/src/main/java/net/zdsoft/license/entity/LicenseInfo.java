/** 
 * @author wangsn
 * @since 1.0
 * @version $Id: LicenseInfo.java, v 1.0 2011-6-22 下午02:41:50 wangsn Exp $
 */
package net.zdsoft.license.entity;

import net.zdsoft.license.exception.InvalidLicenseException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class LicenseInfo implements Serializable {
	
	private static final long serialVersionUID = -2165045660804954001L;
	private static DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

    private Date expireDate; 
    private String unitName;
    private int unitNum;
    private int userNumPerUnit;
    private int apNum;
    private Set<String> availableSubsystems;
    private String productCode;
    private String productName;


    public void setExpireDate(Date expireDate) {
        this.expireDate=expireDate;
    }
    public Date getExpireDate() {
        return this.expireDate;
    }


    public void setUnitName(String unitName) {
        this.unitName=unitName;
    }
    public String getUnitName() {
        return this.unitName;
    }


    public void setUnitNum(int unitNum) {
        this.unitNum=unitNum;
    }
    public int getUnitNum() {
        return this.unitNum;
    }


    public void setUserNumPerUnit(int userNumPerUnit) {
        this.userNumPerUnit=userNumPerUnit;
    }
    public int getUserNumPerUnit() {
        return this.userNumPerUnit;
    }


    public void setApNum(int apNum) {
        this.apNum=apNum;
    }
    public int getApNum() {
        return this.apNum;
    }


    public void setAvailableSubsystems(Set<String> availableSubsystems) {
        this.availableSubsystems=availableSubsystems;
    }
    public Set<String> getAvailableSubsystems() {
        return this.availableSubsystems;
    }


    public void setProductCode(String productCode) {
        this.productCode=productCode;
    }
    public String getProductCode() {
        return this.productCode;
    }

    public Properties toProperties() {
        Properties prop = new Properties();
        prop.setProperty("expireDate", dateFormat.format(this.expireDate));
        prop.setProperty("unitName", this.unitName);
        prop.setProperty("unitNum", String.valueOf(this.unitNum));
        prop.setProperty("userNumPerUnit", String.valueOf(this.userNumPerUnit));
        prop.setProperty("apNum", String.valueOf(this.apNum));
        prop.setProperty("availableSubsystems", concatSubsystem(this.availableSubsystems));
        prop.setProperty("productCode", this.productCode);
        return prop;
    }

    public static LicenseInfo fromProperties(Properties prop) throws InvalidLicenseException {
        LicenseInfo licenseInfo = new LicenseInfo();
    	String expDateStr = prop.getProperty("expireDate");
    	if (expDateStr == null ) {
    		throw new InvalidLicenseException("非法的序列号信息");
    	}
        try {
            Date expDate = dateFormat.parse(expDateStr);
            licenseInfo.setExpireDate(expDate);
        } catch (ParseException e) {
            throw new InvalidLicenseException("日期格式非法");
        }

        String unitName = prop.getProperty("unitName");
        if (unitName == null || unitName.trim().equals("")) {
            throw new InvalidLicenseException("单位名称为空");
        }
        licenseInfo.setUnitName(unitName);

        try {
            if (StringUtils.isNotBlank(prop.getProperty("unitNum"))) {
                licenseInfo.setUnitNum(Integer.parseInt(prop.getProperty("unitNum")));
            }
            if (StringUtils.isNotBlank(prop.getProperty("userNumPerUnit"))) {
                licenseInfo.setUserNumPerUnit(Integer.parseInt(prop.getProperty("userNumPerUnit")));
            }
            if (StringUtils.isNotBlank(prop.getProperty("apNum"))) {
                licenseInfo.setApNum(Integer.parseInt(prop.getProperty("apNum")));
            }
        } catch (NumberFormatException e) {
            throw new InvalidLicenseException("序列号数字格式信息解析异常");
        }
        licenseInfo.setAvailableSubsystems(splitSubsystem(prop.getProperty("availableSubsystems")));
        licenseInfo.setProductCode(prop.getProperty("productCode"));
        return licenseInfo;
    }

    public static String concatSubsystem(Set<String> subsystems) {
        StringBuilder sb = new StringBuilder(); 
        if (subsystems == null || subsystems.size() == 0 ) return "";
        for (String subsystem: subsystems) {
            sb.append(subsystem).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static Set<String> splitSubsystem(String subsystemStr) {
        if (subsystemStr == null || subsystemStr.trim().equals("")) {
            return new HashSet<String>();
        }
        String[] subsysArr = subsystemStr.split(",");
        Set<String> result = new HashSet<String>();
        for (String subsys : subsysArr ) {
            result.add(subsys);
        }
        return result;
    }

    public void setProductName(String productName) {
        this.productName=productName;
    }
    public String getProductName() {
        return this.productName;
    }

}
