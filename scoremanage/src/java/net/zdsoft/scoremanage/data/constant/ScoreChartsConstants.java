package net.zdsoft.scoremanage.data.constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import net.zdsoft.scoremanage.data.dto.ChartsDetailDto;
import net.zdsoft.scoremanage.data.dto.ChartsDetailParDto;

public class ScoreChartsConstants {
	
	public static final Map<String,ChartsDetailDto> SCORE_CHARTS_ALL_MAP = new LinkedHashMap<String,ChartsDetailDto>();
	public static final Map<String,String> SCORE_CHARTS_73_MAP = new LinkedHashMap<String,String>();
	public static final Map<String,String> SCORE_CHARTS_KS_MAP = new LinkedHashMap<String,String>();
	public static final Map<String,String> SCORE_CHARTS_RC_MAP = new LinkedHashMap<String,String>();
    static{
    	ChartsDetailDto cddto = new ChartsDetailDto();
    	Map<String,ChartsDetailParDto> parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setName("7选3年级内排名成绩报表");
    	cddto.setDocumentLabel("score7choose3Grade");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	ChartsDetailParDto cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_73_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("score7choose3All");
    	cddto.setName("7选3统考排名成绩报表");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_73_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreStudentTotal");
    	cddto.setName("学生考试成绩总分报表");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreClassExamRatio");
    	cddto.setName("班级各个科目的成绩比例表格");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(1);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreGradeAverage");
    	cddto.setName("考试年级下各班级平均分表格");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreCourseSingle");
    	cddto.setName("年级班级单科考试成绩表格");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("年级");
    	cdpdto.setParCode("grade_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreClassScatter");
    	cddto.setName("班级单科和总分分布散点图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreClassCourse");
    	cddto.setName("班级科目成绩分布柱状图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreStudentRadar");
    	cddto.setName("学生成绩雷达图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(1);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("学生");
    	cdpdto.setParCode("student_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreStudentRank");
    	cddto.setName("学生历次考试总分排名折线图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(1);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("学生");
    	cdpdto.setParCode("student_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_RC_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreStudentTrend");
    	cddto.setName("学生纵向成绩折线图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreCharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(1);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("学生");
    	cdpdto.setParCode("student_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(1);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_RC_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("scoreTeacherCourse");
    	cddto.setName("教师历次考试教学成绩效果折线图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreChartsForTea.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(1);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("部门");
    	cdpdto.setParCode("dept_id");
    	cdpdto.setIsAssistPar(1);
    	cdpdto.setIsMust(0);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("教师");
    	cdpdto.setParCode("teacher_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_RC_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    	cddto = new ChartsDetailDto();
    	parMap = new HashMap<String,ChartsDetailParDto>();
    	cddto.setDocumentLabel("echartsSubjectScoreDistribute");
    	cddto.setName("班级学科成绩分布散点图");
    	cddto.setUrlFtl("/scoremanage/scoreStatistic/scoreECharts.ftl");
    	cddto.setParMap(parMap);
    	cddto.setChartType(2);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("考试");
    	cdpdto.setParCode("exam_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("班级");
    	cdpdto.setParCode("class_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	cdpdto = new ChartsDetailParDto();
    	cdpdto.setParName("科目");
    	cdpdto.setParCode("subject_id");
    	cdpdto.setIsAssistPar(0);
    	cdpdto.setIsMust(1);
    	cdpdto.setIsMultiple(0);
    	parMap.put(cdpdto.getParCode(), cdpdto);
    	SCORE_CHARTS_ALL_MAP.put(cddto.getDocumentLabel(), cddto);
    	SCORE_CHARTS_KS_MAP.put(cddto.getDocumentLabel(), JSON.toJSONString(cddto));
    	
    }
    
}