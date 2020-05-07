<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="" id="gradeId" class="form-control" onChange="searchList()">
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
					<select name="" id="classId" class="form-control" onChange="searchList()">
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
				<a class="btn btn-blue" onclick="saveFinally();">换算成绩</a>
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
							<th>总分</th>
							<th>排名</th>
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
									<#if courseList?exists && courseList?size gt 0>
										<#list courseList as course>
											<td>${item.scoreMap[course.id]!}</td>
										</#list>
									</#if>
									<td>${item.toScore!}</td>
									<td>${item.ranking!}</td>
								</tr>
							</#list>
						<#else>
							<tr><td colspan="16" align="center">暂无数据</td></tr>
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
		url='${request.contextPath}/comprehensive/exam/finally/list'+c2;
		$("#itemShowDivId").load(url);
	}
	
	var isSubmit=false;
	function saveFinally(){
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		if(isSubmit){
			isSubmit=false;
			return;
		}
		isSubmit = true;
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/comprehensive/exam/finally/saveFinally',
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
				isSubmit =false;
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>
