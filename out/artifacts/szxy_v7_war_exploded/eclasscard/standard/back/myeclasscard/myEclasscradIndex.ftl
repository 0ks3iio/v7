<div id="myEclasscardDiv">

</div>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">
var lastEclasscardId = '';
$(function(){
	myEclasscardList();
});
function myEclasscardList(){
	var url =  '${request.contextPath}/eclasscard/standard/myclasscard/list';
	$("#myEclasscardDiv").load(url);
}
function showPVDetail(id,type,tabType,changeable){
	var url =  '${request.contextPath}/eclasscard/standard/pvalbum/list?folderId='+id+'&tabType='+tabType+'&type='+type+'&changeable='+changeable;
	$("#myEclasscardDiv").load(url);
}

function myEclasscardEdit(id,tabType,subTabType){
	lastEclasscardId = id;
	if(!subTabType){
		subTabType = '1';
	}
	var url =  '${request.contextPath}/eclasscard/standard/myclasscard/tab?id='+id+'&tabType='+tabType+'&subTabType='+subTabType;
	$("#myEclasscardDiv").load(url);
}

function backFolderIndex(tabType,subTabType){
	myEclasscardEdit(lastEclasscardId,tabType,subTabType)
	hidenBreadBack();
	setTimeout(function(){
		showBreadBack(myEclasscardList,true,"我的班牌");
	},100); 
}
</script>
