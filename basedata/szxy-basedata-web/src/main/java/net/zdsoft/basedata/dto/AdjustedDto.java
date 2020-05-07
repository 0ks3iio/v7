package net.zdsoft.basedata.dto;

/**
 *
 * @since 2018/10/17
 */
public class AdjustedDto {

    private static final long serialVersionUID = 1L;
    private String Id;
    private String operatorId; // 一般情况为需调教师Id
    private String operatorName;
    private String classId;
    private String className;
    private String adjustingId;
    private String adjustingName;
    private String adjustingTeacherName;
    private String beenAdjustedId;
    private String beenAdjustedName;
    private String beenAdjustedTeacherName;
    private String remark;
    private String state;
    private String applyType; // 自主申请或者教务安排
    private boolean canDelete; // 只要需调或被调课程其中一项在今天或今天之后即可删除

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAdjustingId() {
        return adjustingId;
    }

    public void setAdjustingId(String adjustingId) {
        this.adjustingId = adjustingId;
    }

    public String getAdjustingName() {
        return adjustingName;
    }

    public void setAdjustingName(String adjustingName) {
        this.adjustingName = adjustingName;
    }

    public String getBeenAdjustedId() {
        return beenAdjustedId;
    }

    public void setBeenAdjustedId(String beenAdjustedId) {
        this.beenAdjustedId = beenAdjustedId;
    }

    public String getBeenAdjustedName() {
        return beenAdjustedName;
    }

    public void setBeenAdjustedName(String beenAdjustedName) {
        this.beenAdjustedName = beenAdjustedName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getAdjustingTeacherName() {
        return adjustingTeacherName;
    }

    public void setAdjustingTeacherName(String adjustingTeacherName) {
        this.adjustingTeacherName = adjustingTeacherName;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getBeenAdjustedTeacherName() {
        return beenAdjustedTeacherName;
    }

    public void setBeenAdjustedTeacherName(String beenAdjustedTeacherName) {
        this.beenAdjustedTeacherName = beenAdjustedTeacherName;
    }
}
