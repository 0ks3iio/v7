<div id="indexContent">
</div>
<script type="text/javascript">
	$(function(){
		gotoTeachClassItem();
	});
	function gotoTeachClassItem(){
		var url =  '${request.contextPath}/basedata/teachclass/indexItem/page?showTabType=${showTabType!}';
		$("#indexContent").load(url);
	}
</script>