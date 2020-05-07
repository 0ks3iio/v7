<#if itemList?exists && (itemList?size gt 0)>
<form method="post" id="scaleForm">
    <div class="evaluate-item clearfix mt10">
        <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">评价类型得分占比：</span>
        <div class="evaluate-item-right">
            <div class="evaluate-item mb10 point-label-tip typeDiv sumnum1">
                <i class="sz-icon icontishi"></i>占比总和应为100%，当前为<span id="sumnum1">0</span>%
            </div>
            <div class="point-pro-item clearfix">
                <div class="point-proitem-left">日常：</div>
                <div class="point-proitem-right nopadding-td">
                    <div class="point-td-input">
                              <#if dailySacle?exists>
<#--                                  <div class="form-shuzi form-shuzi-lg" style="width: 120px;">
                                      <input type="number"  id="test0" vtype="int"  max="99" min="0" name="stutotalityScaleList[0].scale" class="form-control evaluate-control pointnum1"
                                             value="${dailySacle.scale!}"  style="width: 120px;" placeholder="请输入"/>
                                      <input type="hidden" name="stutotalityScaleList[0].type"  value="2" />
                                      <input type="hidden" name="stutotalityScaleList[0].id"  value="${dailySacle.id!}" />
                                      <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                          <i class="fa fa-angle-up"></i>
                                      </div>
                                      <div  class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                          <i class="fa fa-angle-down"></i>
                                      </div>
                                  </div>-->

                                  <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                                      <input type="number" name="stutotalityScaleList[0].scale"  id="test0"   max="99" min="0"

                                             value="${dailySacle.scale!?string('0.0')}"
                                             class="form-control evaluate-control pointnum1"   numtype=""/>
                                      <input type="hidden" name="stutotalityScaleList[0].type"  value="2" />
                                      <input type="hidden" name="stutotalityScaleList[0].id"  value="${dailySacle.id!}" />
                                      <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                      <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                          <i class="fa fa-angle-up"></i>
                                      </div>
                                      <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                          <i class="fa fa-angle-down"></i>
                                      </div>
                                  </div>
                                <#else>
                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                                    <input type="number" name="stutotalityScaleList[0].scale"   id="test0"   class="form-control evaluate-control pointnum1"   numtype=""   max="99" min="0"   value="30"/>
                                    <input type="hidden" name="stutotalityScaleList[0].type"  value="2" />
                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                        <i class="fa fa-angle-up"></i>
                                    </div>
                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                        <i class="fa fa-angle-down"></i>
                                    </div>
                                </div>
                            </#if>
                    </div>
                    %
                </div>
            </div>
            <div class="point-pro-item clearfix">
                <div class="point-proitem-left">期末：</div>
                <div class="point-proitem-right nopadding-td">
                    <div class="point-td-input">
                        <#if termScale?exists>
<#--                            <div class="form-shuzi form-shuzi-lg" style="width: 120px;">
                                <input type="number" id="test1"  vtype="int"  max="99" min="0" name="stutotalityScaleList[1].scale"  class="form-control evaluate-control pointnum1" value="${termScale.scale!}" style="width: 120px;" placeholder="请输入"/>
                                <input type="hidden" name="stutotalityScaleList[1].type"  value="3" />
                                <input type="hidden" name="stutotalityScaleList[1].id"  value="${termScale.id!}" />
                                <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                    <i class="fa fa-angle-up"></i>
                                </div>
                                <div  class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                    <i class="fa fa-angle-down"></i>
                                </div>
                            </div>-->
                            <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                                <input type="number" name="stutotalityScaleList[1].scale"   id="test1"   max="99" min="0"    value="${termScale.scale!?string('0.0')}" class="form-control evaluate-control pointnum1" numtype=""/>
                                <input type="hidden" name="stutotalityScaleList[1].type"  value="3" />
                                <input type="hidden" name="stutotalityScaleList[1].id"  value="${termScale.id!}" />
                                <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                    <i class="fa fa-angle-up"></i>
                                </div>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                    <i class="fa fa-angle-down"></i>
                                </div>
                            </div>
                        <#else>
