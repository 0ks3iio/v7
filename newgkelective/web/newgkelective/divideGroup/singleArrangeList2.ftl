<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<#--<a href="javascript:void(0)" onclick="toBack()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
<form id="editForm" name="editForm" method="post" >
<div class="box-body groupArrangeList">
	<#if (aExList?exists && aExList?size gt 0) || (bExList?exists && bExList?size gt 0)>
	<#assign allindex=-1>
	<div class="row">
	<div class="col-sm-6">
	<#--选考批次：--><input type="hidden" style="width:10%" id="batchCountTypea" nullable="false" vtype="int" name="batchCountTypea" max="10" min="1" <#if allTwo> readOnly="readOnly" </#if> value="${divide.batchCountTypea?default(3)}" >
	<table class="table table-bordered" style="margin-top:10px">
		<tbody>
			<tr>
				<th width="20%">科目</th>
				<th width="20%">人数</th>
				<th width="20%">班级数</th>
				<th width="">每班人数（浮动值）
					<input style="width:20%" type="text" id="aFloNumId" onkeydown="dispSetFloNum(this)" <#if !canEdit?default(true)>readOnly="readOnly"</#if>>
					<a type="button" class="btn btn-default <#if !canEdit?default(true)>disabled</#if>" onclick="setFloNum(this)">批量赋值</a>
				</th>
			</tr>
			<#if aExList?exists && aExList?size gt 0>
			<#list aExList as ex>
			<tr>
				<#assign allindex=allindex+1>
				<#if canEdit>
				<input type="hidden" name="exList[${allindex!}].id" value="${ex.id!}">
				<input type="hidden" name="exList[${allindex!}].divideId" value="${divide.id!}">
				<input type="hidden" name="exList[${allindex!}].subjectId" value="${ex.subjectId!}">
				<input type="hidden" name="exList[${allindex!}].subjectType" value="${ex.subjectType!'B'}">
				<input type="hidden" name="exList[${allindex!}].range" value="${ex.range!}">
				<input type="hidden" id="${allindex}_leastNum" name="exList[${allindex!}].leastNum" value="${ex.leastNum!}">
				<input type="hidden" id="${allindex}_maximum" name="exList[${allindex!}].maximum" value="${ex.maximum!}">
				</#if>
				<td>${ex.subjectName!}A</td>
				<td>${ex.stuNum!}</td>
				<td>
					<input type="hidden" id="${allindex}_hiddenStuNum" value="${ex.stuNum?default(0)}">
					<input type="text" class="inputClClass form-control pull-left num-input" <#if !canEdit?default(true)>readOnly="readOnly"</#if> nullable="false" vtype="int" min="0" max="${ex.stuNum?default(1)}" style="width:80%" id="${allindex}_inputCl" groupIndex="${allindex}" name="exList[${allindex}].classNum" value="${ex.classNum!}" onchange="doChangeClassNum(this,'${allindex}')">
				</td>
				<td>
					<div class="filter-content">
						<#assign cnum=-1>
						<#assign dnum=-1>
						<#if ex.maximum?exists && ex.leastNum?exists>
							<#assign cnum=((ex.maximum+ex.leastNum)/2)>
							<#assign dnum=((ex.maximum-ex.leastNum)/2)>
						</#if>
						<input type="text" style="width:40%" class="form-control pull-left" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_aveNum" nullable="false" vtype="int" max="${ex.stuNum?default(1)}" min="0" <#if cnum?default(-1)!=-1>value="${cnum?string('0')}"</#if>>
						<span class="pull-left">±</span>
						<input type="text" style="width:40%" class="form-control pull-left floClass" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_floNum" nullable="false" vtype="int" max="99" min="0" <#if dnum?default(-1)!=-1>value="${dnum?string('0')}"<#else>value="${defaultNum!}"</#if>>
					</div>
				</td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
	<#--学考批次：--><input type="hidden" style="width:10%" id="batchCountTypeb" nullable="false" vtype="int" name="batchCountTypeb" <#if !canEdit?default(true)>readOnly="readOnly"</#if>max="10" min="3" value="${divide.batchCountTypeb?default(3)}">
	<table class="table table-bordered" style="margin-top:10px">
		<tbody>
			<tr>
				<th width="20%">科目</th>
				<th width="20%">人数</th>
				<th width="20%">班级数</th>
				<th width="">每班人数（浮动值）
					<input style="width:20%" type="text" id="bFloNumId" onkeydown="dispSetFloNum(this)" <#if !canEdit?default(true)>readOnly="readOnly"</#if>>
					<a type="button" class="btn btn-default  <#if !canEdit?default(true)>disabled</#if>" onclick="setFloNum(this)">批量赋值</a>
				</th>
			</tr>
			<#if bExList?exists && bExList?size gt 0>
			<#list bExList as ex>
			<tr>
				<#assign allindex=allindex+1>
				<#if canEdit>
				<input type="hidden" name="exList[${allindex!}].id" value="${ex.id!}">
				<input type="hidden" name="exList[${allindex!}].divideId" value="${divide.id!}">
				<input type="hidden" name="exList[${allindex!}].subjectId" value="${ex.subjectId!}">
				<input type="hidden" name="exList[${allindex!}].subjectType" value="${ex.subjectType!'B'}">
				<input type="hidden" name="exList[${allindex!}].range" value="${ex.range!}">
				<input type="hidden" id="${allindex}_leastNum" name="exList[${allindex!}].leastNum" value="${ex.leastNum!}">
				<input type="hidden" id="${allindex}_maximum" name="exList[${allindex!}].maximum" value="${ex.maximum!}">
				</#if>
				<td>${ex.subjectName!}B</td>
				<td>${ex.stuNum!}</td>
				<td>
					<input type="hidden" id="${allindex}_hiddenStuNum" value="${ex.stuNum?default(0)}">
					<input type="text" class="inputClClass form-control pull-left num-input" <#if !canEdit?default(true)>readOnly="readOnly"</#if> nullable="false" vtype="int" min="0" max="${ex.stuNum?default(1)}" style="width:80%" id="${allindex}_inputCl" groupIndex="${allindex}" name="exList[${allindex}].classNum" value="${ex.classNum!}" onchange="doChangeClassNum(this,'${allindex}')">
				</td>
				<td>
					<div class="filter-content">
						<#assign cnum=-1>
						<#assign dnum=-1>
						<#if ex.maximum?exists && ex.leastNum?exists>
							<#assign cnum=((ex.maximum+ex.leastNum)/2)>
							<#assign dnum=((ex.maximum-ex.leastNum)/2)>
						</#if>
						<input type="text" style="width:40%" class="form-control pull-left" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_aveNum" nullable="false" vtype="int" max="${ex.stuNum?default(1)}" min="0" <#if cnum?default(-1)!=-1>value="${cnum?string('0')}"</#if>>
						<span class="pull-left">±</span>
						<input type="text" style="width:40%" class="form-control pull-left floClass" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_floNum" nullable="false" vtype="int" max="99" min="0" <#if dnum?default(-1)!=-1>value="${dnum?string('0')}"<#else>value="${defaultNum!}"</#if>>
					</div>
				</td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	</div>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noOpenClass.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">请先设置上一步</p>
			</div>
		</div>
	</div>
	</#if>
	
	
	<div class="text-center">
		<a class="btn btn-blue nextStep-btn singleBtnClass <#if haveDivideIng>disabled</#if>" <#if !haveDivideIng> href="javascript:"</#if> >完成开班</a>
		<span class="color-blue" id="showMessId">
		 	<#if haveDivideIng><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
				正在分班中，请稍等．．．
			</#if>
		 </span>
	</div>
