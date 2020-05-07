<div class="chart-set-wrap index">
	<div class="row  no-padding">
			<div class="col-md-8">
				<div class="box box-default lay-made  three-piece">
					<div class="head-60">
							<span>数据质量综合指数</span>
							<div class="pos-right" style="font-size: 12px;">统计时间：${qualityResult.statTime!}</div>
					</div>
					<div class="clearfix" >
						<div class="man-emotion text-center">
                    		<div class="box-emotion" style="background-color: #faad14!important;">
                    			<p>${qualityResult.result?default(0)}</p>
                        		<div>质量指数</div>
                    		</div>
                    	</div>
                    	<div class="man-emotion text-center">
                    		<div class="box-emotion " style="background-color: #3bb7f0!important;">
                    			<p>${qualityResult.grade?default('')}</p>
                        		<div>质量等级</div>
                    		</div>
                    	</div>
                    	<div class="man-emotion text-center">
                    		<div class="box-emotion " <#if qualityResult.trend?default(0) gte 0>style="background-color: #f5222d!important;"<#else>style="background-color: #73d13d!important;"</#if>>
                    			<p><#if qualityResult.trend?default(0) gte 0><i class="wpfont icon-plus"></i> ${qualityResult.trend?default(0)}% <i class="wpfont icon-arrow-up"><#else> ${qualityResult.trend?default(0)}%<i class="wpfont icon-arrow-down"></#if></i></p>
                        		<div>对比趋势</div>
                    		</div>
                    	</div>
					</div>
				</div>					
				<div class="box box-default lay-made three-piece">
						<div class="head-60"> 
								<span>数据质量走势图</span>
								<div class="pos-right" style="font-size: 12px;">统计时间：${qualityResult.statTime!}</div>
						</div>
						<div class="js-chart" id="line-chart" style="height:105px;"></div>
						<div class="js-chart" id="line-chart-no-data" style="height:105px;"></div>
				</div>
			</div>
			<div class="col-md-4" >
				<div class="box box-default lay-made twice">
					<div class="head-60">
						<span>数据质量均衡度</span>
						<div class="pos-right" style="font-size: 12px;">统计时间：${qualityResult.statTime!}</div>
					</div>
					<div class="js-chart" id="radar-chart" style="height:380px;"></div>
					<div class="js-chart" id="radar-chart-no-data" style="height:380px;"></div>
				</div>
			</div>
		<div class="col-md-12">
				<div class="box box-default lay-made">
					<div class="head-60">
							<span id="dynamicNameId">质量维度叠加对比图</span>
							<div class="pos-right" style="font-size: 12px;">统计时间：${qualityResult.statTime!}</div>
					</div>
					<div class="js-chart" id="bar-chart" style="height:290px;"></div>
					<div class="js-chart" id="bar-chart-no-data" style="height:290px;"></div>
				</div>
			</div>			
	</div>
</div>
<script type="text/javascript">

	function loadRadarChart(){
			$('#radar-chart-no-data').hide();
            $('#radar-chart').show();
            
    		var radar_chart_div=echarts.init(document.getElementById('radar-chart'));
	    	radar_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
    	
			$.ajax({
		            url:"${request.contextPath}/bigdata/monitor/quality/getRadarChart",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							radar_chart_div.hideLoading();
                             $('#radar-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#radar-chart-no-data').show();
                    		$('#radar-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
 							radar_chart_div.on('click', function (params) {
 								if(params.name =="质量均衡度"){
        							$('#dynamicNameId').html('质量维度叠加对比图')
        							loadBarChart();
        						}else{
        							$('#dynamicNameId').html("质量维度--"+params.name+'--走势图')
        							loadBarChart(params.name);
        						}
    						});
    						radar_chart_div.setOption(data);
							radar_chart_div.resize();
							radar_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						radar_chart_div.hideLoading();
		          }
		    });
	}
	
	function loadLineChart(){
			$('#line-chart-no-data').hide();
            $('#line-chart').show();
            
    		var line_chart_div=echarts.init(document.getElementById('line-chart'));
	    	line_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
    	
			$.ajax({
		            url:"${request.contextPath}/bigdata/monitor/quality/getLineChart",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							line_chart_div.hideLoading();
                             $('#line-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#line-chart-no-data').show();
                    		$('#line-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							line_chart_div.setOption(data);
							line_chart_div.resize();
							line_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						line_chart_div.hideLoading();
		          }
		    });
	}

	function loadBarChart(dimName){
			$('#bar-chart-no-data').hide();
            $('#bar-chart').show();
            
    		var bar_chart_div=echarts.init(document.getElementById('bar-chart'));
	    	bar_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
			var url='${request.contextPath}/bigdata/monitor/quality/getBarChart';
    		if(dimName){
    			url='${request.contextPath}/bigdata/monitor/quality/getLineChartByDimName?dimName='+dimName;
    		}
			$.ajax({
		            url:url,
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							bar_chart_div.hideLoading();
                             $('#bar-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#bar-chart-no-data').show();
                    		$('#bar-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							bar_chart_div.clear();
							bar_chart_div.setOption(data);
							bar_chart_div.resize();
							bar_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						bar_chart_div.hideLoading();
		          }
		    });
	}
	 
	 $(document).ready(function(){
		loadRadarChart();
		loadLineChart();    
		loadBarChart();    
	});
</script>