<#import "/fw/macro/popupMacro.ftl" as popup />
		<input type="hidden" id="placeIdVal" value="">
		<table class="table table-striped layout-fixed">
			<thead>
				<tr>
					<th>场地代码</th>
					<th>场地名称</th>
					<th>关联场地标识</th>
					<th width="">关联场地名称</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody>
				<#if placeDtos?exists&&placeDtos?size gt 0>
				    <#list placeDtos as item>
					<tr>
						<td>${item.placeCode!}</td>
						<td>${item.name!}</td>
						<td id="relatedIdShow${item.id!}">${item.relatedId!}</td>
						<td>
							<input type="text" id="relatedName${item.id!}" class="form-control" value="${item.relatedName!}"  onclick="editTeaId('${item.id!}')">
						</td>
						<td><a href="javascript:void(0);" onclick="deleteRelated('${item.id!}')">清除</a></td>
					</tr>
				    </#list>
				<#else>
					<tr>
						<td  colspan="5" align="center">
						暂无数据
						</td>
					<tr>
				</#if>
			</tbody>
		</table>
<div style="display: none;">
	<@popup.popup_div clickId="relatedName" columnName="第三方场地(单选)" dataUrl="${request.contextPath}/officework/classroom/popupData"  id="relatedId" name="relatedName" dataLevel="1" type="danxuan" resourceUrl="${resourceUrl}" recentDataUrl="${request.contextPath}/officework/classroom/recentData"  handler="saveRelated()" popupType="one">
		<input type="hidden" id="relatedId" value="">
		<input type="text" id="relatedName" class="form-control" value="">
	</@popup.popup_div>
</div>
<script type="text/javascript">
function editTeaId(placeId){
	var relatedId=$("#relatedIdShow"+placeId).text();
	var relatedName=$("#relatedName"+placeId).val();
	$('#relatedName').val(relatedName);
	$('#relatedId').val(relatedId);
	$('#placeIdVal').val(placeId);
	$('#relatedName').click();
}
isSubmit = false;
function saveRelated(){
	if(isSubmit){
        return;
    }
    var placeId = $("#placeIdVal").val();
    var relatedId = $("#relatedId").val();
    var relatedName = $("#relatedName").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/officework/calss/attance/placeset/save",
			data:{placeId:placeId,relatedId:relatedId,relatedName:relatedName},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"设置失败",data.msg);
		 		}else{
					$("#relatedIdShow"+placeId).text(relatedId);
					$("#relatedName"+placeId).val(relatedName);
		 			layer.msg("设置成功");
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

function deleteRelated(placeId){
	var relatedIdShow = $("#relatedIdShow"+placeId).text();
	if(relatedIdShow == ''){
		layer.msg("没有需要清除的关联场地");
		return;
	}
	var options = {
			url : "${request.contextPath}/officework/calss/attance/placeset/delete",
			data:{placeId:placeId},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"清除失败",data.msg);
		 		}else{
		 			$("#relatedIdShow"+placeId).text('');
					$("#relatedName"+placeId).val('');
		 			layer.msg("清除成功");
    			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

</script>