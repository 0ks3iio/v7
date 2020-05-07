<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>学生情况</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css"/>
</head>
<body class="student-situation">
<!--头部 S-->
<header class="header head">
    <b class="page-title">学生情况大数据分析</b>
    <div class="full-screen-wrap">
        <div class="full-screen-btn">
            <div class="btn-inner"></div>
        </div>
    </div>
</header><!--头部 E-->

<!--主体 S-->
<div class="main-container">

    <div class="box-data-wrap high two-part">
        <div class="box-data mb-20">
            <div class="half-height">
                <div class="box-data-head">
                    <span>在校学生总数</span>
                </div>
                <div class="box-data-body">
                    <ul class="border-number">

                    </ul>
                </div>
            </div>
            <div class="half-height">
                <div class="box-data-head mt-20">
                    <span>男女比例</span>
                </div>
                <div class="box-data-body">
                    <div class="ratio-wrap mt-20">
                        <div class="sex-male" style="width:${boyTotalCount?default(50)}%;">男性：${boyTotalCount?default(50)}%</div>
                        <div class="sex-female" style="width:${girlTotalCount?default(50)}%;">女性：${girlTotalCount?default(50)}%</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box-data">
            <div class="wrap-full corner-top">
                <div class="box-data-head">
                    <span>学生各学段人数分布</span>
                </div>
                <div class="box-data-body">
                    <ul class="proportion-made">
                        <li data-text = "小学">
                            <div class="proportion-wrap">
                                <div class="green" data-num = "${xxCount?default(0)}%" style="width: ${xxCount?default(0)}%;"></div>
                            </div>
                        </li>
                        <li data-text = "初中">
                            <div class="proportion-wrap">
                                <div class="yellow" data-num = "${czCount?default(0)}%" style="width: ${czCount?default(0)}%;"></div>
                            </div>
                        </li>
                        <li data-text = "高中">
                            <div class="proportion-wrap">
                                <div class="purple" data-num = "${gzCount?default(0)}%" style="width: ${gzCount?default(0)}%;"></div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- 在校学生分布情况 -->
    <div class="box-data-wrap wide high">
        <div class="box-data">
            <div class="box-data-head text-left">
                <span>在校学生分布情况</span>
            </div>
            <div class="box-data-body">
                <div id="map" class="wrap-full"></div>
            </div>
        </div>
    </div>

    <!-- 学生民族分布比例、 学生留守儿童比例-->
    <div class="box-data-wrap two-part high">
        <div class="box-data mb-20">
            <div class="box-data-head">
                <span>学生民族分布比例</span>
            </div>
            <div class="box-data-body">
                <div id="chart-student-nation" class="wrap-full"></div>
            </div>
        </div>

        <div class="box-data">
            <div class="box-data-head">
                <span>学生留守儿童比例</span>
            </div>
            <div class="box-data-body">
                <div id="student-proportion" class="wrap-full"></div>
            </div>
        </div>
    </div>

    <!-- 历年学生人数变化情况 -->
    <div class="box-data-wrap wide">
        <div class="box-data">
            <div class="box-data-head">
                <span>历年学生人数变化情况</span>
            </div>
            <div class="box-data-body">
                <div id="student-num" class="wrap-full"></div>
            </div>
        </div>
    </div>

    <!-- 留守儿童 -->
    <div class="box-data-wrap wide">
        <div class="box-data">
            <div class="box-data-head">
                <div class="center-btn-wrap js-last">
                    <button type="button" class="btn mr-10 active" data-option = "option1"><span>留守儿童人数分布</span></button>
                    <button type="button" class="btn" data-option = "option2"><span>留守儿童涨幅趋势</span></button>
                </div>
            </div>
            <div class="box-data-body">
                <div id="children-stay-num" class="wrap-full"></div>
                <div id="children-stay" class="wrap-full hide"></div>
            </div>
        </div>
    </div>

</div><!--主体 E-->


