<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if auditionList?exists&& (auditionList?size > 0)>
<div class="explain">
    <p>参加面试共${auditionList?size}人,</span>
        &nbsp;&nbsp;合格${passNum!}人</span>
    </p>
</div>
<#--<div class="explain">-->
    <#--<p>寝室房间数：<span id="allNum">${auditionList?size}</span>-->
        <#--&nbsp;&nbsp;入住人数：<span id="passNum">${passNum!}</span>-->
    <#--</p>-->
<#--</div>-->
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th width="15%">学校</th>
        <th width="15%">学生姓名</th>
        <th width="15%">学籍号</th>
        <th width="15%">身份证号</th>
        <th width="10%">性别</th>
        <th width="15%">加面试成绩</th>
        <th width="15%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list auditionList as item>
        <tr>
            <td >${item.schoolName!}</td>
            <td >${item.studentName!}</td>
            <td >${item.stuCode!}</td>
            <td>${item.identityCard!}</td>
            <td >${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
            <td >
                <label class="no-margin-top">
                    <input type="radio" name="isPass_${item_index}" class="wp" value="1" <#if item.isPass?default("2")=="1">checked="checked"</#if > onchange="changeStatus('${item.id}',1)">
                    <span class="lbl"> 合格</span>
                </label>
                <label class="no-margin-top">
                    <input type="radio" name="isPass_${item_index}" class="wp" value="2" <#if item.isPass?default("2")=="2">checked="checked"</#if > onchange="changeStatus('${item.id}',2)">
                    <span class="lbl"> 不合格</span>
                </label>
            </td>
            <td ><a href="javascript:void(0);" onclick="doDel('${item.id}')">删除</a></td>
        </tr>
        </#list>
    </tbody>
</table>
<#else >
    <div class="no-data-container">
        <div class="no-data">
                    <span class="no-data-img">
                        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                    </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无相关数据</p>
            </div>
        </div>
    </div>
</#if>
<#--<@htmlcom.pageToolBar container="#tabDiv" class="noprint">-->
<#--</@htmlcom.pageToolBar>-->
<script type="text/javascript">
    function doDel(id) {
        showConfirmMsg('确认删除？','提示',function() {
            var acadyear = $("#acadyear").val();
            var semester = $("#semester").val();
            var schoolId = $("#schoolList").val();
            var examId = $("#examId").val();
            var ii = layer.load();
            $.ajax({
                url: "${request.contextPath}/exammanage/edu/audition/auditionDel",
                data: {id: id},
                success: function (data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if (jsonO.success) {
                        layer.msg(jsonO.msg, {
                            offset: 't',
                            time: 2000
                        });
                        changeExamId();
                    } else {
                        layerTipMsg(jsonO.success, "失败", jsonO.msg);
                    }
                    layer.close(ii);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            })
        })
    }
    function changeStatus(id,status) {
        $.ajax({
            url: "${request.contextPath}/exammanage/edu/audition/updateState",
            data: {id: id,isPass:status},
            success: function (data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if (jsonO.success) {
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    showList();
                } else {
                    layerTipMsg(jsonO.success, "失败,请重试", jsonO.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        })
    }

</script>