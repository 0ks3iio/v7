package net.zdsoft.familydear.entity;

import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;
import java.util.Date;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.activity.entity
 * @ClassName: FamDearAttachment
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/23 15:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/23 15:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Entity
@Table(name="famdear_attachment")
public class FamDearAttachment extends BaseEntity<String> {
    /**
     *
     */
    private String objecttype;
    /**
     *
     */
    private String filename;
    /**
     *
     */
    private long filesize;
    /**
     *
     */
    private String contenttype;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private Integer creationdate;
    /**
     *
     */
    private Integer modificationdate;
    /**
     *
     */
    private String objId;
    /**
     *
     */
    private String unitId;
    /**
     *
     */
    private String dirId;
    /**
     *
     */
    private String filePath;
    /**
     *
     */
    @Column(updatable=false)
    private Date creationTime;
    /**
     *
     */
    private Date modifyTime;
    /**
     *
     */
    private String extName;

    private String isDelete;

    @Transient
    private String originFilePath;
    @Transient
    public File originFile;
    @Transient
    public File smallFile;
    @Transient
    public String smallFullPath;// fullpath

    public static enum FamDearAttachmentObjType{
        ACTIVITY_PIC1(FamDearConstant.ACTIVITY1,100,100),
        ACTIVITY_PIC2(FamDearConstant.ACTIVITY2,100,100),
        ACTIVITY_PIC3(FamDearConstant.ACTIVITY3,100,100),
        ACTIVITY_PIC4(FamDearConstant.ACTIVITY4,100,100),
        ACTIVITY_PIC5(FamDearConstant.ACTIVITY5,100,100),
        ACTIVITY_PIC6(FamDearConstant.ACTIVITY6,100,100),
        ACTIVITY_PIC7(FamDearConstant.ACTIVITY7,100,100),
        ACTIVITY_PIC8(FamDearConstant.ACTIVITY8,100,100),
        ACTIVITY_PIC9(FamDearConstant.ACTIVITY9,100,100),
        ACTIVITY_PIC10(FamDearConstant.ACTIVITY10,100,100);

//        STUDEVELOP_STU_PIC(StuDevelopConstant.OBJTYPE_STU_PIC, 130, 130),
//        FALMILY_PIC(StuDevelopConstant.OBJTYPE_FALMILY_PIC, 500,400),
//        FALMILYDEAR_PIC("1", 100,100),
//        SCHOOL_INFO(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_INFO, 750, 650),
//        MASTER_PIC(StuDevelopConstant.OBJTYPE_MASTER_PIC,130,130),
//        SCHOOL_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_ACTIVITY,600,500),
//        CLASS_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY,600,500),
//        CLASS_HONOR(StuDevelopConstant.ACTIVITY_TYPE_CLASS_HONOR,600,500),
//        THEME_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_THEME_ACTIVITY,600,500),
//        OUT_SCHOOL_PERFORMANCE(StuDevelopConstant.ACTIVITY_TYPE_OUT_SCHOOL_PERFORMANCE,600,500),
//        KIDS_HOLIDAY(StuDevelopConstant.ACTIVITY_TYPE_KIDS_HOLIDAY,600,500),
//        STUDEV_HONOR(StuDevelopConstant.OBJTYPE_STUDEV_HONOR,300,200);

        FamDearAttachmentObjType(String value, int wight, int height) {
            this.value = value;
            this.wight = wight;
            this.height = height;
        }

        private String value;
        private int wight;
        private int height;

        public String getValue() {
            return value;
        }

        public int getWight() {
            return wight;
        }

        public int getHeight() {
            return height;
        }

        public static FamDearAttachment.FamDearAttachmentObjType getType(String value){
            for(FamDearAttachment.FamDearAttachmentObjType ent : values()){
                if(ent.getValue().equals(value))
                    return ent;
            }
            return null;
        }
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Integer creationdate) {
        this.creationdate = creationdate;
    }

    public Integer getModificationdate() {
        return modificationdate;
    }

    public void setModificationdate(Integer modificationdate) {
        this.modificationdate = modificationdate;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getOriginFilePath() {
        return originFilePath;
    }

    public void setOriginFilePath(String originFilePath) {
        this.originFilePath = originFilePath;
    }

    public File getOriginFile() {
        return originFile;
    }

    public void setOriginFile(File originFile) {
        this.originFile = originFile;
    }

    public File getSmallFile() {
        return smallFile;
    }

    public void setSmallFile(File smallFile) {
        this.smallFile = smallFile;
    }

    public String getSmallFullPath() {
        return smallFullPath;
    }

    public void setSmallFullPath(String smallFullPath) {
        this.smallFullPath = smallFullPath;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String fetchCacheEntitName() {
        return "famDearAttachment";
    }
}
