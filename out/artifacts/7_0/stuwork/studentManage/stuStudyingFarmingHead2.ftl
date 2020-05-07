<div class="main-content">				
				<div class="main-content-inner">
					<div class="page-content">
						<div class="box box-default">
							<div class="box-body">
								<div class="filter">
									
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
										<span class="filter-name">录入情况：</span>
										<div class="filter-content">
											<select name="enter" id="enter" class="form-control" onChange="searchList();">
											       <option value="">---全部---</option>
											       <option value="1">---已录入---</option>
											       <option value="0">---未录入--</option>
				                            </select>
										</div>
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
	searchList();
});

function getClassList() {
	var gradeId = $('#gradeId').val();
	var clsIdSel = $("#classId");
	$.ajax({
		url: "${request.contextPath}/stuwork/studyingFarming/clsList",
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
		}
	});
	var enter = $('#enter').val();
    var url = "${request.contextPath}/stuwork/studyingFarming/pageList?gradeId="+gradeId+"&enter="+enter;
    $("#showList").load(url);
}

function searchList(){
    var classId = $('#classId').val();
    var gradeId = $('#gradeId').val();
    var enter = $('#enter').val();
    var url = "${request.contextPath}/stuwork/studyingFarming/pageList?gradeId="+gradeId+"&classId=" + classId+"&enter="+enter;
    $("#showList").load(url);
}
</script>