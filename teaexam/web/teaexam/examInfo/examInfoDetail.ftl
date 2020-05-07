<div class="box box-default">
					<div class="box-body">
<#assign examEdit = true />
<#assign infoStr = '考试' />
<#if teaexamInfo.infoType?default(0) !=0>
<#assign infoStr = '培训' />
<#assign examEdit = false />
</#if>					
<div class="form-horizontal" role="form">
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">${infoStr!}名称：</label>
										<div class="col-sm-3">
											${teaexamInfo.examName!}
										</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">报名开始时间：</label>
										<div class="col-sm-3">
											${teaexamInfo.registerBegin?string('yyyy-MM-dd')!}
										</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">报名结束时间：</label>
										<div class="col-sm-3">
											${teaexamInfo.registerEnd?string('yyyy-MM-dd')!}
										</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">${infoStr!}开始时间：</label>
										<div class="col-sm-3">
											${teaexamInfo.examStart?string('yyyy-MM-dd')?default("")!}
										</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">${infoStr!}结束时间：</label>
										<div class="col-sm-3">
											${teaexamInfo.examEnd?string('yyyy-MM-dd')?default("")!}
										</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									<#if examEdit>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">考试科目：</label>
										<div class="col-sm-6">
											<table class="table table-bordered table-striped table-hover no-margin">
												<thead>
													<tr>
														<th>科目</th>
														<th>学段</th>
														<th>考试时间</th>
													</tr>
												</thead>
												<tbody>
												<#if subjectList?exists && subjectList?size gt 0>
												    <#list subjectList as item>
													<tr>
													    <td width="30%" style="word-break:break-all;">
													    	${item.subjectName!}
													    </td>
													    <td>
													    	<#if item.section==1>
													    	    小学
													    	<#elseif item.section==0>
													    	  学前
													    	<#elseif item.section==2>
													    	    初中
													    	<#elseif item.section==3>
													    	    高中
													    	</#if>
													    	
													    </td>
													    <td>
													    	${item.startTime?string('yyyy-MM-dd HH:mm')!} ~ ${item.endTime?string('yyyy-MM-dd HH:mm')!}
													    </td>
													</tr>
													</#list>
												</#if>
												</tbody>
											</table>
										</div>
									</div>
									</#if>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">${infoStr!}学校：</label>
										<div class="col-sm-6">
											<table class="table table-bordered table-striped table-hover no-margin">
												<tbody>
												<#if schIdList?exists && schIdList?size gt 0>
												    <#assign e=0>
												    <#list schIdList as item>
													      <#if (item_index+1) %4 == 0>
													         <#assign e=e+1>
													         <tr>
													         <#list 1..4 as c>
													             <#assign a = item_index+1-c>
													             <td>${schNameMap[schIdList[a]]!}</td>
													         </#list>
													         </tr>
													      </#if>
													</#list>
													<#if (schIdList?size%4) gt 0>
													     <#assign t = e*4>
													     <#assign p = (schIdList?size)%4>
													     <tr>
													         <#list 1..p as d>
													           <td>${schNameMap[schIdList[d+t-1]]!}</td>
													         </#list>
													     </tr>
													</#if>
												</#if>												
												</tbody>
											</table>
											<div class="text-right color-999">共${schIdList?size!}所学校</div>
										</div>
									</div>
									<#if !examEdit>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding">培训项目：</label>
										<div class="col-sm-6">${teaexamInfo.trainItems!}</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
									</#if>
									<div class="form-group">
										<label class="col-sm-2 control-label no-padding"><#if examEdit>考试说明<#else>培训要求</#if>：</label>
										<div class="col-sm-6">${teaexamInfo.description!}</div>
										<div class="col-sm-4 control-tips"></div>
									</div>
								</div>
</div>
								</div>
<script>
$(function(){
	showBreadBack(searchList,true,"考试设置");
})

function searchList(){
    var acadyear = '${year!}';
    var semester = '${type!}';
    url = "${request.contextPath}/teaexam/examInfo/index/page?year="+acadyear+"&type="+semester;
    $(".model-div").load(url);
}
</script>