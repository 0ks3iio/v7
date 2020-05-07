<div class="row">
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span><#if isEdu>学校上报情况<#else>基本信息</#if></span>
			</div>
			<div class="box-echarts-body">
				<#if isEdu>
				<div id="chart1" style="height: 150px;margin-bottom: -86px;"></div>
				<div class="row no-margin">
					<div class="col-sm-12" >
						<div class="text-center color-grey">${reportNum?default(0)+noReportNum?default(0)}所</div>
					</div>
				</div>
				<#else>
					<ul class="statistics-list horizontal clearfix">
						<li class="statistics-item clearfix">
							<div class="statistics-media group"><i class="group"></i></div>
							<div class="statistics-num">${allStuNum}</div>
							<div class="statistics-txt">学生数(人)</div>
						</li>
						<li class="statistics-item clearfix">
							<div class="statistics-media"><i class="man"></i></div>
							<div class="statistics-num">${boyNum}</div>
							<div class="statistics-txt">男生(人)</div>
						</li>
						<li class="statistics-item clearfix">
							<div class="statistics-media woman"><i class="woman"></i></div>
							<div class="statistics-num">${girlNum}</div>
							<div class="statistics-txt">女生(人)</div>
						</li>
					</ul>
					<div class="statistics-time">选课时间：${startTime?string('YYYY-MM-dd')}  至  ${endTime?string('YYYY-MM-dd')}</div>
				</#if>
				
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>整体选课情况</span>
			</div>
			<div class="box-echarts-body">
				<div id="chart2" style="height: 150px;"></div>
			</div>
		</div>
	</div>
</div>

<div class="box-echarts">
	<div class="box-echarts-title">
		<span>单科选课情况</span>
		<#if isEdu><a href="javascript:" id="itemOne">查看详情</a></#if>
	</div>
	<div class="box-echarts-body">
		<div id="chart3" style="height: 200px;"></div>
	</div>
</div>

<div class="box-echarts">
	<div class="box-echarts-title">
		<span>多科选课情况</span>
		<#if isEdu><a href="javascript:" id="itemManay">查看详情</a></#if>
	</div>
	<div class="box-echarts-body">
		<!--这里的width根据实际换算下，默认100%；如果个数很多手动调整宽度，建议一项的宽度是150px-->
		<#assign myWidth=(dataNum3?size)*120>
		<div id="chart4" style="width:${myWidth}px;height: 200px;"></div>
	</div>
