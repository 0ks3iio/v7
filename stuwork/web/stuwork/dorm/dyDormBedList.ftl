<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="explain">
	<p>寝室房间数：<span id="allRoomNum"><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="20"></span>
	   &nbsp;&nbsp;入住人数：<span id="ownerNum"><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="20"></span>
	   &nbsp;&nbsp; 未分配寝室数：<span id="noRoomNum"><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="20"></span>
	</p>
</div>
<form name="bedForm" id="bedForm" action="" method="post">
    <table class="table table-striped table-hover table-layout-fixed no-margin">
        <thead>
        <tr>
            <th style="width:10%" align="center">寝室楼</th>
            <th style="width:10%" align="center">寝室类别</th>
            <th style="width:10%" align="center">寝室号</th>
            <th style="width:8%" align="center">楼层</th>
            <th style="width:8%" align="center">容纳人数</th>
            <th align="center" style="width:8%">床位号</th>
            <#if roomProperty?default("1")=="1">
           	 	<th align="center" style="width:12%">班级</th>
            </#if>
            <th align="center" style="width:12%">姓名</th>
            <th align="center" style="width:13%">备注</th>
        </tr>
        </thead>
        <tbody>
		<#-- 床位数 -->
		<#assign bedNumber = 0/>
		<#if roomList?exists && roomList?size gt 0>
			<#list roomList as room>
            <tr>
			<#--<td rowspan="${room.bedList?size}">${room_index+1!}</td>-->
                <td rowspan="${room.bedList?size}">${room.buildName!}</td>
                <td rowspan="${room.bedList?size}"><#if room.roomType?default("")=="1">男寝室<#elseif room.roomType?default("")=="2">女寝室</#if></td>
                <td rowspan="${room.bedList?size}">${room.roomName!}
                    <a href="javascript:void(0);" class="ml-5" onclick="clearAllBed('${room.id!}');">[清空]</a></td>
                </td>
                <td rowspan="${room.bedList?size}">${room.floor!}</td>
                <td rowspan="${room.bedList?size}">${room.capacity!}</td>
				<#if room.bedList?exists && room.bedList?size gt 0>
					<#list room.bedList as sdb>
						<#if sdb_index == 0>
						<#else>
                        <tr>
						</#if>
                        <td class="t-center">
                            <input type="hidden"  name="bedList[${bedNumber}].no" value="${sdb_index+1}"/>${sdb.no!}
                            <input type="hidden" name="bedList[${bedNumber}].roomId" value="${sdb.roomId!}"/>
                            <input type="hidden" name="bedList[${bedNumber}].unitId" value="${sdb.unitId!}"/>
                            <input type="hidden" name="bedList[${bedNumber}].acadyear" value="${sdb.acadyear!}"/>
                            <input type="hidden" name="bedList[${bedNumber}].semester" value="${sdb.semester!}"/>
                            <input type="hidden" name="bedList[${bedNumber}].id" value="${sdb.id!}"/>
                        </td>
                        <#if roomProperty?default("1")=="1">
                        <td>
                            <span id="className${bedNumber}">${sdb.className!}</span>
                            <input type="hidden" id="classId${bedNumber}" name="bedList[${bedNumber}].classId" value="${sdb.classId!}"/>
                        </td>
                        </#if>
                        <td>
                            <input type="text" readonly="true" id="ownerName${bedNumber}" value="${sdb.ownerName!}" onclick="editStuId('${bedNumber}')" />
                            <input type="hidden" id="ownerId${bedNumber}" name="bedList[${bedNumber}].ownerId" value="${sdb.ownerId!}"/>
                        </td>
                        <td><input name="bedList[${bedNumber}].remark" id="remark${bedNumber}" class="input-txt" style="width: 140px;" type="text" maxLength="100" value="${sdb.remark!}"/></td>
						<#assign bedNumber = bedNumber+1/>
					</#list>
				</#if>
			</#list>
		<#else>
        <tr >
            <td colspan="10" align="center">
                暂无数据
            </td>
        </tr>
		</#if>
        </tbody>
    </table>
</form>
<@htmlcom.pageToolBar container="#tabDiv" class="noprint">
</@htmlcom.pageToolBar>
<script>
	$(function(){
        getResult();
    });
    function getResult(){
    	var acadyear=$("#acadyearStr").val();
        var semester=$("#semesterStr").val();
		var buildingId=$("#buildingId").val();
        var roomType=$("#roomType").val();
        var roomProperty=$("#roomProperty").val();
        var roomFloor = $("#roomFloor").val();
        var roomName = $("#roomName").val();
        var roomState = $("#roomState").val();
		$.ajax({
            url:"${request.contextPath}/stuwork/dorm/bed/getResult",
            data:{acadyear:acadyear,semester:semester,buildingId:buildingId,roomFloor:roomFloor,roomType:roomType,roomName:roomName,roomState:roomState,roomProperty:roomProperty},
            dataType: "json",
            success:function (data) {
            	var allRoomNum=0;
				var ownerNum=0;
				var noRoomNum=0;
            	if(data.code==0){
            		allRoomNum=data.allRoomNum;
            		ownerNum=data.ownerNum;
            		noRoomNum=data.noRoomNum;
            	}
            	$("#allRoomNum").html(allRoomNum);
            	$("#ownerNum").html(ownerNum);
            	$("#noRoomNum").html(noRoomNum);
            }
        });
    }
</script>