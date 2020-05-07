<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<div data-id="view-chart">
	<div class="row">
		<div class="col-sm-6 mt20">
			<div class="result-chart-box">
				<div style="font-size: 16px;font-weight: bold;">选课情况</div>
				<div id="chart01" class="view-chart" style="height:300px;"></div>
			</div>
		</div>
		<div class="col-sm-6 mt20">
			<div class="result-chart-box">
				<div style="font-size: 16px;font-weight: bold;">单科统计结果(共${oneCombineCount!}种)</div>
				<div class="result-chartable-box">
					<div id="chart02" class="view-chart" style="height:300px;width: 50%;"></div>
					<div class="result-ketable-box">
						<div class="result-ketable-item" style="border-top: 1px solid #87B5E6;">
							<div class="result-th">科目</div>
							<div class="result-th">人数</div>
						</div>
						<#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
							<#list courseNameCountMap?keys as key>
								<div class="result-ketable-item">
									<div>${key!}</div>
									<div><a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('1','${couserNameMap[key]!}');">${courseNameCountMap[key]!}</a></div>
								</div>
							</#list>
						</#if>
					</div>
				</div>
			</div>
		</div>
		<div class="col-sm-6 mt20">
			<div class="result-chart-box">
				<div style="font-size: 16px;font-weight: bold;">两科统计结果(共${twoCombineCount!}种)</div>
				<div id="chart03" class="view-chart" style="height:300px;"></div>
				<div id="chart05" class="view-chart" style="height:300px;"></div>
			</div>
		</div>
		<div class="col-sm-6 mt20">
			<div class="result-chart-box">
				<div style="font-size: 16px;font-weight: bold;">三科统计结果(共${threeCombineCount!}种)</div>
				<div id="chart04" class="view-chart" style="height:300px;"></div>
				<div id="chart06" class="view-chart" style="height:300px;"></div>
			</div>
		</div>
	</div>
</div>
<div data-id="view-table" style="display:none;">
	<div class="row">
		<div class="col-md-6">
			<!-- S 整体统计结果 -->
			<div>
				<h3>整体统计结果</h3>
				
				<table class="table table-striped table-hover no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>类型</th>
							<th>选课人数</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td>已选</td>
							<td>
							<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('1','');">${chosenStuIdsNum!}</a>
							</td>
						</tr>
						<tr>
							<td>2</td>
							<td>未选</td>
							<td>
							<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('0','');">${noChosenStuIdsNum!}</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div><!-- E 整体统计结果 -->
			<!-- S 单科统计结果 -->
			<div>
				<h3>单科统计结果（共${oneCombineCount!}种）</h3>
				
				<table class="table table-striped table-hover no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>学科</th>
							<th>选课人数</th>
						</tr>
					</thead>
					<tbody>
						<#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
						 <#list courseNameCountMap?keys as key>
							<tr <#if courseNameCountMap[key]?default(0) == 0>style="display: none" </#if>>
								<td>${key_index+1}</td>
								<td>${key!}</td>
								<td>
								<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('1','${couserNameMap[key]!}');">${courseNameCountMap[key]!}</a>
								</td>
							</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div><!-- E 单科统计结果 -->
			<!-- S 两科统计结果 -->
			<div>
				<h3>两科统计结果（共${twoCombineCount!}种）</h3>
				<#--<p>选取前<input type="text" style="width:40px;" id="arraySize2" name="arraySize2" id="searchTex">组进行<a href="javascript:void(0);" onclick="toAsExport(2,${newConditionList2?size});">同化导出</a></p>
				-->
				<table class="table table-striped table-hover no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>学科</th>
							<th>选课人数</th>
						</tr>
					</thead>
					<tbody>
						<#if newConditionList2?exists && newConditionList2?size gt 0>
						<#list newConditionList2 as item>
							<tr <#if item.sumNum?default(0) == 0>style="display: none" </#if>>
								<td>${item_index+1}</td>
								<td>
                                  <span>${item.subNames[0]!}</span>、
                    	          <span>${item.subNames[1]!}</span>
                          		</td>
								<td>
                              	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('1','${item.subjectIdstr!}');">${item.sumNum!}</a>
                              	</td>
							</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div><!-- E 两科统计结果 -->
		</div>
		<div class="col-md-6">
			<!-- S 三科统计结果 -->
			<div>
				<h3>三科统计结果（共${threeCombineCount!}种）</h3>
				<#--<p>选取前 <input type="text" style="width:40px;" id="arraySize3" name="arraySize3" id="searchTex"> 组进行<a href="javascript:void(0);" onclick="toAsExport(3,${newConditionList3?size});">同化导出</a></p>
				-->
				<table class="table table-striped table-hover no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>学科</th>
							<th>选课人数</th>
						</tr>
					</thead>
					<tbody>
						<#if newConditionList3?exists && newConditionList3?size gt 0>
						<#list newConditionList3 as item>
							<tr <#if item.sumNum?default(0) == 0>style="display: none" </#if>>
								<td>${item_index+1}</td>
								<td>
                                  <span>${item.subNames[0]!}</span>、
                    	          <span>${item.subNames[1]!}</span>、
                    	          <span>${item.subNames[2]!}</span>
                          		</td>
								<td>
								<#if item.limitSubject?default(false)>
									不推荐
								<#else>
                              		<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="toChosenList('1','${item.subjectIdstr!}');">${item.sumNum!}</a>
								</#if>
                              	</td>
							</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div><!-- E 三科统计结果 -->
		</div>
	</div>
		
