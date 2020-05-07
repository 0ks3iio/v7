<div class="table-container-body">
	<table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th class="text-center">记录类型</th>
				<th>内容</th>
				<th>登记时间</th>
				<th>登记导师</th>
			</tr>
		</thead>
		<tbody>
		   <#if trMap??&& trMap?size gt 0>
             <#list trMap?keys as key>
                 <#list trMap[key] as tr>
                    <tr class="${tr.recordType!}">
                       <#if tr_index == 0>
							<td class="text-center" rowspan="${trMap[key]?size!}">${mcodeSetting.getMcode("DM-DSJL-LX","${tr.recordType!}")}</td>
					   </#if>
						<td>${tr.recordResult!}</td>
						<td>${tr.creationTime?string('yyyy-MM-dd HH:mm')!}</td>
						<td>${tr.teacherName!}</td>
						<input type="hidden"  class="recordType"  value="${tr.recordType!}"/>
					</tr>
                 </#list>
            </#list>
          <#else>
			<tr>
				<#if tManage?default(false)>
				<td  colspan="5" align="center">
				<#else>
				<td  colspan="4" align="center">
				</#if>
				 ${messageEmpty!"暂无记录"}
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>
