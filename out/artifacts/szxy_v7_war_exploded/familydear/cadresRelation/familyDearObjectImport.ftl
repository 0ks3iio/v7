<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var params="{'acadyear':'${acadyear!}','semester':'${semester!}'}";
	dataimport(params);
}

function goBack(){
    $(".model-div").load("${request.contextPath}/familydear/cadresRelation/cadresRelationManagerIndex");
}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
✔  &nbsp;&nbsp;&nbsp;     导入表格中字段类别为多选，有多个类别时，用" , " 分开。导入类别如下：</br>党员，四老人员，低保户，困难户，困难家庭，老党员，农民，贫困党员，贫困户，贫困家庭，其他，其他困难，其他困难群体，其他困难群众，其他人员，群众，文化程度不高，一般户，宗教人士
</@import.import>