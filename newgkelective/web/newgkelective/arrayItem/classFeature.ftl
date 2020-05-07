<div class="table-container">
	<div class="table-container-body">
		<form id="classFeatures">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>科目</th>
					<th>教师</th>
					<th>课时</th>
					<th>单双周</th>
					<th>禁排时间</th>
					<th>合班数量</th>
					<th>同时排课数量</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<#if courseTimeList?? && courseTimeList?size gt 0>
			<#list courseTimeList as ct>
			<#assign subjectCode = ct.subjectId + "-"+ ct.subjectType!>
			<#assign combineClass = combineMap[subjectCode]!>
			<#assign meanwhiles = meanwhileMap[subjectCode]!>
				<tr>
					<td>${ct.subjectName!}
						<input type="hidden" class="subjectId" name="courseTimeList[${ct_index}].subjectId" value="${ct.subjectId!}"/>
						<input type="hidden" class="subjectType" name="courseTimeList[${ct_index}].subjectType" value="${ct.subjectType!}"/>
						<input type="hidden" class="subjectCode" value="${subjectCode!}"/>
					</td>
					<td>
						<input type="hidden" class="teacherId" value=""/>
						<span class="teacherName"></span>
					</td>
					<td>
						<div class="form-number  form-number-sm period-number" data-step="1">
				    		<input type="text" name="courseTimeList[${ct_index}].courseWorkDay" class="form-control courseWorkDay"  value="${ct.courseWorkDay!}" onblur="changeInputNum(this)"/>
				    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    	<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
				    	</div>
					</td>
					<td>
						<select name="courseTimeList[${ct_index}].weekType" onchange="updateWeekType(this)" class="form-control weekType">
							<option value="3">自动</option>
							<option value="1" <#if ct.weekType== 1>selected</#if>>单周</option>
							<option value="2" <#if ct.weekType== 2>selected</#if>>双周</option>
						</select>
					</td>
					<td>
						<div class="clearfix js-hover">
				    		<input type="hidden" class="js-choosetime noArrangeTime" name="courseTimeList[${ct_index}].noArrangeTime" value="${ct.noArrangeTime!}" >
				    		<span class="pull-left js-hover-num color-blue"><#if ct.noArrangeTime??>${(ct.noArrangeTime)?split(",")?size!}<#else>0</#if></span>
					    	<span class="pull-right js-hover-opt" style="display: block;">
					    		<a href="javascript:void(0)" class="js-changeTime" data-toggle="tooltip" data-placement="top" title="编辑" data-original-title="编辑" onclick="changeLimitTime(this)"><i class="fa fa-edit color-blue"></i></a>
					    	</span>
				    	</div>
					</td>
					<td>
						合班数：<span class="combineNum">${combineClass?size!}</span> &nbsp;
						
						<input class="combineClass" type="hidden" name="courseTimeList[${ct_index}].combineClass" value="${(combineClass?join(','))!}"/>
					</td>
					<td>
						同时排课：<span class="meanwhileNum">${meanwhiles?size!}</span>
						
						<input class="meanwhiles" type="hidden" name="courseTimeList[${ct_index}].meanwhiles" value="${(meanwhiles?join(','))!}"/>
					</td>
					<td>
						<a href="javascript:void(0);" onclick="combineClass(this)">合班</a>&nbsp;&nbsp;
						<a href="javascript:void(0);" onclick="meanwhileLecture(this)">同时排课</a>&nbsp;&nbsp;
						<a href="javascript:void(0);" onclick="copyItem(this)">复制到</a>
					</td>
				</tr>
			</#list>
			<#else>
				<tr>
					<td colspan="7">没有数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
		</form>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
    <a href="javascript:void(0)" class="btn btn-blue" onclick="saveClassTime()">保存</a>
