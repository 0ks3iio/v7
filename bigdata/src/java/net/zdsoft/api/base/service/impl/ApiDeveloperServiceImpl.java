package net.zdsoft.api.base.service.impl;

import net.zdsoft.api.base.dao.ApiDeveloperDao;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.exception.MailSenderException;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

@Service("apiDeveloperService")
public class ApiDeveloperServiceImpl extends BaseServiceImpl<ApiDeveloper, String> implements ApiDeveloperService {

    @Autowired
    private ApiDeveloperDao developerDao;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private BigLogService bigLogService;

    @Override
    protected BaseJpaRepositoryDao<ApiDeveloper, String> getJpaDao() {
        return developerDao;
    }

    @Override
    protected Class<ApiDeveloper> getEntityClass() {
        return ApiDeveloper.class;
    }

    @Override
    public ApiDeveloper findByTicketKey(String ticketKey) {
        return developerDao.findByTicketKey(ticketKey);
    }

    @Override
    public ApiDeveloper findByUsername(String username) {
        return developerDao.findByUsername(username);
    }

    @Override
    public void save(ApiDeveloper user) {
        developerDao.save(user);
    }

    @Override
    public int updatePwd(String newPwd, String id) {
        return developerDao.updatePwd(newPwd, id);
    }

    @Transactional(rollbackFor = MailSenderException.class)
    @Override
    public void updatePasswordAndSendEmail(String password, String id) throws MailSenderException {
        ApiDeveloper oldDeveloper = findOne(id);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-optionParam");
        logDto.setDescription("重置"+oldDeveloper.getRealName() +"的密码 ");
        logDto.setOldData(oldDeveloper.getPassword());
        logDto.setNewData(password);
        logDto.setBizName("开发者管理");
        bigLogService.updateLog(logDto);
        developerDao.updatePwd(password, id);
        ApiDeveloper developer = findOne(id);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(((JavaMailSenderImpl)javaMailSender).getUsername());
        String mailText = "您的开发者账号密码已被重置【"+ PWD.decode(password)+"】，该邮件是由robot自动发送请不要回复该邮件";
        message.setText(mailText);
        message.setSubject("密码重置提醒");
        message.setTo(developer.getEmail());
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MailSenderException("密码重置邮件发送失败", e);
        }
    }

    @Override
    public int updateDeveloper(ApiDeveloper developer) {
        return developerDao.updateDeveloper(
                nullToString(developer.getDescription()), nullToString(developer.getRealName()),
                nullToString(developer.getMobilePhone()), nullToString(developer.getEmail()),
                nullToString(developer.getAddress()),nullToString(developer.getIps()), developer.getId());
    }

    private String nullToString(String str) {
        if (Validators.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @Override
    public List<ApiDeveloper> getAllOdereByCreationTime() {
        return developerDao.findAllOdereByCreationTime();
    }

    @Override
    public int updateUnitName(String id, String name) {
        return developerDao.updateUnitName(name, id);
    }

    @Override
    public int updateIps(String id, String ips) {
        return developerDao.updateIps(ips, id);
    }

	@Override
	public List<ApiDeveloper> findByApkeyAndCreationTimeDesc(String apKey) {
		return developerDao.findByApkeyAndCreationTimeDesc(apKey);
	}

	@Override
	public void delInterface(String[] type, String ticketKey) {
		// TODO 删除申请接口和通过的字段
		
	}

	@Override
	public List<ApiDeveloper> findByTicketKeyIn(String[] ticketKeys) {
		return developerDao.findByTicketKeyIn(ticketKeys);
	}
}
