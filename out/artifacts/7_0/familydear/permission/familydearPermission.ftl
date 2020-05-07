<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="permissionForm" >
<div class="table-container-body">
	<input type="hidden" id="classIdVal" value="">
	<table class="table table-striped layout-fixed">
		<thead>

            <tr>
                <th width="15%" style="text-align:center;">角色</th>
                <th width="65%" style="text-align:center;">人员</th>
                <th width="20%" style="text-align:center;">操作</th>
            </tr>
		</thead>
		<tbody>
        <tr>


            <td align="center" ><#if permission.permissionType?default('1')=='1'>报名审核
            <#elseif permission.permissionType?default('1')=='2'>年度文件管理<#elseif permission.permissionType?default('1')=='3'>轮次管理
            <#elseif permission.permissionType?default('1')=='5'>结亲对象管理<#elseif permission.permissionType?default('1')=='7'>三进两联管理
                <#elseif permission.permissionType?default('1')=='4'>信息填报管理
                <#elseif permission.permissionType?default('1')=='6'>部门活动管理
                <#elseif permission.permissionType?default('1')=='8'>访亲人员名单管理
            <#else>权限管理</#if></td>
			<td align="center"  >${permission.userNames!}</td>

            <td  align="center">
                <input type="hidden" name="permissionType"  value="${permission.permissionType!}" />
                <input type="hidden" name="unitId"  value="${permission.unitId!}" />
                <input type="hidden" name="id"  value="${permission.id!}" />
                <input type="hidden" name="creationTime"  value="${(permission.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" />
                <a href="#" class="color-lightblue" onclick="editGrade();">修改</a>
                <div style="display: none;">
				<@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePermission()">
                    <input type="hidden" id="userIds" name="userIds" value="${permission.userIds!}">
                    <input type="text" id="userName" class="form-control" value="${permission.userNames!}">
				</@popup.selectMoreTeacherUser>
                </div>
            </td>
        </tr>
		</tbody>
	</table>
</div>
</form>
<script type="text/javascript">

    function editGrade(){

        $('#userName').click();
    }
isSubmit = false;
function savePermission(){
	if(isSubmit){
        return;
    }
	var userIds = $("#userIds").val();
	if(userIds != ''){
		var arr = userIds.split(",");
		if(arr.length > 100){
            layerTipMsgWarn("保存失败","人员最多100人！");
			return;
		}
	}
    var userIds = $("#userIds").val();
    var userName = $("#userName").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/familydear/permissionSet/save",
			data:$("#permissionForm").serialize(),
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
                    layer.msg("保存失败", {
                        offset: 't',
                        time: 2000
                    });
		 		}else{

                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
    			}
                showPermList();
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
function showPermList(){
    var   url =  '${request.contextPath}/familydear/permissionSet/list.action?permissionType=${permission.permissionType!}';
    $("#showPermissionList").load(url);
}
</script>