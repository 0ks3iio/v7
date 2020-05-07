<div <#if !isAll> class="box-body" style="padding-bottom:0px;"</#if>>
	<div class="table-container">
		<div class="table-container-header text-right">
			<a class="btn btn-blue" onclick="bulletinEdit('1')">发布公告</a>
		</div>
		<div  class="table-container-body">
			<table class="table table-striped">
				<thead>
					<tr>
						<th>展示时间</th>
						<th>发布人</th>
						<th>公告标题</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#if eccBulletins?exists&&eccBulletins?size gt 0>
			          	<#list eccBulletins as item>
			          	<tr>
							<td>${item.beginTime!}至${item.endTime!}</td>
							<td>${item.userName!}</td>
							<td>${item.title!}</td>
							<td><i class="fa fa-circle color-green"></i> 
							<#if item.status==0>未展示
							<#elseif item.status==1>展示中
							<#else>已展示</#if>
							</td>
							<td>${item.createTime?string('yyyy-MM-dd HH:mm')}</td>
							<td>
							<#if item.status==0 && item.canEdit>
								<a href="javascript:void(0);" onclick="bulletinEdit('1','${item.id!}')">编辑</a>
							</#if>
								<a href="javascript:void(0);" onclick="bulletinEdit('0','${item.id!}')">查看</a>
								<a href="javascript:void(0);" onclick="bulletinDelete('${item.id!}')">删除</a>
							</td>
						</tr>
			      	    </#list>
			  	    <#else>
						<tr>
							<td  colspan="88" align="center">
							暂无数据
							</td>
						<tr>
			        </#if>
				</tbody>
			</table>
		</div>
	</div>
</div>
<#if eccBulletins?exists&&eccBulletins?size gt 0>
	<#if isAll> 
	<@w.pagination  container="#bulletinDiv" pagination=pagination page_index=1/>
	<#else>
	<div style="padding-left: 20px;">
	<@w.pagination  container="#tabContent" pagination=pagination page_index=1/>
	</div>
	</#if>
</#if>
<script type="text/javascript">
function bulletinEdit(isEdit,id){
	var url =  '${request.contextPath}/eclasscard/bulletin/edit?isEdit='+isEdit+'&id='+id;
	<#if isAll> 
	$("#bulletinDiv").load(url);
	<#else>
	url = url+"&eccInfoId=${eccInfoId!}"
	$("#tabContent").load(url);
    </#if>
}

function bulletinDelete(id){
	$.ajax({
		url:'${request.contextPath}/eclasscard/bulletin/delete',
		data: {'id':id},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			backList();
	 		}else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function backList(){
	<#if isAll>
	bulletinList();
	<#else> 
	var url = '${request.contextPath}/eclasscard/bulletin/list?eccInfoId=${eccInfoId!}';
	$("#tabContent").load(url);
	</#if>
}
</script>