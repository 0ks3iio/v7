package net.zdsoft.bigdata.data.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2018/6/4 9:37.
 */
public class ReportTermTree {

    private String nodeName;

    private ReportTerm reportTerm;

    private List<ReportTermTree> childs;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public ReportTerm getReportTerm() {
        return reportTerm;
    }

    public void setReportTerm(ReportTerm reportTerm) {
        this.reportTerm = reportTerm;
    }

    public List<ReportTermTree> getChilds() {
        return childs;
    }

    public void setChilds(List<ReportTermTree> childs) {
        this.childs = childs;
    }
}
