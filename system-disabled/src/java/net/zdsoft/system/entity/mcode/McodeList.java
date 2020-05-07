package net.zdsoft.system.entity.mcode;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_mcode_list")
public class McodeList extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String mcodeId;
    private String mcodeName;
    private Integer mcodeLength;
    private Integer mcodeType;
    private Integer maintain;
    private String mcodeRemark;
    private Integer subsystem;
    private Integer isReport;
    private Integer isUsing;

    public String getMcodeId() {
        return mcodeId;
    }

    public void setMcodeId(String mcodeId) {
        this.mcodeId = mcodeId;
    }

    public String getMcodeName() {
        return mcodeName;
    }

    public void setMcodeName(String mcodeName) {
        this.mcodeName = mcodeName;
    }

    public Integer getMcodeLength() {
        return mcodeLength;
    }

    public void setMcodeLength(Integer mcodeLength) {
        this.mcodeLength = mcodeLength;
    }

    public Integer getMcodeType() {
        return mcodeType;
    }

    public void setMcodeType(Integer mcodeType) {
        this.mcodeType = mcodeType;
    }

    public Integer getMaintain() {
        return maintain;
    }

    public void setMaintain(Integer maintain) {
        this.maintain = maintain;
    }

    public String getMcodeRemark() {
        return mcodeRemark;
    }

    public void setMcodeRemark(String mcodeRemark) {
        this.mcodeRemark = mcodeRemark;
    }

    public Integer getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(Integer subsystem) {
        this.subsystem = subsystem;
    }

    public Integer getIsReport() {
        return isReport;
    }

    public void setIsReport(Integer isReport) {
        this.isReport = isReport;
    }

    public Integer getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Integer isUsing) {
        this.isUsing = isUsing;
    }

    @Override
    public String fetchCacheEntitName() {
        return "mcodeList";
    }

}
