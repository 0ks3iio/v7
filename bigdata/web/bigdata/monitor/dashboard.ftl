<div class="chart-set-wrap index" >
	<div class="row data-box-wrap no-padding">
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-yuan.png">
				</div>
				<div class="data-head">
					数据源
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.db_today_num!} | ${summaryStat.db_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-API.png">
				</div>
				<div class="data-head">
					API
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.api_today_num!} | ${summaryStat.api_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-model.png">
				</div>
				<div class="data-head">
					模型
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.model_today_num!} | ${summaryStat.model_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-JOB.png">
				</div>
				<div class="data-head">
					任务
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.job_today_num!} | ${summaryStat.job_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-screen.png">
				</div>
				<div class="data-head">
					大屏
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.screen_today_num!} | ${summaryStat.screen_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-chart.png">
				</div>
				<div class="data-head">
					图表
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.chart_today_num!} | ${summaryStat.chart_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-table.png">
				</div>
				<div class="data-head">
					报表
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.report_today_num!} | ${summaryStat.report_total_num!}
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="data-box">
				<div class="left-pos-pic">
					<img src="${request.contextPath}/static/bigdata/images/big-data-events.png">
				</div>
				<div class="data-head">
					事件
					<span class="pos-right">今日/累计</span>
				</div>
				<div class="data-num">
					${summaryStat.event_today_num!} | ${summaryStat.event_total_num!}
				</div>
			</div>
		</div>
	</div>
	<div class="row">
			<div class="col-md-6">
				<div class="box box-default lay-made">
					<div class="head-60">
						<span>HDFS文件概览</span>
						<div class="pos-right" style="font-size: 12px;">统计时间：${statTime!}</div>
					</div>
					<div class="js-chart centered" id="">
						<div class="man-emotion text-center">
                    		<div class="box-emotion" style="background-color: #36cfc9!important;">
                    			<p>${hdfsStat.totalsize!}</p>
                			<div>已用空间</div>
                		</div>
                	</div>
                	<div class="man-emotion text-center">
                		<div class="box-emotion" style="background-color: #f759ab!important;">
                			<p>${hdfsStat.recordnum!}</p>
                			<div>总文件数</div>
                		</div>
                	</div>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="box box-default lay-made">
				<div class="head-60">
					<span>数据库概览</span>
					<div class="pos-right" style="font-size: 12px;">统计时间：${statTime!}</div>
				</div>
				<div class="js-chart centered" id="" >
					<div class="man-emotion text-center" >
                		<div class="box-emotion"  style="background-color: #40a9ff!important;">
                			<p>${mysqlStat.totalsize!}</p>
                			<div>已用空间</div>
                		</div>
                	</div>
                	<div class="man-emotion text-center">
                		<div class="box-emotion"  style="background-color: #ffa940!important;">
                			<p>${mysqlStat.recordnum!}</p>
                			<div>总数据量</div>
                		</div>
                	</div>
				</div>
			</div>
		</div>
	<div class="col-md-12">
		<div class="box box-default lay-made">
			<div class="head-60">
					<span>最近一周模块点击量</span>
					<div class="pos-right" style="font-size: 12px;">统计时间：${statTime!}</div>
			</div>
			<div class="js-chart" id="model-chart" style="height:200px;"></div>
			<div class="js-chart" id="model-chart-no-data" style="height:200px;"></div>
		</div>
	</div>			
	<div class="col-md-6">
		<div class="box box-default lay-made normal">
			<div class="head-60">
					<span>最近一周任务运行概览</span>
					<div class="pos-right" style="font-size: 12px;">统计时间：${statTime!}</div>
			</div>
			<div class="clearfix" >
				<div class="man-emotion text-center">
            		<div class="box-emotion" style="background-color: #73d13d!important;">
            			<p>${jobStat.success?default('0')}</p>
                		<div>成功</div>
            		</div>
            	</div>
            	<div class="man-emotion text-center">
            		<div class="box-emotion " style="background-color: #f5222d!important;">
            			<p>${jobStat.error?default('0')}</p>
                		<div>失败</div>
            		</div>
            	</div>
			</div>
		</div>					
		<div class="box box-default lay-made three-piece">
			<div class="head-60">
					<span>最近一周日志概览</span>
					<div class="pos-right" style="font-size: 12px;">统计时间：${statTime!}</div>
			</div>
			<div class="clearfix" >
				<div class="man-emotion text-center" >
            		<div class="box-emotion" style="background-color: #faad14!important;">
            			<p>${logStat.WARN?default('0')}</p>
            			<div>警告</div>
            		</div>
            	</div>
            	<div class="man-emotion text-center" >
            		<div class="box-emotion" style="background-color: #f5222d!important;">
            			<p>${logStat.ERROR?default('0')}</p>
            			<div>错误</div>
            		</div>
            	</div>
            	<div class="man-emotion text-center" >
            		<div class="box-emotion " style="background-color: #a8071a!important;">
            			<p>${logStat.FATAL?default('0')}</p>
            			<div>严重错误</div>
            		</div>
            	</div>
			</div>
		</div>
	</div>				
	<div class="col-md-6" id="logListDiv">
		
	</div>
</div>
</div>	
<script type="text/javascript">
	 $(document).ready(function(){
    		$('#model-chart-no-data').hide();
            $('#model-chart').show();
            
    		var model_chart_div=echarts.init(document.getElementById('model-chart'));
	    	model_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
    	
			$.ajax({
		            url:"${request.contextPath}/bigdata/monitor/getModelChart",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							model_chart_div.hideLoading();
                             $('#model-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#model-chart-no-data').show();
                    		$('#model-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							model_chart_div.setOption(data);
							model_chart_div.resize();
							model_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						model_chart_div.hideLoading();
		          }
		    });

			var index=layer.msg('数据加载中......', {
			      icon: 16,
			      time:0,
			      shade: 0.01
		    });
		    
			$("#logListDiv").load( "${request.contextPath}/bigdata/monitor/log/component",function() {
	  			layer.close(index);
			});
			
		});
</script>