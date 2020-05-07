<#import "/fw/macro/mobilecommon.ftl" as common>
<@common.moduleDiv titleName="党建管理">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav" style="height:0px;display:none;">
    <a class="mui-btn-close">关闭</a>
    <a class="mui-icon mui-icon-more mui-pull-right"></a>
    <h1 class="mui-title">党建管理</h1>
</header>
<div class="mui-content" style="padding-top:0px;">
	    <!--=S 九宫格 Start-->
        
        <ul class="mui-table-view mui-grid-view mui-grid-6">
            <#--<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
            	<a href="#">
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-1.png" class="mui-media-img">
		            <div class="mui-media-body">个人信息</div>
		        </a>
            </li>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
            	<a href="#">
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-4.png" class="mui-media-img">
		            <div class="mui-media-body">组织管理</div>
		        </a>
            </li>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
            	<a href="#">
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-5.png" class="mui-media-img">
		            <div class="mui-media-body">转出申请</div>
		        </a>
            </li>-->
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
            	<a href="#" onclick="doActivist();" >
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-6.png" class="mui-media-img">
		            <div class="mui-media-body">积极分子申请</div>
		        </a>
            </li>
            <#if enterOrg?default(false)>
            <#if rdsp?default(false)>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4 pro-audit" applyState="1">
            	<a href="#">
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-2.png" class="mui-media-img">
		            <div class="mui-media-body">入党审批</div>
		        </a>
            </li>
            </#if>
            <#if dyzz?default(false)>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4 formal-audit" applyState="2">
            	<a href="#">
		            <img src="${request.contextPath}/partybuild7/mobile/images/channel/grid-3.png" class="mui-media-img">
		            <div class="mui-media-body">党员转正</div>
		        </a>
            </li>
            </#if>
            </#if>
        </ul> 
        <!--=S 九宫格 End-->
        <!--=S 横排九宫格 Start-->
        <#--
        <div class="mui-index-wrap">
        	<div class="mui-index-tit">
				<a href="#">日常管理</a>
			</div>
	        <ul class="mui-table-view mui-grid-view mui-grid-4">
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-1.png" class="mui-media-img">
			            <div class="mui-media-body">党员信息维护</div>
			        </a>
	            </li>
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-2.png" class="mui-media-img">
			            <div class="mui-media-body">增加既有党员</div>
			        </a>
	            </li>
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-3.png" class="mui-media-img">
			            <div class="mui-media-body">年度优秀党员</div>
			        </a>
	            </li>
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-4.png" class="mui-media-img">
			            <div class="mui-media-body">年度优秀党组织</div>
			        </a>
	            </li>
	        </ul> 
        </div>
        -->
        <#if enterOrg?default(false)>
        <#if jjfzkc?default(false)>
        <div class="mui-index-wrap">
        	<div class="mui-index-tit">
				<a href="#">组织关系</a>
			</div>
	        <ul class="mui-table-view mui-grid-view mui-grid-4">
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6 activist-audit" applyState="0">
	            	<a href="#" >
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-5.png" class="mui-media-img">
			            <div class="mui-media-body">积极分子考察</div>
			        </a>
	            </li>
	            <#--<li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-6.png" class="mui-media-img">
			            <div class="mui-media-body">转入转出记录</div>
			        </a>
	            </li>
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-7.png" class="mui-media-img">
			            <div class="mui-media-body">党员转入审批</div>
			        </a>
	            </li>
	            <li class="mui-table-view-cell mui-media mui-col-xs-6 mui-col-sm-6">
	            	<a href="#">
			            <img src="${request.contextPath}/partybuild7/mobile/images/channel/sub-grid-8.png" class="mui-media-img">
			            <div class="mui-media-body">党员转出审批</div>
			        </a>
	            </li>-->
	        </ul> 
        </div>
        </#if>
        <!--=S 横排九宫格 End-->
        <!--=S 动态 Start-->
        <ul class="mui-table-view mui-table-view-concern">
        	<#if orgTrend?exists>
        	<li class="mui-table-view-cell mui-media orgtread-li" liId="${orgTrend.id!}">
	        	<div class="mui-table orgtread-more-div">
	        		<div class="mui-table-cell mui-col-xs-9">
	    	            <div class="mui-media-body">
	    	                <p class="mui-ellipsis">${orgTrend.timeStr!} 发布了<a href="#">支部动态</a></p>
    	                </div>
	        		</div>
	        		<div class="mui-table-cell mui-col-xs-3 mui-text-right">
	        			<a href="#" class="mui-more-txt">更多动态 ></a>
	        		</div>
	        	</div>
            	<h4 class="mui-ellipsis-2">${orgTrend.topic!}</h4>
            	<div class="mui-table mui-table-inner">${orgTrend.trendsContent!}</div>
    	    </li>
    	    </#if>
    	    <#if meet?exists>
    	    <li class="mui-table-view-cell mui-media meet-li" liId="${meet.id!}">
	        	<div class="mui-table meet-more-div">
	        		<div class="mui-table-cell mui-col-xs-9">
	    	            <div class="mui-media-body">
	    	                <p class="mui-ellipsis">${meet.timeStr!} 发布了<a href="#">会议纪要</a></p>
    	                </div>
	        		</div>
	        		<div class="mui-table-cell mui-col-xs-3 mui-text-right">
	        			<a href="#" class="mui-more-txt">更多纪要 ></a>
	        		</div>
	        	</div>
            	<h4 class="mui-ellipsis-2">${meet.meetingName!}</h4>
            	<div class="mui-table mui-table-inner">${meet.meetingAim!}</div>
    	    </li>
    	    </#if>
    	    <#if commonAct?exists>
    	    <li class="mui-table-view-cell mui-media commonact-li" liId="${commonAct.id!}">
	        	<div class="mui-table commonact-more-div">
	        		<div class="mui-table-cell mui-col-xs-9">
	    	            <div class="mui-media-body">
	    	                <p class="mui-ellipsis">${commonAct.timeStr!} 发布了<a href="#">党员活动</a></p>
    	                </div>
	        		</div>
	        		<div class="mui-table-cell mui-col-xs-3 mui-text-right">
	        			<a href="#" class="mui-more-txt">更多活动 ></a>
	        		</div>
	        	</div>
            	<h4 class="mui-ellipsis-2">${commonAct.name!}</h4>
            	<div class="mui-table mui-table-inner">${commonAct.content!}</div>
    	    </li>
    	    </#if>
    	    <#if leaderAct?exists>
    	    <li class="mui-table-view-cell mui-media leaderact-li" liId="${leaderAct.id!}">
	        	<div class="mui-table leaderact-more-div">
	        		<div class="mui-table-cell mui-col-xs-9">
	    	            <div class="mui-media-body">
	    	                <p class="mui-ellipsis">${leaderAct.timeStr!} 发布了<a href="#">领导活动</a></p>
    	                </div>
	        		</div>
	        		<div class="mui-table-cell mui-col-xs-3 mui-text-right">
	        			<a href="#" class="mui-more-txt">更多活动 ></a>
	        		</div>
	        	</div>
            	<h4 class="mui-ellipsis-2">${leaderAct.name!}</h4>
            	<div class="mui-table mui-table-inner">${leaderAct.content!}</div>
    	    </li>
    	    </#if>
        </ul>
        <!--=S 动态 End-->
	</div>
	</#if>
