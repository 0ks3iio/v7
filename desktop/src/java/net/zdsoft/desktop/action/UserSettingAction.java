package net.zdsoft.desktop.action;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.SysUserBindRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.desktop.service.UserSetService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shenke
 * @since 2016/12/29 12:46
 */
@Controller
@RequestMapping("/desktop/user")
public class UserSettingAction extends DeskTopBaseAction{

    @Autowired private UserRemoteService userRemoteService;
    @Autowired private UserSetService userSetService;
    @Autowired private SystemIniRemoteService systemIniRemoteService;
    @Autowired private SysUserBindRemoteService sysUserBindRemoteService;

    @ResponseBody
    @RequestMapping("/pwd/reset")
    @ControllerInfo("重置密码")
    public String doSetPassword(HttpServletRequest request,@RequestBody String pass){
    	String password = null;
    	String newPassword = null;
    	String confirmPwd = null;
    	JSONObject jsonObject = SUtils.dc(pass, JSONObject.class);    	
    	password = jsonObject.getString("c1");
    	newPassword = jsonObject.getString("c3");
    	confirmPwd = jsonObject.getString("c2");
        User user = null;
        try{
            String decodePwd = PWD.decode(password);
            user = SUtils.dc(userRemoteService.findOneById(getUserId()),User.class);
            if (user !=null && !StringUtils.equals(PWD.decode(user.getPassword()),decodePwd)){
                return error("原密码错误");
            }
            if(StringUtils.equals(password,newPassword)){
            	return error("原密码和修改的密码一致");
            }
            if (!StringUtils.equals(newPassword,confirmPwd)){
                return error("两次输入密码不一致");
            }
            String  regex = systemIniRemoteService.findValue(Constant.SYSTEM_PASSWORD_STRONG);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(PWD.decode(newPassword));
            //弱密码
            if ( matcher.matches() ) {
                String alert = sysOptionRemoteService.findValue(Constant.SYSTEM_PASSWORD_ALERT);
                return error((StringUtils.isBlank(alert) ? "密码强度不够（建议使用数字、字母、符号组合）" : alert));
            }

            newPassword = PWD.decode(newPassword);
            userRemoteService.updatePassportPasswordByUsername(getLoginInfo().getUserName(), PWD.encodeIfNot(newPassword));
            
//            if (Evn.isPassport()) {
//                Account account = PassportClientUtils.getPassportClient().queryAccountByUsername(getLoginInfo().getUserName());
//                if ( account != null) {
//                    userRemoteService.updatePasswordByUsername(getLoginInfo().getUserName(), PWD.encodeIfNot(newPassword));
//                    account.setPassword(SecurityUtils.encodeBySelf(newPassword));
//                    PassportClientUtils.getPassportClient().modifyAccount(account, new String[]{"password"});
//                    return success("密码重置成功");
//                } else {
//                    LOG.error("passport账户不存在");
//                    return error("密码重置失败");
//                }
//            }else{
//                userRemoteService.updatePasswordByUsername(getLoginInfo().getUserName(),  PWD.encodeIfNot(newPassword));
                return success("密码重置成功");
//            }
        }catch(Exception e){
            LOG.error("密码重置失败",e);
            //userRemoteService.updatePasswordByUsername(getLoginInfo().getUserName(),PWD.encodeIfNot(password));
            return error("密码重置失败");
        }
    }

    @ResponseBody
    @RequestMapping("/pwd/verify")
    @ControllerInfo(ignoreLog=1, value="校验密码")
    public String doVerifyPassword(){
        try{
            String pwd = getRequest().getParameter("c1");
            User user = SUtils.dt(userRemoteService.findOneById(getLoginInfo().getUserId()),new TypeReference<User>(){});
            String realPwd = PWD.decode(user.getPassword());
            return new ResultDto().setSuccess(StringUtils.equals(realPwd,pwd)).toJSONString();
        }catch(Exception e){
            LOG.error("密码校验异常",e);
            return error("网络错误，请稍后重试");
        }
    }

