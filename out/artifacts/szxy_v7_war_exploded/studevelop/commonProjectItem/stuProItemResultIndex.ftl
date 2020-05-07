<title>身心健康登记</title>
<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row">
    <div class="col-xs-12">
        <div class="box box-default">
            <div class="row">
                <div class="col-sm-3" id="treeDivId">
                    <#if isAdmin?default(false)>
	                    <div class="box box-default" id="id1">
	                        <div class="box-header">
	                            <h3 class="box-title">班级菜单</h3>
	                        </div>
	                    <@studevelopTreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
	                    </div>
	                 </#if>
                </div>
                <div class="col-sm-9" >
                    <div class="box-body" id="id2">
                        <div class="box-body">
                            <!-- PAGE CONTENT BEGINS -->
                            <div class="filter clearfix" style="padding-left: 40px;">
                                <div class="filter-item">
                                    <label for="" class="filter-name">学年：</label>
                                    <div class="filter-content">
                                        <select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="changeAcadyear()">
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
                                        <select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="changeAcadyear()">
                                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" id="studentId">
                                <input type="hidden" id="isAdmin" value='${isAdmin?default(false)?string('true','false')}'>
                                <input type="hidden" id="classId">
                                <input type="hidden" id="isExportId">
                                <a href="javascript:" class="btn btn-blue attBtn" onclick="doImport()" disabled="disabled">导入</a>
                            </div>
                            <ul class="nav nav-tabs mt7" role="tablist" id="code">
                                <li role="presentation" class="active"  >
                                    <a href="javascript:void(0)"   role="tab" val="1" onclick="doSearch('1');" data-toggle="tab">报告单成绩登记</a>
                                </li>
                                <li role="presentation"   >
                                    <a href="javascript:void(0)"   role="tab" val="2" onclick="doSearch('2');" data-toggle="tab">身心健康</a>
                                </li>
                                <li role="presentation"  >
                                    <a href="javascript:void(0)"   role="tab" val="3" onclick="doSearch('3');" data-toggle="tab">思想素质</a>
                                </li>
                                <li role="presentation"  >
                                    <a href="javascript:void(0)"   role="tab" val="6" onclick="doSearch('6');" data-toggle="tab">期末评价</a>
                                </li>
                            </ul>
                            <div class="box-body showList" id="showList"  >

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
	jQuery(document).ready(function(){
		<#if !isAdmin?default(false)>
			getMyTree();
		</#if>
    })
    function getMyTree(){
    	var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var str='?acadyear='+acadyear+"&semester="+semester;
        var url='${request.contextPath}/stuDevelop/proItemResult/toClassTree/page'+str;
        $('#treeDivId').load(url);
    }
    function changeAcadyear() {
        var code = $("#code").find("li[class = 'active']").find("a").attr("val");
        <#if !isAdmin?default(false)>
			getMyTree();
			$("#classId").val("");
            $("#studentId").val("");
			$("#showList").text("");
		<#else>
    	    doSearch(code);
		</#if>
    }
    function onTreeClick(event, treeId, treeNode, clickFlag){
        var code = $("#code").find("li[class = 'active']").find("a").attr("val");
        if(treeNode.type == "student"){
            var id = treeNode.id;
            $("#studentId").val(id);
            $("#classId").val(treeNode.pId);
            $(".attBtn").attr("disabled", false);
            doSearch(code);
        }else if(treeNode.type == "class"){
            var id = treeNode.id;
            $("#classId").val(id);
            $("#studentId").val("");
            $(".attBtn").attr("disabled", false);
            doClaSearch(code);
        }
    }
    function doClaSearch(code){
    	var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var classId=$("#classId").val();
        if(classId == ""){
            return;
        }
		$(".attBtn").show();
        var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId +"&code="+code;
        var url = "";
         if(code =='1'){
            url='${request.contextPath}/stuDevelop/proItemResult/gradeScore/classList'+ass;
        }else if(code == '6'){
        	url='${request.contextPath}/studevelop/evaluateRecord/classList'+ass;
        } else{
            url='${request.contextPath}/stuDevelop/proItemResult/classList'+ass;
        }
        $('#showList').load(url);
    }
    function doSearch(code){
    	var classId=$("#classId").val();
    	if(classId){
    		$(".attBtn").show();
    	}
        var stuId=$("#studentId").val();
        if(!stuId){
        	doClaSearch(code);
        	return;
        }
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        if(stuId == ''){
            return;
        }
        var ass = '?acadyear='+acadyear+'&semester='+semester+'&studentId='+stuId +"&code="+code;
        var url = "";
        if(code =='1'){
            url='${request.contextPath}/stuDevelop/proItemResult/gradeScore/edit'+ass;
        }else if(code == '6'){
            url='${request.contextPath}/studevelop/evaluateRecord/Edit'+ass;
        }else if(code == '7'){
            url='${request.contextPath}/stuDevelop/proItemResult/gradeScore/edit'+ass;
        } else{
            url='${request.contextPath}/stuDevelop/proItemResult/edit'+ass;
        }
        $('#showList').load(url);
    }
    function  changeOption(){
        doSearch('1');
    }
    //期末评价 填写
    function EvaluateList(){

        var code = $("#code").find("li[class = 'active']").find("a").attr("val");
        if(code != '6'){
            return;
        }
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

    function doImport(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var classId=$("#classId").val();
        var isAdmin=$("#isAdmin").val();
        if(classId == ""){
            return;
        }
        var code = $("#code").find("li[class = 'active']").find("a").attr("val");
    	$("#isExportId").val("1");//进入导入页面
        $(".attBtn").hide();
        var str = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId+'&code='+code+"&isAdmin="+isAdmin;
        var url='${request.contextPath}/studevelop/resultImport/head'+str;
		$(".showList").load(url);
    }
</script>