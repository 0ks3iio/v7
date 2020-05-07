<#if reportDetailList?exists&&reportDetailList?size gt 0>
<div class="box box-structure box-structure-inner">
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
	<div class="box-body mb-20">
		<div class="box-default">
			<div class="box-body">
				<div class="row no-padding kanban-wrap border report-set-wrap report-view">
					<#list reportDetailList as component>
						<div class="part-box col-md-12"  id="report-detail-${component.id!}"></div>
					</#list>					
				</div>
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
<script type="text/javascript" src="${request.contextPath}/bigdata/v3/static/plugs/wangEditor/wangEditor.min.js"></script>
<script type="text/javascript">
    var E = window.wangEditor;
    var editorMap=new Map();
	$(document).ready(function(){
		<#if reportDetailList?exists&&reportDetailList?size gt 0>
			<#list reportDetailList as component>
				  $("#report-detail-${component.id!}").load("${request.contextPath}/bigdata/multireport/component/detail?preview=yes&type=7&componentId=${component.id!}");		
			</#list>
		</#if>
	});
</script>