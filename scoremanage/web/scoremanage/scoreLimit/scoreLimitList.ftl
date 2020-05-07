<#import "/fw/macro/popupMacro.ftl" as popupMacro>

<@popupMacro.selectMoreTeacher clickId="searchTeacherName" id="searchTeacherId" name="searchTeacherName" handler="saveAllTeacherToClass()">
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<input type="hidden" id="searchTeacherId" value="">
			<input type="hidden" id="searchTeacherName" value="">
			<a class="btn btn-sm btn-blue js-add" id="teacherIdBtn" onclick="addTeacherIdToClass()">添加录分人员</a>
		</div>
	</div>
</@popupMacro.selectMoreTeacher>
<div class="table-container-body">
	<div class="clearfix">
	<em>先选择班级，然后点击添加录分人员</em>
	</div>
	<input type="hidden" id="searchSubjectId" value='${searchSubjectId!}'>
	<table class="table table-bordered table-striped table-hover" id="limit1table">
		<thead>
			<tr>
				<th width="18%">
					<label>
						<input type="checkbox" class="wp" name="allCheckClassId" id="allCheckClassId" onChange="checkboxAllSelect()">
						<span class="lbl"> 全选</span>
					</label>
				</th>
				<th>录分人员</th>
			</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as item>
			<tr name="tr_${item_index}">
				<td>
					<label>
						<input type="checkbox" name="checkClassId"  class="wp checkboxOneClass"  value="${item.classId!}" data-value="${item.classType!}">
						<span class="lbl">${item.className!}</span>
					</label>
				</td>
				<td>
					<#assign teacherMap=item.teacherMap>
					<#if teacherMap?exists && teacherMap?size gt 0>
					<#list teacherMap?keys as key>
					<span class="member">
						${teacherMap[key]!}
						<a href="#" data-classId="${item.classId!}" data-teacherId="${key!}" class="deleteLimitOne" ><i class="fa fa-times-circle"></i></a>
					</span>
					</#list>
					</#if>
				</td>
			</tr>
			</#list>
			</#if>
		</tbody>
	</table>
</div>
<script>
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
		$("#searchTeacherName").click();
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
			return;
		}
		if(classTypes!=""){
			classTypes=classTypes.substring(1);
		}
		var teacherIds=$("#searchTeacherId").val();
		if(teacherIds==""){
			//没有选中
			layer.alert('请选择教师',{icon:7});
			return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var searchSubjectId=$("#searchSubjectId").val();
		
		
		$.ajax({
			url:"${request.contextPath}/scoremanage/scoreLimit/saveBySubject",
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
			url:"${request.contextPath}/scoremanage/scoreLimit/deleteOne",
			data:{acadyear:acadyear,semester:semester,examId:examId,subjectId:searchSubjectId,classId:classId,teacherId:teacherId},
			type:"post",
			dataType: "json",
			success: function(data){
				if(data.success){
	    			// 显示成功信息
		 			$(elem).closest("span").remove();
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
