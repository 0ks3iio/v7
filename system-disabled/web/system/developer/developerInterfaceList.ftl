<div role="tabpanel" class="tab-pane active" id="${activeId!}">
<div class="filter">
	<div class="filter-item">
	    <#if activeId == 'aa' >
			<button class="btn btn-sm btn-blue" onclick= "doChange('a')">审核通过</button>
			<button class="btn btn-sm btn-blue" onclick= "doChange('b')">审核不通过</button>
		<#else>
			<button class="btn btn-sm btn-blue" onclick= "doChange('d','${activeId!}')">删除</button>
		</#if>
	</div>
</div>
<table class="table table-striped table-hover no-margin">
	<thead>
		<tr>
			<th width="20">
				<label class="pos-rel">
					<input name="course-checkbox" type="checkbox" class="wp" onclick="checkInterfaceAll(this)">
					<span class="lbl"></span>
				</label>
			</th>
			<th width="300">订阅数据</th>
			<th>敏感字段</th>
			<th width="200">操作</th>
			<#if activeId == 'bb' >
			    <th>调用接口次数</th>
			</#if>
		</tr>
	</thead>
	<tbody class="interList">
	       <#if interFaceList?exists && interFaceList?size gt 0 >
                <#list interFaceList as item>
					<tr>
						<td>
							<label class="pos-rel">
								<input name="course-checkbox" type="checkbox" class="wp" value="${item.type!}">
								<span class="lbl"></span>
							</label>
						</td>
						<td>${item.typeName!}</td>
						<td>
							<#if activeId == 'bb' >
                            <a href="javascript:checkSensitive('${item.type!}','${item.typeName!}');" data-type="${item.type!}" data-typename="${item.typeName!}" class="table-btn color-orange js-sensitive">${item.sensitiveNum!}个敏感字段</a>
                            <#else>
                            /
                            </#if>
						</td>
						<td>
						  <#if activeId == 'aa' >
							<a href="javascript:showPassDiv('${item.type}');" class="table-btn color-blue js-audit">审核通过</a>
							<a href="javascript:toUnPass('${item.type}');" class="table-btn color-blue js-audit">审核不通过</a>
						  <#else>
                            <a href="javascript:delInterface('${item.type}','${activeId!}');" class="table-btn color-red js-dle">删除</a>
						  </#if>
						</td>
						<#if activeId == 'bb' >
			                <td>${item.count!}</td>
			            </#if>
					</tr>
				</#list>
		   <#else>
		        <tr>
		            <#if activeId == 'aa' >
			            <td  colspan="4" align="center">
						   暂无需要审核的接口
						</td>
		            <#elseif activeId == 'bb'>
						<td  colspan="5" align="center">
						   暂无通过的接口
						</td>
					<#else>
					    <td  colspan="4" align="center">
						   暂无审核不通过的接口
						</td>
		            </#if>
				<tr>
		   </#if>
	</tbody>
</table>
<script>
//全选
<#--  
function checkInterfaceAll(){
   var total = $('#interfaceList :checkbox').length;
	var length = $('#interfaceList :checkbox:checked').length;
	if(length != total){
		$('#interfaceList :checkbox').prop("checked", "true");
		this.prop("checked", "true");
	}else{
		$('#interfaceList :checkbox').removeAttr("checked");
		this.removeAttr("checked");
	}
};
-->
function checkInterfaceAll(obj){
	var checked = $(obj).prop("checked");
	var all = $(obj).parents("table").find("tbody input:checkbox[name='course-checkbox']");
	if(checked){
		all.prop('checked',true);
	}else{
		all.prop('checked',false);
	}
}

var isSubmit=false;
function doChange(param,activeId){
   if(isSubmit){
	  return;
   }
   isSubmit = true;
   var selEle = $('.interList :checkbox:checked');
   if(selEle.length <= 0) {
    showMsgError('请先勾选接口','');
    isSubmit = false;
   }
   if(isSubmit){
       isSubmit = false;
	   var types;
	   for(var i=0;i<selEle.length;i++){
			if(i == 0){
			  types = selEle.eq(i).val();
			}else{
			  types = types + ',' + selEle.eq(i).val();
			}
	   } 
	   if(param == 'a'){ 
	      showPassDiv(types);
	   }
	   if(param == 'b'){ 
	      toUnPass(types);
	   }
	   if(param == 'd'){ 
	      delInterface(types,activeId);
	   }
   }
}


</script>