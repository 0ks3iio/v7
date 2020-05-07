<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if jsonStringData3?exists&&jsonStringData3!="">
    <div class="row">
			<div class="col-xs-6">
                <@chartstructure.radar loadingDivId="mychart03"  isShowToolbox=false  jsonStringData=jsonStringData3 />
            </div>
        <div class="col-xs-6">
            <p>图表说明：</p>
            <p>1、各科均衡分析按照班级标准分T为参照进行对比分析。</p>
            <p>2、标准分T高的学科是相对优势的学科，标准分T低的学科是相对劣势的学科，群体的平均标准分T为50。</p>
        </div>
    </div>
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