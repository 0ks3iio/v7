package net.zdsoft.activity.dto;

import net.zdsoft.familydear.entity.FamilyDearObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.activity.dto
 * @ClassName: FamilyDearAuditDto
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/6/19 17:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/19 17:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FamilyDearAuditDto implements Serializable {

            //干部部门，结亲村，干部姓名，干部联系电话，结亲对象，结亲对象联系电话，结亲对象地址，报名批次，报名时间
    private static final long serialVersionUID = 1L;
    private String id;
    private String deptName;
    private String contrys;
    private String contrysSub;
    private int state;
    private String teacherName;
    private String teacherPhone;
    private String batchType;
    private Date applyTime;
    private String remark;
    private List<FamilyDearObject> familyDearObjectList;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getContrys() {
        return contrys;
    }

    public void setContrys(String contrys) {
        this.contrys = contrys;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }


    public List<FamilyDearObject> getFamilyDearObjectList() {
        return familyDearObjectList;
    }

    public void setFamilyDearObjectList(List<FamilyDearObject> familyDearObjectList) {
        this.familyDearObjectList = familyDearObjectList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContrysSub() {
        return contrysSub;
    }

    public void setContrysSub(String contrysSub) {
        this.contrysSub = contrysSub;
    }
}
