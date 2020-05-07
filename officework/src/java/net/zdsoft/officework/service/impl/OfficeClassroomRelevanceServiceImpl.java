package net.zdsoft.officework.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dao.OfficeClassroomRelevanceDao;
import net.zdsoft.officework.dto.ClassRoomDto;
import net.zdsoft.officework.entity.OfficeClassroomRelevance;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.service.OfficeClassroomRelevanceService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.officework.utils.OfficeHealthUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("officeClassroomRelevanceService")
public class OfficeClassroomRelevanceServiceImpl extends BaseServiceImpl<OfficeClassroomRelevance, String> implements OfficeClassroomRelevanceService{

	@Autowired
	private OfficeClassroomRelevanceDao officeClassroomRelevanceDao;
	@Autowired
	private OfficeHealthDeviceService officeHealthDeviceService;
	
	@Override
	protected BaseJpaRepositoryDao<OfficeClassroomRelevance, String> getJpaDao() {
		return officeClassroomRelevanceDao;
	}

	@Override
	protected Class<OfficeClassroomRelevance> getEntityClass() {
		return OfficeClassroomRelevance.class;
	}

	@Override
	public OfficeClassroomRelevance findByPlaceId(String placeId) {
		return officeClassroomRelevanceDao.findByPlaceId(placeId);
	}

	@Override
	public List<OfficeClassroomRelevance> findByUnitId(String unitId) {
		return officeClassroomRelevanceDao.findByUnitId(unitId);
	}

	@Override
	public void deleteByPlaceId(String placeId) {
		officeClassroomRelevanceDao.deleteByPlaceId(placeId);
	}

	@Override
	public List<ClassRoomDto> getClassRoomList(String unitId) {
		return RedisUtils.getObject("officework.classroom." + unitId + ".list", 10*60, new TypeReference<List<ClassRoomDto>>() {
        }, new RedisInterface<List<ClassRoomDto>>() {
            @Override
            public List<ClassRoomDto> queryData() {
            	List<OfficeHealthDevice> devices = officeHealthDeviceService.getByUnitAndType(unitId,OfficeConstants.HEALTH_DEVICE_TYPE_04);
            	if(CollectionUtils.isEmpty(devices) || devices.get(0) == null){
            		List<ClassRoomDto> classRoomDtos = Lists.newArrayList();
            		return classRoomDtos;
            	}
            	String devSn = devices.get(0).getSerialNumber();
            	return OfficeHealthUtils.getClassroomIdList(devSn);
            }
        });
	}


}
