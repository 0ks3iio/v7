<title>年级班级设置</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="" name="年级管理" />
		</div>
	</div>
</div>

<div class="filter" id="searchDiv">
	<div class="filter-item">
		<#if xdMap?? && (xdMap?size>0)>
		<label for="" class="filter-name">学段：</label>
		<div class="filter-content">
			<select class="form-control" id="searchSection" name="searchSection" onChange="doQuery()">
				<option value="">所有</option>
				<#list xdMap?keys as key>
					<option value="${key}" <#if searchSection?default('')==key>selected</#if>>${xdMap[key]}</option>
				</#list>
			</select>
		</div>
		<#else>
		<label for="" class="filter-name">未设置学段，无法生成年级</label>
		</#if>
	</div>
	<div class="filter-item pull-right">
		<#if isEdit!>
			<@w.btn btnId="btn-init" btnValue="初始化年级" btnClass="fa-undo" title=""/>
		</#if>
		<@w.btn btnId="btn-syn" btnValue="同步年级名称" btnClass="fa-undo" title="用于名称升级"/>
		<@w.btn btnId="btn-addGrade" btnValue="新增年级" btnClass="fa-plus" />
		<@w.btn btnId="btn-addBatchClass" btnValue="批量新增班级" btnClass="fa-plus" />
	</div>
</div>

<div class="row listDiv" id="graDiv">	
</div>
<br>
<input type="hidden" id="selectGradeId" value="">
<input type="hidden" id="selectSection" value="">
<div class="filter" id="searchClassDiv" style="display:none">
	<div class="filter-item pull-right">
		<@w.btn btnId="btn-addClass" btnValue="新增班级" btnClass="fa-plus" />
	</div>
</div>
<div class="row listDiv" id="claDiv">	
</div>
<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
	<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	var scripts = [null, 
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		"${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js",
		"${request.contextPath}/static/ace/assets/js/src/ace.scrolltop.js",
		null];
	options_default = {
		width:1000
	};
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		$("#btn-init").on("click", function(){
			$.ajax({
	    		url:'${request.contextPath}/basedata/grade/initGrade',
	    		success:function(data) {
	    			var jsonO = JSON.parse(data);
			 		if(jsonO.success){
			 			swal({title: "操作成功!",
	    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:3000},
	    					function(){
	    						$("#btn-init").hide();
	    						$("#gradeList").trigger("reloadGrid");
	    						sweetAlert.close();
	    					}
	    				);
			 		}
			 		else{
	    				swal({title: "操作失败!",
	    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
	    				);
	    			}
	    		}
			});
		});
		$("#btn-addGrade").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/grade/edit/page");
		});
		$("#btn-addClass").on("click", function(){
			var selectGradeId=$("#selectGradeId").val();
			var selectSection=$("#selectSection").val();
			indexDiv = layerDivUrl("${request.contextPath}/basedata/class/edit/page?gradeId="+selectGradeId+"&section="+selectSection);
		});
		$("#btn-syn").on("click", function(){
			swal(
			{
				title: "同步年级名称", 
				html: true, 
				text: "确认同步(若名称正常则不需要同步)？",   
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/grade/synNmae',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:3000},
		    					function(){
		    						$("#gradeList").trigger("reloadGrid");
		    						$("#classList").trigger("reloadGrid");
		    						sweetAlert.close();
		    					}
		    				);
				 		}
				 		else{
		    				swal({title: "操作失败!",
		    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
		    				);
		    			}
		    		}
				});
			});
		});
		$("#btn-addBatchClass").on("click", function(){
			var selectGradeId=$("#selectGradeId").val();
			var selectSection=$("#selectSection").val();
			indexDiv = layerDivUrl("${request.contextPath}/basedata/class/batchAdd/page?gradeId="+selectGradeId+"&section="+selectSection);
		});
		
		doQuery();
	});
	function doQuery(){
		var url =  '${request.contextPath}/basedata/grade/unit/${unitId!}/list/page';
		$("#graDiv").load(url);
	}
</script>