<#--                            <div class="form-shuzi form-shuzi-lg" style="width: 120px;">
                            <input type="number" name="stutotalityScaleList[1].scale"   vtype="int"  id="test1" max="99" min="0" class="form-control evaluate-control pointnum1" value="70" style="width: 120px;" placeholder="请输入"/>
                            <input type="hidden" name="stutotalityScaleList[1].type"  value="3" />
                            <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                <i class="fa fa-angle-up"></i>
                            </div>
                            <div  class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                <i class="fa fa-angle-down"></i>
                            </div>
                        </div>-->
                            <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                                <input type="number" name="stutotalityScaleList[1].scale"    id="test1"   max="99" min="0"  class="form-control evaluate-control pointnum1" numtype="" value="70"/>
                                <input type="hidden" name="stutotalityScaleList[1].type"  value="3" />
                                <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                    <i class="fa fa-angle-up"></i>
                                </div>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                    <i class="fa fa-angle-down"></i>
                                </div>
                            </div>

                        </#if>
                    </div>%
                </div>
            </div>
        </div>
    </div>
    <div class="evaluate-item clearfix" style="font-size: 0;">
        <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">各项目得分占比：</span>
        <div class="evaluate-item-right point-probox-right">
            <div class="evaluate-item mb10 point-label-tip sumnum2">
                <i class="sz-icon icontishi"></i>占比总和应为100%，当前为<span id="sumnum2">0</span>%
            </div>
            <#assign i = 2>
            <#list itemList  as item>
                <div class="point-pro-item clearfix">
                    <div class="point-proitem-left">${item.itemName!}：</div>
                    <div class="point-proitem-right nopadding-td">
                        <div class="point-td-input">
                            <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                                <input type="number"  id="test${i!}" name="stutotalityScaleList[${item_index+2}].scale"  step="0.1"   max="99" min="0"
                                        <#if item.scale?exists&& item.scale != 0>
                                            value="${item.scale?string('0.0')}"
                                            <#else >
                                                value=""
                                        </#if>
                                       class="form-control evaluate-control pointnum2" numtype=""/>
                                <input type="hidden"  name="stutotalityScaleList[${item_index+2}].itemId" value="${item.id!}">
                                <input type="hidden"  name="stutotalityScaleList[${item_index+2}].type" value="1">
                                <input type="hidden"  name="stutotalityScaleList[${item_index+2}].id" value="${item.scaleId!}">
                                <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                    <i class="fa fa-angle-up"></i>
                                </div>
                                <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                    <i class="fa fa-angle-down"></i>
                                </div>
                            </div>
                        </div>
                        %
                    </div>
                </div>
                <#assign i = i+1>
            </#list>
        </div>
    </div>
    <div class="evaluate-item clearfix mt10">
        <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;width: 140px;"></span>
        <div class="evaluate-item-right">
            <div class="btn btn-blue font-14" onclick="saveNewScale()">保存</div>
            <div class="btn btn-default font-14" onclick="acadyearSearch()">取消</div>
        </div>
    </div>
</form>
<#else >
    <div class="eva-no-content">
        <img src="/static/images/evaluate/nocontent.png">
        <div class="mb10">请先维护该年级的评价项目设置~</div>
    </div>
