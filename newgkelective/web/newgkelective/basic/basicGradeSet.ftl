<style>
	#table01 > tbody > tr.AB > td,#table01 > tbody > tr.PB > td,#table01 > tbody > tr.NS > td{border-right:none;border-left:none;background: #f2f2f2;}
	#table01 > tbody > tr.AB > td:first-child,#table01 > tbody > tr.PB > td:first-child,#table01 > tbody > tr.NS > td:first-child{border-right:1px solid #ddd;background: #fff;}
</style>
<div id="a1" class="tab-pane active">
	<div class="filter">														
		<div class="filter-item">
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-MS" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 早自习</span></label>
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-AM" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 上午</span></label>
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-AB" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 上午大课间</span></label>
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-NS" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 午休</span></label>	
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-PM" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 下午</span></label>
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-PB" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 下午大课间</span></label>
			<label class="mr20"><input type="checkbox" name="checkbox" class="wp" id="chk-ES" onclick="ClassTableTime.chkChange(this);"><span class="lbl"> 晚自习</span></label>
		</div>
	</div>
	<div class="table-container">
		<div class="table-container-header">
			<div class="schedule-labels js-schedule-labels" data-for="table01">
				<input type="hidden" name="filterType" id="type${gradeId!}" value="1">
				<input type="hidden" name="objId" id="objId1" value="${gradeId!}">
				<input type="hidden" name="groupType" id="groupType1" value="1">
				<input type="hidden" name="is_join" id="isJoin1" value="0">
				<input type="hidden" name="timeType" id="timeType1" value="01">
				<label class="schedule-label schedule-label-white schedule-label-1 selected" id="data1" data-filter="1" data-filter-content="不可排课"><i class="fa fa-plus"></i> 不可排课</label>
				
				<#if subs?exists && subs?size gt 0>
				<input type="hidden" name="filterType" id="type${fixGuid!}" value="2">
				<input type="hidden" name="objId" id="objId2" value="${fixGuid!}">
				<input type="hidden" name="groupType" id="groupType2" value="1">
				<input type="hidden" name="is_join" id="isJoin2" value="1">
				<input type="hidden" name="timeType" id="timeType2" value="02">
				<label class="schedule-label schedule-label-white schedule-label-2" id="data2" data-filter="2" data-objid="${fixGuid!}" data-filter-content="固定排课"><i class="fa fa-plus"></i>固定排课</label>
				</#if>
			</div>
		</div>
		<div class="table-container-body">
			<form id="gradeTime" action="" method="POST">
			<input type="hidden" name="needSource" value="true">
			<input type="hidden" name="selGradeId" value="${gradeId!}">
			<input type="hidden" name="gradeDto.mornPeriods" id="mornPeriods" value="${mornPeriods?default(0)}">
			<input type="hidden" name="gradeDto.amLessonCount" id="amCount" value="${amCount?default(1)}">
			<input type="hidden" name="gradeDto.pmLessonCount" id="pmCount" value="${pmCount?default(1)}">
			<input type="hidden" name="gradeDto.nightLessonCount" id="nightCount" value="${nightCount?default(0)}">
			<input type="hidden" name="gradeDto.recess" id="recess" value="${recess?default('')}">
			<table id="table01" class="table table-bordered table-schedule text-center" data-filter="100">
				<tbody>
					<tr class="theader">
						<th class="text-center" width="180">节次</th>
						<#list 0..(weekDays-1) as day>
						<th class="text-center" name="${day}">${dayOfWeekMap2[day+""]!}</th>
						</#list>
					</tr>
				</tbody>
			</table>
			</form>
		</div>
	</div>
	<div class="navbar-fixed-bottom opt-bottom">
		<a src="javascript:void(0);" class="btn btn-blue" id="confirm-submit">保存</a>
	</div>
</div>
<script>
/*当前操作类型*/
var type = 1; 
var content="不可排课";
/*课时计数器*/
var selTdCount=0;

function canLoadOther(){
	return true;
}
function backClick(){
	//如果不执行切换
}
$('.js-schedule-labels label').on('click',function(){
	if(!$(this).hasClass('selected')){
		$(this).addClass('selected').siblings().removeClass('selected');
	}
	type = $(this).data('filter');
	content = $(this).data('filter-content');
});

