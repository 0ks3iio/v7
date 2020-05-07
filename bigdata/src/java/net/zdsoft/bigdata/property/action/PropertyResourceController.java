package net.zdsoft.bigdata.property.action;

import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2019/1/28 14:23.
 */
@Controller
@RequestMapping(value = { "/bigdata/property" })
public class PropertyResourceController extends BigdataBaseAction {

	@Resource
	private FolderService folderService;

	@RequestMapping("/resource/index")
	public String allFolder(ModelMap map) {
		List<FolderEx> folderTree = folderService.findFolderTree();
		map.put("folderTree", folderTree);
		map.put("type", "/all");
		map.put("identity", "admin");
		return "/bigdata/property/content/index.ftl";
	}
}
