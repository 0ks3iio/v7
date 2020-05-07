<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
	//每个导入必须实现这个方法
	function businessDataImport(){
		$('#busDataImport').addClass('disabled');
		//处理逻辑　并将参数组织成json格式　调用公共的导入方法
		dataimport('${paramObj!}');
	}

	function goBack(){
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		$("#itemShowDivId").load("${request.contextPath}/comprehensive/exam/first/list?gradeId="+gradeId+"&classId="+classId);
	}
	function findByCon(){
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		$("#itemShowDivId").load("${request.contextPath}/comprehensive/exam/firstImport/main?gradeId="+gradeId+"&classId="+classId);
	}
</script>
<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="gradeId" id="gradeId" class="form-control" onChange="findByCon()">
				<#if gradeList?exists && gradeList?size gt 0>
					<#list gradeList as item>
						<option  value = "${item.id!}" <#if (item.id!) == (gradeId!) >selected="selected"</#if> >${item.gradeName!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select name="classId" id="classId" class="form-control" onChange="findByCon()">
				<#if classList?exists && classList?size gt 0>
					<option value="">---全部---</option>
					<#list classList as item>
						<option value="${item.id!}" <#if item.id==classId?default("")>selected="selected"</#if>>${item.classNameDynamic!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
</div><#--validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}"-->
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">
</@import.import>