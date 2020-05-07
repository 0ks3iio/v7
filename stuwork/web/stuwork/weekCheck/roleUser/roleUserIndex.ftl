<#import "/fw/macro/popupMacro.ftl" as popup />
			<div class="box box-default">
				<div class="box-body">
					<ul class="nav nav-tabs" role="tablist">
						<li <#if roleType == '01'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('01')">总管理员</a></li>
						<#if hasAdmin == '1'>
							<li <#if roleType == '02'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('02')">值周干部</a></li>
							<li <#if roleType == '03'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('03')">值周班</a></li>
							<li <#if roleType == '04'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('04')">学生处</a></li>
							<li <#if roleType == '05'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('05')">保卫处</a></li>
							<li <#if roleType == '06'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('06')">年级组</a></li>
							<li <#if roleType == '07'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('07')">体育老师</a></li>
                            <li <#if roleType == '08'>class="active"</#if> role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('08')">卫生检查</a></li>
						</#if>
					</ul>
					<div class="tab-content">
					<#if roleType == '06'>
					<div class="tab-pane active" role="tabpanel">
						<table class="table">
							<thead>
								<tr>
									<th width="20%" style="text-align:center;">年级</th>
									<th width="60%" style="text-align:center;">人员</th>
									<th width="20%" style="text-align:center;">操作</th>
								</tr>
							</thead>
							<tbody>
								<#if grades?exists && grades?size gt 0>
									<#list grades as g>
										<tr>
											<td width="20%" align="center">${g.gradeName!}</td>
											<td width="60%" align="center">
											<#assign valueId = ''>
											<#assign valueName = ''>
												<#if roles?exists && roles?size gt 0>
													<#list roles as r>
														<#if r.grade == g.gradeCode>
															<#if valueName==''>
															${r.roleName!}
															<#assign valueName = r.roleName>
															<#assign valueId = r.userId>
															<#else>
															、${r.roleName!}
															<#assign valueName = valueName +','+ r.roleName>
															<#assign valueId = valueId +','+ r.userId>
															</#if>
														</#if>
													</#list>
												</#if>
												<input id='valueId${g.gradeCode!}' value='${valueId}' type='hidden'/>
												<input id='valueName${g.gradeCode!}' value='${valueName}' type='hidden'/>
											</td>
											<td width="20%" align="center">
											<a href="#" class="color-lightblue" onclick="editGrade('${g.gradeCode!}');">修改</a>
												<#assign valueId = ''>
												<#assign valueName = ''>
												<div style="display: none;">
												<@popup.selectMoreUser clickId="userName${g.gradeCode!}" id="userId${g.gradeCode!}" name="userName${g.gradeCode!}" handler="save()">
													<input type="text" id="userName${g.gradeCode!}" value="${valueName!}"/>
													<input type="hidden" id="userId${g.gradeCode!}" value="${valueId!}"/>
													</@popup.selectMoreUser>
												</div>
											</td>
										</tr>
									</#list>
								</#if>
							</tbody>
						</table>
					</div>
					<#elseif roleType == '01' || roleType == '05' || roleType == '04'||roleType='08'>
						<div class="tab-pane active" role="tabpanel">
							<table class="table">
								<thead>
									<tr>
										<th style="text-align:center;" width="20%">角色</th>
										<th style="text-align:center;" width="60%">人员</th>
										<th style="text-align:center;" width="20%">操作</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td width="20%" align="center">
											<#if roleType == '01'>
											总管理员
											<#elseif roleType == '04'>
											学生处老师
											<#elseif roleType == '05'>
											保卫处老师
											<#elseif roleType == '08'>
											卫生检查
											</#if>
										</td>
										<td width="60%" align="center">
										${roleNames!}
										</td>
										<td width="20%" align="center"><a href="#" class="color-lightblue" onclick="edit();">修改</a></td>
									</tr>
								</tbody>
							</table>
							<div style="display: none;">
						<#--<@popup.popup_div dataUrl="${request.contextPath}/common/div/user/popupData" id_value="${roleIds!}" name_value="${roleNames!}" id="userId" name="userName1" dataLevel="2" type="duoxuan" putRecentDataUrl="${request.contextPath}/common/div/user/recentData" resourceUrl="${resourceUrl}" handler="save()">
							<input type="text" id="userName1" class="duoduan" value="${roleNames!}" data-value="${roleIds!}" />
							</@popup.popup_div>-->
							<@popup.selectMoreUser clickId="userName1" id="userId" name="userName1" handler="save()">
								<input type="text" id="userName1" value="${roleNames!}"/>
								<input type="hidden" id="userId" value="${roleIds!}"/>
								</@popup.selectMoreUser>
							</div>
						</div>
						<#elseif roleType == '07'>
						<div class="tab-pane active" role="tabpanel">
						<table class="table">
							<thead>
								<tr>
									<th width="20%" style="text-align:center;">学段</th>
									<th width="60%" style="text-align:center;">人员</th>
									<th width="20%" style="text-align:center;">操作</th>
								</tr>
							</thead>
							<tbody>
								<#if sections?exists && sections?size gt 0>
									<#list sections as sec>
										<tr>
											<td width="20%" align="center"><#--if sec == '1'>小学
												<#elseif sec == '2'>初中
												<#elseif sec == '3'>高中
												<#elseif sec == '4'>剑桥高中
												</#if-->
												${mcodeSetting.getMcode("DM-RKXD","${sec?default('0')}")}
											</td>
											<td width="60%" align="center">
											<#assign valueId = ''>
											<#assign valueName = ''>
												<#if roles?exists && roles?size gt 0>
													<#list roles as r>
														<#if r.section == sec>
															<#if valueName==''>
															${r.roleName!}
															<#assign valueName = r.roleName>
															<#assign valueId = r.userId>
															<#else>
															、${r.roleName!}
															<#assign valueName = valueName +','+ r.roleName>
															<#assign valueId = valueId +','+ r.userId>
															</#if>
														</#if>
													</#list>
												</#if>
											</td>
											<td width="20%" align="center">
											<a href="#" class="color-lightblue" onclick="editSec('${sec!}');">修改</a>
												<div style="display: none;">
												<@popup.selectMoreUser clickId="userName${sec!}" id="userId${sec!}" name="userName${sec!}" handler="save()">
													<input type="text" id="userName${sec!}" value="${valueName!}"/>
													<input type="hidden" id="userId${sec!}" value="${valueId!}"/>
													</@popup.selectMoreUser>
												</div>
											</td>
										</tr>
									</#list>
								</#if>
							</tbody>
						</table>
					</div>
						<#elseif roleType == '03'>
						<div class="tab-pane active" role="tabpanel">
										<div class="table-container">
											<div class="table-container-header text-right">
												<button class="btn btn-white" onclick="doImport();">导入Excel</button>
												<button class="btn btn-white" onclick="doExport();">导出</button>
											</div>
										<form id="aaasadsa" class="print">
											<div class="table-container-body">
												<table class="table table-striped">
													<thead>
														<tr>
															<th width="10%" style="text-align:center;">周次</th>
															<th width="20%" style="text-align:center;">日期</th>
															<#if sections?exists && sections?size gt 0>
															<#list sections as sec>
															<#-- if sec == '1'><th>小学</th>
															<#elseif sec == '2'><th>初中</th>
															<#elseif sec == '3'><th>高中</th>
															<#elseif sec == '4'><th>剑桥高中</th>
															</#if-->
															<th style="text-align:center;">${mcodeSetting.getMcode("DM-RKXD","${sec?default('0')}")}</th>
															</#list>
															</#if>
														</tr>
													</thead>
													<tbody>
 												<#if dateDto?exists && dateDto?size gt 0>
													<#list dateDto as dto>
														<tr>
															<td width="10%" align="center">第${dto.week!}周</td>
															<td width="20%" align="center">${dto.beginDate?string('yyyy-MM-dd')!}~${dto.endDate?string('yyyy-MM-dd')}</td>
															<#if sections?exists && sections?size gt 0>
															<#list sections as sec>
															<td align="center">
															<#assign idValue=''>
															<#assign nameValue=''>
																<#if roles?exists && roles?size gt 0>
																	<#list roles as ro>
																		<#if ro.section== sec && ro.week == dto.week?number>
																			${ro.roleName!}
																			<#assign idValue=ro.classId>
																			<#assign nameValue=ro.roleName>
																		</#if>
																	</#list>
																</#if>
																<#-- <a href="#" class="color-lightblue noprint" onclick="editClass('${dto.week!}','${sec!}','${idValue}','${nameValue}');">修改</a>-->
																<a href="#" class="color-lightblue noprint" onclick="editClassStu('${dto.week!}','${sec!}','${idValue}','${nameValue}');">修改</a>
															</td>
															</#list>
															</#if>
														</tr>
														</#list>
														</#if>
													</tbody>
												</table>
											</div>
											</form>
										</div>
									</div>
						</#if>
					</div>
				</div>
			</div>
