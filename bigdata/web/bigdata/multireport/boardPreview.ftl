<#if reportDetailList?exists&&reportDetailList?size gt 0>
<div class="box box-structure">
	<#if containHeader! =="zbxx">
		<div class="box-header clearfix">
		<div class="edit-name edit-name-32">
			<span>${multiReport.name!}</span>
			</div> 
			<div class="float-right">
				<button class="btn btn-lightblue">导出</button>
				<button class="btn btn-default color-999 ">打印</button>
			</div>
		</div>
		</#if>
		<div class="box-body">
			<div class="row no-padding kanban-wrap">
				<#list reportDetailList as component>
					<div class="part-box ${component.width!}"  id="board-detail-${component.id!}"></div>
				</#list>
			</div>	
		</div>
	</div>
</div>
<#else>
<div class="no-data">
	<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data.png"/>
	<div>暂无数据</div>
</div>
</#if>
<script type="text/javascript">	
	$(document).ready(function(){
		<#if reportDetailList?exists&&reportDetailList?size gt 0>
			<#list reportDetailList as component>
				  $("#board-detail-${component.id!}").load("${request.contextPath}/bigdata/multireport/component/detail?preview=yes&type=6&componentId=${component.id!}");		
			</#list>
		</#if>
	});
</script>