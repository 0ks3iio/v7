package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/28 17:52.
 */
public class JobContent {

    private JobContentReader reader;

    private JobContentWriter writer;

    private List<JobContentTransformer> transformer;

    public JobContentReader getReader() {
        return reader;
    }

    public JobContent setReader(JobContentReader reader) {
        this.reader = reader;
        return this;
    }

    public JobContentWriter getWriter() {
        return writer;
    }

    public JobContent setWriter(JobContentWriter writer) {
        this.writer = writer;
        return this;
    }

    public List<JobContentTransformer> getTransformer() {
        return transformer;
    }

    public JobContent setTransformer(List<JobContentTransformer> transformer) {
        this.transformer = transformer;
        return this;
    }
}
