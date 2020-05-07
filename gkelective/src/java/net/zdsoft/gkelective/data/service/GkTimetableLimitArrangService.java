package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;

public interface GkTimetableLimitArrangService extends BaseService<GkTimetableLimitArrang, String>{
    void save(GkTimetableLimitArrang gkTimetableLimitArrang);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangList(String acadyear, String semester, String limitId,
            String limitType);

    void deleteById(String id);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangListBylimitIdIn(String acadyear, String semester,
            String limitType, String arrangId, String[] limitIds);

    void batchSave(List<GkTimetableLimitArrang> gkTimetableLimitArrangList);

    void deleteBySelectXyz(String acadyear, String semester, String roundsId, String period, String periodInterval,
            String weekday);

    GkTimetableLimitArrang findGkTimetableLimitArrangBySelectXyz(String acadyear, String semester, String limitId,
            String period, String periodInterval, String weekday);

    String saveTimetableLimitArrang(String selectxyz, String planId, String dataFilter);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangListbyLimitIds(String searchAcadyear, String searchSemester,
            String limitType, String... limitIds);
    public List<GkTimetableLimitArrang> saveAllEntitys(GkTimetableLimitArrang... gkTimetableLimitArrang);
    
    public void deleteByRoundsId(String acadyear, String semester, String roundsId);

	void notUsing(String acadyear, String semester, String roundsId);

	void yesUsing(String acadyear, String semester, String roundsId);

	public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListIsUsing(
			String searchAcadyear, String searchSemester, String limitId,
			String limitType);

	public List<GkTimetableLimitArrang> findByArrangeIdType(String acadyear, String semester,String arrangeId,
			String limitType) ;

	public void deleteBtIds(String[] ids);
}
