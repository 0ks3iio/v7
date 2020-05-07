<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="main-content">				
				<div class="main-content-inner">
					<div class="page-content">
						<div class="box box-default">
							<div class="box-body">
								<div class="filter">
									<div class="filter-item">
										<span class="filter-name">学年：</span>
										<div class="filter-content">
											<select name="acadyearId" id="acadyearId" class="form-control" onChange="searchList();">
												<#if acadyearList?exists && acadyearList?size gt 0>
													<#list acadyearList as acadyear>
														<option value="${acadyear!}" <#if acadyear == nowAcadyear>selected</#if>>${acadyear!}</option>
													</#list>
												</#if>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">学期：</span>
										<div class="filter-content">
											<select name="semesterId" id="semesterId" class="form-control" onChange="searchList();">
												<option value="1" <#if 1==nowSemester>selected</#if>>第一学期</option>
												<option value="2" <#if 2==nowSemester>selected</#if>>第二学期</option>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">年级：</span>
										<div class="filter-content">
											<select name="gradeId" id="gradeId" class="form-control" onChange="getClassList();">
											<option value="">--请选择--</option>
											<#if gradeList?exists && gradeList?size gt 0>
											  <#list gradeList as item>
												 <option value="${item.id!}">${item.gradeName!}</option>
											  </#list>
											</#if>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">班级：</span>
										<div class="filter-content">
											<select name="classId" id="classId" class="form-control" onChange="searchList();">
											<option value="">---请选择---</option>
											<#if classList?exists && (classList?size>0)>
					                           <#list classList as item>
						                           <option value="${item.id!}">${item.classNameDynamic!}</option>
					                           </#list>
				                            </#if>	
				                            </select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">姓名：</span>
										<div class="filter-content">
											<input type="text" class="form-control" id="stuName">
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">类型：</span>
										<div class="filter-content">
											<select name="punishTypeId" id="punishTypeId" class="form-control" onChange="searchList();">
											<option value="">--请选择--</option>
											<#if dyBusinessOptionList?exists && dyBusinessOptionList?size gt 0>
											   <#list dyBusinessOptionList as item>
												  <option value="${item.id!}">${item.optionName!}</option>						
											   </#list>
											</#if>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">时间段：</span>
										<div class="filter-content">
											<div class="input-group" style="width:300px;">
												<input type="text" name="startTime" id="startTime" class="form-control datepicker" onChange="searchList();" readOnly>
												<span class="input-group-addon">
													<i class="fa fa-minus"></i>
												</span>
												<input type="text" name="endTime" id="endTime" class="form-control datepicker" onChange="searchList();" readOnly>
											</div>
										</div>
									</div>
									<div class="filter-item">
										<a class="btn btn-blue" onClick="searchList();">查询</a>
									</div>
									
								</div>
                                <div class="table-container-body" id="showList">
		                        </div>
								
							</div>
						</div>
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
<script>		
$(function(){
    $('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});		
	searchList();
});

function getClassList() {
	var gradeId = $('#gradeId').val();
	var clsIdSel = $("#classId");
	$.ajax({
		url: "${request.contextPath}/stuwork/studentManage/clsList",
		data: {gradeId: gradeId},
		dataType: "json",
		success: function (data) {
			clsIdSel.html("");
			clsIdSel.chosen("destroy");
			if (data == null) {
				clsIdSel.append("<option value='' >---请选择---</option>");
			} else {
				clsIdSel.append("<option value='' >---请选择---</option>");
				for (var m = 0; m < data.length; m++) {
					clsIdSel.append("<option value='" + data[m].id + "' >" + data[m].classNameDynamic + "</option>");
				}
			}
			if (m == 0) {
				//subjectIdSel.append("<option value='' >---请选择---</option>");
			}
		}
	});
	//var gradeId = $('#gradeId').val();
	//if (gradeId != '') {
		//layerTipMsgWarn("提示", "请选择一个班级！");
	//}
	searchList();
}

function searchList(){
	var acadyear = $("#acadyearId").val();
	var semester = $("#semesterId").val();
    var classId = $('#classId').val();
    var gradeId = $('#gradeId').val();
    var studentName = $.trim(encodeURI($('#stuName').val()));
    var punishTypeId = $('#punishTypeId').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    if(startTime != '' && endTime != '' && startTime>endTime){
        layerTipMsgWarn("提示","开始时间不能大于结束时间");
	    return;
    }
    var url = "${request.contextPath}/stuwork/studentManage/punishScoreQueryList?gradeId="+gradeId+"&studentName=" + studentName + "&classId=" + classId + "&punishTypeId=" + punishTypeId + "&startTime=" + startTime + "&endTime=" + endTime + "&acadyear=" + acadyear + "&semester=" + semester;
    $("#showList").load(url);
}
</script>