package net.zdsoft.bigdata.metadata.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/2/19 16:03.
 */
public class FolderEx {


    private String id;

    private Integer folderType;

    private String folderName;

    private List<FolderEx> childFolder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<FolderEx> getChildFolder() {
        return childFolder;
    }

    public void setChildFolder(List<FolderEx> childFolder) {
        this.childFolder = childFolder;
    }
}
