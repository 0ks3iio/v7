<div id="chart-step" style="height:500px;"></div>
<ul class="health-data">
	<li class="health-data-step">
		<span><i></i></span>
		<h5>步数</h5>
		<em>${step!}</em>
	</li>
	<li class="health-data-km">
		<span>km</span>
		<h5>公里</h5>
		<em>${distance!}</em>
	</li>
	<li class="health-data-cal">
		<span>cal</span>
		<h5>千卡</h5>
		<em>${calorie!}</em>
	</li>
	<li class="health-data-rank">
		<span><i></i></span>
		<h5>班级排名</h5>
		<em>${rank!}</em>
	</li>
</ul>
<script>
	$(document).ready(function(){
		var stepChart = echarts.init($('#chart-step')[0])
		stepChart.setOption({
			color: ['#4bb0d8'],
			   tooltip : {
			       trigger: 'axis',
			       formatter: '{c}'
			   },
			   grid: {
			       left: '3%',
			       right: '4%',
			       bottom: '3%',
			       containLabel: true
			   },
			   xAxis : [
			       {
			           type : 'category',
			           boundaryGap : false,
			           data :${TimeList!}
			       }
			   ],
			   yAxis : [
			       {
			           type : 'value'
			       }
			   ],
			   series : [
			       {
			           type:'line',
			           areaStyle: {normal: {}},
			           data:${stepList!}
			       }
			   ]
		});
	})
</script>