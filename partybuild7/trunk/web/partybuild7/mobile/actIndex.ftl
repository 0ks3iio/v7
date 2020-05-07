<#import "/fw/macro/mobilecommon.ftl" as common>
<#assign tn = '党员活动' />
<#if level?default(1) == 2>
<#assign tn = '领导活动' />	
</#if>
<@common.moduleDiv titleName=tn>
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">${tn!}</h1>
</header>
<div class="mui-content" style="padding-top:0px;">
<ul class="mui-table-view mui-table-view-chevron" id="actListUl">
</ul>
</div>
<script type="text/javascript" charset="utf-8">
mui.init({
    <#--
    pullRefresh : {
        container:".mui-content",//待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
        up : {
            height:50,// 可选.默认50.触发上拉加载拖动距离
            auto:false,// 可选,默认false.自动上拉加载一次
            contentrefresh : "正在加载...",// 可选，正在加载状态时，上拉加载控件上显示的标题内容
            contentnomore:'没有更多数据了',// 可选，请求完毕若没有更多数据时显示的提醒内容；
            callback : function() {
                var self = this; // 这里的this == mui('#refreshContainer').pullRefresh()
                // 加载更多的内容
                loadMore(this);
            } //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
        }
    }-->
});

var str = 'teacherId=${teacherId!}&level=${level?default(1)}&pageIndex=1';
var url = "${request.contextPath}/mobile/open/partybuild7/activity/list/page?"+str;
$('#actListUl').load(url);	  	

$("#cancelId").click(function(){
	load("${request.contextPath}/mobile/open/partybuild7/homepage?teacherId=${teacherId!}");
});

</script>  
</@common.moduleDiv>