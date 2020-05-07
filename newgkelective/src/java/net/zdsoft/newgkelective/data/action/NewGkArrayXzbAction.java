package net.zdsoft.newgkelective.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.*;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.service.*;
import net.zdsoft.newgkelective.data.utils.MyNumberUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/newgkelective/xzb")
public class NewGkArrayXzbAction extends NewGkRoleCommonAction{
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkArrayService newGkArrayService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkLessonTimeExService newGkLessonTimeExService;
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkArrayItemService newGkArrayItemService;
	@Autowired
	private NewGkSubjectTimeService newGkSubjectTimeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private NewGkTeacherPlanService teacherPlanService;
	@Autowired
	private NewGkElectiveArrayComputeService arrayComputeService;


	@RequestMapping("{arrayId}/mutilArray/select")
	@ControllerInfo("行政班跨年级排课选择页")
	public String mutilArraySelect(@PathVariable String arrayId, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		List<Grade> gradeList =SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
		List<String> gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
		List<NewGkDivide> divideList = newGkDivideService.findListByOpenTypeAndGradeIdIn(unitId, NewGkElectiveConstant.DIVIDE_TYPE_07, gradeIdList.toArray(new String[gradeIdList.size()]));
		List<String> divideIdList = EntityUtils.getList(divideList, NewGkDivide::getId);
		//获取未毕业的排课结果
		List<NewGkArray> allList = newGkArrayService.findByDivideIds(unitId,divideIdList.toArray(new String[divideIdList.size()]));
		if(CollectionUtils.isEmpty(allList)){
			return errorFtl(map,"请至少创建一个排课方案");
		}

		Map<String, List<NewGkArray>> arrayByGrade = allList.stream().filter(e ->true|| "0".equals(e.getStat())).collect(Collectors.groupingBy(NewGkArray::getGradeId));
		Map<String, String> stMap = EntityUtils.getMap(allList, e -> e.getId(), e -> e.getStat());

		String nowGradeId = allList.stream().filter(e->e.getId().equals(arrayId)).map(e->e.getGradeId()).findFirst().orElse("");
		map.put("gradeList",gradeList);
		map.put("arrayByGrade",arrayByGrade);
		map.put("nowGradeId",nowGradeId);
		map.put("arrayId",arrayId);
		map.put("stMapJsonStr", JSONObject.toJSONString(stMap));

		return "/newgkelective/xzb/mutilArray.ftl";
	}


