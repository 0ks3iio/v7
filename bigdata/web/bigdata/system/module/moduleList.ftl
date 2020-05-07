<table class="tables">
	<thead>
		<tr>
			<th>模块名称</th>
			<th>类型</th>
			<th>所属目录</th>
			<th>用户类型</th>
			<th>打开方式</th>
			<th>是否启用</th>
			<th>是否需要授权</th>
			<th>是否置顶</th>
			<th>描述</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if  moduleList?exists &&moduleList?size gt 0>
          	<#list moduleList as module>
				<tr>
					<td>${module.name!}</td>
					<td><#if module.type! =="dir">目录<#elseif module.type! =="item">模块<#elseif module.type! =="custom-dir">自定义目录</#if></td>
					<td>${module.parentModuleName!}</td>
					<td>${module.userTypeName!}</td>
					<td><#if module.openType! ==1>div<#elseif module.openType! ==2>iframe<#elseif module.openType! ==3>newwindow</#if></td>
					<td><#if module.mark! ==1>是<#else>否</#if></td>
					<td><#if module.common! ==1>否<#else>是</#if></td>
					<td><#if module.top! ==1>是<#else>否</#if></td>
					<td>${module.description!}</td>
					<td>
					<#if module.fixed! ==0>
						<#if module.mark! ==1>
						<a class="look-over" href="javascript:void(0);" onclick="updateMark('${module.id!}',0,'${module.name!}')">
						停用</a>
						<#elseif module.mark! ==0>
						<a class="look-over" href="javascript:void(0);" onclick="updateMark('${module.id!}',1,'${module.name!}')">
						启用</a>
						</#if>
					</#if>
					<#if module.common! ==0>
					<span class="tables-line">|</span><a class="look-over" href="javascript:void(0);" onclick="loadModuleUserData('${module.id!}')">
					授权</a>&nbsp;
					</#if>
					</td>
				</tr>
      	    </#list>
         <#else>
		<tr >
			<td  colspan="10" align="center">
			暂无模块
			</td>
		<tr>
  	</#if>
	</tbody>
</table>
<script>
	function updateMark(id,mark,name){
		var title="启用";
		if(mark==0){
		 	title="停用";
		}
		showConfirmTips('prompt',"提示","您确定要"+title+name+"吗？",function(){
			$.ajax({
		            url:'${request.contextPath}/bigdata/module/updateMark',
		            data:{
		              'id':id,
		              'mark':mark
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 			showLayerTips('success',data.msg,'t');
						  	moduleList();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
	
	function height(){
        $('.js-height').each(function(){
            $(this).css({
            	height: $(window).height() - $(this).offset().top - 20,
                overflow: 'auto'
            });
        });
 	}

	$(document).ready(function(){
		 height();
	});
</script>