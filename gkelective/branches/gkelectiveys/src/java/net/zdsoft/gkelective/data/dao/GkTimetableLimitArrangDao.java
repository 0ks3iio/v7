package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkTimetableLimitArrangDao extends BaseJpaRepositoryDao<GkTimetableLimitArrang, String> {
    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3 and limitId = ?4 and limitType = ?5")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangList(String unitId,String acadyear, String semester,
            String limitId, String limitType);

    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3  and limitType = ?4 and limitId in(?5)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListbyLimitIds(String unitId,String acadyear, String semester,
            String limitType, List<String> list);

    @Modifying
    @Query("delete from GkTimetableLimitArrang where id = ?1")
    void deleteById(String id);

    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3 and limitType = ?4 and arrangId = ?5 and limitId in(?6)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListBylimitIdIn(String unitId,String acadyear, String semester,
            String limitType, String arrangId, String[] limitIds);

    @Modifying
    @Query("delete from GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3 and limitId = ?4 and period = ?5 and periodInterval = ?6 and weekday = ?7")
    public void deleteBySelectXyz(String unitId,String acadyear, String semester, String limitId, String period,
            String periodInterval, String weekday);

    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3 and limitId = ?4 and period = ?5 and periodInterval = ?6 and weekday = ?7")
    public GkTimetableLimitArrang findGkTimetableLimitArrangBySelectXyz(String unitId,String acadyear, String semester,
            String limitId, String period, String periodInterval, String weekday);
    
    @Query("From GkTimetableLimitArrang where isUsing = 1 and unitId=?1 and acadyear = ?2 and semester = ?3  and period = ?4 and periodInterval = ?5 and weekday = ?6 and limitId in (?7)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangBySelectXyz(String unitId,String acadyear, String semester,
            String period, String periodInterval, String weekday,String[] limitIds);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where id in (?1) ")
    public void deleteByIds(String[] ids);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where unitId=?1 and acadyear=?2 and semester = ?3 and limitId=?4 and limitType = ?5 ")
	public void deleteByLimitId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where unitId=?1 and acadyear=?2 and semester = ?3 and arrangId=?4 and limitType = ?5 ")
	public void deleteByArrangeId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 0 where unitId=?1 and acadyear = ?2 and semester = ?3 and limitId=?4 and limitType = ?5 ")
	public void notUsingByLimitId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 0 where unitId=?1 and acadyear = ?2 and semester = ?3 and arrangId=?4 and limitType = ?5 ")
	public void notUsingByArrangeId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 1 where unitId=?1 and acadyear=?2 and semester = ?3 and limitId=?4 and limitType = ?5 ")
	public void yesUsingByLimitId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 1 where unitId=?1 and acadyear=?2 and semester = ?3 and arrangId=?4 and limitType = ?5 ")
	public void yesUsingByArrangeId(String unitId,String acadyear, String semester,
			String roundsId, String type);
    
    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear = ?2 and semester = ?3 and limitId = ?4 and limitType = ?5 and isUsing=1 ")
	public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListIsUsing(String unitId,
			String searchAcadyear, String searchSemester, String limitId,
			String limitType);
    @Query("From GkTimetableLimitArrang where unitId=?1 and acadyear=?2 and semester = ?3 and arrangId=?4 and limitType = ?5 ")
	public List<GkTimetableLimitArrang> findByArrangeIdType(String uintId,String acadyear,String semester,String arrangeId,
			String limitType);
}
