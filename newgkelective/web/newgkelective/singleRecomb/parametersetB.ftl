<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>选考分层</span></li>
		<li class="active"><span><i>2</i>分选考班</span></li>
		<li class="active"><span><i>3</i>选考班结果</span></li>
		<li class="active"><span><i>4</i>分学考班</span></li>
		<li><span><i>5</i>学考班结果</span></li>
	</ul>
</div>
<div class="row">
	<div class="col-xs-12" id="parmB">
		<input type="hidden" id="weight" value="10">
		<form id="teachForm">
			<input type="hidden" name="subjectType" value="B">
			<input type="hidden" name="followType" id="followType" value="${divide.followType?default('B-1')}">
			<div class="box box-default">
			<div class="box-body">
				<#if xzbList?exists && xzbList?size gt 0>
				<h4 class="form-title">
					<b class="mr20">物理与历史</b>
					<label class="pos-rel color-blue font-12 no-margin">
						<input id="isFollow" type="checkbox" class="wp" name="isFollow" value="${divide.followType?default('B-1')}"  <#if followType?default('B-1')?index_of('B-1') gt -1>checked</#if> <#if !canEdit>disabled <#else> onchange="switchFollow()"</#if>>
						<span class="lbl"> 跟随选考班分班</span>
					</label>
					<span class="font-12 color-999 ml10">(例：物理学考班对应的为历史选考班)</span>
				</h4>
				<#if jxbList?exists>
					<#assign jxbSize = jxbList?size>
				<#else>
					<#assign jxbSize = 0>
				</#if>
				<div class="row optionGroupArrangeList">
					<#list xzbList as dto>
					<div class="col-xs-6">
						<input type="hidden" name="exList[${dto_index+jxbSize}].id" value="${dto.id!}">
						<input type="hidden" name="exList[${dto_index+jxbSize}].divideId" value="${divideId!}">
						<input type="hidden" name="exList[${dto_index+jxbSize}].subjectId" value="${dto.subjectId!}">
						<input type="hidden" name="exList[${dto_index+jxbSize}].subjectType" value="${dto.subjectType!'B'}">
						<input type="hidden" name="exList[${dto_index+jxbSize}].range" value="${dto.range!}">
						<input type="hidden" id="${dto_index+jxbSize}_leastNum" name="exList[${dto_index+jxbSize}].leastNum" value="${dto.leastNum!}">
						<input type="hidden" id="${dto_index+jxbSize}_maximum" name="exList[${dto_index+jxbSize}].maximum" value="${dto.maximum!}">
						<div class="single-subject num-input" groupIndex="${dto_index+jxbSize}">
							<div class="hd">
								<img src="${request.contextPath}/static/images/7choose3/${dto.pngName?default('chemistry')}.png">
								<span class="title">${dto.subjectName!}</span>
							</div>
							<div class="bd">
								<div class="mt20 total" total="${dto.stuNum}">总数：${dto.stuNum}人</div>
								<div class="follow"  <#if !(followType?default('B-1')?index_of('B-1') gt -1)>style="display: none"</#if>>
									<div class="mt30">已跟随行政班分班</div>
									<img class="lock" src="${request.contextPath}/static/images/7choose3/lock.png">
								</div>
								<#assign ex=dto.exList[0]>
								<#assign anum=-1>
								<#assign bnum=-1>
								<#if ex.maximum?exists && ex.leastNum?exists>
									<#assign anum=((ex.maximum+ex.leastNum)/2)>
									<#assign bnum=((ex.maximum-ex.leastNum)/2)>
								</#if>
								<div class="filter mt25 unFollow divide-filter-block" id=""  <#if followType?default('B-1')?index_of('B-1') gt -1>style="display: none"</#if>>
									<div class="filter-item">
										<span class="filter-name">分班数：</span>
										<div class="filter-content">
											<div class="input-group form-num form-num-sm float-left w70">
												<input class="form-control divide-class-num" type="text" onblur="refreshDivideClassNum(this)" nullable="false" vtype="int" maxlength="2" max="${dto.stuNum?default(1)}" min="0" name="exList[${dto_index+jxbSize}].classNum" value="${ex.classNum?default('')}">
												<span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
											</div>
										</div>
									</div>
									<div class="filter-item no-margin">
										<span class="filter-name">每班人数：</span>
										<div class="filter-content">
											<div class="input-group form-num form-num-sm float-left w70">
												<input id="${dto_index+jxbSize}_aveNum" class="form-control divide-filter-num" nullable="false" maxlength="3" vtype="int" <#if !canEdit>readOnly="readOnly"</#if> max="${dto.stuNum?default(1)}" min="0" type="text" <#if anum?default(-1)!=-1>value="${anum?string('0')}"</#if>>
												<span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
											</div>
											<span class="float-left ml5 mr5 mt3">±</span>
											<div class="input-group form-num form-num-sm float-left w70">
												<input id="${dto_index+jxbSize}_floNum" class="form-control" nullable="false" maxlength="2" vtype="int" <#if !canEdit>readOnly="readOnly"</#if> max="${dto.stuNum?default(1)}" min="0" type="text" <#if bnum?default(-1)!=-1>value="${bnum?string('0')}"</#if>>
												<span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					</#list>
				</div>
				</#if>
				<#if jxbList?exists && jxbList?size gt 0>
				<h4 class="form-title">
					<b>走班科目</b>
				</h4>
				<div class="row groupArrangeList">
					<#list jxbList as dto>
					<div class="col-xs-6">
						<input type="hidden" name="exList[${dto_index!}].id" value="${dto.id!}">
						<input type="hidden" name="exList[${dto_index!}].divideId" value="${divideId!}">
						<input type="hidden" name="exList[${dto_index!}].subjectId" value="${dto.subjectId!}">
						<input type="hidden" name="exList[${dto_index!}].subjectType" value="${dto.subjectType!'B'}">
						<input type="hidden" name="exList[${dto_index!}].range" value="${dto.range!}">
						<input type="hidden" id="${dto_index!}_leastNum" name="exList[${dto_index!}].leastNum" value="${dto.leastNum!}">
						<input type="hidden" id="${dto_index!}_maximum" name="exList[${dto_index!}].maximum" value="${dto.maximum!}">
						<div class="single-subject num-input" groupIndex="${dto_index}">
							<div class="hd">
								<img src="${request.contextPath}/static/images/7choose3/${dto.pngName?default('chemistry')}.png">
								<span class="title">${dto.subjectName!}</span>
							</div>
							<div class="bd">
								<div class="mt20 total" total="${dto.stuNum}">总数：${dto.stuNum}人</div>
								<#assign ex=dto.exList[0]>
								<#assign anum=-1>
								<#assign bnum=-1>
								<#if ex.maximum?exists && ex.leastNum?exists>
									<#assign anum=((ex.maximum+ex.leastNum)/2)>
									<#assign bnum=((ex.maximum-ex.leastNum)/2)>
								</#if>
								<div class="filter mt25 divide-filter-block">
									<div class="filter-item">
										<span class="filter-name">分班数：</span>
										<div class="filter-content">
											<div class="input-group form-num form-num-sm float-left w70">
				                                <input class="form-control divide-class-num" onblur="refreshDivideClassNum(this)" nullable="false" vtype="int" maxlength="2" <#if !canEdit>readOnly="readOnly"</#if> max="${dto.stuNum?default(1)}" min="0" type="text" name="exList[${dto_index}].classNum" value="${ex.classNum?default('')}">
				                                <span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
				                            </div>
										</div>
									</div>
									<div class="filter-item no-margin">
										<span class="filter-name">每班人数：</span>
										<div class="filter-content">
											<div class="input-group form-num form-num-sm float-left w70">
				                                <input id="${dto_index}_aveNum" class="form-control divide-filter-num" nullable="false" maxlength="3" <#if !canEdit>readOnly="readOnly"</#if> vtype="int" max="${dto.stuNum?default(1)}" min="0" type="text" <#if anum?default(-1)!=-1>value="${anum?string('0')}"</#if>>
				                                <span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
				                            </div>
				                            <span class="float-left ml5 mr5 mt3">±</span>
				                            <div class="input-group form-num form-num-sm float-left w70">
				                                <input id="${dto_index}_floNum" class="form-control" nullable="false" vtype="int" maxlength="2" <#if !canEdit>readOnly="readOnly"</#if> max="${dto.stuNum?default(1)}" min="0" type="text" <#if bnum?default(-1)!=-1>value="${bnum?string('0')}"</#if>>
				                                <span class="input-group-btn">
				                                    <button class="btn btn-default count-up" type="button">
				                                    	<i class="fa fa-angle-up"></i>
				                                    </button>
				                                    <button class="btn btn-default count-down" type="button">
				                                    	<i class="fa fa-angle-down"></i>
				                                    </button>
				                                </span>
				                            </div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					</#list>
				</div>
				</#if>
			</div>
		</div>
		</form>
	</div>
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

