package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.Developer;

public interface DeveloperDao extends BaseJpaRepositoryDao<Developer, String> {

    public Developer findByTicketKey(String ticketKey);

    public Developer findByUsername(String username);

    /**
     * 更新密码
     * 
     * @author chicb
     * @param newPwd
     * @param id
     * @return
     */
    @Modifying
    @Query("update Developer set password=?1 where id=?2")
    public int updatePwd(String newPwd, String id);

    /**
     * 修改开发者公司名
     * 
     * @author chicb
     * @param name
     * @param id
     * @return
     */
    @Modifying
    @Query("update Developer set unitName=?1 where id=?2")
    public int updateUnitName(String unitName, String id);

    @Modifying
    @Query("update Developer set ips=?1 where id=?2")
    public int updateIps(String ips, String id);

    /**
     * 根据 id 更新 unitName,description,realName,mobilePhone,email,homepage,address,id
     * 
     * @author chicb
     * @param unitName
     * @param descirption
     * @param realName
     * @param mobilePhone
     * @param email
     * @param homePage
     * @param address
     * @param id
     * @return
     */
    @Modifying
    @Query("update Developer set unitName=?1,description=?2,realName=?3,mobilePhone=?4,email=?5,homepage=?6,address=?7,ips=?8 where id=?9")
    public int updateDeveloper(String unitName, String descirption, String realName, String mobilePhone, String email,
            String homePage, String address, String ips, String id);

    /**
     * 查询所有开发者信息，按时间排序
     * 
     * @author chicb
     * @return
     */
    @Query("from Developer order by creationTime desc")
    public List<Developer> findAllOdereByCreationTime();

}
