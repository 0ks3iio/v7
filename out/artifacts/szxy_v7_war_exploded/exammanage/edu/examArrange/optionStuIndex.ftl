<div class="table-container-header text-right">
		<a class="btn btn-blue" onClick="saveFrom();">保存</a>
	</div>
<form id="optionStuForm">
<div id="myDiv">
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th style="width:5%">序号</th>
			<th class="text-center" style="width:25%">学校名称</th>
			<th class="text-center" style="width:30%">已通过报名审核人数</th>
			<th class="text-center" style="width:25%">参考考点名称</th>
			<th class="text-center" style="width:15%">参考人数</th>
		</tr>
	</thead>
	<tbody>
		<#if dtos?exists && (dtos?size > 0)>
		<#assign i = 0>
			<#list dtos as dto>
				<#if dto.enlist?size gt 0>
					<#assign row = dto.enlist?size>
				<#else>
					<#assign row = 1>
				</#if>
				<tr>
					<td rowspan=${row}>${dto_index +1}</td>
					<td class="text-center" rowspan=${row}>
					${dto.schoolName!}
					</td>
					<td class="text-center" rowspan=${row}>
						${dto.count!}<font color="red">（已分配${dto.arrangeCount}人，待分配<#if dto.notArrangeCount < 0>0<#else>${dto.notArrangeCount}</#if>人）</font>
						<input type="hidden" class="schoolId" value="${dto.schoolId!}">
						<input type="hidden" id="school_count_${dto.schoolId}" value="${dto.count!}">
						<input type="hidden" id="schoolName_${dto.schoolId}" value="${dto.schoolName!}">
					</td>
					<#list dto.enlist as en>
						<#if en_index gt 0>
							<tr>
						</#if>
						<td class="text-center">${en.optionName!}</td>
						<td class="text-center">
						<input type="text" class="form-control sub_count_${dto.schoolId!}" nullable="false" vtype="int" max="9999" min="0" id="joinStudentCount${i}" name="enlist[${i}].joinStudentCount" value="${en.joinStudentCount}">
						<input type="hidden" name="enlist[${i}].id" id="enlist${i!}.id" value="${en.id!}">
						<input type="hidden" name="enlist[${i}].optionId" id="enlist${i!}.optionId" value="${en.optionId!}">
						<input type="hidden" name="enlist[${i}].joinSchoolId" id="enlist${i!}.joinSchoolId" value="${en.joinSchoolId!}">
						<input type="hidden" name="enlist[${i}].examId" id="enlist${i!}.examId" value="${en.examId!}">
						<#assign i = i+1>
						</td>
					</tr>
					</#list>
			</#list>
		</#if>
	</tbody>
</table>
</div>
</form>
<script>
function checkOptionStuNum(){
	var obj_schools = $('.schoolId');
	for(var i = 0; i<obj_schools.length;i++){
		var obj_school = obj_schools[i];
		var school_id = obj_school.value;
		var obj_sub_counts = $('.sub_count_'+school_id);
		var school_count = $('#school_count_'+school_id).val();
		var count = 0;
		for(var j = 0; j<obj_sub_counts.length;j++){
			var obj_sub_count = obj_sub_counts[j];
			var sub_count = obj_sub_count.value;
			count = count + parseInt(sub_count);
		}
		var schoolName = $('#schoolName_'+school_id).val();
		if(count > parseInt(school_count)){
			return schoolName;
		}
	}
	return '';
}

var isSubmit=false;
function saveFrom(){
	if(isSubmit){
		return;
	}
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        return;
    }
    var re = checkOptionStuNum();
    if(re && re != ''){
    	layerTipMsg(false,"",re+"人数分配超过超过已通过总人数！");
    	return;
    }
    isSubmit = true;
	var options = {
		url : "${request.contextPath}/exammanage/edu/examArrange/optionStuNum/save",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		isSubmit=false;
		 		return;
		 	}else{
		 		layer.closeAll();
				//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				setTimeout(itemShowList1('2'),500);
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#optionStuForm").ajaxSubmit(options);
}
</script>


