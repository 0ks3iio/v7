<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var ctv = $('#coverType').val();
	<#if classesType?default("1")=="3">
		var params='{"classesType":"${classesType!}","coverType":"'+ctv+'"}';
	<#else>
		var semester=$("#semester").val();
		var acadyear=$("#acadyear").val();
		var params='{"classesType":"${classesType!}","semester":"'+semester+'","acadyear":"'+acadyear+'","coverType":"'+ctv+'"}';
	</#if>
	dataimport(params);
}
function goBack(){
	$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputPage?classesType=${classesType!}");
}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<#if classesType?default("1")!="3">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control">
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" >
						<option value="1" <#if semester == 1>selected</#if>>第一学期</option>
						<option value="2" <#if semester == 2>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			</#if>
			<div class="filter-item">
				<span class="filter-name">导入类型：</span>
				<div class="filter-content">
					<select name="coverType" id="coverType" class="form-control">
						<option value="0" selected>新增导入</option>
						<option value="1">覆盖导入</option>
					</select>
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>