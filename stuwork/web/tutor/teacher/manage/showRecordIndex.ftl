<div class="main-content">
	<div class="main-content-inner">
		<div class="page-content">
			<a  class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
			<div class="box box-default">
				<div class="box-body">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">学年：</span>
							<div class="filter-content">
								<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showRecordList()">
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
								<select id="searchSemester" name="searchSemester" class="form-control" onChange="showRecordList()">
										${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">学生：</span>
							<div class="filter-content">
							    <select name="studentName" id="studentName" class="form-control" onChange="showRecordList()">
								    <#if studentList?exists && (studentList?size>0)>
								      <#list studentList as stu>
								          <option value="${stu.id!}" <#if stu.id == studentId>  selected </#if>>${stu.studentName!}</option>
								      </#list>
								    </#if> 
								</select>
							</div>
						</div>
					</div>
					<div class="table-container" id="showReList">
					
					</div>
				</div>
			</div>
		</div><!-- /.page-content -->
	</div>
</div><!-- /.main-content -->
<div class="layer layer-checkIn-singLe">

</div><!-- 编辑单个记录 -->

<script>
    
    $(document).ready(function(){
        showRecordList();
    });
  
  	function showRecordList(){
  	    var ss = $("#searchAcadyear ").get(0).selectedIndex;
		var acadyear = $("#searchAcadyear").val();
		var semester = $("#searchSemester").val();
		var studentId = $("#studentName").val();
		$("#showReList").load("${request.contextPath}/tutor/manage/showRecordList?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId);
	
	};

    //点击返回
    $('.page-back-btn').click(function(){
        mangeStudentIndex();
    })

</script>



