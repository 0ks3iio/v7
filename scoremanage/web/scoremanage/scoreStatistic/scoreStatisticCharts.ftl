<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#if jsonStringData??>
<@chartstructure.scatter jsonStringData="${jsonStringData}" divStyle="height:400px;width:100%;" isShowLegend=false isShowDataLabel=true dataDisplay="{c}">
</@chartstructure.scatter>
</#if>