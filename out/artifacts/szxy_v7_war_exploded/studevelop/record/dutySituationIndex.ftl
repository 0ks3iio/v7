<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>任职情况登记</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">

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
                       <!-- PAGE CONTENT BEGINS -->
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
                           <a href="javascript:void(0);" class="btn btn-blue pull-left btn-seach"  onclick="addExam()">新增</a>
                           <#--<a href="javascript:void(0);" class="btn btn-blue "  style="margin-left:10px;" onclick="importDutySituation()">导入</a>-->
                       </div>
				   <div class="box-body showList">

				   </div>
                   </div>
               </div>
           </div>
</div>
</div>
</div>
<script>
//	$(function(){
////		changeExam();
//	});
	<#--function changeExam(){-->
		<#--var acadyear=$("#acadyear").val();-->
		<#--var semester=$("#semester").val();-->
		<#--var classId=$("#classIdSearch");-->
		<#--var stuId=$("#stuId");-->
		<#--$.ajax({-->
			<#--url:"${request.contextPath}/studevelop/dutySituation/classIds",-->
			<#--data:{acadyear:acadyear,semester:semester},-->
			<#--dataType: "json",-->
			<#--success: function(data){-->
				<#--classId.html("");-->
				<#--classId.chosen("destroy");-->
				<#--if(data.length==0){-->
					<#--classId.append("<option value='' >-----请选择-----</option>");-->
					<#--stuId.html("");-->
					<#--stuId.chosen("destroy");-->
					<#--stuId.append("<option value='' >-----请选择-----</option>");-->
					<#--stuId.chosen({-->
						<#--width:'145px',-->
						<#--no_results_text:"未找到",//无搜索结果时显示的文本-->
						<#--allow_single_deselect:true,//是否允许取消选择-->
						<#--disable_search:false, //是否有搜索框出现-->
						<#--search_contains:true,//模糊匹配，false是默认从第一个匹配-->
						<#--//max_selected_options:1 //当select为多选时，最多选择个数-->
					<#--}); -->
				<#--}else{-->
					<#--for(var i = 0; i < data.length; i ++){-->
						<#--classId.append("<option value='"+data[i].id+"' >"+data[i].classNameDynamic+"</option>");-->
					<#--}-->
				<#--}-->
				<#--classId.chosen({-->
					<#--width:'145px',-->
					<#--no_results_text:"未找到",//无搜索结果时显示的文本-->
					<#--allow_single_deselect:true,//是否允许取消选择-->
					<#--disable_search:false, //是否有搜索框出现-->
					<#--search_contains:true,//模糊匹配，false是默认从第一个匹配-->
					<#--//max_selected_options:1 //当select为多选时，最多选择个数-->
				<#--}); -->
				<#--changeClass();-->
			<#--}-->
		<#--});-->
	<#--}-->
	<#---->
	<#--function changeClass(){-->
		<#--var classId=$("#classIdSearch").val();-->
		<#--var stuId=$("#stuId");-->
		<#--$.ajax({-->
			<#--url:"${request.contextPath}/studevelop/dutySituation/stuIds",-->
			<#--data:{classId:classId},-->
			<#--dataType: "json",-->
			<#--success: function(data){-->
				<#--stuId.html("");-->
				<#--stuId.chosen("destroy");-->
				<#--if(data.length==0){-->
					<#--stuId.append("<option value='' >---请选择---</option>");-->
				<#--}else{-->
					<#--stuId.append("<option value='' >-----请选择-----</option>");-->
					<#--for(var i = 0; i < data.length; i ++){-->
						<#--stuId.append("<option value='"+data[i].id+"' >"+data[i].studentName+"</option>");-->
					<#--}-->
				<#--}-->
				<#--stuId.chosen({-->
					<#--width:'145px',-->
					<#--no_results_text:"未找到",//无搜索结果时显示的文本-->
					<#--allow_single_deselect:true,//是否允许取消选择-->
					<#--disable_search:false, //是否有搜索框出现-->
					<#--search_contains:true,//模糊匹配，false是默认从第一个匹配-->
					<#--//max_selected_options:1 //当select为多选时，最多选择个数-->
				<#--}); -->
				<#--DutyList();-->
			<#--}-->
		<#--});-->
	<#--}-->
	
	function DutyList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classId").val();
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId;
		var url='${request.contextPath}/studevelop/dutySituation/listAll'+ass;
		$(".showList").load(url);
	}
	
	
	function changeStuId(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var stuId=$("#studentId").val();
		if(stuId==""){
			DutyList();
			return;
		}
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId;
		var url='${request.contextPath}/studevelop/dutySituation/list'+ass;
		$(".showList").load(url);
	}
	
	function addExam(){
		var id="";
		var stuId=$("#studentId").val();
		if(stuId==""){
			layerTipMsg(false,"请选择一个学生!","");
			return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId+'&id='+id;
		var url='${request.contextPath}/studevelop/dutySituation/edit'+ass;
		indexDiv = layerDivUrl(url,{title: "新增",width:750,height:350});
	}
    $(function(){
        $('#id2').height($('#id1').height());
    })
    function onTreeClick(event, treeId, treeNode, clickFlag){
        if(treeNode.type == "student"){
            var id = treeNode.id;
            $("#studentId").val(id);
            changeStuId()
        }else if(treeNode.type == "class"){
            var id = treeNode.id;
            $("#classId").val(id);
            $("#studentId").val("");
            DutyList();
		}
    }
    function doSearch(id){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+id;
        var url='${request.contextPath}/studevelop/dutySituation/list'+ass;
        $(".showList").load(url);
    }
function changeOption(){
    changeStuId();
}
	function importDutySituation(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var classId=$("#classId").val();
        if(classId==""){
            layerTipMsg(false,"请选择一个班级!","");
            return;
        }
        var ass = '?acadyear='+acadyear+'&semester='+semester+'&classId='+classId;
        $(".model-div").load("${request.contextPath}/studevelop/dutySituation/importLink.action"+ass);
        //$(".model-div").load("${request.contextPath}/tempUser/import/index.action");
	}
</script>