</div>
<script>
	<#if isEdu>
		//学校上报情况
		var chart1 = echarts.init(document.getElementById("chart1"));
		option1 = {
			color: ['#2b9cfe', '#b1d3ff'],
		    tooltip : {
		        trigger: 'item',
		        formatter: "{b} : {c} ({d}%)"
		    },
		    calculable : true,
		    series : [
		    	{
		           	name:'数量',
					type:'pie',
					radius: ['50%', '70%'],
					center: ['50%', '50%'],
					data:[
					    {value:${reportNum!}, name:'已上报'},
					    {value:${noReportNum!}, name:'未上报'},
					]
		        }
		    ]
		    <#--
		    series : [
		        {
		            name:'数量',
		            type:'pie',
		            radius : '75%',
		            center: ['50%', '55%'],
		            data:[
		                {value:${reportNum}, name:'已上报'},
		                {value:${noReportNum}, name:'未上报'},
		            ],
		            itemStyle: {
						normal: {
							label : {
								formatter : '{b}{c}所'
							}
						}
					}
		        }
		    ]
		    -->
		};
		chart1.setOption(option1);
	</#if>
	var char2DataContent=new Array();
	var char2BoyDataNum=new Array();
	var char2GirlDataNum=new Array();
	<#list dataContent1 as item>
		char2DataContent[${item_index}]='${item!}';
	</#list>
	<#list dataNum1 as itemDate>
		char2BoyDataNum[${itemDate_index}]=${itemDate[0]};
		char2GirlDataNum[${itemDate_index}]=${itemDate[1]};
	</#list>
	//整体选课情况
	var chart2 = echarts.init(document.getElementById("chart2"));
	option2 = {
		color: ['#2b9cfe', '#ff82ac'],
		tooltip: {
			trigger: 'axis',
			axisPointer: { // 坐标轴指示器，坐标轴触发有效
				type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend: {
			data: ['男', '女'],
			orient: 'vertical',
			x: 'right',
			y: 0,
			padding: 0,
			itemWidth: 12,
			itemHeight: 8,
			textStyle: {
				color: '#666'
			}
		},
		grid: {
			x: 50,
			x2: 40,
			y: 10,
			y2: 20
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			data: char2DataContent
	
		}],
		yAxis: [{
			type: 'value'
		}],
		series: [{
				name: '男',
				type: 'bar',
				stack: '总量',
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char2BoyDataNum
			},
			{
				name: '女',
				type: 'bar',
				stack: '总量',
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char2GirlDataNum
			}
		]
	};
	chart2.setOption(option2);
	
	var char3DataContent=new Array();
	var char3BoyDataNum=new Array();
	var char3GirlDataNum=new Array();
	<#list dataContent2 as item>
		char3DataContent[${item_index}]='${item!}';
	</#list>
	<#list dataNum2 as itemDate>
		char3BoyDataNum[${itemDate_index}]=${itemDate[0]};
		char3GirlDataNum[${itemDate_index}]=${itemDate[1]};
	</#list>
	
	//单科选课情况
	var chart3 = echarts.init(document.getElementById("chart3"));
	option3 = {
		color: ['#2b9cfe', '#ff82ac'],
		tooltip: {
			trigger: 'axis',
			axisPointer: { // 坐标轴指示器，坐标轴触发有效
				type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend: {
			data: ['男', '女'],
			orient: 'vertical',
			x: 'right',
			y: 0,
			padding: 0,
			itemWidth: 12,
			itemHeight: 8,
			textStyle: {
				color: '#666'
			}
		},
		grid: {
			x: 50,
			x2: 40,
			y: 10,
			y2: 20
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			data: char3DataContent
	
		}],
		yAxis: [{
			type: 'value'
		}],
		series: [{
				name: '男',
				type: 'bar',
				stack: '总量',
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char3BoyDataNum
			},
			{
				name: '女',
				type: 'bar',
				stack: '总量',
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char3GirlDataNum
			}
		]
	};
	chart3.setOption(option3);
	
	var char4DataContent=new Array();
	var char4BoyDataNum=new Array();
	var char4GirlDataNum=new Array();
	<#list dataContent3 as item>
		char4DataContent[${item_index}]='${item!}';
	</#list>
	<#list dataNum3 as itemDate>
		char4BoyDataNum[${itemDate_index}]=${itemDate[0]};
		char4GirlDataNum[${itemDate_index}]=${itemDate[1]};
	</#list>
	//多科选课情况
	var chart4 = echarts.init(document.getElementById("chart4"));
	option4 = {
		color: ['#2b9cfe', '#ff82ac'],
		tooltip: {
			trigger: 'axis',
			axisPointer: { // 坐标轴指示器，坐标轴触发有效
				type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend: {
			data: ['男', '女'],
			orient: 'vertical',
			x: 'right',
			y: 0,
			padding: 0,
			itemWidth: 12,
			itemHeight: 8,
			textStyle: {
				color: '#666'
			}
		},
		grid: {
			x: 50,
			x2: 40,
			y: 10,
			y2: 25
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			data: char4DataContent
	
		}],
		yAxis: [{
			type: 'value'
		}],
		series: [{
				name: '男',
				type: 'bar',
				stack: '总量',
				
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char4BoyDataNum
			},
			{
				name: '女',
				type: 'bar',
				stack: '总量',
				
				itemStyle: {
					normal: {
						label: {
							show: true,
							position: 'insideRight',
							formatter: function (params) {
					            if (params.value > 0) {
					                return params.value;
					            } else {
					                return '';
					            }
					        },
						}
					}
				},
				data: char4GirlDataNum
			}
		]
	};
	chart4.setOption(option4);
	<#if isEdu>
		$("#itemOne").on('click',function(){
			$("#showDate").load("${request.contextPath}/newgkelective/edu/subjectHead/page?unitId=${unitId!}&gradeYear=${gradeYear!}");
		})
		
		$("#itemManay").on('click',function(){
			$("#showDate").load("${request.contextPath}/newgkelective/edu/subjectsHead/page?unitId=${unitId!}&gradeYear=${gradeYear!}");
		})
	</#if>
</script>