</div>
<#--- 弹出课程表  ---->
<div class="layer layer-timeArea">
	<div class="layer-content timeArea-div">
		<p class="courseScheduleTime-explain" style="display:none;"><strong class="subjectName">科目：</strong><span>（请选择连续课次）</span></p>
		<table class="table table-bordered table-striped text-center table-schedule table-schedule-sm" style="margin-bottom: -20px;">
			<#assign weekDays = (weekDays!7) - 1>
			<thead>
				<tr>
					<th width="30" class="text-center"></th>
					<th width="30" class="text-center"></th>
					<#list 0..weekDays as day>
		            <th class="text-center">${dayOfWeekMap[day+""]!}</th>
		            </#list>
				</tr>
			</thead>
			<tbody>		
				<#list piMap?keys as piFlag>
				    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
				    <#assign interval = piMap[piFlag]>
				    <#assign intervalName = intervalNameMap[piFlag]>
				    <#list 1..interval as pIndex>
				    <tr>
				    <#if pIndex == 1>
				    	<td class="text-center" rowspan="${interval}">${intervalName!}<input type="hidden" disabled name="period_interval" value="${piFlag}" disable/></td>
				    </#if>
			        	<td class="text-center">${pIndex!}</td>
						<#list 0..weekDays as day>
						
			            <#assign tc = day+"_"+piFlag+"_"+pIndex>
						<td class="text-center edited <#if noClickTimeMap?exists && noClickTimeMap[tc!]?exists> disabled</#if>" data-value="${tc!}" ></td>
						</#list>
				    </tr>
				    
				    </#list>
				    
				    <#if piFlag == "2" && piMap["3"]?? && piMap["3"] gt 0>
						<tr>
							<td class="text-center" colspan="${weekDays + 3}">午休</td>
						</tr>
					</#if>
				    
				    </#if>
			    </#list>						
			</tbody>
		</table>
	</div>
</div>

<#--复制参数--->
<div class="layer layer-copyCourseParm">
	<div class="layer-content">
		<div class="explain" style="margin-bottom:5px;">
			<p>请先确保您的修改已经保存</p>
		</div>
		<table width="100%">
			<tr>
				<td width="80"><p class="mb10" >当前班级：</p></td>
				<td class="text-left">
					<p class="mb10 currentClassName">aaa班</p>
				</td>
				<td width="80"><p class="mb10" >当前科目：</p></td>
				<td class="text-left">
					<p class="mb10 currentSubjectName">bbb科目</p>
				</td>
			</tr>
			<tr>
				<td valign="top"><p class="mb10">复制内容：</p></td>
				<td  colspan="3">
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="1" class="wp"><span class="lbl"> 周课时</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="2" class="wp"><span class="lbl"> 禁排时间</span></label>
				</td>
			</tr>
		</table>
		<div class="gk-copy" style="border: 1px solid #eee;">
			<table class="table no-margin">
				<tr>
					<th width="127">班级</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px;height:405px;">
				
                	<li id="class_" class="courseLi"><a href="#aaa_" data-value="">测试班级1
                	<span class="badge badge-default"></span></a></li>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId"style="position:relative;overflow:auto;height:405px;border-left: 1px solid #eee;">
					<#-- 这里放 各个班级的课程 -->
				</div>
			</div>
		</div>
		
	<#--<div class="no-data-container" id="noDataId" style="display:none;">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">没有相关数据</p>
				</div>
			</div>
		</div> -->
	</div>
</div>
<#-- 合班设置 -->
<div class="layer layer-combineClass">
	<div class="layer-content combineClass-div">
	
		<table width="100%">
			<tr>
				<td class="text-right" width="80"><p>当前科目：</p></td>
				<td><p class="currentSubjectName"></p></td>
			</tr>
			<tr>
				<td class="text-right" width="80"><p>当前班级：</p></td>
				<td><p class="currentClassName"></p></td>
			</tr>
			
			<tr>
				<td class="text-right" valign="top"><p>合班班级：</p></td>
				<td class="combineItems">
					
				</td>
			</tr>
		</table>
	</div>
</div>
<#--同时排课--->
<div class="layer layer-meanwhile">
	<div class="layer-content">
		<input type="hidden" class="subjectIdType" value=""/>
		<div style="max-height:350px;overflow-y:scroll;">
			<table width="100%">
				<tr>
					<td width="25%">当前科目：</td>
					<td width="25%" class="currentSubjectName"></td>
					<td width="25%">当前班级：</td>
					<td width="25%" class="currentClassName"></td>
				</tr>
			</table>
			<table class="table table-bordered table-striped table-hover" style="margin-bottom:3px;">
				<thead>
					<tr>
						<th width="35%">班级</th>
						<th width="35%">科目</th>
						<th width="15%">教师</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody class="functional">
					<tr>
						<td colspan="4" class="text-center">
							<a class="color-blue js-separate-add" href="javascript:void(0)">+ 新增</a>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
