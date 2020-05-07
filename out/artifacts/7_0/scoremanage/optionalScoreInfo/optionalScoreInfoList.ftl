<#if dtoList?exists && (dtoList?size > 0)>
<form id="scoreForm">

<#--<input type="hidden" name="acadyear" value="${acadyear!}"/>
<input type="hidden" name="semester" value="${semester!}"/> -->
<input type="hidden" name="subjectId" value="${subjectId!}"/>
<input type="hidden" name="teachClassId" value="${teachClassId!}"/>
<input type="hidden" name="unitId" value="${unitId!}"/>

<#if hasEditRole == 0>
<div class="table-container-header clearfix">
	<label class="pull-right no-margin">
	<label for="" class="control-label no-padding-right">解锁：</label>
	<input type="checkbox" class="wp wp-switch js-toggleLock" <#if classInfo?exists && (classInfo.isLock!'0') != '1'>checked="true"</#if>><span class="lbl"></span></label>
</div>
</#if>
<div class="table-wrapper" >
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>学号</th>
					<th>班级</th>
					<th>姓名</th>
					<th>考试成绩</th>
					<th>学分</th>
					<th>状态</th>
				</tr>
		</thead>
		<tbody>
			<#list dtoList as dto>
				<tr>
					<td>
					<input type="hidden" name="dtoList[${dto_index}].id" value="${dto.scoreId!}"/>
					<input type="hidden" name="dtoList[${dto_index}].studentId" value="${dto.stuId!}"/>
					${dto.stuCode!}
					</td>
					<td>${dto.className!}</td>
					<td>${dto.stuName!}</td>
					<td>
						<input type="hidden" value="${dto.fullMark!}">
						<input type="text" <#if (classInfo?exists && (classInfo.isLock!'0') == '1') || (hasEditRole == -1)>disabled</#if> class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" vtype = "number" nullable="false"  placeholder="请输入考试成绩" value="${dto.score!}" maxLength="7">
					</td>
					<td><#if dto.toScore?exists>${dto.toScore!}<#elseif dto.score?exists>此班级未设置学分<#else>该学生未录入成绩</#if></td>
					<td>
						<select <#if (classInfo?exists && (classInfo.isLock!'0') == '1') || (hasEditRole == -1)>disabled</#if> name="dtoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control"  notnull="false">
					 		${mcodeSetting.getMcodeSelect("DM-CJLX", '${dto.scoreStatus!}', "0")}
						</select>
					</td>
				</tr>
			</#list>
		</tbody>
	</table>		
</div>
</form>
<br/>
<div class="row">
	<#if classInfo?exists && (classInfo.isLock!'0') == '1'>
		<div class="col-xs-12 text-center">
			<a href="javascript:void(0);" id="printResult" class="btn btn-blue" onclick="doPrintResult()">打印成绩表</a>
		</div>
	<#elseif (hasEditRole gt -1)>
		<div class="col-xs-12 text-right">
			<a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
			<a href="javascript:void(0);" class="btn btn-blue" id="mysubmit">提交</a>
		</div>
	</#if>
