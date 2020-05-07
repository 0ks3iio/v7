<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-striped">
	<thead>
		<tr>
			<th>序号</th>
			<th>学生姓名</th>
			<th>学号</th>
			<th>行政班</th>
			<th>是否有导师</th>
			<th>所选导师</th>
		</tr>
	</thead>
	<tbody>
		<#if tutorStuTeaDtos?exists&&tutorStuTeaDtos?size gt 0>
          	<#list tutorStuTeaDtos as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.student.studentName!}</td>
				<td>${item.student.studentCode!}</td>
				<td>${item.student.className!}</td>
				<td><#if item.haveTutor>是<#else>否</#if></td>
				<td>
			<#--<@popup.selectOneTeacher clickId="teaName${item_index+1}" id="teaId${item_index+1}" name="teaName${item_index+1}" handler="saveTeacher(\"${item.student.id!}\",\"teaId${item_index+1}\")">
				</@popup.selectOneTeacher> -->
					<input type="hidden" id="teaId${item_index+1}" value="${item.tutorTeaId!}">
					<input type="hidden" id="stuId${item_index+1}" value="${item.student.id!}">
					<input type="text" id="teaName${item_index+1}" class="form-control" value="${item.tutorTeaName!}" onclick="editStuId('${item_index+1}')">
				</td>
			</tr>
      	    </#list>
  	    <#else>
			<tr>
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if tutorStuTeaDtos?exists&&tutorStuTeaDtos?size gt 0>
	<@htmlcom.pageToolBar container="#showStuList" class="noprint"/>
</#if>

<div style="display: none;">
	<@popup.selectOneTeacher id="teaId" name="teaName" clickId="teaName"  handler='saveTeacher()' >
		<input type="text"  id="teaName"  value="${nameValue!}" />
		<input type="hidden" id="teaId" name="teaId" value="${idValue!}"/>
		<input type="hidden" id="stuId" name="stuId" value="${stuIdValue!}"/>
	</@popup.selectOneTeacher>
</div>
<script>
isSubmit = false;

var index="";
function editStuId(number){
	index=number;
	var teaId=$("#teaId"+number).val();
	var teaName=$("#teaName"+number).val();
	var stuId =$("#stuId"+number).val();
	$('#teaId').val(teaId);
	$('#teaName').val(teaName);
	$('#stuId').val(stuId);
	$('#teaName').click();
}

function saveTeacher(){
	if(isSubmit){
        return;
    }
    var teacherId=$("#teaId").val();
	var studentId=$("#stuId").val();
	
	var teacherName=$("#teaName").val();
	$("#teaName"+index).val(teacherName);
    
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/tutor/result/student/saveTea",
			data:{"teacherId":teacherId,"studentId":studentId},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
					layerTipMsg(data.success,data.msg,"");
    			}
    			isSubmit = false;
				showStuList('1');
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>