package net.zdsoft.base.service;

import java.util.List;

import net.zdsoft.base.dto.DeveloperDto;
import net.zdsoft.base.dto.InterfaceDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.basedata.service.BaseService;

public interface DeveloperService extends BaseService<Developer, String> {

    public Developer findByTicketKey(String ticketKey);

    public Developer findByUsername(String username);

    @Override
    public void save(Developer user);

    /**
     * 更新密码
     * 
     * @author chicb
     * @param newPwd
     * @param id
     * @return
     */
    public int updatePwd(String newPwd, String id);

    /**
     * 更新开发者信息
     * 
     * @author chicb
     * @param developer
     * @return
     */
    public int updateDeveloper(Developer developer);

    /**
     * 查询所有开发者信息，按时间排序
     * 
     * @author chicb
     * @return
     */
    public List<Developer> getAllOdereByCreationTime();

    /**
     * 修改开发者名称
     * 
     * @author chicb
     * @param id
     * @param name
     */
    public int updateUnitName(String id, String name);

    /**
     * 修改开发者白名单
     * 
     * @param id
     * @param ips
     * @return
     */
    public int updateIps(String id, String ips);

	public List<Developer> findByApkeyAndCreationTimeDesc(String apKey);

	public void delInterface(String[] type, String ticketKey);


}