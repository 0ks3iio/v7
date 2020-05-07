<title>孩子期末评价</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${evaluateRecord.id!}">
<input type="hidden" name="studentId" id="studentId" value="${evaluateRecord.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${evaluateRecord.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${evaluateRecord.semester!}">
<input type="hidden" name="evaluateLevel" id="evaluateLevel" value="${evaluateRecord.evaluateLevel!}">
<input type="hidden" name="teacherEvalContent" id="teacherEvalContent" value='${evaluateRecord.teacherEvalContent!}'>
<input type="hidden" name="stuHonorContent" id="stuHonorContent" value="${evaluateRecord.stuHonorContent!}">
<input type="hidden" name="stuGatherContent" id="stuGatherContent" value="${evaluateRecord.stuGatherContent!}">
<input type="hidden" name="stuWishContent" id="stuWishContent" value="${evaluateRecord.stuWishContent!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
				<div class="filter clearfix">
					<table class="table table-bordered table-striped table-hover no-margin">
		   				<tr>
		      				<th><span style="color:red">*</span>评价：</th>
			  					<td width="" height="30"><textarea  style="width:435px;" id="parentEvalContent" name="parentEvalContent" maxLength="300" rows="2" cols="64" nullable="false" >${evaluateRecord.parentEvalContent!}</textarea></td>
	      					</tr>
					</table>
            </div>
        </div>
			</div>
		</div>
		</form>
	</div>	
	<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">保存</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
    </div>
<script>
	$(function(){
		
		$("#arrange-close").on("click", function(){
    		detail();    
 		});
 		
 		$("#arrange-commit").on("click", function(){
			
			var check = checkValue('#subForm');
			if(!check){
				return;
			} 
			
			var options = {
			url : "${request.contextPath}/studevelop/semesterEndEvaluate/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			return;
		 		}else{
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
			
 		});
	});
</script>