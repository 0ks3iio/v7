package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.*;
import org.springframework.ui.ModelMap;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.NewGkDivideDto;

public interface NewGkDivideService extends BaseService<NewGkDivide, String>{
    
	/**
	 * 根据年级id,查询正常的分班结果
	 * @param unitId
	 * @param gradeId
	 * @param stat 状态 可为null
	 * @return 智能分班（openType!=07）如果查行政班预排 findListByOpenTypeAndGradeIdIn
	 */
	List<NewGkDivide> findByGradeId(String unitId,String gradeId,String stat);
	
	List<NewGkDivide> findByGradeIdWithMaster(String unitId,String gradeId,String stat);

	/**
	 * 软删
	 * @param unitId TODO
	 * @param divideId
	 */
	void deleteDivide(String unitId, String divideId);

	void saveDivide(NewGkDivide newGkDivide, List<NewGkOpenSubject> openSubjectList,
			boolean isAdd) throws Exception;
	/**
	 * 用于命名
	 * @param unitId
	 * @param gradeId
	 * @param isXzb 是否行政班排课 true：行政班排课 false:7选3
	 * @return
	 */
	int findMaxByGradeId(String unitId, String gradeId,boolean isXzb);

	void updateStat(String divideId,String stat);

	List<NewGkDivide> findByChoiceId(String choiceId);

	List<Course> findCourseByDivideId(String divide_id);

	NewGkDivide findById(String divideId);
	/**
	 * 组装页面展现数据
	 * @param divideList
	 * @return
	 */
	public List<NewGkDivideDto> makeDivideItem(List<NewGkDivide> divideList);
	
	public void saveCopyDivide(NewGkDivide divide);
	
	public void saveCombineDivide(NewGkDivide divide1,NewGkDivide divide2,NewGkChoice choice);

	public void saveCombineDivideList(List<NewGkDivide> divideList,NewGkChoice choice);

	List<NewGkDivide> findListByOpenTypeAndGradeIdIn(String unitId, String openType, String[] gradeIds);
	List<NewGkDivide> findListByOpenTypeAndGradeIdInWithMaster(String unitId, String openType, String[] gradeIds);

    // Basedata Sync Method
    void deleteByGradeIds(String... gradeIds);
    
    
    /**
     * 保存 行政班排课 分班方案
     * @param classList
     * @param grade
     * @param acadyear
     * @param semester
     * @param useJxb 
     * @return 
     */
	String saveXzbDivide(List<Clazz> classList, Grade grade, String acadyear, String semester, boolean useJxb);

	void showDivideXzbList(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map);

	/**
	 * 行政班 虚拟教学班 显示
	 * @param divide
	 * @param fromSolve
	 * @param arrayId
	 * @param map
	 * @param type
	 */
    void divideXzbVirtualCount(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map, String type);

    /**
	 * 教学班 或者班级统计数据获取
	 * @param divide
	 * @param fromSolve
	 * @param arrayId
	 * @param map
	 * @param type
	 */
	void divideResultCount(NewGkDivide divide, String fromSolve, String arrayId, ModelMap map, String type);
	/**
	 * @param unitId
	 * @param divideId
	 */
	void deleteOpenXzbNext(String unitId,String divideId);

	void divideJxbResultCount(NewGkDivide divide, String fromSolve, String arrayId, String subjectId, ModelMap map);
	/**
	 * 全手动分班 模式 完成分班
     * @param array
     * @param b
     * @param divide
     * @param savedJxbList 改变教学班的批次点
     * @param updateBatchList
     */
	void saveFinishDivide(NewGkDivide divide, String[] delClassIds, List<NewGkDivideClass> savedJxbList, List<NewGkClassBatch> updateBatchList);
	/**
	 * 3+1+2 清除剩下两科的组合班
	 * @param unitId
	 * @param divideId
	 */
	void deleteOpenLeftZhbNext(String unitId, String divideId);

	void deleteOpenJxbNext(String unitId, String divideId, String subjectType);
	
	void deleteOpenJxb(String unitId, String divideId, String subjectType);
}
