<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${examInfo.id!}">
<input type="hidden" name="unitId" id="unitId" value="${examInfo.unitId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${examInfo.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${examInfo.semester!}">
	<div class="layer-body" style="height:450px;overflow:auto;">
		<div class="filter clearfix"> 
			<div class="filter clearfix">
				<div class="filter-item">
					<label for="" class="filter-name">年级：</label>
					<div class="filter-content" style="width:470px;">
						<#if gradeList?exists && gradeList?size gt 0>
						<#list gradeList as grade>
							<#if grade_index==0>
							<label class="pos-rel labelchose-w">
								<input name="ranges" type="checkbox" <#if examInfo.id?default('')!=''>disabled</#if>  value="${grade.gradeCode!}" class="wp" <#if examInfo.ranges?default('')?index_of(grade.gradeCode) gte 0>checked="checked"</#if> />
								<span class="lbl">${grade.gradeName!}</span>
						    </label>
						    <#else>
						    	<label class="pos-rel labelchose-w">
								<input name="ranges" type="checkbox" <#if examInfo.id?default('')!=''>disabled</#if>  value="${grade.gradeCode!}" class="wp" <#if examInfo.ranges?default('')?index_of(grade.gradeCode) gte 0>checked="checked"</#if> />
								<span class="lbl" style="margin-left:10px;">${grade.gradeName!}</span>
						   		 </label>
						    </#if>
					    </#list>
						</#if>
						
						
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">考试编号：</label>
					<div class="filter-content">
						<input readonly maxLength="10" type="text" name="examInfo.examCode" id="examCode" oid="examCode" placeholder="系统自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${examInfo.examCode!}" />
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">考试开始时间：</label>
					<div class="filter-content">
					    <div class="input-group">
							<input class="form-control date-picker" vtype="data" style="width: 150px" type="text" nullable="false" name="examStartDate" id="examStartDate" placeholder="考试开始时间" value="${(examInfo.examStartDate?string('yyyy-MM-dd'))!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
					    </div>
				     </div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">考试类型：</label>
					<div class="filter-content" id="examTypeDiv">
						<select <#if examInfo.id?default('')!=''>disabled</#if> name="examType" id="examType" oid="examType" onchange="doChangeType();" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
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
					    <div class="input-group">
							<input class="form-control date-picker" vtype="data" style="width: 150px" type="text" nullable="false" name="examEndDate" id="examEndDate" placeholder="考试结束时间" value="${(examInfo.examEndDate?string('yyyy-MM-dd'))!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
					    </div>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">考试名称：</label>
					<div class="filter-content" style="width:470px;">
						<input type="text" class="form-control" name="examName" id="examName" style="width:100%;" value="${examInfo.examName!}" nullable="false"/>
					</div>
				</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">是否新高考：</label>
						<div class="filter-content">
							<#if examInfo.id?default('')!=''>
								<label>
								<#if examInfo.isgkExamType?default('') == '0'>
									否
								<#else>
									是
								</#if>
								</label>
								<input type="hidden" name="isgkExamType" value="${examInfo.isgkExamType!}">
							<#else>
								<label>
									<input name="isgkExamType" type="radio" <#if '${examInfo.isgkExamType!}'=='0' || '${examInfo.isgkExamType!}'==''>checked</#if> class="ace" value="0"/>
									<span class="lbl"> 否</span>
								</label>&nbsp;&nbsp;
								<label>
									<input name="isgkExamType" type="radio" <#if '${examInfo.isgkExamType!}'=='1'>checked</#if> class="ace" value="1"/>
									<span class="lbl"> 是</span>
								</label>
							</#if>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">统考类型：</label>
						<div class="filter-content">
							<select <#if examInfo.id?default('')!=''>disabled</#if> name="examUeType" id="examUeType" oid="examUeType" onchange="changeExamUeType();" nullable="false" data-placeholder="请选择" style="width:470px;" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
								<#if tklxMap?? && (tklxMap?size>0)>
									<#list tklxMap?keys as key> 
				                   		<option value="${key}" <#if key==examInfo.examUeType?default('')>selected</#if>>${tklxMap[key]}</option>
					                </#list>
					            <#else>
					            	 <option value="">--- 请选择 ---</option>
								</#if>
					        </select>
						</div>
					</div>
					<#--
					<div class="filter-item">
						<label for="" class="filter-name">非正常成绩是否统分：</label>
						<div class="filter-content">
							<label>
								<input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='0' || '${examInfo.isTotalScore!}'==''>checked</#if> class="ace" value="0"/>
								<span class="lbl"> 否</span>
							</label>&nbsp;&nbsp;
							<label>
								<input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='1'>checked</#if> class="ace" value="1"/>
								<span class="lbl"> 是</span>
							</label>
						</div>
					</div>	
					-->			
				</div>
				
				<div class="filter-item" id="lkxzSelectDiv">
                   <label class="filter-name">联考选择：</label>
                   <div class="filter-content">
		                <select multiple="multiple" name="lkxzSelect" id="lkxzSelect"  data-placeholder="联考选择">
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
		</div>
	</div>
	</form>
</div>	
<div class="layer-footer">
	<button class="btn btn-lightblue" id="arrange-commit">确定</button>
	<button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<script>
function doChangeType(){
	var examTypeText=$('#examType option:selected').text(); 
	$("#examName").val('${examInfo.examName!}'+examTypeText);
}

function changeExamUeType(){
    if($("#examUeType").val()=='3'){
		$("#lkxzSelectDiv").show();
		$('#layui-layer2').attr("style","z-index: 19891016; width: 750px;  top: 139.5px; left: 240px;");
	}else{
		$("#lkxzSelectDiv").hide();
		$('#layui-layer2').attr("style","z-index: 19891016; width: 750px;  top: 139.5px; left: 240px;");
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
		
		<#if examInfo.examUeType?default('')!='3'>
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
		layerTipMsg(false,"提示!","考试开始时间不能大于考试结束时间!");
		isSubmit=false;
		return;
	} 
	
	var rangesCheck=$("input:checkbox[name='ranges']:checked");
	if(rangesCheck.length<=0){
		layerTipMsg(false,"提示!","年级范围至少选择一个!");
		isSubmit=false;
		return;
	}
	<#if examInfo.id?default('')==''>
		var isgkExamType=$("input[name='isgkExamType']:checked").val();
		
		if(isgkExamType=="1"){
			//是新高考
			var f=false;
			$("input:checkbox[name='ranges']:checked").each(function(){
				var vv=$(this).val();
				if(vv.indexOf("3")!=0){
					f=true;
					return false;
				}
			});
			if(f){
				layerTipMsg(false,"提示!","选择新高考，那么年级只能选择高中范围!");
				isSubmit=false;
				return;
			}
		}
	</#if>
	
	
	var options = {
		url : "${request.contextPath}/scoremanage/examInfo/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
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