<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="filterForm">
<input type="hidden" name="examId" value="${examId!}"/>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th class="" style="width:5%;">考场编号</th>
 			<th class="" style="width:15%;">考试场地</th>
			<th class="" style="width:10%;">科目</th>
			<th class="" style="width:10%;">考试日期</th>
			<th class="" style="width:10%;">时段</th>
 			<th class="" style="width:23%;">开始-结束考号</th>
 			<th class="" style="width:20%;">监考老师</th>
 			<th class="" style="width:7%;">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if teacherDtosList?exists && (teacherDtosList?size > 0)>
			<#list teacherDtosList as dto>
				<tr>
					<input type="hidden" id="teacherIds${dto_index}" name="teacherIds" value="${dto.teacherIds!}"/>
					<input type="hidden" id="emPlaceTeacherId${dto_index}" name="emPlaceTeacherId" value="${dto.emPlaceTeacherId!}"/>
					<td class="">${dto.examPlaceCode!}</td>
					<td class="">${dto.examPlaceName!}</td>
					<td class="">${dto.subjectName!}</td>
					<td class="">${(dto.startTime?string('yyyy-MM-dd'))?if_exists}</td>
					<td class="">
								${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
					<#-- ${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists} -->
					</td>
					<td class="" title="${dto.examStartAndEndNumber!}"><p style="width:225px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${dto.examStartAndEndNumber!}</p></td>
					<td class="">
					<#if (dto.teacherIds!) == ''>
						<span class="color-red">未安排</span>
					<#else>
						${dto.teacherNames!}
					</#if>
					</td>
					<td class=""><a href="javascript:edit('${dto_index}','${dto.emSubjectInfoId!}','${dto.emPlaceId!}');" class="table-btn color-red">编辑</a></td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
</form>
<script>
$(function(){
	
})

function edit(index,emSubjectInfoId,emPlaceId) {
	var teacherIds = $('#teacherIds'+index).val();
	var emPlaceTeacherId = $('#emPlaceTeacherId'+index).val();
	var status = "${status!}";
	var subjectId = "${subjectId!}";
	var str = "&emSubjectInfoId=" + emSubjectInfoId + "&emPlaceId=" + emPlaceId + "&teacherIds=" + teacherIds + "&status=" + status + "&subjectId=" + subjectId + "&emPlaceTeacherId=" + emPlaceTeacherId;
	var url = "${request.contextPath}/exammanage/examArrange/invigilateEdit/page?examId=${examId!}"+str;
	indexDiv = layerDivUrl(url,{title: "编辑",width:380,height:450});
}

var isSubmit=false;
function saveFilter(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$("#js-saveFilter").addClass("disabled");
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/filterSave',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				showFilterList();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#js-saveFilter").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#filterForm").ajaxSubmit(options);
}
function setFilter(type){
	var objElem=$('input:checkbox[name=stuCheckboxName]');
	var ids="";
	if(objElem.length>0){
		$('input:checkbox[name=stuCheckboxName]').each(function(i){
			if($(this).is(':checked')){
				ids=ids+","+$(this).val();
			}
		});
	}else{
		layerTipMsg(false,"提示","请先选择学生！");
	}
	if(ids==""){
		layerTipMsg(false,"提示","请先选择学生！");
	}
	ids=ids.substring(1);
	
	
	var ii = layer.load();
    $.ajax({
			url:'${request.contextPath}/exammanage/examArrange/filterSet',
			data: {'ids':ids,'examId':'${examId!}','type':type},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layerTipMsg(jsonO.success,"设置成功",jsonO.msg);
				  	showFilterList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"设置失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
}
function exportFilter(){
}

</script>