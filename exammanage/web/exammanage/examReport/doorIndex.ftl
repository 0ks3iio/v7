<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if isgk=="1">
    <#if (groupList?exists && groupList?size>0) && (emPlaceList?exists && emPlaceList?size>0)>
        <div class="filter filter-f16">
            <div class="filter">
                <div class="filter-item">
                   	<a href="javascript:" class="btn btn-white  detaileExport" data-toggle="tooltip">导出</a>
                   	<a href="javascript:" class="btn btn-white  detaileAllExport" data-toggle="tooltip">批量导出</a>
                    <@htmlcomponent.printToolBar container=".print" btn2="false" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
                    <a href="javascript:" class="btn pull-right btn-white" style="margin-left:5px" onclick="onBatchPrint();">批量打印</a>
                </div>
            </div>
            <div class="picker-table">
                <table class="table">
                    <tbody>
                    <tr>
                        <th width="150">科目组：</th>
                        <td>
                            <div class="outter">
                                <#list groupList as group>
                                    <#if (group_index==0)>
                                        <input style="display: none" value="${group.id}" id="initGroupId">
                                    </#if>
                                    <#if (group.subType?exists&&group.subType=="1")>
                                        <a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-orange position-relative top-1">选</span></a>
                                    <#elseif (group.subType?exists&&group.subType=="2")>
                                        <a class="groupList"  style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-green position-relative top-1">学</span></a>
                                    <#else>
                                        <a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}</a>
                                    </#if>
                                    <input value="${groupId!}" id="groupId" style="display: none">
                                </#list>
                            </div>
                        </td>
                        <td width="75" style="vertical-align: top;">
                            <div class="outter">
                                <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>考场编号：</th>
                        <td>
                            <div class="outter">
                                <#if emPlaceList?exists>
                                    <#list emPlaceList as item>
                                        <#if (item_index==0)>
                                            <input style="display: none" value="${item.id}" id="initPlaceId">
                                        </#if>
                                        <a class="emPlaceList" style="cursor:pointer" onclick="onEmplaceClick('${item.id}')" id="place_${item.id}">${item.examPlaceCode!}</a>
                                    </#list>
                                </#if>
                            </div>
                        </td>
                        <td width="75" style="vertical-align: top;">
                            <div class="outter">
                                <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="table-container print" id="doorDiv">
        </div>
    <#else>
    <div class="filter filter-f16">
        <div class="no-data-container">
            <div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
			</span>
                <#if !(groupList?exists && groupList?size>0)>
                    <h3 class="no-data-body">
                        还未设置过考试科目
                    </h3>
                <#else >
                    <h3 class="no-data-body">
                        还未设置过考场考生编排
                    </h3>
                </#if>
            </div>
        </div>
    </div>
    </#if>
<#else>
    <#if (emPlaceList?exists && emPlaceList?size>0)>
    <div class="filter filter-f16">
        <div class="filter-item">
        	<a href="javascript:" class="btn btn-white  detaileExport" data-toggle="tooltip">导出</a>
        	<a href="javascript:" class="btn btn-white  detaileAllExport" data-toggle="tooltip">批量导出</a>
            <@htmlcomponent.printToolBar container=".print" btn2="false" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
            <a href="javascript:" class="btn pull-right btn-white" style="margin-left:5px" onclick="onBatchPrint();">批量打印</a>
        </div>
    </div>
    <div class="filter filter-f16">
        <div class="picker-table">
            <table class="table">
                <tbody>
                <tr>
                    <th width="150">考场编号：</th>
                    <td>
                        <div class="outter">
                            <#if emPlaceList?exists>
                                <#list emPlaceList as item>
                                    <#if (item_index==0)>
                                        <input style="display: none" value="${item.id}" id="initPlaceId">
                                    </#if>
                                    <a class="emPlaceList" style="cursor:pointer" onclick="onEmplaceClick('${item.id}')" id="place_${item.id}">${item.examPlaceCode!}</a>
                                </#list>
                            </#if>
                        </div>
                    </td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <#else >
    <div class="filter filter-f16">
        <div class="no-data-container">
            <div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
			</span>
                <h3 class="no-data-body">
                    还未设置过考场考生编排
                </h3>
            </div>
        </div>
    </div>
    </#if>
</#if>
</div>
<div class="table-container">
    <div class="table-container-body" id="doorDiv">
    </div>
