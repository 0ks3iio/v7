<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<li style="width: ${(data.col/12)*100}%;float: left;height: 250px;">
    <!--柱状图-->
    <@chartstructure.histogram isLine=true titleFontSize=data.titleSize loadingDivId="div${UuidUtils.generateUuid()!}" divClass="" divStyle="width: 100%;height: 100%;float:left;"  jsonStringData=data.jsonData isDifferentColour=data.differentColors barWidth=data.barW isShowLegend=data.showLegend isShowToolbox=data.showToolBox color="${data.colors!}" />
</li>

