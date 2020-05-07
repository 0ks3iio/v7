<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">类型：</span>
				<div class="filter-content">
					<select name="" id="source" class="form-control" onchange="showRoleList()">
					    <option value="">请选择</option>
						<option value="1">默认</option>
						<option value="2">其它</option>
					</select>
				</div>
			</div>
			<#--  
			<div class="filter-item">
				<span class="filter-name">角色名称：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="roleName" onchange="showRoleList()" >
				</div>
			</div>
			-->
			<div class="filter-item">
				<span class="filter-name">第三方ap：</span>
				<div class="filter-content">
					<select name="searchServer" id="serverId" class="form-control" onChange="showRoleList()">
						    <option value="">---请选择---</option>
							<#if serverList?exists && (serverList?size>0)>
			                    <#list serverList as server>
				                     <option value="${server.id!}">${server.name!}</option>
			                    </#list>
		                    </#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn">登记角色</a>
			</div>
		</div>
		<div class="table-container" id="showUserList">
		
		
		</div>
	</div>
</div>
<div class="layer layer-role-detailedId ">

</div><!-- E 登记记录 -->


<script>
    
    $(document).ready(function(){
	    $("#roleName").keypress(function () {
				displayResult();
	    });
	    showRoleList();
   })
  
  function showRoleList(){
	var source = $("#source").val();
	var roleName = $("#roleName").val();
	var serverId = $("#serverId").val();
	powerName =  encodeURI(roleName);
	var url =  '${request.contextPath}/system/ap/role/findRoleList/page?source='+source+'&serverId='+serverId+'&roleName='+roleName;
	$(".table-container").load(url);
  }
  
  function displayResult(){	
		var x;
        if(window.event){
        	 // IE8 以及更早版本
        	x=event.keyCode;
        }else if(event.which){
        	// IE9/Firefox/Chrome/Opera/Safari
            x=event.which;
        }
        if(13==x){
            showRoleList();
        }
    }
    //登记角色
     $('.filter .js-checkIn').on('click', function(e){
		$('.layer-role-detailedId').load("${request.contextPath}/system/ap/role/register/role/page",function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '登记角色',
					area: '500px',
					btn: ['确定','取消'],
					yes:function(index,layero){
				       saveRole("${request.contextPath}/system/ap/role/saveRole");
		            },
					content: $('.layer-role-detailedId')
		           })
         });
	})
    //角色的保存
    var isSubmit=false;
	function saveRole(contextPath){
	    if(isSubmit){
			return;
		}
		isSubmit = true;
	    var check = checkValue('.layer-role-detailedId');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
	  $.ajax({
	        url:contextPath,
	        data:dealDValue(".layer-role-detailedId"),
	        clearForm : false,
	        resetForm : false,
	        dataType:'json',
	        contentType: "application/json",
	        type:'post',
	        success:function (data) {
	            isSubmit = false;
	            if(data.success){
	               showSuccessMsgWithCall(data.msg,roleIndex);
	            }else{
	                showErrorMsg(data.msg);
	            }
	        }
	  })
	}
</script>