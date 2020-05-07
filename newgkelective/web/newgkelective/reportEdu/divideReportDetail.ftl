<#if notEdit?default(false)>
<div class="form-horizontal" role="form">
</#if>		
<form id="myform">
<div class="form-group">
	<label class="col-sm-2 control-label no-padding-right">行政班数：</label>
	
	<div class="col-sm-3">
		<input type="text" name="xzbNumber" id="xzbNumber" vtype="int" min="0" maxlength="3" nullable="false" value="${report.xzbNumber!}" <#if notEdit?default(false)>disabled</#if> class="form-control">
		<input type="hidden" name="jxbNumber" value="${report.jxbNumber?default(0)}">
	</div>
	<div class="col-sm-4 control-tips"></div>
</div>
<div class="form-group">
    <input type="hidden" name="noStunumber" value="${report.noStunumber!}">
    <input type="hidden" name="twoStunumber" value="${report.twoStunumber!}">
    <input type="hidden" name="threeStunumber" value="${report.threeStunumber!}">
	<label class="col-sm-2 control-label no-padding">学生走班统计：</label>
	<div class="col-sm-10">
	   	 全走班（${report.noStunumber!}人），两科固定（${report.twoStunumber!}人），三科固定（${report.threeStunumber!}人）
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label no-padding-right">各班开班统计：</label>
	<div class="col-sm-4">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>选考科目</th>
					<th>总人数</th>
					<th>行政班数</th>
					<th>教学班数</th>
				</tr>
			</thead>
			<tbody>
				<#if aDivideList?? && aDivideList?size gt 0>
				<#list aDivideList as item>
				<tr>
				    <td>${item_index+1}</td>
				    <td>
				    	<input type="hidden" name="divideList[${item_index}].subjectType" value="${item.subjectType!}">
				    	<input type="hidden" name="divideList[${item_index}].subjectId" value="${item.subjectId!}">
				    	${courseNameMap[item.subjectId!]}
				    </td>
				    <td>
				    	<input type="hidden" name="divideList[${item_index}].studentNumber" value="${item.studentNumber!}">
				   		${item.studentNumber!}
				    </td>
				    <td>
				    	<input type="text" vtype="int" min="0" maxlength="3" nullable="false" <#if notEdit?default(false)>disabled</#if> id="${item_index}xzbNumber"  name="divideList[${item_index}].xzbNumber" class="form-control" value="${item.xzbNumber!}">
				    </td>
				    <td>
				    	<input type="text" vtype="int" min="0" maxlength="3" nullable="false" <#if notEdit?default(false)>disabled</#if> id="${item_index}jxbNumber"  name="divideList[${item_index}].jxbNumber" class="form-control" value="${item.jxbNumber!}">
			    	</td>
				</tr>
				</#list>
				</#if>
			</tbody>
 		</table>
	</div>
	<div class="col-sm-4">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学考科目</th>
					<th>总人数</th>
					<th>行政班数</th>
					<th>教学班数</th>
				</tr>
			</thead>
			<tbody>
				<#if bDivideList?? && bDivideList?size gt 0>
				<#list bDivideList as item>
				<#assign index=aDivideList?size+item_index>
				<tr>
				    <td>${item_index+1}</td>
				    <td>
				    	<input type="hidden" name="divideList[${index}].subjectType" value="${item.subjectType!}">
				    	<input type="hidden" name="divideList[${index}].subjectId" value="${item.subjectId!}">
				    	${courseNameMap[item.subjectId!]}
				    </td>
				    <td>
				    	<input type="hidden" name="divideList[${index}].studentNumber" value="${item.studentNumber!}">
				   		${item.studentNumber!}
				    </td>
				    <td>
				    	<input type="text" vtype="int" maxlength="3" nullable="false" <#if notEdit?default(false)>disabled</#if> id="${index}xzbNumber" name="divideList[${index}].xzbNumber" class="form-control" value="${item.xzbNumber!}">
				    </td>
				    <td>
				    	<input type="text" vtype="int" maxlength="3" nullable="false" <#if notEdit?default(false)>disabled</#if> id="${index}jxbNumber" name="divideList[${index}].jxbNumber" class="form-control" value="${item.jxbNumber!}">
			    	</td>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
</form>
<div class="form-group">
	<label class="col-sm-2">
	
	</label>
	
	<div class="col-sm-10">
		<#if notEdit?default(false)>
		<a href="javascript:void(0)" class="btn btn-blue" onclick="reReportDivide();">重新上报</a>
		<#else>
		<a href="javascript:void(0)" class="btn btn-blue" onclick="divideReportSave();">上报</a>
		</#if>
	</div>
</div>


<#if notEdit?default(false)>
</div>
</#if>
<script>
var isSubmit = false;
function divideReportSave(){
	if(isSubmit){
		return;
	}

	var checkVal = checkValue("#myform");
	if(!checkVal){
	 	isSubmit=false;
	 	return;
	}
	isSubmit=true;
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/newgkelective/report/divide/save',
		data:{'gradeId':'${gradeId!}'},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				var url = "${request.contextPath}/newgkelective/report/divide/index/page?gradeId=${gradeId!}&isMaster=1";
				$("#aa").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#myform").ajaxSubmit(options);
}

function reReportDivide() {
	var url = "${request.contextPath}/newgkelective/report/divide/index/page?gradeId=${gradeId!}&toEdit=1";
    $("#aa").load(url);
}
</script>