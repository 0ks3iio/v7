<script src="${request.contextPath}/gkelective2/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/gkelective2/openClassArrange/openClassArrange.js"></script>
<a href="#" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}</h4>
		<a href="javascript:" class="btn btn-blue pull-right js-addNewPlan" id="btn-addNewPlan">新增方案</a>
	</div>
	<div class="box-body">
		<table class="table table-striped table-hover no-margin">
			<thead>
				<tr>
					<th width="10%">学年</th>
					<th width="10%">学期</th>
					<th width="50%">上课时间段</th>
					<th width="10%">所选轮次</th>
					<th width="7%">课表时间设置</th>
					<th width="7%">教室老师安排</th>
					<th width="">编辑</th>
				</tr>
			</thead>
			<tbody>
			<#if plansList?exists && (plansList?size>0)>
				<#list plansList as item>
				<tr>
					<td>${item.acadyear!}</td>
					<td>第${item.semester!}学期</td>
					<td>${(item.startTime?string('yyyy-MM-dd'))?if_exists}（${item.timeStr!}）~${(item.endTime?string('yyyy-MM-dd'))?if_exists}（${item.timeStr2!}）
					<td>第${(item.gkRounds.orderId)?if_exists}轮</td>
					<td>
						<a href="javascript:void(0)" onclick="doClassTime('${item.id}')">去设置</a>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="openTeachPlace('${item.id}')" >去安排</a>
					</td>
					<td><#if item.canDelete><a class="color-red" href="javascript:void(0)" onclick="doRemove('${item.id!}')">删除</a></#if></td>
				</tr>
				</#list>
			</#if>
			</tbody>
		</table>
		<#if plansList?exists && (plansList?size>0)>
		<em>说明：只能新增已有方案之后的时间，删除只能在方案没有开始的时候进行删除。</em>
		</#if>
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var contextPath = '${request.contextPath}';
	var arrangeId = '${arrangeId!}';
	function addPlan(){
		var url = contextPath+"/gkelective/"+arrangeId+"/arrangePlan/edit/page";
		$("#showList").load(url);
	}
	$("#btn-addNewPlan").on("click",function(){
		addPlan();
	});
	
	function doClassTime(planId){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/goClassTime/index/page?planId='+planId;
		$("#showList").load(url);
	}
	function openTeachPlace(planId){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/openTeachPlace/page?planId='+planId;
		$("#showList").load(url);
	}
	
	function doRemove(planId){
		showConfirmMsg('删除方案会同步删除所有相关数据，是否确认删除？','提示',function(){
		var ii = layer.load();
			$.ajax({
            url:'${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/doDelete',
            data: {'planId':planId},
            type:'post',
            success:function(data) {
				var jsonO = JSON.parse(data);
				layer.closeAll();
                if(jsonO.success){
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    toback();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    	});
	}
	
	function toback(){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/index/page';
		$("#showList").load(url);
	}
	
</script>