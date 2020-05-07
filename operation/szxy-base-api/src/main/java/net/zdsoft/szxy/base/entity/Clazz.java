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
@Table(name = "base_class")
public class Clazz implements Serializable {

    @Id
    private String id;
    /**
     * 学校id
     */
    @Column(updatable=false)
    private String schoolId;
    /**
     * 班级编码
     */
    private String classCode;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 入学学年
     */
    private String acadyear;
    /**
     * 是否毕业
     */
    private Integer isGraduate;
    /**
     * 学制
     */
    private Integer schoolingLength;
    /**
     * 年级id
     */
    private String gradeId;
    /**
     * 所属学段 必填
     */
    @Column(updatable=false)
    private Integer section;
    /**
     * 班级荣誉称号
     */
    private String honor;
    /**
     * 建班年月
     */
    private Date buildDate;
    /**
     * 班级类型
     * 微代码 DM-BJLX
     */
    private String classType;
    /**
     * 文理类型
     */
    private Integer artScienceType;
    /**
     * 毕业日期
     */
    private Date graduateDate;
    /**
     * 班主任id
     */
    private String teacherId;
    /**
     * 班长id
     */
    private String studentId;
    /**
     * 副班主任id
     */
    private String viceTeacherId;
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
     * 更新戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 软删除
     */
    private int isDeleted;
    /**
     * 时间来源
     */
    private int eventSource;
    /**
     * 校区id
     */
    private String campusId;
    /**
     * 是否复式班级
     */
    private String isDuplexClass;
    /**
     * 生活指导老师
     */
    private String lifeGuideTeacherId;
    /**
     * 合作性质
     */
    private String partnership;
    /**
     * 合作单位
     */
    private String partners;
    /**
     * 单位联系人
     */
    private String contacts;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 所在教师区
     */
    private String teachAreaId;
    /**
     * 专用教室
     */
    private String teachPlaceId;










}
