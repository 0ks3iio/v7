<div class="filter-item">
	<#-- <a class="btn btn-white js-add" onclick="importTeachGroup();">导入</a>  -->
	<a class="btn btn-blue" onclick="backTeacherFeature();">返回</a>
</div>
<div class="filter-item filter-item-right">
	<#-- <a class="btn btn-white js-add" onclick="importTeachGroup();">导入</a>  -->
	<a class="btn btn-blue js-add" onclick="initTg();">初始化</a>
	<a class="btn btn-blue js-add" onclick="addTeacherGroup();">新增</a>
	<a class="btn btn-danger js-add" onclick="deleteMoreTeacherGroup();">删除</a>
</div>
<div class="table-container-body">
	<table class="table table-bordered table-striped table-hover" id="tgTable">
		<thead>
			<tr>
				<th width="5%">
					<label>
						<input type="checkbox" class="wp" onclick="swapCheck()">
						<span class="lbl"> 全选</span>
					</label>
				</th>
				<th>序号</th>
				<th>教师组名称</th>
				<th width="50%">成员</th>
				<th width="10%">禁排时间</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
		<#if tgList?exists && (tgList?size gt 0)>
			<#list tgList as tg>
				<tr>
					<td>
						<label>
							<input type="checkbox" class="wp other">
							<span class="lbl"> </span>
							<input type="hidden" class="teacherGroupId" value="${tg.id!}">
						</label>
					</td>
					<td>${tg_index+1}</td>
					<td class="tgName">${tg.teacherGroupName!}</td>
					<td>
			    		<input type="hidden" class="teacherIds" value="${tg.teacherIdSet?join(',')}" >
						${tg.teacherNameStr!}
					</td>
					<td>
						<div class="clearfix js-hover">
				    		<input type="hidden" class="js-choosetime noArrangeTime" value="${tg.noTimeStr?default('')}" >
				    		<span class="pull-left js-hover-num color-blue"><#if tg.noTimeStr?? && tg.noTimeStr != ''>${tg.noTimeStr?split(",")?size}<#else>0</#if></span>
					    	<span class="pull-right js-hover-opt" style="display: block;">
					    		<a href="javascript:void(0)" class="js-changeTime" data-toggle="tooltip" data-placement="top" 
					    			title="" data-original-title="编辑" onclick="changeLimitTime(this)">
					    			<i class="fa fa-edit color-blue"></i>
					    		</a>
					    	</span>
				    	</div>
					</td>
					<td>
						<a class="table-btn" href="javascript:void(0);" onclick="addTeacherGroup(this);">修改</a>
						<a class="table-btn" href="javascript:void(0);" onclick="deleteTeacherGroup('${tg.id!}');">删除</a>
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="88" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
		</tbody>
	</table>
</div>
<#-- 新增 教师组  -->
<div class="layer layer-tg">
	<div class="layer-content">
		<div class="form-horizontal">
			<form id = "teacherGroupForm">
				<input type="hidden" name="teacherGroupId" value="">
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">教师组名称：</label>
					<div class="col-sm-8" id="sectionId">
						<input type="text" class="form-control" id="teacherGroupName" name="teacherGroupName" value="" nullable="false">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">成员：</label>
					<div class="col-sm-8">
						<input type="hidden" class="form-control teacherIds" value=""/>
						<input type="type" class="form-control teacherNames" onfocus="selectTea(this);" value=""/>
					</div>
				</div>
				<#-- 
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">排序号：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="orderId" name="orderId" value="${teachGroupDto.orderId!}" vtype="int" maxlength="4">
					</div>
				</div>  -->
			</form>
		</div>
		<#-- <div class="layer-footer" style="vertical-align: middle">
			<button class="btn btn-lightblue" onclick="saveGroup();">确定</button>
			<button class="btn btn-grey" id="arrange-close">取消</button>  -->
		</div>
	</div>
</div>

