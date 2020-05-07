<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if carResourceList?exists&&carResourceList?size gt 0>
<div class="table-container-body">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>
			    	<label><input type="checkbox" id="carResouceCheckboxAll" name="carResouceCheckboxAll" class="wp" onchange="carResouceCheckboxAllSelect()"><span class="lbl"></span></label>
			    </th>
				<th>标题</th>
				<th>发布时间</th>
				<th>类型</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#list carResourceList as list>
			<tr>
				<td>
					<label><input type="checkbox" name="carResouceCheckbox" class="wp" value="${list.id!}"><span class="lbl"></span></label>
				</td>
			    <td>${list.title!}</td>
			    <td>${(list.creationTime?string('yyyy-MM-dd HH:mm'))!}</td>
			    <td>
			  	<#if list.type == 0>
			   		视频、资讯
			  	<#elseif list.type == 1>
			  		视频
			  	<#else>
			  		资讯
			  	</#if>
			    </td>
			    <td>
			    	<a class="color-blue mr10" href="#" onClick="oneResource('${list.id!}')">编辑</a><a class="color-blue" href="#" onClick="deleteResource('${list.id!}')">删除</a>
			    </td>
			</tr>
			</#list>
		</tbody>
	</table>
</div>
<@htmlcom.pageToolBar container="#resourceListDiv" class="noprint"/>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">暂无相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
	function carResouceCheckboxAllSelect() {
		if($("#carResouceCheckboxAll").is(':checked')){
			$('input:checkbox[name=carResouceCheckbox]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$('input:checkbox[name=carResouceCheckbox]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
</script>