	@RequestMapping("{arrayId}/mutilArray/start")
	@ResponseBody
	@ControllerInfo("跨年级排课启动")
	public String mutilArrayStart(@PathVariable String arrayId, @RequestParam(name="arrayIds") String[] arrayIds) {
		arrayIds = Stream.of(arrayIds).filter(StringUtils::isNotBlank).toArray(String[]::new);
		List<NewGkArray> arrays = newGkArrayService.findListByIds(arrayIds);
		List<CGInputData> inputList = new ArrayList<>();
		arrayId = arrayIds[0];
		String reKey = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId;
		String reKey1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+arrayId+"_mess";
		NewGkArray newGkArray = null;

		Set<String> gradeIds = EntityUtils.getSet(arrays, e -> e.getGradeId());
		if(gradeIds.size() < arrays.size()){
			RedisUtils.set(reKey,"error");
			RedisUtils.set(reKey1, "每一个年级只能有一个排课方案");
			return error("每一个年级只能有一个排课方案");
		}

		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),Grade.class);
		Grade grade = gradeList.get(0);
		for (Grade gradeTemp : gradeList) {
			if(!java.util.Objects.equals(grade.getMornPeriods(),gradeTemp.getMornPeriods())
					|| !java.util.Objects.equals(grade.getAmLessonCount(),gradeTemp.getAmLessonCount())
					|| !java.util.Objects.equals(grade.getPmLessonCount(),gradeTemp.getPmLessonCount())
					|| !java.util.Objects.equals(grade.getNightLessonCount(),gradeTemp.getNightLessonCount())
					|| !java.util.Objects.equals(grade.getWeekDays(),gradeTemp.getWeekDays())){
				return error("请保持各年级授课时间相同");
			}
		}

		try {
//			int ii = 0;
			for (NewGkArray array : arrays) {
				final String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId();
				final String key1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId()+"_mess";

				if(arrayIds[0].equals(array.getId())){
					RedisUtils.set(key,"start");
					RedisUtils.set(key1, "进行中");
					newGkArray = array;
				}
				CGInputData cgInputData = arrayComputeService.computeArray3F7(key, key1, array);
				inputList.add(cgInputData);
			}
			if(newGkArray != null){
				arrayComputeService.dealMutilArray(inputList,newGkArray,reKey,reKey1);
			}else{
				throw new RuntimeException("array 为空");
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			if(reKey != null){
				RedisUtils.set(reKey,"error");
				RedisUtils.set(reKey1, e.getMessage());
			}
			return error(""+e.getMessage());
		}

		return returnSuccess();
	}



	@RequestMapping("/index/page")
	@ControllerInfo("行政班排课首页")
	public String showArrayIndex( ModelMap map, String useMaster) {
		map.put("useMaster", useMaster);
		return "/newgkelective/xzb/arrayIndex.ftl";
	}

	@RequestMapping("/index/list/page")
	@ControllerInfo("行政班排课列表")
	public String showArrayList(String arrayId, String useMaster, PageInfo pageInfo, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		Map<String, List<String>> roleMap = findRoleByUserId(unitId, userId);
//		List<Grade> gradeList = gradeRemoteService.findListObjectBy(new String[] {"schoolId", "isDeleted",  "isGraduate"}, new Object[] {unitId, 0, "0"} );
		List<Grade> gradeList =SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
		List<String> gradeIdList;

		if (roleMap.containsKey(NewGkRoleCommonAction.ROLE_ADMIN)) {
			gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
		} else if (roleMap.containsKey(NewGkRoleCommonAction.ROLE_GRADE)) {
			gradeIdList = roleMap.get(NewGkRoleCommonAction.ROLE_GRADE);
		} else {
			map.put("limit", true);
			return "/newgkelective/xzb/arrayList.ftl";
		}

		List<NewGkDivide> divideList = newGkDivideService.findListByOpenTypeAndGradeIdIn(unitId, NewGkElectiveConstant.DIVIDE_TYPE_07, gradeIdList.toArray(new String[gradeIdList.size()]));
		List<String> divideIdList = EntityUtils.getList(divideList, NewGkDivide::getId);
		//获取未毕业的排课结果
		List<NewGkArray> allList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(divideIdList)){
			if(Objects.equals(useMaster, "1")) {
				allList = newGkArrayService.findByDivideIdsWithMaster(unitId,divideIdList.toArray(new String[divideIdList.size()]));
			}else {
				allList = newGkArrayService.findByDivideIds(unitId,divideIdList.toArray(new String[divideIdList.size()]));
			}
		}

		boolean makeCril=false;
		map.put("allList", allList);

		List<NewGkArray> arrayList = null;
		if(StringUtils.isNotBlank(arrayId)) {
			NewGkArray array;
			if(Objects.equals(useMaster, "1"))
				array = newGkArrayService.findOneWithMaster(arrayId);
			else
				array = newGkArrayService.findOne(arrayId);


			if(NewGkElectiveConstant.IF_0.equals(array.getIsDeleted()+"")) {
				arrayList = Arrays.asList(array);
				map.put("arrayId", arrayId);
			}
		}

		if(arrayList == null){
			allList.sort(new Comparator<NewGkArray>() {
				@Override
				public int compare(NewGkArray o1, NewGkArray o2) {
					int b1 = Optional.ofNullable(o2.getIsDefault()).orElse(0) - Optional.ofNullable(o1.getIsDefault()).orElse(0);
					if(b1!=0)
						return b1;
					int b2 = Optional.ofNullable(o2.getTimes()).orElse(0) - Optional.ofNullable(o1.getTimes()).orElse(0);
					if(b2!=0)
						return b2;
					if(o2.getCreationTime() != null)
						return o2.getCreationTime().compareTo(o1.getCreationTime());
					else
						return -1;
				}
			});

			pageInfo.setItemsNum(allList.size());
			pageInfo.refresh();
			pageInfo.makeShowCount();
			//[2,5,10,15]
			List<Integer> pageList = new ArrayList<>();
			pageList.add(2);
			pageList.add(5);
			pageList.add(10);
			pageList.add(15);
			pageInfo.setPageList(pageList);
			arrayList = allList.subList(pageInfo.getStartIndex()-1, pageInfo.getEndIndex());
		}

		for(NewGkArray array:arrayList){
			String key = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId();
			String key1 = NewGkElectiveConstant.ARRAY_LESSON+"_"+array.getId()+"_mess";
			if(newGkArrayService.checkIsArrayIng(array.getId())){
				array.setNow(true);
				if(!makeCril){
					makeCril=true;
				}
			}else{
				array.setNow(false);
				//去除不必要的redis
				if("error".equals(RedisUtils.get(key))){
					//失败消息
					String errorMsg = RedisUtils.get(key1);
					if(errorMsg.length()>100) {
						errorMsg = errorMsg.substring(0, 100)+" ..";
					}
					array.setErrorMess(errorMsg);
				}else if("success".equals(RedisUtils.get(key))){
					RedisUtils.del(key,key1);
				}
			}
		}

		map.put("arrayList", arrayList);
		map.put("makeCril", makeCril);
		return "/newgkelective/xzb/arrayList.ftl";
	}
	@ResponseBody
	@RequestMapping("/saveGradeClass")
	@ControllerInfo("新增分班方案")
	public String saveGradeClass(String gradeId,String classIds, String acadyear, String useJxb, String semester) {
		String unitId=getLoginInfo().getUnitId();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		if(grade==null) {
			return error("没有找到年级");
		}
		List<Clazz> classList =new ArrayList<>();
		if(StringUtils.isNotBlank(classIds)) {
//			String[] classIdArr = classIds.split(",");findBySchoolIdGradeId
//			classList = SUtils.dt(classRemoteService.findListByIds(classIdArr),Clazz.class);
		}else {
			classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),Clazz.class);
		}
		if(CollectionUtils.isEmpty(classList)) {
			return error("没有找到班级");
		}
		classList = classList.stream().filter(e->Objects.equals(e.getIsGraduate(),0)).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(classList)) {
			return error("没有找到班级");
		}
		boolean useJxb2 = Objects.equals("1",useJxb);
		if(useJxb2 && (StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)))
			return error("请选择学年学期");

		try {
			String msg = newGkDivideService.saveXzbDivide(classList, grade, acadyear, semester, useJxb2);
			if(StringUtils.isNotBlank(msg))
				return error(msg);
		} catch (Exception e1) {
			e1.printStackTrace();
			return error(e1.getMessage()+"");
		}

		return returnSuccess();
	}


	@ResponseBody
	@RequestMapping("/deleteDivide")
	@ControllerInfo("删除分班方案")
	public String deleteDivide(String divideId) {
		List<NewGkArray> list=newGkArrayService.findByDivideId(divideId);
		if(CollectionUtils.isNotEmpty(list)){
			return error("已被使用，不能删除");
		}
		try {
			newGkDivideService.deleteDivide(getLoginInfo().getUnitId(),divideId);
		}catch (Exception e) {
			return error("操作失败");
		}
		return success("");
	}

	@ResponseBody
	@RequestMapping("/saveName")
	@ControllerInfo("修改排课特征名称")
	public String saveArrayName(String arrayId,String arrayName) {
		List<NewGkArray> arrayList = newGkArrayService.findByGradeId(getLoginInfo().getUnitId(),null,null,NewGkElectiveConstant.ARRANGE_XZB);
		if("create".equals(arrayId)) {
			for(NewGkArray ch : arrayList) {
				if(ch.getArrayName().equals(arrayName)) {
					return error("该名称已被其他记录使用！");
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(arrayList)) {
				NewGkArray toCh = null;
				for(NewGkArray ch : arrayList) {
					if(ch.getId().equals(arrayId)) {
						toCh = ch;
					} else if(ch.getArrayName().equals(arrayName)) {
						return error("该名称已被其他记录使用！");
					}
				}
				if(toCh == null) {
					return error("排记录不存在或已被删除！");
				}
				toCh.setArrayName(arrayName);
				toCh.setModifyTime(new Date());
				newGkArrayService.save(toCh);
			} else {
				return error("排课记录不存在或已被删除！");
			}
		}

		return success("操作成功");
	}
	@ResponseBody
	@RequestMapping("/saveDivideName")
	@ControllerInfo("修改分班特征名称")
	public String saveDivideName(String divideId,String divideName) {
		List<NewGkDivide> divideList = newGkDivideService.findListByOpenTypeAndGradeIdIn(getLoginInfo().getUnitId(), NewGkElectiveConstant.DIVIDE_TYPE_07, null);
		if("create".equals(divideId)) {
			for(NewGkDivide ch : divideList) {
				if(ch.getDivideName().equals(divideName)) {
					return error("该名称已被其他记录使用！");
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(divideList)) {
				NewGkDivide toCh = null;
				for(NewGkDivide ch : divideList) {
					if(ch.getId().equals(divideId)) {
						toCh = ch;
					} else if(ch.getDivideName().equals(divideName)) {
						return error("该名称已被其他记录使用！");
					}
				}
				if(toCh == null) {
					return error("记录不存在或已被删除！");
				}
				toCh.setDivideName(divideName);
				toCh.setModifyTime(new Date());
				newGkDivideService.save(toCh);
			} else {
				return error("记录不存在或已被删除！");
			}
		}

		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/saveArray")
	@ControllerInfo(value = "保存排课方案")
	public String saveArray(NewGkArray dto){
		NewGkArray addDto=null;
		NewGkArray old=null;
		NewGkDivide gkDivide = newGkDivideService.findOne(dto.getDivideId());
		if(gkDivide==null) {
			return error("分班方案不存在！");
		}
		dto.setGradeId(gkDivide.getGradeId());
		if(StringUtils.isNotBlank(dto.getId())){
			old = newGkArrayService.findOne(dto.getId());
		}
		if(old!=null){
			addDto=old;
			addDto.setArrayName(dto.getArrayName());
		}else{
			addDto=new NewGkArray();
			addDto.setId(UuidUtils.generateUuid());
			addDto.setCreationTime(new Date());
			//取得数据库最大值
			int maxTimes=newGkArrayService.findMaxByGradeId(getLoginInfo().getUnitId(), null, NewGkElectiveConstant.ARRANGE_XZB);
			addDto.setTimes(maxTimes+1);

			String arrayName = "";
			if(StringUtils.isBlank(dto.getArrayName())) {
				//获取学年学期 年级信息
				String semesterJson = semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId());
				Semester semester = SUtils.dc(semesterJson, Semester.class);
				Grade grade = SUtils.dc(gradeRemoteService.findOneById(dto.getGradeId()), Grade.class);
				arrayName = semester.getAcadyear()+"学年"+grade.getGradeName()+"第"+
						semester.getSemester()+"学期排课方案"+addDto.getTimes();
			}else {
				arrayName = dto.getArrayName();
			}

			addDto.setArrayName(arrayName);
		}

		if(addDto.getArrayName().getBytes().length > 80){
			return error("名称长度不能超过80. 每个汉字长度为2，其他长度为1");
		}
		addDto.setUnitId(getLoginInfo().getUnitId());
		addDto.setGradeId(dto.getGradeId());
		addDto.setDivideId(dto.getDivideId());
		addDto.setIsDeleted(NewGkElectiveConstant.IF_INT_0);
		addDto.setLessonArrangeId(dto.getLessonArrangeId());
		addDto.setModifyTime(new Date());
		addDto.setPlaceArrangeId(dto.getPlaceArrangeId());
		addDto.setStat(NewGkElectiveConstant.IF_0);
		addDto.setArrangeType(NewGkElectiveConstant.ARRANGE_XZB);
		try{
			newGkArrayService.saveArray(addDto);
		}catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage()+"");  // 避免前端提示框显示上一次的结果
		}
		return success(addDto.getId()==null?"success":addDto.getId());
	}



	@RequestMapping("/addArray/page")
	@ControllerInfo("新增行政班排课方案")
	public String addArrayIndex(String arrayId,String divideId, String useMaster, HttpServletRequest request, ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		String userId = getLoginInfo().getUserId();
		Map<String, List<String>> roleMap = findRoleByUserId(unitId, userId);

		// 当前未毕业年级
		// List<Grade> gradeList = gradeRemoteService.findListObjectBy(new String[] {"schoolId", "isDeleted",  "isGraduate"}, new Object[] {unitId, 0, "0"} );

		List<Grade> gradeList =SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);

		List<String> gradeIdList;
		if (roleMap.containsKey(NewGkRoleCommonAction.ROLE_ADMIN)) {
			gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
		} else if (roleMap.containsKey(NewGkRoleCommonAction.ROLE_GRADE)) {
			gradeIdList = roleMap.get(NewGkRoleCommonAction.ROLE_GRADE);
			gradeList = gradeList.stream().filter(e -> gradeIdList.indexOf(e.getId()) != -1).collect(Collectors.toList());
		} else {
			return errorFtl(map, "无行政班排课权限，请联系管理员处理");
		}

		Map<String,Grade> gradeMap=EntityUtils.getMap(gradeList, e->e.getId());
		map.put("gradeList", gradeList);
		//未毕业班级的所有分班方案
		List<NewGkDivide> divideList = null;
		if(Objects.equals(useMaster, "1")) {
			divideList = newGkDivideService.findListByOpenTypeAndGradeIdInWithMaster(unitId, NewGkElectiveConstant.DIVIDE_TYPE_07, gradeIdList.toArray(new String[gradeIdList.size()]));
		}else {
			divideList = newGkDivideService.findListByOpenTypeAndGradeIdIn(unitId, NewGkElectiveConstant.DIVIDE_TYPE_07, gradeIdList.toArray(new String[gradeIdList.size()]));
		}
		for(NewGkDivide d:divideList) {
			d.setGradeName(gradeMap.get(d.getGradeId()).getGradeName());
		}
		map.put("divideList", divideList);
		NewGkArray newGkArray=null;
		if(StringUtils.isNotBlank(arrayId)){
			newGkArray=newGkArrayService.findOne(arrayId);
		}
		if(newGkArray==null) {
			newGkArray=new NewGkArray();
		}
		if(StringUtils.isNotBlank(divideId)) {
			newGkArray.setDivideId(divideId);
		}
		if(StringUtils.isBlank(divideId) && StringUtils.isBlank(newGkArray.getDivideId())){
			//取得默认值
			if(CollectionUtils.isNotEmpty(divideList)){
				divideId=divideList.get(0).getId();
				newGkArray.setDivideId(divideId);
			}
		}
		//特征选中
		String lessArrayId=StringUtils.trimToEmpty(request.getParameter("lessArrayId"));
		if(StringUtils.isNotBlank(lessArrayId)) {
			newGkArray.setLessonArrangeId(lessArrayId);
		}
		map.put("newGkArray", newGkArray);
		String arrayName="";
		//取得数据库最大值
		//获取学年学期 年级信息
		String semesterJson = semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId());
		Semester semester = SUtils.dc(semesterJson, Semester.class);
		if(StringUtils.isNotBlank(newGkArray.getArrayName())) {
			arrayName=newGkArray.getArrayName();
		}else {
			int maxTimes=newGkArrayService.findMaxByGradeId(unitId, null, NewGkElectiveConstant.ARRANGE_XZB);
			arrayName = semester.getAcadyear()+"学年"+"第"+
					semester.getSemester()+"学期行政班排课方案"+(maxTimes+1);
		}
		map.put("arrayName", arrayName);

		// 学年学期信息
		List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TypeReference<List<String>>() {});
		map.put("acadyearList", acadyearList==null?new ArrayList<>():acadyearList);
		map.put("acadyear", semester.getAcadyear());
		map.put("semester", String.valueOf(semester.getSemester()));

		return "/newgkelective/xzb/arrayAdd.ftl";
	}

	@RequestMapping("/arraySet/pageIndex")
	@ControllerInfo(value = "查看排课设置")
	public String showArrayItem(String arrayId,ModelMap map){
		NewGkArray newArray = newGkArrayService.findOne(arrayId);
		if(newArray==null){
			return errorFtl(map, "排课方案不存在");
		}
		NewGkDivide newDivide = newGkDivideService.findById(newArray.getDivideId());
		if(newDivide!=null){
			map.put("newDivide",newDivide);
			List<NewGkDivide> divideList=new ArrayList<NewGkDivide>();
			divideList.add(newDivide);
			//提示
			List<NewGkDivideDto> list = newGkDivideService.makeDivideItem(divideList);
			if(CollectionUtils.isNotEmpty(list)){
				map.put("divideDto", list.get(0));
			}
		}

		Set<String> set=new HashSet<String>();
		set.add(newArray.getLessonArrangeId());
		set.add(newArray.getPlaceArrangeId());
		List<NewGkArrayItem> list=newGkArrayItemService.findListByIdIn(set.toArray(new String[]{}));
		Map<String,NewGkArrayItem> itemMap=new HashMap<String,NewGkArrayItem>();
		if(CollectionUtils.isNotEmpty(list)){
			List<NewGkArrayItem> itemList=null;
			for(NewGkArrayItem item:list){
				itemList = new ArrayList<NewGkArrayItem>();
				itemList.add(item);
				if(NewGkElectiveConstant.ARRANGE_TYPE_01.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_01,newArray.getGradeId(), itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_01, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_03.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_03,newArray.getGradeId(), itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_03, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_04.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_04, newArray.getGradeId(),itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_04, item);
				}else if(NewGkElectiveConstant.ARRANGE_TYPE_02.contains(item.getDivideType())){
					newGkArrayItemService.makeDtoData(NewGkElectiveConstant.ARRANGE_TYPE_02, newArray.getGradeId(),itemList);
					itemMap.put(NewGkElectiveConstant.ARRANGE_TYPE_02, item);
				}
			}

		}
		map.put("itemMap", itemMap);

		map.put("newArray", newArray);
		return "/newgkelective/xzb/arrayShowSet.ftl";

	}



















	@RequestMapping("/gradeArrange/edit")
	@ControllerInfo("排课特征编辑index")
	public String infoIndex(String divideId, String fromSolve, String arrayId, String arrayItemId, HttpServletRequest request, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findById(divideId);
		map.put("fromSolve", fromSolve);
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		map.put("gradeId", divide.getGradeId());
		if(StringUtils.isEmpty(arrayItemId)) {
			// 默认新增
			arrayItemId = defaultSaveTime(divide);
		}
		map.put("arrayItemId", StringUtils.trimToEmpty(arrayItemId));
		String toLi = request.getParameter("toLi");
		map.put("toLi", toLi);
		map.put("lessArrayId", StringUtils.trimToEmpty(request.getParameter("lessArrayId")));
		return "/newgkelective/xzb/arrangeEditIndex.ftl";
	}

	@ControllerInfo("年级特征编辑")
	@RequestMapping("/gradeSet/edit")
	public String gradeSetEdit(String divideId, String arrayItemId, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);;
		if (StringUtils.isNotEmpty(grade.getRecess())) {
			String[] ris = grade.getRecess().split(",");
			for(String ri : ris) {
				if(StringUtils.isNotEmpty(ri)) {
					if (ri.startsWith(BaseConstants.PERIOD_INTERVAL_2)) {
						map.put("ab", NumberUtils.toInt(ri.substring(1))-1);
					} else if(ri.startsWith(BaseConstants.PERIOD_INTERVAL_3)) {
						map.put("pb", NumberUtils.toInt(ri.substring(1))-1);
					}
				}
			}
		}
