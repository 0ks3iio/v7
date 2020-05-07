<#macro selectMoreStudent clickId="" columnName="学生(多选)" dataUrl="${request.contextPath}/common/div/student/popupData" id="" name="" dataLevel="4" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/student/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneStudent clickId="" columnName="学生(单选)" dataUrl="${request.contextPath}/common/div/student/popupData" id="" name="" dataLevel="4" type="danxuan" recentDataUrl="${request.contextPath}/common/div/student/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectMoreTeacher clickId="" columnName="教师(多选)" dataUrl="${request.contextPath}/common/div/teacher/popupData" id="" name="" dataLevel="2" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/teacher/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>


<#macro selectOneTeacher clickId="" columnName="教师(单选)" dataUrl="${request.contextPath}/common/div/teacher/popupData" id="" name="" dataLevel="2" type="danxuan" recentDataUrl="${request.contextPath}/common/div/teacher/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectMoreClass clickId="" columnName="班级(多选)" dataUrl="${request.contextPath}/common/div/class/popupData" id="" name="" dataLevel="3" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/type/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneClass clickId="" columnName="班级(单选)" dataUrl="${request.contextPath}/common/div/class/popupData" id="" name="" dataLevel="3" type="danxuan" recentDataUrl="${request.contextPath}/common/div/class/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneDept clickId="" columnName="部门(单选)" dataUrl="${request.contextPath}/common/div/dept/popupData" id="" name="" dataLevel="3" type="danxuan" recentDataUrl="${request.contextPath}/common/div/dept/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectMoreUser clickId="" columnName="用户(多选)" dataUrl="${request.contextPath}/common/div/user/popupData" id="" name="" dataLevel="2" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/user/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneUser clickId="" columnName="用户(单选)" dataUrl="${request.contextPath}/common/div/user/popupData" id="" name="" dataLevel="2" type="danxuan" recentDataUrl="${request.contextPath}/common/div/user/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectMoreTeacherUser clickId="" columnName="用户(多选)" dataUrl="${request.contextPath}/common/div/user/popupData?ownerType=2" id="" name="" dataLevel="2" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/user/recentData?ownerType=2" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneTeacherUser clickId="" columnName="用户(单选)" dataUrl="${request.contextPath}/common/div/user/popupData?ownerType=2" id="" name="" dataLevel="2" type="danxuan" recentDataUrl="${request.contextPath}/common/div/user/recentData?ownerType=2" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectMoreStuByClass clickId="" columnName="学生(多选)" dataUrl="${request.contextPath}/common/div/student/popupData/class" id="" name="" dataLevel="1" type="duoxuan" resourceUrl="${resourceUrl}" handler="" popupType="one">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler popupType=popupType>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneStuByClass clickId="" columnName="学生(单选)" dataUrl="${request.contextPath}/common/div/student/popupData/class" id="" name="" dataLevel="1" type="danxuan" resourceUrl="${resourceUrl}" handler="" popupType="one">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler popupType=popupType>
		<#nested>
	</@popup_div>
</#macro>


<#--根据类型多选场地-->
<#macro selectPlaceByBuild clickId="" columnName="场地(多选)" dataUrl="${request.contextPath}/common/div/teachPlace/popupData/build" id="" name="" dataLevel="2" type="duoxuan" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler popupType=popupType>
		<#nested>
	</@popup_div>
</#macro>


<#--根据类型单选场地-->
<#macro selectOnePlaceByType clickId="" columnName="场地(单选)" dataUrl="${request.contextPath}/common/div/teachPlace/popupData" id="" name="" dataLevel="2" type="danxuan" putRecentDataUrl="${request.contextPath}/common/div/teachPlace/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>


<#macro selectMoreUnit clickId="" columnName="单位(多选)" dataUrl="${request.contextPath}/common/div/unit/popupData" id="" name="" dataLevel="2" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/unit/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#macro selectOneUnit clickId="" columnName="单位(单选)" dataUrl="${request.contextPath}/common/div/unit/popupData" id="" name="" dataLevel="2" type="danxuan" putRecentDataUrl="${request.contextPath}/common/div/unit/recentData" resourceUrl="${resourceUrl}" handler="">
	<@popup_div clickId=clickId columnName=columnName dataUrl=dataUrl id=id name=name dataLevel=dataLevel type=type recentDataUrl=recentDataUrl resourceUrl=resourceUrl handler=handler>
		<#nested>
	</@popup_div>
</#macro>

<#--
	columnName	列名
	dataUrl　获取数据URL
	id 数据id
	name  数据名称
	dataLevel 数据在第几层
	type 	danxuan duoxuan 单选还是多选
	recentDataUrl 记录最近记录的url
	resourceUrl
	handler回调函数 
	popupType　级联还是单级
-->
<#macro popup_div clickId="" columnName="" dataUrl="" id="" name="" dataLevel="" type="" recentDataUrl="" resourceUrl="" handler="" popupType="all">
	<link href="${request.contextPath}/static/js/popup/popup.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="${request.contextPath}/static/js/popup/popup.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/js/popup/popup_search.js"></script>
	<#nested>
	<input type="hidden" id="popup-extra-param" value=""/>
	<script>
		$('#${clickId}').unbind("click").bind("click", function(){
			popupSlect('${id}','${name}','${type}','${dataLevel}','${handler}','${columnName}','${recentDataUrl}','${dataUrl}','${popupType}');
		});
	</script>
</#macro>