<#-- 选择教师 -->
<div class="layer layer-selTea">
	<div class="layer-content">
		
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各科教师</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher" class="form-control input-sm js-search" placeholder="输入教师姓名查询" value="">
			        </div>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default" onClick="findTeacher();">
					    	<i class="fa fa-search"></i>
					    </button>
				    </div>
			    </div>
			</div>
			<table class="table no-margin">
				<tr>
					<th width="127">科目</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px; height:448px;">
					<#if coumap?exists>
	                    <#list coumap?keys as mkey>
	                    	<li id="course_${mkey!}" class="courseLi <#if mkey_index == 0>active</#if>"><a  href="#aaa_${mkey!}" data-value="${mkey!}">${coumap[mkey]!}
	                    	<span class="badge badge-default"></span></a></li>
	                    </#list>
	                </#if>
				</ul>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId"style="position:relative;height:448px;overflow:auto;border-left: 1px solid #eee;">
					<#if coumap?exists>
	                    <#list coumap?keys as mkey>
	                    	<div id="aaa_${mkey!}"  data-value="${mkey!}">
								<div class="form-title ml-15 mt10 mb10">${coumap[mkey]!}<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> <a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> </div>
								<ul class="gk-copy-list">
									 <#if subjectTeacherPlanExMap?exists && subjectTeacherPlanExMap?size gt 0>
									 <#if subjectTeacherPlanExMap[mkey]?exists>
									 <#assign teachTeachers = subjectTeacherPlanExMap[mkey] />
									 <#if teachTeachers?exists && teachTeachers?size gt 0>
	                               	     <#list teachTeachers as tt >
											<label class="mr20">
												<input type="checkbox" class="wp" name="copyTeacher" value="${tt.teacherId!}" data-value="${tt.teacherName!}">
												<span class="lbl"> ${tt.teacherName!}</span>
											</label>
										 </#list>
									 </#if>
									 </#if>
									 </#if>
								</ul>
							</div>
	                    </#list>
	                </#if>
				</div>
			</div>
		</div>
		<div class="no-data-container" id="noDataId" style="display:none;">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">没有相关数据</p>
				</div>
			</div>
		</div>
	</div>
</div>

<#---时间---->
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

<script>


var isCheckAll = false;  
function swapCheck() {
	if (isCheckAll) {  
        $("input[type='checkbox']").each(function() {  
            this.checked = false;  
        });  
        isCheckAll = false;  
    } else {  
        $("input[type='checkbox']").each(function() {  
            this.checked = true;  
        });  
        isCheckAll = true;  
    }  
}

function refreshTg(){
	var url = '${request.contextPath}/newgkelective/${gradeId!}/goBasic/teacherGroup/index';
	$("#gradeTableList").load(url);
}

function addTeacherGroup(obj) {
	// 清空数据
	$("#teacherGroupName").val("");
	$("#teacherGroupForm .teacherIds").val("");
	$("#teacherGroupForm .teacherNames").val("");
	
	var tgId ="";
	if(obj){
		var tgName = $(obj).parents("tr").find(".tgName").text();
		var tids = $(obj).parents("tr").find(".teacherIds").val();
		var tNames = $(obj).parents("tr").find(".teacherIds").parent().text().trim();
		$("#teacherGroupName").val(tgName);
		$("#teacherGroupForm .teacherIds").val(tids);
		$("#teacherGroupForm .teacherNames").val(tNames);
		
		tgId = $(obj).parents("tr").find(".teacherGroupId").val();
	}
	
	layer.open({
			type: 1,
			shadow: 0.5,
			title: '教师组设置',
			area: '520px',
			btn: ['确定', '取消'],
			scrollbar:false,
			btn1:function(index){
				
				saveGroup(tgId);
			},
			btn2:function(){
				layer.closeAll();
			},
			content: $('.layer-tg')
		});
}

