<#if haveExsit?default("")=="true">
	<#if reportType?default("")!="2">
	<div class="table-container">
		<div class="table-container-body" style="overflow-x: auto;">
			<form class="print">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center" colspan="${rangeNameList?size+1}">${title!}</th>
					</tr>
					<tr>
						<#if type?default("")=="3"><th>赋分等级</th>
							<#elseif type?default("")=="2"><th style="padding-right: 45px;">分数段</th>
							<#else><th style="padding-right: 25px;" >名次段</th>
						</#if>
						<#list rangeNameList as rangeName>
							<th <#if rangeName?default("")=="年级全体">style="padding-right: 50px;"<#else>style="padding-right: 20px;"</#if> >${rangeName!}</th>
						</#list>
					</tr>
				</thead>
				<tbody>
					<#list scoreItemList as item>
		 				<tr>
						    <td>${item!}</td>
						    <#if rangeNameList?exists && rangeNameList?size gt 0>
								<#list rangeNameList as rangeName>
									<#if valueMap[item+rangeName]?exists>
									<td>${valueMap[item+rangeName]?default()}</td>
									<#else><td>-</td>
									</#if>
								</#list>
							</#if>
						</tr>
					</#list>
				</tbody>
			</table>
			</form>
		</div>
	</div>
	<#else>
	<div class="filter">
		<div class="filter-item">
			<label><input type="radio" name="conType"  onclick="changeScore('1')" checked=""class="wp"><span class="lbl">各班间对比</span></label>
		</div>
		<div class="filter-item">
			<label><input type="radio" name="conType" onclick="changeScore('2')"  class="wp"><span class="lbl">年级全局</span></label>
		</div>
	</div>
	<div id="mychart01" style="height:400px;"></div>
	<div id="mychart02" style="height:400px;"></div>
	<script type="text/javascript">
		function changeScore(conType){
			if(conType &&conType=="2"){
				$("#mychart02").show();
				$("#mychart01").hide();
			}else{
				$("#mychart01").show();
				$("#mychart02").hide();
			}
		}
	</script>
	<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('mychart02'));
    // 指定图表的配置项和数据
    var option = {
		    color: ['#3398DB'],
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
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
		            data : [<#list scoreItemList as item>
								<#if item_index!=0>,</#if>
								'${item!}'
							</#list>],
		            axisTick: {
		                alignWithLabel: true
		            }
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'人数',
		            type:'bar',
		            barWidth: '60%',
		            data:
		           		 [<#list scoreItemList as item>
							<#if item_index!=0>,</#if>
							'${scoreItemNumMap[item]?default(0)}'
						  </#list>
						  ],
		            label: {
		                normal: {
		                    show: true,
		                    color: '#333',
		                    position: 'top'
		                }
		            },
		        }
		    ]
		};
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $("#mychart02").hide();
    
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('mychart01'));
    // 指定图表的配置项和数据
    var option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		    	type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 20,
		        bottom: 20,
		        data:[
					<#list scoreItemList as item>
						<#if item_index!=0>,</#if>
						'${item!}'
					</#list>
					]
		    },
		    grid: {
		        left: '3%',
		        right: '10%',
		        bottom: '3%',
		        containLabel: true
		    },
		    yAxis:  {
		        type: 'value'
		    },
		    xAxis: {
		        type: 'category',
		        data: [
						<#list rangeNameList as rangeName>
							<#if rangeName_index!=0>,</#if>
								'${rangeName!}'
						</#list>
					 ]
		    },
		    series: [
					<#list scoreItemList as item>
						<#if item_index!=0>,</#if>
						{
				            name: '${item!}',
				            type: 'bar',
				            stack: '总量',
				            data: [
								<#list rangeNameList as rangeName>
									<#if rangeName_index!=0>,</#if>
									'${valueMap[item+rangeName+'num']?default(0)}'
								</#list>
							]
						 }
					</#list>
		    ]
		};
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
	</script>
	</#if>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>
<script>
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	var acadyear = $('#acadyear').val();
	LODOP.SAVE_TO_FILE('${title!}'+".xls");
}
</script>