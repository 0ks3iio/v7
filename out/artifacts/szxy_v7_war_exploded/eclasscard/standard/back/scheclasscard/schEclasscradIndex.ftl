<div id="schEclasscardDiv">

</div>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">		
var ue;
$(function(){
	schEclasscardTab('1');
});
function schEclasscardTab(tabType){
	var url =  '${request.contextPath}/eclasscard/standard/schclasscard/tab?tabType='+tabType;
	$("#schEclasscardDiv").load(url);
}

function showPVDetail(id,type,tabType,changeable){
	var url =  '${request.contextPath}/eclasscard/standard/pvalbum/list?folderId='+id+'&tabType='+tabType+'&type='+type+'&changeable='+changeable;
	$("#schEclasscardDiv").load(url);
}

function backFolderIndex(tabType){
	hidenBreadBack();
	schEclasscardTab(tabType);
}
</script>
