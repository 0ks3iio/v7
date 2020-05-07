<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print">
<div class="tab-pane chosenSubjectHeaderClass">
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
            <select name="roomName" id="roomName" class="form-control" onchange="doRoomSearch()" style="width:120px">
            </select>
        </div>
    </div>
    <div class="filter-item" style="margin-right: 10px;">
        <span class="filter-name">寝室类型：</span>
        <div class="filter-content">
            <select name="roomType" id="roomType" class="form-control" onchange="doRoomSearch()" style="width:120px">
                <option value="">请选择</option>
                <option value="1" <#if "1"==roomType?default("")>selected="selected"</#if>>男寝室</option>
                <option value="2" <#if "2"==roomType?default("")>selected="selected"</#if>>女寝室</option>
            </select>
        </div>
    </div>
    <div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">寝室属性：</span>
            <div class="filter-content">
                <select name="roomProperty" id="roomProperty" class="form-control" onchange="doRoomSearch()" style="width:120px">
                    <option value="">请选择</option>
                    <option value="1" <#if "1"==roomProperty?default("")>selected="selected"</#if>>学生寝室</option>
                    <option value="2" <#if "2"==roomProperty?default("")>selected="selected"</#if>>老师寝室</option>
                </select>
            </div>
        </div>
    <div class="filter-item filter-item-right">
        <div class="filter">
            <a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="addRoom()">新增寝室号</a>
            <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doImport()">导入Excel</a>
            <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doExportRoom()">导出</a>
        </div>
    </div>
    <div id ="tabDiv" class="tab-content">
    </div>
</div>
</div>
<script>
    $(function(){
        getRoomFloor();
    });
    function getRoomFloor() {
        var buildingId = $("#buildingId").val();
        var url ="${request.contextPath}/stuwork/dorm/room/getRoomFloor";
        var roomFloors = $("#roomFloor");
        $.ajax({
            url:url,
            data:{buildingId:buildingId},
            dataType: "json",
            success:function (data) {
                var infolist=data.infolist;
                var code = data.code;
                roomFloors.html("");
                roomFloors.chosen("destroy");
                if(infolist==null||infolist.length==0){
                    roomFloors.append("<option value='' >请选择</option>");
                }else{
                    var roomFloor='';
                    roomFloor+="<option value='' >请选择</option>";
                    roomFloors.append(roomFloor);
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
        var url ="${request.contextPath}/stuwork/dorm/room/getRoomName";
        var roomNames = $("#roomName");
        $.ajax({
            url:url,
            data:{buildingId:buildingId,roomFloor:roomFloor},
            dataType: "json",
            success:function (data) {
                var infolist=data.infolist;
                var code = data.code;
                roomNames.html("");
                roomNames.chosen("destroy");
                if(infolist==null||infolist.length==0){
                    roomNames.append("<option value='' >--请选择--</option>");
                }else{
                    var roomName='';
                    roomName+="<option value='' >--请选择--</option>";
                    roomNames.append(roomName);
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
                doRoomSearch();
            }
        });

    }
    function doRoomSearch(currentPageIndex,currentPageSize){
        var buildingId=$("#buildingId").val();
        var roomType=$("#roomType").val();
        var floor = $("#roomFloor").val();
        var roomName = $("#roomName").val();
        var roomProperty = $("#roomProperty").val();
        var url="${request.contextPath}/stuwork/dorm/room/list/page?buildingId="+buildingId+"&roomType="+roomType+"&floor="+floor
                +"&roomName="+roomName+"&roomProperty="+roomProperty;
        if(typeof (currentPageIndex)!="undefined"&&typeof (currentPageSize)!="undefined") {
            url = url + "&_pageIndex=" + currentPageIndex + "&_pageSize=" + currentPageSize;
        }
        $("#tabDiv").load(url);
    }
    function addRoom(){
        var url = "${request.contextPath}/stuwork/dorm/room/edit/page";
        indexDiv = layerDivUrl(url,{title: "寝室信息维护",width:500,height:500});
    }
    function editRoom(id){
        var url = "${request.contextPath}/stuwork/dorm/room/edit/page?id="+id;
        indexDiv = layerDivUrl(url,{title: "寝室信息维护",width:500,height:500});
    }
    function deleteRoom(id){
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            $.ajax({
                url:'${request.contextPath}/stuwork/dorm/room/delete',
                data: {'id':id},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        doRoomSearch();
                    }else{
                        layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    }
                    layer.close(ii);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }
    function doImport(){
        var acadyear=$("#acadyearStr").val();
        var semester=$("#semesterStr").val();
        var buildingId=$("#buildingId").val();
        var roomType = $("#roomType").val()
        $("#itemShowDivId").load("${request.contextPath}/stuwork/dyDormRoomImport/main?acadyear="+acadyear+"&semester="+semester+"&buildingId="+buildingId+"&roomType="+roomType);
    }
    function doExportRoom() {
        var buildingId=$("#buildingId").val();
        var roomType=$("#roomType").val();
        var floor = $("#roomFloor").val();
        var roomName = $("#roomName").val();
        var roomProperty = $("#roomProperty").val();
        location.href = '${request.contextPath}/stuwork/dorm/room/export?buildingId='+buildingId+"&roomType="+roomType+"&floor="+floor+"&roomName="+roomName+"&roomProperty="+roomProperty;
    }
</script>