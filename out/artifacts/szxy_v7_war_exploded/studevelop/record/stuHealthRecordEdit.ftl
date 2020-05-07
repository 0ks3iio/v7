<title>身心健康详情</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${healthRecord.id!}">
<input type="hidden" name="studentId" id="studentId" value="${healthRecord.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${healthRecord.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${healthRecord.semester!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
				<div class="filter clearfix">
					<table class="table table-bordered table-striped table-hover no-margin">
		 <tr>
	        <th><span style="color:red">*</span>身高(厘米)：</th>
	        <td><input name="height" id="height" nullable="false" dataType="float" type="text"  size="19" min="0" max="999" vtype="number" maxlength="5" value="${healthRecord.height!}"></td>
	     </tr>
	     <tr>
	        <th><span style="color:red">*</span>体重(千克)：</th>
	        <td><input name="weight" id="weight"  nullable="false" dataType="float" type="text"  size="19" min="0" max="500" vtype="number" maxlength="5" value="${healthRecord.weight!}"></td>
	     </tr>
	     <tr>
	      	<th><span style="color:red">*</span>视力：</th>
	        <td>
	        	左&nbsp;<input name="leftEye" id="leftEye" nullable="false" dataType="float" type="text"  style="width:40px;" size="5"  min="0" max="99" vtype="number" maxlength="4" value="${healthRecord.leftEye!}">
	        	&nbsp;&nbsp;&nbsp;右&nbsp;<input name="rightEye" id="rightEye" nullable="false" dataType="float" type="text"  style="width:40px;" size="5"  min="0" max="99" vtype="number" maxlength="4" value="${healthRecord.rightEye!}">
	        </td>
	      </tr>
	      <tr>
	      	<th>体质测试(数字)：</th>
	        <td><input name="physique" id="physique" notNull="0" dataType="float" type="text" size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.physique!}"></td>
	      </tr>
	      <tr>
	        <th >成长阅读(数字)：</th>
	        <td><input name="groupRead" id="groupRead"  notNull="0" dataType="float" type="text" size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.groupRead!}"></td>
	      	</tr>
	      <tr>
	      	<th>社会实践(数字)：</th>
	        <td><input name="socialPractice" id="socialPractice"  notNull="0" dataType="float" type="text" size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.socialPractice!}"></td>
	      </tr>
		  <tr>
		  	<th colspan="2" style="text-align:center;font-size:20px">心理素质</th>
		  </tr>
		  <tr>
	        <th>自制力(数字)：</th>
	        <td><input name="selfControl" id="selfControl" msgName="自制力" notNull="0" dataType="float" type="text" size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.selfControl!}"></td>
	      </tr>
	      <tr>
	        <th>自信心(数字)：</th>
	        <td><input name="confidence" id="confidence" msgName="自信心" notNull="0" dataType="float" type="text"  size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.confidence!}"></td>
	      </tr>
	      <tr>
	      	<th>合作交往(数字)：</th>
	        <td><input name="contact" id="contact" msgName="合作交往" notNull="0" dataType="float" type="text"  size="16" maxlength="4" min="0" max="99" vtype="number" value="${healthRecord.contact!}"></td>
	      </tr>
	      <tr>
		  	<th colspan="2" style="text-align:center;font-size:20px">身心健康</th>
		  </tr>
		   <tr>
	        <th>注意：</th>
	        <td>
	        	<select name="attention" id="attention" class="input-sel150">
		  		<option value='1' <#if (healthRecord.attention?default(''))=='1'>selected</#if>>集中</option>
		  		<option value='2' <#if (healthRecord.attention?default(''))=='2'>selected</#if>>较集中</option>
		  		<option value='3' <#if (healthRecord.attention?default(''))=='3'>selected</#if>>需集中</option>
			</select>
	        </td>
	      </tr>
	      <tr>
	        <th>观察：</th>
	        <td>
	        	<select name="observation" id="observation" class="input-sel150">
		  		<option value='1' <#if (healthRecord.observation?default(''))=='1'>selected</#if>>仔细</option>
		  		<option value='2' <#if (healthRecord.observation?default(''))=='2'>selected</#if>>较仔细</option>
		  		<option value='3' <#if (healthRecord.observation?default(''))=='3'>selected</#if>>需仔细</option>
			</select>
	        </td>
	      </tr>
	      <tr>
	        <th>记忆：</th>
	        <td>
	        	<select name="memory" id="memory" class="input-sel150">
		  		<option value='1' <#if (healthRecord.memory?default(''))=='1'>selected</#if>>强</option>
		  		<option value='2' <#if (healthRecord.memory?default(''))=='2'>selected</#if>>较强</option>
		  		<option value='3' <#if (healthRecord.memory?default(''))=='3'>selected</#if>>需加强</option>
			</select>
	        </td>
	      </tr>
	      <tr>
	        <th>思维：</th>
	        <td>
	        	<select name="thinking" id="thinking" class="input-sel150">
		  		<option value='1' <#if (healthRecord.thinking?default(''))=='1'>selected</#if>>活跃</option>
		  		<option value='2' <#if (healthRecord.thinking?default(''))=='2'>selected</#if>>较活跃</option>
		  		<option value='3' <#if (healthRecord.thinking?default(''))=='3'>selected</#if>>需活跃</option>
			</select>
	        </td>
	      </tr>
	      <tr>
	        <th>情绪：</th>
	        <td>
	        	<select name="mood" id="mood" class="input-sel150">
		  		<option value='1' <#if (healthRecord.mood?default(''))=='1'>selected</#if>>积极</option>
		  		<option value='2' <#if (healthRecord.mood?default(''))=='2'>selected</#if>>较积极</option>
		  		<option value='3' <#if (healthRecord.mood?default(''))=='3'>selected</#if>>需积极</option>
			</select>
	        </td>
	      </tr>
	      <tr>
	        <th>意志：</th>
	        <td>
	        	<select name="will" id="will" class="input-sel150">
		  		<option value='1' <#if (healthRecord.will?default(''))=='1'>selected</#if>>坚强</option>
		  		<option value='2' <#if (healthRecord.will?default(''))=='2'>selected</#if>>较坚强</option>
		  		<option value='3' <#if (healthRecord.will?default(''))=='3'>selected</#if>>需坚强</option>
			</select>
	        </td>
	      </tr>
					</table>
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
	var isSubmit = false;
	
	$("#arrange-close").on("click", function(){
    	changeStuId();    
 	});
 	
 	$("#arrange-commit").on("click", function(){
		if(isSubmit){
			return;
		} 	
	
		isSubmit = true;
 		var stuId = $("#stuId").val();
 		if(stuId == ""){
			layerTipMsg(false,"请选择一个学生!","");
			isSubmit=false;
			return;
		}
		
		var check = checkValue('#subForm');
		if(!check){
		isSubmit=false;
		return;
		}
				
		var options = {
			url : "${request.contextPath}/studevelop/healthRecord/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit = false;
		 			return;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					isSubmit = true;
				  	changeStuId();
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