function initTdEdit(){
	$('#table01').find('.edited').off('click').on('click', function(e){
		var pb = $(this).parent();
		if(pb.hasClass('AB') || pb.hasClass('PB') || pb.hasClass('NS')){
			layer.msg("此为休息时段，请选择其他行操作！", {
						offset: 't',
						time: 2000
					});
			return;
		}
		if($(this).children().length == 0){
			var label = getTdContent(this);
			$(this).append(label);
		}
	});
	
	$('#table01 .edited').on('click', '.del', function(e){
		e.stopPropagation();
		$(this).parent().remove();
	});
}

function getInter(obj){
	var dr = $(obj).attr('data-row');
	var drs = dr.split('-');
	var pi = '';
	switch (drs[0]) {
		case "MS":
         pi='1';break;
         case "AM":
         pi='2';break;
         case "PM":
         pi='3';break;
         case "ES":
         pi='4';break;
    }
    var pe = parseInt(drs[1])+1;
    return {peinter:pi,period:pe};
}

function getTdContent(obj, selType, selContent){
	if(!selType){
		selType = type;
	}
	if(!selContent){
		selContent = content;
	}
	
	//找出在一天中的那个时间段
	var rowInf = getInter(obj);
	var period_interval = rowInf.peinter;
	//这个时间段中的第几节课
	var period = rowInf.period;
	//找出是周几
	var weekday;
	if($(obj).parent().children("[rowspan]").length>0){
		weekday = obj.cellIndex-2;
	}else{
		weekday = obj.cellIndex-1;
	}
	var tdi = selTdCount;
	selTdCount=selTdCount+1;
	return getTdContentStr(selType, selContent,tdi, weekday,period_interval,period);
}

function getTdContentStr(selType, selContent, tdi, weekday, period_interval, period,objId){
	if(selContent.length>7){
		selContent=selContent.substring(0,7);
	}
	
	var join = $('#isJoin'+selType).val();
	if(!objId){
		objId = $('#objId'+selType).val();
	}
	if(objId=='${fixGuid!}'){
		objId='';
	}
	var gtype = $('#groupType'+selType).val();
	var ttype = $('#timeType'+selType).val();
	var selCo = parseInt(selType);
	if(selCo>7){
		selCo = selCo-6;
	}
	var label = '<div><input type="hidden" name="lessonTimeDto['+tdi+'].weekday" value="'+weekday+'"/>'
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].period_interval" value="'+period_interval+'"/>'
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].period" value="'+period+'"/>'
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].is_join" value="'+join+'"/>'
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].timeType" value="'+ttype+'"/>';
	
	label = label + getObjStr(ttype,tdi,selType,selCo,selContent,objId)
		+'<input type="hidden" name="sourceTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
		+'<input type="hidden" name="sourceTimeDto['+tdi+'].is_join" value="'+join+'"/></div>';
		
	return label;
}

function getObjStr(ttype,tdi,selType,selCo,selContent,objId){
	var strs = '';
	if(ttype == '01'){
		strs = '<label class="schedule-label schedule-label-'+selCo+' thin selected label-children" data-label="'+selType+'">'+selContent+'</label><span class="del"><strong class="fa fa-times-circle"></strong></span>' 
		+'<input type="hidden" name="lessonTimeDto['+tdi+'].objId" value="'+objId+'"/>'
		+'<input type="hidden" name="sourceTimeDto['+tdi+'].objId" value="'+objId+'"/>';
	} else {
		strs = '<select name="lessonTimeDto['+tdi+'].objId" class="form-control label-children label-timeselect" source-index="'+tdi+'" onchange="$(\'#sourceObjId'+tdi+'\').val(this.value);" data-label="'+selType+'">';
		var sobjid = objId;
		<#list subs as sub>
		strs += '<option value="${sub.id!}"';
		if((objId!='' && '${sub.id!}'==objId) || (objId=='' && '${sub_index}'=='0')){
			strs+=' selected';
			sobjid = '${sub.id!}';
		}
		strs +='>${sub.subjectName!}</option>'
		</#list>
		strs += '</select><span class="del"><strong class="fa fa-times-circle"></strong></span>';
		strs += '<input type="hidden" name="sourceTimeDto['+tdi+'].objId" id="sourceObjId'+tdi+'" value="'+sobjid+'"/>'; 
	}
	return strs;
}

