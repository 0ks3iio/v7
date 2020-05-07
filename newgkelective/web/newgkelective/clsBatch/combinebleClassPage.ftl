<a>合班</a><span>(请选择一个班级进行合班)</span>
<div class="publish-course choose-course" id="copyItemDiv">
	<#if showPureList?exists && showPureList?size gt 0>
		<#list showPureList as clz>
			<span class="active"  data-value="" id="${clz.id!}">
				${clz.className!}(${clz.studentCount!0})
			</span>
		</#list>	
	</#if>
</div>

<script>
function combine(){
	var stuIds = [];
	var i = 0;
	$("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:checked').each(function(i){
		stuIds[i]=$(this).val();
		i=i+1;
	});
	var classId = $('#copyItemDiv span.active').attr('id');
	if(stuIds.length == 0 || !classId ){
		alert('选中学生以及要合班的班级才能合班');
	}
	var params = {"stuIds":stuIds,"classId":classId,"subjectId":${subjectId!}};
	$.ajax({
		url:"${request.contextPath}/newgkelective/${divideId}/doCombine",
		data:params,
		dataType: "JSON",
		success: function(data){
			if(data.success){
				layer.msg("合班成功！", {
					offset: 't',
					time: 2000
				});
				updateNum();
			}else{
				layerTipMsg(data.success,"失败",data.msg);
				
			}
			submitting = false;
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
	
}
function updateNum(){
	updateTableInf("leftTable"); 
	
	updateSort();
}
function updateSort(){
	if($("#0_sort")){
		$("#0_sort").trigger("update");
	}
	if($("#1_sort")){
		$("#1_sort").trigger("update");
	}
}
function updateTableInf(obj){
	$("#"+obj).find('tbody input:checkbox[name="course-checkbox"]:checked').parents('tr').remove();

	var all = $("#"+obj).find('tbody tr');
	$("#"+obj).find(".labelInf span em:eq(0)").text(all.length);
}
</script>