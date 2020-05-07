package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 15:07.
 */
public class Job {

    private List<JobContent> content;

    private JobSetting setting;

    public List<JobContent> getContent() {
        return content;
    }

    public Job setContent(List<JobContent> content) {
        this.content = content;
        return this;
    }

    public JobSetting getSetting() {
        return setting;
    }

    public Job setSetting(JobSetting setting) {
        this.setting = setting;
        return this;
    }

    public static void main(String[] args) {

//        JobContentReader reader = new JobContentReader();
//        JobContentParameter parameter = new JobContentParameter();
//        JobContentParameterConnection connection = new JobContentParameterConnection();
//        connection.setJdbcUrl(Lists.newArrayList("jdbc:mysql://127.0.0.1:3306/dq"));
//        connection.setTable(Lists.newArrayList("table1"));
//        parameter.setColumn(Lists.newArrayList("id", "name")).setConnection(Lists.newArrayList(connection)).setUsername("wdd").setPassword("123456");
//        reader.setName("mysqlreader").setParameter(parameter);
//
//        JobContentWriter writer = new JobContentWriter();
//        JobContentParameter wParameter = new JobContentParameter();
//        JobContentParameterConnection wConnection = new JobContentParameterConnection();
//        wConnection.setJdbcUrl(Lists.newArrayList("jdbc:mysql://127.0.0.1:3306/dq"));
//        wConnection.setTable(Lists.newArrayList("table1"));
//        wParameter.setColumn(Lists.newArrayList("id", "name")).setConnection(Lists.newArrayList(wConnection)).setUsername("wdd").setPassword("123456");
//        writer.setName("mysqlreader").setParameter(wParameter);
//
//        JobSetting setting = new JobSetting().setSpeed(new JobSettingSpeed().setChannel("1"));
//        Job job = new Job().setContent(Lists.newArrayList(new JobContent().setReader(reader).setWriter(writer))).setSetting(setting);
//
//        System.out.println(JSON.toJSONString(job));
    }
}
