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
                <button type="button" class="btn active" onclick="allInformation()"><span>全部</span></button>
                <button type="button" class="btn" onclick="nearOneMonth()"><span>近一月</span></button>
                <button type="button" class="btn" onclick="nearThreeMonth()"><span>近三月</span></button>
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
                    <table class="tables everyday-table">
                    	<thead>
                    		<tr>
                    			<th style="width: 15%;">排名</th>
                    			<th style="width: 45%;">单位名称</th>
                    			<th>通知数</th>
                                <th>公告数</th>
                    		</tr>
                    	</thead>
                    	<tbody id="noticeOrder">
                        <#if noticeOrderAll?exists && noticeOrderAll?size gt 0>
                            <#list noticeOrderAll as notice>
                            <tr>
                                <td style="width: 15%;">${notice_index + 1}</td>
                                <td style="width: 45%;">${notice.unit_name!}</td>
                                <td>${notice.visitnotice_num!}</td>
                                <td>${notice.visitbulletin_num!}</td>
                            </tr>
                            </#list>
                        </#if>
                    	</tbody>
                    </table>
                    <!-- <div class="chart-bottom" id="one"></div>
                    <div class="bottom-tab">
                        <ul>
                            <li>通知</li>
                            <li class="active">公告</li>
                            <li>教育新闻</li>
                        </ul>
                    </div> -->

                </div>
            </div>
        </div>

        <div class="everyday-box-wrap">
            <div class="everyday-box">
                <div class="everyday-box-body">
                    <div class="box-board-wrap clearfix">
                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>通知发送（条）</span>
                                    <div id="relenotice_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.relenotice_num!}</#if>" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>通知阅读（条）</span>
                                    <div id="visitnotice_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.visitnotice_num!}</#if>" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>公告发布（条）</span>
                                    <div id="relebulletin_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.relebulletin_num!}</#if>" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>公告阅读（条）</span>
                                    <div id="visitbulletin_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.visitbulletin_num!}</#if>" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>教育信息发布（条）</span>
                                    <div id="releeduinf_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.releeduinf_num!}</#if>" data-speed="500"></div>
                                </div>
                            </div>
                        </div>

                        <div class="box-board">
                            <div class="box-board-content flex-centered">
                                <div class="text-center">
                                    <span>教育信息阅读（条）</span>
                                    <div id="visiteduinf_num" class="num" data-from="0" data-to="<#if commonModulesAll??>${commonModulesAll.visiteduinf_num!}<#else >0</#if>" data-speed="500"></div>
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
                    <table class="tables everyday-table">
                    	<thead>
                    		<tr>
                    			<th style="width: 15%;">排名</th>
                    			<th>单位名称</th>
                    			<th>累计次数</th>
                    		</tr>
                    	</thead>
                    	<tbody id="leaderActivities">
                        <#if leaderActivityAll?exists && leaderActivityAll?size gt 0>
                            <#list leaderActivityAll as notice>
                                <tr>
                                    <td style="width: 15%;">${notice_index + 1}</td>
                                    <td>${notice.unit_name!}</td>
                                    <td>${notice.visitleadactday_num!0}</td>
                                </tr>
                            </#list>
                        </#if>
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

        var noticeOrder30;
        <#if noticeOrder30?exists && noticeOrder30?size gt 0>
            <#list noticeOrder30 as notice>
                noticeOrder30+='<tr>\n' +
                                '<td style="width: 15%;">${notice_index + 1}</td>\n' +
                                '<td style="width: 45%;">${notice.unit_name!}</td>\n' +
                                '<td>${notice.visitnotice_num!}</td>\n' +
                                '<td>${notice.visitbulletin_num!}</td>\n' +
                                '</tr>';
            </#list>
        </#if>
        var noticeOrder90;
        <#if noticeOrder90?exists && noticeOrder90?size gt 0>
        <#list noticeOrder90 as notice>
        noticeOrder90+='<tr>\n' +
            '<td style="width: 15%;">${notice_index + 1}</td>\n' +
            '<td style="width: 45%;">${notice.unit_name!}</td>\n' +
            '<td>${notice.visitnotice_num!}</td>\n' +
            '<td>${notice.visitbulletin_num!}</td>\n' +
            '</tr>';
        </#list>
        </#if>
        var noticeOrderAll;
        <#if noticeOrderAll?exists && noticeOrderAll?size gt 0>
        <#list noticeOrderAll as notice>
        noticeOrderAll+='<tr>\n' +
            '<td style="width: 15%;">${notice_index + 1}</td>\n' +
            '<td style="width: 45%;">${notice.unit_name!}</td>\n' +
            '<td>${notice.visitnotice_num!}</td>\n' +
            '<td>${notice.visitbulletin_num!}</td>\n' +
            '</tr>';
        </#list>
        </#if>

        var leaderActivity30;
        <#if leaderActivity30?exists && leaderActivity30?size gt 0>
        <#list leaderActivity30 as notice>
        leaderActivity30+=" <tr>\n" +
            "        <td style=\"width: 15%;\">${notice_index + 1}</td>\n" +
            "        <td>${notice.unit_name!}</td>\n" +
            "        <td>${notice.visitleadactday_num!0}</td>\n" +
            "        </tr>";
        </#list>
        </#if>
        var leaderActivity90;
        <#if leaderActivity90?exists && leaderActivity90?size gt 0>
        <#list leaderActivity90 as notice>
        leaderActivity90+=" <tr>\n" +
            "        <td style=\"width: 15%;\">${notice_index + 1}</td>\n" +
            "        <td>${notice.unit_name!}</td>\n" +
            "        <td>${notice.visitleadactday_num!0}</td>\n" +
            "        </tr>";
        </#list>
        </#if>
        var leaderActivityAll;
        <#if leaderActivityAll?exists && leaderActivityAll?size gt 0>
        <#list leaderActivityAll as notice>
       leaderActivityAll+=" <tr>\n" +
           "        <td style=\"width: 15%;\">${notice_index + 1}</td>\n" +
           "        <td>${notice.unit_name!}</td>\n" +
           "        <td>${notice.visitleadactday_num!0}</td>\n" +
           "        </tr>";
        </#list>
        </#if>

        // 各地区领导活动每日报
        var chartFour = echarts.init(document.getElementById('four'));
        var opFour1 = {
            tooltip : {
                show: true
            },
            xAxis: {
                data: [<#if leadershipDailyMapAll?exists && leadershipDailyMapAll?size gt 0>
                    <#list leadershipDailyMapAll?keys as key>'${key}',</#list></#if>],
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
                    data: [<#if leadershipDailyMapAll?exists && leadershipDailyMapAll?size gt 0>
                        <#list leadershipDailyMapAll?values as value>'${value[0]}',</#list></#if>]
                }
            ]
        };

        // 各市级地区统计工资记录数量
        var chartFive = echarts.init(document.getElementById('five'));
        var opFive1 = {
            tooltip : {
                show: true
            },
            xAxis: {
                data: [<#if leadershipDailyMapAll?exists && leadershipDailyMapAll?size gt 0>
                    <#list leadershipDailyMapAll?keys as key>'${key}',</#list></#if>],
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
                    data: [<#if leadershipDailyMapAll?exists && leadershipDailyMapAll?size gt 0>
                        <#list leadershipDailyMapAll?values as value>'${value[1]}',</#list></#if>]
                }
            ]
        };

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
            arr.push(chartFour);
            chartFour.setOption(opFour1);

            // 各市级地区统计工资记录数量
            arr.push(chartFive);
            chartFive.setOption(opFive1);

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


        })

        function nearOneMonth() {
            <#if commonModules30??>
            $("#relenotice_num").data('to','${commonModules30.relenotice_num!}');
            $("#visitnotice_num").data('to','${commonModules30.visitnotice_num!}');
            $("#relebulletin_num").data('to','${commonModules30.relebulletin_num!}');
            $("#visitbulletin_num").data('to','${commonModules30.visitbulletin_num!}');
            $("#releeduinf_num").data('to','${commonModules30.releeduinf_num!}');
            $("#visiteduinf_num").data('to','${commonModules30.visiteduinf_num!}');
            </#if>
            <#if leadershipDailyMap30?exists && leadershipDailyMap30?size gt 0>
            opFour1.xAxis.data=[<#list leadershipDailyMap30?keys as key>'${key}',</#list>];
            opFour1.series[0].data=[<#list leadershipDailyMap30?values as value>'${value[0]}',</#list>];
            chartFour.setOption(opFour1);
            opFive1.xAxis.data=[<#list leadershipDailyMap30?keys as key>'${key}',</#list>];
            opFive1.series[0].data=[<#list leadershipDailyMap30?values as value>'${value[1]}',</#list>];
            chartFive.setOption(opFive1);
            </#if>
            $("#noticeOrder").find('tr').remove();
            $("#noticeOrder").append(noticeOrder30);
            $("#leaderActivities").find('tr').remove();
            $("#leaderActivities").append(leaderActivity30);
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
        function nearThreeMonth() {
            <#if commonModules90??>
            $("#relenotice_num").data('to','${commonModules90.relenotice_num!}');
            $("#visitnotice_num").data('to','${commonModules90.visitnotice_num!}');
            $("#relebulletin_num").data('to','${commonModules90.relebulletin_num!}');
            $("#visitbulletin_num").data('to','${commonModules90.visitbulletin_num!}');
            $("#releeduinf_num").data('to','${commonModules90.releeduinf_num!}');
            $("#visiteduinf_num").data('to','${commonModules90.visiteduinf_num!}');
            </#if>
            <#if leadershipDailyMap90?exists && leadershipDailyMap90?size gt 0>
            opFour1.xAxis.data=[<#list leadershipDailyMap90?keys as key>'${key}',</#list>];
            opFour1.series[0].data=[<#list leadershipDailyMap90?values as value>'${value[0]}',</#list>];
            chartFour.setOption(opFour1);
            opFive1.xAxis.data=[<#list leadershipDailyMap90?keys as key>'${key}',</#list>];
            opFive1.series[0].data=[<#list leadershipDailyMap90?values as value>'${value[1]}',</#list>];
            chartFive.setOption(opFive1);
            </#if>
            $("#noticeOrder").find('tr').remove();
            $("#noticeOrder").append(noticeOrder90);
            $("#leaderActivities").find('tr').remove();
            $("#leaderActivities").append(leaderActivity90);
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
        function allInformation() {
            <#if commonModulesAll??>
            $("#relenotice_num").data('to','${commonModulesAll.relenotice_num!}');
            $("#visitnotice_num").data('to','${commonModulesAll.visitnotice_num!}');
            $("#relebulletin_num").data('to','${commonModulesAll.relebulletin_num!}');
            $("#visitbulletin_num").data('to','${commonModulesAll.visitbulletin_num!}');
            $("#releeduinf_num").data('to','${commonModulesAll.releeduinf_num!}');
            $("#visiteduinf_num").data('to','${commonModulesAll.visiteduinf_num!}');
            </#if>
            <#if leadershipDailyMapAll?exists && leadershipDailyMapAll?size gt 0>
            opFour1.xAxis.data=[<#list leadershipDailyMapAll?keys as key>'${key}',</#list>];
            opFour1.series[0].data=[<#list leadershipDailyMapAll?values as value>'${value[0]}',</#list>];
            chartFour.setOption(opFour1);
            opFive1.xAxis.data=[<#list leadershipDailyMapAll?keys as key>'${key}',</#list>];
            opFive1.series[0].data=[<#list leadershipDailyMapAll?values as value>'${value[1]}',</#list>];
            chartFive.setOption(opFive1);
            </#if>
            $("#noticeOrder").find('tr').remove();
            $("#noticeOrder").append(noticeOrderAll);
            $("#leaderActivities").find('tr').remove();
            $("#leaderActivities").append(leaderActivityAll);
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
    </script>
</body>
</html>