<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
								<div class="table-container">
									<div class="table-container-header text-right">
										<a class="btn btn-white" onClick="doExport();">导出</a>
									</div>
									<div class="table-container-body">
										<table class="table table-striped table-hover">
											<thead>
												<tr>
													<th>班級</th>
													<th>学生姓名</th>
													<th>学号</th>
													<th>违纪类型</th>
													<th>违纪原因</th>
													<th>违纪扣分</th>
													<th>违纪时间</th>
												</tr>
											</thead>
											<tbody>
										    <#if dyStuPunishmentList?exists && dyStuPunishmentList?size gt 0>
											    <#list dyStuPunishmentList as item>
												<tr>
												    <input type="hidden" id="id" value="${item.id!}">
												    <td width="10%">${item.className!}</td>
													<td width="10%">${item.studentName!}</td>
													<td width="10%">${item.studentCode!}</td>
													<td width="20%" style="word-break:break-all;">${item.punishName!}</td>
													<td width="30%" style="word-break:break-all;">${item.punishContent!}</td>
													<td width="10%">${(item.score)?string('0.#')!}</td>
													<td width="20%">${(item.punishDate?string("yyyy-MM-dd"))!}</td>
												</tr>
												</#list>
											</#if>
											</tbody>
										</table>
										<#if dyStuPunishmentList?exists && dyStuPunishmentList?size gt 0>
  	                                        <@htmlcom.pageToolBar container="#showList" class="noprint"/>
                                        </#if>
									</div>
								</div>
<script>
function doExport(){
   var classId = $('#classId').val();
   var gradeId = $('#gradeId').val();
   var studentName = $('#stuName').val();
   var punishTypeId = $('#punishTypeId').val();
   var startTime = $('#startTime').val();
   var endTime = $('#endTime').val();
   if(startTime != '' && endTime != '' && startTime>endTime){
        layerTipMsgWarn("提示","开始时间不能大于结束时间");
	    return;
    }
   document.location.href = "${request.contextPath}/stuwork/studentManage/punishScoreQueryExport?gradeId="+gradeId+"&classId="+classId+"&studentName="+studentName+"&punishTypeId="+punishTypeId+"&startTime="+startTime+"&endTime="+endTime;
}
</script>