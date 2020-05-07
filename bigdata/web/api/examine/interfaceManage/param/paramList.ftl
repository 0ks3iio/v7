<div class="wrap-full scrollBar4" >
    <table class="tables">
		<thead>
			<tr>
				<th>类型</th>
				<th>接口名称</th>
				<th>接口地址</th>
				<th>参数字段</th>
				<th>数据库参数</th>
				<th>参数描述</th>
				<th>是否必填</th>
				<th>微代码</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if openParamDtos??&& openParamDtos?size gt 0>
             <#list openParamDtos as dto>
                <tr data-paramid="${dto.id!}">
                   <td>${dto.type!} </td>
                   <td>${dto.interfaceName!} </td>
                   <td>${dto.uri!} </td>
                   <td title= "${dto.paramName!}"><#if dto.paramName?length gt 7>${dto.paramName?substring(0,8)}...<#else>${dto.paramName!}</#if> </td>
                   <td title= "${dto.paramColumnName!}"><#if dto.paramColumnName?length gt 7>${dto.paramColumnName?substring(0,8)}...<#else>${dto.paramColumnName!}</#if> </td>
                   <td title= "${dto.description!}"> <#if dto.description?exists && dto.description?length gt 7>${dto.description?substring(0,8)}...<#else>${dto.description!}</#if></td>
                   <td><#if dto.mandatory == 1>是<#else>否</#if></td>
                   <td>${dto.mcodeId!} </td>
                   <td>
                       <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
		               <a href="javascript:;" class="table-btn color-red delete">删除</a>
		           </td>
				</tr>
            </#list>
          <#else>
			<tr>
				<td  colspan="9" align="center">
				 暂无参数
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>

<script>
$(function(){
  $('.delete').on('click',deleteParam);//删除参数
  $('.modify').on('click',modifyParam);//修改接口
});
</script>
