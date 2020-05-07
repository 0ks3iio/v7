<div class="grid">
<#if eccInfo.type=="10">
	<div class="grid-cell grid-3of10">
		<div class="box">
			<div class="box-body" style="height:810px;">
				<div id="classInfoDiv">
					<div class="role">
						<span class="role-img" style="height:143px;"></span>
						<span class="label label-fill label-fill-bluePurple">班主任</span>
						<h4 class="role-name"></h4>
					</div>

					<ul class="data-list">
						<li><span>班级人数</span><span><em></em></span></li>
						<li><span>请假人数</span><span><em></em></span></li>
						<li>
							<span>请假名单</span>
							<span></span>
							<div class="leave-list">
								<h4>请假名单</h4>
								<div class="leave-list-content"></div>
							</div>
						</li>
					</ul>
				</div> 
				<div id="honorDiv">
					<div class="box-horizontal">
						<div class="box-horizontal-header">
							<h3>班级荣誉</h3>
						</div>
						<div class="box-horizontal-body" style="height:154px;">
							<ul class="honor-list honor-list-h js-honor-slick">
							</ul>
						</div>
					</div>
					<div class="box-horizontal">
						<div class="box-horizontal-header">
							<h3>个人荣誉</h3>
						</div>
						<div class="box-horizontal-body" style="height:154px;">
							<ul class="personal-honor">
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="grid-cell grid-4of10">
		<div id="albumDiv" class="box box-slider">
			<div class="slider-wrap">
				<div class="slider-btn">
					<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
					<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
				</div>
				<div class="slider-counter"></div>
				<div class="slider">
					<div class="slider-item">
						<div class="img-wrap img-wrap-16by9" style="background-image:url('${request.contextPath}/static/eclasscard/standard/show/images/body-bg.png');"></div>
						<h4 class="slider-title"></h4>
					</div>
				</div>
			</div>
		</div>
		<#if layout>
		<div id="courseDiv" class="box">
			<div class="box-body">
				<div class="course-notice-wrapper">
				</div>
			</div>
		</div>
		<#else>
		<div id="bulletinDiv" class="box">
			<div class="box-header">
				<h3 class="box-title">通知公告</h3>
			</div>
			<div class="box-body">
				<div class="post-container" style="height:300px;">
				</div>
			</div>
		</div>
		</#if>
	</div>
	<#if layout>
	<div class="grid-cell grid-3of10">
        <div id="bulletinDiv" class="box">
        	<div class="box-header">
				<h3 class="box-title">通知公告</h3>
			</div>
			<div class="box-body">
				<div class="post-container" style="height:710px;">
				</div>
			</div>
    	</div>
	</div>
	<#else>
	<div class="grid-cell grid-3of10">
		<div id="courseDiv" class="box">
			<div class="box-body">
				<div class="course-notice-wrapper" style="height:770px;">
				</div>
			</div>
		</div>
	</div>
	</#if>
<#elseif eccInfo.type=="20">
	<div class="grid-cell index-main">
		<div id="albumDiv" class="box box-slider">
			<div class="slider-wrap">
				<div class="slider-btn">
					<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
					<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
				</div>
				<div class="slider-counter"></div>
				<div class="slider">
					<div class="slider-item">
						<div class="img-wrap img-wrap-16by9" style="background-image:url('${request.contextPath}/static/eclasscard/standard/show/images/body-bg.png');"></div>
						<h4 class="slider-title"></h4>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="grid-cell index-side">
		<div id="courseDiv" class="box" style="height:395px; overflow-y:auto;">
		</div>
		<div id="bulletinDiv" class="box">
			<div class="box-header">
				<h4 class="box-title">通知公告</h4>
			</div>
			<div class="box-body">
				<div class="post-container" style="height:290px;">
				</div>
			</div>
		</div>
	</div>
<#elseif eccInfo.type=="60">
	<div class="grid-cell index-main">
		<div id="albumDiv" class="box box-slider">
			<div class="slider-wrap">
				<div class="slider-btn">
					<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
					<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
				</div>
				<div class="slider-counter"></div>
				<div class="slider">
					<div class="slider-item">
						<div class="img-wrap img-wrap-16by9" style="background-image:url('${request.contextPath}/static/eclasscard/standard/show/images/body-bg.png');"></div>
						<h4 class="slider-title"></h4>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="grid-cell index-side">
		<div id="bulletinDiv" class="box">
			<div class="box-header">
				<h4 class="box-title">通知公告</h4>
			</div>
			<div class="box-body">
				<div class="post-container" style="height:710px;">
				</div>
			</div>
		</div>
	</div>
<#else>
	<div class="grid-cell index-main">
		<div id="albumDiv" class="box box-slider">
			<div class="slider-wrap">
				<div class="slider-btn">
					<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
					<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
				</div>
				<div class="slider-counter"></div>
				<div class="slider">
					<div class="slider-item">
						<div class="img-wrap img-wrap-16by9" style="background-image:url('${request.contextPath}/static/eclasscard/standard/show/images/body-bg.png');"></div>
						<h4 class="slider-title"></h4>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="grid-cell index-side">
		<div class="box pos-rel">
			<div class="box-header">
				<h4 class="box-title">打卡情况</h4>
			</div>
			<#if eccInfo.type=="30">
			<div class="clock-record" id="dormClockGradeDiv" style="display:none">

			</div>
			<div class="brush-face clearfix" onClick="openFaceView()" style="display:none">
				<img src="${request.contextPath}/static/eclasscard/standard/show/images/brush-face.png" class="pull-left">
			</div>
			<#else>
				<div class="brush-face clearfix" onClick="openFaceView()">
					<img src="${request.contextPath}/static/eclasscard/standard/show/images/brush-face.png" class="pull-left">
				</div>
			</#if>
			<div class="box-body clock-case" id="clockRecordDiv" style="height:335px;">

			</div>
		</div>
		<div id="bulletinDiv" class="box">
			<div class="box-header">
				<h4 class="box-title">通知公告</h4>
			</div>
			<div class="box-body">
				<div class="post-container" style="height:295px;">
				</div>
			</div>
		</div>
	</div>
