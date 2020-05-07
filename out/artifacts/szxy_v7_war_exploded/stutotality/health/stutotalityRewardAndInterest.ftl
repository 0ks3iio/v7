<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<div class="evaluate-body col-xs-12" id="divTable">
<form id="myForm">
    <div class="pro-other-box" >
        <div class="evaluate-you-title">全优生设置</div>
        <div class="evaluate-item clearfix">
            <span class="layer-evaluate-label evaluate-item-left">全优生占比：</span>
            <div class="evaluate-item-right eva-pro-item">
                <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="100">
                    <input type="number" id="scale"  nullable="false"  max="99" min="0" class="form-control evaluate-control pointnum2"  numtype=""
                        <#if scale?exists>
                            value="${scale!?string('0.0')}"
                            <#else >
                                value=""
                        </#if>

                    />
                    <i class="wpfont icon-close-round-fill evaluate-shuzi-close"
                       onclick="closenuminput(this)"></i>
                    <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                        <i class="fa fa-angle-up"></i>
                    </div>
                    <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                        <i class="fa fa-angle-down"></i>
                    </div>
                </div> %
            </div>
        </div>
        <div class="evaluate-item clearfix">
            <span class="layer-evaluate-label evaluate-item-left">需达标科目：</span>
            <div class="evaluate-item-right eva-pro-item">
                <#if socreItemList ?exists && socreItemList?size gt 0>
                    <select  id="subjectId" data-id="1"   class="form-control evaluate-control" style="width: 200px;">
                        <#if subjectId?exists>
                            <#list socreItemList as items>
                                <option  name = "stutotality.name"  <#if subjectId ==  items.subjectId>selected</#if> data-id="${items.subjectId}" value="${items.id}">${items.itemName}</option>
                            </#list>
                            <#else >
                                <#list socreItemList as items>
                                    <option  name = "stutotality.name"  data-id="${items.subjectId}" value="${items.id}">${items.itemName}</option>
                                </#list>
                        </#if>
                    </select>
                    <#else >
                     <select  id="subjectId" data-id="2" class="form-control evaluate-control" style="width: 200px;">
                         <option value="">暂无评价项科目数据</option>
                     </select>
                </#if>

            </div>
        </div>
        <div class="evaluate-item clearfix">
            <span class="layer-evaluate-label evaluate-item-left">达标线：</span>
            <div class="evaluate-item-right eva-pro-item">
                <#if score?exists>
                <ul class="starul" data-score="${score!}" style="position: relative;top: 9px;">
                <#else >
                    <ul class="starul" data-score=4.0 style="position: relative;top: 9px;">
                </#if>
                    <li class="emptyStar">
                        <a href="javascript:;" data-index="1"></a><a href="javascript:;" data-index="2"></a>
                    </li>
                    <li class="emptyStar">
                        <a href="javascript:;" data-index="3"></a><a href="javascript:;" data-index="4"></a>
                    </li>
                    <li class="emptyStar">
                        <a href="javascript:;" data-index="5"></a><a href="javascript:;" data-index="6"></a>
                    </li>
                    <li class="emptyStar">
                        <a href="javascript:;" data-index="7"></a><a href="javascript:;" data-index="8"></a>
                    </li>
                    <li class="emptyStar">
                        <a href="javascript:;" data-index="9"></a><a href="javascript:;" data-index="10"></a>
                    </li>
                </ul>
            </div>
        </div>

        <div class="evaluate-you-title">其他项目</div>
        <div class="evaluate-item clearfix">
                <span class="layer-evaluate-label evaluate-item-left">获奖情况：</span>
            <div class="evaluate-item-right eva-pro-item rewardDiv">
               <#-- <#assign num = 0>-->
                <#if rewardList?exists && rewardList?size gt 0>
                    <#list rewardList as reward>
                        <div class="evaluate-item-min ">
                            <div class="addlayer-evaluate-input">
                                <input type="hidden" name="rewardList[${reward_index}].id" value="${reward.id!}"/>
                                <input type="text" id="rewardNameId" maxlength="10" nullable="false" data-class="form-control evaluate-control" name="rewardList[${reward_index!}].rewardName" value="${reward.rewardName!}" placeholder="请输入" class="form-control evaluate-control evaluate-layer-con"/>
                            </div>
                            <div class="addlayer-evaluate-input" style="width: 60px;">
                                <input type="number" vtype="int" max="3" min="1" id="rewardStartNumberId${reward_index}" name="rewardList[${reward_index}].starNumber" class="form-control evaluate-control evaluate-layer-num" value="${reward.starNumber!}"/>
                            </div>
                            <div class="addlayer-eva-addicon addlayer-eva-icon" onclick="addminpro(this)">
                                <i class="wpfont icon-plus"></i>
                            </div>
                            <div class="addlayer-eva-delicon addlayer-eva-icon">
                                <i class="sz-icon iconshanchu"></i>
                            </div>
                        </div>
                       <#-- <#assign num=num+1>-->
                    </#list>
                </#if>
            </div>
        </div>

        <div class="evaluate-item clearfix">
                <span class="layer-evaluate-label evaluate-item-left">兴趣特长：</span>
            <div class="evaluate-item-right">
                <div class="evaluate-item-min">
                    <div class="addlayer-evaluate-input">
                        <input type="text" id="interestId" maxlength="20" class="form-control evaluate-control evaluate-layer-con pro-other-name"
                               name="subjectType" value="" placeholder="按Enter键可快速添加"/>
                    </div>
                    <div class="btn btn-white font-14 pro-other-btn" onclick="addtag()">
                        添加
                    </div>
                </div>
            </div>
        </div>

        <div class="clearfix">
                <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;"></span>
            <div class="evaluate-item-right pro-other-itembox interestDiv">
                <#--<#assign ind = 0>-->
                    <#if interestList?exists && interestList?size gt 0>
                        <#list interestList as interest>
                            <span class="pro-other-item">
                                <input type="hidden" name="interestList[${interest_index}].itemName" value="${interest.itemName!}"/>
                                <span>${interest.itemName!}</span>
                                <i class="wpfont icon-close-round-fill"></i>
                            </span>
                           <#-- <#assign ind=ind+1>-->
                        </#list>
                    </#if>
            </div>

        </div>
        <div class="evaluate-item clearfix mt10">
                <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;"></span>
            <div class="evaluate-item-right">
                <div class="btn btn-blue font-14" onclick="save()">保存</div>
            </div>
        </div>
    </div>
