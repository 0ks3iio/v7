<#if dtoList?exists && (dtoList?size > 0)>
<form id="scoreForm">
<input type="hidden" name="subjectId" value="${searchDto.subjectId!}"/>
<input type="hidden" name="inputType" value="${subjectInfo.inputType!}"/>
<input type="hidden" name="gradeType" value="${subjectInfo.gradeType!}"/>

<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin" id="showGradeTabble">
		<thead>
			<tr>
				<th>姓名</th>
				<th>考号</th>
				<th>学号</th>
				<th>班级</th>
				<th>考试成绩</th>
				<th>状态</th>
				<#if needGeneral?exists && needGeneral>
			        <th>总评成绩</th>
		        </#if>
			</tr>
		</thead>
		<tbody>
				<#list dtoList as dto>
					<tr>
						<td>
						<input type="hidden" name="dtoList[${dto_index}].id" value="${dto.scoreId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].studentId" value="${dto.stuId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].classId" value="${dto.classId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].teachClassId" value="${dto.teachClassId!}"/>
						${dto.stuName!}
						</td>
						<td>${dto.stuExamNum!}</td>
						<td>${dto.stuCode!}</td>
						<td>${dto.className!}</td>
						<#if subjectInfo.inputType=='S'>
							<td>
							<input type="text"  class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if lockState?default("")=='1' || dto.scoreStatus?default("0")=="1">disabled</#if> vtype = "number" nullable="false"  <#if dto.scoreStatus?default("0")!="1">value="${dto.score!}" placeholder="请输入考试成绩"</#if> maxLength="7">
								<#--<#if dto_index lt 30>
									<input type="text"  class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if lockState?default("")=='1'>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入考试成绩" value="${dto.score!}" maxLength="7">
								<#else>
									<input type="text"  class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if lockState?default("")=='1'>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入考试成绩" value="${dto.score!}" maxLength="7">
								</#if>-->
							</td>
						<#else>
							<td>
								<select <#if lockState?default("")=='1' || dto.scoreStatus?default("0")=="1">disabled</#if> name="dtoList[${dto_index}].score" id="score_${dto_index}"  class="form-control">
							 		${mcodeSetting.getMcodeSelect('${subjectInfo.gradeType!}','${dto.score!}',"0")}
								</select>
							</td>
						</#if>
						<td>
							<select <#if lockState?default("")=='1'>disabled</#if> onchange="changeScore('${dto_index}');" name="dtoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control"  notnull="false">
						 		${mcodeSetting.getMcodeSelect("DM-CJLX", '${dto.scoreStatus!}', "0")}
							</select>
						</td>
						<#if needGeneral?exists && needGeneral>
					        <td>
					        	<input type="text" class="table-input score_class" name="dtoList[${dto_index}].toScore" id="toScore_${dto_index}" <#if lockState?default("")=='1'>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入总评成绩" value="${dto.toScore!}" maxLength="7">
					        </td>
				        </#if>
					</tr>
				</#list>
		</tbody>
	</table>		
</div>
</form>
<br/>
<div class="row">
	<#if lockState?default("")=='1'>
		<div class="col-xs-12 text-center">
			<a href="javascript:void(0);" id="printResult" class="btn btn-blue" onclick="doPrintResult()">打印成绩表</a>
		</div>
	<#else>
		<div class="col-xs-12 text-right">
			<a href="javascript:void(0);" class="btn btn-blue" onclick="save('0');">保存</a>
			<a href="javascript:void(0);" class="btn btn-blue" id="mysubmit">提交</a>
		</div>
	</#if>
