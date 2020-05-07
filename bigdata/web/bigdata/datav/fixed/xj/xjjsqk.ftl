<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <title>教师情况大数据分析</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/teacher.css" />
</head>
<body>
<header class="header head">
    <h1 class="page-title">教师情况大数据分析</h1>
    <div class="full-screen-wrap">
        <div class="full-screen-btn">
            <div class="btn-inner"></div>
        </div>
    </div>
</header>
<main class="main teacher-main">
    <div class="row teacher-body">
        <div class="teacher-top-box">
            <div class="col-cell col-6">
                <div class="box box-sm">
                    <div class="box-header">
                        <h3 class="box-title teacher-more-title">各学段男女教师比例</h3>
                    </div>
                    <div class="box-body teacher-more-body">
                        <#--<div class="row teacher-body-chart">-->
                            <#--<div class="col-cell col-6">-->
                                <#--<div class="chart chart-teacher-sex"></div>-->
                            <#--</div>-->
                            <#--<div class="col-cell col-6">-->
                                <#--<div class="chart chart-teacher-sex"></div>-->
                            <#--</div>-->
                            <#--<div class="col-cell col-6">-->
                                <#--<div class="chart chart-teacher-sex"></div>-->
                            <#--</div>-->
                            <#--<div class="col-cell col-6">-->
                                <#--<div class="chart chart-teacher-sex"></div>-->
                            <#--</div>-->
                        <#--</div>-->
                    </div>
                </div>

                <div class="box box-sm">
                    <div class="box-header">
                        <h3 class="box-title">教师民族分布比例</h3>
                    </div>
                    <div class="box-body">
                        <div id="chart-teacher-title" class="chart"></div>
                    </div>
                </div>
            </div>
            <div class="col-cell col-12">
                <div class="box box-xbig">
                    <div class="box-body mapchart">
                        <div id="map" class="chart"></div>
                    </div>
                </div>
            </div>
            <div class="col-cell col-6">
                <div class="box box-sm">
                    <div class="box-header">
                        <h3 class="box-title">教师政治面貌比例</h3>
                    </div>
                    <div class="box-body">
                        <div id="chart-teacher-ethnic" class="chart"></div>
                    </div>
                </div>

                <div class="box box-sm">
                    <div class="box-header">
                        <h3 class="box-title">教师学位分布</h3>
                    </div>
                    <div class="box-body">
                        <div id="chart-teacher-education" class="chart"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="teacher-down-box">
            <div class="col-cell col-12">
                <div class="box box-xbig">
                    <div class="box-header teacher-age-body">
                        <span class="age-btn active" data-option="ageoption1">教师工龄分布</span>
                        <span class="age-btn" data-option="ageoption2">教师年龄分布</span>
                    </div>
                    <div class="box-body">
                        <div id="chart-teacher-seniority" class="chart"></div>
                    </div>
                </div>
            </div>
            <div class="col-cell col-12">
                <div class="box box-xbig">
                    <div class="box-header">
                        <h3 class="box-title">各学段不同职称教师分布</h3>
                    </div>
                    <div class="box-body">
                        <#--<div id="chart-teacher-grade" class="chart"></div>-->
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.all.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
<script>

    //图表
    var arr = [];
    function resizeChart() {
        for (var i = 0; i < arr.length; i++) {
            arr[i].resize()
        }
    }
    var myChart = echarts.init(document.getElementById('map'));
    arr.push(myChart);
    $.get('${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/xinjiang2.json', function (geoJson) {

        myChart.hideLoading();

        echarts.registerMap('xinjiang', geoJson);

        myChart.setOption({
            title: {
                text: '在职教师总数',
                textStyle: {
                    fontSize: 16,
                    color: '#00c1de'
                },
                padding: [20, 0],
                subtext: '${teacherSize!'0'}',
                subtextStyle: {
                    fontFamily: 'Quartz',
                    fontSize: 50,
                    fontWeight: 'bold',
                    color: '#00c1de'
                }
            },

            tooltip: {
                trigger: 'item',
                formatter: '{b}<br/>{c} (人)'
            },
            geo: {
                map: 'xinjiang',
                zoom: 1.2,
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
                max: 60000,
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
                    zoom: 1.2,
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
                        <#if dqfbMap?exists && dqfbMap?size gt 0>
                        <#list dqfbMap?keys as key>
                        {name: '${key}', value: ${dqfbMap[key]}}<#if key_index + 1 != dqfbMap?size>,</#if>
                        </#list>
                        </#if>
                    ]
                }
            ]
        });
    });

    // 各学段男女教师比例
    function renderTeacherSexChart(el, option){
        var chartTeacherSex = echarts.init(el);
        arr.push(chartTeacherSex);
        chartTeacherSex.setOption({
            color: ['#1f83f5', '#d142a4'],
            title: {
                text: option.title,
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                    fontWeight: 'normal'
                }
            },
            legend: {
                textStyle: {
                    color: '#fff'
                },
                orient: 'vertical',
                left: 0,
                top: 30,
                data: ['男', '女'],
                itemWidth: 13,
                itemHeight: 13,
            },
            tooltip : {
                trigger: 'item'
            },
            grid: {
                top: '30',
                left: '3%',
                right: '4%',
                bottom: '20'
            },
            series: [
                {
                    name: '教师人数',
                    type: 'pie',
                    radius: ['50%', '80%'],
                    center: ['50%', '70%'],
                    label: {
                        normal: {
                            show: false
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data: option.data
                }
            ]
        });
    }

    var chartTeacherSexData = [
        {
            title: '小学',
            data: [
                {name: '男', value: 345},
                {name: '女', value: 545}
            ]
        },
        {
            title: '中学',
            data: [
                {name: '男', value: 345},
                {name: '女', value: 545}
            ]
        },
        {
            title: '中专',
            data: [
                {name: '男', value: 345},
                {name: '女', value: 545}
            ]
        },
        {
            title: '高校',
            data: [
                {name: '男', value: 345},
                {name: '女', value: 545}
            ]
        },
    ];

    // $('.chart-teacher-sex').each(function(index){
    //     renderTeacherSexChart(this, chartTeacherSexData[index]);
    // });

    var nationColor = new Array();
    <#if nationSizeMap?exists && nationSizeMap?size gt 0>
        nationColor = randomColor(${nationSizeMap?size});
    </#if>

    // 民族教师占比情况
    var chartTeacherTitle = echarts.init($('#chart-teacher-title')[0]);
    arr.push(chartTeacherTitle);
    chartTeacherTitle.setOption({
        color: nationColor,
        legend: {
            textStyle: {
                color: '#fff'
            },
            orient: 'vertical',
            left: 0,
            data: [
                <#list nationSizeMap?keys as key>
                '${key}'<#if key_index + 1 != nationSizeMap?size>,</#if>
                </#list>
            ],
            itemWidth: 13,
            itemHeight: 13,
        },
        tooltip : {
            trigger: 'item'
        },
        grid: {
            top: '30',
            left: '3%',
            right: '4%',
            bottom: '20',
            containLabel: true
        },
        series : [
            {
                name: '教师人数',
                type:'pie',
                radius: ['0%', '65%'],
                center: ['60%', '50%'],
                minAngle:10,
                avoidLabelOverlap: true,
                data: [
                    <#list nationSizeMap?keys as key>
                    {name: '${key}', value: ${nationSizeMap[key]}}<#if key_index + 1 != nationSizeMap?size>,</#if>
                    </#list>
                ],
                label:{
                    formatter: '{d}%'
                }
            }
        ]
    });

    var backColor = new Array();
    <#if backSizeMap?exists && backSizeMap?size gt 0>
        backColor = randomColor(${backSizeMap?size});
    </#if>

    // 教师政治面貌比例
    var chartTeacherEthnic = echarts.init($('#chart-teacher-ethnic')[0]);
    arr.push(chartTeacherEthnic);
    chartTeacherEthnic.setOption({
        color: backColor,
        legend: {
            textStyle: {
                color: '#fff'
            },
            orient: 'vertical',
            left: 0,
            data: [
                <#list backSizeMap?keys as key>
                '${key}'<#if key_index+1 != backSizeMap?size>,</#if>
                </#list>
            ],
            itemWidth: 13,
            itemHeight: 13,
        },
        tooltip : {
            trigger: 'item'
        },
        grid: {
            top: '30',
            left: '3%',
            right: '4%',
            bottom: '20',
            containLabel: true
        },
        series : [
            {
                name: '教师人数',
                type:'pie',
                radius: ['0%', '65%'],
                center: ['60%', '50%'],
                minAngle:10,
                avoidLabelOverlap: true,
                data: [
                    <#list backSizeMap?keys as key>
                    {name: '${key}', value: ${backSizeMap[key]}}<#if key_index+1 != backSizeMap?size>,</#if>
                    </#list>
                ],
                label:{
                    formatter: '{d}%'
                }
            }
        ]
    });


    // 教师学位分布
    var chartTeacherEducation = echarts.init($('#chart-teacher-education')[0]);
    arr.push(chartTeacherEducation);
    chartTeacherEducation.setOption({
        color: ['#1F83F5','#D142A4','#1EBCD3','#ee913a', '#9949d7', '#1ebcd3'],
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type : 'shadow'
            }
        },
        grid: {
            top: '0%',
            left: '3%',
            right: '4%',
            bottom: '20',
            containLabel: true
        },
        yAxis : [
            {
                type : 'category',
                boundaryGap : true,
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisLabel: {
                    color: '#fff'
                },
                axisTick: {
                    show: false,
                    alignWithLabel: true
                },
                data : ['博士','硕士','学士']
            }
        ],
        xAxis : [
            {
                type : 'value',
                name: '人数',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                }
            }
        ],
        series : [
            {
                name: '教师',
                type:'bar',
                data:[
                    <#if xwJson?exists>
                        ${xwJson.bs},${xwJson.ss},${xwJson.xs}
                    <#else>
                        0,0,0
                    </#if>
                ],
                label: {
                    normal: {
                        show: true,
                        position: 'right',
                        color:"#fff",
                        formatter:'{c}'
                    }
                },
            }
        ]
    });


    // 教师工龄分布
    var stepChart = echarts.init($('#chart-teacher-seniority')[0]);
    arr.push(stepChart);
    var ageoption1 = {
        color: ['#1ebcd3'],
        tooltip : {
            trigger: 'axis',
            formatter:function(params){
                return params[0].data[0] + '年</br>' + params[0].data[1] + '人';
            }
        },
        grid: {
            top: '30',
            left: '3%',
            right: '5%',
            bottom: '20',
            containLabel: true
        },
        xAxis : [
            {
                type : 'value',
                name:'年',
                min:0,
                max:60,
                interval:5,
                boundaryGap : false,
                splitLine: {
                    show: false
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisLabel: {
                    color: '#fff'
                },
                axisTick: {
                    show: false
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
                name: '单位(人)',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                }
            }
        ],
        series : [
            {
                type:'line',
                areaStyle: {normal: {
                        color: {
                            type: 'linear',
                            x: 0,
                            y: 0,
                            x2: 0,
                            y2: 1,
                            colorStops: [{
                                offset: 0, color: '#16637e' // 0% 处的颜色
                            }, {
                                offset: 1, color: '#112f4e' // 100% 处的颜色
                            }],
                        }
                    }},
                showSymbol: false,
                data:[
                    <#if workAgeList?exists && workAgeList?size gt 0>
                    <#list workAgeList as list>
                        [${list.work_age},${list.count}]<#if list_index + 1 != workAgeList?size>,</#if>
                    </#list>
                    <#else>
                        [0,0],[5,0],[10,0],[15,0],[20,0],[25,0],[30,0],[35,0],[40,0],[45,0],[50,0],[55,0],[60,0]
                    </#if>
                ],
                smooth: true
            }
        ]
    };

    // 教师年龄分布
    var ageoption2 = {
        color: ['#1ebcd3'],
        tooltip : {
            trigger: 'axis',
            formatter: '{c}'
        },
        grid: {
            top: '30',
            left: '3%',
            right: '4%',
            bottom: '20',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                axisLabel: {
                    color: '#fff'
                },
                axisTick: {
                    show: false
                },
                data : ['24岁以下', '25-29岁', '30-34岁', '35-39岁', '40-44岁', '45-49岁', '50-54岁', '55-59岁', '60-64岁', '65岁及以上']
            }
        ],
        yAxis : [
            {
                type : 'value',
                name: '单位（人）',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#fff'
                    }
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                }
            }
        ],
        series : [
            {
                type:'line',
                areaStyle: {normal: {
                        color: {
                            type: 'linear',
                            x: 0,
                            y: 0,
                            x2: 0,
                            y2: 1,
                            colorStops: [{
                                offset: 0, color: '#16637e' // 0% 处的颜色
                            }, {
                                offset: 1, color: '#112f4e' // 100% 处的颜色
                            }],
                        }
                    }},
                data:[
                    <#if ageJson?exists>
                    ${ageJson.yx24s}, ${ageJson.s2529}, ${ageJson.s3034}, ${ageJson.s3539}, ${ageJson.s4044}, ${ageJson.s4549}, ${ageJson.s5054},
                    ${ageJson.s5559}, ${ageJson.s6064}, ${ageJson.ys65s}
                    <#else>
                        0,0,0,0,0,0,0,0,0,0
                    </#if>
                ],
                smooth: true
            }
        ]
    };

    stepChart.setOption(ageoption1);
    var timeNum1 = 0;
    var timer1 = setInterval(slide, 5000);
    $('.teacher-age-body .age-btn').on('click', function () {
        $(this).siblings(".age-btn").removeClass("active");
        $(this).addClass("active");
        clearInterval(timer1);
        eval('stepChart.setOption(' + $(this).data('option') + ')');
        stepChart.resize();
        timeNum1 = $(this).index();
        timer1 = setInterval(slide, 5000);
    });
    function slide() {
        timeNum1++;
        if (timeNum1 == $('.teacher-age-body .age-btn').length) {
            timeNum1 = 0
        }
        var $target = $('.teacher-age-body .age-btn').eq(timeNum1);
        $target.addClass('active').siblings().removeClass('active');
        eval('stepChart.setOption(' + $target.data('option') + ')');
        stepChart.resize();
    }




    // 各学段不同职称教师分布
    // var chartTeacherGrade = echarts.init($('#chart-teacher-grade')[0]);
    // arr.push(chartTeacherGrade);
    // chartTeacherGrade.setOption({
    //     color: ['#1F83F5','#D142A4','#1EBCD3','#ee913a', '#9949d7', '#1ebcd3'],
    //     legend: {
    //         textStyle: {
    //             color: '#fff'
    //         },
    //         data: ['一级', '二级', '三级','高级', '副教授', '讲师'],
    //         itemWidth: 13,
    //         itemHeight: 13,
    //         right: 0
    //     },
    //     tooltip : {
    //         trigger: 'axis',
    //         axisPointer : {
    //             type : 'shadow'
    //         }
    //     },
    //     grid: {
    //         top: '30',
    //         left: '3%',
    //         right: '4%',
    //         bottom: '20',
    //         containLabel: true
    //     },
    //     xAxis : [
    //         {
    //             type : 'category',
    //             boundaryGap : true,
    //             axisLine: {
    //                 show: false,
    //                 lineStyle: {
    //                     color: '#fff'
    //                 }
    //             },
    //             axisLabel: {
    //                 color: '#fff'
    //             },
    //             axisTick: {
    //                 show: false,
    //                 alignWithLabel: true
    //             },
    //             data : ['小学', '中学', '中专', '高校']
    //         }
    //     ],
    //     yAxis : [
    //         {
    //             type : 'value',
    //             name: '单位（人）',
    //             axisLine: {
    //                 show: false,
    //                 lineStyle: {
    //                     color: '#fff'
    //                 }
    //             },
    //             splitLine: {
    //                 show: false
    //             },
    //             axisTick: {
    //                 show: false
    //             }
    //         }
    //     ],
    //     series : [
    //         {
    //             name: '一级',
    //             type:'bar',
    //             data:[245, 331, 567, 234]
    //         },
    //         {
    //             name: '二级',
    //             type:'bar',
    //             data:[240, 341, 587, 362]
    //         },
    //         {
    //             name: '三级',
    //             type:'bar',
    //             data:[240, 331, 487, 321]
    //         },
    //         {
    //             name: '高级',
    //             type:'bar',
    //             data:[240, 331, 487, 234]
    //         },
    //         {
    //             name: '副教授',
    //             type:'bar',
    //             data:[240, 331, 487, 321]
    //         },
    //         {
    //             name: '讲师',
    //             type:'bar',
    //             data:[240, 331, 487, 234]
    //         }
    //     ]
    // });

    //窗口变化，图表resize
    $(window).resize(function () {
        resizeChart();
    })

    function circle() {}

    function btnOverstep(){}

    function randomColor(size){
        var colors = ["#3ebb99","#69a4f0","#af89f3","#ed5784","#ed7e46","#9266ff","#ff66e9", "#f0ff73", "#7bffeb", "#ff20f6", "#ff8a10", "#ff0e29"];
        var nowColors = new Array();
        for(var i = 0;i<size;i++){
            nowColors[i] = colors[i];
        }
        return nowColors;
    }
</script>
