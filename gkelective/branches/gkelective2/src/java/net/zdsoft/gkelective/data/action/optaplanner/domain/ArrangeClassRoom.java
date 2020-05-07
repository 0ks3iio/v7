package net.zdsoft.gkelective.data.action.optaplanner.domain;

import java.util.List;

public class ArrangeClassRoom extends AbstractPersistable {

	private static final long serialVersionUID = 1L;
	private String classRoomId;
	private String subjectId;
	// A选考 B学考
	private String roomType;
	/* subjectId + roomType */
	private String subjectIdRoomType;
    private int minCapacity;
    private int maxCapacity;
    private int batch;
    private String level;
    
    
    // 返回值
    private List<ArrangeStudent> studentList;
    
    @Override
    public String toString() {
        return classRoomId;
    }

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 获取classRoomId
	 * @return classRoomId
	 */
	public String getClassRoomId() {
	    return classRoomId;
	}

	/**
	 * 设置classRoomId
	 * @param classRoomId classRoomId
	 */
	public void setClassRoomId(String classRoomId) {
	    this.classRoomId = classRoomId;
	}

	/**
	 * 获取subjectId
	 * @return subjectId
	 */
	public String getSubjectId() {
	    return subjectId;
	}

	/**
	 * 设置subjectId
	 * @param subjectId subjectId
	 */
	public void setSubjectId(String subjectId) {
	    this.subjectId = subjectId;
	}

	/**
	 * 获取roomType
	 * @return roomType
	 */
	public String getRoomType() {
	    return roomType;
	}

	/**
	 * 设置roomType
	 * @param roomType roomType
	 */
	public void setRoomType(String roomType) {
	    this.roomType = roomType;
	}

	/**
	 * 获取subjectIdRoomType
	 * @return subjectIdRoomType
	 */
	public String getSubjectIdRoomType() {
	    return subjectIdRoomType;
	}

	/**
	 * 设置subjectIdRoomType
	 * @param subjectIdRoomType subjectIdRoomType
	 */
	public void setSubjectIdRoomType(String subjectIdRoomType) {
	    this.subjectIdRoomType = subjectIdRoomType;
	}

	/**
	 * 获取minCapacity
	 * @return minCapacity
	 */
	public int getMinCapacity() {
	    return minCapacity;
	}
	/**
	 * 设置minCapacity
	 * @param minCapacity minCapacity
	 */
	public void setMinCapacity(int minCapacity) {
	    this.minCapacity = minCapacity;
	}
	/**
	 * 获取maxCapacity
	 * @return maxCapacity
	 */
	public int getMaxCapacity() {
	    return maxCapacity;
	}
	/**
	 * 设置maxCapacity
	 * @param maxCapacity maxCapacity
	 */
	public void setMaxCapacity(int maxCapacity) {
	    this.maxCapacity = maxCapacity;
	}

	/**
	 * 获取batch
	 * @return batch
	 */
	public int getBatch() {
	    return batch;
	}

	/**
	 * 设置batch
	 * @param batch batch
	 */
	public void setBatch(int batch) {
	    this.batch = batch;
	}

	/**
	 * 获取studentList
	 * @return studentList
	 */
	public List<ArrangeStudent> getStudentList() {
	    return studentList;
	}

	/**
	 * 设置studentList
	 * @param studentList studentList
	 */
	public void setStudentList(List<ArrangeStudent> studentList) {
	    this.studentList = studentList;
	}

//	/**
//	 * 获取studentList
//	 * @return studentList
//	 */
//	public List<ArrangeStudent> getStudentList() {
//	    return studentList;
//	}
//
//	/**
//	 * 设置studentList
//	 * @param studentList studentList
//	 */
//	public void setStudentList(List<ArrangeStudent> studentList) {
//	    this.studentList = studentList;
//	}
	
	
}
