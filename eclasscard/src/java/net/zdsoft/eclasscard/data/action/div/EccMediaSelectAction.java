package net.zdsoft.eclasscard.data.action.div;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.JsonArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/eclasscard/group/media")
public class EccMediaSelectAction  extends BaseAction{
	Logger logger = Logger.getLogger(EccMediaSelectAction.class);
	

	@Autowired
	private EccAttachFolderService eccAttachFolderService;

	
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData(String dataId,String toObjectId) {
		List<String[]> dataList = new LinkedList<String[]>();
		List<EccAttachFolder> attachFolders = Lists.newArrayList();
		if(StringUtils.isNotBlank(toObjectId)){
			attachFolders = eccAttachFolderService.findListByObjId(toObjectId,EccConstants.ECC_FOLDER_RANGE_1);
		}else{
			attachFolders = eccAttachFolderService.findListBySchIdRange2(getLoginInfo().getUnitId(),EccConstants.ECC_FOLDER_RANGE_2);
		}
		attachFolders = attachFolders.stream().filter(line -> (line.getNumber()!=null && line.getNumber()>0)).collect(Collectors.toList());
		for (EccAttachFolder attachFolder : attachFolders) {
			String[] data = new String[5];
			data[0] = attachFolder.getType()+"";//分组和封面区分
			data[1] = attachFolder.getId();//唯一标识
			data[2] = attachFolder.getTitle();//显示名称
			data[3] = StringUtils.isNotBlank(attachFolder.getId())&&attachFolder.getId().equals(dataId)?"1":"0";//1.已选择，0未选择
			data[4] = attachFolder.getCoverUrl();//图片
			dataList.add(data);
		}
		return JsonArray.toJSON(dataList).toString();
	}
}
