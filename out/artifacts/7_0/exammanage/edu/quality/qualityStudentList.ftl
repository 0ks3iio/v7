<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="qualityForm">
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center" style="width:5%">序号</th>
 			<th class="text-center" style="width:10%">考号</th>
			<th class="text-center" style="width:15%">学校</th>
			<th class="text-center" style="width:10%">姓名</th>
			<th class="text-center" style="width:10%">学籍号</th>
			<th class="text-center" style="width:10%">身份证号</th>
			<th class="text-center" style="width:5%">性别</th>
 			<th class="text-center" style="width:20%">操行评语</th>
		</tr>
	</thead>
	<tbody>
		<#if qualityList?exists && (qualityList?size > 0)>
			<#list qualityList as dto>
				<tr>
					<#if dto.student?exists>
					<td class="text-center">${dto_index+1!}</td>
					<td class="text-center">${dto.examCode!}</td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.student.studentName!}</td>
					<td class="text-center">${dto.student.unitiveCode!}</td>
					<td class="text-center">${dto.student.identityCard!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</td>
					<td class="text-center">
					<input id="dtos[${dto_index}].evaluation" name="dtos[${dto_index}].evaluation" maxLength="200" type="text" nullable="true" class="form-control" value="${dto.evaluation!}"/>
					<input type="hidden" name="dtos[${dto_index}].schoolId" value="${dto.schoolId!}"/>
					<input type="hidden" name="dtos[${dto_index}].studentId" value="${dto.student.id!}"/>
					<input type="hidden" name="dtos[${dto_index}].examId" value="${dto.examId!}"/>
					</td>
					<#else>
						<td colspan="8" class="text-center">该学生已删除</td>
					</#if>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="8" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
</form>
<#-- if qualityList?exists&&qualityList?size gt 0>
		<@htmlcom.pageToolBar container="#stuList" class=""/>
</#if-->
<script>


var isSubmit = false;
function save(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var examId = $("#examId").val();
	var acadyear = $("#acadyear").val();
	var semester = $("#searchSemester").val();
	var options = {
		url : "${request.contextPath}/exammanage/edu/quality/save?acadyear="+acadyear+"&semester="+semester+"&examId="+examId,
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit = false;
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	setTimeout(showStuList(),500);
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#qualityForm").ajaxSubmit(options);	
}

</script>