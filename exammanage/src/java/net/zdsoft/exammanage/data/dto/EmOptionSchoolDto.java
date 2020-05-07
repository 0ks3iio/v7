package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmOptionSchool;

import java.util.ArrayList;
import java.util.List;

public class EmOptionSchoolDto {
    private String schoolId;
    private String schoolName;
    private int count;
    private int arrangeCount;
    private int notArrangeCount;
    private List<EmOptionSchool> enlist = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getArrangeCount() {
        return arrangeCount;
    }

    public void setArrangeCount(int arrangeCount) {
        this.arrangeCount = arrangeCount;
    }

    public int getNotArrangeCount() {
        return notArrangeCount;
    }

    public void setNotArrangeCount(int notArrangeCount) {
        this.notArrangeCount = notArrangeCount;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<EmOptionSchool> getEnlist() {
        return enlist;
    }

    public void setEnlist(List<EmOptionSchool> enlist) {
        this.enlist = enlist;
    }


}
