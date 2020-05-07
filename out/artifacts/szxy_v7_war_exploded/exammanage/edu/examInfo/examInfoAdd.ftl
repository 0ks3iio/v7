<style>
.layui-layer .chosen-container-multi .chosen-choices{overflow:auto;height:80px!important;}
.layui-layer .chosen-container .chosen-results{max-height:180px;}
</style>

<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv" >
<form id="subForm">
	<input type="hidden" name="id" id="id" value="${examInfo.id!}">
	<input type="hidden" name="unitId" id="unitId" value="${examInfo.unitId!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select  <#if examInfo.id?default('')!=''>disabled</#if> class="form-control" id="acadyear" name="acadyear" style="width:150px;" nullable="false" onchange="doChangeType();">
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
						<select  <#if examInfo.id?default('')!=''>disabled</#if> class="form-control" id="semester" name="semester" nullable="false" style="width:150px;" onchange="doChangeType();">
							${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
						</select>
					</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试编号：</label>
				<div class="filter-content">
					<input readonly maxLength="10" type="text" name="examCode" id="examCode" oid="examCode" placeholder="系统自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " style="width:150px;" value="${examInfo.examCode!}" />
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试开始时间：</label>
				<div class="filter-content">
				    <div class="input-group"  style="width: 150px">
						<input class="form-control date-picker" vtype="data" type="text" nullable="false" name="examStartDate" id="examStartDate" placeholder="考试开始时间" value="${(examInfo.examStartDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
				    </div>
				 </div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试类型：</label>
				<div class="filter-content" id="examTypeDiv">
					<select <#if examInfo.id?default('')!=''>disabled</#if> name="examType" id="examType" oid="examType" onchange="doChangeType();" nullable="false" data-placeholder="请选择" style="width:150px;" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >
				        <option value="">--- 请选择 ---</option>
				        <#if kslbList?? && (kslbList?size>0)>
							<#list kslbList as item>
								<option value="${item.thisId}" <#if item.thisId==examInfo.examType?default('')>selected</#if>>${item.mcodeContent!}</option>
							</#list>
						    <#else>
						</#if>
			        </select>
					  
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试结束时间：</label>
				<div class="filter-content">
				    <div class="input-group"  style="width: 150px">
					<input class="form-control date-picker" vtype="data" type="text" nullable="false" name="examEndDate" id="examEndDate" placeholder="考试结束时间" value="${(examInfo.examEndDate?string('yyyy-MM-dd'))!}">
					<span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
				    </div>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content">
					<input type="text" class="form-control" name="examName" id="examName" value="${examInfo.examName!}" style="width:150px;" nullable="false"/>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content" id="gradeCodesDiv">
					<select <#if examInfo.id?default('')!=''>disabled</#if> name="gradeCodes" id="gradeCodes" oid="gradeCodes"  nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " style="width:150px;">
				        <option value="">--- 请选择 ---</option>
				        <#if gradeList?? && (gradeList?size>0)>
							<#list gradeList as item>
								<option value="${item.gradeCode!}" <#if item.gradeCode==examInfo.gradeCodes?default('')>selected</#if>>${item.gradeName!}</option>
							</#list>
						    
						</#if>
			        </select>
					  
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">统考类型：</label>
				<div class="filter-content">
					<select <#if examInfo.id?default('')!=''>disabled</#if> name="examUeType" id="examUeType" oid="examUeType" onchange="changeExamUeType();" nullable="false" data-placeholder="请选择" style="width:150px;" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >
				        <option value="">--- 请选择 ---</option>
						<#if tklxMap?? && (tklxMap?size>0)>
							<#list tklxMap?keys as key> 
		                   		<option value="${key}" <#if key==examInfo.examUeType?default('')>selected</#if>>${tklxMap[key]}</option>
			                </#list>
						</#if>
			        </select>
				</div>
			</div>
			<#if region>
				<div class="filter-item">
					<label for="" class="filter-name">是否可学生端报名：</label>
					<div class="filter-content" id="isStuSignDivId">
						<input type="radio" <#if examInfo.isStuSign?default("")=="1">checked="checked"</#if> name="isStuSign" value="1">是 &nbsp;
						<input type="radio" <#if examInfo.isStuSign?default("")=="0">checked="checked"</#if> name="isStuSign" value="0">否
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">报名开始时间：</label>
					<div class="filter-content">
						<div class="input-group" style="width: 150px" >
							<input class="form-control date-picker" vtype="data"type="text" nullable="false" name="signStartTime" id="signStartTime" placeholder="考试开始时间" value="${(examInfo.signStartTime?string('yyyy-MM-dd'))!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">报名结束时间：</label>
					<div class="filter-content">
						<div class="input-group" style="width: 150px">
							<input class="form-control date-picker" vtype="data" type="text" nullable="false" name="signEndTime" id="signEndTime" placeholder="考试结束时间" value="${(examInfo.signEndTime?string('yyyy-MM-dd'))!}">
							<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
						</div>
					</div>
				</div>
			</#if>
            <div class="filter-item" id="lkxzSelectDiv">
               <label class="filter-name">联考选择：</label>
               <div class="filter-content">
            		<select multiple="multiple" name="lkxzSelect" id="lkxzSelect"  data-placeholder="联考选择" style="width:150px;">
                        <#if unitList?? && (unitList?size>0)>
							<#list unitList as item>
								<option value="${item.id!}" <#if examInfo.lkxzSelectMap?? && examInfo.lkxzSelectMap[item.id]??>selected</#if>>${item.unitName?default('')}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
           			 </select>
           		</div>
            <div>
	     </div>  
     </div>
</form>
</div>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>
function doChangeType(){
	var examTypeText=$('#examType option:selected').text(); 
	var acadyearText=$('#acadyear option:selected').text();
	var semesterText=$('#semester option:selected').text();
	$("#examName").val(acadyearText+semesterText+examTypeText);
}

function changeExamUeType(){
    if($("#examUeType").val()=='3'||$("#examUeType").val()=='4'){
		$("#lkxzSelectDiv").show();
	}else{
		$("#lkxzSelectDiv").hide();
	}
}

 
$('#lkxzSelect').chosen({
	width:'500px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});


$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
		//初始化多选控件
		initChosenMore("#myDiv");
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
		
		<#if examInfo.examUeType?default('')!='3'&&examInfo.examUeType?default('')!='4'>
	    $("#lkxzSelectDiv").hide();
	    </#if>
	});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	    
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var examStartDate = $('#examStartDate').val();
	var examEndDate = $('#examEndDate').val();
	if(examStartDate>examEndDate){
		layerTipMsg(false,"提示","考试开始时间不能大于考试结束时间！");
		isSubmit=false;
		return;
	}
	<#if region>
		var signStartTime = $('#signStartTime').val();
		var signEndTime = $('#signEndTime').val();
		if(signStartTime>signStartTime){
            layerTipMsg(false,"提示","报名开始时间不能大于报名结束时间！");
            isSubmit=false;
            return;
        }
		if(signStartTime<examStartDate||signEndTime>examEndDate){
			layerTipMsg(false,"提示","报名时间必须在考试时间之间！");
			isSubmit=false;
			return;
		}
    </#if>
	if($("#examUeType").val()=='3'||$("#examUeType").val()=='4'){
		var chooseSchool=$("#lkxzSelect").val();
		if(chooseSchool==null){
			layerTipMsg(false,"提示","选择校校联考，联考选择不能为空！");
			isSubmit=false;
			return;
		}		
	}
	//考试名称问题
	var examName=$("#examName").val();
	
	var reg=/^[\u4E00-\u9FA5A-Za-z0-9-]+$/;
	if(!reg.test(examName)){
		showMsgError("考试名称只能由数字或者英文或者中文汉字或者-组成！");
		return;
	}
	
	
	var options = {
		url : "${request.contextPath}/exammanage/edu/examInfo/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	showList();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
		
	 });
</script>