<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>社团活动登记</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row" id="importDiv">
	<div class="col-xs-12">
	   <div class="box box-default">
	      <#--<div class="box-body">-->
		  <#--<!-- PAGE CONTENT BEGINS &ndash;&gt;-->
			<#---->
           <#--</div>-->

           <div class="row">
               <div class="col-sm-2">
                   <div class="box box-default" id="id1">
                       <div class="box-header">
                           <h3 class="box-title">班级菜单</h3>
                       </div>
				   <@studevelopTreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
                   </div>
               </div>
	<div class="col-sm-10" >
	      <div class="box-body">
				<div class="filter clearfix">
					<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
							<div class="filter-content">
								<select class="form-control" id="queryAcadyear" name="queryAcadyear" onChange="searchList4()">
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
						<label for="" class="filter-name">学期：</label>
							<div class="filter-content">
								<select class="form-control" id="querySemester" name="querySemester" onChange="searchList4()">
									${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
								</select>
							</div>
					</div>
					
					<#--div class="filter-item">
				        <label for="" class="filter-name">班级：</label>
				             <div class="filter-content">
					            <select vtype="selectOne" class="form-control" id="classIdSearch" onChange="searchList2()">
						           <#if classList?exists && (classList?size>0)>
						                <option value="">---请选择---</option>
					                    <#list classList as item>					                     
						                     <option value="${item.id!}">${item.classNameDynamic!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">---请选择---</option>
				                     </#if>						           
					            </select>
				             </div>
			        </div-->
					
					<div class="filter-item">
	                    <span class="filter-name">科目：</span>
	                    <div class="filter-content">
	                        <select name="subjectId" id="subjectId" class="form-control" onchange="searchList1()">   
	                            
	                            <#if classTeachingList?exists && (classTeachingList?size>0)>
					                <option value="">---请选择---</option>
					                <#list classTeachingList as item>					              
						                <option value="${item.subjectId!}">${item.subjectName!}</option>
					                </#list>
				                <#else>
					                    <option value="">---请选择---</option>
				                </#if>		              
	                        </select>
	                    </div>
	                </div>
					
				
				<#--div class="filter clearfix">
					<div class="filter-item">
				        <label for="" class="filter-name">学生姓名：</label>
				             <div class="filter-content">
					            <select vtype="selectOne" class="form-control" id="studentId" onChange="searchList3()">
						           <#if studentList?exists && (studentList?size>0)>
						                <option value="">---请选择---</option>
					                    <#list studentList as item>
						                     <option value="${item.id!}">${item.studentName!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">---请选择---</option>
				                     </#if>						           
					            </select>
				             </div>
			        </div>
			    </div-->    
				<input type="hidden" id="studentId">
                <input type="hidden" id="classId">
				<div class="table-wrapper" id="showList">
				</div>
		</div>
	</div>
</div>
</div>


<!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
	//初始化单选控件
	<#--var classIdSearch = $('#classIdSearch');
	$(classIdSearch).chosen({
	width:'130px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});-->
	var subjectSearch = $('#subjectId');
	$(subjectSearch).chosen({
	width:'130px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
	
	<#--var studentId = $('#studentId');
	$(studentId).chosen({
	width:'130px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	}); -->
	searchList();
});

function onTreeClick(event, treeId, treeNode, clickFlag){
    if(treeNode.type == "student"){
        var id = treeNode.id;
        $("#studentId").val(id);
        searchList3();
    }else if(treeNode.type == "class"){
        var id = treeNode.id;
        $("#classId").val(id);
        $("#studentId").val("");
        searchList2();
    }
}

function　searchList(){
   var queryAcadyear = $('#queryAcadyear').val();
   var querySemester = $('#querySemester').val();
   var classId = $('#classId').val();
   var subjectId = $('#subjectId').val();
   var str = "?queryAcadyear="+queryAcadyear+"&querySemester="+querySemester+"&classId="+classId+"&subjectId="+subjectId;
   var url = "${request.contextPath}/studevelop/mannerRecord/tablist"+str;
   $("#showList").load(url);
}

<#--function searchAndChangeAca(){
      var acadyear = $('#queryAcadyear').val();
      var clsIdSel=$("#classIdSearch");
      $.ajax({
			url:"${request.contextPath}/studevelop/mannerRecord/clsList",
			data:{acadyear:acadyear},
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
		
		var subjectIdSel=$("#subjectId");
		var studentIdSel=$("#studentId");
		subjectIdSel.html("");
		subjectIdSel.chosen("destroy");
		subjectIdSel.append("<option value='' >---请选择---</option>");
		studentIdSel.html("");
		studentIdSel.chosen("destroy");
		studentIdSel.append("<option value='' >---请选择---</option>");
		searchList();
}-->

function searchList1(){
   var studentId = $('#studentId').val();
   if(studentId == ''){
      searchList();
   }else{
      searchList3();
   }
}

function searchList2(){
        var queryAcadyear = $('#queryAcadyear').val();
        var querySemester = $('#querySemester').val();
        var classId = $('#classId').val();
		var subjectIdSel=$("#subjectId");
		var studentIdSel=$("#studentId");
		$.ajax({
			url:"${request.contextPath}/studevelop/mannerRecord/subjectList",
			data:{queryAcadyear:queryAcadyear,querySemester:querySemester,classId:classId},
			dataType: "json",
			success: function(data){
			subjectIdSel.html("");
			subjectIdSel.chosen("destroy");
			if(data==null){
				subjectIdSel.append("<option value='' >---请选择---</option>");
			}else{
			    subjectIdSel.append("<option value='' >---请选择---</option>");
				for(var m=0;m<data.length;m++){
				subjectIdSel.append("<option value='"+data[m].subjectId+"' >"+data[m].subjectName+"</option>");
				}
			}
			if(m==0){
				//subjectIdSel.append("<option value='' >---请选择---</option>");
			}
			}
		});
		
		<#--$.ajax({
			url:"${request.contextPath}/studevelop/mannerRecord/studentList",
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
		});-->
	   searchList();
}

function　searchList3(){
   var subjectId=$("#subjectId").val();
   var studentId = $('#studentId').val();
   var queryAcadyear = $('#queryAcadyear').val();
   var querySemester = $('#querySemester').val();
   var classId = $('#classId').val();
   if(studentId == ''){
      searchList();
   }else{
      var str = "?queryAcadyear="+queryAcadyear+"&querySemester="+querySemester+"&classId="+classId+"&studentId="+studentId+"&subjectId="+subjectId;
      var url = "${request.contextPath}/studevelop/mannerRecord/stuRecblist"+str;
      $("#showList").load(url);
   }
}


function searchList4(){
        var queryAcadyear = $('#queryAcadyear').val();
        var querySemester = $('#querySemester').val();
        var classId = $('#classId').val();
		var subjectIdSel=$("#subjectId");
		$.ajax({
			url:"${request.contextPath}/studevelop/mannerRecord/subjectList",
			data:{queryAcadyear:queryAcadyear,querySemester:querySemester,classId:classId},
			dataType: "json",
			success: function(data){
			subjectIdSel.html("");
			subjectIdSel.chosen("destroy");
			if(data==null){
				subjectIdSel.append("<option value='' >---请选择---</option>");
			}else{
			    subjectIdSel.append("<option value='' >---请选择---</option>");
				for(var m=0;m<data.length;m++){
				subjectIdSel.append("<option value='"+data[m].subjectId+"' >"+data[m].subjectName+"</option>");
				}
			}
			if(m==0){
				//subjectIdSel.append("<option value='' >---请选择---</option>");
			}
			}
		});
	    searchList();
}
</script>






