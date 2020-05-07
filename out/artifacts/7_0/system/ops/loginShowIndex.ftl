<div id="regionIndex" >
</div>
<div id="pageSetIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	showRegionIndex();
});
function showRegionIndex(){
	var url =  '${request.contextPath}/system/ops/loginDomain/index/page';
	$("#regionIndex").load(url);
	$("#regionIndex").show();
	$("#pageSetIndex").hide();
}
function showLoginSet(unitId){
	var url =  '${request.contextPath}/ops/loginSet/domain/page?unitId='+unitId;
	$("#pageSetIndex").load(url);
	$("#pageSetIndex").show();
	$("#regionIndex").hide();
}

</script>