//		map.put("gradeId", divide.getGradeId());
//		map.put("divideId", divideId);
//		map.put("objectType", NewGkElectiveConstant.LIMIT_GRADE_0);
//		map.put("arrayItemId", arrayItemId);
//		map.put("fixGuid", BaseConstants.ZERO_GUID);
//		List<Course> subs = SUtils.dt(courseRemoteService.getListByCondition(getLoginInfo().getUnitId(), null,
//				 null, BaseConstants.ZERO_GUID, null, 1, null), new TR<List<Course>>() {});
//		map.put("subs", subs);
//
//		Map<String, Integer> piMap = getIntervalMap(grade);
//		if(grade.getWeekDays()==null) {
//			grade.setWeekDays(7);
//		}
//		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
//		map.put("piMap", piMap);
//		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
//		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
//		return "/newgkelective/xzb/gradeSetEdit.ftl";

		map.put("msCount", grade.getMornPeriods());
		map.put("amCount", grade.getAmLessonCount());
		map.put("pmCount", grade.getPmLessonCount());
		map.put("nightCount", grade.getNightLessonCount());
		map.put("recess", grade.getRecess());
		map.put("gradeId", divide.getGradeId());
		map.put("divideId", divideId);
		map.put("objectType", NewGkElectiveConstant.LIMIT_GRADE_0);
		map.put("arrayItemId", arrayItemId);
		map.put("fixGuid", BaseConstants.ZERO_GUID);
		List<Course> subs = SUtils.dt(courseRemoteService.getListByCondition(getLoginInfo().getUnitId(), null,
				null, BaseConstants.ZERO_GUID, null, 1, null), new TR<List<Course>>() {});
		map.put("subs", subs);

		if(grade.getWeekDays()==null) {
			grade.setWeekDays(7);
		}
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("dayOfWeekMap2", BaseConstants.dayOfWeekMap2);
		return "/newgkelective/basic/basicGradeSet.ftl";
	}

	private Map<String, Integer> getIntervalMap(Grade grade) {
		Integer mmCount = grade.getMornPeriods();
		Integer amCount = grade.getAmLessonCount();
		Integer pmCount = grade.getPmLessonCount();
		Integer nightCount = grade.getNightLessonCount();

		Map<String,Integer> piMap = new LinkedHashMap<>();
		piMap.put(BaseConstants.PERIOD_INTERVAL_1, mmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_2, amCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_3, pmCount);
		piMap.put(BaseConstants.PERIOD_INTERVAL_4, nightCount);
		return piMap;
	}

	/**
	 *  新增排课特征时，年级特征默认取添加周末不排课时间
	 * @param divide
	 * @return
	 */
	private String defaultSaveTime(NewGkDivide divide) {
		try {
			LessonTimeDtoPack pack = new LessonTimeDtoPack();
			pack.setObjType(NewGkElectiveConstant.LIMIT_GRADE_0);
			pack.setNeedSource(true);
			List<LessonTimeDto> sources = new ArrayList<LessonTimeDto>();
			List<LessonTimeDto> lds = new ArrayList<LessonTimeDto>();
			//默认周末2天不排课
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);
			List<String> msList = MyNumberUtils.getNumList(grade.getMornPeriods());
			List<String> amList = MyNumberUtils.getNumList(grade.getAmLessonCount());
			List<String> pmList = MyNumberUtils.getNumList(grade.getPmLessonCount());
			List<String> nightList = MyNumberUtils.getNumList(grade.getNightLessonCount());

			LessonTimeDto sd = new LessonTimeDto();
			sd.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
			sd.setIs_join(0);
			sd.setObjId(divide.getGradeId());
			sd.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
			sources.add(sd);

			Date now = new Date();
			int[] wds = {5,6};// 周六、日
			if(CollectionUtils.isNotEmpty(msList)) {
				for(String ms : msList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(divide.getGradeId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_1);
						dto.setPeriod(NumberUtils.toInt(ms));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(amList)) {
				for(String am : amList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(divide.getGradeId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_2);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(pmList)) {
				for(String am : pmList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(divide.getGradeId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_3);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(nightList)) {
				for(String am : nightList) {
					for (int wd : wds) {
						LessonTimeDto dto = new LessonTimeDto();
						dto.setObjId(divide.getGradeId());
						dto.setWeekday(wd);
						dto.setPeriod_interval(BaseConstants.PERIOD_INTERVAL_4);
						dto.setPeriod(NumberUtils.toInt(am));
						dto.setIs_join(0);
						dto.setGroupType(NewGkElectiveConstant.DIVIDE_GROUP_1);
						dto.setTimeType(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
						lds.add(dto);
					}
				}
			}
			pack.setSourceTimeDto(sources);
			pack.setLessonTimeDto(lds);

			//行政班 排课 存在走班课程时 统计虚拟课程 作为 课程特征的科目
			List<NewGkDivideClass> jxbList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divide.getId(),
					new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			List<NewGkSubjectTime> subjectTimeList = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(jxbList)) {
				Set<String> subIds = EntityUtils.getSet(jxbList, NewGkDivideClass::getSubjectIds);
				NewGkSubjectTime st = null;
				for (String subId : subIds) {
					st = new NewGkSubjectTime();
					st.setId(UuidUtils.generateUuid());
					st.setSubjectId(subId);
					st.setPeriod(0);
					st.setArrayItemId("");  //TODO
					st.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
					st.setIsNeed(NewGkElectiveConstant.IF_INT_1);
					st.setCreationTime(now);
					st.setModifyTime(now);
					subjectTimeList.add(st);
				}
			}

			String arrayItemId = newGkLessonTimeExService.addLessonTimeTable(pack, divide.getId());
			subjectTimeList.forEach(e->e.setArrayItemId(arrayItemId));
			//TODO 初始化 教师安排 NewGkTeacherPlan NewGkTeacherPlanEx
			List<NewGkTeacherPlan> jxbTpList = teacherPlanService.findByArrayItemIds(new String[] {divide.getId()}, true);
			List<NewGkTeacherPlan> tpList = new ArrayList<>();
			List<NewGkTeacherPlanEx> tpeList = new ArrayList<>();
			for (NewGkTeacherPlan tp : jxbTpList) {
				List<NewGkTeacherPlanEx> tpes = tp.getTeacherPlanExList();

				NewGkTeacherPlan newtp = EntityUtils.copyProperties(tp, NewGkTeacherPlan.class);
				newtp.setId(UuidUtils.generateUuid());
				newtp.setCreationTime(now);
				newtp.setModifyTime(now);
				newtp.setArrayItemId(arrayItemId);

				List<NewGkTeacherPlanEx> newtpes = EntityUtils.copyProperties(tpes, NewGkTeacherPlanEx.class, NewGkTeacherPlanEx.class);
				newtpes.forEach(e->{
					e.setId(UuidUtils.generateUuid());
					e.setTeacherPlanId(newtp.getId());
				});

				tpList.add(newtp);
				tpeList.addAll(newtpes);
			}

			//TODO 保存结果 ；为保征事务一致性需要放在一个service里面
			newGkSubjectTimeService.saveAll(subjectTimeList.toArray(new NewGkSubjectTime[0]));
			teacherPlanService.saveList(tpList, tpeList);

			return arrayItemId;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping("/courseFeatures/index")
	public String showCourseFeaturesIndex(String arrayItemId, String useMaster, ModelMap map) {
		NewGkArrayItem arrayItem = newGkArrayItemService.findOne(arrayItemId);
		String divideId = arrayItem.getDivideId();
		NewGkDivide divide = newGkDivideService.findById(divideId);
		String openType = divide.getOpenType();

		map.put("arrayItemId", arrayItemId);
		map.put("openType", openType);
		map.put("arrayItemId", arrayItemId);
		map.put("gradeId", divide.getGradeId());

		// 先取得整体时间参数
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(divide.getGradeId()), Grade.class);

		Map<String, Integer> piMap = getIntervalMap(grade);

		map.put("piMap", piMap);
		map.put("dayOfWeekMap", BaseConstants.dayOfWeekMap2);
		map.put("intervalNameMap", BaseConstants.PERIOD_INTERVAL_Map2);
		map.put("weekDays", grade.getWeekDays()==null?7:grade.getWeekDays());

		map.put("notBasic", "1");
		if (StringUtils.isNotEmpty(grade.getRecess())) {
			String[] ris = grade.getRecess().split(",");
			for(String ri : ris) {
				if(StringUtils.isNotEmpty(ri)) {
					if (ri.startsWith(BaseConstants.PERIOD_INTERVAL_2)) {
						map.put("ab", NumberUtils.toInt(ri.substring(1)));
					} else if(ri.startsWith(BaseConstants.PERIOD_INTERVAL_3)) {
						map.put("pb", NumberUtils.toInt(ri.substring(1)));
					}
				}
			}
		}
		String unitId=getLoginInfo().getUnitId();
		List<NewGKCourseFeatureDto> dtoList = null;
		if(Objects.equals(useMaster, "1")) {
			dtoList = newGkSubjectTimeService.findCourseFeaturesWithMaster(arrayItemId, map, grade, unitId);
		}else {
			dtoList = newGkSubjectTimeService.findCourseFeatures(arrayItemId, map, grade, unitId);
		}

		if (CollectionUtils.isNotEmpty(dtoList)) {
			Collections.sort(dtoList, new Comparator<NewGKCourseFeatureDto>() {
				@Override
				public int compare(NewGKCourseFeatureDto o1, NewGKCourseFeatureDto o2) {
					if(o1.getOrder() == null && o2.getOrder() == null) {
						if(o1.getSubjectId().equals(o2.getSubjectId())) {
							if (StringUtils.isNotBlank(o1.getSubjectType())) {
								return o1.getSubjectType().compareTo(o2.getSubjectType());
							} else {
								return 0;
							}
						}else {
							return o1.getSubjectId().compareTo(o2.getSubjectId());
						}
					}
					if (o1.getOrder() == null) {
						return 0;
					}
					if (o2.getOrder() == null) {
						return -1;
					}
					if (o1.getOrder().equals(o2.getOrder())) {
						if(o1.getSubjectId().equals(o2.getSubjectId())) {
							if (StringUtils.isNotBlank(o1.getSubjectType())) {
								return o1.getSubjectType().compareTo(o2.getSubjectType());
							} else {
								return 0;
							}
						}else {
							return o1.getSubjectId().compareTo(o2.getSubjectId());
						}
					}
					return o1.getOrder() - o2.getOrder();
				}

			});
		}
		map.put("dtoList", dtoList);
		Map<String, List<String[]>> mcodeMap = findMcode();
		map.put("blpfsList", mcodeMap.get(NewGkElectiveConstant.MCODE_BLPFS));
		map.put("btksfpList", mcodeMap.get(NewGkElectiveConstant.MCODE_BTKSFP));
		map.put("ksfpList", mcodeMap.get(NewGkElectiveConstant.MCODE_KSFP));
		return "/newgkelective/xzb/courseFeatures.ftl";
	}

//	private List<NewGKCourseFeatureDto> findXzbCourseFeatures(String arrayItemId, Map<String,Object> map, NewGkDivide divide,
//			String unitId) {
//		List<Course> xzbCourseList= findByGradeId(unitId, divide.getGradeId());
//		map.put("xzbCourseList", xzbCourseList);
//
//		List<Course> gkCourseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
//		});
//		map.put("gkCourseList", gkCourseList);
//
//		// 年级基本设置--object_type=0 objectId=gradeId(不排课) objectId=subjectId(固定科目排课)
//		// 科目设置--object_type=9 objectId=subjectId LevelType=A/B/O
//		List<NewGkLessonTime> newGkLessonTimeList = newGkLessonTimeService.findByItemIdObjectIdAndGroupType(arrayItemId,
//				null, new String[] { NewGkElectiveConstant.LIMIT_GRADE_0, NewGkElectiveConstant.LIMIT_SUBJECT_9 },
//				null);
//		// 年级默认已设置时间点
//		Map<String, String> noClickTimeMap = new HashMap<>();
//		// 科目时间 key:subjectId-A,不排课，时间
//		Map<String, Map<String, List<String>>> subjectTimeExMap = new HashMap<>();
//		makeTimeEx(newGkLessonTimeList, noClickTimeMap, subjectTimeExMap);
//
//		map.put("noClickTimeMap", noClickTimeMap);
//
//
//		// 科目
//		List<NewGKCourseFeatureDto> dtoList = new ArrayList<>();
//		NewGKCourseFeatureDto dto;
//
//		List<NewGkSubjectTime> sujectTimeList = newGkSubjectTimeService.findByArrayItemId(arrayItemId);
//
//		Set<String> subjectIds = EntityUtils.getSet(sujectTimeList, NewGkSubjectTime::getSubjectId);
//		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[] {})), Course.class);
//		Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
//
//		// 连排科目
//		Map<String, Integer> subjectNoContinue = new HashMap<String, Integer>();
//		List<String[]> subjectNoContinueList = new ArrayList<>();
//		relateSubtimeList(arrayItemId, subjectNoContinue, subjectNoContinueList);
//		map.put("subjectNoContinueList", subjectNoContinueList);
//
//		for (NewGkSubjectTime item : sujectTimeList) {
//			dto = new NewGKCourseFeatureDto();
//
//			String courseName = courseMap.get(item.getSubjectId()).getSubjectName();
//			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(item.getSubjectType())
//					|| NewGkElectiveConstant.SUBJECT_TYPE_B.equals(item.getSubjectType())) {
//				courseName = courseName
//						+ (item.getSubjectType().equals(NewGkElectiveConstant.SUBJECT_TYPE_A) ? "选" : "学");
//			}
//			dto.setCourseName(courseName);
//			dto.setSubjectId(item.getSubjectId());
//			dto.setSubjectType(item.getSubjectType());
//			dto.setOrder(courseMap.get(item.getSubjectId()).getOrderId());
//
//
//			dto.setCourseWorkDay(item.getPeriod());
//			dto.setCourseCoupleType(item.getWeekRowType());
//			dto.setCourseCoupleTimes(item.getWeekRowNumber());
//			dto.setNeedRoom(item.getIsNeed());
//			dto.setPunchCard(item.getPunchCard());
//			// dto.setFirstsdWeek(item.getFirstsdWeek());
//			// 单双周科目
//			dto.setFirstsdWeekSubjectId(item.getFirstsdWeekSubject());
//			dto.setArrangeDay(item.getArrangeDay());
//			dto.setArrangeHalfDay(item.getArrangeHalfDay());
//			dto.setArrangePrior(item.getArrangeFrist());
//			// 不连排科目数量
//			if (subjectNoContinue.containsKey(item.getSubjectId() + "-" + item.getSubjectType())) {
//				dto.setNoContinueNum(subjectNoContinue.get(item.getSubjectId() + "-" + item.getSubjectType()));
//			} else {
//				dto.setNoContinueNum(0);
//			}
//
//			Map<String, List<String>> map1 = subjectTimeExMap
//					.get(item.getSubjectId() + "-" + item.getSubjectType());
//			if (MapUtils.isNotEmpty(map1)) {
//				// 禁止排时间以及数量
//				List<String> ll1 = map1.get(NewGkElectiveConstant.ARRANGE_TIME_TYPE_01);
//				if (CollectionUtils.isNotEmpty(ll1)) {
//					dto.setNoArrangeTimeNum(ll1.size());
//					dto.setNoArrangeTime(ArrayUtil.print(ll1.toArray(new String[] {})));
//				}
//				// 连排时间
//				List<String> ll2 = map1.get(NewGkElectiveConstant.ARRANGE_TIME_TYPE_03);
//				if (CollectionUtils.isNotEmpty(ll2)) {
//					dto.setCourseCoupleTypeTimes(ArrayUtil.print(ll2.toArray(new String[] {})));
//				}
//			}
//			dtoList.add(dto);
//		}
//		return dtoList;
//	}

//	private List<Course> findByGradeId(String unitId, String gradeId) {
//		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
//		if (grade == null) {
//			return new ArrayList<Course>();
//		}
//		// 只取必修课
//		String sectionStr="";
//		Integer section = grade.getSection();
//		if(section!=null) {
//			sectionStr=String.valueOf(section);
//		}
//		List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId,
//				BaseConstants.SUBJECT_TYPE_BX, sectionStr), new TR<List<Course>>() {
//				});
//		return courseList;
//	}
//
//	private void relateSubtimeList(String arrayItemId, Map<String, Integer> subjectNoContinue,
//			List<String[]> subjectNoContinueList) {
//		List<NewGkRelateSubtime> relateSubtimeList = newGkRelateSubtimeService.findListByItemId(arrayItemId);
//		if (CollectionUtils.isNotEmpty(relateSubtimeList)) {
//			for (NewGkRelateSubtime n : relateSubtimeList) {
//				String sIds = n.getSubjectIds();
//				String[] arg = sIds.split(",");
//				String type = n.getType();
//				subjectNoContinueList.add(new String[] { arg[0] + "_" + arg[1], type });
//				if (subjectNoContinue.containsKey(arg[0])) {
//					subjectNoContinue.put(arg[0], subjectNoContinue.get(arg[0]) + 1);
//				} else {
//					subjectNoContinue.put(arg[0], 1);
//				}
//				if (subjectNoContinue.containsKey(arg[1])) {
//					subjectNoContinue.put(arg[1], subjectNoContinue.get(arg[1]) + 1);
//				} else {
//					subjectNoContinue.put(arg[1], 1);
//				}
//			}
//		}
//	}

	public Map<String, List<String[]>> findMcode() {
		Map<String, List<String[]>> returnMap = RedisUtils.getObject("GK_MCODE",
				new TypeReference<Map<String, List<String[]>>>() {
				});
		if (MapUtils.isEmpty(returnMap) || CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_BLPFS))
				|| CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_BTKSFP))
				|| CollectionUtils.isEmpty(returnMap.get(NewGkElectiveConstant.MCODE_KSFP))) {
			returnMap = findMcodeDetail();
			RedisUtils.setObject("GK_MCODE", returnMap, RedisUtils.TIME_ONE_WEEK);
		}
		return returnMap;
	}

	public Map<String, List<String[]>> findMcodeDetail() {
		Map<String, List<String[]>> returnMap1 = new HashMap<>();
		returnMap1.put(NewGkElectiveConstant.MCODE_BLPFS, new ArrayList<>());
		returnMap1.put(NewGkElectiveConstant.MCODE_BTKSFP, new ArrayList<>());
		returnMap1.put(NewGkElectiveConstant.MCODE_KSFP, new ArrayList<>());
		List<McodeDetail> list = SUtils
				.dt(mcodeRemoteService.findAllByMcodeIds(new String[] { NewGkElectiveConstant.MCODE_BLPFS,
						NewGkElectiveConstant.MCODE_BTKSFP, NewGkElectiveConstant.MCODE_KSFP }), McodeDetail.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (McodeDetail m : list) {
				returnMap1.get(m.getMcodeId()).add(new String[] { m.getThisId(), m.getMcodeContent() });
			}
		}
		return returnMap1;
	}

