<link rel="stylesheet" type="text/css"
        href="${request.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css"/>
<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js" type="text/javascript"
        charset="utf-8"></script>         
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery-toast.js" type="text/javascript"
        charset="utf-8"></script>   
<script src="${request.contextPath}/bigdata/v3/static/plugs/webuploader/webuploader.custom.js" type="text/javascript"
        charset="utf-8"></script> 
<script src="${request.contextPath}/bigdata/v3/static/assets/js/myscript.js" type="text/javascript"
        charset="utf-8"></script> 
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery-show.password.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery-sparkline.min.js" type="text/javascript"
        charset="utf-8"></script>
<!--  
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts.min.js" type="text/javascript"
        charset="utf-8"></script>  
-->
<script src="${request.contextPath}/static/ace/js/date-time/moment.js" type="text/javascript"
        charset="utf-8"></script>          
<div class="">
   <div class="row inter-content">
       <div class="col-xs-9">
           <!-- PAGE CONTENT BEGINS -->
           <div class="row inter-body">
               <div class="col-md-4">
                   <div class="inter-num-basic inter-num-one">
                       <div class="inter-num">
                           <div>接口数</div>
                           <div class="inter-num-shu">${all.apiCount!}</div>
                       </div>
                       <div>
                           <div>类型数</div>
                           <div class="inter-num-shu">${all.typeCount!}</div>
                       </div>
                   </div>
               </div>
               <div class="col-md-4">
                   <div class="inter-num-basic inter-num-two">
                       <div class="inter-num">
                           <div>推送数据</div>
                           <div class="inter-num-shu">${all.saveCount!}</div>
                       </div>
                       <div>
                           <div>推送次数</div>
                           <div class="inter-num-shu">${all.saveNum!}</div>
                       </div>
                   </div>
               </div>
               <div class="col-md-4">
                   <div class="inter-num-basic inter-num-three">
                       <div class="inter-num">
                           <div>拉取数据</div>
                           <div class="inter-num-shu">${all.findCount!}</div>
                       </div>
                       <div>
                           <div>拉取次数</div>
                           <div class="inter-num-shu">${all.findNum!}</div>
                       </div>
                   </div>
               </div>
               <div class="col-md-12">
                   <div class="inter-chart-one" id="inter-chart-one">

                   </div>
               </div>
           </div>
           <!-- PAGE CONTENT ENDS -->
       </div><!-- /.col -->
       <div class="col-xs-3" id = "realTime">
       
       
       </div><!-- /.col -->

       <div class="col-xs-12">
           <div class="inter-type">
               <div class="inter-chart-two" id="inter-chart-two">

               </div>
               <div class="inter-timechange">
                   <div class="btn-group">
                       <button class="btn btn-default active" id = "day">当天</button>
                       <button class="btn btn-default"        id = "week">本周</button>
                       <button class="btn btn-default"        id = "mouth">本月</button>
                   </div>
               </div>
               <div class="inter-chart-modal">
               
               </div>
           </div>
       </div>

       <div class="col-xs-12">
           <div class="row">
               <div class="col-md-6">
                   <div class="inter-deve">
                       <div class="inter-deve-header">开发者推送统计</div>
                       <div class="inter-deve-body gun-content">
                           <table class="tables">
                               <tbody>
                                 <#if developers?exists &&(developers?size gt 0)>
                                      <#list developers as dto>
	                                   <tr>
	                                       <td>${dto.developerName!}</td>
	                                       <td>
	                                           <div class="inter-deve-num">${dto.censusCount.saveCount!}</div>
	                                           <div class="inter-deve-title">推送数据</div>
	                                       </td>
	                                       <td>
	                                           <div class="inter-deve-num">${dto.censusCount.saveNum!}</div>
	                                           <div class="inter-deve-title">推送次数</div>
	                                       </td>
	                                       <td style="width: 110px;">
	                                           <div class="inter-table-chart1" id = '${dto.value!}'></div>
	                                       </td>
	                                   </tr>
	                                  </#list>
	                               </#if>
                               </tbody>
                           </table>
                       </div>
                   </div>
               </div>
               <div class="col-md-6">
                   <div class="inter-deve">
                       <div class="inter-deve-header">开发者拉取统计</div>
                       <div class="inter-deve-body gun-content">
                           <table class="tables">
                               <tbody>
                                   <#if developers?exists &&(developers?size gt 0)>
                                      <#list developers as dto>
	                                   <tr>
	                                       <td>${dto.developerName!}</td>
	                                       <td>
	                                           <div class="inter-deve-num">${dto.censusCount.findCount!}</div>
	                                           <div class="inter-deve-title">拉取数据</div>
	                                       </td>
	                                       <td>
	                                           <div class="inter-deve-num">${dto.censusCount.findNum!}</div>
	                                           <div class="inter-deve-title">拉取次数</div>
	                                       </td>
	                                       <td style="width: 110px;">
	                                           <div class="inter-table-chart2" id = '${dto.value!}'></div>
	                                       </td>
	                                   </tr>
	                                  </#list>
	                               </#if>
                               </tbody>
                           </table>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   </div><!-- /.row -->
