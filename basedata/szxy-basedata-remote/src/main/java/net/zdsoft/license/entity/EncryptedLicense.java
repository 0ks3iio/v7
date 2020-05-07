package net.zdsoft.license.entity;

public class EncryptedLicense {

    private String unitName;
    private String licenseText;

    public void setUnitName(String unitName) {
        this.unitName=unitName;
    }
    public String getUnitName() {
        return this.unitName;
    }

    public void setLicenseText(String licenseText) {
        this.licenseText=licenseText;
    }
    public String getLicenseText() {
        return this.licenseText;
    }

}
