<div  id="itemShowDivId">

</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
	    var url = '${request.contextPath}/comprehensive/exam/first/list';
        $("#itemShowDivId").load(url);
	}
</script>