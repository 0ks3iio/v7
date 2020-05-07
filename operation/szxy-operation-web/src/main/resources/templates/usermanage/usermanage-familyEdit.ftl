<form id="family-edit-form">
    <input type="hidden" name="id" value="${family.id}" id="familyId"/>
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">姓名：</label>
                <div class="col-sm-4">
                    <input id="realName" type="text" value="${family.realName}" name="realName"
                           class="form-control radius-4"
                           disabled="disabled">
                </div>
                <label class="col-sm-2 control-label no-padding-right">手机号：</label>
                <div class="col-sm-4">
                    <input id="mobilePhone" type="text" value="${family.mobilePhone!}" name="mobilePhone"
                           class="form-control radius-4">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">学生姓名：</label>
                <div class="col-sm-4 mt7" >${family.studentRealName!}</div>
                <label class="col-sm-2 control-label no-padding-right">学生用户：</label>
                <div class="col-sm-4 mt7" >${family.studentUsername!}</div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">学校：</label>
                <div class="col-sm-4 mt7" >${family.unitName!}</div>
                <label class="col-sm-2 control-label no-padding-right">班级：</label>
                <div class="col-sm-4 mt7" >${family.className!}</div>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        $('#family-edit-form').bootstrapValidator({
            excluded: [":disabled"],
            feedbackIcons: {
                valid: 'glyphicon /*glyphicon-ok*/',
                invalid: 'glyphicon /*glyphicon-remove*/',
                validating: 'glyphicon /*glyphicon-refresh*/'
            },
            autoFocus: true,
            fields: {
                "mobilePhone": {
                    validators: {
                        notEmpty: {
                            message: "手机号 不能为空"
                        },
                        chineseMobile: {

                        }
                    }
                }
            }
        });
    })
</script>