    @ResponseBody
    @RequestMapping("/info/load")
    @ControllerInfo("加载个人信息")
    public String doLoadUserInfo(ModelMap map){
        String msg = null;
        try{
            User user = SUtils.dt(userRemoteService.findOneById(getLoginInfo().getUserId()),new TypeReference<User>(){});
            if(user != null){
                map.put("realName",user.getRealName());
                map.put("sex",user.getSex());
                map.put("birthday", user.getBirthday());
                map.put("mobilePhone",user.getMobilePhone());
                map.put("success","true");
                return JSONUtils.toJSONString(map);
            }
            return error("当前用户已被删除");
        }catch(Exception e){
            LOG.error("个人信息加载失败",e);
            msg = "加载个人信息失败"+e.getMessage();
            return error(msg);
        }
    }

    @ResponseBody
    @RequestMapping("/info/reset")
    @ControllerInfo("保存个人信息")
    public String doResetUserInfo(@RequestBody User user,@RequestBody Teacher teacher, String birthday){
        Account oldAccount = null;
        try {
        	String region = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
            User realUser = new User();
            realUser.setId(getLoginInfo().getUserId());
            realUser.setRealName(StringEscapeUtils.escapeHtml4(user.getRealName()));
            realUser.setSex(user.getSex());
            if (Objects.equals(DeployRegion.DEPLOY__HAIKOU, region)) {
            	if(StringUtils.isNotBlank(birthday)){
            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            		System.out.println(birthday);
            	    Date birthdayDate = formatter.parse(birthday);
            	    realUser.setBirthday(birthdayDate);
            	}
            }
            realUser.setMobilePhone(user.getMobilePhone());
            User u = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()), User.class);
            if(Evn.isPassport()){
            	Account account = PassportClientUtils.getPassportClient().queryAccount(u.getAccountId());
	            oldAccount = (Account) BeanUtils.cloneBean(account);
	            account.setPhone(StringEscapeUtils.escapeHtml4(user.getMobilePhone()));
	            account.setRealName(StringEscapeUtils.escapeHtml4(user.getRealName()));
	            if (Objects.equals(DeployRegion.DEPLOY__HAIKOU, region)) {	            	
	            	if(StringUtils.isNotBlank(birthday)){
	            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	            	    Date birthdayDate = formatter.parse(birthday);
	            		account.setBirthday(birthdayDate);
	            	}
	            }
            	PassportClientUtils.getPassportClient().modifyAccount(account, ArrayUtils.toArray("phone","realName"));
            }
            
