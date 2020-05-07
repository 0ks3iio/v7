<div class="table-container-body">
	<table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th>类别</th>
				<th>类型</th>
				<th>类型名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if openInterfaceTypes??&& openInterfaceTypes?size gt 0>
             <#list openInterfaceTypes as dto>
                <tr data-typeid="${dto.id!}">
                   <td><#if dto.classify == 1>接口类型<#elseif dto.classify == 2>结果类型 <#else> 公用类型</#if></td>
                   <td>${dto.type!} </td>
                   <td>${dto.typeName!} </td>
                   <td>
                       <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
		               <a href="javascript:;" class="table-btn color-red delete">删除</a>
		           </td>
				</tr>
            </#list>
          <#else>
			<tr>
				<td  colspan="4" align="center">
				 暂无类型
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>
<script type="text/javascript">
$(function(){
  $('.delete').on('click',deleteType);//删除接口
  $('.modify').on('click',modifyType);//修改应用
});
</script>