package net.zdsoft.newgkelective.data.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Set;

import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.newgkelective.data.service.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.ChosenSearchDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.system.remote.service.McodeRemoteService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewGkElectiveDivideCommonAction extends DataImportAction {
	@Autowired
	NewGkDivideService newGkDivideService;
	@Autowired
	NewGkChoiceService newGkChoiceService;
	@Autowired
	NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	CourseRemoteService courseRemoteService;
	@Autowired
	NewGkDivideClassService newGkDivideClassService;
	@Autowired
	NewGkChoResultService newGkChoResultService;
	@Autowired
	NewGkClassStudentService newGkClassStudentService;
	@Autowired
	NewGkScoreResultService newGkScoreResultService;
	@Autowired
	NewGkDivideExService newGkDivideExService;
	@Autowired
	StudentRemoteService studentRemoteService;
	@Autowired
	McodeRemoteService mcodeRemoteService;
	@Autowired
	NewGKStudentRangeService newGKStudentRangeService;
	@Autowired
	NewGkChoRelationService newGkChoRelationService;
	@Autowired
	NewGkReferScoreService newGkReferScoreService;
	@Autowired
	ClassRemoteService classRemoteService;
	@Autowired
	NewGkClassBatchService classBatchService;
	@Autowired
    NewGkArrayService newGkArrayService;
	@Autowired
	NewGkSubjectGroupColorService newGkSubjectGroupColorService;

	/**
	 * 是否正在混合分班中
	 * 
	 * @return
	 */
	public boolean isNowDivide(String divideId) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId;
		if (RedisUtils.get(key) != null && "start".equals(RedisUtils.get(key))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否智能分班中
	 * @param divideId
	 * @return
	 */
	public boolean checkAutoTwo(String divideId){
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_" + divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_TWO_"
				+ divideId + "_mess";
		
		if (RedisUtils.get(key) != null && "start".equals(RedisUtils.get(key))) {
			// 智能分班
			return true;
		} else {
			if (RedisUtils.get(key) != null && ("success".equals(RedisUtils.get(key))
					|| "error".equals(RedisUtils.get(key)))) {
				RedisUtils.del(new String[] { key, keyMess });
			}
			return false;
		}
	}

	/**
	 * 查询subjectIds组合的所有学生
	 * 
	 * @param newGkChoice
	 * @param subjectIds
	 * @return
	 */
	public List<StudentResultDto> findByChoiceSubjectIds(
			NewGkChoice newGkChoice, String subjectIds) {
		ChosenSearchDto searchDto = new ChosenSearchDto();
		searchDto.setUnitId(newGkChoice.getUnitId());
		searchDto.setGradeId(newGkChoice.getGradeId());
		if (StringUtils.isNotBlank(subjectIds)
				&& !BaseConstants.ZERO_GUID.equals(subjectIds)) {
			searchDto.setSubjectIds(subjectIds);
		}
		List<StudentResultDto> allStuDtoList = newGkChoResultService.findGradeIdList(newGkChoice.getUnitId(),newGkChoice.getGradeId(),newGkChoice.getId(),searchDto.getSubjectIds());
		return allStuDtoList;
	}

	/**
	 * 将set排序，并转换成以，隔开的字符串
	 * 
	 * @param set
	 * @return
	 */
	public String keySort(Set<String> set) {
		List<String> l = new ArrayList<String>(set);
		return keyListSort(l);
	}
	
	public String keyListSort(List<String> list) {
		Collections.sort(list);
		String s2 = "";
		for (String s1 : list) {
			s2 = s2 + "," + s1;
		}
		s2 = s2.substring(1);
		return s2;
	}
	

	public String nameSet(Map<String, Course> courseMap, String ids) {
		String[] s = ids.split(",");
		Arrays.sort(s);
		String returnS = "";
		for (String s1 : s) {
			returnS = returnS
					+ StringUtils.trimToEmpty(courseMap.get(s1) == null ? ""
							: courseMap.get(s1).getShortName());// 简称
		}
		return returnS;
	}

	// 组合 两两组合
	public List<String> keySort2(Set<String> set) {
		if (set.size() <= 2) {
			// 无需两两组合
			return new ArrayList<String>();
		}
		List<String> returnList = new ArrayList<String>();
		List<String> l = new ArrayList<String>(set);
		Collections.sort(l);
		for (int i = 0; i < l.size() - 1; i++) {
			for (int j = i + 1; j < l.size(); j++) {
				returnList.add(l.get(i) + "," + l.get(j));
			}
		}
		return returnList;
	}

	/**
	 * 返回语数英的科目id
	 * 
	 * @return
	 */
	public Set<String> findByCodesYSY() {
		List<Course> courseList = SUtils.dt(
				courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()), new TR<List<Course>>() {
				});
		return EntityUtils.getSet(courseList, e->e.getId());
	}
	
	/**
	 * 查询学生在多个班级的学生id
	 * @return Map  key:stuId  value:学生所在组合班级name,
	 */
	public Map<String,String> checkStuClassNum(List<NewGkDivideClass> list){
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, String>();
		}
		Map<String,String> returnMap= new HashMap<String, String>();
		Map<String,Set<String>> tempMap= new HashMap<String, Set<String>>();
		Map<String,String>  groupNameMap=new HashMap<String, String>();
		
		
		for(NewGkDivideClass dd:list){
			List<String> stuList = dd.getStudentList();
			if(CollectionUtils.isNotEmpty(stuList)){
				groupNameMap.put(dd.getId(), dd.getClassName());
				for(String s:stuList){
					if(!tempMap.containsKey(s)){
						tempMap.put(s, new HashSet<String>());
					}
					tempMap.get(s).add(dd.getId());
				}
			}
		}

		if(tempMap.size()>0){
			for(String key:tempMap.keySet()){
				if(tempMap.get(key).size()>1){
					Set<String> set = tempMap.get(key);
					String e="";
					for(String id:set){
						e=e+","+groupNameMap.get(id);
					}
					e=e.substring(1);
					returnMap.put(key, e);
				}
			}
		}
		return returnMap;
	}
	
	public static <T> List<T> subList(List<T> list, int startIndex, int endIndex) {
		if (list == null) {
			throw new RuntimeException("list cannot be null!");
		}
		if (startIndex > list.size()) {
			throw new IndexOutOfBoundsException("start index out of size!");
		}
		if (endIndex > list.size()) {
			throw new IndexOutOfBoundsException("end index out of size!");
		}
		List<T> newList = new ArrayList<T>();
		for (int i = startIndex; i < endIndex; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}
	
	public List<String>[] autoStuIdToXzbId(String referScoreId,List<StudentResultDto> stuDtoList,int openNum) {
		makeStudentSubjectScore(referScoreId, stuDtoList);
		// 根据先性别平均 后总成绩分数平均
		// 学生(每班学生)
		List<String>[] array = new List[openNum];
		for (int i = 0; i < openNum; i++) {
			array[i] = new ArrayList<String>();
		}
		// 性别
		List<StudentResultDto> studentsMaleTemp = new ArrayList<StudentResultDto>();// 男
		List<StudentResultDto> studentsFeMaleTemp = new ArrayList<StudentResultDto>();// 女
		for (StudentResultDto student2 : stuDtoList) {
			if ("1".equals(student2.getSex())) {
				// 男
				studentsMaleTemp.add(student2);
			} else {
				studentsFeMaleTemp.add(student2);
			}
		}
		// 按成绩
		onlysortStuScore(studentsMaleTemp);
		onlysortStuScore(studentsFeMaleTemp);
		openClass(openNum, studentsMaleTemp, array);
		// array
		int max = 0;
		int min = 0;
		for (int i = 1; i < openNum; i++) {
			if (array[max].size() < array[i].size()) {
				max = i;
			}
			if (array[min].size() > array[i].size()) {
				min = i;
			}
		}
		if (min < max) {
			// 取得最大值的第一个位置
			int findFirstMax = max;
			for (int i = 0; i < openNum; i++) {
				if (array[max].size() == array[i].size()) {
					findFirstMax = i;
					break;
				}
			}
			// 顺序
			List<String>[] newArray = new List[openNum];
			int j = 0;
			for (int i = findFirstMax - 1; i >= 0; i--) {
				newArray[j] = array[i];
				j++;
			}
			for (int i = openNum - 1; i >= findFirstMax; i--) {
				newArray[j] = array[i];
				j++;
			}
			array = newArray;
			openClass(openNum, studentsFeMaleTemp, array);
		} else {
			// 顺序
			List<String>[] newArray = new List[openNum];
			int j = 0;
			for (int i = min; i < openNum; i++) {
				newArray[j] = array[i];
				j++;
			}
			for (int i = 0; i < min; i++) {
				newArray[j] = array[i];
				j++;
			}
			array = newArray;
			openClass(openNum, studentsFeMaleTemp, array);
		}

		return array;

	}
	
		
	/**
	 * 按成绩
	 * 
	 * @param temp
	 */
	public void onlysortStuScore(List<StudentResultDto> temp) {
		if (CollectionUtils.isNotEmpty(temp)) {
			Collections.sort(temp, new Comparator<StudentResultDto>() {
				@Override
				public int compare(StudentResultDto o1, StudentResultDto o2) {
					float cc = o2.getScore()==null?0:o2.getScore() - (o1.getScore()==null?0:o1.getScore());
					if (cc > 0) {
						return 1;
					} else if (cc < 0) {
						return -1;
					} else {
						return 0;
					}
				}

			});
		}
	}
	
	/**
	 * 
	 * @param index
	 *            开始位置
	 * @param classNum
	 * @param temp
	 * @param array
	 *            return 下一次开始位置
	 */
	public void openClass(int classNum, List<StudentResultDto> temp,
			List<String>[] array) {
		//可以增加一个参数 int maxNum 数据完全平均 maxNum==0是不考虑数据完全平均 否则maxNum人数必须在maxNum范围内 后面再考虑这一点
		//如果array 数量 2,2,2,2,3,3,3,3
		//temp 10个
		//那么顺排array=3,3,3,3,4,4,4,4 temp=2
		//逆排 array=3,3,3,3,4,4,5,5
		
		boolean flag = true;// 顺排：true,逆排 ：false
		int index = 0;
		if (CollectionUtils.isNotEmpty(temp)) {
			for (int i = 0; i < temp.size(); i++) {
				if (flag) {
					array[index].add(temp.get(i).getStudentId());
					index++;
					if (index == classNum) {
						flag = false;
					}
				} else {
					index--;
					array[index].add(temp.get(i).getStudentId());
					if (index == 0) {
						flag = true;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param index
	 *            开始位置
	 * @param classNum
	 * @param temp
	 * @param array
	 *            return 下一次开始位置
	 */
	public void openClassBySex(int classNum, List<StudentResultDto> temp,
			List<String>[] array) {
		
		// 性别
		List<StudentResultDto> studentsMaleTemp = new ArrayList<StudentResultDto>();// 男
		List<StudentResultDto> studentsFeMaleTemp = new ArrayList<StudentResultDto>();// 女
		for (StudentResultDto student2 : temp) {
			if ("男".equals(student2.getSex())) {
				// 男
				studentsMaleTemp.add(student2);
			} else {
				studentsFeMaleTemp.add(student2);
			}
		}
		// 按成绩
		onlysortStuScore(studentsMaleTemp);
		onlysortStuScore(studentsFeMaleTemp);
		openClass(classNum, studentsMaleTemp, array);
		// array
		int max = 0;
		int min = 0;
		for (int i = 1; i < classNum; i++) {
			if (array[max].size() < array[i].size()) {
				max = i;
			}
			if (array[min].size() > array[i].size()) {
				min = i;
			}
		}
		if (min < max) {
			// 取得最大值的第一个位置
			int findFirstMax = max;
			for (int i = 0; i < classNum; i++) {
				if (array[max].size() == array[i].size()) {
					findFirstMax = i;
					break;
				}
			}
			// 顺序
			List<String>[] newArray = new List[classNum];
			int j = 0;
			for (int i = findFirstMax - 1; i >= 0; i--) {
				newArray[j] = array[i];
				j++;
			}
			for (int i = classNum - 1; i >= findFirstMax; i--) {
				newArray[j] = array[i];
				j++;
			}
			array = newArray;
			openClass(classNum, studentsFeMaleTemp, array);
		} else {
			// 顺序
			List<String>[] newArray = new List[classNum];
			int j = 0;
			for (int i = min; i < classNum; i++) {
				newArray[j] = array[i];
				j++;
			}
			for (int i = 0; i < min; i++) {
				newArray[j] = array[i];
				j++;
			}
			array = newArray;
			openClass(classNum, studentsFeMaleTemp, array);
		}
	}
	
	/**
	 * 组装学生各科成绩
	 * 
	 * @param referScoreId
	 * @param stuDtoList
	 */
	public void makeStudentSubjectScore(String referScoreId,
			List<StudentResultDto> stuDtoList) {
		if(StringUtils.isBlank(referScoreId)) {
			return;
		}
		//保留两位小数
		
		// 计算总分
		float all = 0.0f;
		float ysy = 0.0f;
		Map<String, Map<String, Float>> map = newGkScoreResultService
				.findMapByReferScoreId(getLoginInfo().getUnitId(), referScoreId);
		Set<String> ysyIds = findByCodesYSY();
		for (StudentResultDto sDto : stuDtoList) {
			if (map.containsKey(sDto.getStudentId())) {
				Map<String, Float> stuScoreMap = map.get(sDto.getStudentId());
				sDto.setSubjectScore(stuScoreMap);
				all = 0.0f;
				ysy = 0.0f;
				for (Entry<String, Float> item : stuScoreMap.entrySet()) {
					all = all
							+ (item.getValue() == null ? 0.0f : item.getValue());
					if (ysyIds.contains(item.getKey())) {
						ysy = ysy
								+ (item.getValue() == null ? 0.0f : item
										.getValue());
					}
				}
				sDto.getSubjectScore()
						.put(NewGkElectiveConstant.YSY_SUBID,makeTwo(ysy));
				sDto.getSubjectScore()
						.put(NewGkElectiveConstant.ZCJ_SUBID, makeTwo(all));
				sDto.setScore(makeTwo(all));
			} else {
				sDto.setSubjectScore(new HashMap<String, Float>());
				sDto.getSubjectScore().put(NewGkElectiveConstant.YSY_SUBID,
						0.0f);
				sDto.getSubjectScore().put(NewGkElectiveConstant.ZCJ_SUBID,
						0.0f);
				sDto.setScore(0.0f);
			}
		}

	}
	/**
	 * 四舍五入 保留两位
	 * @param ft
	 * @return
	 */
	public float makeTwo(float ft) {
		BigDecimal   bd  =   new  BigDecimal((double)ft);  
		int   roundingMode  =  4;//四舍五入
		bd   =  bd.setScale(2,roundingMode);  
		ft   =  bd.floatValue(); 
		return ft;
	}
	
	public float makeDoubleTwo(double ft) {
		BigDecimal   bd  =   new  BigDecimal(ft);  
		int   roundingMode  =  4;//四舍五入
		bd   =  bd.setScale(2,roundingMode);  
		float ft1   =  bd.floatValue(); 
		return ft1;
	}
	
	public NewGkClassStudent initClassStudent(String unitId,String divideId,String classId,String studentId) {
		NewGkClassStudent item=new NewGkClassStudent();
		item.setId(UuidUtils.generateUuid());
		item.setUnitId(unitId);
		item.setDivideId(divideId);
		item.setClassId(classId);
		item.setStudentId(studentId);
		item.setModifyTime(new Date());
		item.setCreationTime(new Date());
		return item;
	}
	
	public NewGkDivideClass initNewGkDivideClass(String divideId,
			String subjectIds,String classType) {
		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		newGkDivideClass.setId(UuidUtils.generateUuid());
		newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
		newGkDivideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		newGkDivideClass.setModifyTime(new Date());
		newGkDivideClass.setCreationTime(new Date());
		newGkDivideClass.setDivideId(divideId);
		newGkDivideClass.setClassType(classType);
		if(StringUtils.isNotBlank(subjectIds)) {
			newGkDivideClass.setSubjectIds(subjectIds);
		}
		// newGkDivideClass.setBestType(NewGkElectiveConstant.BEST_TYPE_2);
		return newGkDivideClass;
	}
	
	

	@Override
	public String getObjectName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public List<String> getRowTitleList() {
		return null;
	}

	@Override
	public String dataImport(String filePath, String params) {
		return null;
	}

	@Override
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

	}
	
	
	/**
	 * 获取某个年级的实际平均班级人数 缓存10分钟
	 * @param gradeId
	 * @return
	 */
	public int findAvgByGradeId(String gradeId) {
		Integer avg= RedisUtils.getObject("grade_class_num"+gradeId, RedisUtils.TIME_TEN_MINUTES, new TypeReference<Integer>(){}, new RedisInterface<Integer>(){
			@Override
			public Integer queryData() {
				long allStuCount = studentRemoteService.CountStudByGradeId(gradeId);
				List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {});
				int avg=50;
				if(allStuCount!=0 && CollectionUtils.isNotEmpty(clazzList)) {
					avg=(int) ((allStuCount-1)/clazzList.size())+1;
				}
				return avg;
			}
        });
		if(avg==null || avg==0) {
			avg=50;
		}
		return avg;
	}
	
	 public String initTwoClass(NewGkDivide divide, Map<String,Course> courseMap, List<NewGkDivideClass> oldClassList,List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStudentList) {
    	String divideId = divide.getId();
    	String unitId = divide.getUnitId();
    	String followType = divide.getFollowType();
    	List<NewGkDivideClass> list3 = oldClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
    	Map<String, NewGkDivideClass> classMap = EntityUtils.getMap(oldClassList, NewGkDivideClass::getId);
    	List<NewGkDivideClass> newClassList = new ArrayList<NewGkDivideClass>();
    	NewGkDivideClass initNewGkDivideClass;
    	for (NewGkDivideClass cls : list3) {
    		String parentId = cls.getParentId();
    		NewGkDivideClass xzb = classMap.get(parentId);
    		if(xzb == null) {
    			return cls.getClassName()+" 找不到对应的行政班";
    		}
    		if(StringUtils.isBlank(cls.getSubjectIds())) {
    			return cls.getClassName()+" 未保存选考科目";
    		}
    		if(StringUtils.isBlank(cls.getSubjectIdsB())) {
    			return cls.getClassName()+" 未保存学考科目";
    		}
    		String xzbName = xzb.getClassName();
    		if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_A2)) {
    			initNewGkDivideClass = initNewGkDivideClass(divideId, cls.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_2);
    			initNewGkDivideClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
    			initNewGkDivideClass.setRelateId(cls.getId());
    			initNewGkDivideClass.setClassName(xzbName+"-"+courseMap.get(cls.getSubjectIds()).getSubjectName()+"选");
    			newClassList.add(initNewGkDivideClass);
    			 if (CollectionUtils.isNotEmpty(cls.getStudentList())) {
                     for (String student : cls.getStudentList()) {
                         insertStudentList.add(initClassStudent(unitId, divideId, initNewGkDivideClass.getId(), student));
                     }
                 }
    		}
    		if(followType.contains(NewGkElectiveConstant.FOLLER_TYPE_B2)) {
    			initNewGkDivideClass = initNewGkDivideClass(divideId, cls.getSubjectIdsB(), NewGkElectiveConstant.CLASS_TYPE_2);
    			initNewGkDivideClass.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
    			initNewGkDivideClass.setClassName(xzbName+"-"+courseMap.get(cls.getSubjectIdsB()).getSubjectName()+"学");
    			initNewGkDivideClass.setRelateId(cls.getId());
    			newClassList.add(initNewGkDivideClass);
    			 if (CollectionUtils.isNotEmpty(cls.getStudentList())) {
                     for (String student : cls.getStudentList()) {
                         insertStudentList.add(initClassStudent(unitId, divideId, initNewGkDivideClass.getId(), student));
                     }
                 }
    		}
		}
    	newClassList.forEach(e->{
    		e.setParentId(null);
    	});
    	insertClassList.addAll(newClassList);
    	return null;
    }
	 
	 public Integer subNum(String className) {
       //正则表达式，用于匹配非数字串，+号用于匹配出多个非数字串
       String regEx="[^0-9]+"; 
       Pattern pattern = Pattern.compile(regEx);
       //用定义好的正则表达式拆分字符串，把字符串中的数字留出来
       String[] cs = pattern.split(className);
       for(String cc:cs) {
    	   if(StringUtils.isNotBlank(cc)) {
    		   String regEx2="^\\d+$"; 
    		   Pattern pattern1 = Pattern.compile(regEx2);
    		   if(pattern1.matcher(cc.trim()).matches()) {
        		   return Integer.parseInt(cc.trim());
        	   }
    	   }
    	   
       }
       return null;
	}
}
