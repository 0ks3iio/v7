<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var classId=$("#classId").val();
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var params='{"acadyear":"'+acadyear+'","semester":"'+semester+'","classId":"'+classId+'"}';
	dataimport(params);
}

function goBack(){
	var classId=$("#classId").val();
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	if(acadyear==undefined){
		acadyear="";
	}
	if(classId==undefined){
		classId="";
	}
	if(semester==undefined){
		semester="";
	}
	$("#itemShowDivId").load("${request.contextPath}/stuwork/evaluation/stu/list/page?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
}
</script>

<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<input type="hidden" name="acadyear"   id="acadyear" value="${acadyear!}">
				<div class="filter-content">
				${acadyear!}
				</div>
			</div>
			<input type="hidden" name="semester" id="semester" value="${semester!}">
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
				${semester!}
				</div>
			</div>
			<input type="hidden" name="classId" id="classId" value="${classId!}">
			<div class="filter-item">
				<label for="" class="filter-name">班级：</label>
				<div class="filter-content">
				${className!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>