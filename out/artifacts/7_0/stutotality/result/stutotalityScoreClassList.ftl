<#if stuDtoList?exists&&(stuDtoList?size>0)>
    <div class="filter">
        <div class="filter-item">
            <button class="btn btn-blue mr10 font-14" onclick="showalledit()">
                批量评价
            </button>
            <#if resultType?default("")=="2">
                <button class="btn btn-blue mr10 font-14" onclick="importData()">
                    导入
                </button>
            </#if>
        </div>
        <div class="filter-item filter-item-right">
            <div class="filter-content">
                <div class="typein-right-tip">
                    <i class="sz-icon icontishi"></i>评分标准
                </div>
            </div>
        </div>
    </div>
    <table class="table  table-striped table-hover typein-table">
        <thead>
        <th width="10">
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
        <th width="30">姓名</th>
        <th width="40">学号</th>
        <th width="10">性别</th>
        <th width="80">评价</th>
        <th width="100">备注</th>
        </thead>
        <tbody>
        <#if stuDtoList?exists&&(stuDtoList?size>0)>
            <#list stuDtoList as stu>
                <tr>
                    <td rowspan="1">
                        <label class="pos-rel">
                            <input name="course-checkbox" value="${stu.id!}-${stu.resultId!}" type="checkbox" class="wp alltype1" data-id="1"
                            />
                            <span class="lbl"></span>
                        </label>
                    </td>
                    <td>${stu.studentName!}</td>
                    <td>${stu.studentCode!}</td>
                    <td>${mcodeSetting.getMcode("DM-XB","${stu.sex!}")}</td>
                    <td>
                        <ul class="starul" data-score="${stu.result!}">
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
                        <span class="star-tip">${stu.result!}星</span>
                        <input type="hidden" class="resultId" name="stutotalityStuResults[${stu_index+1}].resultId" value="${stu.resultId!}">
                        <input type="hidden" class="studentId" name="stutotalityStuResults[${stu_index+1}].studentId" value="${stu.id!}">
                        <input type="hidden" name="stutotalityStuResults[${stu_index+1}].result" value="0">
                    </td>
                    <td class="td-text-over">
                    <a
                            href="#"
                            class="evaluate-btn-text"
                            onclick="showeditarea(this,'${stu.id!}','${stu.resultId!}','${stu.remark!}')"
                    >编辑</a
                    >
                    <span class="typein-item-text"
                    >${stu.remarkSubStr!}</span
                    >
                    </td>

                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
    <!-- 备注 -->
    <div class="layer layer-area-edit">
        <input type="hidden" id="editStudentId">
        <input type="hidden" id="editResultId">
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

    <script>
        var editareabtn = null;
        var isSubmit = false;
        $(function () {
            //打星
            initstar();
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
                }
            });
            $(".typein-right-tip").mouseover(function() {
                layer.tips("标准：${desc!}",".typein-right-tip", {
                    tips: [3, "#317EEB"], //3指类型
                    time: 2000, //20s后自动关闭
                    maxWidth: 360
                });
            });
            <#--保存单个评价-->
            $(".starul li a").on("click", function() {
                if(isSubmit){
                    return;
                }
                isSubmit=true;
                var that=$(this);
                var score = that.attr("data-index") / 2;
                var resultId = that.parent().parent().parent().find("input[class='resultId']").val();
                var itemId=$("#itemId").val();
                var optionId=$("#optionId").val();
                if(!itemId){
                    itemId="";
                }
                if(!optionId){
                    optionId="";
                }
                var studentId = that.parent().parent().parent().find("input[class='studentId']").val();;
                var classId = '${classId!}';
                var resultType = '${resultType!}';
                var type = '${type!}';
                if(that.attr("type")!="2") {
                    $.ajax({
                        url: "${request.contextPath}/stutotality/result/saveOneStuResult",
                        data: {
                            'resultId': resultId,
                            'itemId': itemId,
                            'optionId': optionId,
                            'studentId': studentId,
                            'result': score,
                            'resultType': resultType
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {
                            var jsonO = data;
                            if (!jsonO.success) {
                                layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                            } else {
                                setStar(that);
                                checkStu(classId);
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
        })
        function setStar(that){
            //判断是全星点还是半星点，修改当前标签的父标签li的class为对应的星星图像
            that.parents(".starul").attr("data-score", parseInt(that.attr("data-index")) / 2);
            that.parents(".starul").siblings(".star-tip").html(parseInt(that.attr("data-index")) / 2 + "星");
            if (parseInt(that.attr("data-index")) % 2 == 1) {
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
            var starnum = $(".starallul").attr("data-score");
            var alltext = $.trim($("#alltext").val());
            var classId = '${classId!}'
            var j=0;
            var params=[];
            $(".alltype1").each(function(i, e) {
                if ($(e)[0].checked) {
                    params[j] = $(e).val();
                    j=j+1
                }
            });
            var itemId=$("#itemId").val();
            var optionId=$("#optionId").val();
            if(!itemId){
                itemId="";
            }
            if(!optionId){
                optionId="";
            }
            var resultType = '${resultType!}';
            var type = '${type!}';
            $.ajax({
                url:"${request.contextPath}/stutotality/result/saveSomeStusResult",
                data:{'params':params,'itemId':itemId,'optionId':optionId,'result':starnum,'remark':alltext,"resultType":resultType},
                type:'post',
                dataType:'json',
                traditional: true,
                success:function(data) {
                    var jsonO = data;
                    if(!jsonO.success) {
                        layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                        isSubmit = false;
                        return;
                    }else {
                        layer.msg(jsonO.msg, {
                            offset: 't',
                            time: 2000
                        });
                        showTab($("#type").val());
                        checkStu(classId);
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                }
            })
            hidelayer();
        }

        //显示编辑
        function showeditarea(that,studentId,resultId,remark) {
            $("#onetext").val(remark);
            $("#editStudentId").val(studentId);
            $("#editResultId").val(resultId);
            editareabtn = that;
            layer.open({
                type: 1,
                shadow: 0.5,
                area: "400px",
                content: $(".layer-area-edit")
            });
        }

        //确认编辑备注
        isSubmit = false;
        function sureeditarea() {
            if(isSubmit){
                return;
            }
            isSubmit=true;
            var text = $("#onetext").val();
            var resultId = $("#editResultId").val();
            var studentId = $("#editStudentId").val();
            var itemId=$("#itemId").val();
            var optionId=$("#optionId").val();
            if(!itemId){
                itemId="";
            }
            if(!optionId){
                optionId="";
            }
            var classId = '${classId!}';
            var resultType = '${resultType!}';
            var type = '${type!}';
            $.ajax({
                url:"${request.contextPath}/stutotality/result/saveOneStuResult",
                data:{'resultId':resultId,'itemId':itemId,'optionId':optionId,'studentId':studentId,'remark':text,'resultType':resultType},
                type:'post',
                dataType:'json',
                success:function(data) {
                    var jsonO = data;
                    isSubmit = false;
                    if(!jsonO.success) {
                        layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                    }else {
                        /*if($("#stuDiv"+studentId).find("i").length==0) {
                            $("#stuDiv" + studentId).find("a").after('<i class="typein-success-tip icon-select wpfont"></i>');
                            setUnsignNum();
                        }*/
                        $(editareabtn).siblings(".typein-item-text").html(text);
                        hidelayer();
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                }
            })
            hidelayer();
        }
        //星星初始化
        function initstar() {
            $(".starul").each(function() {
                var score = $(this).attr("data-score") * 2;
                $(this).siblings(".star-tip").html($(this).attr("data-score") + "星");
                $(this).find("li").each(function(i, e) {
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