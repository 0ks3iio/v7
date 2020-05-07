package net.zdsoft.bigdata.metadata.entity;

public enum FolderType {

    /**
     * 目录
     */
    DIRECTORY(1),
    /**
     * 文件夹
     */
    FOLDER(2);

    private Integer value;

    FolderType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
