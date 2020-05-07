package net.zdsoft.bigdata.stat.service.impl;

import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgDwRankService;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.stat.service.BgMetadataStatService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service("bgMetadataStatService")
public class BgMetadataStatServiceImpl implements BgMetadataStatService {

    private static Logger logger = LoggerFactory
            .getLogger(BgMetadataStatServiceImpl.class);

    @Autowired
    MetadataService metadataService;

    @Autowired
    MetadataRelationService metadataRelationService;

    @Autowired
    MysqlClientService mysqlClientService;

    @Autowired
    HbaseClientService hbaseClientService;

    @Autowired
    BgPropertyTopicService bgPropertyTopicService;

    @Autowired
    BgDwRankService bgDwRankService;

    @Autowired
    PhoenixClientService phoenixClientService;

    @Override
    public void metadataStatByDaily(Date today) {

        String tableName = "DWC_STAT_METADATA_DAILY";

        String insertSql = "upsert into DWC_STAT_METADATA_DAILY(ID, TOPIC_NAME, TOPIC_ID," +
                "WH_RANK_ID,WH_RANK_NAME,TOTAL_AMOUNT,DAILY_AMOUNT," +
                "API_AMOUNT,SOURCE_AMOUNT,TARGET_AMOUNT,DATA_QUALITY,STAT_TIME) " +
                "values('@ID','@TOPIC_NAME','@TOPIC_ID','@WH_RANK_ID','@WH_RANK_NAME','@TOTAL_AMOUNT'," +
                "'@DAILY_AMOUNT','@API_AMOUNT','@SOURCE_AMOUNT','@TARGET_AMOUNT','@DATA_QUALITY','@STAT_TIME')";

        List<Metadata> mdList = metadataService.findPropertyMetadata();

        if (today == null)
            today = DateUtils.addDay(new Date(), -1);

        Date yesturday = DateUtils.addDay(today, -1);

        List<String> resultSqls = new ArrayList<>();
        List<DwRank> rankList = bgDwRankService.findAll();
        Map<String, DwRank> rankMap = new HashMap<>();
        for (DwRank rank : rankList) {
            rankMap.put(rank.getId(), rank);
        }


        List<PropertyTopic> topicList = bgPropertyTopicService.findAll();
        Map<String, PropertyTopic> topicMap = new HashMap<>();
        for (PropertyTopic topic : topicList) {
            topicMap.put(topic.getId(), topic);
        }

        Map<String, Long> totalCountMap = new HashMap<>();

        Map<String, Long> dailyCountMap = new HashMap<>();


        for (Metadata md : mdList) {
            if (md.getIsProperty() == null || 0 == md.getIsProperty()) {
                continue;
            }

            String currentSql = insertSql;

            String rowkey = md.getId() + DateUtils.date2String(today, "yyyyMMdd");
            //主题名称
            currentSql = currentSql.replace("@TOPIC_NAME", topicMap.get(md.getPropertyTopicId()).getName());

            //主题Id
            currentSql = currentSql.replace("@TOPIC_ID", md.getPropertyTopicId());

            //层次ID
            currentSql = currentSql.replace("@WH_RANK_ID", md.getDwRankId());

            //层次名称
            currentSql = currentSql.replace("@WH_RANK_NAME", rankMap.get(md.getDwRankId()).getName());

            //接口数量
            Integer apiCount = metadataRelationService.countApiByMetadataId(md.getId());
            if (apiCount == null)
                apiCount = 0;
            currentSql = currentSql.replace("@API_AMOUNT", String.valueOf(apiCount));

            //数据提供应用数量:
            Integer sourceAppCount = metadataRelationService.countSourceAppByMetadataId(md.getId());
            if (sourceAppCount == null)
                sourceAppCount = 0;
            currentSql = currentSql.replace("@SOURCE_AMOUNT", String.valueOf(sourceAppCount));

            //数据使用应用数量:
            Integer targetAppCount = metadataRelationService.countTargetAppByMetadataId(md.getId());
            if (targetAppCount == null)
                targetAppCount = 0;
            currentSql = currentSql.replace("@TARGET_AMOUNT", String.valueOf(targetAppCount));

            //数据质量分数
            //TODO
            currentSql = currentSql.replace("@DATA_QUALITY", String.valueOf(""));

            //统计日期
            currentSql = currentSql.replace("@STAT_TIME", DateUtils.date2String(today, "yyyy-MM-dd"));

            Long totalCount = Long.valueOf(0);
            Long dailyCount = Long.valueOf(0);
            if ("hbase".equals(md.getDbType())) {
                //总数量
                String sql = "select count(1) as totalCount from " + md.getTableName();
                List<Json> countDataJson = phoenixClientService.getDataListFromPhoenix(null, sql, null);
                if (CollectionUtils.isNotEmpty(countDataJson)) {
                    totalCount = countDataJson.get(0).getLongValue("totalcount");
                }
                currentSql = currentSql.replace("@TOTAL_AMOUNT", String.valueOf(totalCount));

                //当天数据总量
                dailyCount = totalCount - getYesturdayData(md.getId(), tableName, yesturday);
                currentSql = currentSql.replace("@DAILY_AMOUNT", String.valueOf(dailyCount));

            } else if ("mysql".equals(md.getDbType())) {
                //总数量
                String sql = "select count(1) as num from " + md.getTableName();

                List<Json> totalCountResult = mysqlClientService.getDataListFromMysql(null, null, sql, null, null);
                if (CollectionUtils.isNotEmpty(totalCountResult)) {
                    totalCount = totalCountResult.get(0).getLong("num");
                }
                currentSql = currentSql.replace("@TOTAL_AMOUNT", String.valueOf(totalCount));

                //当天数据总量
                dailyCount = totalCount - getYesturdayData(md.getId(), tableName, yesturday);
                currentSql = currentSql.replace("@DAILY_AMOUNT", String.valueOf(dailyCount));
            }
            currentSql = currentSql.replace("@ID", rowkey);
            resultSqls.add(currentSql);

            //聚合统计 总和
            Long totalAll = totalCountMap.get("all");
            if (totalAll == null)
                totalAll = Long.valueOf(0);
            totalAll += totalCount;
            totalCountMap.put("all", totalAll);

            Long totalByRank2Topic = totalCountMap.get(md.getDwRankId() + md.getPropertyTopicId());
            if (totalByRank2Topic == null)
                totalByRank2Topic = Long.valueOf(0);
            totalByRank2Topic += totalCount;
            totalCountMap.put(md.getDwRankId() + md.getPropertyTopicId(), totalByRank2Topic);

            Long totalByTopic = totalCountMap.get(md.getPropertyTopicId());
            if (totalByTopic == null)
                totalByTopic = Long.valueOf(0);
            totalByTopic += totalCount;
            totalCountMap.put(md.getPropertyTopicId(), totalByTopic);


            //聚合统计 当日
            Long dailyAll = dailyCountMap.get("all");
            if (dailyAll == null)
                dailyAll = Long.valueOf(0);
            dailyAll += dailyCount;
            dailyCountMap.put("all", dailyAll);

            Long dailyByRank2Topic = dailyCountMap.get(md.getDwRankId() + md.getPropertyTopicId());
            if (dailyByRank2Topic == null)
                dailyByRank2Topic = Long.valueOf(0);
            dailyByRank2Topic += dailyCount;
            dailyCountMap.put(md.getDwRankId() + md.getPropertyTopicId(), dailyByRank2Topic);


            Long dailyByTopic = dailyCountMap.get(md.getPropertyTopicId());
            if (dailyByTopic == null)
                dailyByTopic = Long.valueOf(0);
            dailyByTopic += dailyCount;
            dailyCountMap.put(md.getPropertyTopicId(), dailyByTopic);
        }

        for (PropertyTopic topic : topicList) {
            for (DwRank rank : rankList) {
                String currentSql = "upsert into DWC_STAT_METADATA_DAILY(ID, TOPIC_NAME, TOPIC_ID," +
                        "WH_RANK_ID,WH_RANK_NAME,TOTAL_AMOUNT,DAILY_AMOUNT," +
                        "API_AMOUNT,SOURCE_AMOUNT,TARGET_AMOUNT,DATA_QUALITY,MD_AMOUNT,STAT_TIME) " +
                        "values('@ID','@TOPIC_NAME','@TOPIC_ID','@WH_RANK_ID','@WH_RANK_NAME','@TOTAL_AMOUNT'," +
                        "'@DAILY_AMOUNT','@API_AMOUNT','@SOURCE_AMOUNT','@TARGET_AMOUNT','@DATA_QUALITY','@MD_AMOUNT','@STAT_TIME')";

                String rowkey = rank.getId() + topic.getId() + DateUtils.date2String(today, "yyyyMMdd");
                //主题名称
                currentSql = currentSql.replace("@TOPIC_NAME", topic.getName());

                //主题Id
                currentSql = currentSql.replace("@TOPIC_ID", topic.getId());

                //层次ID
                currentSql = currentSql.replace("@WH_RANK_ID", rank.getId());

                //层次名称
                currentSql = currentSql.replace("@WH_RANK_NAME", rank.getName());

                Integer rtApiCount = metadataRelationService.countApiBydwRankIdAndPropertyTopicId(rank.getId(), topic.getId());
                if (rtApiCount == null)
                    rtApiCount = 0;
                currentSql = currentSql.replace("@API_AMOUNT", String.valueOf(rtApiCount));

                //数据提供应用数量:
                Integer rtSourceAppCount = metadataRelationService.countSourceAppBydwRankIdAndPropertyTopicId(rank.getId(), topic.getId());
                if (rtSourceAppCount == null)
                    rtSourceAppCount = 0;
                currentSql = currentSql.replace("@SOURCE_AMOUNT", String.valueOf(rtSourceAppCount));

                //数据使用应用数量:
                Integer rtTargetAppCount = metadataRelationService.countTargetAppBydwRankIdAndPropertyTopicId(rank.getId(), topic.getId());
                if (rtTargetAppCount == null)
                    rtTargetAppCount = 0;
                currentSql = currentSql.replace("@TARGET_AMOUNT", String.valueOf(rtTargetAppCount));

                //数据质量分数
                //TODO
                currentSql = currentSql.replace("@DATA_QUALITY", String.valueOf(""));

                //元数据数量
                Integer rtMdCount = metadataService.countByDwRankAndPropertyTopic(rank.getId(), topic.getId());
                if (rtMdCount == null)
                    rtMdCount = 0;
                currentSql = currentSql.replace("@MD_AMOUNT", String.valueOf(rtMdCount));

                //统计日期
                currentSql = currentSql.replace("@STAT_TIME", DateUtils.date2String(today, "yyyy-MM-dd"));

                //总数量
                Long totalCount = totalCountMap.get(rank.getId() + topic.getId());
                if (totalCount == null)
                    totalCount = Long.valueOf(0);
                currentSql = currentSql.replace("@TOTAL_AMOUNT", String.valueOf(totalCount));

                //当天数据总量
                Long dailyCount = dailyCountMap.get(rank.getId() + topic.getId());
                if (dailyCount == null)
                    dailyCount = Long.valueOf(0);
                currentSql = currentSql.replace("@DAILY_AMOUNT", String.valueOf(dailyCount));

                currentSql = currentSql.replace("@ID", rowkey);
                resultSqls.add(currentSql);
            }
            String currentTopicSql = "upsert into DWC_STAT_METADATA_DAILY(ID, TOPIC_NAME, TOPIC_ID," +
                    "TOTAL_AMOUNT,DAILY_AMOUNT," +
                    "API_AMOUNT,SOURCE_AMOUNT,TARGET_AMOUNT,DATA_QUALITY,MD_AMOUNT,STAT_TIME) " +
                    "values('@ID','@TOPIC_NAME','@TOPIC_ID','@TOTAL_AMOUNT'," +
                    "'@DAILY_AMOUNT','@API_AMOUNT','@SOURCE_AMOUNT','@TARGET_AMOUNT','@DATA_QUALITY','@MD_AMOUNT','@STAT_TIME')";

            String topicRowkey = topic.getId() + DateUtils.date2String(today, "yyyyMMdd");
            //主题名称
            currentTopicSql = currentTopicSql.replace("@TOPIC_NAME", topic.getName());

            //主题Id
            currentTopicSql = currentTopicSql.replace("@TOPIC_ID", topic.getId());

            Integer tApiCount = metadataRelationService.countApiByPropertyTopicId(topic.getId());
            if (tApiCount == null)
                tApiCount = 0;
            currentTopicSql = currentTopicSql.replace("@API_AMOUNT", String.valueOf(tApiCount));

            //数据提供应用数量:
            Integer tSourceAppCount =metadataRelationService.countSourceAppByPropertyTopicId(topic.getId());
            if(tSourceAppCount ==null)
                tSourceAppCount=0;
            currentTopicSql = currentTopicSql.replace("@SOURCE_AMOUNT", String.valueOf(tSourceAppCount));

            //数据使用应用数量:
            Integer tTargetAppCount =metadataRelationService.countTargetAppByPropertyTopicId(topic.getId());
            if(tTargetAppCount ==null)
                tTargetAppCount=0;
            currentTopicSql = currentTopicSql.replace("@TARGET_AMOUNT", String.valueOf(tTargetAppCount));

            //数据质量分数
            //TODO
            currentTopicSql = currentTopicSql.replace("@DATA_QUALITY", String.valueOf(""));

            //元数据数量
            Integer tMdCount =metadataService.countByPropertyTopic(topic.getId());
            if(tMdCount ==null)
                tMdCount=0;
            currentTopicSql = currentTopicSql.replace("@MD_AMOUNT", String.valueOf(tMdCount));

            //统计日期
            currentTopicSql = currentTopicSql.replace("@STAT_TIME", DateUtils.date2String(today, "yyyy-MM-dd"));

            //总数量
            Long tTotalCount = totalCountMap.get(topic.getId());
            if (tTotalCount == null)
                tTotalCount = Long.valueOf(0);
            currentTopicSql = currentTopicSql.replace("@TOTAL_AMOUNT", String.valueOf(tTotalCount));

            //当天数据总量
            Long dailyCount = dailyCountMap.get(topic.getId());
            if (dailyCount == null)
                dailyCount = Long.valueOf(0);
            currentTopicSql = currentTopicSql.replace("@DAILY_AMOUNT", String.valueOf(dailyCount));

            currentTopicSql = currentTopicSql.replace("@ID", topicRowkey);
            resultSqls.add(currentTopicSql);
        }

        String totalRowkEy = "00000000000000000000000000000000" + DateUtils.date2String(today, "yyyyMMdd");
        String totalCountSql = "upsert into DWC_STAT_METADATA_DAILY(ID,TOTAL_AMOUNT,DAILY_AMOUNT," +
                "API_AMOUNT,SOURCE_AMOUNT,TARGET_AMOUNT,DATA_QUALITY,MD_AMOUNT,STAT_TIME) " +
                "values('@ID','@TOTAL_AMOUNT'," +
                "'@DAILY_AMOUNT','@API_AMOUNT','@SOURCE_AMOUNT','@TARGET_AMOUNT','@DATA_QUALITY','@MD_AMOUNT','@STAT_TIME')";

        Integer allApiCount = metadataRelationService.countAllApi();
        if (allApiCount == null)
            allApiCount = 0;
        totalCountSql = totalCountSql.replace("@API_AMOUNT", String.valueOf(allApiCount));

        //数据提供应用数量:
        Integer allSourceAppCount = metadataRelationService.countAllSourceApp();
        if (allSourceAppCount == null)
            allSourceAppCount = 0;
        totalCountSql = totalCountSql.replace("@SOURCE_AMOUNT", String.valueOf(allSourceAppCount));

        //数据使用应用数量:
        Integer allTargetAppCount = metadataRelationService.countAllTargetApp();
        if (allTargetAppCount == null)
            allTargetAppCount = 0;
        totalCountSql = totalCountSql.replace("@TARGET_AMOUNT", String.valueOf(allTargetAppCount));

        //数据质量分数
        //TODO
        totalCountSql = totalCountSql.replace("@DATA_QUALITY", String.valueOf(""));

        //元数据数量
        Integer totalMdCount = metadataService.countAllMetadata();
        if (totalMdCount == null)
            totalMdCount = 0;
        totalCountSql = totalCountSql.replace("@MD_AMOUNT", String.valueOf(totalMdCount));

        //统计日期
        totalCountSql = totalCountSql.replace("@STAT_TIME", DateUtils.date2String(today, "yyyy-MM-dd"));

        //总数量
        Long totalCount = totalCountMap.get("all");
        if (totalCount == null)
            totalCount = Long.valueOf(0);
        totalCountSql = totalCountSql.replace("@TOTAL_AMOUNT", String.valueOf(totalCount));

        //当天数据总量
        Long dailyCount = dailyCountMap.get("all");
        if (dailyCount == null)
            dailyCount = Long.valueOf(0);
        totalCountSql = totalCountSql.replace("@DAILY_AMOUNT", String.valueOf(dailyCount));

        totalCountSql = totalCountSql.replace("@ID", totalRowkEy);
        resultSqls.add(totalCountSql);

        try {
            phoenixClientService.upsert(resultSqls);
        } catch (SQLException e) {
            logger.error("元数据统计失败:" + e.getMessage());
        }
    }

