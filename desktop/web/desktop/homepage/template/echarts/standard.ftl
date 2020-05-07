<!-- 单个图表模板 -->
<div class="col-sm-${(data.col?default('4'))!4}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>
        </div>
        <div class="box-body">
        
             <div style="height:${(data.height)!182}px;">
               <div id="moduleusage${echartsDivId!}"  style="width: 100%;height: 100%;"></div>
               <script type="text/javascript">
					$(document).ready(function(){
					<#if data.jsonData?default('') != ''>
						var moduleusage${echartsDivId!}myChart = echarts.init(document.getElementById('moduleusage${echartsDivId!}'));
						var option = jQuery.parseJSON('${data.jsonData}');
						moduleusage${echartsDivId!}myChart.setOption(option);
						</#if>
					});
				</script>
            </div>
        </div>
    </div>
</div>