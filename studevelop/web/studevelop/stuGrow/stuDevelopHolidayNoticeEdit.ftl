<input type="hidden" name="creationTime" value="${stuDevelopHolidayNotice.creationTime!}" >
<input type="hidden" name="modifyTime" value="${stuDevelopHolidayNotice.modifyTime!}" >
<input type="hidden" name="id" value="${stuDevelopHolidayNotice.id!}" >
<input type="hidden" name="unitId" value="${stuDevelopHolidayNotice.unitId!}" >
<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-2 control-label">假期事项</label>
        <div class="col-sm-6">
            <div class="textarea-container">
                <textarea name="notice" id="notice" cols="30" rows="10"  maxlength="200" nullable="false" class="form-control">${stuDevelopHolidayNotice.notice!}</textarea>
                <span>200</span>
            </div>

        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="button" onclick="saveNotice();" class="btn btn-blue btn-long">保存</button>
        </div>
    </div>
</div>
<script type="text/javascript">

    function saveNotice(){
        var check = checkValue("#noticeForm")
        if(!check){
            return false;
        }
        var ii = layer.load();
        var options = {
            url:"${request.contextPath}/studevelop/holidayNotice/save",
            dataType:"json",
            type:"post",
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    // $("#arrange-commit").removeClass("disabled");
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);

                }
                layer.close(ii);
            },
            clearForm : false,
            resetForm : false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
        }
        $("#noticeForm").ajaxSubmit(options);
    }

</script>