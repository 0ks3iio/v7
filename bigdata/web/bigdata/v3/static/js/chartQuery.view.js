$(function () {
    var chartRun = true;
    $('.left-part>p').click(function () {
        $(this).siblings('.bg-grey').trigger('click');
        $(this).toggleClass('bg-grey');
        $('.left-part>p>i').attr('class', 'arrow fa fa-angle-right');
        $('.left-part>p.bg-grey>i').attr('class', 'arrow fa fa-angle-down');
    });
    $('.collapse img').click(function () {
        $(this).addClass('border-active').siblings().removeClass('border-active');
        $(this).parent().parent('.collapse').siblings('.collapse').find('img').removeClass('border-active');
    });

    //设置高度
    var $height = $('#sidebar').height() - 40;
    $('.text').find('textarea').height($height - 279);
    $('.blank').height($height - 216);
    $('.left-part').height($height);
    $('.chart-show').height($height);

    //中间echarts图表
    $('#main').height($('.chart-show').height() - 100).width($('.chart-show').width() - 20);
    $(window).resize(function () {
        $('#main').height($('.chart-show').height() - 100).width($('.chart-show').width() - 20);
    });
    //初始化返回按钮
    showBreadBack(go2ChartList, true, '图表查询');

    function go2ChartList() {
        openModel('708033', '图表查询', 1, _contextPath + '/bigdata/chartQuery/index', '大数据管理', '数据分析', null, false);
    }
    //切换不同的图表类型
    $('#chart_type_list').on('click', 'img', function () {
        httpInvoker.runChart(_chartEdit_json.id, $('img.border-active').attr('type'), false);
    });




    var httpInvoker = {};
    httpInvoker.runChart = function (chartId, chartType, success, refresh) {
        //TODO 有待进一步区分图表类型
        $.ajax({
            // url: _contextPath + '/bigdata/chartQuery/' + chartId + '/' + chartType,
            url: _contextPath + '/bigdata/echarts/view',
            data: {
                chartId: chartId,
                chartType : chartType
            },
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                }
                else {
                    try {
                        var op = JSON.parse(response.data);
                        if (op.echarts) {
                            var currentInstance = echarts.getInstanceByDom(document.getElementById('main'));

                            if (currentInstance != null && !refresh) {
                                currentInstance.dispose();
                            }
                            if (op.map) {
                                httpInvoker.doGetMap(op.mapName, op.option, refresh);
                            } else {
                                var echart_div = refresh && currentInstance != null ? currentInstance : echarts.init(document.getElementById('main'));
                                echart_div.setOption(op.option);
                            }
                        }
                        else {
                            myChart.init(document.getElementById("main"), op.option, op.chartType, _chartEdit_json.name, true);
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
            $.get(_contextPath + '/static/bigdata/js/echarts/map/json/province/' + regionCode + ".json", function (geoJson) {

                var currentEcharts = echarts.getInstanceByDom(document.getElementById('main'));
                currentEcharts = currentEcharts != null ? currentEcharts :  echarts.init(document.getElementById("main"));
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
    //

    httpInvoker.runChart(_chartEdit_json.id, _chartEdit_json.chartType, function () {
        chartRun = true;
    }, false);
    var interval;
    if (_chartEdit_json && _chartEdit_json.updateInterval > 0) {
        interval = setInterval(function () {
            httpInvoker.runChart(_chartEdit_json.id, $('img.border-active').attr('type'), true);
        }, _chartEdit_json.updateInterval * 1000);
    }
});