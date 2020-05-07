<div class="table-container">
    <div class="table-container-header clearfix mb-5">
        <button class="btn btn-blue relationType no-radius" id="relation_report">表格</button>
        <button class="btn btn-white relationType no-radius" id="relation_chart" style="margin-left: -5px">图表</button>
        <#--<button class="pull-right btn btn-lightblue add-data" onclick="editRelation('')">新增血缘关系</button>-->
    </div>
    <div class="table-container-body" style="overflow: auto;">
        <div id="chartDiv" hidden="hidden" class="tab-pane height-1of1 active">
            <div class="js-chart" id="chart" style="height: 400px;"></div>
        </div>
        <table class="tables" id="tableDiv">
            <thead>
            <tr>
                <th>源类型</th>
                <th>源名称</th>
                <th>目标类型</th>
                <th>目标名称</th>
                <#--<th>操作</th>-->
            </tr>
            </thead>
            <tbody>
            <#if  metadataRelationList?exists &&metadataRelationList?size gt 0>
                <#list metadataRelationList as item>
                <tr>
                    <td>
                        <#if item.sourceType?default('table') == 'table'>表</#if>
                        <#if item.sourceType?default('table') == 'job'>任务</#if>
                        <#if item.sourceType?default('table') == 'model'>模型</#if>
                        <#if item.sourceType?default('table') == 'event'>事件</#if>
                        <#if item.sourceType?default('table') == 'app'>应用</#if>
                    </td>
                    <td>${item.sourceName!}</td>
                    <td>
                        <#if item.targetType?default('table') == 'table'>表</#if>
                        <#if item.targetType?default('table') == 'job'>任务</#if>
                        <#if item.targetType?default('table') == 'model'>模型</#if>
                        <#if item.targetType?default('table') == 'event'>事件</#if>
                        <#if item.targetType?default('table') == 'app'>应用</#if>
                    </td>
                    <td>${item.targetName!}</td>
                    <#--<td>-->
                        <#--<a href="javascript:void(0);" onclick="editRelation('${item.id!}')">编辑</a><span class="tables-line">|</span>-->
                        <#--<a href="javascript:void(0);" onclick="deleteRelation('${item.id!}', '${item.sourceName!}', '${item.targetName!}')">删除</a>-->
                    <#--</td>-->
                </tr>
                </#list>
            <#else>
                <tr>
                    <td colspan="5" align="center">
                        暂无血缘信息
                    </td>
                </tr>
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
        var metadataId = $('.count-line.active').attr('id');
        var mdType = $('.filter-item.active').attr('mdType');
        $.ajax({
            url: '${request.contextPath}/bigdata/metadata/'+ metadataId +'/bloodRelationshipChart?mdType=' + mdType,
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                } else {
                    var data = response.data;
                    debugger
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
                    {name: '表',textStyle:{color:'#666'} },
                    {name: 'etl任务',textStyle:{color:'#666'}},
                    {name: '应用',textStyle:{color:'#666'}},
                    {name: '数据模型',textStyle:{color:'#666'}},
                    {name: '接口',textStyle:{color:'#666'}},
                    {name: '事件管理',textStyle:{color:'#666'}},
                    {name: 'datax任务',textStyle:{color:'#666'}}
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
                    categories: [{name: '表'}, {name: 'etl任务'}, {name: '应用'},{name: '数据模型'},{name: '接口'},{name: '事件管理'},{name: 'datax任务'}],
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