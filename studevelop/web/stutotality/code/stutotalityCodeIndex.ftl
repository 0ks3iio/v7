<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<div class="row">
    <form id="codeFormId">
        <div class="col-xs-12">
            <div class="box box-default">
                <div class="box-body clearfix" style="padding-top: 0;">
                    <div class="point-pro-box">
                        <div class="evaluate-item clearfix mt10">
                            <span class="layer-evaluate-label evaluate-item-left point-box-title">生成设置</span>
                            <div class="evaluate-item-right">
                              <span class="evacode-title-tip"><i class="sz-icon iconchakan font-12"></i>查看二维码样例</span>
                            </div>
                        </div>
                        <div class="evaluate-item clearfix">
                            <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">课程：</span>
                            <div class="evaluate-item-right" id="itemDivId" style="width: calc(100% - 150px);">
                                <#if itemList?exists && itemList?size gt 0>
                                    <#list  itemList as item>
                                    <span class="evaluate-select-item item <#if item_index==0>active</#if>" data-id="${item.id!}" data-name="${item.shortName!}">${item.itemName!}</span>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="evaluate-item clearfix mt10">
                            <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">课程缩写：</span>
                            <div class="evaluate-item-right">
                                <input name="name" id="itemNameId" type="text" nullable="false" class="form-control evaluate-control" value="" style="width: 312px;"  placeholder="请输入"/>
                            </div>
                        </div>
                        <div class="evaluate-item clearfix mt10">
                            <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">二维码数：</span>
                                <div class="evaluate-item-right" style="width: 312px;">
                                    <table class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>分值</th>
                                            <th>个数<span class="evacode-th-tip">(不打印不需要输入)</span></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>5</td>
                                        <td>
                                            <div class="point-td-input">
                                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="50"  data-max="1000">
                                                    <input type="text" vtype="int" id="number5" name="number5" min="1" class="form-control evaluate-control" style="height: 36px;"
                                                            numtype="" placeholder="最大1000"/>
                                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                        <i class="fa fa-angle-up"></i>
                                                    </div>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                        <i class="fa fa-angle-down"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>4</td>
                                        <td>
                                            <div class="point-td-input">
                                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="50" data-max="1000">
                                                    <input type="text" vtype="int" id="number4" name="number4" min="1"  class="form-control evaluate-control" style="height: 36px;"
                                                           numtype="" placeholder="最大1000"/>
                                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                        <i class="fa fa-angle-up"></i>
                                                    </div>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                        <i class="fa fa-angle-down"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td>
                                            <div class="point-td-input">
                                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="50" data-max="1000">
                                                    <input type="text" vtype="int" id="number3" name="number3" min="1"  class="form-control evaluate-control" style="height: 36px;"
                                                           numtype="" placeholder="最大1000"/>
                                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                        <i class="fa fa-angle-up"></i>
                                                    </div>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                        <i class="fa fa-angle-down"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>
                                            <div class="point-td-input">
                                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="50" data-max="1000">
                                                    <input type="text" vtype="int" id="number2" name="number2" min="1"  class="form-control evaluate-control" style="height: 36px;"
                                                           numtype="" placeholder="最大1000"/>
                                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                        <i class="fa fa-angle-up"></i>
                                                    </div>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                        <i class="fa fa-angle-down"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>1</td>
                                        <td>
                                            <div class="point-td-input">
                                                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="50" data-max="1000" >
                                                    <input type="text" vtype="int" id="number1" name="number1" min="1"  class="form-control evaluate-control" style="height: 36px;"
                                                           numtype="" placeholder="最大1000"/>
                                                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                        <i class="fa fa-angle-up"></i>
                                                    </div>
                                                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                        <i class="fa fa-angle-down"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="evaluate-item clearfix mt10">
                            <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">打印列数：</span>
                            <div class="evaluate-item-right">
                                <div class="evacode-item-input">
                                    <div class="point-td-input">
                                        <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="10">
                                            <input type="text" vtype="int" id="columnId" name="column" min="1" class="form-control evaluate-control" style="height: 36px;"
                                                   numtype="" value="10" nullable="false"/>
                                            <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)"></i>
                                            <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                                <i class="fa fa-angle-up"></i>
                                            </div>
                                            <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                                <i class="fa fa-angle-down"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <span>列</span>
                                <div class="mb10 point-label-tip">
                                    <i class="sz-icon icontishi"></i>最多为10列(默认10列)
                                </div>
                            </div>
                        </div>
                        <div class="evaluate-item clearfix mt10">
                            <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;width: 140px;"></span>
                            <div class="evaluate-item-right">
                                <button  type="button" class="btn btn-blue font-14" onclick="exportCode()">
                                    开始导出
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
         </div>
    </form>
