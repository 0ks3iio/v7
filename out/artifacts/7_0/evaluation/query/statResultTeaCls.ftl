<#import "/fw/macro/popupMacro.ftl" as popup />
<a href="javascript:void(0);" onclick="blackIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>

<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0);" onclick="toClsTeaStat();">按<#if project.evaluateType == '11'>班级<#else>个人</#if>统计</a></li>
		</ul>
		<div class="tab-content" id="print">
			<div class="tab-pane active" role="tabpanel">
				<div class="filter">
				<#if project.evaluateType == '11'>
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<select name="classId" id="classId" class="form-control" onchange="toClsTeaStat1()">
								<option value="">--请选择--</option>
							<#if clslist?exists && clslist?size gt 0>
								<#list clslist as cls>
									<option value="${cls.id!}" <#if cls.id == classId!>selected</#if>>${cls.classNameDynamic!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					<#else>
					<div class="filter-item">
						<span class="filter-name">导师：</span>
						<div class="filter-content">
							<p>${teaName!}</p>
							<input type="hidden" id="teaId" value="${teaId!}"/>
						</div>
					</div>
					</#if>
					<div class="filter-item">
						<span class="filter-name">题型：</span>
						<div class="filter-content">
							<select name="itemType" id="itemType" class="form-control" onchange="toClsTeaStat1()">
								<option value="0" <#if itemType == '0'>selected</#if>>选择题</option>
								<option value="1" <#if itemType == '1'>selected</#if>>解答题</option>
							</select>
						</div>
					</div>
					<#if project.evaluateType == '11'>
					<div class="filter-item">
						<span class="filter-name">班主任：</span>
						<div class="filter-content">
							<p>${teacherName!}</p>
						</div>
					</div>
					</#if>
					<div class="filter-item">
						<span class="filter-name">提交学生数：</span>
						<div class="filter-content">
							<p>${stuCount!}</p>
						</div>
					</div>
					<#if itemType=='0'>
					<div class="filter-item">
						<span class="filter-name">总分：</span>
						<div class="filter-content">
							<p>${countScore?string('0.##')}</p>
						</div>
					</div>
					</#if>
					<div class="filter-item filter-item-right">
					<button class="btn btn-blue" onclick="doExport();">导出</button>
				</div>
				</div>
				<#if itemType == '0'>
				<div class="table-container">
					<div class="table-container-body">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>项目名称</th>
									<th>选项</th>
									<th>比例（%）</th>
									<th>得分</th>
								</tr>
							</thead>
							<tbody>
							<#assign i = 0>
							<#if dto.evaluaList?size gt 0>
								<#list dto.evaluaList as eva>
									<tr>
										<td rowspan="${eva.optionList?size}">${i+1}<#assign i = i + 1></td>
										<td rowspan="${eva.optionList?size}">${eva.itemName!}</td>
										<td>${eva.optionList[0].optionName!}</td>
										<td>${(clsItemScoreMap[eva.optionList[0].id]?default(0))?string('0.##')}</td>
										<td rowspan="${eva.optionList?size}">${(clsItemScoreMap[eva.id]?default(0))?string('0.##')}</td>
									</tr>
									<#list eva.optionList as option>
											<#if option_index gt 0>
											<tr>
												<td>${option.optionName!}</td>
												<td>${(clsItemScoreMap[option.id]?default(0))?string('0.##')}</td>
											</tr>
											</#if>
										</#list>
								</#list>
							</#if>
							<!--满意度-->
							<#if dto.fillList?size gt 0>
								<#list dto.fillList as fill>
									<tr>
										<td rowspan="${fill.optionList?size}">${i+1}<#assign i = i + 1></td>
										<td rowspan="${fill.optionList?size}">${fill.itemName!}</td>
										<td>${fill.optionList[0].optionName!}</td>
										<td colspan="2">${(clsItemScoreMap[fill.optionList[0].id]?default(0))?string('0.##')}</td>
									</tr>
									<#list fill.optionList as option>
											<#if option_index gt 0>
											<tr>
												<td>${option.optionName!}</td>
												<td colspan="2">${(clsItemScoreMap[option.id]?default(0))?string('0.##')}</td>
											</tr>
											</#if>
										</#list>
								</#list>
							</#if>
							</tbody>
						</table>
					</div>
				</div>
			<#else>
				<div class="table-container">
					<div class="table-container-body">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<#--<th>学生</th>
									<th>学号</th>-->
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
												<td>${txtDto.itemTxtMap[answer.id]?default('')}</td>
											</#list>
										</#if>
									</tr>
									</#list>
									</#if>
							</tbody>
						</table>
					</div>
				</div>
				</#if>
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
	LODOP.SAVE_TO_FILE("${project.projectName!}<#if project.evaluateType == '11'>班级<#else>个人</#if>数据表"+".xls");
}

function blackIndex(){
	var url =  '${request.contextPath}/evaluate/query/index/page?acadyear=${project.acadyear!}&semester=${project.semester!}&evaluateType=${evaluateType!}';
	$(".model-div").load(url);
}
function toClsTeaStat(){
	var url =  '${request.contextPath}/evaluate/query/statTeaClsResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}';
	$(".model-div").load(url);
}

function toClsTeaStat1(){
<#if project.evaluateType == '11'>
	var classId = $('#classId').val();
	var itemType = $('#itemType').val();
	var url =  '${request.contextPath}/evaluate/query/statTeaClsResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}&classId='+classId+'&itemType='+itemType;
	$(".model-div").load(url);
	<#else>
	var teaId = $('#teaId').val();
	var itemType = $('#itemType').val();
	var url =  '${request.contextPath}/evaluate/query/statTeaClsResult/page?evaluateType=${evaluateType!}&projectId=${project.id!}&teaId='+teaId+'&itemType='+itemType;
	$(".model-div").load(url);
	</#if>
}
</script>
