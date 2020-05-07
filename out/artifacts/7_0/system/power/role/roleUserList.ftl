<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
			    <th width="8%">
			           选择
		        </th>
				<th>排序号</th>
				<th>账号</th>
				<th>姓名</th>
				<th>用户状态</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody id='list'>
				<#if userRoleDtoList?exists&&userRoleDtoList?size gt 0>
		          	<#list userRoleDtoList as urdto>
					<tr>
					    <td>
						    <label class="pos-rel">
		                    <input name="userId" type="checkbox" class="wp" <#if urdto.isAppoint == 'true'> checked = "true"</#if>
		                     value="${urdto.user.id!}">
		                    <span class="lbl"></span>
		                    </label>
	                    </td>
						<td>${urdto_index+1!}</td>
						<td>${urdto.user.username!}</td>
						<td>${urdto.user.realName!}</td>
						<td><#if urdto.user.userState == 1>正常<#else> 注销  </#if></td>
						<td>${urdto.user.creationTime?string('yyyy-MM-dd')!}</td>
					</tr>
		      	    </#list>
		  	    <#else>
					<tr>
						<td  colspan="88" align="center">
						暂无用户数据
						</td>
					<tr>
		        </#if>
		</tbody>
	</table>
</div>
<div class="table-container-footer">
	<div class="pull-left">
		<button class="btn btn-sm btn-white" onclick= "checkAll()">全选</button>
		<button class="btn btn-sm btn-blue" onclick= "saveUserRole()">保存</button>
	</div> 
	
	<nav class="nav-page no-margin clearfix">
		<ul class="pagination pagination-sm pull-right">
		  <#if userRoleDtoList?exists&&userRoleDtoList?size gt 0>
				<@htmlcom.pageToolBar container="#showUserRoleList" class="noprint"/>
		  </#if>
		</ul>
	</nav>
</div>  <!-- table-container-footer -->
<script>

//全选
function checkAll(){
   var total = $('#list :checkbox').length;
	var length = $('#list :checkbox:checked').length;
	if(length != total){
		$('#list :checkbox').prop("checked", "true");
		$(this).prop("checked", "true");
	}else{
		$('#list :checkbox').removeAttr("checked");
		$(this).removeAttr("checked");
	}
}
//批量委派用户角色
function saveUserRole(){
    var selEle = $('#list :checkbox:checked');
    <#--  
	if(selEle.length<1){
		layerTipMsg(false,"失败",'请先选择想要进行操作的用户！');
		return;
	}
	-->
	var param = new Array();
	for(var i=0;i<selEle.length;i++){
		param.push(selEle.eq(i).val());
	}
	saveUserByIds(param,selEle);
}


function saveUserByIds(idArray,that){
    var allIds = new Array();
   <#if userIdList?exists&&userIdList?size gt 0>
		 <#list userIdList as userId>
		     allIds.push('${userId!}');    	
		 </#list>
   </#if>         	
	var url = '${request.contextPath}/system/ap/role/saveUserRole?roleId='+'${roleId!}';
	var params = {"ids":idArray,
	              "allIds":allIds
	              };
	$.ajax({
		   type: "POST",
		   url: url,
		   data: JSON.stringify(params),
		   contentType: "application/json",
		   dataType: "JSON",
		   success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,roleIndex);
                }else{
                    showErrorMsg(data.msg);
                }
		    }
		});
}

</script>