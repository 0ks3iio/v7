<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">最近30天新增的考试</h4>
	</div>
	<div class="box-body clearfix">
	        <div class="table-container">
				<div class="table-container-body" id="showList1">
					
				</div>
			</div>
				
	</div>
</div>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">30天以前的考试</h4>
	</div>
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showList2()">
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
	                    <#else>
		                    <option value="">未设置</option>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select class="form-control" id="searchSemester" name="searchSemester" onChange="showList2()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="searchGradeCode" name="searchGradeCode"  onChange="showList2()">
						<option value="">全部</option>
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.gradeCode!}">${item.gradeName!}</option>
		                    </#list>
	                     </#if>
					</select>
				</div>
			</div>
		</div>
		<div class="table-container">
			<div class="table-container-body" id="showList2">
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		showList1();
		showList2();
	});
	function showList2(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
	     var searchGradeCode = $('#searchGradeCode').val();
		 var searchType = $('#searchType').val();
		 var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchGradeCode="+searchGradeCode+"&searchType="+searchType;
	     var url =  '${request.contextPath}/exammanage/edu/examArrange/list2/page'+str;
		 $("#showList2").load(url);
	}
	function showList1(){
		var url =  '${request.contextPath}/exammanage/edu/examArrange/list1/page';
		$("#showList1").load(url);
	}
	
</script>

