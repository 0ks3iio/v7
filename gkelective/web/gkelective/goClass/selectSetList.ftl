<script src="${request.contextPath}/gkelective/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/gkelective/openClassArrange/openClassArrange.js"></script>
<a href="javascript:goBack()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}</h4>
		<a href="javascript:addSet()" class="btn btn-blue pull-right">新增</a>
	</div>
	<div class="box-body">
		<table class="table table-striped table-hover no-margin">
			<thead>
				<tr>
					<th width="10%">序号</th>
					<th width="80%">限选科目</th>
					<th width="10%">编辑</th>
				</tr>
			</thead>
			<tbody>
			<#if GkLimitSubjectList?exists && (GkLimitSubjectList?size>0)>
				<#list GkLimitSubjectList as item>
				<tr>
					<td>${item_index+1}</td>
					<td>${item.subjectNames!}</td>
					<td><a class="color-red" href="javascript:void(0)" onclick="removeSet('${item.id!}')">删除</a></td>
				</tr>
				</#list>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
	var indexDiv = 0;
	$(window).bind("resize",function(){
		resizeLayer(indexDiv); 
	});

	function addSet() {
		var url = '${request.contextPath}/gkelective/${arrangeId!}/selectSet/edit';
	    var indexDiv = layerDivUrl(url,{title: "新增",width:400,height:300});
	}

	function goBack() {
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/goClass/index/page';
		$("#showList").load(url);
	}

	function removeSet(id) {
		showConfirmMsg('是否确认删除？','提示',function(){
		var ii = layer.load();
			$.ajax({
            url:'${request.contextPath}/gkelective/${arrangeId!}/selectSet/delete',
            data: {'id':id},
            type:'post',
            success:function(data) {
				var jsonO = JSON.parse(data);
				layer.closeAll();
                if(jsonO.success){
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    url = '${request.contextPath}/gkelective/${arrangeId}/selectSet/List/page';
					$("#showList").load(url);
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        	});
    	});
	}
</script>