<li style="width:100%;float: left;height: 350px;" id = "echart-result-stat">

</li>
<script>
$(function(){
    // 基于准备好的dom，初始化echarts实例
	var myChart1 = echarts.init(document.getElementById("echart-result-stat"));
	window.addEventListener('resize',function(){myChart1.resize();},false);
	var chartObj = jQuery.parseJSON('${data.jsonData!}');
	var xAxisData= chartObj.xAxisData;
	var loadingData=chartObj.loadingData;
	var dataEnd=[];
		for(var i=0;i<loadingData[0].length;i++){
			dataEnd[i]={
				value:loadingData[0][i],
			};
	}
    // 指定图表的配置项和数据
	var option1 = {
		title: {
	        left: 'center',
	        text: '调用接口次数统计'
	    },
		color: ['#317eeb'],
	    tooltip : {
	        trigger: 'item',
	        axisPointer : {
	            type : 'shadow'
	        }
	    },
	    grid: {
	        left: '2%',
	        right: '2%',
	        bottom: '2%',
	        containLabel: true,
	        borderColor: '#e8e8e8'
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: xAxisData,
	            axisTick: {
	                alignWithLabel: true
	            }
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            type:'bar',
	            barWidth: '60%',
	            tooltip:{
	            	formatter: '{b} {c}次'
	            },
	            data:dataEnd,
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'top'
	                }
	            }
	        }
	    ]
	};
    // 使用刚指定的配置项和数据显示图表
	myChart1.setOption(option1);
})
</script>		