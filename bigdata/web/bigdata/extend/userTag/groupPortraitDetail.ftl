<div class="box box-default chart-part" data-height ="${template.height!}">
<div class="chart-header">
	<span title="${tagName!}">${tagName!}</span>
	<#if type == 0>
	<div class="box-tools dropdown">
        <a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown" aria-expanded="false"><i class="wpfont icon-ellipsis"></i></a>
        <ul class="box-tools-menu dropdown-menu">
            <span class="box-tools-menu-angle"></span>
            <li><a class="js-set" href="javascript:void(0);" onclick="componentSet('${template.id!}');">设置</a></li>
            <li><a class="js-box-remove" href="javascript:void(0);" onclick="deleteComponent('${template.id!}');">删除</a></li>
        </ul>
    </div>
    </#if>
</div>
<p class="color-999"></p>
<div class="chart-wrap" id="chart-div-${template.id!}">
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999">无数据</p>
		</div>
	</div>
</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
	var chartDivIndex=layer.msg('数据加载中......', {
	      icon: 16,
	      time:0,
	      shade: 0.01
    });
	$.ajax({
            url: "${request.contextPath}/bigdata/groupAnalysis/template?templateId=${template.id!}",
            data: {profileCode : "${template.profileCode!}", tagArray : '${tagArray!}'},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
            	layer.close(chartDivIndex);
                if (response.success) {
	                <#if template.chartType =="scale">
	                	showScaleDiv(response.data,$("#chart-div-${template.id!}"));
	                <#elseif template.chartType =="map">	
		                 if (echarts.getMap("${template.regionCode!}") == null) {
				             $.get('${request.contextPath}/static/bigdata/js/echarts/map/json/province/${template.regionCode!}.json', function (geoJson) {
				             		echarts.registerMap("${template.regionCode!}", geoJson);
				                 	var echart_div = echarts.init(document.getElementById('chart-div-${template.id!}'));
				                    var data = JSON.parse(response.data);
				                    echart_div.setOption(data);
				                    echart_div.resize();
				             });
	    				 }else{
	    				 	var echart_div = echarts.init(document.getElementById('chart-div-${template.id!}'));
		                    var data = JSON.parse(response.data);
		                    echart_div.setOption(data);
		                    echart_div.resize();
		                    window.onresize=echart_div.resize;
	    				 }
	                <#else>	
	                	var echart_div = echarts.init(document.getElementById('chart-div-${template.id!}'));
	                    var data = JSON.parse(response.data);
	                    echart_div.setOption(data);
	                    echart_div.resize();
	                    window.onresize=echart_div.resize;
                    </#if>
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	layer.close(chartDivIndex);
            }
        });
});


</script>