<script src="${request.contextPath}/gkelectiveys/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/gkelectiveys/openClassArrange/openClassArrange.js"></script>
<a href="#" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}</h4>
		<a href="javascript:" class="btn btn-blue pull-right js-addNewRound" id="btn-addNewRound">新增轮次</a>
		<div class="filter-item header_filter  filter-item-right">
			<label for="" class="filter-name">轮次：</label>
			<div class="filter-content" style="width:150px;" >
				<select vtype="selectOne" class="form-control" id="choiceDivideId" onChange="refreshRoundsList(this.value)">
					<option value="">---请选择---</option>
				<#if allList?exists && (allList?size>0)>
				<#list allList as item>
					<option value="${item.id!}" <#if roundsId! == item.id!>selected</#if>>第${item.orderId!}轮</option>
				</#list>
				</#if>
				</select>		
			</div>
		</div>
	</div>
	<div class="box-body">
		<table class="table table-striped table-hover no-margin">
			<thead>
				<tr>
					<th>轮次</th>
					<th>创建时间</th>
					<th>开设学考班</th>
					<th>重组行政班</th>
					<th>已完成开班</th>
					<th>科目设置</th>
					<th>开班安排</th>
					<th>编辑</th>
				</tr>
			</thead>
			<tbody>
			<#if roundsList?exists && (roundsList?size>0)>
				<#list roundsList as item>
				<tr>
					<td>第${item.orderId!}轮</td>
					<td>${(item.creationTime?string('yyyy-MM-dd HH:mm:ss'))?if_exists}</td>
					<td>
						<#if item.openClass?exists && item.openClass == '1'>
							<i class="ace-icon glyphicon glyphicon-ok"></i>
						<#else>
							<i class="ace-icon glyphicon glyphicon-remove"></i>
						</#if>
					</td>
					<td>
						<#if item.openClassType?exists && item.openClassType == '1'>
							<i class="ace-icon glyphicon glyphicon-ok"></i>
						<#else>
							<i class="ace-icon glyphicon glyphicon-remove"></i>
						</#if>
					</td>
					<td>
						<#if item.step == 5>
							<i class="ace-icon glyphicon glyphicon-ok"></i>
						<#else>
							<#if item.openIng>
							正在开班计算中...
							<#else>
							<i class="ace-icon glyphicon glyphicon-remove"></i>
							</#if>
						</#if>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="doOpenClassSub('${item.id}')">去设置</a>
					</td>
					<td>
						<a href="javascript:void(0)" <#if (item.step?default(0)>0)> onclick="openClass('${item.id}')" <#else>class="disabled"</#if>>去开班</a>
					</td>
					<td><#if (item.canDelete)><a class="color-red" href="javascript:void(0)"  onclick="doRemove('${item.id!}')">删除</a></#if></td>
				</tr>
				</#list>
			</#if>
			</tbody>
		</table>
		<#if roundsList?exists && (roundsList?size>0)>
		<em>说明：轮次一旦被采用，则轮次不能被删除。</em>
		</#if>
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var contextPath = '${request.contextPath}';
	var arrangeId = '${arrangeId!}';
	function openClass(roundsId){
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/index/page?arrangeId='+arrangeId;
		$("#showList").load(url);
	}
	function addRounds(){
		var url = contextPath+"/gkelective/"+arrangeId+"/arrangeRounds/edit/page";
		indexDiv = layerDivUrl(url,{title: "新建轮次",width:350,height:350});
	}
	$("#btn-addNewRound").on("click",function(){
		addRounds();
	});
	
	function doOpenClassSub(roundsId){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/openClasSub/list/page?roundsId='+roundsId;
		$("#showList").load(url);
	}
	
	function doRemove(roundsId){
		showConfirmMsg('删除轮次会同步删除所有相关数据，是否确认删除？','提示',function(){
		var ii = layer.load();
			$.ajax({
            url:'${request.contextPath}/gkelective/${arrangeId!}/arrangeRounds/doDelete?arrangeId=${arrangeId!}',
            data: {'roundsId':roundsId},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                layer.closeAll();
                if(jsonO.success){
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    doEnterSetp('${arrangeId}',4);
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    	});
	}
	
	function refreshRoundsList(roundsId){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangeRounds/index/page?roundsId='+roundsId;
		$("#showList").load(url);
	}
	
</script>
