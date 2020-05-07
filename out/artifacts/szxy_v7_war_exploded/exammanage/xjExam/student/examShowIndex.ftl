<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/bootstrap.css" />
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/font-awesome.css" />
<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
<script src="${request.contextPath}/static/js/tool.js"></script>
<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<script src="${request.contextPath}/static/js/desktop.js"></script>
<script src="${request.contextPath}/static/components/layer/layer.js"></script>

<div id="examIndex" > 
</div>
<div id="showExamIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	examIndex();
});
function examIndex(){
	var url =  '${request.contextPath}/examinfo/stu/index';
	$("#examIndex").load(url);
	$("#examIndex").show();
	$("#showExamIndex").hide();
}
function showExamIndex(studentName,admission,examType){
	var url =  '${request.contextPath}/examinfo/stu/showSeatList?studentName='+studentName+'&admission='+admission+'&examType='+examType;
	$("#showExamIndex").load(url);
	$("#showExamIndex").show();
	$("#examIndex").hide();
}

</script>