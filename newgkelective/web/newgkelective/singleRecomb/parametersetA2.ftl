<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>原行政班</span></li>
		<li class="active"><span><i>2</i>选考分层</span></li>
		<li class="active"><span><i>3</i>分选考班</span></li>
		<li><span><i>4</i>选考班结果</span></li>
		<li><span><i>5</i>分学考班</span></li>
		<li><span><i>6</i>学考班结果</span></li>
	</ul>
</div>
<div class="box box-default" id="parmA">
	<input type="hidden" id="weight" value="10">
	<#assign allindex=-1 />
	<form id="editForm" name="editForm" method="post" >
		<input type="hidden" name="followType" id="followType">
		<div class="box-body">	
			<div class="row groupArrangeList">
				<#if jxbList?exists && jxbList?size gt 0>
				<div class="col-sm-12">
					<h4 class="form-title">
						<b>走班科目</b>
					</h4>
					<table class="table table-bordered" style="margin-top:10px">
						<tbody>
							<tr>
								<th width="10%">科目</th>
								<th width="10%">总人数</th>
								<th width="10%">层级</th>
								<th width="10%">人数</th>
								<th width="20%">班级数</th>
								<th width="">每班人数（浮动值）
									<input style="width:20%;<#if !canEdit?default(true)>border: 1px solid #ccc;background-color: #eee;</#if>" type="text" id="aFloNumId" onkeydown="dispSetFloNum(this)" <#if !canEdit?default(true)>readOnly="readOnly"</#if>>
									<a type="button" class="btn btn-default <#if !canEdit?default(true)>disabled</#if>" onclick="setFloNum(this)">批量赋值</a>
								</th>
							</tr>
							<#list jxbList as dto>
								<#assign aSize=dto.exList?size />
								<#list dto.exList as ex>
								<#assign allindex=allindex+1 />
								<tr>
									<#if ex_index==0>
									<td rowspan="${aSize!}">${dto.subjectName!}A</td>
									<td rowspan="${aSize!}">${dto.stuNum!}</td>
									</#if>
									<#if canEdit>
									<input type="hidden" name="exList[${allindex!}].id" value="${ex.id!}">
									<input type="hidden" name="exList[${allindex!}].divideId" value="${divideId!}">
									<input type="hidden" name="exList[${allindex!}].subjectId" value="${dto.subjectId!}">
									<input type="hidden" name="exList[${allindex!}].subjectType" value="${ex.subjectType!'A'}">
									<input type="hidden" name="exList[${allindex!}].range" value="${ex.range!}">
									<input type="hidden" id="${allindex}_leastNum" name="exList[${allindex!}].leastNum" value="${ex.leastNum!}">
									<input type="hidden" id="${allindex}_maximum" name="exList[${allindex!}].maximum" value="${ex.maximum!}">
									</#if>
									<td>${ex.range!}</td>
									<td>${ex.stuNum!}</td>
									<td>
										<input type="hidden" id="${allindex}_hiddenStuNum" value="${ex.stuNum?default(0)}">
										<input type="text" class="inputClClass form-control pull-left num-input" <#if !canEdit?default(true)>readOnly="readOnly"</#if> nullable="false" vtype="int" max="${ex.stuNum?default(1)}" min="0" style="width:80%" id="${allindex!}_inputCl" groupIndex="${allindex}" name="exList[${allindex}].classNum" value="<#if ex.classNum?default(0) !=0 >${ex.classNum?default(0)}</#if>" onchange="doChangeClassNum(this,'${allindex!}')">
									</td>
									<td>
										<#assign anum=-1>
										<#assign bnum=-1>
										<#if ex.maximum?exists && ex.leastNum?exists>
											<#assign anum=((ex.maximum+ex.leastNum)/2)>
											<#assign bnum=((ex.maximum-ex.leastNum)/2)>
										</#if>
										<input type="text" style="width:40%" class="form-control pull-left" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_aveNum" nullable="false" vtype="int" max="${ex.stuNum?default(0)}" min="0" <#if anum?default(-1)!=-1>value="${anum?string('0')}"</#if>>
										<span class="pull-left">±</span>
										<input type="text" style="width:40%" class="form-control pull-left floClass" <#if !canEdit?default(true)>readOnly="readOnly"</#if> id="${allindex}_floNum" nullable="false" vtype="int" max="99" min="0" <#if bnum?default(-1)!=-1>value="${bnum?string('0')}"<#else>value="${floNum!}"</#if>>
									</td>
									</#list>
									</td>
								</tr>
							</#list>
						</tbody>
					</table>
				</div>
				</#if>
			</div>
			<#if (jxbList?exists && jxbList?size gt 0)>
			
			<#else>
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/7choose3/noSelectSystem.png" alt="">
					</span>
					<div class="no-data-body">
						<p class="no-data-txt">请先设置上一步</p>
					</div>
				</div>
			</div>
			</#if>
		</div>
	</form>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<#if !canEdit && !isDivideNow>
		<a class="btn btn-white" href="javascript:" onclick="clearNext();">重新安排</a>
	</#if>
	<a class="btn btn-white" href="javascript:" onclick="toBack();">上一步</a>
	<#if !isDivideNow>
	<a class="btn btn-blue nextStep-btn"  href="javascript:" onclick="nextfun()">下一步</a>
	</#if>
	<span class="color-blue" id="showMessId">
		<#if isDivideNow>
	 	<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
		正在分班中，请稍等．．．
		<#elseif errorOpenDivide?default('')!=''>
			${errorOpenDivide!}
		</#if>
	 </span>
