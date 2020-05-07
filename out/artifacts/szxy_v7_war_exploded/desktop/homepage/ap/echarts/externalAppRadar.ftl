<!--雷达图-->
<div class="col-sm-${col?default('12')}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${titleName!}</h4>
        </div>
        <div class="box-body">
            <#if jsonStringCharts??>
                <@chartstructure.radar loadingDivId="loadingDivIdRad" divClass="" divStyle="width: 100%;height: 100%;" jsonStringData=jsonStringCharts[0][0] titleFontSize=10 isShowLegend=false isShowToolbox=false />
            <#else>
            </#if>
        </div>
    </div>
</div>