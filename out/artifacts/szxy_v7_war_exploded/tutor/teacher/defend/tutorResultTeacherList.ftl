<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>教师姓名</th>
				<th>人员情况</th>
				<th>所导学生</th>
			</tr>
		</thead>
		<tbody>
				<#if teaStuDtos?exists&&teaStuDtos?size gt 0>
		          	<#list teaStuDtos as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.teacherName!}</td>
						<td><b class="color-red">${item.studentNum!}</b>/${maxStuNum!}</td>
						<td>
					<#--  	<@popup.selectMoreStudent clickId="stuName${item_index+1}" id="stuIds${item_index+1}" name="stuName${item_index+1}" handler="saveStudent(\"${item.teacherId!}\",\"stuIds${item_index+1}\")">
						</@popup.selectMoreStudent> -->
							<input type="hidden" id="stuIds${item_index+1}" value="${item.studentIds!}">
							<input type="hidden" id="teaIds${item_index+1}" value="${item.teacherId!}">
							<input type="text" id="stuName${item_index+1}" class="form-control" value="${item.studentNames!}" onclick="editStuId('${item_index+1}')">
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
</div>
<#if teaStuDtos?exists&&teaStuDtos?size gt 0>
	<@htmlcom.pageToolBar container="#showTeaList" class="noprint"/>
</#if>
<div style="display: none;">
	<@popup.selectMoreStudent id="stuIds" name="stuName" clickId="stuName"  handler='saveStudent()' >
		<input type="text"  id="stuName"  value="${nameValue!}" />
		<input type="hidden" id="stuIds" name="stuIds" value="${idValue!}"/>
		<input type="hidden" id="teaIds" name="teaIds" value="${teacherIdValue!}"/>
	</@popup.selectMoreStudent>
</div>
<script>
isSubmit = false;


var index="";
function editStuId(number){
	index=number;
	var studentId=$("#stuIds"+number).val();
	var studentName=$("#stuName"+number).val();
	var teacherId =$("#teaIds"+number).val();
	$('#stuName').val(studentName);
	$('#stuIds').val(studentId);
	$('#teaIds').val(teacherId);
	$('#stuName').click();
}


function saveStudent(){
	if(isSubmit){
        return;
    }
    var teacherId=$("#teaIds").val();
	var stuIds=$("#stuIds").val();
	var tutorId = $("#tutorName").val();
	
	var studentName=$("#stuName").val();
	$("#stuName"+index).val(studentName);
	
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/tutor/result/teacher/savestu",
			data:{"teacherId":teacherId,"stuIds":stuIds,"tutorId":tutorId},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
					layerTipMsg(data.success,data.msg,"");
    			}
				showTeaList();
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>