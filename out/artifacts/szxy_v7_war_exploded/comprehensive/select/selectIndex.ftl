<div  id="itemShowDivId">

</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
	    var url = '${request.contextPath}/comprehensive/select/gradeIndex/page';
        $("#itemShowDivId").load(url);
	}
</script>