</form>

</div>





<script type="text/javascript">

    //判断获奖情况多少
    function howmuchrow() {
        if($(".eva-pro-item").find(".evaluate-item-min").length >=3 ){
            $(".eva-pro-item").find(".addlayer-eva-addicon").css("display","none")
        }else{
            $(".eva-pro-item").find(".addlayer-eva-addicon").css("display","inline-block")
        }
    }

    //新增项目
    function addminpro(that) {
        var num = $(".rewardDiv div").length/5;
        $(that).parents(".evaluate-item-min").after('<div class="evaluate-item-min">'+
            '<div class="addlayer-evaluate-input">'+
            '<input type="text" id="rewardListRewardName" maxlength="10" nullable="false" name="rewardList['+ num+'].rewardName" class="form-control evaluate-control evaluate-layer-con" value="" placeholder="请输入"/></div>'+
            '<div class="addlayer-evaluate-input" style="width: 60px;">'+
            '<input type="number" id="rewardListStartNumber" max="3" min="1" name="rewardList['+ num+'].starNumber" class="form-control evaluate-control evaluate-layer-num" value="3"/></div>'+
            '<div class="addlayer-eva-addicon addlayer-eva-icon" onclick="addminpro(this)">'+
            '<i class="wpfont icon-plus"></i>'+
            '</div>'+
            '<div class="addlayer-eva-delicon addlayer-eva-icon">'+
            '<i class="sz-icon iconshanchu"></i>'+
            '</div></div>');

        renum();
        howmuchrow();
    }

    $(function() {

        //包含小数点数字输入框
        $('.form-shuzi input.form-control').keyup(function(){
            var num = /^\d+(\.\d{0,1})?$/;
            var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
            if (max === undefined) {max = 999;} else{max = parseFloat(max);}
            if($(this).attr("numtype") == "int") {num = /^\d*$/;}else{num = /^\d+(\.\d{0,1})?$/;}
            if(!num.test($(this).val())){$(this).val('');$(this).change();};
            if($(this).val() > max) {$(this).val(max);$(this).change();}
        });
        $('.form-shuzi > .btn').click(function(e){
            e.preventDefault();
            var $num = $(this).siblings('input.form-control');
            var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
            var val = $num.val();
            if (!val ) val = 0;
            var num = parseFloat(val);
            var step = $num.parent('.form-shuzi').attr('data-step');
            if (step === undefined) {step = 1;} else{step = parseFloat(step);}
            if ($(this).hasClass('form-shuzi-add')) {num = parseFloat(numAdd(num,step));} else{num = parseFloat(numSub(num,step));
                if (num <= 0) num = 0;}
            if(num > max) {$num.val(max);$num.change();}else{$num.val(num);$num.change();}
        });
        $(".form-shuzi").find("input.form-control").bind("input propertychange", function(event) {
            if($(this).val() == ""){$(this).siblings(".evaluate-shuzi-close").removeClass("active");
            }else{$(this).siblings(".evaluate-shuzi-close").addClass("active");}
        });
        $(".form-shuzi").find("input.form-control").on("change", function(event) {
            if($(this).val() == ""){$(this).siblings(".evaluate-shuzi-close").removeClass("active");
            }else{$(this).siblings(".evaluate-shuzi-close").addClass("active");}
        });
        $(".form-shuzi").find("input.form-control").focus(function() {
            if($(this).val() == ""){$(this).siblings(".evaluate-shuzi-close").removeClass("active");}else{
                $(this).siblings(".evaluate-shuzi-close").addClass("active");}
        })
        //打星
        initstar();
        $(".starul li a").on("click", function () {
            //判断是全星点还是半星点，修改当前标签的父标签li的class为对应的星星图像
            $(this).parents(".starul").attr("data-score", parseInt($(this).attr("data-index")) / 2);
            $(this).parents(".starul").siblings(".star-tip").html(parseInt($(this).attr("data-index")) / 2 + "星");
            if (parseInt($(this).attr("data-index")) % 2 == 1) {
                $(this).parent().attr("class", "halfStar");
            } else {
                $(this).parent().attr("class", "fullStar");
            }
            //对前方的星星进行处理，遍历前方的li使背景图均变为全星
            var prev = $(this).parent();
            for (
                var i = 0;
                i <= parseInt($(this).attr("data-index")) / 2 - 1;
                i++
            ) {
                prev.prev().attr("class", "fullStar");
                prev = prev.prev();
            }
            //对后方星星进行处理，遍历后面的li使背景图均变为空星
            var after = $(this).parent();
            for (
                var i = 0;
                i <= 5 - parseInt($(this).attr("data-index")) / 2 - 1;
                i++
            ) {
                after.next().attr("class", "emptyStar");
                after = after.next();
            }
        });

        renum();
        howmuchrow();
        //删除标签
        $("body").on("click", ".icon-close-round-fill", function() {
            $(this).parents(".pro-other-item").remove();
        });
        $(".pro-other-name").bind("keypress", function(event) {
            if (event.keyCode == "13") {
                addtag();
            }
        });
        //项目删除
        $("body").on("click", ".addlayer-eva-delicon", function() {
            $(this).parents(".evaluate-item-min").remove();
            renum();
            howmuchrow();
        });
    });

    // 新增弹窗数目判断
    function renum() {
        if ($(".eva-pro-item").find(".evaluate-item-min").length == 1) {
            $(".eva-pro-item")
                .find(".evaluate-item-min")
                .find(".addlayer-eva-delicon")
                .hide();
        } else {
            $(".eva-pro-item")
                .find(".evaluate-item-min")
                .find(".addlayer-eva-delicon")
                .css("display", "inline-block");
        }
    }

    //增加标签
    var length=${interestList?size};
    function addtag() {
        var tagname = $.trim($(".pro-other-name").val());
        if (tagname.length == 0) {
            layer.msg("标签内容不能为空", { time: 2000 });
        } else  {
            $(".pro-other-itembox").append('<span class="pro-other-item"><span>'+tagname+'</span><input type="hidden" name="interestList['+length+'].itemName" value="'+tagname+'"/> '+
                                '<i class="wpfont icon-close-round-fill"></i></span>');
            $(".pro-other-name").val("");
        }
        length++;
    }
    //保存
    function save() {
        var num =  $("#subjectId").attr("data-id")
        if(num == 2){
            layer.msg("请先维护需达标科目", {
                offset: 't',
                time: 2000
            });
            return false;
        }
        var check = checkValue("#myForm")
        if(!check){
            return false;
        }
        var scale = $("#scale").val()
        var score = $(".starul").attr("data-score")
        var options=$("#subjectId option:selected");
        var subjectId=options.attr("data-id");
        var itemName=options.text();
        var ii = layer.load();
        var options= {
            type:"post",
            dataType:"json",
            data:{"scale":scale,"stutotalityItem.itemName":itemName,"stutotalityItem.subjectId":subjectId,"score":score},
            url: "${request.contextPath}/stutotality/rewardRank/list",
            success: function (data) {
                //var jsonO=JSON.parse(data);
                var jsonO=data;
                //debugger;
                layer.close(ii);
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    // layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    var url = '${request.contextPath}/stutotality/rewardAndInterest/index';
                    $('#showTabList').load(url);
                }
            }
        }
        $('#myForm').ajaxSubmit(options);

    }
    //星星初始化
    function initstar() {
        $(".starul").each(function () {
            var score = $(this).attr("data-score") * 2;
            $(this)
                .siblings(".star-tip")
                .html($(this).attr("data-score") + "星");
            $(this)
                .find("li")
                .each(function (i, e) {
                    if (2 * i + 2 <= score) {
                        $(e).attr("class", "fullStar");
                    } else if (2 * i + 1 <= score) {
                        $(e).attr("class", "halfStar");
                    } else {
                        $(e).attr("class", "emptyStar");
                    }
                });
        });
    }
</script>