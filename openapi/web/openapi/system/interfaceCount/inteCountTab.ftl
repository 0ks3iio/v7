<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">图形展示</a></li>
			<li role="presentation"><a href="#tabList" onclick="showTab('2')" role="tab" data-toggle="tab">报表展示</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabList" class="tab-pane active" role="tabpanel">

			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	showTab('1');
});
function showTab(type){
	if(type=='1'){
		var url =  '${request.contextPath}/developer/interface/count/showOpenapiCount/page?isBackstage=true&type=1';
		$("#tabList").load(url);
	}else {
		var url =  '${request.contextPath}/developer/interface/count/showOpenapiCount/page?isBackstage=true&type=2';
		$("#tabList").load(url);
	}
}
</script>
