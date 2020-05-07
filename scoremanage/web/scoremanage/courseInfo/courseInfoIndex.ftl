<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/> 
<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>

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
									<select <#if unitClass?default(-1) != 2>style="display:none"</#if> class="form-control" id="searchType" name="searchType" onChange="doChangeDate()">
										<option value="1">本单位设定的考试</option>
										<#if unitClass?default(-1) == 2>
										<option value="2">直属教育局设定的考试</option>
										<option value="3">参与的校校联考</option>
										</#if>
									</select>
									<select style="width:280px;" class="form-control" id="examIdSearch"  onChange="doChangeExam()">
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
											<a href="javascript:" class="btn btn-blue showUseDivClass copyUse" style="display:none">复用以往的考试设置</a>
											<a href="javascript:" class="btn btn-blue showUseDivClass allCourseInfoOut" style="display:none">考试科目总览导出</a>
											<a href="javascript:" class="btn btn-blue showUseDivClass clearClass" style="display:none">清空选中考试【所有年级】设置</a>
											<a href="javascript:" class="btn btn-blue copyClassSetShow copyClassSet" style="display:none">复用以往的班级设置</a>
											<br><span class="showUseDivClass" style="display:none;color:red">操作选中考试下【所有年级】的考试科目，操作前请先保存未保存的数据！</span>
											<span style="color:red" class="copyClassSetShow" style="display:none">【复用以往的班级设置】操作会先清空选中考试【所有年级】下的班级设置，然后再复用数据！</span>
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
			<!-- 科目及分数设置开始 -->
			<div class="box box-transparent listDiv">

			</div><!-- 科目及分数设置结束 -->
			<div class="page-btns" id="savaBtenDiv">
				<a href="javascript:" class="btn btn-blue saveBtn" id="courseInfo-commit" onClick="saveInfo()">保存</a>
			</div>
		</div>
	</div>
</div>
<script>
	var ownUnitId='${unitId!}';	
	//本单位开设的考试
	var ownExamMap={};
	//考试对象 
	var examinfoListMap={};
	var isExportOld=false;
	$(function(){
		doChangeDate();
		//清空
		$(".clearClass").on("click",function(){
			var examId=$("#examIdSearch").val();
			if(examId==""){
				layer.tips('请先选择考试!', $("#examIdSearch"), {
					tipsMore: true,
					tips: 2
				});
				return;
			}
			showConfirmMsg('确认清空当前考试所有[年级]设置？','提示',function(){
				var ii = layer.load();
				$.ajax({
				    url:'${request.contextPath}/scoremanage/courseInfo/clearCurr',
				    data: {'examId':examId},  
				    type:'post',  
				    success:function(data) {
				    	var jsonO = JSON.parse(data);
				    	if(jsonO.success){
				    		layer.closeAll();
				    		oldQuery();
				 		}
				 		else{
							layerTipMsg(jsonO.success,"失败",jsonO.msg);
						}
						layer.close(ii);
				    }
				});
			});
		});
		//导出
		$(".allCourseInfoOut").on("click",function(){
			if(isExportOld){
				return;
			}
			isExportOld = true;
			var examId=$("#examIdSearch").val();
			if(examId==""){
				layer.tips('请先选择考试!', $("#examIdSearch"), {
					tipsMore: true,
					tips: 2
				});
				isSubmit = false;
				return;
			}
			var searchType=$("#searchType").val();
			var ii = layer.load();
			location.href = '${request.contextPath}/scoremanage/courseInfo/exportReport?examId='+examId+"&searchType="+searchType;
			layer.close(ii);
			isExportOld = false;
		});
		//复制以往考试设置
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
			var url = "${request.contextPath}/scoremanage/courseInfo/copyAdd/page?examId="+oldExamId;
			layerDivUrl(url);
		});
		//复用班级设置
		$(".copyClassSet").on("click", function(){
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
			var searchType = $("#searchType").val();
			var url = "${request.contextPath}/scoremanage/courseInfo/copyClassAdd/page?examId="+oldExamId+"&searchType="+searchType;
			layerDivUrl(url);
		});
	})	
	//年级切换
	$("#gradeCodeSearch").on("click",".label-select-item",function(){
		$("#gradeCodeSearch .active").removeClass("active");
		$(this).addClass("active");
		searchList();
	});
	//查询考试列表
	function doChangeDate(){
		var acadyear = $("#acadyearSearch").val();
		var semester = $("#semesterSearch").val();
		var searchType = $("#searchType").val();
		$("#examIdSearch option").remove();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/examInfo/findList',
		    data: {'searchAcadyear':acadyear,'searchSemester':semester,'searchType':searchType},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	examinfoListMap={};
		    	ownExamMap={};
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    			$("#examIdSearch").append(htmlOption);
		    			examinfoListMap[jsonO[index].id]=jsonO[index];
		    			if(jsonO[index].unitId==ownUnitId){
		    				ownExamMap[jsonO[index].id]=jsonO[index].id;
		    			}
		    			
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
			$(".showUseDivClass").hide();
			$(".copyClassSetShow").hide();
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
	//listDiv部分需要的参数
	var oldUrl = '';
	var oldAcadyear = '';
	var oldSemester = '';
	var oldGradeCode = '';
	var oldGradeName = '';
	var oldExamId = '';
	var oldExamName = '';
	var oldSearchType = '';
	//查询科目设置列表
	function searchList(){
		//考试是本单位设置的  操作按钮
		
		var examId=$("#examIdSearch").val();
		var gradeCode=$("#gradeCodeSearch .active").attr("data-value");
		var searchType=$("#searchType").val();
		
		if(examId==""){
			$("#showDiv").hide();
			$(".showUseDivClass").hide();
			$(".copyClassSetShow").hide();
			return;
		}
		
		if(searchType=='1'){
			$(".showUseDivClass").show();
			$(".copyClassSetShow").hide();
		}else{
			<#if unitClass?default(-1) == 2>
				if(ownExamMap[examId]){
					//学校创建的考试
					$(".showUseDivClass").show();
					$(".copyClassSetShow").hide();
				}else{
					$(".copyClassSetShow").show();
					$(".showUseDivClass").hide();
				}
			<#else>
				$(".showUseDivClass").hide();
			</#if>
		}
		if(!gradeCode || gradeCode==""){
			$("#showDiv").hide();
			$(".showUseDivClass").hide();
			return;
		}
		var url =  '${request.contextPath}/scoremanage/courseInfo/list/page?examId='+examId+'&gradeCode='+gradeCode;
		
		oldUrl = url;
		oldGradeCode = gradeCode;
		oldExamId = examId;
		oldSearchType = searchType;
		oldGradeName = $("#gradeCodeSearch .active").html();
		oldExamName = $("#examIdSearch").find("option:selected").text();
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		oldAcadyear=acadyearSearch;
		oldSemester=semesterSearch;
		$(".listDiv").load(url);
	}	
	function oldQuery(){
		if(oldExamId != ''){
			$(".listDiv").load(oldUrl);
		}
	}
</script>
