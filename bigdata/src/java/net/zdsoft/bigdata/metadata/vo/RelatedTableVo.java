package net.zdsoft.bigdata.metadata.vo;


/**
 * 元数据关联表vo
 * @author zhanwz
 * @since 2019-6-20
 */
public class RelatedTableVo {
    private String id;
    private String masterTableName;
    private String followerTableName;
    private String masterTableColumnName;
    private String followerTableColumnName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterTableName() {
        return masterTableName;
    }

    public void setMasterTableName(String masterTableName) {
        this.masterTableName = masterTableName;
    }

    public String getFollowerTableName() {
        return followerTableName;
    }

    public void setFollowerTableName(String followerTableName) {
        this.followerTableName = followerTableName;
    }

    public String getMasterTableColumnName() {
        return masterTableColumnName;
    }

    public void setMasterTableColumnName(String masterTableColumnName) {
        this.masterTableColumnName = masterTableColumnName;
    }

    public String getFollowerTableColumnName() {
        return followerTableColumnName;
    }

    public void setFollowerTableColumnName(String followerTableColumnName) {
        this.followerTableColumnName = followerTableColumnName;
    }
}
