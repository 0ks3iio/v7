<script type="text/javascript" src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<div id="mychart02" style="width:100%;height:430px;"></div>
<script>
	if(document.getElementById("mychart02")) {
	// 基于准备好的dom，初始化echarts实例
	var myChart2 = echarts.init(document.getElementById("mychart02"));
	$(window).on('resize',function(){myChart2.resize();});
	// 指定图表的配置项和数据
	var option2 = {
		title: {
			text: '${studentName!}的成绩分析'
		},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data:['考试一','考试二','考试三','考试四']
		},
		grid: {
			left: '3%',
			right: '4%',
			bottom: '3%',
			containLabel: true
		},
		toolbox: {
			feature: {
				saveAsImage: {}
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: ${examNames!}
		},
		yAxis: {
			type: 'value'
		},
		series: [
			{
				name:'${titleName!}分析',
				type:'line',
				stack: '总量',
				data: ${scores!}
			}
		]
	};
		myChart2.setOption(option2);
	} 
</script>  