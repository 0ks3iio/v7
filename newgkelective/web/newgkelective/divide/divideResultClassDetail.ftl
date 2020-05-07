<#--<a href="javascript:void(0);" onclick="goback()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<#--<input type="hidden" value="${divideClass.id!}" id="c">-->
<input type="hidden" value="${divideClass.divideId!}" id="divideId">
<input type="hidden" value="${divideClass.classType!}" id="classType">
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${divideClass.className!}学生名单</h3>
	</div>
	<div class="box-body">
		<div class="filter-item">
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<select name="" id="divideClassId" class="form-control" onChange="toClassDetail();">
				<#if classList?exists && classList?size gt 0>
				    <#list classList as item>
					    <option value="${item.id!}" <#if item.id==divideClass.id?default("")>selected="selected"</#if>>${item.className!}</option>
					</#list>
				</#if>
				</select>
			</div>
		</div>
		<#if state?default("")=="0">
		<#--<div class="filter-item filter-item-right">
			<a align="right" type="button" class="btn btn-sm btn-white" href="javascript:void(0);" onclick="toStudent('')">调整学生</a>
		</div>-->
		</#if>
	</div>
	<div class="box-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th width="5%">序号</th>
					<th width="12%">学号</th>
					<th width="10%">姓名</th>
					<th width="5%">性别</th>
					<th width="10%">原行政班</th>
					<th>现属班级</th>
					<#--<th>操作</th>-->
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
					<#list dtoList as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.studentCode!}</td>
						<td>${item.studentName!}</td>
						<td>${mcodeSetting.getMcode("DM-XB","${item.sex}")}</td>
						<td>${item.oldClassName!}</td>
						<td>${item.nowClassName!}</td>
						<#--<td><a href="javascript:void(0);" onclick="toStudent()">调整</a></td>-->
					</tr>
					</#list>
				<#else>
					<tr><td colspan="6" align="center">暂无数据</td></tr>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
	showBreadBack(goback,false,"分班结果");
	function toStudent(){
		var divideClassId=$("#divideClassId").val();
		var divideId=$("#divideId").val();
		var classType=$("#classType").val();
		$("#showList").load("${request.contextPath}/newgkelective/"+divideId+"/divideClass/toStudentIndex?classType="+classType+"&divideClassId="+divideClassId+"&isback=true");
	}
	function goback(){
		var divideClassId=$("#divideClassId").val();
		var divideId=$("#divideId").val();
		var url='${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList?divideClassId='+divideClassId+'&groupType=${groupType!}';
		$("#showList").load(url);
	}
	function toClassDetail(){
		var divideId=$("#divideId").val();
		var divideClassId=$("#divideClassId").val();
		var url = '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassDeatil?divideClassId='+divideClassId;
		$("#showList").load(url);
	}
</script>