</#if>
</div>

<script>
$(document).ready(function(){
	$("#footTabHome").addClass('active').siblings().removeClass('active');
	$("#clock-tab-type").val(1);
	var	bulletinUrl =  "${request.contextPath}/eccShow/eclasscard/standard/showindex/bulletin?cardId="+_cardId+"&view="+_view;
	var	albumUrl =  "${request.contextPath}/eccShow/eclasscard/standard/showindex/album?cardId="+_cardId+"&view="+_view;
	$("#bulletinDiv").load(bulletinUrl);
	$("#albumDiv").load(albumUrl);
	var	clockRecordUrl =  "${request.contextPath}/eccShow/eclasscard/standard/showindex/clockin?cardId="+_cardId+"&view="+_view;
	<#if eccInfo.type=="10">
		var	classInfoUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/class/info?cardId="+_cardId+"&view="+_view;
		var	honorUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/honorlist?cardId="+_cardId+"&view="+_view;
		var	courseUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/stuschedule?cardId="+_cardId+"&view="+_view;
		var	indexMsgUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/indexMsg?cardId="+_cardId+"&view="+_view;
		$("#classInfoDiv").load(classInfoUrl);
		$("#honorDiv").load(honorUrl);
		$("#courseDiv").load(courseUrl);
		$("#indexMsg").load(indexMsgUrl);
	<#elseif eccInfo.type=="20">
		var	courseUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/stuschedule?cardId="+_cardId+"&view="+_view;
		$("#courseDiv").load(courseUrl);
	<#elseif eccInfo.type=="60">
	<#else>
		$("#clockRecordDiv").load(clockRecordUrl);
		<#if eccInfo.type=="30">
		$("#dormClockGradeDiv").load("${request.contextPath}/eccShow/eclasscard/showIndex/clockGrade?cardId="+_cardId+"&view="+_view);
		</#if>
		if (isActivate == "false") {
			$(".brush-face").attr("style","display:none");
		}
	</#if>

	<#if eccInfo.url?default('')!=''>
		$("#albumDiv").on("click",function(){
			if (window.jsInterface && jsInterface.loadOutLink){
				jsInterface.loadOutLink('${eccInfo.url!}'); 
			}
		})
	</#if>
})

function bulletinShowDetail(id){
	var url =  '${request.contextPath}/eccShow/eclasscard/standard/showindex/bulletindetail?id='+id+"&view="+_view;
	$("#showDetailLayer").load(url,function() {
		  showDetailLayer();
		});
}

function showDetailLayer(){
		var index = layer.open({
			type: 1,
			title: false,
			shade: .5,
			shadeClose: true,
			closeBtn: 0,
			area: ['1000px','80%'],
			content: $('.layer-post')
		})
		layer.style(index, {
			overflow: 'auto'
		})
		
		setTimeout(function(){  
	        layer.close(index);  
	    },1000*60);
}

function homeClockMethed(cardNumber){
	<#if eccInfo.type=="41"||eccInfo.type=="42"||eccInfo.type=="30"||eccInfo.type=="50">
		 $.ajax({
	        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
	        data:{"cardNumber":cardNumber,"cardId":_cardId, "clockType":0},
	        type:'post',
	        success:function(data){
	            var result = JSON.parse(data);
	            if(result.haveStu){
	            	if(result.status==1){
	            		showStuSuccess();
	            		<#if eccInfo.type=="41">
							showPrompt("inSuc");
						<#elseif eccInfo.type=="42">
							showPrompt("outSuc");
						<#elseif eccInfo.type=="30" || eccInfo.type=="50">
							showPrompt("signSuc");
						</#if>
	            	} else if(result.status==2) {
						<#if eccInfo.type=="42">
							showPrompt("outFail");
						<#elseif eccInfo.type=="30" || eccInfo.type=="50">
							showPrompt("signFail");
						</#if>
					}
	            	showStuClockMsg(result);
	            }else{
	            	showMsgTip(result.msg);
					<#if eccInfo.type=="42">
					showPrompt("outFail");
					<#elseif eccInfo.type=="30" || eccInfo.type=="50">
					showPrompt("signFail");
					</#if>
	            }
	        },
	        error : function(XMLHttpRequest, textStatus, errorThrown) {
				<#if eccInfo.type=="42">
				showPrompt("outFail");
				<#elseif eccInfo.type=="30" || eccInfo.type=="50">
				showPrompt("signFail");
				</#if>
	        }
	    });
    </#if>
}
function showStuSuccess(){
	var clockUrl =  "${request.contextPath}/eccShow/eclasscard/standard/showindex/clockin?cardId="+_cardId+"&view="+_view;
	$("#clockRecordDiv").load(clockUrl);
}
</script>
