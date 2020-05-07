package net.zdsoft.newgkelective.data.dto;

import net.zdsoft.basedata.entity.Course;

import java.util.List;

/**
 * 选课类别与科目组合
 */
public class CourseCategoryDto {

    // 类别名称
    private String id;
    private String categoryName;
    private Integer orderId;
    private Integer maxNum;
    private Integer minNum;
	private List<Course> courseList;
    private List<CourseCategoryDto> courseCombination;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Integer getMinNum() {
        return minNum;
    }

    public void setMinNum(Integer minNum) {
        this.minNum = minNum;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<CourseCategoryDto> getCourseCombination() {
        return courseCombination;
    }

    public void setCourseCombination(List<CourseCategoryDto> courseCombination) {
        this.courseCombination = courseCombination;
    }

    @Override
    public String toString() {
        return "CourseCategoryDto{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", maxNum=" + maxNum +
                ", minNum=" + minNum +
                ", courseList=" + courseList +
                ", courseCombination=" + courseCombination +
                '}';
    }
}
