package net.zdsoft.gkelective.data.dto;

import java.util.List;

public class Room {
    /* 科目 */
    private String subjectId;
    
    /* 批次 */
    private int batch;
    
    /* 序号 */
    private int number;
    
    private List<StudentSubjectDto> studentList;
    
    private Combined combined;
    
//    private List<String> combinedSubjectOrder;
    
    //关联批次
    private int connectBatch;
    
    private String type;//学考 选考
    
    private String level;
    
    
    @Override
	protected Room clone() throws CloneNotSupportedException {
    	// 内部学生不拷贝
		return (Room)super.clone();
	}
    
    
    public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
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
     * 获取number
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * 设置number
     * @param number number
     */
    public void setNumber(int number) {
        this.number = number;
    }
    
    public List<StudentSubjectDto> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<StudentSubjectDto> studentList) {
		this.studentList = studentList;
	}

	/**
     * 获取combined
     * @return combined
     */
    public Combined getCombined() {
        return combined;
    }

    /**
     * 设置combined
     * @param combined combined
     */
    public void setCombined(Combined combined) {
        this.combined = combined;
    }

    /**
     * 获取connectBatch
     * @return connectBatch
     */
    public int getConnectBatch() {
        return connectBatch;
    }

    /**
     * 设置connectBatch
     * @param connectBatch connectBatch
     */
    public void setConnectBatch(int connectBatch) {
        this.connectBatch = connectBatch;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
}
