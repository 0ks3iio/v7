package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.dao.MetadataTableColumnDao;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class MetadataTableColumnServiceImpl extends BaseServiceImpl<MetadataTableColumn, String> implements MetadataTableColumnService {

    @Resource
    private MetadataTableColumnDao metadataTableColumnDao;

    @Resource
    private MetadataService metadataService;

    @Override
    protected BaseJpaRepositoryDao<MetadataTableColumn, String> getJpaDao() {
        return metadataTableColumnDao;
    }

    @Override
    protected Class<MetadataTableColumn> getEntityClass() {
        return MetadataTableColumn.class;
    }

    @Override
    public List<MetadataTableColumn> findByMetadataId(String metadataId) {
        return metadataTableColumnDao.findAllByMetadataIdOrderByOrderId(metadataId);
    }

    @Override
    public void deleteByMetadataId(String metadataId) {
        metadataTableColumnDao.deleteByMetadataId(metadataId);
    }

    @Override
    public void saveMetadataTableColumn(MetadataTableColumn metadataTableColumn) throws BigDataBusinessException {
        if (metadataTableColumn.getIsPrimaryKey() == null || metadataTableColumn.getIsPrimaryKey() == 0) {
            metadataTableColumn.setPrimaryType(null);
        } else {
            // 查询是否已存在主键
            List<MetadataTableColumn> listForPrimary = this.findListBy(new String[]{"isPrimaryKey", "metadataId"}, new String[]{"1", metadataTableColumn.getMetadataId()});
            if (listForPrimary.size() > 0) {
                if (StringUtils.isBlank(metadataTableColumn.getId())) {
                    throw new BigDataBusinessException("该元数据主键已存在!");
                }
                if (!listForPrimary.get(0).getId().equals(metadataTableColumn.getId())) {
                    throw new BigDataBusinessException("该元数据主键已存在!");
                }
            }

        }

        List<MetadataTableColumn> listForName = this.findListBy(new String[]{"columnName", "metadataId"}, new String[]{metadataTableColumn.getColumnName(), metadataTableColumn.getMetadataId()});
        metadataTableColumn.setModifyTime(new Date());
        if (StringUtils.isBlank(metadataTableColumn.getId())) {
            if (listForName.size() > 0) {
                throw new BigDataBusinessException("该列名已存在!");
            }
            metadataTableColumn.setCreationTime(new Date());
            metadataTableColumn.setId(UuidUtils.generateUuid());
            metadataTableColumnDao.save(metadataTableColumn);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(metadataTableColumn.getId())) {
                throw new BigDataBusinessException("该列名已存在!");
            }
        }

        Metadata md = metadataService.findOne(metadataTableColumn.getMetadataId());

        if (!"float".equals(metadataTableColumn.getColumnType()) && !"double".equals(metadataTableColumn.getColumnType()) && !"decimal".equals(metadataTableColumn.getColumnType())) {
            if ("kylin".equals(md.getDbType()) || "impala".equals(md.getDbType())) {
                if ("string".equals(metadataTableColumn.getColumnType())) {
                    metadataTableColumn.setColumnLength(null);
                }
            }
            metadataTableColumn.setDecimalLength(null);
        }
        metadataTableColumnDao.update(metadataTableColumn, new String[]{"name", "columnName", "columnType", "columnLength", "decimalLength", "statType", "remark", "orderId", "modifyTime", "isPrimaryKey", "primaryType", "columnFormat"});
    }

    @Override
    public List<MetadataTableColumn> findByColumnName(String columnName) {
        return metadataTableColumnDao.findByColumnName(columnName);
    }

    @Override
    public List<MetadataTableColumn> findAllModelUsed() {
        return metadataTableColumnDao.findAllModelUsed();
    }

    @Override
    public Integer getMaxOrderIdByMetadataId(String metadataId) {
        return metadataTableColumnDao.getMaxOrderIdByMetadataId(metadataId);
    }

    @Override
    public Optional<MetadataTableColumn> findById(String mdColumnId) {
        return metadataTableColumnDao.findById(mdColumnId);
    }
}
