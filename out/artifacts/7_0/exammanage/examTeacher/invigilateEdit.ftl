<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="teachForm">
<div class="layer-body">
	<div class="filter clearfix">
		<input type="hidden" name="id" id="id" value="${examId!}">
		<input type="hidden" name="subjectId" id="subjectId" value="${emSubjectInfo.subjectId!}">
		<input type="hidden" name="startDate" id="startDate" value="${(emSubjectInfo.startDate?string('yyyy-MM-dd HH:mm:ss'))?if_exists}">
		<input type="hidden" name="endDate" id="endDate" value="${(emSubjectInfo.endDate?string('yyyy-MM-dd HH:mm:ss'))?if_exists}">
		<input type="hidden" name="gkSubType" id="endDate" value="${emSubjectInfo.gkSubType!}">
		<input type="hidden" name="gkEndDate" id="gkEndDate" value="${(emSubjectInfo.gkEndDate?string('yyyy-MM-dd HH:mm:ss'))?if_exists}">
		<div class="filter-item block">
			<label for="" class="filter-name">考试科目：</label>
			<span class="filter-name text-left">${emSubjectInfo.courseName!}</span>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">考试时间：</label>
			<div class="filter-content">
				<span class="lbl">${(emSubjectInfo.startDate?string('yyyy-MM-dd'))?if_exists}&nbsp;&nbsp;${(emSubjectInfo.startDate?string('HH:mm'))?if_exists}-${(emSubjectInfo.endDate?string('HH:mm'))?if_exists}</span>
			</div>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">考试场地：</label>
			<span class="filter-name">${emPlace.placeName!}</span>
		</div>
		<div class="filter-item block">
			<div class="" style="float: left;">
				<label for="" class="filter-name">在校老师：</label>
				<@popupMacro.selectMoreTeacher clickId="invigilateTeacherName" id="invigilateTeacherIds" name="invigilateTeacherName" handler="">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="hidden" id="invigilateTeacherIds" name="invigilateTeacherIds" value="${teachInIds!}">
							<input type="text" id="invigilateTeacherName" class="form-control" value="${teacherInNames!}">
							<a class="input-group-addon" href="javascript:void(0);"></a>
						</div>
					</div>
				</@popupMacro.selectMoreTeacher>
			</div>
		</div>
		<div class="filter-item block myteacherSelectDiv">
			<label for="" class="filter-name">校外老师：</label>&nbsp;
			<select multiple="multiple" name="teacherSelect" id="teacherSelect" class="form-control chosen-select chosen-results" data-placeholder="请选择老师"  style="width:220px;">
				<#if teacherOutLists?? && (teacherOutLists?size>0)>
					<#list teacherOutLists as item>
						<#assign ff=false>
						<#if teacherOutIdsList?? && (teacherOutIdsList?size>0)>
							<#list teacherOutIdsList as Ids>
								<#if (item.id!) == Ids>
									<#assign ff=true>
									<#break>
								</#if>
							</#list>
							<option value="${item.id!}" <#if ff>selected</#if>>${item.teacherName?default('')}</option>
						<#else>
							<option value="${item.id!}" >${item.teacherName?default('')}</option>
						</#if>
					</#list>
				<#else>
					<option value="" disabled="disabled">暂无数据</option>
				</#if>
			</select>  
		</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
$(function(){
	$('#teacherSelect').chosen({
		width:'220px',
		results_height:'200px',
		multi_container_height:'30px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	});
	$('.chosen-results').css({height: '100px',overflow:'auto'});
	
});

var isSubmit=false;
$("#arrange-commit").on("click",function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
    var teacherInIds = $("#invigilateTeacherIds").val();
	var teacherOutIds = $("#teacherSelect").val();
	if (teacherOutIds == null) {
		teacherOutIds = "";
	}
	if (teacherInIds == "" && teacherOutIds == "") {
        layer.closeAll();
        layer.msg("监考老师不能为空！", {
            offset: 't',
            time: 2000
        });
		isSubmit=false;
		return;
	}
	$("#arrange-commit").addClass("disabled");
    var str = "?examId=${examId!}&emPlaceTeacherId=${emPlaceTeacherId!}&placeId=${placeId!}&teacherInIds="+teacherInIds+"&teacherOutIds="+teacherOutIds;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/examTeacher/invigilateSave'+str,
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
                layer.closeAll();
                layer.msg("操作成功！", {
                    offset: 't',
                    time: 2000
                });
				var url =  '${request.contextPath}/exammanage/examTeacher/invigilateTeacherIndex/page?examId=${examId!}&status=${status!}&subjectId=${subjectId!}';
				$("#showTabDiv").load(url);
	 		}
	 		else{
                layer.closeAll();
                layer.msg(data.msg, {
                    offset: 't',
                    time: 2000
                });
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#teachForm").ajaxSubmit(options);
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
</script>