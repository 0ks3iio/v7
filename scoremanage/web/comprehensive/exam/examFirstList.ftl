<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" onChange="searchList()">
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.id!}" <#if item.id==gradeId>selected</#if>>${item.gradeName!}</option>
		                    </#list>
		                <#else>
		                	<option value="">未设置</option>
	                    </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" onChange="searchList()">
						<#if classList?exists && classList?size gt 0>
							<option value="">---全部---</option>
							<#list classList as item>
								<option value="${item.id!}" <#if item.id==classId?default("")>selected="selected"</#if>>${item.classNameDynamic!}</option>
							</#list>
						<#else>
		                	<option value="">未设置</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a class="btn btn-blue" onclick="saveAll();">保存</a>
				<a class="btn btn-blue" onclick="synFirst();">同步成绩</a>
				<a class="btn btn-blue" onclick="doImport()">导入Excel</a>
			</div>
		</div>
		<div class="table-container">
			<div class="table-container-header">共有<#if scoreList?exists>${scoreList?size}<#else>0</#if>个数据</div>
			<div class="table-container-body">
				<form id="checkForm">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>学号</th>
							<th>姓名</th>
							<th>班级</th>
							<#if courseList?exists && courseList?size gt 0>
							<#list courseList as course>
								<th>${course.subjectName!}</th>
							</#list>
							</#if>
						</tr>
					</thead>
					<tbody>
						<#if scoreList?exists && scoreList?size gt 0>
							<#list scoreList as item>
								<tr>
									<td>${item_index+1}</td>
									<td>${item.studentCode!}</td>
									<td>${item.studentName!}</td>
									<td>${item.className!}</td>
									<input type="hidden" name="scoreList[${item_index}].studentId" value="${item.studentId!}">
									<input type="hidden" name="scoreList[${item_index}].classId" value="${item.classId!}">
									<#if courseList?exists && courseList?size gt 0>
										<#list courseList as course>
											<td><input type="text" id="scoreList${item_index}${course_index}" value="${item.scoreMap[course.id]!}"  maxlength="1" name="scoreList[${item_index}].scoreMap['${course.id}']" style="width:40px" nullable="true"></td>
										</#list>
									</#if>
								</tr>
							</#list>
						<#else>
							<tr><td colspan="14" align="center">暂无数据</td></tr>
						</#if>
					</tbody>
				</table>
				<#if scoreList?exists && scoreList?size gt 0>
				<@htmlcom.pageToolBar container="#itemShowDivId" >
 				 </@htmlcom.pageToolBar>
 				 </#if>
				</form>
			</div>
		</div>
				
	</div>
</div>
<script>
	function searchList(){
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		var c2='?gradeId='+gradeId+'&classId='+classId;
		url='${request.contextPath}/comprehensive/exam/first/list'+c2;
		$("#itemShowDivId").load(url);
	}
	
	var isSubmit=false;
	function synFirst(){
		var gradeId=$("#gradeId").val();
		if(isSubmit){
			return;
		}
		if(gradeId==""){
			layer.tips("请先选择年级", "#gradeId", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		isSubmit = true;
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/comprehensive/exam/first/synFirst',
			data: {'gradeId':gradeId},
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					searchList();
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	function saveAll(){
		if(isSubmit){
			return false;
		}
		isSubmit = true;
		var check = checkValue('#checkForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		var options = {
		       url:'${request.contextPath}/comprehensive/exam/first/saveAll', 
		       dataType : 'json',
		       clearForm : false,
		       resetForm : false,
		       type : 'post',
		       data: {'gradeId':gradeId,'classId':classId},
		       success : function(data){
		 			var jsonO = data;
			 		if(!jsonO.success){
			 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 			isSubmit = false;
			 		}else{
						layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					  	searchList();
	    			}
    			},
    			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	   	 };
		$('#checkForm').ajaxSubmit(options);
	}
	function doImport(){
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		$("#itemShowDivId").load("${request.contextPath}/comprehensive/exam/firstImport/main?gradeId="+gradeId+"&classId="+classId);
	}
</script>
