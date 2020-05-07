package net.zdsoft.bigdata.extend.data.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.service.UserProfileService;
import net.zdsoft.bigdata.extend.data.service.UserTagService;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

/**
 * Created by wangdongdong on 2018/8/22 16:03.
 */
@Service
public class GroupAnalysisBiz {

    @Resource
    private UserTagService userTagService;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private EsClientService esClientService;
    @Resource
    private KylinClientService kylinClientService;

    /**
     * 查询标签结果
     *
     * @param profileCode
     * @param tagArray
     * @param title
     * @return
     */
    public List<Json> queryTagResult(String profileCode, String tagArray,
                                     String title) {
        String[] tagIds = JSON.parseArray(tagArray).toArray(new String[]{});
        List<UserTag> userTags = userTagService.findListByIdIn(tagIds);
        List<UserProfile> userProfiles = userProfileService.findListBy("code",
                profileCode);
        UserProfile userProfile = userProfiles.get(0);
        userProfile.setAggEngine("es");
        if ("es".equals(userProfile.getAggEngine())) {
            return this.queryAggResultFromEs(userProfiles.get(0), userTags,
                    title);
        } else {
            String sql = this.builderQuerySql(profileCode, userTags, title);
            return this.queryFromKylin(sql, userProfiles.get(0)
                            .getProjectName(), new String[]{"key", "value"},
                    new String[]{"string", "string"});
        }
    }

    /**
     * es 聚合页 查询数据
     *
     * @param pageIndex
     * @param userTags
     * @return
     */
    private List<Json> queryAggResultFromEs(UserProfile userProfile,
                                            List<UserTag> userTags, String groupByName) {
    	List<UserTag> firstTagList = userTagService
				.getfirstUserTagByProfileCode(userProfile.getCode());
    	Map<String,UserTag> colMap = EntityUtils.getMap(firstTagList, UserTag::getId);
    	userTags.forEach(e -> {if(colMap.containsKey(e.getParentId()))e.setTargetColumn(colMap.get(e.getParentId()).getTargetColumn());});
        Map<String, List<UserTag>> columnMap = userTags.stream().collect(
                Collectors.groupingBy(UserTag::getTargetColumn));
        List<Json> paramList = new ArrayList<>();
        columnMap.forEach((k, v) -> {
            Json queryParam = new Json();
            queryParam.put("type", "must");
            queryParam.put("field", k);
            String value = "";
            for (UserTag userTag : v) {
                value = value + userTag.getTagName() + ";";
            }
            value = value.substring(0, value.length() - 1);
            queryParam.put("value", value);
            paramList.add(queryParam);
        });
        List<Json> aggParamList = new ArrayList<Json>();
        Json aggParam = new Json();
        aggParam = new Json();
        aggParam.put("field", groupByName);
        aggParamList.add(aggParam);

        return esClientService.queryAggregation(userProfile.getIndexName(),
                userProfile.getTypeName(), null, null, aggParamList, paramList);
    }

    /**
     * 组装sql
     *
     * @param profileCode
     * @param userTags
     * @param typeColumnName
     * @return
     */
    private String builderQuerySql(String profileCode, List<UserTag> userTags,
                                   String typeColumnName) {
        StringBuilder sql = new StringBuilder("select ")
                .append(typeColumnName)
                .append(" as dimension,count(1) as dimension_count from kylin_")
                .append(profileCode).append("_tag ");

        if (userTags.size() > 0) {
            sql.append("where ");
            int i = 1;
            Map<String, List<UserTag>> columnMap = userTags.stream().collect(
                    Collectors.groupingBy(UserTag::getTargetColumn));
            for (Map.Entry<String, List<UserTag>> entry : columnMap.entrySet()) {
                int j = 1;
                for (UserTag tag : entry.getValue()) {
                    sql.append(tag.getTargetColumn()).append(" = ").append("'")
                            .append(tag.getTagName()).append("'");
                    if (j++ < entry.getValue().size()) {
                        sql.append(" or ");
                    }
                }
                if (i++ < columnMap.size()) {
                    sql.append(" and ");
                }
            }
        }
        return sql.append(" group by ").append(typeColumnName).toString();
    }

    /**
     * 查询
     *
     * @param sql
     * @param projectName
     * @param fields
     * @param dataTypes
     * @return
     */
    private synchronized List<Json> queryFromKylin(String sql,
                                                   String projectName, String[] fields, String[] dataTypes) {
        List<Json> resultFieldList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Json fieldJson = new Json();
            fieldJson.put("field", fields[i]);
            fieldJson.put("dataType", dataTypes[i]);
            resultFieldList.add(fieldJson);
        }
        List<Json> kylinJsonList = kylinClientService.getDataListFromKylin(
                projectName, sql, new ArrayList<>(), resultFieldList);
        List<Json> resultList = new ArrayList<>();
        for (Json originalJson : kylinJsonList) {
            Json resultJson = new Json();
            if (StringUtils.isBlank(originalJson.getString("dimension")) || "null".equals(originalJson.getString("dimension"))) {
                resultJson.put("key", "未知");
            } else {
                resultJson.put("key", originalJson.getString("dimension"));
            }
            resultJson.put("value", originalJson.getString("dimension_count"));
            resultList.add(resultJson);
        }
        return resultList;
    }
}
