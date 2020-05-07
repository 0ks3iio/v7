<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onchange="changeStat()">
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" onchange="changeStat()">
						<option value="1" <#if semester == '1'>selected</#if>>第一学期</option>
						<option value="2" <#if semester == '2'>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">评教类型：</span>
				<div class="filter-content">
					<select class="form-control" id="evaluateType" name="evaluateType" onchange="changeStat()">
						<option value="" >--请选择--</option>
						${mcodeSetting.getMcodeSelect('DM-PJLX',(evaluateType?default(''))?string,'')}
					</select>
				</div>
			</div>
		</div>

		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>序号</th>
							<th>名称</th>
							<th>调查类型</th>
							<th>调查时间</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<#if projectList?exists && projectList?size gt 0>
					<#list projectList as project>
						<tr>
							<td>${project_index+1}</td>
							<td>${project.projectName!}</td>
							<td>${mcodeSetting.getMcode("DM-PJLX","${project.evaluateType!}")}</td>
							<td>${project.beginTime?string('yyyy-MM-dd HH:mm')}  —  ${project.endTime?string('yyyy-MM-dd HH:mm')}</td>
							<td>
								<#if project.beginTime gt .now>
								未开始
							<#elseif project.endTime lt .now>
								已结束
							<#else>
								进行中
							</#if>
							</td>
							<td>
							<#if project.hasStat == '1'>
								<a class="color-orange" href="javascript:void(0);" onclick="statResult('${project.id!}')">重新统计</a>&emsp;
								<a href="javascript:void(0);" onclick="showResultTable('${project.id!}')">查看结果</a>&emsp;
								<#if project.evaluateType == '11'>
								    <a href="javascript:void(0);" onclick="doExportAll1('${project.id!}')">导出结果</a>
								<#elseif project.evaluateType == '12'>
								    <a href="javascript:void(0);" onclick="doExportAll2('${project.id!}')">导出导师结果</a>&emsp;
								    <a href="javascript:void(0);" onclick="doExportAll3('${project.id!}')">导出解答题结果</a>
								</#if>
							<#elseif project.hasStat == '2'>
								<a class="color-orange" href="javascript:void(0);" onclick="statResult('${project.id!}')">重新统计</a>&emsp;
								<a class="color-orange" href="javascript:void(0);">统计中...</a>
							<#else>
								<#if project.endTime lt .now>
								<#if project.hasResult == '1'>
									<a class="color-orange" href="javascript:void(0);" onclick="statResult('${project.id!}')">开始统计</a>
									<#else>
									<p><font color="#FF0000" size="1">无学生提交结果，无法统计</font></p>
									</#if>
								</#if>
							</#if>
							</td>
						</tr>
					</#list>
					</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<script>
function changeStat(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var evaluateType = $('#evaluateType').val();
	var url =  '${request.contextPath}/evaluate/stat/index/page?acadyear='+acadyear+'&semester='+semester+'&evaluateType='+evaluateType;
	$(".model-div").load(url);
}

function showResultTable(projectId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var evaluateType = $('#evaluateType').val();
	var url =  '${request.contextPath}/evaluate/stat/statResult/page?acadyear='+acadyear+'&semester='+semester+'&evaluateType='+evaluateType+'&projectId='+projectId;
	$(".model-div").load(url);
}

function statResult(projectId){
	$.ajax({
		url:"${request.contextPath}/evaluate/stat/statResult",
		data: {'projectId':projectId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layerTipMsg(jsonO.success,jsonO.msg,"");
					$(".model-div").load("${request.contextPath}/evaluate/stat/index/page");
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
}

function doExportAll1(projectId){
    var evaluateType = $('#evaluateType').val();
    var acadyear = $('#acadyear').val();
    document.location.href="${request.contextPath}/evaluate/stat/doBjExportAll?evaluateType="+evaluateType+"&projectId="+projectId+"&acadyear="+acadyear;
}

function doExportAll2(projectId){
    var evaluateType = $('#evaluateType').val();
    var acadyear = $('#acadyear').val();
    document.location.href="${request.contextPath}/evaluate/stat/doDsExportAll1?evaluateType="+evaluateType+"&projectId="+projectId+"&acadyear="+acadyear;
}

function doExportAll3(projectId){
    var evaluateType = $('#evaluateType').val();
    var acadyear = $('#acadyear').val();
    document.location.href="${request.contextPath}/evaluate/stat/doDsExportAll2?evaluateType="+evaluateType+"&projectId="+projectId+"&acadyear="+acadyear;
}
</script>