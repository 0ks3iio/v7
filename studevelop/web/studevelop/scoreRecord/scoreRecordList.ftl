<form id="subForm">
<div class="box box-default">
		<div class="box-header">
			<h3 class="box-title">${studentName!}</h3>
		</div>
		<div class="box-body">
			<div class="table-container">
				<div class="table-container-header text-right">
					<a class="btn btn-blue" onclick="saveScore();">保存</a>
					<a class="btn btn-blue" onclick="doImport();">导入</a>
				</div>
				<div class="table-container-body" id="myDiv">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th class="text-center" width="10%" style="word-break:break-all;">学科</th>
								<th class="text-center" width="10%" style="word-break:break-all;">学科类别</th>
								<#if stuDevelopProjectList?exists && stuDevelopProjectList?size gt 0>
								    <#list stuDevelopProjectList as pro>
								         <td width="100px;" style="word-break:break-all;background-color:#ececec;font-weight: bold;">${pro.projectName!}</td>
								    </#list>
								</#if>
							</tr>
						</thead>
						<tbody>
						    <#assign c = 0>
							<#if subjectList?exists && subjectList?size gt 0>
								<#list subjectList as sub>
								<#assign a=0>
								<#if sub.state?default('') == '0'>
								    <tr>
									   <td colspan = "2" class="text-center" style="word-break:break-all;" width="20%">${sub.name!}</td>	
									   <#if stuDevelopProjectList?exists && stuDevelopProjectList?size gt 0>
								           <#list stuDevelopProjectList as pro>
								                <#assign score = ''>
								                <#assign scoreid = ''>							                          
								                <td class="text-center">
								                <#if stuDevelopScoreRecordList?exists && stuDevelopScoreRecordList?size gt 0>
								                    <#list stuDevelopScoreRecordList as score>
								                       <#if sub.id == score.subjectId && pro.id == score.projectId>
								                           <#assign score = score.score?default('')>
								                           <#assign scoreid = score.id?default('')>
								                       </#if>
								                    </#list>
								                </#if>
								                    <input type="hidden" name="stuDevelopScoreRecordList[${c}].id" id="id${c}" class="form-control number" value="${scoreid}">
								                    <input type="text" name="stuDevelopScoreRecordList[${c}].score" id="score${c}" class="form-control number" value="${score}" maxlength="12">
								                    <input type="hidden" name="stuDevelopScoreRecordList[${c}].subjectId" id="subjectId${c}" class="form-control number" value="${sub.id!}">
								                    <input type="hidden" name="stuDevelopScoreRecordList[${c}].projectId" id="projectId${c}" class="form-control number" value="${pro.id!}">							                  
								                </td>
								           <#assign c = c+1>
								           </#list>	
								           							           
								       </#if>
								       							    
								    </tr>
								<#else>
								<#if stuDevelopCateGoryList?exists && stuDevelopCateGoryList?size gt 0>
								       <#list stuDevelopCateGoryList as gory>
								            <#if sub.id == gory.subjectId>
								                <#assign a=a+1>
								            </#if>
								       </#list>
								</#if>
								<tr>
									<td rowspan="${a+1}" class="text-center" style="word-break:break-all;" width="10%">${sub.name!}</td>								    
								</tr>	
								<#if stuDevelopCateGoryList?exists && stuDevelopCateGoryList?size gt 0>
								       <#assign b = 0>
								       <#list stuDevelopCateGoryList as gory>
								            <#if sub.id == gory.subjectId>
								            <tr>
								                <td class="text-center" style="word-break:break-all;" width="10%">${gory.categoryName!}</td>
								                <#if stuDevelopProjectList?exists && stuDevelopProjectList?size gt 0>
								                   <#list stuDevelopProjectList as pro>
								                       <#assign score = ''>
								                       <#assign scoreid = ''>
								                       <#if pro.state == '2' && b==0>								                          
								                           <td rowspan="${a}" class="text-center">
								                               <#if stuDevelopScoreRecordList?exists && stuDevelopScoreRecordList?size gt 0>
								                                   <#list stuDevelopScoreRecordList as score>
								                                       <#if sub.id == score.subjectId && pro.id == score.projectId && pro.state == '2'>
								                                          <#assign score = score.score?default('')>
								                                          <#assign scoreid = score.id?default('')>
								                                       </#if>
								                                   </#list>
								                               </#if>
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].id" id="id${c}" class="form-control number" value="${scoreid}">
								                               <input type="text" name="stuDevelopScoreRecordList[${c}].score" id="score${c}" class="form-control number" value="${score}" maxlength="12">
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].subjectId" id="subjectId${c}" class="form-control number" value="${sub.id!}">
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].projectId" id="projectId${c}" class="form-control number" value="${pro.id!}">
								                           </td>
								                       <#elseif pro.state == '1'>
								                           <td class="text-center">
								                               <#if stuDevelopScoreRecordList?exists && stuDevelopScoreRecordList?size gt 0>
								                                   <#list stuDevelopScoreRecordList as score>
								                                       <#if sub.id == score.subjectId && gory.id == score.categoryId?default('') && pro.id == score.projectId && pro.state == '1'>
								                                          <#assign score = score.score?default('')>
								                                           <#assign scoreid = score.id?default('')>
								                                       </#if>
								                                   </#list>
								                               </#if>
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].id" id="id${c}" class="form-control number" value="${scoreid}">
								                               <input type="text" name="stuDevelopScoreRecordList[${c}].score" id="score${c}" class="form-control number" value="${score}" maxlength="12">
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].subjectId" id="subjectId${c}" class="form-control number" value="${sub.id!}">
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].categoryId" id="categoryId${c}" class="form-control number" value="${gory.id!}">
								                               <input type="hidden" name="stuDevelopScoreRecordList[${c}].projectId" id="projectId${c}" class="form-control number" value="${pro.id!}">
								                           </td>
								                       </#if>
								                       <#assign c = c+1>
								                   </#list>
								                </#if>
								                <#assign b = b+1>
							                </tr>
								            </#if>
								       </#list>
								</#if>
								</#if>				
								</#list>
							</#if>
						</tbody>
					</table>
					<div class="text-right" style="margin-top:10px;">
			             <a class="btn btn-blue" onclick="saveScore('');">保存</a>
	                </div>
				</div>
			</div>	
		</div>
	</div>
</form>
<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<script>
var isSubmit=false;
function saveScore(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/scoreRecord/save?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		//$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
	            var url = "${request.contextPath}/studevelop/scoreRecord/list?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}";
	            $('#showList').load(url);
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}	
function doImport(){
    $('#importDiv').load("${request.contextPath}/studevelop/scoreRecord/import/main?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}");
}
</script>