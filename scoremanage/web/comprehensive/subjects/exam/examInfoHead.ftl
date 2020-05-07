<div class="box box-default">
	<div class="box-header">
		<h3>最近30天新增的考试</h3>
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
		<h3>30天以前的考试</h3>
	</div>
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="acadyear" name="acadyear" onChange="showList2()">
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
					<select class="form-control" id="semester" name="semester" onChange="showList2()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="gradeId" name="gradeId" onChange="showList2()">
						<option value="">全部</option>
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.id!}">${item.gradeName!}</option>
		                    </#list>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span  class="filter-name">类别：</span>
				<div class="filter-content">
					<select name="examType" id="examType" oid="examType" class="form-control" onChange="showList2()">		
						        <option value="">--- 请选择 ---</option>
						        <#if kslbList?? && (kslbList?size>0)>
								<#list kslbList as item>
									<option value="${item.thisId}">${item.mcodeContent!}</option>
								</#list>
							    <#else>
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

<script>
	$(function(){
		showList1();
		showList2();
	});
	function showList2(){
		 var acadyear = $('#acadyear').val();
	     var semester = $('#semester').val();
	     var gradeId = $('#gradeId').val();
		 var examType = $('#examType').val();
		 var str = "?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&examType="+examType;
	     var url =  '${request.contextPath}/comprehensive/subjects/score/list2/page'+str;
		 $("#showList2").load(url);
	}
	function showList1(){
		var url =  '${request.contextPath}/comprehensive/subjects/score/list1/page';
		$("#showList1").load(url);
	}
</script>

