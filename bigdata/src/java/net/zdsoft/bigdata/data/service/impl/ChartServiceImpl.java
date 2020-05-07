package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.code.ChartClassificationUtils;
import net.zdsoft.bigdata.data.dao.ChartDao;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.*;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static net.zdsoft.bigdata.data.ChartBusinessType.CHART;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 下午3:56
 */
@Service
public class ChartServiceImpl extends BaseServiceImpl<Chart, String> implements ChartService {

    private Logger logger = LoggerFactory.getLogger(ChartServiceImpl.class);

    @Resource
    private ChartDao chartDao;
    @Resource
    private TagService tagService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private ReportTemplateService reportTemplateService;
    @Resource
    private ReportTermService reportTermService;
    @Resource
    private CockpitChartService cockpitChartService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
    private BigLogService bigLogService;

    @Override
    public long count(Date start, Date end, Integer type) {
        if (start == null && end == null) {
            return chartDao.count((Specification<Chart>) (root, criteriaQuery, criteriaBuilder)
                    -> criteriaQuery.where(criteriaBuilder.equal(root.get("businessType").as(Integer.class), type))
                    .getRestriction());
        } else {
            return chartDao.count((Specification<Chart>) (root, criteriaQuery, criteriaBuilder)
                    -> {
                Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
                Predicate t = criteriaBuilder.equal(root.get("businessType").as(Integer.class), type);
                return criteriaQuery.where(time, t).getRestriction();
            });
        }
    }

    @Override
    public Chart getChartPartOfCockpitById(String id) {
        return chartDao.getChartByIdAndBusinessType(id, ChartBusinessType.COCKPIT.getBusinessType());
    }

    @Override
    public List<Chart> getChartsByUnitId(String unitId) {
        if (StringUtils.isBlank(unitId)) {
            return Collections.emptyList();
        }
        return chartDao.getChartsByUnitId(unitId);
    }

    @Override
    public List<Chart> getChartsByUnitIdAndChartType(String unitId, Integer chartType) {
        return chartDao.getChartsByUnitIdAndChartType(unitId, chartType);
    }

    @Override
    public List<Chart> getChartsPartOfCockpitByIds(String... ids) {
        if (ids.length == 1) {
            return Collections.singletonList(getChartPartOfCockpitById(ids[0]));
        }
        return chartDao.getChartsByIdInAndBusinessType(ids, ChartBusinessType.COCKPIT.getBusinessType());
    }

    @Override
    public List<Chart> getChartsByUnitIdAndBusinessTypeAndSort(String unitId, int businessType) {
        return chartDao.getChartsByUnitIdAndBusinessType(unitId, businessType, new Sort(Sort.Direction.DESC, "modifyTime"));
    }

    @Override
    public List<Chart> getChartsByUnitIdAndBusinessTypeAndSort(String unitId, int businessType, Pagination pagination) {
        Page<Chart> page = chartDao.getChartsByUnitIdAndBusinessTypeOrderByModifyTime(unitId, businessType, pagination.toPageable());
        pagination.setMaxRowCount(page.getTotalPages());
        return page.getContent();
    }

    @Override
    public Page<Chart> getChartsByIds(String[] ids, Pageable pageable) {
        return chartDao.getChartsByIdInOrderByModifyTime(ids, pageable);
    }

    @Override
    public List<Chart> findByIds(String[] ids, Pagination pagination) {
        Page<Chart> page = chartDao.getAllByIdIn(ids, pagination.toPageable());
        pagination.setMaxRowCount((int) page.getTotalElements());
        pagination.initialize();
        return page.getContent();
    }

