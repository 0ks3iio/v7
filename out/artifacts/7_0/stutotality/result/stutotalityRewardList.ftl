<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<div class="typein-award-tip">
    获得一次班级奖项得1个星，一次校级得2个星，一次市级得3个星。
</div>
<input type="hidden" id="rewardId" >
<div class="evaluate-item clearfix mt10">
    <div class="typein-award-additem" onclick="addaward()">
        <div>
            <i class="wpfont icon-plus"></i>
        </div>
        <span class="typeaward-additem-btn">新增奖项</span>
    </div>
    <#if stutotalityStuRewards?exists&&(stutotalityStuRewards?size>0)>
        <#list stutotalityStuRewards as item>
            <div class="typein-award-item">
                <div class="award-item-left <#if item.starNum==3>award-item-left3<#elseif item.starNum==2>award-item-left2<#elseif item.starNum==1>award-item-left1<#else>award-item-left1</#if>"></div>
                <div class="award-item-right">
                    <i class="wpfont icon-close-round" onclick="deletesure('确定是要删除获奖记录吗','delAward','${item.id!}')"></i>
                    <div class="aitem-right-title">
                        <span>${item.rewardName!}</span>
                        <i>获奖于${item.creationTimeStr!}</i>
                    </div>
                    <div class="aitem-right-content">
                        <i class="sz-icon iconbianji" onclick="editaward('${item.rewardName!}','${item.description!}','${item.id!}')"></i>
                        <span>${item.description!}</span>
                    </div>
                    <div class="aitem-right-star">
                        <ul class="starul starallul" data-score='${item.starNum!}' style="width:85px;">
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="1"></a><a href="javascript:;" data-index="2"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="3"></a><a href="javascript:;" data-index="4"></a>
                            </li>
                            <li class="emptyStar">
                                <a href="javascript:;" data-index="5"></a><a href="javascript:;" data-index="6"></a>
                            </li>
                        </ul>
                        <span class="star-tip">3星</span>
                    </div>
                </div>
            </div>
        </#list>
    </#if>
</div>
 <div class="layer layer-pro-add">
     <div class="layer-content">
         <div class="form-horizontal layer-edit-body" style="margin-bottom: 10px;">
             <div class="rewardList">
                 <span class="layer-evaluate-label">类型：</span>
                 <#if stutotalityRewards?exists&&(stutotalityRewards?size>0)>
                    <#list stutotalityRewards as item>
                        <span class="evaluate-select-item<#if item_index==0> active</#if>" id="reward${item_index!}" data-type="${item.id!}">${item.rewardName!}</span>
                    </#list>
                 </#if>
             </div>
             <div class="evaluate-item clearfix">
                 <span class="layer-evaluate-label evaluate-item-left">备注：</span>
                 <div class="evaluate-item-right">
                     <div class="addlayer-evaluate-input">
                         <textarea class="typein-all-text" id="onetext" maxlength="50"></textarea>
                     </div>
                 </div>
             </div>
         </div>
         <div class="layer-evaluate-right" >
             <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                 取消
             </button>
             <button class="btn btn-blue font-14" onclick="sureeditarea()">
                 确定
             </button>
         </div>
     </div>
 </div>
<script type="text/javascript">
    $(function() {
        container = $("#showTabDiv").html();

        //新增奖项切换类型
        $(".evaluate-select-item").on("click", function() {
            $(this).siblings(".evaluate-select-item").removeClass("active");
            $(this).addClass("active");
        });
        //打星
        initstar();
    });

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
    
    function delAward(id) {
        var studentId = "${studentId!}";
        if(id&&studentId) {
            $.ajax({
                url: "${request.contextPath}/stutotality/result/delStuReward",
                data: {'rewardId': id},
                type: 'post',
                success: function (data) {
                    var jsonO = JSON.parse(data);
                    if (jsonO.success) {
                        layer.msg("删除成功", {
                            offset: 't',
                            time: 2000
                        });
                        var url = '${request.contextPath}/stutotality/result/getStuRewardList?studentId=' + studentId;
                        $("#showTabDiv").load(url);
                        layer.closeAll();
                    } else {
                        layerTipMsg(jsonO.success, "失败", jsonO.msg);
                        layer.closeAll();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                }
            });
        }else{
            layer.closeAll();
        }
    }

    //新增奖项
    function addaward() {
        $(".evaluate-select-item").removeClass("active");
         $("#reward0").addClass("active");
        $("#onetext").val("");
        $("#rewardId").val("");
        layer.open({
            type: 1,
            shadow: 0.5,
            title: '新增奖项',
            area: "500px",
            content: $(".layer-pro-add")
        });
    }

    //编辑奖项
    function editaward(name,desc,id) {
        var name0 = document.getElementById("reward0").innerText;
        var name1 = document.getElementById("reward1").innerText;
        $(".evaluate-select-item").removeClass("active");
        if(name0 == name ){
            $("#reward0").addClass("active");
        }else if(name1 == name){
            $("#reward1").addClass("active");
        }else {
            $("#reward2").addClass("active");
        }
        $("#rewardId").val(id);
        $("#onetext").val(desc);
        layer.open({
            type: 1,
            shadow: 0.5,
            title: '编辑奖项',
            area: "500px",
            content: $(".layer-pro-add")
        });
    }

    //确认编辑备注
    function sureeditarea() {
        var id =  $("#rewardId").val();
        var rewardId = $(".evaluate-select-item.active").attr("data-type");
        var studentId = "${studentId!}";
        var gradeId = "${gradeId!}";
        var text = $.trim($("#onetext").val());
        if(!rewardId){
            layer.msg("没有设置奖项类型", { time: 2000 });
            return;
        }
        $.ajax({
            url:"${request.contextPath}/stutotality/result/saveStuReward?",
            data:{'id':id,'rewardId':rewardId,'remark':text,'studentId':studentId},
            type:'post',
            dataType:'json',
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "操作失败！", jsonO.msg);
                    isSubmit = false;
                    return;
                }else {
                    var url = '${request.contextPath}/stutotality/result/getStuRewardList?studentId=' + studentId;
                    $("#showTabDiv").load(url);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        })
        hidelayer();
    }
</script>