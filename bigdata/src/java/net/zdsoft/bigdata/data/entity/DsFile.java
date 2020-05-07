package net.zdsoft.bigdata.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件数据源
 *
 */
@Entity
@Table(name = "bg_sys_ds_file")
public class DsFile extends AbstractDatabase<String> {

    private String type;
    private Short headType;
    private String filePath;
    private String fileName;
    private String thumbnail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Short getHeadType() {
        return headType;
    }

    public void setHeadType(Short headType) {
        this.headType = headType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DsFile";
    }
}
