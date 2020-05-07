<#assign CHART_TYPE_RADAR=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_RADAR") />
<#assign CHART_TYPE_HISTOGRAM=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_HISTOGRAM") />
<#assign CHART_TYPE_LINE=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_LINE") />
<#assign CHART_TYPE_PIE=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_PIE") />
<!-- 复合图表 -->
<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<div class="col-sm-${data.col?default('12')}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${data.title!}</h4>
        </div>
        <div class="box-body" style="height: 210px;">
            <ul >
            <#if data.jsonData?exists && freemarkerUtils.convertToSequence(JSONUtils.parse(data.jsonData))?size gt 0>
                <#assign simpleHashData=freemarkerUtils.convertToSequence(JSONUtils.parse(data.jsonData)) />
                <#list simpleHashData as realData>
                    <#if realData?exists>
                        <li style="width: ${(realData.col/12)*100}%;float: left;height: 180px;">
                        <!--雷达图-->
                        <#if realData.type?default('')==CHART_TYPE_RADAR>
                            <@chartstructure.radar loadingDivId="div${UuidUtils.generateUuid()!}" divClass="" divStyle="width: 100%;height: 100%; float:left;" jsonStringData=JSONUtils.toJSONString(realData.data.jsonData) titleFontSize=realData.data.titleSize isShowLegend=realData.data.showLegend isShowToolbox=realData.data.showToolBox />
                            <!--柱状图-->
                        <#elseif realData.type?default('')==CHART_TYPE_HISTOGRAM>
                            <@chartstructure.histogram loadingDivId="div${UuidUtils.generateUuid()!}" divClass="" divStyle="width: 100%;height: 100%;float:left;" jsonStringData=JSONUtils.toJSONString(realData.data.jsonData) isDifferentColour=realData.data.differentColors barWidth=realData.data.barW isShowLegend=realData.data.showLegend isShowToolbox=realData.data.showToolBox color="${realData.data.colors!}"/>
                            <!--折线图-->
                        <#elseif realData.type?default('')==CHART_TYPE_LINE>
                            <@chartstructure.histogram isLine=true loadingDivId="div${UuidUtils.generateUuid()!}" divClass="" divStyle="width: 100%;height: 100%;float:left;" jsonStringData=JSONUtils.toJSONString(realData.data.jsonData) isDifferentColour=realData.data.differentColors barWidth=realData.data.barW isShowLegend=realData.data.showLegend isShowToolbox=realData.data.showToolBox color="${realData.data.colors!}" />
                            <!--饼状图-->
                        <#elseif realData.type?default('')==CHART_TYPE_PIE>
                            <@chartstructure.pieChart loadingDivId="div${UuidUtils.generateUuid()!}"  divStyle="width: 100%;height: 100%;float:left;" jsonStringData=JSONUtils.toJSONString(realData.data.jsonData) titleFontSize=realData.data.titleSize  isShowDataLabel=false isShowLegend=realData.data.showLegend isShowToolbox=realData.data.showToolBox color="${realData.data.colors!}"/>
                        </#if>
                        </li>
                    </#if>
                </#list>
            <#else>
            </#if>
            </ul>
        </div>
    </div>
</div>
