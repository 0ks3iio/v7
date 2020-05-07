<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-body">
						<div class="filter">
							<div class="filter-item">
								<span class="filter-name">学年：</span>
								<div class="filter-content">
									<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showExamList()">
										<#if acadyearList?exists && (acadyearList?size>0)>
						                    <#list acadyearList as item>
							                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
						                    </#list>
					                    </#if>
									</select>
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">学期：</span>
								<div class="filter-content">
									<select id="searchSemester" name="searchSemester" class="form-control" onChange="showExamList()">
										${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
									</select>
								</div>
							</div>
						</div>
						<div class="table-container" id= "showFamRList">
							
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<script>
    
    $(document).ready(function(){
       
        showExamList();
    });
  
  	function showExamList(){
		var acadyear = $("#searchAcadyear").val();
		var semester = $("#searchSemester").val();
		$("#showFamRList").load("${request.contextPath}/tutor/family/showRecordList?acadyear="+acadyear+"&semester="+semester);
	
	};



</script>
