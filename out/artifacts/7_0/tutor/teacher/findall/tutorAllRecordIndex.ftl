<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">导师记录查询</a></li>
			<li role="presentation"><a href="#tabList" onclick="showTab('2')" role="tab" data-toggle="tab">导师记录汇总</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabList" class="tab-pane active" role="tabpanel">

			</div>
		</div>
	</div>
</div>
<div class="layer layer-tutor-detailedId ">

</div><!-- E 登记记录 -->
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
$(function(){
	showTab('1');
});
function showTab(type){
	var url =  '${request.contextPath}/tutor/allrecord/query/head?type='+type;
	$("#tabList").load(url);
}
</script>