            boolean succ = userRemoteService.updateUser2(getLoginInfo().getUserId(), StringEscapeUtils.escapeHtml4(user.getRealName()), user.getSex(), user.getMobilePhone(), realUser.getBirthday(),teacher);
            return succ?success("个人信息更新成功"):error("个人信息更新失败");
        } catch (PassportException e) {
            LOG.error("passport更新个人信息失败",e);
            return error("个人信息更新失败");
        } catch (RpcException e) {
            LOG.error("远程调用失败");
            try {
            	if(Evn.isPassport()){
            		PassportClientUtils.getPassportClient().modifyAccount(oldAccount,ArrayUtils.toArray("phone","realName","birthday"));
            	}
            } catch (Exception e1){
                return error("passport信息更新成功，EIS更新失败");
            }
            return error("更新失败");
        } catch (Exception e ) {
            try {
            	if(Evn.isPassport()){
            		PassportClientUtils.getPassportClient().modifyAccount(oldAccount,ArrayUtils.toArray("phone","realName","birthday"));
            	}
            } catch (Exception e1){
                return error("passport信息更新成功，EIS更新失败");
            }
            return error("更新失败");
        } finally {
            oldAccount = null;
        }
    }
    
    //解除qq和微信
    @ResponseBody
    @RequestMapping("/relieve")
    @ControllerInfo("解除QQ/微信绑定")
    public String doRelieve(String type){
        try {
        //	http://passport.dqedu.net:83
        	String url = Evn.getString(net.zdsoft.framework.entity.Constant.DQ_WEB_URL);
            User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()),User.class);
            String accountId = user.getAccountId();
            url = UrlUtils.ignoreLastRightSlash(url)
    				+ "/unbinding.htm";
        	String dataUrl = url +"?accountId="+accountId +"&type=" +type;
    		String data = UrlUtils.readContent(dataUrl, getRequest().getSession()
    				.getId(), "utf8");
    		JSONObject json= JSONObject.parseObject(data);
    		if(json.getString("success").equals("1")){
    			return success(json.getString("message"));
    		}else {
    			return error(json.getString("message"));
			}
        } catch (Exception e){
            LOG.error("");
            return error("解绑失败");
        }
        
    }
    
    @ResponseBody
    @RequestMapping(value = "/layout/save")
    @ControllerInfo("保存桌面布局")
    public String doSaveLayout(String layout){
        try {
            UserSet userSet = userSetService.findByUserId(getUserId());
            if (userSet == null){
                userSet = initDefaultUserSet(getUserId());
            }
            userSet.setLayout(layout);
            userSetService.save(userSet);
        } catch (Exception e){
            LOG.error("保存布局失败 layout {}, error {}",layout, ExceptionUtils.getStackTrace(e));
            return error("保存失败");
        }
        return success("保存成功");
    }
    
    @ResponseBody
    @RequestMapping(value = "/unbind")
    @ControllerInfo("解绑用户")
    public String doUnbind(String remoteUserId){
		try {
			sysUserBindRemoteService.deleteByRemoteUserIdIn(new String[]{remoteUserId});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("用户解绑失败！");
		}
		return success("用户解绑成功！");
	}
	
    @ResponseBody
    @RequestMapping(value = "/bind/save")
    @ControllerInfo("保存用户绑定")
	public String doSaveUserBind(HttpServletRequest request,@RequestBody String pass){
    	JSONObject jsonObject = SUtils.dc(pass, JSONObject.class);    	
    	String remoteUserId = jsonObject.getString("c1");
    	String userId = jsonObject.getString("c2");
    	String remoteUsername = jsonObject.getString("c3");
    	String remotePassword = jsonObject.getString("c4");
		try {
			SysUserBind bi;
			if(StringUtils.isBlank(remoteUserId)){
				bi = new SysUserBind();
				bi.setCreationTime(new Date());
				bi.setRemoteUserId(UuidUtils.generateUuid());
			}else{
				bi = SUtils.dc(sysUserBindRemoteService.getSysUserBindById(remoteUserId), SysUserBind.class);
			}
			if(StringUtils.isNotBlank(remoteUsername)){
				SysUserBind bi1 = SUtils.dc(sysUserBindRemoteService.findByRemoteUsername(remoteUsername), SysUserBind.class);
				if(bi1 != null && !StringUtils.equals(bi1.getUserId(), userId)){
					return error("该平台用户已经被绑定过了！");
				}
			}
			bi.setModifyTime(new Date());
			bi.setUserId(userId);
			bi.setRemoteUsername(remoteUsername);
			bi.setRemotePassword(remotePassword);
			sysUserBindRemoteService.save(bi);
		} catch (Exception e){
			log.error(e.getMessage(), e);
			return error("用户绑定失败！");
		}
		return success("保存成功");
	}
    
    
    private UserSet initDefaultUserSet(String userId){
        UserSet userSet = new UserSet();
        userSet = new UserSet();
        userSet.setLayout(UserSet.LAYOUT_DEFAULT);
        userSet.setUserId(getUserId());
        userSet.setId(UuidUtils.generateUuid());
        userSetService.save(userSet);
        return userSet;
    }
    
    public static void main(String[] args){
        String regex = "^[0-9]+$|^[a-zA-Z]+$|^(.)\\1+$|^(.){0,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(PWD.decode("zdsoft12"));
        System.out.println(matcher.matches());
    }
}
