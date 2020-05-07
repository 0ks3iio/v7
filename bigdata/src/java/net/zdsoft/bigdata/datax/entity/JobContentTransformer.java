package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/5/9 9:37.
 */
public class JobContentTransformer {

    private String name;

    private JobContentTransformerParameter parameter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobContentTransformerParameter getParameter() {
        return parameter;
    }

    public void setParameter(JobContentTransformerParameter parameter) {
        this.parameter = parameter;
    }
}
