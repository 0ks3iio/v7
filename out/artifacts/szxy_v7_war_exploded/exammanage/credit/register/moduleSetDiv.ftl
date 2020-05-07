<form id="subForm">
<div class="layer-content">
	<div class="form-horizontal">
	  <div class="form-group">
	    <label class="col-sm-2 control-label no-padding-right">考试：</label>
	    <div class="col-sm-9">
	      <select class="form-control" name="setExamId" id="setExamId">
	      <#if examlist?exists && examlist?size gt 0>
	      	<#list examlist as exam>
		      	<option value="${exam.id}" <#if setExamId == exam.id>selected</#if>>${exam.examName!}</option>
	      	</#list>
	      <#else>
	      	<option value="">无相关年级考试</option>
	      </#if>
	      </select>
	    </div>
	  </div>
	</div>
</div>
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-blue" id="arrange-commit" <#if examlist?exists && examlist?size gt 0><#else>style="display:none;"</#if>>确定</a>
	<a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	var setExamId = $('#setExamId').val();
	if(setExamId == ""){
		showMsgError("请选择考试！");
		return;
	}
	isSubmit = true;
	var options = {
		url : "${request.contextPath}/exammanage/credit/register/exam/moduleSet/save?acadyear=${acadyear}&semester=${semester}&gradeId=${gradeId}&setId=${setId}&setExamId="+setExamId,
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			$("#arrange-commit").removeClass("disabled");
				layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
				return;
	 		}
	 		else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
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