package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.SystemIniRemoteService;
import net.zdsoft.szxy.base.dao.SystemIniDao;
import net.zdsoft.szxy.base.entity.SystemIni;
import net.zdsoft.szxy.base.wrapper.OptionValue;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/3/22 上午10:26
 */
@Service("systemIniRemoteService")
public class SystemIniServiceImpl implements SystemIniRemoteService {

    @Resource
    private SystemIniDao systemIniDao;

    @Override
    public OptionValue getOptionValueByIniId(String iniId) {
        AssertUtils.notNull(iniId, "iniId can't null");
        return OptionValue.of(systemIniDao.getNowValueByIniId(iniId));
    }

    @Override
    public SystemIni getSystemIniByIniId(String iniId) {
        AssertUtils.notNull(iniId, "iniId can't null");
        return systemIniDao.getSystemIniByIniId(iniId);
    }

    @Override
    public String getRawValueByIniId(String iniId) {
        AssertUtils.notNull(iniId, "iniId can't null");
        return systemIniDao.getNowValueByIniId(iniId);
    }
}
