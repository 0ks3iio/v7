<#if statArrangeList?exists && statArrangeList?size gt 0>
	<#if reportType?default("")!="2">
	<div class="table-container">
		<div class="table-container-body" style="overflow-x: auto;">
			<form class="print">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center" colspan=<#if is73Sub?default("")=="true">"14"<#else>"9"</#if>>${title!}</th>
					</tr>
					<tr>
						<th>序号</th>
						<th>教学班</th>
						<th>任课老师</th>
						<th>考试最高分</th>
						<th>考试最低分</th>
						<th>考试标准差</th>
						<th>考试平均分</th>
						<th>班级平均标准分T(年级)</th>
						<th>考试平均分排名</th>
						<#if is73Sub?default("")=="true">
							<th>赋分最高分</th>
							<th>赋分最低分</th>
							<th>赋分标准差</th>
							<th>赋分平均分</th>
							<th>赋分平均分排名</th>
						</#if>
					</tr>
				</thead>
				<tbody>
					<#list statArrangeList as item>
		 				<tr>
						    <td>${item_index+1}</td>
						    <td>${item.rangeName!}</td>
						    <td>${item.teacherName!}</td>
						    <td>${item.maxScore?default(0.0)?string("0.0#")}</td>
						    <td>${item.minScore?default(0.0)?string("0.0#")}</td>
						    <td>${item.normDeviation?default(0.0)?string("0.0#")}</td>
						    <td>${item.avgScore?default(0.0)?string("0.0#")}</td>
							<td>${item.avgScoreT?default(0)?string('0.#')}</td>
						    <td>${item.rank!}</td>
						    <#if is73Sub?default("")=="true">
						    <td>${item.conScoreUp?default(0.0)?string("0.0#")}</td>
						    <td>${item.conScoreLow?default(0.0)?string("0.0#")}</td>
						    <td>${item.conNormDeviation?default(0.0)?string("0.0#")}</td>
						    <td>${item.conAvgScore?default(0.0)?string("0.0#")}</td>
						    <td>${item.conAvgRank!}</td>
						    	
							</#if>
						</tr>
					</#list>
				</tbody>
			</table>
			</form>
		</div>
	</div>
	<#else>
	<#if is73Sub?default("")=="true">
		<div class="filter">
			<div class="filter-item">
				<label><input type="radio" name="conType"  onclick="changeScore('1')" checked=""class="wp"><span class="lbl">考试分平均分</span></label>
			</div>
			<div class="filter-item">
				<label><input type="radio" name="conType" onclick="changeScore('2')"  class="wp"><span class="lbl">赋分平均分</span></label>
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
	<#else>
		<#--<div id="mychart01" style="height:400px;"></div>-->
		<div class="filter">
			<div class="filter-item">
				<label>历次考试对比分析（建议参照标准分T为准）</label>
			</div>
		</div>
		<div class="filter">
			<div class="filter-item">
				<label><input type="radio" name="conType"  onclick="changeScore('1')" checked=""class="wp"><span class="lbl">班级总分平均分</span></label>
			</div>
			<div class="filter-item">
				<label><input type="radio" name="conType" onclick="changeScore('2')"  class="wp"><span class="lbl">班级平均标准分T(年级)</span></label>
			</div>
		</div>
		<div id="mychart01" style="height:400px;" onclick="changeScore('1')"></div>
		<div id="mychart03" style="height:400px;" onclick="changeScore('2')"></div>
		<script type="text/javascript">
			function changeScore(conType){
				if(conType &&conType=="2"){
					$("#mychart03").show();
					$("#mychart01").hide();
				}else{
					$("#mychart01").show();
					$("#mychart03").hide();
				}
			}
		</script>
	</#if>
	<script type="text/javascript">
	<#if is73Sub?default("")=="true">
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
			            data : [<#list statArrangeList as item>
									<#if item_index!=0>,</#if>
									'${item.rangeName!}'
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
			            name:'平均分',
			            type:'bar',
			            barWidth: '60%',
			            data:
			           		 [<#list statArrangeList as item>
								<#if item_index!=0>,</#if>
								'${item.conAvgScore?default(0.0)?string("0.0#")}'
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
    </#if>
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('mychart01'));
    
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
			            data : [<#list statArrangeList as item>
									<#if item_index!=0>,</#if>
									'${item.rangeName!}'
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
			            name:'平均分',
			            type:'bar',
			            barWidth: '60%',
			            data:
			           		 [<#list statArrangeList as item>
								<#if item_index!=0>,</#if>
								'${item.avgScore?default(0.0)?string("0.0#")}'
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

	var myChart = echarts.init(document.getElementById('mychart03'));

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
				data : [<#list statArrangeList as item>
					<#if item_index!=0>,</#if>
					'${item.rangeName!}'
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
				name:'班级平均标准分T',
				type:'bar',
				barWidth: '60%',
				data:
						[<#list statArrangeList as item>
							<#if item_index!=0>,</#if>
							'${item.avgScoreT?default(0.0)?string("0.0#")}'
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
	$("#mychart03").hide();
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