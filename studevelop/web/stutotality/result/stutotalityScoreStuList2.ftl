<#if itemList?exists&&(itemList?size>0)>
    <div>
        <button class="btn btn-blue mr10 font-14" onclick="showalledit()" style="margin-top: 5px;">
            批量评价
        </button>
        <button class="btn btn-blue mr10 font-14" onclick="importData()" style="margin-top: 5px;">
            导入
        </button>
    </div>
    <div class="evaluate-item">
        <table class="table table-bordered table-striped table-hover typein-table">
            <thead>
            <th>
                <label class="pos-rel">
                    <input
                            name="course-checkbox"
                            type="checkbox"
                            class="wp alltypes"
                            data-id="0"
                    />
                    <span class="lbl"></span>
                </label>
            </th>
            <th>评价</th>
            <th>备注</th>
            </thead>
            <tbody>
            <#list itemList as item1>
                <tr>
                    <td >
                        <label class="pos-rel">
                            <input value="${item1.resultId!}-${item1.id!}" name="course-checkbox" type="checkbox" class="wp alltype1" data-id="1"
                            />
                            <span class="lbl">${item1.itemName!}</span>
                        </label>
                    </td>
                    <td>
                        <ul class="starul" data-score=${item1.result!}>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="1"></a
                                ><a href="javascript:;" data-index="2"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="3"></a
                                ><a href="javascript:;" data-index="4"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="5"></a
                                ><a href="javascript:;" data-index="6"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="7"></a
                                ><a href="javascript:;" data-index="8"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="9"></a
                                ><a href="javascript:;" data-index="10"></a>
                            </li>
                        </ul>
                        <span class="star-tip">${item1.result!}星</span>
                        <input type="hidden" class="resultId" name="stutotalityStuResults[${item1_index+1}].resultId" value="${item1.resultId!}">
                        <input type="hidden" class="itemId" name="stutotalityStuResults[${item1_index+1}].itemId" value="${item1.id!}">
                        <input type="hidden" name="stutotalityStuResults[${item1_index+1}].result" value="0">
                    </td>
                    <td class="td-text-over">
                        <a
                                href="#"
                                class="evaluate-btn-text"
                                onclick="showeditarea(this,'${item1.resultId!}','${item1.id}','${item1.remark!}')"
                        >编辑</a
                        >
                        <span class="typein-item-text"
                        >${item1.remarkSubStr!}</span
                        >
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
    <div class="evaluate-item"></div>
<#else >
    <div class="no-data-container">
        <div class="no-data">
    <span class="no-data-img">
        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
    </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无相关数据</p>
            </div>
        </div>
    </div>
</#if>

<!-- 批量设置 -->
<div class="layer layer-all-edit">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <div class="evaluate-item clearfix">
                <span class="layer-evaluate-label evaluate-item-left">评星：</span>
                <div class="evaluate-item-right">
                    <ul class="starul starallul" data-score="5">
                        <li class="emptyStar">
                            <a href="javascript:;" data-index="1" type="2"></a
                            ><a href="javascript:;" data-index="2" type="2"></a>
                        </li>
                        <li class="emptyStar">
                            <a href="javascript:;" data-index="3" type="2"></a
                            ><a href="javascript:;" data-index="4" type="2"></a>
                        </li>
                        <li class="emptyStar">
                            <a href="javascript:;" data-index="5" type="2"></a
                            ><a href="javascript:;" data-index="6" type="2"></a>
                        </li>
                        <li class="emptyStar">
                            <a href="javascript:;" data-index="7" type="2"></a
                            ><a href="javascript:;" data-index="8" type="2"></a>
                        </li>
                        <li class="emptyStar">
                            <a href="javascript:;" data-index="9" type="2"></a
                            ><a href="javascript:;" data-index="10" type="2"></a>
                        </li>
                    </ul>
                    <span class="star-tip">5星</span>
                </div>
            </div>
            <div class="evaluate-item clearfix mb20">
                <span class="layer-evaluate-label evaluate-item-left">备注：</span>
                <div class="evaluate-item-right">
                    <textarea class="typein-all-text" maxlength="100" id="alltext"></textarea>
                </div>
            </div>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                取消
            </button>
            <button class="btn btn-blue font-14" onclick="suremorestar()">
                确定
            </button>
        </div>
    </div>
