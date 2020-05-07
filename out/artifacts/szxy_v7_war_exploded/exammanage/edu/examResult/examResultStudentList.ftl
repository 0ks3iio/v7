<div class="box-body">
	<div class="filter-container">
		<div class="filter">
			<button class="btn btn-blue" onclick="backResultDetail()">返回</button>
		</div>
	</div>
	<div class="filter">
		<div class="filter-item">
			<div class="filter-name">考区名称：</div>
			<div class="filter-content">
				<p>${regionName!}</p>
			</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考点名称：</div>
			<div class="filter-content">
				<p>${optionName!}</p>
			</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考场编号：</div>
			<div class="filter-content">
				<p>${placeCode!}</p>
			</div>
		</div>
	</div>
	<div class="row">
		<#if emPlaceStudentList?exists && emPlaceStudentList?size gt 0>
		<#list emPlaceStudentList as item>
		<div class="col-sm-3">
			<div class="report-tpl report-tpl2 clearfix">
				<img src="${request.contextPath}${item.studentFilePath!}"  class="report-tpl-img" width="75px" height="105px">
				<h4 class="report-tpl-name">座位号：${item.seatNum!}</h4>
				<h4>姓名：${item.studentName!}</h4>
				<h4>考号：${item.examNumber!}</h4>
			</div>
		</div>
		</#list>
		</#if>
	</div>		
</div>
<input type="hidden" id="cityRegionCode" value="${cityRegionCode!}">
<input type="hidden" id="regionId" value="${regionId!}">
<input type="hidden" id="optionId" value="${optionId!}">
<input type="hidden" id="noArrangeNum" value="${noArrangeNum!}">
<input type="hidden" id="examId" value="${examId!}">
<script>
	function backResultDetail(){
		var cityRegionCode=$("#cityRegionCode").val();
		var regionId=$("#regionId").val();
		var optionId=$("#optionId").val();
		var noArrangeNum=$("#noArrangeNum").val();
		var examId=$("#examId").val();
		var url =  '${request.contextPath}/exammanage/edu/examResult/detailList/page?examId='+examId+"&noArrangeNum="+noArrangeNum+"&regionId="+regionId+"&optionId="+optionId+"&cityRegionCode="+cityRegionCode;
		$("#examResultDiv").load(url);
	}
</script>