//时间点击js
$(".edited").click(function(e){
	
	if(!$(this).hasClass("disabled")){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}
	}else{
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}
	}
});

function changeLimitTime(obj){
	var chooseValueObj=$(obj).parents("td").find(".js-choosetime");
	var oldTimes = $(chooseValueObj).val();
	var title="禁排时间";
	showTime(title,oldTimes,chooseValueObj,false);
}

function showTime(title,oldClikeTimes,divKeyObj,isNear){
	if(isNear){
		var subjectName=$(divKeyObj).parents("tr").find(".td-subjectName").html().trim();
		$('.layer-timeArea').find(".subjectName").html('科目：'+subjectName);
		$(".courseScheduleTime-explain").show();
	}else{
		$(".courseScheduleTime-explain").hide();
	}
	$('.layer-timeArea').find(".edited").removeClass("active");
	
	if(oldClikeTimes!=""){
		$('.layer-timeArea').find(".edited").each(function(){
			var key=$(this).attr("data-value");
			if(oldClikeTimes.indexOf(key)>-1){
				$(this).addClass("active");
			}
		})
	}
	layer.open({
		type: 1,
		shadow: 0.5,
		title: title,
		area: '620px',
		btn: ['确定', '取消'],
		btn1:function(){
			//opoNum++;
			var chooseTimes="";
			var arr=new Array();
			var i=0;
			$('.layer-timeArea').find(".edited").each(function(){
				if($(this).hasClass("active")){
					chooseTimes=chooseTimes+","+$(this).attr("data-value");
					arr[i]=$(this).attr("data-value");
					i++;
				}
			})
			if(chooseTimes!=""){
				chooseTimes=chooseTimes.substring(1);
				if(isNear){
					var flag=true;
					for(var j=0;j<arr.length;j++){
						var temp=arr[j];
						var rr=temp.split("_");
						var pp=parseInt(rr[2]);
						var beforeStr=rr[0]+"_"+rr[1]+"_"+(pp-1);
						var afterStr=rr[0]+"_"+rr[1]+"_"+(pp+1);
						var chooseStr=new Array();
						if(abnum>0 && rr[1]=='2'){
							//如果是上午
							if(pp==abnum){
								//只能考虑 beforeStr
								chooseStr[0]=beforeStr;
							}else if(pp+1==abnum){
								//只能考虑 afterStr
								chooseStr[0]=afterStr;
							}else{
								//考虑 beforeStr,afterStr
								chooseStr[0]=beforeStr;
								chooseStr[1]=afterStr;
							}
							
						}else if(pbnum>0 && rr[1]=='3'){
							if(pp==pbnum){
								//只能考虑 beforeStr
								chooseStr[0]=beforeStr;
							}else if(pp+1==pbnum){
								//只能考虑 afterStr
								chooseStr[0]=afterStr;
							}else{
								//考虑 beforeStr,afterStr
								chooseStr[0]=beforeStr;
								chooseStr[1]=afterStr;
							}
						}else{
							//考虑 beforeStr,afterStr
							chooseStr[0]=beforeStr;
							chooseStr[1]=afterStr;
						}
						if(chooseStr.length==1){
							if(chooseTimes.indexOf(chooseStr[0])>-1){
								
							}else{
								flag=false;
								break;
							}
						}else{
							if(chooseTimes.indexOf(chooseStr[0])>-1 || chooseTimes.indexOf(chooseStr[1])>-1){
								
							}else{
								flag=false;
								break;
							}
						}
					}
					if(flag){
						$(divKeyObj).val(chooseTimes);
						layer.closeAll();
					}else{
						layer.msg('时间范围需是选择连续的节次！', {
							icon: 2,
							time: 1500,
							shade: 0.2
						});
					}
				}else{
					$(divKeyObj).parents("td").find(".js-hover-num").html(arr.length);
					$(divKeyObj).val(chooseTimes);
					layer.closeAll();
				}
				
			}else{
				if(!isNear){
					$(divKeyObj).parents("td").find(".js-hover-num").html(0);
				}
				$(divKeyObj).val(chooseTimes);
				layer.closeAll();
			}
			
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-timeArea')
	});
}

function checkClassByCourse(subjectCode){
	var classNameMap = [];
	var index = fakeXzbArr.indexOf(classId);
	for(cid in classCourseObj){
		if(fakeXzbArr.indexOf(cid)>-1 ^ index >-1){
			continue;
		}
		var courses = classCourseObj[cid];
		for(c in courses){
			if(courses[c].subjectId +"-"+ courses[c].subjectType == subjectCode){
				classNameMap.push({"classId":cid, "className" : classNameTab[cid]});
				break;
			}
		}
	}
	
	return classNameMap;
}

var coursTimeinfo = {
	<#if courseTimeList?? && courseTimeList?size gt 0>
	<#list courseTimeList as ct>
		<#if ct_index != 0>,</#if>	
		"${ct.subjectId +"-"+ct.subjectType}":"${ct.subjectName!}"
	</#list>
	</#if>
	};
// 每个课程所对应的可合班的班级
var courseCombineClass = {};
var classId = $(".tree-list a.active").attr("id");
var currClassName = classNameTab[classId];
var subjectTeacherTab = {};
$(function(){
	//提示
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});


	for(code in coursTimeinfo){
		var classNames = checkClassByCourse(code);
		courseCombineClass[code] = classNames;
	}
	$(".currentClassName").html(currClassName);
	$("#myscrollspy ul").html(copyItemClass);
	$("#scrollspyDivId").html(classCourseHtml);
	$("#scrollspyDivId .js-clearChoose").hide();

	$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' });

	// 没有单双周的课程禁止设置单双周
	// 填充老师
	subjectTeacherTab = classCouTeaObj[classId];
	<#if courseTimeList?? && courseTimeList?size gt 0>
	$("#classFeatures table tbody tr").each(function(i,obj){
		var subId = $(obj).find(".subjectId").val();
		var subType = $(obj).find(".subjectType").val();
		// 单双周
		if(!firstsdSubjectMap[subId+"_"+subType]){
			$(obj).find(".weekType").attr("disabled",true);
			$(obj).find(".weekType option:eq(0)").text("无");
		}

		//填充老师
		if(subjectTeacherTab[subId] && subjectTeacherTab[subId].teacherName){
			var teacherName = subjectTeacherTab[subId].teacherName;
			$(obj).find(".teacherName").html(teacherName);
			$(obj).find(".teacherId").val(subjectTeacherTab[subId].teacherId);
		}
		else
			$(obj).find(".teacherName").html("/");
	});
	</#if>
	
});
// 合班
function combineClass(obj){
	
	var subjectId = $(obj).parents("tr").find(".subjectId").val();
	var teacherId = $(obj).parents("tr").find(".teacherId").val();
	var subjectType = $(obj).parents("tr").find(".subjectType").val();
	var subjectCode = subjectId+"-"+subjectType;
	var comnibeStr = (""+$(obj).parents("tr").find(".combineClass").val());
	var combineClazzs = new String(comnibeStr).split(",");
	
	var showClass =  courseCombineClass[subjectCode];
	var cids = [];
	for(i in showClass){
		cids.push(showClass[i].classId);
	}
	
	var s = "";
	for(cid in classNameTab){
		if(cids.indexOf(cid) < 0)
			continue;
		cn = classNameTab[cid];
		var teacher = classCouTeaObj[cid][subjectId]; 
		var disblestr = '';
		if(teacher.teacherId != teacherId)
			disblestr = 'disabled="disabled"';
		if(teacher.teacherName)
			cn += " (" + teacher.teacherName+")";
		
		var chec = "";
		if(classId == cid)
			continue;
		if(combineClazzs && combineClazzs.indexOf(cid) > -1)
			chec = 'checked="checked"';
		s += '<label class="mb10 mr10 w155 ellipsis"><input type="checkbox" '+disblestr+' data-value="'+ teacher.teacherId +'" class="wp" '+chec+' value="'+cid+'"\
			onclick="checkChange(this)"><span class="lbl"> '+ cn +'</span></label>';
	}
	
	$(".layer-combineClass .combineItems").html(s);
	if(subjectTeacherTab[subjectId].teacherName)
		$(".currentSubjectName").html(coursTimeinfo[subjectCode] +"("+subjectTeacherTab[subjectId].teacherName+")");
	else
		$(".currentSubjectName").html(coursTimeinfo[subjectCode]);
	
	
	$(".layer-combineClass .combineItems").off("click").on("click","label",function(){
		var input = $(this).find("input");
		if(input.attr("disabled")){
			input.prop('checked',false);
		}
	});
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '合班',
		area: '620px',
		btn: ['确定', '取消'],
		btn1:function(){
			var res = $(".layer-combineClass .combineItems input:checked");
			var size = res.length;
			var clasStr = "";
			
			var old = teacherId;
			var f = true;
			
			res.each(function(i,e){
				if(i != 0)
					clasStr += ",";
				clasStr += e.value;
				
				var tid = $(e).attr("data-value");
				
				if(tid != old){
					f = false;
					return false;
				}
			});
			if(!f){
				layer.msg('合班班级老师必须相同！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
				return;
			}
			
			$(obj).parents("tr").find(".combineNum").html(size);
			$(obj).parents("tr").find(".combineClass").val(clasStr);
			
			
			layer.closeAll();
		},
		btn2:function(){
		
			layer.closeAll();
		},
		content: $('.layer-combineClass')
	});

}

