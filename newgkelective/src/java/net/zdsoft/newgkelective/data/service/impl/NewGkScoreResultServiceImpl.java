package net.zdsoft.newgkelective.data.service.impl;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.newgkelective.data.dao.NewGkScoreResultDao;
import net.zdsoft.newgkelective.data.dao.NewGkScoreResultJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;

@Service("newGkScoreResultService")
public class NewGkScoreResultServiceImpl extends BaseServiceImpl<NewGkScoreResult, String> implements NewGkScoreResultService{

	@Autowired
	private NewGkScoreResultDao newGkScoreResultDao;
	@Autowired
	private NewGkScoreResultJdbcDao newGkScoreResultJdbcDao;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkScoreResult, String> getJpaDao() {
		return newGkScoreResultDao;
	}
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Override
	protected Class<NewGkScoreResult> getEntityClass() {
		return NewGkScoreResult.class;
	}

	@Override
	public String doImport(String unitId, String gradeId, String[] realTitles,List<String[]> datas,String filePath,boolean isNoTitle) {
       
		//错误数据序列号
        String refNameData = datas.get(0)[0].trim();
        datas.remove(0);
        datas.remove(0);
		int totalSize =datas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
		if(StringUtils.isBlank(refNameData)){
			String[] errorData=new String[4];
        	errorData[0]=String.valueOf(1);
        	errorData[1]="参考成绩名称";
        	errorData[2]="";
        	errorData[3]="参考成绩名称不能为空";
        	errorDataList.add(errorData);
        	return result(totalSize,0,totalSize,errorDataList,"");
		}
        if(CollectionUtils.isEmpty(datas)){
        	String[] errorData=new String[4];
        	errorData[0]=String.valueOf(1);
        	errorData[1]="导入数据";
        	errorData[2]="";
        	errorData[3]="没有导入数据";
        	errorDataList.add(errorData);
        	return result(totalSize,0,totalSize,errorDataList,"");
        }

        Map<String,String> coursesMap = new LinkedHashMap<String,String>();
        List<Course> coursesList =SUtils.dt(courseRemoteService.findByCodes73YSY(unitId),new TR<List<Course>>() {});
        for (Course course : coursesList) {
        	coursesMap.put(course.getSubjectName(), course.getId());
        }
        
        Pattern pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
		String gradeName = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeName();
		
		Set<String> referNameSet = new HashSet<String>();
		referNameSet.add(refNameData);
		Set<String> stuCodeSet = new HashSet<String>();
		for(String[] arr : datas){
			if(StringUtils.isNotBlank(arr[0])){
				stuCodeSet.add(arr[0]);
			}
        }
		//原来成绩referId
		Set<String> referScoreIdSet = new HashSet<String>();
		Map<String, String> referIdByNameMap = new HashMap<String, String>();
		Map<String, String> referNameByIdMap = new HashMap<String, String>();
		int maxIndex=0;
		if(CollectionUtils.isNotEmpty(referNameSet)){
			//因为名称是唯一的 这里直接用map
			//该年级导入成绩次数最大值
			List<NewGkReferScore> newGkReferScoreList =newGkReferScoreService.findListByGradeId(unitId, gradeId,false,false);
//			List<NewGkReferScore> newGkReferScoreList = newGkReferScoreDao.findByNames(unitId, gradeId, referNameSet.toArray(new String[0]));
			for(NewGkReferScore item : newGkReferScoreList){
				referScoreIdSet.add(item.getId());
				referNameByIdMap.put(item.getId(), item.getName());
				referIdByNameMap.put(item.getName(), item.getId());
				if(maxIndex<item.getTimes()){
					maxIndex=item.getTimes();
				}
			}
		}
		Set<String> clsIdSet= new HashSet<String>();
		//学生
		List<Student> stuList = new ArrayList<Student>();
		Map<String, Student> stuByIdMap = new HashMap<String, Student>();
		//考虑学号重复问题
		Map<String, Set<String>> stuIdsByCodeMap = new HashMap<String, Set<String>>();
		
		if(CollectionUtils.isNotEmpty(stuCodeSet)){
			stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
		    for(Student stu : stuList){
		    	stuByIdMap.put(stu.getId(), stu);
		    	if(!stuIdsByCodeMap.containsKey(stu.getStudentCode())){
		    		stuIdsByCodeMap.put(stu.getStudentCode(), new HashSet<String>());
		    	}
		    	stuIdsByCodeMap.get(stu.getStudentCode()).add(stu.getId());
		    	clsIdSet.add(stu.getClassId());
		    }
		}
		
		List<Clazz> clsList = new ArrayList<Clazz>();
		Map<String, String> gradeIdByclassIdMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(clsIdSet)){
			clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
			for(Clazz cls : clsList){
				gradeIdByclassIdMap.put(cls.getId(), cls.getGradeId());
			}
		}
		
