package net.zdsoft.license.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.license.dao.LicenseDao;
import net.zdsoft.license.entity.EncryptedLicense;

/**
 * @author zhangza
 * @date 2011-5-18
 */
@Repository
public class LicenseDaoImpl extends BaseDao<EncryptedLicense> implements LicenseDao {

    private static final String SQL_GET_LICENSE = "SELECT * FROM sys_license";
    private static final String SQL_INSERT_LICENSE = "INSERT INTO sys_license (unit_name,license_txt) values (?,?)";
    private static final String SQL_UPDATE_LICENSE = "update sys_license set unit_name=? , license_txt = ? ";

    @Override
    public EncryptedLicense getLicense() {
        EncryptedLicense lic = this.query(SQL_GET_LICENSE, new SingleRow());
        return lic;
    }

    @Override
    public void saveLicense(EncryptedLicense lic) {
        int result = this.update(SQL_UPDATE_LICENSE, new Object[] { lic.getUnitName(), lic.getLicenseText() });
        if (result < 1) {
            this.update(SQL_INSERT_LICENSE, new Object[] { lic.getUnitName(), lic.getLicenseText() });
        }
    }

    @Override
    public EncryptedLicense setField(ResultSet rs) throws SQLException {
        EncryptedLicense license = new EncryptedLicense();
        license.setUnitName(rs.getString("unit_name"));
        license.setLicenseText(rs.getString("license_txt"));
        return license;
    }

}
