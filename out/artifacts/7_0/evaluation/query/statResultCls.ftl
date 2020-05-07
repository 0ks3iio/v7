<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div id="dtolist">
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1">
			<li class="active" role="presentation"><a href="javascript:void(0);" onclick="searTable();">按班级统计</a></li>
			<li role="presentation"><a href="javascript:void(0);" onclick="toResultSub();">按学科统计</a></li>
		</ul>
		<div class="filter baseData-inner-wrap">
			<div class="filter-item">
				<span class="filter-name">科目：</span>
				<div class="filter-content">
					<select name="subjectId" id="subjectId" class="form-control" onchange="searTable()">
						<option value="">--请选择--</option>
						<#list courseList as sub>
						<option value="${sub.id!}" <#if sub.id==subjectId?default("")>selected="selected"</#if>>${sub.subjectName!}</option>
						</#list>
					</select>
				</div>
			</div>
			 <div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" onchange="searTable()">
						<option value="">--请选择--</option>
						<#list classes as cls>
						<option value="${cls.id!}" <#if cls.id==classId?default("")>selected="selected"</#if>>${cls.classNameDynamic!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="doExport();">导出</button>
			</div>
		</div>
		<#assign i = 1>
		<div class="table-container">
			<div class="table-container-body" id="print">
				<table id="exampleCls" class="table table-bordered table-striped text-center">
					<thead>
						<tr>
							<th rowspan="3" class="text-center">序号</th>
							<th rowspan="3" class="text-center">教师姓名</th>
							<th rowspan="3" class="text-center">科目</th>
							<th rowspan="3" class="text-center">班级</th>
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
								<th class="js-wordwrap"  rowspan="2">${i}、${eva.itemName!}</th>
								<#assign i = i+1>
								</#list>
								<th rowspan="2">总分</th>
							</#if>
							<#if dto.fillList?size gt 0>
							<#list dto.fillList as fill>
								<th colspan="${fill.optionList?size}" class="text-center" >${i}、${fill.itemName!}</th>
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
							<td>${dto1.teaName!}</td>
							<td>${dto1.subName!}</td>
							<td>${dto1.clsName!}</td>
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
							<td class="noprint" >
								<a href="javascript:void(0)" onclick="toResultTxt('${dto1.teaId!}','${dto1.subId!}','${dto1.clsId!}');">查看</a>
							</td>
						</tr>
						</#list>
					</#if>
					</tbody>
				</table>
			</div>
			<#-- 
			<#if dtolist?? && dtolist?size gt 0>
			<@htmlcom.pageToolBar container="#dtolist" class="noprint">
			</@htmlcom.pageToolBar>
			</#if> -->
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
	LODOP.SAVE_TO_FILE("${project.projectName}按班级统计数据表"+".xls");
}

function blackIndex(){
	var url =  '${request.contextPath}/evaluate/query/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}';
	$(".model-div").load(url);
}
function toResultSub(){
	var subjectId = $('#subjectId').val();
	var classId = $('#classId').val();
	var url =  '${request.contextPath}/evaluate/query/statResultSub/page?evaluateType=${evaluateType!}&projectId=${project.id!}&subjectId='+subjectId+'&classId='+classId;
	$(".model-div").load(url);
}

function toResultTxt(teaId,subId,clsId){
	var subjectId = $('#subjectId').val();
	var classId = $('#classId').val();
	if(teaId && teaId != ""){
		var url =  '${request.contextPath}/evaluate/query/statResultTxt/page?subjectId='+subjectId+'&teaId='+teaId+'&subId='+subId+'&clsId='+clsId+'&classId='+classId+'&evaluateType=${evaluateType!}&projectId=${project.id!}';
	}
	$(".model-div").load(url);
}

function searTable(){
	var subjectId = $('#subjectId').val();
	var classId = $('#classId').val();
	var url =  '${request.contextPath}/evaluate/query/statResult/page?subjectId='+subjectId+'&classId='+classId+'&evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}
$(function(){
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
		$('#exampleCls').DataTable({
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
	            leftColumns: 4,
	            rightColumns: 1
	        }
	    });
	    });
</script>
</div>