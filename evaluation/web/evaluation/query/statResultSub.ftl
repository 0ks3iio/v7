<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
<div class="box-body">
	<ul class="nav nav-tabs nav-tabs-1">
		<li role="presentation"><a href="javascript:void(0);" onclick="blackTable();">按班级统计</a></li>
		<li class="active" role="presentation"><a href="javascript:void(0);" onclick="toResultSub();">按学科统计</a></li>
	</ul>
	<div class="filter baseData-inner-wrap">
		<div class="filter-item">
				<span class="filter-name">科目${evaluateType!}：</span>
				<div class="filter-content">
					<select name="subId" id="subId" class="form-control" onchange="toResultSub()">
						<option value="">--请选择--</option>
						<#list courseList as sub>
						<option value="${sub.id!}" <#if sub.id==subId?default("")>selected="selected"</#if>>${sub.subjectName!}</option>
						</#list>
					</select>
				</div>
			</div>
			<input type="hidden" id="teacherId" value="${teacherId!}"/>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="doExport();">导出</button>
			</div>
		</div>
<#assign i = 1>
	<div class="table-container">
		<div class="table-container-body" id="print">
			<table id="exampleSub" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="text-center" rowspan="3">&emsp;&emsp;姓名&emsp;&emsp;</th>
						<th class="text-center" rowspan="3">&emsp;&emsp;科目&emsp;&emsp;</th>
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
					</tr>
					<tr>
						<#if dto.evaluaList?size gt 0>
							<#list dto.evaluaList as eva>
							<th rowspan="2" class="js-wordwrap">${i}、${eva.itemName!}</th>
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
					<#if subDtoList?? && subDtoList?size gt 0>
						<#list subDtoList as dto1>
						<tr style="height: 57px;">
							<td>${dto1.teaName!}</td>
							<td>${dto1.subName!!}</td>
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
						</tr>
						</#list>
					</#if>
				</tbody>
			</table>
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
	LODOP.SAVE_TO_FILE("${project.projectName}按学科统计数据表"+".xls");
}

function blackIndex(){
	var url =  '${request.contextPath}/evaluate/query/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}';
	$(".model-div").load(url);
}
function toResultSub(){
	var subId = $('#subId').val();
	var teacherId = $('#teacherId').val();
	var url =  '${request.contextPath}/evaluate/query/statResultSub/page?evaluateType=${evaluateType!}&projectId=${project.id!}&subjectId=${subjectId!}&classId=${classId!}&subId='+subId+'&teacherId='+teacherId;
	$(".model-div").load(url);
}
function blackTable(){
	var url =  '${request.contextPath}/evaluate/query/statResult/page?subjectId=${subjectId!}&classId=${classId!}&evaluateType=${evaluateType!}&projectId=${projectId!}';
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
	$('#exampleSub').DataTable({
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
            leftColumns: 1
        }
    });
});
</script>

