<#import "/fw/macro/webmacro.ftl" as w>
<#import "/basedata/class/detailmacro.ftl" as d>

<#if errorMsg?default('')!=''>
	<div class="widget-box" style="margin-top:80px;margin-bottom:80px;">
		<div class="widget-body">
			<div class="widget-main">
				<p class="alert alert-warning center">${errorMsg!}</p>
			</div>
		</div>
	</div>
	<div class="clearfix form-actions center">
		<@w.btn btnId="studentFlowIn-errorYes" btnClass="fa-check" btnValue="确定" />
	</div>
	<script>
		$('.page-content-area').ace_ajax('loadScripts', [], function() {
			$("#studentFlowIn-close").on("click",function(){
				layer.closeAll();
			});
			$("#studentFlowIn-errorYes").on("click",function(){
				$(".js-inSchool").unbind().addClass("disabled");
				layer.closeAll();
			});
		});
	</script>
<#else>
<div class="row studentDetail" style="width:400px;">
	<div class="clearfix">
		<div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">
		
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10" id="gradeDiv">
				<label for="" class="filter-name">转入年级：</label>
				<div class="filter-content">
					<select class="form-control clazz" id="grade" onChange="gradeChange();">
						<option value="">请选择年级</option>
						<#if grades?exists && grades?size gt 0>
						<#list grades as grade>
							<option value="${grade.id!}">${grade.gradeName!}</option>
						</#list>
						</#if>
					</select>
				</div>
			</div>
			
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name">转入班级：</label>
				<div class="filter-content">
					<select class="form-control clazz" id="cls" onChange="">
						<option value="">请选择班级</option>
						<#if classList?exists && classList?size gt 0>
							<#list classList as clazz>
							<option value="${clazz.id!}">${clazz.className!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
		</div>
	</div>
	
	<div class="clearfix form-actions center">
		<@w.btn btnId="btn-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="btn-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>
<script>
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		var commit =false;
		$("#btn-commit").unbind("click").bind("click",function(){
			$("#btn-commit").addClass("disabled");
			
			if(!checkVal()){
			 	$("#btn-commit").removeClass("disabled");
			 	return;
			}
		
			var studentId = "";
			var newClassId = $("#cls").val();
			
			commit = true;
			$.ajax({
				url:"${request.contextPath}/basedata/classflow/student/flow?studentId=${studentId!}&newClassId="+newClassId,
				type:'post', 
				cache:false,  
				contentType: "application/json",
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
		    			showMsgError(jsonO.msg,"操作失败!",function(index){
				  			layer.close(index);
			    			$("#btn-commit").removeClass("disabled");
			    			commit = false;
						});
			 		}
			 		else{
		    			showMsgSuccess(jsonO.msg,"操作成功!",function(index){
				  			layer.close(index);
				  			$("#btn-close").click();
			    			$(".btn-classflow").each(function(){
			    				if($(this).attr("studentId")=="${studentId!}"){
			    					$(this).unbind().attr("disabled","disabled").addClass("disabled").text("转入成功");
			    				}
			    			});
			    			$("#btn-commit").unbind();
						});
	    			}	
				}
			});
		});
		$("#btn-close").unbind("click").bind("click",function(){
			layer.closeAll();
		});
	});
	
	function gradeChange(){
		var gradeId = $("#grade").val();
		if(gradeId==''){
			$("#cls").html("<option value=\"\">请选择班级</option>");
			return ;
		}
		$.ajax({
			url:"${request.contextPath}/basedata/classflow/"+gradeId+"/clazz/optionhtml?unitId=${unitId!}&gradeId="+gradeId,
			type:'post', 
			cache:false,  
			contentType: "application/json",
			success:function(data) {
				$("#cls").html(data);
			 }
		});	
	}
	
	function checkVal(){
		var newClassId = $("#cls").val();
		var newGradeId = $("#grade").val();
		var isOk = true;
		if(newClassId == ''){
			layer.tips('班级不能为空', '#cls',{tipsMore: true});
			isOk = false;
		}
		if(newGradeId == ''){
			layer.tips('年级不能为空', '#grade',{tipsMore: true});
			isOk = false;
		}
		return isOk;
	}
</script>
</#if>