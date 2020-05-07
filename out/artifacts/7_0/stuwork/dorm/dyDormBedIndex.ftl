<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="box print">
    <div class="tab-pane chosenSubjectHeaderClass">
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">学年：</span>
            <div class="filter-content">
                <select name="acadyearStr" id="acadyearStr" class="form-control" onchange="doSearch()" style="width:120px">
                <#if acadyearList?? && (acadyearList?size>0)>
                    <#list acadyearList as acadyear>
                        <option value="${acadyear!}" <#if acadyear==acadyearStr?default("")>selected="selected"</#if>>${acadyear!}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">学期：</span>
            <div class="filter-content">
                <select name="semesterStr" id="semesterStr" class="form-control" onchange="doSearch()" style="width:120px">
                    <option value="1" <#if "1"==semesterStr?default("")>selected="selected"</#if>>第一学期</option>
                    <option value="2" <#if "2"==semesterStr?default("")>selected="selected"</#if>>第二学期</option>
                </select>
            </div>
        </div>
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">寝室楼：</span>
            <div class="filter-content">
                <select name="buildingId" id="buildingId" class="form-control" onchange="getRoomFloor()" style="width:120px">
                <option value="">全部</option>
                <#if buildingList?? && (buildingList?size>0)>
                    <#list buildingList as building>
                        <option value="${building.id!}" <#if building.id==buildingId?default("")>selected="selected"</#if>>${building.name!}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">寝室楼层：</span>
            <div class="filter-content">
                <select name="roomFloor" id="roomFloor" class="form-control" onchange="getRoomName()" style="width:120px">
                </select>
            </div>
        </div>
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">寝室号：</span>
            <div class="filter-content">
                <select name="roomName" id="roomName" class="form-control" onchange="doSearch()" style="width:120px">
                </select>
            </div>
        </div>
        <div class="filter-item" style="margin-right: 10px;">
	        <span class="filter-name">寝室类型：</span>
	        <div class="filter-content">
	            <select name="roomType" id="roomType" class="form-control" onchange="doSearch()" style="width:120px">
	                <option value="">请选择</option>
	                <option value="1" <#if "1"==roomType?default("")>selected="selected"</#if>>男寝室</option>
	                <option value="2" <#if "2"==roomType?default("")>selected="selected"</#if>>女寝室</option>
	            </select>
	        </div>
	    </div>
        <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">入住情况：</span>
            <div class="filter-content">
                <select name="roomState" id="roomState" class="form-control" onchange="doSearch()" style="width:120px">
                    <option value="1" <#if "1"==roomState?default("")>selected="selected"</#if>>全部</option>
                    <option value="2" <#if "2"==roomState?default("")>selected="selected"</#if>>未住满</option>
                    <option value="3" <#if "3"==roomState?default("")>selected="selected"</#if>>已住满</option>
                    <option value="4" <#if "4"==roomState?default("")>selected="selected"</#if>>未入住</option>
                </select>
            </div>
        </div>
        <div class="filter filter-f16">
            <div class="filter">
                <a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="clearAllBed()">清空所有寝室</a>
                <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doImport()">导入Excel</a>
                <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doExportBed()">导出</a>
                <a href="javascript:" class="btn btn-blue " style="margin-bottom:5px;" onclick="saveAllBed()">提交</a>
            </div>
        </div>
    </div>
    <div id ="tabDiv" class="tab-content">
    </div>
</div>
<input type="hidden" id="roomProperty" name="roomProperty" value="${roomProperty!}"/>
<div style="display: none;">
<#if roomProperty?default("1")=="1">
	<@popup.selectOneStudent id="ownerId" name="ownerName" clickId="ownerName"  handler='setClassName()' >
	    <input type="text"  id="ownerName"  value="${nameValue!}" />
	    <input type="hidden" id="ownerId" name="ownerId" value="${idValue!}"/>
	</@popup.selectOneStudent>
<#else>
	<@popup.selectOneTeacher id="ownerId" name="ownerName"  clickId="ownerName" handler='setClassName()'>
	    <input type="text"  id="ownerName"  value="${nameValue!}" />
	    <input type="hidden" id="ownerId" name="ownerId" value="${idValue!}"/>
	</@popup.selectOneTeacher>