</div>

<!-- 备注 -->
<div class="layer layer-area-edit">
    <input type="hidden" id="itemId">
    <input type="hidden" id="resultId">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <div class="evaluate-item clearfix mb20">
                <div class="evaluate-item-right">
                    <textarea class="typein-all-text" id="onetext" maxlength="100"></textarea>
                </div>
            </div>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                取消
            </button>
            <button class="btn btn-blue font-14" onclick="sureeditarea()">
                确定
            </button>
        </div>
    </div>
</div>
<script>
    var editareabtn = null;
    var isSubmit=false;
    $(function() {
        //打星
        initstar();
        //initSignNum();

        $(".starul li a").on("click", function() {
            if(isSubmit){
                return;
            }
            isSubmit=true;
            var that=$(this);
            var score = that.attr("data-index") / 2;
            var resultId = that.parent().parent().parent().find("input[class='resultId']").val();
            var itemId = that.parent().parent().parent().find("input[class='itemId']").val();
            var studentId = $("#studentId").val();
            var classId = $("#classId").val();
            var resultType = "${resultType!}";
            if(that.attr("type")!="2"){
                $.ajax({
                    url: "${request.contextPath}/stutotality/result/saveOneStuResult",
                    data: {
                        'resultId': resultId,
                        'itemId': itemId,
                        'studentId': studentId,
                        'result': score,
                        'resultType':resultType
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var jsonO = data;
                        if (!jsonO.success) {
                            layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                        } else {
                            checkOneStu(studentId);
                            setStar(that);
                        }
                        isSubmit = false;
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                })
            }else{

                setStar(that);
                isSubmit = false;
            }
        });
    });
    function setStar(that){
        //判断是全星点还是半星点，修改当前标签的父标签li的class为对应的星星图像
        that.parents(".starul").attr("data-score", parseInt(that.attr("data-index")) / 2);
        that.parents(".starul").siblings(".star-tip").html(parseInt(that.attr("data-index")) / 2 + "星");
        if (parseInt($(that).attr("data-index")) % 2 == 1) {
            that.parent().attr("class", "halfStar");
        } else {
            that.parent().attr("class", "fullStar");
        }
        //对前方的星星进行处理，遍历前方的li使背景图均变为全星
        var prev = that.parent();
        for (
            var i = 0;
            i <= parseInt(that.attr("data-index")) / 2 - 1;
            i++
        ) {
            prev.prev().attr("class", "fullStar");
            prev = prev.prev();
        }
        //对后方星星进行处理，遍历后面的li使背景图均变为空星
        var after = that.parent();
        for (
            var i = 0;
            i <= 5 - parseInt(that.attr("data-index")) / 2 - 1;
            i++
        ) {
            after.next().attr("class", "emptyStar");
            after = after.next();
        }
    }
    //星星初始化
    function initstar() {
        $(".starul").each(function() {
            var score = $(this).attr("data-score") * 2;
            $(this)
                .siblings(".star-tip")
                .html($(this).attr("data-score") + "星");
            $(this)
                .find("li")
                .each(function(i, e) {
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

    //监听hceck的点击事件
    $("body").on("click", ".wp", function() {
        var that = this;
        if ($(this).hasClass("alltypes")) {
            if ($(this)[0].checked) {
                $(".wp").each(function(i, e) {
                    $(e)[0].checked = true;
                });
            } else {
                $(".wp").each(function(i, e) {
                    $(e)[0].checked = false;
                });
            }
        } else if ($(this).hasClass("alltype1")) {
            if ($(this)[0].checked) {
                $(".alltype2").each(function(index, even) {
                    if ($(even).attr("data-parent") == $(that).attr("data-id")) {
                        $(even)[0].checked = true;
                    }
                });
            } else {
                $(".alltype2").each(function(index, even) {
                    if ($(even).attr("data-parent") == $(that).attr("data-id")) {
                        $(even)[0].checked = false;
                    }
                });
            }
        }
    });

    //批量评价
    function showalledit() {
        selectid = [];
        $(".wp").each(function(i, e) {
            if ($(e)[0].checked) {
                selectid.push($(e).attr("data-id"));
            }
        });
        if (selectid.length == 0) {
            layer.msg("请选择项目", { time: 2000 });
        } else {
            $(".starallul").attr("data-score", 5);
            $(".typein-all-text").val("");
            initstar();
            layer.open({
                type: 1,
                shadow: 0.5,
                area: "510px",
                title: "批量评价",
                content: $(".layer-all-edit")
            });
        }
    }

    //确认批量评价
    function suremorestar() {
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var starnum = $(".starallul").attr("data-score");
        var alltext = $.trim($("#alltext").val());
        var params=[];
        $(".alltype1").each(function(i, e) {
            if ($(e)[0].checked) {
                params[i] = $(e).val();
            }
        });
        var studentId = $("#studentId").val();
        var classId = $("#classId").val();
        var resultType = "${resultType!}";
        $.ajax({
            url:"${request.contextPath}/stutotality/result/saveStuMoreResult",
            data:{'params':params,'result':starnum,'studentId':studentId,'remark':alltext,'resultType':resultType},
            type:'post',
            dataType:'json',
            traditional: true,
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                }else {
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    showTab($("#type").val());
                    checkOneStu(studentId);
                }
                isSubmit = false;
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        })
        hidelayer();
    }

    //显示编辑
    function showeditarea(that,resultId,itemId,remark) {
        $("#onetext").val(remark);
        $("#resultId").val(resultId);
        $("#itemId").val(itemId);
        editareabtn = that;
        layer.open({
            type: 1,
            shadow: 0.5,
            area: "400px",
            content: $(".layer-area-edit")
        });
    }

    //确认编辑备注
    function sureeditarea() {
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var text = $.trim($("#onetext").val());
        var resultId = $("#resultId").val();
        var itemId = $("#itemId").val();
        var studentId = $("#studentId").val();
        var classId = $("#classId").val();
        var resultType = "${resultType!}";
        $.ajax({
            url:"${request.contextPath}/stutotality/result/saveOneStuResult",
            data:{'resultId':resultId,'itemId':itemId,'studentId':studentId,'des':text,'resultType':resultType},
            type:'post',
            dataType:'json',
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                }else {
                    /*if($("#stuDiv"+studentId).find("i").length==0) {
                        $("#stuDiv" + studentId).find("a").after('<i class="typein-success-tip icon-select wpfont"></i>');
                    }
                    setUnsignNum();*/
                    $(editareabtn).siblings(".typein-item-text").html(text);
                    hidelayer();
                }
                isSubmit = false;
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        })
        hidelayer();
    }

    function initSignNum() {

        <#if resStuIds?exists&&(resStuIds?size>0)>
        $(".chosen-tree.chosen-tree-tier3").find("div").each(function () {
            $(this).find("i").remove();
        })
        <#list resStuIds as id>
        if($("#stuDiv"+'${id}').find("i").length==0) {
            $("#stuDiv" + '${id}').find("a").after('<i class="typein-success-tip icon-select wpfont"></i>');
        }
        </#list>
        var num=0;
        $(".chosen-tree.chosen-tree-tier3").find("i").each(function () {
            num = num+1;
        })
        var totalNum=0;
        $(".chosen-tree.chosen-tree-tier3").find("div").each(function () {
            totalNum = totalNum+1;
        })
        $("#unSignNum").html(totalNum-num+"人未评");
        </#if>

    }

</script>