</div>

<script>
$(function(){
	//选课情况
	var chart01 = document.getElementById("chart01");
	var c01 = echarts.init(chart01);
	c01.setOption({
		color: ['#4A96F8', '#999999'],
		tooltip: {
			trigger: 'item',
			formatter: "{b} : {c} ({d}%)"
		},
		legend: {
			bottom: 10,
			left: 'center',
			data: ['已选', '未选']
		},
		series: [
			{
				name: '选课情况',
				type: 'pie',
				radius: '50%',
				data: [{
					name: '已选',
					value: '${chosenStuIdsNum!}',
					mark: 1
				}, {
					name: '未选',
					value: '${noChosenStuIdsNum!}',
					mark: 0
				}],
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				},
			}
		]
	})
	c01.on('click', function(params){
		var data1=params.data;
		toChosenList(data1.mark,'');
	});
    var chart02 = document.getElementById('chart02');
    var jsonStringData1=jQuery.parseJSON('${jsonStringData1!}');
    var legendData1=jsonStringData1.legendData;
    var loadingData1=jsonStringData1.loadingData;
    var realData1 = jsonStringData1.realData;
    if(legendData1.length>0) {
		echarts.init(chart02).setOption({
			tooltip: {},
			color: ['#4B9BF8'],
			radar: {
				name: {
					textStyle: {
						color: '#333',
						borderRadius: 3,
						padding: [3, 5]
					}
				},
				indicator: loadingData1,
				axisLine: {
					lineStyle: {
						color: '#87B5E6'
					}
				},
				splitLine: {
					lineStyle: {
						color: '#87B5E6'
					}
				},
				splitArea: {
					areaStyle: {
						color: '#F2F6F9'
					}
				}
			},
			series: [{
				name: '单科',
				type: 'radar',
				// areaStyle: {normal: {}},
				data: [
					{
						value: realData1,
						name: '单科'
					}
				],
				areaStyle: {
					normal: {
						color: {
							type: 'linear',
							x: 0,
							y: 0,
							x2: 0,
							y2: 1,
							colorStops: [{
								offset: 0, color: '#4A96F8' // 0% 处的颜色
							}, {
								offset: 1, color: '#54C8FB' // 100% 处的颜色
							}],
							global: false // 缺省为 false
						},
					}
				},
				symbol: 'rect',
				symbolSize: loadingData1.length,
				itemStyle: {
					color: '#4B9BF8'
				}
			}]
		})
	}
    var chart03 = document.getElementById('chart03');
    var jsonStringData2=jQuery.parseJSON('${jsonStringData2!}');
    var legendData2=jsonStringData2.legendData;
    var loadingData2=jsonStringData2.loadingData;
	var allSizeData2 = jsonStringData2.allSizeData;
	var c03 = echarts.init(chart03);
	c03.setOption({
		tooltip: {
			trigger: "axis",
			axisPointer: {
				// 坐标轴指示器，坐标轴触发有效
				type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
			},
			formatter: "{b} {c1}人"
		},
		grid: {
			top: "0",
			left: "2%",
			bottom: "10%",
			containLabel: true
		},
		xAxis: {
			max: '${chosenStuIdsNum!}',
			type: "value",
			position: "top",
			axisLine: {
				show: false
			},
			axisLabel: {
				show: false
			},
			axisTick: {
				show: false
			},
			splitLine: {
				show: false
			}
		},
		yAxis: {
			type: "category",
			data: legendData2,
			axisTick: {
				show: false
			},
			axisLine: {
				show: false
			}
		},
		series: [
			{
				// For shadow
				type: "bar",
				itemStyle: {
					normal: { color: "#DDEBF4" }
				},
				barGap: "-100%",
				barCategoryGap: "40%",
				data: allSizeData2,
				animation: false,
				label: {
					normal: {
						show: true,
						textStyle: {
							color: "#333"
						},
						position: "right"
					}
				}
			},
			{
				name: "选课人数",
				type: "bar",
				data: loadingData2,
				label: {
					normal: {
						show: true,
						textStyle: {
							color: "#333"
						},
						position: "right"
					}
				},
				itemStyle: {
					normal: {
						color: {
							type: "linear",
							x: 0,
							y: 0,
							x2: 1,
							y2: 0,
							colorStops: [
								{
									offset: 0,
									color: "#29A8FA" // 0% 处的颜色
								},
								{
									offset: 1,
									color: "#56E4FC" // 100% 处的颜色
								}
							],
							global: false // 缺省为 false
						}
					}
				}
			}
		]
	});
	c03.on('click', function(params){
		var data2=params.data;
		toChosenList('1',data2.subjectId);
	});
	var chart05 = document.getElementById("chart05");
	var c05 = echarts.init(chart05);
	c05.setOption({
		color:['#F8BD48','#2B9CFE','#F68F5A','#F46E97','#AF89F3','#47C6A4','#CCCCCC '],
		tooltip: {
			trigger: 'item',
			formatter: "{b} : {c} ({d}%)"
		},
		legend: {
			bottom: 10,
			left: 'center',
			data: legendData2,
		},
		series: [
			{
				name: '选课情况',
				type: 'pie',
				radius: '50%',
				data: loadingData2,
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				},
			}
		]
	})
	c05.on('click', function(params){
		var data5=params.data;
		toChosenList('1',data5.subjectId);
	});

    var chart04 = document.getElementById('chart04');
    var jsonStringData3=jQuery.parseJSON('${jsonStringData3!}');
    var legendData3=jsonStringData3.legendData;
    var loadingData3=jsonStringData3.loadingData;
	var allSizeData3 = jsonStringData3.allSizeData;
	var c04 = echarts.init(chart04);
	c04.setOption({
		tooltip: {
			trigger: "axis",
			axisPointer: {
				// 坐标轴指示器，坐标轴触发有效
				type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
			},
			formatter: "{b} {c1}人"
		},
		grid: {
			top: "0",
			left: "2%",
			bottom: "10%",
			containLabel: true
		},
		xAxis: {
			max:'${chosenStuIdsNum!}',
			type: "value",
			position: "top",
			axisLine: {
				show: false
			},
			axisLabel: {
				show: false
			},
			axisTick: {
				show: false
			},
			splitLine: {
				show: false
			}
		},
		yAxis: {
			type: "category",
			data: legendData3,
			axisTick: {
				show: false
			},
			axisLine: {
				show: false
			}
		},
		series: [
			{
				// For shadow
				type: "bar",
				itemStyle: {
					normal: { color: "#DDEBF4" }
				},
				barGap: "-100%",
				barCategoryGap: "40%",
				data: allSizeData3,
				animation: false,
				label: {
					normal: {
						show: true,
						textStyle: {
							color: "#333"
						},
						position: "right"
					}
				}
			},
			{
				name: "选课人数",
				type: "bar",
				data: loadingData3,
				label: {
					normal: {
						show: true,
						textStyle: {
							color: "#333"
						},
						position: "right"
					}
				},
				itemStyle: {
					normal: {
						color: {
							type: "linear",
							x: 0,
							y: 0,
							x2: 1,
							y2: 0,
							colorStops: [
								{
									offset: 0,
									color: "#29A8FA" // 0% 处的颜色
								},
								{
									offset: 1,
									color: "#56E4FC" // 100% 处的颜色
								}
							],
							global: false // 缺省为 false
						}
					}
				}
			}
		]
	});
	c04.on('click', function(params){
		var data3=params.data;
		toChosenList('1',data3.subjectId);
	});
	var chart06 = document.getElementById("chart06");
	var c06 = echarts.init(chart06);
	c06.setOption({
		color:['#F8BD48','#2B9CFE','#F68F5A','#F46E97','#AF89F3','#47C6A4','#CCCCCC '],
		tooltip: {
			trigger: 'item',
			formatter: "{b} : {c} ({d}%)"
		},
		legend: {
			bottom: 10,
			left: 'center',
			data: legendData3,
		},
		series: [
			{
				name: '选课情况',
				type: 'pie',
				radius: '50%',
				data: loadingData3,
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					}
				},
			}
		]
	})
	c06.on('click', function(params){
		var data6=params.data;
		toChosenList('1',data6.subjectId);
	});

	if(${showType?default("1")}=="2"){
		$("#view-table").addClass('active').siblings().removeClass('active');
		$('[data-id="view-table"]').show().siblings('div').hide();
	}
})

