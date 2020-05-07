<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>公文消息数据监控</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>

<body class="domess-box">
<!--头部 S-->
<div class="head clearfix domess-header">
    <div class="titl-wrap">
        <span>公文消息数据监控</span>
    </div>
    <div class="full-screen-wrap">
        <div class="full-screen-btn">
            <div class="btn-inner"></div>
        </div>
    </div>
</div>
<!--头部 E-->

<!--主体 S-->
<div class="main-container">
    <div class="wrap-full ">

        <div class="domess-left-box">
            <!-- 公文数据总览 -->
            <div class="box-data-wrap">
                <div class="box-data">
                    <div class="box-data-head clearfix">
                        <div class="box-data-name">
                            <span>公文数据总览</span>
                        </div>
                        <div class="box-data-scope clearfix js-data-pandect">
                            <button type="button" class="btn active" onclick="officedoc30()"><span>近一月</span></button>
                            <button type="button" class="btn" onclick="officedoc90()"><span>近三月</span></button>
                            <button type="button" class="btn" onclick="officedocAll()"><span>全部</span></button>
                        </div>
                    </div>
                    <div class="box-data-content js-two">
                        <div class="domess-chart-box">
                            <div class="box-chart mychart1 domess-chart"></div>
                            <div class="domess-chart-right">
                                <div class="domess-cr-box">
                                    <div class="domess-chart-num">
                                        <div class="domess-cn-title">公文签收率</div>
                                        <div id="sign_num"><#if officedoc30??>${officedoc30.sign_num?string("0.00%")}</#if></div>
                                    </div>
                                    <div class="domess-chart-line"></div>
                                    <div class="domess-chart-num">
                                        <div class="domess-cn-title">签收平均间隔</div>
                                        <div id="average_num"><#if officedoc30??>${officedoc30.average_num!}天</#if></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 发文属性分布 -->
            <div class="box-data-wrap ">
                <div class="box-data">
                    <div class="box-data-head clearfix">
                        <div class="box-data-name">
                            <span>发文属性分布</span>
                        </div>
                    </div>
                    <div class="box-data-content clearfix js-one">
                        <div class="left-btn-wrap">
                            <ul>
                                <li class="active" data-num="1382" data-option="opOne1">发文类型</li>
                                <li data-num="257" data-option="opOne2">公开属性</li>
                                <li data-num="51498" data-option="opOne3">紧急程度</li>
                            </ul>
                        </div>
                        <div class="right-chart-wrap">
                            <div class="right-chart-content">
                                <div class="box-chart partOne"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 地图-->
        <div class="domess-left-box">
            <div class="domess-map-box">
                <div id="map" class="box-chart"></div>
            </div>
            <div class="domess-map-title">
                <div class="map-title-first bold" id="mapAreaName">乌鲁木齐市</div>
                <div class="map-title-secound">
                    <div class="map-title-basic">
                        <div class="map-num" id="mapOfficeNum">${WLMQ[0]}</div>
                        <div>公文收发数(条)</div>
                    </div>
                    <div class="map-title-basic">
                        <div class="map-num" id="mapMsgNum">${WLMQ[1]}</div>
                        <div>消息收发数(条)</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="domess-left-box">
            <!-- 消息数据总览 -->
            <div class="box-data-wrap">
                <div class="box-data">
                    <div class="box-data-head clearfix">
                        <div class="box-data-name">
                            <span>消息数据总览</span>
                        </div>
                        <div class="box-data-scope clearfix js-data-pandect">
                            <!-- <div class="btn-group filter">
                                <button class="btn dropdown-toggle">近一周 <i
                                        class="wpfont icon-angle-single-down"></i></button>
                                <ul class="dropdown-menu">
                                    <li><a href="#">全部</a></li>
                                    <li><a href="#">近一月</a></li>
                                    <li><a href="#">近一周</a></li>
                                </ul>
                            </div> -->
                            <button type="button" class="btn active" onclick="msg30()"><span>近一月</span></button>
                            <button type="button" class="btn" onclick="msg90()"><span>近三月</span></button>
                            <button type="button" class="btn" onclick="msgAll()"><span>全部</span></button>
                        </div>
                    </div>
                    <div class="box-data-content js-two">
                        <div class="domess-chart-box">
                            <div class="box-chart mychart2 domess-chart"></div>
                            <div class="domess-chart-right">
                                <div class="domess-cr-box">
                                    <div class="domess-chart-num">
                                        <div class="domess-cn-title">消息阅读率</div>
                                        <div id="read_num"><#if msg30??>${msg30.read_num?string("0.00%")}</#if></div>
                                    </div>
                                    <div class="domess-chart-line"></div>
                                    <div class="domess-chart-num">
                                        <div class="domess-cn-title">活跃用户数</div>
                                        <div id="actuser_num"><#if msg30??>${msg30.actuser_num}人</#if></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 消息属性分布 -->
            <div class="box-data-wrap ">
                <div class="box-data">
                    <div class="box-data-head clearfix">
                        <div class="box-data-name">
                            <span>消息属性分布</span>
                        </div>
                    </div>
                    <div class="box-data-content clearfix js-two">
                        <div class="left-btn-wrap">
                            <ul>
                                <li class="active" data-num="1382" data-option="opTwo1">紧急程度</li>
                                <li data-num="257" data-option="opTwo2">是否待办</li>
                            </ul>
                        </div>
                        <div class="right-chart-wrap">
                            <div class="right-chart-content">
                                <div class="box-chart partTwo"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 公文数据总览 -->
        <div class="box-data-wrap box-data-wrap-big">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>公文收发情况</span>
                    </div>
                    <div class="box-data-scope clearfix js-data-pandect">
                        <button type="button" class="btn active" onclick="xinjiangDept30()"><span>近一月</span></button>
                        <button type="button" class="btn" onclick="xinjiangDept90()"><span>近三月</span></button>
                        <button type="button" class="btn" onclick="xinjiangDeptAll()"><span>全部</span></button>
                    </div>
                </div>
                <div class="box-data-content clearfix js-three">
                    <div class="left-btn-wrap">
                        <ul>
                            <li class="active" data-num="1382" data-option="opThree1">省厅各部门</li>
                            <li data-num="257" data-option="opThree2">各地区</li>
                        </ul>
                    </div>
                    <div class="right-chart-wrap">
                        <div class="right-chart-content">
                            <div class="box-chart teach1"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 消息收发情况 -->
        <div class="box-data-wrap box-data-wrap-big">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>消息收发情况</span>
                    </div>
                    <div class="box-data-scope clearfix js-data-pandect">
                        <button type="button" class="btn active" onclick="xinjiangDeptMsg30()"><span>近一月</span></button>
                        <button type="button" class="btn" onclick="xinjiangDeptMsg90()"><span>近三月</span></button>
                        <button type="button" class="btn" onclick="xinjiangDeptMsgAll()"><span>全部</span></button>
                    </div>
                </div>
                <div class="box-data-content clearfix js-four">
                    <div class="left-btn-wrap">
                        <ul>
                            <li class="active" data-num="1382" data-option="opFour1">省厅各部门</li>
                            <li data-num="257" data-option="opFour2">各地区</li>
                        </ul>
                    </div>
                    <div class="right-chart-wrap">
                        <div class="right-chart-content">
                            <div class="box-chart teach2"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--主体 E-->