		//暂时不考虑并发
		//key的顺序 referScoreId,studentId,subjectId
		Map<String,Map<String,Map<String,NewGkScoreResult>>> oldScoreMap=new HashMap<String,Map<String,Map<String,NewGkScoreResult>>>();
		Map<String, Map<String, NewGkScoreResult>> stuScoreSMap;
		Map<String, NewGkScoreResult> oneStuScore;
		//获取表里已有数据
		List<NewGkScoreResult> newGkScoreResultList = new ArrayList<NewGkScoreResult>();
		if(CollectionUtils.isNotEmpty(referScoreIdSet)){
			newGkScoreResultList = newGkScoreResultDao.findByReferScoreIds(unitId,referScoreIdSet.toArray(new String[0]));
			for(NewGkScoreResult item : newGkScoreResultList){
				stuScoreSMap = oldScoreMap.get(item.getReferScoreId());
				if(stuScoreSMap==null){
					stuScoreSMap=new HashMap<String, Map<String, NewGkScoreResult>>();
				}
				oldScoreMap.put(item.getReferScoreId(), stuScoreSMap);
				oneStuScore = stuScoreSMap.get(item.getStudentId());
				if(oneStuScore==null){
					oneStuScore=new HashMap<String, NewGkScoreResult>();
				}
				stuScoreSMap.put(item.getStudentId(), oneStuScore);
				oneStuScore.put(item.getSubjectId(), item);
			}
		}
		//根据模板头部realTitles
		Map<Integer,String> subjectIdByIndex=new HashMap<Integer,String>();
		for(int i=2;i<realTitles.length;i++){
			subjectIdByIndex.put(i, coursesMap.get(realTitles[i]));
		}
		int tileLength=realTitles.length;

