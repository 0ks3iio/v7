<div class="row-made">
    <div class="col-made-6">
        <div class="screen-box type2 add-group">
            <div class="add-new" onclick="addGroup();">
                <i class="wpfont icon-plus"></i>
                <p>新增分组</p>
            </div>
        </div>
    </div>
    <#if groupVos?? && groupVos?size gt 0>
        <#list groupVos as groupVo>
            <div class="col-made-6">
                <div class="screen-box type2">
                    <div class="box-img clearfix">
                        <#if groupVo.groupScreenVos?? && groupVo.groupScreenVos?size gt 0>
                            <#list groupVo.groupScreenVos as groupScreenVo>
                                <#if groupScreenVo_index lt 1>
                                    <img src="${fileUrl!}/bigdata/datav/${groupScreenVo.unitId!}/${groupScreenVo.id!}.jpg?${.now?string('HH-ss')}">
                                </#if>
                            </#list>
                        </#if>
                        <div class="cover-box" data-id="${groupVo.id!}">
                            <div class="pos-r-corner" data-id="${groupVo.id!}">
                                <i class="iconfont icon-editor-fill edit-btn js-add-new"></i>
                                <i class="iconfont icon-delete-bell remove-btn js-delete"></i>
                            </div>
                            <div class="btn-look-over group-view-btn" data-view="<#if groupVo.groupScreenVos?? && groupVo.groupScreenVos?size gt 0>1<#else>0</#if>">
                                <span>查看</span>
                            </div>
                        </div>
                    </div>
                    <div class="box-text">
                        <span>${groupVo.name!}</span>
                    </div>
                </div>
            </div>
        </#list>
    </#if>
</div>

    <div id="layer-group">

    </div>

<script>
    $(function () {
        $('.edit-btn').on('click', function () {
            addGroup($(this).parent().data('id'));
        });
        $('.remove-btn').on('click', function () {
            let id = $(this).parent().data('id');
            deleteGroup(id);
        });
        $('.group-view-btn').on('click', function () {
            if ($(this).data('view')=='0') {
                let id = $(this).parent().data('id');
                layer.confirm("该分组下的大屏已被删除您无法查看，是否删除该分组？", {icon: 3, title:'提示'}, function (index) {
                    deleteGroup(id);
                    layer.close(index);
                });
            } else {
                window.open('${request.contextPath}/bigdata/datav/screen/groupView/' + $(this).parent().data('id'), $(this).data('name'));
            }
        });
    });

    function deleteGroup(id) {
        layer.confirm("确认删除该分组？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0},function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/cockpitQuery/delete/screenGroup/' + id,
                type: 'GET',
                success: function (res) {
                    if (res.success) {
                        layer.msg("删除成功", {icon: 1});
                        reload();
                    } else {
                        layer.msg("删除失败", {icon: 2});
                    }
                },
                error: function () {
                    layer.msg("网络异常", {icon: 2});
                }
            });
        });
    }

    function reload() {
        $('#group').load('${request.contextPath}/bigdata/cockpitQuery/groupIndex', function () {
            cardH();
        });
    }

    function addGroup(groupId) {
        $('#layer-group').removeClass('hide').load('${request.contextPath}/bigdata/cockpitQuery/groupLayer?groupId=' + (groupId ? groupId : ''), function () {
            popUpWindows('700px','520px','新增分组','.layer-group');
            var h = parseInt($('.layer-group').find('.box-img').first().width() * 9 / 16) + 'px';
            $('.layer-group').find('.box-img').each(function (index, ele) {
                $(this).height(h)
            });
            $('.group-ok').click(function () {
                if(!check()) {
                    return ;
                }
                submitGroup();
            });
        });
    }

    // 自定义弹窗设置
    function popUpWindows(w,h,t,ele){
        $(ele).show();
        $(ele).children().width(w).height(h);
        $(ele).find('.layer-head-name').text(t);
        $(ele).on('click','.js-close',function(){
            $(ele).hide()
        })
    }

    function check() {
        let name = $('#groupName').val();
        if ($.trim(name) == '') {
            layer.tips('请输入分组名称', '#groupName', {
                tipsMore: true,
                tips: 3,
                time: 2000
            });
            return false;
        }
        if (name.length>16) {
            layer.tips('名称长度超过最大限制', '#groupName', {
                tipsMore: true,
                tips: 3,
                time: 2000
            });
            return false;
        }
        return true;
    }

    var isSubmitGroup = false;

    function submitGroup() {

        let group = {};
        group.name = $('#groupName').val();
        group.id = $('#groupId').val();
        if ($('.yes').is(':checked')) {
            group.interval = 15;
        } else {
            group.interval = 0;
        }
        let screenIds = [];
        $('.screens').each(function () {
            if ($(this).is(':checked')) {
                screenIds.push($(this).data('id'))
            }
        });
        if (screenIds.length === 0) {
            layer.msg("请至少选择一个大屏", {icon: 0, offset: 't'});
            return;
        }
        group.screenIds = screenIds.join(',');
        group.createUserId = $('#groupUserId').val();

        if (isSubmitGroup) {
            return;
        }

        isSubmitGroup = true;
        $.ajax({
            url: '${request.contextPath}/bigdata/cockpitQuery/screenGroup',
            data: group,
            type: 'post',
            success: function (res) {
                if (res.success) {
                    layer.msg("保存成功", {icon: 1});
                    reload();
                    $('#layer-group').addClass('hide')

                    isSubmitGroup = false;
                } else {
                    layer.msg("保存失败", {icon: 2});
                    isSubmitGroup = false;
                }
            },
            error: function () {
                layer.msg("网络异常", {icon: 2});
                isSubmitGroup = false;
            }
        })
    }
</script>