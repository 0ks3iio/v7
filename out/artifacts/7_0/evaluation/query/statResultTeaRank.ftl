<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1">
			<li role="presentation"><a href="javascript:void(0);" onclick="toSchTeaStat();">整体情况统计</a></li>
			<li role="presentation"><a href="javascript:void(0);" onclick="toClsTeaStat();">按<#if project.evaluateType == '11'>班级<#else>个人</#if>统计</a></li>
			<li class="active" role="presentation"><a href="javascript:void(0);" onclick="toTeaRankStat();">教师排名</a></li>
		</ul>
		<div class="filter baseData-inner-wrap">
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="doExport();">导出</button>
			</div>
		</div>
				<div class="table-container">
					<div class="table-container-body" id="print">
						<table id="exampleTea" class="table table-bordered table-striped text-center">
							<thead>
								<tr>
									<#if project.evaluateType == '11'>
									<th class="text-center">班级</th>
									</#if>
									<th class="text-center">教师</th>
									<th class="text-center">总分</th>
									<th class="text-center">排名</th>
								</tr>
							</thead>
							<tbody>
								<#if dtolist?exists && dtolist?size gt 0>
								<#list dtolist as dto>
								<tr>
								<#if project.evaluateType == '11'>
								<td>${dto.clsName!}</td>
								</#if>
								<td>${dto.teaName!}</td>
								<td>${dto.countScore?string('0.##')}</td>
								<td>${dto.rank!}</td>
								</tr>
								</#list>
								</#if>
							</tbody>
						</table>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
function blackIndex(){
	var url =  '${request.contextPath}/evaluate/query/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}';
	$(".model-div").load(url);
}

function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("#print")));
	LODOP.SAVE_TO_FILE("${project.projectName!}教师排名表"+".xls");
}
function toSchTeaStat(){
	var url =  '${request.contextPath}/evaluate/query/statResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}
function toClsTeaStat(){
	var url =  '${request.contextPath}/evaluate/query/statTeaClsResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}
</script>