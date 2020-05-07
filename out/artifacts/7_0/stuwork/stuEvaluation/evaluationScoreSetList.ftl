	<div class="main-content" >
		
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-body">
						<div class="filter" id="searchType">
							<div class="filter-item">
								<span class="filter-name">选项类型：</span>
				                <div class="filter-content">
				                    <select name="businessType11" id="businessType11" class="form-control" onchange="doSearch()" style="width:135px">
				                        <option value="1010" <#if "1010"==businessType?default("")>selected="selected"</#if>>学生违纪</option>
				                        <option value="1020" <#if "1020"==businessType?default("")>selected="selected"</#if>>学生评语</option>
				                        <option value="1030" <#if "1030"==businessType?default("")>selected="selected"</#if>>军训等第</option>
				                        <option value="1040" <#if "1040"==businessType?default("")>selected="selected"</#if>>值周表现等第</option>
				                        <option value="1050" <#if "1050"==businessType?default("")>selected="selected"</#if>>学农等第</option>
				                    </select>
				                </div>
							</div>
						
						</div>
						
						<div class="table-container">
							<div class="table-container-header text-right">
								<button class="btn btn-blue" onclick="addOption('')">新增</button>
							</div>
							<form id="submitForm">
							<div class="table-container-body">
									<table class="table table-striped table-hover">
									<thead>
									<tr>
									    <th>序号</th>
										<th>选项名称</th>										
										<th>是否需要折分</th>
										<th>是否自定义折分</th>
										<th>折分</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<#if boptionList?exists && boptionList?size gt 0>
										<#list boptionList as item>
											<tr>
											    <td width="10%">${item.orderId!}</td>
												<td width="50%" style="word-break:break-all;">${item.optionName!}</td>
												
										
													<td width="10%">
												    <#if '${item.hasScore!}' == '1'>
												                                是
												    <#else>
												                               否
												    </#if>
												</td>
												<td width="10%">
												<#if '${item.hasScore!}' == '1'>
												    <#if '${item.isCustom!}' == '1'>
												                                是
												    <#else>
												                               否
												    </#if>
												</#if>
												</td>
												<td width="10%">
												<#if '${item.hasScore!}' == '1' && '${item.isCustom!}' == '0'>
													${(item.score)?string('0.#')!}
												</#if>
												</td>
												<td width="10%">
													<a href="javascript:" class="color-lightblue"  onclick="addOption('${item.id!}')">修改</a>
													<#if yyBopSet?exists && yyBopSet?size gt 0>
                                                         <#if !yyBopSet?seq_contains('${item.id!}')>
													      <a href="javascript:" class="color-blue"  onclick="deleteOption('${item.id!}')">删除</a>
													     </#if>
													<#else>
													<a href="javascript:" class="color-blue"  onclick="deleteOption('${item.id!}')">删除</a>
													</#if>
												</td>
											</tr>
										</#list>
									<#else>
										<tr>
											<td colspan="6" align="center">暂无数据</td>
										</tr>
									</#if>
								</tbody>
								</table>
							</div>
							</form>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->
<script>
	function addOption(id){
	    var businessType = $('#businessType11').val();
		var url = "${request.contextPath}/stuwork/evaluation/score/addOption?id="+id+"&businessType="+businessType;
		indexDiv = layerDivUrl(url,{title: "选项维护",width:520,height:480});
	}
	function doSearch(){
	 	var businessType=$("#businessType11").val();
		var url="${request.contextPath}/stuwork/evaluation/score/list/page?businessType="+businessType;
		$("#itemShowDivId").load(url);
	}
	function deleteOption(id){
	    var businessType=$("#businessType11").val();
		showConfirmMsg('确认删除？','提示',function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/stuwork/evaluation/score/deleteOption',
				data: {'id':id,'businessType':businessType},
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
