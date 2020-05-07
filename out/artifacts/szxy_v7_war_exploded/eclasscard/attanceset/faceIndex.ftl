<div class="clearfix margin-b-10 js-click">
	<span class="fake-btn <#if subTabType == '1'>fake-btn-blue <#else>fake-btn-default</#if> no-radius-right no-border-right" onclick="showFaceDiv('1',this)">照片上传</span>
	<span class="fake-btn  <#if subTabType == '2'>fake-btn-blue <#else>fake-btn-default</#if> no-radius-left no-border-left" onclick="showFaceDiv('2',this)">照片下发</span>
</div>
<div class="" id="listFaceDiv">
	
</div>
<script>
$(function(){
	showFaceDiv(${subTabType?default('1')});
});
function showFaceDiv(type,spanThis){
	var url = '';
	if(type=='1'){
		url =  '${request.contextPath}/eclasscard/face/student/tab';
		$("#listFaceDiv").load(url);
		$(spanThis).addClass('fake-btn-blue').removeClass('fake-btn-default').siblings().addClass('fake-btn-default').removeClass('fake-btn-blue');
	}else{
		url =  '${request.contextPath}/eclasscard/face/lower/hair';
		$("#listFaceDiv").load(url);
		$(spanThis).addClass('fake-btn-blue').removeClass('fake-btn-default').siblings().addClass('fake-btn-default').removeClass('fake-btn-blue');
	}
}
</script>