<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.all.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    $(document).ready(function(){
        // 在校学生总人数
        var headcount = (Array(7).join('0') + "${totalCount!'0'}").slice(-7);
        for (var i=0;i<headcount.length;i++) {
            $(".border-number").append("<li><span>" + headcount.charAt(i) + "</span></li>");
        }

        //图表
        var arr=[];
        function resizeChart(){
            for(var i = 0;i < arr.length;i ++){
                arr[i].resize()
            }
        }
        //窗口变化，图表resize
        $(window).resize(function(){
            resizeChart();
        });

        // 在校学生分布情况
        var myChart = echarts.init(document.getElementById('map'));
        arr.push(myChart);
        $.get('${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/xinjiang2.json', function (geoJson) {
            myChart.hideLoading();
            echarts.registerMap('xinjiang', geoJson);
            myChart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}<br/>{c} (人)'
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
                    max: 500000,
                    text: ['高', '低'],
                    textStyle: {
                        color: '#fff'
                    },
                    realtime: true,
                    calculable: true,
                    inRange: {
                        color: ['#081f3a', '#1f83f5', '#1ebcd3']
                    }
                },
                series: [
                    {
                        name: '新疆各地区学生分布情况',
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
                            <#if areaSizeMap?exists && areaSizeMap?size gt 0>
                            <#list areaSizeMap?keys as key>
                            {name:'${key}', value: ${areaSizeMap[key]}}<#if key_index + 1 != areaSizeMap?size >,</#if>
                            </#list>
                            </#if>
                        ]
                    }
                ]
            });
        });

        var colors = new Array();
        <#if nationSizeMap?exists && nationSizeMap?size gt 0>
            colors = randomColor(${nationSizeMap?size});
        </#if>

        // 学生民族分布比例
        var chartStudentNation = echarts.init(document.getElementById('chart-student-nation'));
        arr.push(chartStudentNation);
        chartStudentNation.setOption({
            color: colors,
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                textStyle: {
                    color: 'white'
                },
                left: 0,
                data: [
                    <#if nationSizeMap?exists && nationSizeMap?size gt 0>
                    <#list nationSizeMap?keys as key>
                        {name:'${key}'}<#if key_index + 1 != nationSizeMap?size >,</#if>
                    </#list>
                    </#if>
                ]
            },
            series : [
                {
                    name: '学生人数',
                    type: 'pie',
                    radius : '70%',
                    center: ['60%', '50%'],
                    minAngle:10,
                    avoidLabelOverlap: true,
                    data: [
                        <#if nationSizeMap?exists && nationSizeMap?size gt 0>
                        <#list nationSizeMap?keys as key>
                        {value: ${nationSizeMap[key]}, name:'${key}'}<#if key_index + 1 != nationSizeMap?size >,</#if>
                        </#list>
                        </#if>
                    ]
                }
            ]
        });

        // 学生留守儿童比例
        var chartProportion = echarts.init($('#student-proportion')[0]);
        arr.push(chartProportion);
        chartProportion.setOption({
            color: ['#1f83f5', '#1ebcd3', '#d142a4'],
            legend: {
                textStyle: {
                    color: '#fff'
                },
                orient: 'vertical',
                left: 0,
                data: ['非留守儿童', '单亲留守','双亲留守']
            },
            tooltip : {
                trigger: 'item'
            },

            series : [
                {
                    name: '儿童人数',
                    type:'pie',
                    radius: ['50%', '70%'],
                    center: ['55%', '50%'],
                    minAngle:20,
                    avoidLabelOverlap: true,
                    data: [
                        {name: '非留守儿童', value: ${nols}},
                        {name: '单亲留守', value: ${dqls}},
                        {name: '双亲留守', value: ${sqls}}
                    ]
                }
            ]
        });

        //学生人数变化情况
        var studentNum = echarts.init($('#student-num')[0]);
        arr.push(studentNum);
        studentNum.setOption({
            tooltip : {
                trigger: 'axis'
            },
            color: ['#1ebcd3', '#1f83f5', '#d142a4', '#9949d7', '#ee913a'],
            legend: {
                data:['小学','初中','高中'],
                x: 'right',
                y: 'top',
                icon: 'rect',
                textStyle: {
                    color: '#fff'
                }
            },
            calculable : true,
            grid: {
                top: 30,
                left: 0,
                right: 30,
                bottom: 10,
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : true,
                    axisLine: {show: false},
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    data : [
                        <#list years as year>
                            '${year!}'<#if year_index + 1 != years?size>,</#if>
                        </#list>
                    ]
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name : '单位(人)',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    axisLine: {show: false},
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    min: 100
                }
            ],
            series : [
                {
                    name:'小学',
                    type:'line',
                    data:[
                        <#if xxSize?exists && xxSize?size gt 0>
                        <#list xxSize as xx>
                            ${xx}<#if xx_index + 1 != xxSize?size>,</#if>
                        </#list>
                        <#else>
                            0,0,0,0,0,0
                        </#if>
                    ]
                },
                {
                    name:'初中',
                    type:'line',
                    data:[
                        <#if czSize?exists && czSize?size gt 0>
                        <#list czSize as cz>
                            ${cz}<#if cz_index + 1 != czSize?size>,</#if>
                        </#list>
                        <#else>
                            0,0,0,0,0,0
                        </#if>
                    ]
                },
                {
                    name:'高中',
                    type:'line',
                    data:[
                        <#if gzSize?exists && gzSize?size gt 0>
                        <#list gzSize as gz>
                            ${gz}<#if gz_index + 1 != gzSize?size>,</#if>
                        </#list>
                        <#else>
                            0,0,0,0,0,0
                        </#if>
                    ]
                },
            ]
        });

        // 留守儿童
        var chartStayNum = echarts.init($('#children-stay-num')[0]);
        var chartStay = echarts.init($('#children-stay')[0]);
        arr.push(chartStayNum,chartStay);
        $('.js-last .btn').click(function(){
            if ($(this).hasClass('active') == false){
                $(this).addClass('active').siblings().removeClass('active');
                $(this).closest('.box-data-head').siblings('.box-data-body').find('.wrap-full').eq($(this).index()).removeClass('hide').siblings().addClass('hide');
                if ($(this).index() == 0) {
                    chartOne();
                    chartStayNum.resize()
                } else{
                    chartTwo();
                    chartStay.resize()
                }
            }
        });

        chartOne();
        function chartOne(){
            chartStayNum.setOption({
                legend: {
                    x : 'right',
                    y : '10',
                    textStyle:{
                        color: '#fff'
                    }
                },
                tooltip: {},
                color: ['#1f83f5', '#d142a4'],
                dataset: {
                    dimensions: ['product', '单亲留守儿童', '双亲留守儿童'],
                    source: [
                        <#if lsfbs?exists && lsfbs?size gt 0>
                        <#list lsfbs as lsfb>
                            {product: '${lsfb[0]}', '单亲留守儿童': ${lsfb[1]}, '双亲留守儿童': ${lsfb[2]}}<#if lsfb_index + 1 != lsfbs?size>,</#if>
                        </#list>
                        <#else>
                            {product: '无', '单亲留守儿童': 0, '双亲留守儿童': 0}
                        </#if>
                    ]
                },
                xAxis: {
                    type: 'category',
                    axisLine: {show: false},
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        },
                        formatter:function (value, index) {
                            var v = value.substring(0,3) + '...';
                            return value.length > 3 ? v : value;
                        }
                    }
                },
                yAxis: {
                    axisLine: {show: false},
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    min: 0
                },
                grid: {
                    top: 30,
                    left: 5,
                    right: 0,
                    bottom: 10,
                    containLabel: true
                },
                series: [
                    {type: 'bar'},
                    {type: 'bar'}
                ]
            })
        }

        function chartTwo(){
            chartStay.setOption({
                tooltip : {
                    trigger: 'axis'
                },
                color: ['#1ebcd3','#ee913a'],
                legend: {
                    data:['小学','初中'],
                    x: 'right',
                    y: 'top',
                    icon: 'rect',
                    textStyle: {
                        color: '#fff'
                    }
                },
                calculable : true,
                grid: {
                    top: 30,
                    left: 0,
                    right: 30,
                    bottom: 10,
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : true,
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine:{show:false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        data : [
                            <#list years as year>
                            '${year!}'<#if year_index + 1 != years?size>,</#if>
                            </#list>
                        ]
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        name : '单位(人)',
                        nameTextStyle: {
                            color: '#fff',
                        },
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine:{show:false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        min: 100
                    }
                ],
                series : [
                    {
                        name:'小学',
                        type:'line',
                        data:[
                            <#if xxlsSize?exists && xxlsSize?size gt 0>
                            <#list xxlsSize as xx>
                                ${xx}<#if xx_index + 1 != xxlsSize?size>,</#if>
                            </#list>
                            <#else>
                                0,0,0,0,0,0
                            </#if>
                        ]
                    },
                    {
                        name:'初中',
                        type:'line',
                        data:[
                            <#if czlsSize?exists && czlsSize?size gt 0>
                            <#list czlsSize as cz>
                                ${cz}<#if cz_index + 1 != czlsSize?size>,</#if>
                            </#list>
                            <#else>
                                0,0,0,0,0,0
                            </#if>
                        ]
                    }
                ]
            })
        }

    })

    function randomColor(size){
        var colors = ["#3ebb99","#69a4f0","#af89f3","#ed5784","#ed7e46","#9266ff","#ff66e9", "#f0ff73", "#7bffeb", "#ff20f6", "#ff8a10", "#ff0e29"];
        var nowColors = new Array();
        for(var i = 0;i<size;i++){
            nowColors[i] = colors[i];
        }
        return nowColors;
    }
</script>
</body>
</html>