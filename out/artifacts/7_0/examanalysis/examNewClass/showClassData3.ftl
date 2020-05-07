<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if emStatRangeList?exists && emStatRangeList?size gt 0>
<#if showType?default("0")!="1">
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
		<table class="table table-bordered table-striped table-hover">
		<#if tableType?default("1")=='1'>
			<thead>
				<tr>
					<th class="text-center" colspan="9">${title!}</th>
				</tr>
				<tr>
					<th>序号</th>
					<th>行政班级</th>
					<th>班主任</th>
					<th>总分最高分</th>
					<th>总分最低分</th>
					<th>标准差</th>
					<th>总分平均分</th>
					<th>班级平均标准分T(年级)</th>
					<th>总分平均分排名</th>
				</tr>
			</thead>
			<tbody>
				<#if emStatRangeList?exists && emStatRangeList?size gt 0>
					<#list emStatRangeList as item>
		 				<tr>
						    <td>${item.classNameOrder!}</td>
						    <td>${item.className!}</td>
						    <td>${item.teacherName!}</td>
						    <td>${item.maxScore?default(0)?string('0.#')}</td>
						    <td>${item.minScore?default(0)?string('0.#')}</td>
						    <td>${item.normDeviation?default(0)?string('0.##')}</td>
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
					<th>序号</th>
					<th>行政班级</th>
					<th>班主任</th>
					<th>总分最高分</th>
					<th>总分最低分</th>
					<th>标准差</th>
					<th>总分平均分</th>
					<th>总分平均分排名</th>
				</tr>
			</thead>
			<tbody>
				<#if emStatRangeList?exists && emStatRangeList?size gt 0>
					<#list emStatRangeList as item>
		 				<tr>
						    <td>${item.classNameOrder!}</td>
						    <td>${item.className!}</td>
						    <td>${item.teacherName!}</td>
						    <td>${item.maxScore?default(0)?string('0.#')}</td>
						    <td>${item.minScore?default(0)?string('0.#')}</td>
						    <td>${item.normDeviation?default(0)?string('0.#')}</td>
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
	<@chartstructure.histogram divStyle="height:400px;" loadingDivId="mychart02" jsonStringData=jsonStringData2 />
	<@chartstructure.histogram isStack=true divStyle="height:400px;" loadingDivId="mychart01" jsonStringData=jsonStringData1 />
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
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("${title!}.xls");
}
</script>
