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
	<nav class="mui-bar mui-bar-tab" id="js-course-bg">
		<div class="gk-course-tip" style="display:none;">
			<span class="gk-course-tip-bg"></span>
			<span id="tipsSpan"><span class="mui-icon mui-minus"></span></span>
		</div>
		<div>
			<!--按钮添加disabled即为禁用状态-->
	        <#if timeState?default(0)==1>
			<a class="mui-tab-item f-16 mui-active js-submitCourse" href="javascript:;" style="background: #2f7bff;color: #fff;">下一步</a>
	        <#elseif timeState?default(0)==0>
			<a class="mui-tab-item f-16 mui-active" disabled href="javascript:;" style="background: #2f7bff;color: #fff;">选课还未开始</a>
	        <#else>
			<a class="mui-tab-item f-16 mui-active" disabled href="javascript:;" style="background: #2f7bff;color: #fff;">选课已结束</a>
	        </#if>
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
                <div style="padding-bottom:32px;">
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
					<#if courseNameMap?exists && recommendChoMap?exists && (recommendChoMap?size gt 0)>
					<div class="box-selectGroup">
						<div class="box-selectGroup-header">推荐组合</div>
						<div class="box-selectGroup-body">
							<div class="box-selectCourse">
								<div class="box-body">
								    <ul class="gk-course-list js-selectGroup mui-style01 mui-table-view mui-grid-view">
										<#list recommendChoMap? keys as key>
										<li class="mui-table-view-cell mui-media mui-col-xs-6" data-course="${key!}">
											<a href="javascript:;">
												<span class="mui-icon gk-course-xk"></span>
												<div class="gk-course-img">
													<#list recommendChoMap[key] as item >
													<span class="gk-course gk-course-g">${courseNameMap[item]!}</span>
													</#list>
												</div>
												<h3 class="gk-course-name">${key!}</h3>
											</a>
										</li>
										</#list>
									</ul>
							    </div>
						    </div>
					    </div>
				    </div>
				    </#if>
				    <div class="mui-bg-g"></div>
				    <div class="box-selectGroup">
						<div class="box-selectGroup-header"><span class="mui-pull-right">请选择${choice.chooseNum?default(3)}门</span>选课科目</div>
						<div class="box-selectGroup-body">
							<#if courseNameMap?exists && categoryDtoList?exists && categoryDtoList?size gt 0>
							<#list categoryDtoList as category>
						    <div class="box-selectCourse category" data-minnum="${category.minNum?default(0)}" data-maxnum="${category.maxNum?default(0)}" data-name="${category.categoryName!}">
								<div class="box-header">${category.categoryName!}<span class="color-gray">
								（最多选${category.maxNum?default(0)}组/门，最少选${category.minNum?default(0)}组/门）
								</span></div>
								<div class="box-body mui-km-course">
									<ul class="gk-course-list gk-selectOne">
										<#if category.choNameMap?exists && category.choNameMap?size gt 0>
										<#assign choNameMap=category.choNameMap>
										<#list choNameMap? keys as key>
										<li class="mui-col-xs-3" id="${key!}">
											<a href="javascript:;">
												<h3 class="gk-course-name">${choNameMap[key]!}</h3>
											</a>
										</li>
										</#list>
										</#if>
									</ul>
								</div>
							</div>
							</#list>
							</#if>
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
</div>
<input type="hidden" id="showNum" value="${showNum!}">
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

var resultCountMap = {};
var resultNameMap = {};
var showNum = $("#showNum").val();
var hintContent='${hintContent!}';
var showTips = false;
var isMaster;
$('#cancelId').off('click').on('click',function(){
	var url = '${request.contextPath}/mobile/open/newgkelective/homepage?studentId=${studentId!}'; 
	if(${chooseNum?default(0)}!=0){
		url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/page?choiceId=${choiceId!}&isMaster='+isMaster;		
	}
	loadByHref(url);
});

var groupClass = {
	<#list codeNames as cns>
	'${cns[0]!}': 'gk-course-g-${cns[1]!}'<#if cns_has_next>,</#if>
	</#list>
};

