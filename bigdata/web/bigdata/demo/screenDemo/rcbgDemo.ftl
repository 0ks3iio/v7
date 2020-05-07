<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="UTF-8">
    <title>日常办公</title>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>
<body class="everyday-work">
	<!--头部 S-->
    <div class="head-wrap flex-centered">
        <div class="time-box-wrap">
            <div class="time-wrap">
                <i class="wpfont icon-clock"></i>
                <span class="time"></span>
            </div>
        </div>
        
        <div class="title-content">
            <b>日常办公数据监控</b>
        </div>
        
        <div class="box-data-head clearfix">
            <div class="box-data-scope clearfix js-data-pandect">
                <button type="button" class="btn active" id="all" ><span>全部</span></button>
                <button type="button" class="btn" id="lastMonth" ><span>近一月</span></button>
                <button type="button" class="btn" id="threeMonth" ><span>近三月</span></button>
            </div>
        </div>
        
        <div class="full-screen-wrap">
            <div class="full-screen-btn">
                <div class="btn-inner"></div>
            </div>
        </div>
    </div><!--头部 E-->
    
    <!--主体 S-->
    <div class="main-content clearfix">
        <div class="everyday-box-wrap">
            <div class="everyday-box">
                <div class="everyday-box-head">
                    <b>省厅通知公告阅读排名TOP10</b>
                </div>
                <div class="everyday-box-body">
                    <table class="tables everyday-table" id="shengTing">
                    	<thead>
                    		<tr>
                    			<th>排名</th>
                    			<th>单位名称</th>
                    			<th>通知数</th>
                                <th>公告数</th>
                    		</tr>
                    	</thead>
                    	<tbody>
                            <tr>
                                <td>NO.1</td>
                                <td>新疆维吾尔自治区教育局</td>
                                <td>15245</td>
                                <td>2568</td>
                            </tr>
                            <tr>
                                <td>NO.2</td>
                                <td>巴音郭楞蒙古自治州教育局</td>
                                <td>12545</td>
                                <td>2545</td>
                            </tr>
                            <tr>
                                <td>NO.3</td>
                                <td>和田地区教育局</td>
                                <td>8450</td>
                                <td>2017</td>
                            </tr>
                            <tr>
                                <td>NO.4</td>
                                <td>乌鲁木齐市教育局</td>
                                <td>6724</td>
                                <td>1021</td>
                            </tr>
                            <tr>
                                <td>NO.5</td>
                                <td>克孜勒苏柯尔克孜自治州教育局</td>
                                <td>6811</td>
                                <td>801</td>
                            </tr>
                            <tr>
                                <td>NO.6</td>
                                <td>昌吉回族自治州教育局</td>
                                <td>4258</td>
                                <td>800</td>
                            </tr>
                            <tr>
                                <td>NO.7</td>
                                <td>阿克苏地区教育局</td>
                                <td>4125</td>
                                <td>788</td>
                            </tr>
                            <tr>
                                <td>NO.8</td>
                                <td>阿勒泰地区教育局</td>
                                <td>4015</td>
                                <td>679</td>
                            </tr>
                            <tr>
                                <td>NO.9</td>
                                <td>吐鲁番市教育局</td>
                                <td>2587</td>
                                <td>553</td>
                            </tr>
                            <tr>
                                <td>NO.10</td>
                                <td>哈密市教育局</td>
                                <td>1577</td>
                                <td>422</td>
                            </tr>
                        </tbody>
                    </table>
            
                    
                </div>
            </div>
        </div>
        
        <div class="everyday-box-wrap">
            <div class="everyday-box">
                <div class="everyday-box-body">
                    <div id="box-board-wrap" class="box-board-wrap clearfix">
                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>通知发送（条）</span>
                                    <div id="sendNews" class="num" data-from="0" data-to="875" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>通知阅读（条）</span>
                                    <div id="readNews"class="num" data-from="0" data-to="68547" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>公告发布（条）</span>
                                    <div id="sendAnnouncement" class="num" data-from="0" data-to="258" data-speed="500"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>公告阅读（条）</span>
                                    <div id="readAnnouncement"class="num" data-from="0" data-to="10259" data-speed="500"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>教育信息发布（条）</span>
                                    <div id="sendEducationInfo"class="num" data-from="0" data-to="852" data-speed="500"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>教育信息阅读（条）</span>
                                    <div id="readEducationInfo"class="num" data-from="0" data-to="25621" data-speed="500"></div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
        
        <div class="everyday-box-wrap">
            <div class="everyday-box">
                <div class="everyday-box-head">
                    <b>领导活动每日报排名TOP10</b>
                </div>
                <div class="everyday-box-body pt-10">
                    <table class="tables everyday-table" id="leader">
                    	<thead>
                    		<tr>
                    			<th>排名</th>
                    			<th>单位名称</th>
                    			<th>累计次数</th>
                    		</tr>
                    	</thead>
                    	<tbody>
                    		<tr>
                    			<td>NO.1</td>
                    			<td>新疆维吾尔自治区教育厅</td>
                    			<td>1027</td>
                    		</tr>
                            <tr>
                                <td>NO.2</td>
                                <td>克孜勒苏柯尔克孜自治州教育局</td>
                                <td>854</td>
                            </tr>
                            <tr>
                                <td>NO.3</td>
                                <td>巴音郭楞蒙古自治州教育局</td>
                                <td>766</td>
                            </tr>
                            <tr>
                                <td>NO.4</td>
                                <td>和田地区教育局</td>
                                <td>752</td>
                            </tr>
                            <tr>
                                <td>NO.5</td>
                                <td>乌鲁木齐市教育局</td>
                                <td>514</td>
                            </tr>
                            <tr>
                                <td>NO.6</td>
                                <td>新疆大学</td>
                                <td>504</td>
                            </tr>
                            <tr>
                                <td>NO.7</td>
                                <td>昌吉回族自治州教育局</td>
                                <td>488</td>
                            </tr>
                            <tr>
                                <td>NO.8</td>
                                <td>阿克苏地区教育局</td>
                                <td>412</td>
                            </tr>
                            <tr>
                                <td>NO.9</td>
                                <td>阿勒泰地区教育局</td>
                                <td>210</td>
                            </tr>
                            <tr>
                                <td>NO.10</td>
                                <td>吐鲁番市教育局</td>
                                <td>120</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="everyday-box-wrap wide">
            <div class="everyday-box">
                <div class="everyday-box-head">
                    <b>各地区领导活动每日报</b>
                </div>
                <div class="everyday-box-body">
                    <div class="wrap-full" id="four"></div>
                </div>
            </div>
        </div>
        
        <div class="everyday-box-wrap wide">
            <div class="everyday-box">
                <div class="everyday-box-head">
                    <b>各市级地区统计工资记录数量</b>
                </div>
                <div class="everyday-box-body">
                    <div class="wrap-full" id="five"></div>
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
            //图表
            var arr=[];
            function resizeChart(){
                for(var i = 0;i < arr.length;i ++){
                    arr[i].resize()
                }
                chartFour.resize()
            }
            //窗口变化，图表resize
            $(window).resize(function(){
                resizeChart();
            });
            getTime();
            setInterval(getTime, 1000);
            function getTime(){
                var date = new Date();
                var year = date.getFullYear();
                var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
                var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
                $('.time').text(year + '-' + month + '-' + day + ' ' + hours + ":" + minutes);
            }
            
            setInterval(scrollTable, 5000);
            var bool = true;
            function scrollTable(){
                if (bool){
                    $('.everyday-table tbody').each(function(index,ele){
                        $(this).addClass('opacity');
                        setTimeout(function(){
                            $(ele).scrollTop(400);
                            $(ele).removeClass('opacity');
                        },1000)
                    });
                    
                } else {
                    $('.everyday-table tbody').each(function(index,ele){
                        $(this).addClass('opacity');
                        setTimeout(function(){
                            $(ele).scrollTop(0);
                            $(ele).removeClass('opacity');
                        },1000)
                    });
                }
                bool = !bool;
            }
            
            // 各地区领导活动每日报
            var chartFour = echarts.init(document.getElementById('four'));
            arr.push(chartFour);
            chartFour.setOption({
                tooltip : {
                    show: true
                },
                xAxis: {
                    data: ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '克孜勒苏柯尔克孜自治州', '昌吉回族自治州', '阿克苏地区', '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区', '博尔塔拉蒙古自治州', '伊犁哈萨克自治州'],
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
                    axisLine: {show: false},
                    z: 10
                },
                yAxis: {
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: 'rgba(16,51,113,.5)'
                        } 
                    },
                    axisTick: {show: false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: 'rgba(16,51,113,.5)'
                        } 
                    }
                },
                grid:{
                    x:40,
                    y:50,
                    x2:0,
                    bottom:30,
                },
                series: [
                    {
                        type: 'bar',
                        barWidth : '30%',
                        itemStyle: {
                            normal: {
                                barBorderRadius: [15, 15, 0, 0],
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#b91bfe'},
                                        {offset: 0.5, color: '#7e7cfd'},
                                        {offset: 1, color: '#48d1ff'}
                                    ]
                                )
                            },
                            emphasis: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#48d1ff'},
                                        {offset: 0.5, color: '#7e7cfd'},
                                        {offset: 1, color: '#b91bfe'}
                                    ]
                                )
                            }
                        },
                        data: [1522, 1506, 1149, 1712, 903, 859, 410, 534, 240, 215, 351, 280, 359]
                    }
                ]
            });
            
            // 各市级地区统计工资记录数量
            var chartFive = echarts.init(document.getElementById('five'));
            arr.push(chartFive);
            chartFive.setOption({
                tooltip : {
                    show: true
                },
                xAxis: {
                    data: ['克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '昌吉回族自治州', '阿克苏地区',
                        '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区'],
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
                    axisTick: {show: false},
                    axisLine: {show: false},
                    z: 10
                },
                yAxis: {
                    axisLine:{
                        show:true,
                        lineStyle: {
                            color: 'rgba(16,51,113,.5)'
                        }
                    },
                    axisTick: {show: false},
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine:{
                        show:true,
                        lineStyle: {
                            color: 'rgba(16,51,113,.5)'
                        }
                    }
                },
                grid:{
                    x:50,
                    y:50,
                    x2:0,
                    y2:30,
                },
                series: [
                    {
                        type: 'bar',
                        barWidth : '30%',
                        itemStyle: {
                            normal: {
                                barBorderRadius: [15, 15, 0, 0],
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#48d1ff'},
                                        {offset: 0.5, color: '#3eafff'},
                                        {offset: 1, color: '#3591fe'}
                                    ]
                                )
                            },
                            emphasis: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#3591fe'},
                                        {offset: 0.5, color: '#3eafff'},
                                        {offset: 1, color: '#48d1ff'}
                                    ]
                                )
                            }
                        },
                        data: [25245, 15487, 14255, 13841, 12588, 9457, 9245, 8957, 2440, 1587, 1455]
                    }
                ]
            })
            
            //字体翻滚
            fontEffects();
            function fontEffects(){
                $.fn.countTo = function (options) {
                    options = options || {};
                    return $(this).each(function () {
                        // set options for current element
                        var settings = $.extend({}, $.fn.countTo.defaults, {
                            from:            $(this).data('from'),
                            to:              $(this).data('to'),
                            speed:           $(this).data('speed'),
                            refreshInterval: $(this).data('refresh-interval'),
                            decimals:        $(this).data('decimals')
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
            
                            if (typeof(settings.onUpdate) == 'function') {
                                settings.onUpdate.call(self, value);
                            }
            
                            if (loopCount >= loops) {
                                // remove the interval
                                $self.removeData('countTo');
                                clearInterval(data.interval);
                                value = settings.to;
            
                                if (typeof(settings.onComplete) == 'function') {
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
                $('.num').data('countToOptions', {
                    formatter: function (value, options) {
                        return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
                    }
                });
            
                // start all the timers
                $('.num').each(count);
            
                function count(options) {
                    var $this = $(this);
                    options = $.extend({}, options || {}, $this.data('countToOptions') || {});
                    $this.countTo(options);
                }
            }

            $("#all").click(function () {
                $("#shengTing tbody").html("");
                $("#leader tbody").html("");
                $("#shengTing tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>15245</td><td>2568</td></tr>" +
                    "<tr><td>NO.2</td><td>巴音郭楞蒙古自治州教育局</td><td>12545</td><td>2545</td></tr>"+
                    "<tr><td>NO.3</td><td>和田地区教育局</td><td>8450</td><td>2017</td></tr>"+
                    "<tr><td>NO.4</td><td>乌鲁木齐市教育局</td><td>6724</td><td>1021</td></tr>"+
                    "<tr><td>NO.5</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>6811</td><td>801</td></tr>"+
                    "<tr><td>NO.6</td><td>昌吉回族自治州教育局</td><td>4258</td><td>800</td></tr>"+
                    "<tr><td>NO.7</td><td>阿克苏地区教育局</td><td>4125</td><td>788</td></tr>"+
                    "<tr><td>NO.8</td><td>阿勒泰地区教育局</td><td>4015</td><td>679</td></tr>"+
                    "<tr><td>NO.9</td><td>吐鲁番市教育局</td><td>2587</td><td>553</td></tr>"+
                    "<tr><td>NO.10</td><td>哈密市教育局</td><td>1577</td><td>422</td></tr>");
                $("#leader tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>1027</td></tr>"+
                    "<tr><td>NO.2</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>854</td></tr>"+
                    "<tr><td>NO.3</td><td>巴音郭楞蒙古自治州教育局</td><td>766</td></tr>"+
                    "<tr><td>NO.4</td><td>和田地区教育局</td><td>752</td></tr>"+
                    "<tr><td>NO.5</td><td>乌鲁木齐市教育局</td><td>514</td></tr>"+
                    "<tr><td>NO.6</td><td>新疆大学</td><td>504</td></tr>"+
                    "<tr><td>NO.7</td><td>昌吉回族自治州教育局</td><td>488</td></tr>"+
                    "<tr><td>NO.8</td><td>阿克苏地区教育局</td><td>412</td></tr>"+
                    "<tr><td>NO.9</td><td>阿勒泰地区教育局</td><td>210</td></tr>"+
                    "<tr><td>NO.10</td><td>吐鲁番市教育局</td><td>120</td></tr>");
                $("#sendNews").data('to','875');
                $("#readNews").data('to',"68547");
                $("#sendAnnouncement").data('to',"258");
                $("#readAnnouncement").data('to',"10259");
                $("#sendEducationInfo").data('to',"852");
                $("#readEducationInfo").data('to',"25621");
                dataChange();
                chartFour.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '克孜勒苏柯尔克孜自治州', '昌吉回族自治州', '阿克苏地区', '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区', '博尔塔拉蒙古自治州', '伊犁哈萨克自治州'],
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
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:40,
                        y:50,
                        x2:0,
                        bottom:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#b91bfe'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#b91bfe'}
                                        ]
                                    )
                                }
                            },
                            data: [1522, 1506, 1149, 1712, 903, 859, 410, 534, 240, 215, 351, 280, 359]
                        }
                    ]
                });

                chartFive.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '昌吉回族自治州', '阿克苏地区',
                            '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区'],
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
                        axisTick: {show: false},
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:50,
                        y:50,
                        x2:0,
                        y2:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#3591fe'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#3591fe'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                }
                            },
                            data: [25245, 15487, 14255, 13841, 12588, 9457, 9245, 8957, 2440, 1587, 1455]
                        }
                    ]
                })

            })

            $("#lastMonth").click(function () {
                $("#shengTing tbody").html("");
                $("#leader tbody").html("");
                $("#shengTing tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>768</td><td>122</td></tr>" +
                    "<tr><td>NO.2</td><td>乌鲁木齐市教育局</td><td>621</td><td>131</td></tr>"+
                    "<tr><td>NO.3</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>427</td><td>101</td></tr>"+
                    "<tr><td>NO.4</td><td>昌吉回族自治州教育局</td><td>320</td><td>55</td></tr>"+
                    "<tr><td>NO.5</td><td>巴音郭楞蒙古自治州教育局</td><td>312</td><td>42</td></tr>"+
                    "<tr><td>NO.6</td><td>和田地区教育局</td><td>213</td><td>36</td></tr>"+
                    "<tr><td>NO.7</td><td>吐鲁番市教育局</td><td>215</td><td>38</td></tr>"+
                    "<tr><td>NO.8</td><td>哈密市教育局</td><td>203</td><td>34</td></tr>"+
                    "<tr><td>NO.9</td><td>阿克苏地区教育局</td><td>128</td><td>27</td></tr>"+
                    "<tr><td>NO.10</td><td>阿勒泰地区教育局</td><td>86</td><td>22</td></tr>");
                $("#leader tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>22</td></tr>"+
                    "<tr><td>NO.2</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>22</td></tr>"+
                    "<tr><td>NO.3</td><td>巴音郭楞蒙古自治州教育局</td><td>20</td></tr>"+
                    "<tr><td>NO.4</td><td>和田地区教育局</td><td>18</td></tr>"+
                    "<tr><td>NO.5</td><td>乌鲁木齐市教育局</td><td>18</td></tr>"+
                    "<tr><td>NO.6</td><td>昌吉回族自治州教育局</td><td>18</td></tr>"+
                    "<tr><td>NO.7</td><td>阿克苏地区教育局</td><td>14</td></tr>"+
                    "<tr><td>NO.8</td><td>阿勒泰地区教育局</td><td>13</td></tr>"+
                    "<tr><td>NO.9</td><td>吐鲁番市教育局</td><td>13</td></tr>"+
                    "<tr><td>NO.10</td><td>哈密市教育局</td><td>12</td></tr>");
                $("#sendNews").data('to','41');
                $("#readNews").data('to',"3359");
                $("#sendAnnouncement").data('to',"11");
                $("#readAnnouncement").data('to',"489");
                $("#sendEducationInfo").data('to',"42");
                $("#readEducationInfo").data('to',"1237");
                dataChange();

                chartFour.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '克孜勒苏柯尔克孜自治州', '昌吉回族自治州', '阿克苏地区', '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区', '博尔塔拉蒙古自治州', '伊犁哈萨克自治州'],
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
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:40,
                        y:50,
                        x2:0,
                        bottom:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#b91bfe'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#b91bfe'}
                                        ]
                                    )
                                }
                            },
                            data: [75, 60, 30, 90, 75, 8, 28, 40, 33, 7, 18, 20, 25]
                        }
                    ]
                });

                chartFive.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '昌吉回族自治州', '阿克苏地区',
                            '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区'],
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
                        axisTick: {show: false},
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:40,
                        y:50,
                        x2:0,
                        y2:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#3591fe'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#3591fe'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                }
                            },
                            data: [1022, 741, 627, 783, 654, 523, 471, 499, 154, 135, 85]
                        }
                    ]
                })

            })

            $("#threeMonth").click(function () {
                $("#shengTing tbody").html("");
                $("#leader tbody").html("");
                $("#shengTing tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>1522</td><td>256</td></tr>" +
                    "<tr><td>NO.2</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>1323</td><td>260</td></tr>"+
                    "<tr><td>NO.3</td><td>乌鲁木齐市教育局</td><td>853</td><td>213</td></tr>"+
                    "<tr><td>NO.4</td><td>巴音郭楞蒙古自治州教育局</td><td>640</td><td>108</td></tr>"+
                    "<tr><td>NO.5</td><td>昌吉回族自治州教育局</td><td>603</td><td>89</td></tr>"+
                    "<tr><td>NO.6</td><td>和田地区教育局</td><td>439</td><td>65</td></tr>"+
                    "<tr><td>NO.7</td><td>吐鲁番市教育局</td><td>410</td><td>74</td></tr>"+
                    "<tr><td>NO.8</td><td>阿勒泰地区教育局</td><td>406</td><td>72</td></tr>"+
                    "<tr><td>NO.9</td><td>阿克苏地区教育局</td><td>267</td><td>54</td></tr>"+
                    "<tr><td>NO.10</td><td>哈密市教育局</td><td>193</td><td>46</td></tr>");
                $("#leader tbody").append(
                    "<tr><td>NO.1</td><td>新疆维吾尔自治区教育厅</td><td>66</td></tr>"+
                    "<tr><td>NO.2</td><td>克孜勒苏柯尔克孜自治州教育局</td><td>63</td></tr>"+
                    "<tr><td>NO.3</td><td>巴音郭楞蒙古自治州教育局</td><td>58</td></tr>"+
                    "<tr><td>NO.4</td><td>昌吉回族自治州教育局</td><td>56</td></tr>"+
                    "<tr><td>NO.5</td><td>乌鲁木齐市教育局</td><td>54</td></tr>"+
                    "<tr><td>NO.6</td><td>和田地区教育局</td><td>54</td></tr>"+
                    "<tr><td>NO.7</td><td>阿勒泰地区教育局</td><td>45</td></tr>"+
                    "<tr><td>NO.8</td><td>阿克苏地区教育局</td><td>40</td></tr>"+
                    "<tr><td>NO.9</td><td>吐鲁番市教育局</td><td>39</td></tr>"+
                    "<tr><td>NO.10</td><td>哈密市教育局</td><td>34</td></tr>");
                $("#sendNews").data('to','87');
                $("#readNews").data('to',"6843");
                $("#sendAnnouncement").data('to',"24");
                $("#readAnnouncement").data('to',"1032");
                $("#sendEducationInfo").data('to',"91");
                $("#readEducationInfo").data('to',"2542");
                chartFour.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '克孜勒苏柯尔克孜自治州', '昌吉回族自治州', '阿克苏地区', '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区', '博尔塔拉蒙古自治州', '伊犁哈萨克自治州'],
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
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:40,
                        y:50,
                        x2:0,
                        bottom:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#b91bfe'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#7e7cfd'},
                                            {offset: 1, color: '#b91bfe'}
                                        ]
                                    )
                                }
                            },
                            data: [160, 146, 102, 165, 100, 12, 56, 70, 40, 15, 36, 26, 40]
                        }
                    ]
                });

                chartFive.setOption({
                    tooltip : {
                        show: true
                    },
                    xAxis: {
                        data: ['克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '和田地区', '乌鲁木齐市', '昌吉回族自治州', '阿克苏地区',
                            '阿勒泰地区', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区'],
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
                        axisTick: {show: false},
                        axisLine: {show: false},
                        z: 10
                    },
                    yAxis: {
                        axisLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        },
                        axisTick: {show: false},
                        axisLabel: {
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        splitLine:{
                            show:true,
                            lineStyle: {
                                color: 'rgba(16,51,113,.5)'
                            }
                        }
                    },
                    grid:{
                        x:40,
                        y:50,
                        x2:0,
                        y2:30,
                    },
                    series: [
                        {
                            type: 'bar',
                            barWidth : '30%',
                            itemStyle: {
                                normal: {
                                    barBorderRadius: [15, 15, 0, 0],
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#48d1ff'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#3591fe'}
                                        ]
                                    )
                                },
                                emphasis: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#3591fe'},
                                            {offset: 0.5, color: '#3eafff'},
                                            {offset: 1, color: '#48d1ff'}
                                        ]
                                    )
                                }
                            },
                            data: [2500, 1648, 1325, 1484, 1158, 1045, 974, 895, 344, 208, 105]
                        }
                    ]
                })

                dataChange();
            })
        }

        );

        function dataChange() {
            $('.num').data('countToOptions', {
                formatter: function (value, options) {
                    return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
                }
            });

            // start all the timers
            $('.num').each(count);

            function count(options) {
                var $this = $(this);
                options = $.extend({}, options || {}, $this.data('countToOptions') || {});
                $this.countTo(options);
            }
        }
    </script>
</body>
</html>