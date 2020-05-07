<#import "/fw/macro/chartstructure.ftl" as chartstructure>
<div class="tab-content" id="resultListDiv">
	<div id="aa" class="tab-pane active" role="tabpanel">
		<div class="row">
			<div class="col-sm-6">
				<h4 class="noprint">单科统计结果</h4>
				<#if jsonStringData1?exists>
					<@chartstructure.pieChart divClass="noprint" loadingDivId="oneChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;" jsonStringData="${jsonStringData1!}" isShowLegend=false isShowToolbox=false/>
				<#else>
					<div style="height:400px;border:1px solid #ddd;margin-bottom:20px;">暂无数据</div>
				</#if>
			</div>
			<div class="col-sm-6">
				<h4 class="noprint">二科统计结果</h4>
				<#if jsonStringData2?exists>
					<@chartstructure.histogram divClass="noprint" loadingDivId="twoChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;"  jsonStringData="${jsonStringData2!}" isShowLegend=false isShowToolbox=false />
				<#else>
					<div style="height:400px;border:1px solid #ddd;margin-bottom:20px;" >暂无数据</div>
				</#if>
			</div>
			<div class="col-sm-12">
				<h4 class="noprint">三科统计结果</h4>
				<#if jsonStringData1?exists>
					<@chartstructure.histogram divClass="noprint" loadingDivId="threeChart" divStyle="height:400px;border:1px solid #ddd;margin-bottom:20px;"  jsonStringData="${jsonStringData3!}" isShowLegend=false isShowToolbox=false/>
				<#else>
					<div style="height:400px;border:1px solid #ddd;margin-bottom:20px;">暂无数据</div>
				</#if>
			</div>
		</div>
	</div>
</div>
