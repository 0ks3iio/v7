<div class="layer layer-add">
			<div class="layer-content">
				<div class="form-horizontal">					
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">科目：</label>
						<div class="col-sm-9">
						<#if emSubjectInfoList?exists && emSubjectInfoList?size gt 0>
						    <#list emSubjectInfoList as item>
							   <label><input type="checkbox" name="cheackId" class="wp" value="${item.subjectId!}" <#if '${item.daytime!}' == '1'>checked</#if> <#if '${item.isUsed!}' == '1'>disabled="disabled"</#if>><span class="lbl">${item.courseName!}</span></label>
                            </#list>
						</#if>
						</div>
					</div>
				</div>
			</div>
		</div>
		
<script>
function saveSubjects(){
   var id_array=new Array();  
   $('input[name="cheackId"]:checked').each(function(){  
       id_array.push($(this).val());//向数组中添加元素  
   });  
   var subjectIds=id_array.join(',');//将数组元素连接起来以构建一个字符串  
   if(id_array.length==0){
      layerTipMsgWarn("提示","请选择一个科目！");
      return;
   }
   var examId = '${examId!}';
   var gradeId = '${gradeId!}';
   $.ajax({
		url:"${request.contextPath}/comprehensive/subjects/score/subjectsSave",
		data:{subjectIds:subjectIds,examId:examId,gradeId:gradeId},
		dataType: "json",
		success: function(data){
		    if(data.success){
		        layer.closeAll();
			    layerTipMsg(data.success,"保存成功",data.msg);
			    showExamIndex();
		    }else{
		        layerTipMsg(data.success,"保存失败",data.msg);
		 		//$("#arrange-commit").removeClass("disabled");
		 		return;
		    }
		}
	});
}
</script>