<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<#if type?default('')=="1">
	<#if jsonStringData?exists && jsonStringData!="">
		<@chartstructure.histogram  divClass="noprint" isShowLegend=true isStack=true loadingDivId="oneChart" divStyle="height:450px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}"  isShowToolbox=false brLength=0 rotateNum=0 isShowDataLabel=false/>
	<#else>
		<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">${message?default('暂无数据')}</div>
	</#if>
<#elseif type?default('')=="2">
	<#if jsonStringData?exists>
		<@chartstructure.histogram  isLine=true divClass="noprint" isShowLegend=false  loadingDivId="twoChart" divStyle="height:450px;width:${width?default(1000)}px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData!}"  isShowToolbox=false brLength=0 rotateNum=0/>
	<#else>
		<div style="height:430px;border:1px solid #ddd;margin-bottom:20px;">暂无数据</div>
	</#if>
</#if>