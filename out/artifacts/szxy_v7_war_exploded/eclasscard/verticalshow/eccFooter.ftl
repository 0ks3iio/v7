<ul class="menu-list">
	<li <#if type=='1'>class="active"</#if>>
		<a href="javascript:void(0);" onclick="showIndex()">
			<span class="icon icon-home"></span>
			<h5 class="menu-name">首页</h5>
		</a>
	</li>
<#if eccInfo.type=="10" || eccInfo.type=="20">
	<li <#if type=='2'>class="active"</#if>>
		<a href="javascript:void(0);" onclick="showClassClock()">
			<span class="icon icon-sign"></span>
			<h5 class="menu-name">上课签到</h5>
		</a>
	</li>
</#if>
<#if eccInfo.type=="10">
	<li <#if type=='3'>class="active"</#if>>
		<a href="javascript:void(0);" onclick="showClassSpace()">
			<span class="icon icon-classSpace"></span>
			<h5 class="menu-name">班级空间</h5>
		</a>
	</li>
</#if>
	<li <#if type=='4'>class="active"</#if>>
		<a href="javascript:void(0);" onclick="showStudentSpace()">
			<span class="icon icon-stuSpace"></span>
			<h5 class="menu-name">学生空间</h5>
		</a>
	</li>
</ul>
<script>
//wesocket调用此方法，修改方法名，后台也要修改
function showClassClock(){
	 $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/classClockIn/chechClock',
        data:{"deviceNumber":_deviceNumber},
        type:'post',
        success:function(data){
            var result = JSON.parse(data);
            if(result.success){
				location.href = "${request.contextPath}/eccShow/eclasscard/classClockIn/index?deviceNumber="+_deviceNumber+"&id="+result.businessValue+"&view="+_view;
            }else{
            	showMsgTip('当前未到打卡时间，请于上课前10分钟前来打卡');
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        
        }
    });
}

function showStudentSpace(){
<#if type != '4'>
	location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/login?deviceNumber="+_deviceNumber+"&view="+_view;
</#if>
}
function showClassSpace(){
	location.href = "${request.contextPath}/eccShow/eclasscard/classSpace/index?deviceNumber="+_deviceNumber+"&view="+_view;
}
function showIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?deviceNumber="+_deviceNumber+"&view="+_view;
}
//test
function showClass(){
	location.href = "${request.contextPath}/eccShow/eclasscard/classClockIn/index?deviceNumber="+_deviceNumber+"&id=00EA65CEE080DE8DE050A8C09B006DCP&view="+_view;
}
</script>