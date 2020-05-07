package net.zdsoft.officework.remote.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.officework.constant.OfficeConstants;
import net.zdsoft.officework.entity.OfficeClassroomRelevance;
import net.zdsoft.officework.entity.OfficeHealthDevice;
import net.zdsoft.officework.remote.service.OfficeAttanceRemoteService;
import net.zdsoft.officework.service.OfficeClassroomRelevanceService;
import net.zdsoft.officework.service.OfficeHealthDeviceService;
import net.zdsoft.officework.utils.OfficeHealthUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service("officeAttanceRemoteService")
public class OfficeAttanceRemoteServiceImpl implements OfficeAttanceRemoteService {

	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private OfficeClassroomRelevanceService officeClassroomRelevanceService;
	@Autowired
	private OfficeHealthDeviceService officeHealthDeviceService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Override
	public Set<String> getAttUnitIds() {
		List<OfficeHealthDevice> devices = officeHealthDeviceService.findByType(OfficeConstants.HEALTH_DEVICE_TYPE_04);
		Set<String> unitIds = EntityUtils.getSet(devices, OfficeHealthDevice::getUnitId);
		return unitIds;
	}

	@Override
	public Set<String> findInClassStudentIds(String unitId, String placeId,
			Set<String> stuIds) {
		Set<String> studentIds = Sets.newHashSet();
		List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
		Set<String> studentCodes = EntityUtils.getSet(students, Student::getStudentCode);
		Map<String,String> studentCodeMap = EntityUtils.getMap(students, Student::getId,Student::getStudentCode);
		studentCodes.remove(null);
		List<OfficeHealthDevice> devices = officeHealthDeviceService.getByUnitAndType(unitId,OfficeConstants.HEALTH_DEVICE_TYPE_04);
		OfficeHealthDevice device = devices.get(0);
		if(device == null){
			return studentIds;
		}
		String devSn = device.getSerialNumber();
		OfficeClassroomRelevance relevance = officeClassroomRelevanceService.findByPlaceId(placeId);
		String classroomId = "";
		String classroomName = "";
		if(relevance!=null){
			classroomId = relevance.getClassroomId();
		}
		TeachPlace place =  SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(placeId),new TR<TeachPlace>() {});
		if(place!=null && StringUtils.isNotEmpty(place.getPlaceName())){
			classroomName = place.getPlaceName().trim();
		}
		if(devSn==null||studentCodes.size()==0||(StringUtils.isBlank(classroomId) && StringUtils.isBlank(classroomName))){
			return studentIds;
		}
		Map<String, String> inClassMap = OfficeHealthUtils.getStudentinClassroom(devSn, classroomId,classroomName, studentCodes.toArray(new String[0]));
		for(String stuId:studentCodeMap.keySet()){
			String stuCode = studentCodeMap.get(stuId);
			if(StringUtils.isNotEmpty(stuCode)&&inClassMap.containsKey(stuCode)){
				if("1".equals(inClassMap.get(stuCode))){
					studentIds.add(stuId);
				}
			}
		}
		return studentIds;
	}



}
