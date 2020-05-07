<div class="padding-20 js-scroll-height js-detail">
	<#if type! =="frame">
		 <div class="filter-made mb-10">
			<div class="filter-item">
	        	<div class="form-group">
	        		<label for="c2">是否启用:</label>
	                <label class="switch" for="status">
	                    <input type="checkbox" id="status" name="switch-field-1" <#if status?default(0) == 1 >checked="checked"</#if> onchange="changeStatus(this)"/>
	                    <span class="switch-name">显示</span>
	                </label>
	        	</div>
			</div>
			<#if status?default(0) == 1 >
			<#if code! == "es" || code! =="kylin"|| code! =="kafka">
	          	<div align="right"><h4 style="color:red;">${remark!}</h4></div>
	    	</#if>
	    	</#if>
		</div>	
	</#if>
    <table class="tables">
        <thead>
        <tr>
            <th>名称</th>
            <th>key</th>
            <th>value</th>
            <th width="70px;">排序号</th>
            <th>备注</th>
            <th width="50px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list paramList as param>
        <tr>
            <td>${param.paramName!}</td>
            <td>${param.paramKey!}</td>
            <td>${param.paramValue!}</td>
            <td>${param.orderId!}</td>
            <td title="${param.remark!?html}"><#if param.remark! !="" && param.remark?length gt 30>${param.remark?substring(0, 30)}......<#else>${param.remark!}</#if> </td>
            <td>
                <a href="javascript:void(0);" class="look-over js-editParam" paramId="${param.id!}" paramName="${param.paramName!}">编辑</a>
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
<div class="layer layer-editParam layui-layer-wrap" style="display: none;">
    <div class="layer-content">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">key：</label>
                <div class="col-sm-8">
                    <input type="text" id="paramKey" class="form-control" maxlength="50" readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">value：</label>
                <div class="col-sm-8">
                    <input type="text" id="paramValue" class="form-control" nullable="false" maxlength="250">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">排序号：</label>
                <div class="col-sm-8">
                    <input type="text" id="orderId" class="form-control" nullable="true" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="3">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">备注：</label>
                <div class="col-sm-8">
                    <textarea id="remark" maxlength="200" class="form-control"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        $('.js-editParam').on('click', function(){
            var paramId = $(this).attr('paramId');
            var paramName = $(this).attr('paramName');

            $.ajax({
                url: '${request.contextPath}/bigdata/setting/option/editParam',
                type: 'POST',
                data: {frameParamId:paramId},
                dataType: 'json',
                success: function (response) {
                    var param = response.data;
                    $('#paramKey').val(param.paramKey);
                    $('#paramValue').val(param.paramValue);
                    $('#orderId').val(param.orderId);
                    $('#remark').text(param.remark);
                }
            });
            var isSubmit = false;
            layer.open({
                type: 1,
                shade: 0.5,
                title: paramName,
                area: '500px',
                btn: ['保存', '取消'],
                yes:function(index, layero){
                    if (isSubmit) {
                        return;
                    }
                    isSubmit = true;

                    if ($('#paramKey').val() == "") {
                        layer.tips("不能为空", "#paramKey", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    if ($('#paramValue').val() == "") {
                        layer.tips("不能为空", "#paramValue", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    if ($('#orderId').val() == "") {
                        layer.tips("不能为空", "#orderId", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    $.ajax({
                        url: '${request.contextPath}/bigdata/setting/option/saveParam',
                        type: 'POST',
                        data: {
                            id:paramId,
                            paramName:paramName,
                            paramKey:$('#paramKey').val(),
                            paramValue:$('#paramValue').val(),
                            orderId:$('#orderId').val(),
                            remark:$('#remark').val()
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (!data.success) {
                            	showLayerTips4Confirm('error',data.message);
                                isSubmit = false;
                            } else {
                            	showLayerTips('success','保存成功!','t');
                                layer.close(index);
                                $('.bg-DEEBFC').children().first().trigger("click");
                            }
                        }
                    });
                },
                content: $('.layer-editParam')
            })
        })
    })
    
    function resetFrame(code){	
    	     $.ajax({
                url: '${request.contextPath}/bigdata/setting/option/resetFrame',
                type: 'POST',
                data: {code:code},
                dataType: 'json',
                success: function (data) {
                	layer.closeAll();
                    if(!data.success){
			 			showLayerTips4Confirm('error',data.message);
			 		}else{
			 		   showLayerTips('success',data.message,'t');
	    			}
                }
            });
	}
    
</script>