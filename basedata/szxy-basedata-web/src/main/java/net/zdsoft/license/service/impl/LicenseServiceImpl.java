package net.zdsoft.license.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.license.LicenseDecryptor;
import net.zdsoft.license.dao.LicenseDao;
import net.zdsoft.license.entity.EncryptedLicense;
import net.zdsoft.license.entity.LicenseInfo;
import net.zdsoft.license.exception.InvalidLicenseException;
import net.zdsoft.license.service.LicenseService;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseDao licenseDao;

    @Override
    public String getEncryptedLicenseStr() {
        EncryptedLicense encryptedLicense = licenseDao.getLicense();
        if (encryptedLicense == null) {
            return null;
        }
        return encryptedLicense.getLicenseText();
    }

    @Override
    public LicenseInfo getLicense() {
        EncryptedLicense encryptedLicense = licenseDao.getLicense();
        if (encryptedLicense == null) {
            return null;
        }
        LicenseInfo license = null;
        try {
            license = LicenseDecryptor.decode(encryptedLicense.getLicenseText());
        }
        catch (InvalidLicenseException e) {
        }
        return license;
    }

    @Override
    public String verifyLicense(String unitName, String licenseStr) {
        LicenseInfo license = null;
        unitName = unitName.trim();

        if (unitName == null || unitName.trim().equals("")) {
            return "单位名不能为空";
        }
        if (licenseStr == null || licenseStr.trim().equals("")) {
            return "license文本不能为空";
        }

        try {
            license = LicenseDecryptor.decode(licenseStr);
        }
        catch (InvalidLicenseException e) {
            return e.getMessage();
        }
        if (!unitName.equals(license.getUnitName())) {
            return "注册单位和license文本信息中的单位名称不匹配";
        }
        if (license.getExpireDate().compareTo(new Date()) < 0) {
            return "license已经过期.";
        }
        return "";
    }

    @Override
    public String saveLicense(String unitName, String licenseStr) {
        String msg = verifyLicense(unitName, licenseStr);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(msg)) {
            return msg;
        }
        EncryptedLicense encryptedLicense = new EncryptedLicense();
        encryptedLicense.setUnitName(unitName);
        encryptedLicense.setLicenseText(licenseStr);
        licenseDao.saveLicense(encryptedLicense);
        return "";
    }

    @Override
    public LicenseInfo decodeLicense(String licenseStr) {
        LicenseInfo license = null;
        try {
            license = LicenseDecryptor.decode(licenseStr);
        }
        catch (InvalidLicenseException e) {
        }
        return license;
    }

    public void setLicenseDao(LicenseDao licenseDao) {
        this.licenseDao = licenseDao;
    }

}
