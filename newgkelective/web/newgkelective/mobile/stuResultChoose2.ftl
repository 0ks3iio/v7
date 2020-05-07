<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>新高考选课</title>
	<link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<style> 
	/*html {height: 100%;overflow: hidden;}*/
	.mui-bar-tab~.mui-content {padding-bottom: 90px;}
	.mui-popup-text {text-align: left;max-height: 380px;overflow-y: auto;}
	.layer-fixed {height: 100%;overflow: hidden;}
</style>
<body class="mui-bg-white">
<#--
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">${choice.choiceName!}</h1>
</header>
-->
<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none"></a>
	<nav class="mui-bar mui-bar-tab">
		<div>
			<a class="mui-tab-item f-16 mui-active js-submitBack" href="#" style="background: #2f7bff;color: #fff;">上一步</a>
			<a class="mui-tab-item f-16 mui-active js-submitCourse" href="#" style="background: #2f7bff;color: #fff;">确认选课</a>
		</div>
	</nav>
	<div class="mui-content mui-bg-white" style="padding-top:0px;" id="js-content-wrap">
		<div class="tab01Container">
			<div class="tabMenu01 tab_menu">
                <ul class="fn-flex">
                    <li class="mainlevel fn-flex-auto active"><a class="mainlevel_a" href="javascript:;">学生选课</a></li>
		        	<#if isOpen?default(false)>
                    <li class="mainlevel fn-flex-auto"><a class="mainlevel_a" href="javascript:;">选课统计</a></li>
		       		</#if>
                </ul>
            </div>
		    <div class="tabBox01 tab_box">
                <!-- 学生选课 开始 -->
                <div>
                	<ul class="mui-style01 mui-table-view mui-table-view-chevron">
						<li class="mui-table-view-cell mui-media">
							<a class="mui-navigate-right">
								<span class="alertBtn mui-message f-17 c-999">说明</span>
								<div class="mui-media-body">
									<p class="mui-ellipsis f-15 c-333">选课时间：</p>
									<p class="mui-ellipsis f-14 c-333">${(choice.startTime?string('yyyy-MM-dd HH:mm'))?if_exists}至 ${(choice.endTime?string('yyyy-MM-dd HH:mm'))?if_exists}</p>
								</div>
							</a>
						</li>
					</ul>
					<div class="mui-bg-g"></div>
					<div class="box-selectGroup">
						<div class="box-selectGroup-header">已选科目</div>
						<div class="box-selectGroup-body">
							<div class="box-selectCourse">
								<div class="box-body mui-km-course mui-adjust-course">
									<ul class="gk-course-list  gk-course-list-show gk-selectOne">
										<#if courseNameMap?exists && resultList?exists && (resultList?size gt 0)>
										<#list resultList as item>
										<li id="${item.subjectId!}" class="mui-row">
											<a class="mui-col-xs-3" href="javascript:;">
												<h3 class="gk-course-name">${courseNameMap[item.subjectId!]!}</h3>
											</a>
											<div class="mui-col-xs-1"></div>
											<div class="mui-col-xs-8 mui-row adjust-btn">
												<a class="mui-col-xs-4 <#if item.subjectType?default('1')=='1'>active</#if>" href="javascript:;" value="1">不可调剂</a>
												<a class="mui-col-xs-4 <#if item.subjectType?default('1')=='2'>active</#if>" href="javascript:;" value="2">可调剂</a>
												<a class="mui-col-xs-4 <#if item.subjectType?default('1')=='3'>active</#if>" href="javascript:;" value="3">优先调剂</a>		
											</div>
										</li>
										</#list>
										</#if>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id="isWantToSubject">
					<div class="mui-bg-g"></div>
					<div class="box-selectGroup">
						<div class="box-selectGroup-header">优先调剂到<span class="color-gray">（非必选）</span></div>
						<div class="box-selectGroup-body">
							<div class="box-selectCourse">
								<div class="box-body mui-km-course">
									<ul class="gk-course-list" id="wantToSubject">
										<#if courseNameMap?exists && courseChoList?exists && (courseChoList?size gt 0)>
										<#list courseChoList as item>
										<li class="mui-col-xs-3" id="want_${item!}" data-reverse="nowant_${item!}" data-value="${item!}">
											<a href="javascript:;">
												<h3 class="gk-course-name">${courseNameMap[item]!}</h3>
											</a>
										</li>
										</#list>
										</#if>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div class="mui-bg-g"></div>
					<div class="box-selectGroup">
						<div class="box-selectGroup-header">明确不选<span class="color-gray">（非必选）</span></div>
						<div class="box-selectGroup-body">
							<div class="box-selectCourse">
								<div class="box-body mui-km-course">
									<ul class="gk-course-list" id="noWantToSubject">
										<#if courseNameMap?exists && courseChoList?exists && (courseChoList?size gt 0)>
										<#list courseChoList as item>
										<li class="mui-col-xs-3" id="nowant_${item!}" data-reverse="want_${item!}" data-value="${item!}">
											<a href="javascript:;">
												<h3 class="gk-course-name">${courseNameMap[item]!}</h3>
											</a>
										</li>
										</#list>
										</#if>
									</ul>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 学生选课 结束 -->
                <!-- 选课统计 开始 -->
				<#if isOpen?default(false)>
                <div>
                	<div class="mui-bg-g"></div>
		            <div id="echarts1" style=" height: 900px"></div>
                </div>                     
		        </#if>
                <!-- 选课统计 结束 -->
			</div>
		</div>
	</div>
