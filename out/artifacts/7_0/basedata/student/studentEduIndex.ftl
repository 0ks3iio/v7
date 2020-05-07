<title>科目管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/unit/page" name="单位管理" />
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/teacher/page" name="教师管理" />	
		</div>
	</div>
</div>
<div class="row col-xs-12">
	<!-- 条件筛选开始 -->
	<div class="filter ">
		<div class="filter-item">
			<label for="" class="filter-name">教育局：</label>
			<div class="filter-content">
				<select class="form-control" id="eduId" name="eduId" onChange="changeEduId();">
					<#if eduList?exists && (eduList?size>0) >
						<#list eduList as eduItem>
							<option value='${eduItem.id!}'>${eduItem.unitName!}</option>
						</#list>
					<#else>
						<option value="">---请选择---</option>
					</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<label for="" class="filter-name">学校：</label>
			<div class="filter-content">
				<select class="form-control" id="schoolId" name="schoolId" onChange="changeSchoolId();">
					<option value="">---所有直属学校---</option>
					<#if schoolList?exists && (schoolList?size>0)>
						<#list schoolList as schItem>
							<option value='${schItem.id!}'>${schItem.unitName!}</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>	
		<div class="filter-item">
			<label for="" class="filter-name">年级：</label>
			<div class="filter-content">
				<select class="form-control" id="gradeId" name="gradeId" onChange="changeGradeId();">
					<option value="">---请选择---</option>
				</select>
			</div>
		</div>	
		<div class="filter-item">
			<label for="" class="filter-name">班级：</label>
			<div class="filter-content">
				<select class="form-control" id="classId" name="classId" onChange="searchList();">
					<option value="">---请选择---</option>
				</select>
			</div>
		</div>		
	</div>
	<div class="filter ">	
		<div class="filter-item">
			<label for="" class="filter-name">学生姓名：</label>
			<div class="filter-content">
			    <input  type="text" id="stuName" name="stuName" class="form-control">
			</div>
		</div>	
		<button class="btn btn-darkblue  btn-seach">查找</button>
		<button class="btn btn-darkblue  btn-addStudent">新增</button>
	</div>
</div><!-- 条件筛选结束 -->
<div class="row col-xs-12  listDiv">	
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	var scripts = [null, 
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		"${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js",
		"${request.contextPath}/static/ace/components/chosen/chosen.jquery.min.js",
		"${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js",
		null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		// 新增操作
		$(".btn-addStudent").on("click", function(){
			var classId=$("#classId").val();
			if(classId==""){
				swal("请先选择一个班级!");
				return;
			}
			var url =  '${request.contextPath}/basedata/student/add/page?classId='+classId;
			indexDiv = layerDivUrl(url,{title:"学生信息"});
		});
		$(".filter .btn-seach").on("click",function(){
			searchList();
		});
		searchList();
		$('#eduId').chosen({
			width:'240px',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
		$('#schoolId').chosen({
			width:'250px',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
		
	});
	
	function changeEduId(){
		var eduId=$("#eduId").val();
		var schoolClass=$("#schoolId");
		$.ajax({
			url:"${request.contextPath}/basedata/student/schoolList",
			data:{eduId:eduId},
			dataType: "json",
			success: function(data){
				schoolClass.html("");
				schoolClass.chosen("destroy");
				if(data.length==0){
					schoolClass.append("<option value='' >---所有直属学校---</option>");
				}else{
					schoolClass.append("<option value='' >---所有直属学校---</option>");
					for(var i = 0; i < data.length; i ++){
						schoolClass.append("<option value='"+data[i].id+"' >"+data[i].unitName+"</option>");
					}
				}
				$(schoolClass).chosen({
					width:'240px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				changeSchoolId();
			}
		});
	}
	
	function changeSchoolId(){
		var schoolId=$("#schoolId").val();
		var gradeClass=$("#gradeId");
		if(schoolId==""){
			gradeClass.html("");
			gradeClass.append("<option value='' >--请选择--</option>");
			changeGradeId();
		}else{
			$.ajax({
			url:"${request.contextPath}/basedata/student/gradeList",
			data:{schoolId:schoolId},
			dataType: "json",
			success: function(data){
				gradeClass.html("");
				if(data.length==0){
					gradeClass.append("<option value='' >--请选择--</option>");
				}else{
					gradeClass.append("<option value='' >--请选择--</option>");
					for(var i = 0; i < data.length; i ++){
						gradeClass.append("<option value='"+data[i].id+"' >"+data[i].gradeName+"</option>");
					}
				}
				changeGradeId();
				}
			});
		}
	}
	function changeGradeId(){
		var gradeId=$("#gradeId").val();
		var classClass=$("#classId");
		if(gradeId==""){
			classClass.html("");
			classClass.append("<option value='' >--请选择--</option>");
			searchList();
		}else{
			$.ajax({
			url:"${request.contextPath}/basedata/student/classList",
			data:{gradeId:gradeId},
			dataType: "json",
			success: function(data){
				classClass.html("");
				if(data.length==0){
					classClass.append("<option value='' >--请选择--</option>");
				}else{
					classClass.append("<option value='' >--请选择--</option>");
					for(var i = 0; i < data.length; i ++){
						classClass.append("<option value='"+data[i].id+"' >"+data[i].classnamedynamic+"</option>");
					}
				}
				searchList();
				}
			});
		}
	}
	
	
	function searchList(){
		var eduId=$("#eduId").val();
		var schoolId=$("#schoolId").val();
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		var stuName=$("#stuName").val();
		var url="?unitId="+eduId+"&schoolId="+schoolId+"&gradeId="+gradeId+"&classId="+classId+"&stuName="+stuName;
		$(".listDiv").load(encodeURI("${request.contextPath}/basedata/student/stulist/page"+url));
	}
</script>
