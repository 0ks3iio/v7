<title>考号管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>
<#-- jqGrid报表引入文件 -->

<!-- ajax layout which only needs content area -->
<div class="row col-xs-12">
	<!-- 条件筛选开始 -->
	<div class="filter clearfix">
		<div class="filter-item">
			<label for="" class="filter-name">学年：</label>
			<div class="filter-content">
				<select class="form-control" id="acadyearSearch" onChange="changeExam()">
				<#if (acadyearList?size>0)>
					<#list acadyearList as item>
					<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}学年</option>
					</#list>
				</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<label for="" class="filter-name">学期：</label>
			<div class="filter-content">
				<select class="form-control" id="semesterSearch" onChange="changeExam()">
				 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
				</select>
			</div>
		</div>	
		<div class="filter-item">
			<label for="" class="filter-name">考试名称：</label>
			<div class="filter-content">
				<select class="form-control" id="examId" onChange="changeClassType()">
				<#if examList?exists && (examList?size>0)>
					<option value="">---请选择---</option>
					<#list examList as item>
						<option value="${item.id!}" <#if item.id==examId>selected="selected"</#if>>${item.examNameOther!}
						</option>
					</#list>
				<#else>
					<option value="">---请选择---</option>
				</#if>
				
				</select>
			</div>
		</div>	
		<button class="btn btn-darkblue js-copyTestNum" onclick="copyTestNum()">复制学号作为考号</button>
		<button class="btn btn-darkblue js-clearTestNum" onclick="clearTestNum()">一键清除考号</button>
		
	</div>
	<div class="filter clearfix">
		<div class="filter-item">
			<label for="" class="filter-name">班级类型：</label>
			<div class="filter-content">
				<select class="form-control" id="classType" onChange="changeClassType()">
					<option value="1" <#if classType?default('1')=='1'>selected="selected"</#if>>行政班</option>
					<option value="2" <#if classType?default('1')=='2'>selected="selected"</#if>>教学班</option>
				</select>
			</div>
		</div>	
		<div class="filter-item">
			<label for="" class="filter-name">班级：</label>
			<div class="filter-content">
				<select class="form-control" id="classIdSearch" onChange="search()">
				<#if (classMap?exists)>
					<option value="">---请选择---</option>
					<#list classMap?keys as key>
						<option value="${key!}" <#if key==classIdSearch>selected="selected"</#if>>${classMap[key]!}</option>
					</#list>
				<#else>
					<option value="">---请选择---</option>
				</#if>
				
				</select>
			</div>
		</div>		
		<button class="btn btn-darkblue pull-left btn-seach" onclick="search()">查找</button>
</div><!-- 条件筛选结束 -->
<div class="row  listDiv">	
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
	
		$('#classIdSearch').chosen({
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
		$('#examId').chosen({
			width:'100%',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
		search();
	});
	
	function clearTestNum(){
		var examId=$("#examId").val();
		if(examId == ""){
			return;
		}
		var examName=$("#examId option:selected").text();
		layer.confirm(
		"您即将清除“"+examName+"考试”中所有学生的考号。", 
		{ btn: ['确定','取消'] }, 
		function(){
 			//确定按钮方法				 
		 	$.ajax({
				url:'${request.contextPath}/scoremanage/examNum/clearTestNum',
				data:{unitId:'${unitId!}',examId:examId},
				success:function(data){
					layer.closeAll();
					var jsonO = JSON.parse(data);
				 		if(!jsonO.success){
				 			swal({title: "操作失败!",
			    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
									layer.closeAll();
									search();
			    			});
				 		}
				 		else{
				 			// 显示成功信息
				 			swal({title: "操作成功!",
			    				text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"}, function(){
									layer.closeAll();
									search();
			    			});			 			
		    			}
				}
			});			
		}, function(){
			//取消按钮方法
				layer.closeAll();			
		 });
	}
	function copyTestNum(){
		var examId=$("#examId").val();
		if(examId == ""){
			return;
		}
		var examName=$("#examId option:selected").text();
		layer.confirm(
		"您即将复制学生学号作为“"+examName+"考试”的考号，复制后，考号与学号相同。", 
		{ btn: ['确定','取消'] }, 
		function(){
 			//确定按钮方法				 
		 	$.ajax({
				url:'${request.contextPath}/scoremanage/examNum/copyTestNum',
				data:{unitId:'${unitId!}',examId:examId},
				success:function(data){
					layer.closeAll();
					var jsonO = JSON.parse(data);
				 		if(!jsonO.success){
				 			swal({title: "操作失败!",
			    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
									layer.closeAll();
									search();
			    			});
				 		}
				 		else{
				 			// 显示成功信息
				 			swal({title: "操作成功!",
			    				text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"}, function(){
									layer.closeAll();
									search();
			    			});			 			
		    			}
				}
			});			
		}, function(){
			//取消按钮方法
				layer.closeAll();			
		 });
	}
	
	function search(){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var classIdSearch=$("#classIdSearch").val();
		var c2='?examId='+examId+'&classType='+classType+'&classIdSearch='+classIdSearch;
		var url='${request.contextPath}/scoremanage/examNum/list/page'+c2;
		$(".listDiv").load(url);
	}
	
	function changeExam(){
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/examNum/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					examClass.append("<option value='' >--请选择--</option>");
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examNameOther+"</option>");
					}
				}
				$(examClass).chosen({
					width:'100%',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				changeClassType();
			}
		});
	}
	
	function changeClassType(){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var selClass=$("#classIdSearch");
		$.ajax({
			url:"${request.contextPath}/scoremanage/examNum/clsList",
			data:{examId:examId,classType:classType,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				selClass.html("");
				selClass.chosen("destroy");
				if(data==null){
					selClass.append("<option value='' >---请选择---</option>");
				}else{
					selClass.append("<option value='' >---请选择---</option>");
					for(key in data){
						selClass.append("<option value='"+key+"' >"+data[key]+"</option>");
					}
				}
				$(selClass).chosen({
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				search();
			}
		});
	}

</script>