    @Override
    public void saveChartAngTags(Chart chart, List<String> tagNames) {
        //检查是否存在新增的tagName
        List<Tag> tags = tagService.findTagsByUnitIdAndTagTypeAndTagNames(chart.getUnitId(),
                CHART.getBusinessType().shortValue(), tagNames.toArray(new String[0]));
        Set<String> existTagNameSet = tags.stream().map(Tag::getTagName).collect(Collectors.toSet());
        Tag[] newTags = tagNames.stream().map(tagName -> {
            if (!existTagNameSet.contains(tagName)) {
                Tag tag = new Tag();
                tag.setCreationTime(new Date());
                tag.setTagName(tagName);
                tag.setTagType(CHART.getBusinessType().shortValue());
                tag.setUnitId(chart.getUnitId());
                tag.setId(UuidUtils.generateUuid());
                return tag;
            }
            return null;
        }).filter(Objects::nonNull).toArray(Tag[]::new);
        //新增Tag
        if (!ObjectUtils.isEmpty(newTags)) {
            if (logger.isDebugEnabled()) {
                logger.debug("新增Tag {}", Arrays.stream(newTags).map(Tag::getTagName).toArray());
            }
            tagService.saveAll(newTags);
        }

        tags.addAll(Arrays.asList(newTags));
        //建立关联关系
        //key=id value=Tag
        Map<String, Tag> tagMap = tags.parallelStream().collect(Collectors.toMap(Tag::getId, tag -> tag));
        List<TagRelation> tagRelations = tagRelationService.findTagRelationByBusinessId(chart.getId());
        //
        Map<String, TagRelation> tagRelationMap = tagRelations.stream()
                .collect(Collectors.toMap(TagRelation::getTagId, tagRelation -> tagRelation));

        TagRelation[] newTagRelations = tags.stream().map(tag -> {
            if (!tagRelationMap.containsKey(tag.getId())) {
                TagRelation tagRelation = new TagRelation();
                tagRelation.setBusinessId(chart.getId());
                tagRelation.setTagId(tag.getId());
                tagRelation.setTagName(tag.getTagName());
                tagRelation.setId(UuidUtils.generateUuid());
                tagRelation.setCreationTime(new Date());
                return tagRelation;
            }
            return null;
        }).filter(Objects::nonNull).toArray(TagRelation[]::new);
        TagRelation[] deleteTagRelations = tagRelations.stream()
                .filter(tagRelation -> !tagMap.containsKey(tagRelation.getTagId())).toArray(TagRelation[]::new);
        tagRelationService.deleteAll(deleteTagRelations);
        tagRelationService.saveAll(newTagRelations);
        //保存Chart
        chartDao.save(chart);
        //业务日志埋点  新增
        LogDto logDto=new LogDto();
        logDto.setBizCode("insert-chart");
        logDto.setDescription("数据图表"+chart.getName());
        logDto.setNewData(chart);
        logDto.setBizName("数据图表设计");
        bigLogService.insertLog(logDto);
    }

    @Override
    public void saveChart(Chart chart, String[] tags, String[] orderUnits, String[] orderUsers) {
        // 更新标签关系
        tagService.updateTagRelationByBusinessId(tags, chart.getId());
        //更新chart
        save(chart);
        //更新订阅关系
        //单位订阅
        subscribeService.addAuthorization(new String[]{chart.getId()}, orderUnits, orderUsers, chart.getUnitId(), chart.getOrderType());
        // 删除之前文件夹关系
        folderDetailService.deleteByBusinessId(chart.getId());
        // 保存文件夹关系
        FolderDetail folderDetail = new FolderDetail();
        folderDetail.setBusinessId(chart.getId());
        folderDetail.setBusinessName(chart.getName());
        if (chart.getChartType() == 0) {
            folderDetail.setBusinessType(ChartBusinessType.REPORT.getBusinessType().toString());
        } else {
            folderDetail.setBusinessType(ChartBusinessType.CHART.getBusinessType().toString());
        }
        folderDetail.setCreationTime(new Date());
        folderDetail.setOperatorId(chart.getOperatorId());
        folderDetail.setOrderId(chart.getOrderId());
        folderDetail.setFolderId(chart.getFolderId());
        folderDetail.setId(UuidUtils.generateUuid());
        folderDetail.setUnitId(chart.getUnitId());
        folderDetail.setOrderType(chart.getOrderType());
        folderDetailService.save(folderDetail);
    }

