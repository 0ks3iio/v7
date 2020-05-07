<div class="box box-default">
	<div class="box-body">
		<div id="itemShowDiv">
			
		</div>
	</div>
</div>

<script>
$(function(){
	load("0");
});

function load(withMaster) {
	url =  '${request.contextPath}/basedata/teachgroup/list/index/page?withMaster=' + withMaster;
    $("#itemShowDiv").load(url);
}
</script>