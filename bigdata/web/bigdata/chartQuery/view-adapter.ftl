<#if viewType=='kanban'>
    <div class="common-chart-view wrap-full chart" id="${uuid}_main">

    </div>
<#else >
    <div class="box box-structure">
        <div class="box box-default body-scroll-32 chart-main-view">
            <div class="common-chart-view wrap-full chart" id="${uuid}_main">

            </div>
        </div>
    </div>
</#if>

<#--<script src="${request.contextPath}/static/bigdata/js/echarts/echarts-wordcloud.min.js"></script>-->
<script src="${request.contextPath}/static/bigdata/js/echarts/myCharts.js"></script>
<script type="text/javascript">
    $(function () {
        <#if id??>

        var httpInvoker = {};
        httpInvoker.runChart = function (chartId, chartType, success, refresh) {
            //TODO 有待进一步区分图表类型
            $.ajax({
                // url: _contextPath + '/bigdata/chartQuery/' + chartId + '/' + chartType,
                url: '${springMacroRequestContext.contextPath}/bigdata/echarts/view',
                data: {
                    chartId: chartId,
                    chartType: chartType
                },
                type: 'GET',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        try {
                            var op = JSON.parse(response.data);
                            if (op.echarts) {
                                var currentInstance = echarts.getInstanceByDom(document.getElementById('${uuid}_main'));

                                if (currentInstance != null && !refresh) {
                                    currentInstance.dispose();
                                }
                                if (op.map) {
                                    httpInvoker.doGetMap(op.mapName, op.option, refresh);
                                } else {
                                    var echart_div = refresh && currentInstance != null ? currentInstance : echarts.init(document.getElementById('${uuid}_main'));
                                    echart_div.setOption(op.option);
                                }
                            } else {
                                myChart.init(document.getElementById("${uuid}_main"), op.option, op.chartType, _chartEdit_json.name, true);
                            }
                            if (typeof success === 'function') {
                                success(response.data);
                            }
                        } catch (e) {
                            try {
                                clearInterval(interval);
                            } catch (e) {
                            }
                        } finally {
                            if (typeof success === 'function') {
                                success(response.data);
                            }
                        }

                    }
                }
            });
        };

        httpInvoker.doGetMap = function (regionCode, option, refresh) {
            var map = echarts.getMap(regionCode);
            if (map) {
                var currentEchartsInstance = echarts.getInstanceByDom(document.getElementById('main'));
                currentEchartsInstance = currentEchartsInstance != null ? currentEchartsInstance : echarts.init(document.getElementById('main'));
                option.series[0].mapType = regionCode;
                currentEchartsInstance.setOption(option);
            } else {
                $.get('${springMacroRequestContext.contextPath}/static/bigdata/js/echarts/map/json/province/' + regionCode + ".json", function (geoJson) {

                    var currentEcharts = echarts.getInstanceByDom(document.getElementById('${uuid}_main'));
                    currentEcharts = currentEcharts != null ? currentEcharts : echarts.init(document.getElementById("${uuid}_main"));
                    //currentEcharts.hideLoading();
                    echarts.registerMap(regionCode, geoJson);
                    if (typeof option === 'undefined') {
                        currentEcharts.setOption({
                            series: [
                                {
                                    name: '',
                                    zoom: 1.2,
                                    type: 'map',
                                    mapType: regionCode,
                                    label: {show: true}
                                }
                            ]
                        });
                    } else {
                        option.series[0].mapType = regionCode;
                        currentEcharts.setOption(option);
                    }
                });
            }
        };

        httpInvoker.runChart('${id!}', '${type!}', false);
        <#else >
        alert("该图表已被删除");
        </#if>

    })

</script>
</body>
</html>