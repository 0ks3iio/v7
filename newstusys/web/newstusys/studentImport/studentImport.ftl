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
<a href="javascript:void(0)" onclick="goBackToStu()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${request.contextPath}/static" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">
   <form id="remarkform" name="remarkform" method="post">
   <div class="import-step clearfix">
		<iframe id="hiddenIframe2" name="hiddenIframe2" style="display:none"></iframe>
		<div id="testDiv" style="display:none;"></div>
		<span class="import-step-num">✔</span>
		<div class="import-content">
			<p>下载对照文件：<a href="javascript:void(0);" id="remarkDocBtn">${businessName!}行政区划对照文件</a></p>
		</div>
	</div>
	</form>
</@import.import>
<script>
   $(function(){
   		$("#templateParams").val('${importParams!}');
   		
   		$('#remarkBtn,#remarkDocBtn').on('click',function(){
   			var remarkform=document.getElementById('remarkform');
			if(remarkform){
				remarkform.action="${request.contextPath}/newstusys/sch/student/studentImport/remarkDoc";
				remarkform.target="hiddenIframe2";
				remarkform.submit();
			}
   		});
   });
   
   function goBackToStu(){
    $('.model-div').load('${request.contextPath}/newstusys/sch/student/studentadmin');
   }
</script>

<#--**********End********-->
</div>
