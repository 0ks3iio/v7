<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="col-sm-12">
	<div class="tab-content">
		<div class="tab-pane active" role="tabpanel" id="aa">
			<div class="table-container">
				<div class="table-container-header text-right">
					<button class="btn btn-sm btn-blue js-add" id="addTeacher" onclick="addTeacherBefore();">添加主任课老师</button>
					<button class="btn btn-sm btn-blue js-add" id="addOtherTeacher" onclick="addOtherTeacherBefore();">添加助教老师</button>
				</div>
				<div class="table-container-body">
					<table class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th width="10%">
									<label>
										<input type="checkbox" class="wp" onclick="swapCheck()">
										<span class="lbl"> 全选</span>
									</label>
								</th>
								<th>班级</th>
								<th>科目</th>
								<th>主任课老师</th>
								<th>助教老师</th>
							</tr>
						</thead>
						<tbody>
							<#if teachSettingDtoList?exists && (teachSettingDtoList?size>0)>
			                	<#list teachSettingDtoList as item>
				                     <tr>
										<td>
											<label>
												<input type="checkbox" class="wp other" value="${item.classTeachingId!}">
												<span class="lbl"> </span>
											</label>
										</td>
										<td>${item.className!}</td>
										<td>${item.subName!}</td>
										<td>
											<#if item.mainTeacherName??>
											<span class="member">
												${item.mainTeacherName!}
												<a href="javascript:void(0);" onclick="deleteMainTeacher('${item.classTeachingId!}')"><i class="fa fa-times-circle"></i></a>
											</span>
											</#if> 
											<span class="pull-right js-hover-opt" style="width:5%">
									    		<a href="javascript:;" onclick="doClickOne('${item.classTeachingId!}','${item.mainTeacherId!}')" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
									    	</span>
										</td>
										<td>
											<#if item.teacherDtoList?exists && (item.teacherDtoList?size>0)>
			                					<#list item.teacherDtoList as ite>
													<span class="member">
														${ite.teacherName!}
														<a href="javascript:void(0);" onclick="deleteAndSaveOtherTeacher('${item.classTeachingId!}','${ite.teacherId!}','')"><i class="fa fa-times-circle"></i></a>
													</span>
												</#list>
											</#if>
											<span class="pull-right js-hover-opt" style="width:5%">
									    		<a href="javascript:;" onclick="doClickMore('${item.classTeachingId!}','${item.otherTeacherIds!}')" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
									    	</span>
										</td>
									</tr>
			                    </#list>
			                </#if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<@popupMacro.selectOneTeacher clickId="oneTeacherId" id="oneTeacherId" name="oneTeacherName" resourceUrl="${request.contextPath}/static" handler="saveMainTeacher()">
	<input type="hidden" id="oneTeacherId" name="oneTeacherId" value=""/>
</@popupMacro.selectOneTeacher>
<@popupMacro.selectMoreTeacher clickId="moreTeacherId" id="moreTeacherId" name="moreTeacherName" resourceUrl="${request.contextPath}/static" handler="saveOtherTeacher()">
	<input type="hidden" id="moreTeacherId" name="moreTeacherId" value=""/>
</@popupMacro.selectMoreTeacher>
<script>
var isCheckAll = false;  
function swapCheck() {
	if (isCheckAll) {  
        $("input[type='checkbox']").each(function() {  
            this.checked = false;  
        });  
        isCheckAll = false;  
    } else {  
        $("input[type='checkbox']").each(function() {  
            this.checked = true;  
        });  
        isCheckAll = true;  
    }  
}

var ctId='';
function doClickOne(classTeachingId, mainTeacherId){
	ctId=classTeachingId;
	$('#oneTeacherId').val(mainTeacherId);
	$('#oneTeacherId').click();
}

var otId='';
function doClickMore(classTeachingId, otherTeacherId){
	ctId=classTeachingId;
	otId=otherTeacherId;
	$('#moreTeacherId').val(otherTeacherId);
	$('#moreTeacherId').click();
}

function saveMainTeacher(){
	var teacherId=$("#oneTeacherId").val();
	if(teacherId==""){
		deleteMainTeacher(ctId);
	}else{
		var acadyear = $("#acadyear").val(); 
		var semester = $("#semester").val(); 
		$.ajax({
			url:"${request.contextPath}/basedata/courseopen/main/teacher/save",
			data:{"acadyear":acadyear, "semester":semester, "classTeachingId":ctId, "teacherId":teacherId},
			dataType: "json",
			success: function(data){
				if(data.success){
		 			layer.msg(data.msg, {offset: 't',time: 2000});
					teachSetting("1");
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	}
}

function saveOtherTeacher(){
	var teacherId=$("#moreTeacherId").val();
	if(otId!=""){
		if(teacherId==""){
			layer.alert('请选择至少一个老师',{icon:7});
		}else{
			deleteAndSaveOtherTeacher(ctId,otId,teacherId);
		}
	}else{
		var acadyear = $("#acadyear").val(); 
		var semester = $("#semester").val(); 
		$.ajax({
			url:"${request.contextPath}/basedata/courseopen/other/teacher/save",
			data:{"acadyear":acadyear,"semester":semester,"classTeachingId":ctId,"teacherId":teacherId},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
					teachSetting("1");
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	}
}

function addTeacherBefore() {
	var items = $("input.other:checked");
	
	if(items.length == 0) {
		layer.alert('请勾选至少一条记录',{icon:7});
	}else {
		var classTeachingId = [];
		items.each(function(){
			classTeachingId.push($(this).val());
		})
		doClickOne(classTeachingId.join(","),'');
	}
}

function addOtherTeacherBefore() {
	$(".teacher").val("");
	var items = $("input.other:checked");
	if(items.length == 0) {
		layer.alert('请勾选至少一条记录',{icon:7});
	}else {
		var classTeachingId = [];
		items.each(function(){
			classTeachingId.push($(this).val());
		})
		doClickMore(classTeachingId.join(","),'');
	}
}

function deleteMainTeacher(classTeachingId) {
	$.ajax({
		url:"${request.contextPath}/basedata/courseopen/delete/main/teacher",
		data:{"classTeachingId":classTeachingId},
		dataType: "json",
		success: function(data){
			if(data.success){
				layer.msg(data.msg, {offset: 't',time: 2000});
				teachSetting("1");
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		}
	});
}

function deleteAndSaveOtherTeacher(classTeachingId, oldTeacherId, teacherId) {
	$.ajax({
		url:"${request.contextPath}/basedata/courseopen/delete/other/teacher",
		data:{"classTeachingId":classTeachingId,"teacherId":teacherId,"oldTeacherId":oldTeacherId},
		dataType: "json",
		success: function(data){
			if(data.success){
				layer.msg(data.msg, {offset: 't',time: 2000});
				teachSetting("1");
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		}
	});
}
</script>

