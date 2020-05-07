package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;

public interface GkTimetableLimitArrangService extends BaseService<GkTimetableLimitArrang, String>{
    void save(GkTimetableLimitArrang gkTimetableLimitArrang);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangList(String unitId, String acadyear, String semester,
            String limitId, String limitType);

    void deleteById(String id);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangListBylimitIdIn(String unitId, String acadyear,
            String semester, String limitType, String arrangId, String[] limitIds);

    void batchSave(List<GkTimetableLimitArrang> gkTimetableLimitArrangList);

    void deleteBySelectXyz(String acadyear, String semester, String roundsId, String period, String periodInterval,
            String weekday);

    GkTimetableLimitArrang findGkTimetableLimitArrangBySelectXyz(String unitId, String acadyear, String semester,
            String limitId, String period, String periodInterval, String weekday);

    String saveTimetableLimitArrang(String selectxyz, String planId, String dataFilter);

    List<GkTimetableLimitArrang> findGkTimetableLimitArrangListbyLimitIds(String unitId, String searchAcadyear,
            String searchSemester, String limitType, String... limitIds);
    public List<GkTimetableLimitArrang> saveAllEntitys(GkTimetableLimitArrang... gkTimetableLimitArrang);
    
    public void deleteByRoundsId(String unitId, String acadyear, String semester, String roundsId);

	void notUsing(String unitId, String acadyear, String semester, String roundsId);

	void yesUsing(String unitId, String acadyear, String semester, String roundsId);

	public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListIsUsing(
			String unitId, String searchAcadyear, String searchSemester,
			String limitId, String limitType);

	public List<GkTimetableLimitArrang> findByArrangeIdType(String unitId, String acadyear,String semester,
			String arrangeId, String limitType) ;

	public void deleteBtIds(String[] ids);
}
