package net.zdsoft.system.service.config.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.dao.config.UnitIniDao;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.service.config.UnitIniService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("unitIniService")
@Lazy(false)
public class UnitIniServiceImpl extends BaseServiceImpl<UnitIni, String> implements UnitIniService {

    public static final String KEY = "unitIni.iniid.";
    @Autowired
    private UnitIniDao unitIniDao;
	@Override
	public UnitIni getUnitIni(String unitId, String iniId) {
		 return RedisUtils.getObject(KEY + unitId+"."+iniId + ".object", 0, new TypeReference<UnitIni>() {
	        }, new RedisInterface<UnitIni>() {
	            @Override
	            public UnitIni queryData() {
	                return unitIniDao.getUnitIni(unitId, iniId);
	            }

	        });
	}
	@Override
	public void updateNowvalue(String nowValue, String iniid, String unitId) {
		unitIniDao.updateNowvalue(nowValue, iniid, unitId);
		RedisUtils.del(KEY + unitId+"."+iniid + ".object");
	}
	@Override
	protected BaseJpaRepositoryDao<UnitIni, String> getJpaDao() {
		return unitIniDao;
	}
	@Override
	protected Class<UnitIni> getEntityClass() {
		return UnitIni.class;
	}
	@Override
	public long findMaxId() {
		return unitIniDao.findMaxId();
	}


}