</div>
<#--隐藏的打印内容-->
<#if rowCount?exists>
<div style="display:none;" id="printContent">
	<h1 class="text-center">${unitName?replace("数字校园", "") + "("+acadyear+semesterName + ")"!}<br>${examName!}成绩登记表</h1>
	<div style="col-xs-12">
		<span class="col-xs-4">班级：${className!}</span>
	</div>
	<table class="table table-bordered table-condensed no-margin"  style="border:1px solid #000">
	    <thead>
	       <tr>
		         <th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;"  style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">考试成绩</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">学分</th>
		        
				<th width="20px;" style="border:1px solid #000"></th>

		         <th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;"  style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">考试成绩</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">学分</th>
	        </tr>
	    </thead>
	    <tbody>
	    <#if rowCount?default(0) gt 0>
	    <#list 1..rowCount as item>
	    	<tr  >
		        <td class="text-center" style="border:1px solid #000">${item_index+1}</td>
		        <td style="border:1px solid #000" class="text-left">${dtoList[item_index].stuName!}</td>
		        <td class="text-center" style="border:1px solid #000">${dtoList[item_index].score!}</td>
		        <td class="text-center" style="border:1px solid #000">${dtoList[item_index].toScore!}</td>
		        
		         <#if item_index == 0>
		      		<td width="10%" class="text-center" style="border:1px solid #000" rowspan="${rowCount!}"></td>
		      	</#if>
		        
		        <#if !(stuCount lt (item_index+rowCount+1))>
			        <td class="text-center" style="border:1px solid #000">${item_index+rowCount+1}</td>
			        <td style="border:1px solid #000" class="text-left">${dtoList[item_index+rowCount].stuName!}</td>
			        <td class="text-center" style="border:1px solid #000">${dtoList[item_index+rowCount].score!}</td>
			        <td class="text-center" style="border:1px solid #000">${dtoList[item_index+rowCount].toScore!}</td>
		        </#if>
	        </tr>
	    </#list>
	    </#if>
	    </tbody>
	</table>
	<div style="col-xs-12" style="margin-top:20px;padding-top:20px;">
    	<span class="col-xs-5">备课组长（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-5">任课老师（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-2">打印时间：<span id="time"></span></span>
	</div>	
</div>
</#if>
<script>
<#if (hasEditRole == 0)>
$(function(){
	//解锁 上锁
    $('.js-toggleLock').on('change', function(){
		if($(this).prop('checked') === true){
			//解锁
			changeLockStatus("0");
		}else{
			//上锁
			changeLockStatus("1");
		}
	});
});
</#if>
//上锁 解锁
function changeLockStatus(isLock,index){
	var examClassId = '${classInfo.id!}';
	url = '${request.contextPath}/scoremanage/scoreInfo/lock';
	params = 'examClassId='+examClassId+'&isLock='+isLock;
	function submit(index){
		$.ajax({
			type: "POST",
			url: url,
			data: params,
			success: function(msg){
				if(msg.success){
					if(isLock == null){
						layerTipMsg(true,"成功",msg.msg);
					}
					searchList();
				}else{
					layerTipMsg(msg.success,"失败",data.msg);
				}
			},
			dataType: "JSON"
    	});
		
		layer.close(index);
	}
	
	if(isLock == null){
		submit(index);
	}else{
		submit();
	}
}
$(function(){
	// 回车获取焦点
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
		    	save(true,index);
			}
		});
    });    
});
<#if classInfo?exists && (classInfo.isLock!'0') == '1'>
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
<#elseif (hasEditRole gt -1)>
function save(toSubmit,index){
	if(!allCheck()){
		return ;
	}
	// 提交数据
	var options = {
		url : '${request.contextPath}/scoremanage/optionalScore/saveAll',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			if(toSubmit){
	 				changeLockStatus(null,index);
	 			}else{
		 			layerTipMsg(data.success,"成功",data.msg);
					searchList();
	 			}
				//chooseTab('${subjectId!}','${subjectInfoId!}');
	 		}
	 		else{
	 			layerTipMsg(data.success,data.msg,data.detailError);
	 			//isSubmit=false;
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
	var check = checkValue('#scoreForm');
	if(!check){
		//alert('check not pass');
		return false;
	}
	//var reg = /^(([0-9])|([1-9][0-9]{1,2})|([0-9]\.[0-9]{1})|([1-9][0-9]{1,2}\.[0-9]{2}))$/;
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	var f=false;
	$(".score_class").each(function(){
		var r = $(this).val().match(reg);
		var max=$(this).siblings().val();
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
		var s=parseFloat($(this).val());
		if(s>max){
			f=true;
			layer.tips('考试成绩不能超过'+max+'!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	//alert();
	return true;
}
</#if>

</script>
<#else>
暂无数据，此班级可能没有学生
</#if>