<#-- 初始化数据组装，需要修改 -->
//对不排课的位置，模拟点击
function imitateClick(lessonInfJson){
	var length = lessonInfJson.length;
	$.each(lessonInfJson,function(i,e){
		var objId = e.objId;
		var period_interval = e.period_interval;
		var period = e.period;
		var weekday = e.weekday;
		var trCls = '';
		switch(period_interval){
			case '1':
				trCls='MS';break;
			case '2':
				trCls='AM';break;
			case '3':
				trCls='PM';break;
			case '4':
				trCls='ES';break;
		}
		var $tr = $('.row'+trCls+'-'+(period-1));
		var typeobj = objId;
		if(typeobj != '${gradeId!}'){
			typeobj='${fixGuid!}';
		}
		var tdtype = $('#type'+typeobj).val(); 
		var tdlabel = $('#data'+tdtype);
		var tdcontent = $(tdlabel).data('filter-content');
		var tdobj = $tr.find(".edited:eq("+weekday+")");
		var tdi = selTdCount;
		selTdCount=selTdCount+1;
		var label = getTdContentStr(tdtype, tdcontent, tdi, weekday, period_interval, period,objId);
		$(tdobj).empty().append(label);
	});
}

//如果是编辑功能，页面加载完毕时，获取课时安排数据，并且模拟点击
$(function(){
	var array_item_id = $("input[name='array_item_id']").val();
	if(array_item_id==""){
		return;
	}
	var url = "${request.contextPath}/newgkelective/"+array_item_id+"/gradeLessonTimeinf/json";
	$.post(url, 
		function(data){
			var dataJson = $.parseJSON(data);
			//模拟点击总课表
			imitateClick(dataJson.lessonTimeDtosJson);
		});
});

$('#confirm-submit').click(function(){
    if(arrayId){
        layer.confirm('如果您还未完成排课，修改此结果可能会影响预排课表部分课程，继续吗？',
            function(index){
                saveGrade();
            }
        );
    }else{
        saveGrade();
    }
});

var hasSubmit=false;
function saveGrade(){
	if(hasSubmit){
		return;
	}
	hasSubmit=true;
	
	var amc = $("#table01 tr.AM").length;
	var pmc = $("#table01 tr.PM").length;
	var nmc = $("#table01 tr.ES").length;
	var ab = $("#table01 tr.AB").length;
	var pb = $("#table01 tr.PB").length;
	var ms = $("#table01 tr.MS").length;
	var res = '';
	if(ab > 0){
		var ami = parseInt($("#table01 tr.AB").prev().attr('inter-index'))+1;
		res='2'+ami;
	}
	if(pb > 0){
		var pmi = parseInt($("#table01 tr.PB").prev().attr('inter-index'))+1;
		if(res != ''){
			res=res+',';
		}
		res=res+'3'+pmi;
	}
	
	$('#amCount').val(amc);
	$('#pmCount').val(pmc);
	$('#nightCount').val(nmc);
	$('#recess').val(res);
	$('#mornPeriods').val(ms);
	var array_item_id = $('[name="array_item_id"]').val();
	var surl = '${request.contextPath}/newgkelective/${gradeId!}/goBasic/gradeSetSave?arrayItemId='
			+array_item_id+'&objectType=${objectType!'0'}&arrayId='+arrayId;
	var params = $("#gradeTime").serialize();

	$.post(surl, 
		params, 
		function(data){
			hasSubmit=false;
			var dataJson = $.parseJSON(data);
			if(!dataJson.success){
		 		layerTipMsg(dataJson.success,"保存失败",dataJson.msg);
		 	}else{
				layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
			}
			return;
		});
}

function initRowPk(period,wkd,pi){
	var tdi = selTdCount;
	selTdCount=selTdCount+1;
	return getTdContentStr('1', '不可排课', tdi, wkd, pi, period, '${gradeId!}');
}

