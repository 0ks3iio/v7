<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="10%">序号</th>
			<th width="20%">类型</th>
			<th width="20%">时段</th>
			<th>考勤签到年级</th>
			<th width="10%">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if attenceDormPeriods?exists&&attenceDormPeriods?size gt 0>
          	<#list attenceDormPeriods as item>
			<tr>
				<td>${item_index+1}</td>
				<td><#if item.type==1>上学日<#else>收假日</#if>考勤</td>
				<td>${item.beginTime!}-${item.endTime!}</td>
				<td>${item.gradeNames!}</td>
				<td>
					<a class="js-editCheckTime" href="javascript:void(0);" onclick="periodEdit('${item.id!}')">编辑</a>
					<a href="javascript:void(0);" onclick="periodDelete('${item.id!}')">删除</a>
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
<#if attenceDormPeriods?exists&&attenceDormPeriods?size gt 0>
	<@htmlcom.pageToolBar container="#infoShowDiv" class="noprint"/>
</#if>
<!-- S 考勤时间 -->
<div id="dormPeriodEditLayer" class="layer layer-editCheckTime">

</div><!-- E 考勤时间 -->
<script type="text/javascript">
function periodEdit(id){
	// 编辑考勤时间
	var url =  '${request.contextPath}/eclasscard/attence/dorm/period/edit?id='+id;
	$("#dormPeriodEditLayer").load(url,function() {
		  dormPeriodEditLayer();
		});
}

function periodDelete(id){
	layer.confirm('删除时段，则该时段的历史考勤记录无法查询，确定要删除吗？', function(index){
		$.ajax({
			url:'${request.contextPath}/eclasscard/attence/dorm/period/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			loadDormPeriod();
		 			layer.msg("删除成功");
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
}
function dormPeriodEditLayer(){
	layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '编辑',
	    	area: '400px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveDormPeriod(index);
			  },
	    	zIndex: 1030,
	    	content: $('.layer-editCheckTime')
	    })
	}

</script>