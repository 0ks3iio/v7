<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1">
			<li class="active" role="presentation"><a href="javascript:void(0);" onclick="toSchTeaStat();">整体情况统计</a></li>
			<li role="presentation"><a href="javascript:void(0);" onclick="toClsTeaStat();">按<#if project.evaluateType == '11'>班级<#else>个人</#if>统计</a></li>
			<li role="presentation"><a href="javascript:void(0);" onclick="toTeaRankStat();">教师排名</a></li>
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
									<th rowspan="2" class="text-center">序号</th>
									<th rowspan="2" class="text-center">项目名称</th>
									<th rowspan="2" class="text-center">选项</th>
									<#if gradelist?exists && gradelist?size gt 0>
									<th colspan="2" class="text-center">全校情况</th>
									<#list gradelist as grade>
									<th colspan="2" class="text-center">${grade.gradeName!}</th>
									</#list>
									</#if>
								</tr>
								<tr> 
									<#if gradelist?exists && gradelist?size gt 0>
									<th class="text-center">比例（%）</th>
									<th class="text-center">得分</th>
									<#list gradelist as grade>
										<th class="text-center">比例（%）</th>
										<th class="text-center">得分</th>
									</#list>
									</#if>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="3">总分</td>
									<td style="display:none">1</td>
									<td style="display:none">1</td>
									<#if gradelist?exists && gradelist?size gt 0>
										<td colspan="2">${(gradeItemScoreMap['school,countScore']?default(0))?string('0.##')}</td>
										<td style="display:none">1</td>
										<#list gradelist as grade>
											<td colspan="2">
												${(gradeItemScoreMap[grade.id+',countScore']?default(0))?string('0.##')}
											</td>
											<td style="display:none">1</td>
										</#list>
									</#if>
								</tr>
								<!--评教项目-->
								<#assign i = 0>
								<#if dto?exists && dto.evaluaList?size gt 0>
									<#list dto.evaluaList as eva>
									<tr>
										<td rowspan="${eva.optionList?size!}">${i+1}<#assign i = i + 1></td>
										<td rowspan="${eva.optionList?size!}">${eva.itemName}</td>
										<td>${eva.optionList[0].optionName!}</td>
										<#if gradelist?exists && gradelist?size gt 0>
											<td>${(gradeItemScoreMap['school,'+eva.optionList[0].id]?default(0))?string('0.##')}</td>
											<td rowspan="${eva.optionList?size!}">${(gradeItemScoreMap['school,'+eva.id]?default(0))?string('0.##')}</td>
											<#list gradelist as grade>
												<td>${(gradeItemScoreMap[grade.id+','+eva.optionList[0].id]?default(0))?string('0.##')}</td>
												<td rowspan="${eva.optionList?size!}">${(gradeItemScoreMap[grade.id+','+eva.id]?default(0))?string('0.##')}</td>
											</#list>
										</#if>
									</tr>
										<#list eva.optionList as option>
											<#if option_index gt 0>
											<tr>
												<td style="display:none">1</td>
												<td style="display:none">1</td>
												<td>${option.optionName!}</td>
												<#if gradelist?exists && gradelist?size gt 0>
												<td>${(gradeItemScoreMap['school,'+option.id]?default(0))?string('0.##')}</td>
												<td style="display:none">1</td>
												<#list gradelist as grade>
													<td>${(gradeItemScoreMap[grade.id+','+option.id]?default(0))?string('0.##')}</td>
													<td style="display:none">1</td>
												</#list>
												</#if>
											</tr>
											</#if>
										</#list>
									</#list>
								</#if>
								
								<!--满意度-->
								<#if dto?exists && dto.fillList?size gt 0>
									<#list dto.fillList as fill>
									<tr>
										<td rowspan="${fill.optionList?size!}">${i+1}<#assign i = i + 1></td>
										<td rowspan="${fill.optionList?size!}">${fill.itemName}</td>
										<td>${fill.optionList[0].optionName!}</td>
										<#if gradelist?exists && gradelist?size gt 0>
											<td colspan="2">${(gradeItemScoreMap['school,'+fill.optionList[0].id]?default(0))?string('0.##')}</td>
											<td style="display:none">1</td>
											<#list gradelist as grade>
												<td colspan="2">${(gradeItemScoreMap[grade.id+','+fill.optionList[0].id]?default(0))?string('0.##')}</td>
												<td style="display:none">1</td>
											</#list>
										</#if>
									</tr>
										<#list fill.optionList as option>
											<#if option_index gt 0>
											<tr>
												<td style="display:none">1</td>
												<td style="display:none">1</td>
												<td>${option.optionName!}</td>
												<#if gradelist?exists && gradelist?size gt 0>
												<td colspan="2">${(gradeItemScoreMap['school,'+option.id]?default(0))?string('0.##')}</td>
												<td style="display:none">1</td>
												<#list gradelist as grade>
													<td colspan="2">${(gradeItemScoreMap[grade.id+','+option.id]?default(0))?string('0.##')}</td>
													<td style="display:none">1</td>
												</#list>
												</#if>
											</tr>
											</#if>
										</#list>
									</#list>
								</#if>
								
								
							</tbody>
						</table>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
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
	LODOP.SAVE_TO_FILE("${project.projectName!}整体情况数据表"+".xls");
}
function toSchTeaStat(){
	var url =  '${request.contextPath}/evaluate/query/statResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}
function toClsTeaStat(){
	var url =  '${request.contextPath}/evaluate/query/statTeaClsResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}
function toTeaRankStat(){
	var url =  '${request.contextPath}/evaluate/query/statTeaRankResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}

	$('#exampleTea').DataTable({
        scrollY: "380px",
		info: false,
		searching: false,
		autoWidth: false,
        scrollX: true,
        sort: false,
        scrollCollapse: true,
        paging: false,
        oLanguage:{sEmptyTable:"无数据!"},
        fixedColumns: {
            leftColumns: 3
        }
    });
</script>