<script>
	var setTimeClick;
	$(function () {
		<#if canEdit>
		$(".count-up").on("click", function () {
			var tmp = $(this).parent().prev();
			if (!parseInt(tmp.val())) {
				tmp.val('0')
			}
			tmp.val(parseInt(tmp.val()) + 1);
			refreshDivideClassNum(this);
		});

		$(".count-down").on("click", function () {
			var tmp = $(this).parent().prev();
			if (!parseInt(tmp.val())) {
				tmp.val('0')
			}
			if (parseInt(tmp.val()) == 0) {
				return;
			}
			tmp.val(parseInt(tmp.val()) - 1);
			refreshDivideClassNum(this);
		});
		</#if>
		<#if isDivideNow>
			setTimeClick=setTimeout("refeshParmB1()",30000);
		</#if>
	});

	function refreshDivideClassNum(obj) {
		var total = parseInt($(obj).parents(".bd").find(".total").attr("total"));
		var classNum;
		if (parseInt($(obj).parents(".bd").find(".divide-class-num").val())) {
			classNum = parseInt($(obj).parents(".bd").find(".divide-class-num").val());
		} else {
			return;
		}
		var studentPerClass = Math.round(total / classNum);
		$(obj).parents(".bd").find(".divide-filter-num").val(studentPerClass);
	}

	function nextfun() {
		//保存数据
		<#if canEdit>
			saveTeachGroup();
		<#else>
			<#if !isDivideNow>
				showResultB();
			</#if>
		</#if>
	}
	
	
	function refeshParmB1(){
		if(document.getElementById("parmB")){
			refeshParmB();
		} else {
			clearTimeout(setTimeClick);
		}
	}
	
	function refeshParmB(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/parametersetB';
		$("#showList").load(url);
	}
	
	function showResultB() {
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultB/page';
		$("#showList").load(url);
	}

	function toBack() {
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultA/page';
		$("#showList").load(url);
	}

	function switchFollow() {
		if ($("#isFollow").prop("checked")) {
			$("#isFollow").val("B-1");
			$("#followType").val("B-1");
			$(".follow").show();
			$(".unFollow").hide();
		} else {
			$("#isFollow").val("B-0");
			$("#followType").val("B-0");
			$(".follow").hide();
			$(".unFollow").show();
		}
	}

	var saveBtn = '.nextStep-btn';
	var isSubmit = false;

	function saveTeachGroup() {
		if (isSubmit) {
			return;
		}
		isSubmit = true;
		$(saveBtn).addClass("disabled");
		var check;
		if (!$("#isFollow").prop("checked")) {
			check = checkValue('.optionGroupArrangeList');
			if (!check) {
				$(saveBtn).removeClass("disabled");
				isSubmit = false;
				return;
			}
		}
		check = checkValue('.groupArrangeList');
		if (!check) {
			$(saveBtn).removeClass("disabled");
			isSubmit = false;
			return;
		}
		var flag = true;
		$('.num-input').each(function () {
			var order = $(this).attr('groupIndex');
			var classNum = parseInt($(this).find(".divide-class-num").val());
			var aveNum = $("#" + order + "_aveNum").val();
			var floNum = $("#" + order + "_floNum").val();
			var stuNum = $(this).find(".total").attr("total");
			if (aveNum - floNum < 0) {
				flag = false;
				layer.tips("浮动值不合理请重新设置", "#" + order + "_floNum", {
					tipsMore: true,
					tips: 3
				});
				$("#" + order + "_floNum").val("");
				return;
			}
			var low = (parseInt(aveNum) - parseInt(floNum)) * classNum;
			var high = (parseInt(aveNum) + parseInt(floNum)) * classNum;
			if (stuNum < low) {
				layer.tips("班级数量太多", $(this).find(".divide-class-num"), {
					tipsMore: true,
					tips:3
				});
				flag=false;
				return;
			}
			if (stuNum > high) {
				layer.tips("班级数量太少", $(this).find(".divide-class-num"), {
					tipsMore: true,
					tips:3
				});
				flag=false;
				return;
			}
			$("#" + order + "_leastNum").val(aveNum - floNum);
			$("#" + order + "_maximum").val(parseInt(aveNum) + parseInt(floNum));
		});
		if (!flag) {
			$(saveBtn).removeClass("disabled");
			isSubmit = false;
			return;
		}

		// 提交数据
		var options = {
			url: '${request.contextPath}/newgkelective/${divideId!}/singleList/threeOneTwo/save',
			dataType: 'json',
			type: 'post',
			cache: false,
			success: function (data) {
				var jsonO = data;
				if (jsonO.success) {
					layer.closeAll();
					autoSingleAllClass('${divideId!}');
				} else {
					layerTipMsg(jsonO.success, "失败", jsonO.msg);
					$(saveBtn).removeClass("disabled");
					isSubmit = false;
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
			}//请求出错
		};
		$('#teachForm').ajaxSubmit(options);
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

	autoSingleAllClass2(divideId,"1");
}

function autoSingleAllClass2(divideId,indexStr){
    var urlStr="${request.contextPath}/newgkelective/"+divideId+"/divideClass/autoShuff";
    var weight=$("#weight").val();
    var data1={"divideId":divideId,"subjectType":"B"};
    if(weight!=""){
    	data1={"divideId":divideId,"subjectType":"B","weight":weight};
    }
	$.ajax({
		url:urlStr,
		data:data1,
		dataType: "json",
		success: function(data){
			if(data.stat=="success"){
				//进入下一步结果
				showResultB();
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
	 				isSubmit = false;
	 				$("#showMessId").html(data.message);
 				}
 			}else{
 				//访问结果
 				refeshParmB();
 				
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}


var isAllMove=false;
function clearNext(){
	if(isAllMove){
		return;
	}
	isAllMove=true;
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("是否确定重新安排学考",options,function(){
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearJxb',
			data:{"subjectType":"B"},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			refeshParmB();
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