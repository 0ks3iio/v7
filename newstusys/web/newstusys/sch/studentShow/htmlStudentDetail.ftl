<!DOCTYPE html>
<html lang="en">
	<head>
		<script>
		_contextPath = "${request.contextPath}";
		</script>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>${frameworkEnv.getString("platform_name")!"首页"}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<!--[if !IE]> -->
		<!-- <link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/pace.css" />
		<script data-pace-options='{ "ajax": true, "document": true, "eventLag": false, "elements": false }' src="${request.contextPath}/static/ace/components/PACE/pace.js"></script> -->
		<!-- <![endif]-->
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/font-awesome/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/chosen/chosen.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-skins.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-rtl.css" />
	
		<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace-ie.css" />
		<![endif]-->
		<!-- ace settings handler -->
		<script src="${request.contextPath}/static/ace/assets/js/ace-extra.js"></script>
		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="${request.contextPath}/static/ace/components/html5shiv/dist/html5shiv.min.js"></script>
		<script src="${request.contextPath}/static/ace/components/respond/dest/respond.min.js"></script>
		<![endif]-->
<style>
.table tr{
	background-color: #fff;
	page-break-inside: avoid !important;
}
.table-striped > tbody > tr:nth-of-type(odd) {
  background-color: #fff;
}
</style>
</head>
<body class="no-skin">
<div class="main-content" style="">
	<div class="main-content-inner">
		<div class="page-content" style="padding: 0px 0px 0px;">
			<#if studentList?exists && studentList?size gt 0>
			<#list studentList as student>
			<div class="box-body" style="page-break-inside: avoid !important;">
    <table class="table table-bordered table-striped no-margin">
        <tr>
            <td colspan="4" class="text-center" style="background-color: #f9f9f9;">学生信息
            </td>
        </tr>
        <tbody>
        <tr>
            <td class="text-left">姓名:${student.studentName?default('')}</td>
            <td>曾用名:${student.oldName?default('')}</td>
            <td>班级:${student.className!}</td>
            <td rowspan="5" width="135px;">
            	<#if student.hasPic?default(false)><img width="130px;" height="130px;" src="${student.oldFilePath!}"></#if>
            </td>
        </tr>
        <tr>
            <td class="text-left">性别:${mcodeSetting.getMcode("DM-XB", (student.sex?string)?default("0"))}</td>
            <td>民族:${mcodeSetting.getMcode("DM-MZ", student.nation?default(''))}</td>
            <td>国籍:${mcodeSetting.getMcode("DM-COUNTRY", student.country?default(''))}</td>
        </tr>
        <tr>
            <td class="text-left">班内编号:${student.classInnerCode?default('')}</td>
            <td class="text-left">学号:${student.studentCode?default('')}</td>
            <td class="text-left">出生日期:${(student.birthday?string('yyyy-MM-dd'))?default('')}</td>
        </tr>
        
        <tr>
            <td class="text-left">学生类别:${mcodeSetting.getMcode("DM-XSLB", student.studentType?default(''))}</td>
			<td class="text-left">入学年月:${(student.toSchoolDate?string('yyyy-MM'))?default('')}</td>
            <td class="text-left">港澳台侨外:${mcodeSetting.getMcode("DM-GATQ", (student.compatriots?string("number"))?default(''))}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">籍贯:${student.nativePlace?default('')}
            </td>
            <td class="text-left">政治面貌:${mcodeSetting.getMcode("DM-ZZMM", student.background?default(''))}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">证件类型:${mcodeSetting.getMcode("DM-SFZJLX", student.identitycardType?default(''))}</td>
            <td class="text-left" colspan="2">证件号:${student.identityCard?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">原毕业学校:${student.oldSchoolName?default('')}</td>
            <td class="text-left" colspan="2">一卡通卡号:${student.cardNumber?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">户籍省县:${student.registerPlace?default('')}</td>
            <td class="text-left" colspan="2">户籍镇/街:${student.registerStreet?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">家庭地址:${student.homeAddress?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">家庭邮编:${student.postalcode?default('')}</td>
            <td class="text-left" colspan="2">家庭电话:${student.familyMobile?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">特长爱好:${student.strong?default('')}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">获奖情况:${student.rewardRemark?default('')}</td>
        </tr>
        </tbody>
    </table>
    <table class="table table-bordered table-striped no-margin">
        <tr>
            <td colspan="4" class="text-center" style="background-color: #f9f9f9;">监护人信息</td>
        </tr>
        <tr>
            <td class="text-left" width="30%">监护人姓名:${(student.family1.realName)!}</td>
            <td class="text-left" width="20%">监护人联系电话:${(student.family1.mobilePhone)!}</td>
            <td class="text-left">监护人与学生关系:${mcodeSetting.getMcode("DM-GX", student.family1.relation?default(''))}
            </td>
        </tr>
        <tr>
            <td colspan="3" class="text-center" style="background-color: #f9f9f9;">父亲信息</td>
        </tr>
        <tr>
            <td class="text-left">父亲姓名:${(student.family2.realName)!}</td>
            <td class="text-left">手机号码:${(student.family2.mobilePhone)!}</td>
            <td class="text-left">政治面貌:${mcodeSetting.getMcode("DM-ZZMM", student.family2.politicalStatus?default(''))}
            </td>
        </tr>
        <tr>
        	<td class="text-left">国籍:${mcodeSetting.getMcode("DM-COUNTRY", student.family2.country?default(''))}</td>
            <td class="text-left">出生日期:${(student.family2.birthday?string('yyyy-MM-dd'))?default('')}</td>
            <td class="text-left">文化程度:${mcodeSetting.getMcode("DM-WHCD", student.family2.culture?default(''))}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">证件类型:${mcodeSetting.getMcode("DM-SFZJLX", student.family2.identitycardType?default(''))}</td>
            <td class="text-left">证件号:${(student.family2.identityCard)!}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">工作单位:${(student.family2.company)!}</td>
            <td class="text-left">职务:${(student.family2.duty)!}</td>
        </tr>
        <tr style="background-color: #f9f9f9;">
            <td colspan="3" class="text-center">母亲信息</td>
        </tr>
        <tr>
            <td class="text-left">母亲姓名:${(student.family3.realName)!}</td>
            <td class="text-left">手机号码:${(student.family3.mobilePhone)!}</td>
            <td class="text-left">政治面貌:${mcodeSetting.getMcode("DM-ZZMM", student.family3.politicalStatus?default(''))}
            </td>
        </tr>
        <tr>
        	<td class="text-left">国籍:${mcodeSetting.getMcode("DM-COUNTRY", student.family3.country?default(''))}</td>
            <td class="text-left">出生日期:${(student.family3.birthday?string('yyyy-MM-dd'))?default('')}</td>
            <td class="text-left">文化程度:${mcodeSetting.getMcode("DM-WHCD", student.family3.culture?default(''))}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">证件类型:${mcodeSetting.getMcode("DM-SFZJLX", student.family3.identitycardType?default(''))}</td>
            <td class="text-left">证件号:${(student.family3.identityCard)!}</td>
        </tr>
        <tr>
            <td class="text-left" colspan="2">工作单位:${(student.family3.company)!}</td>
            <td class="text-left">职务:${(student.family3.duty)!}</td>
        </tr>
    </table>
    <table class="table table-bordered table-striped no-margin">
        <tr>
            <td colspan="2" class="text-center" style="background-color: #f9f9f9;">简历信息</td>
        </tr>
        <#if student.studentResumeList?exists && student.studentResumeList?size gt 0>
            <#list student.studentResumeList as resume>
            <tr class="month"  id="resume${resume_index}" >
                <td class="text-left" style="white-space: nowrap">开始年月-结束年月:${(resume.startdate?string('yyyy-MM'))?if_exists}-${(resume.enddate?string('yyyy-MM'))?if_exists}
                </td>
                <td class="text-left">所在学校:${resume.schoolname?default('')}</td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="2" class="text-center">暂无</td>
        </tr>
        </#if>
    </table>
	</div>
	<div style="<#if student_has_next>page-break-before: always;</#if>"></div>
	</#list>
	</#if>
</div>
</div>
</div>
</script>
<!--[if IE]>
<script src="${request.contextPath}/static/ace/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<link rel="stylesheet" href="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.css" />
<script src="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.js"></script>
<script src="${request.contextPath}/static/ace/components/bootstrap/dist/js/bootstrap.js"></script>
<!-- ace scripts -->
<script src="${request.contextPath}/static/ace/assets/js/src/elements.scroller.js"></script>
<script src="${request.contextPath}/static/ace/assets/js/src/ace.js"></script>
<script src="${request.contextPath}/static/ace/assets/js/src/ace.basics.js"></script>
<script src="${request.contextPath}/static/ace/assets/js/src/ace.ajax-content.js"></script>
<script src="${request.contextPath}/static/ace/assets/js/src/ace.widget-on-reload.js"></script>
<script src="${request.contextPath}/static/ace/components/autosize/dist/autosize.js"></script>
</body>
</html>