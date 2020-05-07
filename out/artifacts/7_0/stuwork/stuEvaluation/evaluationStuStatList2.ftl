
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
								<div class="table-container">
									<div class="table-container-header text-right">
										<a class="btn btn-white" onClick="doExport();">导出</a>
									</div>
									<div class="table-container-body" style="width:100%;overflow-x:scroll">
										<table class="table table-striped table-hover">
											<thead>
												<tr>
													<th width="10%">学生姓名</th>
													<th width="10%">班级&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
													<#if adaSemList?exists && adaSemList?size gt 0>
													     <#list adaSemList as item>
													           <th>${item[0]!}<br>&nbsp;&nbsp;&nbsp;第${item[1]!}学期</th>
													     </#list>
													</#if>
												</tr>
											</thead>
											<tbody>
										    <#if stuList?exists && stuList?size gt 0>
											    <#list stuList as item>
												<tr>
                                                      <td width="10%">${item.studentName!}</td>
                                                      <td width="10%">${item.className!}</td>
                                                      <#if adaSemList?exists && adaSemList?size gt 0>
													     <#list adaSemList as item2>
													           <td>${resultMap[item.id+item2[0]+item2[1]]}</td>
													     </#list>
													</#if>
												</tr>
												</#list>
											</#if>
											</tbody>
										</table>
									</div>
										<#if stuList?exists && stuList?size gt 0>
  	                                        <@htmlcom.pageToolBar container="#showList" class="noprint"/>
                                        </#if>
								</div>
<script>
function doExport(){
    var acadyear = $("#acadyearId").val();
	var semester = $("#semesterId").val();
    var classId = $('#classId').val();
    var gradeId = $('#gradeId').val();
    var allcheck = "0";
    if($('#allcheck').is(':checked')){
         allcheck = "1";
    }
    var url = "${request.contextPath}/stuwork/evaluation/stat/doExport?gradeId="+gradeId+"&classId=" + classId + "&acadyear=" + acadyear + "&semester=" + semester+"&allcheck="+allcheck;
   document.location.href = url;
}
</script>