</div>
<iframe name="batchPrintId" id="batchPrintId" style="display:none;" width="100%" height="100%" frameborder="0" ></iframe>		
<script>
$(function(){
	$('.detaileExport').on("click",function(){
		var examPlaceId = $("#initPlaceId").val();
		<#if isgk=="1">
		var groupId=$("#groupId").val();
		var url = "${request.contextPath}/exammanage/examReport/doorIndex/export?examId=${examId!}&placeId="+examPlaceId+"&groupId="+groupId;
		<#else>
		var url = "${request.contextPath}/exammanage/examReport/doorIndex/export?examId=${examId!}&placeId="+examPlaceId;
		</#if>
		batchPrintId.location.href=url;
	});
	$('.detaileAllExport').on("click",function(){
		var url = "${request.contextPath}/exammanage/examReport/doorIndex/batchExport?examId=${examId!}";
		batchPrintId.location.href=url;
	});
    var examPlaceId=$("#examPlaceId").val();
    if(examPlaceId==""){
        $("#doorDiv").remove();
    }else{
    <#if isgk=="1">
        <#if (groupList?exists && groupList?size>0)&&(emPlaceList?exists && emPlaceList?size>0)>
            var placeId = $("#initPlaceId").val();
            var groupId=$("#groupId").val();
            if(groupId==""||typeof (groupId)=="undefined") {
                groupId = $("#initGroupId").val();
            }
            var url =  '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+placeId+'&groupId='+groupId;
            $("#doorDiv").load(url,function () {
                $(".emPlaceList").each(function () {
                    $(this).removeClass("selected")
                });
                $(eval("place_"+placeId)).addClass("selected");
                $(eval("group_"+groupId)).addClass("selected");
            });
        </#if>
    <#else>
        <#if (emPlaceList?exists && emPlaceList?size>0)>
            var placeId = $("#initPlaceId").val();
            var url = '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+ placeId;
            $("#doorDiv").load(url, function () {
                $(".emPlaceList").each(function () {
                    $(this).removeClass("selected")
                });
                $(eval("place_" + placeId)).addClass("selected");
            });
        </#if>
    </#if>
    }
    $('.picker-more').click(function(){
        if($(this).children('span').text()=='展开'){
            $(this).children('span').text('折叠');
            $(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
        }else{
            $(this).children('span').text('展开');
            $(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
        };
        $(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
    });
	//showDoorList();
})
function showDoorList(){
	var examPlaceId=$("#examPlaceId").val();
	if(examPlaceId==""){
		$("#doorDiv").remove();
	}else{
		<#if isgk=="1">
			<#if (groupList?exists && groupList?size>0)&&(emPlaceList?exists && emPlaceList?size>0)>
				var groupId=$("#groupId").val();
				var url =  '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+examPlaceId+'&groupId='+groupId;
				$("#doorDiv").load(url);
			</#if>
		<#else>
			var url =  '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+examPlaceId;
			$("#doorDiv").load(url);
		</#if>
	}
}

function onGroupClick(groupid) {
    var url =  '${request.contextPath}/exammanage/examReport/doorIndex/page?examId=${examInfo.id!}&groupId='+groupid;
    $("#showTabDiv").load(url,function () {
        $(".groupList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("group_"+groupid)).addClass("selected");
    });
};
function onEmplaceClick(id) {
    <#if isgk=="1">
        <#if (groupList?exists && groupList?size>0)&&(emPlaceList?exists && emPlaceList?size>0)>
            var groupId=$("#groupId").val();
            if(groupId==""||typeof (groupId)=="undefined") {
                groupId = $("#initGroupId").val();
            }
            var url =  '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+id+'&groupId='+groupId;
            $("#doorDiv").load(url,function () {
                $(".emPlaceList").each(function () {
                    $(this).removeClass("selected")
                });
                $(eval("place_"+id)).addClass("selected");
            });
            $("#initPlaceId").attr('value',id);
        </#if>
    <#else>
        var url =  '${request.contextPath}/exammanage/examReoprt/doorList/page?examPlaceId='+id;
        $("#doorDiv").load(url,function () {
            $(".emPlaceList").each(function () {
                $(this).removeClass("selected")
            });
            $(eval("place_"+id)).addClass("selected");
        });
        $("#initPlaceId").attr('value',id);
    </#if>
};

function changGroupId(){
	var groupId=$("#groupId").val();
	if(groupId==""){
		$("#doorDiv").remove();
	}else{
		var url =  '${request.contextPath}/exammanage/examReport/doorIndex/page?examId=${examInfo.id!}&groupId='+groupId;
		$("#showTabDiv").load(url);
	}
}

function onBatchPrint(){
    document.getElementById('batchPrintId').src="${request.contextPath}/exammanage/examReoprt/doorList/onBatchPrint?examId=${examInfo.id!}&&batchId=${batchId!}";
}

function doPrint(){
	$("#printRemoveDiv").removeClass('box-default');
	var batchIdLeftVal = window.frames["batchPrintId"].document.getElementById("batchIdLeft").value;
	//alert(batchIdLeftVal);
	if (batchIdLeftVal == "") {
		if (window.frames["batchPrintId"].document.getElementById("doNotPrint").value == "0") {
			LODOP=getLodop();  
			LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
			LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
		  	LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm",batchPrintId.window.getSubContent());
			//LODOP.PREVIEW();
			LODOP.PRINT();
			//closeTip();
	     	showMsgSuccess("打印成功！");
		} 
	} else {
		LODOP=getLodop();  
		LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
	  	LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm",batchPrintId.window.getSubContent());
		//LODOP.PREVIEW();
		LODOP.PRINT();
		document.getElementById('batchPrintId').src="${request.contextPath}/exammanage/examReoprt/doorList/onBatchPrint?examId=${examInfo.id!}&&batchId="+batchIdLeftVal;
	}
}

</script>
