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
											<#if gradeList?exists && gradeList?size gt 0>
											  <#list gradeList as item>
												 <option value="${item.id!}">${item.gradeName!}</option>
											  </#list>
											<#else>
											     <option value="">--请选择--</option>
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
												<option value="3" <#if 3==nowSemester>selected</#if>>一学年</option>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<div class="filter-content">
											<label><input type="checkbox" class="wp checked-input" id="allcheck" onClick="searchList();"><span class="lbl"></span></label>
										</div>									
										<span class="filter-name">展示所有学年学期</span>
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
	getClassList();
});

function getClassList() {
	var gradeId = $('#gradeId').val();
	var clsIdSel = $("#classId");
	$.ajax({
		url: "${request.contextPath}/stuwork/evaluation/clsList",
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
	var acadyear = $("#acadyearId").val();
	var semester = $("#semesterId").val();
    var gradeId = $('#gradeId').val();
    var allcheck = "0";
    if($('#allcheck').is(':checked')){
         allcheck = "1";
    }
    var url = "${request.contextPath}/stuwork/evaluation/stat/list?gradeId="+gradeId+"&acadyear=" + acadyear + "&semester=" + semester+"&allcheck="+allcheck;
    $("#showList").load(url);
}

function searchList(){
	var acadyear = $("#acadyearId").val();
	var semester = $("#semesterId").val();
    var classId = $('#classId').val();
    var gradeId = $('#gradeId').val();
    var allcheck = "0";
    if($('#allcheck').is(':checked')){
         allcheck = "1";
    }
    var url = "${request.contextPath}/stuwork/evaluation/stat/list?gradeId="+gradeId+"&classId=" + classId + "&acadyear=" + acadyear + "&semester=" + semester+"&allcheck="+allcheck;
    $("#showList").load(url);
}
</script>