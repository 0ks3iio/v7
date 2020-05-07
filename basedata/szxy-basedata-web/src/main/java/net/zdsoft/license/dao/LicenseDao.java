package net.zdsoft.license.dao;

import net.zdsoft.license.entity.EncryptedLicense;



public interface LicenseDao {

    /**
     * 取得许可证信息
     * @return
     */

    public EncryptedLicense getLicense();

    /**
     * 保存许可证信息
     * @param license
     */

    public void saveLicense(EncryptedLicense license);


}
