<form id="metadataForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${metadata.id!}">
        <input type="hidden" name="mdType" value="${mdType!}">
        <input type="hidden" id="dbId" name="dbId" value="">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据库类型：</label>
            <div class="col-sm-8">
                <select id="databaseTypeIdSelect" name="dbType" class="form-control" nullable="false"
                        <#if metadata.id! !="">disabled</#if>>
                    <option value="">请选择数据库类型</option>
                    <option <#if metadata.dbType! == "mysql">selected="selected"</#if> value="mysql">mysql</option>
                    <option <#if metadata.dbType! == "hbase">selected="selected"</#if> value="hbase">hbase</option>
                    <option <#if metadata.dbType! == "kylin">selected="selected"</#if> value="kylin">kylin</option>
                    <option <#if metadata.dbType! == "impala">selected="selected"</#if> value="impala">impala</option>
                    <option <#if metadata.dbType! == "es">selected="selected"</#if> value="es">es</option>
                </select>
            </div>
        </div>
        <div class="form-group <#if metadata.dbType! != "hbase">hide</#if>">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>hbase类型：</label>
            <div class="col-sm-8">
                <select id="isPhoenixSelect" name="isPhoenix" class="form-control" nullable="false"
                        <#if metadata.id! !="">disabled</#if>>
                    <option <#if metadata.isPhoenix?default(0) == 0>selected="selected"</#if> value="0">原生</option>
                    <option <#if metadata.isPhoenix?default(0) == 1>selected="selected"</#if> value="1">phoenix</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"></label>
            <div class="col-sm-8">
                <div class="mt-7"><font style="color:red;">数据库类型一旦确定，不可修改！</font></div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="50"
                       value="${metadata.name!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>表名：</label>
            <div class="col-sm-8">
                <input type="text" name="tableName" id="tableName" class="form-control" nullable="false" maxlength="50"
                       value="${metadata.tableName!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>是否计入数据资产：</label>
            <div class="col-sm-8">
                <label class="choice">
                    <input type="radio" <#if metadata.isProperty?default(0) == 1>checked="checked"</#if>
                           name="isProperty" value="1">
                    <span class="choice-name">是</span>
                </label>
                <label class="choice">
                    <input type="radio" <#if metadata.isProperty?default(0) == 0>checked="checked"</#if>
                           name="isProperty" value="0">
                    <span class="choice-name">否</span>
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>是否支持api：</label>
            <div class="col-sm-8">
                <label class="choice">
                    <input type="radio" <#if metadata.isSupportApi?default(0) == 1>checked="checked"</#if>
                           name="isSupportApi" value="1">
                    <span class="choice-name">是</span>
                </label>
                <label class="choice">
                    <input type="radio" <#if metadata.isSupportApi?default(0) == 0>checked="checked"</#if>
                           name="isSupportApi" value="0">
                    <span class="choice-name">否</span>
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据资产层级：</label>
            <div class="col-sm-8">
                <select id="dwRankSelect" class="form-control" name="dwRankId">
                    <#list dwRanks as item>
                        <option <#if metadata.dwRankId! == item.id>selected="selected"</#if>
                                value="${item.id!}">${item.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
        <input type="hidden" name="isCustom" value="0">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据资产主题：</label>
            <div class="col-sm-8">
                <select id="propertyTopicSelect" class="form-control" name="propertyTopicId">
                    <#list propertyTopics as item>
                        <option <#if metadata.propertyTopicId! == item.id>selected="selected"</#if>
                                value="${item.id!}">${item.name!}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">资产标签：</label>
            <div class="col-sm-8">
                <p class="parallel">
                    标签设置不可超过4个字<span class="control-label tagManage">标签管理</span>
                </p>
                <div class="tag-set">
                    <#if propertyTags?exists && propertyTags?size gt 0>
                        <#list propertyTags as tag>
                            <span>
                            <span tag_id="${tag.id!}" class="label-name <#if tagIds?contains(tag.id)>selected</#if>">${tag.name!}</span>
                            <i class="fa fa-minus <#if tag.isCustom?default(0) == 1>readonly<#else>modify</#if>"></i>
                        </span>
                        </#list>
                    </#if>
                    <span>
                        <span class="plus-label">+</span>
                    </span>
                </div>
            </div>
        </div>
        <input type="hidden" id="status" name="status" value="${metadata.status?default('0')}">
        <#--<div class="form-group">-->
            <#--<label class="col-sm-3 control-label no-padding-right">是否启用：</label>-->
            <#--<div class="col-sm-8">-->
                <#--<input type="hidden" id="status" name="status" value="${metadata.status?default('0')}">-->
                <#--<label class="switch switch-txt mt-5">-->
                    <#--<input type="checkbox" <#if metadata.status?default(0) == 1>checked="checked"</#if> name="" onchange="changeStatus(this)">-->
                    <#--<span class="switch-name">显示</span>-->
                <#--</label>-->
            <#--</div>-->
        <#--</div>-->

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:60px;resize: none;">${metadata.remark!}</textarea>
            </div>
        </div>

    </div>
</form>
<script>
    $(function () {

        $('#databaseTypeIdSelect').change(function () {
            if ($(this).val() == 'hbase') {
                $('#isPhoenixSelect').parent().parent().removeClass('hide');
            } else {
                $('#isPhoenixSelect').parent().parent().addClass('hide');
            }
        });
        
        var httpInvoke = {};
        /**
         * 删除标签
         * @param tagId 标签ID
         * @param success 成功之后的回调函数
         */
        httpInvoke.deleteTag = function (tagId, success) {
            $.ajax({
                url: _contextPath + '/bigdata/propertyTag/deleteTag',
                type: 'POST',
                data:{tagId:tagId},
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
            $.ajax({
                url: _contextPath + '/bigdata/propertyTag/addTag',
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

        //标签管理
        $('.tagManage').click(function () {
            if ($('.tag-set>span i.modify').css('display') == 'none') {
                $('.tag-set i.modify').show();
                $('span.plus-label').hide();
            } else {
                $('.tag-set i.modify').hide();
                $('span.plus-label').show();
            }
        });
        //删除标签
        $('.tag-set').on('click', '.fa-minus', function () {
            var $this = $(this);
            if ($this.hasClass('readonly')) {
                showLayerTips4Confirm('error','系统内置标签不能删除!');
                return;
            }

            httpInvoke.deleteTag($this.prev('span').attr('tag_id'), function () {
                $this.parent().remove();
            });
        });
        //新增标签
        $('.plus-label').click(function () {
            var str = $('<span><span><input type="" value="" maxlength="4" id="label-input"/></span><i class="fa fa-minus modify"></i></span>');
            $(this).parent().before(str);
            $('#label-input').focus();
            $('.tag-set input').css({'width': '100%', 'height': '23px', 'border': 'none', 'padding': 0})
        });
        var tag_index = 0;
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
                    layer.tips(res.message, '#_new_tag_input' + tag_index, {
                        tipsMore: true,
                        tips: 3,
                        time: 2000
                    });
                    $('#_new_tag_input' + tag_index).parent().parent().remove();
                    tag_index++;
                });
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
    });
    
    function changeStatus(e) {
        if ($(e).is(':checked')) {
            $('#status').val('1');
        } else {
            $('#status').val('0');
        }
    }
</script>