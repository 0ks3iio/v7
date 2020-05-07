package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.SysOptionRemoteService;
import net.zdsoft.szxy.base.dao.SysOptionDao;
import net.zdsoft.szxy.base.entity.SysOption;
import net.zdsoft.szxy.base.wrapper.OptionValue;
import net.zdsoft.szxy.utils.AssertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/3/19 下午6:24
 */
@Service("sysOptionRemoteService")
public class SysOptionServiceImpl implements SysOptionRemoteService {

    @Resource
    private SysOptionDao sysOptionDao;

    @Override
    public SysOption getSysOptionByOptionCode(String optionCode) {
        AssertUtils.notNull(optionCode, "optionCode不能为空");
        return sysOptionDao.getSysOptionByOptionCode(optionCode);
    }

    @Override
    public OptionValue getOptionByOptionCode(String optionCode) {
        AssertUtils.notNull(optionCode, "OptionCode不能为空");
        return OptionValue.of(sysOptionDao.getValueByOptionCode(optionCode));
    }

    @Override
    public String getValueByOptionCode(String optionCode) {
        return sysOptionDao.getValueByOptionCode(optionCode);
    }
}
