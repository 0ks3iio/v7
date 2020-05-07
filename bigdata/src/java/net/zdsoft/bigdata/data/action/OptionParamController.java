package net.zdsoft.bigdata.data.action;

import net.zdsoft.bigdata.data.dto.DesktopOptionDto;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.Option;
import net.zdsoft.bigdata.data.entity.OptionParam;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.OptionParamService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.druid.DruidClientService;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by wangdongdong on 2018/11/27 15:40.
 */
@Controller
@RequestMapping("/bigdata/setting/option")
public class OptionParamController extends BigdataBaseAction{

	@Resource
	private OptionService optionService;
	@Resource
	private OptionParamService optionParamService;
	@Resource
	private SysOptionRemoteService sysOptionRemoteService;
	@Resource
	private EsClientService esClientService;
	@Resource
	private DruidClientService druidClientService;
	@Resource
	private KylinClientService kylinClientService;
	@Resource
	private BigLogService bigLogService;


	@RequestMapping("/index")
	public String optionIndex(ModelMap map) {
		return "/bigdata/setting/optionIndex.ftl";
	}

	@RequestMapping("/list")
	public String list(String type, ModelMap map) {
		if ("desktop".equals(type)) {
			List<OptionParam> paramList = optionParamService
					.findByOptionCode("desktop");
			Json desktopOption = new Json();
			for (OptionParam param : paramList) {
				desktopOption.put(param.getParamKey(), param.getParamValue());
			}
			String systemFilePath = sysOptionRemoteService
					.findValue(Constant.FILE_URL);
			map.put("systemFilePath", systemFilePath);
			map.put("desktopOption", desktopOption);
			return "/bigdata/setting/desktopOption.ftl";
		}
		List<Option> optionList = optionService.findAllOptionByType(type);
		map.put("optionList", optionList);
		map.put("type", type);
		return "/bigdata/setting/optionList.ftl";

	}

	@RequestMapping("/view")
	public String view(String code, ModelMap map)
			throws BigDataBusinessException {
		Option frame = optionService.findByCode(code);
		List<OptionParam> paramList = optionParamService.findByOptionCode(code);
		map.put("paramList", paramList);
		map.put("status", frame.getStatus());
		map.put("code", frame.getCode());
		map.put("type", frame.getType());
		map.put("remark", frame.getRemark());
		return "/bigdata/setting/optionDetail.ftl";
	}

	@RequestMapping("/editParam")
	@ResponseBody
	public Response editParam(String frameParamId) {
		OptionParam frameParam = optionParamService.findOne(frameParamId);
		return Response.ok().data(frameParam).build();
	}

	/**
	 * 更新框架参数状态
	 * @param frameParamId
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateParamStatus")
	@ResponseBody
	public Response updateParamStatus(String frameParamId, Integer status) {
		Option frameParam = optionService.findOne(frameParamId);
		optionService.updateOptionStatus(frameParamId, status);
		if ("kylin".equals(frameParam.getCode())) {
			kylinClientService.reInit();
		}
		//业务日志埋点
		LogDto logDto=new LogDto();
		logDto.setBizCode("update-optionParamStatus");
		logDto.setDescription(frameParam.getName()+"的启用状态");
		logDto.setOldData(frameParam);
		Option newFrameParam = optionService.findOne(frameParamId);
		logDto.setNewData(newFrameParam);
		logDto.setBizName("参数设置");
		bigLogService.updateLog(logDto);

		return Response.ok().build();
	}

	/**
	 * 编辑参数设置
	 * @param frameParam
	 * @return
	 */
	@RequestMapping("/saveParam")
	@ResponseBody
	public Response saveParam(OptionParam frameParam) {
		OptionParam oldFrameParam = optionParamService.findOne(frameParam.getId());
		optionParamService.saveOptionParam(frameParam);

		//业务日志埋点 编辑修改
		LogDto logDto=new LogDto();
		logDto.setBizCode("update-optionParam");
		logDto.setDescription(frameParam.getParamName());
		logDto.setOldData(oldFrameParam);
		logDto.setNewData(frameParam);
		logDto.setBizName("参数设置");
		bigLogService.updateLog(logDto);

		return Response.ok().build();
	}


	@RequestMapping("/saveDesktopParam")
	@ResponseBody
	public Response saveDesktopParam(DesktopOptionDto desktopParam) {
		try {
			List<OptionParam> paramList = optionParamService
					.findByOptionCode("desktop");
			for (OptionParam param : paramList) {
				if (param.getParamKey().equals("platform_name")) {
					//业务日志埋点  修改
					LogDto logDto=new LogDto();
					logDto.setBizCode("update-desktopParam");
					logDto.setDescription("桌面参数 "+param.getParamValue());
					logDto.setOldData(param);
					logDto.setNewData(desktopParam);
					logDto.setBizName("参数设置");
					bigLogService.updateLog(logDto);
					param.setParamValue(desktopParam.getPlatformName());
					optionParamService.saveOptionParam(param);

				}
				if (param.getParamKey().equals("logo")) {
					if (StringUtils.isNotBlank(desktopParam.getLogoPath())
							&& StringUtils.isNotBlank(desktopParam
									.getLogoFileName())) {
						String tempFileFullPath = desktopParam.getLogoPath()
								+ File.separator
								+ desktopParam.getLogoFileName();
						String systemFilePath = sysOptionRemoteService
								.findValue(Constant.FILE_PATH);
						String realFilePath = systemFilePath
								+ java.io.File.separator + "bigdata"
								+ java.io.File.separator + "desktop"
								+ java.io.File.separator + "logo";
						String realFileFullPath = realFilePath
								+ java.io.File.separator
								+ desktopParam.getLogoFileName();
						String relativeFilePath = java.io.File.separator
								+ "bigdata" + java.io.File.separator
								+ "desktop" + java.io.File.separator + "logo"
								+ java.io.File.separator
								+ desktopParam.getLogoFileName();
						File tempFile = new File(tempFileFullPath);
						if (!tempFileFullPath
								.equalsIgnoreCase(realFileFullPath)) {
							File newFile = new File(realFilePath);
							if (!newFile.exists()) {
								newFile.mkdirs();
							}
							copyFile(tempFileFullPath, realFileFullPath);
							if (tempFile.exists())
								tempFile.delete();
						}
						param.setParamValue(relativeFilePath);
						optionParamService.saveOptionParam(param);
					}
				}
			}


			return Response.ok().message("保存成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}

	}

	@RequestMapping("/resetFrame")
	@ResponseBody
	public Response resetFrame(String code) {
		return Response.ok().message("客户端重新初始化成功").build();
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}
}
