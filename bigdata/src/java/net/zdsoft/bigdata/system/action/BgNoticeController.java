package net.zdsoft.bigdata.system.action;

import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.system.entity.BgNotice;
import net.zdsoft.bigdata.system.service.BgNoticeService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/bigdata/notice")
public class BgNoticeController extends BigdataBaseAction {

	@Autowired
	private BgNoticeService bgNoticeService;

	@Resource
	private BigLogService bigLogService;


	@RequestMapping("/index")
	public String index(ModelMap map) {
		List<BgNotice> noticeList = bgNoticeService.findNoticeList(null);
		map.put("noticeList", noticeList);
		return "/bigdata/system/notice/noticeList.ftl";
	}
	
	@RequestMapping("/component")
	public String component(HttpServletRequest request,ModelMap map) {
		Pagination page = createPagination(request);
		page.setPageSize(5);
		List<BgNotice> noticeList = bgNoticeService.findNoticeListByStatus(1,page);
		map.put("noticeList", noticeList);
		return "/bigdata/user/component/notice.ftl";
	}

	@RequestMapping("/add")
	public String add(ModelMap map) {
		BgNotice notice = new BgNotice();
		notice.setStatus(1);
		map.put("notice", notice);
		return "/bigdata/system/notice/noticeEdit.ftl";
	}

	@RequestMapping("/edit")
	public String edit(String id, ModelMap map) {
		BgNotice notice = bgNoticeService.findOne(id);
		map.put("notice", notice);
		return "/bigdata/system/notice/noticeEdit.ftl";
	}
	
	@RequestMapping("/preview")
	public String preview(String id, ModelMap map) {
		BgNotice notice = bgNoticeService.findOne(id);
		map.put("notice", notice);
		return "/bigdata/system/notice/noticePreview.ftl";
	}

	@RequestMapping("/save")
	@ResponseBody
	public Response save(BgNotice notice) {
		try {
			List<BgNotice> noticeList = bgNoticeService
					.findNoticeListByTitle(notice.getTitle());
			notice.setNoticeTypeName(getNoticeTypeName(notice.getNoticeType()));
			if (StringUtils.isBlank(notice.getId())) {
				if (CollectionUtils.isNotEmpty(noticeList)) {
					return Response.error().message("公告名称已经存在,请重新维护").build();
				}
				notice.setId(UuidUtils.generateUuid());
				notice.setCreationTime(new Date());
				notice.setModifyTime(new Date());
				bgNoticeService.save(notice);

				//业务日志埋点  新增
				LogDto logDto=new LogDto();
				logDto.setBizCode("insert-notice");
				logDto.setDescription("公告 "+notice.getTitle());
				logDto.setNewData(notice);
				logDto.setBizName("公告设置");
				bigLogService.insertLog(logDto);

				return Response.ok().message("保存成功").build();
			} else {
				if (CollectionUtils.isNotEmpty(noticeList)) {
					if (!noticeList.get(0).getId().equals(notice.getId())) {
						return Response.error().message("公告名称已经存在,请重新维护").build();
					}
				}
				notice.setModifyTime(new Date());

				BgNotice oldNotice = bgNoticeService.findOne(notice.getId());

				bgNoticeService.update(notice, notice.getId(), new String[] {
						"title", "noticeType", "noticeTypeName", "content",
						"status","modifyTime" });

				//业务日志埋点  修改
				LogDto logDto=new LogDto();
				logDto.setBizCode("update-notice");
				logDto.setDescription("公告 "+notice.getTitle());
				logDto.setOldData(oldNotice);
				logDto.setNewData(notice);
				logDto.setBizName("公告设置");
				bigLogService.updateLog(logDto);

				return Response.ok().message("保存成功").build();
			}

		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}
	
	@RequestMapping("/saveWithNoFresh")
	@ResponseBody
	public Response saveWithNoFresh(BgNotice notice) {
		try {
			List<BgNotice> noticeList = bgNoticeService
					.findNoticeListByTitle(notice.getTitle());
			notice.setNoticeTypeName(getNoticeTypeName(notice.getNoticeType()));
			if (StringUtils.isBlank(notice.getId())) {
				if (CollectionUtils.isNotEmpty(noticeList)) {
					return Response.error().message("公告名称已经存在,请重新维护").build();
				}
				notice.setId(UuidUtils.generateUuid());
				notice.setCreationTime(new Date());
				notice.setModifyTime(new Date());
				bgNoticeService.save(notice);

				//业务日志埋点  新增
				LogDto logDto=new LogDto();
				logDto.setBizCode("insert-notice");
				logDto.setDescription("公告 "+notice.getTitle());
				logDto.setNewData(notice);
				logDto.setBizName("公告设置");
				bigLogService.insertLog(logDto);

				return Response.ok().message(notice.getId()).build();
			} else {
				if (CollectionUtils.isNotEmpty(noticeList)) {
					if (!noticeList.get(0).getId().equals(notice.getId())) {
						return Response.error().message("公告名称已经存在,请重新维护").build();
					}
				}
				notice.setModifyTime(new Date());
				BgNotice oldNotice = bgNoticeService.findOne(notice.getId());
				bgNoticeService.update(notice, notice.getId(), new String[] {
						"title", "noticeType", "noticeTypeName", "content",
						"status","modifyTime" });
				//业务日志埋点  修改
				LogDto logDto=new LogDto();
				logDto.setBizCode("update-notice");
				logDto.setDescription("公告 "+notice.getTitle());
				logDto.setOldData(oldNotice);
				logDto.setNewData(notice);
				logDto.setBizName("公告设置");
				bigLogService.updateLog(logDto);

				return Response.ok().message(notice.getId()).build();
			}

		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/changeStatus")
	@ResponseBody
	public Response changeStatus(String id, Integer status) {
		try {
			BgNotice bgNotice = bgNoticeService.findOne(id);
			BgNotice notice = new BgNotice();
			EntityUtils.copyProperties(bgNotice,notice);
			notice.setStatus(status);

			bgNoticeService.update(notice, bgNotice.getId(),
					new String[] { "status" });

			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-noticeStatus");
			logDto.setDescription("公告 "+notice.getTitle()+" 的上下架状态");
			logDto.setOldData(bgNotice);
			logDto.setNewData(notice);
			logDto.setBizName("公告设置");
			bigLogService.updateLog(logDto);

			return Response.ok().message("修改成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Response deleteNotice(String id) {
		try {
			BgNotice oldNotice = bgNoticeService.findOne(id);
			bgNoticeService.delete(id);

			//业务日志埋点  删除
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-notice");
			logDto.setDescription("公告 "+oldNotice.getTitle());
			logDto.setBizName("公告设置");
			logDto.setOldData(oldNotice);
			bigLogService.deleteLog(logDto);

			return Response.ok().message("删除成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	private String getNoticeTypeName(int noticeType) {
		String noticeTypeName = null;
		switch (noticeType) {
		case 1:
			noticeTypeName = "发布";
			break;
		case 2:
			noticeTypeName = "升级";
			break;
		case 3:
			noticeTypeName = "安全";
			break;
		case 4:
			noticeTypeName = "新功能";
			break;
		default:
			noticeTypeName = "其他";
			break;
		}
		return noticeTypeName;
	}

}
