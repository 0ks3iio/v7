package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_arrange")
public class EmArrange extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String examId;
    //编排方式 0：随机 1：同校不相邻 2：同班不相邻
    private String type;
    private int sumSeatNum;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSumSeatNum() {
        return sumSeatNum;
    }

    public void setSumSeatNum(int sumSeatNum) {
        this.sumSeatNum = sumSeatNum;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emArrange";
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

}