</div>
<#--打印内容 -->
<#if lockState?default("")=='1'>
<div id="printContent" style="display:none;">
	<h3 class="text-center">${unitName?replace("数字校园", "") + "("+acadyear+semesterName + ")"!}<br>${examName!}成绩登记表</h3>
	<div class="col-xs-12">
    	<span class="col-xs-4">班级：${classAllName!}</span>
		<span class="col-xs-4">科目：${subjectName!}</span>
	</div>
	
	<table class="table table-bordered table-condensed no-margin text-center" style="border:1px solid #000">
	    <thead style="border:1px solid #000">
	    	<tr >
		        <th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;"  style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">考试成绩</th>
		        <#if bindWriting?exists && bindWriting>
			        <th width="10%" class="text-center" style="border:1px solid #000">写作</th>
		        </#if>
		        <#if bindSpeeh?exists && bindSpeeh>
			        <th width="10%" class="text-center" style="border:1px solid #000">口试</th>
		        </#if>
		        <#if needGeneral?exists && needGeneral>
			        <th width="10%" class="text-center" style="border:1px solid #000">总评成绩</th>
		        </#if>
		        
				<th width="20px;" style="border:1px solid #000"></th>
		        
		        <th width="50px;" class="text-center" style="border:1px solid #000">序号</th>
		        <th width="200px;" class="text-center" style="border:1px solid #000">姓名</th>
		        <th width="10%" class="text-center" style="border:1px solid #000">考试成绩</th>
		        <#if bindWriting?exists && bindWriting>
			        <th width="10%" class="text-center" style="border:1px solid #000">写作</th>
		        </#if>
		        <#if bindSpeeh?exists && bindSpeeh>
			        <th width="10%" class="text-center" style="border:1px solid #000">口试</th>
		        </#if>
		        <#if needGeneral?exists && needGeneral>
			        <th width="10%" class="text-center" style="border:1px solid #000">总评成绩</th>
		        </#if>
	        </tr>
	    </thead>
	    <tbody>
	     <#if rowCount?default(0) gt 0>
	    <#list 1..rowCount as item>
	    	<tr >
		        <td style="border:1px solid #000">${item_index+1}</td>
		        <td style="border:1px solid #000" class="text-left">${dtoList[item_index].stuName!}</td>
		        <td style="border:1px solid #000">${dtoList[item_index].score!}</td>
		        <#if bindWriting?exists && bindWriting>
			        <td style="border:1px solid #000">${dtoList[item_index].writingScore!}</td>
		        </#if>
		        <#if bindSpeeh?exists && bindSpeeh>
			        <td style="border:1px solid #000">${dtoList[item_index].speehScore!}</td>
		        </#if>
		        <#if needGeneral?exists && needGeneral>
			        <td style="border:1px solid #000">${dtoList[item_index].toScore!}</td>
		        </#if>
		        
		        <#if item_index == 0>
		      		<td style="border:1px solid #000" rowspan="${rowCount!}"></td>
		      	</#if>
		        
		        <#if !(stuCount lt (item_index+rowCount+1))>
			        <td style="border:1px solid #000">${item_index+rowCount+1}</td>
			        <td style="border:1px solid #000" class="text-left">${dtoList[item_index+rowCount].stuName!}</td>
			        <td style="border:1px solid #000">${dtoList[item_index+rowCount].score!}</td>
			        <#if bindWriting?exists && bindWriting>
				        <td style="border:1px solid #000">${dtoList[item_index+rowCount].writingScore!}</td>
			        </#if>
			        <#if bindSpeeh?exists && bindSpeeh>
				        <td style="border:1px solid #000">${dtoList[item_index+rowCount].speehScore!}</td>
			        </#if>
			        <#if needGeneral?exists && needGeneral>
				        <td style="border:1px solid #000">${dtoList[item_index+rowCount].toScore!}</td>
			        </#if>

		        </#if>
	        </tr>
	    </#list>
	    </#if>
	    </tbody>
	</table>
	<p />
	<div style="col-xs-12" style="margin-top:20px;padding-top:20px;">
    	<span class="col-xs-5">备课组长（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-5">任课老师（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-2">打印时间：<span id="time"></span></span>
	</div>
</div>
</#if>
<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<script>
$(function(){
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
    });  
    
    //提交 
    $('#mysubmit').on('click',function(){
    	if(!allCheck()){
			return;
		}
    	
    	var remark = '确定提交吗？'
				+'<br><span class="text-danger">请务必确保提交之前已经保存，'
				+'<br> 提交以后将无法修改成绩信息</span>';
		layer.confirm(remark, {
			btn: ['确定', '取消'],
			yes: function(index){
		    	save("1");
			}
		});
    });        
})
function changeScore(index){
	if($("#scoreStatus_"+index).val()=="1"){
		$("#score_"+index).attr("disabled",true); 
		if($("#toScore_"+index)){
			$("#toScore_"+index).attr("disabled",true); 
		}
	}else{
		$("#score_"+index).attr("disabled",false);
		if($("#toScore_"+index)){
			$("#toScore_"+index).attr("disabled",false); 
		}
	}
}
var isSubmit=false;
function save(state){
	if(isSubmit){
		return;
	}
	layer.closeAll();
	if(!allCheck()){
		return;
	}
	isSubmit=true;
	var classId=$("#classId").val();
	var classType=$("#classType").val();
	var examId=$("#examId").val();
	var subType=$("#subType").val();
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/scoreNewInput/saveAll',
		data:{state:state,classId:classId,classType:classType,examId:examId,subType:subType},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				chooseTab('${subjectId!}',subType);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#scoreForm").ajaxSubmit(options);
} 
function allCheck(){
	<#if subjectInfo.inputType=='S' || (needGeneral?exists && needGeneral)>
	var f=false;
	$(".score_class").each(function(){
		var index=$(this).attr("id").split("_")[1];
		if($(this).val()=='' && $("#scoreStatus_"+index).val()!=1){
			f=true;
			layer.tips('不能为空', $(this), {
				tipsMore: true,
				tips: 3
			});
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	var max=${subjectInfo.fullScore!};
	
	$(".score_class").each(function(){
		var score=$(this).val();
		if(score){
			var r = score.match(reg);
			if(r==null){
				f=true;
				layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
			var s=parseFloat(score);
			if(s>max){
				f=true;
				layer.tips('考试成绩不能超过'+max+'!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	</#if>
	return true;
}

//打印成绩表
function doPrintResult(){
	var now = new Date();
	$("#time").html(now.getFullYear()+'/'+(now.getMonth()+1)+'/'+now.getDate());
	
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	if (LODOP==undefined || LODOP==null) {
		return;
	}
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_HTM("15mm","10mm","RightMargin:10mm","BottomMargin:3mm",getPrintContent($("#printContent")));
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//纵向打印
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.PREVIEW();//打印预览
}
</script>
<#else>
 	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>