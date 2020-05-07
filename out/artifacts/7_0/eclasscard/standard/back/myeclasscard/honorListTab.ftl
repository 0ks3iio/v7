<div id="cc" class="tab-pane" role="tabpanel">
	<div class="clearfix margin-b-10 js-click">
		<span class="fake-btn <#if subTabType == '1'>fake-btn-blue <#else>fake-btn-default</#if> no-radius-right no-border-right" onclick="showHonorDiv('1',this)">学生荣誉</span>
		<span class="fake-btn  <#if subTabType == '2'>fake-btn-blue <#else>fake-btn-default</#if> no-radius-left no-border-left" onclick="showHonorDiv('2',this)">班级荣誉</span>
	</div>
	<div class="" id="listHonorDiv">
		
	</div>
</div>
<script>
$(function(){
	showHonorDiv(${subTabType?default('1')});
});
function showHonorDiv(type,spanThis){
	var url = '';
	if(type=='1'){
		url =  '${request.contextPath}/eclasscard/standard/honor/stulist?eccInfoId=${eccInfoId!}';
		$("#listHonorDiv").load(url);
		$(spanThis).addClass('fake-btn-blue').removeClass('fake-btn-default').siblings().addClass('fake-btn-default').removeClass('fake-btn-blue');
	}else{
		url =  '${request.contextPath}/eclasscard/standard/honor/classlist?eccInfoId=${eccInfoId!}';
		$("#listHonorDiv").load(url);
		$(spanThis).addClass('fake-btn-blue').removeClass('fake-btn-default').siblings().addClass('fake-btn-default').removeClass('fake-btn-blue');
	}
}
</script>