// 同时排课
function meanwhileLecture(obj){
	var subjectId = $(obj).parents("tr").find(".subjectId").val();
	var subjectType = $(obj).parents("tr").find(".subjectType").val();
	var subjectCode = subjectId+"-"+subjectType;
	var this_tn = $(obj).parents("tr").find(".teacherName").text();

	// 清除数据
	$(".js-separate-add").parents("tr").siblings("tr").remove();
	// 将历史数据放进去
	var s = $(obj).parents("tr").find(".meanwhiles").val();
	if(s){
		var pairs = s.split(",");
		for(i in pairs){
			var pair = pairs[i].split(":");
			
			$(".js-separate-add").parents("tr").before(addSeparateTr(classId,pair[0]));
			getRespSubject($(".js-separate-add").parents("tr").prev("tr").find(".classId")[0],pair[1]);
		}
		
	}
	
	
	$(".currentSubjectName").html(coursTimeinfo[subjectCode]);
	
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '同时排课',
		area: '620px',
		btn: ['确定', '取消'],
		btn1:function(){
			var $trs = $(".layer-meanwhile tbody.functional tr");
			var size = $trs.length;
			s = "";
			
			var f = true;
			var tns = [];
			$trs.each(function(i,e){
				if(i+1 < size){
					if(i != 0)
						s+=",";
					var cid = $(e).find(".classId").val();
					if(s.indexOf(cid) > -1){
						layer.msg('不能出现相同班级', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
						f = false;
						return false;
					}
						
					s += cid +":"+ $(e).find(".subjectCode").val();
					
					var tn = $(e).find(".tn").text();
					if(tn!='/' && tns.indexOf(tn)>-1){
						layer.msg('不能出现相同教师', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
						f = false;
						return false;
					}else if(tn!='/'){
						tns.push(tn);
					}
				}
			});
			if(!f)
				return;
			if(tns.indexOf(this_tn)>-1){
				layer.msg('不能与本班科目老师相同', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
						return;
			}
			
			$(obj).parents("tr").find(".meanwhileNum").html(size-1);
			$(obj).parents("tr").find(".meanwhiles").val(s);
			
			layer.closeAll();
		},
		btn2:function(){
		
			layer.closeAll();
		},
		content: $('.layer-meanwhile')
	});
}
// 同时排课  删除
$(".layer-meanwhile").on('click','.js-separate-delete',function(){
	$(this).parents("tr").remove();
})
// 点击增加同时排课
$(".js-separate-add").on('click',function(){
	var kkId=$('.layer-separate').find(".subjectIdType").val();
	$(this).parents("tr").before(addSeparateTr(classId));
	getRespSubject($(".js-separate-add").parents("tr").prev("tr").find(".classId")[0]);
});


function addSeparateTr(currClassId,defaultId){
	var index = fakeXzbArr.indexOf(currClassId);
	
	var optionHtml = "";
	for(cid in classNameTab){
		if(currClassId == cid)
			continue;
		var index2 = fakeXzbArr.indexOf(cid);
		if((index2<0&& index >= 0) || (index2>=0 && index <0)){
			continue;
		}
		if(defaultId && defaultId == cid)
			optionHtml += '<option value="'+cid+'" selected>'+ classNameTab[cid] +'</option>';
		else
			optionHtml += '<option value="'+cid+'">'+ classNameTab[cid] +'</option>';
	}
	var selectHtml = '<select name="classId" id="" class="form-control classId" onchange="getRespSubject(this)" styple="min-width:90px;">'+ optionHtml +'</select>';
	return '<tr><td>'+selectHtml+'</td><td></td>'
		+'<td class="tn"></td>'
		+'<td><a class="color-blue js-separate-delete" href="javascript:void(0)">删除</a></td>'
		+'</tr>';
}
// 根据班级获取对应的科目
function getRespSubject(obj,defaultSubjectCode){
	var cid = $(obj).val();
	var subjects = classCourseObj[cid];
	var optionHtml = "";
	for(i in subjects){
		var code = subjects[i].subjectId+'-'+subjects[i].subjectType;
		if(defaultSubjectCode && defaultSubjectCode == code)
			optionHtml += '<option value="'+ code +'" selected>'+ subjects[i].subjectName +'</option>';
		else
			optionHtml += '<option value="'+ code +'">'+ subjects[i].subjectName +'</option>';
	}
	var selectHtml = '<select name="subjectCode" id="" class="form-control subjectCode" onchange="getRespTeacher(this);" style="min-width:90px;">'+ optionHtml +'</select>';
	$(obj).parents("td").next("td").html(selectHtml);
	getRespTeacher($(obj).parents('tr').find('.subjectCode')[0]);
}
function getRespTeacher(obj){
	var $tr = $(obj).parents('tr');
	var classId = $(obj).parents('tr').find('.classId').val();
	var subjectId = $(obj).parents('tr').find('.subjectCode').val().split('-')[0];
	
	var tea = classCouTeaObj[classId][subjectId];
	if(tea.teacherName){
		$tr.find('.tn').text(tea.teacherName);
	}else{
		$tr.find('.tn').text('/');
	}
}
// 复制
function copyItem(obj){
	var subjectCode = $(obj).parents("tr").find(".subjectCode").val();
	
	$(".currentSubjectName").html(coursTimeinfo[subjectCode]);
	
	// 清除历史信息
	$(".layer-copyCourseParm :checkbox").prop("checked",false);
	$(".layer-copyCourseParm :checkbox").attr("disabled",false);
	$(".gk-copy-nav").find("span").each(function(){
		$(this).text("");
	});
	$("#scrollspyDivId .js-clearChoose").hide();
	$("#scrollspyDivId .js-allChoose").show();
	
	var sc = classId +"-"+ $(obj).parents("tr").find(".subjectCode").val();
	
	$("#scrollspyDivId :checkbox[value='"+sc+"']").attr("disabled",true);
	//console.log("nu = "+$("#scrollspyDivId :checkbox[value='"+sc+"']").length);
	$("#myscrollspy li").removeClass("active");
	$("#class_"+classId).addClass("active");
	
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '复制到',
		area: ['1000px','700px'],
		btn: ['确定', '取消'],
		btn1:function(){
			
			var fs = [];
			$('[name="copyField"]:checked').each(function(i,obj){
				fs.push($(obj).val());
			});
			
			//console.log("fs = " + fs);
			
			// 要复制 班级科目
			var csts = [];
			$("#scrollspyDivId :checked").each(function(i,obj){
				csts.push($(obj).val());
			});
			
			
			updateCopy(fs,csts, subjectCode)
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-copyCourseParm')
	});
}

