			<div class="box box-default">
				<div class="box-body">
					<div class="table-container">
					<#if hasAdmin == '1'>
						<div class="table-container-header text-right">
							<button class="btn btn-blue js-add" onclick="addItem('')">新增</button>
						</div>
						</#if>
						<div class="table-container-body">
							<table class="table table-striped">
								<thead>
									<tr>
										<th>序号</th>
										<th width="15%">考核项名称</th>
										<th>类别</th>
										<th>总分值</th>
										<th>录入角色</th>
										<th>考核时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								<#if hasAdmin == '1'>
								<#if items?exists && items?size gt 0>
								<#list items as item>
								<tr>
										<td>${item.orderId}</td>
										<td width="15%">${item.itemName}</td>
										<td><#if item.type == 1>卫生<#elseif item.type == 2>纪律</#if></td>
										<td><#if item.hasTotalScore == 0>无总分<#else>${item.totalScore?string('0.#')}</#if></td>
										<td>
										<#list item.itemRoles as role>
										${role.roleName!}<#if role_index lt item.itemRoles?size - 1>、</#if>
										</#list>
										</td>
										<td>
										<#list item.itemDays as day>
										${day.dayName!}<#if day_index lt item.itemDays?size - 1>、</#if>
										</#list>
										</td>
										<td>
											<a href="javascript:void(0)" onclick="addItem('${item.id!}')" class="color-lightblue">编辑</a>
											<a href="javascript:void(0)" onclick="deleteItem('${item.id!}')">删除</a>
										</td>
									</tr>
								</#list>
								<#else>
								<tr>
								<td colspan = '7' align="center">无数据！</td>
								</tr>
								</#if>
								<#else>
								<tr>
								<td colspan = '7' align="center">你不是总管理员，没有权限操作！</td>
								</tr>
								</#if>
								</tbody>
							</table>
						</div>
					</div>
							
				</div>
			</div>
<script>
function addItem(itemId){
	var url = "${request.contextPath}/stuwork/checkweek/itemEdit/edit/page?itemId="+itemId;
	indexDiv = layerDivUrl(url,{title: "编辑考核项",width:610,height:530});
}

function deleteItem(itemId){
if(confirm('确定要删除该条考核项吗？')){
	$.ajax({
		url:"${request.contextPath}/stuwork/checkweek/itemEdit/deleteItem",
		data: {'itemId':itemId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					$(".model-div").load("${request.contextPath}/stuwork/checkweek/itemEdit/page");
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
	}
}
</script>