package net.zdsoft.newgkelective.data.dto;

import net.zdsoft.basedata.entity.Course;

public class ChoiceCourseDto {

    private Course course;
    private String state; // 1 代表选择该科目 0代表未选

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}