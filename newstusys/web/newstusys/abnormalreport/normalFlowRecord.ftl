<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">异动类型：</span>
		<div class="filter-content">
			<select name="state" id="state" class="form-control" onchange="searchFlow()">
					<option value="">全部</option>
					<option value="1" <#if '${state!}'=='1'>selected</#if>><#if deploy?default('')=='hangwai'>学籍增加<#else>转入</#if></option>
					<option value="2" <#if '${state!}'=='2'>selected</#if>><#if deploy?default('')=='hangwai'>学籍减少<#else>转出</#if></option>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">异动原因：</span>
		<div class="filter-content">
			<select name="flowType" id="flowType" class="form-control" onchange="searchFlow()">
				<option value="">全部</option>
				<#if '${state!}'!='2'>
					<#if enterFlowTypes?exists && enterFlowTypes?size gt 0>
					<#list enterFlowTypes as entype>
					<option value="${entype.thisId!}" class="zr" <#if flowType! == entype.thisId!>selected</#if>>${entype.mcodeContent!}</option>
					<#--
					option value="12" class="zr" <#if '${flowType!}'=='12'>selected</#if>>复学</option>
					<option value="07" class="zr" <#if '${flowType!}'=='07'>selected</#if>>入境</option>
					<option value="08" class="zr" <#if '${flowType!}'=='08'>selected</#if>>县区内转入</option>
					<option value="26" class="zr" <#if '${flowType!}'=='26'>selected</#if>>省内转入</option>
					<option value="27" class="zr" <#if '${flowType!}'=='27'>selected</#if>>省外转入</option>
					<option value="28" class="zr" <#if '${flowType!}'=='28'>selected</#if>>户口新报</option>
					<option value="29" class="zr" <#if '${flowType!}'=='29'>selected</#if>>一年级补报</option>
					<option value="89" class="zr" <#if '${flowType!}'=='89'>selected</#if>>其他增加</option-->
					</#list>
					</#if>
				</#if>
				<#if '${state!}'!='1'>
					<#if leaveFlowTypes?exists && leaveFlowTypes?size gt 0>
					<#list leaveFlowTypes as entype>
					<option value="${entype.thisId!}" class="zc" <#if flowType! == entype.thisId!>selected</#if>>${entype.mcodeContent!}</option>
					<#-- 
					option value="11" class="zc" <#if '${flowType!}'=='11'>selected</#if>>休学</option>
					<option value="35" class="zc" <#if '${flowType!}'=='35'>selected</#if>>出境</option>
					<option value="36" class="zc" <#if '${flowType!}'=='36'>selected</#if>>转往县区内</option>
					<option value="37" class="zc" <#if '${flowType!}'=='37'>selected</#if>>转往省内</option>
					<option value="38" class="zc" <#if '${flowType!}'=='38'>selected</#if>>转往省外</option>			
					<option value="51" class="zc" <#if '${flowType!}'=='51'>selected</#if>>死亡</option>
					<option value="99" class="zc" <#if '${flowType!}'=='99'>selected</#if>>其他</option -->
					</#list>
					</#if>
				</#if>			
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
	</div>
</div>
<div class="table-container">
	<div class="table-container-body print">
		<form id="subForm">
		<table class="table table-bordered">
			<thead>
				<tr>
				    <th class="text-center">姓名</th>
				    <th class="text-center">身份证号</th>
				    <th class="text-center">异动类型</th>
				    <th class="text-center">转入（转出）学校</th>
				    <th class="text-center">年级</th>
				    <th class="text-center">班级</th>
				    <th class="text-center">异动时间</th>
				    <th class="text-center">异动原因</th>
				    <th class="text-center">备注</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
  <#list dtoList as dto>
  	 <tr>
  	 	<td class="text-center">${dto.studentName!}</td>
        <td class="text-center">${dto.idCardNo!}</td>
        <td class="text-center">${dto.flowtypeName!}</td>
        <td class="text-center">${dto.schName!}</td>
        <td class="text-center">${dto.gradeName!}</td>
        <td class="text-center">${dto.className!}</td>
        <td class="text-center">${dto.flowdate?string('yyyy-MM-dd')!}</td>
        <td class="text-center">${dto.flowreason!}</td>
        <td class="text-center" style="max-width:150px;word-break:break-all;">${dto.remark!}</td>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="9" align="center">
  		暂无数据
  	</td>
  <tr>
  </#if>
			
			</tbody>
		</table>
	
		</form>
	</div>
		<#if dtoList?exists&&dtoList?size gt 0>
	         <@htmlcom.pageToolBar container="#rosterDiv" class="noprint"/>
        </#if>
</div>	
<script>
function doExport(){
    var state = $('#state').val();
    var flowType = $('#flowType').val();
    var unitId = $('#unitId').val();
    document.location.href="${request.contextPath}/newstusys/student/abnormalreport/normalFlowRecordExport?unitId=${unitId!}&state="+state+"&flowType="+flowType;
}

function searchFlow(){
   var state = $('#state').val();
   var flowType = $('#flowType').val();
   var unitId = $('#unitId').val();
   
   var url =  '${request.contextPath}/newstusys/student/abnormalreport/normalFlowRecord?unitId=${unitId!}&state='+state+'&flowType='+flowType;
   $("#rosterDiv").load(url); 
}
</script>