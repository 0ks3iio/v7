<div class="tab-content">
	<div id="aa" class="tab-pane active" role="tabpanel">
		<div class="table-container">
			<input type="hidden" value="${dto.itemType!}" id="itemType">
			<span class="filter-name" style="margin-left:10px;margin-top:15px;">评价类型：</span>
            <div class="filter-content" style="margin-top:10px;">
                <select name="evaluateType" id="evaluateType" class="form-control" onchange="doSearch()" style="width:120px">
                	<#if pjmcodeList?exists && pjmcodeList?size gt 0>
                		<#list pjmcodeList as item>
	                        <option value="${item.thisId!}" <#if item.thisId==dto.evaluateType?default("")>selected="selected"</#if>>${item.mcodeContent!}</option>
                		</#list>
                	</#if>
                </select>
            </div>
			<div class="table-container-header text-right">
				<#if dto.itemType?default("")=="11">
					<button class="btn btn-blue js-add" onclick="editItem('O','')">新增单选</button>
				<#elseif dto.itemType?default("")=="12">
					<button class="btn btn-blue js-add" onclick="editItem('N','')">新增项目</button>
				<#else>
					<button class="btn btn-blue js-add" onclick="editItem('O','')">新增单选</button>
					<button class="btn btn-blue js-add" onclick="editItem('M','')">新增多选</button>
				</#if>
			</div>
			<div class="table-container-body">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th width="5%">序号</th>
							<th width="50%" >名称</th>
							<#if dto.itemType?default("")!="12"><#--解答题项目-->
							   <th width="10%">类型</th>
							   <th width="20%">选项</th>
							</#if>
							<th width="15%">操作</th>
						</tr>
					</thead>
					<tbody>
						<#if itemList?exists && itemList?size gt 0>
							<#list itemList as item>
								<tr>
								<td>${item_index+1}</td>
								<td style="word-break:break-all;">${item.itemName!}</td>
								<#if dto.itemType?default("")!="12"><#--解答题项目-->
									<td><#if item.showType?default("")=="O">单选<#else>多选</#if></td><#--item.showType?default("")=="M"-->
									<td style="word-break:break-all;">
										<#if item.optionList?exists && item.optionList?size gt 0>
											<#list item.optionList as option>
												<p><label><input name="type${item_index}" <#if item.showType?default("")=="O">type="radio"<#else>type="checkbox"</#if> class="wp"><span class="lbl">${option.optionName!}</span></label></p>
											</#list>
										</#if>
									</td>
								</#if>
								<td>
									<a href="javascript:editItem('${item.showType!}','${item.id!}')" class="color-lightblue">编辑</a>
									<a href="javascript:deleteItem('${item.id!}')" class="color-blue">删除</a>
								</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td  <#if dto.itemType?default("")!="12">colspan="5"<#else>colspan="3"</#if> align="center">暂无数据</td>
							</tr>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<script>
	function doSearch(){
		var itemType=$("#itemType").val();
		var evaluateType=$("#evaluateType").val();
		var  url ='${request.contextPath}/evaluate/option/list/page?itemType='+itemType+"&evaluateType="+evaluateType;
        $("#itemShowDivId").load(url);
	}

	function editItem(showType,id){
		var itemType=$("#itemType").val();
		var evaluateType=$("#evaluateType").val();
		var url = "${request.contextPath}/evaluate/option/editItem?itemType="+itemType+"&evaluateType="+evaluateType+"&showType="+showType+"&id="+id;
		var heightstr="";
		<#if dto.itemType?default("")!="12">
		heightstr=550;
		<#else>heightstr=350;
		</#if>
		indexDiv = layerDivUrl(url,{title: "新增指标",width:550,height:heightstr});
	}
	function deleteItem(id){
		showConfirmMsg('确认删除？','提示',function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/evaluate/option/deletItem',
				data: {'id':id},
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					  	doSearch();
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