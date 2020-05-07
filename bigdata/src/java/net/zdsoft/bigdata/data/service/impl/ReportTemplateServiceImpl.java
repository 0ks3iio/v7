package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.ReportTemplateDao;
import net.zdsoft.bigdata.data.entity.ReportTemplate;
import net.zdsoft.bigdata.data.service.ReportTemplateService;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/5/10.
 */
@Service
public class ReportTemplateServiceImpl extends BaseServiceImpl<ReportTemplate, String> implements ReportTemplateService {

    @Resource
    private ReportTemplateDao reportTemplateDao;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;

    @Override
    public List<ReportTemplate> findReportTemplatesByUnitId(String unitId) {
        return reportTemplateDao.getReportTemplatesByUnitId(unitId);
    }

    @Override
    public void saveReportTemplate(ReportTemplate template) throws IOException {
        if (!template.getTemplateFileName().endsWith("jasper")) {
            throw new ControllerException("模版文件格式不正确!");
        }
        if (StringUtils.isBlank(template.getThumbnailPath())) {
            throw new ControllerException("缩略图不能为空!");
        }
        String systemFilePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        // 拷贝模版
        String templateTempPath = template.getTemplatePath();
        if (!templateTempPath.contains(template.getTemplateFileName())) {
            templateTempPath = templateTempPath + File.separator + template.getTemplateFileName();
        }
        String templatePath = "bigdata" + File.separator + "report"
                + File.separator + "template" + File.separator + template.getUnitId() + File.separator + template.getTemplateFileName();
        String templateRealPath = systemFilePath + File.separator + templatePath;
        copyFile(templateTempPath, templateRealPath);
        if (template.getThumbnailFileName() != null) {
            // 拷贝缩略图
            String thumbnailTempPath = template.getThumbnailPath();
            if (!thumbnailTempPath.contains(template.getThumbnailFileName())) {
                thumbnailTempPath = thumbnailTempPath + File.separator + template.getThumbnailFileName();
            }
            String thumbnailPath = "bigdata" + File.separator + "report"
                    + File.separator + template.getUnitId() + File.separator + template.getThumbnailFileName();
            String thumbnailRealPath = systemFilePath + File.separator + thumbnailPath;
            copyFile(thumbnailTempPath, thumbnailRealPath);
            template.setThumbnailPath(thumbnailPath);
        }

        template.setTemplatePath(templatePath);
        template.setModifyTime(new Date());
        if (StringUtils.isBlank(template.getId())) {
            template.setId(UuidUtils.generateUuid());
            template.setCreationTime(new Date());
            reportTemplateDao.save(template);
            return;
        }
        reportTemplateDao.update(template, template.getId(), new String[]{"templatePath", "templateFileName", "thumbnailFileName", "thumbnailPath"
                , "remark", "modifyTime"});
    }

    @Override
    public void deleteReportTemplate(String templateId, String unitId) {
        reportTemplateDao.deleteById(templateId);
    }

    private void copyFile(String tempPath, String realPath) throws IOException {
        if (tempPath.equalsIgnoreCase(realPath))
            return;
        File tempFile = new File(tempPath);
        File realFile = new File(realPath);
        if (tempFile.exists()) {
            FileUtils.copyFile(tempFile, realFile);
        }
    }

    @Override
    protected BaseJpaRepositoryDao<ReportTemplate, String> getJpaDao() {
        return reportTemplateDao;
    }

    @Override
    protected Class<ReportTemplate> getEntityClass() {
        return ReportTemplate.class;
    }
}
