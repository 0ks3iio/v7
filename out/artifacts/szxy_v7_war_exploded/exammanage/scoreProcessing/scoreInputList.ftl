<div class="table-container-header clearfix">
<#if !isCanScoreInfo>
	<span class="tip tip-grey">
			您没有该科目录分权限
	</span>
<#elseif isLock?default('0')=='1'>
	<span class="tip tip-grey">
			已锁定
	</span>
<#else>
	<div class="pull-right">
		 <a href="javascript:" class="btn btn-blue" onclick="saveScore()">保存</a>
	</div>
</#if>	
</div>
<form id="scoreForm">
<input type="hidden" name="emSubjectInfo.examId"  value="${emSubject.examId!}"/>
<input type="hidden" name="emSubjectInfo.subjectId" value="${emSubject.subjectId!}"/>
<input type="hidden" name="emSubjectInfo.id" value="${emSubject.id!}"/>
<input type="hidden" name="emSubjectInfo.fullScore" id="fullScore" value="${emSubject.fullScore?default(0.0)}"/>
<input type="hidden" name="emSubjectInfo.gradeType" value="${emSubject.gradeType!}"/>
<input type="hidden" name="emSubjectInfo.inputType" id="inputType" value="${emSubject.inputType!}"/>
<div class="table-container scoreFormDiv">
    <div class="table-container-body">
		<table class="table table-bordered table-striped table-hover exaRoom-table">
			<thead>
				<tr>
					<th class="text-center">考号</th>
					<th class="text-center">姓名</th>
					<th class="text-center">考场编号</th>
					<th class="text-center">学号</th>
					<th class="text-center">行政班</th>
					<th class="text-center">分数</th>
					<th class="text-center">考试状态</th>
					<th class="text-center">分数统计状态</th>
				</tr>
			</thead>
			<tbody>
				<#if (dtoList?exists && dtoList?size>0)>
				<#list dtoList as dto>
				<tr>
					<td class="text-center">
					<input type="hidden" name="scoreInfoList[${dto_index}].studentId" value="${dto.studentId!}"/>
					<input type="hidden" name="scoreInfoList[${dto_index}].classId" value="${dto.classId!}"/>
					<#if dto.scoreInfo?exists>
						<input type="hidden" name="scoreInfoList[${dto_index}].id" value="${dto.scoreInfo.id!}"/>
					<#else>
						<input type="hidden" name="scoreInfoList[${dto_index}].id" value=""/>
					</#if>	
					${dto.examNum!}
					</td>
					<td class="text-center">${dto.stuName!}</td>
					<td class="text-center">${dto.placeCode!}</td>
					<td class="text-center">${dto.stuCode!}</td>
					<td class="text-center">${dto.className!}</td>
					
					<#if dto.scoreInfo?exists>
						<#if emSubject.inputType=='S'>	
							<td>
							<#if isCanScoreInfo && isLock?default('0')=='0'>
								<input type="text"  class="table-input score_class" name="scoreInfoList[${dto_index}].score" id="score_${dto_index}"  vtype = "number" nullable="false"  placeholder="请输入分数" value="${dto.scoreInfo.score?default('0')}" maxLength="7">
							<#else>
								<input type="text"  readonly="readonly" class="table-input score_class" name="scoreInfoList[${dto_index}].score" id="score_${dto_index}"  vtype = "number" nullable="false"  placeholder="请输入分数" value="${dto.scoreInfo.score!}">
							</#if>
							</td>
						<#else>
							<td>
								<select name="scoreInfoList[${dto_index}].score" id="score_${dto_index}" <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if> class="form-control">
							 		${mcodeSetting.getMcodeSelect('${emSubject.gradeType!}','${dto.scoreInfo.score!}',"0")}
								</select>
							</td>
						</#if>

						<td class="text-center">
						   <select name="scoreInfoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control" <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if> notnull="false">
						 		${mcodeSetting.getMcodeSelect("DM-CJLX", '${dto.scoreInfo.scoreStatus!}', "0")}
							</select>
						</td>
						
						<td class="text-center">
						    <select name="scoreInfoList[${dto_index}].facet" id="facet_${dto_index}" class="form-control" <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if>>
								<option value="1" <#if dto.scoreInfo.facet?default('1')=='1'>selected</#if>>统计</option>
								<option value="0" <#if dto.scoreInfo.facet?default('1')=='0'>selected</#if>>不统计</option>
							</select>
						</td>
					<#else>
						<#if emSubject.inputType=='S'>	
							<td>
							<#if isCanScoreInfo && isLock?default('0')=='0'>
								<input type="text"  class="table-input score_class" name="scoreInfoList[${dto_index}].score" id="score_${dto_index}"  vtype = "number" nullable="false"  placeholder="请输入分数" value="0" maxLength="7">
							<#else>
								<input type="text"  readonly="readonly" class="table-input score_class" name="scoreInfoList[${dto_index}].score" id="score_${dto_index}"  vtype = "number" nullable="false"  placeholder="请输入分数" value="">
							</#if>
							</td>
						<#else>
							<td>
								<select name="scoreInfoList[${dto_index}].score" id="score_${dto_index}"  <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if> class="form-control">
							 		${mcodeSetting.getMcodeSelect('${emSubject.gradeType!}','',"0")}
								</select>
							</td>
						</#if>

						<td class="text-center">
						   <select name="scoreInfoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control" <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if> notnull="false">
						 		${mcodeSetting.getMcodeSelect("DM-CJLX", '', "0")}
							</select>
						</td>
						
						<td class="text-center">
						   <select name="scoreInfoList[${dto_index}].facet" id="facet_${dto_index}" class="form-control" <#if (!isCanScoreInfo) || (isLock?default('0')=='1')>  disabled="disabled"</#if>>
								<option value="1" selected>统计</option>
								<option value="0" >不统计</option>
							</select>
						</td>
					</#if>
					
				</tr>
				</#list>
				</#if>
			</tbody>
	</div>
</div>
</form>
<script>

function checkScore(){
	var inputType=$("#inputType").val();
	var f=false;
	if(inputType=="S"){
		var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
		var max=parseFloat($("#fullScore").val());
		$(".score_class").each(function(){
			var r = $(this).val().match(reg);
			if(r==null){
				
				layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
					tipsMore: true,
					tips: 3
				});
				if(!f){
					f=true;
				}
				return;
			}
			var s=parseFloat($(this).val());
			if(s>max){
				layer.tips('分数不能超过'+max+'!', $(this), {
					tipsMore: true,
					tips: 3
				});
				if(!f){
					f=true;
				}
				return;
			}
	});	
		
	}
	if(f){
		return false;
	}else{
		return true;
	}
}
var isSubmit=false;
function saveScore(){
	if(isSubmit){
		return;
	}
	var check = checkValue('.scoreFormDiv');
	if(!check){
		isSubmit=false;
		return;
	}
	var check = checkScore();
	if(!check){
		isSubmit=false;
		return;
	}
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/scoreProcessing/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				showScoreInput('${emSubject.subjectId!}','${emSubject.id!}');
				isSubmit=false;
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#scoreForm").ajaxSubmit(options);
}
</script>