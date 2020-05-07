<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>学生评价</title>
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
                                   <select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="changeOption()">
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
                                   <select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="changeOption()">
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
                           <a href="javascript:doImport();" class="btn btn-blue pull-left btn-seach">导入</a>
                       </div>
                       <div class="box-body showList">

                       </div>
				   </div>
</div>
</div>
</div>
<script>
	function EvaluateList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classId").val();
        if(classId==""){
            layerTipMsg(false,"请选择一个班级!","");
            return;
        }
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId;
		var url='${request.contextPath}/studevelop/evaluateRecord/listAll'+ass;
		$(".showList").load(url);
	}  
	
	
	function changeStuId(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var stuId=$("#studentId").val();
		if(stuId==""){
			EvaluateList();
			return;
		}
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&studentId='+stuId;
		var url='${request.contextPath}/studevelop/evaluateRecord/Edit'+ass;
		$(".showList").load(url);
	}

function onTreeClick(event, treeId, treeNode, clickFlag){
    if(treeNode.type == "student"){
        var id = treeNode.id;
        $("#studentId").val(id);
        changeStuId()
    }else if(treeNode.type == "class"){
        var id = treeNode.id;
        $("#classId").val(id);
        $("#studentId").val("");
        EvaluateList();
    }
}
function changeOption(){
    changeStuId();

}
function doImport(){
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
   var classId = $('#classId').val();
   if(classId == '' || classId == undefined){
       layerTipMsg(false,"请选择一个班级!","");
	   return;
   }
   $("#importDiv").load("${request.contextPath}/studevelop/evaluateRecord/import/main?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
}	
</script>