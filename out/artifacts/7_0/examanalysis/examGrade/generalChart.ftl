<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<#if jsonStringData?exists>
	<@chartstructure.histogram isLine=true divClass="noprint" loadingDivId="oneChart" divStyle="height:430px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}" isShowLegend=false isShowToolbox=false brLength=9 rotateNum=0/>
<#else>
	<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">暂无数据</div>
</#if>