<div style="display: none;">
	<#-- <@popup.selectOneClass clickId="className" id="classId" name="className" handler="save()">
			<input type="text" id="className" value="${nameValue!}"/>
	        <input type="hidden" id="classId" value="${idValue!}"/>
	</@popup.selectOneClass> -->
	
	<@popup.selectMoreStudent clickId="studentName" id="studentId" name="studentName" handler="save()">
		<input type="text" id="studentName" value="${nameValue!}"/>
	    <input type="hidden" id="studentId" value="${idValue!}"/>
	</@popup.selectMoreStudent>
	
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
function doImport(){
	$(".model-div").load("${request.contextPath}/stuwork/roleClass/main");
}

function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("值周班"+getNowFormatDate()+".xls");
}
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = "-";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + "," + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

var gId = '';
var sec = '';
var we = '';
function editClassStu(week,section,idValue,nameValue){
	we = week;
	sec = section;
	$('#studentName').val(nameValue);
	$('#studentId').val(idValue);
	$('#studentName').click();
}
function editSec(section){
	sec = section;	
	$('#userName'+section).click();
}
function editGrade(gradeId){
	gId = gradeId;	
	$('#userName'+gradeId).click();
}
function changeRoleType(type){
	if(type == '02'){
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUserTeacher/page?roleType=02");
	}else{
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType="+type);
	}
}
function edit(){
	$('#userName1').click();
}
function save(){
<#if roleType == '03'>
var userIds = $('#studentId').val();
var roleType  ='${roleType}';
$.ajax({
		url:"${request.contextPath}/stuwork/weekCheck/roleUser/role/save",
		data: {'userIds':userIds,'roleType':roleType,'gradeId':we,'section':sec},  
		type:'post',
		success:function(data) {
		var jsonO = JSON.parse(data);
			if(jsonO.success){
				layerTipMsg(jsonO.success,"成功",jsonO.msg);
				$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType=${roleType!}");
			}else{
				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
<#elseif roleType == '06'>
	var userIds = $('#userId'+gId).val();
	var roleType  ='${roleType}';
	$.ajax({
		url:"${request.contextPath}/stuwork/weekCheck/roleUser/role/save",
		data: {'userIds':userIds,'roleType':roleType,'gradeId':gId},  
		type:'post',
		success:function(data) {
			layerTipMsg('success',"成功",data.msg);
			$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType=${roleType!}");
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
<#elseif roleType == '07'>
	var userIds = $('#userId'+sec).val();
	var roleType  ='${roleType}';
	$.ajax({
		url:"${request.contextPath}/stuwork/weekCheck/roleUser/role/save",
		data: {'userIds':userIds,'roleType':roleType,'section':sec},  
		type:'post',
		success:function(data) {
			layerTipMsg('success',"成功",data.msg);
			$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType=${roleType!}");
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
<#elseif roleType == '01' || roleType == '05' || roleType == '04'||roleType == '08'>
	var userIds = $('#userId').val();
	var roleType  ='${roleType}';
	$.ajax({
		url:"${request.contextPath}/stuwork/weekCheck/roleUser/role/save",
		data: {'userIds':userIds,'roleType':roleType},  
		type:'post',
		success:function(data) {
			layerTipMsg('success',"成功",data.msg);
			$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType=${roleType!}");
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
</#if>
}
</script>
