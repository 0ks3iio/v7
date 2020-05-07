package net.zdsoft.bigdata.metadata.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.bigdata.metadata.entity.*;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/1/28 14:23.
 */
@Controller
@RequestMapping(value = { "/bigdata/user" })
public class FolderController extends BigdataBaseAction {

	@Resource
	private FolderService folderService;
	@Resource
	private FolderDetailService folderDetailService;

	private final String USER_FOLDER_PREFIX = "BG_USER_FOLDER_";
	private final String ALL_FOLDER_KEY = "BG_ALL_FOLDER";

	@RequestMapping("/folder/all")
	public String allFolder(ModelMap map) {
		List<FolderEx> folderTree = folderService.findFolderTree();
		map.put("folderTree", folderTree);
		map.put("type", "/all");
		map.put("identity", "admin");
		return "/bigdata/folder/index.ftl";
	}

	@RequestMapping("/folder")
	public String userFolder(ModelMap map) {
		String userId = getLoginInfo().getUserId();
		List<FolderEx> authFolderTree = RedisUtils.getObject(USER_FOLDER_PREFIX + userId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<FolderEx>>() {}, new RedisInterface<List<FolderEx>>(){
			@Override
			public List<FolderEx> queryData() {
				return folderService.findAuthFolderTree(
						getLoginInfo().getUnitId(), userId);
			}
		});
		map.put("folderTree", authFolderTree);
		map.put("identity", "user");
		return "/bigdata/folder/index.ftl";
	}

	@RequestMapping("/folderDetail/{folderId}")
	public String folderDetail(ModelMap map,
			@PathVariable("folderId") String folderId) {
		LoginInfo loginInfo = getLoginInfo();
		// 查询该目录下所有文件夹
		List<Folder> folders = folderService.findListBy(new String[] {
				"parentId", "folderType" }, new String[] { folderId,
				FolderType.FOLDER.getValue().toString() });
		if (folders.size() < 1) {
			return "/bigdata/folder/folderDetail.ftl";
		}
		String[] folderIds = folders.stream().map(Folder::getId)
				.toArray(String[]::new);
		Map<String, List<FolderDetail>> folderDetailMap = folderService
				.findFolderDetailMap(loginInfo.getUnitId(),
						loginInfo.getUserId(), folderIds);
		Iterator<Folder> it = folders.iterator();
		while (it.hasNext()) {
			Folder folder = it.next();
			if (!folderDetailMap.containsKey(folder.getId())) {
				it.remove();
			}
		}
		map.put("folders", folders);
		map.put("folderDetailMap", folderDetailMap);
		map.put("imgMap", FolderDetailImg.toMap());
		map.put("identity", "user");
		return "/bigdata/folder/folderDetail.ftl";
	}

	@RequestMapping("/folderDetail/all/{folderId}")
	public String allFolderDetail(ModelMap map,
			@PathVariable("folderId") String folderId) {
		// 查询该目录下所有文件夹
		List<Folder> folders = folderService.findListBy(new String[] {
				"parentId", "folderType" }, new String[] { folderId,
				FolderType.FOLDER.getValue().toString() });
		if (folders.size() < 1) {
			return "/bigdata/folder/folderDetail.ftl";
		}
		String[] folderIds = folders.stream().map(Folder::getId)
				.toArray(String[]::new);
		Map<String, List<FolderDetail>> folderDetailMap = folderDetailService
				.findAllFolderDetailByFolderId(folderIds).stream()
				.collect(Collectors.groupingBy(FolderDetail::getFolderId));
		map.put("folders", folders);
		map.put("folderDetailMap", folderDetailMap);
		map.put("imgMap", FolderDetailImg.toMap());
		map.put("identity", "admin");
		return "/bigdata/folder/folderDetail.ftl";
	}

	@RequestMapping("/folderDetail/search/index")
	public String searchFolderDetailIndex(ModelMap map, String identity) {
		List<FolderDetail> folderDetails = null;
		if ("user".equals(identity)) {
			folderDetails = folderDetailService.findRecentAuthorityFolderDetail(getLoginInfo().getUnitId(), getLoginInfo().getUserId());
		} else {
			folderDetails = folderDetailService.findRecentFolderDetail();
		}
		map.put("folderDetails", folderDetails);
		map.put("imgMap", FolderDetailImg.toMap());
		map.put("identity", identity);
		return "/bigdata/folder/search/index.ftl";
	}

	@RequestMapping("/folderDetail/search")
	public String searchFolderDetail(ModelMap map, String businessName, String identity) {
		List<FolderDetail> folderDetails = null;
		if ("user".equals(identity)) {
			folderDetails = folderDetailService.findAllAuthorityFolderDetailByBusinessName(getLoginInfo().getUnitId(), getLoginInfo().getUserId(), businessName);
		} else {
			folderDetails = folderDetailService.findAllFolderDetailByBusinessName(businessName);
		}
		map.put("folderDetails", folderDetails);
		map.put("imgMap", FolderDetailImg.toMap());
		return "/bigdata/folder/search/searchResult.ftl";
	}


	@RequestMapping("/folder/view")
	public String view(ModelMap map, String folderDetailId) {
		map.put("containHeader", false);
		FolderDetail folderDetail = folderDetailService.findOne(folderDetailId);
		if (folderDetail == null) {
			map.put("errorMsg", "该报表已经被删除");
			return "/bigdata/v3/common/error-full.ftl";
		}
		doLogAccess(getLoginInfo().getUserId() + "-desktop-report-mark", Json
				.toJSONString(folderDetail), Long.valueOf(9));
		map.put("folderDetail", folderDetail);
		map.put("hasDelete", false);
		return "/bigdata/folder/viewPage/index.ftl";
	}

	@RequestMapping("/report/preview")
	public String preview(ModelMap map, String businessId, String businessType,
			String businessName) {
		FolderDetail folderDetail = new FolderDetail();
		folderDetail.setBusinessId(businessId);
		folderDetail.setBusinessType(businessType);
		folderDetail.setBusinessName(businessName);
		map.put("folderDetail", folderDetail);
		map.put("containHeader", false);
		map.put("hasDelete", false);
		return "/bigdata/folder/viewPage/index.ftl";
	}

	private void doLogAccess(String type, String value, Long length) {
		String luaScript = "local length=redis.call('lpush', KEYS[1], ARGV[1]) if (length>%s) then redis.call('ltrim',KEYS[1], 0, %s); end;";
		luaScript = String.format(luaScript, length, length - 1);
		RedisUtils.eval(luaScript, type, Collections.singletonList(value));
	}
}
