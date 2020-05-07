<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<script>
var oldName = "${divideName!}";
var pass = true;
var checkMsg;
function modiName(obj,val){
	var newName = $.trim(val);
	if(newName == oldName){
		pass = true;
		return;
	}
	if(newName==''){
		layer.tips('名称不能为空！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '名称不能为空！';
		return;	
	}
	if(getLength(newName)>50){
		layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '名称内容不能超过50个字节（一个汉字为两个字节）！';
		return;	
	}
	
	
	var id = "create";
	$.ajax({
			url:'${request.contextPath}/newgkelective/${gradeId!}/goDivide/saveName',
			data: {'divideId':id,'divideName':newName},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
				  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
					oldName = newName;
					pass = true;
		 		}else{
		 			layer.tips(jsonO.msg,$(obj), {
						tipsMore: true,
						tips: 3
					});
					pass = false;
					checkMsg = jsonO.msg;
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
}

</script>
	<form id="divideForm">
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title" style="width:50%;">
			<i class="fa fa-edit" title="点击标题可编辑" data-toggle="tooltip" data-placement="bottom"></i>
			<input style="width:85%;" type="text" onblur="modiName(this,this.value)" name="ent.divideName" id="divideName"  class="table-input" value="${divideName!}">
		</h3>
	</div>
	<div class="box-body">
		<input type="hidden" name="ent.id" value="${newGkDivide.id!}">
		<input type="hidden" name="ent.gradeId" value="${newGkDivide.gradeId!}">
		
		<h4 class="form-title"><b>选课情况</b></h4>
		<input type="hidden" name="choiceId" id="choiceId" value=""/>
		<ul class="courseList clearfix">
			<#if newGkChoiceList?exists && newGkChoiceList?size gt 0>
			<#list newGkChoiceList as item>
				<li class="choiceId-class <#if chooseChiceId?default('')==item.id>active</#if>" data-value="${item.id!}">
					<div class="date">${(item.creationTime?string('yyyy-MM-dd HH:mm'))!}</div>
					<div class="title">${item.choiceName!}</div>
				</li>
			</#list>
			<#else>
				<div class="filter mt30">
					暂无可用数据
				</div>
			</#if>					
		</ul>							
		<h4 class="form-title mt30"><b>参考成绩</b></h4>
		<input type="hidden" name="referScoreId" id="referScoreId" value=""/>
		<ul class="courseList clearfix">
			<#if referScoreList?exists && referScoreList?size gt 0>
				<#list referScoreList as score>
				<li class="referScoreId-class <#if score_index==0>active</#if>" data-value="${score.id!}">
					<div class="title score"> ${score.name!}</div>
				</li>
				</#list>
			<#else>
				<div class="filter mt30">
					暂无可用数据
				</div>
			</#if>
		</ul>
		<h4 class="form-title mt30"><b>开班模式</b>
			<span class="font-14 color-999">需选择开班模式 <a class="js-setting-tip js-openType-tip" href=""><i class="fa fa-question-circle color-yellow"></i></a></span>
		</h4>
		<input type="hidden" name="openType" id="openType" value=""/>
		<ul class="courseList modelList clearfix">
			<li class="openType-class active" data-value="08">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model5.png">
				<div class="title mt3">智能组合分班</div>
			</li>
			
			<li class="openType-class" data-value="05">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model1.png">
				<div class="title mt3">全走单科分层模式</div>
			</li>
			
			<li class="openType-class" data-value="01">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model2.png">
				<div class="title mt3">全固定模式</div>
			</li>
			
			<li class="openType-class" data-value="02">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model3.png">
				<div class="title mt3">半固定模式</div>
			</li>
			<#-- 
			<li class="openType-class" data-value="06">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model4.png">
				<div class="title mt3">全手动模式</div>
			</li>
			-->
			<li class="openType-class" data-value="09">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model7.png">
				<div class="title mt3">3+1+2单科分层模式</div>
			</li>
			<li class="openType-class" data-value="10">
				<img class="float-left mr20" src="${request.contextPath}/static/images/7choose3/model8.png">
				<div class="title mt3">3+1+2组合固定模式</div>
			</li>
		</ul>
		<div class="background-fafafa border-color-e8e8e8 mt10 padding-20 type312">
			<input type="hidden"  name="recombination" id="recombination" value="0">
			<span>原行政班是否重组：</span>
			<button type="button" class="btn btn-sm btn-radius15 btn-blue noRem_0" onClick="noRemt('0')">重组</button>
			<button type="button" class="btn btn-sm btn-radius15 btn-default noRem_1"  onClick="noRemt('1')">不重组</button>
		</div>
		<h4 class="form-title mt30">
			<b>开班科目</b>
		</h4>
		<div id="openAbCourse" class="bg-blue mt20">
			<div class="form-horizontal mb15 mt25">
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right">选考：</label>
					<div class="col-sm-11">
						<input type="hidden" value="" name="courseA" id="courseA">
						<div class="publish-course" id="course-A">
						
						</div>
						</div>
					</div>
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right">学考：</label>
					<div class="col-sm-11">
						<input type="hidden" value="" name="courseB" id="courseB">
						<div class="publish-course" id="course-B">
						
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	</form>
	<div class="navbar-fixed-bottom opt-bottom">
    <a href="javascript:" class="btn btn-blue" id="divide-commit" onclick="saveDivide()">确定</a>
	</div>
	<div class="layer layer-setting-tip openType-tip">
			<div class="layer-cotnent">
				<p>
					<strong>智能组合分班：</strong>
					即遵循学生的选课志愿，行政班按照选考科目三科固定、定二走一、定一走二、混合开班多种模式自由搭配，选考学考科目尽量在原行政班中上课，减少学生走班数量。
				</p>
				<p>
					<strong>全走单科分层模式：</strong>
					即遵循学生的选课志愿，选考开设各种单科分层、并保留之前的行政班、学考科目走班（不分层）。
				</p>
				<p>
					<strong>全固定模式：</strong>
					即遵循学生的选课志愿，行政班按照选考科目，全部以三科固定及两科固定形式，重组行政班；学考科目走班。
				</p>
				<p>
					<strong>半固定模式：</strong>
					即遵循学生的选课志愿，行政班按照选考科目，以部分三科固定或两科固定形式重组行政班，其余选考全走班形式，学考科目走班。
				</p>
				<p>
					<strong>3+1+2单科分层模式:</strong>
					物理历史二选一模式下，行政班按物理历史重组或不重组，其余科目单科分层走班。
				</p>
				<p>
					<strong>3+1+2组合固定模式:</strong>
					物理历史二选一模式下，行政班按物理历史重组或不重组，其余两科按组合重新编排教学班。
				</p>
				      
			</div>
		</div>
		
		<div class="layer layer-settingtip openCourse-tip">
			<div class="layer-cotnent">
				<p>
					<strong>行政班科目：</strong>
					获取学校年级开设课程信息显示相应的必修课程（具体设置在：教务管理v7->教学计划->课程开设->年级课程开设）
				</p>
			</div>
		</div>
</div>
<script>
var chooseChoiceData;
$(function(){
	showBreadBack(toMyBack,false,"新增分班");
	initCourseSelectAB();
	$('.choiceId-class').on('click', function(){
		var chooseChoiceId=$(this).attr('data-value');
		$("#choiceId").val(chooseChoiceId);
		if($(this).hasClass('active')){
			
		}else{
			$('.choiceId-class').removeClass('active');
			$(this).addClass('active');
			findSubjectByChoiceId(chooseChoiceId);
		}
		
	})
	
	$('.referScoreId-class').on('click', function(){
		var choosereferScoreId=$(this).attr('data-value');
		$("#referScoreId").val(choosereferScoreId);
		if($(this).hasClass('active')){
		    $(this).removeClass('active');
            $("#referScoreId").val("");
		}else{
			$('.referScoreId-class').removeClass('active');
			$(this).addClass('active');
		}
		
	})
	
	
	$('.openType-class').on('click', function(){
		var chooseOpenType=$(this).attr('data-value');
		if($(this).hasClass('active')){
			
		}else{
			$('.openType-class').removeClass('active');
			$(this).addClass('active');
			$("#openType").val(chooseOpenType);
			//3+1+2模式
			changeABShow(chooseOpenType);
		}
	})

	
	findSubjectByChoiceId("${chooseChiceId?default('')}");
	// 开班模式提示
	$('.js-openType-tip').on('click', function(e){
		e.preventDefault();
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '开班模式说明',
			area: '520px',
			btn:'知道了',
			content: $('.openType-tip')
		})
	});
	
	$('.js-openCourse-tip').on('click', function(e){
		e.preventDefault();
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '行政班科目来源说明',
			area: '520px',
			btn:'知道了',
			content: $('.openCourse-tip')
		})
	});
	
})

	<#--选课id-->
	function makeChooseLiActive(liClassName,itemId){
		var chooseId="";
		$('.'+liClassName).each(function(){
			if($(this).hasClass("active")){
				chooseId=$(this).attr("data-value");
				return false;
			}
		})
		$("#"+itemId).val(chooseId);
	}

	function findSubjectByChoiceId(choiceId){
		if(choiceId==""){
			$("#course-A").html("");
			$("#course-B").html("");
			return;
		}
		//选择方式
		makeChooseLiActive("openType-class","openType");
		var openType=$("#openType").val();
		$.ajax({
			url:"${request.contextPath}/newgkelective/goDivide/chosenSubjectByChoiceId",
			data:{"choiceId":choiceId},
			dataType: "json",
			success: function(data){
				chooseChoiceData=data;
				changeABShow(openType);
			}
		});
		
	}
	
	function changeABShow(openType){
		if(openType=="09" || openType=="10"){
			$(".type312").show();
		}else{
			$(".type312").hide();
		}
		var courseList=chooseChoiceData.courseList;
		var twoCourseList=chooseChoiceData.twoCourseList;
		var arr=[];
		var htmlA="";
		var htmlB="";
		if(openType=="09" || openType=="10"){
			if(!twoCourseList){
				//不存在
				$("#course-A").html("");
				$("#course-B").html("");
				layerTipMsg(false,"失败",chooseChoiceData.msg);
				return ;
			}
			if(twoCourseList.length>0){
				for(var i = 0; i < twoCourseList.length; i ++){
					arr[twoCourseList[i].id]=twoCourseList[i].id;
					htmlA=htmlA+"<span class='active disabled'  data-value='"+twoCourseList[i].id+"'>"+twoCourseList[i].subjectName+"</span>";
					htmlB=htmlB+"<span class='active disabled'  data-value='"+twoCourseList[i].id+"'>"+twoCourseList[i].subjectName+"</span>";
				}
			}
		}
		
		
		if(courseList.length>0){
			for(var i = 0; i < courseList.length; i ++){
				if(!arr[courseList[i].id]){
					htmlA=htmlA+"<span class='active' data-value='"+courseList[i].id+"'>"+courseList[i].subjectName+"</span>";
					htmlB=htmlB+"<span class='active' data-value='"+courseList[i].id+"'>"+courseList[i].subjectName+"</span>";
				}
			}
		}
		$("#course-A").html(htmlA);
		$("#course-B").html(htmlB);
		initCourseSelectAB();
	}

	
	
	
	
	function initCourseSelectAB(){
		//7选3模式
		$('#openAbCourse .publish-course span').off('click').on('click', function(e){
			e.preventDefault();
			if($(this).hasClass('disabled')) return;
	
			if($(this).hasClass('active')){
				$(this).removeClass('active');
			}else{
				$(this).addClass('active');
			}
		});
		
	}
	
	function makeIds(spanParentId,inputId){
		var subjectIds="";
		$("#"+spanParentId+" span").each(function(){
			if($(this).hasClass("active") && (!$(this).hasClass("disabled"))){
				var subId=$(this).attr("data-value");
				subjectIds=subjectIds+","+subId;
			}
		})
		if(subjectIds!=""){
			subjectIds=subjectIds.substring(1);
		}
		$("#"+inputId).val(subjectIds);
	}

	function checkMyForm(){
		makeChooseLiActive("choiceId-class","choiceId");
		makeChooseLiActive("referScoreId-class","referScoreId");
		makeChooseLiActive("openType-class","openType");
		var choiceId=$("#choiceId").val();
		if(!choiceId || choiceId==""){
			layerTipMsg(false,"保存失败","请选择选课情况");
			return false;
		}

		var openType=$("#openType").val();
		if(!openType || openType==""){
			layerTipMsg(false,"保存失败","请选择开班模式");
			return false;
		}
		//7选3 上课科目组装
		makeIds("course-A","courseA");
		makeIds("course-B","courseB");
		if($("#courseA").val()=="" &&　$("#courseB").val()==""){
			layerTipMsg(false,"保存失败","请选择开班科目");
			return false;
		}
			
		
		var check = checkValue('#divideForm');
	    if(!check){
	        return false;
	    }
		return true;
	}
	var isSubmit=false;
	function saveDivide(){
		if(isSubmit){
			return;
		}
		
		if(!pass){
			layer.tips(checkMsg,$("#divideName"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		
		isSubmit=true;
		var check = checkMyForm();
	    if(!check){
	        $("#divide-commit").removeClass("disabled");
	        isSubmit=false;
	        return;
	    }
		var options = {
			url : "${request.contextPath}/newgkelective/goDivide/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#divide-commit").removeClass("disabled");
		 			isSubmit=false;
		 			return;
		 		}else{
		 			layer.closeAll();
		 			if("success"==jsonO.msg){
		 				layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
		 			}else{
		 				//setTimeout(function(){
							//var url =  '${request.contextPath}/newgkelective/'+jsonO.msg+'/divideClass/resultClassList?type=1';
							var url =  '${request.contextPath}/newgkelective/'+jsonO.msg+'/divideClass/item';
							$("#showList").load(url);
						//},500)
				
		 				//setTimeout(toDivideItem(jsonO.msg),500);
		 			}
					
				  	
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#divideForm").ajaxSubmit(options);
	}
	
	function toDivideItem(divideId){
		var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList?type=1';
		$("#showList").load(url);
	}
	function toMyBack(){
		var url =  '${request.contextPath}/newgkelective/${newGkDivide.gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}
	
	$(function(){
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});	
	});	
	
	function noRemt(tvalue){
		$("#recombination").val(tvalue);
		if(tvalue=='0'){
			$(".noRem_0").removeClass("btn-default");
			$(".noRem_0").addClass("btn-blue");
			$(".noRem_1").removeClass("btn-blue");
			$(".noRem_1").addClass("btn-default");
		}else{
			$(".noRem_1").removeClass("btn-default");
			$(".noRem_1").addClass("btn-blue");
			$(".noRem_0").removeClass("btn-blue");
			$(".noRem_0").addClass("btn-default");
		}
	}
</script>