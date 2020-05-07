package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.UserProfileDao;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.service.UserProfileService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * Created by wangdongdong on 2018/7/9 13:37.
 */
@Service
public class UserProfileServiceImpl extends BaseServiceImpl<UserProfile, String> implements UserProfileService {

    @Resource
    private UserProfileDao userProfileDao;
    @Resource
	private MetadataService metadataService;
	@Resource
	private MetadataTableColumnService metadataTableColumnService;

    @Override
    protected BaseJpaRepositoryDao<UserProfile, String> getJpaDao() {
        return userProfileDao;
    }

    @Override
    protected Class<UserProfile> getEntityClass() {
        return UserProfile.class;
    }

	@Override
	public List<MetadataTableColumn> findMainCols(String userProfileCode) {
		List<MetadataTableColumn> cols = Lists.newArrayList();
		UserProfile pf = userProfileDao.findByCode(userProfileCode);
		if(pf==null||StringUtils.isBlank(pf.getBasicColumns()))return cols;
		String[] cls = pf.getBasicColumns().split(",");
		Set<String> bcols = new HashSet<>(Arrays.asList(cls));
		Metadata meta= metadataService.findByTableName(pf.getMainTableName());
		if(meta==null)return cols;
		cols =  metadataTableColumnService.findByMetadataId(meta.getId());
		return cols.stream().filter(c->bcols.contains(c.getColumnName())).collect(Collectors.toList());
	}
}
