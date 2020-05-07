<div class="box box-default">
<div class="nav-tabs-wrap clearfix">
<div class="row">
	<div class="col-xs-12">
	   
	      <div class="box-body">
	         <div class="filter clearfix">
	        	<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="queryAcadyear" onChange="changeClsAndSearchList()" style="width:168px;">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="querySemester" onChange="searchList()" style="width:168px;">
						 ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
						</select>
					</div>
				</div>
				
				<div class="filter-item">
				   <label for="" class="filter-name">班级：</label>
				     <div class="filter-content">
					     <select vtype="selectOne" class="form-control" id="classIdSearch" onChange="searchStudent()" style="width:168px;">
						     <#if classList?exists && (classList?size>0)>
					              <#list classList as item>
						               <option value="${item.id!}" <#if '${item.id!}' == '${classId!}'>selected</#if>>${item.classNameDynamic!}</option>
					              </#list>
				             <#else>
					                   <option value="">---请选择---</option>
				             </#if>						           
					     </select>
				    </div>
			    </div>
				
				<div class="filter-item">
				        <label for="" class="filter-name">学生姓名：</label>
				             <div class="filter-content">
					            <select vtype="selectOne" class="form-control" id="studentId" onChange="searchList();" style="width:168px;">
						           <#if studentList?exists && (studentList?size>0)>
					                    <#list studentList as item>
						                     <option value="${item.id!}" <#if '${item.id!}' == '${studentId!}'>selected</#if>>${item.studentName!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">---请选择---</option>
				                     </#if>						           
					            </select>
				             </div>
			        </div>													
			</div>
			<div class="table-wrapper" id="showList">
			</div>
	    </div>
	</div>
</div>
</div>
</div>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script>
$(function(){
	//初始化单选控件
	var classIdSearch = $('#classIdSearch');
	$(classIdSearch).chosen({
		width:'130px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:false, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
	var studentSearch = $('#studentId');
	$(studentSearch).chosen({
		width:'130px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:false, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
	searchList();
});

function　searchStudent(){
   var classId = $('#classIdSearch').val();
   var studentIdSel=$("#studentId");
   $.ajax({
			url:"${request.contextPath}/studevelop/rewardsRecord/studentList",
			data:{classId:classId},
			dataType: "json",
			success: function(data){
			studentIdSel.html("");
			studentIdSel.chosen("destroy");
			searchList();
			if(data==null){
				studentIdSel.append("<option value='' >---请选择---</option>");
			}else{
			    studentIdSel.append("<option value='' >---请选择---</option>");
				for(var m=0;m<data.length;m++){
				studentIdSel.append("<option value='"+data[m].id+"' >"+data[m].studentName+"</option>");
				}
			}
			if(m==0){
				//studentIdSel.append("<option value='' >---请选择---</option>");
			}
			$(studentIdSel).chosen({
		        width:'130px',
		        no_results_text:"未找到",//无搜索结果时显示的文本
		        allow_single_deselect:true,//是否允许取消选择
		        disable_search:false, //是否有搜索框出现
		        search_contains:true,//模糊匹配，false是默认从第一个匹配
	        });
			}
		});
}

function changeClsAndSearchList(){
      var acayear = $('#queryAcadyear').val();
      var clsIdSel=$("#classIdSearch");
      $.ajax({
			url:"${request.contextPath}/studevelop/abilityQuery/clsList",
			data:{acayear:acayear},
			dataType: "json",
			success: function(data){
			clsIdSel.html("");
			clsIdSel.chosen("destroy");
			if(data==null){
				clsIdSel.append("<option value='' >---请选择---</option>");
			}else{
			    clsIdSel.append("<option value='' >---请选择---</option>");
				for(var m=0;m<data.length;m++){
				clsIdSel.append("<option value='"+data[m].id+"' >"+data[m].classNameDynamic+"</option>");
				}
			}
			if(m==0){
				//subjectIdSel.append("<option value='' >---请选择---</option>");
			}
			}
		});		
		var studentIdSel=$("#studentId");
		studentIdSel.html("");
		studentIdSel.chosen("destroy");
		studentIdSel.append("<option value='' >---请选择---</option>");
		searchList();
}

function searchList(){
    var queryAcadyear = $('#queryAcadyear').val();
    var querySemester = $('#querySemester').val();
	var studentId = $('#studentId').val();
	//if(studentId == ''){
	    //layerTipMsg(false,"提示!","请选择一个学生!");
		//return;
	//}
	var str = "?acadyear="+queryAcadyear+"&semester="+querySemester+"&studentId="+studentId;
	var url = "${request.contextPath}/studevelop/abilityQuery/showAblity"+str;
	$("#showList").load(url);
}

</script>
       