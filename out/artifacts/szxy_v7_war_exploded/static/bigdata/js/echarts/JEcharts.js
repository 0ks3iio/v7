(function () {

    var root = this;

    var JEcharts = {};

    JEcharts.init = function (op) {

        //其他类型
        if (op.initOther && op.initDemo) {
            this.initOtherDemo(op);
            return;
        }

        JEcharts.doGetOption(op.chartId, op.cockpit, function (optionEx) {
            JEcharts.doRender(optionEx, op.dom, op.refresh, function (ec_instance) {
                if (!op.refresh) {
                    op.success(op.uuid);
                }
                if (optionEx.autoRefresh) {
                    op.refresh = true;
                    try {
                        if ($('div[uuid="' + op.uuid + '"]').length > 0) {
                            setTimeout(function () {
                                JEcharts.init(op)
                            }, optionEx.autoRefreshInterval * 1000);
                        }
                    } catch (e) {

                    }
                }
            }, op);
        }, op.error);
    };

    JEcharts.initOtherDemo = function (op) {
        //
        if (op.initDemo) {
            this.doGetDemoData(op.chartType, function (data) {
                let chart = {};
                chart.dataSet = JSON.stringify(data);
                chart.chartType = op.chartType;
                chart.dataSourceType = 3;
                JEcharts.doGetDemoOption(chart, function (optionEx) {
                    JEcharts.doRender(optionEx, op.dom, op.refresh, function () {
                        if (!op.refresh) {
                            op.success(op.uuid, chart);
                        }
                        if (optionEx.autoRefresh) {
                            op.refresh = true;
                            try {
                                if ($('div[uuid="'+op.uuid+'"]').length > 0) {
                                    setTimeout(function () {
                                        JEcharts.init(op)
                                    }, optionEx.autoRefreshInterval * 1000);
                                }
                            } catch (e) {

                            }
                        }
                    })
                })
            })
        }
    };

    JEcharts.initOther = function(op) {
        JEcharts.doGetDemoOption(op.chart, function (optionEx) {
            JEcharts.doRender(optionEx, op.dom, op.refresh, function () {
                if (!op.refresh) {
                    op.success(op.uuid, op.chart);
                }
                if (optionEx.autoRefresh) {
                    op.refresh = true;
                    try {
                        if ($('div[uuid="'+op.uuid+'"]').length > 0) {
                            setTimeout(function () {
                                JEcharts.init(op)
                            }, optionEx.autoRefreshInterval * 1000);
                        }
                    } catch (e) {

                    }
                }
            })
        })
    };

    JEcharts.refresh = function (op) {
        JEcharts.doGetOption(op.chartId, op.cockpit, function (optionEx) {
            JEcharts.doRender(optionEx, op.dom, true, function (ec_instance) {
                op.success(optionEx, ec_instance);
            });
        }, op.error);
    };

    JEcharts.doGetOption = function (chartId, cockpit, success, error, parent, parentParent) {
        let d = {
            chartId: chartId,
            cockpit: cockpit
        };
        if (parentParent) {
            d.parentParent = parentParent;
        }
        if (parent) {
            d.parent = parent;
        }
        $.ajax({
            url: window._contextPath + '/bigdata/echarts/view',
            data: d,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    //渲染图表
                    let op = JSON.parse(res.data);
                    if (typeof success === 'function') {
                        success(op);
                    }
                } else {
                    if (typeof  error === 'function') {
                        error(res.message);
                    }
                }
            },
            error: function () {
                error("网络异常！");
            }
        });
    };

    JEcharts.doGetOptionRealTime = function(chart, success, error) {
        $.ajax({
            url: window._contextPath + '/bigdata/echarts/view/editChart',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data : JSON.stringify(chart),
            success: function (res) {
                if (res.success) {
                    success(res.data);
                } else {
                    error(res.message);
                }
            },
            error: function () {
                layer.msg('网络异常', {icon: 3});
            }
        })
    };

    JEcharts.mapClick = function(params, op) {
        JEcharts.doGetOption(op.chartId, op.cockpit, function (optionEx) {
            JEcharts.doRender(optionEx, op.dom, op.refresh, function () {
                op.uuid = new Date().getTime();
                $('div[uuid="'+op.uuid+'"]').attr('uuid', op.uuid);
                if (!op.refresh) {
                    op.success(op.uuid);
                }
                if (optionEx.autoRefresh) {
                    op.refresh = true;
                    try {
                        if ($('div[uuid="'+op.uuid+'"]').length > 0) {
                            setTimeout(function () {
                                JEcharts.init(op)
                            }, optionEx.autoRefreshInterval * 1000);
                        }
                    } catch (e) {

                    }
                }
            }, op)
        }, op.error, params.name, params.parentParent)
    };

    function isMapClick(parent) {
        if (parent.length == 4) {
            return false;
        }
        if (/11|12|31|50|81|82/.test(parent)) {
            return false;
        }
        return true;
    }

    function isBack(params) {
        let bp  = {};
        if (!params.parent) {
            bp.back = false;
            return bp;
        }
        if (params.top == params.parentParent) {
            bp.back = true;
            bp.parent = params.top;
            bp.parentParent = params.top;
            return bp;
        }
        if (params.top == '00') {
            if (params.parent.length == 4 && params.parentParent.length == 2) {
                bp.back = true;
                bp.parent = params.parentParent;
                bp.parentParent = '00';
                return bp;
            }
        }
    }

    JEcharts.doRender = function (optionEx, dom, refresh, call, opt) {
        let op = optionEx;
        if (op.echarts) {
            var old = echarts.getInstanceByDom(dom);
            if (old != null && !refresh) {
                echarts.dispose(dom);
            }
            if (op.map) {
                JEcharts.doGetGeo(op.mapName, function () {
                    var echart_div = echarts.init(dom);
                    echart_div.setOption(op.option);
                    if (isMapClick(optionEx.mapName)) {
                        echart_div.on('click', function (params) {
                            if (params.name != "" && params.seriesType == 'map') {
                                params.parentParent = optionEx.mapName;
                                JEcharts.mapClick(params, opt)
                            }
                        });
                    }
                    echart_div.on('contextmenu', function (params) {
                        params.top = op.top;
                        params.parentParent = op.parentParent;
                        params.parent = op.parentMap;
                        let bp = isBack(params);
                        if (bp.back) {
                            bp.name = bp.parent;
                            bp.parent = bp.parentParent;
                            JEcharts.mapClick(bp, opt)
                        }
                    })
                    call(echart_div);
                    if (typeof call === 'function') {
                    }
                });
            } else {
                var echart_div = echarts.init(dom);
                echart_div.setOption(op.option);
                if (typeof call === 'function') {
                    call(echart_div);
                }
            }
        }
        else {
            myChart.init(dom, op.option, op.chartType, "", true);
            call();
        }
    };

    JEcharts.doGetGeo = function (mapName, call) {
        //避免重复请求
        console.log(echarts.getMap(mapName))
        if (!echarts.getMap(mapName)) {
            $.get(window._contextPath + '/static/bigdata/js/echarts/map/json/province/' + mapName + ".json", function (geoJson) {
                echarts.registerMap(mapName, geoJson);
                if (typeof call === 'function') {
                    call();
                }
            });
        } else {
            if (typeof call === 'function') {
                call();
            }
        }
    };

    JEcharts.doGetOtherData = function(chartId, chartType, call) {
        if (!chartId && chartType) {
            JEcharts.doGetDemoData(chartType, call);
            return;
        }
        $.ajax({
            url: window._contextPath + '/bigdata/chart/' + chartId,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    call(res.data);
                } else {
                    layer.msg('服务异常' + res.message, {icon: 3});
                }
            },
            error: function () {
                layer.msg('网络异常', {icon: 3});
            }
        })
    };

    JEcharts.doGetDemoData = function (chartType, call) {
        $.ajax({
            url: window._contextPath + '/bigdata/chart/template/data?chartType=' + chartType,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    call(res.data.data);
                }
                else {
                    layer.msg('服务异常' + res.message, {icon: 3});
                }
            },
            error: function () {
                layer.msg('网络异常', {icon: 3});
            }
        })
    };

    JEcharts.doGetDemoOption = function (data, call) {
        $.ajax({
            url: window._contextPath + '/bigdata/echarts/view/editChart',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    call(JSON.parse(res.data));
                }
                else {
                    layer.msg('服务异常' + res.message, {icon: 3});
                }
            },
            error: function () {
                layer.msg('网络异常', {icon: 3});
            }
        });
    };

    JEcharts._contextPath = window._contextPath;
    root.eisCharts = JEcharts;
}).call(this);