<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item"><a class="btn btn-blue" href="javascript:void(0);" onclick="bulletinEdit('1')">发布公告</a></div>
	</div>
	<div class="table-container" id="bulletinListTable">
		<div class="table-container-body">
			<table class="table table-striped">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="8%">公告类型</th>
						<th width="25%">班牌展示时间</th>
						<th>发布人</th>
						<th>公告内容</th>
						<th>展示状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<#if eccBulletins?exists&&eccBulletins?size gt 0>
			        <#list eccBulletins as item>
					<tr id="${item.fullObjAllId!}">
						<td>${item_index+1}</td>
						<td><#if item.type == 1>普通公告<#elseif item.type == 2>顶栏公告<#else>全屏公告</#if></td>
						<td>${item.beginTime!}至${item.endTime!}</td>
						<td>${item.userName!}</td>
						<#if item.title?exists>
						<td>${item.title!}</td>
						<#elseif item.content?exists && item.content?length gt 20>
						<td>${item.content?substring(0,20)}...</td>
						<#else>
						<td>${item.content!}</td>
						</#if>
						<td>
						<#if item.status==0>
						<i class="fa fa-circle color-orange"></i> 未开始
						<#elseif item.status==1>
						<i class="fa fa-circle color-green"></i> 展示中
						<#else>
						<i class="fa fa-circle color-grey"></i> 已结束
						</#if>
						<span class="lock-flag-show">
						<#if item.lockScreen><i class="fa fa-lock color-orangeYellow font-16"></i> 已锁定 </#if>
						<span>
						</td>
						<td>
						<#if item.status ==0 && item.canEdit>
							<a href="javascript:void(0);" onclick="bulletinEdit('1','${item.id!}')">编辑</a>
						</#if>
						<#if item.type == 3 && item.status ==1 && item.canEdit>
							<span class="lock-btn-show">
							<#if item.lockScreen>
							<a href="javascript:void(0);" onclick="fullObjAllLock('${item.fullObjAllId!}','0')">解锁</a>
							<#else>
							<a href="javascript:void(0);" onclick="fullObjAllLock('${item.fullObjAllId!}','1')">锁定</a>
							</#if>
							</span>
						</#if>
							<a href="javascript:void(0);" onclick="bulletinEdit('0','${item.id!}')">查看</a>
						<#if item.canEdit>
							<a href="javascript:void(0);" onclick="bulletinDelete('${item.id!}')">删除</a>
						</#if>
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
		<#if eccBulletins?exists&&eccBulletins?size gt 0>
		<@htmlcom.pageToolBar container="#tabContent" class="noprint"/>
		</#if>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$("#bulletinListTable").css({
		height: $(window).height() - $("#bulletinListTable").offset().top - 60,
		overflowY: 'auto'
	})
});
function bulletinEdit(isEdit,id){
	var url =  '${request.contextPath}/eclasscard/standard/bulletin/edit?isEdit='+isEdit+'&id='+id;
	$("#schEclasscardDiv").load(url);
}

function bulletinDelete(id){
	layer.confirm('确定要删除吗？', function(index){
		$.ajax({
			url:'${request.contextPath}/eclasscard/bulletin/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			showContent('1');
		 			layer.msg("删除成功");
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
}

function fullObjAllLock(id,lock){
	var sucLockStr = '<a href="javascript:void(0);" onclick="fullObjAllLock(\''+id+'\',\'0\')">解锁</a>';
	var sucUnLockStr = '<a href="javascript:void(0);" onclick="fullObjAllLock(\''+id+'\',\'1\')">锁定</a>';
	$.ajax({
		url:'${request.contextPath}/eclasscard/standard/fullscreensch/lockobj',
		data: {'id':id,'lock':lock},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			layer.msg(jsonO.msg);
 			if(jsonO.success){
 				if(lock=='1'){
	 				$("#"+id).find('.lock-flag-show').html('<i class="fa fa-lock color-orangeYellow font-16"></i> 已锁定 ');
	 				$("#"+id).find('.lock-btn-show').html(sucLockStr);
 				}else{
	 				$("#"+id).find('.lock-flag-show').html('');
	 				$("#"+id).find('.lock-btn-show').html(sucUnLockStr);
 				}
	 		}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>