<#if !(type?default(0)==0 && studentId?default("")!="")><#--不计入总分以及个人的情况，是不需要有条件的-->
    <div class="evaluate-item">
        <div class="picker-table">
            <table class="table">
                <tbody>
                <#if type?default(0)!=0>
                    <tr>
                        <th width="150" style="vertical-align: top;">类型：</th>
                        <td>
                            <div class="outter">
                                <a <#if resultType?default("")=="1">class="selected"</#if> href="javascript:;" data-type="resultType" data-id="1">日常</a>
                                <a <#if resultType?default("")=="2">class="selected"</#if> href="javascript:;" data-type="resultType" data-id="2">期末</a>
                            </div>
                        </td>
                        <td width="100" style="vertical-align: top;"></td>
                    </tr>
                </#if>
                <#if studentId?default("")==""><#--个人的情况是都没有项目和内容的-->
                    <tr>
                        <th width="150" style="vertical-align: top;">项目：</th>
                        <td>
                            <div class="outter <#if itemExpand?default("")=="1">outter-auto</#if>">
                                <div class="evaluate-outter-body" >
                                    <#if itemList?exists && itemList?size gt 0>
                                        <#list itemList as item>
                                            <a <#if itemId?default("")==item.id>class="selected"</#if> href="javascript:;" data-type="item" data-id="${item.id!}">${item.itemName!}</a>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </td>
                        <td width="100" style="vertical-align: top;">
                            <div>
                                <#if itemExpand?default("")=="1">
                                    <a class="picker-more" href="javascript:;"><span>折叠</span><i class="fa fa-angle-up"></i></a>
                                <#else>
                                    <a class="picker-more" href="javascript:;"><span>展开</span><i class="fa fa-angle-down"></i></a>
                                </#if>
                            </div>
                        </td>
                    </tr>
                    <#if resultType?default("1")!="2"><#--班级期末的情况是没有内容的-->
                        <tr>
                            <th width="150" style="vertical-align: top;">项目内容：</th>
                            <td>
                                <div class="outter">
                                    <#if optionList?exists && optionList?size gt 0>
                                        <#list optionList as option>
                                            <a <#if optionId?default("")==option.id>class="selected"</#if> href="javascript:;" data-type="option" data-id="${option.id!}">${option.optionName!}</a>
                                        </#list>
                                    </#if>
                                </div>
                            </td>
                            <td width="100" style="vertical-align: top;"></td>
                        </tr>
                    </#if>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</#if>

<div id="showListDiv">

</div>

<script type="text/javascript">
    var container = $("#showTabDiv").html();
    function outtersize() {
        $(".evaluate-outter-body").each(function(i,v) {
            if($(v).height() <= 34){
                $(v).parents("tr").find(".picker-more").css("display","none");
            }else{
                $(v).parents("tr").find(".picker-more").css("display","inline-block");
            }
        })
    }
    $(function () {
        <#if isTeaching?default(false)>
            $("#type0").hide();
            $("#type").val("${type!}");
            $('#type0').parent().removeClass("active");
            $('#type1').parent().removeClass("active");
            $('#type1').parent().addClass("active");
        <#else>
            $("#type0").show();
        </#if>
        $("#resultType").val('${resultType!}');
        $("#itemId").val('${itemId!}');
        $("#optionId").val('${optionId!}');
        var studentId=$("#studentId").val();
        if(studentId && studentId!=""){
            showStuList();
        }else {
            showClassList();
        }
        outtersize();
        $(".picker-table").find(".outter a").click(function () {
            $(this).siblings().removeClass("selected");
            $(this).addClass("selected");
            var dataType=$(this).attr("data-type");

            var type=$("#type").val();
            // var gradeId=$("#gradeId").val();
            var resultType=$("#resultType").val();
            var itemExpand=$("#itemExpand").val();
            var url = '${request.contextPath}/stutotality/result/scoreTab/page?type='+type+"&itemExpand="+itemExpand;
            if(studentId && studentId!=""){
                //学生的情况 只有计入总分才有选择日常期末的可能
                resultType=$(this).attr("data-id");
                url +="&resultType="+resultType+"&studentId="+studentId;
            }else{
                var classId=$("#classId").val();
                var itemId=$("#itemId").val();
                var optionId=$("#optionId").val();
                studentId="";
                if(dataType=="resultType"){
                    resultType=$(this).attr("data-id");
                }else if(dataType=="item"){
                    itemId=$(this).attr("data-id");
                    optionId="";
                }else{
                    optionId=$(this).attr("data-id");
                }
                url +="&classId="+classId+"&resultType="+resultType+"&itemId="+itemId+"&optionId="+optionId;
            }

            $("#showTabDiv").load(url);
        });
        // 展开/折叠
        $(".picker-more").on('click',function(e) {
            if ($(this).children("span").text() == "展开") {
                $("#itemExpand").val('1');
                $(this).children("span").text("折叠");
                $(this).children(".fa").addClass("fa-angle-up").removeClass("fa-angle-down");
            } else {
                $("#itemExpand").val('0');
                $(this).children("span").text("展开");
                $(this).children(".fa").addClass("fa-angle-down").removeClass("fa-angle-up");
            }
            $(this).parents("td").siblings("td").children(".outter").toggleClass("outter-auto");
        });
    })
    $(window).resize(function() {
        $(".point-pro-box").css("min-height", $("#sidebar").height() - 240);
        outtersize();
    })
    function showClassList() {
        /*var type=$("#type").val();
        var resultType=$("#resultType").val();
        var classId=$("#classId").val();
        var itemId=$("#itemId").val();
        var optionId=$("#optionId").val();
        alert($("#showTextId").serialize());*/
        $("#showListDiv").load('${request.contextPath}/stutotality/result/scoreClassList/page?'+$("#searchFormId").serialize()+'&isTeaching=${isTeaching?c}');
    }
    function showStuList(){
        $("#showListDiv").load('${request.contextPath}/stutotality/result/scoreStuList/page?'+$("#searchFormId").serialize()+'&isTeaching=${isTeaching?c}');
    }
</script>