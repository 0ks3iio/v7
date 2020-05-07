<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="main-content-inner">
	<div class="page-content">
		<div class="box box-default">
		<div class="box-body">
	<div class="filter-container">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">学年：</span>
			<div class="filter-content">
				<select name="" class="form-control" id="acadyear" onChange="searchWeek();">
				<#if acadyearList?exists && acadyearList?size gt 0>
				  <#list acadyearList as item>
					 <option value="${item!}" <#if '${acadyear!}' == '${item!}'>selected</#if>>${item!}</option>
				  </#list>
			    </#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">学期：</span>
			<div class="filter-content">
				<select name="" id="semester" class="form-control" onChange="searchWeek();">
					<option value="1" <#if '${semester!}' == '1'>selected</#if>>第一学期</option>
					<option value="2" <#if '${semester!}' == '2'>selected</#if>>第二学期</option>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">周次：</span>
			<div class="filter-content">
				<select name="" id="week" class="form-control" onChange="searchList3();">
				<#if weekArray?exists && weekArray?size gt 0>
				  <#list weekArray as item>
					 <option value="${item!}" <#if '${week!}' == '${item!}'>selected</#if>>${item!}</option>
				  </#list>
				<#else>
				     <option value='' >---请选择---</option>
			    </#if>
				</select>
			</div>
		</div>
	</div>
</div>
<div class="week-choose">
	<button class="btn btn-long <#if '${weekDay!}' == '7'>btn-blue<#else>btn-white</#if>" value="7" onClick="searchList2('7');">周日</button>
	<button class="btn btn-long <#if '${weekDay!}' == '1'>btn-blue<#else>btn-white</#if>" value="1" onClick="searchList2('1');">周一</button>
	<button class="btn btn-long <#if '${weekDay!}' == '2'>btn-blue<#else>btn-white</#if>" value="2" onClick="searchList2('2');">周二</button>
	<button class="btn btn-long <#if '${weekDay!}' == '3'>btn-blue<#else>btn-white</#if>" value="3" onClick="searchList2('3');">周三</button>
	<button class="btn btn-long <#if '${weekDay!}' == '4'>btn-blue<#else>btn-white</#if>" value="4" onClick="searchList2('4');">周四</button>
	<button class="btn btn-long <#if '${weekDay!}' == '5'>btn-blue<#else>btn-white</#if>" value="5" onClick="searchList2('5');">周五</button>
	<button class="btn btn-long <#if '${weekDay!}' == '6'>btn-blue<#else>btn-white</#if>" value="6" onClick="searchList2('6');">周六</button>
</div>
</div>
</div>
        <div class="row">
			<div class="col-sm-3">
				<div class="box box-default" id="id1">
					<div class="box-header">
						<h3 class="box-title">班级菜单</h3>
					</div>
					<#if unitClass?default(-1) != 2>
						<div class="box-body">
						   <ul id="schoolTree" class="ztree"></ul>
						</div>
					<#else>
						<@dytreemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
					</#if>
			</div>
		</div>
		<div class="col-sm-9">
			<div class="box box-default" id="showList">
				<div class="box-body" id="id2">										
				</div>
			</div>
		</div>
</div>
</div>
</div>
<input type="hidden" id="classId">
<script>
$(document).ready(function(){
    $('#id2').height($('#id1').height());
});

$('.week-choose .btn').on('click', function(){
	$(this).removeClass('btn-white').addClass('btn-blue').siblings().removeClass('btn-blue').addClass('btn-white')
});

function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "class"){
		var id = treeNode.id;
		$('#classId').val(id);
		searchList(id);
	}
}

function searchList(classId){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var week = $('#week').val();
    var weekDay = $('.btn-blue').val()
    if(week == ''){
        layerTipMsgWarn("提示","请选择周次！");
	    return;
    }
    var str = "?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&weekDay="+weekDay+"&classId="+classId;
    var url = "${request.contextPath}/stuwork/courserecord/recordCollectList"+str;
    $("#showList").load(url);
}

function searchList2(weekDay){
    var classId = $('#classId').val();
    if(classId != ''){
       var acadyear = $('#acadyear').val();
       var semester = $('#semester').val();
       var week = $('#week').val();
       var str = "?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&weekDay="+weekDay+"&classId="+classId;
       var url = "${request.contextPath}/stuwork/courserecord/recordCollectList"+str;
       $("#showList").load(url);
    }
}

function searchList3(){
    var classId = $('#classId').val();
    if(classId != ''){
       var acadyear = $('#acadyear').val();
       var semester = $('#semester').val();
       var week = $('#week').val();
       var weekDay = $('.btn-blue').val();
       var str = "?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&weekDay="+weekDay+"&classId="+classId;
       var url = "${request.contextPath}/stuwork/courserecord/recordCollectList"+str;
       $("#showList").load(url);
    }
}

function searchWeek(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var weekSel=$("#week");
    $.ajax({
			url:"${request.contextPath}/stuwork/courserecord/searchWeek",
			data:{acadyear:acadyear,semester:semester},
			dataType: "json",
			success: function(data){
			weekSel.html("");
			weekSel.chosen("destroy");
			if(data==null){
				weekSel.append("<option value='' >---请选择---</option>");
			}else{
			    weekSel.append("<option value='' >---请选择---</option>");
				for(var m=0;m<data.length;m++){
				weekSel.append("<option value='"+data[m]+"' >"+data[m]+"</option>");
				}
			}
			if(m==0){
				//subjectIdSel.append("<option value='' >---请选择---</option>");
			}
			}
		});
}
</script>