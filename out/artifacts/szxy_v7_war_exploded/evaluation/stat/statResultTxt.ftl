<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${project.projectName!}</h4>
	</div>
	<div class="box-body">
		<div class="filter">
	<#-- if projectType?exists && projectType == '10'>
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
					<@popup.selectOneClass clickId="className" id="classId" name="className" handler="searTable()">
						<input type="text" class="form-control" id="className" value="${className!}"/>
						<input type="hidden" id="classId" value="${classId!}"/>
					</@popup.selectOneClass>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<@popup.selectOneTeacher clickId="teaName" id="teaId" name="teaName" handler="searTable()">
						<input type="text" class="form-control" id="teaName" value="${teaName!}"/>
						<input type="hidden" id="teaId" value="${teaId!}"/>
					</@popup.selectOneTeacher>
				</div>
		</#if-->
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="doExport();">导出</button>
			</div>
			</div>
		</div>
		<div class="table-container">
			<div class="table-container-body" id="print">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<#-- <th>学生</th>
							<th>学号</th> -->
							<#if dto.answerList?size gt 0>
								<#list dto.answerList as answer>
									<th>${answer_index + 1}、${answer.itemName!}</th>
								</#list>
							</#if>
						</tr>
					</thead>
					<tbody>
					<#if txtDtolist?exists && txtDtolist?size gt 0>
					<#list txtDtolist as txtDto>
					<tr>
						<#-- <td>${txtDto.stuName!}</td>
						<td>${txtDto.stuCode!}</td> -->
						<#if dto.answerList?size gt 0>
							<#list dto.answerList as answer>
								<td>${txtDto.itemTxtMap[answer.id]}</td>
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
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>

function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("#print")));
	LODOP.SAVE_TO_FILE("${project.projectName}文本数据表"+".xls");
}

function blackIndex(){
<#if projectType?exists && projectType == '10'>
	<#-- var url =  '${request.contextPath}/evaluate/stat/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}'; -->
	var url =  '${request.contextPath}/evaluate/stat/statResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}&subjectId=${subjectId!}&classId=${classId!}&teaId=${teacherId!}';
	<#else>
	var url =  '${request.contextPath}/evaluate/stat/statResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	</#if>
	
	$(".model-div").load(url);
}
<#if projectType?exists && projectType == '10'>
function searTable(){
	var subjectId = $('#subjectId').val();
	var classId = $('#classId').val();
	var teaId = $('#teaId').val();
	if(teaId && teaId != ""){
		var url =  '${request.contextPath}/evaluate/stat/statResultTxt/page?subjectId='+subjectId+'&teaId='+teaId+'&classId='+classId+'&evaluateType=${evaluateType!}&projectId=${project.id!}';
	}else{
		var url =  '${request.contextPath}/evaluate/stat/statResult/page?subjectId='+subjectId+'&classId='+classId+'&evaluateType=${evaluateType!}&projectId=${project.id!}';
	}
	$(".model-div").load(url);
}
</#if>
</script>