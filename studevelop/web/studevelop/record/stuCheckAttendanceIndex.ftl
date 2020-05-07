<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>考勤总计</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
   <div class="row">
   <#if isAdmin?default(false)>
	   <div class="col-sm-2">
	       <div class="box box-default" id="id1">
	           <div class="box-header">
	               <h3 class="box-title">班级菜单</h3>
	           </div>
		   <@studevelopTreemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
	       </div>
   		</div>
   	<div class="col-sm-10" >
   <#else>
  	 <div class="col-sm-12" >
   </#if>
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
               <#if !isAdmin?default(false)>
	               <div class="filter-item">
	                   <label for="" class="filter-name">班级：</label>
	                   <div class="filter-content">
	                       <select vtype="selectOne" class="form-control" name="classId" id="classId" onChange="changeOption()">
							   <#if clazzList?? && (clazzList?size>0)>
								   <#list clazzList as item>
		                               <option value="${item.id}" <#if item.id==classId?default('')>selected</#if>>${item.classNameDynamic!}</option>
								   </#list>
							   <#else>
		                           <option value="">暂无数据</option>
							   </#if>
	                       </select>
	                   </div>
	               </div>
	           <#else>
	               <input type="hidden" id="studentId">
	               <input type="hidden" id="classId">
               </#if>
               <input type="hidden" id="isExportId">
	            <div class="filter filter-item-right">
	                <a href="javascript:" class="btn btn-blue attBtn" style="margin-bottom:5px;" onclick="saveAllAtt()" disabled="disabled">保存</a>
	                <a href="javascript:" class="btn btn-blue attBtn" style="margin-bottom:5px;" onclick="exportAtt()" disabled="disabled">导入</a>
	            </div>
	       </div>
           <div class="box-body showList">

           </div>
	   </div>
   </div>
</div>
</div>
</div>
<script>
	<#if !isAdmin?default(false)>
		jQuery(document).ready(function(){
			checkList();
			$(".attBtn").attr("disabled", false);
	    })
	</#if>
	function checkList(){
		var isExportId=$("#isExportId").val();
		if(isExportId && isExportId=="1"){
			exportAtt();
			return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classId").val();
        if(classId == ""){
            return ;
        }
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId;
		var url='${request.contextPath}/studevelop/checkAttendance/listAll'+ass;
		$(".showList").load(url);
	}
	<#-- function changeStuId(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var stuId=$("#studentId").val();
		if(stuId==""){
			checkList();
			return;
		}
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId;
		var url='${request.contextPath}/studevelop/checkAttendance/list'+ass;
		$(".showList").load(url);
	}
	$(function(){
	
	})
-->
function onTreeClick(event, treeId, treeNode, clickFlag){
    if(treeNode.type == "student"){
        var id = treeNode.id;
        $("#studentId").val(id);
        changeStuId();
    }else if(treeNode.type == "class"){
        var id = treeNode.id;
        $("#classId").val(id);
        $("#studentId").val("");
        $(".attBtn").attr("disabled", false);
        checkList();
    }
}
function changeOption(){
    checkList();
}
function exportAtt(){
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var classId=$("#classId").val();
    if(classId == ""){
        return ;
    }
	$("#isExportId").val("1");//进入导入页面
	$(".attBtn").hide();
	var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId;
	var url='${request.contextPath}/studevelop/attImport/main'+ass;
	$(".showList").load(url);
}
</script>