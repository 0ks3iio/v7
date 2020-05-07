<ul class="nav nav-tabs nav-tabs-1 nav-tabs-top-15">
 	<li  class="active" onclick="loadRoleModuleData()">
 		<a data-toggle="tab" >模块信息</a>
 	</li>
 	<li  class="" onclick="loadRoleUserData()">
 		<a data-toggle="tab" >授权信息</a>
 	</li>
</ul>
<div class="tab-content slimScrollBar-made">
	<div id="moduleListDiv" class="tab-pane height-1of1">
	</div>
	<div id="roleUserListDiv" class="tab-pane height-1of1">
	</div>
</div>
<script>
	function loadRoleModuleData(){
			$('#${roleId!}').addClass('active').siblings().removeClass('active');
			$('#moduleListDiv').addClass('active').siblings().removeClass('active');
			var url =  "${request.contextPath}/bigdata/role/roleModuleList?roleId=${roleId!}";
			$("#moduleListDiv").load(url);
	}

	function loadRoleUserData(){
			$('#roleUserListDiv').addClass('active').siblings().removeClass('active');
			var url =  "${request.contextPath}/bigdata/role/roleUserList?roleId=${roleId!}";
			$("#roleUserListDiv").load(url);
	}
	
	$(document).ready(function(){
		loadRoleModuleData();
	});
</script>