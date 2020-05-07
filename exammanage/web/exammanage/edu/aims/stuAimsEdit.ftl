<div class="" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="aimsId" id="aimsId" value="${dto.aimsId!}">
<input type="hidden" name="id" id="id" value="${dto.id!}">
<input type="hidden" name="studentName" id="studentName" value="${dto.student.studentName!}">
<input type="hidden" name="studentCode" id="studentCode" value="${dto.student.unitiveCode!}">
<input type="hidden" name="identityCard" id="identityCard" value="${dto.student.identityCard!}">
<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">学校：</label>
				<div class="col-sm-8" style="margin-top:7px;">${dto.schoolName}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">姓名：</label>
				<div class="col-sm-8" style="margin-top:7px;">${dto.student.studentName}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">班级：</label>
				<div class="col-sm-8" style="margin-top:7px;">${dto.student.className!}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">学籍号：</label>
				<div class="col-sm-8" style="margin-top:7px;">${dto.student.unitiveCode!}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">证件号：</label>
				<div class="col-sm-8" style="margin-top:7px;">${dto.student.identityCard!}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">性别：</label>
				<div class="col-sm-8" style="margin-top:7px;">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</div>
			</div>
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">民族：</label>
				<div class="col-sm-8" style="margin-top:7px;">${mcodeSetting.getMcode("DM-MZ","${dto.student.nation!}")}</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>填报学校</label>
				<div class="col-sm-8 filter-content">
					<select class="form-control" id="aimsSchoolId" name="aimsSchoolId">
					<#if schools?? && (schools?size>0)>
						<#list schools as item>
							<option value="${item.id!}" <#if dto.aimsSchoolId! == item.id!>selected</#if>>${item.schoolName?default('')}</option>
						</#list>
					<#else>
						<option value="">暂无数据</option>
					</#if>
					</select>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</div>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 $(function(){
	$(".layer-add").show();
	$("#result-close").on("click", function(){
	    layer.closeAll();
	});
});

function queryInfo() {
	var url = '${request.contextPath}/exammanage/edu/stuAims/page';
	$("#model-div-39015").load(url);
}

var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	var schoolArr = $("#aimsSchoolId").val();
	if(schoolArr && schoolArr.length > 0){
	}else{
		layerTipMsg(false,"提示","请选择填报学校！");
		isSubmit=false;
		return;
	}
	var options = {
		url : "${request.contextPath}/exammanage/edu/stuAims/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	queryInfo();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
});

</script>