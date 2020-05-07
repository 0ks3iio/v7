<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>

<div data-id="view-chart" >
	<h3>选课情况</h3>
	<div class="explain-default clearfix" >
		<ul class="gk-student-stat">
			<li><span>已选择学生：<a href="javascript:void(0);" onclick="toChosenList('1','');">${chosenStuIdsNum!}</a>人</span></li>
			<li><span>未选择学生：<a href="javascript:void(0);" onclick="toChosenList('0','');">${noChosenStuIdsNum!}</a>人</span></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<h3>单科统计结果（共${oneCombineCount!}种）</h3>
			<div id="chart02" style="height:300px;"></div>
			<h3>两科统计结果（共${twoCombineCount!}种）</h3>
			<div id="chart03" style="height:600px;"></div>
		</div>
		<div class="col-sm-6">
			<h3>三科统计结果（共${threeCombineCount!}种）</h3>
			<div id="chart04" style="height:1000px;"></div>
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
    var chart02 = document.getElementById('chart02');
    var jsonStringData1=jQuery.parseJSON('${jsonStringData1!}');
    var legendData1=jsonStringData1.legendData;
    var loadingData1=jsonStringData1.loadingData;
    var c02 = echarts.init( chart02 );
	c02.setOption({
	    color: ['#cc1c27'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b} {c}人'
	    },
	    grid: {
	    	top: '10',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        position: 'top',
	        minInterval : 1 
	    },
	    yAxis:{
            type: 'category',
            data: legendData1,
            axisTick: {
                alignWithLabel: true
            }
        },
	    series: [
	        {
	            name: '选课人数',
	            type: 'bar',
	            data: loadingData1,
				barMaxWidth: '50',
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            },
                itemStyle: {
                    normal: {
                        color: function (params){
                            var colorList = ['#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff'];
                            return colorList[params.dataIndex];
                        }
                    }
                }
	        }
	   ]
    });
    c02.on('click', function(params){
	    var data1=params.data;
	    toChosenList('1',data1.subjectId);
	});

    var chart03 = document.getElementById('chart03');
    var jsonStringData2=jQuery.parseJSON('${jsonStringData2!}');
    var legendData2=jsonStringData2.legendData;
    var loadingData2=jsonStringData2.loadingData;
    var c03=echarts.init( chart03 );
	c03.setOption({
	    color: ['#1322cc'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b} {c}人'
	    },
	    grid: {
	    	top: '10',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        position: 'top',
	        minInterval : 1 
	    },
	    yAxis:{
            type: 'category',
            data: legendData2,
            axisTick: {
                alignWithLabel: true
            }
        },
	    series: [
	        {
	            name: '选课人数',
	            type: 'bar',
	            data: loadingData2,
				barMaxWidth: '50',
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            },
                itemStyle: {
                    normal: {
                        color: function (params){
                            var colorList = ['#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff'];
                            return colorList[params.dataIndex];
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

    var chart04 = document.getElementById('chart04');
    var jsonStringData3=jQuery.parseJSON('${jsonStringData3!}');
    var legendData3=jsonStringData3.legendData;
    var loadingData3=jsonStringData3.loadingData;
    var c04=echarts.init( chart04 );
	c04.setOption({
	    color: ['#1ccc16'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b} {c}人'
	    },
	    grid: {
	    	top: '10',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        position: 'top',
	        minInterval : 1 
	    },
	    yAxis:{
            type: 'category',
            data: legendData3,
            axisTick: {
                alignWithLabel: true
            }
        },
	    series: [
	        {
	            name: '选课人数',
	            type: 'bar',
	            data: loadingData3,
				barMaxWidth: '50',
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            },
                itemStyle: {
                    normal: {
                        color: function (params){
                            var colorList = ['#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff','#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff'];
                            return colorList[params.dataIndex];
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
	
	if(${showType?default("1")}=="2"){
		$("#view-table").addClass('active').siblings().removeClass('active');
		$('[data-id="view-table"]').show().siblings().hide();
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
