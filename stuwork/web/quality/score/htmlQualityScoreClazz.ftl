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

<#--		<link rel="stylesheet" href="${request.contextPath}/static/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />-->

        <style>
            .table tr{
                background-color: #fff;
            }
            .table-striped > tbody > tr:nth-of-type(odd) {
              background-color: #fff;
            }
            .table td {
                text-align: center;
                vertical-align: middle!important;
                height: 24px;
                word-wrap:break-word;
                page-break-inside: avoid!important;
            }
            .table {
                font-size:10px;
            }

        </style>
	</head>
<body class="no-skin">
<div class="main-content" style="">
	<div class="main-content-inner">
		<div class="page-content">
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
                <#assign xnxq=dto.xnxq>
                <#assign acadyears=dto.acadyears>
                <#assign yybs=dto.yybs>
                <#assign yyks=dto.yyks>
                <#assign xkjsList=dto.xkjsList>
                <#assign xkcj=dto.xkcj>
                <#assign dyList=dto.dyList>
                <#assign tycj=dto.tycj>
                <#assign myList=dto.myList>
                <div class="box-body" style="page-break-inside: avoid!important;">
                    <table class="table table-bordered table-striped no-margin" style="table-layout:fixed;width: 100%">
                        <tbody>
                        <tr>
                            <td colspan="20" style="font-size: 16px">${graduateYear!}届高中学生综合素质评定成绩
                            </td>
                        </tr>
                        <tr>
                            <td>姓名</td>
                            <td colspan="2">${dto.studentName!}</td>
                            <td>学号</td>
                            <td colspan="2">${dto.studentCode!}</td>
                            <td>性别</td>
                            <td colspan="2">${mcodeSetting.getMcode("DM-XB", (dto.sex?string)?default("0"))}</td>
                            <td>班级</td>
                            <td colspan="2">${dto.className!}</td>
                            <td colspan="2">最终总分</td>
                            <td colspan="2">${dto.totalScore?string("0.##")}</td>
                            <td colspan="4"></td>
                        </tr>
                        <tr>
                            <td colspan="20" style="font-size: 14px">文化成绩</td>
                        </tr>
                        <tr>
                            <td></td>
                            <#if isShow>
                            <td></td>
                            <td colspan="3">${xnxq[0]!}</th>
                            </#if>
                            <td colspan="4"></td>
                            <td colspan="3">${xnxq[1]!}</td>
                            <td colspan="3">${xnxq[2]!}</td>
                            <td colspan="3">${xnxq[3]!}</td>
                            <td colspan="3">${xnxq[4]!}</td>
                            <td colspan="3">${xnxq[5]!}</td>
                        </tr>
                        <tr>
                            <td style="width:5%" rowspan="2">学科成绩折分</td>
                            <td style="width:5%" <#if !isShow>colspan="4"</#if> rowspan="2">${xkcj[0]!}</td>
                            <#if isShow>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td style="width:5%">折分</td>
                            </#if>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td style="width:5%">折分</td>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td style="width:5%">折分</td>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td style="width:5%">折分</td>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td style="width:5%">折分</td>
                            <td style="width:5%">成绩</td>
                            <td style="width:5%">年级排名</td>
                            <td width="">折分</td>
                        </tr>
                        <tr>
                            <#if isShow>
                            <td>${xkcj[1]!}</td>
                            <td>${xkcj[2]!}</td>
                            <td>${xkcj[3]!}</td>
                            </#if>
                            <td>${xkcj[4]!}</td>
                            <td>${xkcj[5]!}</td>
                            <td>${xkcj[6]!}</td>
                            <td>${xkcj[7]!}</td>
                            <td>${xkcj[8]!}</td>
                            <td>${xkcj[9]!}</td>
                            <td>${xkcj[10]!}</td>
                            <td>${xkcj[11]!}</td>
                            <td>${xkcj[12]!}</td>
                            <td>${xkcj[13]!}</td>
                            <td>${xkcj[14]!}</td>
                            <td>${xkcj[15]!}</td>
                            <td>${xkcj[16]!}</td>
                            <td>${xkcj[17]!}</td>
                            <td>${xkcj[18]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="2">英语笔试折分</td>
                            <td <#if !isShow>colspan="4"</#if> rowspan="2">${yybs[0]!}</td>
                            <#if isShow>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            </#if>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <#if isShow>
                            <td>${yybs[1]!}</td>
                            <td>${yybs[2]!}</td>
                            <td>${yybs[3]!}</td>
                            </#if>
                            <td>${yybs[4]!}</td>
                            <td>${yybs[5]!}</td>
                            <td>${yybs[6]!}</td>
                            <td>${yybs[7]!}</td>
                            <td>${yybs[8]!}</td>
                            <td>${yybs[9]!}</td>
                            <td>${yybs[10]!}</td>
                            <td>${yybs[11]!}</td>
                            <td>${yybs[12]!}</td>
                            <td>${yybs[13]!}</td>
                            <td>${yybs[14]!}</td>
                            <td>${yybs[15]!}</td>
                            <td>${yybs[16]!}</td>
                            <td>${yybs[17]!}</td>
                            <td>${yybs[18]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="2">英语口试折分</td>
                            <td <#if !isShow>colspan="4"</#if> rowspan="2">${yyks[0]!}</td>
                            <#if isShow>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            </#if>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                            <td>成绩</td>
                            <td>年级排名</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <#if isShow>
                            <td>${yyks[1]!}</td>
                            <td>${yyks[2]!}</td>
                            <td>${yyks[3]!}</td>
                            </#if>
                            <td>${yyks[4]!}</td>
                            <td>${yyks[5]!}</td>
                            <td>${yyks[6]!}</td>
                            <td>${yyks[7]!}</td>
                            <td>${yyks[8]!}</td>
                            <td>${yyks[9]!}</td>
                            <td>${yyks[10]!}</td>
                            <td>${yyks[11]!}</td>
                            <td>${yyks[12]!}</td>
                            <td>${yyks[13]!}</td>
                            <td>${yyks[14]!}</td>
                            <td>${yyks[15]!}</td>
                            <td>${yyks[16]!}</td>
                            <td>${yyks[17]!}</td>
                            <td>${yyks[18]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="2">学科竞赛</td>
                            <td <#if !isShow>colspan="4"</#if> rowspan="2">${dto.xkjsScore?string("0.##")}</td>
                            <#if isShow>
                            <td width="10.5%" colspan="2">${acadyears[0]!}学年</td>
                            <td>折分</td>
                            </#if>
                            <td width="25%" colspan="5">${acadyears[1]!}学年</td>
                            <td>折分</td>
                            <td width="25%" colspan="5">${acadyears[2]!}学年</td>
                            <td>折分</td>
                            <td width="10.5%" colspan="2">${acadyears[3]!}学年</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <#if isShow>
                            <td colspan="2">${xkjsList[0][0]!}</td>
                            <td>${xkjsList[0][1]!}</td>
                            </#if>
                            <td colspan="5">${xkjsList[1][0]!}</td>
                            <td>${xkjsList[1][1]!}</td>
                            <td colspan="5">${xkjsList[2][0]!}</td>
                            <td>${xkjsList[2][1]!}</td>
                            <td colspan="2">${xkjsList[3][0]!}</td>
                            <td>${xkjsList[3][1]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="2" colspan="5">学考折分</td>
                            <td rowspan="2" colspan="2">${dto.xkScore!}</td>
                            <td rowspan="2" colspan="13">${dto.xkResult!}</td>
                        </tr>
                        <tr></tr>
                        <tr>
                            <td colspan="20" style="font-size: 14px">德育成绩</td>
                        </tr>
                        <#if isShow>
                        <tr>
                            <td rowspan="16">德育成绩</td>
                            <td rowspan="16" colspan="2">${dto.dyScore?string("0.##")}</td>
                            <td rowspan="4" colspan="2">${acadyears[0]!}学年</td>
                            <td colspan="2">操行等第</td>
                            <td>折分</td>
                            <td colspan="2">学生干部</td>
                            <td>折分</td>
                            <td colspan="2">社团骨干</td>
                            <td>折分</td>
                            <td colspan="2">突出贡献</td>
                            <td>折分</td>
                            <td colspan="2">违纪处罚</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[0][0]!}</td>
                            <td>${dyList[0][1]!}</td>
                            <td colspan="2">${dyList[0][2]!}</td>
                            <td>${dyList[0][3]!}</td>
                            <td colspan="2">${dyList[0][4]!}</td>
                            <td>${dyList[0][5]!}</td>
                            <td colspan="2">${dyList[0][6]!}</td>
                            <td>${dyList[0][7]!}</td>
                            <td colspan="2">${dyList[0][8]!}</td>
                            <td>${dyList[0][9]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">值周表现</td>
                            <td>折分</td>
                            <td colspan="2">军训</td>
                            <td>折分</td>
                            <td colspan="2">学农</td>
                            <td>折分</td>
                            <td colspan="2">评优</td>
                            <td>折分</td>
                            <td colspan="2">先进</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[0][10]!}</td>
                            <td>${dyList[0][11]!}</td>
                            <td colspan="2">${dyList[0][12]!}</td>
                            <td>${dyList[0][13]!}</td>
                            <td colspan="2">${dyList[0][14]!}</td>
                            <td>${dyList[0][15]!}</td>
                            <td colspan="2">${dyList[0][16]!}</td>
                            <td>${dyList[0][17]!}</td>
                            <td colspan="2">${dyList[0][18]!}</td>
                            <td>${dyList[0][19]!}</td>
                        </tr>
                        </#if>
                        <tr>
                            <#if !isShow>
                            <td rowspan="12">德育成绩</td>
                            <td rowspan="12" colspan="2">${dto.dyScore?string("0.##")}</td>
                            </#if>
                            <td rowspan="4" colspan="2">${acadyears[1]!}学年</td>
                            <td colspan="2">操行等第</td>
                            <td>折分</td>
                            <td colspan="2">学生干部</td>
                            <td>折分</td>
                            <td colspan="2">社团骨干</td>
                            <td>折分</td>
                            <td colspan="2">突出贡献</td>
                            <td>折分</td>
                            <td colspan="2">违纪处罚</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[1][0]!}</td>
                            <td>${dyList[1][1]!}</td>
                            <td colspan="2">${dyList[1][2]!}</td>
                            <td>${dyList[1][3]!}</td>
                            <td colspan="2">${dyList[1][4]!}</td>
                            <td>${dyList[1][5]!}</td>
                            <td colspan="2">${dyList[1][6]!}</td>
                            <td>${dyList[1][7]!}</td>
                            <td colspan="2">${dyList[1][8]!}</td>
                            <td>${dyList[1][9]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">值周表现</td>
                            <td>折分</td>
                            <td colspan="2">军训</td>
                            <td>折分</td>
                            <td colspan="2">学农</td>
                            <td>折分</td>
                            <td colspan="2">评优</td>
                            <td>折分</td>
                            <td colspan="2">先进</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[1][10]!}</td>
                            <td>${dyList[1][11]!}</td>
                            <td colspan="2">${dyList[1][12]!}</td>
                            <td>${dyList[1][13]!}</td>
                            <td colspan="2">${dyList[1][14]!}</td>
                            <td>${dyList[1][15]!}</td>
                            <td colspan="2">${dyList[1][16]!}</td>
                            <td>${dyList[1][17]!}</td>
                            <td colspan="2">${dyList[1][18]!}</td>
                            <td>${dyList[1][19]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="4" colspan="2">${acadyears[2]!}学年</td>
                            <td colspan="2">操行等第</td>
                            <td>折分</td>
                            <td colspan="2">学生干部</td>
                            <td>折分</td>
                            <td colspan="2">社团骨干</td>
                            <td>折分</td>
                            <td colspan="2">突出贡献</td>
                            <td>折分</td>
                            <td colspan="2">违纪处罚</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[2][0]!}</td>
                            <td>${dyList[2][1]!}</td>
                            <td colspan="2">${dyList[2][2]!}</td>
                            <td>${dyList[2][3]!}</td>
                            <td colspan="2">${dyList[2][4]!}</td>
                            <td>${dyList[2][5]!}</td>
                            <td colspan="2">${dyList[2][6]!}</td>
                            <td>${dyList[2][7]!}</td>
                            <td colspan="2">${dyList[2][8]!}</td>
                            <td>${dyList[2][9]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">值周表现</td>
                            <td>折分</td>
                            <td colspan="2">军训</td>
                            <td>折分</td>
                            <td colspan="2">学农</td>
                            <td>折分</td>
                            <td colspan="2">评优</td>
                            <td>折分</td>
                            <td colspan="2">先进</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[2][10]!}</td>
                            <td>${dyList[2][11]!}</td>
                            <td colspan="2">${dyList[2][12]!}</td>
                            <td>${dyList[2][13]!}</td>
                            <td colspan="2">${dyList[2][14]!}</td>
                            <td>${dyList[2][15]!}</td>
                            <td colspan="2">${dyList[2][16]!}</td>
                            <td>${dyList[2][17]!}</td>
                            <td colspan="2">${dyList[2][18]!}</td>
                            <td>${dyList[2][19]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="4" colspan="2">${acadyears[3]!}学年</td>
                            <td colspan="2">操行等第</td>
                            <td>折分</td>
                            <td colspan="2">学生干部</td>
                            <td>折分</td>
                            <td colspan="2">社团骨干</td>
                            <td>折分</td>
                            <td colspan="2">突出贡献</td>
                            <td>折分</td>
                            <td colspan="2">违纪处罚</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[3][0]!}</td>
                            <td>${dyList[3][1]!}</td>
                            <td colspan="2">${dyList[3][2]!}</td>
                            <td>${dyList[3][3]!}</td>
                            <td colspan="2">${dyList[3][4]!}</td>
                            <td>${dyList[3][5]!}</td>
                            <td colspan="2">${dyList[3][6]!}</td>
                            <td>${dyList[3][7]!}</td>
                            <td colspan="2">${dyList[3][8]!}</td>
                            <td>${dyList[3][9]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">值周表现</td>
                            <td>折分</td>
                            <td colspan="2">军训</td>
                            <td>折分</td>
                            <td colspan="2">学农</td>
                            <td>折分</td>
                            <td colspan="2">评优</td>
                            <td>折分</td>
                            <td colspan="2">先进</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${dyList[3][10]!}</td>
                            <td>${dyList[3][11]!}</td>
                            <td colspan="2">${dyList[3][12]!}</td>
                            <td>${dyList[3][13]!}</td>
                            <td colspan="2">${dyList[3][14]!}</td>
                            <td>${dyList[3][15]!}</td>
                            <td colspan="2">${dyList[3][16]!}</td>
                            <td>${dyList[3][17]!}</td>
                            <td colspan="2">${dyList[3][18]!}</td>
                            <td>${dyList[3][19]!}</td>
                        </tr>
                        <tr>
                            <td colspan="20" style="font-size: 14px">体育、美育成绩</td>
                        </tr>
                        <tr>
                            <td rowspan="2">体育</td>
                            <td rowspan="2" colspan="2">${tycj[0]!}</td>
                            <td rowspan="2" colspan="2">${acadyears[0]!}学年</td>
                            <td colspan="2">实分1</td>
                            <td>折分</td>
                            <td rowspan="2">${acadyears[1]!}学年</td>
                            <td>实分1</td>
                            <td>实分2</td>
                            <td>折分</td>
                            <td rowspan="2">${acadyears[2]!}学年</td>
                            <td>实分1</td>
                            <td>实分2</td>
                            <td>折分</td>
                            <td rowspan="2">${acadyears[3]!}学年</td>
                            <td colspan="2">实分1</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${tycj[2]!}</td>
                            <td>${tycj[3]!}</td>
                            <td>${tycj[5]!}</td>
                            <td>${tycj[6]!}</td>
                            <td>${tycj[7]!}</td>
                            <td>${tycj[9]!}</td>
                            <td>${tycj[10]!}</td>
                            <td>${tycj[11]!}</td>
                            <td colspan="2">${tycj[13]!}</td>
                            <td>${tycj[14]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="24">各大节日及全校性活动</td>
                            <td rowspan="24" colspan="2">${dto.myScore?string("0.##")}</td>
                            <td rowspan="6" colspan="2">${acadyears[0]!}学年</td>
                            <td colspan="2">体育节-个人</td>
                            <td>折分</td>
                            <td colspan="3">体育节-团体</td>
                            <td>折分</td>
                            <td colspan="3">外语节-个人</td>
                            <td>折分</td>
                            <td colspan="3">外语节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[0][0]!}</td>
                            <td>${myList[0][1]!}</td>
                            <td colspan="3">${myList[0][2]!}</td>
                            <td>${myList[0][3]!}</td>
                            <td colspan="3">${myList[0][4]!}</td>
                            <td>${myList[0][5]!}</td>
                            <td colspan="3">${myList[0][6]!}</td>
                            <td>${myList[0][7]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">艺术节-个人</td>
                            <td>折分</td>
                            <td colspan="3">艺术节-团体</td>
                            <td>折分</td>
                            <td colspan="3">科技节-个人</td>
                            <td>折分</td>
                            <td colspan="3">科技节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[0][8]!}</td>
                            <td>${myList[0][9]!}</td>
                            <td colspan="3">${myList[0][10]!}</td>
                            <td>${myList[0][11]!}</td>
                            <td colspan="3">${myList[0][12]!}</td>
                            <td>${myList[0][13]!}</td>
                            <td colspan="3">${myList[0][14]!}</td>
                            <td>${myList[0][15]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">文化节-个人</td>
                            <td>折分</td>
                            <td colspan="3">文化节-团体</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-个人</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[0][16]!}</td>
                            <td>${myList[0][17]!}</td>
                            <td colspan="3">${myList[0][18]!}</td>
                            <td>${myList[0][19]!}</td>
                            <td colspan="3">${myList[0][20]!}</td>
                            <td>${myList[0][21]!}</td>
                            <td colspan="3">${myList[0][22]!}</td>
                            <td>${myList[0][23]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="6" colspan="2">${acadyears[1]!}学年</td>
                            <td colspan="2">体育节-个人</td>
                            <td>折分</td>
                            <td colspan="3">体育节-团体</td>
                            <td>折分</td>
                            <td colspan="3">外语节-个人</td>
                            <td>折分</td>
                            <td colspan="3">外语节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[1][0]!}</td>
                            <td>${myList[1][1]!}</td>
                            <td colspan="3">${myList[1][2]!}</td>
                            <td>${myList[1][3]!}</td>
                            <td colspan="3">${myList[1][4]!}</td>
                            <td>${myList[1][5]!}</td>
                            <td colspan="3">${myList[1][6]!}</td>
                            <td>${myList[1][7]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">艺术节-个人</td>
                            <td>折分</td>
                            <td colspan="3">艺术节-团体</td>
                            <td>折分</td>
                            <td colspan="3">科技节-个人</td>
                            <td>折分</td>
                            <td colspan="3">科技节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[1][8]!}</td>
                            <td>${myList[1][9]!}</td>
                            <td colspan="3">${myList[1][10]!}</td>
                            <td>${myList[1][11]!}</td>
                            <td colspan="3">${myList[1][12]!}</td>
                            <td>${myList[1][13]!}</td>
                            <td colspan="3">${myList[1][14]!}</td>
                            <td>${myList[1][15]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">文化节-个人</td>
                            <td>折分</td>
                            <td colspan="3">文化节-团体</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-个人</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[1][16]!}</td>
                            <td>${myList[1][17]!}</td>
                            <td colspan="3">${myList[1][18]!}</td>
                            <td>${myList[1][19]!}</td>
                            <td colspan="3">${myList[1][20]!}</td>
                            <td>${myList[1][21]!}</td>
                            <td colspan="3">${myList[1][22]!}</td>
                            <td>${myList[1][23]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="6" colspan="2">${acadyears[2]!}学年</td>
                            <td colspan="2">体育节-个人</td>
                            <td>折分</td>
                            <td colspan="3">体育节-团体</td>
                            <td>折分</td>
                            <td colspan="3">外语节-个人</td>
                            <td>折分</td>
                            <td colspan="3">外语节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[2][0]!}</td>
                            <td>${myList[2][1]!}</td>
                            <td colspan="3">${myList[2][2]!}</td>
                            <td>${myList[2][3]!}</td>
                            <td colspan="3">${myList[2][4]!}</td>
                            <td>${myList[2][5]!}</td>
                            <td colspan="3">${myList[2][6]!}</td>
                            <td>${myList[2][7]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">艺术节-个人</td>
                            <td>折分</td>
                            <td colspan="3">艺术节-团体</td>
                            <td>折分</td>
                            <td colspan="3">科技节-个人</td>
                            <td>折分</td>
                            <td colspan="3">科技节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[2][8]!}</td>
                            <td>${myList[2][9]!}</td>
                            <td colspan="3">${myList[2][10]!}</td>
                            <td>${myList[2][11]!}</td>
                            <td colspan="3">${myList[2][12]!}</td>
                            <td>${myList[2][13]!}</td>
                            <td colspan="3">${myList[2][14]!}</td>
                            <td>${myList[2][15]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">文化节-个人</td>
                            <td>折分</td>
                            <td colspan="3">文化节-团体</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-个人</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-团体</td>
                            <td>折分</td>

                        </tr>
                        <tr>
                            <td colspan="2">${myList[2][16]!}</td>
                            <td>${myList[2][17]!}</td>
                            <td colspan="3">${myList[2][18]!}</td>
                            <td>${myList[2][19]!}</td>
                            <td colspan="3">${myList[2][20]!}</td>
                            <td>${myList[2][21]!}</td>
                            <td colspan="3">${myList[2][22]!}</td>
                            <td>${myList[2][23]!}</td>
                        </tr>
                        <tr>
                            <td rowspan="6" colspan="2">${acadyears[3]!}学年</td>
                            <td colspan="2">体育节-个人</td>
                            <td>折分</td>
                            <td colspan="3">体育节-团体</td>
                            <td>折分</td>
                            <td colspan="3">外语节-个人</td>
                            <td>折分</td>
                            <td colspan="3">外语节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[3][0]!}</td>
                            <td>${myList[3][1]!}</td>
                            <td colspan="3">${myList[3][2]!}</td>
                            <td>${myList[3][3]!}</td>
                            <td colspan="3">${myList[3][4]!}</td>
                            <td>${myList[3][5]!}</td>
                            <td colspan="3">${myList[3][6]!}</td>
                            <td>${myList[3][7]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">艺术节-个人</td>
                            <td>折分</td>
                            <td colspan="3">艺术节-团体</td>
                            <td>折分</td>
                            <td colspan="3">科技节-个人</td>
                            <td>折分</td>
                            <td colspan="3">科技节-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[3][8]!}</td>
                            <td>${myList[3][9]!}</td>
                            <td colspan="3">${myList[3][10]!}</td>
                            <td>${myList[3][11]!}</td>
                            <td colspan="3">${myList[3][12]!}</td>
                            <td>${myList[3][13]!}</td>
                            <td colspan="3">${myList[3][14]!}</td>
                            <td>${myList[3][15]!}</td>
                        </tr>
                        <tr>
                            <td colspan="2">文化节-个人</td>
                            <td>折分</td>
                            <td colspan="3">文化节-团体</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-个人</td>
                            <td>折分</td>
                            <td colspan="3">全校性活动-团体</td>
                            <td>折分</td>
                        </tr>
                        <tr>
                            <td colspan="2">${myList[3][16]!}</td>
                            <td>${myList[3][17]!}</td>
                            <td colspan="3">${myList[3][18]!}</td>
                            <td>${myList[3][19]!}</td>
                            <td colspan="3">${myList[3][20]!}</td>
                            <td>${myList[3][21]!}</td>
                            <td colspan="3">${myList[3][22]!}</td>
                            <td>${myList[3][23]!}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
               <#-- <div style="height: 340mm;width:100%;page-break-inside: avoid!important;">
                    <table class="table table-striped no-margin" style="table-layout:fixed;height: 100%;">
                        <tr>
                            <td style="height:100%;">
                            </td>
                        </tr>
                    </table>
                </div>-->
            <div style="height: 340mm;width:100%;page-break-inside: avoid!important;">
            </#list>
            </#if>
         </div>
    </div>
</div>
</div>
<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
$(function(){

})
</body>
</html>