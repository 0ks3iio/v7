<#if lastList?exists && lastList?size gt 0>
	<div class="table-container">
		<div class="table-container-body" style="overflow-x: auto;">
			<form class="print">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>学科</th>
						<th>年级参考人数</th>
						<th>满分值</th>
						<th>年级考试平均分</th>
						<th>难度系数</th>
						<th>考试最高分</th>
						<th>考试最低分</th>
						<th>标准差</th>
						<th>优秀人数</th>
						<th>不及格人数</th>
					</tr>
				</thead>
				<tbody>
					<#list lastList as item>
		 				<tr>
						    <td>${item_index+1}</td>
						    <td>${item.subjectName!}</td>
						    <td>${item.statNum!}</td>
						    <td>${item.fullScore?default(0.0)?string("0.#")}</td>
						    <td>${item.avgScore?default(0.0)?string("0.#")}</td>
						    <td>${(item.avgScore/item.fullScore)?default(0.0)?string("0.0#")}</td>
						    <td>${item.maxScore?default(0.0)?string("0.#")}</td>
						    <td>${item.minScore?default(0.0)?string("0.#")}</td>
						    <td>${item.normDeviation?default(0.0)?string("0.#")}</td>
						    <td>${item.goodNumber!}(${(item.goodNumber/item.statNum*100)?default(0.0)?string("0.#")}%)</td>
						    <td>${item.failedNumber!}(${(item.failedNumber/item.statNum*100)?default(0.0)?string("0.#")}%)</td>
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
function doExportTotal(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	var acadyear = $('#acadyear').val();
	LODOP.SAVE_TO_FILE('${title!}'+".xls");
}
</script>