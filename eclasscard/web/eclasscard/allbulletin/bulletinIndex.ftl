<div id="bulletinDiv" class="box box-default">
</div>
<script type="text/javascript">
$(function(){
	bulletinList();
});
function bulletinList(){
	var url =  '${request.contextPath}/eclasscard/bulletin/list';
	$("#bulletinDiv").load(url);
}

</script>