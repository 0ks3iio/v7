<#import "/fw/macro/chartstructure.ftl" as chartstructure />	
<h4 class="mt20 mb20"><b>各学科均衡度分析</b><span class="font-12 color-999">（以年级标准分T为参照）</span></h4>		
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">考试：</span>
		<div class="filter-content">
			<select class="form-control" id="examId" name="examId" onChange="showRadar('toChange')" style="width:200px;">
				<#if examList?exists && (examList?size>0)>
                    <#list examList as item>
	                     <option value="${item.id!}" <#if examId?default(' ')==item.id>selected</#if>>${item.examName!}</option>
                    </#list>
                <#else>
                    <option value="">---请选择---</option>
                 </#if>
			</select>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-6">
		<#if statList?exists && statList?size gt 0>
			<@chartstructure.radar loadingDivId="mychart01" divStyle="height:550px;" jsonStringData=jsonStringData />
		<#else>
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
					</span>
					<div class="no-data-body">
						<p class="no-data-txt">暂无记录</p>
					</div>
				</div>
			</div>
		</#if>
	</div>
	<div class="col-xs-6">
		<p>图表说明：</p>
		<p>1、各科均衡分析按照年级标准分T为参照进行对比分析。</p>
		<p>2、标准分T高的学科是相对优势的学科，标准分T低的学科是相对劣势的学科，群体的平均标准分T为50。</p>
	</div>
</div>
<script>
$(function(){
	var examClass=$("#examId");
	$(examClass).chosen({
		width:'200px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:false, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
});
</script>
