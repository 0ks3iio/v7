<div class="table-container">
    <div class="table-container-header clearfix mb-5">
        <button class="btn btn-blue relationType no-radius" id="relation_report">表格</button>
        <button class="btn btn-white relationType no-radius" id="relation_chart" style="margin-left: -5px">图表</button>
        <#--<button class="pull-right btn btn-lightblue add-data" onclick="editRelation('')">新增血缘关系</button>-->
    </div>
    <div class="table-container-body">
        <div id="chartDiv" hidden="hidden" class="tab-pane height-1of1 active">
            <div class="js-chart" id="chart" style="height: 600px;"></div>
        </div>
        <table class="tables tables-striped tables-hover" id="tableDiv">
            <thead>
            <tr>
                <th>源名称</th>
                <th>源类型</th>
                <th>目标名称</th>
                <th>目标类型</th>
            </tr>
            </thead>
            <tbody>
            <#if (source?exists && source?size gt 0) || (target?exists && target?size gt 0)>
            <#list source as item>
                <tr>
                    <td><span class="badge badge-primary badge-sm">来源</span> ${item.sourceName!}</td>
                    <td>
                        <#if item.sourceType?default('table') == 'table'>数据表</#if>
                        <#if item.sourceType?default('table') == 'dataxJob'>数据采集</#if>
                        <#if item.sourceType?default('table') == 'job'>ETL调度</#if>
                        <#if item.sourceType?default('table') == 'model'>数据模型</#if>
                        <#if item.sourceType?default('table') == 'event'>事件管理</#if>
                        <#if item.sourceType?default('table') == 'app'>数据应用</#if>
                        <#if item.sourceType?default('table') == 'api'>数据接口</#if>
                    </td>
                    <td><span class="badge badge-default badge-sm">去向</span> ${item.targetName!}</td>
                    <td>
                        <#if item.targetType?default('table') == 'table'>数据表</#if>
                        <#if item.targetType?default('table') == 'dataxJob'>数据采集</#if>
                        <#if item.targetType?default('table') == 'job'>ETL调度</#if>
                        <#if item.targetType?default('table') == 'model'>数据模型</#if>
                        <#if item.targetType?default('table') == 'event'>事件管理</#if>
                        <#if item.targetType?default('table') == 'app'>数据应用</#if>
                        <#if item.targetType?default('table') == 'api'>数据接口</#if>
                    </td>
                </tr>
            </#list>
            <#list target as item>
                <tr>
                    <td><span class="badge badge-primary badge-sm">来源</span> ${item.sourceName!}</td>
                    <td>
                        <#if item.sourceType?default('table') == 'table'>数据表</#if>
                        <#if item.sourceType?default('table') == 'dataxJob'>数据采集</#if>
                        <#if item.sourceType?default('table') == 'job'>ETL调度</#if>
                        <#if item.sourceType?default('table') == 'model'>数据模型</#if>
                        <#if item.sourceType?default('table') == 'event'>事件管理</#if>
                        <#if item.sourceType?default('table') == 'app'>数据应用</#if>
                        <#if item.sourceType?default('table') == 'api'>数据接口</#if>
                    </td>
                    <td><span class="badge badge-default badge-sm">去向</span> ${item.targetName!}</td>
                    <td>
                        <#if item.targetType?default('table') == 'table'>数据表</#if>
                        <#if item.targetType?default('table') == 'dataxJob'>数据采集</#if>
                        <#if item.targetType?default('table') == 'job'>ETL调度</#if>
                        <#if item.targetType?default('table') == 'model'>数据模型</#if>
                        <#if item.targetType?default('table') == 'event'>事件管理</#if>
                        <#if item.targetType?default('table') == 'app'>数据应用</#if>
                        <#if item.targetType?default('table') == 'api'>数据接口</#if>
                    </td>
                </tr>
            </#list>
            <#else>
                <tr >
                    <td  colspan="4" align="center">
                        暂无数据
                    </td>
                <tr>
            </#if>
            </tbody>
        </table>
    </div>
