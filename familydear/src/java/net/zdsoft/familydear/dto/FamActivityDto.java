package net.zdsoft.familydear.dto;

import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamDearArrange;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.dto
 * @ClassName: FamActivityDto
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/8 16:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/8 16:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FamActivityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<FamDearArrange> famDearArrangeList;
    private String createUserId;
    private String unitId;
    private String id;
    private String year;
    private String planId;
    private String activityType;
    private String title;
    private String workObjective;
    private String require;
    private String content;
    private String fileContent;
    private String state;
    public List<FamDearArrange> getFamDearArrangeList() {
        return famDearArrangeList;
    }

    public void setFamDearArrangeList(List<FamDearArrange> famDearArrangeList) {
        this.famDearArrangeList = famDearArrangeList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkObjective() {
        return workObjective;
    }

    public void setWorkObjective(String workObjective) {
        this.workObjective = workObjective;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
