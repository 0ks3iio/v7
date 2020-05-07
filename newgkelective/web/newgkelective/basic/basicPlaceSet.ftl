<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<script src="${request.contextPath}/static/sortable/Sortable.min.js"></script>
<div id="inner">
    <div class="explain-default clearfix">
        <p class="pull-left mt3" style="margin-top: 6.5px"><b>&nbsp;&nbsp;&nbsp;待安排教室：</b></p>
        <form id="savePlaceForm"><input type="hidden" id="placeIdsTemp" name="placeIdsTemp" value="${placeIdsTemp!}"/>
        <input type="hidden" name="placeNamesTemp" id="placeNamesTemp" class="form-control" value="${placeNamesTemp!}"></form>
        <span>
            <button onclick="addPlace();" class="btn btn-blue pull-right">添加</button>
            <button class="btn btn-white pull-right mr10 adjustIndex">调整排序</button>
        </span>
        <span style="display: none">
            <button class="btn btn-white pull-right sortCancel">取消</button>
            <button class="btn btn-blue pull-right mr10 sortSave">保存</button>
            <span class="color-grey pull-right mr10 mt3"><i
                    class="fa fa-exclamation-circle color-yellow"></i> 请拖动调整顺序</span>
        </span>
    </div>
    <div class="classroomArrangement classroomArrangementSimple classList clearfix" id="sortable1">
        <#--<a class="item" href="#">
            <span class="del">&times;</span>
            <span class="bj">教室1</span>
            <span class="bjId" style="display: none"></span>
            <span class="stu clearfix"><span class="float-left">来源:高三楼</span>
            </span>
        </a>-->
    <#if arrangePlaceList?? && arrangePlaceList?size gt 0>
    <#list arrangePlaceList as place>
	    <a class="item" href="#" title="${place.placeName?default('未知')+'\n来源'+place.teachBuildingName?default('未知')}">
            <span id="place${place_index}" class="del" onclick="deletePlace('${place_index}')">&times;</span>
            <span class="bj">${place.placeName?default('未知')}</span>
            <span class="bjId" style="display: none">${place.id!}</span>
            <span class="stu clearfix" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
            	<span class="float-left">来源:${place.teachBuildingName?default('未知')}</span>
           </span>
        </a>
    </#list>
    <#else>
    	<div class="no-data">
            <span class="no-data-img">
            <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
            </span>
            <div class="no-data-body">
            <p class="no-data-txt">尚未添加任何场地</p>
            </div>
            </div>
    
    </#if>
    </div>
    <hr>
</div>
<#-- 添加场地 -->
<div class="layer layer-copyCourseParm">
	<div class="layer-content">
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各个场地</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher" class="form-control input-sm js-search" placeholder="输入场地名称查询" value="">
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
					<th width="127">楼层</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px;height:437px;">
				<#if buildList?? && buildList?size gt 0>
				<#list buildList as build>
                	<li id="class_${build.id!}" class="courseLi <#if build_index==0>active</#if>">
	                	<a href="#aaa_${build.id!}" data-value="${build.id!}">${build.buildingName!}
	                		<span class="badge badge-default"></span>
	                	</a>
                	</li>
				</#list>
				</#if>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId"style="position:relative;overflow:auto;height:437px;border-left: 1px solid #eee;">
					<#-- 这里放 各个班级的课程 -->
				<#if buildList?? && buildList?size gt 0>
				<#list buildList as build>
                	<div id="aaa_${build.id!}"  data-value="${build.id!}">
						<div class="form-title ml-15 mt10 mb10">${build.buildingName!}
							<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> 
							<a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> 
						</div>
						<ul class="gk-copy-list">
						<#assign places = placeByBuildingMap[build.id!]!>
						<#if places?? && places?size gt 0>
						<#list places as p>
							<label class="mr20">
								<input type="checkbox" class="wp" name="copyTeacher" value="${p.id!}" data-value="${p.placeName!}">
								<span class="lbl">${p.placeName!}</span>
							</label>
						</#list>
						</#if>
						</ul>
					</div>
				</#list>
				</#if>
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
<script>
var placeIds = [];
<#if placeIds?? && placeIds?size gt 0>
<#list placeIds as pid>
	placeIds.push("${pid!}");
</#list>
</#if>
var placeNameObj = {};
<#if teachPlaceList?? && teachPlaceList?size gt 0>
<#list teachPlaceList as place>
	placeNameObj["${place.id!}"]="${place.placeName!}";
</#list>
</#if>

