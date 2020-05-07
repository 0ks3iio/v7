package net.zdsoft.gkelective.data.service;

public interface GkClassChangeService {

	/**
	 * 调班保存
	 * @param arrangeId TODO
	 * @param roundId TODO
	 * @param operator TODO
	 * @param leftClassSelect 左边班级 type#classId#subjectIds#batch#classType
	 * @param rightClassSelect 右边班级 type#classId#subjectIds#batch#classType
	 * @param leftAddStu 从右边调整到左边的学生
	 * @param rightAddStu 从左边调整到右边的学生
	 */
	void saveClassChange(String arrangeId, String roundId, String operator, String leftClassSelect, String rightClassSelect, String leftAddStu, String rightAddStu);
	
	/**
	 * 学生选课调整
	 * @param unitId
	 * @param arrangeId
	 * @param roundId
	 * @param stuId
	 * @param searchClassType
	 * @param chosenClassIds
	 * @param searchSubjectIds TODO
	 */
	void saveStuSubChange(String unitId, String arrangeId,String roundId,String stuId,String searchClassType, String chosenClassIds, String searchSubjectIds);

}