        int successCount=0;
        Set<String> arrangeStuId=new HashSet<String>();
        //包括修改
        List<NewGkScoreResult> insertList=new ArrayList<NewGkScoreResult>();
        List<NewGkReferScore> insertRefList=new ArrayList<NewGkReferScore>();
        maxIndex++;
        int t = 0;
        for (String[] arr:datas) {
            t++;
        	String[] errorData=new String[4];
            String stuCodeData=arr[0]==null?null:StringUtils.trim(arr[0]);
            String stuNameData=arr[1]==null?null:StringUtils.trim(arr[1]);
            
            if(StringUtils.isBlank(stuCodeData)){
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=t+"";
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(stuNameData)){
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=t+"";
                errorData[1]="姓名";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            //判断学生是否存在
           Set<String> stuIds = stuIdsByCodeMap.get(stuCodeData);
           Student student=null;
           if(CollectionUtils.isEmpty(stuIds)){
               // errorData[0]=String.valueOf(sequence);
               errorData[0]=t+"";
               errorData[1]="学号";
               errorData[2]=stuCodeData;
               errorData[3]="学号不存在";
               errorDataList.add(errorData);
               continue;
           }else{
        	   List<Student> sameCodeAndName=new ArrayList<Student>();
        	   for(String s:stuIds){
        		   if(stuByIdMap.get(s).getStudentName().equals(stuNameData)){
        			   sameCodeAndName.add(stuByIdMap.get(s));
        		   }
        	   }
        	   if(CollectionUtils.isEmpty(sameCodeAndName)){
                   // errorData[0]=String.valueOf(sequence);
                   errorData[0]=t+"";
                   errorData[1]="姓名";
                   errorData[2]=stuNameData;
                   errorData[3]="学号所对应的学生姓名错误";
                   errorDataList.add(errorData);
                   continue;
        	   }
        	   //判断是不是该年级下的
        	   List<Student> sameCodeAndNameInGrade=new ArrayList<Student>();
        	   for(Student st:sameCodeAndName){
        		   if(gradeId.equals(gradeIdByclassIdMap.get(st.getClassId()))){
        			   sameCodeAndNameInGrade.add(st);
        		   }
        	   }
        	   if(CollectionUtils.isEmpty(sameCodeAndNameInGrade)){
                   // errorData[0]=String.valueOf(sequence);
                   errorData[0]=t+"";
                   errorData[1]="姓名";
                   errorData[2]=stuNameData;
                   errorData[3]="该学生不属于"+gradeName+"年级";
                   errorDataList.add(errorData);
                   continue;
        	   }
        	   if(sameCodeAndNameInGrade.size()>1){
                   // errorData[0]=String.valueOf(sequence);
                   errorData[0]=t+"";
                   errorData[1]="姓名";
                   errorData[2]=stuNameData;
                   errorData[3]=gradeName+"年级下存在多个匹配学号姓名的学生";
                   errorDataList.add(errorData);
                   continue;
        	   }
        	   student=sameCodeAndNameInGrade.get(0);
           }
           if(student==null){
            	continue;
           }
           //验证成绩  一行完全正确才能保存到数据库
           //有录入成绩 则保存以及覆盖 没有成绩不进入数据库 但不代表数据错误
           boolean flag=true;
           Map<String,Float> scoreMap=new HashMap<String,Float>();
           for(int i=2;i<arr.length;i++){
        	   if(i>tileLength){
        		   //超出头部
        		   break;
        	   }
        	   String scoreStr = arr[i]==null?null:StringUtils.trim(arr[i]);
        	   if(StringUtils.isBlank(scoreStr)){
        		   continue;
        	   }
        	   //判断输入成绩是否正确
        	   if (!pattern.matcher(scoreStr).matches()){
					errorData = new String[4];
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
					errorData[1]=realTitles[i];
					errorData[2]=scoreStr;
					errorData[3]="分数："+arr[5]+"格式不正确(最多3位整数，2位小数)!";
					errorDataList.add(errorData);
					flag=false;
					break;
				}
        	    //数据
        	   scoreMap.put(subjectIdByIndex.get(i), Float.parseFloat(scoreStr));
        	   
           }
           if(!flag){
        	   continue; 
           }
           //验证成功
           if(arrangeStuId.contains(student.getId())){
               // errorData[0]=String.valueOf(sequence);
               errorData[0]=t+"";
               errorData[1]="姓名";
               errorData[2]=stuNameData;
               errorData[3]="之前已有该学生成绩，重复录入";
               errorDataList.add(errorData);
               continue;
           }
           String referId = referIdByNameMap.get(refNameData);
           Map<String, NewGkScoreResult> oldStuScore=new HashMap<String, NewGkScoreResult>();
           if(StringUtils.isNotBlank(referId)){
        	   if(oldScoreMap.containsKey(referId)){
        		   if(oldScoreMap.get(referId).containsKey(student.getId())){
        			  oldStuScore = oldScoreMap.get(referId).get(student.getId());
        		   }
        	   }
           }else{
        	   //新增
        	   NewGkReferScore refIn = initGkReferScore(gradeId, unitId, refNameData, maxIndex);
        	   insertRefList.add(refIn);
        	   referId=refIn.getId();
        	   referIdByNameMap.put(refNameData, referId);
        	   referNameByIdMap.put(referId, refNameData);
        	   maxIndex++;
           }
           
           //学生科目成绩
           for(Entry<String, Float> item:scoreMap.entrySet()){
        	   NewGkScoreResult result1 = oldStuScore.get(item.getKey());
        	   if(result1!=null){
        		   result1.setModifyTime(new Date());
        		   result1.setScore(item.getValue());
        		   insertList.add(result1);
        	   }else{
        		   result1=new NewGkScoreResult();
        		   result1.setCreationTime(new Date());
        		   result1.setModifyTime(new Date());
        		   result1.setScore(item.getValue());
        		   result1.setId(UuidUtils.generateUuid());
        		   result1.setReferScoreId(referId);
        		   result1.setStudentId(student.getId());
        		   result1.setSubjectId(item.getKey());
        		   result1.setUnitId(unitId);
        		   insertList.add(result1);
        	   }
           }
           arrangeStuId.add(student.getId());
           successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();

            //标题行固定
            sheet.createFreezePane(0, 3);
            // List<String> titleList = Arrays.asList(realTitles);
            List<String> titleList = new ArrayList<>();
            titleList.addAll(Arrays.asList(realTitles));
            titleList.add("错误数据");
            titleList.add("错误原因");

            // 注意事项样式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont headfont = workbook.createFont();
            headfont.setFontHeightInPoints((short) 9);// 字体大小
            headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
            style.setFont(headfont);
            style.setAlignment(HorizontalAlignment.LEFT);//水平居左
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            style.setWrapText(true);//自动换行
            
            // 标题样式
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            titleStyle.setWrapText(true);//自动换行
            
            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            CellRangeAddress RemarkCar = new CellRangeAddress(0,0,0,titleList.size()-1);
            sheet.addMergedRegion(RemarkCar);
            HSSFRow remarkRow = sheet.createRow(0);
            //高度：4倍默认高度
            remarkRow.setHeightInPoints(4*sheet.getDefaultRowHeightInPoints());
            HSSFCell remarkCell = remarkRow.createCell(0);
            remarkCell.setCellValue(new HSSFRichTextString("修改注意：\n"+"1.请勿改动标题或移除此行;"));
            remarkCell.setCellStyle(style);

            CellRangeAddress titleCar = new CellRangeAddress(1,1,0,titleList.size()-1);
            sheet.addMergedRegion(titleCar);
            HSSFRow titleRow = sheet.createRow(1);
            HSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(new HSSFRichTextString(refNameData));
            titleCell.setCellStyle(titleStyle);

            HSSFRow rowTitle = sheet.createRow(2);
            for(int j=0;j<titleList.size();j++){
            	sheet.setColumnWidth(j, 10 * 256);//列宽
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+3);
                String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
                for (int k=0;k<titleList.size();k++) {
                    HSSFCell cell = row.createCell(k);
                    if (k<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[k]));
                    } else if (k==titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[1]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }

        String result ="";
        try{
        	if (CollectionUtils.isNotEmpty(insertRefList)) {
				newGkReferScoreService.saveAll(insertRefList.toArray(new NewGkReferScore[]{}) );
			}
			if (CollectionUtils.isNotEmpty(insertList)) {
				this.saveAll(insertList.toArray(new NewGkScoreResult[]{}));
			}
			int errorCount = totalSize-successCount;
	        result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
        }catch (Exception e){
            e.printStackTrace();
            int errorCount = totalSize ;
            result = result(totalSize,0,errorCount,errorDataList,errorExcelPath);
        }
        
		return result;
	}
	
