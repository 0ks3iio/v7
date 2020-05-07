<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div style="display:none">
<@popupMacro.selectMoreTeacher clickId="teacherName" id="teacherId" name="teacherName" handler="saveTeachers();">
	<div class="input-group">
		<input type="hidden" id="teacherId" name="teacherId" value="${teacherIds!}"/>
		<input type="text" id="teacherName" class="form-control" style="width:400px;" value="${teacherNames!}"/>
	</div>
</@popupMacro.selectMoreTeacher>
</div>
<div class="row">
	<div class="col-xs-12">
	    <div class="box box-default">
	    	<div class="box-body">
	    		<p class="font-16">总管理员</p>
	    		<button class="btn btn-blue mb5" onclick="updateTea();">+ 新增</button>
	    		<input type="hidden" id="delId" value=""/>
	    		<#if tea?exists && tea?size gt 0>
	    			<#list tea as t>
	    				<button class="btn btn-white mb5" onclick="del('${t.id!}');">${t.teacherName!}<span>×</span></button>
	    			</#list>
	    		</#if>
	    	</div>
	    </div>
	    <div class="box box-default">
			<div class="box-body clearfix">
                <div class="tab-container">
					<div class="tab-header clearfix">
						<ul class="nav nav-tabs nav-tabs-1">
						 	<li class="active">
						 		<a data-toggle="tab" href="javascript:void(0)" onclick="chooseTab('1')">必修课</a>
						 	</li>
						 	<li class="">
						 		<a data-toggle="tab" href="javascript:void(0)" onclick="chooseTab('2')">选修课</a>
						 	</li>
						</ul>
					</div>
					<div class="tab-content" id="tabList"></div>
			    </div>
			</div>
		</div>

	</div><!-- /.col -->
</div><!-- /.row -->
					
<script>
$(function(){
	//初始化
	chooseTab('1');
});
function chooseTab(tab){
	var url='${request.contextPath}/exammanage/scoreLimit/tabHead/page?type='+tab;
	$("#tabList").load(url);
}

function updateTea(){
	$("#teacherName").click();
}

function del(id){
	if(id && id != ''){
		$('#delId').val(id);
		saveTeachers();
	}
};

function saveTeachers(){
	//保存
	var teacherIds=$("#teacherId").val();
	var delId=$("#delId").val();
	//直接保存
	$.ajax({
		url:'${request.contextPath}/exammanage/scoreLimit/saveNotLimit',
		data:{'teacherIds':teacherIds,'delId':delId},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	    		// 显示成功信息
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				var url='${request.contextPath}/exammanage/scoreLimit/index/page';
				$(".model-div").load(url);
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#teacherId").val($("#oldteacherId").val());
	 			$("#teacherName").val($("#oldteacherName").val());
	 		}	
		}
	});
}
</script>