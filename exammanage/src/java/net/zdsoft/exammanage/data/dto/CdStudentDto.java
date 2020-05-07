package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmTitleInfo;

import java.io.Serializable;
import java.util.Map;

public class CdStudentDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String studentName;
    private String rhcode;//容厚学生唯一性参数
    private float zf;//总分

    // 小题分数
    private Map<EmTitleInfo, Float> titleScoreMap;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public float getZf() {
        return zf;
    }

    public void setZf(float zf) {
        this.zf = zf;
    }

    public String getRhcode() {
        return rhcode;
    }

    public void setRhcode(String rhcode) {
        this.rhcode = rhcode;
    }

    public Map<EmTitleInfo, Float> getTitleScoreMap() {
        return titleScoreMap;
    }

    public void setTitleScoreMap(Map<EmTitleInfo, Float> titleScoreMap) {
        this.titleScoreMap = titleScoreMap;
    }


}
