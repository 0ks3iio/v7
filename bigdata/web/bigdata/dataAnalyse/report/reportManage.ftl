<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<script defer src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="target-bank">
    <div class="pb-10">
        <div class="form-group no-margin">
            <select multiple name="" id="select"
                    class="form-control chosen-select"
                    data-placeholder="输入标签查询">
                <#if tags?exists && tags?size gt 0>
                    <#list tags as tag>
                        <option value="${tag.id!}">${tag.tagName!}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    <div class="target-content">
        <table class="tables">
            <thead>
            <tr>
                <th>名称</th>
                <th>标签</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
        <div class="scrollBar4" style="height: 260px;">
            <table class="tables">
                <tbody id="tableList">
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>

    $(function () {
       loadTableList();
    });
    
    function loadTableList() {
        var tagIds = $('#select').val();
        var tags = "";
        if (tagIds != null) {
            $.each(tagIds, function (n, v) {
                if (n == 0){
                    tags = v;
                } else {
                    tags = tags + "," + v;
                }
            })
        }
        $('#tableList').load('${request.contextPath}/bigdata/data/analyse/reportList?selectTags=' + tags);
    }
    
    function editModelFavorite(id) {
        layer.closeAll();
        $('.page-content').load('${request.contextPath}/bigdata/data/analyse/analyse?favoriteId=' + id);
    }
    
    function deleteModelFavorite(id, name) {
        layer.confirm('确定删除' + name + '?', {btn: ['确定', '取消'], title: '删除多维分析', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/data/analyse/deleteModelFavorite',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message);
                    } else {
                        showLayerTips('success', '删除成功', 't');
                        $('#favorite_' + id).remove();
                    }
                }
            });
        });
    }
    
    //下拉选择框
    function selectChosen() {
        $('#select').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            no_results_text: '没有找到标签'
        }).change(loadTableList);

        $(window).off('resize.chosen')
            .on('resize.chosen', function () {
                $('.chosen-select').each(function () {
                    $(this).next().css({'width': $('.target-content').width() + 'px'});
                    $(this).next().find('.chosen-results').css('height','125px').addClass('scrollbar-made');
                })
            }).trigger('resize.chosen');
        $('.chosen-choices').css('height', '32px');
    }

    selectChosen();
</script>