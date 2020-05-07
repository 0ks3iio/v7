package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/19 下午6:07
 */
@Data
@Entity
@Table(name = "base_grade")
public class Grade implements Serializable {
    @Id
    private String id;
    /**
     * 年级名称
     */
    private String gradeName;
    /**
     * 学校id
     */
    @Column(updatable=false)
    private String schoolId;
    /**
     * 上午开课数
     */
    private Integer amLessonCount;
    /**
     * 下午开课数
     */
    private Integer pmLessonCount;
    /**
     * 晚上开课数
     */
    private Integer nightLessonCount;
    /**
     *开设学年学期 必填
     */
    @Column(updatable=false)
    private String openAcadyear;
    /**
     * 学制
     * 默认小学6年/初高3年
     */
    private Integer schoolingLength;
    /**
     * 毕业标识
     */
    private Integer isGraduate;
    /**
     * 学段
     * 0幼儿/1小学/2初中/3高中
     */
    private Integer section;
    /**
     * 年级组长id
     */
    private String teacherId;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 软删除标记
     */
    private Integer isDeleted;
    /**
     * 事件来源
     */
    private Integer eventSource;
    /**
     * 年级编号
     */
    private String gradeCode;
    /**
     *校区id
     */
    private String subschoolId;
    /**
     * 大课间，如22,32，表示上午第2节课后和下午第2节课后是大课间
     */
    private String recess;
    /**
     * 早自习节数
     */
    private Integer mornPeriods;
    /**
     * 周天数
     */
    private Integer weekDays;


}
