<div class="wrap-full scrollBar4" >
    <table class="tables">
		<thead>
			<tr>
				<th width="7%">类型</th>
				<th width="10%">属性名</th>
				<th width="10%">字段名称</th>
				<th width="11%">数据库字段类型</th>
				<th width="8%">是否非空</th>
				<th width="15%">说明</th>
				<th width="10%">是否敏感字段</th>
				<th width="10%">微代码</th>
				<th width="6%">是否启用</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if openEntityDtos??&& openEntityDtos?size gt 0>
             <#list openEntityDtos as dto>
                <tr data-entityid="${dto.id!}">
                   <td>${dto.type!} </td>
                   <td>${dto.entityName!} </td>
                   <td>${dto.displayName!} </td>
                   <td>${dto.entityType!} </td>
                   <td><#if dto.mandatory == 1>是<#else>否</#if></td>
                   <td>${dto.entityComment!} </td>
                   <td><#if dto.isSensitive == 1>是<#else>否</#if></td>
                   <td>${dto.mcodeId!} </td>
                   <td><#if dto.isUsing == 1>是<#else>否</#if></td>
                   <td>
                       <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
		               <a href="javascript:;" class="table-btn color-red delete">删除</a>
		               <#if dto.isUsing == 0>
                         <a href="javascript:;" class="table-btn color-green start">启用</a>
                       <#else>
                         <a href="javascript:;" class="table-btn color-red stop">停用</a>
                       </#if>
		           </td>
				</tr>
            </#list>
          <#else>
			<tr>
				<td  colspan="10" align="center">
				 暂无属性
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>

<script>
$(function(){
	  $('.stop').on('click',stopEntity);//停用
	  $('.start').on('click',startEntity);//启用
	  $('.delete').on('click',deleteEntity);//删除
	  $('.modify').on('click',modifyEntity);//修改
});
</script>
