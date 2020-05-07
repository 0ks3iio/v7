<form id="haveChoice">
<input type="hidden" name="acadyear" value="${acadyear!}">
<input type="hidden" name="semester" value="${semester!}">
<input type="hidden" name="gradeId" value="${gradeId!}">
<#if classId?default('')!=''>
<input type="hidden" name="classId" value="${classId!}">
</#if>
<input type="hidden" name="subjectType" value="${subjectType!}">
<input type="hidden" name="xxType" value="${xxType!}">
<div style="max-height:400px;overflow-y:auto;">
<ul class="label-list clearfix">
	<#if gradeTeachingDtoList?exists && (gradeTeachingDtoList?size>0)>
		<#list gradeTeachingDtoList as item>
		<li>
			<label>
			<input type="checkbox" class="wp" <#if item.flag?default('0')=='1'>checked</#if> name="gradeTeachingDtoList[${item_index}].flag" value="1" onClick="checkChange()" >
			<span class="lbl"> ${item.subjectName}</span>
			<input type="hidden" name="gradeTeachingDtoList[${item_index}].subjectType" value="${item.subjectType!}">
			<input type="hidden" name="gradeTeachingDtoList[${item_index}].subjectId" value="${item.subjectId!}">
				<input type="hidden" name="gradeTeachingDtoList[${item_index}].credit" value="${item.credit!}">
			</label>
		</li>
		</#list>
	<#else>
		没有相关课程，请去添加
	</#if>
</ul>
</div>
</form>
<script>

function checkChange(obj) {
	if($(obj).attr("checked")){
		$(obj).attr("checked",false);
	}else{
		$(obj).attr("checked",true);
	}	
}

var isMySubmit=false;
function saveCourseList(){
	<#if !(gradeTeachingDtoList?exists && gradeTeachingDtoList?size gt 0)>
		layer.alert('没有需要保存的数据',{icon:7});
		return;
	</#if>
	if(isMySubmit){
		return;
	}
	isMySubmit=false;
	if('${subjectType?default(1)}'=='1'){
		layer.confirm('如果取消课程后，该<#if classId?default('')==''>年级下所有行政班此</#if>课程所对应的课程表数据都会被删除。确定要保存吗？', function(index){
			layer.close(index);
			doSaveCourseList();
		},function(index){
			return;
		})
	}else{
		doSaveCourseList();
	}
}

function doSaveCourseList(){
	isMySubmit=true;
	var options = {
		url : '${request.contextPath}/basedata/courseopen<#if classId?default('')!=''>/class</#if>/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				layer.closeAll();
	 			layer.msg(data.msg, {offset: 't',time: 2000});
				<#if classId?default('')!=''>
	 			change2("1");
				<#else>
				change("1");
	 			</#if>
	 		}
	 		else{
	 			isMySubmit=false;
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#haveChoice").ajaxSubmit(options);
}
</script>