    @Override
    public List<Chart> getCurrentUserCharts(String userId, String unitId, String[] tagIds, String name, Pagination pagination, boolean editPage, boolean isReport, Boolean isForCockpit) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        name = "%" + name + "%";
        Page<Chart> page = null;
        Pageable pageable = pagination == null ? null : pagination.toPageable();
        if (editPage) {

            if (ArrayUtils.isNotEmpty(tagIds)) {
                if (isReport) {
                    page = chartDao.getCurrentUserEditReportCharts(unitId, tagIds, name, pageable);
                } else {
                    if (isForCockpit == null) {
                        page = chartDao.getCurrentUserEditGraphCharts(unitId, tagIds, name, pageable);
                    } else {
                        page = chartDao.getCurrentUserEditGraphCharts(unitId, tagIds, name, isForCockpit ? 1 : 0, pageable);
                    }
                }
            } else {
                if (isReport) {
                    page = chartDao.getCurrentUserEditReportCharts(unitId, name, pageable);
                } else {
                    if (isForCockpit == null) {
                        page = chartDao.getCurrentUserEditGraphCharts(unitId, name, pageable);
                    } else {
                        page = chartDao.getCurrentUserEditGraphCharts(unitId, name, isForCockpit ? 1 : 0, pageable);
                    }
                }
            }
        } else {
            if (ArrayUtils.isNotEmpty(tagIds)) {
                if (isReport) {
                    page = chartDao.getCurrentUserQueryReportChart(userId, unitId, tagIds, name, pageable);
                } else {
                    if (isForCockpit) {
                        page = chartDao.getCurrentUserQueryGraphChartsForCockpit(userId, unitId, tagIds, name, pageable);
                    } else {
                        page = chartDao.getCurrentUserQueryGraphCharts(userId, unitId, tagIds, name, pageable);
                    }
                }
            } else {
                if (isReport) {
                    page = chartDao.getCurrentUserQueryReportChart(userId, unitId, name, pageable);
                } else {
                    if (isForCockpit) {
                        page = chartDao.getCurrentUserQueryGraphChartsForCockpit(userId, unitId, name, pageable);
                    } else {
                        page = chartDao.getCurrentUserQueryGraphCharts(userId, unitId, name, pageable);
                    }
                }
            }
        }
        if (pagination != null) {
            pagination.setMaxRowCount((int) page.getTotalElements());
        }
        return page.getContent();
    }

    @Override
    public void deleteReport(String reportId, String templateId) {
        this.delete(reportId);

        // 删除模版
        reportTemplateService.delete(templateId);
        // 删除条件
        reportTermService.deleteByReportId(reportId);
        // 删除文件夹关联信息
        folderDetailService.deleteByBusinessId(reportId);
        // 删除订阅
        subscribeService.deleteByBusinessId(new String[]{reportId});
        // 删除标签关系
        tagRelationService.deleteByBusinessId(reportId);
    }

    @Transactional(
            transactionManager = "txManagerJap",
            rollbackFor = Throwable.class
    )
    @Override
    public void batchDelete(String[] chartIds) {
        /*
         * 删除和图表惯量的相关数据
         * 1、Tag
         * 2、Cockpit
         */
        tagRelationService.deleteByBusinessId(chartIds);
        cockpitChartService.deleteCockpitChartByChartId(chartIds);
        folderDetailService.deleteByBusinessIdIn(chartIds);
        chartDao.batchDelete(chartIds);
    }

    @Override
    public void deleteChart(String chartId) {
        /*
         * 删除和图表惯量的相关数据
         * 1、Tag
         * 2、Cockpit
         */
        tagRelationService.deleteByBusinessId(chartId);
        cockpitChartService.deleteCockpitChartByChartId(new String[]{chartId});
        // 删除文件夹关联信息
        folderDetailService.deleteByBusinessId(chartId);
        // 删除订阅
        subscribeService.deleteByBusinessId(new String[]{chartId});

        this.delete(chartId);
    }

    @Override
    public void saveReport(Chart chart, ReportTemplate reportTemplate, List<ReportTermTree> reportTermTrees, String[] tags, String[] orderUnits, String[] orderUsers) throws IOException {
        reportTemplateService.saveReportTemplate(reportTemplate);
        chart.setTemplateId(reportTemplate.getId());

        this.saveChart(chart, tags, orderUnits, orderUsers);
        reportTermService.saveReportTerms(reportTermTrees, chart.getId());
    }

    @Override
    public List<Chart> getTextChartByIds(String[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return chartDao.getTextChartsByIds(ids);
    }

    @Override
    public List<Chart> getOtherChartByIds(String[] ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return chartDao.getChartsByIdsAndChartTypes(ids,
                ChartClassificationUtils.getOtherChartType().toArray(new Integer[0]));
    }

    @Override
    protected BaseJpaRepositoryDao<Chart, String> getJpaDao() {
        return chartDao;
    }

    @Override
    protected Class<Chart> getEntityClass() {
        return Chart.class;
    }
}
