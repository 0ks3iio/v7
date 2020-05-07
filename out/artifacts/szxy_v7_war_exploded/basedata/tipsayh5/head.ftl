<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
</head>
<body>
	<#--
    <header class="mui-bar mui-bar-nav" style="display:none;">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"></a>
        <h1 class="mui-title"><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>管理</h1>
    </header>
    -->
     <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none"></a>		
    <div class="tabMenu01 tab_menu" style="position:fixed;z-index:10;left:0;right:0;">
        <ul class="fn-flex">
            <li class="mainlevel fn-flex-auto statButton <#if statType?default('0')=='0'>active</#if> " data-value="0"><a class="mainlevel_a" href="#" >待审核</a></li>
            <li class="mainlevel fn-flex-auto statButton <#if statType?default('0')=='1'>active</#if> " data-value="1"><a class="mainlevel_a" href="#" >已审核</a></li>
        </ul>
    </div>
    <div class="fn-flex select-container" style="top: 44px;">
    	<div class="fn-flex-auto year">
    		<span class="name" data-value="${semesterObj.acadyear?default('')}">${semesterObj.acadyear?default('未设置')}</span>
			<span class="mui-icon mui-icon-arrowdown"></span>
    	</div>
    	<div class="fn-flex-auto term">
    		<span class="name" data-value="${semesterObj.semester?default(1)}"><#if semesterObj.semester?default(1)==1>第一学期<#else>第二学期</#if></span>
			<span class="mui-icon mui-icon-arrowdown"></span>
    	</div>
    	<div class="fn-flex-auto week">
    		<#if nowWeek==0>
    			<span class="name" data-value="">全部</span>
    		<#else>
    			<span class="name" data-value="${nowWeek!}">第${nowWeek!}周</span>
    		</#if>
    		
			<span class="mui-icon mui-icon-arrowdown"></span>
    	</div>
	</div>
	<div class="option-container-layer" style="top: 86px;"></div>
	<div class="option-container" style="top: 86px;">
		<ul data-type="year" style="top: 86px;">
			<#if acadyearList?exists && acadyearList?size gt 0>
				<#list acadyearList as acad>
					<li><span data-value="${acad!}">${acad!}</span>
					<i <#if semesterObj.acadyear?default('')==acad> class="fa fa-check-circle" </#if> ></i>
					</li>
				</#list>
			<#else>
				<li><span data-value="">未设置</span><i class="fa fa-check-circle"></i></li>		
			</#if>
		</ul>
		<ul data-type="term" style="top: 86px;">
			<li><span data-value="1">第一学期</span>
			<i <#if semesterObj.semester?default(1)==1> class="fa fa-check-circle" </#if>></i>
			</li>
			<li><span data-value="2">第二学期</span>
			<i <#if semesterObj.semester?default(1)==2> class="fa fa-check-circle" </#if> ></i></li>			
		</ul>
		<ul data-type="week" style="top: 86px;">
			<li><span data-value="">全部</span><i <#if nowWeek==0>class="fa fa-check-circle" </#if>></i></li>
			<#if weekList?exists && weekList?size gt 0>
    		<#list weekList as week>
    			<li><span data-value="${week!}">第${week!}周</span>
    			<i <#if nowWeek==week>class="fa fa-check-circle" </#if>></i>
    			</li>
    		</#list>
    		</#if>	
		</ul>
	</div>
    <div class="mui-content" style="padding-top:85px;padding-bottom: 51px;" id="tipsayList">
    	<div class="mui-page-noData"> 
	    	 <i></i>
	    	 <p class="f-16">没有找到相关数据</p>
	    </div>
    </div>
    <nav class="mui-bar mui-bar-tab">
    	<#if adminType?default('0')=='0'>
        <a class="mui-tab-item mui-active f-16 apply_arrange" href="javascript:">申请教务安排</a>
        <a class="mui-tab-item mui-active f-16 apply_tipsay" href="javascript:">自主申请<#if type?default('')=='1'>代<#elseif type?default('')=='2'>管</#if>课</a>
        <#else>
        <a class="mui-tab-item mui-active f-16 add_tipsay" href="javascript:">教务安排</a>
        </#if>
    </nav>
    <div id="delete" class="mui-popover mui-popover-bottom mui-popover-action">
        <!-- 可选择菜单 -->
		<ul class="mui-table-view">
			<li class="mui-table-view-cell">
				<a href="javascript:" class="f-16 c-red deleteSubmit" id="yesBtn">确认删除</a>
			</li>
		</ul>
		<!-- 取消菜单 -->
		<ul class="mui-table-view">
			<li class="mui-table-view-cell">
				<a href="#delete" class="f-16 c-333" id="noBtn">取消</a>
			</li>
		</ul>
    </div> 
	
    <script type="text/javascript">
    var statType="${statType?default('0')}";
    $('#cancelId').off('click').on('click',function(){
		var url = '${request.contextPath}/mobile/open/adjusttipsay/modelList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}';
		loadByHref(url);
	});
	
	$(".statButton").click(function(){
		$(".statButton").removeClass("active");
		$(this).addClass("active");
		statType=$(this).attr("data-value");
		loadTipdatList();
	})
   
    $(function(){
    	// select
    	var select = $(".select-container div")
    	select.click(function(){
    		var index = select.index(this);
    		if($(this).parent().siblings(".option-container").children().eq(index).css("display") === "block"){
    			$(".option-container-layer").hide();
    			$(".option-container").hide();
    			$(".option-container ul").hide();
    		}else{
    			$(".option-container-layer").hide();
    			$(".option-container").hide();
	    		$(".option-container ul").hide();
				$(this).parent().siblings(".option-container-layer").show().siblings(".option-container").show().children().eq(index).show();
    		}
    	});
    	$(".option-container li").click(function(){
    		var option_type = $(this).parent().attr("data-type");
    		var text = $(this).children("span").text();
    		var dataValue = $(this).children("span").attr("data-value");
    		$(this).children("i").addClass("fa fa-check-circle").parent().siblings().children("i").removeClass("fa fa-check-circle");
    		$(this).parents(".option-container").siblings(".select-container").find("." + option_type).children(".name").text(text).attr("data-value",dataValue);
    		$(".option-container-layer").hide();
    		$(".option-container").hide();
    		$(".option-container ul").hide();
    		if(option_type=="week"){
    			loadTipdatList();
    		}else{
    			loadWeekList();
    		}
    		 
    	})
    	$(".option-container-layer").click(function(){
    		$(".option-container-layer").hide();
    		$(".option-container").hide();
    		$(".option-container ul").hide();
    	})
    	
    	loadTipdatList();
    })
    
    var oldAcadyear="";
    var oldSemester="";
    var oldWeek="";
    var oldStatType="";
    function loadTipdatList(){
    	var acadyear=$(".select-container .year .name").attr("data-value");
    	var semester=$(".select-container .term .name").attr("data-value");
    	var week=$(".select-container .week .name").attr("data-value");
    	if(oldAcadyear==acadyear && oldSemester==semester && oldWeek==week && oldStatType==statType){
    		return;
    	}
    	oldAcadyear=acadyear; 
    	oldSemester=semester;
    	oldWeek=week;
    	var url='${request.contextPath}/mobile/open/adjusttipsay/showTipsayList/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}&acadyear='+acadyear+'&semester='+semester+'&week='+week+'&statType='+statType;
    	$("#tipsayList").load(url);
    }
    
    function loadWeekList(){
    	var acadyear=$(".select-container .year .name").attr("data-value");
    	var semester=$(".select-container .term .name").attr("data-value");
    	var week=$(".select-container .week .name").attr("data-value");
    	var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}&acadyear='+acadyear+'&semester='+semester+'&week='+week+'&statType='+statType;
		loadByHref(url);
    }
    //详情
    $("#tipsayList").on('click','.tipsayItem',function(e){
    	e.preventDefault();
    	var tipsayId=$(this).parent().attr("data-value");
    	loadItem(tipsayId);
    })
    
    function loadItem(tipsayId){
    	var acadyear=$(".select-container .year .name").attr("data-value");
    	var semester=$(".select-container .term .name").attr("data-value");
    	var week=$(".select-container .week .name").attr("data-value");
    	var url = '${request.contextPath}/mobile/open/adjusttipsay/showTipsayItem/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}&acadyear='+acadyear+'&semester='+semester+'&week='+week+'&statType='+statType+'&tipsayId='+tipsayId;
		loadByHref(url);
    }
    
    var chooseId="";
    
    
    <#if adminType?default('0')=='1'>
	$(".add_tipsay").on('tap',function(){
		addApplyTipsay("3");
	})
	//不通过
	$("#tipsayList").on("click",".noAgreeTipsay",function(e){
		e.preventDefault();
		chooseId=$(this).attr("data-value");
		$("#yesBtn").attr("data-name","noAgree");
		$("#yesBtn").html("确认不通过");
		mui('#delete').popover('toggle');
	})
	//通过
	$("#tipsayList").on("click",".agreeTipsay",function(e){
		chooseId=$(this).attr("data-value");
		$("#yesBtn").attr("data-name","agree");
		$("#yesBtn").html("确认通过");
		mui('#delete').popover('toggle');
	})
	<#--去安排--->
	$("#tipsayList").on("click",".ArrangeTipsayTeacher",function(e){
		e.preventDefault();
		var tipsayId=$(this).attr("data-value");
		loadItem(tipsayId);
	})
	
	
	
	<#else>
		$(".apply_arrange").on('tap',function(){
			addApplyTipsay("1");
		})
		$(".apply_tipsay").on('tap',function(){
			addApplyTipsay("2");
		})
		
	</#if>
	function addApplyTipsay(applyType){
		var acadyear=$(".select-container .year .name").attr("data-value");
    	var semester=$(".select-container .term .name").attr("data-value");
    	var week=$(".select-container .week .name").attr("data-value");
		if(acadyear=="" || semester==""){
			toastMsg("请先去节假日设置");
			return;
		}

		var parm='&acadyear='+acadyear+'&semester='+semester+'&week='+week+'&applyType='+applyType+'&statType='+statType;
		var url = '${request.contextPath}/mobile/open/adjusttipsay/addApplyTipsay/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}'+parm;
		loadByHref(url);
	}
	
	$("#tipsayList").on("click",".deleteTipsay",function(e){
		e.preventDefault();
		chooseId=$(this).attr("data-value");
		$("#yesBtn").attr("data-name","delete");
		$("#yesBtn").html("确认撤销");
		mui('#delete').popover('toggle');
	})
	$(".deleteSubmit").click(function(e){
		e.preventDefault();
		if(chooseId!=""){
			var yesBtnType=$(this).attr("data-name");
			yesBtnFunction(chooseId,yesBtnType);
		}else{
			mui('#delete').popover('toggle');
		}
	})
	
	function yesBtnFunction(tipsayId,yesBtnType){
		if(yesBtnType=='delete'){
			deleteTipsay(tipsayId);
		}else if(yesBtnType=='noAgree'){
			agreeOrNot(tipsayId,"0");
		}else if(yesBtnType=='agree'){
			agreeOrNot(tipsayId,"1");
		}else{
			mui('#delete').popover('toggle');
		}
	}
	
	var agreeOrNotFlag=false;
	function agreeOrNot(tipsayId,state){
		if(agreeOrNotFlag){
			return;
		}
		agreeOrNotFlag=true;
		$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/agreeOrNot',
			data:{'tipsayId':tipsayId,'state':state,'teacherId':'${teacherId!}'},
			type:'post', 
			dataType:'json',
			success:function(jsonO){
				agreeOrNotFlag=false;
				if(jsonO.success){
					loadWeekList();
				}else{
					toastMsg(jsonO.msg);
				}
			}
		});
	}
	
	var deletedFlg=false;
	function deleteTipsay(tipsayId){
		if(deletedFlg){
			return;
		}
		deletedFlg=true;
		$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/deleteTipsay',
			data:{'tipsayId':tipsayId,'type':'${type!}'},
			type:'post', 
			dataType:'json',
			success:function(jsonO){
				deletedFlg=false;
				mui('#delete').popover('toggle');
				if(jsonO.success){
					loadWeekList();
				}else{
					toastMsg(jsonO.msg);
				}
			}
		});	
		
	}
    </script>
</body>
</html>
