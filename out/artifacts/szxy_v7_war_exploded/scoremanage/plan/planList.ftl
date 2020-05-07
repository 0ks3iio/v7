<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>方案名称</th>
			<th>学年</th>
			<th>学期</th>
			<th>考试</th>
			<th>年级</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
	<#if planList?exists && planList?size gt 0>
	    <#list planList as item>
		<tr>
			<td>${item.examName!}-${item.gradeName!}(${(item.creationTime?string('yyyy-MM-dd HH:mm:ss'))!})</td>
			<td>${item.acadyear!}</td>
			<td>${mcodeSetting.getMcode('DM-XQ','${item.semester!}')}</td>
			<td>${item.examName!}</td>
			<td>${item.gradeName!}</td>
			<td>
				<a href="javascript:showDetail('${item.id!}');" class="table-btn color-red">查看成绩</a>
				<a href="javascript:doDelete('${item.id!}');" class="table-btn color-red">删除</a>
			</td>
		</tr>
		</#list>
	<#else>
		<tr>
			<td colspan="6" class="text-center">暂无相关方案，请先新增！</td>
		</tr>
	</#if>
	</tbody>
</table>
<#if planList?exists>
<@htmlcom.pageToolBar container="#showItemDiv"/>
</#if>
<script>
function showDetail(planId){
   var url = "${request.contextPath}/scoremanage/plan/detail/page?planId="+planId;
	$("#showList").load(url);
}
var isSubmit=false;
function doDelete(id){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	layer.confirm('确定删除吗？', function(index){
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/scoremanage/plan/delete',
			data: {'planId':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.closeAll();
					layer.msg(jsonO.msg,{
						offset: 't',
						time: 2000
					});
					showItem();
				}
				else{
					layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
					isSubmit=false;
				}
				layer.close(ii);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});

	},function(){
		isSubmit=false;
	})
}
</script>
