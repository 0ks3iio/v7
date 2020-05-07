package net.zdsoft.system.service.config.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dao.config.UnitIniDao;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.service.config.SystemIniService;
import net.zdsoft.system.service.config.UnitIniService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import javax.annotation.Resource;
import java.util.List;

@Service("unitIniService")
@Lazy(false)
public class UnitIniServiceImpl extends BaseServiceImpl<UnitIni, String> implements UnitIniService {

    public static final String KEY = "unitIni.iniid.";
    @Autowired
    private UnitIniDao unitIniDao;
    @Resource
    private SystemIniService systemIniService;

    @Override
    public UnitIni getUnitIni(String unitId, String iniId) {
        return RedisUtils.getObject(KEY + unitId + "." + iniId + ".object", 0, new TypeReference<UnitIni>() {
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
        RedisUtils.del(KEY + unitId + "." + iniid + ".object");
        RedisUtils.del(KEY +"." + iniid + ".object.list");
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

    @Override
    public void initUnitDefaultOptions(String unitId) {
        List<SystemIni> list = systemIniService.getSystemIniByViewable(2);
        UnitIni unitIni;
        for (SystemIni systemIni : list) {
            unitIni = new UnitIni();
            long id = RedisUtils.incrby("UNIT_INI", findMaxId());
            if (id == -1) {
                throw new RuntimeException("Auto-increment id error");
            }
            unitIni.setId(String.valueOf(id));
            unitIni.setIniid(systemIni.getIniid());
            unitIni.setName(systemIni.getName());
            unitIni.setDvalue(systemIni.getDvalue());
            unitIni.setDescription(systemIni.getDescription());
            unitIni.setNowvalue(systemIni.getNowvalue());
            unitIni.setValidatejs(systemIni.getValidatejs());
            unitIni.setUnitid(unitId);
            unitIniDao.save(unitIni);
        }
    }

    @Override
    public void deleteUnitInisByUnitId(String unitId) {
        unitIniDao.deleteUnitInisByUnitid(unitId);
    }

	@Override
	public List<UnitIni> getIniList(String iniId) {
		return RedisUtils.getObject(KEY+"." + iniId + ".object.list", 0, new TypeReference<List<UnitIni>>() {
        }, new RedisInterface<List<UnitIni>>() {
            @Override
            public List<UnitIni> queryData() {
                return unitIniDao.getIniList(iniId);
            }

        });
	}
}
