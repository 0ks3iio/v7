<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
	<div class="layer-content">
		<input type="hidden" id="teacherPlanId" value=${teacherPlanId!}>
		<div class="publish-course publish-course-sm">
			<#if choiceTeacherDtoList?exists && (choiceTeacherDtoList?size>0)>
				<#list choiceTeacherDtoList as item>
					<input type="hidden" value="${item.teacherId!}">
					<#if item.state == "0">
						<span class="teacherList" onclick="change(this)">${item.teacherName!}</span>
					<#else >
						<span class="teacherList active" onclick="change(this)">${item.teacherName}</span>
					</#if>
				</#list>
			</#if>
		</div>
	</div>
	<div class="layer-footer">
   		<button class="btn btn-lightblue" id="arrange-commit">确定</button>
   		<button class="btn btn-grey" id="arrange-close">取消</button>
    </div>
</div>

<script>
// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 // 确定按钮操作功能
 $("#arrange-commit").on("click", function(){	
 	var content = "";
 	var teacherIds = "";
 	var teacherPlanId = $("#teacherPlanId").val();
 	var indexId = $("#indexId"+teacherPlanId).val();
	$(".teacherList.active").each(function(i){
		var teacherId = $(this).prev().val();
		teacherIds += (","+teacherId);
    	var teacherName = $(this).html();
    	content += ("<span>"+teacherName+"</span>");
 	});
 	teacherIds = teacherIds.substring(1);
	var str = '<input type="hidden" name="dtoList['+indexId+'].teacherIds" value="'+teacherIds+'">';
	content = str+content;;
 	content += '<a href="javascript:void(0)" style="font-size:14px" onclick="alertList(\''+teacherPlanId+'\',\''+teacherIds+'\')">修改</a>';
 	$("#recommend-list"+indexId).html(content);
 	layer.closeAll();
});

function change(obj){
	obj = $(obj);
	if(obj.hasClass('active')){
		obj.removeClass('active');
	}else{
		obj.addClass('active');
	}
}

</script>

