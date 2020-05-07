package net.zdsoft.eclasscard.data.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: panlf
 * @Date: 2019/12/2 15:23
 */

public class EccSeatSetDto {
    private String id;
    private String gradeId;
    private String unitId;
    @NotBlank(message = "classId不能为空")
    private String classId;
    @Pattern(regexp = "^[12]$",message = "班级类型不匹配")
    private String classType;
    @NotNull(message = "请输入行数")
    @Max(value = 15,message = "列数最多为15列")
    private Integer rowNumber;
    @NotNull(message = "请输入列数")
    @Max(value =15,message = "行数最多为15行")
    private Integer colNumber;
    @NotNull(message = "请输入过道1")
    private Integer space1;  //过道1
    @NotNull(message = "请输入过道2")
    private Integer space2;  //过道2

    /**
     * 默认设置
     * @return
     */
    public static EccSeatSetDto defaultInstance() {
        EccSeatSetDto dto = new EccSeatSetDto();
        dto.setRowNumber(8);
        dto.setColNumber(10);
        dto.setSpace1(3);
        dto.setSpace2(7);
        return dto;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColNumber() {
        return colNumber;
    }

    public void setColNumber(Integer colNumber) {
        this.colNumber = colNumber;
    }

    public Integer getSpace1() {
        return space1;
    }

    public void setSpace1(Integer space1) {
        this.space1 = space1;
    }

    public Integer getSpace2() {
        return space2;
    }

    public void setSpace2(Integer space2) {
        this.space2 = space2;
    }
}