</div>
</form>
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(toBack,false,"分班安排");
})
$('.nextStep-btn').on("click",function(){
	<#if canEdit>
		saveGroup();
	</#if>
});

function setFloNum(obj){
	var thisNum = $(obj).siblings().val();
	if(!/^\d+$/.test(thisNum) || thisNum < 0){
		var thisId = $(obj).siblings().attr('id');
		layer.tips("请输入正整数", "#"+thisId, {
			tipsMore: true,
			tips:3		
		});
		return;
	}
	$(obj).parents("table").find(".floClass").each(function(){
		$(this).val(thisNum);
	})
}
function dispSetFloNum(obj){
	var x;
    if(window.event) // IE8 以及更早版本
    {	x=event.keyCode;
    }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    {
        x=event.which;
    }
    if(13==x){
        setFloNum($(obj).siblings());
    }
}

function toBack(){
	var url =  '${request.contextPath}/newgkelective/${divide.id!}/divideClass/resultClassList';
	$("#showList").load(url);
}

function doChangeClassNum(obj,index){
	var clanum = $(obj).val($(obj).val().trim()).val();
	var stuNum = $(obj).parents("tr").find("#"+index+"_hiddenStuNum").val();
	var aveNumObj = $(obj).parents("tr").find("#"+index+"_aveNum");
	if (!/^\d+$/.test(clanum) || clanum < 0) {
		layer.tips("请输入正整数", "#"+index+"_inputCl", {
			tipsMore: true,
			tips:3		
		});
		aveNumObj.val("");
		return;
	}
	if(clanum==0){
		if(stuNum>0){
			layer.tips("请输入正整数", "#"+index+"_inputCl", {
				tipsMore: true,
				tips:3		
			});
			aveNumObj.val("");
			return;
		}
		aveNumObj.val(0);
		$(obj).parents("tr").find("#"+index+"_floNum").val(0);
		return;
	}
	var aveNum = parseInt(stuNum/clanum);
	var floNum = $(obj).parents("tr").find("#"+index+"_floNum").val();
	if((aveNum<1) || (floNum!=null && floNum!="" && aveNum<floNum)){
		layer.tips("班级数不合理请重新设置", "#"+index+"_inputCl", {
			tipsMore: true,
			tips:3		
		});
		aveNumObj.val("");
		return;
	}
	aveNumObj.val(aveNum);
}
var isSubmit=false;
var saveBtn = '.nextStep-btn';
function saveGroup(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(saveBtn).addClass("disabled");
	
	var check = checkValue('.groupArrangeList');
	if(!check){
	 	$(saveBtn).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	
	var flag = true;
	$('.num-input').each(function(){
		var order = $(this).attr('groupIndex');
		var aveNum = $("#"+order+"_aveNum").val();
		var floNum = $("#"+order+"_floNum").val();
		var stuNum = $(this).parents("tr").find("#"+order+"_hiddenStuNum").val();
		if(aveNum-floNum<0){
			flag=false;
			layer.tips("浮动值不合理请重新设置", "#"+order+"_floNum", {
				tipsMore: true,
				tips:3		
			});
			$("#"+order+"_floNum").val("");
			return;
		}
		$("#"+order+"_leastNum").val(aveNum-floNum);
		$("#"+order+"_maximum").val(parseInt(aveNum)+parseInt(floNum));
	});
	if(!flag){
		$(saveBtn).removeClass("disabled");
		isSubmit=false;
		return;
	}
	
	// 提交数据
	var options = {
	    url:'${request.contextPath}/newgkelective/${divide.id!}/singleList/save',
	    dataType : 'json',
	    type:'post',  
	    cache:false, 
	    success:function(data) {
	    	var jsonO = data;//JSON.parse(data);
	    	if(jsonO.success){
	 			layer.closeAll();
	    		//var url='${request.contextPath}/newgkelective/${divide.id!}/singleList/page';
	    		//$("#showList").load(url);
	    		autoAllClassByGroup('${divide.id!}');
	 		}
	 		else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 			$(saveBtn).removeClass("disabled");
	 			isSubmit=false;
			}
	     },
	     error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
 	$('#editForm').ajaxSubmit(options);
}

var isGroupAuto=false;
function autoAllClassByGroup(divideId){
	editDate=false;
	if(isGroupAuto){
		return;
	}
	isGroupAuto=true;
	var text='<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">'
				+'正在分班中，请稍等．．．';
	$("#showMessId").html(text);
	//分班代码
	autoAllClass3(divideId,"1");
}

function autoAllClass3(divideId,indexStr){
    
    var urlStr="${request.contextPath}/newgkelective/BathDivide/"+divideId+"/openClassArrange/saveBath";
    
	$.ajax({
		url:urlStr,
		data:{"divideId":divideId},
		dataType: "json",
		success: function(data){
			if(data.stat=="success"){
				//进入结果
				gobackResultList(divideId);
 			}else if(data.stat=="error"){
 				if(indexStr=="1"){
 					//上次失败进入分班
 					autoAllClass3(divideId,"0");
 				}else{
 					$(saveBtn).removeClass("disabled");
	 				isGroupAuto=false;
	 				$("#showMessId").html(data.message);
 				}
 			}else{
 				//不循环访问结果--直接进入首页autoAllClass2
 				gobackResult(divideId);
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}


</script>