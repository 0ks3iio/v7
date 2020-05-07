<div class="card-item js-add">
    <div class="bd-view bg-fafafa plus">
        <a href="javascript:void(0);" onclick="addGroup();">
            <div class="drive screen-team bg-fafafa">
            </div>
            <div class="padding-side-20">
                <h3></h3>
            </div>
            <i class="fa fa-plus fa-4x"></i>
        </a>
    </div>
</div>
<#if groupVos?? && groupVos?size gt 0>
    <#list groupVos as groupVo>
                    <div class="card-item">
                        <div class="bd-view">
                            <a href="javascript:;">
                                <div class="drive screen-team">
                                    <#if groupVo.groupScreenVos?? && groupVo.groupScreenVos?size gt 0>
                                        <#list groupVo.groupScreenVos as groupScreenVo>
                                            <#if groupScreenVo_index lt 3>
                                            <img class="screens-shot >" src="${fileUrl!}/bigdata/datav/${groupScreenVo.unitId!}/${groupScreenVo.id!}.jpg?${.now}"
                                                 alt="">
                                            </#if>
                                        </#list>
                                    </#if>
                                    <div class="coverage">
                                        <div class="vetically-center" data-id="${groupVo.id!}">
                                            <img src="${request.contextPath}/static/bigdata/images/remove.png"
                                                 width="20" height="20" class="remove-btn"/>
                                            <img src="${request.contextPath}/static/bigdata/images/pencil.png"
                                                 width="20" height="20" class="edit-btn"/>
                                            <button class="btn btn-blue btn-long group-view-btn"
                                                    data-name="${groupVo.name!}" data-view="<#if groupVo.groupScreenVos?? && groupVo.groupScreenVos?size gt 0>1<#else>0</#if>">查看
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="padding-side-20">
                                    <h3>${groupVo.name!}</h3>
                                </div>
                            </a>
                        </div>
                    </div>
    </#list>
</#if>
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
        $('#group').load('${request.contextPath}/bigdata/cockpitQuery/groupIndex', screenReHeight);
    }

    function addGroup(groupId) {
        $('#layer-group').load('${request.contextPath}/bigdata/cockpitQuery/groupLayer?groupId=' + (groupId ? groupId : ''), function () {
            layer.open({
                type: 1,
                shade: .6,
                title: '新增分组',
                btn: ['确定', '取消'],
                area: ['400px', '70%'],
                content: $('#layer-group'),
                yes: function (index) {
                    if (check()) {
                        layer.close(index);
                        submitGroup();
                    }
                }
            });
        });
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

    function submitGroup() {

        let group = {};
        group.name = $('#groupName').val();
        group.id = $('#groupId').val();
        if ($('.yes').prev().is(':checked')) {
            group.interval = $('#groupInterval').val();
        } else {
            group.interval = 0;
        }
        let screenIds = [];
        $('.screens').each(function () {
            if ($(this).is(':checked')) {
                screenIds.push($(this).data('id'))
            }
        });
        group.screenIds = screenIds.join(',');
        group.createUserId = $('#groupUserId').val();

        $.ajax({
            url: '${request.contextPath}/bigdata/cockpitQuery/screenGroup',
            data: group,
            type: 'post',
            success: function (res) {
                if (res.success) {
                    layer.msg("保存成功", {icon: 1});
                    reload();
                } else {
                    layer.msg("保存失败", {icon: 2});
                }
            },
            error: function () {
                layer.msg("网络异常", {icon: 2});
            }
        })
    }
</script>