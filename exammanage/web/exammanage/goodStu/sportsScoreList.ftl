<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mysavediv">
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center" style="width:5%">序号</th>
			<th class="text-center" style="width:15%">学校</th>
			<th class="text-center" style="width:10%">学生姓名</th>
			<th class="text-center" style="width:10%">考号</th>
			<th class="text-center" style="width:10%">学籍号</th>
			<th class="text-center" style="width:10%">身份证号</th>
			<th class="text-center" style="width:10%">性别</th>
			<th class="text-center" style="width:10%">过程成绩</th>
			<th class="text-center" style="width:10%">考试成绩</th>
			<th class="text-center" style="width:10%">状态</th>
		</tr>
	</thead>
	<tbody>
		<#if exammanageSportsScores?exists && (exammanageSportsScores?size > 0)>
			<#list exammanageSportsScores as dto>
				<tr>
					<td class="text-center">${dto_index+1}</td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.stuName!}</td>
					<td class="text-center">${dto.examNum!}</td>
					<td class="text-center">${dto.xueji!}</td>
					<td class="text-center">${dto.cardNum!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-XB","${dto.sex!}")}</td>
					<td class="text-center">
						<input id="dtos[${dto_index}].courseScore" name="dtos[${dto_index}].courseScore" type="text" class="form-control inline-block number" maxLength="4" value="${dto.courseScore!}"/>
						<input type="hidden" name="dtos[${dto_index}].schoolId" value="${dto.schoolId!}"/>
						<input type="hidden" name="dtos[${dto_index}].studentId" value="${dto.studentId!}"/>
						<input type="hidden" name="dtos[${dto_index}].stuName" value="${dto.stuName!}"/>
						<input type="hidden" name="dtos[${dto_index}].examId" value="${dto.examId!}"/>
						<input type="hidden" name="dtos[${dto_index}].id" value="${dto.id!}"/>
					</td>
					<td class="text-center">
						<input id="dtos[${dto_index}].examScore" name="dtos[${dto_index}].examScore" type="text" class="form-control inline-block number" maxLength="4" value="${dto.examScore!}"/>
					</td>
					<td class="text-center">
						<select class="form-control" id="dtos[${dto_index}].state" name="dtos[${dto_index}].state">
							<option value="">请选择</option>
							<option value="1" <#if dto.state?default('a')=='1'>selected</#if>>正常</option>
							<option value="2" <#if dto.state?default('a')=='2'>selected</#if>>作弊</option>
							<option value="3" <#if dto.state?default('a')=='3'>selected</#if>>免考</option>
							<option value="4" <#if dto.state?default('a')=='4'>selected</#if>>缺考</option>
						</select>
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="10" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
</form>
<#if exammanageSportsScores?exists&&exammanageSportsScores?size gt 0>
		<@htmlcom.pageToolBar container="#stuList" class=""/>
</#if>
<script>
var ABIndex = ${exammanageSportsScores?size};
	var isSubmit = false;
function save(){
	if(isSubmit){
		return;
	}
	
	for(var i=0; i<ABIndex; i++) {
		var stuName=$('input[name="dtos['+i+'].stuName"]').val();
		var courseScore=$('input[name="dtos['+i+'].courseScore"]').val();
		var examScore=$('input[name="dtos['+i+'].examScore"]').val();
		var state=$('select[name="dtos['+i+'].state"]').val();
		if(!courseScore){
			
		}else{
			if(!checkRates(courseScore)){
				layerTipMsg(false,"提示",stuName+"同学过程成绩格式不正确(请输入最多4位的数字)！");
				isSubmit=false;
				return;
			}
		}
		if(!examScore){
			
		}else{
			if(!checkRates(examScore)){
				layerTipMsg(false,"提示",stuName+"同学考试成绩格式不正确(请输入最多4位的数字)！");
				isSubmit=false;
				return;
			}
		}
		//if(!state){
		//	layerTipMsg(false,"提示",stuName+"同学状态设置不能为空！");
		//	isSubmit=false;
		//	return;
		//}
	}
	
	isSubmit = true;
	var examId = $("#examId").val();
	var acadyear = $("#acadyear").val();
	var semester = $("#searchSemester").val();
	var options = {
		url : "${request.contextPath}/exammanage/edu/sports/save?acadyear="+acadyear+"&semester="+semester+"&examId="+examId,
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit = false;
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	setTimeout(showStuList(),500);
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#mysavediv").ajaxSubmit(options);	
}

function checkRates(str){
    var re = /^(([1-9][0-9]*\.[0-9][0-9]*)|([0]\.[0-9][0-9]*)|([1-9][0-9]*)|([0]{1}))$/; 
     var Sure;
     if (!re.test(str)){
         Sure =false;
     }else{
         Sure =true;
     }
     return Sure;
}
</script>