function toAsExport(type,max){
	var pattern=/[^0-9]/;
	var nmv = $.trim($('#arraySize'+type).val());
	if(nmv==''){
		layerTipMsg(true,'提示','组数不能为空！');
		return;
	}
	var min = 1;
	if(type==2){
		min=2;
	}
	if(pattern.test(nmv)||nmv.slice(0,1)=="0"){
		layerTipMsg(true,'提示','组数只能维护'+min+'~'+max+'的整数！');
		return;
	}
	var nm = parseInt(nmv);
	if(nm < min || nm > max){
		layerTipMsg(true,'提示','组数只能维护'+min+'~'+max+'的整数！');
		return;
	}
	var choiceId=$("#choiceId").val();
	window.open('${request.contextPath}/newgkelective/'+choiceId+'/choiceResult/export?arrayType='+type+'&arraySize='+nm);
}

function toChosenList(type,subjectIds){
	var choiceId=$("#choiceId").val();
	var url ='${request.contextPath}/newgkelective/'+choiceId+'/chosen/tabHead/page?chosenType='+type+"&subjectIds="+subjectIds+"&scourceType=2";
	if(!$("#allClass").hasClass("selected")){
		var classIdArr = new Array();
		$(".clazz-mark a.selected").each(function(){
			classIdArr.push($(this).attr("data-value"));
		})
		url += "&classIds="+classIdArr.join(",");
	}
	$("#showList").load(url);
}

	
</script>