</div>
<!-- 标准设置 -->
<div class="layer layer-norm-code">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
            <div class="evacode-layer-item"><img src="${request.contextPath}/static/images/evaluate/code.png"></div>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-blue font-14" onclick="hidelayer()" >
                知道了
            </button>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        <#if itemList?exists && itemList?size gt 0>
            $("#itemNameId").val('${itemList[0].shortName!}');
        </#if>
        //显示示例
        $(".evacode-title-tip").click(function() {
            layer.open({
                type: 1,
                shadow: 0.5,
                title: "二维码样例",
                area: "595px",
                content: $(".layer-norm-code")
            });
        });
        //切换项目s
        $(".evaluate-select-item.item").on("click", function() {
            $(this).siblings(".evaluate-select-item").removeClass("active");
            $(this).addClass("active");
            var shortName=$(this).attr("data-name");
            if(shortName){
                $("#itemNameId").val(shortName);
            }else{
                $("#itemNameId").val("");
            }
        });
        //包含小数点数字输入框
        $('.form-shuzi input.form-control').keyup(function(){
            var num = /^\d+(\.\d{0,2})?$/;
            var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
            if (max === undefined) {
                max = 1000;
            } else{
                max = parseFloat(max);
            }
            if($(this).attr("numtype") == "int") {
                num = /^\d*$/;
            }else{
                num = /^\d+(\.\d{0,2})?$/;
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
    });

    $(window).resize(function() {
        $(".point-pro-box").css("min-height", $("#sidebar").height() - 120);
    });

    function exportCode(){

        if(!($(".evaluate-select-item.item.active") && $(".evaluate-select-item.item.active").attr("data-id"))){
            layerTipMsg(false,"提示","课程为空,不能导出！");
            return;
        }
        var check = checkValue('#codeFormId');
        if(!check){
            return;
        }
        var itemNameId=$("#itemNameId").val();
        if(itemNameId && itemNameId.length>1){
            layer.tips('课程缩写只能一位!', $("#itemNameId"), {
                time:1000,
                tipsMore: true,
                tips: 2
            });
            return;
        }
        var number1=$("#number1").val();
        var number2=$("#number2").val();
        var number3=$("#number3").val();
        var number4=$("#number4").val();
        var number5=$("#number5").val();
        if(!number1 && !number2 && !number3 && !number4 && !number5){
            layer.tips('个数不能全为空!', $("#number5"), {
                time:1000,
                tipsMore: true,
                tips: 2
            });
            return;
        }
        var itemId=$(".evaluate-select-item.item.active").attr("data-id");
        var gradeId=$(".evaluate-select-item.grade.active").attr("data-id");
        location.href="${request.contextPath}/stutotality/code/export?itemId="+itemId+"&gradeId="+gradeId+"&"+$("#codeFormId").serialize();
    }

</script>
<#--
    //切换年级
    $(".evaluate-select-item.grade").on("click", function() {
    $(this).siblings(".evaluate-select-item").removeClass("active");
    $(this).addClass("active");
    var gradeId=$(".evaluate-select-item.grade.active").attr("data-id");
    $.ajax({
    url:"${request.contextPath}/stutotality/code/getItemList",
    data:{"gradeId":gradeId},
    type:'post',
    success:function(data) {
    var htmlClass=$("#itemDivId");
    htmlClass.html("");
    var html="";
    if(data){
    for(var i=0;i<data.length;i++){
    html+='<span class="evaluate-select-item item ';
                                if(i==0){
                                    html+=' active ';
                                }
                                html+='" data-id="'+data[i].id+'">'+data[i].itemName+'</span>';
    }
    htmlClass.html(html);
    $(".evaluate-select-item.item").on("click", function() {
    $(this)
    .siblings(".evaluate-select-item")
    .removeClass("active");
    $(this).addClass("active");
    });
    }
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
    layer.msg(XMLHttpRequest.status);
    }
    });

    });-->
