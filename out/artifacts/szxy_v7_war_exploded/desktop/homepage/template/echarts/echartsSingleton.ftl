<!-- 单个图表模板 -->
<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<div class="col-sm-${data.col?default('4')}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${data.title!}</h4>
        </div>
        <div class="box-body">
        
        <#if data.jsonData??>
            <div style="height:${data.height!182}px;">
                <!--雷达图-->
                <#if data.type?default('')=='radar'>
                    <@chartstructure.radar loadingDivId="div${echartsDivId!}" divClass="" divStyle="width: 100%;height: 100%;" jsonStringData=data.jsonData titleFontSize=data.titleSize isShowLegend=data.showLegend isShowToolbox=data.showToolBox />
                <!--柱状图-->
                <#elseif data.type?default('')=='histogram'>
                    <@chartstructure.histogram loadingDivId="div${echartsDivId!}" divClass="" divStyle="width: 100%;height: 100%;" jsonStringData=data.jsonData isDifferentColour=data.differentColors barWidth=data.barW isShowLegend=data.showLegend isShowToolbox=data.showToolBox color="${data.colors}"/>
                <!--折线图-->
                <#elseif data.type?default('')=='line'>
                    <@chartstructure.histogram isLine=true loadingDivId="div${echartsDivId!}" divClass="" divStyle="width: 100%;height: 100%;" jsonStringData=data.jsonData isDifferentColour=data.differentColors barWidth=barW isShowLegend=data.showLegend isShowToolbox=data.showToolBox color="${data.colors}" />
                <!--饼状图-->
                <#elseif data.type?default('')=='pie'>
                    <@chartstructure.pieChart loadingDivId="div${echartsDivId!}"  divStyle="width: 100%;height: 100%;" jsonStringData=data.jsonData titleFontSize=data.titleSize  isShowDataLabel=false isShowLegend=data.showLegend isShowToolbox=data.showToolBox color="${data.colors}"/>
                <!--仪表盘-单一 -->
                <#elseif data.type?default('')=='radar'>
                    <div>
                        <img src="${request.contextPath}/desktop/images/loading.gif">
                    </div>
                </#if>
            </div>
        <#else>
        </#if>
        </div>
    </div>
</div>