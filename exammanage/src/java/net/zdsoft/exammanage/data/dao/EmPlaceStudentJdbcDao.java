package net.zdsoft.exammanage.data.dao;

import java.util.Map;
import java.util.Set;

public interface EmPlaceStudentJdbcDao {

    public Set<String> getClsSchMap(String examId);

    public Set<String> getExamIds(String[] examIds);

    public Set<String> getPlaceIdSet(String examId);

    public Map<String, Integer> getAllCountMap(String[] examIds);

    public Map<String, Integer> getCountMapByPlaceIds(String examId, String groupId, String[] emPlaceIds);

    public int getCountByPlaceIds(String... placeIds);
}
