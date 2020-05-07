<form id="submitForm">
<div class="layer-syncScore">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">学年：</span>
			<div class="filter-content">
				<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showExamList()">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
	                    </#list>
                    <#else>
	                    <option value="">未设置</option>
                     </#if>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">学期：</span>
			<div class="filter-content">
				<select class="form-control" id="searchSemester" name="searchSemester" onChange="showExamList()">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">考试名称：</span>
			<div class="filter-content" id="searchExamIdDivId">
				<select name="searchExamId" id="searchExamId" onChange="showSubjectList()" class="form-control">
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">科目：</span>
			<div class="filter-content">
				<ul class="label-list clearfix" id="chosenSubjectIds">
				</ul>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveScore()">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	var examinfoListMap={};
	var examinfoSubjectListMap={};
	$(function(){
		showExamList();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	});
	function showExamList(){
		var acadyear = $("#searchAcadyear").val();
		var semester = $("#searchSemester").val();
		$("#searchExamId option").remove();
		if(examinfoListMap[acadyear+semester]){
			var jsonO = examinfoListMap[acadyear+semester];
			if(jsonO.length>0){
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    		htmlOption+="</option>";
		    		$("#searchExamId").append(htmlOption);
		    	});
	    	}else{
	    		$("#searchExamId").append('<option value="">暂无数据</option>');
	    	}
	    	showSubjectList();
	    	return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${arrangeId}/basisSet/score/findExamList',
		    data: {'acadyear':acadyear,'semester':semester},  
		    type:'post',
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	examinfoListMap[acadyear+semester]=jsonO;
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    			$("#searchExamId").append(htmlOption);
			    	});
			    }else{
		    		$("#searchExamId").append('<option value="">暂无数据</option>');
		    	}
		    	showSubjectList();
		    	layer.close(ii);
		    }
		});
	};
	function showSubjectList(){
		var examId = $("#searchExamId").val();
		$("#chosenSubjectIds").html("");
		if(examId==""){
			$("#chosenSubjectIds").html("暂无数据");
			$("#result-commit").addClass("disabled");
			return;
		}
		if(examinfoSubjectListMap[examId]){
			var jsonO = examinfoSubjectListMap[examId];
			if(jsonO.length>0){
				$.each(jsonO,function(index){
					var htmlOption="<li><label>";
	    			htmlOption+="<input type='checkbox' name='subjectIds' class='wp' value='"+jsonO[index].id+"'>";
	    			htmlOption+="<span class='lbl'> "+jsonO[index].subjectName+"</span>";
	    			htmlOption+="</label></li>";
	    			$("#chosenSubjectIds").append(htmlOption);
	    			$("#result-commit").removeClass("disabled");
		    	});
	    	}else{
	    		$("#chosenSubjectIds").html("暂无数据");
	    		$("#result-commit").addClass("disabled");
	    	}
	    	return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${arrangeId}/basisSet/score/findSubjectList',
		    data: {'examId':examId},
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	examinfoSubjectListMap[examId]=jsonO;
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<li><label>";
		    			htmlOption+="<input type='checkbox' name='subjectIds' class='wp' value='"+jsonO[index].id+"'>";
		    			htmlOption+="<span class='lbl'> "+jsonO[index].subjectName+"</span>";
		    			htmlOption+="</label></li>";
		    			$("#chosenSubjectIds").append(htmlOption);
		    			$("#result-commit").removeClass("disabled");
			    	});
			    }else{
			    	$("#chosenSubjectIds").html("暂无数据");
			    	$("#result-commit").addClass("disabled");
		    	}
		    	layer.close(ii);
		    }
		});
	}
	var isSubmit=false;
	function doSaveScore(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		if($("input[type='checkbox']:checked").length == 0){
			layerTipMsgWarn("提示","请选择科目");
			isSubmit = false;
			return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/gkelective/${arrangeId}/basisSet/score/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	findByCondition();
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
</script>
