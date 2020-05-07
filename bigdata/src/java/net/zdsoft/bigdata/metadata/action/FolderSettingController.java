package net.zdsoft.bigdata.metadata.action;

import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.entity.FolderType;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2019/1/28 14:23.
 */
@Controller
@RequestMapping(value = "/bigdata/folder")
public class FolderSettingController extends BigdataBaseAction {

    @Resource
    private FolderService folderService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private BigLogService bigLogService;

    @RequestMapping("/set")
    public String index(ModelMap map, String parentId) {

        List<FolderEx> folderTree = folderService.findFolderTree();
        map.put("folderTree", folderTree);
        return "/bigdata/setting/folder/index.ftl";
    }


    @RequestMapping("/folderEdit")
    public String folderEdit(String id, String parentId, ModelMap map) {
        Folder folder = StringUtils.isNotBlank(id) ? folderService.findOne(id) : new Folder();
        map.put("folder", folder);
        map.put("parentId", parentId);
        return "/bigdata/setting/folder/folderEdit.ftl";
    }

    @RequestMapping("/saveFolder")
    @ResponseBody
    public Response saveFolder(Folder folder) {
        try {
            Integer orderId = folderService.getMaxOrderIdByParentId(folder.getParentId());
            folder.setUserId(getLoginInfo().getUserId());
            folder.setUsage(2);
            folder.setStatus(1);
            folder.setOrderId(orderId+1);
            folderService.saveFolder(folder);

            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-folder");
            logDto.setDescription("内容目录 "+folder.getFolderName());
            logDto.setNewData(folder);
            logDto.setBizName("内容目录设置");
            bigLogService.insertLog(logDto);

            return Response.ok().data(folder.getId()).build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/modifyFolderName")
    @ResponseBody
    public Response modifyFolderName(Folder folder) {
        try {
            Folder oldFolder = folderService.findOne(folder.getId());
            folderService.updateFolderName(folder);

            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-folder");
            logDto.setDescription("名称为 "+folder.getFolderName());
            logDto.setOldData(oldFolder.getFolderName());
            logDto.setNewData(folder.getFolderName());
            logDto.setBizName("内容目录设置");
            bigLogService.updateLog(logDto);

            return Response.ok().data(folder.getId()).build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteDirectory")
    @ResponseBody
    public Response deleteDirectory(String folderId) {
        try {
            Folder oldFolder = folderService.findOne(folderId);
            folderService.deleteFolder(folderId, FolderType.DIRECTORY.getValue());

            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-directory");
            logDto.setDescription("目录 "+oldFolder.getFolderName());
            logDto.setBizName("内容目录设置");
            logDto.setOldData(oldFolder);
            bigLogService.deleteLog(logDto);

            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/deleteFolder")
    @ResponseBody
    public Response deleteFolder(String folderId) {
        try {
            Folder oldFolder = folderService.findOne(folderId);
            folderService.deleteFolder(folderId, FolderType.FOLDER.getValue());

            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-folder");
            logDto.setDescription("文件夹 "+oldFolder.getFolderName());
            logDto.setBizName("内容目录设置");
            logDto.setOldData(oldFolder);
            bigLogService.deleteLog(logDto);

            return Response.ok().build();
        } catch (BigDataBusinessException e) {
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/getMaxOrderId")
    @ResponseBody
    public Response getMaxOrderId(String folderId) {
        Integer orderId = folderDetailService.getMaxOrderIdByFolderId(folderId);
        return Response.ok().data(orderId + 1).build();
    }

    @RequestMapping("/getChildFolder")
    @ResponseBody
    public Response getChildFolder(String folderId) {
        return Response.ok().data(folderService.findAllFolderByParentId(folderId, FolderType.FOLDER.getValue())).build();
    }
}
