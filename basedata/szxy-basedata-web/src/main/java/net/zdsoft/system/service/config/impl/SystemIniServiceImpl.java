package net.zdsoft.system.service.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.dao.config.SystemIniDao;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.service.config.SystemIniService;

@Service("systemIniService")
@Lazy(false)
public class SystemIniServiceImpl extends BaseServiceImpl<SystemIni, String> implements SystemIniService {

    private Logger logger = Logger.getLogger(SystemIniServiceImpl.class);

    public static final String KEY = "systemIni.iniid.";
    @Autowired
    private SystemIniDao systemIniDao;

    @PostConstruct
    public void initSystem(){
        String connectPassport = findValue(Constant.SYSTEM_PASSPORT_SWITCH);
        System.setProperty("connection_passport", connectPassport);
        logger.error("oa7 sys_option表 passport连接 【" + connectPassport + "】");
    }

    @Override
    public String findValue(final String iniid) {
    	SystemIni ini = findOneByIniid(iniid);
    	if(ini != null){
    		String  val =ini.getNowvalue();
    		if(StringUtils.isBlank(val)){
    			val = ini.getDvalue();
    			if(StringUtils.isBlank(val)){
    				return null;
    			}
    		}
    		return val;
    	}
    	return null;
    }

    @Override
    public List<SystemIni> getSystemIniByViewable(Integer viewable) {
        return systemIniDao.getSystemInisByViewable(viewable);
    }

    @Override
    public SystemIni saveOne(final SystemIni systemIni) {
        final SystemIni save = systemIniDao.save(systemIni);
        doCacheValueSave(systemIni.getIniid(), save);
        return save;
    }

    private void doCacheValueSave(final String iniid, final SystemIni save) {
        String value = null;
        if (save != null) {
            value = StringUtils.isNotBlank(save.getNowvalue()) ? save.getNowvalue() : save.getDvalue();
        }
        if (value != null) {
            RedisUtils.setObject(RedisUtils.getKey(SystemIni.class, iniid), value);
        }
    }

    @Override
    public void doRefreshCache(String... iniid) {
        List<SystemIni> list = systemIniDao.findByIniidIn(iniid);
        if (list == null) {
            list = new ArrayList<>();
        }
        refreshCache(list, iniid);
    }

    private void refreshCache(List<SystemIni> list, String... iniids) {
        final Map<String, SystemIni> map = new HashMap<>();
        for (SystemIni item : list) {
            String iniid = item.getIniid();
            doCacheValueSave(iniid, item);
        }
    }

    @Override
    public void doRefreshCacheAll() {
        List<SystemIni> list = systemIniDao.findAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        Set<String> optionCode = new HashSet<>();
        for (SystemIni item : list) {
            optionCode.add(item.getIniid());
        }
        refreshCache(list, optionCode.toArray(new String[0]));
    }

    @Override
	public SystemIni findOneByIniid(String iniid) {
		if (StringUtils.isBlank(iniid)) {
			return null;
		}
		return RedisUtils.getObject(RedisUtils.getKey(System.class, iniid), RedisUtils.TIME_FIVE_MINUTES, SystemIni.class,
				() -> systemIniDao.findByIniid(iniid));
	}

    @Override
    protected BaseJpaRepositoryDao<SystemIni, String> getJpaDao() {
        return systemIniDao;
    }

    @Override
    protected Class<SystemIni> getEntityClass() {
        return SystemIni.class;
    }

    @Override
    public void updateNowvalue(String nowvalue, String iniid) {
        systemIniDao.updateNowvalue(nowvalue, iniid);
        RedisUtils.del(RedisUtils.getKey(SystemIni.class, iniid));
    }

    @Override
    public void updateValueType(int valueType, String iniid) {
        systemIniDao.updateValueType(valueType, iniid);
        RedisUtils.del(RedisUtils.getKey(SystemIni.class, iniid));
    }

}
