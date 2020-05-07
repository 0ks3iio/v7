<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/> 
<div class="box box-default">
	<div class="box-body">
		<div class="box-transparent">
			<div class="box-header" style="margin-bottom:10px;">
				<h4 class="box-title">通用信息设置</h4>
			</div>
			<div id="tongyongDiv">
				<div class="filter">
					<div class="filter-item block">
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
									<select class="form-control" id="examIdSearch"  onChange="doChangeExam()">
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="filter-item block">
						<label for="" class="filter-name">操作栏：</label>
						<div class="filter-content">
							<div class="row">
								<div class="col-xs-12">
									<div class="box-boder">
										<div class="multiselect " >
											<a href="javascript:" class="btn btn-blue copyUse">复用以往的考试设置</a>
											<span style="color:red">操作前请先保存未保存的数据！</span>
											<a href="javascript:" class="btn btn-blue clearClass">清空当前考试所有设置</a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="filter-item block">
						<label for="" class="filter-name">考试对象：</label>
						<div class="filter-content">
							<div class="row">
								<div class="col-xs-12">
									<div class="box-boder">
										<div class="multiselect" id="gradeCodeSearch">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="showDiv" style="display:none">
			<div class="listDiv">

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var unitClass='${unitClass?default(-1)?string}';

	var indexDiv = 0;
	var isSubmit = false;
	var examinfoListMap={};
	var examinfoMap={};
	$(function(){
		doChangeDate();
		$(".clearClass").on("click",function(){
			var examId=$("#examIdSearch").val();
			if(examId==""){
				layer.tips('请先选择考试!', $("#examIdSearch"), {
					tipsMore: true,
					tips: 2
				});
				return;
			}
			showConfirmMsg('确认清空当前考试所有设置？','提示',function(){
				var ii = layer.load();
				$.ajax({
				    url:'${request.contextPath}/scoremanage/borderline/clearCurr',
				    data: {'examId':examId},  
				    type:'post',  
				    success:function(data) {
				    	var jsonO = JSON.parse(data);
				    	if(jsonO.success){
				 			layer.closeAll();
				    		oldQuery();
				 		}
				 		else{
							layerTipMsg(data.success,"失败",data.msg);
						}
				    	layer.close(ii);
				    }
				});
			});
		});
		$("#gradeCodeSearch").on("click",".label-select-item",function(){
			$("#gradeCodeSearch .active").removeClass("active");
			$(this).addClass("active");
			searchList();
		});
	});
	var oldUrl = '';
	var oldAcadyear = '';
	var oldSemester = '';
	var oldExamId = '';
	var oldExamName = '';
	var oldGradeCode = '';
	function searchList(){
		var examId=$("#examIdSearch").val();
		var gradeCode=$("#gradeCodeSearch .active").attr("data-value");
		if(examId==""){
			$("#showDiv").hide();
			return;
		}
		if(gradeCode==""){
			$("#showDiv").hide();
			return;
		}
		var url =  '${request.contextPath}/scoremanage/borderline/list/page?examId='+examId+"&gradeCode="+gradeCode;
		oldUrl = url;
		oldExamId = examId;
		oldExamName = $("#examIdSearch").find("option:selected").text();
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		oldAcadyear=acadyearSearch;
		oldSemester=semesterSearch;
		oldGradeCode=gradeCode;
		$(".listDiv").load(url);
	}
	
	function oldQuery(){
		if(oldExamId != ''){
			$(".listDiv").load(oldUrl);
		}
	}
	
	function doChangeDate(isInit){
		var acadyear = $("#acadyearSearch").val();
		var semester = $("#semesterSearch").val();
		$("#examIdSearch option").remove();
		if(examinfoListMap[acadyear+semester]){
			var jsonO = examinfoListMap[acadyear+semester];
			if(jsonO.length>0){
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    		htmlOption+="</option>";
		    		$("#examIdSearch").append(htmlOption);
		    	});
	    	}else{
	    		$("#examIdSearch").append('<option value="">暂无数据</option>');
	    	}
	    	searchList();
	    	return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/examInfo/findList',
		    data: {'searchAcadyear':acadyear,'searchSemester':semester,'searchType':1},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	examinfoListMap[acadyear+semester]=jsonO;
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		examinfoMap[jsonO[index].id]=jsonO[index];
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    			$("#examIdSearch").append(htmlOption);
			    	});
			    }else{
		    		$("#examIdSearch").append('<option value="">暂无数据</option>');
		    	}
		    	doChangeExam();
		    	layer.close(ii);
		    }
		});
		
	}
	
	
	
	//查询考试列表
	function doChangeDate(){
		var acadyear = $("#acadyearSearch").val();
		var semester = $("#semesterSearch").val();
		var searchType = $("#searchType").val();
		$("#examIdSearch option").remove();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/examInfo/findList',
		    data: {'searchAcadyear':acadyear,'searchSemester':semester,'searchType':1},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	examinfoListMap={};
		    	ownExamMap={};
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		examinfoMap[jsonO[index].id]=jsonO[index];
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    			$("#examIdSearch").append(htmlOption);
		    			examinfoListMap[jsonO[index].id]=jsonO[index];
		    			
			    	});
			    }else{
		    		$("#examIdSearch").append('<option value="">暂无数据</option>');
		    	}
		    	doChangeExam();
		    }
		});
		
	}
	
	
	
		//切换考试 改变考试对象
	function doChangeExam(){
		var examIdSearch=$("#examIdSearch").val();
		$("#gradeCodeSearch span").remove();
		if(examIdSearch==""){
			$(".listDiv").html('');
			$("#showDiv").hide();
			return;
		}
		if(examinfoListMap[examIdSearch]){
			var item=examinfoListMap[examIdSearch];
			var rangeCodeName=item.rangeCodeName;
			if(rangeCodeName.length>0){
		    	$.each(rangeCodeName,function(index){
		    		if(index == 0){
			    		var htmlOption="<span class='label-select-item active' ";
					}else{
			    		var htmlOption="<span class='label-select-item' ";
					}
		    		htmlOption+=" data-value='"+rangeCodeName[index][0]+"'>"+rangeCodeName[index][1];
		    		htmlOption+="</span>";
		    		$("#gradeCodeSearch").append(htmlOption);
		    	});
		    	searchList();
		    	return;
			  }
		}
		
		$.ajax({
		    url:'${request.contextPath}/scoremanage/examInfo/findByExamId',
		    data: {'examId':examIdSearch},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO==null){
		    		layer.tips('未找到考试对象', $("#examIdSearch"), {
						tipsMore: true,
						tips: 2
					});
		    		return;
		    	}
		    	var rangeCodeName=jsonO.rangeCodeName;
		    	if(rangeCodeName.length>0){
		    		examinfoListMap[jsonO.id]=jsonO;
		    		$.each(rangeCodeName,function(index){
			    		if(index == 0){
				    		var htmlOption="<span class='label-select-item active' ";
						}else{
				    		var htmlOption="<span class='label-select-item' ";
						}
			    		htmlOption+=" data-value='"+rangeCodeName[index][0]+"'>"+rangeCodeName[index][1];
			    		htmlOption+="</span>";
			    		$("#gradeCodeSearch").append(htmlOption);
		    		});
		    	}else{
		    		layer.tips('未找到考试对象', $("#examIdSearch"), {
						tipsMore: true,
						tips: 2
					});
		    	}
		    	searchList();
		    }
		});
	}
	$(".copyUse").on("click", function(){
		options_default = {
			width:500,
			height:270
		};
		var examId=$("#examIdSearch").val();
		if(examId==""){
			layer.tips('请先选择考试!', $("#examIdSearch"), {
				tipsMore: true,
				tips: 2
			});
			return;
		}
		var url = "${request.contextPath}/scoremanage/borderline/copyAdd/page?examId="+oldExamId;
		layerDivUrl(url);
	});
</script>