function updateCopy(fs,csts,subjectCode){
	if(!fs || fs.length <1 || !csts || csts.length <1){
		layer.msg('请先选择复制内容和课程！', {
				icon: 2,
				time: 1500,
				shade: 0.2
			});
		return false;
	}
	var ii = layer.load();
	$.ajax({
		url: "${request.contextPath}/newgkelective/"+arrayItemId+"/classFeature/copyItem",
		data: {"classId":classId, "subjectCode":subjectCode, "copyFlags":fs.join(","), "classSubjectCodes":csts},
		type: "POST",
		success:function(data){
			//layer.close(ii);
			layer.closeAll();
			if(data.success){
				layer.msg(data.msg,{offset:'t',time:2000});
				if(fs.indexOf("1") != -1){
					classTable(classId);
				}else{
					queryClass(classId);
				}
			}
			else
				alert(data.msg);
		},
		dataType:"JSON",
		error:function(e){alert(e);}
	});
	return true;
}

function saveClassTime(){
	

	var params = $("#classFeatures").serialize();
	
	var f = true;
	params = [];
	var weekTypeObj = {};
	$("#classFeatures tbody tr").each(function(i,e){
		var subjectId = $(e).find(".subjectId").val();
		var subjectType = $(e).find(".subjectType").val();
		var courseWorkDay = $(e).find(".courseWorkDay").val();
		var noArrangeTime = $(e).find(".noArrangeTime").val();
		var combineClass = $(e).find(".combineClass").val();
		var meanwhiles = $(e).find(".meanwhiles").val();
		var weekType = $(e).find(".weekType").val();

		if (!/^\d+$/.test(courseWorkDay)) {
			layer.tips("请输入整数！",$(e).find('.courseWorkDay'), {
				tipsMore: true,
				tips: 3
			});
			f=false;
			return false;
		}
		if (firstsdSubjectMap[subjectId+"_"+subjectType]) {
			if(courseWorkDay <= 0){
				var respCode = firstsdSubjectMap[subjectId+"_"+subjectType];
				var $respTr = $("#classFeatures tbody .subjectCode[value='"+respCode+"']").parents("tr");
				if(!$respTr || $respTr.find(".courseWorkDay").val()>0){
					layer.tips("单双周科目至少有一节课或者都为0！",$(e).find('.courseWorkDay'), {
						tipsMore: true,
						tips: 3
					});
					f=false;
					return false;
				}
			}

			var relaSub = firstsdSubjectMap[subjectId+"_"+subjectType];
			if(weekType && weekTypeObj[relaSub]){
				if((weekType == weekTypeObj[relaSub] && weekType !="3")
						|| (weekType !="3" && weekTypeObj[relaSub] == "3")
						|| (weekType =="3" && weekTypeObj[relaSub] != "3")){
					layer.tips("成对课程单双周属性必须相反",$(e).find('.weekType'), {
						tipsMore: true,
						tips: 3
					});
					f=false;
					return false;
				}
			}
			weekTypeObj[subjectId+"-"+subjectType] = weekType;
		}
		
		params.push({"subjectId":subjectId,"subjectType":subjectType,
			"courseWorkDay":courseWorkDay,"noArrangeTime":noArrangeTime,
			"combineClass":combineClass,"meanwhiles":meanwhiles,"weekType":weekType});
	});
	if(!f){return;}
	
	
	//params += "&classId="+classId;
	var ii = layer.load();
	$.ajax({
		url : "${request.contextPath}/newgkelective/"+arrayItemId+"/classFeature/save?classId="+classId,
		data : JSON.stringify(params),
		contentType:"application/json;charset=utf-8",
		success : function(data){
			layer.close(ii);
			if(data.success){
				xzbAllCountMap[classId] = countTotal();
				layer.msg("保存成功！",{offset:'t',time:2000});
			}
			else{
				alert(data.msg);
			} 
		},
		dataType: "JSON",
		type:"POST",
		error:function(data){alert(data);}
	});
	
	//console.log("保存");
}

