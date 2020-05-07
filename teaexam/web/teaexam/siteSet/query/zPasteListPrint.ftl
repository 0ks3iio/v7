<script type="text/javascript" src="${resourceUrl}/js/LodopFuncs.js"></script>
<script type="text/javascript" src="${resourceUrl}/components/jquery/dist/jquery.js"></script>
<link rel="stylesheet" href="${resourceUrl}/css/pages.css" />
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css" />
<link rel="stylesheet" href="${resourceUrl}/css/components.css" />
<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css" />
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.min.css" />
<input type="hidden" id="batchIdLeft" value="${batchId!}">
<input type="hidden" id="doNotPrint" value="${doNotPrint!}">
<input type="hidden" id="examName" value="${examName!}">
<input type="hidden" id="unitName" value="${roomNoLocationIdMap[roomNo!]!}">
<input type="hidden" id="roomNo" value="${roomNo!}">
<input type="hidden" id="listSize" value="${regList2?size}">
<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item">
			<button class="btn btn-blue" onClick="doprint();">打印</button>
			<button class="btn btn-white">批量打印</button>
		</div>
	</div>
	<div class="picker-table">
		<table class="table">
			<tbody>
				<tr>
					<th>筛选条件：</th>
					<td>
						<div class="float-left mt3">科目：</div>
						<div class="float-left mr10">
							<select name="" id="subjectId" class="form-control" onChange="queryMList('${roomNo!}');">
							<option value="">--请选择--</option>
							<#if subList?exists && subList?size gt 0>
							    <#list subList as sub>
								    <option value="${sub.id!}" <#if subId?exists && '${subId!}' == '${sub.id!}'>selected="selected"</#if>>${sub.subjectName!}
								    (
								    <#if sub.section == 1>
								     小学
								    <#elseif sub.section == 0>
						    		学前
								    <#elseif sub.section == 2>
								    初中
								    <#elseif sub.section == 3>
								    高中
								    </#if>
								    )</option>
								</#list>
						    </#if>
							</select>
						</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<th width="150" style="vertical-align: top;">考场：</th>
					<td>
						<div class="outter">
							<#if roomNoList?exists && roomNoList?size gt 0>
							    <#list roomNoList as item>
							          <a <#if roomNo?exists && roomNo == item>class="selected"</#if> href="javascript:void(0);" onClick="queryMList('${item!}');">${item!}</a>
							    </#list>
							</#if>
						</div>
					</td>
					<td width="75" style="vertical-align: top;">
						<div class="outter">
							<a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="filter">
		<div class="filter-item">
			<div class="filter-name">考试名称：</div>
			<div class="filter-content">${examName!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考点：</div>
			<div class="filter-content">${roomNoLocationIdMap[roomNo!]!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考场：</div>
			<div class="filter-content">${roomNo!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考生：</div>
			<div class="filter-content">${regList2?size}人</div>
		</div>
	</div>
	<h1 class="text-center">${roomNo!}考场座位号</h1>
    <div class="row" id="print">
    <#if regList2?exists && regList2?size gt 0>
		    <#list regList2 as item>
		<div class="col-xs-4" <#if (item_index+1)%15==0>style="page-break-after:always;"</#if>> 
            <div class="box-boder exaRoom-border">
		         <h1 class="exaRoom-fontSize text-center">${item.seatNo!}</h1>
		         <table class="table table-bordered table-striped table-condensed table-hover exaRoom-table">
					<tbody>
						<tr>
							<td class="text-center">姓名：</td>
							<td>${item.teacherName!}</td>
						</tr>
						<tr>
							<td class="text-center">科目：</td>
							<td>${item.subName!}</td>
						</tr>
						<tr>
							<td class="text-center">考号：</td>
							<td>${item.cardNo!}</td>
						</tr>
						<tr>
							<td class="text-center">考场：</td>
							<td>${roomNo!}</td>
						</tr>
					</tbody>
				 </table>
		    </div>
	   </div>
	   </#list>
	</#if>
	</div>
</div>
<script>
$(function(){
	window.parent.doBatchPrint();
})

function getSubContent() {
	return getPrintContent(jQuery("#print"));
}
</script>