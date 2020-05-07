<div class=" layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<input type="hidden" name="id" id="id" value="${examInfo.id!}">
	<input type="hidden" name="unitId" id="unitId" value="${examInfo.unitId!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select  <#if examInfo.id?default('')!=''>disabled</#if> class="form-control" id="acadyear" name="acadyear" nullable="false"    onchange="doChangeType();" style="width: 150px">
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if examInfo.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
	                    <#else>
		                    <option value="">未设置</option>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
					<div class="filter-content" style="width: 150px">
						<select  <#if examInfo.id?default('')!=''>disabled</#if> class="form-control" id="semester" name="semester" nullable="false"  style="width:150px;" onchange="doChangeType();">
							${mcodeSetting.getMcodeSelect('DM-XQ',(examInfo.semester?default(0))?string,'0')}
						</select>
					</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试编号：</label>
				<div class="filter-content">
					<input readonly maxLength="10" type="text" name="examCode" id="examCode" oid="examCode" placeholder="系统自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 "  style="width:150px;" value="${examInfo.examCode!}" />
				</div>
			</div>
            <div class="filter-item">
                <label for="" class="filter-name">年级：</label>
                <div class="filter-content" id="gradeCodesDiv" style="width: 150px">
                    <select <#if examInfo.id?default('')!=''>disabled</#if> name="gradeCodes" id="gradeCodes" oid="gradeCodes"  nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 "  style="width:150px;">
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
				<label for="" class="filter-name">考试开始时间：</label>
				<div class="filter-content">
				    <div class="input-group" style="width: 150px">
						<input class="form-control date-picker" vtype="data"  type="text" nullable="false" name="examStartDate" id="examStartDate" placeholder="考试开始时间" value="${(examInfo.examStartDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
				    </div>
				 </div>
			</div>
            <div class="filter-item">
                <label for="" class="filter-name">考试结束时间：</label>
                <div class="filter-content">
                    <div class="input-group" style="width: 150px">
                        <input class="form-control date-picker" vtype="data"  type="text" nullable="false" name="examEndDate" id="examEndDate" placeholder="考试结束时间" value="${(examInfo.examEndDate?string('yyyy-MM-dd'))!}">
                        <span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
                    </div>
                </div>
            </div>
			<div class="filter-item">
				<label for="" class="filter-name">考试类型：</label>
				<div class="filter-content" id="examTypeDiv">
					<select <#if examInfo.id?default('')!=''>disabled</#if> name="examType" id="examType" oid="examType" onchange="doChangeType();" nullable="false" data-placeholder="请选择"  style="width:150px;" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >
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
                <label for="" class="filter-name">统考类型：</label>
                <div class="filter-content">
                    <select <#if examInfo.id?default('')!=''>disabled</#if> name="examUeType" id="examUeType" oid="examUeType" onchange="changeExamUeType();" nullable="false" data-placeholder="请选择"  style="width:150px;"class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >
                        <option value="">--- 请选择 ---</option>
					<#if tklxMap?? && (tklxMap?size>0)>
						<#list tklxMap?keys as key>
                            <option value="${key}" <#if key==examInfo.examUeType?default('')>selected</#if>>${tklxMap[key]}</option>
						</#list>
					</#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content" >
					<input type="text" class="form-control" name="examName" id="examName" value="${examInfo.examName!}" nullable="false" style="width:436px"/>
				</div>
			</div>
			 <div class="filter-item">
				<label for="" class="filter-name">考试模式：</label>
				<div class="filter-content" id="isgkExamTypeDivId">
					<input <#if examInfo.id?default('')!='' && haveExamSub>disabled</#if> type="radio" <#if examInfo.isgkExamType?default("")=="0">checked="checked"</#if> name="isgkExamType" value="0">普通模式 &nbsp;
					<input <#if examInfo.id?default('')!='' && haveExamSub>disabled</#if> type="radio" <#if examInfo.isgkExamType?default("")=="1">checked="checked"</#if> name="isgkExamType" value="1">高考模式
 					&nbsp;<a href="javascipt:void(0);" data-toggle="tooltip" data-placement="top" title="" data-original-title="高考模式将获取学选考名单，按照各个科目组分别进行编排"><i class="fa fa-question-circle"></i></a>
				</div>
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
           	</div>
           	<div class="filter-item" id="toNeweletive" style="display:none;">
           		 <a href="javascipt:void(0);" class="toNeweletiveModel">系统无法获取该年级相应的7选3选学考名单，请先前往7选3模块中对该年级的选课结果轮次选择置为默认</a>
           	</div>
           	
           	<div class="filter-item" id="goGrade" <#if goGrade == '0'>style="display:none;"</#if>>
           		 <a href="javascipt:void(0);" class="goGradeModel">当前学年还没有进行年级升级毕业操作，请先前往设置</a>
           	</div>
           	
		</div>
     </div>
