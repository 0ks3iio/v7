package net.zdsoft.newgkelective.data.dto;

import java.util.Date;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;

/**
 * Created by Administrator on 2018/3/7.
 */
public class NewGkSubjectTeacherDto {
    private String itemId;
    private String itemName;
    private Date creationTime;
    private List<NewGkTeacherPlan> teacherPlanList;


    public List<NewGkTeacherPlan> getTeacherPlanList() {
        return teacherPlanList;
    }

    public void setTeacherPlanList(List<NewGkTeacherPlan> teacherPlanList) {
        this.teacherPlanList = teacherPlanList;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }


}
