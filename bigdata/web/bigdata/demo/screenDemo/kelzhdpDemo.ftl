<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>综合大屏</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>

<body class="subsume-box">
<!--主体 E-->
<div class="subsume-body">
    <div class="subsume-map" id="container">
    </div>
    <div class="subsume-search-box">
        <div class="btn-group subsume-select-box filter">
            <button class="btn btn-default dropdown-toggle subsume-dropdown-btn">
                初中
            </button>
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/unfold-icon.png" class="subsume-dropdown-img">
            <ul class="dropdown-menu subsume-dropdown-menu">
                <li><a href="#">初中</a></li>
                <li class="active"><a href="#">高中</a></li>
                <li><a href="#">小学</a></li>
            </ul>
        </div>
        <div class="subsume-search-input">
            <div class="subsume-search-sou">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/search-icon.png">
                <input type="text" placeholder="搜索" id="subsumesearch">
            </div>
            <ul class="subsume-search-menu">
                <li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>
                <li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>
                <li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>
            </ul>
        </div>
        <div class="subsume-shou active">
            <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/left-icon.png">
            <span>search</span>
        </div>
        <div>

        </div>
    </div>

    <div class="subsume-right">
        <div class="subsume-title">库尔勒综合大屏</div>
        <div class="subsume-right-item subsume-right-one">
            <div class="subsume-ritem-title">
                <div class="subsume-ritem-back">总体指标</div>
                <div class="subsume-ritem-line"></div>
                <div class="subsume-ritem-bian">
                    <div></div>
                    <div></div>
                </div>
            </div>
            <div class="subsume-ritem-body">
                <div class="subsume-ritem-norm">
                    <div class="subsume-norm-item">
                        <div class="subsume-norm-title">学生总数</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="87093"
                                                         data-speed="2000">87093</b></div>
                        <div class="subsume-norm-title">班级总数</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="2051"
                                                         data-speed="200">2051</b></div>
                    </div>
                    <div class="subsume-norm-line-item">
                        <div class="subsume-norm-line"></div>
                        <div class="subsume-norm-line"></div>
                    </div>
                    <div class="subsume-norm-item">
                        <div class="subsume-norm-title">教师总数</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="8868"
                                                         data-speed="200">8868</b></div>
                        <div class="subsume-norm-title">平均班级人数</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="41"
                                                         data-speed="5">41</b></div>
                    </div>
                    <div class="subsume-norm-line-item">
                        <div class="subsume-norm-line"></div>
                        <div class="subsume-norm-line"></div>
                    </div>
                    <div class="subsume-norm-item">
                        <div class="subsume-norm-title">设备总数</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="24216"
                                                         data-speed="2000">24216</b></div>
                        <div class="subsume-norm-title">教师人均面积</div>
                        <div class="subsume-norm-num"><b class="norm-num" data-from="0" data-to="64216"
                                                         data-speed="2000">14</b></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="subsume-right-item subsume-right-two">
            <div class="subsume-ritem-title">
                <div class="subsume-ritem-back">各科目教师人数</div>
                <div class="subsume-ritem-line"></div>
                <div class="subsume-ritem-bian">
                    <div></div>
                    <div></div>
                </div>
            </div>
            <div class="subsume-ritem-body subsume-person-box">
                <div class="subsume-person-chart">
                    <div class="subsume-person-title">各科教师数</div>
                    <div class="subsume-person-tu" id="person-chart"></div>
                </div>
                <div class="subsume-person-right">
                    <div class="subsume-person-title">各科师生比</div>
                    <div class="subsume-person-tubiao" id="subsume-person-tubiao">
                        <div class="subsume-person-li">
                            <span>语文</span>
                            <span>1:20</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>数学</span>
                            <span>1:16</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>英语</span>
                            <span>1:30</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>物理</span>
                            <span>1:34</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>化学</span>
                            <span>1:28</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>生物</span>
                            <span>1:22</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>历史</span>
                            <span>1:39</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>地理</span>
                            <span>1:29</span>
                        </div>
                        <div class="subsume-person-li">
                            <span>政治</span>
                            <span>1:26</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="subsume-right-item subsume-right-three">
            <div class="subsume-ritem-title">
                <div class="subsume-ritem-back">教师工龄结构</div>
                <div class="subsume-ritem-line"></div>
                <div class="subsume-ritem-bian">
                    <div></div>
                    <div></div>
                </div>
                <div class="subsume-btn-group">
                    <div class="subsume-btn-item active">工龄</div>
                    <div class="subsume-btn-item">年龄</div>
                </div>
            </div>
            <div class="subsume-ritem-body" id="seniority-chart">
            </div>
        </div>
    </div>


    <div class="subsume-bottom">
        <div class="subsume-bottom-body">
            <div class="subsume-right-item subsume-bottom-one">
                <div class="subsume-ritem-title">
                    <div class="subsume-ritem-back">师生性别/民汉比</div>
                    <div class="subsume-ritem-line"></div>
                    <div class="subsume-ritem-bian">
                        <div></div>
                        <div></div>
                    </div>
                    <div class="subsume-btn-group">
                        <div class="subsume-btn-item active">教师</div>
                        <div class="subsume-btn-item">学生</div>
                    </div>
                </div>

                <div class="subsume-boitem-left">
                    <div class="subsume-teacher-chart">
                        <div class="subsume-person-title">各科教师数</div>
                        <div class="subsume-teacher-tu">
                            <div class="subsume-teacher-tuli">
                                <div class="subsume-man">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                                <div class="subsume-woman">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                            </div>
                            <div class="subsume-teacher-tuli">
                                <div class="subsume-man">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                                <div class="subsume-woman">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                            </div>
                            <div class="subsume-teacher-tuli">
                                <div class="subsume-man">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                                <div class="subsume-woman">
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                    <div class="subsume-man-li"></div>
                                </div>
                            </div>
                            <div class="subsume-teacher-tuli">
                                <span>44%</span>
                                <span>56%</span>
                            </div>
                        </div>
                    </div>

                    <div class="subsume-teacher-chart">
                        <div class="subsume-person-title">民汉比例</div>
                        <div class="subsume-teacher-tu" id="ratio-chart"></div>
                    </div>
                </div>
            </div>

            <div class="subsume-right-item subsume-bottom-two">
                <div class="subsume-ritem-title">
                    <div class="subsume-ritem-back">设备总览</div>
                    <div class="subsume-ritem-line"></div>
                    <div class="subsume-ritem-bian">
                        <div></div>
                        <div></div>
                    </div>
                </div>
                <div class="subsume-equ">
                    <div class="subsume-equ-body">
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">房屋和建筑类</div>
                            <div class="subsume-equ-li2">2356</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">计算机设备</div>
                            <div class="subsume-equ-li2">12352</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">耗材</div>
                            <div class="subsume-equ-li2">29877</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">打印机</div>
                            <div class="subsume-equ-li2">1698</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">课桌</div>
                            <div class="subsume-equ-li2">87289</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">讲台</div>
                            <div class="subsume-equ-li2">1897</div>
                        </div>
                        <div class="subsume-equ-li">
                            <div class="subsume-equ-li1">实验室</div>
                            <div class="subsume-equ-li2">256</div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="https://webapi.amap.com/maps?v=1.4.15&key=da1bc644fff023714ecda46aca2448a4"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
