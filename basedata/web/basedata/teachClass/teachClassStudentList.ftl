<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="box box-default">
	<div class="box-header">
		<input type="hidden" id="teachClassId" value="${teachClass.id!}">
		<h3 class="box-title">${teachClass.name!}</h3>
		<div class="filter-item filter-item-right">
			<a class="btn btn-blue js-addStudent" href="javascript:" onclick="backTeachClassList()">返回</a>
			<#if isView?default('0')=='0'>
			<a class="btn btn-blue js-addStudent" href="javascript:" onclick="addTeachStudent()">添加学生</a>
			<a class="btn btn-blue js-clear" href="javascript:" onclick="clearTeachStudent('${teachClass.id!}')">全部清空</a>
			</#if>
		</div>
	</div>
	<#if isView?default('0')=='0'>
	<div style="">
		<@popupMacro.selectMoreStudent clickId="studentName" columnName="学生(多选)" dataUrl="${request.contextPath}/common/div/student/popupData" id="studentId" name="studentName" dataLevel="4" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/student/recentData" resourceUrl="${resourceUrl}" handler="saveTeacherIds()">
			<div class="input-group">
				<input type="hidden" id="studentId" name="studentId" value="">
				<input type="hidden" id="studentName" class="form-control" style="width:100%;" value="">
			</div>
		</@popupMacro.selectMoreStudent>
	</div>
	</#if>
	<div class="box-body">
		<table class="table table-bordered table-striped table-hover table-student">
			<thead>
				<tr>
					<th>序号</th>
					<th>姓名</th>
					<th>学号</th>
					<th>行政班级</th>
					<th>班内编号</th>
					<#if isView?default('0')=='0'>
					<th>操作</th>
					</#if>
				</tr>
			</thead>
			<tbody>
				<#if dtos?exists && dtos?size gt 0>
					<#list dtos as dto>
					<tr>
						<td>${dto_index+1}</td>
						<td>${dto.student.studentName!}</td>
						<td>${dto.student.studentCode!}</td>
						<td>${dto.className!}</td>
						<td>${dto.student.classInnerCode!}</td>
						<#if isView?default('0')=='0'>
						<td><a class="js-del" href="javascript:" onclick="deleteStu('${teachClass.id!}','${dto.student.id!}')">删除</a></td>
						</#if>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>

<script>
	var parmUrl='acadyearSearch=${searchDto.acadyearSearch!}&semesterSearch=${searchDto.semesterSearch!}&gradeIds=${searchDto.gradeIds!}&showTabType=${searchDto.showTabType!}&teachClassName=${searchDto.teachClassName!}&studentName=${searchDto.studentName!}&teacherName=${searchDto.teacherName!}';
	function backTeachClassList(){
		var url =  '${request.contextPath}/basedata/teachclass/head/page?'+parmUrl
		$("#tablistDiv").load(encodeURI(url));
	}
	function freshByTeachClassId(teachClassId,useMaster){
		var url =  '${request.contextPath}/basedata/teachclass/studentList/page?teachClassId='+teachClassId+'&useMaster='+useMaster+'&'+parmUrl;
		$("#tablistDiv").load(encodeURI(url));
	}
	<#if isView?default('0')=='0'>
	function deleteStu(teachClassId,studentId){
		if(confirm('您确认要删除该学生吗？')){
			$.ajax({
				url:'${request.contextPath}/basedata/teachclass/deleteClassStudent',
				data:{'teachClassId':teachClassId,'studentId':studentId},
				dataType:'json',
				success: function(data){
					var jsonO = data;
			 		if(!jsonO.success){
			 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 			return;
			 		}else{
			 			layer.closeAll();
						layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
					  	freshByTeachClassId(teachClassId,'1');
					}
				}
			});
		}
	}
	function addTeachStudent(){
		$("#studentName").click();
	}
	
	function saveTeacherIds(){
		var teachClassId=$("#teachClassId").val();
		var studentIds=$("#studentId").val();
		$.ajax({
			url:'${request.contextPath}/basedata/teachclass/saveSomeStudent', 
			data:{id:teachClassId,studentIds:studentIds},
		    dataType:'json',
		    type:'post',
		    success:function(data) {
		    	layer.closeAll();
		    	if(data.success){
		    		layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
		            freshByTeachClassId(teachClassId,'1');
		        }else{
		         	layerTipMsg(data.success,"失败",data.msg);
		         	 //去除选中数据
		       		 $("#studentId").val("");
		        }
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	
	function clearTeachStudent(teachClassId){
		if(confirm('您确认要清除该班级学生，清空后不能撤销，确定吗？')){
			$.ajax({
				url:'${request.contextPath}/basedata/teachclass/deleteAllStudent', 
				data:{id:teachClassId},
			    dataType:'json',
			    type:'post',
			    success:function(data) {
			    	layer.closeAll();
			    	if(data.success){
			    		layer.msg("删除成功！", {
							offset: 't',
							time: 2000
						});
			            freshByTeachClassId(teachClassId,'1');
			        }else{
			         	layerTipMsg(data.success,"失败",data.msg);
			        }
			        
			    },
			    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});	
		}
	}
	</#if>
</script>