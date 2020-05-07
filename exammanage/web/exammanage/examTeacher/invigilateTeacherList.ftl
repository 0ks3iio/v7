<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="filterForm">
<input type="hidden" name="examId" value="${examId!}"/>
<#if teacherDtosList?exists && (teacherDtosList?size > 0)>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th  style="width:5%;">考场编号</th>
 			<th  style="width:15%;">考试场地</th>
			<th  style="width:10%;">科目</th>
			<th  style="width:10%;">考试日期</th>
			<th  style="width:10%;">时段</th>
 			<th  style="width:23%;">开始-结束考号</th>
 			<th  style="width:20%;">监考老师</th>
 			<th  style="width:7%;">操作</th>
		</tr>
	</thead>
	<tbody>
		
			<#list teacherDtosList as dto>
				<tr>
					<input type="hidden" id="teacherIds${dto_index}" name="teacherIds" value="${dto.teacherIds!}"/>
					<input type="hidden" id="emPlaceTeacherId${dto_index}" name="emPlaceTeacherId" value="${dto.emPlaceTeacherId!}"/>
					<td >${dto.examPlaceCode!}</td>
					<td >${dto.examPlaceName!}</td>
					<td >${dto.subjectName!}</td>
					<td >${(dto.startTime?string('yyyy-MM-dd'))?if_exists}</td>
					<td >
								${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
					<#-- ${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists} -->
					</td>
					<td  title="${dto.examStartAndEndNumber!}"><p style="width:225px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${dto.examStartAndEndNumber!}</p></td>
					<td >
					<#if (dto.teacherIds!) == ''>
						<span class="color-d9d9d9">未安排</span>
					<#else>
						${dto.teacherNames!}
					</#if>
					</td>
					<td ><a href="javascript:edit('${dto_index}','${dto.emSubjectInfoId!}','${dto.emPlaceId!}');" class="table-btn color-blue">编辑</a></td>
				</tr>
			</#list>
		
	</tbody>
	<#else>
	        <div class="no-data-container">
	            <div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
	                <div class="no-data-body">
	                    <p class="no-data-txt">暂无相关数据</p>
	                </div>
	            </div>
	        </div>
		</#if>
</table>
</form>
<script>

function edit(index,emSubjectInfoId,emPlaceId) {
	var teacherIds = $('#teacherIds'+index).val();
	var emPlaceTeacherId = $('#emPlaceTeacherId'+index).val();
	var status = "${status!}";
	var subjectId = "${subjectId!}";
	var str = "&emSubjectInfoId=" + emSubjectInfoId + "&emPlaceId=" + emPlaceId + "&teacherIds=" + teacherIds + "&status=" + status + "&subjectId=" + subjectId + "&emPlaceTeacherId=" + emPlaceTeacherId;
	var url = "${request.contextPath}/exammanage/examTeacher/invigilateEdit/page?examId=${examId!}"+str;
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
		url : '${request.contextPath}/exammanage/examTeacher/filterSave',
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
			url:'${request.contextPath}/exammanage/examTeacher/filterSet',
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