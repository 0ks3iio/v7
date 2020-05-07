<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print" id="mydiv">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter filter-f16">
                    <div class="filter-item">
                        <div class="filter-content">
                            <span class="filter-name">活动开始时间：</span>
                            <div class="pos-rel pull-left">
                                <input class='form-control date-picker' style="width:111px;" vtype='data' id="startTime" autocomplete="off" data-provide="typeahead" placeholder="请输入活动开始时间" onchange="showList2()" <#--onkeydown="dispResInvigilate()"-->>
                            </div>
                        </div>
                    </div>
                    <div class="filter-item">
                        <div class="filter-content">
                            <span class="filter-name">活动结束时间：</span>
                            <div class="pos-rel pull-left">
                                <input class='form-control date-picker' style="width:111px;" vtype='data' id="endTime" autocomplete="off" data-provide="typeahead" placeholder="请输入活动结束时间" onchange="showList2()">
                            </div>
                        </div>
                    </div>
                    <div class="filter-item ">
                        <div class="text-right">
                            <a class="btn btn-white" onclick="clearTime()">清空时间</a>
                        </div>
                    </div>
                    <div class="filter-item">
                        <div class="filter-content">
                            <div class="input-group input-group-search">
                                <span class="filter-name">活动类型：</span>
                                <div class="pos-rel pull-left">
                                    <select class="form-control" id="type" onchange="showList2()">
                                        <option value="1">访亲轮次活动</option>
                                        <option value="2">部门每月活动</option>
                                    </select>
                                <#--<input type="text" name="village" id="village" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入结亲村">-->
                                </div>
                                <div class="input-group-btn">
                                    <button type="button" class="btn btn-default" onclick="showList2()">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="filter-item " id="addDiv" <#if !hasLeadPermission>style="display: none" </#if> <#if !hasLeadPermission&&!hasLeadPermission>style="display: none" </#if>>
                        <div class="text-right">
                            <a class="btn btn-blue" onclick="add()">新增</a>
                        </div>
                    </div>
                    <div class="filter-item ">
                        <div class="text-right">
                            <a class="btn btn-blue" onclick="exportList()">导出</a>
                        </div>
                    </div>
                </div>
                <#--<div class="filter filter-f16">-->

                <#--</div>-->

            </div>
            
            <div class="table-container" id="showList2">
            <#--<div class="table-container-body" id="showList1">-->
            <#--</div>-->
            </div>
        </div>
    </div>
</div>
<div class="layer layer-new" id="layerContent">
</div>
<script>
    function clearTime(){
        $("#startTime").val("");
        $("#endTime").val("");
        showList2();
    }
    $(function(){
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#mydiv",".date-picker",viewContent);
        showList2();
    });
    function add(){
        // var year = $("#searchYear").val();
        var type = $("#type").val();
        <#--var url = "${request.contextPath}/familydear/famdearMonth/famdearMonthAdd?&type="+type;-->
        <#--$("#layerContent").load(url,-->
                <#--function() {-->
                    <#--var index = layer.open({-->
                        <#--type: 1,-->
                        <#--skin: 'layui-layer-demo',-->
                        <#--closeBtn: 1,-->
                        <#--shift: 2,-->
                        <#--shadeClose: false,-->
                        <#--maxmin: false,-->
                        <#--scrollbar: false,-->
                        <#--title: "每月活动编辑",-->
                        <#--area: ['750px','500px'],-->
                        <#--content: $("#layerContent"),-->
                        <#--cancel:function () {-->
                            <#--goBack();-->
                        <#--}-->
                    <#--});-->
                <#--}-->
        <#--);-->
        var url = "${request.contextPath}/familydear/famdearMonth/famdearMonthAdd?&type="+type;
        indexDiv = layerDivUrl(url,{title: "新增月活动填报",width:750,height:500});
    }
    function goBack(){
        var picIds = $("#picIds").val();
        if(picIds){
            url="${request.contextPath}/familydear/famdearActualReport/famdearAttachBack?picIds="+picIds
        }else {
            url="${request.contextPath}/familydear/famdearActualReport/famdearAttachBack";
        }
        //返回的时候删除为未添加标题的图片
        var options = {
            url : url,
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    return;
                }else{
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $.ajax(options);
        // searchList();
    }
    function showList2(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var type = $("#type").val();
        if(type==2){
            <#if hasPermission>
                $("#addDiv").show();
            <#else >
                $("#addDiv").hide();
            </#if>
        }else{
            <#if hasLeadPermission>
                $("#addDiv").show();
            <#else >
                $("#addDiv").hide();
            </#if>
        }
        if(startTime&&endTime) {
            if (startTime > endTime) {
                layerTipMsg(false, "提示!", "活动开始时间不能大于结束时间!");
                return;
            }
            var url =  '${request.contextPath}/familydear/famdearMonth/famdearMonthList?&type='+type+"&endTime="+endTime+"&startTime="+startTime;
            $("#showList2").load(url);
        }else {
            var url =  '${request.contextPath}/familydear/famdearMonth/famdearMonthList?&type='+type+"&endTime="+endTime+"&startTime="+startTime;
            $("#showList2").load(url);
        }

    }

    function exportList() {
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var type = $("#type").val();
        if(startTime&&endTime) {
            if (startTime > endTime) {
                layerTipMsg(false, "提示!", "活动开始时间不能大于结束时间!");
                return;
            }
            document.location.href='${request.contextPath}/familydear/famdearMonth/export?&type='+type+'&endTime='+endTime+'&startTime='+startTime;
        }else {
            document.location.href='${request.contextPath}/familydear/famdearMonth/export?&type='+type+'&endTime='+endTime+'&startTime='+startTime;
        }

    }
</script>