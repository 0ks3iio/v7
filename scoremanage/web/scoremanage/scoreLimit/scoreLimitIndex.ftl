<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="box box-default">
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">录分总管理员：</span>
				<div class="filter-content">
					<input type="hidden" id="oldteacherId" name="oldteacherId" value="${teacherIds!}"/>
					<input type="hidden" id="oldteacherName" class="form-control" style="width:400px;" value="${teacherNames!}"/>
					<@popupMacro.selectMoreTeacher clickId="teacherName" id="teacherId" name="teacherName" handler="saveTeachers();">
						<div class="input-group">
							<input type="hidden" id="teacherId" name="teacherId" value="${teacherIds!}"/>
							<input type="text" id="teacherName" class="form-control" style="width:400px;" value="${teacherNames!}"/>
						</div>
					</@popupMacro.selectMoreTeacher>
				</div>
			</div>
		</div>

		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active">
				<a role="tab" data-toggle="tab" href="javascript:void(0)" onclick="chooseTab('1')">必修课</a>
			</li>
			<li role="presentation">
				<a role="tab" data-toggle="tab" href="javascript:void(0)" onclick="chooseTab('2')">选修课</a>
			</li>
		</ul>

		<div class="tab-content" id="tabList">
		</div>
	</div>
</div>

<script>
$(function(){
	//初始化
	chooseTab('1');
	
});
function chooseTab(tab){
	var url='${request.contextPath}/scoremanage/scoreLimit/tabHead/page?type='+tab;
	$("#tabList").load(url);
}

function saveTeachers(){
	//保存
	var teacherIds=$("#teacherId").val();
	//直接保存
	$.ajax({
		url:'${request.contextPath}/scoremanage/scoreLimit/saveNotLimit',
		data:{'teacherIds':teacherIds},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	    		// 显示成功信息
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#teacherId").val($("#oldteacherId").val());
	 			$("#teacherName").val($("#oldteacherName").val());
	 		}	
		}
	});
}
</script>