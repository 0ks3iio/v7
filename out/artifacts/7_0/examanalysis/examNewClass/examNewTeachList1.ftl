<#if statList?exists && statList?size gt 0>
<#if summary?default("")!="">
	<div class="explain">
		<p>${summary!}</p>
	</div>
</#if>
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
		<form class="print">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center" <#if is73Sub?default("")=='true'>colspan="10"<#else>colspan="9"</#if>>${title!}</th>
				</tr>
				<tr>
					<th>序号</th>
					<th>考号</th>
					<th>行政班</th>
					<th>学生</th>
					<th>考试分</th>
					<th>标准分T（年级）</th>
					<th>百分等级分数（年级）</th>
					<#if is73Sub?default("")=='true'><th>赋分</th></#if>
					<th>年级排名</th>
					<th>班级排名</th>
				</tr>
			</thead>
			<tbody>
				<#list statList as stat>
	 				<tr>
					    <td>${stat_index+1}</td>
					    <td>${stat.examNum!}</td>
					    <td>${stat.className!}</td>
					    <td>${stat.studentName!}</td>
					    <td>${stat.score?default(0.0)?string("0.0#")}</td>
					    <td>${stat.scoreT?default(0.0)?string("0.0#")}</td>
					    <td>${stat.scoreLevel?default(0.0)?string("0.0#")}</td>
					    <#if is73Sub?default("")=='true'><td>${stat.conScore?default(0.0)?string("0.0#")}</td></#if>
					    <td>${stat.gradeRank!}</td>
					    <td>${stat.classRank!}</td>
					</tr>
				</#list>
			</tbody>
		</table>
		</form>
	</div>
</div>
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