<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<#if jsonStringData?exists && jsonStringData!="">
	<@chartstructure.histogram isLine=false divClass="noprint" loadingDivId="twoChart" divStyle="height:430px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}" isShowLegend=false isShowToolbox=false brLength=0 rotateNum=0 barGap="5%"/>
<#else>
	<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">${message?default('暂无数据')}</div>
</#if>