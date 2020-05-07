<title>教学班管理</title>
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
			<@w.pageRef url="${request.contextPath}/basedata/class/page" name="班级管理" />
			<@w.arrowRight />
			<@w.pageRef url="${request.contextPath}/basedata/teachclass/page" name="教学班管理" />	
		</div>
	</div>
</div>
<div class="row col-xs-12">
	<!-- 条件筛选开始 -->
	<div class="filter ">
		<div class="filter-item">
			<label for="" class="filter-name">学年：</label>
			<div class="filter-content">
			    <select id="acadyear" nullable="false" data-placeholder="请选择" class="form-control" onchange="selchange()">
					<#if acadyearList?exists && (acadyearList?size>0)>
						<#list acadyearList as item>
							<option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
						</#list>
					<#else>
						<option value="">未设置</option>
					</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<label for="" class="filter-name">学期：</label>
			<div class="filter-content">
			    <select id="semester" nullable="false" data-placeholder="请选择" class="form-control" onchange="selchange()">
					${mcodeSetting.getMcodeSelect('DM-XQ',semester, "0")}
				</select>
			</div>
		</div>				
		<!--button class="btn btn-darkblue pull-left btn-seach">查找</button-->
		<div class="filter-item pull-right">
				<@w.btn btnId="btn-addTeachlass" btnValue="新增教学班" btnClass="fa-plus" />
		</div>
		<div class="filter-item pull-right">
			<div class="filter-content">
				<a href="javascript:" class="btn btn-sm btn-lightblue js-layer-01 copyTeachClass" >复制上学期教学班</a>
			</div>
		</div>
</div><!-- 条件筛选结束 -->
<div class="row listDiv">	
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	var scripts = [null, 
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		"${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js",
		"${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js",
		null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		// 新增操作
		$("#btn-addTeachlass").on("click", function(){
			options_default = {
				width:1000,
				height:300
			};
			acadyear=$("#acadyear").val();
			semester=$("#semester").val();
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teachclass/addoredit/0/page"+"?acadyear="+acadyear+"&semester="+semester,{height:370});
		});
		
		var url =  "${request.contextPath}/basedata/"+acadyear+"/"+semester+"/teachclass/page";
		$(".listDiv").load(url);
	});
	
	function selchange(){
		var acadyear=$("#acadyear").val();
		acadyear = acadyear==""?"1":acadyear;
		var semester=$("#semester").val();
		semester = semester==""?"99":semester;
		var url =  "${request.contextPath}/basedata/"+acadyear+"/"+semester+"/teachclass/page";
		$(".listDiv").load(url);
	}
	
	$(".copyTeachClass").on("click", function(){
		options_default = {
			width:500,
			height:300
		};
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var url = "${request.contextPath}/basedata/teachclass/copyAdd/page?acadyear="+acadyear+"&semester="+semester;
		layerDivUrl(url);
	});
</script>
