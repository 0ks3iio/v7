<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>身心健康登记</title>
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
                   <div class="box-body" id="id2" >
                       <div class="filter clearfix" style="padding-left: 20px;">
                           <div class="filter-item">
                               <label for="" class="filter-name">学年：</label>
                               <div class="filter-content">
                                   <select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="changeStuId()">
								   <#if acadyearList?? && (acadyearList?size>0)>
									   <#list acadyearList as item>
                                           <option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}</option>
									   </#list>
								   <#else>
                                       <option value="">暂无数据</option>
								   </#if>
                                   </select>
                               </div>
                           </div>
                           <div class="filter-item">
                               <label for="" class="filter-name">学期：</label>
                               <div class="filter-content">
                                   <select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="changeStuId()">
								   ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                   </select>
                               </div>
                           </div>
                           <input type="hidden" id="studentId">
                           <input type="hidden" id="classId">
					   <#--<div class="filter-item">-->
					   <#--<label for="" class="filter-name">班级：</label>-->
					   <#--<div class="filter-content">-->
					   <#--<select vtype="selectOne" class="form-control" id="classIdSearch" onChange="changeClass()">-->
					   <#--</select>-->
					   <#--</div>-->
					   <#--</div>		-->
					   <#--<div class="filter-item">-->
					   <#--<label for="" class="filter-name">姓名：</label>-->
					   <#--<div class="filter-content">-->
					   <#--<select class="form-control" id="stuId" onChange="changeStuId()">-->
					   <#--</select>-->
					   <#--</div>-->
					   <#--</div>-->
                       </div>
                       <div class="box-body showList">

                       </div>
				   </div>
</div>
</div>
</div>
<script>
	<#-- $(function(){
		changeExam("false");
	});
	
	function changeExam(canShow){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classIdSearch");
		var stuId=$("#stuId");
		var stuVal = $("#stuId").val();
		$.ajax({
			url:"${request.contextPath}/studevelop/healthRecord/classIds",
			data:{acadyear:acadyear,semester:semester},
			dataType: "json",
			success: function(data){
				classId.html("");
				classId.chosen("destroy");
				if(data.length==0){
					classId.append("<option value='' >-----请选择-----</option>");
					stuId.html("");
					stuId.chosen("destroy");
					stuId.append("<option value='' >-----请选择-----</option>");
					stuId.chosen({
						width:'145px',
						no_results_text:"未找到",//无搜索结果时显示的文本
						allow_single_deselect:true,//是否允许取消选择
						disable_search:false, //是否有搜索框出现
						search_contains:true,//模糊匹配，false是默认从第一个匹配
						//max_selected_options:1 //当select为多选时，最多选择个数
					}); 
				}else{
					for(var i = 0; i < data.length; i ++){
						classId.append("<option value='"+data[i].id+"' >"+data[i].classNameDynamic+"</option>");
					}
				}
				classId.chosen({
					width:'145px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				});
				if(stuVal == ""){
					canShow = "false";
				}
				changeClass(canShow);
			}
		});
	}
	
	function changeClass(canShow){
		var classId=$("#classIdSearch").val();
		var stuId=$("#stuId");
		var stuVal = $("#stuId").val();
		$.ajax({
			url:"${request.contextPath}/studevelop/healthRecord/stuIds",
			data:{classId:classId},
			dataType: "json",
			success: function(data){
				stuId.html("");
				stuId.chosen("destroy");
				if(data.length==0){
					stuId.append("<option value='' >---请选择---</option>");
				}else{
					stuId.append("<option value='' >-----请选择-----</option>");
					for(var i = 0; i < data.length; i ++){
						stuId.append("<option value='"+data[i].id+"' >"+data[i].studentName+"</option>");
					}
				}
				stuId.chosen({
					width:'145px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				});
				if(stuVal == ""){
					canShow = "false";
				}
				if(canShow == "true"){
					changeStuId();
				}
			}
		});
	}-->
	
	function changeStuId(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var stuId=$("#studentId").val();
		if(stuId == '' || stuId == undefined){
		     return;
		}
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId;
		var url='${request.contextPath}/studevelop/healthRecord/Edit'+ass;
		$(".showList").load(url);
	}
	function onTreeClick(event, treeId, treeNode, clickFlag){
    if(treeNode.type == "student"){
        var id = treeNode.id;
        $("#studentId").val(id);
        changeStuId();
    }
}
</script>