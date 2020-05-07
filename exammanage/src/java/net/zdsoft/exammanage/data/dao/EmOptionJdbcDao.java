package net.zdsoft.exammanage.data.dao;

import java.util.Set;

public interface EmOptionJdbcDao {

    public Set<String> getExamRegionId(String examId);
}
