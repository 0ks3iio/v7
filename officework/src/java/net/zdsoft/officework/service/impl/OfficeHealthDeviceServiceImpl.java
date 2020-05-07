package net.zdsoft.officework.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.officework.dao.OfficeHealthDeviceDao;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.service.OfficeHealthDeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("officeHealthDeviceService")
public class OfficeHealthDeviceServiceImpl extends BaseServiceImpl<OfficeHealthDevice, String> implements OfficeHealthDeviceService {
	
	@Autowired
	private OfficeHealthDeviceDao officeHealthDeviceDao;
	
	@Override
	protected BaseJpaRepositoryDao<OfficeHealthDevice, String> getJpaDao() {
		return officeHealthDeviceDao;
	}

	@Override
	protected Class<OfficeHealthDevice> getEntityClass() {
		return OfficeHealthDevice.class;
	}

	@Override
	public OfficeHealthDevice findBySerialNumber(String serialNumber) {
		 return RedisUtils.getObject("officework.device." + serialNumber + ".object", 0, new TypeReference<OfficeHealthDevice>() {
	        }, new RedisInterface<OfficeHealthDevice>() {
	            @Override
	            public OfficeHealthDevice queryData() {
	            	return officeHealthDeviceDao.findBySerialNumber(serialNumber);
	            }

	        });
	}

	@Override
	public List<OfficeHealthDevice> findByType(String type) {
		 return RedisUtils.getObject("officework.device." + type + ".list", 0, new TypeReference<List<OfficeHealthDevice>>() {
	        }, new RedisInterface<List<OfficeHealthDevice>>() {
	            @Override
	            public List<OfficeHealthDevice> queryData() {
	            	return officeHealthDeviceDao.findByType(type);
	            }
	        });
	}

	@Override
	public List<OfficeHealthDevice> getByUnitAndType(String unitId, String type) {
		return RedisUtils.getObject("officework.device." + unitId + type + ".list", 0, new TypeReference<List<OfficeHealthDevice>>() {
        }, new RedisInterface<List<OfficeHealthDevice>>() {
            @Override
            public List<OfficeHealthDevice> queryData() {
            	return officeHealthDeviceDao.getByUnitAndType(unitId, type);
            }
        });
	}

	
	
}