var xy = function () { };
(function (xy) {			
    //字符串处理
    xy.string = {
        //格式字符串
        //例：xy.string.format("您好：{0}，现在是：{1}","某某某","2016-04-19")
        format: function (source, params) {
            params = params === null ? "" : params;
            if (!params && params != 0) { return source; }
            if (arguments.length == 1)
                return function () {
                    var args = $.makeArray(arguments);
                    args.unshift(source);
                    return strFormatapply(this, args);
                };
            if (arguments.length > 2 && params.constructor != Array) {
                params = $.makeArray(arguments).slice(1);
            }
            if (params.constructor != Array) {
                params = [params];
            }
            $.each(params, function (i, n) {
                source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
            });
            return source;
        }
    }
    
})(xy);

var firstRowStyle='style="border-top: 3px solid #ddd;"'
var handOp = false;
var ClassTableTime = {

    //类型按钮单击事件
    chkChange: function (e) {
        var $this = $(e);
        var classStr = $(e).attr("id").replace("chk-", "");
        
        var ms = 1;
        if(${msCount?default(0)} > 0){
        	ms = ${msCount?default(0)};
        }
        var amc = 4;
        if(${amCount?default(0)} > 0){
        	amc = ${amCount?default(0)};
        }
        var pmc = 4;
        if(${pmCount?default(0)} > 0){
        	pmc = ${pmCount?default(0)};
        }
        var nic = 1;
        if(${nightCount?default(0)} > 0){
        	nic = ${nightCount?default(0)};
        }
        if ($this.is(':checked')) {
            switch (classStr) {
                case "MS":
                    $("#table01 tr.theader").after(ClassTableTime.createTrTd(classStr, ms));
                    break;
                case "AM":
                    $("#table01 tr.MS,tr.theader").last().after(ClassTableTime.createTrTd(classStr, amc));
                    break;
                case "AB":
                    var count = $("#table01 tr.AM").length;
                    if (count >= 2) {
                        if (count == 2) {
                            $("#table01 tr.AM").first().after(ClassTableTime.createTrTd(classStr, 1));
                        }
                        else {
                            $("#table01 tr.AM").eq(${ab?default(1)}).after(ClassTableTime.createTrTd(classStr, 1));
                        }
                    }
                    else {
                    	layer.msg('上午课时小于2，不足以有大课间');
                        $this.removeAttr("checked");
                    }
                    break;
                case "NS":
                    if ($("#table01 tr.AM").length > 0 && $("#table01 tr.PM").length > 0) {
                        $("#table01 tr.AM").last().after(ClassTableTime.createTrTd(classStr, 1));
                    }
                    else {
                    	layer.msg('当上午和下午同时有课时，才可以设置午休');
                        $this.removeAttr("checked");
                    }
                    break;    
                case "PM":
                    $("#table01 tr.theader,tr.MS,tr.AM").last().after(ClassTableTime.createTrTd(classStr, pmc));
                    break;
                case "PB":
                    var count = $("#table01 tr.PM").length;
                    if (count >= 2) {
                        if (count == 2) {
                            $("#table01 tr.PM").first().after(ClassTableTime.createTrTd(classStr, 1));
                        }
                        else {
                            $("#table01 tr.PM").eq(${pb?default(1)}).after(ClassTableTime.createTrTd(classStr, 1));
                        }
                    }
                    else {
                    	layer.msg('下午课时小于2，不足以有大课间');
                        $this.removeAttr("checked");
                    }
                    break;
                case "ES":
                    $("#table01").append(ClassTableTime.createTrTd(classStr, nic));
                    break;
                default:
                    break;
            }
        }
        else {
            handOp=true;
            $(xy.string.format("#table01 tr.{0}", classStr)).remove();
            if (classStr == "AM") { $("#table01 tr.AB,tr.NS").remove(); $("#chk-AB,#chk-NS").removeAttr("checked"); }
			if (classStr == "PM") { $("#table01 tr.PB,tr.NS").remove(); $("#chk-PB,#chk-NS").removeAttr("checked"); }
        }

        //设置行名称
        ClassTableTime.setRowName();
    },

    //创建 TR TD
    createTrTd: function (trClass, trCount) {
        var isbreak = trClass.indexOf('B')!=-1;
        var trStr = "";
        //同类开型已经有的行数据
        var rowStartIndex = $(xy.string.format("#table01 tr.{0}", trClass)).length;
        //创建TR
        var pi='';
        if(trClass=='MS'){
        	pi='1';
        }else if(trClass=='AM'){
        	pi='2';
        } else if(trClass=='PM'){
        	pi='3';
        } else if(trClass=='ES'){
        	pi='4';
        }
        for (var j = 0; j < trCount; j++) {
            var tdStr = "";
            //创建TD
            for (var i = 0; i < ${weekDays+1}; i++) {
                var colHeader = $(xy.string.format("#table01 tr th:eq({0})", i));
                var colHeaderName = colHeader.attr("name");
                if ($.isEmptyObject(colHeaderName)) colHeaderName = "";
                var classStr = "content-02";
                var onclick = "";
                if (i > 0) {
                    var noDisplay = "";
                    classStr = xy.string.format("edited {0}", noDisplay);
                }
                var tdh='';
                if(handOp && pi != '' && i>=6){
                	tdh=initRowPk(rowStartIndex+j+1,i-1,pi);
                }
                tdStr += xy.string.format("<td data-col='{0}' name='{1}' class='{2}' data-row='{3}-{4}' {5}>"+tdh+"</td>", i - 1, colHeaderName, classStr, trClass, rowStartIndex + j, onclick);
            }
			var trStyle = '';
			if(!isbreak && rowStartIndex==0 && j == 0){
				trStyle = firstRowStyle;
			}
            trStr += xy.string.format("<tr class='{0} row{0}-{1}' inter-index='{1}' {2}>{3}</tr>", trClass, rowStartIndex + j, trStyle, tdStr);
        }

        return trStr;
    },

    //设置行名称
    setRowName: function () {
        var zhNumArray = new Array("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二");
        $("#table01 tr.MS td.content-02").each(function (i) { $(this).html(xy.string.format("<span class=\"pull-left\">早自习{0}</span>", zhNumArray[i])); });
        $("#table01 tr.AM td.content-02,#table01 tr.PM td.content-02").each(function (i) { $(this).html(xy.string.format("<span class=\"pull-left\">第{0}节</span>", zhNumArray[i])); });
        $("#table01 tr.AB td.content-02").each(function (i) { $(this).html("<span class=\"pull-left\">上午大课间</span><span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-arrow-circle-up color-blue' onclick=\"ClassTableTime.upRow('AB');\"></span></a><span> 课间 </span><a href=\"javascript:;\"><span class='fa fa-arrow-circle-down color-blue' onclick=\"ClassTableTime.downRow('AB');\"></span></a></span>"); });
        $("#table01 tr.NS td.content-02").each(function (i) { $(this).html("<span class=\"pull-left\">午休</span>"); });
        $("#table01 tr.PB td.content-02").each(function (i) { $(this).html("<span class=\"pull-left\">下午大课间</span><span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-arrow-circle-up color-blue' onclick=\"ClassTableTime.upRow('PB');\"></span></a><span> 课间 </span><a href=\"javascript:;\"><span class='fa fa-arrow-circle-down color-blue' onclick=\"ClassTableTime.downRow('PB');\"></span></a></span>"); });
		$("#table01 tr.ES td.content-02").each(function (i) { $(this).html(xy.string.format("<span class=\"pull-left\">晚自习{0}</span>", zhNumArray[i])); });
		
        //设置图标
        $("#table01 tr.MS td.content-02").last().append("<span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-plus-circle color-blue' onclick=\"ClassTableTime.addRow('MS',${maxMm?default(2)});\"></span></a><span> 自习 </span><a href=\"javascript:;\"><span class='fa fa-minus-circle color-blue' onclick=\"ClassTableTime.deleteRow('MS');\"></span></span></a>");
        $("#table01 tr.AM td.content-02").last().append("<span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-plus-circle color-blue' onclick=\"ClassTableTime.addRow('AM',${maxam?default(6)});\"></span></a><span> 上午 </span><a href=\"javascript:;\"><span class='fa fa-minus-circle color-blue' onclick=\"ClassTableTime.deleteRow('AM');\"></span></span></a>");
        $("#table01 tr.PM td.content-02").last().append("<span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-plus-circle color-blue' onclick=\"ClassTableTime.addRow('PM',${maxpm?default(6)});\"></span></a><span> 下午 </span><a href=\"javascript:;\"><span class='fa fa-minus-circle color-blue' onclick=\"ClassTableTime.deleteRow('PM');\"></span></span></a>");
        $("#table01 tr.ES td.content-02").last().append("<span class=\"pull-right\"><a href=\"javascript:;\"><span class='fa fa-plus-circle color-blue' onclick=\"ClassTableTime.addRow('ES',${maxNi?default(4)});\"></span></a><span> 自习 </span><a href=\"javascript:;\"><span class='fa fa-minus-circle color-blue' onclick=\"ClassTableTime.deleteRow('ES');\"></span></span></a>");

        //设置选择框的选中状态
        $("#table01 tr.MS").length > 0 ? $("#chk-MS").attr("checked", "checked") : $("#chk-MS").removeAttr("checked");
        $("#table01 tr.AM").length > 0 ? $("#chk-AM").attr("checked", "checked") : $("#chk-AM").removeAttr("checked");
        $("#table01 tr.AB").length > 0 ? $("#chk-AB").attr("checked", "checked") : $("#chk-AB").removeAttr("checked");
        $("#table01 tr.NS").length > 0 ? $("#chk-NS").attr("checked", "checked") : $("#chk-NS").removeAttr("checked");
        $("#table01 tr.PM").length > 0 ? $("#chk-PM").attr("checked", "checked") : $("#chk-PM").removeAttr("checked");
        $("#table01 tr.PB").length > 0 ? $("#chk-PB").attr("checked", "checked") : $("#chk-PB").removeAttr("checked");
        $("#table01 tr.ES").length > 0 ? $("#chk-ES").attr("checked", "checked") : $("#chk-ES").removeAttr("checked");
        
        initTdEdit();
    },

    //删除行
    deleteRow: function (rowClass) {
		handOp=true;
        ClassTableTime.cancelBubble();
        //event.stopPropagation();
        $(xy.string.format("#table01 tr.{0}", rowClass)).last().remove();
        ClassTableTime.setRowName();
        //删除的同时，设置选择框的选中状态
        var typeRowCount = $(xy.string.format("#table01 tr.{0}", rowClass)).length;
        if (typeRowCount == 0) {
            $(xy.string.format("#chk-{0}", rowClass)).removeAttr("checked");
        }

        //操作上午时的判断
        if (rowClass == "AM") {
            //取消上午课间和午休
            if (typeRowCount == 0) {
                $("#chk-AB,#chk-NS").removeAttr("checked");
                $("#table01 tr.AB,tr.NS").remove();
            }
            else if (typeRowCount == 1) {
                //如剩一节课，移除课间
                $("#chk-AB").removeAttr("checked");
                $("#table01 tr.AB").remove();
            }
            else {
                //存在上午课间
                if ($("#table01 tr.AB").length > 0) {
                    //并且课间后没有了上午的课间
                    if ($("#table01 tr.AB").next("tr.AM").length == 0) {
                        //把课间放到最后一节的前边
                        $(xy.string.format("#table01 tr.{0}", rowClass)).last().before($("#table01 tr.AB"));
                    }
                }
            }

        }

        //操作下午时的判断
        if (rowClass == "PM") {
            //取消上午课间和午休
            if (typeRowCount == 0) {
                $("#chk-PB,#chk-NS").removeAttr("checked");
                $("#table01 tr.PB,tr.NS").remove();
            }
            else if (typeRowCount == 1) {
                //取消课间
                $("#chk-PB").removeAttr("checked");
                $("#table01 tr.PB").remove();
            }
            else {
                //存在下午课间
                if ($("#table01 tr.PB").length > 0) {
                    //并且课间后没有了下午的课间
                    if ($("#table01 tr.PB").next("tr.PM").length == 0) {
                        //把课间放到最后一节的前边
                        $(xy.string.format("#table01 tr.{0}", rowClass)).last().before($("#table01 tr.PB"));
                    }
                }
            }
        }
    },

    //添加行
    addRow: function (rowClass, maxCount) {
		handOp=true;
        ClassTableTime.cancelBubble();
        //event.stopPropagation();
        var rowArray = $(xy.string.format("#table01 tr.{0}", rowClass));
        if (rowArray.length < maxCount) {
            rowArray.last().after(ClassTableTime.createTrTd(rowClass, 1));

            //判断上午
            if (rowClass == "AM" && $("#table01 tr.AB").length > 0 && $("#table01 tr.AM").length > 2) {
                //把课间放到第二节的后边
                $(xy.string.format("#table01 tr.{0}", rowClass)).eq(1).after($("#table01 tr.AB"));
            }
            //判断下午
            if (rowClass == "PM" && $("#table01 tr.PB").length > 0 && $("#table01 tr.PM").length > 2) {
                //把课间放到第二节的后边
                $(xy.string.format("#table01 tr.{0}", rowClass)).eq(1).after($("#table01 tr.PB"));
            }

            ClassTableTime.setRowName();
        }
        else {
        	layer.msg('课时达到上限，不能再添加');
        }
    },

    //向上移动行
    upRow: function (rowClass) {
        handOp=true;
        ClassTableTime.cancelBubble();
        var prevClass = rowClass == "AB" ? "AM" : "PM";
        var curEle = $(xy.string.format("#table01 tr.{0}", rowClass));
        var prevEle = curEle.prev();
        if (prevEle.prev().hasClass(prevClass)) {
            prevEle.before(curEle);
        }
        else {
        	layer.msg('大课间不能移动到第一节之前');
        }
    },

    //向下移动行
    downRow: function (rowClass) {
        handOp=true;
        ClassTableTime.cancelBubble();
        var prevClass = rowClass == "AB" ? "AM" : "PM";
        var curEle = $(xy.string.format("#table01 tr.{0}", rowClass));
        var nextEle = curEle.next();
        if (nextEle.next().hasClass(prevClass)) {
            nextEle.after(curEle);
        }
        else {
        	layer.msg('大课间不能移动到最后一节之后');
        }
    },

    //得到事件
    getEvent: function () {
        if (window.event) { return window.event; }
        func = this.getEvent.caller;
        while (func != null) {
            var arg0 = func.arguments[0];
            if (arg0) {
                if ((arg0.constructor == Event || arg0.constructor == MouseEvent
                   || arg0.constructor == KeyboardEvent)
                   || (typeof (arg0) == "object" && arg0.preventDefault
                   && arg0.stopPropagation)) {
                    return arg0;
                }
            }
            func = func.caller;
        }
        return null;
    },
    //阻止冒泡
    cancelBubble: function () {
        var e = this.getEvent();
        if (window.event) {
            e.cancelBubble = true;//阻止冒泡
        } else if (e.preventDefault) {
            e.stopPropagation();//阻止冒泡
        }
    }
}

function initChk(){
	//如果是首次调协，初始化表格
	//早上
	<#if msCount?default(0) gt 0>
    	$("#chk-MS").click();
    </#if>
    //上午
    <#if amCount?default(0) gt 0>
    $("#chk-AM").click();
	    <#if ab?default(-1) gt -1>
	    $("#chk-AB").click();
	    </#if>
    </#if>
    //下午
    <#if pmCount?default(0) gt 0>
    $("#chk-PM").click();
	    <#if pb?default(-1) gt -1>
	    $("#chk-PB").click();
	    </#if>
	</#if>
	//午休,需要在上午和下午都设置后，才可添加午休
    <#if amCount?default(0) gt 0 && pmCount?default(0) gt 0>
    $("#chk-NS").click();
    </#if>
	//晚上
	<#if nightCount?default(0) gt 0>
    $("#chk-ES").click();
	</#if>

    handOp=false;
    handOp=true;
}
			
$(document).ready(function () {
    initChk();
});
</script>