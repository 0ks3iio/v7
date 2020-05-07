<!-- 桌面首页 -->
<#assign USER_LAYOUT_TWO2ONE =stack.findValue("net.zdsoft.desktop.entity.UserSet@LAYOUT_TWO2ONE")>
<#-- 布局方式1 2:1 -->
<#if layout?default('') == USER_LAYOUT_TWO2ONE>
<div class="col-sm-8 no-padding">
	<@loopFunctionAreaUser functionAreaUsers=twoFunctionAreaUsers callBack="bindFunction" />
</div>
<div class="col-sm-4 no-padding">
    <@loopFunctionAreaUser functionAreaUsers=oneFunctionAreaUsers callBack="bindFunction" />
</div>
<#else>
    <@loopFunctionAreaUser functionAreaUsers=functionAreas callBack="bindFunction" />
</#if>
<script>
    var functionAreaSetDOM = $("#box-tool-template");
    function bindFunction(functionAreaUserId,functionAreaIndex) {
        <#if functionSet?default(false)>
        $("#"+functionAreaUserId +" .box-body").after(functionAreaSetDOM.html());
        $("#"+functionAreaUserId +" .box-tool-template").removeClass("box-tool-template");
        $("#"+functionAreaUserId + " .js-box-remove").unbind("click").bind("click",function(){
            var that=$(this);
            layer.confirm('确定删除该功能区吗？',{
                title:['提示','font-size:16px;'],
                btn:['确定','取消'],
                yes: function(index){
                    $.ajax({
                        url:"${request.contextPath}/desktop/app/delete/functionAreaUserSet?functionAreaUserId="+functionAreaUserId+"&functionAreaIndex="+functionAreaIndex,
                        data:{},
                        dataType:'json',
                        contentType:'application/json',
                        type:'GET',
                        success:function (data) {
                            if(data.success){
                                that.closest('.box').remove();
                               // showSuccessMsg(data.msg);
                            }else{
                                showErrorMsg(data.msg);
                            }
                        }
                    });
                    layer.close(index);
                }
            })
        });
        $("#"+functionAreaUserId + " .js-box-setting").unbind("click").bind("click",function(){
            $(".layer-boxSetting").load("${request.contextPath}/desktop/app/showFunctionAreaUserSet/?layout=${layout!}&functionAreaUserId="+functionAreaUserId+"&functionAreaIndex="+functionAreaIndex+"&"+new Date().getTime(),function(){
                layer.open({
                    type: 1,
                    shade: .5,
                    title: ['设置','font-size:16px'],
                    area: ['940px','720px'],
                    maxmin: true,
                    btn:['确定','取消'],
                    content: $('.layer-boxSetting'),
                    resize:true,
                    yes:function (index) {
                        if(!deskTopCheckVal(".layer-boxSetting")) {
                            return ;
                        }
                        $.ajax({
                            url:"${request.contextPath}/desktop/app/save/functionAreaUserSet",
                            data:dealDValue(".layer-boxSetting"),
                            dataType:'json',
                            contentType: "application/json",
                            type:'post',
                            success:function (data) {
                                if(data.success){
                                    showErrorMsgWithCall(data.msg,goHome);
                                }else{
                                    showErrorMsg(data.msg);
                                }
                            }
                        })
                    },
                    btn2:function (index) {
                        layer.closeAll();
                    }
                });
                $(".layer-boxSetting").parent().css('overflow','auto');
            })
        });
        </#if>
    }
</script>


<#-- 遍历功能区模板 -->
<#macro loopFunctionAreaUser functionAreaUsers=[] callBack="" >
    <#if functionAreaUsers?exists && functionAreaUsers?size gt 0>
        <#list functionAreaUsers as fa>
        <div id="${fa.id}" />
        </#list>
    <script>
        $(document).ready(function () {
            <#list functionAreaUsers as fa>
                $("#${fa.id}").load("${request.contextPath}/desktop/app/showFunctionArea?functionAreaUserId=${fa.id}",function(){loadCallBack('${fa.id!}','${fa_index+1}')});
            </#list>
        });
        function loadCallBack(functionAreaUserId,index) {
            <#if callBack?default('') != ''>
                ${callBack}(functionAreaUserId,index);
            </#if>
        }
    </script>
    </#if>
</#macro>
<#--  -->
<div id="box-tool-template" style="display: none;">
    <div class="box-tools dropdown">
        <a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
        <ul class="box-tools-menu dropdown-menu">
            <span class="box-tools-menu-angle"></span>
            <li><a class="js-box-setting" href="javascript:void(0);">设置</a></li>
            <li><a class="js-box-remove" href="javascript:void(0);">删除</a></li>
        </ul>
    </div>
</div>