// 班级全选
$(".js-allChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	var num=0;
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		if(!$(this).is(':disabled')){
			$(this).prop('checked',true);
			num++;
		}
	})
	$("#class_"+cId).find("span").text(""+num);
	$(closeDiv).find(".js-allChoose").hide();
	$(closeDiv).find(".js-clearChoose").show();
});

// 班级取消全选
$(".js-clearChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		$(this).prop('checked',false);
	})
	$("#class_"+cId).find("span").text("");
	$(closeDiv).find(".js-allChoose").show();
	$(closeDiv).find(".js-clearChoose").hide();
});
// 选中所有班级所有课程
$('input:checkbox[name=copyTeacherAll]').on('change',function(){
	var actioveDiv=$(".copyteacherTab").find("div.active");
	if($(this).is(':checked')){
		$('input:checkbox[name=copyTeacher]').each(function(i){
			if(!$(this).is(':disabled')){
				$(this).prop('checked',true);
			}
		})
	}else{
		$('input:checkbox[name=copyTeacher]').each(function(i){
			$(this).prop('checked',false);
		})
	}
	//整体数量操作
	$(".courseLi").each(function(){
		var cid=$(this).find("a").attr("data-value");
		//计算数量
		var length=$("#aaa_"+cid).find('input:checkbox[name=copyTeacher]:checked').length;
		if(length>0){
			$(this).find("span").text(""+length);
			$("#aaa_"+cid).find(".js-allChoose").hide();
			$("#aaa_"+cid).find(".js-clearChoose").show();
		}else{
			$(this).find("span").text("");
			$("#aaa_"+cid).find(".js-allChoose").show();
			$("#aaa_"+cid).find(".js-clearChoose").hide();
		}
	})
});

