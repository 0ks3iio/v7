<div class="report-subtit mb20">进步度/本次考试标准分T（年级）象限图</div>
<div class="row">
	<div class="col-xs-6">
		<div id="chart5" style="height: 500px;"></div>
	</div>
	<div class="col-xs-5 color-666">
		<div>【图表说明】</div>
		<div>A：标准分T以50为成绩好与差的分界线（即整个年级群体平均标准分T为50）</div>
		<div>B：进步度为正数说明进步，负数说明退步</div>
		<div class="mt20 text-center"><img src="${request.contextPath}/static/images/scoreAnalysis/echarts-info.jpg"></div>
	</div>
</div>
<div class="report-subtit mb20">进步度/上次考试标准分T（年级）象限图</div>
<div class="row">
	<div class="col-xs-6">
		<div id="chart6" style="height: 500px;"></div>
	</div>
	<div class="col-xs-5 color-666">
		<div>【图表说明】</div>
		<div>A：标准分T以50为成绩好与差的分界线（即整个年级群体平均标准分T为50）</div>
		<div>B：进步度为正数说明进步，负数说明退步</div>
		<div class="mt20 text-center"><img src="${request.contextPath}/static/images/scoreAnalysis/echarts-info2.jpg"></div>
	</div>
</div>
<script>
	$(function(){
		setChart5();
		setChart6();
	})
	function setChart5(){
		var chart5 = echarts.init(document.getElementById("chart5"));
		//进步度/本次考试标准分T（年级）象限图
		var img = new Image();
		img.src='${request.contextPath}/static/images/scoreAnalysis/echarts-bg.png';
		debugger;
		option5 = {
			color: ['#F65353','#05D2B3', '#0073F9', '#F4AE38'],
			backgroundColor: {
			    image: img,
			    repeat: 'no-repeat'
			},
			tooltip : {
                show: true
            },
			grid: {
				x: 35,
				y: 30
			},
		    xAxis: {
		    	name: '标准分T-50',
		    	splitLine:{
                    show:false
               },
		    	axisLabel: {
                    textStyle: {
                        color: '#666'
                    }
                },
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#666'
                    } 
                }
		    },
		    yAxis: {
		    	name: '进步度',
		    	axisLabel: {
                    textStyle: {
                        color: '#666'
                    }
                },
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#666'
                    } 
                },
                splitLine:{
                    show:false
                }
		    },
		     series: [{
			        symbolSize: 6,
			        data: [
			           	<#if string1?exists && string1?size gt 0>
			        		<#list string1 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string2?exists && string2?size gt 0>
			        		<#list string2 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string3?exists && string3?size gt 0>
			        		<#list string3 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string4?exists && string4?size gt 0>
			        		<#list string4 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    }]
		};             
		chart5.setOption(option5);
	}
	function setChart6(){
		debugger;
		var chart6 = echarts.init(document.getElementById("chart6"));
		//进步度/本次考试标准分T（年级）象限图
		var img = new Image();
		img.src='${request.contextPath}/static/images/scoreAnalysis/echarts-bg.png'
		option6 = {
			color: ['#F65353','#05D2B3', '#0073F9', '#F4AE38'],
			backgroundColor: {
			    image: img,
			    repeat: 'no-repeat'
			},
			tooltip : {
                show: true
            },
			grid: {
				x: 35,
				y: 30
			},
		    xAxis: {
		    	name: '标准分T-50',
		    	splitLine:{
                    show:false
               },
		    	axisLabel: {
                    textStyle: {
                        color: '#666'
                    }
                },
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#666'
                    } 
                }
		    },
		    yAxis: {
		    	name: '进步度',
		    	axisLabel: {
                    textStyle: {
                        color: '#666'
                    }
                },
                axisLine:{
                    show:true,
                    lineStyle: {
                        color: '#666'
                    } 
                },
                splitLine:{
                    show:false
                }
		    },
		    series: [{
			        symbolSize: 6,
			        data: [
			           	<#if string11?exists && string11?size gt 0>
			        		<#list string11 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string12?exists && string12?size gt 0>
			        		<#list string12 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string13?exists && string13?size gt 0>
			        		<#list string13 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    },{
			        symbolSize: 6,
			        data: [
			            <#if string14?exists && string14?size gt 0>
			        		<#list string14 as item>
			        			<#if item_index!=0>,</#if>
			        			[${item[0]!},${item[1]}]
			        		</#list>
		        		</#if>
			        ],
			        type: 'scatter'
			    }]
		};             
		chart6.setOption(option6);
	}
</script>