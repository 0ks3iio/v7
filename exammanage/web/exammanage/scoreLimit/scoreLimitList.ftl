<#import "/fw/macro/popupMacro.ftl" as popupMacro>

<@popupMacro.selectMoreTeacher clickId="searchTeacherName" id="searchTeacherId" name="searchTeacherName" handler="saveTeacher()">
	<div class="filter">
		<div class="filter-item">
			<input type="hidden" id="searchTeacherId" value="">
			<input type="hidden" id="searchTeacherName" value="">
			<button class="btn btn-blue btn-sm" id="teacherIdBtn" onclick="addTeacherIdToClass()">批量添加</button>
		</div>
	</div>
</@popupMacro.selectMoreTeacher>
<input type="hidden" id="searchSubjectId" value='${searchSubjectId!}'>
<table class="table table-bordered table-striped table-hover no-margin">
    <thead>
		<tr>
		    <td width="50"><label><input type="checkbox" class="wp" name="allCheckClassId" id="allCheckClassId" onChange="checkboxAllSelect()">
		    	<span class="lbl"></span></label>
		    </td>
			<td width="100"><b>班级名称</b></td>
			<td ><b>录分人员</b></td>
			<td width="100"><b>操作</b></td>
			<td width="19"></td>
		</tr>
	</thead>
</table>
<div style="height: 508px;overflow-y: scroll;">
	<table class="table table-bordered table-striped table-hover no-margin"  id="limit1table">
        <tbody>	
        <#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as item>
		    <tr name="tr_${item_index}">
				<td width="50"><label><input type="checkbox" name="checkClassId"  class="wp checkboxOneClass" id="classId${item_index}" value="${item.classId!}" data-value="${item.classType!}">
					<span class="lbl"></span></label>
				</td>
				<td width="100">${item.className!}</td>
				<#assign teacherMap=item.teacherMap>
				<#assign teaId="">
				<#assign teaName="">
				<td>
					<#if teacherMap?exists && teacherMap?size gt 0>
					<div class="mb-5">
						<#list teacherMap?keys as key>
						<span>
							<button class="btn btn-sm btn-white mb5 deleteLimitOne" data-classId="${item.classId!}" data-teacherId="${key!}">${teacherMap[key]!}<span>×</span></button>
						</span>
							<#if teaId == "">
								<#assign teaId=key>
								<#assign teaName=teacherMap[key]>
							<#else>
								<#assign teaId=teaId + "," + key>
								<#assign teaName=teaName + "," + teacherMap[key]>
							</#if>
						</#list>
					</div>
					</#if>
				</td>
				<td width="100">
					<input type="hidden" id="teaId${item_index}" value="${teaId!}">
					<input type="hidden" id="teaName${item_index}" value="${teaName!}">
					<a class="color-blue" href="javascript:;" onClick="addTeaBth('${item_index}')">添加人员</a>
				</td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	var isSingeBth = false;
	var saveType = 0;
	var indexTr = 0;
	function addTeaBth(index){
		if(isSingeBth){
			return;
		}
		isSingeBth=true;
		var teaId = $('#teaId'+index).attr('value');
		var teaName = $('#teaName'+index).attr('value');
		$('#searchTeacherId').attr('value',teaId);
		$('#searchTeacherName').attr('value',teaName);
		saveType = 1;
		indexTr = index;
		$("#searchTeacherName").click();
		isSingeBth=false;
	}
	var isAddBtn=false;
	function addTeacherIdToClass(){
		if(isAddBtn){
			return;
		}
		isAddBtn=true;
		var length=$("input[name='checkClassId']:checked").length;
		if(length<=0){
			//没有选中
			layer.alert('请选择班级',{icon:7});
			isAddBtn=false;
			return;
		}
		saveType = 0;
		$("#searchTeacherName").click();
	}
	function saveTeacher(){
		if(saveType == 1){
			saveOneTeacherToClass();
		}else{
			saveAllTeacherToClass();
		}
	}
	
	function saveOneTeacherToClass(){
		var classIds = $('#classId'+indexTr).attr('value');
		var classTypes = $('#classId'+indexTr).attr('data-value');
		var teacherIds=$("#searchTeacherId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var searchSubjectId=$("#searchSubjectId").val();
		var teaId = $('#teaId'+indexTr).attr('value');
		$.ajax({
			url:"${request.contextPath}/exammanage/scoreLimit/saveBySubject",
			data:{acadyear:acadyear,semester:semester,examId:examId,subjectId:searchSubjectId,classIds:classIds,teacherIds:teacherIds,classTypes:classTypes,teaId:teaId},
			type:"post",
			dataType: "json",
			success: function(data){
				if(data.success){
	    			// 显示成功信息
		 			layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
					showList(searchSubjectId);
		 		}else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			isAddBtn = false;
		 		}	
			}
		});
		
	}
	
	function saveAllTeacherToClass(){
		var classIds="";
		var classTypes="";
		$("input[name='checkClassId']:checked").each(function(){
			var classId=$(this).val();
			classIds=classIds+","+classId;
			var classType=$(this).attr("data-value");
			classTypes=classTypes+","+classType;
			
		});
		if(classIds!=""){
			classIds=classIds.substring(1);
		}else{
			layer.alert('请选择班级',{icon:7});
			isAddBtn = false;
			return;
		}
		if(classTypes!=""){
			classTypes=classTypes.substring(1);
		}
		var teacherIds=$("#searchTeacherId").val();
		if(teacherIds==""){
			//没有选中
			layer.alert('请选择教师',{icon:7});
			isAddBtn = false;
			return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var searchSubjectId=$("#searchSubjectId").val();
		$.ajax({
			url:"${request.contextPath}/exammanage/scoreLimit/saveBySubject",
			data:{acadyear:acadyear,semester:semester,examId:examId,subjectId:searchSubjectId,classIds:classIds,teacherIds:teacherIds,classTypes:classTypes},
			type:"post",
			dataType: "json",
			success: function(data){
				if(data.success){
	    			// 显示成功信息
		 			layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
					showList(searchSubjectId);
		 		}else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			isAddBtn = false;
		 		}	
			}
		});
	}
	$(document).on('click', '#limit1table .deleteLimitOne', function(e){
		e.preventDefault();
		var subjectId=$(this).attr("data-classId");
		var teacherId=$(this).attr("data-teacherId");
		deleteOne(subjectId,teacherId,$(this));
	})
	function deleteOne(classId,teacherId,elem){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var searchSubjectId=$("#searchSubjectId").val();
		$.ajax({
			url:"${request.contextPath}/exammanage/scoreLimit/deleteOne",
			data:{acadyear:acadyear,semester:semester,examId:examId,subjectId:searchSubjectId,classId:classId,teacherId:teacherId},
			type:"post",
			dataType: "json",
			success: function(data){
				if(data.success){
	    			// 显示成功信息
		 			$(elem).closest("span").remove();
		 			showList(searchSubjectId);
		 		}else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 		}	
			}
		});
	}
	
	function checkboxAllSelect(){
		if($("#allCheckClassId").is(':checked')){
			$('input:checkbox[name=checkClassId]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$('input:checkbox[name=checkClassId]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
	$('.checkboxOneClass').on('change',function(){
		var checkLength=$('input:checkbox[name=checkClassId]:checked').length;
		var allLength=$('input:checkbox[name=checkClassId]').length;
		if(checkLength<allLength || allLength==0){
			$("#allCheckClassId").prop('checked',false);
		}else{
			$("#allCheckClassId").prop('checked',true);
		}
	});
</script>
