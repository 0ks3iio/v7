package net.zdsoft.exammanage.data.entity;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.exammanage.data.entity
 * @ClassName: EmStatHeadEntity
 * @Description: json头部信息实体类
 * @Author: Sweet
 * @CreateDate: 2018/8/22 17:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/22 17:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class EmStatHeadEntity {
    private String title;
    private String header;
    private String summary;
    private String columnHeader;

    public String getTitle() {
        if (title == null) {
            title = "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        if (header == null) {
            header = "";
        }
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getColumnHeader() {
        if (columnHeader == null) {
            columnHeader = "";
        }
        return columnHeader;
    }

    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    public String getSummary() {
        if (summary == null) {
            summary = "";
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
