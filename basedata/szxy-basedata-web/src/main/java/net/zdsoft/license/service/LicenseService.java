package net.zdsoft.license.service;

import net.zdsoft.license.entity.LicenseInfo;

public interface LicenseService {

    public LicenseInfo getLicense();

    public String getEncryptedLicenseStr();

    /**
     * 验证序列号
     * 
     * @param unitName
     * @param licenseStr
     * @return 如果返回空串""，则保存成功，否则返回具体错误信息
     */
    public String verifyLicense(String unitName, String licenseStr);

    /**
     * 保存序列号
     * 
     * @param unitName
     * @param licenseStr
     * @return 如果返回空串""，则保存成功，否则返回具体错误信息
     */
    public String saveLicense(String unitName, String licenseStr);

    public LicenseInfo decodeLicense(String licenseStr);

}
