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
                                <button type="button" class="btn active" id="overviewJan"><span>近一月</span></button>
                                <button type="button" class="btn" id="overviewMar"><span>近三月</span></button>
                                <button type="button" class="btn" id="overviewAll"><span>全部</span></button>
                            </div>
                        </div>
                        <div class="box-data-content js-two">
                            <div class="domess-chart-box">
                                <div class="box-chart mychart1 domess-chart"></div>
                                <div class="domess-chart-right">
                                    <div class="domess-cr-box">
                                        <div class="domess-chart-num">
                                            <div class="domess-cn-title">公文签收率</div>
                                            <div id="overviewQS">88.4%</div>
                                        </div>
                                        <div class="domess-chart-line"></div>
                                        <div class="domess-chart-num">
                                            <div class="domess-cn-title">活跃用户数</div>
                                            <div ID="overviewYH">994</div>
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
                            <div class="map-num" id="mapOfficeNum">425</div>
                            <div>公文收发数(条)</div>
                        </div>
                        <div class="map-title-basic">
                            <div class="map-num" id="mapMsgNum">2543</div>
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
                                <button type="button" class="btn active" id="messageJan"><span>近一月</span></button>
                                <button type="button" class="btn" id="messageMar"><span>近三月</span></button>
                                <button type="button" class="btn" id="messageAll"><span>全部</span></button>
                            </div>
                        </div>
                        <div class="box-data-content js-two">
                            <div class="domess-chart-box">
                                <div class="box-chart mychart2 domess-chart"></div>
                                <div class="domess-chart-right">
                                    <div class="domess-cr-box">
                                        <div class="domess-chart-num">
                                            <div class="domess-cn-title">消息阅读率</div>
                                            <div id="messageYD">87.3%</div>
                                        </div>
                                        <div class="domess-chart-line"></div>
                                        <div class="domess-chart-num">
                                            <div class="domess-cn-title">活跃用户数</div>
                                            <div id="messageYH">2142</div>
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
                            <div class="box-data-name box-long-border">
                                <span>消息收发属性分布</span>
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
                            <button type="button" class="btn active" id="documentJan"><span>近一月</span></button>
                            <button type="button" class="btn" id="documentMar"><span>近三月</span></button>
                            <button type="button" class="btn" id="documentAll"><span>全部</span></button>
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
                            <button type="button" class="btn active" id="messageDRJan"><span>近一月</span></button>
                            <button type="button" class="btn" id="messageDRMar"><span>近三月</span></button>
                            <button type="button" class="btn" id="messageDRAll"><span>全部</span></button>
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
        $(function () {

            //图表
            var arr = [];
            function resizeChart() {
                for (var i = 0; i < arr.length; i++) {
                    arr[i].resize()
                }
            }

            //公文数据总览
            var mychart1 = echarts.init($('.mychart1')[0]);
            arr.push(mychart1);
            var overviewDataAxis = ['省厅发文', '平台总发文', '平台总收文'];
            var overviewData = [63, 153, 2554];
            function mapmychart1() {
                option = {
                    xAxis: {
                        data: overviewDataAxis,
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
                            data: overviewData
                        }
                    ]
                };
                mychart1.setOption(option)
            }
            mapmychart1();
            $("#overviewJan").click(function () {
                overviewData = [63, 153, 2554];
                $("#overviewQS").html("88.4%")
                $("#overviewYH").html("845")
                mapmychart1();
            })
            $("#overviewMar").click(function () {
                overviewData = [228, 425, 5257];
                $("#overviewQS").html("85.2%")
                $("#overviewYH").html("994")
                mapmychart1();
            })
            $("#overviewAll").click(function () {
                overviewData = [4216, 8254, 175045];
                $("#overviewQS").html("83.2%")
                $("#overviewYH").html("1542")
                mapmychart1();
            })


            // 发文属性分布
            var chartOne = echarts.init($('.partOne')[0]);
            arr.push(chartOne);
            var opOne1 = {
                color: ['#ef913a', '#00cce4', '#9a48d8', '#d242a4', '#1f83f5'],
                legend: {
                    x: 'right',
                    y: '10',
                    data: ['决议', '决定', '命令', '通知', '通报'],
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
                    formatter: "{b}: {c} ({d}%)"
                },
                series: [
                    {
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
                            { value: 15, name: '决议' },
                            { value: 10, name: '决定' },
                            { value: 17, name: '命令' },
                            { value: 22, name: '通知' },
                            { value: 27, name: '通报' }
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
                    formatter: "{b}: {c} ({d}%)"
		        },
                series: [
                    {
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
                            { value: 87, name: '主动公开' },
                            { value: 12, name: '依申请公开' },
                            { value: 5, name: '不予公开' }
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
                    formatter: "{b}: {c} ({d}%)"
		        },
                series: [
                    {
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
                            { value: 2, name: '特提' },
                            { value:8, name: '特急' },
                            { value: 11, name: '紧急' },
                            { value: 79, name: '一般' }
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
            var mychart2 = echarts.init($('.mychart2')[0]);
            arr.push(mychart2);
            var messageDataAxis = ['省厅发消息', '平台发消息', '平台收消息'];
            var messageData = [2874, 7564, 35424];
            function mapmychart2() {
                option = {
                    xAxis: {
                        data: messageDataAxis,
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
                            data: messageData
                        }
                    ]
                };
                mychart2.setOption(option)
            }
            mapmychart2();
            $("#messageJan").click(function () {
                messageData = [2874, 7564, 35424];
                $("#messageYD").html("87.3%")
                $("#messageYH").html("2142")
                mapmychart2();
            })
            $("#messageMar").click(function () {
                messageData = [9524, 29985, 78517];
                $("#messageYD").html("91.2%")
                $("#messageYH").html("4625")
                mapmychart2();
            })
            $("#messageAll").click(function () {
                messageData = [85811, 220472, 1582620];
                $("#messageYD").html("80.5%")
                $("#messageYH").html("5478")
                mapmychart2();
            })


            // 消息收发属性分布
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
                    formatter: "{b}: {c} ({d}%)"
		        },
                series: [
                    {
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
                            { value: 15, name: '一般' },
                            { value: 10, name: '紧急' }
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
                    formatter: "{b}: {c} ({d}%)"
		        },
                calculable: true,
                series: [
                    {
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
                            { value: 6, name: '是' },
                            { value: 3, name: '否' }
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
            var chartThree = echarts.init($('.teach1')[0]);
            arr.push(chartThree);
            var dispatchData=[34,18,17,12,8,3,3,1,1,1,1]
            var receiptData=[339,180,168,121,23,30,30,11,9,8,9,10]
            var opThree1 = {
                    xAxis: {
                        data: ['办公室', '政策法规处', '思想政治工作处','教育督导处','维稳工作处','基础教育处','高等教育处 ','职业教育处','外资办','信息中心 ','支教办'],
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
                        left:55
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
                            data: dispatchData
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
                            data: receiptData
                        }
                    ]
                };
            chartThree.setOption(opThree1);
            var areaDispatchData=[41,35,20,20,19,18,16]
            var areaReceiptData=[142,575,588,508,262,285,225]
            var opThree2 = {
                xAxis: {
                    data: ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市','克孜勒苏柯尔克孜自治州','昌吉回族自治州','阿克苏地区','阿勒泰地区'],
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
                    left:55
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
                        data: areaDispatchData
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
                        data: areaReceiptData
                    }
                ]
            };

            $("#documentJan").click(function () {
                if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
                    dispatchData=[34,18,17,12,8,3,3,1,1,1,1]
                    receiptData=[339,180,168,121,23,30,30,11,9,8,9,10]
                    opThree1.series[0].data=dispatchData;
                    opThree1.series[1].data=dispatchData;
                    chartThree.setOption(opThree1);
                }else{
                    areaDispatchData=[41,35,20,20,19,18,16]
                    areaReceiptData=[142,575,588,508,262,285,225]
                    opThree2.series[0].data=areaDispatchData;
                    opThree2.series[1].data=areaReceiptData;
                    chartThree.setOption(opThree2);
                }
            })
            $("#documentMar").click(function () {
                if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
                    console.log(11111)
                    dispatchData=[152,42,37,35,21,15,9,6,6,3,3]
                    receiptData=[1520,423,378,351,211,146,90,61,62,29,31]
                    opThree1.series[0].data=dispatchData;
                    opThree1.series[1].data=dispatchData;
                    chartThree.setOption(opThree1);
                }else{
                    areaDispatchData=[114,105,85,61,60,56,64]
                    areaReceiptData=[1252,1075,1288,1308,1262,1185,1022]
                    opThree2.series[0].data=areaDispatchData;
                    opThree2.series[1].data=areaReceiptData;
                    chartThree.setOption(opThree2);
                }
            })

            $("#documentAll").click(function () {
                if ($('.js-three .left-btn-wrap li').eq(0).hasClass('active')){
                    dispatchData=[3521,485,378,350,214,152,97,68,67,39,32]
                    receiptData=[35219,48521,3788,3511,2112,1524,979,689,677,398,316]
                    opThree1.series[0].data=dispatchData;
                    opThree1.series[1].data=dispatchData;
                    chartThree.setOption(opThree1);
                }else{
                    areaDispatchData=[1241,1035,920,920,799,787,582]
                    areaReceiptData=[8142,4575,4598,4598,3562,2585,2545]
                    opThree2.series[0].data=areaDispatchData;
                    opThree2.series[1].data=areaReceiptData;
                    chartThree.setOption(opThree2);
                }
            })

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
            var chartFour = echarts.init($('.teach2')[0]);
            arr.push(chartFour);
            var provinceMessageDispatchData=[215,181,173,123,102,96,88,74,66,65,51];
            var provinceMessageReceiveData=[1070,900,851,601,501,499,450,354,328,325,256];
            var opFour1 = {
                xAxis: {
                    data: ['信息中心 ', '政策法规处','思想政治工作处','教育督导处','维稳工作处','基础教育处','办公室','高等教育处','职业教育处','外资办','支教办'],
                    axisLabel: {
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
                    left:55
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
                        data: provinceMessageDispatchData
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
                        data: provinceMessageReceiveData
                    }
                ]
            };
            var areaMessageDispatchData=[1502,1254,1102,9582,5811,2411,1852];
            var areaMessageReceiveData=[4500,3752,3301,28504,17532,7215,5536];
            var opFour2 = {
                xAxis: {
                    data: ['克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '和田地区','乌鲁木齐市','昌吉回族自治州','阿克苏地区','阿勒泰地区'],
                    axisLabel: {
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
                    left:55
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
                        data: areaMessageDispatchData
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
                        data: areaMessageReceiveData
                    }
                ]
            };
            chartFour.setOption(opFour1);

            $("#messageDRJan").click(function () {
                if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
                    provinceMessageDispatchData=[215,181,173,123,102,96,88,74,66,65,51];
                    provinceMessageReceiveData=[1070,900,851,601,501,499,450,354,328,325,256];
                    opFour1.series[0].data=provinceMessageDispatchData;
                    opFour1.series[1].data=provinceMessageReceiveData;
                    chartFour.setOption(opFour1);
                }else{
                    areaMessageDispatchData=[1502,1254,1102,9582,5811,2411,1852];
                    areaMessageReceiveData=[4500,3752,3301,28504,17532,7215,5536];
                    opFour2.series[0].data=areaMessageDispatchData;
                    opFour2.series[1].data=areaMessageReceiveData;
                    chartFour.setOption(opFour2);
                }
            })
            $("#messageDRMar").click(function () {
                if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
                    provinceMessageDispatchData=[730,241,519,336,331,310,240,221,187,190,55];
                    provinceMessageReceiveData=[3250,2710,2510,1820,1520,1503,1356,1050,984,980,780];
                    opFour1.series[0].data=provinceMessageDispatchData;
                    opFour1.series[1].data=provinceMessageReceiveData;
                    chartFour.setOption(opFour1);
                }else{
                    areaMessageDispatchData=[4500,3752,3301,28504,17532,7215,5536];
                    areaMessageReceiveData=[13500,13920,13250,84560,51263,21455,16589];
                    opFour2.series[0].data=areaMessageDispatchData;
                    opFour2.series[1].data=areaMessageReceiveData;
                    chartFour.setOption(opFour2);                }
            })
            //数据不完整
            $("#messageDRAll").click(function () {
                if ($('.js-four .left-btn-wrap li').eq(0).hasClass('active')){
                    provinceMessageDispatchData=[9150,8813,4734,3237,2020,1796,1988,1774,1666,1965,1451];
                    provinceMessageReceiveData=[45810,44860,23500,16120,10956,9860,9860,8550,8650,9984,7500];
                    opFour1.series[0].data=provinceMessageDispatchData;
                    opFour1.series[1].data=provinceMessageReceiveData;
                    chartFour.setOption(opFour1);
                }else{
                    areaMessageDispatchData=[18009,14450,13249,115782,6850,49620,21560];
                    areaMessageReceiveData=[54089,42560,40156,335899,19875,155600,65540];
                    opFour2.series[0].data=areaMessageDispatchData;
                    opFour2.series[1].data=areaMessageReceiveData;
                    chartFour.setOption(opFour2);                }
            })



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
                                {name: '乌鲁木齐市', officeNum: 8574,msgNum:50057,countNum:50057},
                                {name: '克拉玛依市',  officeNum: 8500,msgNum:42312,countNum:42312},
                                {name: '吐鲁番市',  officeNum: 8459,msgNum:39654,countNum:39654},
                                {name: '哈密市',  officeNum: 8370,msgNum:48510,countNum:48510},
                                {name: '阿克苏地区',  officeNum: 8780,msgNum:35210,countNum:35210},
                                {name: '喀什地区', officeNum: 8250,msgNum:29870,countNum:29870},
                                {name: '和田地区',  officeNum: 7890,msgNum:45210,countNum:45210},
                                {name: '昌吉回族自治州',  officeNum: 7680,msgNum:49501,countNum:49501},
                                {name: '博尔塔拉蒙古自治州',  officeNum: 9810,msgNum:39571,countNum:39571},
                                {name: '巴音郭楞蒙古自治州',  officeNum: 9301,msgNum:42586,countNum:42586},
                                {name: '克孜勒苏柯尔克孜自治州',  officeNum: 8652,msgNum:46154,countNum:46154},
                                {name: '伊犁哈萨克自治州',  officeNum: 8539,msgNum:41256,countNum:41256},
                                {name: '塔城地区', officeNum: 8878,msgNum:48762,countNum:48762},
                                {name: '阿勒泰地区',  officeNum: 8921,msgNum:36875,countNum:36875},
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

    </script>
</body>

</html>