$(function(){
		$('.mui-tab-item').on('touchstart', function(e){
    		e.stopPropagation();
    	});

		var disabledGroup = [];/**不推荐完整名称**/
		var disabledShortGroup = [];/**不推荐简称**/
		<#if limitChoList?exists && (limitChoList?size gt 0)>
		<#list limitChoList as item>
			disabledGroup.push('${item[0]}');
			disabledShortGroup.push('${item[1]}');
		</#list>
		</#if>
		
		<#if isTips?default(false)>
			var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
			var nameJson = jsonStringData.legendData;
		    var countJson=jsonStringData.loadingData;
		    
		    for(i=0;i<countJson.length;i++){
		   		resultNameMap[countJson[i].subjectId]=nameJson[i];
		    	resultCountMap[countJson[i].subjectId]=countJson[i].value;
			}
		</#if>
		
		var course = [];	/*已选择的课程*/
		var counter = 0;	/*已选课程数*/

		var groupitem = $('.js-selectGroup li');
		var singleItem = $('.gk-selectOne .gk-course-name');
		var submitCourse = $('.js-submitCourse');
       
		function isdisabled(){
			var selSubs = [];;
			if(counter == ${choice.chooseNum?default(3)}){
				/***已经满足啦，显示下一步，选课科目中其他点击失效***/
				submitCourse.attr("disabled",false).attr("id","js-submitCourse");
				singleItem.each(function(){
					if(!$(this).parents("li").hasClass('selected')){
						$(this).parents("li").addClass('disabled');
					}else{
						$(this).parents("li").removeClass('disabled');
						selSubs.push($(this).text());
					}
				})
			} else {
				/***未选中满***/
				submitCourse.attr('disabled',true).attr("id","");
				singleItem.each(function(){
					if($(this).parents("li").hasClass('selected')){
						var text=$(this).text();
						var names=text.split(",");
						for(var ii=0;ii<names.length;ii++){
							selSubs.push(names[ii]);
						}
					}
				})
				$(".category").each(function(){
					var len = $(this).find('.gk-selectOne li.selected').length;
					var name= $(this).data("name");
					if($(this).data("maxnum") && len>=$(this).data("maxnum")){
						$(this).find('.gk-selectOne li').each(function(){
							if(!$(this).hasClass("selected")){
								$(this).addClass("disabled");
							}
						})
					}
				})

				
			}
			var disStrs = '';
			if(disabledGroup.length > 0 && selSubs.length > 0){
				for(var i=0;i<disabledGroup.length;i++){
					var dig = disabledGroup[i];
					var flag = true;
					for(var j=0;j<selSubs.length;j++){
						if(dig.indexOf(selSubs[j])<0){
							flag = false;
							break;
						}
					}
					if(!flag){
						continue;
					}
					if(disStrs!=''){
						disStrs+='、';
					}
					disStrs+=disabledShortGroup[i];
				}
			}
			if(disStrs != ''){
				$('#tipsSpan').html('<span class="mui-icon mui-minus"></span>相关不推荐组合“'+disStrs+'”');
				$('.gk-course-tip').show();
				showTips =true;
			} else{
				showTips =false;
				$('#tipsSpan').html('');
				$('.gk-course-tip').hide();
			}
			
			<#if isTips?default(false)>
				if(counter == ${choice.chooseNum?default(3)}){
					var subjectIds = new Array();  
					singleItem.each(function(index){
						if($(this).parents("li").hasClass('selected')){
							subjectIds.push($(this).parents("li").attr("id"));
						}
					})
					subjectIds.sort();
					subjectIds = subjectIds.join(",");
					if(resultCountMap[subjectIds]!=null&&resultCountMap[subjectIds]<showNum){
						if(!showTips){
							$('#tipsSpan').html(hintContent);
							$(".gk-course-tip").show();
						}
					}
				}else{
					if(!showTips){
						$('#tipsSpan').html('');
						$('.gk-course-tip').hide();
					}
				}
		    </#if>
		}
		
		// 选择组合
		//取消选中，所有科目取消置灰及选中
		//选中 对应的科目也选中，先考虑多科目合并---如果出现两个都符合条件的 默认一个
		groupitem.on('click', function(e){
			e.preventDefault();
			if($(this).hasClass('selected')){
				/***清空所有置灰选项**/
				$(this).removeClass('selected');
				counter = 0;
				course = [];
				singleItem.each(function(){
					$(this).parents("li").removeClass('selected disabled');
				})
			}else{
				singleItem.each(function(){
					$(this).parents("li").removeClass('selected');
				})
				$(this).addClass('selected').siblings().removeClass('selected');
				counter = ${choice.chooseNum?default(3)};
				course = $(this).data('course').split('、');
				zhFirst(course,false);
				
			}
			
			isdisabled();
		});
		//组合优先原则 
		function zhFirst(course,flag){
			var selectMap={};//已经选中的科目
			var len=course.length;
			//前提各个选课条件中的数据不会重复
			if(len>1){
				//考虑两门
				singleItem.each(function(){
					var c = $(this).text();
					var namesArr=c.split(",");
					if(namesArr.length>1){
						$(this).parents("li").removeClass('selected disabled');
						var issame=true;
						for(var ii=0;ii<namesArr.length;ii++){
							if(!selectMap[namesArr[ii]]){
								var f=false;
								for(var j = 0; j < course.length; j++){
									if(namesArr[ii] === course[j]){
										f=true;
										break;
									}
								}
								if(!f){
									/***没有完全一样**/
									issame=false;
									break;
								}
							}else{
								issame=false;
							}
							
						}
						if(issame){
							//完全一样
							for(var ii=0;ii<namesArr.length;ii++){
								selectMap[namesArr[ii]]=namesArr[ii];
							}
							$(this).parents("li").addClass('selected');
						}
					}
					
				})
			}
			if(flag){
				//先考虑选中的单科--如果组合优先去除单科原来选中
				singleItem.each(function(){
					var c = $(this).text();
					var namesArr=c.split(",");
					if(namesArr.length==1 && $(this).parents("li").hasClass('selected')){
						//优先选中的
						if(!selectMap[c]){
							var f=false;
							for(var j = 0; j < course.length; j++){
								if(c === course[j]){
									f=true;
									break;
								}
							}
							if(f){
								//不会出现有连个一样的
								selectMap[c]=c;
							}else{
								//不会出现
								$(this).parents("li").removeClass('selected');
							}
							
						}else{
							//相同科目数据不能重复选中---被组合优先 不能选中
							$(this).parents("li").removeClass('selected');
						}
						
					}
					
				})
			}
			//考虑1门
			singleItem.each(function(){
				var c = $(this).text();
				var namesArr=c.split(",");
				if(namesArr.length==1 && !$(this).parents("li").hasClass('selected')){
					$(this).parents("li").removeClass('disabled');
					if(!selectMap[c]){
						var f=false;
						for(var j = 0; j < course.length; j++){
							if(c === course[j]){
								f=true;
								break;
							}
						}
						//完全一样
						if(f){
							selectMap[c]=c;
							$(this).parents("li").addClass('selected');
						}else{
							$(this).parents("li").removeClass('selected');
						}
					}else{
						//相同科目数据不能重复选中
						$(this).parents("li").removeClass('selected');
						$(this).parents("li").addClass('disabled');
					}
					
				}
				
			})
			
			if(${choice.chooseNum?default(3)}-course.length==1){
				//差一门  如果有两门的未选中的组合需要置灰
				singleItem.each(function(){
					if(!$(this).parents("li").hasClass('selected')){
						var text=$(this).text();
						var names=text.split(",");
						if(names.length>1){
							$(this).parents("li").addClass('disabled');
						}
					}
				})
			}else if(${choice.chooseNum?default(3)}-course.length>1){
				singleItem.each(function(){
					if(!$(this).parents("li").hasClass('selected')){
						var text=$(this).text();
						var names=text.split(",");
						if(names.length>1){
							$(this).parents("li").removeClass('disabled');
						}
					}
				})
			}
		}

		// 单科选择
		singleItem.on('click', function(e){
			e.preventDefault();
			if($(this).parents("li").hasClass('disabled')) return;
			var c = $(this).text();
			var namesArr=c.split(",");
			if($(this).parents("li").hasClass('selected')){
				$(this).parents("li").removeClass('selected');
				//删除掉选中的科目
				var ss=[];
				if(course.length != 0){
					for(var i=0; i<course.length; i++){
						var gg=true;
						for(var ii=0;ii<namesArr.length;ii++){
							if(namesArr[ii] == course[i]){
								f=true;
								gg=false;
								break;
							}
						}
						if(gg){
							ss.push(course[i]);
						}
					}
				}
				counter=ss.length;
				course=ss;
				
			}else{
				//增加新的科目
				$(this).parents("li").addClass('selected');
				if(course.length != 0){
					for(var ii=0;ii<namesArr.length;ii++){
						//去除重复数据
						var gg=false;
						if(counter>0){
							for(var jj=0;jj<course.length;jj++){
								if(course[jj]==namesArr[ii]){
									gg=true;
									break;
								}
							}
						}
						if(!gg){
							course.push(namesArr[ii]);
							counter++;
						}
					}
				}else{
					counter=namesArr.length;
					course=namesArr;
				}
			}
			zhFirst(course,true);
			//是否完全等于推荐组合
			if(counter == ${choice.chooseNum?default(3)}){
				groupitem.each(function(){
					if( course.sort().toString() === $(this).data('course').split('、').sort().toString()){
						$(this).addClass('selected');
					}
				})
			}else{
				groupitem.each(function(){
					if($(this).hasClass('selected')){
						$(this).removeClass('selected');
					}
				})
			}

			isdisabled();
		})
		
	
	
		
		// 提交选课
		var isSubmit=false;
		submitCourse.on('click', function(e){
			e.preventDefault();
			if(isSubmit){
				return false;
			}
			isSubmit=true;

			if($(this).attr("disabled")){ 
				isSubmit=false;
				return;
			}

			if(counter !== ${choice.chooseNum?default(3)} ){
				isSubmit=false;
				toastMsg('请选择${choice.chooseNum!'3'}门科目！');
				return ;
			}
			//提交验证
			var tips = "";
			$(".category").each(function(){
				var len = $(this).find('.gk-selectOne li.selected').length;
				var name= $(this).data("name");
				if($(this).data("minnum") && len<$(this).data("minnum")){
				 	tips = name+"最少选"+$(this).data("minnum")+"组/门";
					return;
				}
				if($(this).data("maxnum") && len>$(this).data("maxnum")){
					tips = name+"最多选"+$(this).data("maxnum")+"组/门";
					return;
				}
				
			})
			if(tips!=""){
				isSubmit=false;
				toastMsg(tips);
				return ;
			}
			
			var sids = '';
			var stypes = '';
        	singleItem.each(function(){
				if($(this).parents('li').hasClass('selected')){
					var sid = $(this).parents('li').attr('id');
					if(sids != ''){
						sids+=',';
					}
					sids+=sid;
				}
			});	
            $.ajax({
				url:'${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/check',
				data: {'choiceId':'${choiceId!}','subjectIds':sids},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
					isSubmit=false;
					if(jsonO.success){
						var url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/next/page?choiceId=${choiceId!}&subjectIds='+sids; 
						loadByHref(url);
					}else{
						toastMsg(jsonO.msg);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		})
		
		//tab切换
		var $div_li1=$(".tab_menu > ul > li");
		$div_li1.click(function(){
			$(this).addClass("active").siblings().removeClass("active");
			var index=$div_li1.index(this);
			$(".tab_box > div").eq(index).show().siblings().hide();
			initCount();
		});
		
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
		
		//课程提示
		function coursebgHeight(){
		     var coursebg_H = $('#js-course-bg').height();
		     var change_H = parseInt(coursebg_H - 10);
		     $('#js-content-wrap').css('padding-bottom',change_H);
		}
		coursebgHeight();
		
		initData();
		
		initCount();
});

function initData(){
	$('.js-selectGroup li .gk-course-g').each(function(){
		var key = $(this).html();
		$(this).addClass(groupClass[key]);
	});
	
	<#if resultList?exists && (resultList?size gt 0)>
		<#list resultList as item>
			var ob = $('.gk-course-list li[id="${item.subjectId!}"]');
			ob.find('.gk-course-name').click();
		</#list>
	</#if>
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