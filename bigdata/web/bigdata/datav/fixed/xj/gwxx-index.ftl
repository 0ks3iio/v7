<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>公文数据监控</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>

<body class="offmonitor-box">
<div class="offmo-body">
    <!--头部 S-->
    <div class="offmo-header">
        <div class="offmo-title">
            <div>新疆公文数据监控</div>
            <div class="offmo-title-back"></div>
        </div>
        <div class="offmo-header-li">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/dispatch.png">
            <div>
                <div class="offmo-top">发文总量</div>
                <div class="offmo-down" id="sendNum"></div>
            </div>
        </div>
        <div class="offmo-header-li">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/receipt.png">
            <div>
                <div class="offmo-top">收文总量</div>
                <div class="offmo-down" id="receiveNum"></div>
            </div>
        </div>
        <div class="offmo-header-li">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/population.png">
            <div>
                <div class="offmo-top">累计收文覆盖人数</div>
                <div class="offmo-down" id="activePeople"></div>
            </div>
        </div>
        <div class="offmo-header-li">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/signature.png">
            <div>
                <div class="offmo-top">累计签收量</div>
                <div class="offmo-down" id="signNum"></div>
            </div>
        </div>
        <div class="offmo-header-li">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/signature.png">
            <div>
                <div class="offmo-top">收文归档量</div>
                <div class="offmo-down" id="fileNum"></div>
            </div>
        </div>
        <div class="time-box-wrap mymaptime">
            <div class="time-wrap">
                <i class="wpfont icon-clock"></i>
                <span class="time"></span>
            </div>
        </div>
    </div>
    <!--头部 E-->
    <!--主体 S-->
    <div class="offmo-content">
        <div class="offmo-left">
            <div class="offmo-maptitle"><span class="offmo-dizhi">新疆</span>数据总览</div>
            <div class="dispatch-btngroup map-btngroup" id="map-one">
                <span class="active" data-id="1">全平台</span>
                <span data-id="2">省厅</span>
                <span data-id="3">地区模式</span>
            </div>
            <div id="map" class="offmo-map"></div>
            <div class="dispatch-content">
                <div class="dispatch-title gongwentitle">各地级市公文量</div>
                <div class="dispatch-body">
                    <div class="dispatch-left">
                        <div class="dispatch-btngroup" id="dispatch-one">
                            <span class="active" data-id="1">发文</span>
                            <span data-id="2">收文</span>
                        </div>
                        <div id="chart1" class="dispatch-chart1"></div>
                    </div>
                    <div class="dispatch-right">
                        <div class="dispatch-rtitle">公文量</div>
                        <div class="dispatch-rbody">
                            <div class="dispatch-oneli">
                                <div><span class="shoufa-title">发文</span>时间</div>
                                <div><span class="shoufa-title">发文</span>单位</div>
                                <div id="titleType">接收人</div>
                                <div><span class="shoufa-title">发文</span>名称</div>
                            </div>
                            <div class="dispatch-rcontent">
                                <div class="dispatch-rshow">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="offmo-right">
            <div class="offmo-rli">
                <div class="offmo-rli-header">收发文趋势</div>
                <div class="offmo-rli-content">
                    <div id="chart2" class="offmo-chart2"></div>
                </div>
            </div>
            <div class="offmo-rli">
                <div class="offmo-rli-header"><span class="shoufa-rank"></span>收发文排名</div>
                <div class="offmo-rli-content">
                    <div class="dispatch-btngroup" id="dispatch-two">
                        <span class="active" data-id="1">发文</span>
                        <span data-id="2">收文</span>
                    </div>
                    <div class="dispatch-rank" id="dispatch-rank">

                    </div>
                </div>
            </div>
            <div class="offmo-rli">
                <div class="offmo-rli-header">发文属性分布</div>
                <div class="offmo-rli-content">
                    <div class="offmo-chart3-title">
                        <div>发文类型</div>
                        <div>公开属性</div>
                        <div>紧急程度</div>
                    </div>
                    <div id="chart3" class="offmo-chart3"></div>
                </div>
            </div>
        </div>
    </div>
    <!--主体 E-->
