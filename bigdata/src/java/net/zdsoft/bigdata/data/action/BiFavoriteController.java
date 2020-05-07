package net.zdsoft.bigdata.data.action;

import net.zdsoft.bigdata.data.entity.BiFavorite;
import net.zdsoft.bigdata.data.service.BiFavoriteService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/bigdata/favorite")
public class BiFavoriteController extends BigdataBaseAction {

	@Resource
	private BiFavoriteService biFavoriteService;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		List<BiFavorite> favoriteList = biFavoriteService
				.findBiFavoriteListByUserId(getLoginInfo().getUserId(), null);
		map.put("favoriteList", favoriteList);
		return "/bigdata/user/favorite/favoriteList.ftl";
	}

	@RequestMapping("/common/bi/index")
	public String biIndex(ModelMap map, HttpServletRequest request) {
		Pagination page = createPagination(request);
		page.setPageSize(10);
		List<BiFavorite> favoriteList = biFavoriteService
				.findBiFavoriteListByUserId(getLoginInfo().getUserId(), page);
		sendPagination(request, map,"2", page);
		map.put("favoriteList", favoriteList);
		return "/bigdata/user/favorite/bi/favoriteList4Bi.ftl";
	}

	@RequestMapping("/component")
	public String component(HttpServletRequest request, ModelMap map) {
		Pagination page = createPagination(request);
		page.setPageSize(6);
		List<BiFavorite> favoriteList = biFavoriteService
				.findBiFavoriteListByUserId(getLoginInfo().getUserId(), page);
		map.put("favoriteList", favoriteList);
		return "/bigdata/user/component/favorite.ftl";
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Response deleteFavorite(String id) {
		try {
			biFavoriteService.delete(id);
			String key = getLoginInfo().getUserId() + "-user-diy-stat";
			RedisUtils.del(key);
			return Response.ok().message("取消收藏成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/addFavorite")
	@ResponseBody
	public Response addFavorite(BiFavorite biFavorite) {
		try {
			String key = getLoginInfo().getUserId() + "-user-diy-stat";
			RedisUtils.del(key);
			List<BiFavorite> favorites = biFavoriteService
					.findBiFavoriteListByUserIdAndBusinessId(getLoginInfo()
							.getUserId(), biFavorite.getBusinessId());
			if (favorites.size() > 0) {
				return Response.error()
						.message("【" + biFavorite.getBusinessName() + "】已经收藏了")
						.build();
			}
			biFavorite.setUserId(getLoginInfo().getUserId());
			biFavoriteService.addBiFavorite(biFavorite);
			return Response.ok().message("收藏成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

}
