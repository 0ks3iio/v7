package net.zdsoft.career.data.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.career.data.constant.CarConstants;
import net.zdsoft.career.data.dao.CarPlanResultDao;
import net.zdsoft.career.data.entity.CarPlanResult;
import net.zdsoft.career.data.entity.CarTypeResult;
import net.zdsoft.career.data.enums.CarTypeEnum;
import net.zdsoft.career.data.service.CarPlanResultService;
import net.zdsoft.career.data.service.CarTypeResultService;
import net.zdsoft.career.data.utils.SortResultType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("carPlanResultService")
public class CarPlanResultServiceImpl extends BaseServiceImpl<CarPlanResult,String> implements CarPlanResultService{

	@Autowired
	private CarTypeResultService carTypeResultService;
	@Autowired
	private CarPlanResultDao carPlanResultDao;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<CarPlanResult, String> getJpaDao() {
		return carPlanResultDao;
	}

	@Override
	protected Class<CarPlanResult> getEntityClass() {
		return CarPlanResult.class;
	}

	@Override
	public List<CarPlanResult> findByStudentIds(String[] studentIds) {
		List<CarPlanResult> carPlanTestResults = carPlanResultDao.findByStudentIds(studentIds);
		if (CollectionUtils.isNotEmpty(carPlanTestResults)) {
			List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds),new TR<List<Student>>() {});
			Map<String,String> studentNameMap = students.stream().collect(Collectors.toMap(Student::getId, Student::getStudentName));
			CarTypeResult carTypeResult = null;
			char[] types = null;
			List<String> typelists = null;
			for (CarPlanResult carPlanResult : carPlanTestResults) {
				carPlanResult.setStudentName(studentNameMap.get(carPlanResult.getStudentId()));
				if (RedisUtils.exists(CarConstants.CAR_RESULT_LIST+carPlanResult.getResultType())) {
					carPlanResult.setContent(RedisUtils.get(CarConstants.CAR_RESULT_LIST+carPlanResult.getResultType()));
				} else {
					carTypeResult = carTypeResultService.findByResultType(carPlanResult.getResultType());
					if (carTypeResult != null) {
						RedisUtils.set(CarConstants.CAR_RESULT_LIST+carPlanResult.getResultType(), carTypeResult.getContent());
						carPlanResult.setContent(carTypeResult.getContent());
					}
				}
				types =  carPlanResult.getResultType().toCharArray();
				typelists = Lists.newArrayList();
				for (char str : types) {
					typelists.add(CarTypeEnum.getName(String.valueOf(str)));
				}
				carPlanResult.setTypelist(typelists);
			}
		}
		return carPlanTestResults;
	}

	@Override
	public void saveTestResult(CarPlanResult testResult,String unitId,String studentId) {
		testResult.setId(UuidUtils.generateUuid());
		testResult.setSchoolId(unitId);
		testResult.setStudentId(studentId);
		testResult.setIsDeleted(0);
		testResult.setCreationTime(new Date());
		testResult.setModifyTime(new Date());
		Set<String> types = null;
		if (RedisUtils.exists(CarConstants.CAR_TYPE_LIST)) {
			types = RedisUtils.smembers(CarConstants.CAR_TYPE_LIST);
		} else {
			types = carTypeResultService.findAllTypes();
			RedisUtils.sadd(CarConstants.CAR_TYPE_LIST, types.toArray(new String[0]));
		}
		List<String> resultTypes = Arrays.asList(testResult.getResultType().split(","));
		String[] maxResult = resultTypes.stream().sorted((r1,r2) -> Integer.parseInt(r2.substring(2))-Integer.parseInt(r1.substring(2))).toArray(String[]::new);
		List<String> typelist = Lists.newArrayList();
		SortResultType.Perm(maxResult, 0, 5, typelist);
		boolean haveType = false;
		for (String str : typelist) {
			if (types.contains(str)) {
				testResult.setResultType(str);
				haveType = true;
				break;
			}
		}
		if (!haveType) {
			testResult.setResultType(maxResult[0].substring(0,1)+maxResult[1].substring(0,1)+maxResult[2].substring(0,1));
		}
		List<CarPlanResult> carPlanTestResult = carPlanResultDao.findByStudentIds(new String[]{studentId});
		if (CollectionUtils.isNotEmpty(carPlanTestResult)) {
			carPlanResultDao.deleteTest(new String[]{studentId});
		}
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		String uu=dir.getDir()+File.separator+"careerplan"+File.separator+unitId+File.separator+studentId+".pdf";
		File f = new File(uu);
		if (f.exists()) {
			f.delete();
		}
		carPlanResultDao.save(testResult);
	}

	@Override
	public List<CarPlanResult> findAllByTimeAndName(String unitId, String classId, String studentName, Pagination page) {
		List<CarPlanResult> carPlanTestResults = null;
		Pageable pageable = Pagination.toPageable(page);
		studentName = "%" + studentName + "%";
		if (StringUtils.isNotBlank(classId)) {
			carPlanTestResults = carPlanResultDao.findByClassIdAndStuName(unitId,classId,studentName,pageable);
			page.setMaxRowCount(carPlanResultDao.findByClassIdAndStuName(unitId,classId,studentName).size());
		} else {
			carPlanTestResults = carPlanResultDao.findBystuName(unitId,studentName,pageable);
			page.setMaxRowCount(carPlanResultDao.findBystuName(unitId,studentName).size());
		}
		String[] studentIds = carPlanTestResults.stream().map(res -> res.getStudentId()).toArray(String[]::new);
		List<Student> students = Lists.newArrayList();
		if (studentIds.length > 1000) {
			int a = studentIds.length/500;
			int b = studentIds.length%500;
			for (int i = 0; i < a; i++) {
				students.addAll(SUtils.dt(studentRemoteService.findListByIds((String[])ArrayUtils.subarray(studentIds, i*500, (i+1)*500)),new TR<List<Student>>() {}));
			}
			if (b!=0) {
				students.addAll(SUtils.dt(studentRemoteService.findListByIds((String[])ArrayUtils.subarray(studentIds, a*500, studentIds.length)),new TR<List<Student>>() {}));
			}
		} else {
			students = SUtils.dt(studentRemoteService.findListByIds(studentIds),new TR<List<Student>>() {});
		}
		Map<String,String> studentNameMap = students.stream().collect(Collectors.toMap(Student::getId,Student::getStudentName));
		Clazz clazz = null;
		Map<String,String> stuClass = null;
		Map<String,String> clazzName = null;
		if (StringUtils.isNotBlank(classId)) {
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(new String[]{classId}),new TR<List<Clazz>>() {});
			clazz = clazzs.get(0);
		} else {
			stuClass = students.stream().collect(Collectors.toMap(Student::getId, Student::getClassId));
			Set<String> classIds = students.stream().map(stu -> stu.getClassId()).collect(Collectors.toSet());
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])),new TR<List<Clazz>>() {});
			clazzName = clazzs.stream().collect(Collectors.toMap(Clazz::getId, Clazz::getClassNameDynamic));
		}
		for (CarPlanResult carPlanTestResult : carPlanTestResults) {
			carPlanTestResult.setStudentName(studentNameMap.get(carPlanTestResult.getStudentId()));
			if (StringUtils.isNotBlank(classId)) {
				carPlanTestResult.setClassName(clazz.getClassNameDynamic());
			} else {
				carPlanTestResult.setClassName(clazzName.get(stuClass.get(carPlanTestResult.getStudentId())));
			}
		}
		return carPlanTestResults;
	}

	@Override
	public void deleteTest(String studentIds,String unitId) {
		if (StringUtils.isNotBlank(studentIds)) {
			String[] stuIds = studentIds.split(","); 
			StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
			carPlanResultDao.deleteTest(stuIds);
			String uu = "";
			File f = null;
			for (String studentId : stuIds) {
				uu = dir.getDir()+File.separator+"careerplan"+File.separator+unitId+File.separator+studentId+".pdf";
				f = new File(uu);
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}

}
