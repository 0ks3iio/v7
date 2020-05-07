<title>记录回顾与展望</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${evaluateRecord.id!}">
<input type="hidden" name="studentId" id="studentId" value="${evaluateRecord.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${evaluateRecord.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${evaluateRecord.semester!}">
<input type="hidden" name="evaluateLevel" id="evaluateLevel" value="${evaluateRecord.evaluateLevel!}">
<input type="hidden" name="teacherEvalContent" id="teacherEvalContent" value='${evaluateRecord.teacherEvalContent!}'>
<input type="hidden" name="parentEvalContent" id="parentEvalContent" value='${evaluateRecord.parentEvalContent!}'>
	<div class="layer-body">
		<div class="filter clearfix"> 
				<div class="filter clearfix">
					<table class="table table-bordered table-striped table-hover no-margin">
		   <tr>
		      <th><span style="color:red">*</span>我的荣誉：</th>
			  <td width="" height="30"><textarea  style="width:435px;" id="stuHonorContent" name="stuHonorContent" maxLength="300" rows="2" cols="64" nullable="false" >${evaluateRecord.stuHonorContent!}</textarea></td>
	      </tr>
		  <tr>
		      <th><span style="color:red">*</span>我的收获：</th>
			  <td width="" height="30"><textarea  style="width:435px;" id="stuGatherContent" name="stuGatherContent" maxLength="300" rows="2" cols="64" nullable="false" >${evaluateRecord.stuGatherContent!}</textarea></td>
	      </tr>
	      <tr>
		      <th><span style="color:red">*</span>我的愿望：</th>
			  <td width="" height="30"><textarea  style="width:435px;" id="stuWishContent" name="stuWishContent" maxLength="300" rows="2" cols="64" nullable="false" >${evaluateRecord.stuWishContent!}</textarea></td>
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
			url : "${request.contextPath}/studevelop/recordReview/save",
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
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		};
		$("#subForm").ajaxSubmit(options);
			
 		});
	});
</script>