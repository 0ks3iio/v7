	<div class="chart-set-wrap index">
		<div class="row  no-padding"> 
				<div class="col-md-4">
					<div class="box box-default lay-made">
						<div class="head-60">
								<span>当日概览</span>
						</div>
						<div class="js-chart" id="today-chart" style="height:200px;"></div>
						<div class="js-chart" id="today-chart-no-data" style="height:200px;"></div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="box box-default lay-made">
						<div class="head-60">
								<span>最近7天概览</span>
						</div>
						<div class="js-chart" id="week-chart" style="height:200px;"></div>
						<div class="js-chart" id="week-chart-no-data" style="height:200px;"></div>
					</div>
				</div>	
				<div class="col-md-4">
					<div class="box box-default lay-made">
						<div class="head-60">
								<span>最近14天概览</span>
						</div>
						<div class="js-chart" id="week2-chart" style="height:200px;"></div>
						<div class="js-chart" id="week2-chart-no-data" style="height:200px;"></div>
					</div>
				</div>						
				<div class="col-md-12">
					<div class="box box-default lay-made">
						<div class="head-60">
								<span>最近30天概览</span>
						</div>
						<div class="js-chart" id="month-chart" style="height:230px;"></div>
						<div class="js-chart" id="month-chart-no-data" style="height:230px;"></div>
					</div>
				</div>					
				<div class="col-md-12">
					<div class="box box-default lay-made">
						<div class="head-60">
							<span>最新日志列表</span>
							(<div><span class="circle circle-very-wrong pos-left"></span>严重错误</div>
							<div><span class="circle circle-wrong pos-left"></span>错误</div>
							<div><span class="circle circle-warning pos-left"></span>警告</div>)
						</div>
						<#if logList?exists&&logList?size gt 0>
						<div class="clearfix" >
							<table class="table table-striped table-hover">
									<thead>
										<tr>
											<th width="12px;"></th>
											<th width="200px;">日志时间</th>
											<th>日志类</th>
											<th width="100px;">位置</th>
											<th>日志信息</th>
										</tr>
									</thead>
									<tbody>
								          	<#list logList as log>
												<tr>
													<td <#if log.log_type =="ERROR">bgcolor="#f5222d"<#elseif  log.log_type =="FATAL">bgcolor="#a8071a"<#else>bgcolor="#faad14"</#if></td>
													<td>${log.log_time!}</td>														
													<td>${log.log_class!}</td>
													<td>${log.log_line!}行</td>
												    <td ><#if log.log_message! !="" && log.log_message?length gt 200>${log.log_message?substring(0, 200)}......<#else>${log.log_message!}</#if></td>
												</tr>
								      	    </#list>
									</tbody>
								</table>
						</div>
						<#else>
							<div class="no-data-common">
								<div class="text-center">
									<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
									<p class="color-999">暂无日志记录</p>
								</div>
							</div>
					</#if>
					</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	 $(document).ready(function(){
    		$('#today-chart-no-data').hide();
            $('#today-chart').show();
    		$('#week-chart-no-data').hide();
            $('#week-chart').show();
			$('#week2-chart-no-data').hide();
            $('#week2-chart').show();
    		$('#month-chart-no-data').hide();
            $('#month-chart').show();
            
    		var today_chart_div=echarts.init(document.getElementById('today-chart'));
	    	today_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
			
			var week_chart_div=echarts.init(document.getElementById('week-chart'));
	    	week_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
			
			var week2_chart_div=echarts.init(document.getElementById('week2-chart'));
	    	week2_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
			
			var month_chart_div=echarts.init(document.getElementById('month-chart'));
	    	month_chart_div.showLoading({
			　   text: '数据正在努力加载...'
			});
    	
			$.ajax({
		            url:"${request.contextPath}/bigdata/monitor/log/chart/today",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							today_chart_div.hideLoading();
                             $('#today-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#today-chart-no-data').show();
                    		$('#today-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							today_chart_div.setOption(data);
							today_chart_div.resize();
							today_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						today_chart_div.hideLoading();
		          }
		    });
		    
		    $.ajax({
		            url:"${request.contextPath}/bigdata/monitor/log/chart/week",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							week_chart_div.hideLoading();
                             $('#week-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#week-chart-no-data').show();
                    		$('#week-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							week_chart_div.setOption(data);
							week_chart_div.resize();
							week_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						week_chart_div.hideLoading();
		          }
		    });

			$.ajax({
		            url:"${request.contextPath}/bigdata/monitor/log/chart/week2",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							week2_chart_div.hideLoading();
                             $('#week2-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#week2-chart-no-data').show();
                    		$('#week2-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							week2_chart_div.setOption(data);
							week2_chart_div.resize();
							week2_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						week2_chart_div.hideLoading();
		          }
		    });
		    
		    $.ajax({
		            url:"${request.contextPath}/bigdata/monitor/log/chart/month",
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
							month_chart_div.hideLoading();
                             $('#month-chart-no-data').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    		$('#month-chart-no-data').show();
                    		$('#month-chart').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							month_chart_div.setOption(data);
							month_chart_div.resize();
							month_chart_div.hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						month_chart_div.hideLoading();
		          }
		    });
		});
</script>