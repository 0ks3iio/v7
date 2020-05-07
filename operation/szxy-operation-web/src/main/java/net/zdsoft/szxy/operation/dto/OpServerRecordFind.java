package net.zdsoft.szxy.operation.dto;

/**
 * 服务记录动态 分页查询 实体对象
 *
 * @author panlf   2019年1月21日
 */
public class OpServerRecordFind {
    private String unitId;
    private String start;
    private String end;
    private Integer type;
    private Integer page;
    private Integer rows;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ServerRecordFind [unitId=" + unitId + ", start=" + start + ", end=" + end + ", type=" + type + ", page="
                + page + ", rows=" + rows + "]";
    }


}
