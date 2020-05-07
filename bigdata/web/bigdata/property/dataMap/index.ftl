<div class="index data-map">
    <div class="wrap-full myChart"></div>
    <div class="wrap-full nodatadiv" style="display: none">
        <div class="no-data-common">
            <div class="text-center">
                <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-search-100.png"/>
                <p class="color-999">暂无您要的数据</p>
            </div>
        </div>
    </div>
    <div class="pos-left-top">
        <b>主题：</b>
        <select id="topicId" name="topicId" class="form-control mt-15 mb-20" onchange="changeChoose()">
            <#if topicList?exists && topicList?size gt 0>
                <#list topicList as topic>
                    <option value="${topic.id!}">${topic.name!}</option>
                </#list>
            </#if>
        </select>
        <b>层次：</b>
        <select id="dwRankId" name="dwRankId" class="form-control mt-15 mb-20" onchange="changeChoose()">
            <option value="">全部层次</option>
            <#if dwRankList?exists && dwRankList?size gt 0>
                <#list dwRankList as dw>
                    <option value="${dw.id!}">${dw.name!}</option>
                </#list>
            </#if>
        </select>
    </div>
    <div class="bottom-explain hide">
        <div class="bottom-explain-head">
            <b id="metadataName">元数据</b>
            <div class="pos-right js-close">
                <i class="wpfont icon-close"></i>
            </div>
        </div>
        <div class="bottom-explain-body clearfix" id="metadataDiv">

        </div>
    </div>
</div>
<script>
    var myChart = null;
    var option = null;
    $(function(){
        changeChoose();
        $('body').on('click','.js-close',function(){
            $('.bottom-explain').addClass('hide');
        });
        myChart = echarts.init($('.myChart')[0]);
        myChart.on('click', function (param){
            showDataContent(param.data.defaultName,param.data.name,param.data.mdType);
        });
    })

    function showDataContent(name,id,mdType) {
        $(".bottom-explain").removeClass('active');
        if (name != "该数据源已被删除") {
            $("#metadataName").html(name);
            var url = "${request.contextPath}/bigdata/property/map/metadata?id="+id+"&mdType="+mdType;
            $("#metadataDiv").load(url);
            $('.bottom-explain').removeClass('hide');
        } else {
            $('.bottom-explain').addClass('hide');
        }
    }

    function changeChoose() {
        var topicId = $("#topicId").val();
        var dwRankId = $("#dwRankId").val();
        $.ajax({
            url: '${request.contextPath}/bigdata/property/map/relation',
            data: {"dwRankId":dwRankId,"topicId":topicId},
            type: 'post',
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    var data = JSON.parse(response.data);
                    if (data.length != 0) {
                        showRelation(data);
                        $(".nodatadiv").attr("style","display:none");
                        $(".myChart").attr("style","display:block");
                    } else {
                        $(".nodatadiv").attr("style","display:block");
                        $(".myChart").attr("style","display:none");
                    }
                    $('.bottom-explain').addClass('hide');
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
            if (item.targetId != null && $.inArray(item.targetId,dataIds)<0) {
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
        if (item.targetId != null) {
            var linkObj = new Object();
            linkObj.source = item.sourceId;
            linkObj.target = item.targetId;
            linksArray[linksArray.length] = linkObj;
        }
    }

    var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
    function eachFindData(id,type,name,dataArray) {
        var dataObj = new Object();
        dataObj.name = id;
        dataObj.defaultName = name;
        if(reg.test(name)){
            if (name.length>8) {
                name = name.substring(0,8) + "...";
            }
        } else {
            if (name.length>12) {
                name = name.substring(0,12) + "...";
            }
        }
        dataObj.label = name;
        dataObj.mdType = type;
        dataObj.draggable = true;
        if (type == "table") {
            dataObj.category = "数据表";
        } else if (type == "dataxJob") {
            dataObj.category = "数据采集";
        } else if (type == "job") {
            dataObj.category = "ETL调度";
        } else if (type == "api") {
            dataObj.category = "数据接口";
        } else if (type == "app") {
            dataObj.category = "数据应用";
        } else if (type == "model") {
            dataObj.category = "数据模型";
        }
        //  else if (type == "event") {
        //     dataObj.category = "事件管理";
        // }
        dataArray[dataArray.length] = dataObj;
    }

    function setOption(optionData,optionLinks) {
        option = {
            tooltip: {
                formatter: function(params){
                    return params.data.defaultName;
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
            color: ["#3ebb99", "#69a4f0", "#af89f3", "#ed5784", "#ed7e46", "#9266ff"],  //"#ff66e9"
            legend: {show: true, x: "center", x : 'left', y : '200', orient: 'vertical',
                data: [
                    {name: '数据表',textStyle:{color:'#666'} },
                    {name: '数据采集',textStyle:{color:'#666'}},
                    {name: 'ETL调度',textStyle:{color:'#666'}},
                    {name: '数据接口',textStyle:{color:'#666'}},
                    {name: '数据应用',textStyle:{color:'#666'}},
                    {name: '数据模型',textStyle:{color:'#666'}}
                ]      //,{name: '事件管理',textStyle:{color:'#666'}}
            },
            series: [
                {
                    type: 'graph',
                    layout: 'force',
                    focusNodeAdjacency: true,
                    roam: true,
                    nodeScaleRatio:0,
                    symbol: 'rect',
                    symbolSize: [110, 32],
                    categories: [{name: '数据表'}, {name: '数据采集'}, {name: 'ETL调度'},{name: '数据接口'},{name: '数据应用'},{name: '数据模型'}], //{name: '事件管理'}
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