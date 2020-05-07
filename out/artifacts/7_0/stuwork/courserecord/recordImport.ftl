<div id="importStuDiv">
<#--**********Start********-->
<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
    	$('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params='${importParams!}';
        dataimport(params);
    }
</script>
<#--a href="javascript:void(0)" onclick="goBackTo()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a-->
<a class="btn btn-blue mt7" onClick="goBackTo()" href="javascript:void(0)">返回</a>
<div style="height: 10px"></div>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${request.contextPath}/static" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>导入信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
				${acadyear!}
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
				<#if semester?default("")=="1">第一学期<#else>第二学期</#if>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">日期：</label>
				<div class="filter-content">
				${queryDate!}
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>
<script>
   function goBackTo(){
    	<#if type?default('1') == '1'>
    		var url =  '${request.contextPath}/stuwork/courserecord/myCourseHead?queryDate=${queryDate!}&period=${period!}';
    		$('#itemShowDivId').load(url);
    	<#else>
    		var url = '${request.contextPath}/stuwork/courserecord/nightCoursePage?queryDate=${queryDate!}&gradeId=${gradeId!}';
    		$('.model-div').load(url);
    	</#if>
   }
</script>
<#--**********End********-->
</div>
