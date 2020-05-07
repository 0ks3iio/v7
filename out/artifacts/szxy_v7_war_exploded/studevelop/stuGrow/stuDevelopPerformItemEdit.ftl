
<div class="layer-content">
    <h4>评估项目</h4>
    <div class="filter-item block">
        <span class="filter-name">名称：</span>
        <div class="filter-content">
            <input type="hidden" id = "itemUnitId" name="unitId" value="${stuDevelopPerformItem.unitId!}">
            <input type="hidden" name="displayOrder" value="${stuDevelopPerformItem.displayOrder!}">
            <input type="hidden" id="itemId" name="id" value="${stuDevelopPerformItem.id!}">
            <input type="text" id="itemName" class="form-control" name="itemName" value="${stuDevelopPerformItem.itemName!}" nullable="false" maxlength="50">
        </div>
    </div>
    <h4>等级设置</h4>
    <div class="filter" id="divContainer">
        <#assign arr = ['A','B','C','D','E'] >
        <#if stuDevelopPerformItemCodeList?exists && (stuDevelopPerformItemCodeList?size gt 0) >
            <#list stuDevelopPerformItemCodeList as code>
                <div class="filter-item block">
                    <span class="filter-name">${arr[code_index]!}：</span>
                    <div class="filter-content">
                        <input type="text" id="codeContent${code_index}" name="codeList[${code_index}].codeContent" label="existed" class="form-control" value="${code.codeContent!}" nullable="false" maxlength="90">
                        <input type="hidden" name="codeList[${code_index}].itemId" value="${code.itemId!}" >
                        <input type="hidden" name="codeList[${code_index}].displayOrder" value="${code.displayOrder!}" >
                        <input type="hidden" name="codeList[${code_index}].creationTime" value="${code.creationTime!}" >
                        <input type="hidden" name="codeList[${code_index}].id" value="${code.id!}" >
                        <a href="javascript:void(0)" onclick="doDeleteItemCode(this ,'${code.id!}');">删除</a>
                    </div>
                </div>
            </#list>
        <#else >
            <div class="filter-item block">
                <span class="filter-name">A：</span>
                <div class="filter-content">
                    <input type="text" id="codeContent1" name="codeContent" class="form-control" nullable="false" value="优秀" maxlength="90">
                    <a href="javascript:void(0)" onclick="doDeleteItemCode(this,'');">删除</a>
                </div>
            </div>
            <div class="filter-item block">
                <span class="filter-name">B：</span>
                <div class="filter-content">
                    <input type="text" id="codeContent2" class="form-control" name="codeContent"  nullable="false" value="良好" maxlength="90" >
                    <a href="javascript:void(0)" onclick="doDeleteItemCode(this,'');">删除</a>
                </div>
            </div>
            <div class="filter-item block">
                <span class="filter-name">C：</span>
                <div class="filter-content">
                    <input type="text" class="form-control" id="codeContent3"  name="codeContent" nullable="false" value="有待改进" maxlength="90" >
                    <a href="javascript:void(0)" onclick="doDeleteItemCode(this,'');">删除</a>
                </div>
            </div>
        </#if>


        <a href="javascript:void(0)" id="addGrade" onclick="doAddGrade();" class="add-scoreLevel">新增等级</a>
    </div>
    <div id="replaceDiv"  style="display:none">

        <div class="filter-item block">
            <span class="filter-name">C：</span>
            <div class="filter-content">
                <input type="text" class="form-control"  name="codeContent" value="" nullable="false"  maxlength="90" >
                <a href="javascript:void(0)" onclick="doDeleteItemCode(this,'');">删除</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var arr = new Array("A","B","C","D","E");

    function doDeleteItemCode(data ,itemCodeId) {
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        var size = $("#divContainer").find(".filter-item.block").size();
        if(size == 1){
            layerTipMsgWarn("提示","等级设置选项数量不能小于1！");
            return;
        }
        if(itemCodeId != ""){
            var options = {
                url:"${request.contextPath}/studevelop/performanceItemcode/delete",
                data:{"itemCodeId":itemCodeId},
                dataType:"json",
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        return;
                    }else{
                        layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                        $("#itemDiv").load("${request.contextPath}/studevelop/performanceItem/list/page?gradeCode=" + gradeCode);
                    }
                },
                error:function(XMLHttpRequest ,textStatus,errorThrown){}
            }
            $.ajax(options);
        }
        $(data).parent().parent().remove();
        $("#addGrade").show();
        var i=0;
        $("#divContainer").find("span.filter-name").each(function(){
            $(this).text(arr[i]+"：");
            i = i +1;
        })
    }
    var num =5;
    function doAddGrade(){
        var size = $("#divContainer").find(".filter-item.block").size();
        if(size < 5 ){
            $("#replaceDiv").find("span.filter-name").text(arr[size]+"：")
            $("#replaceDiv").find("input").attr("name","codeContent");
             num = num +1;
            $("#replaceDiv").find("input").attr("id","addcodeContent" + num);
            $("#addGrade").before( $("#replaceDiv").html());
            if(size == 4){
                $("#addGrade").hide();
            }
        }else{
            layerTipMsgWarn("提示","等级设置选项数量不能大于5！");
        }
    }
    function onChack() {
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        var unitId = $("#itemUnitId").val();
        var id = $("#itemId").val();
        var itemName = $("#itemName").val();
        var flag =false;
        var options={
            url:"${request.contextPath}/studevelop/performanceItem/checkItemName",
            dataType:"json",
            type:"post",
            data:{"gradeCode":gradeCode ,"unitId":unitId,"itemId":id ,"itemName":itemName},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layer.tips("评估项目名称在该年级下已存在，请修改！", "#itemName", {
                        tipsMore: true,
                        tips:3
                    });
                    flag = false;
                }else{
                    flag = true;
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $.ajax(options);
        return flag;
    }
    $(document).ready(function () {
        var size = $("#divContainer").find(".filter-item.block").size();
        if(size == 5){
            $("#addGrade").hide();
        }
    })
</script>