package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.newgkelective.data.entity.NewGkArray;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;

public interface NewGkElectiveArrayComputeService {

	/**
	 * 7选3 模式排课
	 * @param key
	 * @param key1
	 * @param newGkArray
	 * @return
	 */
	CGInputData computeArray3F7(String key, String key1, NewGkArray newGkArray);

	/**
	 * 设置自习教室
	 * @param arrayId
	 */
	//void rangeFreePeriods(String arrayId);

	/**
	 * 排课入口；统一 普通排课 和 跨年级排课
	 * @param inputList
	 * @param newGkArray
	 * @param key
	 * @param key1
	 */
	void dealMutilArray(List<CGInputData> inputList, NewGkArray newGkArray, String key, String key1);

	/**
	 * 保存 7选3模式 排课结果
	 * @param cgInputData
	 */
	void saveResult2(CGInputData cgInputData);

	/**
	 * 保存排课结果
	 * @param cgInputData
	 * @param arrayIds 跨年级排课中涉及到的 排课方案id
	 */
    void saveResult3(CGInputData cgInputData, List<String> arrayIds);

    /**
	 * 将分班方案中的 班级学生信息复制一份
	 * @param oldClassList 原班级学生信息
	 * @param array 新复制班级的divideId
	 * @param newSourceType 新复制班级的sourceType 
	 * @param newStuList 新的学生信息 需传一个空的容器
	 * @param newClassList 新的班级信息  需传一个空的容器
	 * @return
	 */
	Map<String, NewGkDivideClass> copyDivideClassToArray(List<NewGkDivideClass> oldClassList, NewGkArray array, String newSourceType, List<NewGkClassStudent> newStuList, List<NewGkDivideClass> newClassList);

}