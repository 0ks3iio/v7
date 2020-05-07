<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if emStatRangeList?exists && emStatRangeList?size gt 0>
<#if showType?default("0")!="1">
<div class="table-container no-margin">
	<div class="table-container-body" style="overflow-x: auto;">
			<input type="hidden" id="classId" value=${classId!}>
		<table class="table table-bordered table-striped table-hover">
		<#if tableType?default("1")=='1'>
			<thead>
				<tr>
					<th class="text-center" colspan="9">${title!}</th>
				</tr>
				<tr>
					<th>考试</th>
					<th>统计人数</th>
					<th>总分最高分</th>
					<th>总分最低分</th>
					<th>标准差</th>
					<th>班级所有学生总分之和</th>
					<th>班级总分平均分</th>
					<th>班级平均标准分T(年级)</th>
					<th>总分平均分排名</th>
				</tr>
			</thead>
			<tbody>
				<#if emStatRangeList?exists && emStatRangeList?size gt 0>
					<#list emStatRangeList as item>
		 				<tr>
						    <td><span class="ellipsis w155 inline-block" title="${item.examName!}">${item.examName!}</span></td>
						    <td>${item.statNum?default(0)}</td>
						    <td>${item.maxScore?default(0)?string('0.#')}</td>
						    <td>${item.minScore?default(0)?string('0.#')}</td>
						    <td>${item.normDeviation?default(0)?string('0.##')}</td>
						    <td>${item.sumScore?default(0)?string('0.#')}</td>
						    <td>${item.avgScore?default(0)?string('0.#')}</td>
							<td>${item.avgScoreT?default(0)?string('0.#')}</td>
						    <td>${item.rank!}</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		<#else>
			<thead>
				<tr>
					<th class="text-center" colspan="8">${title!}</th>
				</tr>
				<tr>
					<th>考试</th>
					<th>统计人数</th>
					<th>总分最高分</th>
					<th>总分最低分</th>
					<th>标准差</th>
					<th>班级所有学生总分之和</th>
					<th>班级总分平均分</th>
					<th>总分平均分排名</th>
				</tr>
			</thead>
			<tbody>
				<#if emStatRangeList?exists && emStatRangeList?size gt 0>
					<#list emStatRangeList as item>
						<tr>
						    <td>${item.examName!}</td>
						    <td>${item.statNum?default(0)}</td>
						    <td>${item.maxScore?default(0)?string('0.#')}</td>
						    <td>${item.minScore?default(0)?string('0.#')}</td>
						    <td>${item.normDeviation?default(0)?string('0.##')}</td>
						    <td>${item.sumScore?default(0)?string('0.#')}</td>
						    <td>${item.avgScore?default(0)?string('0.#')}</td>
						    <td>${item.rank!}</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</#if>
		</table>
	</div>
</div>
<#else>
    <#if jsonStringData1?exists&&jsonStringData1!=""&&jsonStringData2?exists&&jsonStringData2!="">
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
	<@chartstructure.histogram isLine=true divStyle="height:400px;" loadingDivId="mychart02" jsonStringData=jsonStringData2 />
	<@chartstructure.histogram isStack=true isLine=true divStyle="height:400px;" loadingDivId="mychart01" jsonStringData=jsonStringData1 />
	<div id="u1235" class="ax_default heading_1">
		<div id="u1235_div" class=""></div>
		<div id="u1235_text" class="text ">
			<p><span>各学科均衡度分析(以班级标准分T为参照)</span></p>
		</div>
	</div>
	<div class="filter">
		<div class="filter-item">
			<label for="" class="filter-name">考试：</label>
			<div class="filter-content">
				<select class="form-control" id="examId" onChange="changeExam()" style="width: 300px;">
					<#if emExamInfoList?exists && emExamInfoList?size gt 0>
							<#list emExamInfoList as item>
								<option value="${item.id!}">${item.examName!}</option>
							</#list>
					</#if>
					<#--${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}-->
				</select>
			</div>
		</div>
	</div>
	<div id="chart4"></div>
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
	function changeScore(conType){
		if(conType &&conType=="2"){
			$("#mychart02").show();
			$("#mychart01").hide();
		}else{
			$("#mychart01").show();
			$("#mychart02").hide();
		}
	}
	$("#mychart02").hide();
	var examId = $("#examId").val();
	var classId = $("#classId").val();
	if(examId&&classId) {
		$("#chart4").load("${request.contextPath}/examanalysis/examNewClass/showClassData5?examId=" + examId + "&classId=" + classId);
	}
	function changeExam(){
		var examId = $("#examId").val();
		var classId = $("#classId").val();
		$("#chart4").load("${request.contextPath}/examanalysis/examNewClass/showClassData5?examId="+examId+"&classId="+classId);
	}

	//var myChart = echarts.init(document.getElementById('mychart03'));
	// 指定图表的配置项和数据
	// var option = {
	// 	// title : {
	// 	// 	text: '罗纳尔多 vs 舍普琴科',
	// 	// 	//subtext: '完全实况球员数据'
	// 	// },
	// 	tooltip : {
	// 		trigger: 'axis'
	// 	},
	// 	// legend: {
	// 	// 	x : 'center',
	// 	// 	data:['罗纳尔多','舍普琴科']
	// 	// },
	// 	toolbox: {
	// 		show : true,
	// 		feature : {
	// 			mark : {show: true},
	// 			dataView : {show: true, readOnly: false},
	// 			restore : {show: true},
	// 			saveAsImage : {show: true}
	// 		}
	// 	},
	// 	calculable : true,
	// 	polar : [
	// 		{
	// 			indicator : [
	// 				{text : '进攻', max  : 100},
	// 				{text : '防守', max  : 100},
	// 				{text : '体能', max  : 100},
	// 				{text : '速度', max  : 100},
	// 				{text : '力量', max  : 100},
	// 				{text : '技巧', max  : 100}
	// 			],
	// 			radius : 130
	// 		}
	// 	],
	// 	series : [
	// 		{
	// 			name: '完全实况球员数据',
	// 			type: 'radar',
	// 			itemStyle: {
	// 				normal: {
	// 					areaStyle: {
	// 						type: 'default'
	// 					}
	// 				}
	// 			},
	// 			data : [
	// 				{
	// 					value : [97, 42, 88, 94, 90, 86],
	// 					name : '舍普琴科'
	// 				},
	// 				{
	// 					value : [97, 32, 74, 95, 88, 92],
	// 					name : '罗纳尔多'
	// 				}
	// 			]
	// 		}
	// 	]
	// };
	// // 使用刚指定的配置项和数据显示图表。
	// myChart.setOption(option);
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("${title!}.xls");
}
</script>