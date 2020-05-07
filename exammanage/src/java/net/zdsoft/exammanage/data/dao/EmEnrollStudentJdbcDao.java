package net.zdsoft.exammanage.data.dao;

import java.util.Map;

public interface EmEnrollStudentJdbcDao {

    public Map<String, Integer> getStudentAllCount(String[] examIds);

    public Map<String, Integer> getStudentPassNum(String[] examIds);

    public Map<String, Integer> getStudentAuditNum(String[] examIds);

    public Map<String, Integer> findByExamIdAndHasPassAndHasGood(String examId, String hasPass, String hasGood);
}
