<form id="copyForm">
<input type="hidden" name="examId" id="examId" value="${examId!}"/>
<input type="hidden" name="type" id="type" value="${type!}"/>
<div class="layer-syncScore">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">学年：</span>
			<div class="filter-content">
				<select class="form-control" style="width:250px;" id="searchAcadyear" name="searchAcadyear" onChange="searchList()">
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
				<select class="form-control" style="width:250px;" id="searchSemester" name="searchSemester" onChange="searchList()">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select class="form-control" style="width:250px;" id="searchGradeCode" name="searchGradeCode" onChange="searchList()">
					<option value="">全部</option>
					<#if gradeList?exists && (gradeList?size>0)>
	                    <#list gradeList as item>
		                     <option value="${item.gradeCode!}">${item.gradeName!}</option>
	                    </#list>
                     </#if>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">类型：</span>
			<div class="filter-content">
				<select class="form-control" style="width:250px;" id="searchType" name="searchType" onChange="searchList()">
					<option value="1">本单位设定的考试</option>
                    <#if unitClass?default(-1) == 2>
                        <option value="2">直属教育局设定的考试</option>
                        <option value="3">参与的校校联考</option>
                    </#if>
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">考试：</span>
			<div class="filter-content">
				<select class="form-control" style="width:250px;" id="copyExamId" name="copyExamId">
				</select>
			</div>
		</div>
		
	</div>
</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="copySubjectClass-commit">确定</button>
	<button class="btn btn-grey" id="copySubjectClass-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
$(function(){
	searchList();
});

function searchList(){
	 var searchAcadyear = $('#searchAcadyear').val();
     var searchSemester = $('#searchSemester').val();
     var searchGradeCode = $('#searchGradeCode').val();
	 var searchType = $('#searchType').val();
	 var examId=$('#examId').val(); 
	 $("#copyExamId option").remove();
	$.ajax({
	    url:'${request.contextPath}/exammanage/examInfo/findExam',
	    data: {'searchAcadyear':searchAcadyear,'searchSemester':searchSemester,'searchGradeCode':searchGradeCode,'searchType':searchType},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	var i=0;
	    	if(jsonO.length==0){
	    		$("#copyExamId").append('<option value="">未找到</option>');
	    	}else{
	    		$.each(jsonO,function(index){
	    			if(jsonO[index].id!=examId){
	    				var htmlOption="<option ";
			    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
			    		htmlOption+="</option>";
			    		$("#copyExamId").append(htmlOption);
			    		i++;
	    			}
		    	});
		    	if(i==0){
		    		$("#copyExamId").append('<option value="">未找到</option>');
		    	}
	    	}
	    }
	});
}

// 取消按钮操作功能
$("#copySubjectClass-close").on("click", function(){
	doLayerOk("#copySubjectClass-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
 
var isSubmit=false;
$("#copySubjectClass-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var examId=$('#examId').val(); 

	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/exammanage/examInfo/saveCopySubjectClass',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				var url =  '${request.contextPath}/exammanage/subjectClassIndex/index/page?examId='+examId;
				$("#exammanageDiv").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#copySubjectClass-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#copyForm").ajaxSubmit(options);
 });
</script>

