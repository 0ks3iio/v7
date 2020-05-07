<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<input type="hidden" id="currentModuleId" value="" />
	<div  id="module-div" class="filter-item active"  onclick="showList('module');">
        <span>模块管理(授权)</span>
    </div>
    <div id="role-authority-div" class="filter-item" onclick="showList('role-authority');">
        <span>用户组管理(授权)</span>
    </div>
    <div id="user-auth-div" class="filter-item" onclick="showList('user-auth');">
        <span>后台用户管理</span>
    </div>
    <div id="user-query-div" class="filter-item" onclick="showList('user-query');">
        <span>用户权限查询</span>
    </div>
</div>
<div class="box box-default clearfix no-margin" id="contentDiv"></div>
<script type="text/javascript">
	function showList(type){
		var url;
		if(!type)
			type=$("#type").val();
		$("#type").val(type);
	    if(type =="module"){
	    	$('#module-div').addClass('active').siblings().removeClass('active');
	    	router.go({
		        path: '/bigdata/module/index',
		        name: '模块管理',
		        level: 2
		    }, function () {
				url =  "${request.contextPath}/bigdata/module/index";
				$("#contentDiv").load(url);
	    	 });
	    }else if (type == "role-authority"){
	    	$('#role-authority-div').addClass('active').siblings().removeClass('active');
	    	router.go({
		        path: '/bigdata/role/index',
		        name: '用户组管理',
		        level: 2
		    }, function () {
				url =  "${request.contextPath}/bigdata/role/index";
				$("#contentDiv").load(url);
	    	 });
	    }else if (type == "user-auth"){
	    	$('#user-auth-div').addClass('active').siblings().removeClass('active');
	    	router.go({
		        path: '/bigdata/userauth/index',
		        name: '后台用户管理',
		        level: 2
		    }, function () {
				url =  "${request.contextPath}/bigdata/userauth/index";
				$("#contentDiv").load(url);
	    	 });
	    }else if (type == "user-query"){
	    	$('#user-query-div').addClass('active').siblings().removeClass('active');
	    	router.go({
		        path: '/bigdata/authority/userquery',
		        name: '用户权限查询',
		        level: 2
		    }, function () {
				url =  "${request.contextPath}/bigdata/authority/userquery";
				$("#contentDiv").load(url);
	    	 });
	    }
	}
	
	$(document).ready(function(){
		showList('module');
	});
</script>