<script type="text/javascript" charset="utf-8">
mui.init();

$('.activist-audit,.pro-audit, .formal-audit').click(function(){
	var aps = $(this).attr('applyState');
	load('${request.contextPath}/mobile/open/partybuild7/memberList/index?teacherId=${teacherId?default("")}&applyState='+aps);
});

//会议
$('.meet-more-div').click(function(){
	load('${request.contextPath}/mobile/open/partybuild7/memberMeeting/meetingList?teacherId=${teacherId?default("")}');
});
$('.meet-li .mui-ellipsis-2,.meet-li .mui-table-inner').click(function(){
	var id=$('.meet-li').attr('liId');
	load('${request.contextPath}/mobile/open/partybuild7/memberMeeting/info?meetingId='+id);
});

// 支部动态
$('.orgtread-more-div').click(function(){
	load('${request.contextPath}/mobile/open/partybuild7/orgTrend/list?teacherId=${teacherId?default("")}');
});
$('.orgtread-li .mui-ellipsis-2,.orgtread-li .mui-table-inner').click(function(){
	var id=$('.orgtread-li').attr('liId');
	load('${request.contextPath}/mobile/open/partybuild7/orgTrend/showInfo?id='+id);
});

// 领导活动
$('.leaderact-more-div').click(function(){
	doLoadAct('2');
});

$('.leaderact-li .mui-ellipsis-2,.leaderact-li .mui-table-inner').click(function(){
	var id=$('.leaderact-li').attr('liId');
	doLoadActDetail(id);
});

// 党员活动
$('.commonact-more-div').click(function(){
	doLoadAct('1');
});
$('.commonact-li .mui-ellipsis-2,.commonact-li .mui-table-inner').click(function(){
	var id=$('.commonact-li').attr('liId');
	doLoadActDetail(id);
});

function doLoadActDetail(id){
	load("${request.contextPath}/mobile/open/partybuild7/activity/detail?id="+id);
}

function doLoadAct(level){
	var url = "${request.contextPath}/mobile/open/partybuild7/activity/list/index?teacherId=${teacherId?default("")}&level="+level; 
	load(url);
}
function doActivist() {
    var url="${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?teacherId=${teacherId?default("")}";
    load(url);
}
storage.set(WeikeConstants.WEIKE_FLAG_KEY, WeikeConstants.WEIKE_FLAG_VALUE_TYPE_2);
storage.set('current.teacher.id','${teacherId?default("")}');
storage.set('current.user.id','${userId?default("")}');

<#--//取消
$("#cancelId").click(function(){
	try{
		mobile.back();
	}catch(e){
		location.reload();
	}
});-->
</script>
</@common.moduleDiv>