</div>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts-gl.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
<script>
    $(function () {
        var timeList = ${timeList};
        var allNumTitle = ${allNumTitle};
        var receiveActiveList = ${receiveActiveList};
        var distributedList = ${distributedList};
        var trendList = ${trendList};
        var gundongtime;
        var nowTime = ${nowTime};
        //显示时间
        getTime();
        setInterval(getTime, 1000);
        function getTime() {
            var date = new Date(nowTime);
            var year = date.getFullYear();
            var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            $('.time').text(year + '-' + month + '-' + day + ' ' + hours + ":" + minutes + ":" + seconds);
            nowTime += 1000;
        }

        //图表
        var arr = [];
        function resizeChart() {
            for (var i = 0; i < arr.length; i++) {
                arr[i].resize()
            }
        }

        // 初始化图表
        var myChart = echarts.init(document.getElementById('map'));
        arr.push(myChart);
        // JSON文件(地图数据)路径
        var uploadedDataURL = "${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/650000.json";

        var data_1 = [
                { name: '巴音郭楞蒙古自治州', value: 0},
                { name: '乌鲁木齐市', value: 0, label: {padding: [30, 0, 0, 0]}},
                { name: '克拉玛依市', value: 0 },
                { name: '吐鲁番市', value: 0 },
                { name: '哈密市', value: 0 },
                { name: '阿克苏地区', value: 0 },
                { name: '喀什地区', value: 0 },
                { name: '和田地区', value: 0 },
                { name: '昌吉回族自治州', value: 0 },
                { name: '博尔塔拉蒙古自治州', value: 0 },
                { name: '克孜勒苏柯尔克孜自治州', value: 0 },
                { name: '伊犁哈萨克自治州', value: 0 },
                { name: '塔城地区', value: 0, label: {padding: [0, 0, 30, 0]}},
                { name: '阿勒泰地区', value: 0 }
            ];

        var data_2 = [
            { name: '巴音郭楞蒙古自治州', value: 0,
                itemStyle: {            // itemStyle高亮时的配置
                    color: '#63FFFF'   // 高亮时地图板块颜色改变
                },
                label: {
                    show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                    //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                    //formatter:,               // 标签内容格式器
                    textStyle: {                // 标签的字体样式
                        color: '#fff',                  // 地图初始化区域字体颜色
                        fontSize: 14,                    // 字体大小
                        opacity: 1,                     // 字体透明度
                        backgroundColor: 'rgba(0,0,0,0.5)'      // 字体背景色
                    }
                }},
            { name: '乌鲁木齐市', value: 0},
            { name: '克拉玛依市', value: 0 },
            { name: '吐鲁番市', value: 0 },
            { name: '哈密市', value: 0 },
            { name: '阿克苏地区', value: 0 },
            { name: '喀什地区', value: 0 },
            { name: '和田地区', value: 0 },
            { name: '昌吉回族自治州', value: 0 },
            { name: '博尔塔拉蒙古自治州', value: 0 },
            { name: '克孜勒苏柯尔克孜自治州', value: 0 },
            { name: '伊犁哈萨克自治州', value: 0 },
            { name: '塔城地区', value: 0},
            { name: '阿勒泰地区', value: 0 }
        ];

        $.each(receiveActiveList, function (x, allActive) {
            var regionName = getRegionName(allActive.region_code + "00");
            if (regionName !== "无") {
                $.each(data_1, function (y, oneActive) {
                    if (oneActive.name === regionName) {
                        oneActive.value = allActive.receive_activeuser_num;
                    }
                });
                $.each(data_2, function (y, oneActive) {
                    if (oneActive.name === regionName) {
                        oneActive.value = allActive.receive_activeuser_num;
                    }
                });
            }
        });

        var showindex = 0;
        // 引入JSON文件
        $.getJSON(uploadedDataURL, function (geoJson) {

            // 注册地图名字(xinjiang)和数据(geoJson)
            echarts.registerMap('xinjiang', geoJson);

            // 图表配置项
            var option = {
                tooltip: {							// 提示框
                    trigger: 'item',
                    formatter: function (params) {
                        return params.value;
                    }
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
                        emphasis: {             // 鼠标 hover 高亮时图形和标签的样式 (当鼠标放上去时  label和itemStyle 的样式)
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
                                    backgroundColor: 'rgba(0,0,0,0.5)'      // 字体背景色
                                },
                            }
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

                        data: data_2
                    }]
            };

            //全平台热力图
            var option_1 = {

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
                    max: 50000,
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
                        data: data_1
                    }
                ]
            };

            //省厅飞线图
            var option_2 = {
                geo3D: {
                    map: 'xinjiang',
                    light: {
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
                    tooltip: {							// 提示框
                        trigger: 'item',
                        formatter: function (params) {
                            return params.name;
                        }
                    },
                    label: {                // 标签的相关设置
                        show: false,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                        //distance: 50,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                        //formatter:,               // 标签内容格式器
                        textStyle: {                // 标签的字体样式
                            color: '#000',                  // 地图初始化区域字体颜色
                            fontSize: 12,                    // 字体大小
                            opacity: 1,                     // 字体透明度
                            backgroundColor: 'rgba(0,23,11,0)',      // 字体背景色
                            borderColor: '#fff'
                        }
                    },
                    emphasis: {
                        label: {                // 标签的相关设置
                            show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                            //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                            //formatter:,               // 标签内容格式器
                            textStyle: {                // 标签的字体样式
                                color: '#fff',                  // 地图初始化区域字体颜色
                                fontSize: 14,                    // 字体大小
                                opacity: 1,                     // 字体透明度
                                backgroundColor: 'rgba(0,0,0,5)'      // 字体背景色
                            },
                        },
                    },
                    viewControl: {
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

                    itemStyle: {
                        color: '#1C1F6B'
                    },

                    regionHeight: 3
                },
                series: [{
                    type: 'lines3D',

                    coordinateSystem: 'geo3D',

                    effect: {
                        show: true,
                        trailWidth: 2,
                        trailOpacity: 0.5,
                        trailLength: 0.2,
                        constantSpeed: 5
                    },

                    blendMode: 'lighter',

                    lineStyle: {
                        width: 1,
                        opacity: 0.25,
                        color: '#EE913A'
                    },

                    data: [
                        [
                            [83.395137, 39.321629], [92.171177, 38.960525]
                        ],
                        [
                            [83.340969, 42.109448], [89.158031, 45.596913]
                        ],
                        [
                            [88.690053, 36.368078], [89.158031, 45.596913]
                        ],
                        [
                            [78.082143, 40.231728], [89.158031, 45.596913]
                        ],
                        [
                            [83.340969, 42.109448], [83.794464, 44.719469]
                        ]
                    ]
                }]
            }

            // 设置图表实例的配置项以及数据，万能接口，所有参数和数据的修改都可以通过setOption完成，ECharts 会合并新的参数和数据，然后刷新图表。
            myChart.setOption(option_1);

            var timeindex = 0;//一二模式的计时器
            var btnindex = 1; //确定在哪个模式的参数
            showindex = 0;  //三模式的计时器
            var mytime = null;

            //各个模式的循环函数
            function start() {
                if (mytime != null) {//判断计时器是否为空
                    clearInterval(mytime);
                    mytime = null;
                }

                mytime = setInterval(function () {
                    //第一模式
                    if (btnindex == 1) {
                        $(".offmo-dizhi").html('新疆');
                        $(".gongwentitle").html('各地级市公文量');
                        $(".shoufa-rank").html('');
                        if (timeindex == 0) {
                            myChart.clear();
                            myChart.setOption(option_1);
                        }
                        timeindex++;
                        //20s后切换到第二状态，修改各个状态，修改地图
                        if (timeindex == 2) {
                            timeindex = 0;
                            btnindex = 2;
                            $("#map-one").find("span").eq(1).addClass("active").siblings().removeClass("active");
                            $(".gongwentitle").html('省厅各部门公文量');
                            $(".shoufa-rank").html('省厅直属大学');
                            myChart.clear();
                            myChart.setOption(option_2);
                            allhidedong();
                            setTimeout(function () {
                                refreshDataTwo();
                            }, 500)
                            setTimeout(function () {
                                showdong();
                            }, 1000)
                        }
                    } else if (btnindex == 2) {
                        //第二模式
                        $(".gongwentitle").html('省厅各部门公文量');
                        $(".shoufa-rank").html('省厅直属大学');
                        if (timeindex == 0) {
                            //option.series[0].itemStyle.color = '#63FFFF';
                            myChart.clear();
                            myChart.setOption(option_2);
                        }
                        timeindex++;
                        //20s后切换到第二状态，修改各个状态，修改地图
                        if (timeindex == 2) {
                            timeindex = 0;
                            btnindex = 3;
                            $("#map-one").find("span").eq(2).addClass("active").siblings().removeClass("active");
                            $(".gongwentitle").html('各教育局公文量');
                            $(".shoufa-rank").html('');
                            myChart.clear();
                            onetimeshow();
                        }
                    } else if (btnindex == 3) {
                        //第三模式
                        $(".gongwentitle").html('各教育局公文量');
                        $(".shoufa-rank").html('');
                        myChart.clear();
                        showindex++;
                        onetimeshow();
                    }
                }, 10000)
            }

            //地区模式的切换函数
            function onetimeshow() {
                for (var i = 0; i < data_2.length; i++) {
                    delete data_2[i].itemStyle;
                    delete data_2[i].label;
                }
                if (showindex >= 13) {
                    showindex = 0;
                    btnindex = 1;
                    timeindex = 0;
                    $("#map-one").find("span").eq(0).addClass("active").siblings().removeClass("active");
                    myChart.clear();
                    myChart.setOption(option_1);
                    allhidedong();
                    setTimeout(function () {
                        refreshDataOne();
                    }, 500);
                    setTimeout(function () {
                        showdong();
                    }, 1000);
                } else {
                    $(".offmo-dizhi").html(data_2[showindex].name);
                    data_2[showindex].itemStyle = {color: '#63FFFF'};
                    data_2[showindex].label = {
                        show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                        //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                        //formatter:,               // 标签内容格式器
                        textStyle: {                // 标签的字体样式
                            color: '#fff',                  // 地图初始化区域字体颜色
                            fontSize: 14,                    // 字体大小
                            opacity: 1,                     // 字体透明度
                            backgroundColor: 'rgba(0,0,0,0.5)'      // 字体背景色
                        }
                    };
                    option.series[0].data = data_2;
                    //myChart.resize();
                    myChart.setOption(option);
                    allhidedong();
                    setTimeout(function () {
                        refreshDataThree(data_2[showindex].name);
                    }, 500);
                    setTimeout(function () {
                        showdong();
                    }, 1000)
                }

            }


            // 处理地图点击事件,高亮
            myChart.on('click', function (params) {
                if (btnindex == 3) {
                    for (var i = 0; i < data_2.length; i++) {
                        delete data_2[i].itemStyle;
                        delete data_2[i].label;
                    }
                    $(".offmo-dizhi").html(params.name);
                    window.clearInterval(mytime);
                    showindex = params.dataIndex;
                    data_2[showindex].itemStyle = {color: '#63FFFF'};
                    data_2[showindex].label = {
                        show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                        //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                        //formatter:,               // 标签内容格式器
                        textStyle: {                // 标签的字体样式
                            color: '#fff',                  // 地图初始化区域字体颜色
                            fontSize: 14,                    // 字体大小
                            opacity: 1,                     // 字体透明度
                            backgroundColor: 'rgba(0,0,0,0.5)'      // 字体背景色
                        }
                    };
                    option.series[0].data = data_2;
                    //myChart.resize();
                    myChart.setOption(option);
                    allhidedong();
                    setTimeout(function () {
                        refreshDataThree(params.name);
                    }, 500);
                    setTimeout(function () {
                        showdong();
                    }, 1000)
                    start();
                }
            });

            //地图按钮组,切换模式
            $("#map-one").find("span").click(function () {
                btnindex = $(this).attr("data-id");
                $(this).addClass("active").siblings().removeClass("active");
                clearInterval(mytime);
                mytime = null;
                timeindex = 0;
                showindex = 0;
                if (btnindex == 1) {
                    myChart.clear();
                    myChart.setOption(option_1);
                    allhidedong();
                    setTimeout(function () {
                        refreshDataOne();
                    }, 500);
                    setTimeout(function () {
                        showdong();
                    }, 1000)
                } else if (btnindex == 2) {
                    myChart.clear();
                    myChart.setOption(option_2);
                    allhidedong();
                    setTimeout(function () {
                        refreshDataTwo();
                    }, 500);
                    setTimeout(function () {
                        showdong();
                    }, 1000)
                } else if (btnindex == 3) {
                    myChart.clear();
                    onetimeshow();
                }
                start();
            })


            //移入地图不切换

            $("#map").hover(function () {
                clearInterval(mytime);
                mytime = null;
            }, function () {
                start();
            })

            setTimeout(function () {
                start()
            }, 2000)
        });


        //公文量
        var myChart1 = echarts.init(document.getElementById('chart1'));
        arr.push(myChart1);
        var onelightnum = -1;

        option1 = {
            xAxis: {
                data: [],
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
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10,
            },
            yAxis: {
                show: false
            },
            grid: {
                top: 20,
                left: 10,
                right: 10,
                bottom: 30,
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: '{b}<br/>{c1}'
            },
            dataZoom: [
                {
                    type: 'slider',
                    start: 0,
                    end: 40,
                    bottom: 0,
                    textStyle: {
                        color: '#ffffff'
                    },
                    borderColor: 'rgba(31, 28, 98, 0)',
                    fillerColor: '#41A3D2',
                    handleSize: 0,
                    backgroundColor: '#1F1C62',
                    height: 6,
                }
            ],
            series: [
                { // For shadow
                    type: 'bar',
                    itemStyle: {
                        normal: { color: 'rgba(31, 28, 98, 1)' },
                    },
                    barGap: '-100%',
                    barCategoryGap: '40%',
                    data: [],
                    animation: false
                },
                {
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            color: function (params) {
                                return (params.dataIndex !== onelightnum) ?
                                    '#4DE8EA' :
                                    '#4ea3f7';
                            }
                        },
                        emphasis: {
                            color: '#4ea3f7'
                        },
                    },
                    data: []
                }
            ]
        };

        myChart1.on('click', function (params) {
            onelightnum = params.dataIndex;
            myChart1.resize();
            $(".dispatch-rcontent").find(".dispatch-no-content").remove();
            $(".dispatch-rtitle").html(params.name);
            var tabType = $("#map-one").find(".active").attr("data-id");
            var documentType = $("#dispatch-one").find(".active").attr("data-id");
            showRunningWater(tabType,documentType,params.name);
        });

        myChart2 = echarts.init(document.getElementById('chart2'));
        arr.push(myChart2);

        var timeArray = new Array(5);
        $.each(timeList, function (index, value) {
            timeArray[index] = value.substring(2);
        });

        var tabOneSendDateMonth = [0, 0, 0, 0, 0];
        var tabOneReceiveDateMonth = [0, 0, 0, 0, 0];
        $.each(trendList, function (x, trendValue) {
            $.each(timeList, function (y, timwValue) {
                if (trendValue.time === timwValue) {
                    tabOneSendDateMonth[y] += trendValue.send_month_num;
                    tabOneReceiveDateMonth[y] += trendValue.receive_month_num;
                }
            });
        });
        var tabTwoSendDateMonth = [0, 0, 0, 0, 0];
        var tabTwoReceiveDateMonth = [0, 0, 0, 0, 0];
        $.each(${sTtrendList}, function (x, trendValue) {
            $.each(timeList, function (y, timwValue) {
                if (trendValue.time === timwValue) {
                    tabTwoSendDateMonth[y] += trendValue.send_month_num;
                    tabTwoReceiveDateMonth[y] += trendValue.receive_month_num;
                }
            });
        });

        option2 = {
            tooltip: {
                trigger: 'axis'
            },
            color: ['#1EBCD3', '#0256E1'],
            legend: {
                data: ['发文','收文'],
                top: 20,
                textStyle: {
                    color: '#41A3D2'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: timeArray,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        color: '#41A3D2',
                        fontSize: 14
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        color: '#41A3D2',
                        fontSize: 14
                    },
                    splitLine: {
                        show: false
                    }
                }
            ],
            series: [
                {
                    name: '发文',
                    type: 'line',
                    areaStyle: {
                        opacity: 0.5
                    },
                    data: []
                },
                {
                    name: '收文',
                    type: 'line',
                    areaStyle: {
                        opacity: 0.5
                    },
                    data: []
                }
            ]
        };

        myChart3 = echarts.init(document.getElementById('chart3'));
        arr.push(myChart3);

        var otherSendType = 0;
        var send_type_resolution = 0,send_type_decide = 0,send_type_command = 0,send_type_publicreport = 0,send_type_pubnotice = 0,send_type_announce = 0,
            send_type_view = 0,send_type_notice = 0,send_type_bulletin = 0,send_type_report = 0,send_type_requestinst = 0,send_type_approval = 0,
            send_type_bill = 0,send_type_letter = 0, send_type_summary = 0,send_type_instruct = 0,send_type_rules = 0,send_type_regulations = 0,
            send_type_briefing = 0,send_type_other = 0,send_prop_pereopen = 0,send_prop_applipublic = 0,send_prop_noapplipublic = 0,send_extraurgent = 0,
            send_urgent = 0,send_commonly = 0;
        $.each(distributedList, function (index, value) {
            send_type_resolution += value.send_type_resolution;send_type_decide += value.send_type_decide;send_type_command += value.send_type_command;
            send_type_publicreport += value.send_type_publicreport;send_type_pubnotice += value.send_type_pubnotice;send_type_announce += value.send_type_announce;
            send_type_view += value.send_type_view;send_type_notice += value.send_type_notice;send_type_bulletin += value.send_type_bulletin;
            send_type_report += value.send_type_report;send_type_requestinst += value.send_type_requestinst;send_type_approval += value.send_type_approval;
            send_type_bill += value.send_type_bill;send_type_letter += value.send_type_letter;send_type_summary += value.send_type_summary;
            send_type_instruct += value.send_type_instruct;send_type_rules += value.send_type_rules;send_type_regulations += value.send_type_regulations;
            send_type_briefing += value.send_type_briefing;send_type_other += value.send_type_other;send_prop_pereopen += value.send_prop_pereopen;
            send_prop_applipublic += value.send_prop_applipublic;send_prop_noapplipublic += value.send_prop_noapplipublic;send_extraurgent += value.send_extraurgent;
            send_urgent += value.send_urgent;send_commonly += value.send_commonly;
        });
        otherSendType = send_type_other;
        var tabOneSendType = [{name:"决议",value:send_type_resolution},{name:"决定",value:send_type_decide},{name:"命令",value:send_type_command},
            {name:"公报",value:send_type_publicreport},{name:"公告",value:send_type_pubnotice},{name:"通告",value:send_type_announce},
            {name:"意见",value:send_type_view},{name:"通知",value:send_type_notice},{name:"通报",value:send_type_bulletin},
            {name:"报告",value:send_type_report},{name:"请示",value:send_type_requestinst},{name:"批复",value:send_type_approval},
            {name:"议案",value:send_type_bill},{name:"函",value:send_type_letter},{name:"纪要",value:send_type_summary},
            {name:"指示",value:send_type_instruct},{name:"条例",value:send_type_rules},{name:"规定",value:send_type_regulations},
            {name:"简报",value:send_type_briefing}];
        //排序
        rankSendProperty(tabOneSendType);
        for (var i=5;i<tabOneSendType.length;i++) {
            otherSendType += tabOneSendType[i].value;
        }
        tabOneSendType.splice(5,14); tabOneSendType.push({name:"其他",value:otherSendType});
        rankSendProperty(tabOneSendType);
        var tabOneSendPropPereopen = [{name:"主动公开",value:send_prop_pereopen},{name:"申请公开",value:send_prop_applipublic},{name:"不予公开",value:send_prop_noapplipublic}];
        var tabOneSendLevel = [{name:"特急",value:send_extraurgent},{name:"紧急",value:send_urgent},{name:"一般",value:send_commonly}];
        rankSendProperty(tabOneSendPropPereopen);
        rankSendProperty(tabOneSendLevel);

        var tabTwoSendType;
        var tabTwoSendPropPereopen;
        var tabTwoSendLevel;
        <#if sendSTDistributed?exists>
            var sendSTDistributed = ${sendSTDistributed};
            otherSendType = sendSTDistributed.send_type_other;
            tabTwoSendType = [{name:"决议",value:sendSTDistributed.send_type_resolution},{name:"决定",value:sendSTDistributed.send_type_decide},{name:"命令",value:sendSTDistributed.send_type_command},
                {name:"公报",value:sendSTDistributed.send_type_publicreport},{name:"公告",value:sendSTDistributed.send_type_pubnotice},{name:"通告",value:sendSTDistributed.send_type_announce},
                {name:"意见",value:sendSTDistributed.send_type_view},{name:"通知",value:sendSTDistributed.send_type_notice},{name:"通报",value:sendSTDistributed.send_type_bulletin},
                {name:"报告",value:sendSTDistributed.send_type_report},{name:"请示",value:sendSTDistributed.send_type_requestinst},{name:"批复",value:sendSTDistributed.send_type_approval},
                {name:"议案",value:sendSTDistributed.send_type_bill},{name:"函",value:sendSTDistributed.send_type_letter},{name:"纪要",value:sendSTDistributed.send_type_summary},
                {name:"指示",value:sendSTDistributed.send_type_instruct},{name:"条例",value:sendSTDistributed.send_type_rules},{name:"规定",value:sendSTDistributed.send_type_regulations},
                {name:"简报",value:sendSTDistributed.send_type_briefing}];
            rankSendProperty(tabTwoSendType);
            for (var i=5;i<tabTwoSendType.length;i++) {
                otherSendType += tabTwoSendType[i].value;
            }
            tabTwoSendType.splice(5,14); tabTwoSendType.push({name:"其他",value:otherSendType});
            rankSendProperty(tabTwoSendType);
            tabTwoSendPropPereopen = [{name:"主动公开",value:sendSTDistributed.send_prop_pereopen},{name:"申请公开",value:sendSTDistributed.send_prop_applipublic},{name:"不予公开",value:sendSTDistributed.send_prop_noapplipublic}];
            tabTwoSendLevel = [{name:"特急",value:sendSTDistributed.send_extraurgent},{name:"紧急",value:sendSTDistributed.send_urgent},{name:"一般",value:sendSTDistributed.send_commonly}];
            rankSendProperty(tabTwoSendPropPereopen);
            rankSendProperty(tabTwoSendLevel);
        <#else>
            tabTwoSendType = [{name:"决议",value:0},{name:"决定",value:0},{name:"命令",value:0},{name:"公报",value:0},{name:"公告",value:0},{name:"其他",value:0}];
            tabTwoSendPropPereopen = [{name:"主动公开",value:0},{name:"申请公开",value:0},{name:"不予公开",value:0}];
            tabTwoSendLevel = [{name:"特急",value:0},{name:"紧急",value:0},{name:"一般",value:0}];
        </#if>

        option3 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            color: ['#59ced8', '#296bc2', '#a555c9', '#84b900', '#f6a527', '#f56000', '#e6303f'],
            calculable: true,
            series: [
                {
                    name: '发文类型',
                    type: 'pie',
                    hoverAnimation: false,
                    radius: [20, 60],
                    center: ['15%', '60%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    //这个需要按照占比大小排序显示，否则层次不齐很难看
                    data: []
                },
                {
                    name: '公开属性',
                    type: 'pie',
                    hoverAnimation: false,
                    radius: [20, 60],
                    center: ['50%', '60%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    //这个需要按照占比大小排序显示，否则层次不齐很难看
                    data: []
                },
                {
                    name: '紧急程度',
                    type: 'pie',
                    hoverAnimation: false,
                    radius: [20, 60],
                    center: ['85%', '60%'],
                    roseType: 'radius',
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    //这个需要按照占比大小排序显示，否则层次不齐很难看
                    data: []
                }
            ]
        };

        //窗口变化，图表resize
        $(window).resize(function () {
            resizeChart();
        })


        //显示弹窗
        function showdong() {
            //公文量
            $(".dispatch-rshow").css("top", 0);
            //收发文排名
            $("#dispatch-rank").scrollTop(0);

            $(".offmo-header").css("bottom", "0");
            $(".offmo-rli").eq(0).css("left", "0");
            setTimeout(function () {
                $(".offmo-rli").eq(1).css("left", "0");
            }, 200)
            setTimeout(function () {
                $(".offmo-rli").eq(2).css("left", "0");
            }, 400)
            $(".offmo-map").css("right", "0");
            setTimeout(function () {
                $(".dispatch-content").css("right", "0");
            }, 200)
        }

        var dispatchOneMap = {};

        //公文量切换收发文
        $("#dispatch-one").find("span").click(function () {
            onelightnum = 0;
            $(this).addClass("active").siblings().removeClass("active");
            var tabType = $("#map-one").find(".active").attr("data-id");
            var documentType = $(this).attr("data-id");
            var showRegionCode = getRegionCode(data_2[showindex].name);
            if (documentType === "1") {
                $(".shoufa-title").html('发文');
                $("#titleType").html("拟稿人");
            } else {
                $(".shoufa-title").html('收文');
                $("#titleType").html("接收人");
            }
            $(".dispatch-rtitle").html("");
            $.ajax({
                url: '${request.contextPath}/bigdata/datav/fixed/screen/officedocnum',
                data: {"tabType":tabType,"documentType":documentType,"showRegionCode":showRegionCode},
                type: 'post',
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        var data = JSON.parse(response.data);
                        var tabName = [];
                        var tabDate = [];
                        if (tabType === "1") {
                            var tabOneIndex = 0;
                            $.each(data, function (index, value) {
                                var name = getRegionName(value.region_code + "00");
                                if (name !== "无") {
                                    tabName[tabOneIndex] = name;
                                    tabDate[tabOneIndex] = value.num;
                                    dispatchOneMap[name] = value.region_code + "00";
                                    tabOneIndex++;
                                }
                            });
                        } else if (tabType === "2") {
                            $.each(data, function (index, value) {
                                tabName[index] = value.dept_name;
                                tabDate[index] = value.num;
                                dispatchOneMap[value.dept_name] = value.dept_id;
                            });
                        } else {
                            $.each(data, function (index, value) {
                                tabName[index] = value.unit_name;
                                tabDate[index] = value.num;
                                dispatchOneMap[value.unit_name] = value.unit_id;
                            });
                        }
                        var tabYMax = Math.random(Math.max.apply(null, tabDate)*1.2);
                        var tabDataShadow = [];
                        for (var i = 0; i < tabDate.length; i++) {tabDataShadow.push(tabYMax);}
                        option1.xAxis.data = tabName;
                        option1.series[0].data = tabDataShadow;
                        option1.series[1].data = tabDate;
                        if (tabName.length !== 0) {
                            $(".dispatch-rcontent").find(".dispatch-no-content").remove();
                            $(".dispatch-rtitle").html(tabName[0]);
                            showRunningWater(tabType,documentType,tabName[0]);
                        } else {
                            $(".dispatch-rcontent").prepend('<div class="dispatch-no-content">暂无数据</div>');
                            $(".dispatch-rshow").html("");
                        }
                        myChart1.resize();
                        myChart1.setOption(option1);
                    }
                }
            });
        });

        function showRunningWater(tabType,documentType,name) {
            $(".dispatch-rshow").html("");
            var id = dispatchOneMap[name];
            $.ajax({
                url: '${request.contextPath}/bigdata/datav/fixed/screen/record',
                data: {"tabType":tabType,"documentType":documentType,"id":id},
                type: 'post',
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        var data = JSON.parse(response.data);
                        clearInterval(gundongtime);
                        if (data.length !== 0) {
                            $.each(data, function (index, value) {
                                var str = '<div class="dispatch-li"><div>'+ value.time +'</div><div>'+ value.unit_name +'</div><div>'+ value.name +'</div><div>'+ value.title +'</div></div>';
                                $(".dispatch-rshow").append(str);
                            });
                            $(".dispatch-rshow").css("top", 0);
                            gunfawen();
                        } else {
                            $(".dispatch-rcontent").prepend('<div class="dispatch-no-content">暂无数据</div>');
                        }
                    }
                }
            });
        }

        var dispatchTwoMap = {};
        var dispatchTwoNoMap = [];

        //切换收发文排名
        $("#dispatch-two").find("span").click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var tabType = $("#map-one").find(".active").attr("data-id");
            var documentType = $(this).attr("data-id");
            var showRegionCode = getRegionCode(data_2[showindex].name);
            if ($.inArray(tabType+documentType+showRegionCode, dispatchTwoNoMap) >= 0) {
                $("#dispatch-rank").html('<div class="dispatch-no-content">暂无数据</div>');
            } else {
                $("#dispatch-rank").html("");
                if (dispatchTwoMap[tabType+documentType+showRegionCode] != null) {
                    $.each(dispatchTwoMap[tabType+documentType+showRegionCode], function (index, value) {
                        var str = '<div class="rank-li"><div>'+ (index + 1) +'</div><div>'+ value.name +'</div><div>'+ value.num +'</div></div>';
                        $("#dispatch-rank").append(str);
                    });
                } else {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/datav/fixed/screen/ranking',
                        data: {"tabType":tabType,"documentType":documentType,"showRegionCode":showRegionCode},
                        type: 'post',
                        dataType: 'json',
                        success: function (response) {
                            if (response.success) {
                                var data = JSON.parse(response.data);
                                if (data.length !== 0) {
                                    $.each(data, function (index, value) {
                                        var str = '<div class="rank-li"><div>'+ (index + 1) +'</div><div>'+ value.name +'</div><div>'+ value.num +'</div></div>';
                                        $("#dispatch-rank").append(str);
                                    });
                                    dispatchTwoMap[tabType+documentType+showRegionCode] = data;
                                } else {
                                    $("#dispatch-rank").html('<div class="dispatch-no-content">暂无数据</div>');
                                    dispatchTwoNoMap.push(tabType+documentType+showRegionCode);
                                }
                            }
                        }
                    });
                }
            }
            $("#dispatch-rank").scrollTop(0);
        })

        function refreshDataOne() {
            // 标题变化
            $(".offmo-dizhi").html('新疆');
            $(".gongwentitle").html('各地级市公文量');
            $(".shoufa-rank").html('');
            // 总数量
            var sendNum = 0;
            var receiveNum = 0;
            var signNum = 0;
            var fileNum = 0;
            var activePeople = 0;
            $.each(allNumTitle, function (index, value) {
                sendNum += value.send_num;
                receiveNum += value.receive_num;
                signNum += value.receive_num - value.receive_unsign_num;
                fileNum += value.receive_file;
            });
            $.each(receiveActiveList, function (index, value) {
                activePeople += value.receive_activeuser_num;
            });
            $("#sendNum").html(sendNum);
            $("#receiveNum").html(receiveNum);
            $("#signNum").html(signNum);
            $("#fileNum").html(fileNum);
            $("#activePeople").html(activePeople);
            //公务量
            $("#dispatch-one").find("span").eq(0).click();
            //收发文趋势
            option2.series[0].data = tabOneSendDateMonth;
            option2.series[1].data = tabOneReceiveDateMonth;
            myChart2.setOption(option2);
            //收发文排名
            $("#dispatch-two").find("span").eq(0).click();
            //发文属性分布
            option3.series[0].data = tabOneSendType;
            option3.series[1].data = tabOneSendPropPereopen;
            option3.series[2].data = tabOneSendLevel;
            myChart3.setOption(option3);
        }

        function refreshDataTwo() {
            $(".offmo-dizhi").html('新疆');
            $(".gongwentitle").html('省厅各部门公文量');
            $(".shoufa-rank").html('省厅直属大学');
            //总数量
            <#if stNum?exists>
                var stNum = ${stNum};
                $("#sendNum").html(stNum.send_num);
                $("#receiveNum").html(stNum.receive_num);
                $("#signNum").html(stNum.receive_num - stNum.receive_unsign_num);
                $("#fileNum").html(stNum.receive_file);
            <#else>
                $("#sendNum").html(0);
                $("#receiveNum").html(0);
                $("#signNum").html(0);
                $("#fileNum").html(0);
            </#if>
            <#if stReceiveActive?exists>
                var stReceiveActive = ${stReceiveActive};
                $("#activePeople").html(stReceiveActive.receive_activeuser_num);
            <#else>
                $("#activePeople").html(0);
            </#if>
            //公务量
            $("#dispatch-one").find("span").eq(0).click();
            //收发文趋势
            option2.series[0].data = tabTwoSendDateMonth;
            option2.series[1].data = tabTwoReceiveDateMonth;
            myChart2.setOption(option2);
            //收发文排名
            $("#dispatch-two").find("span").eq(0).click();
            //发文属性分布
            option3.series[0].data = tabTwoSendType;
            option3.series[1].data = tabTwoSendPropPereopen;
            option3.series[2].data = tabTwoSendLevel;
            myChart3.setOption(option3);
        }

        function refreshDataThree(name) {
            $(".gongwentitle").html('各教育局公文量');
            $(".shoufa-rank").html('');
            // 总数量
            var regionCode = getRegionCode(name);
            $("#sendNum").html(0);
            $("#receiveNum").html(0);
            $("#signNum").html(0);
            $("#fileNum").html(0);
            $("#activePeople").html(0);
            $.each(allNumTitle, function (index, value) {
                if ((value.region_code + "00") === regionCode) {
                    $("#sendNum").html(value.send_num);
                    $("#receiveNum").html(value.receive_num);
                    $("#signNum").html(value.receive_num - value.receive_unsign_num);
                    $("#fileNum").html(value.receive_file);
                    return false;
                }
            });
            $.each(receiveActiveList, function (index, value) {
                if ((value.region_code + "00") === regionCode) {
                    $("#activePeople").html(value.receive_activeuser_num);
                    return false;
                }
            });
            // 公务量
            $("#dispatch-one").find("span").eq(0).click();
            //收发文趋势
            var tabThreeSendDateMonth = [0, 0, 0, 0, 0];
            var tabThreeReceiveDateMonth = [0, 0, 0, 0, 0];
            $.each(trendList, function (x, trendValue) {
                $.each(timeList, function (y, timwValue) {
                    if ((trendValue.region_code + "00") === regionCode && trendValue.time === timwValue) {
                        tabThreeSendDateMonth[y] += trendValue.send_month_num;
                        tabThreeReceiveDateMonth[y] += trendValue.receive_month_num;
                    }
                });
            });
            option2.series[0].data = tabThreeSendDateMonth;
            option2.series[1].data = tabThreeReceiveDateMonth;
            myChart2.setOption(option2);
            //收发文排名
            $("#dispatch-two").find("span").eq(0).click();
            //发文属性分布
            var otherSendType = 0;
            var tabThreeSendType = [{name:"决议",value:0},{name:"决定",value:0},{name:"命令",value:0},{name:"公报",value:0},{name:"公告",value:0},
                {name:"通告",value:0},{name:"意见",value:0},{name:"通知",value:0},{name:"通报",value:0},{name:"报告",value:0},{name:"请示",value:0},
                {name:"批复",value:0},{name:"议案",value:0},{name:"函",value:0},{name:"纪要",value:0},{name:"指示",value:0},{name:"条例",value:0},
                {name:"规定",value:0},{name:"简报",value:0}];
            var tabThreeSendPropPereopen = [{name:"主动公开",value:0},{name:"申请公开",value:0},{name:"不予公开",value:0}];
            var tabThreeSendLevel = [{name:"特急",value:0},{name:"紧急",value:0},{name:"一般",value:0}];
            $.each(distributedList, function (index, value) {
                if ((value.region_code + "00") === regionCode) {
                    tabThreeSendType = [{name:"决议",value:value.send_type_resolution},{name:"决定",value:value.send_type_decide},{name:"命令",value:value.send_type_command},
                        {name:"公报",value:value.send_type_publicreport},{name:"公告",value:value.send_type_pubnotice},{name:"通告",value:value.send_type_announce},
                        {name:"意见",value:value.send_type_view},{name:"通知",value:value.send_type_notice},{name:"通报",value:value.send_type_bulletin},
                        {name:"报告",value:value.send_type_report},{name:"请示",value:value.send_type_requestinst},{name:"批复",value:value.send_type_approval},
                        {name:"议案",value:value.send_type_bill},{name:"函",value:value.send_type_letter},{name:"纪要",value:value.send_type_summary},
                        {name:"指示",value:value.send_type_instruct},{name:"条例",value:value.send_type_rules},{name:"规定",value:value.send_type_regulations},
                        {name:"简报",value:value.send_type_briefing}];
                    otherSendType = value.send_type_other;
                    tabThreeSendPropPereopen = [{name:"主动公开",value:value.send_prop_pereopen},{name:"申请公开",value:value.send_prop_applipublic},{name:"不予公开",value:value.send_prop_noapplipublic}];
                    tabThreeSendLevel = [{name:"特急",value:value.send_extraurgent},{name:"紧急",value:value.send_urgent},{name:"一般",value:value.send_commonly}];
                    return false;
                }
            });
            rankSendProperty(tabThreeSendType);
            for (var i=5;i<tabThreeSendType.length;i++) {
                otherSendType += tabThreeSendType[i].value;
            }
            tabThreeSendType.splice(5,14); tabThreeSendType.push({name:"其他",value:otherSendType});
            rankSendProperty(tabThreeSendType);
            rankSendProperty(tabThreeSendPropPereopen);
            rankSendProperty(tabThreeSendLevel);
            option3.series[0].data = tabThreeSendType;
            option3.series[1].data = tabThreeSendPropPereopen;
            option3.series[2].data = tabThreeSendLevel;
            myChart3.setOption(option3);
        }

        function allhidedong() {
            $(".offmo-header").css("bottom", "calc(100% + 25px)");
            $(".offmo-rli").eq(0).css("left", "calc(100% + 40px)");
            setTimeout(function () {
                $(".offmo-rli").eq(1).css("left", "calc(100% + 40px)");
            }, 200)
            setTimeout(function () {
                $(".offmo-rli").eq(2).css("left", "calc(100% + 40px)");
            }, 400)
            $(".dispatch-content").css("right", "calc(100% + 40px)");
        }

        //隐藏弹窗,修改啥数据都写在这
        function hidedong() {

        }

        //滚动发文量
        function gunfawen() {
            var contentnum = parseInt($(".dispatch-rcontent").height() / 36);
            var linum = $(".dispatch-rshow").find(".dispatch-li").length;
            if (linum > contentnum) {
                gundongtime = setInterval(function () {
                    if (($(".dispatch-rshow").position().top - 6) / -36 < (linum - contentnum)) {
                        $(".dispatch-rshow").css("top", $(".dispatch-rshow").position().top - 42);
                    } else {
                        $(".dispatch-rshow").css("top", 0);
                    }
                }, 2000)
            }
        }

        refreshDataOne();
        showdong();

        function getRegionCode(name) {
            if (name === "乌鲁木齐市") {
                return "650100";
            } else if (name === "克拉玛依市") {
                return "650200";
            } else if (name === "吐鲁番市") {
                return "652100";
            } else if (name === "哈密市") {
                return "652200";
            } else if (name === "阿克苏地区") {
                return "652900";
            } else if (name === "喀什地区") {
                return "653100";
            } else if (name === "和田地区") {
                return "653200";
            } else if (name === "昌吉回族自治州") {
                return "652300";
            } else if (name === "博尔塔拉蒙古自治州") {
                return "652700";
            } else if (name === "巴音郭楞蒙古自治州") {
                return "652800";
            } else if (name === "克孜勒苏柯尔克孜自治州") {
                return "653000";
            } else if (name === "伊犁哈萨克自治州") {
                return "654000";
            } else if (name === "塔城地区") {
                return "654200";
            } else if (name === "阿勒泰地区"){
                return "654300";
            } else {
                return "000000";
            }
        }

        function getRegionName(code) {
            if (code === "650100") {
                return "乌鲁木齐市";
            } else if (code === "650200") {
                return "克拉玛依市";
            } else if (code === "652100") {
                return "吐鲁番市";
            } else if (code === "652200") {
                return "哈密市";
            } else if (code === "652900") {
                return "阿克苏地区";
            } else if (code === "653100") {
                return "喀什地区";
            } else if (code === "653200") {
                return "和田地区";
            } else if (code === "652300") {
                return "昌吉回族自治州";
            } else if (code === "652700") {
                return "博尔塔拉蒙古自治州";
            } else if (code === "652800") {
                return "巴音郭楞蒙古自治州";
            } else if (code === "653000") {
                return "克孜勒苏柯尔克孜自治州";
            } else if (code === "654000") {
                return "伊犁哈萨克自治州";
            } else if (code === "654200") {
                return "塔城地区";
            } else if (code === "654300"){
                return "阿勒泰地区";
            } else {
                return "无";
            }
        }

        function rankSendProperty(send) {
            send.sort(function(type1,type2) {
                return type2.value - type1.value;
            });
        }
    });

    //滚动
    function startmarquee(lh, speed, delay, id) {
        var t;
        var oHeight = speed; /** div的高度 **/
        var p = false;
        var o = document.getElementById(id);
        var preTop = 0;
        o.scrollTop = 0;
        function start() {
            t = setInterval(scrolling);
            o.scrollTop += 1;
        }
        function scrolling() {
            if (o.scrollTop % lh != 0 && o.scrollTop % (o.scrollHeight - oHeight - 1) != 0) {
                preTop = o.scrollTop;
                o.scrollTop += 1;
                if (preTop >= o.scrollHeight - o.offsetHeight - 1 || preTop == o.scrollTop) {
                    o.scrollTop = 0;
                }
            } else {
                clearInterval(t);
                setTimeout(start, delay);
            }
        }
        setTimeout(start, delay);
    }

    startmarquee(2, 84, 300, "dispatch-rank");

    function circle() { }

    function btnOverstep() { }

</script>
</body>

</html>