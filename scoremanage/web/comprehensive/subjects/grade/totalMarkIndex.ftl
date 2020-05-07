<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">各科总评成绩管理</h4>
	</div>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="totalMarkList();">
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
					<select name="searchSemester" id="searchSemester" class="form-control" onChange="totalMarkList();">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue js-add" onClick="totalMarkAdd();">新增</button>
			</div>
		</div>
		<div class="table-container" id="totalMarkDiv">
		
		</div>
	</div>
</div>
<script>
	$(function(){
		totalMarkList();
	});
	
	function totalMarkList() {
		var searchAcadyear = $("#searchAcadyear").val();
		var searchSemester = $("#searchSemester").val();
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester;
	    var url =  '${request.contextPath}/comprehensive/subjects/totalMark/List/page'+str;
		$("#totalMarkDiv").load(url);
	}
	
	function totalMarkAdd() {
		var searchAcadyear = $("#searchAcadyear").val();
		var searchSemester = $("#searchSemester").val();
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester;
		var url = "${request.contextPath}/comprehensive/subjects/totalMark/Edit/page"+str;
		indexDiv = layerDivUrl(url,{title: "新增",width:450,height:450});
	}
</script>