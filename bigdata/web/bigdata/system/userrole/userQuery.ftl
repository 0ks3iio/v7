<div class="filter-made mb-10">
	<div class="filter-item">
        <input type="text"  id="username_search" value="${username!}" class="form-control" placeholder="请输入用户名">
	</div>
	<div class="filter-item">
		<div class="form-group">
            <div class="input-group">
                <input type="text" id="realname_search" value="${realname!}" class="form-control" placeholder="请输入真实姓名">
                <a href="javascript:void(0);" class="input-group-addon" onclick="searchUser()" hidefocus="true"><i class="wpfont icon-search"></i></a>
            </div>
        </div>
	</div>
</div>
<#if  userList?exists &&userList?size gt 0>
<table class="tables">
	<thead>
		<tr>
			<th>用户名</th>
			<th>真实姓名</th>
			<th>性别</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
          	<#list userList as user>
				<tr>
					<td>${user.username!}</td>
					<td>${user.realName!}</td>
					<td>${mcodeSetting.getMcode("DM-XB","${user.sex!}")}</td>
					<td>
					<a class="look-over" href="javascript:void(0);" onclick="showModuleList('${user.id!}',${user.userType!})">
					查看</a>
					</td>
				</tr>
      	    </#list>
	</tbody>
</table>
<#else>
<div class="no-data-common height-calc-42">
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
<div class="layer layer-user-module-list" id="userModuleShowDiv"></div>
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
	      searchUser();
	    }  
	});  
	
	$("#realname_search").bind('keydown',function(event){  
	    if(event.keyCode == "13"){  
	     if(($('#realname_search').val() !='')){
            $("#username_search").val('');
         }
	     searchUser();
	    }  
	});  
	
	function showModuleList(userId,userType) {
		$.ajax({
	            url: '${request.contextPath}/bigdata/authority/userModuleDetail',
	            type: 'POST',
	            data: {
	            	userId:userId,
	            	userType:userType
	            },
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#userModuleShowDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#userModuleShowDiv').html(response);
	            }
            });
            layer.open({
            	type: 1,
            	shade: .6,
            	title: '用户模块列表',
	            end:function(){
	               	$('#userModuleShowDiv').empty();
	             },
	            area: ['480px', '500px'],
	            content: $('#userModuleShowDiv')
	        });
	}

	function searchUser() {
		var username = $('#username_search').val();
		var realname = $('#realname_search').val();
		if(username=="" && realname==""){
			showLayerTips('warn','请输入查询条件','t');
			return;
		}
	    var url =  "${request.contextPath}/bigdata/authority/userquery?username=" + username + "&realname=" + realname;
	    $("#contentDiv").load(url);
	}
</script>