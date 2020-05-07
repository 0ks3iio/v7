package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 14:25.
 */
public class JobContentWriter {

    private String name;

    private JobContentParameter parameter;

    public String getName() {
        return name;
    }

    public JobContentWriter setName(String name) {
        this.name = name;
        return this;
    }

    public JobContentParameter getParameter() {
        return parameter;
    }

    public JobContentWriter setParameter(JobContentParameter parameter) {
        this.parameter = parameter;
        return this;
    }
}
