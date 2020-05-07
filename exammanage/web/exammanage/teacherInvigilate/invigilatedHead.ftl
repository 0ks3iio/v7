<div class="box box-default">
	<div class="box-body">
	     <div class="filter">
            <div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onChange="changAcadyear()">
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
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" style="width:300px;" onChange="changAcadyear()">
					     ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">考试：</span>
				<div class="filter-content">
					<select name="examId" id="examId" class="form-control" style="width:300px;" onChange="showList()">
					     <option value="">--未找到--</option>
					</select>
				</div>
			</div>
		</div>
		<div id="showList">
		</div>

	</div>
</div>
<script>
	var type="";
	$(function(){
		changAcadyear();
	});
	function changAcadyear(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/exammanage/teacherInvigilated/findExamList",
			data:{'acadyear':acadyear,'semester':semester},
			dataType: "json",
			success: function(data){
				examClass.html("");
				if(data.length==0){
					examClass.append("<option value='' >--未找到--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						if(i==0){
							examClass.append("<option value='"+data[i].id+"' selected>"+data[i].examName+"</option>");
						}else{
							examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
						}
						
					}
				}
				showList();
			}
		});
		
	}
	
	function showList(){
		var examId=$("#examId").val();
		if(examId==""){
			$("#showList").html(tomakeMess('请先选择考试'));
			return;
		}
		var url =  '${request.contextPath}/exammanage/teacherInvigilated/list/page?examId='+examId;
		$("#showList").load(url);
		
	}
	
	function tomakeMess(mess){
		var htmlText='<table class="table table-bordered "><tr>'
		+'<td colspan="4">'
		+'<p class="alert alert-info center" style="padding:10px;margin:0;">'+mess+'</p>'
    	+'</td><tr></table>'
    	return htmlText;
	}
</script>