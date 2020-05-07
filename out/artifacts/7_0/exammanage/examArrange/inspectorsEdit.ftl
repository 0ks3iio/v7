<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="inspectorsForm">
<div class="layer-body">
	<div class="filter clearfix">
		<input type="hidden" name="id" id="id" value="${examId!}">
		<input type="hidden" name="subjectInfoId" id="subjectInfoId" value="${subjectInfoId!}">
		<div class="filter-item block">
			<div class="" style="float: left;">
				<label for="" class="filter-name">在校老师：</label>
				<@popupMacro.selectMoreTeacher clickId="inspectorsTeacherName" id="teacherIds" name="inspectorsTeacherName" handler="">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="hidden" id="teacherIds" name="teacherIds" value="${teachInIds!}">
							<input type="text" id="inspectorsTeacherName" class="form-control" value="${teacherInNames!}">
							<a class="input-group-addon" href="javascript:void(0);"></a>
						</div>
					</div>
				</@popupMacro.selectMoreTeacher>
			</div>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">校外老师：</label>&nbsp;
			<select multiple="multiple" name="teacherSelect" id="teacherSelect" class="form-control chosen-select chosen-results" data-placeholder="请选择老师"  style="width: 206px;">
				<#if teacherOutLists?? && (teacherOutLists?size>0)>
					<#list teacherOutLists as item>
						<#assign ss=false>
						<#if teacherOutIdsList?? && (teacherOutIdsList?size>0)>
							<#list teacherOutIdsList as Ids>
								<#if (item.id!) == Ids>
									<#assign ss=true>
									<#break>
								</#if>
							</#list>
							<option value="${item.id!}" <#if ss>selected</#if>>${item.teacherName?default('')}</option>
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
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>
$('#teacherSelect').chosen({
	width:'206px',
	results_height:'200px',
	multi_container_height:'50px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});

$(function(){
	$('.chosen-results').css({height: '100px',overflow:'auto'});
});

var isSubmit=false;
$("#arrange-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var teacherInIds = $("#teacherIds").val();
	var teacherOutIds = $("#teacherSelect").val();
	if (teacherOutIds == null) {
		teacherOutIds = "";
	}
	if (teacherInIds == "" && teacherOutIds == "") {
		layerTipMsg(false,"提示","巡考老师不能为空！");
		isSubmit=false;
		return;
	}
	$("#arrange-commit").addClass("disabled");
    var str = "?examId=${examId!}&emPlaceTeacherId=${emPlaceTeacherId!}&teacherInIds="+teacherInIds+"&teacherOutIds="+teacherOutIds;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/inspectorsSave'+str,
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				var url =  '${request.contextPath}/exammanage/examArrange/inspectorsTeacher/page?examId=${examId!}';
				$("#showTabDiv").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#inspectorsForm").ajaxSubmit(options);
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
</script>