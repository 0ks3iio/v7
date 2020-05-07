<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>新疆维吾尔自治区学生大数据分析</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>

<body class="student-screen">
<!--头部 S-->
<div class="student-screen-head">
    <div class="titl-wrap">
        <b>新疆维吾尔自治区学生大数据分析</b>
    </div>
    <div class="time-box-wrap">
        <div class="date-box mr-10"></div>
        <div class="time-box"></div>
    </div>
</div>
<!--头部 E-->

<!--主体 S-->
<div class="student-screen-body">
    <div class="content-wrap clearfix">
        <div class="quarter-box left-coming">
            <div class="border"></div>
            <div class="quarter-box-content">
                <div class="student-title type2">
                    <span>按学年的转学异动情况</span>
                    <div class="btn-group filter2">
                        <button class="btn dropdown-toggle">省内跨地市 <i class="wpfont icon-caret-down"></i></button>
                        <ul class="dropdown-menu">
                            <li><a href="#" id="yd1">跨省</a></li>
                            <li><a href="#" id="yd2">省内小计</a></li>
                            <li><a href="#" id="yd3">省内跨地市</a></li>
                            <li><a href="#" id="yd4">地市内跨县</a></li>
                            <li><a href="#" id="yd5">区县内</a></li>
                        </ul>
                    </div>
                </div>
                <div class="box-screen-one" id="echart-one">

                </div>

                <div class="student-title">
                    <span>本学年各地区的转入、转出数量排行榜</span>
                </div>

                <div class="box-screen-two">
                    <div class="pa-10 text-center">
                        <div class="btn-group box-data-scope">
                            <button class="btn active" id="zr">转入</button>
                            <button class="btn " id="zc">转出</button>
                        </div>
                    </div>
                    <div class="box-screen-table">
                        <table class="tables wrap-full">
                            <tbody class="wrap-full">
                            <tr>
                                <td><em>1</em></td>
                                <td>乌鲁木齐</td>
                                <td id="rc1">15484</td>
                            </tr>
                            <tr>
                                <td><em>2</em></td>
                                <td>克拉玛依市</td>
                                <td id="rc2">12714</td>
                            </tr>
                            <tr>
                                <td><em>3</em></td>
                                <td>吐鲁番市</td>
                                <td id="rc3">1254</td>
                            </tr>
                            <tr>
                                <td><em>4</em></td>
                                <td>哈密市</td>
                                <td id="rc4">1164</td>
                            </tr>
                            <tr>
                                <td><em>5</em></td>
                                <td>阿克苏地区</td>
                                <td id="rc5">986</td>
                            </tr>
                            <tr>
                                <td><em>6</em></td>
                                <td>喀什地区</td>
                                <td id="rc6">654</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="half-box">
            <div class="half-box-head up-coming">
                <div class="">
                    <div class="half-head-box">
                        <div class="half-head-box-title">
                            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-mine.png" >
                            <span>学生总数</span>
                        </div>
                        <div class="half-head-box-content">
                            <span id="studentNum">1623345</span>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="half-head-box">
                        <div class="half-head-box-title">
                            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-class.png" >
                            <span>班级总数</span>
                        </div>
                        <div class="half-head-box-content">
                            <span id="classNum">39516</span>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="half-head-box">
                        <div class="half-head-box-title">
                            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-child.png" >
                            <span>留守儿童</span>
                        </div>
                        <div class="half-head-box-content">
                            <span id="leftoverChildren">627846</span>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="half-head-box">
                        <div class="half-head-box-title">
                            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-handicapped.png" >
                            <span>特教学生</span>
                        </div>
                        <div class="half-head-box-content">
                            <span id="specialStudent">26137</span>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="half-head-box">
                        <div class="half-head-box-title">
                            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-location.png" >
                            <span>随迁子女</span>
                        </div>
                        <div class="half-head-box-content">
                            <span id="ImmigrantChildren">15394</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="half-box-body">
                <div class="box-screen-three center-coming">
                    <div class="box-screen-three-title">
                        <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-circle.png" align="bottom">
                        <span>当前位置：新疆 ></span>
                        <span class="js-location">巴音郭楞蒙古自治州</span>
                    </div>
                    <div class="box-screen-three-body">
                        <div id="map" class="box-screen-three-chart wrap-full">

                        </div>
                    </div>
                </div>

                <div class="box-screen-four down-coming">
                    <div class="border"></div>
                    <div class="shadow-fix"></div>
                    <div class="box-screen-four-content">
                        <div class="student-title type2">
                            <span>学生按年级分布柱状图</span>
                        </div>
                        <div class="box-screen-four-chart" id="echart-three">

                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="quarter-box last-part right-coming">
            <div class="part-wrap">
                <div class="part">
                    <div class="student-title">
                        <span>新生人数与招生人数对比</span>
                        <div class="btn-group box-data-scope">
                            <button class="btn active" id="primary">小学</button>
                            <button class="btn " id="middle">初中</button>
                            <button class="btn " id="high">高中</button>
                        </div>
                    </div>

                    <div class="part-chart" id="echart-four"></div>
                </div>
            </div>
            <div class="part-wrap">
                <div class="part">
                    <div class="student-title">
                        <span>学生情况分析</span>
                    </div>
                    <div class="part-chart clearfix">
                        <div class="quarter-chart echart">

                        </div>
                        <div class="quarter-chart echart">

                        </div>
                        <div class="quarter-chart echart">

                        </div>
                        <div class="quarter-chart echart">

                        </div>
                    </div>
                </div>
            </div>
            <div class="part-wrap">
                <div class="part">
                    <div class="student-title">
                        <span>控辍保学政策情况趋势图</span>
                        <div class="btn-group box-data-scope">
                            <button class="btn active" id="cx1">小学</button>
                            <button class="btn " id="cx2">初中</button>
                        </div>
                    </div>

                    <div class="part-chart" id="echart-six"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- 弹窗 -->
    <div class="layer">
        <div class="border"></div>
        <div class="layer-head">
            <span><span class="js-class-name">XX</span>年级班额情况</span>
            <i class="wpfont icon-close js-close"></i>
        </div>
        <div class="layer-body clearfix">
            <div class="half">
                <div class="half-title">
                    学生人数：<span class="js-student-num">264,372 </span>&nbsp;&nbsp;
                    班级总数：<span id="banjiNum">264,372</span>
                </div>
                <div class="half-content">
                    <div class="half-chart wrap-full"  id="echart-seven">

                    </div>
                </div>
            </div>
            <div class="half">
                <div class="half-title">
                    <span>各地区平均班额排行榜</span>
                </div>
                <div class="half-content box-screen-table">
                    <table class="tables wrap-full">
                        <tbody class="wrap-full">
                        <tr>
                            <td><em>1</em></td>
                            <td>乌鲁木齐</td>
                            <td><span class="c-purple" id="be1">15484</span></td>
                        </tr>
                        <tr>
                            <td><em>2</em></td>
                            <td>卡拉玛依市</td>
                            <td><span class="c-purple" id="be2">12714</span></td>
                        </tr>
                        <tr>
                            <td><em>3</em></td>
                            <td>吐鲁番市</td>
                            <td id="be3">1254</td>
                        </tr>
                        <tr>
                            <td><em>4</em></td>
                            <td>哈密市</td>
                            <td id="be4">1164</td>
                        </tr>
                        <tr>
                            <td><em>5</em></td>
                            <td>阿克苏地区</td>
                            <td><span class="c-green" id="be5">986</span></td>
                        </tr>
                        <tr>
                            <td><em>6</em></td>
                            <td>喀什地区</td>
                            <td><span class="c-green" id="be6">654</span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