function saveGroup(tgId){
	if(!check(tgId)){
		return;
	}
	if(!tgId){
		tgId = "";
	}
	var teacherGroupName = $("#teacherGroupName").val();
	var teacherIds = $("#teacherGroupForm .teacherIds").val();
	var tidArr = teacherIds.split(",");
	$.ajax({
		url:"${request.contextPath}/newgkelective/${gradeId!}/goBasic/teacherGroup/save",
		data:{"id":tgId,"teacherGroupName":teacherGroupName,"teacherIds":tidArr},
		dataType: "json",
		success: function(data){
			if(data.success){
				layer.closeAll();
				layer.msg(data.msg, {offset: 't',time: 2000});
				refreshTg();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		}
	});
	
}
function check(tgId){
	var teacherGroupName = $("#teacherGroupName").val();
	if(!teacherGroupName){
		layer.msg('教师组名称 不能为空', {
			icon: 2,
			time: 1500,
			shade: 0.2
		});
		return false;
	}
	if(teacherGroupName.length>50){
		layer.msg('教师组名称长度 不能超过50', {
			icon: 2,
			time: 1500,
			shade: 0.2
		});
		return false;
	}
	var f = true;
	$("#tgTable tbody tr").each(function(i,obj){
		var name = $(obj).find(".tgName").text();
		var teacherGroupId = $(obj).find(".teacherGroupId").val();
		if(teacherGroupId != tgId && teacherGroupName == name){
			f = false;
		}
		
	});
	if(!f){
		alert("教师组名称 不能重复");
		return false;
	}
	
	return f;
}

function alertTeacherGroup(tgId) {
	addTeacherGroup();
}

function deleteTeacherGroup(teachGroupId) {
	doDel([teachGroupId]);
}

function deleteMoreTeacherGroup() {
	var teacherGroupIds =[];
	var items = $("input.other:checked");
	if(items.length == 0) {
		layer.alert('请先选择教师组',{icon:7});
		return;
	}else {
		items.each(function(i){
			var tgId = $(this).parents("tr").find(".teacherGroupId").val();
			teacherGroupIds.push(tgId);
		});
	}
	
	doDel(teacherGroupIds);
}

function doDel(teacherGroupIds){
	if(!teacherGroupIds || teacherGroupIds.length == 0){
		layerTipMsg(false,"失败","请选择要删除的对象");
	}
	
	layer.confirm('确认删除么？', function(index){
	    $.ajax({
			url:"${request.contextPath}/newgkelective/${gradeId!}/goBasic/teacherGroup/del",
			data:{"teacherGroupIds":teacherGroupIds},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
					refreshTg();
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	});
}

function backTeacherFeature(){
	teaTable();
}

function findTeacher(){
	var teacherName=$('#findTeacher').val().trim();
	if(teacherName!=""){
		
		$(".gk-copy-main .lbl").removeClass("color-blue");
		var first;
		$(".gk-copy-main input").each(function(){
			var objVal = $(this).attr("data-value");
			if (objVal.includes(teacherName)) {
				if(!first){
					first=$(this);
				}
				$(this).siblings().addClass("color-blue");
			}
		});
		if(first){
			//模仿锚点定位
			var divId=$(first).parents("div").attr("id");
			document.getElementById(divId).scrollIntoView();
			
			var buid = $(first).parents("div").attr("data-value");
			document.getElementById("course_"+buid).scrollIntoView();
			setTimeout(function(){
				$("#course_"+buid).siblings().removeClass("active");
				$("#course_"+buid).addClass("active");
			},50);
		}
	}else{
		$(".gk-copy-main .lbl").removeClass("color-blue");
	}
}
// 选择老师 界面 搜索 
$('#findTeacher').bind('keypress',function(event){//监听回车事件
    if(event.keyCode == "13" || event.which == "13")    
    {  
        findTeacher();
    }
});

//点中数量
$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
	var closeDiv=$(this).closest("div");
	var course_id=$(closeDiv).attr("data-value");
	var num=$("#course_"+course_id).find("span.badge").text();
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
		$("#course_"+course_id).find("span.badge").text(""+num);
		//用取消
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	}else{
		$("#course_"+course_id).find("span.badge").text("");
		//用全选
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	}
})
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
	$("#course_"+cId).find("span").text(""+num);
	$(closeDiv).find(".js-allChoose").hide();
	$(closeDiv).find(".js-clearChoose").show();
})

