<style>
.add-exam-item{
	height:280px;
}
</style>
<form id="subjectClassForm">
<div class="box box-default">
  	<div class="box-header">
	 	<h4 class="box-title">${examInfo.examName!}</h4>
	</div>
	<input type="hidden" name="examId" id="examId" value="${examInfo.id!}">
    <#if !isEdu && isJoin>
	<input type="hidden" name="unitId" value="${unitId!}">
	</#if>
	 <div class="box-body">
		<div class="">
			<div class="add-exam add-exam-tbale clearfix courseInfoDivAll">
				<#if subjectInfoList?exists && (subjectInfoList?size>0)>
				<#list subjectInfoList as item>
				<div class="widget-box add-exam-item courseInfoDiv" id="item_${item_index}" style="height: 420px;width: 312px">
					<input type="hidden" value="${item.id!}" id="id_${item_index}" class="idClass" name="emSubjectInfoList[${item_index}].id">
					<input type="hidden" value="${item.unitId!}" id="unitId_${item_index}" class="unitIdClass" name="emSubjectInfoList[${item_index}].unitId">
					<input type="hidden" value="${item.examId!}" id="examId_${item_index}" class="examIdClass" name="emSubjectInfoList[${item_index}].examId">
					<div class="widget-body" style="height:100%">
						<div class="widget-main padding-12">
							<#if isEditSubject ><a href="javascript:" class="add-exam-close delDiv" id="delDiv_${item_index}"><i class="fa fa-times-circle"></i></a></#if>
							<table width="100%">
								<tr>
									<td class="text-right" width="70">科目：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control subjectIdClass" id="subjectId_${item_index}" name="emSubjectInfoList[${item_index}].subjectId" title="考试科目">
											<option value="">请选择考试科目</option>
											<#if courseList?exists && (courseList?size>0)>
											<#list courseList as citem>
												<option value="${citem.id!}" <#if citem.id==item.subjectId?default('')> selected="selected"</#if>>${citem.subjectName!}</option>
											</#list>
											</#if>
										</select>
									</td>
								</tr>
								<tr class="gkDivClass" <#if item.ysy?default(false)>style="display:none"</#if>>
									<td class="text-right" width="70">考生范围：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control gkSubTypeClass" id="gkSubType_${item_index}" name="emSubjectInfoList[${item_index}].gkSubType" title="成绩录入方式">
											<option value="0" <#if item.gkSubType?default("")=="0">selected="selected"</#if>>所有学生</option>
											<option value="1" <#if item.gkSubType?default("")=="1">selected="selected"</#if>>仅选考学生</option>
											<option value="2" <#if item.gkSubType?default("")=="2">selected="selected"</#if>>仅学考学生</option>
										</select>
									</td>
								</tr>
								<tr class="xueDivClass" <#if item.ysy?default(false) || item.gkSubType?default("")=="1">style="display:none"</#if>>
									<td class="text-right" >学考开始时间：</td>
									<td>
										<div class="input-group">
											<input class="form-control date-picker startDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strStartDate" id="strStartDate_${item_index}" value="${(item.gkStartDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xueDivClass" <#if item.ysy?default(false) || item.gkSubType?default("")=="1">style="display:none"</#if>>
									<td class="text-right" style="width:100px;">学考结束时间：</td>
									<td>
										<div class="input-group">
											<input class="form-control date-picker gkEndDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strGkEndDate" id="strGkEndDate${item_index}"  value="${(item.gkEndDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xuanDivClass" <#if item.gkSubType?default("")=="2">style="display:none"</#if>>
									<td class="text-right" ><span class="endSpanClass"><#if !item.ysy?default(false)>选考</#if>开始时间：</span></td>
									<td>
										<div class="input-group">
											<input class="form-control date-picker startDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strStartDate" id="strStartDate_${item_index}" value="${(item.startDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xuanDivClass" <#if item.gkSubType?default("")=="2">style="display:none"</#if>>
									<td class="text-right" style="width:100px;"><span class="endSpanClass"><#if !item.ysy?default(false)>选考</#if>结束时间：</span></td>
									<td>
										<div class="input-group">
											<input class="form-control date-picker endDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strEndDate" id="strEndDate${item_index}"  value="${(item.endDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="text-right">分制：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control inputTypeClass" id="inputType_${item_index}" name="emSubjectInfoList[${item_index}].inputType" title="成绩录入方式">
											${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "${item.inputType!}", "0")}
										</select>
									</td>
								</tr>
								<tr class="inputDivClass" <#if item.inputType?default('')=='G'>style="display:none"</#if>>
									<td class="text-right">满分：</td>
									<td>
										<input <#if !isEditSubject >readonly</#if> type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
											id="fullScore_${item_index}" name="emSubjectInfoList[${item_index}].fullScore"
											value="<#if item.fullScore??>#{item.fullScore?default(0);M2}</#if>">
									</td>
								</tr>
								<tr class="gradeTypeDivClass" <#if item.id?default('')=='' || item.inputType?default('')=='S'>style="display:none"</#if>>
									<td class="text-right">满分：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> id="gradeType_${item_index}" name="emSubjectInfoList[${item_index}].gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
											<option value="">选择等第显示分等(不能为空)</option>
											${mcodeSetting.getMcodeSelect("DM-DDMC", item.gradeType?default(''), "0")}
										</select>
									</td>
								</tr>
								<tr>
									<td class="text-right">锁定成绩：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control isLockClass" id="isLock_${item_index}" name="emSubjectInfoList[${item_index}].isLock" title="是否锁定成绩(默认否)">
											<option value="0" >是否锁定成绩(默认否)</option>
											<option value="1" <#if (item.isLock?default(0))?string=='1'>selected</#if>>是</option>
										</select>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				</#list>
				</#if>
			</div>
		</div>
	</div>
</div>
</form>

<!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
	showBreadBack(gobackIndex,true,"返回");
});
</script>