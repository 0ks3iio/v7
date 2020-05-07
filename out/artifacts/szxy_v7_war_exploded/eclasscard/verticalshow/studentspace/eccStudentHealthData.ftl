<div id="chart-step" style="height:500px;"></div>
<ul class="health-data">
	<li>
		<span><i class="icon icon-run"></i></span>
		<em>${step!}</em>
		<h5>步数</h5>
	</li>
	<li>
		<span><i class="icon icon-km"></i></span>
		<em>${distance!}</em>
		<h5>公里</h5>
	</li>
	<li>
		<span><i class="icon icon-cal"></i></span>
		<em>${calorie!}</em>
		<h5>千卡</h5>
	</li>
	<li>
		<span><i class="icon icon-award"></i></span>
		<em>${rank!}</em>
		<h5>班级排名</h5>
	</li>
</ul>
<script>
	$(document).ready(function(){
			var stepChart = echarts.init($('#chart-step')[0])

			stepChart.setOption({
				color: ['#ffc000'],
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
			            data : ${TimeList!}
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
			            areaStyle: {normal: {
			            	color: {
			            		type: 'linear',
			            		x: 0,
			            		y: 0,
			            		x2: 0,
			            		y2: 1,
			            		colorStops: [{
							        offset: 0, color: '#f9e791' // 0% 处的颜色
							    }, {
							        offset: 1, color: '#fefaeb' // 100% 处的颜色
							    }],
			            	}
			            }},
			            data:${stepList!}
			        }
			    ]
			});
		})
</script>