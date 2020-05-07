package net.zdsoft.bigdata.frame.data.elastic;

import net.zdsoft.bigdata.daq.data.component.BigSqlAnalyseLogController;
import net.zdsoft.bigdata.daq.data.entity.BigSqlAnalyseLog;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Lazy(false)
@Service("esClientService")
public class EsClientServiceImpl implements EsClientService {

    private static Logger logger = LoggerFactory
            .getLogger(EsClientServiceImpl.class);

    private static Object lock = new Object();

    private static TransportClient client = null;

    @Autowired
    private OptionService optionService;

    @PostConstruct
    public void initClient() {
        OptionDto esDto = optionService.getAllOptionParam("es");
        if (esDto == null || esDto.getStatus() == 0) {
            return;
        }
        if (client == null) {
            createClient(esDto);
        }
    }

    private void createClient(OptionDto esDto) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        String HOST = esDto.getFrameParamMap().get("elastic_host");
        Integer PORT = Integer.valueOf(esDto.getFrameParamMap().get(
                "elastic_port"));
        String CLUSTER_NAME = esDto.getFrameParamMap().get(
                "elastic_cluster_name");
        String clientTransportSniff = esDto.getFrameParamMap().get(
                "client_transport_sniff");
        if (StringUtils.isBlank(CLUSTER_NAME))
            clientTransportSniff = "false";
        Settings settings = Settings
                .builder()
                .put("cluster.name", CLUSTER_NAME)
                .put("client.transport.ignore_cluster_name", false)
                .put("client.transport.sniff",
                        BooleanUtils.toBoolean(clientTransportSniff)).build();
        try {
            synchronized (lock) {
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress
                                .getByName(HOST), PORT));
                logger.info("初始化ES成功");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("初始化ES失败:" + e.getMessage());
        }
    }

    public void close() {
        client.close();
    }

    public void insertDatas(String index, String type, List<String> datas) {
        try {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            int count = 1;
            for (String data : datas) {
                bulkRequest.add(client.prepareIndex(index, type).setSource(
                        data, XContentType.JSON));
                // 每100条提交一次
                if (count % 100 == 0) {
                    bulkRequest.execute().actionGet();
                }
                count++;
            }
            // 不满100条的记录最后提交
            bulkRequest.execute().actionGet();
            logger.info("es成功写入" + datas.size() + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("es成功失败:" + e.getMessage());
        }
    }

    public List<Json> queryAggregation(String index, String type,
                                       Json dateAggParam, Json dateRangeParam, List<Json> aggList,
                                       List<Json> paramList) {
        Map<String, String> aggParamMap = new HashMap<String, String>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(aggList))
            aggList.forEach(e -> aggParamMap.put(e.getString("field"),
                    e.getString("serials")));

        SearchRequestBuilder responsebuilder = client.prepareSearch(index)
                .setTypes(type);
        // 时间范围的设定
        RangeQueryBuilder rangeQueryBuilder = null;
        if (dateRangeParam != null) {
            rangeQueryBuilder = QueryBuilders
                    .rangeQuery(dateRangeParam.getString("field"))
                    .from(dateRangeParam.getString("start_date"))
                    .to(dateRangeParam.getString("end_date"));
            responsebuilder = responsebuilder.setQuery(rangeQueryBuilder);
        }
        if (CollectionUtils.isNotEmpty(paramList)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            for (Json param : paramList) {
                BoolQueryBuilder tempQueryBuilder = QueryBuilders.boolQuery();
                String[] values = param.getString("value").split(";");
                if (values.length > 1) {
                    for (String value : values) {
                        tempQueryBuilder.should(QueryBuilders.termQuery(
                                param.getString("field"), value));
                    }
                    switch (param.getString("type")) {
                        case "must":
                            queryBuilder.must(tempQueryBuilder);
                            break;
                        case "mustNot":
                            queryBuilder.mustNot(tempQueryBuilder);
                            break;
                        case "should":
                            queryBuilder.should(tempQueryBuilder);
                            break;
                        default:
                            break;
                    }

                } else {
                    switch (param.getString("type")) {
                        case "must":
                            queryBuilder = queryBuilder.must(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        case "mustNot":
                            queryBuilder = queryBuilder.mustNot(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        case "should":
                            queryBuilder = queryBuilder.should(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        default:
                            break;
                    }
                }
            }

            if (queryBuilder != null) {
                responsebuilder = responsebuilder.setQuery(queryBuilder);
            }
        }
        // responsebuilder=responsebuilder.addSort("age", SortOrder.ASC);
        DateHistogramAggregationBuilder dateAgg = null;
        if (dateAggParam != null) {
            dateAgg = AggregationBuilders.dateHistogram("dateAgg").field(
                    dateAggParam.getString("field"));
            // 统计周期
            // year:年，quarter:季度，month:月，week:周，day:日，hour:小时，miunte：分钟，second:秒
            switch (dateAggParam.getString("interval")) {
                case "year":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.YEAR);
                    dateAgg.format("yyyy");
                    break;
                case "quarter":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.QUARTER);
                    dateAgg.format("yyyy-MM");
                    break;
                case "month":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.MONTH);
                    dateAgg.format("yyyy-MM");
                    break;
                case "week":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.WEEK);
                    dateAgg.format("yyyy-MM-dd");
                    break;
                case "day":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
                    dateAgg.format("MM-dd");
                    break;
                case "hour":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.HOUR);
                    dateAgg.format("yyyy-MM-dd HH");
                    break;
                case "miunte":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.MINUTE);
                    dateAgg.format("yyyy-MM-dd HH:mm");
                    break;
                case "second":
                    dateAgg.dateHistogramInterval(DateHistogramInterval.SECOND);
                    dateAgg.format("yyyy-MM-dd HH:mm:ss");
                    break;
                default:
                    break;
            }
        }

        if (CollectionUtils.isNotEmpty(aggList)) {
            for (Json aggParm : aggList) {
                AggregationBuilder paramAgg = AggregationBuilders
                        .terms(aggParm.getString("field"))
                        .field(aggParm.getString("field")).size(50)
                        .order(BucketOrder.count(false));
                if (dateAgg != null) {
                    dateAgg.subAggregation(paramAgg);
                } else {
                    responsebuilder = responsebuilder.addAggregation(paramAgg);
                }
            }
        }
        if (dateAgg != null)
            responsebuilder = responsebuilder.addAggregation(dateAgg);

        SearchResponse response = responsebuilder.setExplain(true).execute()
                .actionGet();
        List<Json> resultList = new ArrayList<Json>();
        if (dateAgg != null) {
            Map<String, Aggregation> aggMap = response.getAggregations()
                    .asMap();
            InternalDateHistogram idh = (InternalDateHistogram) aggMap
                    .get("dateAgg");
            List<org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket> buckets = idh
                    .getBuckets();
            for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket b : buckets) {
                Aggregations subAgg = b.getAggregations();
                if (CollectionUtils.isNotEmpty(aggList)) {
                    for (Json aggParm : aggList) {
                        StringTerms termsNameTerms = subAgg.get(aggParm
                                .getString("field"));
                        Iterator<Bucket> termsBucketIt = termsNameTerms
                                .getBuckets().iterator();
                        while (termsBucketIt.hasNext()) {
                            Bucket termBucket = termsBucketIt.next();
                            Json result = new Json();
                            switch (dateAggParam.getString("interval")) {
                                case "quarter":
                                    result.put("key", DateUtils.getSeason(
                                            DateUtils.string2Date(
                                                    b.getKeyAsString(), "yyyy-MM"),
                                            true));
                                    break;
                                case "week":
                                    result.put("key", DateUtils
                                            .getWeekBeginAndEndDate(DateUtils
                                                            .string2Date(
                                                                    b.getKeyAsString(),
                                                                    "yyyy-MM-dd"),
                                                    "yyyy-MM-dd"));
                                    break;
                                default:
                                    result.put("key", b.getKeyAsString());
                                    break;
                            }

                            result.put("serials", termBucket.getKey());
                            result.put("name", aggParm.getString("name"));
                            result.put("value", termBucket.getDocCount());
                            resultList.add(result);
                        }
                    }
                } else {
                    Json result = new Json();
                    switch (dateAggParam.getString("interval")) {
                        case "quarter":
                            result.put("key", DateUtils.getSeason(DateUtils
                                            .string2Date(b.getKeyAsString(), "yyyy-MM"),
                                    true));
                            break;
                        case "week":
                            result.put("key", DateUtils.getWeekBeginAndEndDate(
                                    DateUtils.string2Date(b.getKeyAsString(),
                                            "yyyy-MM-dd"), "yyyy-MM-dd"));
                            break;
                        default:
                            if (StringUtils.isBlank(b.getKeyAsString())
                                    || "null".equals(b.getKeyAsString())) {
                                result.put("key", "未知");
                            } else {
                                result.put("key", b.getKeyAsString());

                            }
                            break;
                    }
                    result.put("serials", dateAggParam.getString("name"));
                    result.put("name", dateAggParam.getString("name"));
                    result.put("value", b.getDocCount());
                    resultList.add(result);
                }
            }
        } else {
            List<Aggregation> aggResultList = response.getAggregations()
                    .asList();
            Map<String, Aggregation> aggResultMap = response.getAggregations()
                    .asMap();
            for (Aggregation aggResult : aggResultList) {
                StringTerms terms = (StringTerms) aggResultMap.get(aggResult
                        .getName());
                Iterator<Bucket> termsBucketIt = terms.getBuckets().iterator();
                while (termsBucketIt.hasNext()) {
                    Bucket termBucket = termsBucketIt.next();
                    Json result = new Json();
                    if (StringUtils.isBlank(termBucket.getKeyAsString())
                            || "null".equals(termBucket.getKeyAsString())) {
                        result.put("key", "未知");
                    } else {
                        result.put("key", termBucket.getKeyAsString());

                    }
                    result.put("serials", aggParamMap.get(terms.getName()));
                    result.put("name", aggParamMap.get(terms.getName()));
                    result.put("value", termBucket.getDocCount());
                    resultList.add(result);
                }
            }
        }
        return resultList;
    }

    // 多字段查一值
    // advanced =true 支持通配符, 前缀具高级特性
    public List<Json> multiMatchQuery(String index, String type,
                                      boolean advanced, String text, List<String> resultFieldList,
                                      Json page, String... fieldNames) {
        SearchRequestBuilder responsebuilder = client.prepareSearch(index)
                .setTypes(type);
        SearchResponse myresponse = null;
        QueryBuilder queryBuilder = null;
        if (advanced)
            queryBuilder = QueryBuilders.multiMatchQuery(text, fieldNames);
        else
            queryBuilder = QueryBuilders.termsQuery(text, fieldNames);
        long startTime=System.currentTimeMillis();
        myresponse = getSearchResponse(responsebuilder, queryBuilder, page);
        long endTime=System.currentTimeMillis();
        long totalTime=endTime-startTime;
        //ES sql 日志埋点
        writeSqlLog(responsebuilder,totalTime);
        SearchHits hits = myresponse.getHits();
        List<Json> resultList = new ArrayList<Json>();
        for (int i = 0; i < hits.getHits().length; i++) {
            Json result = new Json();
            for (String filed : resultFieldList) {
                result.put(filed, hits.getHits()[i].getSourceAsMap().get(filed));
            }
            resultList.add(result);
        }
        return resultList;
    }

    // 多字段查多值 一个字段对应一值 json field value type
    public List<Json> multiGroupQuery(String index, String type,
                                      List<Json> paramList, List<String> resultFieldList, Json sortParm,
                                      Json page) {
        SearchRequestBuilder responsebuilder = client.prepareSearch(index)
                .setTypes(type);
        SearchResponse myresponse = null;
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (Json param : paramList) {
            BoolQueryBuilder tempQueryBuilder = QueryBuilders.boolQuery();
            String[] values = param.getString("value").split(";");
            if (values.length > 1) {
                for (String value : values) {
                    tempQueryBuilder.should(QueryBuilders.termQuery(
                            param.getString("field"), value));
                }
                switch (param.getString("type")) {
                    case "must":
                        queryBuilder.must(tempQueryBuilder);
                        break;
                    case "mustNot":
                        queryBuilder.mustNot(tempQueryBuilder);
                        break;
                    case "should":
                        queryBuilder.should(tempQueryBuilder);
                        break;
                    default:
                        break;
                }

            } else {
                switch (param.getString("type")) {
                    case "must":
                        queryBuilder = queryBuilder
                                .must(QueryBuilders.termQuery(
                                        param.getString("field"),
                                        param.getString("value")));
                        break;
                    case "mustNot":
                        queryBuilder = queryBuilder.mustNot(QueryBuilders
                                .termQuery(param.getString("field"),
                                        param.getString("value")));
                        break;
                    case "should":
                        queryBuilder = queryBuilder
                                .should(QueryBuilders.termQuery(
                                        param.getString("field"),
                                        param.getString("value")));
                        break;
                    default:
                        break;
                }
            }

        }
        if (sortParm != null) {
            if ("desc".equals(sortParm.getString("sort_type")))
                responsebuilder.addSort(sortParm.getString("sort_field"),
                        SortOrder.DESC);
            else
                responsebuilder.addSort(sortParm.getString("sort_field"),
                        SortOrder.ASC);
        }
        long startTime=System.currentTimeMillis();
        myresponse = getSearchResponse(responsebuilder, queryBuilder, page);
        long endTime=System.currentTimeMillis();
        long totalTime=endTime-startTime;
        //ES sql 日志埋点
        writeSqlLog(responsebuilder,totalTime);
        SearchHits hits = myresponse.getHits();
        List<Json> resultList = new ArrayList<Json>();
        for (int i = 0; i < hits.getHits().length; i++) {
            Json result = new Json();
            for (String filed : resultFieldList) {
                result.put(filed, hits.getHits()[i].getSourceAsMap().get(filed));
            }
            resultList.add(result);
        }
        return resultList;
    }

    // range查询 json type value
    public List<Json> rangeQuery(String index, String type, List<Json> paramList,
                                 List<Json> rangeParamList, List<String> resultFieldList,Json sortParam, Json page) {
        SearchRequestBuilder responsebuilder = client.prepareSearch(index)
                .setTypes(type);
        SearchResponse myresponse = null;

        for (Json param : rangeParamList) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(param.getString("field"));
            switch (param.getString("type")) {
                case "gt":
                    rangeQueryBuilder.gt(param.getIntValue("value"));
                    break;
                case "lt":
                    rangeQueryBuilder.lt(param.getIntValue("value"));
                    break;
                case "gte":
                    rangeQueryBuilder.gte(param.getIntValue("value"));
                    break;
                case "lte":
                    rangeQueryBuilder.lte(param.getIntValue("value"));
                    break;
                default:
                    break;
            }
            responsebuilder = responsebuilder.setPostFilter(rangeQueryBuilder);
        }



        if (CollectionUtils.isNotEmpty(paramList)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            for (Json param : paramList) {
                BoolQueryBuilder tempQueryBuilder = QueryBuilders.boolQuery();
                String[] values = param.getString("value").split(";");
                if (values.length > 1) {
                    for (String value : values) {
                        tempQueryBuilder.should(QueryBuilders.termQuery(
                                param.getString("field"), value));
                    }
                    switch (param.getString("type")) {
                        case "must":
                            queryBuilder.must(tempQueryBuilder);
                            break;
                        case "mustNot":
                            queryBuilder.mustNot(tempQueryBuilder);
                            break;
                        case "should":
                            queryBuilder.should(tempQueryBuilder);
                            break;
                        default:
                            break;
                    }

                } else {
                    switch (param.getString("type")) {
                        case "must":
                            queryBuilder = queryBuilder.must(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        case "mustNot":
                            queryBuilder = queryBuilder.mustNot(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        case "should":
                            queryBuilder = queryBuilder.should(QueryBuilders
                                    .termQuery(param.getString("field"),
                                            param.getString("value")));
                            break;
                        default:
                            break;
                    }
                }
            }

            if (queryBuilder != null) {
                responsebuilder = responsebuilder.setQuery(queryBuilder);
            }

        }

        if (sortParam != null) {
            if ("desc".equals(sortParam.getString("sort_type")))
                responsebuilder.addSort(sortParam.getString("sort_field"),
                        SortOrder.DESC);
            else
                responsebuilder.addSort(sortParam.getString("sort_field"),
                        SortOrder.ASC);
        }


        //long startTime=System.currentTimeMillis();
        myresponse = getSearchResponse(responsebuilder, null, page);
        //long endTime=System.currentTimeMillis();
        //long totalTime=endTime-startTime;
        //ES sql 日志埋点
        //writeSqlLog(responsebuilder,totalTime);
        SearchHits hits = myresponse.getHits();
        List<Json> resultList = new ArrayList<Json>();
        for (int i = 0; i < hits.getHits().length; i++) {
            Json result = new Json();
            for (String resultField : resultFieldList) {
                result.put(resultField,
                        hits.getHits()[i].getSourceAsMap().get(resultField));
            }
            resultList.add(result);
        }
        return resultList;
    }

    public List<Json> query(String index, String type, Json queryParam,
                            List<String> resultFieldList, Json sortParm, Json page) {
        // param key value
        SearchRequestBuilder responsebuilder = client.prepareSearch(index)
                .setTypes(type);

        SearchResponse myresponse = null;
        QueryBuilder queryBuilder = null;
        switch (queryParam.getString("type")) {
            case "basicQuery":
                // 基本查询
                queryBuilder = QueryBuilders.matchPhraseQuery(
                        queryParam.getString("field"),
                        queryParam.getString("value"));
                break;
            case "matchAllQuery":
                // 查询All
                queryBuilder = QueryBuilders.matchAllQuery();
                break;
            case "commonQuery":
                // 常用词查询
                queryBuilder = QueryBuilders.commonTermsQuery(
                        queryParam.getString("field"),
                        queryParam.getString("value"));
                break;
            case "prefixQuery":
                // 前缀查询
                queryBuilder = QueryBuilders.prefixQuery(
                        queryParam.getString("field"),
                        queryParam.getString("value"));
                break;
            case "wildcardQuery":
                // 通配符查询
                queryBuilder = QueryBuilders.wildcardQuery(
                        queryParam.getString("field"),
                        queryParam.getString("value"));
                break;
            case "regexpQuery":
                // 正则表达式查询
                queryBuilder = QueryBuilders.regexpQuery(
                        queryParam.getString("field"),
                        queryParam.getString("value"));
                break;
            default:
                break;
        }
        if (sortParm != null) {
            if ("desc".equals(sortParm.getString("sort_type")))
                responsebuilder.addSort(sortParm.getString("sort_field"),
                        SortOrder.DESC);
            else
                responsebuilder.addSort(sortParm.getString("sort_field"),
                        SortOrder.ASC);
        }
        long startTime=System.currentTimeMillis();
        myresponse = getSearchResponse(responsebuilder, queryBuilder, page);
        long endTime=System.currentTimeMillis();
        long totalTime=endTime-startTime;
        //ES sql 日志埋点
        writeSqlLog(responsebuilder, totalTime);


        SearchHits hits = myresponse.getHits();
        List<Json> resultList = new ArrayList<Json>();
        for (int i = 0; i < hits.getHits().length; i++) {
            Json result = new Json();
            for (String filed : resultFieldList) {
                result.put(filed, hits.getHits()[i].getSourceAsMap().get(filed));
            }
            resultList.add(result);
        }
        return resultList;
    }

    private void writeSqlLog(SearchRequestBuilder responsebuilder, long totalTime) {
        BigSqlAnalyseLog bigSqlAnalyseLog=new BigSqlAnalyseLog();
        bigSqlAnalyseLog.setId(UuidUtils.generateUuid());
        bigSqlAnalyseLog.setBusinessName("");
        bigSqlAnalyseLog.setDbType("ES");
        bigSqlAnalyseLog.setOperationTime(new Date());
        bigSqlAnalyseLog.setSql(responsebuilder.toString());
        bigSqlAnalyseLog.setDuration(totalTime);
        BigSqlAnalyseLogController.submitBigSqlAnalyseLog(bigSqlAnalyseLog);
    }

    private SearchResponse getSearchResponse(
            SearchRequestBuilder responsebuilder, QueryBuilder queryBuilder,
            Json page) {
        int pageSize = 1000;
        int from = 0;
        if (page != null) {
            int pageIndex = page.getIntValue("pageIndex");
            pageSize = page.getIntValue("pageSize");
            from = pageSize * (pageIndex - 1);
        }
        if (queryBuilder == null)
            return responsebuilder.setFrom(from)
                    .setSize(pageSize).setExplain(true).execute().actionGet();
        else
            return responsebuilder.setQuery(queryBuilder).setFrom(from)
                    .setSize(pageSize).setExplain(true).execute().actionGet();
    }

    /**
     * 判断是否存在该索引
     * es
     *
     * @param indexName 索引名称
     * @return
     */
    public boolean isIndexExists(String indexName) {
        IndicesExistsRequestBuilder builder = client.admin().indices()
                .prepareExists(indexName);
        IndicesExistsResponse res = builder.get();
        return res.isExists();
    }

    public void upsertIndex(String indexName, String indexType, List<String> columns) {
        try {
            Boolean isExist = isIndexExists(indexName);
            if (isExist) {
                //如果是在两台机器上，下面直接putMapping可能会报异常
                PutMappingRequestBuilder builder = client.admin().indices().preparePutMapping(indexName);
                //testType就像当于数据的table
                builder.setType(indexType);
                XContentBuilder mapping = getEsMapping(columns);
                builder.setSource(mapping);
                PutMappingResponse response = builder.execute().actionGet();
                logger.info(response.isAcknowledged() ? "索引修改成功！" : "索引修改失败！");
            } else {
                CreateIndexRequestBuilder builder = client.admin().indices()
                        .prepareCreate(indexName);
                // 直接创建Map结构的setting
                Map<String, Object> settings = new HashMap<>();
                settings.put("number_of_shards", 10); // 分片数
                settings.put("number_of_replicas", 0); // 副本数
                settings.put("refresh_interval", "10s"); // 刷新间隔
                builder.setSettings(settings);

                builder.addMapping(indexType, getEsMapping(columns));
                CreateIndexResponse res = builder.get();
                logger.info(res.isAcknowledged() ? "索引创建成功！" : "索引创建失败！");
            }

        } catch (Exception e) {
            logger.error("创建索引失败！", e);
        }
    }

    public XContentBuilder getEsMapping(List<String> columnsList) throws IOException {
        XContentBuilder source = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("properties");
        for (String column : columnsList) {
            if (column.contains(",")) {
                source = source.startObject(column.split(",")[0])
                        .field("type", column.split(",")[1])
                        .field("store", true);
                if ("date".equals(column.split(",")[1])) {
                    source = source.field("format", column.split(",")[2]);
                }
                source = source.field("index", true)
                        .endObject();
            } else {
                source = source.startObject(column)
                        .field("type", "keyword")
                        .field("store", true)
                        .field("index", true)
                        .endObject();
            }
        }
        source = source.endObject().endObject();
        return source;
    }

    /**
     * 创建module_operation_log索引
     */
    public void createModuleOperationIndex() {
        try {
            CreateIndexRequestBuilder builder = client.admin().indices()
                    .prepareCreate("module_operation_log");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 10); // 分片数
            settings.put("number_of_replicas", 0); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);

            builder.addMapping("module_operation_log",
                    getModuleOperationLogSource());
            CreateIndexResponse res = builder.get();
            logger.info(res.isAcknowledged() ? "索引module_operation_log创建成功！"
                    : "索引module_operation_log创建失败！");
        } catch (Exception e) {
            logger.error("创建module_operation_log索引失败！", e);
        }
    }

    /**
     * 创建biz_operation_log索引
     */
    public void createBizOperationIndex() {
        try {
            CreateIndexRequestBuilder builder = client.admin().indices()
                    .prepareCreate("biz_operation_log");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 10); // 分片数
            settings.put("number_of_replicas", 0); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);
            builder.addMapping("business", getBizOperationLogSource());
            CreateIndexResponse res = builder.get();
            logger.info(res.isAcknowledged() ? "索引biz_operation_log创建成功！"
                    : "索引biz_operation_log创建失败！");
        } catch (Exception e) {
            logger.error("创建biz_operation_log索引失败！", e);
        }
    }


    /**
     * 创建创建sql_analyse_log索引索引
     */
    public void createSqlAnalyseIndex() {
        try {
            CreateIndexRequestBuilder builder = client.admin().indices()
                    .prepareCreate("sql_analyse_log");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 10); // 分片数
            settings.put("number_of_replicas", 0); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);

            builder.addMapping("sql_analyse_log", getSqlAnalyseSource());
            CreateIndexResponse res = builder.get();
            logger.info(res.isAcknowledged() ? "索引sql_analyse_log创建成功！"
                    : "索引sql_analyse_log创建失败！");
        } catch (Exception e) {
            logger.error("创建sql_analyse_log索引失败！", e);
        }
    }

    private XContentBuilder getSqlAnalyseSource() throws IOException {
        XContentBuilder source = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("properties")
                // moduleId
                .startObject("id")
                .field("type", "keyword")
                .field("store", true)
                .endObject()
                // 业务名称
                .startObject("businessName").field("type", "keyword")
                .field("store", true)
                .endObject()
                // 数据库类型
                .startObject("dbType").field("type", "keyword")
                .field("store", true)
                .endObject()
                // sql
                .startObject("sql").field("type", "keyword")
                .field("store", true)
                .endObject()
                // 持续时间
                .startObject("duration").field("type", "long")
                .field("store", true)
                .endObject()
                // 操作时间
                .startObject("operationTime").field("type", "date")
                .field("format", "yyyy-MM-dd HH:mm:ss").field("store", true)
                .endObject().endObject().endObject();
        return source;
    }

    private XContentBuilder getModuleOperationLogSource() throws IOException {
        XContentBuilder source = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("properties")
                // moduleId
                .startObject("moduleId")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // name
                .startObject("name").field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // url
                .startObject("url").field("type", "keyword")
                .field("store", true).field("index", true)
                .endObject()
                // 操作时间
                .startObject("log_time").field("type", "date")
                .field("format", "yyyy-MM-dd HH:mm:ss").field("store", true)
                .field("index", true).endObject().endObject().endObject();
        return source;
    }

    private XContentBuilder getBizOperationLogSource() throws IOException {
        XContentBuilder source = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("properties")
                // logType
                .startObject("logType")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // bizCode
                .startObject("bizCode")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // bizName
                .startObject("bizName")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // subSystem
                .startObject("subSystem")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // description
                .startObject("description")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // oldData
                .startObject("oldData")
                .field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // newData
                .startObject("newData").field("type", "keyword")
                .field("store", true)
                .field("index", true)
                .endObject()
                // operator
                .startObject("operator").field("type", "keyword")
                .field("store", true).field("index", true)
                .endObject()
                // 操作时间
                .startObject("operationTime").field("type", "date")
                .field("format", "yyyy-MM-dd HH:mm:ss").field("store", true)
                .field("index", true).endObject().endObject().endObject();
        return source;
    }

}
