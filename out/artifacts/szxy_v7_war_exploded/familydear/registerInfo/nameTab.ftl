<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
		    <div class="tab-content" id="bmTblDiv"></div>
		</div>
	</div>
</div>
<script>
$(function(){	
	searchBeginList();
});

function searchBeginList(){
    var url = "${request.contextPath}/familydear/nameList/index/list";
    $('#bmTblDiv').load(url);
}
</script>												