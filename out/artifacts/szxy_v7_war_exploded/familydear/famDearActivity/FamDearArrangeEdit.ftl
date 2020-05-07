<#import "/fw/macro/popupMacro.ftl" as popup />
<#if needEditList?exists && (needEditList?size>0)>
<div id="" class="layer-body" style="height:200px;overflow-y: scroll;overflow-x: hidden;" >
<form id="myForm">
    <input type="hidden" name="id" id ="id" value="${activityId}"/>
<table class="table table-striped table-bordered table-hover no-margin" id="tab">
    <thead>
    <tr>
        <th width="100">批次</th>
        <th width="150">活动开始时间</th>
        <th width="150">活动结束时间</th>
        <th width="100">结亲村</th>
        <th width="100">干部人数</th>
        <th width="150">报名开始时间</th>
        <th width="150">报名结束时间</th>
        <th width="100">带队人</th>
    </tr>
    </thead>
    <tbody>

            <#list needEditList as item1 >
            <tr>
                <td><input type="hidden" name='famDearArrangeList[${item1_index}].id' value="${item1.id}"/><input class='form-control' name='famDearArrangeList[${item1_index}].batchType' value="${item1.batchType}" disabled="disabled"/></td>
                <td><input type="text" class='form-control date-picker' vtype='data' name='famDearArrangeList[${item1_index}].startTime' value="${item1.startTime?string('yyyy-MM-dd')!}" disabled="disabled"/></td>
                <td><input type="text" class='form-control date-picker' vtype='data' name='famDearArrangeList[${item1_index}].endTime' value="${item1.endTime?string('yyyy-MM-dd')!}" disabled="disabled"/></td>
                <td><input type="text" class='form-control' name='famDearArrangeList[${item1_index}].rural' value="${item1.rural}" disabled="disabled"/></td>
                <td><input type="text" class='form-control' name='famDearArrangeList[${item1_index}].peopleNumber' value="${item1.peopleNumber}" disabled="disabled"/></td>
                <td><input type="text" class='form-control date-picker' vtype='data' name='famDearArrangeList[${item1_index}].applyTime' value="${item1.applyTime?string('yyyy-MM-dd')!}" disabled="disabled"/></td>
                <td><input type="text" class='form-control date-picker' vtype='data' name='famDearArrangeList[${item1_index}].applyEndTime' value="${item1.applyEndTime?string('yyyy-MM-dd')!}" disabled="disabled"/></td>
                <td>
                    <input type="hidden" class='form-control ' id="leaderUserId${item1_index}" name='famDearArrangeList[${item1_index}].leaderUserId' onclick="editLeader(${item1_index})" value="${item1.leaderUserId!}"/>
                    <input type="text" class='form-control ' id="leaderUserName${item1_index}" onclick="editLeader(${item1_index})" value="${item1.leaderUserNames!}"/>
                    <div style="display: none;">
                        <@popup.selectMoreTeacherUser clickId="user${item1_index}Name" id="user${item1_index}Ids" name="user${item1_index}Name" handler="savePermission(${item1_index})">
                        <input type="hidden" id="user${item1_index}Ids" name="user${item1_index}Ids" value=${item1.leaderUserId!}>
                        <input type="text" id="user${item1_index}Name" class="form-control" value=${item1.leaderUserNames!}>
                        </@popup.selectMoreTeacherUser>
                    </div>
                </td>
            </tr>
            </#list>

    <#if type?default("2")!="2">
    <tr>
        <td colspan="8" class="text-center"><a class="color-blue" href="#" onclick="tt()">+新增安排</a></td>
    </tr>
    </#if>
    </tbody>
</table>
</form>
</div>
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<#else >
        <div class="no-data-container">
            <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无需要维护的批次</p>
                </div>
            </div>
        </div>
</#if>
<script>
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });
    
    function editLeader(index) {
        $('#user'+index+'Name').click();
    }



    var isSubmit=false;

    $("#arrange-commit").on("click",function () {
        var id = $("#activityId").val();
        var options = {
            url : "${request.contextPath}/familydear/activity/edu/saveArrangeEdit",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    showList1();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#myForm").ajaxSubmit(options);
    });
    isSubmit = false;
    function savePermission(index){
        if(isSubmit){
            return;
        }
        var userIds = $('#user'+index+'Ids').val();
        if(userIds != ''){
            var arr = userIds.split(",");
            if(arr.length > 100){
                layerTipMsgWarn("保存失败","人员最多100人！");
                return;
            }
        }
        var userIds = $('#user'+index+'Ids').val();
        var userName = $('#user'+index+'Name').val();
        $('#leaderUserId'+index).val(userIds);
        $('#leaderUserName'+index).val(userName);

    }
</script>