<div id="alertBtn" style="display:none">
	<div class="mui-popup-title pa-15">选课说明</div>
	<div class="mui-popup-text pa-15">${choice.notice!}</div>
</div>
</body>
<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/mui/js/layer/layer.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js" async="async" defer="defer"></script>
<script src="${request.contextPath}/static/mui/js/common.js" async="async" defer="defer"></script>
<#if isOpen?default(false)>
<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
</#if>
<script> 
mui.init();

var hasSaved = false;
var isMaster;
$('#cancelId').off('click').on('click',function(){
	var url = '${request.contextPath}/mobile/open/newgkelective/homepage?studentId=${studentId!}'; 
	if(${chooseNum?default(0)}!=0 || hasSaved){
		url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/page?choiceId=${choiceId!}&isMaster='+isMaster;		
	}
	loadByHref(url);
});

$(function(){
		
		$('.mui-tab-item').on('touchstart', function(e){
    		e.stopPropagation();
    	});
		
		var singleItem = $('.gk-selectOne .gk-course-name');
       	var submitCourse = $('.js-submitCourse');
       	var submitBack = $('.js-submitBack');
       	
       	submitBack.on('click', function(e){
			e.preventDefault();
			var sids = '';
			singleItem.each(function(){
				var sid = $(this).parents('li').attr('id');
				if(sids != ''){
					sids+=',';
				}
				sids+=sid;
			})
			
			var url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/page?choiceId=${choiceId!}&toEdit=1&subjectIds='+sids;
			loadByHref(url);
		})
       
		// 提交选课
		var isSubmit=false;
		submitCourse.on('click', function(e){
			e.preventDefault();
			if(isSubmit){
				return false;
			}
			isSubmit=true;

			var sids = '';
			var stypes = '';
        	singleItem.each(function(){
				var sid = $(this).parents('li').attr('id');
				if(sids != ''){
					sids+=',';
				}
				sids+=sid;
				var stype = $(this).parents('li').find('.active').attr("value");
				if(stype != ''){
					stype+=',';
				}
				stypes+=stype;	
			});	
			
			var wantToIds = new Array();
			var noWantToIds = new Array();
			$("#wantToSubject li.selected").each(function(){
				wantToIds.push($(this).attr("data-value"));
			})
			wantToIds = wantToIds.join(",");
			$("#noWantToSubject li.selected").each(function(){
				noWantToIds.push($(this).attr("data-value"));
			})
			noWantToIds = noWantToIds.join(",");
            $.ajax({
				url:'${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/save',
				data: {'choiceId':'${choiceId!}','subjectIds':sids,'subjectTypes':stypes,'wantToIds':wantToIds,'noWantToIds':noWantToIds},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
					isSubmit=false;
					if(jsonO.success){
						hasSaved = true;
						toastMsg('选课成功！');
						isMaster=1;
						$("#cancelId").click();
					}else{
						toastMsg(jsonO.msg);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		})
		
		//是否调剂
		$(".adjust-btn a").click(function(e){
			e.preventDefault();
			$(this).parent().children().removeClass("active");
			$(this).addClass("active");
			var flag=true;
			$(this).parents("li").siblings().find(".adjust-btn a.active").each(function(){
				if($(this).attr("value")!="1"){
					flag=false;
				}
			})
			if(flag){
				if($(this).attr("value")=="1"){
					initNoWant();
				}else{
					initWant();
				}
			}	
		})
		
		//tab切换
		var $div_li1=$(".tab_menu > ul > li");
		$div_li1.click(function(){
			$(this).addClass("active").siblings().removeClass("active");
			var index=$div_li1.index(this);
			$(".tab_box > div").eq(index).show().siblings().hide();
			initCount();
		});
		
		$("#isWantToSubject").find("li").on("click",function(){
			if($(this).hasClass('disabled')) return;
			if($(this).hasClass("selected")){
				$(this).removeClass("selected");
				$("#"+$(this).attr("data-reverse")).removeClass("disabled");
			}else{
				$(this).addClass("selected");
				$("#"+$(this).attr("data-reverse")).addClass("disabled");
			}
		})
		
		function initNoWant(){
			$("#isWantToSubject").find("li").removeClass("selected").addClass("disabled");
		}
		
		function initWant(){
			$("#isWantToSubject").find("li").removeClass("disabled");
		}
		
		
		$(".alertBtn").on("click",function(){
		    layer.open({
			    type:1,
				title:false,
				content:$('#alertBtn'),
				area:'90%',
				btn:['知道了'],
				shadeClose:true,
				scrollbar:false
			});
		});
		
		initData();
		
		initCount();
});

function initData(){
	<#if wantToSubjectList?exists && wantToSubjectList?size gt 0>
	<#list wantToSubjectList as item>
		$("#want_${item.subjectId!}").addClass("selected");
		$("#nowant_${item.subjectId!}").addClass("disabled");
	</#list>
	</#if>
	
	<#if noWantToSubjectList?exists && noWantToSubjectList?size gt 0>
	<#list noWantToSubjectList as item>
		$("#nowant_${item.subjectId!}").addClass("selected");
		$("#want_${item.subjectId!}").addClass("disabled");
	</#list>
	</#if>
	
	if(($(".adjust-btn a.active[value!=1]").length==0)){
		$("#isWantToSubject").find("li").addClass("disabled");
	}
}

function initCount(){            
	if(document.getElementById("echarts1")) {
	    // 基于准备好的dom，初始化echarts实例
		var myChart1 = echarts.init(document.getElementById("echarts1"));
		window.addEventListener('resize',function(){myChart1.resize();},false);
		var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
	    var legendData=jsonStringData.legendData;
	    var loadingData=jsonStringData.loadingData;
	    // 指定图表的配置项和数据
		var option1 = {
			grid: {
		        left: '3%',
		        containLabel: true
		    },
			xAxis: {
				type: 'value',
				splitLine: {
					show: false
				},
				axisLine:{
					show:false
				},
				axisTick:{
					show:false
				},
				axisLabel:{
					show:false
				}
			},
			yAxis: {
				type: 'category',
				data: legendData,
				splitLine: {
					show: false
				},
				axisTick:{
					show:false
				},
				axisLine:{
					show:false
				},
				axisLabel: {  
	                interval: 0
	            }
			},
			series: [
				{
					type: 'bar',
					itemStyle: {
						normal: {
							color: function (params){
								var colorList = ['rgb(84,187,244)','rgb(118,198,31)','rgb(175,203,17)','rgb(235,216,0)','rgb(249,175,18)','rgb(245,155,89)','rgb(242,107,107)','rgb(223,121,217)','rgb(138,121,223)','rgb(84,187,244)','rgb(118,198,31)','rgb(175,203,17)','rgb(235,216,0)','rgb(249,175,18)','rgb(245,155,89)','rgb(242,107,107)','rgb(223,121,217)','rgb(138,121,223)','rgb(84,187,244)','rgb(118,198,31)','rgb(175,203,17)','rgb(235,216,0)','rgb(249,175,18)','rgb(245,155,89)','rgb(242,107,107)','rgb(223,121,217)','rgb(138,121,223)','rgb(84,187,244)','rgb(118,198,31)','rgb(175,203,17)','rgb(235,216,0)','rgb(249,175,18)','rgb(245,155,89)','rgb(242,107,107)','rgb(223,121,217)'];
		                        return colorList[params.dataIndex];
		                    }
						}
					},
					label: {
		                normal: {
		                    show: true,
		                    position: 'right'
		                }
		            },
					data: loadingData,
					barWidth:20
				}
			]
		};
	    // 使用刚指定的配置项和数据显示图表
		myChart1.setOption(option1);
	}
}
$(function(){
	<#if isOpen?default(false)>
		$("#echarts1").parent("div").hide();
	</#if>
})        
</script>	

</html>