//	private void makeTimeEx(List<NewGkLessonTime> newGkLessonTimeList, Map<String, String> noClickTimeMap,
//			Map<String, Map<String, List<String>>> subjectTimeExMap) {
//		// 对应时间不能排课时间
//		Set<String> ids = EntityUtils.getSet(newGkLessonTimeList, e -> e.getId());
//		List<NewGkLessonTimeEx> exList = newGkLessonTimeExService.findByObjectId(ids.toArray(new String[] {}), null);
//
//		if (CollectionUtils.isNotEmpty(exList)) {
//			Map<String, List<NewGkLessonTimeEx>> exMap = new HashMap<>();
//			for (NewGkLessonTimeEx ex : exList) {
//				List<NewGkLessonTimeEx> ll = exMap.get(ex.getScourceTypeId());
//				if (CollectionUtils.isEmpty(ll)) {
//					ll = new ArrayList<>();
//					exMap.put(ex.getScourceTypeId(), ll);
//				}
//				ll.add(ex);
//			}
//
//			for (NewGkLessonTime n : newGkLessonTimeList) {
//				if (NewGkElectiveConstant.LIMIT_GRADE_0.contains(n.getObjectType())) {
//					if (CollectionUtils.isNotEmpty(exMap.get(n.getId()))) {
//						for (NewGkLessonTimeEx ee : exMap.get(n.getId())) {
//							String key = ee.getDayOfWeek() + "_" + ee.getPeriodInterval() + "_" + ee.getPeriod();
//							noClickTimeMap.put(key, key);
//						}
//					}
//				} else {
//					if (CollectionUtils.isNotEmpty(exMap.get(n.getId()))) {
//						String key = n.getObjectId() + "-"
//								+ (StringUtils.isNotBlank(n.getLevelType()) ? n.getLevelType()
//										: NewGkElectiveConstant.SUBJECT_TYPE_O);
//						Map<String, List<String>> map2 = subjectTimeExMap.get(key);
//						if (MapUtils.isEmpty(map2)) {
//							map2 = new HashMap<>();
//							subjectTimeExMap.put(key, map2);
//						}
//						for (NewGkLessonTimeEx ee : exMap.get(n.getId())) {
//							List<String> l1 = map2.get(ee.getTimeType());
//							if (CollectionUtils.isEmpty(l1)) {
//								l1 = new ArrayList<>();
//								map2.put(ee.getTimeType(), l1);
//							}
//							String zz = ee.getDayOfWeek() + "_" + ee.getPeriodInterval() + "_" + ee.getPeriod();
//							l1.add(zz);
//						}
//					}
//				}
//			}
//		}
//	}

	@RequestMapping("/arrayResult/pageIndex")
	public String toArrayResultIndex(String arrayId, String type, ModelMap map){
		NewGkArray array = newGkArrayService.findById(arrayId);
		if(array==null){
			return errorFtl(map, "该排课数据已不存在");
		}
		NewGkDivide divide = newGkDivideService.findById(array.getDivideId());
		if(divide==null){
			return errorFtl(map, "该排课对应分班数据已不存在");
		}
		String gradeId = array.getGradeId();
		map.put("arrayId", arrayId);
		map.put("gradeId", gradeId);
		map.put("type", type);
		map.put("openType", divide.getOpenType());
		return "/newgkelective/xzb/arrayResultIndex.ftl";
	}

	@RequestMapping("{divideId}/divideResult/index")
	public String divideResultIndex(@PathVariable String divideId, String arrayId, ModelMap map) {
		map.put("divideId", divideId);
		map.put("arrayId", arrayId);
		if(StringUtils.isNotBlank(arrayId)) {
			map.put("fromArray", "1");
		}
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		map.put("gradeId", divide.getGradeId());
		return "/newgkelective/xzb/divideResultIndex.ftl";
	}
	@RequestMapping("{divideId}/divideResult/page")
	public String divideResultPage(@PathVariable String divideId, String arrayId, String fromSolve, String gradeId, String type, ModelMap map) {
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(type == null)
			type = "Y";
		map.put("type", type);
		if("Y".equals(type)) {
			newGkDivideService.showDivideXzbList(divide, fromSolve, arrayId, map);
			map.put("noOrder", true);
			return "/newgkelective/divide/divideXzbResultList.ftl";
		}else if("A".equals(type)) {
			//页面显示批次数量
			newGkDivideService.divideXzbVirtualCount(divide, fromSolve, arrayId, map, type);
			return "/newgkelective/xzb/listXuniJxbResult.ftl";
		}else {
			return errorFtl(map, "类型错误");
		}
	}

	@RequestMapping("{divideId}/showClassDetail/page")
	public String showClassDetail(@PathVariable String divideId, String classId, String type, ModelMap map) {

		String unitId =this.getLoginInfo().getUnitId();
		NewGkDivideClass divideClass = newGkDivideClassService.findById(unitId, classId, true);
		List<String> stuIdList = divideClass.getStudentList();

		List<Student> studentList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(stuIdList)) {
			studentList = SUtils.dt(studentRemoteService.findPartStudentById(stuIdList.toArray(new String[] {})), Student.class);
		}

		String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
		Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId),
				new TypeReference<Map<String, McodeDetail>>() {});

		Set<String> classIds = EntityUtils.getSet(studentList,Student::getClassId);
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[] {})), Clazz.class);
		Map<String, Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);

		List<NewGkDivideClass> divideClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId,
				divideId,
				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		Map<String, List<String>> stuAJxbMap = new HashMap<>();
		Map<String, List<String>> stuBJxbMap = new HashMap<>();

		Map<String, String> stuXzbMap = new HashMap<>();
		for (NewGkDivideClass divideClass2 : divideClassList) {
			if(NewGkElectiveConstant.CLASS_TYPE_1.equals(divideClass2.getClassType())) {
				// 行政班
				for (String stuId : divideClass2.getStudentList()) {
					stuXzbMap.put(stuId, divideClass2.getClassName());
				}
			}else {
				for (String stuId : divideClass2.getStudentList()) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(divideClass2.getSubjectType())) {
						if(!stuAJxbMap.containsKey(stuId)) {
							stuAJxbMap.put(stuId, new ArrayList<>());
						}
						stuAJxbMap.get(stuId).add(divideClass2.getClassName());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(divideClass2.getSubjectType())) {
						if(!stuBJxbMap.containsKey(stuId)) {
							stuBJxbMap.put(stuId, new ArrayList<>());
						}
						stuBJxbMap.get(stuId).add(divideClass2.getClassName());
					}else {

						//其他
						//throw new RuntimeException("error occur in showDivideClassDetail,班级科目类型除了A/B 不应该有其他类型。");
//
//						if(!otherJxbMap.containsKey(stuId)) {
//							otherJxbMap.put(stuId, new ArrayList<>());
//						}
//						otherJxbMap.get(stuId).add(divideClass2.getClassName());
					}
				}
			}

		}

		// 结果
		List<StudentResultDto> dtoList = new ArrayList<>();
		StudentResultDto dto;
		for (Student student : studentList) {
			dto = new StudentResultDto();

			dto.setStudentName(student.getStudentName());
			dto.setStudentCode(student.getStudentCode());
			dto.setSex(codeMap.get(student.getSex()+"").getMcodeContent());
			if(classMap.get(student.getClassId()) == null) {
				dto.setOldClassName("未找到");
			}else {
				dto.setOldClassName(classMap.get(student.getClassId()).getClassName());
			}

			dto.setClassName(stuXzbMap.get(student.getId()));
			dto.setJxbAClasss(stuAJxbMap.get(student.getId()));
			dto.setJxbBClasss(stuBJxbMap.get(student.getId()));
//			dto.getJxbBClasss().addAll(otherJxbMap.get(student.getId()));
			dtoList.add(dto);
		}

		List<NewGkDivideClass> xzbList = divideClassList.stream()
				.filter(e->NewGkElectiveConstant.CLASS_TYPE_1.equals(e.getClassType()))
				.sorted((x,y)->{
					if(x.getOrderId()==null){
						return 1;
					}else if(y.getOrderId()==null){
						return -1;
					}else if(x.getOrderId().compareTo(y.getOrderId()) != 0){
						return x.getOrderId().compareTo(y.getOrderId());
					}
					return x.getClassName().compareTo(y.getClassName());
				})
				.collect(Collectors.toList());
		map.put("xzbList", xzbList);

		map.put("dtoList", dtoList);
		map.put("divideId", divideId);
		map.put("divideClass", divideClass);
		map.put("type", type);


		return "/newgkelective/xzb/classStuDetail.ftl";
	}

}



