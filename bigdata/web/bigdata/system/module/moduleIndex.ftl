<div class="filter-made mb-10">
	<div class="filter-item">
		 <span class="filter-name">目录：</span>
		<div class="form-group">
            <input type="hidden" id="parentId" value="" />
           	<select  class="form-control" name="parentId" onChange="changeParentId(this.value);">
            	<option value="">全部</option>
            	<option value="00000000000000000000000000000000">目录(一级模块)</option>       
            	<#if  moduleList?exists &&moduleList?size gt 0>
				<#list moduleList as module>
					<#if module.type =="dir">
						<option value="${module.id!}">${module.name!}</option>
					</#if>
				</#list>
			</#if>
    	</select>
        </div>
	</div>
	<div class="filter-item">
		 <span class="filter-name">用户类型：</span>
		<div class="form-group">
            <input type="hidden" id="userType" value="" />
           	<select  class="form-control" name="userType" onChange="changeUserType(this.value);">
            	<option value="">全部</option>
            	<option value="1">普通用户</option>       
            	<option value="0">后台用户</option>       
        	</select>
        </div>
	</div>
      <div class="filter-item">
		 <span class="filter-name">是否启用：</span>
		<div class="form-group">
           	 	<input type="hidden" id="mark" value="" />
	           	<select  class="form-control" name="mark" onChange="changeMark(this.value);">
	            	<option value="">全部</option>
	            	<option value="1">启用</option>       
	            	<option value="0">停用</option>       
	        	</select>
        </div>
	</div>
	<div class="filter-item">
		 <span class="filter-name">是否授权：</span>
		<div class="form-group">
            <input type="hidden" id="common" value="" />
           	<select  class="form-control" name="common" onChange="changeCommon(this.value);">
            	<option value="">全部</option>
            	<option value="1">通用</option>       
            	<option value="0">需授权</option>       
        	</select>
        </div>
	</div>
</div>
<div id="moduleListDiv" class="box box-default no-margin clearfix  js-height scrollBar4"></div>
<script type="text/javascript">
	function moduleList(){
		var parentId=$("#parentId").val();
		var userType=$("#userType").val();
		var mark=$("#mark").val();
		var common=$("#common").val();
		var url =  "${request.contextPath}/bigdata/module/list?parentId="+parentId+"&userType="+userType+"&mark="+mark+"&common="+common;
		$("#moduleListDiv").load(url);
	}
	
	function loadModuleUserData(moduleId){
		if(!moduleId)
			moduleId=$("#currentModuleId").val();
		$("#currentModuleId").val(moduleId);
		router.go({
	        path: '/bigdata/module/moduleUserList?moduleId='+moduleId,
	        name: '授权',
	        level: 3
	    }, function () {
			var url =  "${request.contextPath}/bigdata/module/moduleUserList?moduleId="+moduleId;
			$("#contentDiv").load(url);
    	});
		
	}

	function changeParentId(parentId){
		$("#parentId").val(parentId);
		moduleList();
	}

	function changeUserType(userType){
		$("#userType").val(userType);
		moduleList();
	}
	
	function changeMark(mark){
		$("#mark").val(mark);
		moduleList();
	}
	
	function changeCommon(common){
		$("#common").val(common);
		moduleList();
	}

	$(document).ready(function(){
		moduleList();
	});
</script>