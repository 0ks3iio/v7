package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 14:17.
 */
public class JobContentReader {

    private String name;

    private JobContentParameter parameter;

    public String getName() {
        return name;
    }

    public JobContentReader setName(String name) {
        this.name = name;
        return this;
    }

    public JobContentParameter getParameter() {
        return parameter;
    }

    public JobContentReader setParameter(JobContentParameter parameter) {
        this.parameter = parameter;
        return this;
    }
}
