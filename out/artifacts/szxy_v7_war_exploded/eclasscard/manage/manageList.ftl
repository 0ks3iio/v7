<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>设备号</th>
			<th>用途</th>
			<th>安装场地</th>
			<th>所属班级</th>
			<#if isStandard>
			<th>状态</th>
			</#if>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if eccInfos?exists&&eccInfos?size gt 0>
          	<#list eccInfos as item>
				<tr>
					<td>${item_index+1}</td>
					<td>${item.name!}</td>
					<td>${usedForMap[item.type!]!}</td>
					<td>${item.placeName!}</td>
					<td>${item.className!}</td>
					<#if isStandard>
						<td>
						<#if item?exists&&item.status == 2>
						<i class="fa fa-circle color-green"></i> 在线</span><#else>
						<span><i class="fa fa-circle color-red"></i> 离线</span></#if></td>
					</#if>
					<!--
					<#if item.status == 1>
					<td><span><i class="fa fa-circle color-green"></i> 正常</span></td>
					<#else>
					<td><span><i class="fa fa-circle color-red"></i> 异常</span></td>
					</#if>
					-->
					<td>${item.remark!}</td>
					<td><a class="js-edit" href="javascript:void(0);">
						<input type="hidden" name="id" value="${item.id!}">
					编辑</a>
					
					<a href="javascript:void(0);" onclick="manageDelete('${item.id!}')">删除</a>
					<a href="javascript:void(0);" onclick="clearCache('${item.id!}')" style="display:none">清除缓存</a>
					<a href="javascript:void(0);" onclick="clientLog('${item.id!}')" style="display:none">客户端日志</a>
					<a href="javascript:void(0);" onclick="clearCacheByCardId('${item.id!}')" style="display:none">清除课表缓存</a>
					
					</td>
				</tr>
      	    </#list>
  	    <#else>
			<tr >
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if eccInfos?exists&&eccInfos?size gt 0>
	<@htmlcom.pageToolBar container="#showList" class="noprint"/>
</#if>
<!-- S 编辑班牌 -->
<div id="cardEditLayer" class="layer layer-edit">
</div><!-- E 编辑班牌 -->
<div id="choiseClzPlace" class="layer layer-selection">
</div><!-- E 选择班级-场地-->
<script>
$(function(){
    $('.js-edit').on('click',function(e){
    	e.preventDefault();
    	var id = $(this).find('input').val();
    	var url =  '${request.contextPath}/eclasscard/manage/edit?id='+id;
		$("#cardEditLayer").load(url,function() {
			  layerShow();
			});
    })
})
function layerShow(){
     layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '编辑',
	    	area: '400px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveECLassCard(index);
			  },
	    	content: $('.layer-edit')
	    })
    }
    
var isSubmit=false; 
function manageDelete(id) {
	layer.confirm('删除班牌会将其相关的数据全部删除，确定要删除吗？', function(index){
		if(isSubmit){
        	return;
    	}
    	isSubmit = true;
		$.ajax({
			url:'${request.contextPath}/eclasscard/manage/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				layer.close(index);
				var jsonO = JSON.parse(data);
	 			if(jsonO.success){
	 				showList();
	 			}else{
	 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 				isSubmit = false;
				}
			},
 			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	})
}
function clientLog(id) {
	var url =  '${request.contextPath}/eclasscard/client/log/list?cardId='+id;
	$("#showList").load(url);
}
function clearCache(id) {
	$.ajax({
		url:'${request.contextPath}/eclasscard/clear/card/cache',
		data: {'cardId':id},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			if(jsonO.success){
 				layer.msg("清除成功");
 			}else{
 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function clearCacheByCardId(id){
	$.ajax({
		url:'${request.contextPath}/eclasscard/clearCacheByCardId',
		data: {'cardId':id},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			if(jsonO.success){
 				layer.msg("清除成功");
 			}else{
 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>