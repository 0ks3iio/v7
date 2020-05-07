package net.zdsoft.api.base.service;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.exception.MailSenderException;
import net.zdsoft.basedata.service.BaseService;

import java.util.List;

public interface ApiDeveloperService extends BaseService<ApiDeveloper, String> {

    public ApiDeveloper findByTicketKey(String ticketKey);

    public ApiDeveloper findByUsername(String username);

    @Override
    public void save(ApiDeveloper user);

    /**
     * 更新密码
     * 
     * @author chicb
     * @param newPwd
     * @param id
     * @return
     */
    public int updatePwd(String newPwd, String id);

    void updatePasswordAndSendEmail(String password, String id) throws MailSenderException;

    /**
     * 更新开发者信息
     * 
     * @author chicb
     * @param developer
     * @return
     */
    public int updateDeveloper(ApiDeveloper developer);

    /**
     * 查询所有开发者信息，按时间排序
     * 
     * @author chicb
     * @return
     */
    public List<ApiDeveloper> getAllOdereByCreationTime();

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

	public List<ApiDeveloper> findByApkeyAndCreationTimeDesc(String apKey);

	public void delInterface(String[] type, String ticketKey);

	public List<ApiDeveloper> findByTicketKeyIn(String[] ticketKeys);


}
