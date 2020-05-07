<ul class="menu-list">
	<li id="footTabHome" class="active">
		<a href="javascript:void(0);" onclick="changeTabTo('1')">
			<span class="icon icon-home"></span>
			<h5 class="menu-name">首页</h5>
		</a>
	</li>
	<#if eccInfo.type=='10' || eccInfo.type=='20'>
	<li id="footTabClsClock">
		<a href="javascript:void(0);" onclick="changeTabTo('2')">
			<span class="icon icon-sign"></span>
			<h5 class="menu-name">上课签到</h5>
		</a>
	</li>
	</#if>
	<#if eccInfo.type=='10' && isShowClassSpace?default(true)>
	<li id="footTabClsSpace">
		<a href="javascript:void(0);" onclick="changeTabTo('3')">
			<span class="icon icon-classSpace"></span>
			<h5 class="menu-name">班级空间</h5>
		</a>
	</li>
	</#if>
	<li id="footTabStuSpace">
		<a href="javascript:void(0);" onclick="changeTabTo('4')">
			<span class="icon icon-stuSpace"></span>
			<h5 class="menu-name">学生空间</h5>
		</a>
	</li>
	<#if eccInfo.type=='10' && !(isShowClassSpace?default(true))>
	<li id="footTabSchoolSpace">
		<a href="javascript:void(0);" onclick="changeTabTo('6')">
			<span class="icon icon-schoolSpace"></span>
			<h5 class="menu-name">校园空间</h5>
		</a>
	</li>
	</#if>
	<#if eccInfo.type=='10'>
	<li id="footTabInOutAttence">
		<a href="javascript:void(0);" onclick="changeTabTo('7')">
			<span class="icon icon-updown"></span>
			<h5 class="menu-name">上下学考勤</h5>
		</a>
	</li>
	</#if>
	
	
</ul>
<script>
	var loadOk = false;//防止频繁点击
	function changeTabTo(type){
	   	setTimeout(function(){
			loadOk = false;
		},1000);
		if(loadOk){
			return;
		}
		loadOk = true;
		if(null != interval_init_course){  
	       clearInterval(interval_init_course);  
	   	}
		if(type==1){
			showIndex();
		}else if(type==2){
			showClassClock();
		}else if(type==3){
			showClassSpace();
		}else if(type==4){
			showStudentSpace('');
		}else if(type==6){
			showSchoolSpace();
		}else if(type==7){
			showInOutAttence();
		}
		
		if (isActivate == "true") {
			closeFaceView();
		}
	}
	
	function showSchoolSpace(){
		if (window.jsInterface && jsInterface.loadOutLink){
			if('${parmUrl!}'!=''){
				jsInterface.loadOutLink('${parmUrl!}'); 
			}
		}
	}
	
	function showClassSpace() {
		$("#footTabClsSpace").addClass('active').siblings().removeClass('active');
		isLoadpage = true;
		timeoutJump();
		$("#indexMsg").hide();
		var classSpace = "${request.contextPath}/eccShow/eclasscard/standard/classspace/index?cardId="+_cardId+"&view="+_view;
		$("#mainContainerDiv").load(classSpace);
	}
	//wesocket调用此方法，修改方法名，后台也要修改
	function showInOutAttence() {
		$.ajax({
	        url:'${request.contextPath}/eccShow/eclasscard/standard/check/inout/attence',
	        data:{"cardId":_cardId},
	        type:'post',
	        success:function(data){
	            var result = JSON.parse(data);
	            if(result.success){
					skipInOutAttence(result.businessValue);
					if (isActivate == "true") {
						closeFaceView();
					}
	            }else{
	            	showMsgTip('非上下学考勤时间');
	            	if ($("#footTabInOutAttence").hasClass('active')) {
	    				showIndex();
						if (isActivate == "true") {
							closeFaceView();
						}
	    			}
	            }
	        },
	        error : function(XMLHttpRequest, textStatus, errorThrown) {
	        }
	    });
	}
	
	function showStudentSpace(stuId) {
		isLoadpage = true;
		timeoutJump();
		$("#footTabStuSpace").addClass('active').siblings().removeClass('active');
		$("#indexMsg").hide();
		var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/login?cardId="+_cardId+"&view="+_view+"&studentId="+stuId;
		$("#mainContainerDiv").load(studentSpaceUrl);
		faceSubmit = false;
	}
//wesocket调用此方法，修改方法名，后台也要修改
function showClassClock(){
     if (isActivate == "true") {
         closeFaceView();
     }
	 $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/classClockIn/chechClock',
        data:{"cardId":_cardId},
        type:'post',
        success:function(data){
            var result = JSON.parse(data);
            if(result.success){
				skipToClassClock(result.businessValue);
            }else{
            	showMsgTip('当前未到打卡时间，请于上课前10分钟前来打卡');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

</script>