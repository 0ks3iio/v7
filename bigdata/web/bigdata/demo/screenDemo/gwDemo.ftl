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
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/receipt.png">
                <div>
                    <div class="offmo-top">收文总量</div>
                    <div class="offmo-down" id="receiveNum"></div>
                </div>
            </div>
            <div class="offmo-header-li">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/dispatch.png">
                <div>
                    <div class="offmo-top">发文总量</div>
                    <div class="offmo-down" id="sendNum"></div>
                </div>
            </div>
            <div class="offmo-header-li">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/monitor/population.png">
                <div>
                    <div class="offmo-top">累计覆盖人数</div>
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
                    <div class="offmo-top">收文流转量</div>
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
                                    <div>接收人</div>
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
                            <div class="rank-li">
                                <div>1</div>
                                <div>乌鲁木齐</div>
                                <div>583</div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="offmo-rli">
                    <div class="offmo-rli-header">发文属性分布</div>
                    <div class="offmo-rli-content">
                        <div class="offmo-chart3-title">
                            <div>发文属性</div>
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

            getTime();
            setInterval(getTime, 1000);
            function getTime() {
                var date = new Date();
                var year = date.getFullYear();
                var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
                var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
                var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
                $('.time').text(year + '-' + month + '-' + day + ' ' + hours + ":" + minutes + ":" + seconds);
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

            // 显示加载动画效果,可以在加载数据前手动调用该接口显示加载动画，在数据加载完成后调用 hideLoading 隐藏加载动画。
            //myChart.showLoading();

            var data = [
                {
                    name: '巴音郭楞蒙古自治州', value: 21900,
                    itemStyle: {            // itemStyle高亮时的配置
                        color: '#63FFFF',   // 高亮时地图板块颜色改变
                    },
                    label: {
                        textStyle: {
                            fontSize: 16
                        }
                    }
                },
                { name: '乌鲁木齐市', value: 20057, },
                { name: '克拉玛依市', value: 15477 },
                { name: '吐鲁番市', value: 31686 },
                { name: '哈密市', value: 6992 },
                { name: '阿克苏地区', value: 44045 },
                { name: '喀什地区', value: 40689 },
                { name: '和田地区', value: 37659 },
                { name: '昌吉回族自治州', value: 45180 },
                { name: '博尔塔拉蒙古自治州', value: 55204 },
                { name: '克孜勒苏柯尔克孜自治州', value: 4918 },
                { name: '伊犁哈萨克自治州', value: 5881 },
                { name: '塔城地区', value: 4178 },
                { name: '阿勒泰地区', value: 2227 },]


            // 引入JSON文件
            $.getJSON(uploadedDataURL, function (geoJson) {

                // 注册地图名字(xinjiang)和数据(geoJson)
                echarts.registerMap('xinjiang', geoJson);

                // 图表配置项		
                var option = {
                    tooltip: {							// 提示框
                        trigger: 'item',
                        formatter: function (params) {
                            return params.name;
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
                            emphasis: {
                                itemStyle: {            // itemStyle高亮时的配置
                                    color: '#63FFFF',   // 高亮时地图板块颜色改变
                                },
                                label: {                // 标签的相关设置
                                    show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                                    //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                                    //formatter:,               // 标签内容格式器
                                    textStyle: {                // 标签的字体样式
                                        color: '#000',                  // 地图初始化区域字体颜色
                                        fontSize: 16,                    // 字体大小
                                        opacity: 1,                     // 字体透明度
                                        backgroundColor: 'rgba(0,23,11,0)'      // 字体背景色
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

                            data: data
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
                        },
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
                                    position: 'insideTop',
                                    textStyle: {
                                        color: '#fff'
                                    },
                                }
                            },
                            itemStyle: {
                                normal: {
                                    borderColor: '#015678',
                                    areaColor: '#081f3a'
                                }
                            },
                            data: [
                                {
                                    name: '乌鲁木齐市', value: 20057, label: {
                                        padding: [30, 0, 0, 0],
                                    }
                                },
                                { name: '克拉玛依市', value: 15477 },
                                { name: '吐鲁番市', value: 31686 },
                                { name: '哈密市', value: 6992 },
                                { name: '阿克苏地区', value: 44045 },
                                { name: '喀什地区', value: 40689 },
                                { name: '和田地区', value: 37659 },
                                { name: '昌吉回族自治州', value: 45180 },
                                { name: '博尔塔拉蒙古自治州', value: 55204 },
                                { name: '巴音郭楞蒙古自治州', value: 21900 },
                                { name: '克孜勒苏柯尔克孜自治州', value: 4918 },
                                { name: '伊犁哈萨克自治州', value: 5881 },
                                {
                                    name: '塔城地区', value: 4178,
                                    label: {
                                        padding: [0, 0, 30, 0],
                                    }
                                },
                                { name: '阿勒泰地区', value: 2227 },
                                { name: '昆玉市', value: 2000 },
                                { name: '阿拉尔市', value: 2180 },
                                { name: '图木舒克市', value: 2180 },
                                { name: '石河子市', value: 290 },
                                { name: '五家渠市', value: 380 },
                                { name: '北屯市', value: 320 },
                                { name: '铁门关市', value: 180 },
                                { name: '双河市', value: 140 },
                                { name: '可克达拉市', value: 190 }
                            ]
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
                                backgroundColor: 'rgba(0,23,11,0)'      // 字体背景色
                            },
                        },
                        emphasis: {
                            label: {                // 标签的相关设置
                                show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                                //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                                //formatter:,               // 标签内容格式器
                                textStyle: {                // 标签的字体样式
                                    color: '#000',                  // 地图初始化区域字体颜色
                                    fontSize: 16,                    // 字体大小
                                    opacity: 1,                     // 字体透明度
                                    backgroundColor: 'rgba(0,23,11,0)'      // 字体背景色
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
                var showindex = 0;  //三模式的计时器
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
                            hidedong();
                            setTimeout(function () {
                                refreshDataOne();
                            }, 500);
                            setTimeout(function () {
                                showdong();
                            }, 1000);
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
                                hidedong();
                                setTimeout(function () {
                                    refreshDataTwo();
                                }, 500);
                                setTimeout(function () {
                                    showdong();
                                }, 1000);
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
                            hidedong();
                            setTimeout(function () {
                                refreshDataTwo();
                            }, 500);
                            setTimeout(function () {
                                showdong();
                            }, 1000);
                            //20s后切换到第二状态，修改各个状态，修改地图
                            if (timeindex == 2) {
                                timeindex = 0;
                                btnindex = 3;
                                $("#map-one").find("span").eq(2).addClass("active").siblings().removeClass("active");
                                $(".gongwentitle").html('各教育局公文量');
                                $(".shoufa-rank").html('');
                                myChart.clear();
                                onetimeshow();
                                hidedong();
                                setTimeout(function () {
                                    refreshDataThree();
                                }, 500);
                                setTimeout(function () {
                                    showdong();
                                }, 1000);
                            }
                        } else if (btnindex == 3) {
                            //第三模式
                            $(".gongwentitle").html('各教育局公文量');
                            $(".shoufa-rank").html('');
                            myChart.clear();
                            showindex++;
                            onetimeshow();
                            hidedong();
                            setTimeout(function () {
                                refreshDataThree();
                            }, 500);
                            setTimeout(function () {
                                showdong();
                            }, 1000);
                        }
                    }, 10000)
                }

                //地区模式的切换函数
                function onetimeshow() {
                    for (let i = 0; i < data.length; i++) {
                        delete data[i].itemStyle;
                        delete data[i].label;
                    }

                    $(".offmo-dizhi").html(data[showindex].name);

                    if (showindex >= 13) {
                        showindex = 0;
                        btnindex = 1;
                        timeindex = 0;
                        $("#map-one").find("span").eq(0).addClass("active").siblings().removeClass("active");
                        myChart.setOption(option);
                        hidedong();
                        setTimeout(function () {
                            showdong();
                        }, 1000)
                    } else {
                        data[showindex].itemStyle = {            // itemStyle高亮时的配置
                            color: '#63FFFF',   // 高亮时地图板块颜色改变
                        };
                        data[showindex].label = {
                            textStyle: {
                                fontSize: 16,
                            }
                        };

                        option.series[0].data = data;
                        //myChart.resize();
                        myChart.setOption(option);
                        hidedong();
                        setTimeout(function () {
                            showdong();
                        }, 1000)
                    }

                }


                // 处理地图点击事件,高亮
                myChart.on('click', function (params) {
                    if (btnindex == 3) {
                        for (let i = 0; i < data.length; i++) {
                            delete data[i].itemStyle;
                            delete data[i].label;
                        }

                        $(".offmo-dizhi").html(params.name);
                        window.clearInterval(mytime);
                        showindex = params.dataIndex;
                        data[showindex].itemStyle = {            // itemStyle高亮时的配置
                            color: '#63FFFF',   // 高亮时地图板块颜色改变
                        };
                        data[showindex].label = {
                            textStyle: {
                                fontSize: 16,
                            }
                        };

                        option.series[0].data = data;
                        //myChart.resize();
                        myChart.setOption(option);

                        hidedong();
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
                    if (btnindex == 1) {
                        $(".offmo-dizhi").html('新疆');
                        $(".gongwentitle").html('各地级市公文量');
                        $(".shoufa-rank").html('');
                        myChart.clear();
                        myChart.setOption(option_1);
                        hidedong();
                        setTimeout(function () {
                            refreshDataOne();
                        }, 500);
                        setTimeout(function () {
                            showdong();
                        }, 1000)
                    } else if (btnindex == 2) {
                        $(".offmo-dizhi").html('新疆');
                        $(".gongwentitle").html('省厅各部门公文量');
                        $(".shoufa-rank").html('省厅直属大学');
                        myChart.clear();
                        myChart.setOption(option_2);
                        hidedong();
                        setTimeout(function () {
                            refreshDataTwo();
                        }, 500);
                        setTimeout(function () {
                            showdong();
                        }, 1000)
                    } else if (btnindex == 3) {
                        $(".gongwentitle").html('各教育局公文量');
                        $(".shoufa-rank").html('');
                        showindex = 0;
                        myChart.clear();
                        onetimeshow();
                        setTimeout(function () {
                            refreshDataThree();
                        }, 500);
                        setTimeout(function () {
                            showdong();
                        }, 1000)
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
            var dataAxis1 = [];
            //柱子要按照收发文数量进行排次
            var data1 = [];
            var data1_1 = [];
            var yMax1 = 500;
            var dataShadow = [];

            for (var i = 0; i < data1.length; i++) {
                dataShadow.push(yMax1);
            }

            option1 = {
                xAxis: {
                    data: dataAxis1,
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
                        data: dataShadow,
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
                        data: data1
                    }
                ]
            };

            // Enable data zoom when user click bar.
            var zoomSize = 6;

            myChart1.on('click', function (params) {
                onelightnum = params.dataIndex;
                myChart1.resize();
                $(".dispatch-rtitle").html(params.name);
                $(".dispatch-rshow").css("top", 0);
            });

            myChart1.setOption(option1);

            //公文量切换收发文
            $("#dispatch-one").find("span").click(function () {
                $(this).addClass("active").siblings().removeClass("active");
                $(".dispatch-rshow").css("top", 0);
                if ($(this).attr("data-id") == 1) {
                    option1.series[1].data = data1;
                    $(".shoufa-title").html('发文');
                    $(".dispatch-rshow").empty().append('<div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>新疆维吾尔自治区教育厅</div>\n' +
                        '                                            <div>赵刚</div>\n' +
                        '                                            <div>关于举办第七届全国信息技术应用水平大赛的通知</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>新疆维吾尔自治区教育厅</div>\n' +
                        '                                            <div>赵刚</div>\n' +
                        '                                            <div>新疆维吾尔自治区校外培训机构“白名单”公示</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>和田地区教育局</div>\n' +
                        '                                            <div>甘信建</div>\n' +
                        '                                            <div>关于印发《援疆省市支持 新疆现代农业发展联席会商制度》</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>2天前</div>\n' +
                        '                                            <div>和田地区教育局</div>\n' +
                        '                                            <div>甘信建</div>\n' +
                        '                                            <div>关于开展2019年新增学士学位授权专业审核工作的通知</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>2天前</div>\n' +
                        '                                            <div>乌鲁木齐市教育局</div>\n' +
                        '                                            <div>张月</div>\n' +
                        '                                            <div>关于印发自治区教育脱贫攻坚干部教育培训宣传方案的通知</div>\n' +
                        '                                        </div>');
                } else {
                    option1.series[1].data = data1_1;
                    $(".shoufa-title").html('收文');
                    $(".dispatch-rshow").empty().append('<div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>乌鲁木齐市教育局</div>\n' +
                        '                                            <div>张月</div>\n' +
                        '                                            <div>关于举办第七届全国信息技术应用水平大赛的通知</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>阿克苏地区教育局</div>\n' +
                        '                                            <div>刘辉</div>\n' +
                        '                                            <div>关于举办第七届全国信息技术应用水平大赛的通知</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>和田地区教育局</div>\n' +
                        '                                            <div>西日阿洪</div>\n' +
                        '                                            <div>关于举办第七届全国信息技术应用水平大赛的通知</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>和田地区教育局</div>\n' +
                        '                                            <div>西日阿洪</div>\n' +
                        '                                            <div>新疆维吾尔自治区校外培训机构“白名单”公示</div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="dispatch-li">\n' +
                        '                                            <div>1天前</div>\n' +
                        '                                            <div>阿克苏地区教育局</div>\n' +
                        '                                            <div>刘辉</div>\n' +
                        '                                            <div>关于举办第七届全国信息技术应用水平大赛的通知</div>\n' +
                        '                                        </div>');
                }
                myChart1.setOption(option1);
            })

            myChart2 = echarts.init(document.getElementById('chart2'));
            arr.push(myChart2);

            option2 = {
                tooltip: {
                    trigger: 'axis'
                },
                color: ['#1EBCD3', '#0256E1'],
                legend: {
                    data: ['收文', '发文'],
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
                        data: ['19-01', '19-02', '19-03', '19-04', '19-05'],
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
                        name: '收文',
                        type: 'line',
                        areaStyle: {
                            opacity: 0.5
                        },
                        data: [120, 132, 101, 134, 90]
                    },
                    {
                        name: '发文',
                        type: 'line',
                        areaStyle: {
                            opacity: 0.5
                        },
                        data: [220, 182, 191, 234, 290]
                    },
                ]
            };

            myChart2.setOption(option2);

            myChart3 = echarts.init(document.getElementById('chart3'));
            arr.push(myChart3);

            option3 = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                color: ['#59ced8','#296bc2','#a555c9','#84b900','#f6a527','#f56000','#e6303f'],
                calculable: true,
                series: [
                    {
                        name: '发文属性',
                        type: 'pie',
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
                        data: [
                            { value: 5, name: 'rose1' },
                            { value: 10, name: 'rose2' },
                            { value: 15, name: 'rose3' },
                            { value: 20, name: 'rose4' },
                            { value: 25, name: 'rose5' },
                            { value: 25, name: 'rose6' },
                            { value: 25, name: 'rose7' },
                            { value: 25, name: 'rose8' },
                        ]
                    },
                    {
                        name: '公开属性',
                        type: 'pie',
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
                        data: [
                            { value: 5, name: 'rose1' },
                            { value: 10, name: 'rose2' },
                            { value: 15, name: 'rose3' },
                            { value: 20, name: 'rose4' },
                            { value: 25, name: 'rose5' },
                        ]
                    },
                    {
                        name: '紧急程度',
                        type: 'pie',
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
                        data: [
                            { value: 5, name: 'rose1' },
                            { value: 10, name: 'rose2' },
                            { value: 15, name: 'rose3' },
                            { value: 20, name: 'rose4' },
                            { value: 25, name: 'rose5' },
                        ]
                    }
                ]
            };

            myChart3.setOption(option3);


            //窗口变化，图表resize
            $(window).resize(function () {
                resizeChart();
            })


            //显示弹窗
            function showdong() {
                //收发文排名
                $("#dispatch-rank").scrollTop(0);
                //公文量
                $(".dispatch-rshow").css("top", 0);


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
            //隐藏弹窗,修改啥数据都写在这
            function hidedong() {
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
            gunfawen();

            function refreshDataOne() {
                // 标题变化
                $(".offmo-dizhi").html('新疆');
                $(".gongwentitle").html('各地级市公文量');
                $(".shoufa-rank").html('');
                // 总数量
                $("#sendNum").html('175,045');
                $("#receiveNum").html('8,254');
                $("#signNum").html('15,782');
                $("#fileNum").html('164,125');
                $("#activePeople").html('142,140');
                //公务量
                $("#dispatch-one").find("span").eq(0).click();
                //收发文趋势
                option2.series[0].data = [4102, 3958, 3865, 4010, 4200];
                option2.series[1].data = [612, 572, 592, 603, 622];
                myChart2.setOption(option2);
                //收发文排名
                $("#dispatch-two").find("span").eq(0).click();
                //发文属性分布
                option3.series[0].data = [
                    { value: 5, name: '其他' },
                    { value: 10, name: '公告' },
                    { value: 15, name: '公报' },
                    { value: 20, name: '通报' },
                    { value: 25, name: '通知' },
                    { value: 25, name: '命令' },
                    { value: 25, name: '决定' },
                    { value: 25, name: '决议' },
                ];
                option3.series[1].data = [
                    { value: 15, name: '不予公开' },
                    { value: 20, name: '申请公开' },
                    { value: 25, name: '主动公开' },
                ];
                option3.series[2].data = [
                    { value: 5, name: '其他' },
                    { value: 10, name: '一般' },
                    { value: 15, name: '紧急' },
                    { value: 20, name: '特急' },
                    { value: 25, name: '特提' },
                ];
                myChart3.setOption(option3);
                //左下角盒子
                dataAxis1 = ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '克孜勒苏柯尔克孜自治州',
                    '昌吉回族自治州', '阿克苏地区', '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市',
                    '喀什地区', '博尔塔拉蒙古自治州', '伊犁哈萨克自治州'];
                data1 = [1241, 1035, 920, 920, 799, 787, 582, 511, 442, 412, 380, 150, 25];
                data1_1 = [8142, 4575, 4598, 4598, 3562, 2585, 2545, 1854, 1744, 1600, 1500, 1300, 241];
                option1.xAxis.data = dataAxis1;
                option1.series[0].data = data1;
                option1.series[1].data = data1_1;
                $("#dispatch-one").find("span").eq(0).click();
                $("#dispatch-two").find("span").eq(0).click();
            }
        });

        function refreshDataTwo() {
            $(".offmo-dizhi").html('新疆');
            $(".gongwentitle").html('省厅各部门公文量');
            $(".shoufa-rank").html('省厅直属大学');
            // 总数量
            $("#sendNum").html('75,140');
            $("#receiveNum").html('4,216');
            $("#signNum").html('252');
            $("#fileNum").html('82,062');
            $("#activePeople").html('71,070');
            //公务量
            $("#dispatch-one").find("span").eq(0).click();
            //收发文趋势
            option2.series[0].data = [2102, 1958, 1865, 2010, 2200];
            option2.series[1].data = [312, 272, 292, 303, 322];
            myChart2.setOption(option2);
            //收发文排名
            $("#dispatch-two").find("span").eq(0).click();
            //发文属性分布
            option3.series[0].data = [
                { value: 5, name: '其他' },
                { value: 10, name: '公告' },
                { value: 15, name: '公报' },
                { value: 20, name: '通报' },
                { value: 25, name: '通知' },
                { value: 25, name: '命令' },
                { value: 25, name: '决定' },
                { value: 25, name: '决议' },
            ];
            option3.series[1].data = [
                { value: 15, name: '不予公开' },
                { value: 20, name: '申请公开' },
                { value: 25, name: '主动公开' },
            ];
            option3.series[2].data = [
                { value: 5, name: '其他' },
                { value: 10, name: '一般' },
                { value: 15, name: '紧急' },
                { value: 20, name: '特急' },
                { value: 25, name: '特提' },
            ];
            myChart3.setOption(option3);
            //左下角盒子
            dataAxis1 = ['办公室', '政策法规处', '思想政治工作处', '教育督导处',
                '维稳工作处', '基础教育处', '高等教育处', '职业教育处', '外资办', '信息中心',
                '支教办', '纪检组', '教师工作处','学生工作处'];
            data1 = [2514, 1420, 850, 568, 365, 241, 214, 195, 188, 152, 122, 101, 99,47];
            data1_1 = [68750, 2540, 2411, 1955, 1869, 1754, 1623, 1422, 1324, 1200, 1101, 1055
                , 330,585];
            option1.xAxis.data = dataAxis1;
            option1.series[0].data = data1;
            option1.series[1].data = data1_1;
            $("#dispatch-one").find("span").eq(0).click();
            $("#dispatch-two").find("span").eq(0).click();
        }

        function refreshDataThree() {
            $(".gongwentitle").html('各教育局公文量');
            $(".shoufa-rank").html('');
            // 总数量
            $("#sendNum").html('1,241');
            $("#receiveNum").html('8,142');
            $("#signNum").html('2,667');
            $("#fileNum").html('16,412');
            $("#activePeople").html('14,214');
            // 公务量
            $("#dispatch-one").find("span").eq(0).click();
            //收发文趋势
            option2.series[0].data = [302, 358, 365, 310, 300];
            option2.series[1].data = [152, 130, 118, 121, 122];
            myChart2.setOption(option2);
            //收发文排名
            $("#dispatch-two").find("span").eq(0).click();
            //发文属性分布
            option3.series[0].data = [
                { value: 5, name: '其他' },
                { value: 10, name: '公告' },
                { value: 15, name: '公报' },
                { value: 20, name: '通报' },
                { value: 25, name: '通知' },
                { value: 25, name: '命令' },
                { value: 25, name: '决定' },
                { value: 25, name: '决议' },
            ];
            option3.series[1].data = [
                { value: 15, name: '不予公开' },
                { value: 20, name: '申请公开' },
                { value: 25, name: '主动公开' },
            ];
            option3.series[2].data = [
                { value: 5, name: '其他' },
                { value: 10, name: '一般' },
                { value: 15, name: '紧急' },
                { value: 20, name: '特急' },
                { value: 25, name: '特提' },
            ];
            myChart3.setOption(option3);
            //左下角盒子
            dataAxis1 = ['教育局一', '教育局二', '教育局三', '教育局四',
                '教育局五', '教育局六', '教育局七', '教育局八', '教育局九', '教育局十',];
            data1 = [2514, 1420, 850, 568, 365, 241, 214, 195, 188, 152];
            data1_1 = [1869, 1754, 1623, 1422, 1324, 1200, 1101, 105, 330,585];
            option1.xAxis.data = dataAxis1;
            option1.series[0].data = data1;
            option1.series[1].data = data1_1;
            $("#dispatch-one").find("span").eq(0).click();
            $("#dispatch-two").find("span").eq(0).click();
        }

        //收发文排名
        $("#dispatch-two").find("span").eq(0).click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var tabType = $("#map-one").find(".active").attr("data-id");
            if (tabType ==1){
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>新疆维吾尔自治区教育厅</div>\n' +
                    '                                <div>4216</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>巴音郭楞蒙古自治州教育局</div>\n' +
                    '                                <div>1241</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>和田地区教育局</div>\n' +
                    '                                <div>1035</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>乌鲁木齐市教育局</div>\n' +
                    '                                <div>920</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>克孜勒苏柯尔克孜自治州教育局</div>\n' +
                    '                                <div>920</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>昌吉回族自治州教育局</div>\n' +
                    '                                <div>799</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>阿克苏地区教育局</div>\n' +
                    '                                <div>787</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>阿勒泰地区教育局发文</div>\n' +
                    '                                <div>582</div>\n' +
                    '                            </div>')
            } else if (tabType ==2) {
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>新疆大学</div>\n' +
                    '                                <div>354</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>新疆农业大学</div>\n' +
                    '                                <div>275</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>新疆师范大学</div>\n' +
                    '                                <div>244</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>新疆财经学院</div>\n' +
                    '                                <div>201</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>新疆医科大学</div>\n' +
                    '                                <div>183</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>新疆警官高等专科学校</div>\n' +
                    '                                <div>174</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>新疆工业高等专科学校</div>\n' +
                    '                                <div>142</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>新疆艺术学院</div>\n' +
                    '                                <div>52</div>\n' +
                    '                            </div>')
            }else if (tabType == 3){
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>农四师一中</div>\n' +
                    '                                <div>39</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>农五师高级中学</div>\n' +
                    '                                <div>20</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>新疆轮台县第一中学</div>\n' +
                    '                                <div>13</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>新疆兵团农二师八一中学 </div>\n' +
                    '                                <div>10</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>和静高级中学</div>\n' +
                    '                                <div>3</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>新疆兵团农二师华山中学</div>\n' +
                    '                                <div>0</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>巴州第二中学</div>\n' +
                    '                                <div>0</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>库尔勒市第四中学</div>\n' +
                    '                                <div>0</div>\n' +
                    '                            </div>')
            }
            $("#dispatch-rank").scrollTop(0);
        })

        $("#dispatch-two").find("span").eq(1).click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var tabType = $("#map-one").find(".active").attr("data-id");
            if (tabType ==1){
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>新疆维吾尔自治区教育厅</div>\n' +
                    '                                <div>75140</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>巴音郭楞蒙古自治州教育局</div>\n' +
                    '                                <div>8142</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>和田地区教育局</div>\n' +
                    '                                <div>4575</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>乌鲁木齐市教育局</div>\n' +
                    '                                <div>4598</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>克孜勒苏柯尔克孜自治州教育局</div>\n' +
                    '                                <div>4598</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>昌吉回族自治州教育局</div>\n' +
                    '                                <div>3562</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>阿克苏地区教育局</div>\n' +
                    '                                <div>2585</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>阿勒泰地区教育局发文</div>\n' +
                    '                                <div>2545</div>\n' +
                    '                            </div>')
            } else if (tabType ==2) {
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>新疆大学</div>\n' +
                    '                                <div>1842</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>新疆农业大学</div>\n' +
                    '                                <div>985</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>新疆师范大学</div>\n' +
                    '                                <div>987</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>新疆财经学院</div>\n' +
                    '                                <div>958</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>新疆医科大学</div>\n' +
                    '                                <div>1025</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>新疆警官高等专科学校</div>\n' +
                    '                                <div>922</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>新疆工业高等专科学校</div>\n' +
                    '                                <div>924</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>新疆艺术学院</div>\n' +
                    '                                <div>254</div>\n' +
                    '                            </div>')
            }else if (tabType == 3){
                //排名
                $("#dispatch-rank").empty().append('<div class="rank-li">\n' +
                    '                                <div>1</div>\n' +
                    '                                <div>农四师一中</div>\n' +
                    '                                <div>258</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>2</div>\n' +
                    '                                <div>农五师高级中学</div>\n' +
                    '                                <div>221</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>3</div>\n' +
                    '                                <div>新疆轮台县第一中学</div>\n' +
                    '                                <div>185</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>4</div>\n' +
                    '                                <div>新疆兵团农二师八一中学 </div>\n' +
                    '                                <div>183</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>5</div>\n' +
                    '                                <div>和静高级中学</div>\n' +
                    '                                <div>185</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>6</div>\n' +
                    '                                <div>新疆兵团农二师华山中学</div>\n' +
                    '                                <div>182</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>7</div>\n' +
                    '                                <div>巴州第二中学</div>\n' +
                    '                                <div>174</div>\n' +
                    '                            </div><div class="rank-li">\n' +
                    '                                <div>8</div>\n' +
                    '                                <div>库尔勒市第四中学</div>\n' +
                    '                                <div>166</div>\n' +
                    '                            </div>')
            }
            $("#dispatch-rank").scrollTop(0);
        })

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
                    if (preTop >= o.scrollHeight || preTop == o.scrollTop) {
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