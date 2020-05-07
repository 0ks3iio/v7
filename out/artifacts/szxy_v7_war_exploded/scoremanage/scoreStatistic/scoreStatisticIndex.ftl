<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<!-- chartsScript -->
<script src="${request.contextPath}/static/js/chartsScript.js"></script>
<!-- md5 -->
<script src="${request.contextPath}/static/js/md5.js"></script>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active">
				<a role="tab" data-toggle="tab" href="#aaa" onclick="doChangeTab('1')">考试分析</a>
			</li>
			<li role="presentation">
				<a role="tab" data-toggle="tab" href="#bbb" onclick="doChangeTab('2')">日常分析</a>
			</li>
		</ul>
		<br>
		<div class="box-transparent">
			<div class="filter">
				<div class="filter-item block" id="examSelectDivId">
					<div class="row">
						<div class="col-xs-6">
							<label for="" class="filter-name">学年学期：</label>
							<div class="filter-content">
								<select class="form-control" id="acadyearSearch"  onChange="doChangeDate()">
									<#if (acadyearList?size>0)>
										<#list acadyearList as item>
											<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}</option>
										</#list>
									<#else>
										<option value="">暂无数据</option>
									</#if>
								</select>
								<select class="form-control" id="semesterSearch" onChange="doChangeDate()" >
									${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
								</select>
							</div>
						</div>
						<div class="col-xs-6">
							<label for="" class="filter-name">考试选择：</label>
							<div class="filter-content">
								<select <#if unitClass?default(-1) != 2>style="display:none"</#if> class="form-control" id="searchType" name="searchType" onChange="doChangeDate()">
									<option value="1">本单位设定的考试</option>
									<#if unitClass?default(-1) == 2>
										<option value="2">直属教育局设定的考试</option>
										<option value="3">参与的校校联考</option>
									</#if>
								</select>
								<select class="form-control" id="examIdSearch"  onChange="doChangeExam()">
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="filter-item block">
					<div class="row">
						<div class="col-xs-12">
							<label for="" class="filter-name">图表：</label>
							<div class="filter-content">
								<select class="form-control" id="documentLabel"  onChange="searchList()">
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="showDiv" style="display:none">
			<div class="box box-transparent listDiv">

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var unitClass='${unitClass?default(-1)?string}';
	<#--缓存-->
	var examinfoListMap={};
	var examinfoMap={};

	var charts73Map={};
	var chartsKSMap={};
	var chartsRCMap={};

	<#--子页面共享数据-->
	var oldUrl = '';
	var oldAcadyear = '';
	var oldSemester = '';
	var oldExamId = '';
	var oldExamName = '';
	var oldSearchType = '';
	var oldTabIndex = '1';

	<#list charts73Map?keys as key>
	charts73Map['${key}']=JSON.parse('${charts73Map[key]}');
	</#list>
	<#list chartsKSMap?keys as key>
	chartsKSMap['${key}']=JSON.parse('${chartsKSMap[key]}');
	</#list>
	<#list chartsRCMap?keys as key>
	chartsRCMap['${key}']=JSON.parse('${chartsRCMap[key]}');
	</#list>

	$(function(){
		doChangeDate();
	});

	function searchList(){
		var documentLabel = $("#documentLabel").val();
		if(oldTabIndex == '1'){
			var examId=$("#examIdSearch").val();
			var searchType=$("#searchType").val();
			$("#showDiv").hide();
			if(examId==""){
				$("#showDiv").hide();
				return;
			}
			var url =  '${request.contextPath}/scoremanage/scoreStatistic/list/page?examId='+examId+'&documentLabel='+documentLabel;
			oldUrl = url;
			oldExamId = examId;
			oldSearchType = searchType;
			oldExamName = $("#examIdSearch").find("option:selected").text();
			var acadyearSearch=$("#acadyearSearch").val();
			var semesterSearch=$("#semesterSearch").val();
			oldAcadyear=acadyearSearch;
			oldSemester=semesterSearch;
		}else{
			var url =  '${request.contextPath}/scoremanage/scoreStatistic/list/page?documentLabel='+documentLabel;
		}
		$(".listDiv").load(url);
	}

	function oldQuery(){
		$(".listDiv").load(oldUrl);
	}

	function doChangeTab(tabIndex){
		$("#documentLabel option").remove();
		$("#documentLabel").append('<option value="">暂无数据</option>');
		if(tabIndex == '1'){
			oldTabIndex = '1';
			$("#examSelectDivId").show();
			doChangeExam();
		}else if(tabIndex == '2'){
			oldTabIndex = '2';
			$("#examSelectDivId").hide();
			$("#documentLabel option").remove();
			for(var key in chartsRCMap){
				var jsonO = chartsRCMap[key];
				var htmlOption="<option ";
				htmlOption+=" value='"+jsonO.documentLabel+"'>"+jsonO.name;
				htmlOption+="</option>";
				$("#documentLabel").append(htmlOption);
			}
			searchList();
		}
	}

	function doChangeDate(){
		var acadyear = $("#acadyearSearch").val();
		var semester = $("#semesterSearch").val();
		var searchType = $("#searchType").val();
		$("#examIdSearch option").remove();
		if(examinfoListMap[acadyear+semester+searchType]){
			var jsonO = examinfoListMap[acadyear+semester+searchType];
			if(jsonO.length>0){
				$.each(jsonO,function(index){
					if(jsonO[index]){
						var htmlOption="<option ";
						htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
						htmlOption+="</option>";
						$("#examIdSearch").append(htmlOption);
					}
				});
			}else{
				$("#examIdSearch").append('<option value="">暂无数据</option>');
			}
			doChangeExam();
			return;
		}
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/scoremanage/examInfo/findList',
			data: {'searchAcadyear':acadyear,'searchSemester':semester,'searchType':searchType},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
				examinfoListMap[acadyear+semester+searchType]=jsonO;
				if(jsonO.length>0){
					if(searchType == "1"){
						var jsonOother = [];
						$.each(jsonO,function(index){
							if(unitClass == '2' && jsonO[index].examUeType==3){
								return true;
							}
							jsonOother[index]=jsonO[index];
							examinfoMap[jsonO[index].id]=jsonO[index];
							var htmlOption="<option ";
							htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
							$("#examIdSearch").append(htmlOption);
						});
						examinfoListMap[acadyear+semester+searchType]=jsonOother;
						if(jsonOother.length == 0){
							$("#examIdSearch").append('<option value="">暂无数据</option>');
						}
					}else{
						$.each(jsonO,function(index){
							examinfoMap[jsonO[index].id]=jsonO[index];
							var htmlOption="<option ";
							htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
							$("#examIdSearch").append(htmlOption);
						});
					}
				}else{
					$("#examIdSearch").append('<option value="">暂无数据</option>');
				}
				doChangeExam();
				layer.close(ii);
			}
		});

	}
	function doChangeExam(){
		var examId = $("#examIdSearch").val();
		if(examId==""){
			$("#showDiv").hide();
			$("#documentLabel option").remove();
			$("#documentLabel").append('<option value="">暂无数据</option>');
			return;
		}
		var examInfo = examinfoMap[examId];
		$("#documentLabel option").remove();
		if(examInfo.isgkExamType == '1'){
			for(var key in charts73Map){
				var jsonO = charts73Map[key];
				var htmlOption="<option ";
				htmlOption+=" value='"+jsonO.documentLabel+"'>"+jsonO.name;
				htmlOption+="</option>";
				$("#documentLabel").append(htmlOption);
			}
			for(var key in chartsKSMap){
				var jsonO = chartsKSMap[key];
				var htmlOption="<option ";
				htmlOption+=" value='"+jsonO.documentLabel+"'>"+jsonO.name;
				htmlOption+="</option>";
				$("#documentLabel").append(htmlOption);
			}
		}else{
			for(var key in chartsKSMap){
				var jsonO = chartsKSMap[key];
				var htmlOption="<option ";
				htmlOption+=" value='"+jsonO.documentLabel+"'>"+jsonO.name;
				htmlOption+="</option>";
				$("#documentLabel").append(htmlOption);
			}
		}
		searchList();
	}
</script>
