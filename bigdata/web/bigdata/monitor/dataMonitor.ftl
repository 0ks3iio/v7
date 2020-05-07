<div class="box box-default">
	<div class="box-body" >
	   	<div id="monitorDiv" ></div>
	</div>
</div>
 <script type="text/javascript">
    var colorArray=['#c33430','#2e4454','#5fa1a8','#d48363','#91c7af','#719d80','#8085e9','#f7a35c','#91e8e1','#2e7d32','#8085e9','#2b908f','#ffb300','#c33430','#c33430','#c33430','#c33430','#c33430','#c33430','#c33430'];
     var sizeArray=[300,200,200,200,200,200,100,100,100,100,100,50,50,50,50,50,10,10,10,10];
	$(document).ready(function(){
		var monitorDiv = document.getElementById('monitorDiv');
		//自适应宽高
		var myChartContainer = function () {
		    monitorDiv.style.width = ($(".main-content").width()*0.9)+'px';
		    monitorDiv.style.height = ($(".main-content").height()*0.8)+'px';
		};
		myChartContainer();
	    var monitorDivChart = echarts.init(monitorDiv);
		
		var option = {
   			 calculable: true,
    		 series: [{
        		name: '平台系统监控',
       			type: 'treemap',
       			width: '90%',
        		height: '90%',
        		roam: false, //是否开启拖拽漫游（移动和缩放）
       	 		nodeClick: false, //点击节点后的行为,false无反应
        		label: {
            		normal: {
                		textStyle: {
                    		fontWeight: 'bold',
                    		fontSize: '24',
                		}
					}
       			},
        		itemStyle: {
            		normal: {
               			label: {
	                    	show: true,
	                    	formatter: "{b}"
	                	},
	                	borderWidth: 0.51,
	               		borderColor: '#ccc'
	            	}	
        		},
       		 	data: [
       		 	<#if monitorList?exists&&monitorList?size gt 0>
          			<#list monitorList as monitor>
          			<#if monitor_index != 0>
          			,
          			</#if>
	       		 	{
		            	value:  sizeArray[${monitor_index}],
		           		name: '${monitor.monitorName}',
		           		url: '${monitor.monitorUrl!}',
		           		id: '${monitor.monitorUrl!}',
		            	itemStyle: {
		                	normal: {
		                		<#if monitor.monitorUrl! !="">
		                    	color: colorArray[${monitor_index}],
		                    	<#else>
		                    	color: '#9e9e9e',        
		                    	</#if>
		                	}
		            	},
	        		}
        		 </#list>
        		 </#if>
       			 ]
   		 	}]
		};
		monitorDivChart.setOption(option);
		
		monitorDivChart.on('click', function (params) {
			if(params.data.url !=""){
				window.open(params.data.url,params.data.name);
			}
        });
		//浏览器大小改变时重置大小
		window.onresize = function () {
		    myChartContainer();
		    monitorDivChart.resize();
		};
	});
</script>