</form>
</div>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-blue" id="arrange-commit" <#if goGrade == '1'>style="display:none;"</#if>>确定</a>
	<a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
function doChangeType(){
	var examTypeText=$('#examType option:selected').text(); 
	var acadyearText=$('#acadyear option:selected').text();
	var semesterText=$('#semester option:selected').text();
	$("#examName").val(acadyearText+semesterText+examTypeText);
}

function changeExamUeType(){
    if($("#examUeType").val()=='3'){
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
		
		<#if examInfo.examUeType?default('')!='3'>
	    $("#lkxzSelectDiv").hide();
	    </#if>
	    $('[data-toggle="tooltip"]').tooltip();
	    
	    $(".toNeweletiveModel").on("click",function(){
	    	openModel('76101','分班排课','1','${request.contextPath}/newgkelective/index/page','智能分班排课','','	','');
	    	expand();
	    	layer.closeAll();
	    });
	    
	    $(".goGradeModel").on("click",function(){
			$('#509').click();
	    	expand2();
	    	layer.closeAll();
	    });
	});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
//跳转后左侧导航栏调整
function expand(){
	var liarr =$('.nav-list > li');
	liarr.each(function () {
	    if($(this).hasClass('open')) {
            $(this).find('ul').stop().slideUp(150).removeClass('open');
			$(this).find('li').removeClass('open').removeClass('active');
			$(this).removeClass('open');
        }
        var a = $(this).find('a');
        
        if(a[0].dataset.show =='7063301'){
            $(this).find('ul').stop().slideDown(150).addClass('open');
            $(this).find('li').addClass('open');
            $(this).addClass('open');
		}
    })
}

//跳转后左侧导航栏调整
function expand2(){
	var liarr =$('.nav-list > li');
	liarr.each(function () {
	    if($(this).hasClass('open')) {
            $(this).find('ul').stop().slideUp(150).removeClass('open');
			$(this).find('li').removeClass('open').removeClass('active');
			$(this).removeClass('open');
        }
        var a = $(this).find('a');
        if(a[0].dataset.show =='1013301'){
            $(this).find('ul').stop().slideDown(150).addClass('open');
            $('#509').parent().addClass('open');
            //$(this).find('li').addClass('open');
            //$(this).addClass('open');
		}
    })
}
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	var isgkExamType=$('input:radio[name="isgkExamType"]:checked').val();
    if(!isgkExamType){
		layer.tips('不能为空',$("#isgkExamTypeDivId"), {
			tipsMore: true,
			tips: 3
		});
	}
	var check = checkValue('#myDiv');
    if(!check || !isgkExamType){
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
	if($("#examUeType").val()=='3'){
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
		url : "${request.contextPath}/exammanage/examInfo/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			$("#arrange-commit").removeClass("disabled");
	 			if(jsonO.code=="-5"){
					$("#toNeweletive").show();
					return;
		 		}
				layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
				return;
	 		}
	 		else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	showList1();
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