</div>
<script>
    $(function () {
        $('.relationType').on('click', function () {
            if ($(this).attr('id') == 'relation_report') {
                $('#relation_report').removeClass('btn-white').addClass('btn-blue');
                $('#relation_chart').removeClass('btn-blue').addClass('btn-white');
                $('#chartDiv').hide();
                $('#tableDiv').show();
            }

            if ($(this).attr('id') == 'relation_chart') {
                $('#relation_chart').removeClass('btn-white').addClass('btn-blue');
                $('#relation_report').removeClass('btn-blue').addClass('btn-white');
                $('#tableDiv').hide();
                $('#chartDiv').show();
                showChart();
            }

        })
    });

    var myChart = null;
    var option = null;

    function showChart() {
        var metadataId = $('.directory-tree a.active').attr('id');
        var mdType = $('.filter-item.active').attr('mdType');
        $.ajax({
            url: '${request.contextPath}/bigdata/metadata/'+ metadataId +'/bloodRelationshipChart?mdType=' + mdType,
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                } else {
                    var data = response.data;
                    myChart = echarts.init($('.js-chart')[0]);
                    showRelation(data);
                }
            }
        });

    }

    function showRelation(data) {
        var dataArray = new Array();
        var dataIds = new Array();
        $.each(data,function(index,item){
            if ($.inArray(item.sourceId,dataIds)<0) {
                dataIds[dataIds.length] = item.sourceId;
                eachFindData(item.sourceId,item.sourceType,item.sourceName,dataArray);
            }
            if ($.inArray(item.targetId,dataIds)<0) {
                dataIds[dataIds.length] = item.targetId;
                eachFindData(item.targetId,item.targetType,item.targetName,dataArray);
            }
        });
        var linksArray = new Array();
        $.each(data,function(index,item){
            eachFindLinks(item,linksArray);
        });
        setOption(dataArray,linksArray);
        myChart.setOption(option);
    }

    function eachFindLinks(item,linksArray) {
        var linkObj = new Object();
        linkObj.source = item.sourceId;
        linkObj.target = item.targetId;
        linksArray[linksArray.length] = linkObj;
    }

    function eachFindData(id,type,name,dataArray) {
        var dataObj = new Object();
        dataObj.name = id;
        dataObj.label = name;
        dataObj.mdType = type;
        dataObj.draggable = true;
        if (type == "table") {
            dataObj.category = "数据表";
        } else if (type == "job") {
            dataObj.category = "ETL调度";
        } else if (type == "app") {
            dataObj.category = "数据应用";
        } else if (type == "api") {
            dataObj.category = "数据接口";
        } else if (type == "dataxJob") {
            dataObj.category = "数据采集";
        } else if (type == "model") {
            dataObj.category = "数据模型";
        }
        dataArray[dataArray.length] = dataObj;
    }

    function setOption(optionData,optionLinks) {
        option = {
            tooltip: {
                formatter: function(params){
                    return params.data.label;
                }
            },
            animationDurationUpdate: 1500,
            animationEasingUpdate: 'quinticInOut',
            label: {
                normal: {
                    show: true,
                    textStyle: {
                        fontSize: 12
                    }
                }
            },
            color: ["#3ebb99", "#69a4f0", "#af89f3", "#ed5784", "#ed7e46", "#9266ff", "#ff66e9"],
            legend: {show: true, x: "center", x : 'left', y : '200', orient: 'vertical',
                data: [
                    {name: '数据表',textStyle:{color:'#666'} },
                    {name: 'ETL调度',textStyle:{color:'#666'}},
                    {name: '数据应用',textStyle:{color:'#666'}},
                    {name: '数据接口',textStyle:{color:'#666'}},
                    {name: '数据采集',textStyle:{color:'#666'}},
                    {name: '数据模型',textStyle:{color:'#666'}}
                ]
            },
            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    symbolSize: 45,
                    focusNodeAdjacency: true,
                    roam: true,
                    symbol: 'rect',
                    symbolSize: [80, 32],
                    categories: [{name: '数据表'}, {name: 'ETL调度'}, {name: '数据应用'},{name: '数据接口'},{name: '数据采集'},{name: '数据模型'}],
                    label: {
                        normal: {
                            show: true,
                            textStyle: {
                                fontSize: 12
                            },
                            formatter: function(params){
                                return params.data.label;
                            }
                        }
                    },
                    force: {
                        repulsion: 1000
                    },
                    edgeSymbol: ['rectangle', 'arrow'],
                    edgeSymbolSize: [4, 10],
                    data: optionData,
                    links: optionLinks,
                    lineStyle: {
                        normal: {
                            opacity: 0.9,
                            width: 1,
                            curveness: 0
                        }
                    }
                }
            ]
        };
    }
</script>