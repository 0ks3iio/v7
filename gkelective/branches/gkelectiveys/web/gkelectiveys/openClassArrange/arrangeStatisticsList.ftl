<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<#import "/gkelectiveys/js/gkelectivecommon.ftl" as gkelectivecommon />
<div class="row">
	<div class="col-sm-12">
	<#if (gkStuExceptionDtoList?size>0)>
	共有${stuNumber}个学生选课，其中${stuNumber-gkStuExceptionDtoList?size}人已完成安排。
	<br>未完成安排学生：${gkStuExceptionDtoList?size}人<br>
		<#if returnStrMap?exists && (returnStrMap?size>0)>
			<#list returnStrMap?keys as key>
				<font color="red">${key!}有</font>:${returnStrMap[key]!};<br>
			</#list>
		</#if>
		
	<#else>
	共有${stuNumber}学生选课，所有人已完成安排。
	</#if>
	</div>
	<div class="col-sm-6">
	<h4 class="">同一时间至少需要老师：</h4>
	<table class="table table-striped table-bordered table-hover no-margin">
	    <thead>
	        <tr>
	            <th >科目</th>
	            <#if gkBatchMapByCourse?? && (gkBatchMapByCourse?size>0)>
	            	<#list gkBatchMapByCourse?keys as key>
	            		<td>${key!}</td>
	            	</#list>
	            </#if>
	        </tr>
	    </thead>
	    <tbody>
	    	<tr>
	    		<th >数量</th>
	    		<#if gkBatchMapByCourse?? && (gkBatchMapByCourse?size>0)>
	            	<#list gkBatchMapByCourse?keys as key>
	            		<td>${gkBatchMapByCourse[key]!}</td>
	            	</#list>
	            </#if>
	    	</tr>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
	<h4 class="">同一时间至少需要场地：</h4>
	<table class="table table-striped table-bordered table-hover no-margin">
	    <thead>
	        <tr>
	            <th >${PCKC!}</th>
	            <#if gkBatchMapByBatch?? && (gkBatchMapByBatch?size>0)>
	            	<#list gkBatchMapByBatch?keys as key>
	            		<td>${key!}</td>
	            	</#list>
	            </#if>
	        </tr>
	    </thead>
	    <tbody>
	    	<tr>
	    		<th >数量</th>
	    		<#if gkBatchMapByBatch?? && (gkBatchMapByBatch?size>0)>
	            	<#list gkBatchMapByBatch?keys as key>
	            		<td>${gkBatchMapByBatch[key]!}</td>
	            	</#list>
	            </#if>
	    	</tr>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
		<h4 class="noprint">科目统计图</h4>
		<@chartstructure.pieChart divClass="noprint" loadingDivId="courseChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData1!}" isShowLegend=false isShowToolbox=false/>
	</div>
	<div class="col-sm-6">
		<h4 class="noprint">${PCKC!}统计图</h4>
		<@chartstructure.pieChart divClass="noprint" loadingDivId="batchChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData2!}" isShowLegend=false isShowToolbox=false/>
	</div>
</div>
