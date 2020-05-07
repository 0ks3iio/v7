<div class="main-content" id="importDiv">
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-body">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">学年：</span>
						    <div class="filter-content">
						        <select name="" id="acadyear" class="form-control" onChange="searchWeek();">
						        <#if acadyearList?exists && acadyearList?size gt 0>
						            <#list acadyearList as item>
									    <option value="${item!}" <#if '${acadyear!}' == '${item!}'>selected</#if>>${item!}</option>
									</#list>
							    </#if>
								</select>
							</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学期：</span>
						<div class="filter-content">
							<select name="" id="semester" class="form-control" onChange="searchWeek();">
								<option value="1" <#if '${semester!}' == '1'>selected</#if>>第一学期</option>
					            <option value="2" <#if '${semester!}' == '2'>selected</#if>>第二学期</option>
							</select>
						</div>
					</div>
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
							<select name="" id="classId" class="form-control" onChange="searchList();">
							<#if clsList?exists && clsList?size gt 0>
					           <#list clsList as item>
					              <option value="${item.id!}">${item.classNameDynamic!}</option>
					           </#list>
					        <#else>
					              <option value="">---请选择---</option>
					        </#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">周次：</span>
						<div class="filter-content">
							<select name="" id="week" class="form-control" onChange="searchList();">
				            <#if weekArray?exists && weekArray?size gt 0>
				               <option value='' >---全部---</option>
				               <#list weekArray as item>
					               <option value="${item!}" <#if '${week!}' == '${item!}'>selected</#if>>${item!}</option>
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
				<div class="table-container" id="showList">
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$(function(){	
	searchList();
	var gradeId = $('#gradeId').val();
	var clsIdSel = $("#classId");
	$.ajax({
		url: "${request.contextPath}/stuwork/weekCheckPerformance/clsList",
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
});

function searchList(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var classId = $('#classId').val();
    var week = $('#week').val();
    var enter = $('#enter').val();
    var gradeId = $('#gradeId').val();
    var url = "${request.contextPath}/stuwork/weekCheckPerformance/weekCheckPerformanceList?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&week="+week+"&enter="+enter+"&gradeId="+gradeId;
    $("#showList").load(url);
}

function searchWeek(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var weekSel=$("#week");
    $.ajax({
			url:"${request.contextPath}/stuwork/weekCheckPerformance/searchWeek",
			data:{acadyear:acadyear,semester:semester},
			dataType: "json",
			success: function(data){
			   weekSel.html("");
			   weekSel.chosen("destroy");
			   weekSel.attr("selected","");
			   if(data==null){
				   weekSel.append("<option value='' >---请选择---</option>");
			   }else{
			       weekSel.append("<option value='' >---请选择---</option>");
				   for(var m=0;m<data.length;m++){
				      weekSel.append("<option value='"+data[m]+"' >"+data[m]+"</option>");
			       }
			   }
			   searchList();
			}
	});
}

function getClassList() {
	var gradeId = $('#gradeId').val();
	var clsIdSel = $("#classId");
	$.ajax({
		url: "${request.contextPath}/stuwork/weekCheckPerformance/clsList",
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
	var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var week = $('#week').val();
    var url = "${request.contextPath}/stuwork/weekCheckPerformance/weekCheckPerformanceList?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&enter="+enter+"&gradeId="+gradeId;
    $("#showList").load(url);
}
</script>