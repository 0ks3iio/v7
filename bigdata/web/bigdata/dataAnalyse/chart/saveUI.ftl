<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<script defer src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<form id="myForm">
    <div class="tab-content">
        <div id="dd" class="tab-pane active">
            <input type="hidden" value="${favorite.id!}" name="id">
            <input type="hidden" value="${favorite.unitId!}" name="unitId">
            <input type="hidden" value="${favorite.userId!}" name="userId">
            <input type="hidden" value="${favorite.orderType!}" name="orderType">
            <div class="layer-title">
                <b>名称 </b>
            </div>
            <div class="layer-title">
                <input type="text" name="favoriteName" id="favoriteName" class="form-control" nullable="false" maxlength="50"
                       value="${favorite.favoriteName!}">
            </div>
            <div class="layer-title">
                <b>图表类型 </b>
            </div>
            <div class="layer-title">
                <div class="clearfix js-add-active">
                    <div type="line" class="chart-type line-type <#if favorite.chartType?default('line') == 'line'>active</#if> ">
                        <div class="text-center">
                            <i class="iconfont icon-diagram-fill"></i>
                        </div>
                        <div class="" style="line-height:30px;">线图</div>
                    </div>
                    <div type="bar" class="chart-type bar-type <#if favorite.chartType?default('line') == 'bar'>active</#if> ">
                        <div class="text-center">
                            <i class="iconfont icon-columnchart-fill"></i>
                        </div>
                        <div class="" style="line-height:30px;">柱图</div>
                    </div>
                    <div type="cookie" class="chart-type pie-type <#if favorite.chartType?default('line') == 'cookie'>active</#if> ">
                        <div class="text-center">
                            <i class="iconfont icon-piechart-fill"></i>
                        </div>
                        <div class="" style="line-height:30px;">饼图</div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="chartType" name="chartType" value="line">


            <div class="layer-title">
                <b>设置标签 </b>
                <span class="parallel"> (标签设置不可超过4个字)<span class="control-label tag-manage">标签管理</span></span>
            </div>
            <div class="tag-set">
                <#if tags?exists && tags?size gt 0>
                    <#list tags as tag>
                        <span>
                            <span tag_id="${tag.id!}"
                                  class="label-name <#if tag.selected>selected</#if> ">${tag.tagName!}</span>
                            <i class="fa fa-minus"></i>
                        </span>
                    </#list>
                </#if>
                <span>
				<span class="plus-label">+新增标签</span>
			</span>
            </div>

            <div class="layer-title">
                <b>所属文件夹 </b>
            </div>
            <div class="layer-title">
                <select name="folderId" id="folderId" onchange="changeFolder()"
                        class="form-control new-input-style"
                        data-placeholder="<#if folderTree?exists && folderTree?size gt 0>请选择文件夹<#else>请先维护文件夹</#if>">
                    <#if folderTree?exists && folderTree?size gt 0>
                        <#list folderTree as dir>
                            <optgroup label="${dir.folderName!}">
                                <#if folderMap[dir.id]?exists>
                                    <#list folderMap[dir.id] as item>
                                        <option <#if folderDetail.folderId! == item.id!>selected</#if>
                                                value="${item.id!}">
                                            &nbsp;&nbsp;&nbsp;${item.folderName!}</option>
                                    </#list>
                                </#if>
                                <#list dir.childFolder as secondDir>
                                    <optgroup label="&nbsp;&nbsp;&nbsp;${secondDir.folderName!}">
                                        <#if folderMap[secondDir.id]?exists>
                                            <#list folderMap[secondDir.id] as secondDirItem>
                                                <option
                                                        <#if folderDetail.folderId! == secondDirItem.id!>selected</#if>
                                                        value="${secondDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${secondDirItem.folderName!}</option>
                                            </#list>
                                        </#if>
                                        <#list secondDir.childFolder as thirdDir>
                                            <optgroup
                                                    label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDir.folderName!}">
                                                <#if folderMap[thirdDir.id]?exists>
                                                    <#list folderMap[thirdDir.id] as thirdDirItem>
                                                        <option
                                                                <#if folderDetail.folderId! == thirdDirItem.id!>selected</#if>
                                                                value="${thirdDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDirItem.folderName!}</option>
                                                    </#list>
                                                </#if>
                                            </optgroup>
                                        </#list>
                                    </optgroup>
                                </#list>
                            </optgroup>
                        </#list>
                    </#if>
                </select>
            </div>

            <div class="layer-title">
                <b>筛选条件 </b>
            </div>
            <div class="layer-title" style="line-height: 30px;">
                <select multiple name="" id="conditionSelect" class="form-control chosen-select save-chosen-select" data-placeholder="选择筛选条件，最多可选择两个">
                    <#list dimensionList as d>
                        <#if d.isFilter?default(1) != 1>
                            <option value="${d.id!}" <#if conditionIds!?seq_contains(d.id!)?string("yes", "no") == "yes"> selected="selected"</#if>>${d.name!}</option>
                        </#if>
                    </#list>
                </select>
            </div>

            <div class="layer-title">
                <b>排序号 </b>
            </div>
            <div class="layer-title">
                <input type="text" name="orderId" id="rOrderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${favorite.orderId!}">
            </div>
            <div class="layer-title">
                <b>备注 </b>
            </div>
            <div class="layer-title">
            <textarea name="remark" id="remark" type="text/plain" nullable="true" maxLength="200"
                      style="width:100%;height:80px;">${favorite.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<input type="hidden" value="5" id="tag_type" />
<script>
    var httpInvoke = {};
    /**
     * 删除标签
     * @param tagId 标签ID
     * @param success 成功之后的回调函数
     */
    httpInvoke.deleteTag = function (tagId ,success) {
        $.ajax({
            url: '${request.contextPath}/bigdata/tag/' + tagId,
            type: 'DELETE',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2, time: 800});
                } else {
                    if (typeof success === 'function') {
                        success();
                    }
                }
            }
        })
    };

    httpInvoke.addTag = function (tagName, success, error) {
        var data = {};
        data.tagName = tagName;
        data.tagType = $('#tag_type').val();
        $.ajax({
            url: '${request.contextPath}/bigdata/tag/',
            type: 'POST',
            data: data,
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    if (typeof error === 'function') {
                        error(response);
                    }
                } else {
                    if (typeof success === 'function') {
                        success(response);
                    }
                }
            }
        });
    };

    $(function () {
        if ($('#orderId').val() == '') {
            changeFolder();
        }

        initSelect();

        //选择图表类型
        $('body').on('click','.chart-type',function () {
            $('#chartType').val($(this).attr('type'));
        });

        //窗口大小
        $('body').on('click','.js-add-active>div',function(){
            $(this).addClass('active').siblings().removeClass('active')
        });

        //标签管理
        $('.tag-manage').click(function () {
            if ($('.tag-set>span i').css('display') == 'none') {
                $('.tag-set i').show();
                $('span.plus-label').hide();
            } else {
                $('.tag-set i').hide();
                $('span.plus-label').show();
            }
            setTextArea();
        });
        //删除标签
        $('.tag-set').on('click', '.fa-minus', function () {
            var $this = $(this);
            httpInvoke.deleteTag($this.prev('span').attr('tag_id') ,function () {
                $this.parent().remove();
                setTextArea();
            });

        });
        //新增标签
        $('.plus-label').click(function () {
            var str = $('<span><span><input type="" value="" maxlength="4" id="label-input"/></span><i class="fa fa-minus"></i></span>');
            $(this).parent().before(str);
            $('#label-input').focus();
            $('.tag-set input').css({'width': '100%', 'height': '23px', 'border': 'none', 'padding': 0})
            setTextArea();
        });
        var tag_index=0;
        $('.tag-set').on('blur', 'input', function () {
            var $this = $(this);
            var newTagText = $.trim($this.val());
            if (newTagText !== '') {
                if (newTagText.length > 4) {
                    $this.attr('id', '_newTag');
                    layer.tips('标签长度不能超过四个字', '#_newTag', {
                        tipsMore: true,
                        tips: 3,
                        time: 2000
                    });
                    return;
                }
                httpInvoke.addTag($this.val(), function (res) {
                    $this.parent().text($this.val()).attr("tag_id", res.data);
                    $('.tag-set>span>span').addClass('label-name');
                    $(this).parent().remove();
                }, function (res) {
                    $this.attr("id", "_new_tag_input" + tag_index);
                    layer.tips(res.message, '#_new_tag_input'+tag_index,{
                        tipsMore: true,
                        tips: 3,
                        time: 2000
                    });
                    $('#_new_tag_input'+tag_index).parent().parent().remove();
                    tag_index++;
                });
                setTextArea();
            } else {
                $this.parent().parent().remove();
            }
        });
        //选中效果
        var $lastTag = null;
        $('.tag-set').on('click', '.label-name', function () {
            var $this = $(this);
            if ($(".tag-set span.label-name.selected").size() === 3 && !$(this).hasClass('selected')) {
                if ($lastTag) {
                    $lastTag.toggleClass('selected');
                } else {
                    var $selected = $($(".tag-set span.label-name.selected")[0]);
                    $selected.toggleClass('selected');
                }
            }

            $lastTag = $this;
            $this.toggleClass('selected')
        });
    })

    function changeFolder() {
        $.ajax({
            url: '${request.contextPath}/bigdata/folder/getMaxOrderId',
            data:{folderId : $('#folderId').val()},
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    $('#orderId').val(val.data);
                } else {
                    layer.msg(val.message, {icon: 2, time: 1000});
                }
            }
        });
    }

    function initSelect() {
        // 选择框修改
        $('.save-chosen-select').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            max_selected_options: 2,
            no_results_text: '没有找到维度'
        });
        //resize the chosen on window resize

        $(window).off('resize.chosen')
            .on('resize.chosen', function () {
                $('.save-chosen-select').each(function () {
                    var $this = $(this);
                    $this.next().css('cssText', 'width:'+$('.layer-title').width()+'px !important;');
                    $this.next().css('height', '32px');
                    $this.next().find('.chosen-results').addClass('scrollbar-made');
                })
            }).trigger('resize.chosen').trigger("chosen:updated");
    }
</script>