package net.zdsoft.exammanage.data.utils;

public class Group {
    private String groupId;//班级（单位）id
    private int studentNo;//学生数

    public Group(String groupId, int studentNo) {
        this.groupId = groupId;
        this.studentNo = studentNo;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }
}
