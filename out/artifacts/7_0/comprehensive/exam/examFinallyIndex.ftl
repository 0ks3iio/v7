<div  id="itemShowDivId">

</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
	    var url = '${request.contextPath}/comprehensive/exam/finally/list';
        $("#itemShowDivId").load(url);
	}
</script>