<title>科目管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row col-xs-12">
	<!-- 条件筛选开始 -->
	<div class="filter ">
		<div class="filter-item">
			<label for="" class="filter-name">学生姓名：</label>
			<div class="filter-content">
			    <input nullable="false" type="text" id="stuName" class="form-control">
			</div>
		</div>	
		<div class="filter-item">
			<label for="" class="filter-name">性别：</label>
			<div class="filter-content">
				<select class="form-control" id="sex" name="sex">
					<#if sexList?exists && (sexList?size>0) >
						<#list sexList as item>
							<option value='${item.thisId!}'>${item.mcodeContent!}</option>
						</#list>
					<#else>
						<option value="">无</option>
					</#if>
				</select>
			</div>
		</div>
					
		<button class="btn btn-darkblue pull-left btn-seach" onclick="searchList()">查找</button>
</div><!-- 条件筛选结束 -->
<div class="row col-xs-12  listDiv">	
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var scripts = [null, 
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		"${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js",
		"${request.contextPath}/static/ace/assets/js/src/ace.scrolltop.js",
		null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		searchList();
	});
	function searchList(){
		var stuName=$("#stuName").val();
		
		var sex=$("#sex").val();
		var url='${request.contextPath}/basedata/student/cardList/page?stuName='+stuName+'&sex='+sex;
		$(".listDiv").load(encodeURI(url));
	}
</script>
