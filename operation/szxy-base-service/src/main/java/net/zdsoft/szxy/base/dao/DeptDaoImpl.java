package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.enu.DeptConstants;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/3/20 下午4:31
 */
public class DeptDaoImpl {

    @Resource
    private DeptDao deptDao;

    public String getAvailableDeptCodeByUnitId(String unitId) {
        Integer maxCode = deptDao.getMaxDeptCodeByUnitId(unitId);
        if (maxCode == null) {
            maxCode = 0;
        }
        maxCode = maxCode + 1;
        return String.format("%1$0" + DeptConstants.DEPT_CODE_MAX_LENGTH + "d", maxCode);
    }
}