$(".js-clearChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		$(this).prop('checked',false);
	})
	$("#course_"+cId).find("span").text("");
	$(closeDiv).find(".js-allChoose").show();
	$(closeDiv).find(".js-clearChoose").hide();
});

function clearSelTea2(){
	//1. 清除旧数据  2. 设置默认数据
	//数字清空
	$(".gk-copy-nav").find("span").each(function(){
		$(this).text("");
	});
	//去除之前的查询结果
	$('#findTeacher').val('');
	findTeacher();
	
	//取消全选
	$('input:checkbox[name=copyTeacherAll]').prop("checked",false);
	
	//tab默认选中本科目
	$(".courseLi").removeClass("active").eq(0).addClass("active");
	
	//取消所有选中老师与复制的条件
	$('.layer-selTea').find("input:checkbox[name=copyTeacher]").prop('checked',false);
	
	//取消所有不可选
	$('.layer-selTea').find("input:checkbox[name=copyTeacher]").prop('disabled',false);
	
	//全选显示 取隐藏
	$(".js-allChoose").show();
	$(".js-clearChoose").hide();

}

function selectTea(obj){
	clearSelTea2();
	
	var tid_str = $("#teacherGroupForm .teacherIds").val();
	var tids = tid_str.split(",");
	$('.layer-selTea').find("input:checkbox[name=copyTeacher]").each(function(){
		if(tids.indexOf($(this).val()) > -1){
			$(this).click();
		}
	});
	
	$(obj).blur();
	layer.open({
			type: 1,
			shadow: 0.5,
			title: '挑选教师',
			area: ['1000px','660px'],
			btn: ['确定', '取消'],
			scrollbar:false,
			btn1:function(index){
				var tid_str = "";
				var tn_str = "";
				$('.layer-selTea').find("input:checkbox[name=copyTeacher]:checked").each(function(){
					var tid = $(this).val();
					tid_str += "," + tid;
					tn_str += "," + $(this).find(" + span").text();
				});
				tid_str = tid_str.substr(1);
				tn_str = tn_str.substr(1);
				$("#teacherGroupForm .teacherIds").val(tid_str);
				$("#teacherGroupForm .teacherNames").val(tn_str);
				
				layer.close(index);
			},
			btn2:function(index){
				layer.close(index);
			},
			content: $('.layer-selTea')
		});
}

//禁排
function changeLimitTime(obj){
	var chooseValueObj=$(obj).parents("td").find(".js-choosetime");
	var oldTimes=$(chooseValueObj).val();
	var tgId= $(obj).parents("tr").find(".teacherGroupId").val();
	showTime(tgId,oldTimes,chooseValueObj,false);
}

function showTime(tgId,oldClikeTimes,divKeyObj,isNear){
	
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
		title: "禁排时间",
		area: '620px',
		btn: ['确定', '取消'],
		btn1:function(){
			
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
				chooseTimes = chooseTimes.substr(1);
				$(divKeyObj).val(chooseTimes);
			}else{
				$(divKeyObj).val(chooseTimes);
			}
			$(divKeyObj).parents("td").find(".js-hover-num").html(arr.length);
			
			saveNoTime(tgId, chooseTimes);
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-timeArea')
	});
}

function saveNoTime(tgId, chooseTimes){
	var tidArr = [];
	if(chooseTimes){
		tidArr = chooseTimes.split(",");
	}
	
	$.ajax({
		url:"${request.contextPath}/newgkelective/${gradeId!}/goBasic/teacherGroup/saveNoTime",
		data:{"teacherGroupId":tgId,"noTimeArr":tidArr},
		dataType: "json",
		success: function(data){
			if(data.success){
				layer.closeAll();
				layer.msg(data.msg, {offset: 't',time: 2000});
				//refreshTg();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		}
	});
}

function initTg(){
	layer.confirm('初始化教师组？<br>将使用教师特征的数据初始化教师组', function(index){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${gradeId!}/goBasic/teacherGroup/init",
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
					refreshTg();
				}
				else{
					layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	});
}

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

$('[data-toggle="tooltip"]').tooltip({
	container: 'body',
	trigger: 'hover'
});

$(function(){
	$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' });
});
 
 
</script>