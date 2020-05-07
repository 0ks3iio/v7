package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.School;

public interface SchoolRemoteService extends BaseRemoteService<School,String> {

    /**
     * 的获取学校信息
     * 
     * @param regionCodes
     *            学校所在的行政区划
     * @return List(School)
     */
    public String findByRegionCodes(String... regionCodes);

    /**
     * @param code
     * @return
     */
    public String findByCode(String code);

    /**
     * @param districtId
     * @return
     */
    public String findByDistrictId(String districtId);

    /**
     * @param runschtype
     * @param string
     * @param schoolName
     * @return
     */
    public String findByTypeSectionName(String runschType, String section, String schoolName);

    /**
     * @param regiontype
     * @param string
     * @return
     */
    public String findByRegiontypeSection(String regionType, String section);

    /**
     * @param section
     * @param schoolName
     * @return
     */
    public String findBySectionName(String section, String schoolName);

    /**
     * 数组形式entitys参数，返回list的json数据
     * 
     * @param entitys
     * @return
     */
    public String saveAllEntitys(String entitys);

    /**
     * 查询学校学段信息
     * 
     * @author cuimq
     * @param id
     * @return
     */
    public String findSectionsById(String id);

	/**
	 * @param codeIn
	 * @return
	 */
	public String findByCodeIn(String[] schoolCodes);

}
