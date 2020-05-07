
    <div class="form-horizontal" style="width: 95%;">
        <div class="form-group no-margin-bottom">
            <label class="col-sm-2 control-label no-padding-right">小组成员：</label>
            <div class="col-sm-10">
                <div style="border: 1px solid #eee;">
                    <div class="box-body padding-10 clearfix" style="border-bottom: 1px solid #eee;">
                        <label class="float-left"><input type="checkbox" class="wp" id="group-user-check-all"><span class="lbl"> 全选</span></label>
                        <div class="float-right input-group input-group-search">
                            <div class="pull-left">
                                <input type="text" class="form-control" placeholder="请输入" value="">
                            </div>
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-default">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="ml10" style="height:280px;overflow:auto;">
                            <#if users?? && users?size gt 0>
                                <input type="hidden" value="${users?size}" id="allCount" />
                                <form id="group-user-form">
                                <#list users as user>
                                    <label class="mr10 w100 ellipsis">
                                        <input name="id" type="checkbox" class="wp group-users" value="${user.id}" <#if user.checked>checked="checked"</#if> >
                                        <span class="lbl"> ${user.name}</span>
                                    </label>
                                </#list>
                                </form>
                            </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(function () {
            $('#group-user-check-all').change(function () {
                $('.group-users').prop('checked', $(this).is(':checked'));
            });
            $('.group-users').change(function () {
                $('#group-user-check-all').prop('checked', parseInt($('#allCount').val()) === $('.group-users:checked').length);
            });
        })
    </script>
