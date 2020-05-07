package net.zdsoft.dataimport.biz;

import net.zdsoft.dataimport.cache.ReplyCache;
import net.zdsoft.dataimport.cache.ViewCache;
import net.zdsoft.dataimport.process.ExcutorHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2017.08.01
 */
public abstract class AbstractImportAction {

    protected Logger logger = LoggerFactory.getLogger(AbstractImportAction.class);

    @Autowired protected ExcutorHolder excutorHolder;
    @Autowired private ViewCache viewCache;
    @Autowired private ReplyCache replyCache;

    @RequestMapping(value = "import/index")
    public Object importIndex() {

        return createMV("/dataImport/index.ftl");
    }

    @ResponseBody
    @RequestMapping(value = "import/upload")
    public Object importUpload(@RequestParam("file") MultipartFile excel) {

        //任务模式
        if ( hasTask() ) {
            //TODO how

            return success("文件上传成功，导入进度请在导入文件列表中查看");
        }
        //非任务模式
        else {
            //将文件拷贝到指定目录下 FILE_PATH/temp/uuid.xls or xlsx
            String uuid = UUID.randomUUID().toString();
            String tempDir = getTempDir() ;
            String newFile = tempDir + uuid + getFileEx(excel.getOriginalFilename());
            try {
                excel.transferTo(new File(newFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //执行非任务导入
            Reply reply = getImportBiz().execute(newFile, uuid);
            ImportRecord importRecord = new ImportRecord();
            importRecord.setCacheId(uuid);
            importRecord.setCreationTime(new Date());
            importRecord.setStateCode(ImportState.WAIT.getCode());
            importRecord.setOriginFilename(excel.getOriginalFilename());
            viewCache.add("userId", importRecord);
            //reply
            replyCache.put(uuid, reply);
            JSONResponse response = success("导入成功");
            response.setBusinessValue(uuid);
            return response;
        }
    }

    String getFileEx(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @RequestMapping(value = "/import/template/page")
    public Object importTemplate() {
        return createMV("/dataImport/template.ftl")
                .addObject("datas", getImportBiz().templates());
    }

    @ResponseBody
    @RequestMapping(value = "import/download", produces = "application/data;charset=UTF-8")
    public Object importDownload(HttpServletResponse response, String header) {
        File file = null;
        try {
            Workbook workbook = getImportBiz().exportTemplate(Arrays.stream(StringUtils.split(header, ",")).collect(Collectors.toList()));
            response.setHeader("Content-Disposition", "attachment; filename=test.xls");
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            return null;
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "import/viewData")
    public Object importViewData(@RequestParam("cacheId") String cacheId) {
        Reply reply = replyCache.get(cacheId);
        ModelAndView mv = createMV("/dataImport/viewData.ftl");
        try {
            mv.addObject("headers", reply.getHeaders());
            mv.addObject("errorObjects", reply.getErrorObjects());
            mv.addObject("javaObjects", reply.getJavaObjects());
            mv.addObject("reply", reply);
        } catch (Exception e) {
            e.printStackTrace();
            return error("解析Excel数据失败，请联系系统管理员！");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "import/cancel")
    public Object importCancel(@RequestParam("cacheId") String cacheId) {
        Reply reply = replyCache.get(cacheId);
        try {
            //TODO
        } catch (Exception e) {
            logger.error("cancel import error {}", e);
        } finally {
            replyCache.remove(cacheId); // remove之后页面切勿再次取值，否则可能造成状态错误
        }
        return success("操作成功");
    }

    //页面修改之后回调 校验
    @RequestMapping(value = "import/verify")
    public Object importVerify(Object value, String header) {
        getImportBiz().checkForValid((QImportEntity) value, header);
        if ( ((QImportEntity)value).createQImportError().isHasError() ) {
            //错误 FIXME 错误数据
            return error("校验失败");
        }
        return success("校验成功");
    }

    @ResponseBody
    @RequestMapping(value = "import/confirm")
    public Object importConfirm(String cacheId) {
        Reply reply = replyCache.get(cacheId);
        reply.clearGlobeError();
        if ( getImportBiz().importConfirm(reply) ){
            return success("导入成功");
        } else {
            //TODO 区分可修改性错误，数据库错误
            return error("导入失败"); //页面打开数据展示页面，展示错误数据，用户修改
        }
    }

    @RequestMapping(value = "/import/export/template")
    public Object exportTemplate() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "import/list/page")
    public Object importList(String userId) {
        List<ImportRecord> importRecordList = viewCache.getFromCache(userId);
        if ( importRecordList != null ) {
            for (ImportRecord importRecord : importRecordList) {
                if ( importRecord.getStateCode() == ImportState.WAIT.getCode() || importRecord.getStateCode() == ImportState.ING.getCode()) {
                    ImportState importState = replyCache.getState(importRecord.getCacheId());
                    if ( importState != null)
                        viewCache.update(userId, importState, importRecord);
                }
            }
        }
        Optional<List<ImportRecord>> records = Optional.ofNullable(importRecordList);
        return createMV("/dataImport/records.ftl").addObject("importRecords", importRecordList);
    }

    protected abstract AbstractImportBiz getImportBiz();

    protected abstract boolean hasTask();

    protected String getTempDir() {
        //获取当前环境缓存目录
        String tmpDir = System.getProperty("java.io.tmpdir");
        if ( StringUtils.isBlank(tmpDir) ) {
            throw new RuntimeException("can not get cache dir");
        }
        tmpDir = tmpDir + "import" + File.separator + "temp" + File.separator;
        File dir = new File(tmpDir);
        if ( !dir.exists() ) {
            boolean mkdirs = dir.mkdirs();
            if ( !mkdirs ) {
                throw new IllegalArgumentException("无法创建临时目录，请重新指定目录或者给予写权限");
            }
        }
        return tmpDir;
    }

    protected ModelAndView createMV(String viewName) {
        return new ModelAndView(viewName);
    }

    protected JSONResponse error(String msg) {
        return success(msg).setSuccess(Boolean.FALSE);
    }

    protected JSONResponse success(String msg) {
        JSONResponse response = new JSONResponse();
        response.setMsg(msg).setSuccess(Boolean.TRUE);
        return response;
    }
}