</div><!-- /.page-content -->
<script>
		function showRealTimeCount() {
		      var url = "${request.contextPath}/bigdata/monitor/api/realTime/count";
		      $("#realTime").load(url);
		}
        
        //图表
        var arr = [];
        function resizeChart() {
            for (var i = 0; i < arr.length; i++) {
                arr[i].resize()
            }
        }
        //窗口变化，图表resize
        $(window).resize(function () {
            resizeChart();
        })
        
        var myChart1 = echarts.init(document.getElementById('inter-chart-one'));
		arr.push(myChart1);
		var len = 0;
		var timeArr = [];
		var findDate = [];
		var saveDate = [];
		
		function realTimeCensus(){
			showRealTimeCensus();
			//refreshData();
		}
	    
	    function showRealTimeCensus(){
		    var append="append";
		    if(len == 0)
		        append="init";
	    	$.ajax({
	            url:"${request.contextPath}/bigdata/monitor/api/realTime/census",
	            data:{"status":append},
	            async: false,
	            dataType:'json',
	            contentType:'application/json',
	            type:'GET',
	            success: function(response) {
	            	var data = JSON.parse(response.data);
	            	for(var i=0; i < data.length;i++){
	            	    if(timeArr.indexOf(data[i].shijian) != -1){
	            	        continue;
                        }

                        if((findDate.length) >= 30){
                            if(data[i].tuisong)
                                saveDate.push(data[i].tuisong);
                            else
                                saveDate.push(0);
                            if(data[i].laqu)
                                findDate.push(data[i].laqu);
                            else
                                findDate.push(0);
                            timeArr.push(data[i].shijian);
                            findDate.shift();
                            saveDate.shift();
                            timeArr.shift();

                        }else{
                            if(data[i].tuisong)
                                saveDate[len] = data[i].tuisong;
                            else
                                saveDate[len] = 0;
                            if(data[i].laqu)
                                findDate[len] = data[i].laqu;
                            else
                                findDate[len] = 0;
                            timeArr[len]  = data[i].shijian;
                            len++;
                        }
                    }
                    option1 = {
                        title: {
                            text: '实时监控',
                            top: 20,
                            left: 20
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        color: ['#47C6A4', '#AF89F3', '#F46E97', '#F58A53', '#2B9CFE', '#F8BD48', '#71B0FF'],
                        legend: {
                            data: ['推送次数', '拉取次数'],
                            bottom: 20
                        },
                        backgroundColor: '#ffffff',
                        animationEasing: 'elasticOut',
                        animationDelayUpdate: function (idx) {
                            return idx * 1;
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: 60,
                            containLabel: true
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: timeArr
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [
                            {
                                name: '推送次数',
                                type: 'line',
                                data: saveDate,
                            },
                            {
                                name: '拉取次数',
                                type: 'line',
                                data: findDate,
                            }
                        ]
                    };
                    myChart1.setOption(option1);

	            },
	            error: function(XMLHttpRequest, textStatus, errorThrown) {
	    		}
	        });
	    }
	    
	    function refreshData(){
		    //myChart1.set(option1)
			// myChart1.setOption({
			//     xAxis: {
			//         data: timeArr
			//     },
		    //     series: [
		    //        {
		    //          data: saveDate
		    //        },
		    //        {
			//          data: findDate
			//        }
		    //     ]
		    // });
	    }
        

        //类型统计
        var myChart2 = echarts.init(document.getElementById('inter-chart-two'));
        arr.push(myChart2);
        option2 = {
            title: {
                text: '类型统计',
                top: 20,
                left: 20
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            color: ['#47C6A4', '#AF89F3', '#F46E97', '#F58A53', '#2B9CFE', '#F8BD48', '#71B0FF'],
            legend: {
                data: ['推送数据量', '推送次数', '拉取数据量', '拉取次数'],
                bottom: 20
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: 60,
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: [],
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '推送数据量',
                    type: 'bar',
                    data:  [],
                },
                {
                    name: '推送次数',
                    type: 'bar',
                    data:  [],
                },
                {
                    name: '拉取数据量',
                    type: 'bar',
                    data:  [],
                },
                {
                    name: '拉取次数',
                    type: 'bar',
                    data:  [],
                }
            ]
        };

        myChart2.setOption(option2);
        
        myChart2.on('click', function (params) {
            // 根据点击获取数据，弹窗
            var showType = $(".btn-group").find(".active").attr('id');
            var url = "${request.contextPath}/bigdata/monitor/api/interface/countList?resultType=" + params.name + "&showType=" + showType;
            $(".inter-chart-modal").load(url);
            $(".inter-chart-modal").css("right", "0");
        });

        //按鈕組
        $('.btn-group .btn-default').on('click', function () {
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
            //修改數據
            var showType = $(this).attr('id');
            if('mouth' == showType){
            	<#if mouthDto?exists >
	            	option2.xAxis[0].data  = ${JSONUtils.toJSONString(mouthDto.typeArray)};
	            	option2.series[0].data = ${JSONUtils.toJSONString(mouthDto.saveCountArray)};
	            	option2.series[1].data = ${JSONUtils.toJSONString(mouthDto.saveNumArray)};
	            	option2.series[2].data = ${JSONUtils.toJSONString(mouthDto.findCountArray)};
	            	option2.series[3].data = ${JSONUtils.toJSONString(mouthDto.findNumArray)};
	            </#if>
            }else if ('week' == showType){
            	<#if weekDto?exists >
	            	option2.xAxis[0].data  = ${JSONUtils.toJSONString(weekDto.typeArray)};
	            	option2.series[0].data = ${JSONUtils.toJSONString(weekDto.saveCountArray)};
	            	option2.series[1].data = ${JSONUtils.toJSONString(weekDto.saveNumArray)};
	            	option2.series[2].data = ${JSONUtils.toJSONString(weekDto.findCountArray)};
	            	option2.series[3].data = ${JSONUtils.toJSONString(weekDto.findNumArray)};
	            </#if>
            }else if ('day' == showType){
            	<#if dayDto?exists >
		            option2.xAxis[0].data  = ${JSONUtils.toJSONString(dayDto.typeArray)};
		        	option2.series[0].data = ${JSONUtils.toJSONString(dayDto.saveCountArray)};
		        	option2.series[1].data = ${JSONUtils.toJSONString(dayDto.saveNumArray)};
		        	option2.series[2].data = ${JSONUtils.toJSONString(dayDto.findCountArray)};
		        	option2.series[3].data = ${JSONUtils.toJSONString(dayDto.findNumArray)};
		        </#if>
            }
            myChart2.setOption(option2);
        });

        $('.btn-group .btn-default').first().trigger('click');
        
        //类型统计
        $('.inter-deve-body').find('.inter-table-chart1').each(function (i, item) {
        	var ticketKey = $(this).attr('id');
        	var typeArray, saveCountArray,saveNumArray;
        	<#if developers?exists &&(developers?size gt 0)>
               <#list developers as dto>
                    if( '${dto.value!}' ==  ticketKey) {
                    	typeArray      = ${JSONUtils.toJSONString(dto.typeArray)};
                    	saveCountArray = ${JSONUtils.toJSONString(dto.saveCountArray)};
                    	saveNumArray   = ${JSONUtils.toJSONString(dto.saveNumArray)};
                    }
               </#list>
            </#if>
            let myChart3 = echarts.init($('.inter-table-chart1')[i]);
            arr.push(myChart3);
            let option3 = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    position: ['-100%', 0]
                },
                color: ['#47C6A4', '#2B9CFE', '#F46E97', '#F58A53', '#AF89F3', '#F8BD48', '#71B0FF'],
                xAxis: [
                    {
                        type: 'category',
                        show: false,
                        data: typeArray
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        show: false,
                    }
                ],
                series: [
                    {
                        name: '推送数据量',
                        type: 'bar',
                        data: saveCountArray
                    },
                    {
                        name: '推送次数',
                        type: 'bar',
                        data: saveNumArray
                    }
                ]
            };

            myChart3.setOption(option3);
        });

        //开发者拉取统计
        $('.inter-deve-body').find('.inter-table-chart2').each(function (i, item) {
        	var ticketKey = $(this).attr('id');
        	var typeArray, findCountArray,findNumArray;
        	<#if developers?exists &&(developers?size gt 0)>
               <#list developers as dto>
                    if('${dto.value!}' ==  ticketKey) {
                    	typeArray      = ${JSONUtils.toJSONString(dto.typeArray)};
                    	findCountArray = ${JSONUtils.toJSONString(dto.findCountArray)};
                    	findNumArray   = ${JSONUtils.toJSONString(dto.findNumArray)};
                    }
               </#list>
            </#if>
            let myChart4 = echarts.init($('.inter-table-chart2')[i]);
            arr.push(myChart4);
            let option4 = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    position: ['-100%', 0]
                },
                color: ['#47C6A4', '#2B9CFE', '#F46E97', '#F58A53', '#AF89F3', '#F8BD48', '#71B0FF'],
                xAxis: [
                    {
                        type: 'category',
                        show: false,
                        data: typeArray
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        show: false,
                    }
                ],
                series: [
                    {
                        name: '拉取数据量',
                        type: 'line',
                        itemStyle: {
                            normal: {
                                color: '#47C6A4',
                                borderColor: '#47C6A4',
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.1
                                }
                            }
                        },
                        symbolSize: 6,
                        data: findCountArray
                    },
                    {
                        name: '拉取次数',
                        type: 'line',
                        itemStyle: {
                            normal: {
                                color: '#2B9CFE',
                                borderColor: '#2B9CFE',
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.1
                                }
                            }
                        },
                        symbolSize: 6,
                        data: findNumArray
                    }
                ]
            };
            myChart4.setOption(option4);
        });
        
        
        
        function timeControl() {
            timeArr = []
            var startTime = new Date(
                new Date(new Date().toLocaleDateString()).getTime()
            ).getTime()           //当天凌晨十二点               
         
            var endTime = timeFormat(moment().format('YYYY-MM-DD HH:mm:ss'))  //moment.js获取的现在的时间点
            alert(endTime+ "----" + "");
            
            var timerange = endTime - startTime
            var count = Math.floor(timerange / (1 * 60 * 1000))  //时间间隔 （五分钟：5*60*1000）
            console.log(count)
            for (var i = 0; i <= count; i++) {
                var modTine = moment(startTime + (1 * 60 * 1000) * i).format('YYYY-MM-DD HH:mm')
                timeArr.push(modTine)
            }
            if (startTime + count * (60 * 60 * 1000) !== endTime) {
                timeArr.push(moment().format('YYYY-MM-DD HH:mm:ss'))
            }
        }
         
        function timeFormat(time) {             //时间格式封装
            var changetime = time.replace(new RegExp("-", "gm"), "/")
            return (new Date(changetime)).getTime()
        }

        //每一分钟 请求一次
        $(document).ready(function(){
            //去掉定时器的方法
            // window.clearInterval(chartTimer);
            //循环执行，每隔1分钟执行一次
            var chartTimer = window.setInterval(function () {
                showRealTimeCount();
                realTimeCensus();
            }, 60000);
            showRealTimeCount();
            realTimeCensus();
        });
</script>
