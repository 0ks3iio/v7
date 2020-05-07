<#if applies?? && applies?size gt 0>
    <#list applies as apply>
        <form id="form-${apply.id}" class="form-interface">
        <input type="hidden" name="id" value="${apply.id}" />
        <input type="hidden" name="interfaceId" value="${apply.interfaceId!}" />
        <div class="deve-basic-box">
            <div class="deve-basic-heard">
                <div class="deve-bh-first">${apply.method!}</div>
                <div class="deve-bh-right">
                    <div>${apply.uri!}</div>
                </div>
            </div>
            <div class="deve-basic-body deveman-basic-body">
                <div class="deveman-basic-title">
                    <span>每天最大调用次数</span>
                    <input min="1" max="5000" value="${apply.maxInvokeNumber!0}" type="number" name="maxInvokeNumber"  class="form-control" style="width:110px;"/>
                    <span>次</span>
                    <span class="deveman-basic-two">每次最大获取数量</span>
                    <input min="1" max="1000" value="${apply.maxCount!0}" type="number" name="maxCount" class="form-control" style="width:110px;"/>
                    <span> 条</span>
                </div>
                <table class="tables">
                    <thead>
                    <tr>
                        <#if apply.status==3>
                        <th>
                            <label  class="choice choice-allcheck">
                                <input type="checkbox" data-id="${apply.id}"  data-index="0" />
                                <span class="choice-name"></span>
                            </label>
                        </th>
                        <#else >
                        <th></th>
                        </#if>
                        <th>字段名称</th>
                        <th>column</th>
                        <th>敏感字段</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if apply.fields?? && apply.fields?size gt 0>
                        <#list apply.fields as field>
                            <tr>
                                <#if apply.status==3>
                                <td>
                                    <label  class="choice">
                                        <input class="check-${apply.id}" type="checkbox" value="${field.id}" name="fieldIds[${field_index}]"  />
                                        <span class="choice-name"></span>
                                    </label>
                                </td>
                                <#else >
                                <td></td>
                                </#if>
                                <td>${field.displayName!}</td>
                                <td>${field.entityColumnName!}</td>
                                <td>
                                    <#if field.isSensitive==0>否<#else >是</#if>
                                </td>
                            </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
                <div class="deveman-basic-title">
	                <#if apply.alldataSetRules?? && apply.alldataSetRules?size gt 0>
                         <span>数据集规则：</span>
	                     <select id="dataSetRuleId" class="form-control" nullable="false">
	                            <option value="">请选择数据集</option>
							   <#list apply.alldataSetRules as item>
				                    <option <#if apply.dataSetRule?exists && apply.dataSetRule.id! == item.id!>selected="selected"</#if> value="${item.id!}">${item.paramValue!}</omption>
				               </#list>
		                 </select>
	                </#if>
                </div>
                <!-- 未审核页面按钮 -->
                <#if apply.status==3>
                <div class="deveman-bottom">
                    <button type="button" class="btn btn-primary apply-pass" data-id="${apply.id}">审核通过</button>
                    <button type="button" class="btn btn-default apply-unpass" data-id="${apply.id}">审核不通过</button>
                </div>
                <#else >
                <div class="deveman-bottom">
                    <#if apply.status==1>
                    <!-- 审核通过页面的按钮 -->
                    <button type="button" class="btn btn-primary apply-modify" data-id="${apply.id}">更新</button>
                    </#if>
                    <button type="button" class="btn btn-danger apply-delete" data-id="${apply.id}">删除</button>
                </div>
                </#if>
            </div>
        </div>
        </form>
    </#list>
    <script>
        
        var appInterfaceValidator = {
            validateMaxInvokeNumber : function (applyId) {
                var max = $('#form-'+applyId).find('input[name="maxInvokeNumber"]').val();
                if (max > 5000) {
                    showLayerTips('error', '每天最大调用次数 不能超过5000', 't');
                    return false;
                }
                if (max <= 0) {
                    showLayerTips('error', '每天最大调用次数 至少1次', 't');
                    return false;
                }
                return true;
            },
            validateMaxCount: function (applyId) {
                var max = $('#form-'+applyId).find('input[name="maxCount"]').val();
                if (max > 1000) {
                    showLayerTips('error', '每次最大获取数量 不能超过1000', 't');
                    return false;
                }
                if (max <= 0) {
                    showLayerTips('error', '每次最大获取数量 至少1条', 't');
                    return false;
                }
                return true;
            }
        };

        $(function () {
            $(".deve-basic-heard").click(function(){
                if($(this).parent().hasClass("active")){
                    $(this).parent().removeClass("active");
                }else{
                    $(this).parent().siblings().removeClass("active");
                    $(this).parent().addClass("active");
                }
            });

            $('.choice-allcheck input').click(function () {
                var id = $(this).data('id');
                $('.check-' + id).prop('checked', $(this).is(':checked'));
            });
            
            //审核通过
            $('.apply-pass').click(function () {
                //
                var id = $(this).data('id');
                if ($('.check-' + id + ':checked').length === 0) {
                    showLayerTips('warn', '请至少选择一个字段', 't');
                    return;
                }
                if (!appInterfaceValidator.validateMaxCount(id) || !appInterfaceValidator.validateMaxInvokeNumber(id)) {
                    return ;
                }

                var dataSetRuleId =  $(this).parents('.deveman-basic-body').find("#dataSetRuleId").val();
                $('#form-' + id).ajaxSubmit({
                    url: '${request.contextPath}/bigdata/developer/interface-apply/pass?dataSetRuleId='+ dataSetRuleId,
                    type: 'POST',
                    success: function (res) {
                        if (res.success){
                            showLayerTips('success', '审核成功', 't', function () {
                                remove(id);
                            });
                        } else {
                            showLayerTips('error', '操作失败', 't');
                        }
                    }
                });
            });

            $('.apply-unpass').click(function () {
                var id = $(this).data('id');
                $.post('${request.contextPath}/bigdata/developer/interface-apply/un-pass?applyId='+id, function (res) {
                    if (res.success) {
                        showLayerTips('success', '操作成功', 't', function () {
                            remove(id);
                        })
                    } else {
                        showLayerTips('error', '操作失败', 't')
                    }
                })
            });

            $('.apply-delete').click(function () {
                var id = $(this).data('id');
                showConfirmTips('warn', '提示', '删除操作不可恢复，您是否要删除该数据？',  function () {
                    $.post('${request.contextPath}/bigdata/developer/interface-apply/delete?applyId='+id, function (res) {
                        if (res.success) {
                            showLayerTips('success', '操作成功', 't', function () {
                                remove(id);
                            })
                        } else {
                            showLayerTips('error', '操作失败', 't')
                        }
                    })

                })
            });

            $('.apply-modify').click(function () {
                var id = $(this).data('id');
                if (!appInterfaceValidator.validateMaxCount(id) || !appInterfaceValidator.validateMaxInvokeNumber(id)) {
                    return ;
                }
                var dataSetRuleId =  $(this).parents('.deveman-basic-body').find("#dataSetRuleId").val();
                $('#form-' + id).ajaxSubmit({
                    url: '${request.contextPath}/bigdata/developer/interface-apply/modify-limit?dataSetRuleId='+ dataSetRuleId,
                    type: 'POST',
                    success: function (res) {
                        if (res.success){
                            showLayerTips('success', '更新成功', 't');
                        } else {
                            showLayerTips('error', '更新失败', 't');
                        }
                    }
                });
            })
        });

        function remove(id) {
            $('#form-'+id).remove();
            if ($('.form-interface').length === 0) {
                $('#no-apply-data').show();
            }
        }
    </script>
    <div class="col-md-6 " style="display: none;" id="no-apply-data">
        <div class="no-data-common">
            <div class="text-center">
                <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                <p class="color-999">暂无数据</p>
            </div>
        </div>
    </div>
<#else >
    <div class="col-md-6">
        <div class="no-data-common">
            <div class="text-center">
                <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                <p class="color-999">暂无数据</p>
            </div>
        </div>
    </div>
</#if>

