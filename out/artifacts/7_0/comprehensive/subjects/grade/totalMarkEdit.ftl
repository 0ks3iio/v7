	<div class="layer-content">
		<div class="form-horizontal">
			<form id="totalMarkEditForm">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学年：</label>
				<div class="col-sm-9">
					<select name="searchAcadyear" id="searchAcadyear" disabled class="form-control">
						<option value="${searchAcadyear!}">${searchAcadyear!}</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学期：</label>
				<div class="col-sm-9">
					<select name="searchSemester" id="searchSemester" disabled class="form-control" >
						${mcodeSetting.getMcodeSelect('DM-XQ',(searchSemester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">年级：</label>
				<div class="col-sm-9">
					<select name="gradeId" id="gradeId" class="form-control" <#if id??>disabled</#if> onChange="changeGradeId();">
						<#if gradeList?exists && (gradeList?size > 0)>
							<#list gradeList as list>
								<option value="${list.id!}" <#if gradeId?? && (list.id!) == gradeId>selected</#if>>${list.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group"  >
				<label class="col-sm-2 control-label no-padding-right">科目：</label>
				<div class="col-sm-9" id="subjectsDiv" style="height:150px;overflow-y:auto;">
					
				</div>
			</div>
			</form>
			<div class="layer-footer">
				<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
				<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
			</div>
		</div>
	</div>
<script>
$(function(){
	changeGradeId();
});

var isSubmit=false;
$("#arrange-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var searchAcadyear = "${searchAcadyear!}";
	var searchSemester = "${searchSemester!}";
	var gradeId = $("#gradeId").val();
	
	var objElem=$('input:checkbox[name=subList]');
	var ids="";
	if(objElem.length>0){
		$('input:checkbox[name=subList]').each(function(i){
			if($(this).is(':checked')){
				ids=ids+","+$(this).val();
			}
		});
	}else{
		layerTipMsg(false,"提示","请先选择科目！");
		isSubmit=false;
		return;
	}
	if(ids==""){
		layerTipMsg(false,"提示","请先选择科目！");
		isSubmit=false;
		return;
	}
	ids=ids.substring(1);
	
	$("#arrange-commit").addClass("disabled");
	// 提交数据
	var options = {
		url : '${request.contextPath}/comprehensive/subjects/totalMark/subSave/page?gradeId='+gradeId,
		data: {'compreInfoId':'${id!}','ids':ids,'searchAcadyear':searchAcadyear,'searchSemester':searchSemester},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				var url =  '${request.contextPath}/comprehensive/subjects/grade/gradeIndex/page';
				$("#showIndex").load(url);
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
	$("#totalMarkEditForm").ajaxSubmit(options);
});

function changeGradeId() {
	var gradeId = $("#gradeId").val();
	var searchAcadyear = "${searchAcadyear!}";
	var searchSemester = "${searchSemester!}";
	var id = "${id!}";
	var str = "?gradeId="+gradeId+"&searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&id="+id;
	var url =  '${request.contextPath}/comprehensive/subjects/totalMark/SubList/page'+str;
	$("#subjectsDiv").load(url);
}

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
   doLayerOk("#arrange-commit", {
   redirect:function(){},
   window:function(){layer.closeAll()}
   });     
});
</script>