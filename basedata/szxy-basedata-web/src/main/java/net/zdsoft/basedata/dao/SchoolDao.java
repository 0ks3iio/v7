package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface SchoolDao extends BaseJpaRepositoryDao<School, String> {

    /**
     * @param code
     * @return
     */
    @Query("From School where isDeleted = 0 and schoolCode = ?1")
    School findByCode(String code);

    /**
     * @param runschtype
     * @param section
     *            nvl(s.sections,'') like ? " + " AND s.school_name like ? AND s.is_deleted = 0 ORDER BY u.display_order
     * @param schoolName
     * @return
     */
    @Query("From School where isDeleted = 0 and runSchoolType like ?1 and sections like ?2 and schoolName like ?3")
    List<School> findByTypeSectionName(String runschtype, String section, String schoolName);

    /**
     * @param districtId
     *            schoolDistrictId
     * @return
     */
    @Query("From School where isDeleted = 0 and schoolDistrictId = ?1")
    List<School> findByDistrictId(String districtId);

    /**
     * @param section
     * @param schoolName
     * @return
     */
    @Query("From School where isDeleted = 0 and  sections like ?1 and schoolName like ?2")
    List<School> findBySectionName(String section, String schoolName);

    /**
     * @param regiontype
     * @param section
     * @return
     */
    @Query("From School where isDeleted = 0 and  regionType like ?1 and sections like ?2")
    List<School> findByRegiontypeSection(String regiontype, String section);

    /**
     * 查询学段信息
     * 
     * @author cuimq
     * @param id
     * @return
     */
    @Query("select sections From School where id=?1")
    String findSectionsById(String id);

	/**
	 * @param schoolCodes
	 * @return
	 */
    @Query("From School where isDeleted = 0 and schoolCode in ?1")
    List<School> findByCodeIn(String[] schoolCodes);

}
