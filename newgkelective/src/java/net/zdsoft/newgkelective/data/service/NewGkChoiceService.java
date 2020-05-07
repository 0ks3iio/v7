package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;

public interface NewGkChoiceService extends BaseService<NewGkChoice, String>{

	/**
	 * 根据年级id查询选课
	 * @param gradeId
	 * @return
	 */
	List<NewGkChoice> findListByGradeId(String gradeId);
	
	List<NewGkChoice> findListByGradeIdWithMaster(String gradeId);
	
	/**
	 * 查询默认
	 * @param gradeId
	 * @return
	 */
	NewGkChoice findDefaultByGradeId(String gradeId);
	
	/**
	 * 根据年级id查询最近一次选课选课
	 * @param gradeId
	 * @return
	 */
	List<NewGkChoice> findByNowGradeId(String gradeId);

	/*
	 * 查询当前年级最大的选课次数
	 */
	Integer getChoiceMaxTime(String unitId, String gradeId);

	String saveNewGkChoice(NewGkChoice newGkChoice);

	void saveAndDeleteNewGkChoice(NewGkChoice newGkChoice, String choiceId);

	void deleteById(String unitId, String id);

	NewGkChoice findById(String id);
	
	/**
	 * 获取最终选课结果
	 * @param courseList
	 * @param dtoMap
	 * @param result
	 * @param num
	 * @return
	 */
	List<NewGkConditionDto> findSubRes(List<Course> courseList, Map<String, NewGkChoResultDto> dtoMap, Integer[][] result,int num);
	
	/**
	 * 组装数据  用于页面显示
	 * @param gradeId
	 * @param newGkChoiceList
	 * @param classIdList 
	 * @return
	 */
	public List<NewGkChoiceDto> makeChoiceChart(String gradeId,List<NewGkChoice> newGkChoiceList, List<String> classIdList);

    /**
     * 置为默认，并将选课结果写入base_student_selsub
     * @param chs
     */
    void saveDefault(NewGkChoice[] chs);

    // Basedata Sync Method
    void deleteByGradeIds(String... gradeIds);


    void saveAndDeleteNewGkChoice(NewGkChoice newGkChoice, List<NewGkChoRelation> newGkChoRelations, List<NewGkChoCategory> newGkChoCategoryList, String[] oldChoCategoryIds, String choiceId, String unitId);

	void saveNewGkChoice(NewGkChoice newGkChoice, List<NewGkChoRelation> newGkChoRelations, List<NewGkChoCategory> newGkChoCategoryList, String[] oldChoCategoryIds, String unitId);
	
	
	public String checkSubjectIds(String choiceId,String subjectIds);
		
	
}
