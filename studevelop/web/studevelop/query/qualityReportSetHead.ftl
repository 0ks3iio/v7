<div class="col-xs-12">
	<div class="box box-default">
		<div class="box-header">
			<div class="filter filter-f16">
				<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="queryAcadyear" onChange="searchList()" style="width:168px;">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="querySemester" onChange="searchList()" style="width:168px;">
						 ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
						</select>
					</div>
				</div>	
				<#if sectionMap?exists>
				<div class="filter-item">
					<label for="" class="filter-name">学段：</label>
					<div class="filter-content">
						<select class="form-control" id="section" onChange="searchList()" style="width:168px;">
							<#list sectionMap?keys as key> 
							<option value="${key!}">${sectionMap[key]}</option>
							</#list>
						</select>
					</div>
				</div>
				<#else>
				<input type="hidden" id="section" value="${section!}"/>
				</#if>
																						
		   </div>										
	  </div>	
	  <div class="table-wrapper" id="showList"></div>	
   </div>															
</div>

<script>
$(function(){
	searchList(); 
});

function searchList(){
    var queryAcadyear = $('#queryAcadyear').val();
    var querySemester = $('#querySemester').val();
    var section = $('#section').val();
	var str = "?acadyear="+queryAcadyear+"&semester="+querySemester+"&section="+section;
	var url = "${request.contextPath}/studevelop/stuQualityReportSet/list"+str;
	$("#showList").load(url);
}
</script>
