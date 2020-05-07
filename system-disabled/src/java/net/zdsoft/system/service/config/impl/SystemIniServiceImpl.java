package net.zdsoft.system.service.config.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.system.constant.Constant;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.LocalCacheUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.dao.config.SystemIniDao;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.service.config.SystemIniService;

import javax.annotation.PostConstruct;

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
    	if(ini != null)
    		return ini.getNowvalue();
    	return null;
		
//        return RedisUtils.getObject(KEY + iniid + ".value", 0, new TypeReference<String>() {
//        }, new RedisInterface<String>() {
//
//            @Override
//            public String queryData() {
//                SystemIni findByIniid = systemIniDao.findByIniid(iniid);
//                if (findByIniid != null) {
//                    String value = StringUtils.isNotBlank(findByIniid.getNowvalue()) ? findByIniid.getNowvalue()
//                            : findByIniid.getDvalue();
//                    return value;
//                }
//                return null;
//            }
//
//        });
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
            RedisUtils.setObject(KEY + iniid + ".value", value);
        }
    }

    @Override
    public void doRefreshCache(String... iniid) {
        List<SystemIni> list = systemIniDao.findByIniidIn(iniid);
        if (list == null) {
            list = new ArrayList<SystemIni>();
        }
        refreshCache(list, iniid);
    }

    private void refreshCache(List<SystemIni> list, String... iniid) {
        final Map<String, SystemIni> map = new HashMap<String, SystemIni>();
        for (SystemIni item : list) {
            map.put(item.getIniid(), item);
        }
        for (String string : iniid) {
            final String iid = string;
            final SystemIni systemIni = map.get(iid);
            doCacheValueSave(iid, systemIni);
        }
    }

    @Override
    public void doRefreshCacheAll() {
        List<SystemIni> list = systemIniDao.findAll();
        if (list == null) {
            list = new ArrayList<SystemIni>();
        }
        Set<String> optionCode = new HashSet<String>();
        for (SystemIni item : list) {
            optionCode.add(item.getIniid());
        }
        refreshCache(list, optionCode.toArray(new String[0]));
    }

    @Override
    public SystemIni findOneByIniid(String iniid) {
    	if (StringUtils.isBlank(iniid))
			return null;
    	SystemIni ini = LocalCacheUtils.getValue("SystemIni", iniid);
		if(ini == null) {
			ini = systemIniDao.findByIniid(iniid);	
			if(ini != null) {
				LocalCacheUtils.putValue("SystemIni", iniid, ini);
			}
		}
		return ini;
		
//        return systemIniDao.findByIniid(iniid);
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
        RedisUtils.setObject(KEY + iniid + ".value", nowvalue);
    }

    @Override
    public void updateValueType(int valueType, String iniid) {
        systemIniDao.updateValueType(valueType, iniid);
    }

}
