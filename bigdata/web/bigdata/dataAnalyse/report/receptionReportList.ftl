<#if resultList?exists && resultList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th><#if type!  ==6>看板<#else>报告</#if>名称</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
		    	<#list resultList as report>
		    	<tr>
			        <td>${report.name!}</td>
			         <td>
			               <a href="javascript:void(0)" chart-id="${report.id}" onclick="designMultiReport('${report.id!}','${report.type!}')" class="look-over">设计</a><span class="tables-line">|</span>
			               <a href="javascript:void(0)" chart-id="${report.id}" onclick="previewMultiReport('${report.id!}','${report.type!}','${report.name!}')" class="look-over">预览</a><span class="tables-line">|</span>
			               <a href="javascript:void(0)" chart-id="${report.id}" onclick="editMultiReport('${report.id!}','${report.type!}')"  class="look-over">设置</a><span class="tables-line">|</span>
			                <a href="javascript:void(0)" class="remove" onclick="deleteMultiReport('${report.id!}','${report.name!}');" chart-id="${report.id}">删除</a>
			        </td>
			  	</tr>
			  	</#list>
		    </tbody>
		</table>
<#else>
    <div class="no-data-word">
    	<img src="${request.contextPath}/bigdata/v3/static/images/kanban-design/img-focus.png"/>&nbsp;&nbsp;暂无记录，请<span class="js-add-kanban color-00cce3 pointer" onclick="addMultiReport('${type!}')">&nbsp;<#if type!  ==6>新建看板<#else>新建报告</#if></span>
    </div>
</#if>
<div class="layer kanban-set"></div>
<script>
    function designMultiReport(id,type) {
        var titleName ="看板设计";
    	if('7'==type)
    		titleName="报告设计";
    	router.go({
	        path: '/bigdata/data/analyse/design?type='+type+'&reportId='+ id,
	        name: titleName,
	        level: 2
	    }, function () {
	    	var url = '${request.contextPath}/bigdata/data/analyse/design?type='+type+'&reportId='+ id;
	         $('.page-content').load(url);
	    });
    }

	function previewMultiReport(id,type,name) {
		var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
	 	window.open(url,id)
    }
</script>