<form id="subForm">
<div class="table-wrapper" >
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
				    <th>序号</th>
					<th>学号</th>
					<th>姓名</th>
					<th>行政班级</th>
					<th>科目</th>
					<th>补考成绩类型</th>
					<th>原始成绩</th>
					<th>录入补考成绩</th>
				</tr>
		</thead>
		<tbody>
		<#if scoreInfoList?exists && scoreInfoList?size gt 0>
		     <#list scoreInfoList as item>
                <tr>
                     <td>${item_index+1!}</td>
                     <td>${item.studentCode!}</td>
                     <td>${item.studentName!}</td>
                     <td>${item.clsName!}</td>
                     <td>${item.subjectName!}</td>
                     <td><#if needGeneral?exists && needGeneral>总评成绩<#else>考试成绩</#if></td>
                     <td><#if item.toScore?exists && item.toScore!="">${item.toScore!}<#else>${item.score!}</#if></td>
                     <td>
                             <input type="hidden" class="table-input score_class" name="resitScoreList[${item_index}].examId" id="resitScoreList${item_index}examId"  value="${item.examId!}">
                             <input type="hidden" class="table-input score_class" name="resitScoreList[${item_index}].subjectId" id="resitScoreList${item_index}subjectId"  value="${item.subjectId!}">
                             <input type="hidden" class="table-input score_class" name="resitScoreList[${item_index}].studentId" id="resitScoreList${item_index}studentId"  value="${item.studentId!}">
                             <input type="hidden" class="table-input score_class" name="resitScoreList[${item_index}].unitId" id="resitScoreList${item_index}unitId"  value="${item.unitId!}">
                             <input type="hidden" class="table-input score_class" name="resitScoreList[${item_index}].gradeId" id="resitScoreList${item_index}gradeId"  value="${gradeId!}">
                             <input type="text" class="table-input score_class" name="resitScoreList[${item_index}].score" id="resitScoreList${item_index}score"  vtype="number" max="<#if fullMark?exists>${fullMark!}</#if>" min="0" placeholder="请输入补考成绩" value="${item.resitScore!}" maxLength="7">
                     </td>
                </tr>
            </#list>
        </#if>
		</tbody>
	</table>		
</div>
</form>
<div class="row">
	<div class="col-xs-12 text-right" style="margin-top:10px">
		  <a class="btn btn-blue" onclick="save();">保存</a>
	</div>
</div>
<script>
var isSubmit=false;
function save(){
	if(isSubmit){
		return;
	}	
	var check = checkValue('#resitScoreList');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/scoremanage/resitScore/saveAll',
		data:{"examId":'${examId!}',"subjectId":'${subjectId!}',"gradeId":'${gradeId!}'},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
		 		//layerTipMsg(data.success,"成功",data.msg);
		 		layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
		 		isSubmit=false;
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
} 
</script>