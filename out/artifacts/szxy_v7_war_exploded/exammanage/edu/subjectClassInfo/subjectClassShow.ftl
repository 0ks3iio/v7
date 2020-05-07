<style>
.add-exam-item{
	height:280px;
}
</style>
<a href="#" class="page-back-btn gotoExamListClass"><i class="fa fa-arrow-left"></i> 返回</a>
<form id="subjectClassForm">
<div class="box box-default">
  	<div class="box-header">
	 	<h4 class="box-title">${examInfo.examName!}</h4>
	</div>
	<input type="hidden" name="examId" id="examId" value="${examInfo.id!}">
   <#if !isEdu && isJoin>
	<input type="hidden" name="unitId" value="${unitId!}">
   	<div class="box-header">
		<h4 class="box-title">班级设置</h4>
	</div>
    <div class="box-body">
		<div class="filter-item block">
			<div class="row">
				<div class="col-xs-12">
					<div class="box-boder">
						<div class="multiselect" id="classIds">
							<#if clazzList?exists && (clazzList?size>0)>
							<#list clazzList as item>
								<#assign ff=false>
								<#if classInfoList?exists && (classInfoList?size>0)>
									<#list classInfoList as item1>
										<#if item.id==item1.classId>
											<#assign ff=true>
											<#break>
										</#if>
									</#list>
								<#else>
									<#assign ff=false>
								</#if>
								<#if ff><span class="label-select-item active " data-value="${item.id!}">${item.classNameDynamic!}</span></#if>
							</#list>
							</#if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</#if>
	
  	 <div class="box-header">
		 <h4 class="box-title">科目设置</h4>
	 </div>
	 <div class="box-body">
		<div class="">
			<div class="add-exam add-exam-tbale clearfix courseInfoDivAll">
				<#if subjectInfoList?exists && (subjectInfoList?size>0)>
				<#list subjectInfoList as item>
				<div class="widget-box add-exam-item courseInfoDiv" id="item_${item_index}">
					<div class="widget-body" style="height:100%">
						<div class="widget-main padding-12">
							<div class="filter">
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control subjectIdClass" id="subjectId_${item_index}" name="emSubjectInfoList[${item_index}].subjectId" title="考试科目">
										<option value="">请选择考试科目</option>
										<#if courseList?exists && (courseList?size>0)>
										<#list courseList as citem>
											<option value="${citem.id!}" <#if citem.id==item.subjectId?default('')> selected="selected"</#if>>${citem.subjectName!}</option>
										</#list>
										</#if>
									</select>
								</div>
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control inputTypeClass" id="inputType_${item_index}" name="emSubjectInfoList[${item_index}].inputType" title="成绩录入方式">
										${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "${item.inputType!}", "0")}
									</select>
								</div>
								<div class="filter-item">
									<div class="filter-content">
									    <div class="input-group">
											<input class="form-control date-picker startDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].startDate" id="startDate_${item_index}" placeholder="考试开始时间(到分钟)" value="${(item.startDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
									    </div>
									 </div>
								</div>
								<div class="filter-item">
									<div class="filter-content">
									    <div class="input-group">
											<input class="form-control date-picker endDateClass" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].endDate" id="endDate_${item_index}" placeholder="考试结束时间(到分钟)" value="${(item.endDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
									    </div>
									 </div>
								</div>
								
								<div class="filter-item inputDivClass" <#if item.inputType?default('')=='G'>style="display:none"</#if>>
									<input <#if !isEditSubject >readonly</#if> type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
									id="fullScore_${item_index}" name="emSubjectInfoList[${item_index}].fullScore"
									 value="<#if item.fullScore??>#{item.fullScore?default(0);M2}</#if>">
								</div>
								<div class="filter-item gradeTypeDivClass" <#if item.id?default('')=='' || item.inputType?default('')=='S'>style="display:none"</#if>>
									<select <#if !isEditSubject >disabled</#if> id="gradeType_${item_index}" name="emSubjectInfoList[${item_index}].gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
										<option value="">选择等第显示分等(不能为空)</option>
										${mcodeSetting.getMcodeSelect("DM-DDMC", item.gradeType?default(''), "0")}
									</select>
								</div>
								
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control isLockClass" id="isLock_${item_index}" name="emSubjectInfoList[${item_index}].isLock" title="是否锁定成绩(默认否)">
										<option value="0" >是否锁定成绩(默认否)</option>
										<option value="1" <#if (item.isLock?default(0))?string=='1'>selected</#if>>是</option>
									</select>
								</div>
							</div>
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
//初始化日期控件参数
var viewContentTime={
	'format' : 'yyyy-mm-dd hh:ii',
	'minView' : '0',
	'startDate':'${(examInfo.examStartDate?string('yyyy-MM-dd HH:mm'))!}',
	'endDate':'${(examInfo.examEndDate?string('yyyy-MM-dd HH:mm'))!}'
};
$(function(){
	$('.gotoExamListClass').on('click',function(){
		var url =  '${request.contextPath}/exammanage/examInfo/head/page';
		$("#exammanageDiv").load(url);
	});	
});


</script>