</#if>
</div>
<script>
    $(function(){
        getRoomFloor();
    });
    function getRoomFloor() {
        var buildingId = $("#buildingId").val();
        var roomProperty=$("#roomProperty").val();
        var url ="${request.contextPath}/stuwork/dorm/room/getRoomFloor";
        var roomFloors = $("#roomFloor");
        $.ajax({
            url:url,
            data:{buildingId:buildingId,roomProperty:roomProperty},
            dataType: "json",
            success:function (data) {
                var infolist=data.infolist;
                var code = data.code;
                roomFloors.html("");
                roomFloors.chosen("destroy");
                if(infolist==null||infolist.length==0){
                    roomFloors.append("<option value='' >请选择</option>");
                }else{
                    roomFloors.append("<option value='' >请选择</option>");
                    if(code==0) {
                        for (var i = 0; i < infolist.length; i++) {
                            roomFloor = "<option value='" + infolist[i] + "' ";
                            roomFloor += " >" + infolist[i] + "</option>";
                            roomFloors.append(roomFloor);
                        }
                    }
                }
                getRoomName();
            }
        })
    }
    function getRoomName() {
        var buildingId = $("#buildingId").val();
        var roomFloor = $("#roomFloor").val();
        var roomProperty=$("#roomProperty").val();
        var url ="${request.contextPath}/stuwork/dorm/room/getRoomName";
        var roomNames = $("#roomName");
        $.ajax({
            url:url,
            data:{buildingId:buildingId,roomFloor:roomFloor,roomProperty:roomProperty},
            dataType: "json",
            success:function (data) {
                var infolist=data.infolist;
                var code = data.code;
                roomNames.html("");
                roomNames.chosen("destroy");
                if(infolist==null||infolist.length==0){
                    roomNames.append("<option value='' >--请选择--</option>");
                }else{
                    roomNames.append("<option value='' >--请选择--</option>");
                    if(code==0) {
                        for (var i = 0; i < infolist.length; i++) {
                            roomName = "<option value='" + infolist[i] + "' ";
                            roomName += " >" + infolist[i] + "</option>";
                            roomNames.append(roomName);
                        }
                    }
                }
                $("#roomName").chosen({
                    width:'120px',
                    no_results_text:"未找到",//无搜索结果时显示的文本
                    allow_single_deselect:true,//是否允许取消选择
                    disable_search:false, //是否有搜索框出现
                    search_contains:true,//模糊匹配，false是默认从第一个匹配
                    //max_selected_options:1 //当select为多选时，最多选择个数
                });
                doSearch();
            }
        });

    }
    var index="";
    function editStuId(number){
        index=number;
        var ownerId=$("#ownerId"+number).val();
        var ownerName=$("#ownerName"+number).val();
        $('#ownerName').val(ownerName);
        $('#ownerId').val(ownerId);
    	$('#ownerName').click();
    }
    function doImport(){
        var acadyear=$("#acadyearStr").val();
        var semester=$("#semesterStr").val();
        var buildingId=$("#buildingId").val();
        var roomProperty=$("#roomProperty").val();
        $("#itemShowDivId").load("${request.contextPath}/stuwork/bedImport/main?acadyear="+acadyear+"&semester="+semester+"&buildingId="+buildingId+"&roomProperty="+roomProperty);
    }

    function setClassName(){
        var ownerId=$("#ownerId").val();
        var ownerName=$("#ownerName").val();
        $("#ownerName"+index).val(ownerName);
        $("#ownerId"+index).val(ownerId);
        <#if roomProperty?default("1")=="1">
	        if(ownerId==undefined || ownerId==""){
	            $("#classId"+index).val("");
	            $("#className"+index).html("");
	            $("#ownerId"+index).val("");
	        }else{
	            $.ajax({
	                url:'${request.contextPath}/stuwork/dorm/bed/setClassName'	,
	                data: {'ownerId':ownerId},
	                type:'post',
	                success:function(data) {
	                    var jsonO = JSON.parse(data);
	                    $("#classId"+index).val(jsonO.classId);
	                    $("#className"+index).html(jsonO.className);
	                },
	                error : function(XMLHttpRequest, textStatus, errorThrown) {
	
	                }
	            });
	        }
        </#if>
    }
    function doSearch(currentPageIndex,currentPageSize){
        var buildingId=$("#buildingId").val();
        var roomType=$("#roomType").val();
        var roomProperty=$("#roomProperty").val();
        var acadyearStr=$("#acadyearStr").val();
        var semesterStr=$("#semesterStr").val();
        var floor = $("#roomFloor").val();
        var roomName = $("#roomName").val();
        var roomState = $("#roomState").val();
        var url="${request.contextPath}/stuwork/dorm/bed/list/page?buildingId="+buildingId+"&roomType="+roomType
                +"&acadyearStr="+acadyearStr+"&semesterStr="+semesterStr+"&floor="+floor+"&roomName="+roomName+"&roomState="+roomState+"&roomProperty="+roomProperty;
        if(typeof (currentPageIndex)!="undefined"&&typeof (currentPageSize)!="undefined") {
            url = url + "&_pageIndex=" + currentPageIndex + "&_pageSize=" + currentPageSize;
        }
        $("#tabDiv").load(url);
    }
    function addRoom(){
        var url = "${request.contextPath}/stuwork/dorm/bed/edit/page";
        indexDiv = layerDivUrl(url,{title: "寝室信息维护",width:400,height:400});
    }
    function clearAllBed(roomId){
        showConfirmMsg('确认清空？','提示',function(){
            var ii = layer.load();
            if(!roomId){
                roomId="";
            }
            var acadyearStr=$("#acadyearStr").val();
            var semesterStr=$("#semesterStr").val();
            var roomProperty=$("#roomProperty").val();
            var room=$("#semesterStr").val();
            $.ajax({
                url:'${request.contextPath}/stuwork/dorm/bed/clearAllBed',
                data:{'acadyearStr':acadyearStr,'semesterStr':semesterStr,'roomId':roomId,'roomProperty':roomProperty},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        layerTipMsg(jsonO.success,"成功",jsonO.msg);
                        var currentPageIndex=jsonO.currentPageIndex;
                        var currentPageSize=jsonO.currentPageSize;
                        doSearch(currentPageIndex,currentPageSize);
                    }else{
                        layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    }
                    layer.close(ii);
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {}
            });
        });
    }

    var isSubmit=false;
    function saveAllBed(){
        if(isSubmit){
            return false;
        }
        isSubmit = true;
        var check = checkValue('#bedForm');
        if(!check){
            isSubmit=false;
            return;
        }
        var roomProperty=$("#roomProperty").val();
        var options = {
            url:'${request.contextPath}/stuwork/dorm/bed/saveAllBed',
            data:{'roomProperty':roomProperty},
            dataType : 'json',
            clearForm : false,
            resetForm : false,
            type : 'post',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    isSubmit = false;
                }else{
                    //layer.closeAll();
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    isSubmit = false;
                    var currentPageIndex=jsonO.currentPageIndex;
                    var currentPageSize=jsonO.currentPageSize;
                    doSearch(currentPageIndex,currentPageSize);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $('#bedForm').ajaxSubmit(options);
    }
    function doExportBed(){
        var buildingId=$("#buildingId").val();
        var roomType=$("#roomType").val();
        var roomProperty=$("#roomProperty").val();
        var acadyearStr=$("#acadyearStr").val();
        var semesterStr=$("#semesterStr").val();
        var floor = $("#roomFloor").val();
        var roomName = $("#roomName").val();
        var roomState = $("#roomState").val();
        location.href = '${request.contextPath}/stuwork/dorm/bed/export?buildingId='+buildingId+"&roomType="+roomType+"&acadyear="+acadyearStr+"&semester="+semesterStr+"&floor="+floor+"&roomName="+roomName+"&roomState="+roomState+"&roomProperty="+roomProperty;
    }
</script>