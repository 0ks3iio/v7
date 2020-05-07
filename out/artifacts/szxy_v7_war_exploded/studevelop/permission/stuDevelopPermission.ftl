<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="permissionForm" >
<div class="table-container-body">
	<input type="hidden" id="classIdVal" value="">
	<table class="table table-striped layout-fixed">
		<thead>

            <tr>
                <th width="10%" style="text-align:center;">角色</th>
                <th width="70%" style="text-align:center;">人员</th>
                <th width="20%" style="text-align:center;">操作</th>
            </tr>
		</thead>
		<tbody>
        <tr>

            <td align="center" >管理人员</td>
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
		if(arr.length > 15){
            layerTipMsgWarn("保存失败","管理人员最多15人！");
			return;
		}
	}
    var userIds = $("#userIds").val();
    var userName = $("#userName").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/studevelop/permissionSet/save",
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
    var   url =  '${request.contextPath}/studevelop/permissionSet/list.action?permissionType=${permission.permissionType!}';
    <!--  permissionType == 3 是 基建项目管理员设置 -->
    <#if permission.permissionType?default('') == '3'>
        $(".model-div").load(url);
        <#else>
        $("#showPermissionList").load(url);
    </#if>

}
</script>