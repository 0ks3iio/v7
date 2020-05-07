package net.zdsoft.officework.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.dao.OfficeDataLogHkDao;
import net.zdsoft.officework.dto.HkDataDto;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeDataLogHk;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.entity.OfficeHealthDoinoutInfo;
import net.zdsoft.officework.remote.service.OfficeHealthDoinoutInfoRemoteService;
import net.zdsoft.officework.service.OfficeDataLogHkService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.officework.service.OfficeHealthDoinoutInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
@Service("officeDataLogHkService")
public class OfficeDataLogHkServiceImpl extends BaseServiceImpl<OfficeDataLogHk, String> implements
		OfficeDataLogHkService {

	@Autowired
	private OfficeDataLogHkDao officeDataLogHkDao;
	@Autowired
	private OfficeHealthDeviceService officeHealthDeviceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private OfficeHealthDoinoutInfoService officeHealthDoinoutInfoService;
	@Autowired
	private OfficeHealthDoinoutInfoRemoteService officeHealthDoinoutInfoRemoteService;
	
	@Override
	public void saveData(HkDataDto dataDto) {
		OfficeDataLogHk dataLogHk = new OfficeDataLogHk();
		dataLogHk.setId(UuidUtils.generateUuid());
		dataLogHk.setCardNumber(dataDto.getCardNumber());
		dataLogHk.setUnitIdx(dataDto.getUnitIdx());
		dataLogHk.setCommInfo(dataDto.getCommInfo());
		dataLogHk.setCreationTime(new Date());
		dataLogHk.setExtInfo(dataDto.getExtInfo());
		dataLogHk.setModifyTime(new Date());
		dataLogHk.setIsDeleted(0);
		save(dataLogHk);
		if(StringUtils.isNotBlank(dataDto.getCardNumber()) && StringUtils.isNotBlank(dataDto.getPicUrl()) && getEventCodeSet().contains(dataDto.getEventCode())){
			handleInOutData(dataDto);
		}
	}

	private void handleInOutData(HkDataDto dataDto) {
		OfficeHealthDevice device = officeHealthDeviceService.findBySerialNumber(dataDto.getUnitIdx()+"-"+dataDto.getDeviceId());
		if(device!=null){
			String schoolId =  device.getUnitId();
			Student student = SUtils.dt(studentRemoteService.findByCardNumber(schoolId, dataDto.getCardNumber()),new TR<Student>() {});
			if(student!=null){
				List<OfficeHealthDoinoutInfo> list = new ArrayList<>();
				OfficeHealthDoinoutInfo doinoutInfo = new OfficeHealthDoinoutInfo();
				if(2==device.getFlag()){
					doinoutInfo.setInOut(0);
				}else{
					doinoutInfo.setInOut(1);
				}
				doinoutInfo.setInOutTime(dataDto.getInOutTime());
				doinoutInfo.setSourceType(OfficeConstants.HEALTH_DATA_HK);
				doinoutInfo.setStudentCode(student.getStudentCode());
				doinoutInfo.setStudentId(student.getId());
				doinoutInfo.setUnitId(schoolId);
				doinoutInfo.setWristbandId(dataDto.getCardNumber());
				doinoutInfo.setPicUrl(dataDto.getPicUrl());
				doinoutInfo.setPicTips(dataDto.getPicTips());
				list.add(doinoutInfo);
				Map<String, OfficeHealthInfoDto> stepMap = Maps.newHashMap();
				try {
					officeHealthDoinoutInfoRemoteService.dealInOutData(list, stepMap);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("海康门禁一体机进出校：业务处理异常");
				}
			}
		}
	}

	private Set<String> getEventCodeSet(){
		Set<String>  eventCodes = Sets.newHashSet();
		eventCodes.add("83905280");//人脸认证通过
		eventCodes.add("83902976");//人脸加刷卡加指纹认证通过
		eventCodes.add("83902208");//人脸加密码加指纹认证通过
		eventCodes.add("83901440");//人脸加刷卡认证通过
		eventCodes.add("83900672");//人脸加密码认证通过
		eventCodes.add("83899904");//人脸加指纹认证通过
		eventCodes.add("83912960");//人证比对通过
		return eventCodes;
	}
	
	@Override
	protected BaseJpaRepositoryDao<OfficeDataLogHk, String> getJpaDao() {
		return officeDataLogHkDao;
	}

	@Override
	protected Class<OfficeDataLogHk> getEntityClass() {
		return OfficeDataLogHk.class;
	}


}
