<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<form id="adjustTeacher">
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">已选教师：</span>
		<div class="filter-content">
			<#if teacherMap?exists>
				<#list teacherMap?keys as key>
					<#if key_index == 0>
						${teacherMap[key]}
					<#else>
						、${teacherMap[key]}
					</#if>
				</#list>
			</#if>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<a class="btn btn-blue js-add" onclick="doCompleteAdjustTeacher();">保存</a>
	</div>
</div>
<p class="tip"><em>*建议：安排同一名教师只在一个组别任课。例如：张三教师最好不同时在第一组的班级和第二组的班级任课。</em></p>
<input type="hidden" name="subjectId" value="${subjectId!}"/>
<table class="table table-bordered">
	<thead>
		<tr>
			<th>组别 <span data-toggle="tooltip" data-placement="top" title="同一组内的班级可以由任意教师任课；不同组的班级必须由不同教师任课。"><i class="fa fa-question-circle"></i></span></th>
			<th>班级</th>
			<th>上课教室</th>
			<th>上课时间</th>
			<th width="150px;">任课教师</th>
		</tr>
	</thead>
	<tbody>
		<#if arrayTeacherDtoMap?exists>
			<#list arrayTeacherDtoMap?keys as key>
				<#assign items = arrayTeacherDtoMap[key]>   
				<#list items as item>
					<tr>
						<#if item_index == 0>
							<td rowspan="${arrayTeacherDtoMap[key]?size}">第${key_index + 1}组</td>
						</#if>
							<td>${item.className!}</td>
							<td>${item.placeName?default("")}</td>
							<td>${item.haveClassTime!}</td>
							<td>
								<input type="hidden" name="arrayTeacherList[${item.index}].timetableTeachId" value="${item.timetableTeachId!}">
								<input type="hidden" name="arrayTeacherList[${item.index}].index" value="${item.index}">
								<input type="hidden" name="arrayTeacherList[${item.index}].HeapNum" value="${key_index}">
								<input type="hidden" name="arrayTeacherList[${item.index}].timetibleId" value="${item.timetibleId!}">
								<input type="hidden" name="arrayTeacherList[${item.index}].haveClassCode" value="${item.haveClassCode!}">
								<select name="arrayTeacherList[${item.index}].teacherId" id="" class="form-control teacherList" >
									<option value="" >--请选择--</option>
									<#if teacherMap?exists>
										<#list teacherMap?keys as key>
											<#if item.teacherId?default('') == key>
												<option value="${key}" selected="selected">${teacherMap[key]}</option>
											<#else >
												<option value="${key}" >${teacherMap[key]}</option>
											</#if>
										</#list>
									</#if>
								</select>
							</td>
					</tr>
				</#list>
			</#list>
		</#if>
	</tbody>
</table>
</form>

<script>
<#-- 
function saveone(obj) {
	var teacherId = $(obj).children('option:selected').val();
	var haveClassCode = $(obj).prev().val();
	var timetibleId = $(obj).prev().prev().val();
	$.ajax({
		url:'${request.contextPath}/newgkelective/${arrayId}/teacher/one/save',
		data: {'teacherId':teacherId,"haveClassCode":haveClassCode,'timetibleId':timetibleId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			if(jsonO.msg!=""){
	 				 layerTipMsg(jsonO.success,"成功",jsonO.msg);
	 			}
	 		}else{
	 			layerTipMsg(data.success,"失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
--> 

function doCompleteAdjustTeacher() {
	var isnull= false;
	$('.teacherList').each(function() {
		var teaid = $(this).val();
		if(teaid==""){
			isnull=true;
			return false;
		}
	})
	
	if(isnull){
		//layer.msg('请安排任课教师！');
		//return;
		
			layer.confirm('有部分任课教师未安排你确定完成教师安排？', function(index){
			var options = {
			url : '${request.contextPath}/newgkelective/${arrayId}/teacher/adjust/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.msg(data.msg, {offset: 't',time: 2000});
		 		}
		 		else{
		 			layerTipMsg(data.success,"保存失败",data.msg);
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#adjustTeacher").ajaxSubmit(options);      
		
		})
	}else{
		var options = {
			url : '${request.contextPath}/newgkelective/${arrayId}/teacher/adjust/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.msg(data.msg, {offset: 't',time: 2000});
		 		}
		 		else{
		 			layerTipMsg(data.success,"保存失败",data.msg);
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#adjustTeacher").ajaxSubmit(options); 
	
	}
}
</script>