    private Long getYesturdayData(String mdId, String tableName, Date yesturday) {
        String yesturdayRowkey = mdId + DateUtils.date2String(yesturday, "yyyyMMdd");
        String sql = "select TOTAL_AMOUNT as totalcount from " + tableName + " where ID ='" + yesturdayRowkey + "'";
        List<Json> yesturdayCountDataJson = phoenixClientService.getDataListFromPhoenix(null, sql, null);
        int i = 0;
        Date _yesturday = yesturday;
        while (CollectionUtils.isEmpty(yesturdayCountDataJson)) {
            i++;
            if (i == 10) {
                break;
            }
            _yesturday = DateUtils.addDay(_yesturday, -1);
            yesturdayRowkey = mdId + DateUtils.date2String(_yesturday, "yyyyMMdd");
            sql = "select TOTAL_AMOUNT as totalcount from " + tableName + " where ID ='" + yesturdayRowkey + "'";
            yesturdayCountDataJson = phoenixClientService.getDataListFromPhoenix(null, sql, null);
        }

        Long yesturdayTotalCount = Long.valueOf(0);
        if (CollectionUtils.isNotEmpty(yesturdayCountDataJson)) {
            yesturdayTotalCount = yesturdayCountDataJson.get(0).getLongValue("totalcount");
        }

        return yesturdayTotalCount;
    }

}
