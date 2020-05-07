package net.zdsoft.system.action;

import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.dto.common.LoginPageDto;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.service.LoginDomainService;
import net.zdsoft.system.service.ServerRegionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static net.zdsoft.system.constant.Constant.*;

/**
 * oa7 登陆页面设置
 *
 * @author ke_shen@126.com
 * @since 2017/12/26
 */
@Controller
@RequestMapping(value = "ops/loginSet")
public class LoginPageSetAction extends BaseAction {

	private Logger logger = LoggerFactory.getLogger(LoginPageSetAction.class);

	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;

	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private SysProductParamRemoteService sysProductParamRemoteService;
	@Autowired
	private ServerRegionService serverRegionService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private LoginDomainService loginDomainService;


	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String loginShowIndex(String regionCode) {
		return "/system/ops/loginShowIndex.ftl";
	}
	
	/**
	 * 登陆页设置首页
	 */
	@RequestMapping(value = "/domain/page", method = RequestMethod.GET)
	public ModelAndView loginSetIndex(String regionCode,String unitId) {
		ModelAndView mv = new ModelAndView("/system/ops/loginPageSet.ftl");
		String filePath = sysOptionRemoteService.findValue("FILE.PATH");
		
		String domainName = null;
		File configFile;
		boolean isEdit = Boolean.FALSE;
		if(StringUtils.isNotBlank(unitId)){
			List<LoginDomain> loginDomains = loginDomainService.findByUnitIdIn(unitId);
			domainName = loginDomains.get(0).getRegionAdmin();
			regionCode = loginDomains.get(0).getRegion();
			configFile = new File(filePath + File.separator + LOGIN_PAGE_DIR + File.separator + domainName + LOGIN_PAGE_CONF_NAME);
			isEdit = Boolean.TRUE;
			mv.addObject("unitId", unitId);
		}else{
			if (StringUtils.isBlank(regionCode)) {
				regionCode = StringUtils.trimToEmpty(systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION"));
			}
			configFile = new File(filePath + File.separator + LOGIN_PAGE_DIR + File.separator + regionCode + LOGIN_PAGE_CONF_NAME);
		}
		
		mv.addObject("regionCode", regionCode);
		mv.addObject("isEdit", isEdit);  //是否修改
		
		//配置文件不存在
		if (!configFile.exists()) {
			LoginPageDto pageDto = new LoginPageDto();
			pageDto.setCommonPageTitle("智慧校园");
			pageDto.setCopyRight(sysProductParamRemoteService.findProductParamValue(SysProductParam.COMPANY_COPYRIGHT));
			pageDto.setPageTitle("数字校园");
			pageDto.setPageLogoName(pageDto.getPageTitle());
			
			if(StringUtils.isNotBlank(unitId)){
				pageDto.setDomainName(domainName);
			}
			pageDto.setPalyer(true);
			pageDto.setStandard(true);
			pageDto.setEnablePageLogoImage(true);
			pageDto.setEnablePageLogoName(true);
			pageDto.setForgetPassword(false);
			mv.addObject("loginOption", pageDto);
			mv.addObject("player", true);
			mv.addObject("standard", true);
			mv.addObject("phone", false);
			mv.addObject("favicon", false);
			return mv;
		}
		else {
			Properties configProperties = new Properties();
			try {
				//加载原有的配置，有可能做更新操作
				FileInputStream inputStream = new FileInputStream(configFile);
				configProperties.load(inputStream);
				inputStream.close();
			} catch (IOException e) {
				logger.error("加载配置文件失败", e);
			}
			LoginPageDto pageDto = new LoginPageDto();
			pageDto.setCommonPageTitle(configProperties.getProperty(COMMON_PAGE_TITLE));
			pageDto.setPageTitle(configProperties.getProperty(LOGIN_PAGE_TITLE));
			pageDto.setPalyer(BooleanUtils.toBoolean(configProperties.getProperty(LOGIN_PAGE_PLAYER)));
			pageDto.setPageLogoName(configProperties.getProperty(LOGIN_PAGE_LOGO_NAME));
			pageDto.setCopyRight(configProperties.getProperty(SysProductParam.COMPANY_COPYRIGHT));
			pageDto.setForgetPassword(BooleanUtils.toBoolean(configProperties.getProperty(FORGET_PASSWORD)));
			pageDto.setFavicon(BooleanUtils.toBoolean(configProperties.getProperty("favicon")));
			pageDto.setEnablePageLogoImage(BooleanUtils.toBoolean(configProperties.getProperty(ENABLE_LOGIN_PAGE_LOGO_BG_PATH)));
			pageDto.setEnablePageLogoName(BooleanUtils.toBoolean(configProperties.getProperty(ENABLE_LOGIN_PAGE_LOGO_NAME)));
			if(StringUtils.isNotBlank(unitId)){
				pageDto.setDomainName(domainName);
			}
			mv.addObject("player", pageDto.isPalyer());
			mv.addObject("loginOption", pageDto);
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "clear")
	public String clear(String region) {
		if (org.apache.commons.lang3.StringUtils.isBlank(region)) {
			region = systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION");
		}
		String filePath = sysOptionRemoteService.findValue("FILE.PATH");
		File configFile = new File(filePath + File.separator +
				LOGIN_PAGE_DIR + File.separator + region + LOGIN_PAGE_CONF_NAME);
		configFile.deleteOnExit();
		return success("clear success");
	}

	@ResponseBody
	@RequestMapping(value = "/saveLoginPageInfo", method = RequestMethod.POST)
	public String saveLoginInfo(LoginPageDto loginPageDto, String region , String unitId) {
		try {
			String filePath = sysOptionRemoteService.findValue("FILE.PATH");
			if (StringUtils.isBlank(region)) {
				region = StringUtils.trimToEmpty(systemIniRemoteService
						.findValue("SYSTEM.DEPLOY.REGION"));
//				region = systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION");
			}
			String domainName = null;
			if(StringUtils.isNotBlank(unitId)){
				if(isNewLoginDomain(loginPageDto, unitId)){
					domainName = loginPageDto.getDomainName();
				}else{
					return error("配置的域名已经存在，请重新输入");
				}
			}

			String configDirPath = LOGIN_PAGE_DIR;
			//1、检查配置文件目录是否存在
			File configDir = new File(filePath + File.separator + configDirPath);
			if (!configDir.exists()) {
				boolean createDir = configDir.mkdirs();
				if (!createDir) {
					return error("无法创建配置文件目录");
				}
			}
			//2、检查配置文件是否存在
			File configFile;
			if(StringUtils.isNotBlank(domainName)){
				configFile = new File(filePath + File.separator + configDirPath + File.separator + domainName + LOGIN_PAGE_CONF_NAME);
			}else{
				configFile = new File(filePath + File.separator + configDirPath + File.separator + region + LOGIN_PAGE_CONF_NAME);
			}
			if (!configFile.exists()) {
				boolean createConfig = configFile.createNewFile();
				if (!createConfig) {
					return error("无法创建配置文件");
				}
			}
			//加载原有的配置，有可能做更新操作
			Properties configProperties = new Properties();
			FileInputStream inputStream = new FileInputStream(configFile);
			configProperties.load(inputStream);
			inputStream.close();

			doSaveLoginConfig(loginPageDto, region, filePath, configDirPath,
					configFile, configProperties);
			
			if(StringUtils.isNotBlank(unitId) && StringUtils.isNotBlank(loginPageDto.getDomainName())){
				doSaveLoginDomain(loginPageDto, region, unitId);
			}

			RedisUtils.delBeginWith("loginOption");
			return success("保存成功");

		} catch (Exception e) {
			log.error("配置信息保存失败", e);
			return error("配置信息保存失败" + e.getMessage());
		}

	}

	private void doSaveLoginConfig(LoginPageDto loginPageDto, String region,
			String filePath, String configDirPath, File configFile,
			Properties configProperties) throws IOException,
			FileNotFoundException {
		//图片存储
		MultipartFile loginBgImage = loginPageDto.getLoginBgImage();
		String loginBgImagePath =  configDirPath + File.separator
				+ region + "_loginBgImage." + StorageFileUtils.getFileExtension(loginBgImage.getOriginalFilename());
		if (!loginBgImage.isEmpty()) {
			configProperties.setProperty(LOGIN_PAGE_BG_PATH, loginBgImagePath);
			loginBgImage.transferTo(new File(filePath + File.separator + loginBgImagePath));
		} else {
			new File(filePath + File.separator + loginBgImagePath).deleteOnExit();
			configProperties.setProperty(LOGIN_PAGE_BG_PATH, "");
		}

		MultipartFile logoBgImage = loginPageDto.getPageLogoImage();
		String logoBgImagePath = configDirPath + File.separator
				+ region + "logoBgImage." + StorageFileUtils.getFileExtension(logoBgImage.getOriginalFilename());

		if (!logoBgImage.isEmpty()) {
				configProperties.setProperty(LOGIN_PAGE_LOGO_BG_PATH, logoBgImagePath);
				logoBgImage.transferTo(new File(filePath + File.separator + logoBgImagePath));
		}
		else {
			new File(filePath + File.separator + logoBgImagePath).deleteOnExit();
			configProperties.setProperty(LOGIN_PAGE_LOGO_BG_PATH, "");
		}

		configProperties.setProperty(LOGIN_PAGE_LOGO_NAME, loginPageDto.getPageLogoName());
		configProperties.setProperty(LOGIN_PAGE_TITLE, loginPageDto.getPageTitle());
		configProperties.setProperty(COMMON_PAGE_TITLE, loginPageDto.getCommonPageTitle());
		configProperties.setProperty(PHONE_AS_USER_NAME, String.valueOf(loginPageDto.isPhoneAsUserName()));
		configProperties.setProperty(SysProductParam.COMPANY_COPYRIGHT, loginPageDto.getCopyRight());
		configProperties.setProperty(LOGIN_PAGE_PLAYER, String.valueOf(loginPageDto.isPalyer()));
		configProperties.setProperty(FORGET_PASSWORD, String.valueOf(loginPageDto.isForgetPassword()));
		configProperties.setProperty("favicon", String.valueOf(loginPageDto.isFavicon()));
		
		configProperties.setProperty(ENABLE_LOGIN_PAGE_LOGO_NAME, String.valueOf(loginPageDto.isEnablePageLogoName()));
		configProperties.setProperty(ENABLE_LOGIN_PAGE_LOGO_BG_PATH, String.valueOf(loginPageDto.isEnablePageLogoImage()));
		
		FileOutputStream out = new FileOutputStream(configFile);
		configProperties.store(out, region + "登陆页配置文件");
		out.close();
	}

	private Boolean isNewLoginDomain (LoginPageDto loginPageDto,String unitId){
		String haveUnitId = null;
		if(loginPageDto != null){
			LoginDomain nameDomain = loginDomainService.findByRegionAdmin(loginPageDto.getDomainName());
			if(nameDomain != null){
				haveUnitId = nameDomain.getUnitId();
			}
		}
		if(StringUtils.isNotBlank(haveUnitId) && !unitId.equals(haveUnitId)){
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	private void doSaveLoginDomain(LoginPageDto loginPageDto, String region,
			String unitId) {
		List<LoginDomain> loginDomains = loginDomainService.findByUnitIdIn(unitId);
		LoginDomain loginDomain = null;
		if(CollectionUtils.isNotEmpty(loginDomains)){
			loginDomain = loginDomains.get(0);
		}else{
			loginDomain = new LoginDomain();
			loginDomain.setId(UuidUtils.generateUuid());
		}
		loginDomain.setRegion(org.apache.commons.lang3.StringUtils.rightPad(region, 6, "0"));
		loginDomain.setUnitId(unitId);
		loginDomain.setRegionAdmin(loginPageDto.getDomainName());
		loginDomain.setIsDeleted(LoginDomain.DEFAULT_IS_DELETED_VALUE);
		loginDomainService.save(loginDomain);
	}
}




















