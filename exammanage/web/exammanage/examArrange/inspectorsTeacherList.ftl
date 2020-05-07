<form id="filterForm">
<input type="hidden" name="examId" value="${examId!}"/>
<div class="table-container">
	<#if inspectorsList?exists && (inspectorsList?size > 0)>
	<div class="table-container-header text-right">
		<a href="javascript:inspectorsImport();" class="btn btn-blue">导入</a>
	</div>
	<#else>
	<div class="table-container-header text-center">
		<span class="tip tip-grey ">(请先维护考试科目！)</span>
	</div>
	</#if>
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th class="" style="width:10%;">序号</th>
					<th class="" style="width:10%;">科目</th>
					<th class="" style="width:25%;">日期</th>
					<th class="" style="width:25%;">时段</th>
					<th class="" style="width:30%;">巡考老师</th>
				</tr>
			</thead>
			<tbody>
				<#if inspectorsList?exists && (inspectorsList?size > 0)>
				<#list inspectorsList as dto>
				<tr>
					<input type="hidden" id="teacherIds${dto_index}" name="teacherIds" value="${dto.teacherIds!}" >
					<input type="hidden" id="teacherNames${dto_index}" name="teacherNames" value="${dto.teacherNames!}" >
					<input type="hidden" id="emPlaceTeacherId${dto_index}" name="emPlaceTeacherId" value="${dto.emPlaceTeacherId!}" >
					<td class="">${dto_index+1}</td>
					<td class="">${dto.subjectName!}</td>
					<td class="">${(dto.startTime?string('yyyy-MM-dd'))?if_exists}</td>
					<td class="">
						<#if isgk == '1'>
							<#if dto.subType! == '0'>
								<#if dto.gkEndTime?exists>
									学考：${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.gkEndTime?string('HH:mm'))?if_exists}&nbsp;选考：${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
								<#else>
									 ${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
								</#if>
							<#elseif dto.subType! == '1'>
								选考：${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
							<#elseif dto.subType! == '2'>
								学考：${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.gkEndTime?string('HH:mm'))?if_exists}
							</#if>
							<#else>
								${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}
						</#if>
					</td>
					<td class="">
						<#if (dto.teacherIds!) == "">
							<a href="javascript:setTeacher('${dto_index}','${dto.subjectInfoId!}');" class="table-btn color-red">未设置</a>
						<#else>
							<a class="js-selectionTecaher" href="javascript:setTeacher('${dto_index}','${dto.subjectInfoId!}');"><i class="fa fa-edit"></i>${dto.teacherNames!}</a>
						</#if>
					</td>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
</form>
<script>
	function refresh() {
		var url =  '${request.contextPath}/exammanage/examArrange/inspectorsTeacher/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}
	
	function setTeacher(index,subjectInfoId) {
		var teacherIds = $("#teacherIds"+index).val();
		var emPlaceTeacherId = $("#emPlaceTeacherId"+index).val();
		var str = "&subjectInfoId=" + subjectInfoId + "&teacherIds=" + teacherIds + "&emPlaceTeacherId=" + emPlaceTeacherId;
		var url = "${request.contextPath}/exammanage/examArrange/inspectorsEdit?examId=${examId!}"+str;
		indexDiv = layerDivUrl(url,{title: "设置巡考老师",width:350,height:280});
	}
	
	var isSubmit=false;
	function saveInspectors() {
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
					refresh();
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
	
	function inspectorsImport() {
		var url="${request.contextPath}/exammanage/inspectorsImport/main?examId=${examId!}"
		$("#showTabDiv").load(url);
	}
</script>