</div>
<div id="beforeNext" style="display: none">
	<div class="filter-item block" style="margin-top: 20px; margin-left: 30px">
		<form id="followForm">
			<div class="filter-content">
				<input type="radio" class="followType" name="followType" value="A-1" checked>&nbsp;走班时间上课
			</div>
			<div class="filter-content">
				<input type="radio" class="followType" name="followType" value="A-2">&nbsp;行政班时间上课
			</div>
		</form>
	</div>
	<div class="layui-layer-btn">
		<a class="layui-layer-btn0" id="scheduling-commit">确定</a>
		<a class="layui-layer-btn1" id="scheduling-close">取消</a>
	</div>
</div>
<script type="text/javascript">
var setTimeClick;
$(function(){
	showBreadBack(toBackDivideIndex,false,"分班安排");
	<#if isDivideNow>
		setTimeClick=setTimeout("refeshParmA1()",30000);
	</#if>

	// 取消按钮操作功能
	$("#scheduling-close").on("click", function () {
		doLayerOk("#scheduling-commit", {
			redirect: function () {
			},
			window: function () {
				layer.closeAll()
			}
		});
		$(".followType:checked").val("");
	});

	// 确定按钮操作功能

	$("#scheduling-commit").on("click", function () {
		$("#followType").val($(".followType:checked").val());
		saveGroup();
	});
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

function toBackDivideIndex(){
	var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
	$("#showList").load(url);
}

function toBack(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/xkSingle/page';
	$("#showList").load(url);
}
function nextfun(){
	//保存数据
	<#if canEdit>
		saveGroup();
	<#else>
		<#if !isDivideNow>
			//进入next
			showResultA();
		</#if>
	</#if>
}

function showResultA(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultA/page';
	$("#showList").load(url);
}

<#--分班中自动刷新-->
function refeshParmA1(){
	if(document.getElementById("parmA")){
		refeshParmA();
	} else {
		clearTimeout(setTimeClick);
	}
}
function refeshParmA(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/parametersetA';
	$("#showList").load(url);
}

var saveBtn = '.nextStep-btn';
var isSubmit=false;
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
		var classNum = parseInt($(this).val());
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
		var low = (parseInt(aveNum) - parseInt(floNum)) * classNum;
		var high = (parseInt(aveNum) + parseInt(floNum)) * classNum;
		if (stuNum < low) {
			layer.tips("班级数量太多", $(this), {
				tipsMore: true,
				tips:3
			});
			flag=false;
			return;
		}
		if (stuNum > high) {
			layer.tips("班级数量太少", $(this), {
				tipsMore: true,
				tips:3
			});
			flag=false;
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
	if (!$("#followType").val()) {
		$(saveBtn).removeClass("disabled");
		isSubmit = false;
		if ($("#beforeNext").is(':hidden')) {
			$("#beforeNext").removeClass("hide");
		}
		layer.open({
			type: 1,
			skin: 'layui-layer-demo',
			closeBtn: 0,
			shift: 2,
			shadeClose: false,
			maxmin: false,
			scrollbar: false,
			title: '物理与历史选考上课时间设置',
			area: 'auto',
			content: $("#beforeNext")
		});
		$(".layui-layer").focus();
		return;
	}
	
	// 提交数据
	var options = {
	    url:'${request.contextPath}/newgkelective/${divideId!}/singleList/threeOneTwo/save?subjectType=A',
	    dataType : 'json',
	    type:'post',  
	    cache:false, 
	    success:function(data) {
	    	var jsonO = data;
	    	if(jsonO.success){
	 			layer.closeAll();
	    		autoSingleAllClass('${divideId!}');
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

var isSingleAuto=false;
function autoSingleAllClass(divideId){
	if(isSingleAuto){
		return;
	}
	isSingleAuto=true;
	var text='<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">'
				+'正在分班中，请稍等．．．';
	$("#showMessId").html(text);
	//分班代码
	autoSingleAllClass2(divideId,"1");
}

function autoSingleAllClass2(divideId,indexStr){
    var urlStr="${request.contextPath}/newgkelective/"+divideId+"/divideClass/autoShuff";
    var weight=$("#weight").val();
    var data1={"divideId":divideId,"subjectType":"A"};
    if(weight!=""){
    	data1={"divideId":divideId,"subjectType":"A","weight":weight};
    }
	$.ajax({
		url:urlStr,
		data:data1,
		dataType: "json",
		success: function(data){
			if(data.stat=="success"){
				//进入下一步结果
				showResultA();
 			}else if(data.stat=="error"){
 				if(indexStr=="1"){
 					//上次失败进入分班
 					autoSingleAllClass2(divideId,"0");
 				}else{
 					if(setTimeClick){
 						clearTimeout(setTimeClick);
 					}
 					$(saveBtn).removeClass("disabled");
	 				isSingleAuto=false;
	 				$("#showMessId").html(data.message);
 				}
 			}else{
 				//访问结果
 				refeshParmA();
 				
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
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
	if((aveNum<1) || (floNum!=null && floNum!="" && aveNum<=floNum)){
		layer.tips("班级数不合理请重新设置", "#"+index+"_inputCl", {
			tipsMore: true,
			tips:3		
		});
		aveNumObj.val("");
		return;
	}
	aveNumObj.val(aveNum);
}

var isAllMove=false;
function clearNext(){
	if(isAllMove){
		return;
	}
	isAllMove=true;
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("是否确定重新安排选考",options,function(){
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearJxb',
			data:{"subjectType":"A"},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			refeshParmA();
		 		}
		 		else{
		 			isAllMove=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
			
	},function(){
		isAllMove=false;
	});
	isAllMove=false;
}


</script>