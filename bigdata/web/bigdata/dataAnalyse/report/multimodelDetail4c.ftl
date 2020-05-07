<div class="box box-default chart-part auto">
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
<div class="chart-wrap" id="multimodel-div-${component.id!}"></div>
<div class="chart-wrap" id="no-multimodel-div-${component.id!}"></div>
</div>
<script type="text/javascript">

    	$(document).ready(function(){
    		$('#no-multimodel-div-${component.id!}').hide();
            $('#multimodel-div-${component.id!}').show();
    	
            var multimodel_div_name ="multimodel_div_${component.id!}";
            window[multimodel_div_name]=echarts.init(document.getElementById('multimodel-div-${component.id!}'));
            window[multimodel_div_name].showLoading({
                text: '数据正在努力加载...'
            });
            $('#multimodel-div-${component.id!}').load('${request.contextPath}/bigdata/model/report/view?showTitle=false&showChart=false&id=${component.businessId!}');
		});
</script>