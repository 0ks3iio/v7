<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if classify?default(1)==2>
<div class="explain">
    <p>当天新增或修改考勤时间，如果要立即生效，请点击同步按钮，否则第二天生效</p>
</div>
</#if>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="10%">序号</th>
			<th width="20%"><#if classify?default(1)==1>离校</#if>类型</th>
			<th width="20%">时段</th>
			<th>适用年级</th>
			<th width="10%">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if gatePeriods?exists&&gatePeriods?size gt 0>
          	<#list gatePeriods as item>
			<tr>
				<td>${item_index+1}</td>
				<#if classify?default(1)==1>
				<td><#if item.type==1>放假日<#elseif item.type==2>休假日<#elseif item.type==4>通校生<#else>临时</#if>离校</td>
				<#else>
				<td><#if item.type==1>上学<#else>放学</#if></td>
				</#if>
				<td><#if item.type==3>${(item.tempDate?string('yyyy-MM-dd'))?if_exists}</#if> ${item.beginTime!}-${item.endTime!}</td>
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
<#if gatePeriods?exists&&gatePeriods?size gt 0>
	<@htmlcom.pageToolBar container="#infoShowDiv" class="noprint"/>
</#if>
<!-- S 考勤时间 -->
<div id="gatePeriodEditLayer" class="layer layer-editCheckTime">

</div><!-- E 考勤时间 -->
<script type="text/javascript">
function periodEdit(id){
	// 编辑考勤时间
	var url =  '${request.contextPath}/eclasscard/attence/gate/period/edit?classify=${classify?default(1)}&id='+id;
	$("#gatePeriodEditLayer").load(url,function() {
		  gatePeriodEditLayer();
		});
}

function periodDelete(id){
	layer.confirm('确定要删除该时间段吗？', function(index){
		$.ajax({
			url:'${request.contextPath}/eclasscard/attence/gate/period/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			<#if classify?default(1)==1>
				  	loadGatePeriod();
				  	<#else>
				  	loadInOutPeriod();
				  	</#if>
		 			layer.msg("删除成功");
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});	
	});
}
function gatePeriodEditLayer(){
	layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '编辑',
	    	area: '400px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveGatePeriod(index);
			  },
	    	zIndex: 1030,
	    	content: $('#gatePeriodEditLayer')
	    })
	}

</script>