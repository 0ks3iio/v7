<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">用户授权</a></li>
			<li role="presentation"><a href="#tabList" onclick="showTab('2')" role="tab" data-toggle="tab">角色授权</a></li>
			<li role="presentation"><a href="#tabList" onclick="showTab('3')" role="tab" data-toggle="tab">用户权限查看</a></li>
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
	var url =  '${request.contextPath}/system/user/power/index/page?type='+type;
	$(".tab-content #tabList").load(url);
}
</script>