	private NewGkReferScore initGkReferScore(String gradeId,String unitId,String name,int i){
		NewGkReferScore ref = new NewGkReferScore();
		ref.setId(UuidUtils.generateUuid());
		ref.setGradeId(gradeId);
		ref.setUnitId(unitId);
		ref.setName(name);
		ref.setCreationTime(new Date());
		ref.setModifyTime(new Date());
		ref.setIsDeleted(0);
		ref.setIsDefault(0);
		ref.setTimes(i);
		return ref;
	}

	@Override
	public List<NewGkScoreResult> findByReferScoreIds(String unitId, String[] referScoreIds) {
		List<NewGkScoreResult> resultList=new ArrayList<NewGkScoreResult>();
		if(referScoreIds!=null && referScoreIds.length>0){
			if(referScoreIds.length<=1000){
				resultList=newGkScoreResultDao.findByReferScoreIds(unitId, referScoreIds);
			}else{
				int cyc = referScoreIds.length / 1000 + (referScoreIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > referScoreIds.length)
						max = referScoreIds.length;
					String[] referScoreId = ArrayUtils.subarray(referScoreIds, i * 1000, max);
					List<NewGkScoreResult> list = newGkScoreResultDao.findByReferScoreIds(unitId, referScoreId);
					if(CollectionUtils.isNotEmpty(list)){
						resultList.addAll(list);
					}
				}
			}
			
		}
		return resultList;
	}

	@Override
	public List<NewGkScoreResult> findListByReferScoreId(String unitId, String referScoreId, boolean byRedis) {
		if(byRedis) {
			String object = RedisUtils.getObject("STUDENT_SCORE_"+referScoreId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<String>(){}, new RedisInterface<String>(){
				@Override
				public String queryData() {
					return SUtils.s(newGkScoreResultDao.findByUnitIdAndReferScoreId(unitId, referScoreId));
				}
	        });
			return SUtils.dt(object, NewGkScoreResult.class);
		}
		return newGkScoreResultDao.findByUnitIdAndReferScoreId(unitId, referScoreId);
	}
	@Override
	public List<NewGkScoreResult> findListByReferScoreId(String unitId, String referScoreId) {
		return newGkScoreResultDao.findByUnitIdAndReferScoreId(unitId, referScoreId);
	}

	@Override
	public Map<String,Map<String, Float>> findMapByReferScoreId(String unitId, String referScoreId) {
		List<NewGkScoreResult> list = findListByReferScoreId(unitId,referScoreId,true);
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, Map<String, Float>>();
		}
		Map<String,Map<String, Float>> map = new HashMap<String, Map<String, Float>>();
		for(NewGkScoreResult score:list){
			if(!map.containsKey(score.getStudentId())){
				map.put(score.getStudentId(), new HashMap<String, Float>());
			}
			map.get(score.getStudentId()).put(score.getSubjectId(), score.getScore());
		}
		return map;
	}

	@Override
	public List<NewGkScoreResult> findByReferScoreIdAndSubjectId(
			String unitId, String referScoreId, String subjectId) {
		return newGkScoreResultDao.findByUnitIdAndReferScoreIdAndSubjectId(unitId,referScoreId,subjectId);
	}
	
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	 }

	@Override
	public List<NewGkScoreResult> findByReferScoreIdAndStudentIdIn(String unitId, String referScoreId, String[] studentIds) {
		List<NewGkScoreResult> resultList=new ArrayList<NewGkScoreResult>();
		if(studentIds!=null && studentIds.length>0){
			if(studentIds.length<=1000){
				resultList=newGkScoreResultDao.findByUnitIdAndReferScoreIdAndStudentIdIn(unitId, referScoreId, studentIds);
			}else{
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
					List<NewGkScoreResult> list = newGkScoreResultDao.findByUnitIdAndReferScoreIdAndStudentIdIn(unitId, referScoreId, stuId);
					if(CollectionUtils.isNotEmpty(list)){
						resultList.addAll(list);
					}
				}
			}
			
		}

		return resultList;
	}

	@Override
	public Map<String, Integer> findCountByReferId(String unitId, String[] referScoreIds) {
		List<Object[]> list=new ArrayList<Object[]>();
//		int maxLength=300;
//		if(referScoreIds!=null && referScoreIds.length>0){
//			if(referScoreIds.length<=maxLength){
////				list = newGkScoreResultDao.findCountByReferId(referScoreIds);
//				list =newGkScoreResultJdbcDao.findCountByReferId(referScoreIds);
//			}else{
//				int cyc = referScoreIds.length / maxLength + (referScoreIds.length % maxLength == 0 ? 0 : 1);
//				for (int i = 0; i < cyc; i++) {
//					int max = (i + 1) * maxLength;
//					if (max > referScoreIds.length)
//						max = referScoreIds.length;
//					String[] arrId = ArrayUtils.subarray(referScoreIds, i * maxLength, max);
////					List<Object[]> list1 = newGkScoreResultDao.findCountByReferId(arrId);
//					List<Object[]> list1 =newGkScoreResultJdbcDao.findCountByReferId(arrId);
//					if(CollectionUtils.isNotEmpty(list1)){
//						list.addAll(list1);
//					}
//				}
//			}
//			
//		}
		list =newGkScoreResultJdbcDao.findCountByReferId(unitId, referScoreIds);
		Map<String,Integer> coutMap=new HashMap<String,Integer>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] item:list){
				Integer sum = Integer.valueOf("" + item[0]);
				String referId=item[1].toString();
				coutMap.put(referId, sum);
			}
		}
		return coutMap;
	}

	@Override
	public Map<String, Map<String,Float>> findCountSubjectByReferId(String unitId, String[] referScoreIds) {
		List<Object[]> list=new ArrayList<Object[]>();
		if(referScoreIds!=null && referScoreIds.length>0){
//			if(referScoreIds.length<=1000){
//				list = newGkScoreResultDao.findCountSubjectByReferId(unitId, referScoreIds);
//			}else{
//				int cyc = referScoreIds.length / 1000 + (referScoreIds.length % 1000 == 0 ? 0 : 1);
//				for (int i = 0; i < cyc; i++) {
//					int max = (i + 1) * 1000;
//					if (max > referScoreIds.length)
//						max = referScoreIds.length;
//					String[] arrId = ArrayUtils.subarray(referScoreIds, i * 1000, max);
//					List<Object[]> list1 = newGkScoreResultDao.findCountSubjectByReferId(unitId, arrId);
//					if(CollectionUtils.isNotEmpty(list1)){
//						list.addAll(list1);
//					}
//				}
//			}
			list =newGkScoreResultJdbcDao.findCountSubjectByReferId(unitId, referScoreIds);
		}
		Map<String,Map<String,Float>> avgMap=new HashMap<String,Map<String,Float>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] item:list){
				Integer stuNum = Integer.valueOf("" + item[0]);
				Double sum = Double.valueOf("" + item[1]);
				String referId=item[2].toString();
				String subjectId=item[3].toString();
				Float avgff=0.0f;
				if(stuNum!=null && stuNum>0 && sum!=null && sum>0){
					avgff=(float) (sum/stuNum);
				}
				if(!avgMap.containsKey(referId)){
					avgMap.put(referId, new HashMap<String,Float>());
				}
				avgMap.get(referId).put(subjectId, avgff);
			}
		}
		return avgMap;
	}

    @Override
    public void deleteByStudentIds(String... stuids) {
        newGkScoreResultDao.deleteByStudentIdIn(stuids);
    }

    @Override
    public void deleteBySubjectIds(String... subids) {
        newGkScoreResultDao.deleteBySubjectIdIn(subids);
    }

    public String saveErrorExcel(String filePath, HSSFWorkbook workbook) {
        if (Validators.isEmpty(filePath)) {
            return "";
        }
        filePath = filePath.substring(0, filePath.lastIndexOf(".")) + "-errorMessage-" + System.currentTimeMillis() + filePath.substring(filePath.lastIndexOf("."));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream); // 输出文件
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(fileOutputStream);
        }
        return filePath;
    }

	@Override
	public void deleteByReferScoreId(String referScoreId) {
		newGkScoreResultDao.deleteByReferScoreId(referScoreId);
	}
}