<script>
    $(function () {
        //进场动画
        setTimeout(function () {
            $(".subsume-right").css("right", "0");
            $(".subsume-bottom").css("left", "0");
        }, 500)

        //下拉菜单
        $("body").on('click', '.dropdown-toggle', function (e) {
            e.preventDefault();
            $('.dropdown-toggle').each(function () {
                var $g = $(this).parent('.btn-group');
                if ($g.hasClass('open')) {
                    $g.removeClass('open');
                }
            });
            $(this).parent('.btn-group').toggleClass('open');
        });
        $("body").on('click', '.dropdown-menu li:not(.disabled)', function (e) {
            var $btnGroup = $(this).parents('.btn-group');
            $(this).addClass('active').siblings('li').removeClass('active');
            $btnGroup.removeClass('open');
            if ($btnGroup.hasClass('filter')) {
                e.preventDefault();
                var txt = $(this).text();
                $btnGroup.children('.dropdown-toggle').text(txt);
            }
        });
        $(document).click(function (event) {
            var eo = $(event.target);
            if ($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
                $('.btn-group').removeClass('open');
        });

        //搜索框
        $(document).click(function (event) {
            var eo = $(event.target);
            if (eo.attr('class') != 'subsume-search-menu' && !eo.parents('.subsume-search-input').length)
                $('.subsume-search-menu').hide();
        });

        $("#subsumesearch").bind("input propertychange", function () {
            $('.subsume-search-menu').show();
        });



        //收起，打开
        $(".subsume-shou").click(function () {
            $(".subsume-shou").toggleClass("active");
            $(".subsume-search-box").toggleClass("active");
        })

        //按钮组
        $(".subsume-btn-item").click(function () {
            $(this).addClass("active").siblings().removeClass("active");
        })

        gunfawen();
    })
    var map;
    var markers = [];
    function mapInit() {
        $("body").on('click', '.subsume-search-menu li', function (e) {
            $(".subsume-search-menu").hide();
            qiehuan();
        });


        map = new AMap.Map('container', {
            resizeEnable: true,
            rotateEnable: true,
            pitchEnable: false,
            zoom: 16,
            pitch: 30,
            rotation: -30,
            viewMode: '3D',//开启3D视图,默认为关闭
            buildingAnimation: true,//楼块出现是否带动画

            expandZoomRange: true,//	是否支持可以扩展最大缩放级别,和zooms属性配合使用
            zooms: [3, 20],
            //skyColor: '#333333',
            //setMapStyle: 'amap://styles/78cc394aba7d584a9c40c35d08ed63bd'
            center: [86.152044, 41.748162]
        });


        markers = [];
        //获取当前中心点


        function biaodian() {
            var center = map.getCenter();
            var lat = center.lat;
            var lng = center.lng;
            var marker = new AMap.Marker({
                map: map,
                position: new AMap.LngLat(lng, lat),
                anchor: 'bottom-left',
                icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
                offset: new AMap.Pixel(-300, -120),
               // offset: new AMap.Pixel(-13, -30),
                content: `<div class="subsume-dizhi-tip">
              <div class="subsume-dizhi-title">库尔勒市教育局</div>
            <div class="subsume-dizhi-body">
              <div class="subsume-zhu"></div>
              <div class="subsume-quan1">
                <div class="subsume-quan2">
                  <div class="subsume-quan3">

                  </div>
                </div>
              </div>
            </div>
          </div>`
            });

            markers.push(marker);

            setInterval(function () {
                $(".subsume-dizhi-tip").css("transition", "bottom 0.5s")
                $(".subsume-dizhi-tip").css("bottom", "0");
                setTimeout(function () {
                    $(".subsume-dizhi-tip").css("bottom", "50px");
                }, 500)
            }, 1000)
        }


        //定位
        // AMap.plugin('AMap.Geolocation', function () {
        //   var geolocation = new AMap.Geolocation({
        //     enableHighAccuracy: true,//是否使用高精度定位，默认:true
        //     timeout: 10000,          //超过10秒后停止定位，默认：5s
        //     showButton: false
        //     //zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

        //   });
        //   map.addControl(geolocation);
        //   geolocation.getCurrentPosition(function (status, result) {
        //     if (status == 'complete') {
        //       onComplete(result)
        //       setTimeout(function () {
        //         map.setZoom(18);
        //       }, 2000)
        //     } else {
        //       onError(result)
        //     }
        //   });

        // });
        map.setMapStyle('amap://styles/97c27b303dfc26554b76050bc191d3d8');
        setTimeout(function () {
            //map.setZoom(18);
            biaodian();
        }, 1500)

        //解析定位结果
        function onComplete(data) {
            console.log(data)
        }
        //解析定位错误信息
        function onError(data) {
            console.log(data)
        }

        //切换位置
        function qiehuan() {
            var lng = 86.152044 + Math.floor(Math.random() * 589828) / 1e7; //经度范围[121.138398, 121.728226]
            var lat = 41.748162 + Math.floor(Math.random() * 514923) / 1e7; //纬度范围[30.972688, 31.487611]
            map.setZoom(14);
            map.clearMap();

            $(".subsume-right").css("right", "-33.33333%");
            $(".subsume-bottom").css("left", "-66.66666%");

            setTimeout(function () {
                map.setCenter([lng, lat]); //设置地图中心点
                map.setZoom(18);
                setTimeout(function () {
                    biaodian();
                }, 1000)
                $(".subsume-right").css("right", "0");
                $(".subsume-bottom").css("left", "0");
                $("#subsume-person-tubiao").scrollTop(0);
                $("#subsume-equ-body").css("top","0");
            }, 1500)
        }
    }
    mapInit();

    fontEffects();
    function fontEffects() {
        $.fn.countTo = function (options) {
            options = options || {};
            return $(this).each(function () {
                // set options for current element
                var settings = $.extend({}, $.fn.countTo.defaults, {
                    from: $(this).data('from'),
                    to: $(this).data('to'),
                    speed: $(this).data('speed'),
                    refreshInterval: $(this).data('refresh-interval'),
                    decimals: $(this).data('decimals')
                }, options);

                // how many times to update the value, and how much to increment the value on each update
                var loops = Math.ceil(settings.speed / settings.refreshInterval),
                        increment = (settings.to - settings.from) / loops;

                // references & variables that will change with each update
                var self = this,
                        $self = $(this),
                        loopCount = 0,
                        value = settings.from,
                        data = $self.data('countTo') || {};

                $self.data('countTo', data);

                // if an existing interval can be found, clear it first
                if (data.interval) {
                    clearInterval(data.interval);
                }
                data.interval = setInterval(updateTimer, settings.refreshInterval);

                // initialize the element with the starting value
                render(value);

                function updateTimer() {
                    value += increment;
                    loopCount++;

                    render(value);

                    if (typeof (settings.onUpdate) == 'function') {
                        settings.onUpdate.call(self, value);
                    }

                    if (loopCount >= loops) {
                        // remove the interval
                        $self.removeData('countTo');
                        clearInterval(data.interval);
                        value = settings.to;

                        if (typeof (settings.onComplete) == 'function') {
                            settings.onComplete.call(self, value);
                        }
                    }
                }

                function render(value) {
                    //value=toValue;
                    var formattedValue = settings.formatter.call(self, value, settings);
                    $self.html(formattedValue);
                }

            });
        };

        $.fn.countTo.defaults = {
            from: 0,               // the number the element should start at
            to: 0,                 // the number the element should end at
            speed: 1000,           // how long it should take to count between the target numbers
            refreshInterval: 100,  // how often the element should be updated
            decimals: 0,           // the number of decimal places to show
            formatter: formatter,  // handler for formatting the value before rendering
            onUpdate: null,        // callback method for every time the element is updated
            onComplete: null       // callback method for when the element finishes updating
        };

        function formatter(value, settings) {
            return value.toFixed(settings.decimals);
        }

        // custom formatting example
        $('.subsume-norm-num .norm-num').data('countToOptions', {
            formatter: function (value, options) {
                return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
            }
        });

        // start all the timers
        $('.subsume-norm-num .norm-num').each(count);

        function count(options) {
            var $this = $(this);
            options = $.extend({}, options || {}, $this.data('countToOptions') || {});
            $this.countTo(options);
        }
    }

    //图表
    var arr = [];
    function resizeChart() {
        for (var i = 0; i < arr.length; i++) {
            arr[i].resize()
        }
    }

    //各科目教师人数
    var myChart1 = echarts.init(document.getElementById('person-chart'));
    arr.push(myChart1);

    var dataAxis1 = ['语文', '数字', '英语', '化学', '生物', '科学', '物理'];
    //柱子要按照收发文数量进行排次
    var data1 = [220, 182, 191, 234, 290, 330, 310];
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
                    color: '#8C9DBF',
                    fontSize: 14
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
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                textStyle: {
                    color: '#8C9DBF',
                    fontSize: 14
                },
                interval: 0,//标签设置为全部显示
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            splitLine: {
                show: false
            }
        },
        grid: {
            top: 20,
            left: 40,
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
        series: [
            { // For shadow
                type: 'bar',
                itemStyle: {
                    normal: { color: '#303c53' },
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
                            return (params.dataIndex % 2 == 0) ?
                                    '#DA2A3F' :
                                    '#0C94F2';
                        }
                    },
                },
                data: data1,
                animationDelay: 1500
            }
        ]
    };

    myChart1.setOption(option1);

    //各科目教师人数
    var myChart2 = echarts.init(document.getElementById('seniority-chart'));
    arr.push(myChart2);

    var dataAxis2 = ['0-5', '5-10', '10-15', '15-20', '20-25', '25-30'];
    //柱子要按照收发文数量进行排次
    var data2 = [220, 182, 191, 234, 290, 330];

    option2 = {
        color: '#0C94F2',
        xAxis: {
            data: dataAxis2,
            axisLabel: {
                textStyle: {
                    color: '#8C9DBF',
                    fontSize: 14
                },
                interval: 0,//标签设置为全部显示
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                textStyle: {
                    color: '#8C9DBF',
                    fontSize: 14
                },
                interval: 0,//标签设置为全部显示
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            splitLine: {
                show: false
            }
        },
        grid: {
            top: 20,
            left: 40,
            right: 10,
            bottom: 30,
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'line'
            },
        },
        series: [
            {
                type: 'line',
                areaStyle: {
                    normal: {
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
                    }
                },
                data: data2,
                smooth: true,
                showSymbol: false,
                animationDelay: 1500
            }
        ]
    };

    myChart2.setOption(option2);

    //民汉比例
    var myChart3 = echarts.init(document.getElementById('ratio-chart'));
    arr.push(myChart3);

    option3 = {
        color: ['#0C94F2', '#DA2A3F'],
        legend: {
            textStyle: {
                color: '#8C9DBF'
            },
            bottom: 10,
            data: ['男', '女'],
            itemWidth: 13,
            itemHeight: 13,
        },
        tooltip: {
            trigger: 'item'
        },
        grid: {
            top: '3%',
            left: '3%',
            right: '4%',
            bottom: '10'
        },
        series: [
            {
                name: '教师人数',
                type: 'pie',
                radius: ['50%', '70%'],
                center: ['50%', '40%'],
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
                data: [{ name: '男', value: 345 },
                    { name: '女', value: 545 }],
                animationDelay: 1500
            }
        ]
    };

    myChart3.setOption(option3);


    //滚动设备总览
    function gunfawen() {
        var contentnum = parseInt($(".subsume-equ").height() / 42);
        var linum = $(".subsume-equ-body").find(".subsume-equ-li").length;
        if (linum > contentnum) {
            gundongtime = setInterval(function () {
                if (($(".subsume-equ-body").position().top - 10) / -42 < (linum - contentnum)) {
                    if ($(".subsume-equ-body").position().top == 0) {
                        $(".subsume-equ-body").css("top", $(".subsume-equ-body").position().top - 52);
                    } else {
                        $(".subsume-equ-body").css("top", $(".subsume-equ-body").position().top - 42);
                    }
                } else {
                    $(".subsume-equ-body").css("top", 0);
                }
            }, 2000)
        }
    }

    //滚动
    function startmarquee(lh, speed, delay, id) {
        var t;
        var p = false;
        var o = document.getElementById(id);
        var oHeight = o.offsetHeight; /** div的高度 **/
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
                if (preTop >= o.scrollHeight - o.offsetHeight - 3 || preTop == o.scrollTop) {
                    o.scrollTop = 0;
                }
            } else {
                clearInterval(t);
                setTimeout(start, delay);
            }
        }
        setTimeout(start, delay);
    }


    startmarquee(2, 84, 300, "subsume-person-tubiao");
    //窗口变化，图表resize
    $(window).resize(function () {
        resizeChart();
    })
    function circle() { }

    function btnOverstep() { }

</script>
</body>

</html>