<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.all.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
<script>

    // 公文收发情况
    var chartThree = echarts.init($('.teach1')[0]);
    var opThree1 = {
        xAxis: {
            data: [<#list xinjiangDept30 as dept>'${dept.dept_short_name!}',</#list>],
            axisLabel: {
                showMaxLabel: true,
                textStyle: {
                    color: '#fff'
                },
                interval: 0,//标签设置为全部显示
                formatter: function (value, index) {
                    var v = value.substring(0, 3) + '...';
                    return value.length > 3 ? v : value;
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            min: 0,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
        },
        tooltip: {
            trigger: 'axis',
        },
        series: [
            {
                name: '发文',
                type: 'bar',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: [<#list xinjiangDept30 as dept>'${dept.send_num!}',</#list>]
            },
            {
                name: '收文',
                type: 'line',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: '#d242a4'
                    }
                },
                data: [<#list xinjiangDept30 as dept>'${dept.receive_num!}',</#list>]
            }
        ]
    };
    var opThree2 = {
        xAxis: {
            data: [<#list xinjiangArea30 as area>'${area.region_name!}',</#list>],
            axisLabel: {
                showMaxLabel: true,
                textStyle: {
                    color: '#fff'
                },
                interval: 0,//标签设置为全部显示
                formatter: function (value, index) {
                    var v = value.substring(0, 3) + '...';
                    return value.length > 3 ? v : value;
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            min: 0,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
        },
        tooltip: {
            trigger: 'axis',
        },
        series: [
            {
                name: '发文',
                type: 'bar',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: [<#list xinjiangArea30 as area>'${area.send_num!}',</#list>]
            },
            {
                name: '收文',
                type: 'line',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: '#d242a4'
                    }
                },
                data: [<#list xinjiangArea30 as area>'${area.receive_num!}',</#list>]
            }
        ]
    };

    //公文数据总览
    var mychart1 = echarts.init($('.mychart1')[0]);
    var officeOption = {
        xAxis: {
            data: ['省厅发文', '平台总发文', '平台总收文'],
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#158EE9'
                }
            },
            min: 100,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
            left:55
        },
        tooltip: {
            trigger: 'item',
        },
        series: [
            {
                type: 'bar',
                barWidth: 30,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        color:"#fff"
                    }
                },
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: ['<#if xinjiangOfficedoc30??>${xinjiangOfficedoc30.send_month_num!}</#if>',
                    '<#if officedoc30??>${officedoc30.send_num!}</#if>',
                    '<#if officedoc30??>${officedoc30.receive_num!}</#if>']
            }
        ]
    };

    //消息数据总览
    var mychart2 = echarts.init($('.mychart2')[0]);
    var msgOption = {
        xAxis: {
            data: ['省厅发消息', '平台发消息', '平台收消息'],
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#158EE9'
                }
            },
            min: 100,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
            left:55
        },
        tooltip: {
            trigger: 'item',
        },
        series: [
            {
                type: 'bar',
                barWidth: 30,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        color:"#fff"
                    }
                },
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: ['<#if xinjiangMsg30??>${xinjiangMsg30.send_month_num}</#if>',
                    '<#if msg30??>${msg30.send_num}</#if>',
                    '<#if msg30??>${msg30.receive_num}</#if>']
            }
        ]
    };

    // 消息收发情况
    var chartFour = echarts.init($('.teach2')[0]);
    var opFour1 = {
        xAxis: {
            data: [<#list xinjiangDeptMsg30 as msg>'${msg.dept_short_name!}',</#list>],
            axisLabel: {
                showMaxLabel: true,
                textStyle: {
                    color: '#fff'
                },
                interval: 0,//标签设置为全部显示
                formatter: function (value, index) {
                    var v = value.substring(0, 3) + '...';
                    return value.length > 3 ? v : value;
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            min: 0,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
        },
        tooltip: {
            trigger: 'axis',
        },
        series: [
            {
                name: '发文',
                type: 'bar',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: [<#list xinjiangDeptMsg30 as msg>'${msg.send_num!}',</#list>]
            },
            {
                name: '收文',
                type: 'line',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: '#d242a4'
                    }
                },
                data: [<#list xinjiangDeptMsg30 as msg>'${msg.receive_num!}',</#list>]
            }
        ]
    };
    var opFour2 = {
        xAxis: {
            data: [<#list xinjiangAreaMsg30 as area>'${area.region_name!}',</#list>],
            axisLabel: {
                showMaxLabel: true,
                textStyle: {
                    color: '#fff'
                },
                interval: 0,//标签设置为全部显示
                formatter: function (value, index) {
                    var v = value.substring(0, 3) + '...';
                    return value.length > 3 ? v : value;
                }
            },
            axisTick: { show: false },
            axisLine: { show: false },
            z: 10
        },
        yAxis: {
            axisLine: { show: false },
            axisTick: { show: false },
            axisLabel: {
                textStyle: {
                    color: '#fff'
                }
            },
            min: 0,
            splitLine: { show: false }
        },
        grid: {
            x: 40,
            y: 30,
            x2: 0,
            y2: 30,
        },
        tooltip: {
            trigger: 'axis',
        },
        series: [
            {
                name: '发文',
                type: 'bar',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#70c7f2' },
                                { offset: 0.5, color: '#0290d7' },
                                { offset: 1, color: '#0290d7' }
                            ]
                        )
                    },
                    emphasis: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ]
                        )
                    }
                },
                data: [<#list xinjiangAreaMsg30 as area>'${area.send_num!}',</#list>]
            },
            {
                name: '收文',
                type: 'line',
                barWidth: 30,
                itemStyle: {
                    normal: {
                        color: '#d242a4'
                    }
                },
                data: [<#list xinjiangAreaMsg30 as area>'${area.receive_num!}',</#list>]
            }
        ]
    };

    $(function () {

        //图表
        var arr = [];
        function resizeChart() {
            for (var i = 0; i < arr.length; i++) {
                arr[i].resize()
            }
        }

        //公文数据总览

        arr.push(mychart1);
        function mapmychart1() {

            mychart1.setOption(officeOption)
        }
        mapmychart1();

        // 发文属性分布
        var chartOne = echarts.init($('.partOne')[0]);
        arr.push(chartOne);
        var opOne1 = {
            color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
            legend: {
                x: 'right',
                y: '10',
                data: [<#if sendTypeLists?exists && sendTypeLists?size gt 0>
                    <#list sendTypeLists as sendTypeList>'${sendTypeList.name!}', </#list></#if>'其他'],
                orient: 'vertical',
                textStyle: {
                    color: '#fff'
                },
                itemWidth: 13,
                itemHeight: 13,
            },
            calculable: true,
            tooltip: {
                trigger: 'item',
            },
            series: [
                {
                    name: '半径模式',
                    type: 'pie',
                    radius: [20, 70],
                    center: ['50%', '50%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: [
                        <#if sendTypeLists?exists && sendTypeLists?size gt 0>
                        <#list sendTypeLists as sendTypeList>
                        { value: ${sendTypeList.value!}, name: '${sendTypeList.name!}' },
                        </#list>
                        </#if>
                        { value: <#if sendTypeOther??>${sendTypeOther!}</#if>, name: '其他' }
                    ]
                }
            ]
        };
        var opOne2 = {
            color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
            legend: {
                x: 'right',
                y: '10',
                data: ['主动公开', '依申请公开', '不予公开'],
                orient: 'vertical',
                textStyle: {
                    color: '#fff'
                },
                itemWidth: 13,
                itemHeight: 13,
            },
            calculable: true,
            tooltip: {
                trigger: 'item',
            },
            series: [
                {
                    name: '半径模式',
                    type: 'pie',
                    radius: [20, 70],
                    center: ['50%', '50%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: [
                        { value: <#if sendProp??>${sendProp.send_prop_pereopen}<#else >''</#if>, name: '主动公开' },
                        { value: <#if sendProp??>${sendProp.send_prop_applipublic}<#else >''</#if>, name: '依申请公开' },
                        { value: <#if sendProp??>${sendProp.send_prop_noapplipublic}<#else >''</#if>, name: '不予公开' }
                    ]
                }
            ]
        };
        var opOne3 = {
            color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
            legend: {
                x: 'right',
                y: '10',
                data: ['特提', '特急', '紧急', '一般'],
                orient: 'vertical',
                textStyle: {
                    color: '#fff'
                },
                itemWidth: 13,
                itemHeight: 13,
            },
            calculable: true,
            tooltip: {
                trigger: 'item',
            },
            series: [
                {
                    name: '半径模式',
                    type: 'pie',
                    radius: [20, 70],
                    center: ['50%', '50%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: [
                        { value: <#if urgencyDegree??>${urgencyDegree.teti}<#else >''</#if>, name: '特提' },
                        { value: <#if urgencyDegree??>${urgencyDegree.send_extraurgent}<#else >''</#if>, name: '特急' },
                        { value: <#if urgencyDegree??>${urgencyDegree.send_urgent}<#else >''</#if>, name: '紧急' },
                        { value: <#if urgencyDegree??>${urgencyDegree.send_commonly}<#else >''</#if>, name: '一般' }
                    ]
                }
            ]
        };
        chartOne.setOption(opOne1);
        var timeNum1 = 0;
        var timer1 = setInterval(slide, 5000);
        $('.js-one .left-btn-wrap li').on('click', function () {
            clearInterval(timer1);
            eval('chartOne.setOption(' + $(this).data('option') + ')');
            chartOne.resize();
            timeNum1 = $(this).index();
            timer1 = setInterval(slide, 5000);
        });
        function slide() {
            timeNum1++;
            if (timeNum1 == $('.js-one li').length) {
                timeNum1 = 0
            }
            var $target = $('.js-one li').eq(timeNum1);
            $target.addClass('active').siblings().removeClass('active');
            eval('chartOne.setOption(' + $target.data('option') + ')');
            chartOne.resize();
        }

        //消息数据总览

        arr.push(mychart2);
        function mapmychart2() {

            mychart2.setOption(msgOption)
        }
        mapmychart2();

        // 消息属性分布
        var chartTwo = echarts.init($('.partTwo')[0]);
        arr.push(chartTwo);
        var opTwo1 = {
            color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
            legend: {
                x: 'right',
                y: '10',
                data: ['一般', '紧急'],
                orient: 'vertical',
                textStyle: {
                    color: '#fff'
                },
                itemWidth: 13,
                itemHeight: 13,
            },
            calculable: true,
            tooltip: {
                trigger: 'item',
            },
            series: [
                {
                    name: '半径模式',
                    type: 'pie',
                    radius: [20, 70],
                    center: ['50%', '50%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: [
                        { value: <#if msgProp??>${msgProp.msg_commonly}<#else >''</#if>, name: '一般' },
                        { value: <#if msgProp??>${msgProp.msg_emergency}<#else >''</#if>, name: '紧急' }
                    ]
                }
            ]
        };
        var opTwo2 = {
            color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
            legend: {
                x: 'right',
                y: '10',
                data: ['是', '否'],
                orient: 'vertical',
                textStyle: {
                    color: '#fff'
                },
                itemWidth: 13,
                itemHeight: 13,
            },
            tooltip: {
                trigger: 'item',
            },
            calculable: true,
            series: [
                {
                    name: '半径模式',
                    type: 'pie',
                    radius: [20, 70],
                    center: ['50%', '50%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    lableLine: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: [
                        { value: <#if msgAgency??>${msgAgency.agency_yes}<#else >''</#if>, name: '是' },
                        { value: <#if msgAgency??>${msgAgency.agency_no}<#else >''</#if>, name: '否' }
                    ]
                }
            ]
        };
        chartTwo.setOption(opTwo1);
        var timeNum2 = 0;
        var timer2 = setInterval(slidetwo, 5000);
        $('.js-two .left-btn-wrap li').on('click', function () {
            clearInterval(timer2);
            eval('chartTwo.setOption(' + $(this).data('option') + ')');
            chartTwo.resize();
            timeNum2 = $(this).index();
            timer2 = setInterval(slidetwo, 5000);
        });
        function slidetwo() {
            timeNum2++;
            if (timeNum2 == $('.js-two li').length) {
                timeNum2 = 0
            }
            var $target = $('.js-two li').eq(timeNum2);
            $target.addClass('active').siblings().removeClass('active');
            eval('chartTwo.setOption(' + $target.data('option') + ')');
            chartTwo.resize();
        }


        // 公文收发情况
        arr.push(chartThree);
        chartThree.setOption(opThree1);
        var timeNum3 = 0;
        var timer3 = setInterval(slidethree, 5000);
        $('.js-three .left-btn-wrap li').on('click', function () {
            clearInterval(timer3);
            eval('chartThree.setOption(' + $(this).data('option') + ')');
            chartThree.resize();
            timeNum3 = $(this).index();
            timer3 = setInterval(slidethree, 5000);
        });
        function slidethree() {
            timeNum3++;
            if (timeNum3 == $('.js-three li').length) {
                timeNum3 = 0
            }
            var $target = $('.js-three li').eq(timeNum3);
            $target.addClass('active').siblings().removeClass('active');
            eval('chartThree.setOption(' + $target.data('option') + ')');
            chartThree.resize();
        }


        // 消息收发情况
        arr.push(chartFour);
        chartFour.setOption(opFour1);
        var timeNum4 = 0;
        var timer4 = setInterval(slidefour, 5000);
        $('.js-four .left-btn-wrap li').on('click', function () {
            clearInterval(timer4);
            eval('chartFour.setOption(' + $(this).data('option') + ')');
            chartFour.resize();
            timeNum4 = $(this).index();
            timer4 = setInterval(slidefour, 5000);
        });
        function slidefour() {
            timeNum4++;
            if (timeNum4 == $('.js-four li').length) {
                timeNum4 = 0
            }
            var $target = $('.js-four li').eq(timeNum4);
            $target.addClass('active').siblings().removeClass('active');
            eval('chartFour.setOption(' + $target.data('option') + ')');
            chartFour.resize();
        }


        // 地图
        var myChart = echarts.init(document.getElementById('map'));
        arr.push(myChart);
        $.get('${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/xinjiang2.json', function (geoJson) {

            myChart.hideLoading();

            echarts.registerMap('xinjiang', geoJson);

            myChart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: function (params) {
                        $("#mapAreaName").html(params.data['name']);
                        $("#mapOfficeNum").html(params.data['officeNum']);
                        $("#mapMsgNum").html(params.data['msgNum']);
                        return params.data['name']+'<br/>'+params.data['countNum'] +'(条)';
                    }
                },
                geo: {
                    map: 'xinjiang',
                    zoom: 1.1,
                    itemStyle: {
                        normal: {
                            borderWidth: 1,
                            borderColor: 'rgba(0, 222, 255, 1)',
                            shadowBlur: 12,
                            shadowColor: 'rgba(0, 222, 255, 1)'
                        }
                    }
                },
                visualMap: {
                    min: 10,
                    max: 50000,
                    text: ['高', '低'],
                    textStyle: {
                        color: '#fff'
                    },
                    realtime: true,
                    calculable: true,
                    inRange: {
                        color: ['#081f3a', '#1f83f5', '#1ebcd3']
                    },
                    itemHeight: 100
                },
                series: [
                    {
                        name: '公文消息使用热度',
                        type: 'map',
                        zoom: 1.1,
                        mapType: 'xinjiang', // 自定义扩展图表类型
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                borderColor: '#015678',
                                areaColor: '#081f3a'
                            }
                        },
                        data: [
                            {name: '乌鲁木齐市',officeNum:${WLMQ[0]},msgNum:${WLMQ[1]},countNum:${WLMQ[2]}},
                            {name: '克拉玛依市',officeNum:${KLMY[0]},msgNum:${KLMY[1]},countNum:${KLMY[2]}},
                            {name: '吐鲁番市',officeNum:${TLF[0]},msgNum:${TLF[1]},countNum:${TLF[2]}},
                            {name: '哈密市',officeNum:${HM[0]},msgNum:${HM[1]},countNum:${HM[2]}},
                            {name: '阿克苏地区',officeNum:${AKS[0]},msgNum:${AKS[1]},countNum:${AKS[2]}},
                            {name: '喀什地区',officeNum:${KS[0]},msgNum:${KS[1]},countNum:${KS[2]}},
                            {name: '和田地区',officeNum:${HT[0]},msgNum:${HT[1]},countNum:${HT[2]}},
                            {name: '昌吉回族自治州',officeNum:${CJHZ[0]},msgNum:${CJHZ[1]},countNum:${CJHZ[2]}},
                            {name: '博尔塔拉蒙古自治州',officeNum:${BETL[0]},msgNum:${BETL[1]},countNum:${BETL[2]}},
                            {name: '巴音郭楞蒙古自治州',officeNum:${BYGL[0]},msgNum:${BYGL[1]},countNum:${BYGL[2]}},
                            {name: '克孜勒苏柯尔克孜自治州',officeNum:${KZLS[0]},msgNum:${KZLS[1]},countNum:${KZLS[2]}},
                            {name: '伊犁哈萨克自治州',officeNum:${YLHSK[0]},msgNum:${YLHSK[1]},countNum:${YLHSK[2]}},
                            {name: '塔城地区',officeNum:${TC[0]},msgNum:${TC[1]},countNum:${TC[2]}},
                            {name: '阿勒泰地区',officeNum:${ALT[0]},msgNum:${ALT[1]},countNum:${ALT[2]}}
                        ]
                    }
                ]
            });
        });


        //窗口变化，图表resize
        $(window).resize(function () {
            resizeChart();
        })


    })

    function circle() {}

    function btnOverstep(){}

    //公文数据总览
    function officedoc30() {
        $("#sign_num").html("<#if officedoc30??>${officedoc30.sign_num?string("0.00%")}</#if>");
        $("#average_num").html("<#if officedoc30??>${officedoc30.average_num}天</#if>");
        officeOption.series[0].data=['<#if xinjiangOfficedoc30??>${xinjiangOfficedoc30.send_month_num!}</#if>',
            '<#if officedoc30??>${officedoc30.send_num!}</#if>',
            '<#if officedoc30??>${officedoc30.receive_num!}</#if>'];
        mychart1.setOption(officeOption);
    }
    function officedoc90() {
        $("#sign_num").html("<#if officedoc90??>${officedoc90.sign_num?string("0.00%")}</#if>");
        $("#average_num").html("<#if officedoc90??>${officedoc90.average_num}天</#if>");
        officeOption.series[0].data=['<#if xinjiangOfficedoc90??>${xinjiangOfficedoc90.send_month_num!}</#if>',
            '<#if officedoc90??>${officedoc90.send_num!}</#if>',
            '<#if officedoc90??>${officedoc90.receive_num!}</#if>'];
        mychart1.setOption(officeOption)
    }
    function officedocAll() {
        $("#sign_num").html("<#if officedocAll??>${officedocAll.sign_num?string("0.00%")}</#if>");
        $("#average_num").html("<#if officedocAll??>${officedocAll.average_num}天</#if>");
        officeOption.series[0].data=['<#if xinjiangOfficedocAll??>${xinjiangOfficedocAll.send_num!}</#if>',
            '<#if officedocAll??>${officedocAll.send_num!}</#if>',
            '<#if officedocAll??>${officedocAll.receive_num!}</#if>'];
        mychart1.setOption(officeOption)
    }

    //消息数据总览
    function msg30() {
        $("#read_num").html("<#if msg30??>${msg30.read_num?string("0.00%")}</#if>");
        $("#actuser_num").html("<#if msg30??>${msg30.actuser_num}人</#if>");
        msgOption.series[0].data=['<#if xinjiangMsg30??>${xinjiangMsg30.send_month_num}</#if>',
            '<#if msg30??>${msg30.send_num}</#if>',
            '<#if msg30??>${msg30.receive_num}</#if>'];
        mychart2.setOption(msgOption)
    }

    function msg90() {
        $("#read_num").html("<#if msg90??>${msg90.read_num?string("0.00%")}</#if>");
        $("#actuser_num").html("<#if msg90??>${msg90.actuser_num}人</#if>");
        msgOption.series[0].data=['<#if xinjiangMsg90??>${xinjiangMsg90.send_month_num}</#if>',
            '<#if msg90??>${msg90.send_num}</#if>',
            '<#if msg90??>${msg90.receive_num}</#if>'];
        mychart2.setOption(msgOption)
    }

    function msgAll() {
        $("#read_num").html("<#if msgAll??>${msgAll.read_num?string("0.00%")}</#if>");
        $("#actuser_num").html("<#if msgAll??>${msgAll.actuser_num}人</#if>");
        msgOption.series[0].data=['<#if xinjiangMsgAll??>${xinjiangMsgAll.send_num}</#if>',
            '<#if msgAll??>${msgAll.send_num}</#if>',
            '<#if msgAll??>${msgAll.receive_num}</#if>'];
        mychart2.setOption(msgOption)
    }

    function xinjiangDept30() {
        opThree1.xAxis.data=[<#list xinjiangDept30 as dept>'${dept.dept_short_name!}',</#list>];
        opThree1.series[0].data=[<#list xinjiangDept30 as dept>'${dept.send_num!}',</#list>];
        opThree1.series[1].data=[<#list xinjiangDept30 as dept>'${dept.receive_num!}',</#list>];
        opThree2.xAxis.data=[<#list xinjiangArea30 as area>'${area.region_name!}',</#list>];
        opThree2.series[0].data=[<#list xinjiangArea30 as area>'${area.send_num!}',</#list>];
        opThree2.series[1].data=[<#list xinjiangArea30 as area>'${area.receive_num!}',</#list>];
        if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
            chartThree.setOption(opThree1);
        }else{
            chartThree.setOption(opThree2);
        }
    }
    function xinjiangDept90() {
        opThree1.xAxis.data=[<#list xinjiangDept90 as dept>'${dept.dept_short_name!}',</#list>];
        opThree1.series[0].data=[<#list xinjiangDept90 as dept>'${dept.send_num!}',</#list>];
        opThree1.series[1].data=[<#list xinjiangDept90 as dept>'${dept.receive_num!}',</#list>];
        opThree2.xAxis.data=[<#list xinjiangArea90 as area>'${area.region_name!}',</#list>];
        opThree2.series[0].data=[<#list xinjiangArea90 as area>'${area.send_num!}',</#list>];
        opThree2.series[1].data=[<#list xinjiangArea90 as area>'${area.receive_num!}',</#list>];
        if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
            chartThree.setOption(opThree1);
        }else{
            chartThree.setOption(opThree2);
        }
    }
    function xinjiangDeptAll() {
        opThree1.xAxis.data=[<#list xinjiangDeptAll as dept>'${dept.dept_short_name!}',</#list>];
        opThree1.series[0].data=[<#list xinjiangDeptAll as dept>'${dept.send_num!}',</#list>];
        opThree1.series[1].data=[<#list xinjiangDeptAll as dept>'${dept.receive_num!}',</#list>];
        opThree2.xAxis.data=[<#list xinjiangAreaAll as area>'${area.region_name!}',</#list>];
        opThree2.series[0].data=[<#list xinjiangAreaAll as area>'${area.send_num!}',</#list>];
        opThree2.series[1].data=[<#list xinjiangAreaAll as area>'${area.receive_num!}',</#list>];
        if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
            chartThree.setOption(opThree1);
        }else{
            chartThree.setOption(opThree2);
        }
    }

    function xinjiangDeptMsg30() {
        opFour1.xAxis.data=[<#list xinjiangDeptMsg30 as msg>'${msg.dept_short_name!}',</#list>];
        opFour1.series[0].data=[<#list xinjiangDeptMsg30 as msg>'${msg.send_num!}',</#list>];
        opFour1.series[1].data=[<#list xinjiangDeptMsg30 as msg>'${msg.receive_num!}',</#list>];
        opFour2.xAxis.data=[<#list xinjiangAreaMsg30 as area>'${area.region_name!}',</#list>];
        opFour2.series[0].data=[<#list xinjiangAreaMsg30 as area>'${area.send_num!}',</#list>];
        opFour2.series[1].data=[<#list xinjiangAreaMsg30 as area>'${area.receive_num!}',</#list>];
        if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
            chartFour.setOption(opFour1);
        }else{
            chartFour.setOption(opFour2);
        }
    }
    function xinjiangDeptMsg90() {
        opFour1.xAxis.data=[<#list xinjiangDeptMsg90 as msg>'${msg.dept_short_name!}',</#list>];
        opFour1.series[0].data=[<#list xinjiangDeptMsg90 as msg>'${msg.send_num!}',</#list>];
        opFour1.series[1].data=[<#list xinjiangDeptMsg90 as msg>'${msg.receive_num!}',</#list>];
        opFour2.xAxis.data=[<#list xinjiangAreaMsg90 as area>'${area.region_name!}',</#list>];
        opFour2.series[0].data=[<#list xinjiangAreaMsg90 as area>'${area.send_num!}',</#list>];
        opFour2.series[1].data=[<#list xinjiangAreaMsg90 as area>'${area.receive_num!}',</#list>];
        if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
            chartFour.setOption(opFour1);
        }else{
            chartFour.setOption(opFour2);
        }
    }
    function xinjiangDeptMsgAll() {
        opFour1.xAxis.data=[<#list xinjiangDeptMsgAll as msg>'${msg.dept_short_name!}',</#list>];
        opFour1.series[0].data=[<#list xinjiangDeptMsgAll as msg>'${msg.send_num!}',</#list>];
        opFour1.series[1].data=[<#list xinjiangDeptMsgAll as msg>'${msg.receive_num!}',</#list>];
        opFour2.xAxis.data=[<#list xinjiangAreaMsgAll as area>'${area.region_name!}',</#list>];
        opFour2.series[0].data=[<#list xinjiangAreaMsgAll as area>'${area.send_num!}',</#list>];
        opFour2.series[1].data=[<#list xinjiangAreaMsgAll as area>'${area.receive_num!}',</#list>];
        if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
            chartFour.setOption(opFour1);
        }else{
            chartFour.setOption(opFour2);
        }
    }

</script>
</body>

</html>