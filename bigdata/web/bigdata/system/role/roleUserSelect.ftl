<div class="filter-made mb-10">
	<div class="filter-item">
        <input type="text"  id="username_search" value="${username!}" class="form-control" placeholder="请输入用户名">
	</div>
	<div class="filter-item">
		<div class="form-group">
            <div class="input-group">
                <input type="text" id="realname_search" value="${realname!}" class="form-control" placeholder="请输入真实姓名">
                <a href="javascript:void(0);" class="input-group-addon" onclick="searchRoleUser()" hidefocus="true"><i class="wpfont icon-search"></i></a>
            </div>
        </div>
	</div>
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="loadRoleUserData();">返回</button>
	</div>
</div>
<#if  userRoleList?exists &&userRoleList?size gt 0>
<table class="tables">
	<thead>
		<tr>
			<th>用户名</th>
			<th>真实姓名</th>
			<th>性别</th>
			<th>部门</th>
			<th>单位</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
          	<#list userRoleList as user>
				<tr>
					<td>${user.username!}</td>
					<td>${user.realName!}</td>
					<td>${mcodeSetting.getMcode("DM-XB","${user.sex!}")}</td>
					<td>${user.deptName!}</td>
					<td>${user.unitName!}</td>
					<td>
					<#if user.roleId! =="" >
					<a class="look-over" href="javascript:void(0);" onclick="addUser('${user.userId!}')">
					添加</a>
					<#else>
					已添加
					</#if>
					</td>
				</tr>
      	    </#list>
	</tbody>
</table>
<#else>
<div class="no-data-common">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
		<p class="color-999">
			<#if isResult!  ==1>
				用户不存在,请确认搜索条件是否正确	
			<#else>
				请根据条件搜索用户
			</#if>
		</p>
	</div>
</div>
</#if>
<script>
	
	 $("#username_search").on('blur',function(){
         if(($('#username_search').val() !='')){
            $("#realname_search").val('');
        }
    });
            
     $("#realname_search").on('blur',function(){
         if(($('#realname_search').val() !='')){
            $("#username_search").val('');
        }
    });      
    
    $("#username_search").bind('keydown',function(event){  
	    if(event.keyCode == "13"){  
	      if(($('#username_search').val() !='')){
          	$("#realname_search").val('');
          }
	      searchRoleUser();
	    }  
	});  
	
	$("#realname_search").bind('keydown',function(event){  
	    if(event.keyCode == "13"){  
	     if(($('#realname_search').val() !='')){
            $("#username_search").val('');
         }
	     searchRoleUser();
	    }  
	});

	function addUser(id){
			$.ajax({
	            url:'${request.contextPath}/bigdata/role/addUser',
	            data:{
	               'roleId':'${roleId!}',
		           'userId':id
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
	            	layer.closeAll();
			 		if(!data.success){
			 				showLayerTips4Confirm('error',data.msg);
			 		}else{
			 			showLayerTips('success',data.msg,'t');
						loadRoleUserData();
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	}

	function searchRoleUser() {
		var username = $('#username_search').val();
		var realname = $('#realname_search').val();
		if(username=="" && realname==""){
			showLayerTips('warn','请输入查询条件','t');
			return;
		}
	    var url =  "${request.contextPath}/bigdata/role/roleUserSelect?roleId=${roleId!}&username=" + username + "&realname=" + realname;
	    $("#roleUserListDiv").load(url);
	}
</script>