<!--主体 E-->


<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts-gl.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>

<script>
    $(function () {
        // 时间
        getDate();
        getTime();
        setInterval(getTime, 1000);
        function getTime(){
            var date = new Date();
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            $('.time-box').text(hours + ":" + minutes + ":" + seconds);
            if (hours == '00' && minutes == '00' && seconds == '00'){
                getDate()
            }
        }
        // 日期
        function getDate(){
            var date = new Date();
            var year = date.getFullYear();
            var month = (date.getMonth() + 1) < 10 ?(date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
            $('.date-box').text(year +'年'+ month + '月' + day + '日');
        }
        // 初始进入
        showHtml();
        function showHtml() {
            setTimeout(function () {
                $(".left-coming").css("transform", "translateX(0)");
            }, 200);
            setTimeout(function () {
                $(".right-coming ").css("transform", "translateX(0)");
            }, 200);
            setTimeout(function () {
                $(".up-coming").css("transform", "translateY(0)");
            }, 200);
            setTimeout(function () {
                $(".down-coming ").css("transform", "translateY(0)");
            }, 200);
            setTimeout(function () {
                $(".center-coming ").css("opacity", 1);
            }, 200);
        }

        //图表
        var arr = [];
        function resizeChart() {
            for (var i = 0; i < arr.length; i++) {
                arr[i].resize()
            }
        }

        // 按学年的转学异动情况
        var chartOne = echarts.init(document.getElementById('echart-one'));
        arr.push(chartOne);
        var oneOption={
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            color: ['#00dd94', '#00ccdd'],
            legend: {
                data:['转入','转出'],
                top: 10,
                itemWidth: 10,
                itemHeight: 6,
                textStyle: {
                    color: '#fff',
                    fontSize: 12
                }
            },
            grid: {
                left: '3%',
                right: 25,
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'value',
                    axisTick: {show: false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: '#172649'
                        }
                    }
                }
            ],
            yAxis : [
                {
                    type : 'category',
                    name : '学年',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    boundaryGap: true,//坐标轴两边留白
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    data : ['14-13','15-14','16-15','17-16','18-17','19-18']
                }
            ],
            series : [
                {
                    name:'转入',
                    type:'bar',
                    barWidth: '20%',
                    data:[13200, 8320, 16001, 13304, 14390, 15330]
                },
                {
                    name:'转出',
                    type:'bar',
                    barWidth: '20%',
                    data:[8062, 7018, 8964, 10026, 10679, 12600],
                }
            ]
        }
        chartOne.setOption(oneOption);

        $('#yd1').click(function () {
            let into=[random(11000,15000),random(7000,9000),random(14000,18000),random(11000,15000),random(12000,16000),random(13000,17000)]
            let out=[random(7000,9000),random(6000,8000),random(8000,10000),random(9000,11000),random(9000,11000),random(11000,14000)]
            oneOption.series[0].data=into;
            oneOption.series[1].data=out;
            chartOne.setOption(oneOption);

        })
        $('#yd2').click(function () {
            let into=[random(11000,15000),random(7000,9000),random(14000,18000),random(11000,15000),random(12000,16000),random(13000,17000)]
            let out=[random(7000,9000),random(6000,8000),random(8000,10000),random(9000,11000),random(9000,11000),random(11000,14000)]
            oneOption.series[0].data=into;
            oneOption.series[1].data=out;
            chartOne.setOption(oneOption);
        })
        $('#yd3').click(function () {
            let into=[random(11000,15000),random(7000,9000),random(14000,18000),random(11000,15000),random(12000,16000),random(13000,17000)]
            let out=[random(7000,9000),random(6000,8000),random(8000,10000),random(9000,11000),random(9000,11000),random(11000,14000)]
            oneOption.series[0].data=into;
            oneOption.series[1].data=out;
            chartOne.setOption(oneOption);
        })
        $('#yd4').click(function () {
            let into=[random(11000,15000),random(7000,9000),random(14000,18000),random(11000,15000),random(12000,16000),random(13000,17000)]
            let out=[random(7000,9000),random(6000,8000),random(8000,10000),random(9000,11000),random(9000,11000),random(11000,14000)]
            oneOption.series[0].data=into;
            oneOption.series[1].data=out;
            chartOne.setOption(oneOption);
        })
        $('#yd5').click(function () {
            let into=[random(11000,15000),random(7000,9000),random(14000,18000),random(11000,15000),random(12000,16000),random(13000,17000)]
            let out=[random(7000,9000),random(6000,8000),random(8000,10000),random(9000,11000),random(9000,11000),random(11000,14000)]
            oneOption.series[0].data=into;
            oneOption.series[1].data=out;
            chartOne.setOption(oneOption);
        })

        // 地图
        // 初始化图表
        var mapChart = echarts.init(document.getElementById('map'));
        arr.push(mapChart);

        var uploadedDataURL = "${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/650000.json";
        var mapData = [
            {
                name: '巴音郭楞蒙古自治州',
                value: 21900,
                itemStyle: {            // itemStyle高亮时的配置
                    color: '#63FFFF',   // 高亮时地图板块颜色改变
                },
                label: {
                    show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                    //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                    //formatter:,               // 标签内容格式器
                    textStyle: {                // 标签的字体样式
                        color: '#fff',                  // 地图初始化区域字体颜色
                        fontSize: 14,                    // 字体大小
                        opacity: 1,                     // 字体透明度
                        backgroundColor: 'rgba(0,0,0,0.5)',      // 字体背景色
                    },
                }
            },
            { name: '乌鲁木齐市', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '克拉玛依市', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '吐鲁番市', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '哈密市', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '阿克苏地区', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '喀什地区', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '和田地区', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '昌吉回族自治州', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '博尔塔拉蒙古自治州', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            // { name: '巴音郭楞蒙古自治州', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '克孜勒苏柯尔克孜自治州', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '伊犁哈萨克自治州', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '塔城地区', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)},
            { name: '阿勒泰地区', studentNum: random(1500000,1600000), classNum:random(29000,31000),leftoverChildren:random(89000,91000),specialStudent:random(14000,18000),ImmigrantChildren:random(1800,2200)}
        ]

        // 引入JSON文件
        $.getJSON(uploadedDataURL, function (geoJson) {
            echarts.registerMap('xinjiang', geoJson);

            // 图表配置项
            var option = {
                tooltip: {							// 提示框
                    trigger: 'item',
                    formatter: function (params) {
                        if(params.data['name']=='巴音郭楞蒙古自治州'){
                            $("#studentNum").html('1623345');
                            $("#classNum").html('39516');
                            $("#specialStudent").html('627846');
                            $("#leftoverChildren").html('26137');
                            $("#ImmigrantChildren").html('15394');
                        }
                        $("#studentNum").html(params.data['studentNum']);
                        $("#classNum").html(params.data['classNum']);
                        $("#specialStudent").html(params.data['specialStudent']);
                        $("#leftoverChildren").html(params.data['leftoverChildren']);
                        $("#ImmigrantChildren").html(params.data['ImmigrantChildren']);
                    return params.studentNum}
                },
                series: [
                    {
                        type: 'map3D',			  // 系列类型
                        name: 'map3D',			  // 系列名称
                        map: 'xinjiang',           // 地图类型。echarts-gl 中使用的地图类型同 geo 组件相同(ECharts 中提供了两种格式的地图数据，一种是可以直接 script 标签引入的 js 文件，引入后会自动注册地图名字和数据。还有一种是 JSON 文件，需要通过 AJAX 异步加载后手动注册。)

                        // 环境贴图，支持純颜色值，渐变色，全景贴图的 url。默认为 'auto'，在配置有 light.ambientCubemap.texture 的时候会使用该纹理作为环境贴图。否则则不显示环境贴图。
                        // environment: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{      // 配置为垂直渐变的背景
                        //     offset: 0, color: '#030127' // 天空颜色
                        // }, {
                        //     offset: 1, color: '#030127' // 地面颜色
                        // }], false),

                        itemStyle: {            // 三维地理坐标系组件 中三维图形的视觉属性，包括颜色，透明度，描边等。
                            color: '#1C1F6B',       // 地图板块的颜色
                            opacity: 1,                 // 图形的不透明度 [ default: 1 ]
                            borderWidth: 0.5,           // (地图板块间的分隔线)图形描边的宽度。加上描边后可以更清晰的区分每个区域   [ default: 0 ]
                            borderColor: '#0B0B28'         // 图形描边的颜色。[ default: #333 ]
                        },
                        emphasis: {
                            itemStyle: {            // itemStyle高亮时的配置
                                color: '#63FFFF',   // 高亮时地图板块颜色改变
                            },
                            label: {                // 标签的相关设置
                                show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                                //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                                //formatter:,               // 标签内容格式器
                                textStyle: {                // 标签的字体样式
                                    color: '#fff',                  // 地图初始化区域字体颜色
                                    fontSize: 14,                    // 字体大小
                                    opacity: 1,                     // 字体透明度
                                    backgroundColor: 'rgba(0,0,0,0.5)',      // 字体背景色
                                },
                            },
                        },

                        groundPlane: {			// 地面可以让整个组件有个“摆放”的地方，从而使整个场景看起来更真实，更有模型感。
                            show: false,				// 是否显示地面。[ default: false ]
                            color: '#aaa'			// 地面颜色。[ default: '#aaa' ]
                        },

                        //shading: 'lambert',       // 三维地理坐标系组件中三维图形的着色效果，echarts-gl 中支持下面三种着色方式:
                        // 'color' 只显示颜色，不受光照等其它因素的影响。
                        // 'lambert' 通过经典的 lambert 着色表现光照带来的明暗。
                        // 'realistic' 真实感渲染，配合 light.ambientCubemap 和 postEffect 使用可以让展示的画面效果和质感有质的提升。ECharts GL 中使用了基于物理的渲染（PBR） 来表现真实感材质。
                        // realisticMaterial: {}    // 真实感材质相关的配置项，在 shading 为'realistic'时有效。
                        // lambertMaterial: {}      // lambert 材质相关的配置项，在 shading 为'lambert'时有效。
                        // colorMaterial: {}        // color 材质相关的配置项，在 shading 为'color'时有效。

                        label: {                // 标签的相关设置
                            show: false,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                            //distance: 50,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                            //formatter:,               // 标签内容格式器
                            textStyle: {                // 标签的字体样式
                                color: '#000',                  // 地图初始化区域字体颜色
                                fontSize: 12,                    // 字体大小
                                opacity: 1,                     // 字体透明度
                                backgroundColor: 'rgba(0,23,11,0)'      // 字体背景色
                            },
                        },

                        light: {                    // 光照相关的设置。在 shading 为 'color' 的时候无效。  光照的设置会影响到组件以及组件所在坐标系上的所有图表。合理的光照设置能够让整个场景的明暗变得更丰富，更有层次。
                            main: {                     // 场景主光源的设置，在 globe 组件中就是太阳光。
                                color: '#fff',              //主光源的颜色。[ default: #fff ]
                                intensity: 1.2,             //主光源的强度。[ default: 1 ]
                                shadow: false,              //主光源是否投射阴影。默认关闭。    开启阴影可以给场景带来更真实和有层次的光照效果。但是同时也会增加程序的运行开销。
                                //shadowQuality: 'high',      // 阴影的质量。可选'low', 'medium', 'high', 'ultra' [ default: 'medium' ]
                                alpha: 55,                  // 主光源绕 x 轴，即上下旋转的角度。配合 beta 控制光源的方向。[ default: 40 ]
                                beta: 10                    // 主光源绕 y 轴，即左右旋转的角度。[ default: 40 ]
                            },
                            ambient: {                  // 全局的环境光设置。
                                color: '#fff',              // 环境光的颜色。[ default: #fff ]
                                intensity: 0.5              // 环境光的强度。[ default: 0.2 ]
                            }
                        },

                        viewControl: {			// 用于鼠标的旋转，缩放等视角控制。
                            projection: 'perspective',		// 投影方式，默认为透视投影'perspective'，也支持设置为正交投影'orthographic'。
                            autoRotate: false,				// 是否开启视角绕物体的自动旋转查看。[ default: false ]
                            autoRotateDirection: 'cw',		// 物体自传的方向。默认是 'cw' 也就是从上往下看是顺时针方向，也可以取 'ccw'，既从上往下看为逆时针方向。
                            autoRotateSpeed: 10,			// 物体自传的速度。单位为角度 / 秒，默认为10 ，也就是36秒转一圈。
                            autoRotateAfterStill: 3,		// 在鼠标静止操作后恢复自动旋转的时间间隔。在开启 autoRotate 后有效。[ default: 3 ]
                            damping: 0,						// 鼠标进行旋转，缩放等操作时的迟滞因子，在大于等于 1 的时候鼠标在停止操作后，视角仍会因为一定的惯性继续运动（旋转和缩放）。[ default: 0.8 ]
                            rotateSensitivity: 1,			// 旋转操作的灵敏度，值越大越灵敏。支持使用数组分别设置横向和纵向的旋转灵敏度。默认为1, 设置为0后无法旋转。	rotateSensitivity: [1, 0]——只能横向旋转； rotateSensitivity: [0, 1]——只能纵向旋转。
                            zoomSensitivity: 1,				// 缩放操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法缩放。
                            panSensitivity: 1,				// 平移操作的灵敏度，值越大越灵敏。默认为1,设置为0后无法平移。支持使用数组分别设置横向和纵向的平移灵敏度
                            panMouseButton: 'left',			// 平移操作使用的鼠标按键，支持：'left' 鼠标左键（默认）;'middle' 鼠标中键 ;'right' 鼠标右键(注意：如果设置为鼠标右键则会阻止默认的右键菜单。)
                            rotateMouseButton: 'left',		// 旋转操作使用的鼠标按键，支持：'left' 鼠标左键;'middle' 鼠标中键（默认）;'right' 鼠标右键(注意：如果设置为鼠标右键则会阻止默认的右键菜单。)

                            distance: 90,					// [ default: 100 ] 默认视角距离主体的距离，对于 grid3D 和 geo3D 等其它组件来说是距离中心原点的距离,对于 globe 来说是距离地球表面的距离。在 projection 为'perspective'的时候有效。
                            minDistance: 40,				// [ default: 40 ] 视角通过鼠标控制能拉近到主体的最小距离。在 projection 为'perspective'的时候有效。
                            maxDistance: 90,				// [ default: 400 ] 视角通过鼠标控制能拉远到主体的最大距离。在 projection 为'perspective'的时候有效。

                            alpha: 40, 						// 视角绕 x 轴，即上下旋转的角度。配合 beta 可以控制视角的方向。[ default: 40 ]
                            beta: 15,						// 视角绕 y 轴，即左右旋转的角度。[ default: 0 ]
                            minAlpha: 40,					// 上下旋转的最小 alpha 值。即视角能旋转到达最上面的角度。[ default: 5 ]
                            maxAlpha: 40,					// 上下旋转的最大 alpha 值。即视角能旋转到达最下面的角度。[ default: 90 ]
                            minBeta: -Infinity,					// 左右旋转的最小 beta 值。即视角能旋转到达最左的角度。[ default: -80 ]
                            maxBeta: Infinity,					// 左右旋转的最大 beta 值。即视角能旋转到达最右的角度。[ default: 80 ]

                            center: [0, 0, 0],				// 视角中心点，旋转也会围绕这个中心点旋转，默认为[0,0,0]。

                            animation: true,				// 是否开启动画。[ default: true ]
                            animationDurationUpdate: 1000,	// 过渡动画的时长。[ default: 1000 ]
                            animationEasingUpdate: 'cubicInOut'		// 过渡动画的缓动效果。[ default: cubicInOut ]
                        },

                        data: mapData
                    }]
            };

            // 设置图表实例的配置项以及数据，万能接口，所有参数和数据的修改都可以通过setOption完成，ECharts 会合并新的参数和数据，然后刷新图表。
            mapChart.setOption(option);

            // 处理地图点击事件,高亮
            var name='巴音郭楞蒙古自治州';
            mapChart.on('click', function (params) {
                $('.js-location').text(params.name);
                console.log(name)
                if(name!=params.name) {
                    //改变 按学年的转学异动情况
                    let into = [random(11000, 15000), random(7000, 9000), random(14000, 18000), random(11000, 15000), random(12000, 16000), random(13000, 17000)]
                    let out = [random(7000, 9000), random(6000, 8000), random(8000, 10000), random(9000, 11000), random(9000, 11000), random(11000, 14000)]
                    oneOption.series[0].data = into;
                    oneOption.series[1].data = out;
                    chartOne.setOption(oneOption);
                    //改变 本学年各地区的转入、转出数量排行榜
                    $('#rc1').html(random(14000, 16000))
                    $('#rc2').html(random(11000, 13000))
                    $('#rc3').html(random(1100, 1300))
                    $('#rc4').html(random(1000, 1200))
                    $('#rc5').html(random(950, 990))
                    $('#rc6').html(random(620, 680))
                    //改变 学生按年级分布柱状图
                    var data = [random(260000, 280000), random(230000, 250000), random(140000, 160000), random(410000, 430000), random(220000, 240000), random(170000, 190000), random(240000, 260000), random(320000, 340000), random(200000, 220000), random(210000, 230000), random(150000, 170000), random(320000, 340000)];
                    chartTreeOption.series[1].data = data;
                    chartThree.setOption(chartTreeOption);
                    //改变 新生人数与招生人数对比
                    let newData = [random(11000, 13000), random(250000, 270000), random(170000, 190000), random(230000, 270000), random(380000, 460000)]
                    let recruit = [random(300000, 340000), random(150000, 170000), random(120000, 140000), random(320000, 370000), random(200000, 240000)]
                    newAndRecruitStudent.series[0].data = newData;
                    newAndRecruitStudent.series[1].data = recruit;
                    echartFour.setOption(newAndRecruitStudent);
                    //改变 学生情况分析
                    $('.echart').each(function (index, ele) {
                        studentsData[index].data[0].value = random(270, 330);
                        studentsData[index].data[1].value = random(500, 600);
                        students(this, studentsData[index]);
                    });
                    //改变 控辍保学政策情况趋势图
                    let dropOutData = [random(1100, 1300), random(240000, 280000), random(110000, 150000), random(230000, 270000), random(390000, 460000)];
                    let likeDropData = [random(210000, 250000), random(210000, 250000), random(180000, 200000), random(110000, 150000), random(300000, 360000)];
                    let baoXueData = [random(300000, 340000), random(140000, 180000), random(110000, 150000), random(310000, 390000), random(200000, 240000)];
                    sixOption.series[0].data = dropOutData;
                    sixOption.series[1].data = likeDropData;
                    sixOption.series[2].data = baoXueData;
                    echartSix.setOption(sixOption);
                }
                name=params.name;
                showindex = params.dataIndex;
                for (let i = 0; i < mapData.length; i++) {
                    delete mapData[i].itemStyle;
                    delete mapData[i].label;
                }
                mapData[showindex].itemStyle = {            // itemStyle高亮时的配置
                    color: '#63FFFF',   // 高亮时地图板块颜色改变
                };
                mapData[showindex].label = {
                    show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                    //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                    //formatter:,               // 标签内容格式器
                    textStyle: {                // 标签的字体样式
                        color: '#fff',                  // 地图初始化区域字体颜色
                        fontSize: 14,                    // 字体大小
                        opacity: 1,                     // 字体透明度
                        backgroundColor: 'rgba(0,0,0,0.5)',      // 字体背景色
                    },
                };
                option.series[0].data = mapData;
                mapChart.setOption(option);
                console.log(params,params.dataIndex)
            });

        });
        var flag=false;
        $('#zr').click(function () {
            if (flag){
                $('#rc1').html(random(14000,16000))
                $('#rc2').html(random(11000,13000))
                $('#rc3').html(random(1100,1300))
                $('#rc4').html(random(1000,1200))
                $('#rc5').html(random(950,990))
                $('#rc6').html(random(620,680))
            }
            flag=false;
        })
        $('#zc').click(function () {
            if (!flag){
                $('#rc1').html(random(14000,16000))
                $('#rc2').html(random(11000,13000))
                $('#rc3').html(random(1100,1300))
                $('#rc4').html(random(1000,1200))
                $('#rc5').html(random(950,990))
                $('#rc6').html(random(620,680))
            }
            flag=true;
        })


        // 学生按年级分布柱状图
        var chartThree = echarts.init(document.getElementById('echart-three'));
        arr.push(chartThree);
        var data = [random(260000,280000), random(230000,250000), random(140000,160000), random(410000,430000), random(220000,240000), random(170000,190000), random(240000,260000), random(320000,340000), random(200000,220000), random(210000,230000), random(150000,170000),random(320000,340000)];
        var yMax = 500000;
        var dataShadow = [];
        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }
        chartThree.on('click', function (params) {
            $('.layer').show();
            $('.js-student-num').text(params.value);
            $('.js-class-name').text(params.name);
            $('#banjiNum').text(Math.floor(params.value/50))
            $('#be1').text(random(66,70))
            $('#be2').text(random(62,66))
            $('#be3').text(random(58,62))
            $('#be4').text(random(54,58))
            $('#be5').text(random(50,54))
            $('#be6').text(random(46,50))
            layerChart();
        });
        var chartTreeOption={
            tooltip : {
                show: true
            },
            xAxis: {
                data: ['小一', '小二', '小三', '小四', '小五', '小六', '初一', '初二', '初三', '高一', '高二', '高三'],
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
                axisTick: {show: false},
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#243255',
                        width: 2
                    }
                },
                z: 10
            },
            yAxis: {
                name: '人',
                nameTextStyle: {
                    color: '#fff',
                },
                axisTick: {show: false},
                axisLabel: {
                    textStyle: {
                        color: '#fff',
                        fontSize: 12
                    }
                },
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#243255',
                        width: 2
                    }
                },
                splitLine:{
                    show:true,
                    lineStyle: {
                        color: '#172649'
                    }
                }
            },
            grid:{
                x:55,
                y:30,
                x2:15,
                bottom:25,
            },
            series: [
                { // shadow
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            barBorderRadius: 50,
                            color: 'rgba(12,36,79,.7)',
                        }
                    },
                    barGap:'-100%',
                    barCategoryGap:'40%',
                    data: dataShadow,
                    animation: false
                },
                {
                    type: 'bar',
                    barWidth : '30%',
                    itemStyle: {
                        normal: {
                            barBorderRadius: 50,
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#00dd94'},
                                    {offset: 0.7, color: '#0195f7'},
                                    {offset: 1, color: '#0195f7'}
                                ]
                            )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#0195f7'},
                                    {offset: 0.7, color: '#0195f7'},
                                    {offset: 1, color: '#00dd94'}
                                ]
                            )
                        }
                    },
                    data: data
                }
            ]
        }
        chartThree.setOption(chartTreeOption);

        //关闭弹窗
        $('body').on('click','.js-close',function(){
            $(this).closest('.layer').hide()
        });

        function layerChart(){
            var chartSeven = echarts.init(document.getElementById('echart-seven'));
            arr.push(chartSeven);
            chartSeven.setOption({
                tooltip : {
                    formatter: "{a} <br/>{b} : {c}"
                },
                color: ['#00dd94', '#00ccdd', '#905eff'],
                series: [
                    {
                        name: '额度',
                        type: 'gauge',
                        min:0,
                        max:80,
                        splitNumber: 16,
                        title : {
                            textStyle: {
                                fontSize: 14,
                                color: '#fff',
                            }
                        },
                        detail : {
                            offsetCenter: [0, '50%'],
                            textStyle: {
                                fontSize: 20
                            }
                        },
                        axisLine: {            // 坐标轴线
                            lineStyle: {
                                color: [[0.4375,'#00dd94'],[0.625, '#00ccdd'],[1, '#905eff']],
                                width: 10,
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 5
                            }
                        },
                        axisTick: {
                            length :5,
                        },
                        splitLine: {           // 分隔线
                            length :10,
                            lineStyle: {
                                width:1,
                                color: '#fff',
                            }
                        },
                        data: [
                            {
                                value: random(50,68),
                                name: '平均班额',
                                color: '#fff'
                            }
                        ]
                    }
                ]
            });
        }


        // 新生人数与招生人数对比
        var echartFour = echarts.init($('#echart-four')[0]);
        arr.push(echartFour);
        var newAndRecruitStudent={
            tooltip : {
                trigger: 'axis'
            },
            color: ['#905eff', '#00ccdd'],
            legend: {
                data:['新生','招生'],
                x: 'center',
                y: 'top',
                icon: 'rect',
                itemWidth: 10,
                itemHeight: 6,
                textStyle: {
                    color: '#fff',
                    fontSize: 12
                }
            },
            calculable : true,
            grid: {
                top: 30,
                left: 14,
                right: 30,
                bottom: 10,
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    boundaryGap : false,
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    data : ['2014','2015','2016','2017','2018','历年']
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name : '人',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    axisTick: {show: false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: '#172649'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'新生',
                    type:'line',
                    areaStyle: {
                        opacity: 0.3
                    },
                    data:[12000, 260000, 180000, 250004, 420000]
                },
                {
                    name:'招生',
                    type:'line',
                    areaStyle: {
                        opacity: 0.3
                    },
                    data:[320000, 160000, 130000, 354000, 220000]
                }
            ]
        }
        echartFour.setOption(newAndRecruitStudent);

        $('#primary').click(function () {
            let newData=[12000, 260000, 180000, 250004, 420000]
            let recruit=[320000, 160000, 130000, 354000, 220000]
            newAndRecruitStudent.series[0].data=newData;
            newAndRecruitStudent.series[1].data=recruit;
            echartFour.setOption(newAndRecruitStudent);

        })
        $('#middle').click(function () {
            let newData=[random(11000,13000),random(250000,270000),random(170000,190000),random(230000,270000),random(380000,460000)]
            let recruit=[random(300000,340000),random(150000,170000),random(120000,140000),random(320000,370000),random(200000,240000)]
            newAndRecruitStudent.series[0].data=newData;
            newAndRecruitStudent.series[1].data=recruit;
            echartFour.setOption(newAndRecruitStudent);
        })
        $('#high').click(function () {
            let newData=[random(11000,13000),random(250000,270000),random(170000,190000),random(230000,270000),random(380000,460000)]
            let recruit=[random(300000,340000),random(150000,170000),random(120000,140000),random(320000,370000),random(200000,240000)]
            newAndRecruitStudent.series[0].data=newData;
            newAndRecruitStudent.series[1].data=recruit;
            echartFour.setOption(newAndRecruitStudent);
        })


        // 学生情况分析
        function students(el, option) {
            var charts = echarts.init(el);
            arr.push(charts);
            charts.setOption({
                color: ['#00ccdd','#905eff'],
                title: {
                    text: option.title,
                    x: 'center',
                    y: 30,
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
                    left: 'center',
                    bottom: 10,
                    data: option.legend,
                    itemWidth: 10,
                    itemHeight: 6,
                },
                tooltip: {
                    trigger: 'item'
                },
                series: [
                    {
                        name: option.title,
                        type: 'pie',
                        radius: ['50%', '80%'],
                        center: ['50%', '50%'],
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
        var studentsData = [
            {
                title: '留守儿童',
                legend: ['非留守','留守儿童'],
                data: [
                    { name: '非留守', value: random(270,330) },
                    { name: '留守儿童', value: random(500,600) }
                ]
            },
            {
                title: '独生子女',
                legend: ['是','否'],
                data: [
                    { name: '是', value: random(310,370) },
                    { name: '否', value: random(450,500) }
                ]
            },
            {
                title: '性别',
                legend: ['男','女'],
                data: [
                    { name: '男', value: random(310,370) },
                    { name: '女', value: random(220,260) }
                ]
            },
            {
                title: '民族',
                legend: ['汉族','民族'],
                data: [
                    { name: '汉族', value: random(310,370) },
                    { name: '民族', value: random(450,500) }
                ]
            }
        ];
        $('.echart').each(function (index,ele) {
            students(this, studentsData[index]);
        });


        // 控辍保学政策情况趋势图
        var echartSix = echarts.init($('#echart-six')[0]);
        arr.push(echartSix);
        var sixOption={
            tooltip : {
                trigger: 'axis'
            },
            color: ['#00dd94', '#00ccdd', '#905eff'],
            legend: {
                data:['辍学','疑似辍学','返校'],
                x: 'center',
                y: 'top',
                icon: 'rect',
                itemWidth: 10,
                itemHeight: 6,
                textStyle: {
                    color: '#fff',
                    fontSize: 12
                }
            },
            calculable : true,
            grid: {
                top: 30,
                left: 14,
                right: 30,
                bottom: 10,
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    boundaryGap : false,
                    axisTick: {show: false},
                    splitLine:{show:false},
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    data : ['2014','2015','2016','2017','2018','历年']
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name : '人',
                    nameTextStyle: {
                        color: '#fff',
                    },
                    axisTick: {show: false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff',
                            fontSize: 12
                        }
                    },
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: '#243255',
                            width: 2
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: '#172649'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'辍学',
                    type:'line',
                    data:[12000, 260000, 130000, 250004, 420000]
                },
                {
                    name:'疑似辍学',
                    type:'line',
                    data:[230000, 234000, 191000, 130000, 330000]
                },
                {
                    name:'返校',
                    type:'line',
                    data:[320000, 160000, 130000, 354000, 220000]
                }
            ]
        }
        echartSix.setOption(sixOption);

        $('#cx1').click(function () {
            let dropOutData=[random(1100,1300),random(240000,280000),random(110000,150000),random(230000,270000),random(390000,460000)];
            let likeDropData=[random(210000,250000),random(210000,250000),random(180000,200000),random(110000,150000),random(300000,360000)];
            let baoXueData=[random(300000,340000),random(140000,180000),random(110000,150000),random(310000,390000),random(200000,240000)];
            sixOption.series[0].data=dropOutData;
            sixOption.series[1].data=likeDropData;
            sixOption.series[2].data=baoXueData;
            echartSix.setOption(sixOption);
        })
        $('#cx2').click(function () {
            let dropOutData=[random(1100,1300),random(240000,280000),random(110000,150000),random(230000,270000),random(390000,460000)];
            let likeDropData=[random(210000,250000),random(210000,250000),random(180000,200000),random(110000,150000),random(300000,360000)];
            let baoXueData=[random(300000,340000),random(140000,180000),random(110000,150000),random(310000,390000),random(200000,240000)];
            sixOption.series[0].data=dropOutData;
            sixOption.series[1].data=likeDropData;
            sixOption.series[2].data=baoXueData;
            echartSix.setOption(sixOption);
        })
        //窗口变化，图表resize
        $(window).resize(resizeChart);

    })


    function random(lower, upper) {
        return Math.floor(Math.random() * (upper - lower)) + lower;
    }
</script>

</body>

</html>