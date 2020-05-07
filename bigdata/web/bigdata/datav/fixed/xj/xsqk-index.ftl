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
                        <button class="btn dropdown-toggle">跨省 <i class="wpfont icon-caret-down"></i></button>
                        <ul class="dropdown-menu">
                            <li class="active"><a href="#">跨省</a></li>
                            <li><a href="#">省内小计</a></li>
                            <li><a href="#">省内跨地市</a></li>
                            <li><a href="#">地市内跨县</a></li>
                            <li><a href="#">区县内</a></li>
                        </ul>
                    </div>
                </div>
                <div class="box-screen-one" id="echart-one">

                </div>

                <div class="student-title">
                    <span>本学年各学校的转入、转出数量排行榜</span>
                </div>

                <div class="box-screen-two">
                    <div class="pa-10 text-center">
                        <div class="btn-group box-data-scope" id="intoOrOutDiv">
                            <button class="btn active">转入</button>
                            <button class="btn ">转出</button>
                        </div>
                    </div>
                    <div class="box-screen-table slideOne" id="intoOrOutRank">

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
                            <span id="studentNum">0</span>
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
                            <span id="classNum">0</span>
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
                            <span id="stayChildrenNum">0</span>
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
                            <span id="specialStudentNum">0</span>
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
                            <span id="migrantChildrenNum">0</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="half-box-body">
                <div class="box-screen-three center-coming">
                    <div class="box-screen-three-title">
                        <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/icon-circle.png" align="bottom">
                        <span>当前位置：<span style="cursor: pointer" id="province">新疆</span> </span>
                        <span class="js-location"></span>
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
                        <div class="btn-group box-data-scope" id="xszsNumDiv">
                            <button class="btn active">小学</button>
                            <button class="btn ">初中</button>
                            <button class="btn ">高中</button>
                        </div>
                    </div>

                    <div class="part-chart" id="echart-four"></div>
                </div>
            </div>
            <div class="part-wrap">
                <div class="part">
                    <div class="student-title">
                        <span>义务教育学生情况分析</span>
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
                        <div class="btn-group box-data-scope" id="kcbxDiv">
                            <button class="btn active">小学</button>
                            <button class="btn ">初中</button>
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
                    学生人数：<span class="js-student-num">0</span>&nbsp;&nbsp;
                    班级总数：<span class="js-class-num">0</span>
                </div>
                <div class="half-content">
                    <div class="half-chart wrap-full"  id="echart-seven">

                    </div>
                </div>
            </div>
            <div class="half">
                <div class="half-title">
                    <span>各学校平均班额排行榜</span>
                </div>
                <div class="half-content box-screen-table slideTwo" id="stuClassNumRank">

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
        var showRegionCode = "";
        var showTransferType = "";
        var showindex = 0;

        var yearStr = "${yearStr!}".split(",");
        var showYearStr = yearStr;
        showYearStr.push("历年");
        var yearMStr = "${yearMStr!}".split(",");
        var xnStr = "${xnStr!}".split(",");
        var showXnStr = new Array(6);
        $.each(xnStr, function (index, value) {
            var years = value.split("-");
            showXnStr[index] = years[0].substring(2) + "-" + years[1].substring(2);
        });

        // 时间
        var nowTime = ${nowTime};
        getDate();
        getTime();
        setInterval(getTime, 1000);
        function getTime(){
            var date = new Date(nowTime);
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            $('.time-box').text(hours + ":" + minutes + ":" + seconds);
            if (hours === '00' && minutes === '00' && seconds === '00'){
                getDate();
            }
            nowTime += 1000;
        }
        // 日期
        function getDate(){
            var date = new Date(nowTime);
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

        //跑马灯
        var timer;
        var i = 0;
        function slideOne(){
            var h1 = $('.slideOne').height();
            var h2 = $('.slideOne').find('table').height();
            if (h1 - h2 < 0){
                var num = Math.floor((h2 - h1) / 36);
                timer = setInterval(function(){
                    i ++;
                    if (i <= num){
                        $('.slideOne').find('table').css({
                            transform: 'translateY(-' + 36*i + 'px)'
                        });
                    } else {
                        $('.slideOne').find('table').css({
                            transform: 'translateY(-' + (h2 - h1) + 'px)'
                        });
                        i = -1
                    }
                },1000)
            }
        }

        $(".slideOne").hover(function(){
            if ($('.slideOne table').length > 0) {
                clearInterval(timer);
            }
        },function(){
            if ($('.slideOne table').length > 0) {
                slideOne();
            }
        });

        var timer2;
        var k = 0;
        function slideTwo(){
            var h1 = $('.slideTwo').height();
            var h2 = $('.slideTwo table').height();
            if (h1 - h2 < 0){
                var num = Math.floor((h2 - h1) / 36);
                timer2 = setInterval(function(){
                    k ++;
                    if (k <= num){
                        $('.slideTwo table').css({
                            transform: 'translateY(-' + 36*k + 'px)'
                        });
                    } else {
                        $('.slideTwo table').css({
                            transform: 'translateY(-' + (h2 - h1) + 'px)'
                        });
                        k = -1
                    }
                },1000)
            }
        }

        $(".slideTwo").hover(function(){
            if ($('.slideTwo table').length > 0) {
                clearInterval(timer2);
            }
        },function(){
            if ($('.slideTwo table').length > 0) {
                slideTwo();
            }
        });


        //窗口变化,跑马灯，图表resize
        $(window).resize(function(){
            clearInterval(timer);
            slideOne();
            resizeChart();
        });

        //图表
        var arr = [];
        function resizeChart() {
            for (var i = 0; i < arr.length; i++) {
                arr[i].resize()
            }
        }

        // 地图
        // 初始化图表
        var mapChart = echarts.init(document.getElementById('map'));
        arr.push(mapChart);

        var uploadedDataURL = "${request.contextPath}/bigdata/v3/static/datav/custom/js/map/json/province/650000.json";
        var mapData = [
            {name: '巴音郭楞蒙古自治州', value: 6528 },
            { name: '乌鲁木齐市', value: 6501 },
            { name: '克拉玛依市', value: 6502 },
            { name: '吐鲁番市', value: 6521 },
            { name: '哈密市', value: 6522 },
            { name: '阿克苏地区', value: 6529 },
            { name: '喀什地区', value: 6531 },
            { name: '和田地区', value: 6532 },
            { name: '昌吉回族自治州', value: 6523 },
            { name: '博尔塔拉蒙古自治州', value: 6527 },
            { name: '克孜勒苏柯尔克孜自治州', value: 6530 },
            { name: '伊犁哈萨克自治州', value: 6540 },
            { name: '塔城地区', value: 6542 },
            { name: '阿勒泰地区', value: 6543 }];

        // 图表配置项
        var option = {
            tooltip: {							// 提示框
                trigger: 'none',
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
                    emphasis: {
                        itemStyle: {            // itemStyle高亮时的配置
                            color: '#63FFFF'   // 高亮时地图板块颜色改变
                        },
                        label: {                // 标签的相关设置
                            show: true,                 // (地图上的城市名称)是否显示标签 [ default: false ]
                            //distance: 6,               // 标签距离图形的距离，在三维的散点图中这个距离是屏幕空间的像素值，其它图中这个距离是相对的三维距离
                            //formatter:,               // 标签内容格式器
                            textStyle: {                // 标签的字体样式
                                color: '#fff',                  // 地图初始化区域字体颜色
                                fontSize: 14,                    // 字体大小
                                opacity: 1,                     // 字体透明度
                                backgroundColor: 'rgba(0,0,0,0.5)'      // 字体背景色
                            }
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
                        }
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
        // 引入JSON文件
        $.getJSON(uploadedDataURL, function (geoJson) {
            echarts.registerMap('xinjiang', geoJson);
            // 设置图表实例的配置项以及数据，万能接口，所有参数和数据的修改都可以通过setOption完成，ECharts 会合并新的参数和数据，然后刷新图表。
            mapChart.setOption(option);
        });


        // 处理地图点击事件,高亮
        mapChart.on('click', function (params) {
            $('.js-location').text("> " + params.name);
            showindex = params.dataIndex;
            for (var i = 0; i < mapData.length; i++) {
                delete mapData[i].itemStyle;
                delete mapData[i].label;
            }
            mapData[showindex].itemStyle = {            // itemStyle高亮时的配置
                color: '#63FFFF'   // 高亮时地图板块颜色改变
            };
            mapData[showindex].label = {
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
            option.series[0].data = mapData;
            mapChart.setOption(option);
            showRegionCode = params.data.value;
            loadingData();
        });

        $("#province").on("click",function () {
            $('.js-location').text("");
            showindex = -1;
            for (var i = 0; i < mapData.length; i++) {
                delete mapData[i].itemStyle;
                delete mapData[i].label;
            }
            option.series[0].data = mapData;
            mapChart.setOption(option);
            showRegionCode = "";
            loadingData();
        });

        //加载数据
        function loadingData() {
            //改变头部数据信息
            changeHeaderInformation();
            //按学年的转学异动情况
            $(".dropdown-menu li").eq(0).click();
            // //学生按年级分布柱状图
            changeStudentNum();
            // //新生人数和招生人数对比
            $("#xszsNumDiv").find("button").eq(0).click();
            // //义务教育学生情况分析
            changeStudentStatus();
            // //控辍保学
            $("#kcbxDiv").find("button").eq(0).click();
        }

        var xsbjList = ${xsbjList};
        var studentNumAll = 0;
        var classNumAll = 0;
        $.each(xsbjList, function (index,value) {
            studentNumAll += value.hz_xsrs + value.wwrz_xsrs + value.qt_xsrs;
            classNumAll += value.xx_bs + value.cz_bs + value.gz_bs;
        });

        var lsetList = ${lsetList};
        var stayChildrenNumAll = 0;
        $.each(tjxsList, function (index,value) {
            stayChildrenNumAll += value.hj_dqls + value.hj_sqls;
        });

        var tjxsList = ${tjxsList};
        var specialStudentNumAll = 0;
        $.each(tjxsList, function (index,value) {
            specialStudentNumAll += value.xss;
        });

        var sqznList = ${sqznList};
        var migrantChildrenNumAll = 0;
        $.each(sqznList, function (index,value) {
            migrantChildrenNumAll += value.hj_sqzn;
        });
        
        //改变头部数据信息
        function changeHeaderInformation() {
            if (showRegionCode === "") {
                $("#studentNum").text(showNumber(studentNumAll));
                $("#classNum").text(showNumber(classNumAll));
                $("#stayChildrenNum").text(showNumber(stayChildrenNumAll));
                $("#specialStudentNum").text(showNumber(specialStudentNumAll));
                $("#migrantChildrenNum").text(showNumber(migrantChildrenNumAll));
            } else {
                var studentNum = 0;
                var classNum = 0;
                var stayChildrenNum = 0;
                var specialStudentNum = 0;
                var migrantChildrenNum = 0;
                $.each(xsbjList, function (index,value) {
                    if (showRegionCode === value.region_code) {
                        studentNum += value.hz_xsrs + value.wwrz_xsrs + value.qt_xsrs;
                        classNum += value.xx_bs + value.cz_bs + value.gz_bs;
                    }
                });
                $.each(tjxsList, function (index,value) {
                    if (showRegionCode === value.xzdm) {
                        stayChildrenNum += value.hj_dqls + value.hj_sqls;
                    }
                });
                $.each(tjxsList, function (index,value) {
                    if (showRegionCode === value.xzdm) {
                        specialStudentNum += value.xss;
                    }
                });
                $.each(sqznList, function (index,value) {
                    if (showRegionCode === value.xzdm) {
                        migrantChildrenNum += value.hj_sqzn;
                    }
                });
                $("#studentNum").text(showNumber(studentNum));
                $("#classNum").text(showNumber(classNum));
                $("#stayChildrenNum").text(showNumber(stayChildrenNum));
                $("#specialStudentNum").text(showNumber(specialStudentNum));
                $("#migrantChildrenNum").text(showNumber(migrantChildrenNum));
            }
        }

        // 按学年的转学异动情况
        var chartOne = echarts.init(document.getElementById('echart-one'));
        arr.push(chartOne);
        var oneOption = {
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
                        color: '#fff'
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
                    data : showXnStr
                }
            ],
            series : [
                {
                    name:'转入',
                    type:'bar',
                    barWidth: '20%',
                    data:[]
                },
                {
                    name:'转出',
                    type:'bar',
                    barWidth: '20%',
                    data:[]
                }
            ]
        };

        var zxydList = ${zxydList};
        var intoTab1 = [0,0,0,0,0,0];
        var outTab1 = [0,0,0,0,0,0];
        var intoTab2 = [0,0,0,0,0,0];
        var outTab2 = [0,0,0,0,0,0];
        var intoTab3 = [0,0,0,0,0,0];
        var outTab3 = [0,0,0,0,0,0];
        var intoTab4 = [0,0,0,0,0,0];
        var outTab4 = [0,0,0,0,0,0];
        var intoTab5 = [0,0,0,0,0,0];
        var outTab5 = [0,0,0,0,0,0];

        $.each(zxydList, function (x, value) {
            $.each(xnStr, function (y, time) {
                if (time === value.xnxq) {
                    intoTab1[y] += value.zr_ks;
                    outTab1[y] += value.zc_ks;
                    intoTab2[y] += value.zr_sn_xj;
                    outTab2[y] += value.zc_sn_xj;
                    intoTab3[y] += value.zr_snkds;
                    outTab3[y] += value.zc_snkds;
                    intoTab4[y] += value.zr_dskqx;
                    outTab4[y] += value.zc_dskqx;
                    intoTab5[y] += value.zr_qxn;
                    outTab5[y] += value.zc_qxn;
                }
            });
        });

        $(".dropdown-menu li").on("click",function () {
            var transferType = $(this).text();
            showTransferType = transferType;
            if (showRegionCode === "") {
                if (transferType === "跨省") {
                    oneOption.series[0].data = intoTab1;
                    oneOption.series[1].data = outTab1;
                } else if (transferType === "省内小计") {
                    oneOption.series[0].data = intoTab2;
                    oneOption.series[1].data = outTab2;
                } else if (transferType === "省内跨地市") {
                    oneOption.series[0].data = intoTab3;
                    oneOption.series[1].data = outTab3;
                } else if (transferType === "地市内跨县") {
                    oneOption.series[0].data = intoTab4;
                    oneOption.series[1].data = outTab4;
                } else {
                    oneOption.series[0].data = intoTab5;
                    oneOption.series[1].data = outTab5;
                }
            } else {
                var intoTab = [0,0,0,0,0,0];
                var outTab = [0,0,0,0,0,0];
                $.each(zxydList, function (x, value) {
                    if (showRegionCode === value.xzdm) {
                        $.each(xnStr, function (y, time) {
                            if (time === value.xnxq) {
                                if (transferType === "跨省") {
                                    intoTab[y] += value.zr_ks;
                                    outTab[y] += value.zc_ks;
                                } else if (transferType === "省内小计") {
                                    intoTab[y] += value.zr_sn_xj;
                                    outTab[y] += value.zc_sn_xj;
                                } else if (transferType === "省内跨地市") {
                                    intoTab[y] += value.zr_snkds;
                                    outTab[y] += value.zc_snkds;
                                } else if (transferType === "地市内跨县") {
                                    intoTab[y] += value.zr_dskqx;
                                    outTab[y] += value.zc_dskqx;
                                } else {
                                    intoTab[y] += value.zr_qxn;
                                    outTab[y] += value.zc_qxn;
                                }
                            }
                        });
                    }
                });
                oneOption.series[0].data = intoTab;
                oneOption.series[1].data = outTab;
            }
            chartOne.setOption(oneOption);
            $("#intoOrOutDiv").find("button").eq(0).click();
        });

        var intoOrOutMap = {};

        $("#intoOrOutDiv button").on("click", function () {
            i = 0;
            clearInterval(timer);
            $("#intoOrOutRank").html("");
            var type = $(this).text();
            if (intoOrOutMap[type+showTransferType+showRegionCode] != null) {
                $("#intoOrOutRank").html(intoOrOutMap[type+showTransferType+showRegionCode]);
                slideOne();
            } else {
                $.ajax({
                    url: '${request.contextPath}/bigdata/datav/fixed/screen/intoOrOutRank',
                    data: {"type":type,"showRegionCode":showRegionCode,"showTransferType":showTransferType},
                    type: 'post',
                    dataType: 'json',
                    success: function (response) {
                        if (response.success) {
                            var data = JSON.parse(response.data);
                            if (data.length !== 0) {
                                var str = '<table class="tables wrap-full"><tbody>';
                                var name = '';
                                $.each(data, function (index, value) {
                                    if (value.name.length > 8) {
                                        name = value.name.substring(0,8) + '...';
                                    } else {
                                        name = value.name;
                                    }
                                    str += '<tr><td><em>'+ (index + 1) +'</em></td><td>'+ name +
                                        '</td><td>'+ value.num +'</td></tr>';
                                });
                                str += '</tbody></table>';
                                $("#intoOrOutRank").html(str);
                                intoOrOutMap[type+showTransferType+showRegionCode] = str;
                                slideOne();
                            } else {
                                $("#intoOrOutRank").html('<div class="no-data-wrap"><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/no-data.png" ><p>暂无数据</p></div>');
                            }
                        }
                    }
                });
            }
        });

        // 学生按年级分布柱状图
        var chartThree = echarts.init(document.getElementById('echart-three'));
        arr.push(chartThree);
        var threeOption = {
            tooltip : {
                show: true
            },
            xAxis: {
                data: ['小一', '小二', '小三', '小四', '小五', '小六', '初一', '初二', '初三', '初四', '高一', '高二', '高三', '年级'],
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
                    color: '#fff'
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
                bottom:25
            },
            series: [
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
                    data: []
                }
            ]
        };

        var xsnjfbList = ${xsnjfbList};
        var stuNumDataAll = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        $.each(xsnjfbList, function (index, value) {
            stuNumDataAll[0] += value.xx_ynj;
            stuNumDataAll[1] += value.xx_enj;
            stuNumDataAll[2] += value.xx_snj;
            stuNumDataAll[3] += value.xx_sinj;
            stuNumDataAll[4] += value.xx_wnj;
            stuNumDataAll[5] += value.xx_lnj;
            stuNumDataAll[6] += value.cz_ynj;
            stuNumDataAll[7] += value.cz_enj;
            stuNumDataAll[8] += value.cz_snj;
            stuNumDataAll[9] += value.cz_sinj;
            stuNumDataAll[10] += value.gz_ynj;
            stuNumDataAll[11] += value.gz_enj;
            stuNumDataAll[12] += value.gz_snj;
        });

        function changeStudentNum() {
            if (showRegionCode === "") {
                threeOption.series[0].data = stuNumDataAll;
            } else {
                var stuNumData = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                $.each(xsnjfbList, function (index, value) {
                    if (showRegionCode === value.xzdm) {
                        stuNumData[0] += value.xx_ynj;
                        stuNumData[1] += value.xx_enj;
                        stuNumData[2] += value.xx_snj;
                        stuNumData[3] += value.xx_sinj;
                        stuNumData[4] += value.xx_wnj;
                        stuNumData[5] += value.xx_lnj;
                        stuNumData[6] += value.cz_ynj;
                        stuNumData[7] += value.cz_enj;
                        stuNumData[8] += value.cz_snj;
                        stuNumData[9] += value.cz_sinj;
                        stuNumData[10] += value.gz_ynj;
                        stuNumData[11] += value.gz_enj;
                        stuNumData[12] += value.gz_snj;
                    }
                });
                threeOption.series[0].data = stuNumData;
            }
            chartThree.setOption(threeOption);
        }

        var ywjybjslList = ${ywjybjslList};
        var gzbjslList = ${gzbjslList};
        var classNumTab1 = 0, classNumTab2 = 0, classNumTab3 = 0, classNumTab4 = 0, classNumTab5 = 0, classNumTab6 = 0,
            classNumTab7 = 0, classNumTab8 = 0, classNumTab9 = 0, classNumTab10 = 0, classNumTab11 = 0,
            classNumTab12 = 0, classNumTab13 = 0;

        $.each(ywjybjslList, function (index, value) {
            classNumTab1 += value.bjs_11; classNumTab2 += value.bjs_12; classNumTab3 += value.bjs_13;
            classNumTab4 += value.bjs_14; classNumTab5 += value.bjs_15; classNumTab6 += value.bjs_16;
            classNumTab7 += value.bjs_21; classNumTab8 += value.bjs_22; classNumTab9 += value.bjs_23;
            classNumTab10 += value.bjs_24;
        });

        $.each(gzbjslList, function (index, value) {
            classNumTab11 += value.bjs_1;
            classNumTab12 += value.bjs_2;
            classNumTab13 += value.bjs_3;
        });

        var stuClassNumRankMap = {};

        chartThree.on('click', function (params) {
            $('.layer').show();
            $('.js-class-name').text(params.name);
            $('.js-student-num').text(showNumber(params.value));
            var classNum = getClassNum(params.name);
            $('.js-class-num').text(showNumber(classNum));
            if (params.value === 0 || classNum === 0) {
                layerChart(0);
            } else {
                layerChart(Math.round(params.value/classNum));
            }
            k = 0;
            clearInterval(timer2);
            $("#stuClassNumRank").html("");
            if (stuClassNumRankMap[showRegionCode+params.name] != null) {
                $("#stuClassNumRank").html(stuClassNumRankMap[showRegionCode+params.name]);
                slideTwo();
            } else {
                $.ajax({
                    url: '${request.contextPath}/bigdata/datav/fixed/screen/stuClassNumRank',
                    data: {"type":params.name,"showRegionCode":showRegionCode},
                    type: 'post',
                    dataType: 'json',
                    success: function (response) {
                        if (response.success) {
                            var data = JSON.parse(response.data);
                            if (data.length !== 0) {
                                var str = '<table class="tables wrap-full"><tbody>';
                                var name = '';
                                $.each(data, function (index, value) {
                                    if (value.name.length > 8) {
                                        name = value.name.substring(0,8) + '...';
                                    } else {
                                        name = value.name;
                                    }
                                    str += '<tr><td><em>'+ (index + 1) +'</em></td><td>'+ name +'</td>' +
                                        '<td><span class="';
                                    if (value.num >= 50) {
                                        str += 'c-purple';
                                    } else if (value.num < 35) {
                                        str += 'c-green';
                                    }
                                    str += '">'+ value.num +'</span></td></tr>';
                                });
                                str += '</tbody></table>';
                                $("#stuClassNumRank").html(str);
                                stuClassNumRankMap[showRegionCode+params.name] = str;
                                slideTwo();
                            } else {
                                $("#stuClassNumRank").html('<div class="no-data-wrap"><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/xinjiang/no-data.png" ><p>暂无数据</p></div>');
                            }
                        }
                    }
                });
            }
        });

        //关闭弹窗
        $('body').on('click','.js-close',function(){
            $(this).closest('.layer').hide();
            k = 0;
            clearInterval(timer2);
        });

        function layerChart(num){
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
                                color: '#fff'
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
                            length :5
                        },
                        splitLine: {           // 分隔线
                            length :10,
                            lineStyle: {
                                width:1,
                                color: '#fff'
                            }
                        },
                        data: [
                            {
                                value: num,
                                name: '平均班额',
                                color: '#fff'
                            }
                        ]
                    }
                ]
            });
        }

        var classNumMap = {};

        function getClassNum(gradeName) {
            var classNum = 0;
            if (gradeName === "小一") {
                if (showRegionCode === "") {
                    classNum = classNumTab1;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_11;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "小二") {
                if (showRegionCode === "") {
                    classNum = classNumTab2;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_12;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "小三") {
                if (showRegionCode === "") {
                    classNum = classNumTab3;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_13;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "小四") {
                if (showRegionCode === "") {
                    classNum = classNumTab4;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_14;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "小五") {
                if (showRegionCode === "") {
                    classNum = classNumTab5;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_15;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "小六") {
                if (showRegionCode === "") {
                    classNum = classNumTab6;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_16;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "初一") {
                if (showRegionCode === "") {
                    classNum = classNumTab7;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_21;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "初二") {
                if (showRegionCode === "") {
                    classNum = classNumTab8;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_22;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "初三") {
                if (showRegionCode === "") {
                    classNum = classNumTab9;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_23;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "初四") {
                if (showRegionCode === "") {
                    classNum = classNumTab10;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(ywjybjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_24;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "高一") {
                if (showRegionCode === "") {
                    classNum = classNumTab11;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(gzbjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_1;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else if (gradeName === "高二") {
                if (showRegionCode === "") {
                    classNum = classNumTab12;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(gzbjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_2;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            } else {
                if (showRegionCode === "") {
                    classNum = classNumTab13;
                } else {
                    if (classNumMap[showRegionCode+gradeName] != null) {
                        classNum = classNumMap[showRegionCode+gradeName];
                    } else {
                        $.each(gzbjslList, function (index, value) {
                            if (showRegionCode === value.xzdm) {
                                classNum += value.bjs_3;
                            }
                        });
                        classNumMap[showRegionCode+gradeName] = classNum;
                    }
                }
            }
            return classNum;
        }

        // 新生人数与招生人数对比
        var echartFour = echarts.init($('#echart-four')[0]);
        arr.push(echartFour);
        var fourOption = {
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
                        color: '#fff'
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
                    data : showYearStr
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name : '人',
                    nameTextStyle: {
                        color: '#fff'
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
                    data:[]
                },
                {
                    name:'招生',
                    type:'line',
                    areaStyle: {
                        opacity: 0.3
                    },
                    data:[]
                }
            ]
        };

        var lnxsList = ${lnxsList};
        var lnzsList = ${lnzsList};
        var xsNumTabAll1 = [0,0,0,0,0];
        var zsNumTabAll1 = [0,0,0,0,0];
        var xsNumTabAll2 = [0,0,0,0,0];
        var zsNumTabAll2 = [0,0,0,0,0];
        var xsNumTabAll3 = [0,0,0,0,0];
        var zsNumTabAll3 = [0,0,0,0,0];

        $.each(lnxsList, function (x, value) {
            $.each(yearMStr, function (y, time) {
                if (time === value.tjsj) {
                    xsNumTabAll1[y] += value.xx_ynj;
                    xsNumTabAll2[y] += value.cz_ynj;
                    xsNumTabAll3[y] += value.gz_ynj;
                }
            });
        });

        $.each(lnzsList, function (x, value) {
            $.each(yearStr, function (y, time) {
                if (time === value.tjsj) {
                    zsNumTabAll1[y] += value.xx_zsrs;
                    zsNumTabAll2[y] += value.cz_zsrs;
                    zsNumTabAll3[y] += value.gz_zsrs;
                }
            });
        });

        $("#xszsNumDiv button").on("click",function () {
            if (showRegionCode === "") {
                if ($(this).html() === "小学") {
                    fourOption.series[0].data = xsNumTabAll1;
                    fourOption.series[0].data = zsNumTabAll1;
                } else if ($(this).html() === "初中") {
                    fourOption.series[0].data = xsNumTabAll2;
                    fourOption.series[0].data = zsNumTabAll2;
                } else {
                    fourOption.series[0].data = xsNumTabAll3;
                    fourOption.series[0].data = zsNumTabAll3;
                }
            } else {
                var xsNum = [0,0,0,0,0];
                var zsNum = [0,0,0,0,0];
                $.each(lnxsList, function (x, value) {
                    if (showRegionCode === value.xzdm) {
                        $.each(yearMStr, function (y, time) {
                            if (time === value.tjsj) {
                                if ($(this).html() === "小学") {
                                    xsNum[y] += value.xx_ynj;
                                } else if ($(this).html() === "初中") {
                                    xsNum[y] += value.cz_ynj;
                                } else {
                                    xsNum[y] += value.gz_ynj;
                                }
                            }
                        });
                    }
                });
                $.each(lnzsList, function (x, value) {
                    if (showRegionCode === value.xzdm) {
                        $.each(yearStr, function (y, time) {
                            if (time === value.tjsj) {
                                if ($(this).html() === "小学") {
                                    zsNum[y] += value.xx_zsrs;
                                } else if ($(this).html() === "初中") {
                                    zsNum[y] += value.cz_zsrs;
                                } else {
                                    zsNum[y] += value.gz_zsrs;
                                }
                            }
                        });
                    }
                });
                fourOption.series[0].data = xsNum;
                fourOption.series[0].data = zsNum;
            }
            echartFour.setOption(fourOption);
        });


        // 义务教育学生情况分析
        var charts_ywjy_1 = echarts.init($('.echart')[0]);
        arr.push(charts_ywjy_1);
        var ywjyOption1 =  {
            color: ['#00ccdd','#905eff'],
            title: {
                text: '留守儿童',
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
                data: ['非留守','留守儿童'],
                itemWidth: 10,
                itemHeight: 6
            },
            tooltip: {
                trigger: 'item'
            },
            series: [
                {
                    name: '留守儿童',
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
                    data: []
                }
            ]
        };

        var charts_ywjy_2 = echarts.init($('.echart')[1]);
        arr.push(charts_ywjy_2);
        var ywjyOption2 =  {
            color: ['#00ccdd','#905eff'],
            title: {
                text: '独生子女',
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
                data: ['是','否'],
                itemWidth: 10,
                itemHeight: 6
            },
            tooltip: {
                trigger: 'item'
            },
            series: [
                {
                    name: '独生子女',
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
                    data: []
                }
            ]
        };

        var charts_ywjy_3 = echarts.init($('.echart')[2]);
        arr.push(charts_ywjy_3);
        var ywjyOption3 =  {
            color: ['#00ccdd','#905eff'],
            title: {
                text: '性别',
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
                data: ['男','女'],
                itemWidth: 10,
                itemHeight: 6
            },
            tooltip: {
                trigger: 'item'
            },
            series: [
                {
                    name: '性别',
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
                    data: []
                }
            ]
        };

        var charts_ywjy_4 = echarts.init($('.echart')[3]);
        arr.push(charts_ywjy_4);
        var ywjyOption4 =  {
            color: ['#00ccdd','#905eff'],
            title: {
                text: '民族',
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
                data: ['汉族','民族'],
                itemWidth: 10,
                itemHeight: 6
            },
            tooltip: {
                trigger: 'item'
            },
            series: [
                {
                    name: '民族',
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
                    data: []
                }
            ]
        };

        var ywjymzList = ${ywjymzList};
        var ywjydsList = ${ywjydsList};
        var ywjyDataAll1 = [{ name: '非留守', value: 0 }, { name: '留守儿童', value: 0 }];
        var ywjyDataAll2 = [{ name: '是', value: 0 }, { name: '否', value: 0 }];
        var ywjyDataAll3 = [{ name: '男', value: 0 }, { name: '女', value: 0 }];
        var ywjyDataAll4 = [{ name: '汉族', value: 0 }, { name: '民族', value: 0 }];

        $.each(lsetList, function (index, value) {
            ywjyDataAll1[0].value += (value.xxhj - value.xxhj_dqls - value.xxhj_sqls) +
                (value.czhj - value.czhj_dqls - value.czhj_sqls);
            ywjyDataAll1[1].value += value.xxhj_dqls + value.xxhj_sqls + value.czhj_dqls + value.czhj_sqls;
        });

        $.each(ywjydsList, function (index, value) {
            ywjyDataAll2[0].value += value.xx_dszn + value.cz_dszn;
            ywjyDataAll2[1].value += (value.xx_xss - value.xx_dszn) + (value.cz_xss - value.cz_dszn);
        });

        $.each(ywjymzList, function (index, value) {
            ywjyDataAll3[0].value += value.ywjy - value.ywjy_qzn;
            ywjyDataAll3[1].value += value.ywjy_qzn;
            if (value.mzm === "01") {
                ywjyDataAll4[0].value += value.ywjy;
            } else {
                ywjyDataAll4[1].value += value.ywjy;
            }
        });

        function changeStudentStatus() {
            if (showRegionCode === "") {
                ywjyOption1.series[0].data = ywjyDataAll1;
                ywjyOption2.series[0].data = ywjyDataAll2;
                ywjyOption3.series[0].data = ywjyDataAll3;
                ywjyOption4.series[0].data = ywjyDataAll4;
            } else {
                var ywjyData1 = [{ name: '非留守', value: 0 }, { name: '留守儿童', value: 0 }];
                var ywjyData2 = [{ name: '是', value: 0 }, { name: '否', value: 0 }];
                var ywjyData3 = [{ name: '男', value: 0 }, { name: '女', value: 0 }];
                var ywjyData4 = [{ name: '汉族', value: 0 }, { name: '民族', value: 0 }];

                $.each(lsetList, function (index, value) {
                    if (showRegionCode === value.xzdm) {
                        ywjyData1[0].value = (value.xxhj - value.xxhj_dqls - value.xxhj_sqls) +
                            (value.czhj - value.czhj_dqls - value.czhj_sqls);
                        ywjyData1[1].value = value.xxhj_dqls + value.xxhj_sqls + value.czhj_dqls + value.czhj_sqls;
                    }
                });

                $.each(ywjydsList, function (index, value) {
                    if (showRegionCode === value.xzdm) {
                        ywjyData2[0].value += value.xx_dszn + value.cz_dszn;
                        ywjyData2[1].value += (value.xx_xss - value.xx_dszn) + (value.cz_xss - value.cz_dszn);
                    }
                });

                $.each(ywjymzList, function (index, value) {
                    if (showRegionCode === value.region_code) {
                        ywjyData3[0].value += value.ywjy - value.ywjy_qzn;
                        ywjyData3[1].value += value.ywjy_qzn;
                        if (value.mzm === "01") {
                            ywjyData4[0].value += value.ywjy;
                        } else {
                            ywjyData4[1].value += value.ywjy;
                        }
                    }
                });

                ywjyOption1.series[0].data = ywjyData1;
                ywjyOption2.series[0].data = ywjyData2;
                ywjyOption3.series[0].data = ywjyData3;
                ywjyOption4.series[0].data = ywjyData4;
            }
            charts_ywjy_1.setOption(ywjyOption1);
            charts_ywjy_2.setOption(ywjyOption2);
            charts_ywjy_3.setOption(ywjyOption3);
            charts_ywjy_4.setOption(ywjyOption4);
        }


        // 控辍保学政策情况趋势图
        var echartSix = echarts.init($('#echart-six')[0]);
        arr.push(echartSix);
        var kcbxOption = {
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
                        color: '#fff'
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
                    data : showYearStr
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name : '人',
                    nameTextStyle: {
                        color: '#fff'
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
                    data:[]
                },
                {
                    name:'疑似辍学',
                    type:'line',
                    data:[]
                },
                {
                    name:'返校',
                    type:'line',
                    data:[]
                }
            ]
        };
        var kcbxList = ${kcbxList};
        var xxgoBackSchoolAll1 = [0,0,0,0,0];
        var xxgoBackSchoolAll2 = [0,0,0,0,0];
        var xxgoBackSchoolAll3 = [0,0,0,0,0];
        var czgoBackSchoolAll1 = [0,0,0,0,0];
        var czgoBackSchoolAll2 = [0,0,0,0,0];
        var czgoBackSchoolAll3 = [0,0,0,0,0];
        $.each(kcbxList, function (x, value) {
            $.each(yearStr, function (y, time) {
                if (time === value.tjsj) {
                    xxgoBackSchoolAll1[y] += value.xxcx;
                    xxgoBackSchoolAll2[y] += value.xxyscx;
                    xxgoBackSchoolAll3[y] += value.xxfx;
                    czgoBackSchoolAll1[y] += value.czcx;
                    czgoBackSchoolAll2[y] += value.czyscx;
                    czgoBackSchoolAll3[y] += value.czfx;
                }
            });
        });

        $("#kcbxDiv button").on("click",function () {
            if (showRegionCode === "") {
                if ($(this).html() === "小学") {
                    kcbxOption.series[0].data = xxgoBackSchoolAll1;
                    kcbxOption.series[1].data = xxgoBackSchoolAll2;
                    kcbxOption.series[2].data = xxgoBackSchoolAll3;
                } else {
                    kcbxOption.series[0].data = czgoBackSchoolAll1;
                    kcbxOption.series[1].data = czgoBackSchoolAll2;
                    kcbxOption.series[2].data = czgoBackSchoolAll3;
                }
            } else {
                var goBackSchool1 = [0,0,0,0,0];
                var goBackSchool2 = [0,0,0,0,0];
                var goBackSchool3 = [0,0,0,0,0];
                $.each(kcbxList, function (x, value) {
                    if (showRegionCode === value.xzdm) {
                        $.each(yearStr, function (y, time) {
                            if (time === value.tjsj) {
                                if ($(this).html() === "小学") {
                                    goBackSchool1[y] += value.xxcx;
                                    goBackSchool2[y] += value.xxyscx;
                                    goBackSchool3[y] += value.xxfx;
                                } else {
                                    goBackSchool1[y] += value.czcx;
                                    goBackSchool2[y] += value.czyscx;
                                    goBackSchool3[y] += value.czfx;
                                }
                            }
                        });
                    }
                });
                kcbxOption.series[0].data = goBackSchool1;
                kcbxOption.series[1].data = goBackSchool2;
                kcbxOption.series[2].data = goBackSchool3;
            }
            echartSix.setOption(kcbxOption);
        });

        function showNumber(number) {
            var _number = String(number);
            var result = '';            // 转换后的字符串
            var counter = '';
            for (var i = _number.length - 1; i >= 0; i--) {
                counter++;
                result = _number.charAt(i) + result;
                if (!(counter % 3) && i != 0) { result = ',' + result; }
            }
            return result;
        }

        //数据初始化
        $("#province").click();
    });
</script>

</body>

</html>