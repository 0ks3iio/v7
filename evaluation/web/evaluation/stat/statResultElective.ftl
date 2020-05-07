<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${project.projectName!}</h4>
	</div>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="doExport();">导出</button>
			</div>
		</div>
		<#assign i = 1>
		<div class="table-container" id="dtolist">
			<div class="table-container-body" id="print">
				<table id="exampleElective" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th rowspan="3" >序号</th>
							<th rowspan="3" >课程名称</th>
							<th rowspan="3" >任课教师</th>
							<#if dto.evaluaList?size gt 0>
							<th colspan="${dto.evaluaList?size + 1}" class="text-center">评教项目</th>
							</#if>
							<#if dto.fillList?size gt 0>
								<#assign optionNum = 0>
								<#list dto.fillList as fill>
									<#assign optionNum = optionNum + fill.optionList?size>
								</#list>
							<th colspan="${optionNum}" class="text-center">满意度项目</th>
							</#if>
							<th rowspan="3" class="text-center noprint">查看详情</th>
						</tr>
						<tr>
							<#if dto.evaluaList?size gt 0>
								<#list dto.evaluaList as eva>
								<th class="js-wordwrap" width="80" rowspan="2">${i}、${eva.itemName!}</th>
								<#assign i = i+1>
								</#list>
								<th rowspan="2">总分</th>
							</#if>
							<#if dto.fillList?size gt 0>
							<#list dto.fillList as fill>
								<th colspan="${fill.optionList?size}" class="text-center">${i}、${fill.itemName!}</th>
								<#assign i = i+1>
							</#list>
							</#if>
						</tr>
						<tr>
						<#if dto.fillList?size gt 0>
						<#list dto.fillList as fill>
							<#list fill.optionList as option>
								<th>${option.optionName}(%)</th>
							</#list>
						</#list>
						</#if>
						</tr>
					</thead>
					<tbody>
					<#if dtolist?? && dtolist?size gt 0>
						<#list dtolist as dto1>
						<tr style="height: 57px;">
							<td>${dto1_index+1}</td>
							<td>${dto1.subName!}</td>
							<td>${dto1.teaName!}</td>
							<#if dto.evaluaList?size gt 0>
								<#list dto.evaluaList as eva>
								<td>${dto1.itemScoreMap[eva.id]?default(0)?string('0.##')}
								</td>
								</#list>
								<td>${dto1.countScore?string('0.##')}</td>
							</#if>
							<#if dto.fillList?size gt 0>
								<#list dto.fillList as fill>
									<#list fill.optionList as option>
										<td>${dto1.itemScoreMap[option.id]?default(0)?string('0.##')}</td>
									</#list>
								</#list>
							</#if>
							<td class="noprint" ><a href="javascript:void(0)" onclick="toResultTxt('${dto1.teaId!}');">查看</a></td>
						</tr>
						</#list>
					</#if>
					</tbody>
				</table>
			</div><#-- 
			<#if dtolist?? && dtolist?size gt 0>
			<@htmlcom.pageToolBar container="#dtolist" class="noprint">
			</@htmlcom.pageToolBar>
			</#if> -->
		</div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("#print")));
	LODOP.SAVE_TO_FILE("${project.projectName}统计数据表"+".xls");
}
function blackIndex(){
	//table.destroy(); //清空一下table
	var url =  '${request.contextPath}/evaluate/stat/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}';
	$(".model-div").load(url);
}
function toResultTxt(teaId){
	if(teaId && teaId != ""){
		var url =  '${request.contextPath}/evaluate/stat/statResultTxt/page?teaId='+teaId+'&evaluateType=${evaluateType!}&projectId=${project.id!}';
	}
	$(".model-div").load(url);
}
		// 文本断行
		$('.js-wordwrap').each(function(){
			var s = $(this).text().split('');
			for(var i=0; i<s.length; i++){
				if(i > 0 && i%5 == 0){
					s.splice(i, 0, '<br>');
				}
			}
			$(this).html(s.join(''))
		});
		var table = $('#exampleElective').DataTable({
	        scrollY: "300px",
			info: false,
			searching: false,
			autoWidth: false,
	        scrollX: true,
	        sort: false,
	        scrollCollapse: true,
	        paging: false,
	        oLanguage:{sEmptyTable:"无数据!"},
	        fixedColumns: {
	            leftColumns: 3,
	            rightColumns: 1
	        }
	    });
	   <#--  new $.fn.dataTable.FixedColumns( table, {
		    leftColumns: 3,
	            rightColumns: 1
		}); -->
</script>