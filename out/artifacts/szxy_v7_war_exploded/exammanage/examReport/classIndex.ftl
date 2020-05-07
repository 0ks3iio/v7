<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
	<div class="filter filter-f16">
		<#if (emClaList?exists && emClaList?size>0)>
		<div class="filter">
			<div class="filter-item">
				<a href="javascript:" class="btn btn-white  detaileExport" data-toggle="tooltip">导出</a>
                <a href="javascript:" class="btn btn-white  detaileAllExport" data-toggle="tooltip">批量导出</a>
				<@htmlcomponent.printToolBar container=".print" btn2="false" printDirection='true' printUp=7 printBottom=0/>
                <a href="javascript:" class="btn pull-right btn-white" style="margin-left:5px" onclick="onBatchPrint();">批量打印</a>
			</div>
		</div>
		</#if>
		<div class="picker-table">
			<table class="table">
				<tbody>
				<tr>
					<th width="150">班级：</th>
					<td>
						<div class="outter">
						<#if (classList?exists && classList?size>0)>
							<#list classList as cla>
								<#if (cla_index==0)>
									<input style="display: none" value="${cla.id!}" id="initClaId">
								</#if>
								<a class="classList" style="cursor:pointer" onclick="showTableList('${cla.id!}')" id="class_${cla.id!}">${cla.classNameDynamic!}</a>
							<#--<option  value = "${cla.id!}" >${cla.classNameDynamic!}</option>-->
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
				<tr>
				<#if (isGkExamType="1")>
					<th width="150">科目：</th>
					<td>
						<div class="outter">
						<#if (emSubGroupList?exists && emSubGroupList?size>0)>
							<#list emSubGroupList as group>
								<#if (group_index==0)>
									<input style="display: none" value="${group.id}" id="initGroupId">
								</#if>
								<#if (group.subType?exists&&group.subType=="1")>
									<a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-orange position-relative top-1">选</span></a>
								<#elseif (group.subType?exists&&group.subType=="2")>
									<a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-green position-relative top-1">学</span></a>
								<#else >
									<a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}</a>
								</#if>
							</#list>
							<input value="${groupId!}" id="groupId" style="display: none">
						<#else>
							<option  value = "" >未安排</option>
						</#if>
						</div>
					</td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                        </div>
                    </td>
				</#if>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div id="tableDiv">
	</div>
<iframe name="batchPrintId" id="batchPrintId" style="display:none;" width="100%" height="100%" frameborder="0" ></iframe>		
<script>
	var claId;
	var grpId;
$(function(){
    var groupId=$("#initGroupId").val();
    var classId = $("#initClaId").val();
    grpId=groupId;
    var url;
	if(groupId!=""&& typeof(groupId) != "undefined") {
		url = '${request.contextPath}/exammanage/examReoprt/classList/page?examId=${examInfo.id!}&emSubGroupId=' + groupId;
		if(classId!=""&& typeof(classId) != "undefined") {
			url = url+'&examClaId=' + classId
		}
		$("#tableDiv").load(url, function () {
			$(".groupList").each(function () {
				$(this).removeClass("selected")
			});
			$(eval("group_" + groupId)).addClass("selected");
			$(eval("class_"+classId)).addClass("selected");
		});
	}else {
        if(classId!=""&& typeof(classId) != "undefined") {
            url = '${request.contextPath}/exammanage/examReoprt/classList/page?examId=${examInfo.id!}&examClaId=' + classId;
        }
        $("#tableDiv").load(url, function () {
            $(".groupList").each(function () {
                $(this).removeClass("selected")
            });
            $(eval("class_"+classId)).addClass("selected");
        });
	};
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
    $('.detaileExport').on("click",function(){
		var classId = $("#initClaId").val();
		<#if (isGkExamType="1")>
		var groupId=$("#initGroupId").val();
		var url = "${request.contextPath}/exammanage/examReport/classIndex/export?examId=${examId!}&classId="+classId+"&groupId="+groupId;
		<#else>
		var url = "${request.contextPath}/exammanage/examReport/classIndex/export?examId=${examId!}&classId="+classId;
		</#if>
		batchPrintId.location.href=url;
	});
	$('.detaileAllExport').on("click",function(){
		var url = "${request.contextPath}/exammanage/examReport/classIndex/batchExport?examId=${examId!}";
		batchPrintId.location.href=url;
	});
})

function showTableList(classsId){
	claId = classsId;
    var url
	if(grpId!=""&&typeof (grpId)!="undefined") {
        url = '${request.contextPath}/exammanage/examReoprt/classList/page?examId=${examInfo.id!}&examClaId=' + classsId + '&emSubGroupId=' + grpId;
    }else {
        url = '${request.contextPath}/exammanage/examReoprt/classList/page?examId=${examInfo.id!}&examClaId=' + classsId ;
	}
	$("#tableDiv").load(url,function () {
        $(".classList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("class_"+classsId)).addClass("selected");
    });
    $("#initClaId").attr('value',classsId);
}

function onGroupClick(groupid) {
    grpId = groupid;
    if(claId!=""&&typeof (claId)!="undefined") {
        var url = '${request.contextPath}/exammanage/examReoprt/classList/page?examId=${examInfo.id!}&emSubGroupId=' + groupid + '&examClaId=' + claId;
    }
    $("#tableDiv").load(url,function () {
        $(".groupList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("group_"+groupid)).addClass("selected");
    });
    $("#initGroupId").attr('value',groupid);
};

function onBatchPrint(){
    document.getElementById('batchPrintId').src="${request.contextPath}/exammanage/examReoprt/classList/onBatchPrint?examId=${examInfo.id!}&&batchId=${batchId!}";
}

function doPrint(){
	var batchIdLeftVal = window.frames["batchPrintId"].document.getElementById("batchIdLeft").value;
	//alert(batchIdLeftVal);
	if (batchIdLeftVal == "") {
		if (window.frames["batchPrintId"].document.getElementById("doNotPrint").value == "0") {
			LODOP=getLodop();  
			LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
			LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
		  	LODOP.ADD_PRINT_HTM("7mm","0mm","RightMargin:0mm","BottomMargin:0mm",batchPrintId.window.getSubContent());
			//LODOP.PREVIEW();
			LODOP.PRINT();
			//closeTip();
	     	//isSubmit=false;
	     	showMsgSuccess("打印成功！");
		} 
	} else {
		LODOP=getLodop();  
		LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
	  	LODOP.ADD_PRINT_HTM("7mm","0mm","RightMargin:0mm","BottomMargin:0mm",batchPrintId.window.getSubContent());
		//LODOP.PREVIEW();
		LODOP.PRINT();
		document.getElementById('batchPrintId').src="${request.contextPath}/exammanage/examReoprt/classList/onBatchPrint?examId=${examInfo.id!}&&batchId="+batchIdLeftVal;
	}
}
</script>