//点中数量
$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
	var closeDiv=$(this).closest("div");
	var course_id=$(closeDiv).attr("data-value");
	var num=$("#class_"+course_id).find("span.badge").text();
	if(num.trim()==""){
		num=parseInt(0);
	}else{
		num=parseInt(num);
	}
	if($(this).is(":checked")){
		//+1
		num=num+1;
	}else{
		//-1
		num=num-1;
	}
	if(num>0){
		$("#class_"+course_id).find("span.badge").text(""+num);
		//用取消
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	}else{
		$("#class_"+course_id).find("span.badge").text("");
		//用全选
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	}
});
//数字增加以及减少
$("div.table-container").on('click','.form-number > button',function(e){
	e.preventDefault();
	var $num = $(this).siblings('.form-control');
	var val = $num.val();
	if (!val ) val = 0;
	var num = parseInt(val);
	var step = $num.parent('.form-number').attr('data-step');
	if (step === undefined) {
		step = 1;
	} else{
		step = parseInt(step);
	}
	if ($(this).hasClass('form-number-add')) {
		num += step;
	} else{
		num -= step;
		if (num <= 0) num = 0;
	}
	
	$num.val(num);
	countTotal();
});

var jxbCount = xzbMoveCountMap[classId];
function countTotal(){
	var $surplus = $(".tree-list #"+classId+" .lecCount");
	if(!$surplus){
		return 0;
	}
	var xzbNum = 0;
	$("#classFeatures tbody tr").each(function(){
		var subId =  $(this).find(".subjectId").val();
		var subType =  $(this).find(".subjectType").val();
		var workDay =  parseInt($(this).find(".courseWorkDay").val());
		if(workDay > 0){
			if(firstsdSubjectMap[subId+"_"+subType]){
				workDay -=0.5;
			}
			xzbNum += workDay;
		}
	});
	
	var all = xzbNum + jxbCount;
	$(".tree-list #"+classId+" .lecCount").text(all);
	return all;
}
$("input.courseWorkDay").on("change",function(){
	countTotal();
});
function updateWeekType(obj){
	var subId = $(obj).parents("tr").find(".subjectId").val();
	var subType = $(obj).parents("tr").find(".subjectType").val();
	var code = subId+"_"+subType;
	var respCode = firstsdSubjectMap[code];
	var $sel = $("#classFeatures .subjectCode[value="+respCode+"]").parents("tr").find(".weekType");
	var type = parseInt(obj.value);
	if(type == 3){
		$sel.val("3");
	}else{
		$sel.val(3 - type);
	}
}
</script>