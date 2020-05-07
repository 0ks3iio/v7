<#--<a href="javascript:void(0);" class="page-back-btn" onclick="backIndex()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<p>${student.studentName!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学号：</span>
				<div class="filter-content">
					<p>${student.studentCode!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">性别：</span>
				<div class="filter-content">
					<p>${mcodeSetting.getMcode("DM-XB","${student.sex!}")}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">行政班：</span>
				<div class="filter-content">
					<p>${student.className!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">总分：</span>
				<div class="filter-content">
					<p>
						${totalScore?string("0.##")}
					</p>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a a href="javascript:void(0);" onclick="doExport('${student.id}')" class="btn btn-blue">导出PDF</a>
			</div>
		</div>
		
		<h3>文化成绩折分</h3>
		<table class="table table-bordered" style="word-break:break-all;">
			<thead>
				<tr>
					<th width="6%">项目</th>
					<th width="5%">总得分</th>
					<#if isShow>
					<th colspan="3">${xnxq[0]!}</th>
					</#if>
					<th colspan="3">${xnxq[1]!}</th>
					<th colspan="3">${xnxq[2]!}</th>
					<th colspan="3">${xnxq[3]!}</th>
					<th colspan="3">${xnxq[4]!}</th>
					<th colspan="3">${xnxq[5]!}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td rowspan="2">学科成绩折分</td>
					<td rowspan="2">${xkcj[0]!}</td>
					<#if isShow>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
					</#if>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
					<td>成绩</td>
					<td width="5%">年级排名</td>
					<td>折分</td>
				</tr>
				<tr>
					<#if isShow>
					<td>&nbsp;${xkcj[1]!}</td>
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
					<td rowspan="2">${yybs[0]!}</td>
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
					<td>&nbsp;${yybs[1]!}</td>
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
					<td rowspan="2">${yyks[0]!}</td>
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
					<td>&nbsp;${yyks[1]!}</td>
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
					<td rowspan="2">学科竞赛折分</td>
					<td rowspan="2">${xkjsScore?string("0.##")}</td>
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
			</tbody>
		</table>

        <table class="table table-bordered" style="word-break:break-all;">
            <#--<thead>
                <tr>
                    <th>学考科目</th>
                    <#if courseList?exists && courseList?size gt 0>
                        <#list courseList as item>
                            <th>${item.subjectName!}</th>
                        </#list>
                    </#if>
                </tr>
            </thead>-->
            <tbody>
                <#--<tr>
                    <td>得分</td>
                    <#if courseList?exists && courseList?size gt 0>
                        <#list courseList as item>
                            <td>${xkMap[item.id]!}</td>
                        </#list>
                    </#if>
                </tr>-->
                <tr>
                    <td width="15%">
                        学考折分
                    </td>
                    <td width="15%">
                        ${xkTotalScore!}
                    </td>
                    <td>
                        ${xkResult!}
                    </td>
                </tr>
            </tbody>
        </table>

		<h3>德育成绩折分</h3>
		<table class="table table-bordered" style="word-break:break-all;">
			<tbody>
				<#if isShow>
				<tr>
					<td width="6%" rowspan="18">德育成绩折分</td>
					<td width="5%" rowspan="18">${dyScore?string("0.##")}</td>
					<td width="8%" rowspan="4">${acadyears[0]!}学年</td>
					<td width="12%">操行等第</td>
					<td width="4.2%">折分</td>
					<td width="12%">学生干部</td>
					<td width="4.2%">折分</td>
					<td width="12%">社团骨干</td>
					<td width="4.2%">折分</td>
					<td width="12%">突出贡献</td>
					<td width="4.2%">折分</td>
					<td width="12%">违纪处罚</td>
					<td width="4.2%">折分</td>
				</tr>
				<tr>
					<td>${dyList[0][0]!}</td>
					<td>${dyList[0][1]!}</td>
					<td>${dyList[0][2]!}</td>
					<td>${dyList[0][3]!}</td>
					<td>${dyList[0][4]!}</td>
					<td>${dyList[0][5]!}</td>
					<td>${dyList[0][6]!}</td>
					<td>${dyList[0][7]!}</td>
					<td>${dyList[0][8]!}</td>
					<td>${dyList[0][9]!}</td>
				</tr>
				<tr>
					<td>值周表现</td>
					<td>折分</td>
					<td>军训</td>
					<td>折分</td>
					<td>学农</td>
					<td>折分</td>
					<td>评优</td>
					<td>折分</td>
					<td>先进</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[0][10]!}</td>
					<td>${dyList[0][11]!}</td>
					<td>${dyList[0][12]!}</td>
					<td>${dyList[0][13]!}</td>
					<td>${dyList[0][14]!}</td>
					<td>${dyList[0][15]!}</td>
					<td>${dyList[0][16]!}</td>
					<td>${dyList[0][17]!}</td>
					<td>${dyList[0][18]!}</td>
					<td>${dyList[0][19]!}</td>
				</tr>
				</#if>
				<tr>
					<#if !isShow>
					<td width="6%" rowspan="18">德育成绩折分</td>
					<td width="5%" rowspan="18">${dyScore?string("0.##")}</td>
					<td width="8%" rowspan="4">${acadyears[1]!}学年</td>
					<td width="12%">操行等第</td>
					<td width="4.2%">折分</td>
					<td width="12%">学生干部</td>
					<td width="4.2%">折分</td>
					<td width="12%">社团骨干</td>
					<td width="4.2%">折分</td>
					<td width="12%">突出贡献</td>
					<td width="4.2%">折分</td>
					<td width="12%">违纪处罚</td>
					<td width="4.2%">折分</td>
					<#else>
					<td rowspan="4">${acadyears[1]!}学年</td>
					<td>操行等第</td>
					<td>折分</td>
					<td>学生干部</td>
					<td>折分</td>
					<td>社团骨干</td>
					<td>折分</td>
					<td>突出贡献</td>
					<td>折分</td>
					<td>违纪处罚</td>
					<td>折分</td>
					</#if>
				</tr>
				<tr>
					<td>${dyList[1][0]!}</td>
					<td>${dyList[1][1]!}</td>
					<td>${dyList[1][2]!}</td>
					<td>${dyList[1][3]!}</td>
					<td>${dyList[1][4]!}</td>
					<td>${dyList[1][5]!}</td>
					<td>${dyList[1][6]!}</td>
					<td>${dyList[1][7]!}</td>
					<td>${dyList[1][8]!}</td>
					<td>${dyList[1][9]!}</td>
				</tr>
				<tr>
					<td>值周表现</td>
					<td>折分</td>
					<td>军训</td>
					<td>折分</td>
					<td>学农</td>
					<td>折分</td>
					<td>评优</td>
					<td>折分</td>
					<td>先进</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[1][10]!}</td>
					<td>${dyList[1][11]!}</td>
					<td>${dyList[1][12]!}</td>
					<td>${dyList[1][13]!}</td>
					<td>${dyList[1][14]!}</td>
					<td>${dyList[1][15]!}</td>
					<td>${dyList[1][16]!}</td>
					<td>${dyList[1][17]!}</td>
					<td>${dyList[1][18]!}</td>
					<td>${dyList[1][19]!}</td>
				</tr>
				<tr>
					<td rowspan="4">${acadyears[2]!}学年</td>
					<td>操行等第</td>
					<td>折分</td>
					<td>学生干部</td>
					<td>折分</td>
					<td>社团骨干</td>
					<td>折分</td>
					<td>突出贡献</td>
					<td>折分</td>
					<td>违纪处罚</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[2][0]!}</td>
					<td>${dyList[2][1]!}</td>
					<td>${dyList[2][2]!}</td>
					<td>${dyList[2][3]!}</td>
					<td>${dyList[2][4]!}</td>
					<td>${dyList[2][5]!}</td>
					<td>${dyList[2][6]!}</td>
					<td>${dyList[2][7]!}</td>
					<td>${dyList[2][8]!}</td>
					<td>${dyList[2][9]!}</td>
				</tr>
				<tr>
					<td>值周表现</td>
					<td>折分</td>
					<td>军训</td>
					<td>折分</td>
					<td>学农</td>
					<td>折分</td>
					<td>评优</td>
					<td>折分</td>
					<td>先进</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[2][10]!}</td>
					<td>${dyList[2][11]!}</td>
					<td>${dyList[2][12]!}</td>
					<td>${dyList[2][13]!}</td>
					<td>${dyList[2][14]!}</td>
					<td>${dyList[2][15]!}</td>
					<td>${dyList[2][16]!}</td>
					<td>${dyList[2][17]!}</td>
					<td>${dyList[2][18]!}</td>
					<td>${dyList[2][19]!}</td>
				</tr>
				<tr>
					<td rowspan="4">${acadyears[3]!}学年</td>
					<td>操行等第</td>
					<td>折分</td>
					<td>学生干部</td>
					<td>折分</td>
					<td>社团骨干</td>
					<td>折分</td>
					<td>突出贡献</td>
					<td>折分</td>
					<td>违纪处罚</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[3][0]!}</td>
					<td>${dyList[3][1]!}</td>
					<td>${dyList[3][2]!}</td>
					<td>${dyList[3][3]!}</td>
					<td>${dyList[3][4]!}</td>
					<td>${dyList[3][5]!}</td>
					<td>${dyList[3][6]!}</td>
					<td>${dyList[3][7]!}</td>
					<td>${dyList[3][8]!}</td>
					<td>${dyList[3][9]!}</td>
				</tr>
				<tr>
					<td>值周表现</td>
					<td>折分</td>
					<td>军训</td>
					<td>折分</td>
					<td>学农</td>
					<td>折分</td>
					<td>评优</td>
					<td>折分</td>
					<td>先进</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${dyList[3][10]!}</td>
					<td>${dyList[3][11]!}</td>
					<td>${dyList[3][12]!}</td>
					<td>${dyList[3][13]!}</td>
					<td>${dyList[3][14]!}</td>
					<td>${dyList[3][15]!}</td>
					<td>${dyList[3][16]!}</td>
					<td>${dyList[3][17]!}</td>
					<td>${dyList[3][18]!}</td>
					<td>${dyList[3][19]!}</td>
				</tr>
			</tbody>
		</table>

		<h3>体育成绩折分</h3>
		<table class="table table-bordered" style="word-break:break-all;">
			<thead>
				<tr>
					<th width="6%">项目</th>
					<th width="5%">总得分</th>
					<#if isShow>
					<th colspan="2">${tycj[1]!}</th>
					</#if>
					<th colspan="3">${tycj[4]!}</th>
					<th colspan="3">${tycj[8]!}</th>
					<th colspan="2">${tycj[12]!}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td rowspan="2">体育成绩折分</td>
					<td rowspan="2">${tycj[0]!}</td>
					<#if isShow>
					<td>实分1</td>
					<td>折分</td>
					</#if>
					<td>实分1</td>
					<td>实分2</td>
					<td>折分</td>
					<td>实分1</td>
					<td>实分2</td>
					<td>折分</td>
					<td>实分1</td>
					<td>折分</td>
				</tr>
				<tr>
					<#if isShow>
					<td>&nbsp;${tycj[2]!}</td>
					<td>${tycj[3]!}</td>
					</#if>
					<td>${tycj[5]!}</td>
					<td>${tycj[6]!}</td>
					<td>${tycj[7]!}</td>
					<td>${tycj[9]!}</td>
					<td>${tycj[10]!}</td>
					<td>${tycj[11]!}</td>
					<td>${tycj[13]!}</td>
					<td>${tycj[14]!}</td>
				</tr>
			</tbody>
		</table>

		<h3>美育成绩折分</h3>
		<table class="table table-bordered" style="word-break:break-all;">
			<tbody>
				<#if isShow>
				<tr>
					<td width="6%" rowspan="24">美育成绩折分</td>
					<td width="5%" rowspan="24">${myScore?string("0.##")}</td>
					<td width="8%" rowspan="6">${acadyears[0]!}学年</td>
					<td width="16%">体育节-个人</td>
					<td width="4.25%">折分</td>
					<td width="16%">体育节-团体</td>
					<td width="4.25%">折分</td>
					<td width="16%">外语节-个人</td>
					<td width="4.25%">折分</td>
					<td width="16%">外语节-团体</td>
					<td width="4.25%">折分</td>
				</tr>
				<tr>
					<td>${myList[0][0]!}</td>
					<td>${myList[0][1]!}</td>
					<td>${myList[0][2]!}</td>
					<td>${myList[0][3]!}</td>
					<td>${myList[0][4]!}</td>
					<td>${myList[0][5]!}</td>
					<td>${myList[0][6]!}</td>
					<td>${myList[0][7]!}</td>
				</tr>
				<tr>
					<td>艺术节-个人</td>
					<td>折分</td>
					<td>艺术节-团体</td>
					<td>折分</td>
					<td>科技节-个人</td>
					<td>折分</td>
					<td>科技节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[0][8]!}</td>
					<td>${myList[0][9]!}</td>
					<td>${myList[0][10]!}</td>
					<td>${myList[0][11]!}</td>
					<td>${myList[0][12]!}</td>
					<td>${myList[0][13]!}</td>
					<td>${myList[0][14]!}</td>
					<td>${myList[0][15]!}</td>
				</tr>
				<tr>
					<td>文化节-个人</td>
					<td>折分</td>
					<td>文化节-团体</td>
					<td>折分</td>
					<td>全校性活动-个人</td>
					<td>折分</td>
					<td>全校性活动-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[0][16]!}</td>
					<td>${myList[0][17]!}</td>
					<td>${myList[0][18]!}</td>
					<td>${myList[0][19]!}</td>
					<td>${myList[0][20]!}</td>
					<td>${myList[0][21]!}</td>
					<td>${myList[0][22]!}</td>
					<td>${myList[0][23]!}</td>
				</tr>
				</#if>
				<tr>
					<#if !isShow>
					<td width="6%" rowspan="24">美育成绩折分</td>
					<td width="5%" rowspan="24">${myScore?string("0.##")}</td>
					<td width="8%" rowspan="6">${acadyears[1]!}学年</td>
					<td width="16%">体育节-个人</td>
					<td width="4.25%">折分</td>
					<td width="16%">体育节-团体</td>
					<td width="4.25%">折分</td>
					<td width="16%">外语节-个人</td>
					<td width="4.25%">折分</td>
					<td width="16%">外语节-团体</td>
					<td width="4.25%">折分</td>
					<#else>
					<td rowspan="6">${acadyears[1]!}学年</td>
					<td>体育节-个人</td>
					<td>折分</td>
					<td>体育节-团体</td>
					<td>折分</td>
					<td>外语节-个人</td>
					<td>折分</td>
					<td>外语节-团体</td>
					<td>折分</td>
					</#if>
				</tr>
				<tr>
					<td>${myList[1][0]!}</td>
					<td>${myList[1][1]!}</td>
					<td>${myList[1][2]!}</td>
					<td>${myList[1][3]!}</td>
					<td>${myList[1][4]!}</td>
					<td>${myList[1][5]!}</td>
					<td>${myList[1][6]!}</td>
					<td>${myList[1][7]!}</td>
				</tr>
				<tr>
					<td>艺术节-个人</td>
					<td>折分</td>
					<td>艺术节-团体</td>
					<td>折分</td>
					<td>科技节-个人</td>
					<td>折分</td>
					<td>科技节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[1][8]!}</td>
					<td>${myList[1][9]!}</td>
					<td>${myList[1][10]!}</td>
					<td>${myList[1][11]!}</td>
					<td>${myList[1][12]!}</td>
					<td>${myList[1][13]!}</td>
					<td>${myList[1][14]!}</td>
					<td>${myList[1][15]!}</td>
				</tr>
				<tr>
					<td>文化节-个人</td>
					<td>折分</td>
					<td>文化节-团体</td>
					<td>折分</td>
					<td>全校性活动-个人</td>
					<td>折分</td>
					<td>全校性活动-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[1][16]!}</td>
					<td>${myList[1][17]!}</td>
					<td>${myList[1][18]!}</td>
					<td>${myList[1][19]!}</td>
					<td>${myList[1][20]!}</td>
					<td>${myList[1][21]!}</td>
					<td>${myList[1][22]!}</td>
					<td>${myList[1][23]!}</td>
				</tr>
				<tr>
					<td rowspan="6">${acadyears[2]!}学年</td>
					<td>体育节-个人</td>
					<td>折分</td>
					<td>体育节-团体</td>
					<td>折分</td>
					<td>外语节-个人</td>
					<td>折分</td>
					<td>外语节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[2][0]!}</td>
					<td>${myList[2][1]!}</td>
					<td>${myList[2][2]!}</td>
					<td>${myList[2][3]!}</td>
					<td>${myList[2][4]!}</td>
					<td>${myList[2][5]!}</td>
					<td>${myList[2][6]!}</td>
					<td>${myList[2][7]!}</td>
				</tr>
				<tr>
					<td>艺术节-个人</td>
					<td>折分</td>
					<td>艺术节-团体</td>
					<td>折分</td>
					<td>科技节-个人</td>
					<td>折分</td>
					<td>科技节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[2][8]!}</td>
					<td>${myList[2][9]!}</td>
					<td>${myList[2][10]!}</td>
					<td>${myList[2][11]!}</td>
					<td>${myList[2][12]!}</td>
					<td>${myList[2][13]!}</td>
					<td>${myList[2][14]!}</td>
					<td>${myList[2][15]!}</td>
				</tr>
				<tr>
					<td>文化节-个人</td>
					<td>折分</td>
					<td>文化节-团体</td>
					<td>折分</td>
					<td>全校性活动-个人</td>
					<td>折分</td>
					<td>全校性活动-团体</td>
					<td>折分</td>
					
				</tr>
				<tr>
					<td>${myList[2][16]!}</td>
					<td>${myList[2][17]!}</td>
					<td>${myList[2][18]!}</td>
					<td>${myList[2][19]!}</td>
					<td>${myList[2][20]!}</td>
					<td>${myList[2][21]!}</td>
					<td>${myList[2][22]!}</td>
					<td>${myList[2][23]!}</td>
				</tr>
				<tr>
					<td rowspan="6">${acadyears[3]!}学年</td>
					<td>体育节-个人</td>
					<td>折分</td>
					<td>体育节-团体</td>
					<td>折分</td>
					<td>外语节-个人</td>
					<td>折分</td>
					<td>外语节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[3][0]!}</td>
					<td>${myList[3][1]!}</td>
					<td>${myList[3][2]!}</td>
					<td>${myList[3][3]!}</td>
					<td>${myList[3][4]!}</td>
					<td>${myList[3][5]!}</td>
					<td>${myList[3][6]!}</td>
					<td>${myList[3][7]!}</td>
				</tr>
				<tr>
					<td>艺术节-个人</td>
					<td>折分</td>
					<td>艺术节-团体</td>
					<td>折分</td>
					<td>科技节-个人</td>
					<td>折分</td>
					<td>科技节-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[3][8]!}</td>
					<td>${myList[3][9]!}</td>
					<td>${myList[3][10]!}</td>
					<td>${myList[3][11]!}</td>
					<td>${myList[3][12]!}</td>
					<td>${myList[3][13]!}</td>
					<td>${myList[3][14]!}</td>
					<td>${myList[3][15]!}</td>
				</tr>
				<tr>
					<td>文化节-个人</td>
					<td>折分</td>
					<td>文化节-团体</td>
					<td>折分</td>
					<td>全校性活动-个人</td>
					<td>折分</td>
					<td>全校性活动-团体</td>
					<td>折分</td>
				</tr>
				<tr>
					<td>${myList[3][16]!}</td>
					<td>${myList[3][17]!}</td>
					<td>${myList[3][18]!}</td>
					<td>${myList[3][19]!}</td>
					<td>${myList[3][20]!}</td>
					<td>${myList[3][21]!}</td>
					<td>${myList[3][22]!}</td>
					<td>${myList[3][23]!}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script type="text/javascript">
showBreadBack(backIndex, true, '综合素质汇总');

function doExport(studentId){
	var ii = layer.load();
	var downId=new Date().getTime();//以时间戳来区分每次下载
	document.location.href = "${request.contextPath}/quality/sum/student/pdfExport?studentId="+studentId+"&downId="+downId;
	var interval=setInterval(function(){
		var down=$.cookies.get("D"+downId);
		if(down==downId){
			layer.close(ii);
			$.cookies.del("D"+downId);
		}
	},1000);
}
</script>