</#if>

 <script type="text/javascript">
    $(function() {
        //包含小数点数字输入框
        $('.form-shuzi input.form-control').keyup(function(){
            var num = /^\d+(\.\d{0,1})?$/;
            var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
            if (max === undefined) {
                max = 999;
            } else{
                max = parseFloat(max);
            }
            if($(this).attr("numtype") == "int") {
                num = /^\d*$/;
            }else{
                num = /^\d+(\.\d{0,1})?$/;
            }
            if(!num.test($(this).val())){
                $(this).val('');
                $(this).change();
            };
            if($(this).val() > max) {
                $(this).val(max);
                $(this).change();
            }
        });
        $('.form-shuzi > .btn').click(function(e){
            e.preventDefault();
            var $num = $(this).siblings('input.form-control');
            var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
            var val = $num.val();
            if (!val ) val = 0;
            var num = parseFloat(val);
            var step = $num.parent('.form-shuzi').attr('data-step');
            if (step === undefined) {
                step = 1;
            } else{
                step = parseFloat(step);
            }
            if ($(this).hasClass('form-shuzi-add')) {
                num = parseFloat(numAdd(num,step));
            } else{
                num = parseFloat(numSub(num,step));
                if (num <= 0) num = 0;
            }
            if(num > max) {
                $num.val(max);
                $num.change();
            }else{
                $num.val(num);
                $num.change();
            }
        });
        $(".form-shuzi").find("input.form-control").bind("input propertychange", function(event) {
            if($(this).val() == ""){
                $(this).siblings(".evaluate-shuzi-close").removeClass("active");
            }else{
                $(this).siblings(".evaluate-shuzi-close").addClass("active");
            }
        });
        $(".form-shuzi").find("input.form-control").on("change", function(event) {
            if($(this).val() == ""){
                $(this).siblings(".evaluate-shuzi-close").removeClass("active");
            }else{
                $(this).siblings(".evaluate-shuzi-close").addClass("active");
            }
        });
        $(".form-shuzi").find("input.form-control").focus(function() {
            if($(this).val() == ""){
                $(this).siblings(".evaluate-shuzi-close").removeClass("active");
            }else{
                $(this).siblings(".evaluate-shuzi-close").addClass("active");
            }
        })

     scaleShow();
     suanzhi();  //初始算值
     leftresize(); //js样式
    $(".point-probox-right").find(".point-pro-item").each(function(i, e) {if (i > 1) {$(e).css("border-top", "0");}});
    $(".picker-table").find(".outter a").click(function() {
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");
    });
        //输入数字
        $(".pointnum1").bind("input propertychange", function(event) {
            var sunmax = 0;
            var num = $(this).val() ? $(this).val() : 0;
            var minnum = parseFloat(num);
            var twonum = parseFloat(numSub(100,minnum));
            $(this).parents(".point-pro-item").siblings(".point-pro-item").find(".pointnum1").val(twonum);
            $(".pointnum1").each(function(i, e) {
                if (!$(e).val()) {
                    sunmax += 0;
                } else {
                    sunmax += parseFloat($(e).val());
                }
            });
            $("#sumnum1").html(sunmax);
        });
        $(".pointnum1").on("change", function(event) {
            var sunmax = 0;
            var num = $(this).val() ? $(this).val() : 0;
            var minnum = parseFloat(num);
            var twonum = parseFloat(numSub(100,minnum));
            $(this).parents(".point-pro-item").siblings(".point-pro-item").find(".pointnum1").val(twonum);
            $(".pointnum1").each(function(i, e) {
                if (!$(e).val()) {
                    sunmax += 0;
                } else {
                    sunmax += parseFloat($(e).val());
                }
            });
            $("#sumnum1").html(sunmax);
        });

        $(".pointnum2").bind("input propertychange", function(event) {
            var sunmax = 0;
            $(".pointnum2").each(function(i, e) {
                if (!$(e).val()) {
                    sunmax += 0;
                } else {
                    sunmax += parseFloat($(e).val());
                }
            });
            $("#sumnum2").html(sunmax);
        });

        $(".pointnum2").on("change", function(event) {
            var sunmax = 0;
            $(".pointnum2").each(function(i, e) {
                if (!$(e).val()) {
                    sunmax += 0;
                } else {
                    sunmax += parseFloat($(e).val());
                }
            });
            $("#sumnum2").html(sunmax);
        });

    });

    //规则展示
      function scaleShow() {
        var url = '${request.contextPath}/stutotality/rule/show';
        $('#ScaleBox'). load(url);
    }

    //新建规则
      function newScale() {
        var url = '${request.contextPath}/stutotality/rule/add/index';
        $('#addScaleDiv'). load(url);
    }

    //保存新建规则
    function saveNewScale() {
        var num2 = $("#sumnum2").text()
        var acadyear = $("#acadyear").val()
        var semester = $("#semester").val()
        var gradeId = $(".scaleDiv").find("span.active").attr("data-id")
        var check = checkValue("#scaleForm")
        if (!check) {return false;}
        var num1 = $("#sumnum1").text()
        var num2 = $("#sumnum2").text()
        if ( num1 !=100 ){
            layer.tips('占比总和应为100%', $("#sumnum1"), {
                time:1000,
                tipsMore: true,
                tips: 2
            });
            return;
        }
        if (num2 !=100 ){
            layer.tips('占比总和应为100%', $("#sumnum2"), {
                time:1000,
                tipsMore: true,
                tips: 2
            });
            return;
        }
        var options = {
            url: "${request.contextPath}/stutotality/rule/add?acadyear=" + acadyear + "&semester=" + semester + "&gradeId=" + gradeId,
            success: function (data) {
                var jsonO=JSON.parse(data);
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    layer.closeAll();
                    layer.msg("保存成功", {offset: 't', time: 2000});
                    acadyearSearch();
                }
            },
        }
        $('#scaleForm').ajaxSubmit(options);
    }


    function leftresize() {
        var onedom = null;
        $(".point-probox-right").find(".point-pro-item").each(function(i, v) {
                if (i % 2 == 0) {onedom = v;}
                else {
                    if ($(v).height() > $(onedom).height()) {$(onedom).height($(v).height());}
                    else {$(v).height($(onedom).height());}
                }});
        $(".point-probox-basic").find(".point-pro-item").each(function(i, v) {
                if (i % 2 == 0) {onedom = v;}
                else {if ($(v).height() > $(onedom).height()) {
                    $(onedom).height($(v).height())}
                    else {$(v).height($(onedom).height());}
                }
            });
        $(".point-proitem-right").each(function(i, v) {
            $(v).css("height", $(v).siblings(".point-proitem-left").height() + 20);
        });
    }
    //初始化计算比例
        function suanzhi() {
        var sunmax = 0;
        $(".pointnum1").each(function(i, e) {
            if (!$(e).val()) {sunmax += 0;} else {sunmax += parseFloat($(e).val());}});
        $("#sumnum1").html(sunmax);var sunmax = 0;
        $(".pointnum2").each(function(i, e) {if (!$(e).val()) {sunmax += 0;} else {sunmax += parseFloat($(e).val());}});
        $("#sumnum2").html(sunmax);
       }

</script>