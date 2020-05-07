<div class="box box-default chart-part <#if type! ==3>auto</#if>"<#if type! ==6 || type! ==7 >data-height ="${component.height!}"</#if>>
<#if preview! !="yes" && type! ==7>
<div class="box-set">
	<div class="pos-icon show">
		<i class="wpfont icon-close bold js-box-remove" onclick="deleteComponent('${component.id!}','${component.businessName!}');"></i>
	</div>
</div>
</#if>
<div class="chart-header">
	<span title="${component.businessName!}"><#if component.businessName! !="" && component.businessName?length gt 20>${component.businessName?substring(0, 20)}......<#else>${component.businessName!}</#if></span>
		<#if preview! !="yes" && type! ==6>
	<div class="box-tools dropdown">
        <a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown" aria-expanded="false"><i class="wpfont icon-ellipsis"></i></a>
        <ul class="box-tools-menu dropdown-menu">
            <span class="box-tools-menu-angle"></span>
            <li><a class="js-set" href="javascript:void(0);" onclick="componentSet('${component.reportId!}','${component.id!}');">设置</a></li>
            <li><a class="js-box-remove" href="javascript:void(0);" onclick="deleteComponent('${component.id!}','${component.businessName!}');">删除</a></li>
        </ul>
    </div>
	</#if>
</div>
<p class="color-999"></p>
<div class="chart-wrap" id="chart-div-${component.id!}"></div>
</div>
<script type="text/javascript">
    	$(document).ready(function(){
			var chartDivIndex=layer.msg('数据加载中......', {
			      icon: 16,
			      time:0,
			      shade: 0.01
		    });
			var url =  "${request.contextPath}/bigdata/chart/query/adapter/${component.businessId!}?type=kanban";
			$("#chart-div-${component.id!}").load(url,function() {
				layer.close(chartDivIndex);
			});
		});
</script>