<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-header header_filter">
        <div class="filter filter-f16">
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
					<select class="form-control" style="width:120px;" id="semesterSearch" onChange="changeExam()">
					 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="examId" onChange="changeGradeCode()">
						<option value='' >---请选择---</option>
					</select>
				</div>
			</div>	
			<a href="javascript:" class="btn btn-blue js-copyTestNum" onclick="copyTestNum()">复制学号作为考号</a>
			<a href="javascript:" class="btn btn-blue js-clearTestNum" onclick="clearTestNum()">一键清除考号</a>
			<!--<a href="javascript:" class="btn btn-blue js-exportTestNum" >导入</a>-->
        </div>
        <div class="filter filter-f16">
        	<div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" onChange="changeClassType()">
						<option value='' >---请选择---</option>
					</select>
				</div>
			</div>
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
					<select vtype="selectOne" class="form-control" id="classIdSearch" onChange="searchList('')">
					</select>
				</div>
			</div>		
			<a href="javascript:" class="btn btn-blue pull-left btn-seach" onclick="searchList('')">查找</a>
     	</div>
    </div>
    <div class="box-body listDiv">
    
    </div>
</div>
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value='' >-----请选择-----</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examNameOther+"</option>");
					}
				}
				$(examClass).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				changeGradeCode();
			}
		});
	}
	
	function changeGradeCode(){
		var examId=$("#examId").val();
		var gradeCodeClass=$("#gradeCode");
		var i=0;
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/gradeCodeList",
			data:{examId:examId,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				gradeCodeClass.html("");
				if(data==null){
					gradeCodeClass.append("<option value='' >---请选择---</option>");
				}else{
					for(key in data){
						i++;
						gradeCodeClass.append("<option value='"+key+"' >"+data[key]+"</option>");
					}
					if(i==0){
						gradeCodeClass.append("<option value='' >---请选择---</option>");
					}
				}
				changeClassType();
			}
		});
	}
	
	function changeClassType(){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var gradeCode=$("#gradeCode").val();
		var selClass=$("#classIdSearch");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/classList",
			data:{examId:examId,classType:classType,gradeCode:gradeCode,unitId:'${unitId!}'},
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
				searchList("");
			}
		});
	}
	
	function searchList(content){
		var examId=$("#examId").val();
		var gradeCode=$("#gradeCode").val();
		var classType=$("#classType").val();
		var classIdSearch=$("#classIdSearch").val();
		var c2='?examId='+examId+'&gradeCode='+gradeCode+'&classType='+classType+'&classIdSearch='+classIdSearch;
		var url='${request.contextPath}/scoremanage/testNumber/list/page'+c2+content;
		$(".listDiv").load(url);
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
				url:'${request.contextPath}/scoremanage/testNumber/copyTestNum',
				data:{unitId:'${unitId!}',examId:examId},
				success:function(data){
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
		    			layerTipMsg(data.success,"失败",jsonO.msg);
		    			searchList("");
			 		}
			 		else{
			 			// 显示成功信息
		    			layerTipMsg(jsonO.success,"成功",jsonO.msg);	 
		    			searchList("");			
	    			}
				}
			});			
		}, function(){
			//取消按钮方法
				layer.closeAll();			
		 });
	}
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
				url:'${request.contextPath}/scoremanage/testNumber/clearTestNum',
				data:{unitId:'${unitId!}',examId:examId},
				success:function(data){
					layer.closeAll();
					var jsonO = JSON.parse(data);
				 		if(!jsonO.success){
			    			layerTipMsg(data.success,"失败",jsonO.msg);
			    			searchList("");
				 		}
				 		else{
				 			// 显示成功信息
			    			layerTipMsg(jsonO.success,"成功",jsonO.msg);	 
			    			searchList("");			
		    			}
				}
			});			
		}, function(){
			//取消按钮方法
				layer.closeAll();			
		 });
	}
	
</script>