function initAddPlace(){
	// 清除历史信息
	$(".layer-copyCourseParm :checkbox").prop("checked",false);
	$(".layer-copyCourseParm :checkbox").attr("disabled",false);
	$(".gk-copy-nav").find("span").each(function(){
		$(this).text("");
	});
	$("#scrollspyDivId .js-clearChoose").hide();
	$("#scrollspyDivId .js-allChoose").show();
	$('#findTeacher').val("");
	findTeacher();
	var $placeAear = $("#scrollspyDivId")
	for(x in placeIds){
		var pid = placeIds[x];
		$placeAear.find("input[value='"+pid+"']").click();
	}
}

// 添加场地
function addPlace(){
	//$(".currentSubjectName").html(coursTimeinfo[subjectCode]);
	initAddPlace();
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '添加场地',
		area: ['850px','650px'],
		btn: ['确定', '取消'],
		btn1:function(index){
			dealPlace();
			//layer.close(index);
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-copyCourseParm')
	});
}
function findTeacher(){
	//debugger;
	var teacherName=$('#findTeacher').val().trim();
	if(teacherName!=""){
		$(".gk-copy-main .lbl").removeClass("color-blue");
		var first;
		$(".gk-copy-main input").each(function(){
			var tnt = $(this).attr("data-value");
			if (tnt && tnt.includes(teacherName)) {
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
			document.getElementById("class_"+buid).scrollIntoView();
			setTimeout(function(){
				$("#class_"+buid).siblings().removeClass("active");
				$("#class_"+buid).addClass("active");
			},50);
		}
	}else{
		$(".gk-copy-main .lbl").removeClass("color-blue");
	}
}
//回车
$('#findTeacher').bind('keypress',function(event){//监听回车事件
    if(event.keyCode == "13" || event.which == "13")    
    {  
        findTeacher();
    }
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

    var sortObj;
    var htmlcontents;
    $(function () {
        //changeState();

        $(".adjustIndex").on("click", function () {
            htmlcontents=$("#sortable1").html();
            $(".del").remove();
            $(".adjustIndex").parent().attr("style","display:none");
            $(".adjustIndex").parent().next().removeAttr("style");
            var a = document.getElementById('sortable1');
            sortObj=Sortable.create(a, {
                animation: 150
            });
        });

        $(".sortSave").on("click", function () {
            var resultNames=new String();
            var resultIds=new String();
            $(".bj").each(function () {
               resultNames += $(this).html();
               resultNames += ",";
            });
            resultNames=resultNames.substring(0,resultNames.length-1);
            $(".bjId").each(function () {
                resultIds += $(this).html();
                resultIds += ",";
            });
            resultIds=resultIds.substring(0,resultIds.length-1);
            $("#placeIdsTemp").val(resultIds);
            $("#placeNamesTemp").val(resultNames);
            $(".sortSave").parent().attr("style","display:none");
            $(".sortSave").parent().prev().removeAttr("style");
            sortObj.destroy();
            placeIds = resultIds.split(",");
            console.log(resultIds);
            console.log(placeIds);
            changeState();
            savePlaceSet();
        });

        $(".sortCancel").on("click", function () {
            $(".sortCancel").parent().attr("style","display:none");
            $(".sortCancel").parent().prev().removeAttr("style");
            sortObj.destroy();
            $("#sortable1").html(htmlcontents);
        });
    });

    function dealPlace(){
    	//debugger;
        var resultIds=$("#placeIdsTemp").val().split(",");
        var nowPidObj = {};
    	$("#scrollspyDivId input:checked").each(function(index,obj){
    		var placeId = $(obj).val();
    		if(placeIds.indexOf(placeId) == -1){
    			placeIds.push(placeId);
    		}
    		nowPidObj[placeId]=placeId;
    	});
    	var newPids = [];
    	for(x in placeIds){
    		var pid = placeIds[x];
    		if(nowPidObj[pid]){
    			newPids.push(pid);
    		}
    	}
    	placeIds = newPids;
    	$("#placeIdsTemp").val(placeIds.join());
    	
        
        var innerStr="";
        for (var index = 0; index < placeIds.length; index++){
        	var pid = placeIds[index];
            innerStr=innerStr+"<a class=\"item\" href=\"#\" title=\"" + placeNameObj[pid] + "\">\n" +
                    "            <span id=\"place" + index + "\" class=\"del\" onclick=deletePlace(" + index + ")>&times;</span>\n" +
                    "            <span class=\"bj\">" + placeNameObj[pid] + "</span>\n" +
                    "            <span class=\"bjId\" style=\"display: none\">" + pid + "</span>" +
                    "            <span class=\"stu clearfix\" style=\"overflow: hidden;white-space: nowrap;text-overflow: ellipsis;\"><span class=\"float-left\">来源:未知</span>\n" +
                    "            </span>\n" +
                    "        </a>";
        }
        if ($("#placeIdsTemp").val().length<=1){
            var innerStr="<div class=\"no-data\">\n" +
                    "<span class=\"no-data-img\">\n" +
                    "<img src=\"${request.contextPath}/static/images/public/nodata6.png\" alt=\"\">\n" +
                    "</span>\n" +
                    "<div class=\"no-data-body\">\n" +
                    "<p class=\"no-data-txt\">尚未添加任何场地</p>\n" +
                    "</div>\n" +
                    "</div>";
        }
        $("#sortable1").html(innerStr);
        showBuildName();
        savePlaceSet();
    }

    function changeState() {
        if ($("#placeIdsTemp").val().length<=1){
            var innerStr="<div class=\"no-data\">\n" +
                    "<span class=\"no-data-img\">\n" +
                    "<img src=\"${request.contextPath}/static/images/public/nodata6.png\" alt=\"\">\n" +
                    "</span>\n" +
                    "<div class=\"no-data-body\">\n" +
                    "<p class=\"no-data-txt\">尚未添加任何场地</p>\n" +
                    "</div>\n" +
                    "</div>";
            $("#sortable1").html(innerStr);
            return;
        }
        var innerStr="";
         for (var index = 0; index < placeIds.length; index++){
        	var pid = placeIds[index];
            innerStr=innerStr+"<a class=\"item\" href=\"#\" title=\"" + placeNameObj[pid] + "\">\n" +
                    "            <span id=\"place" + index + "\" class=\"del\" onclick=deletePlace(" + index + ")>&times;</span>\n" +
                    "            <span class=\"bj\">" + placeNameObj[pid] + "</span>\n" +
                    "            <span class=\"bjId\" style=\"display: none\">" + pid + "</span>" +
                    "            <span class=\"stu clearfix\" style=\"overflow: hidden;white-space: nowrap;text-overflow: ellipsis;\"><span class=\"float-left\">来源:未知</span>\n" +
                    "            </span>\n" +
                    "        </a>";
        }
        $("#sortable1").html(innerStr);
        showBuildName();
    }

    function savePlaceSet(){
    	placeIds = $("#placeIdsTemp").val().split(",");
        var options = {
            url : "${request.getContextPath()}/newgkelective/${gradeId}/goBasic/placeSetSave",
            type : "post",
            dataType : "json",
            success : function(data){
                if (data.success){
                    layer.closeAll();
                    layer.msg("操作成功", {time: 2000});
                }else{
                    layerTipMsg(data.success,"失败","原因："+data.msg);
                }
            }
        };
        $("#savePlaceForm").ajaxSubmit(options);
    }

    function deletePlace(index) {
        var deleteId = "#place" + index;
        var index = layer.confirm("确认删除？", {
            btn: ["确定", "取消"]
        }, function() {
            $(deleteId).parent().remove();
            var resultNames=new String();
            var resultIds=new String();
            $(".bj").each(function () {
                resultNames += $(this).html();
                resultNames += ",";
            });
            resultNames=resultNames.substring(0,resultNames.length-1);
            $(".bjId").each(function () {
                resultIds += $(this).html();
                resultIds += ",";
            });
            resultIds=resultIds.substring(0,resultIds.length-1);
            $("#placeIdsTemp").val(resultIds);
            $("#placeNamesTemp").val(resultNames);
            if ($("#placeIdsTemp").val().length<=1){
                var innerStr="<div class=\"no-data\">\n" +
                        "<span class=\"no-data-img\">\n" +
                        "<img src=\"${request.contextPath}/static/images/public/nodata6.png\" alt=\"\">\n" +
                        "</span>\n" +
                        "<div class=\"no-data-body\">\n" +
                        "<p class=\"no-data-txt\">尚未添加任何场地</p>\n" +
                        "</div>\n" +
                        "</div>";
                $("#sortable1").html(innerStr);
                layer.closeAll();
            }
            savePlaceSet();
        });
    }
    
    function showBuildName() {
        if($("#placeIdsTemp").val() <= 1){
            return;
        }
        $.ajax({
            url:"${request.getContextPath()}/newgkelective/${gradeId}/goBasic/showBuildName",
            data:{
                placeIds:$("#placeIdsTemp").val()
            },
            type:"post",
            dataType:'json',
            success:function(data){
                var result = data.msg.split(",");
                if (data.success) {
                    for (var index = 0; index < result.length; index++) {
                        $("#place" + index).next().next().next().html("来源：" + result[index]);
                        var obj = $("#place" + index).parent();
                        obj.attr("title", obj.attr("title") + "\n" + "来源：" + result[index]);
                    }
                }
            }
        });
    }

    function height() {
        var maxheight = 0;
        $(".item").each(function () {
            var heightStr = $(this).css("height");
            var height = Number(heightStr.substring(0, heightStr.length - 2));
            if (maxheight < height) {
                maxheight = height;
            }
        });
        $(".item").each(function () {
            $(this).css("height", maxheight + "px");
        });
    }

$(function(){
	$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' });
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
});
</script>