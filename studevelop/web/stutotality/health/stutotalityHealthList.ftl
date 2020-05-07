
<div class="evaluate-body col-xs-12">
    <div>
        <button class="btn btn-blue mr10 font-14" onclick="editcrier()">编辑达标标准</button>
        <button class="btn btn-white font-14" onclick="editHealthItem()">编辑身心项目</button>
    </div>

    <div class="evaluate-item">
        <div>
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th>项目/达标标准</th>
                    <#if gradeList?exists && gradeList?size gt 0>
                        <#list gradeList as grade>
                            <th class="evaluate-center" gradeId="${grade.gradeCode}">${grade.gradeName}</th>
                    </#list>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <#if healthItemList?exists && healthItemList?size gt 0>
                    <#list healthItemList as item>
                        <tr>
                            <td>${item.healthName!}</td>

                            <#if gradeList?exists && gradeList?size gt 0>
                                <#list gradeList as grade>
                                    <#if valueMap[item.id+"_"+grade.gradeCode]?exists>
                                        <#assign value=valueMap[item.id+"_"+grade.gradeCode] >
                                    <#if value == "null">
                                        <td class="td-no-con evaluate-center">无</td>
                                    <#else>
                                        <td class="evaluate-center">${value}</td>
                                    </#if>
                                        <#else>
                                            <td class="td-no-con evaluate-center">-</td>
                                    </#if>
                                </#list>
                            </#if>
                        </tr>

                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>

</div>


<!-- 评价类别设置 -->

<div class="layer layer-criter-edit">

    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <table class="table table-bordered table-striped  layer-table-content">
                <tr>
                    <th width="235">身心项目</th>
                    <th width="235">达标标准</th>
                </tr>
                <tr>
                    <td style="height: 300px;padding: 0;">
                        <ul class="mind-layer-ul">
                            <#assign healthId="">
                            <#if healthItemList?exists && healthItemList?size gt 0>
                                <#list healthItemList as item>
                                <li onclick="healthItemStandard(this,'${item.id!}')"  data-index="${item_index}" class="healthClass <#if item_index == 0 ><#assign healthId=item.id!> active</#if>">
                                    <div >${item.healthName!}</div>
                                    <#if healthIdMap[item.id]?exists>
                                        <#if !healthIdMap[item.id]>
                                            <span class="mind-layer-tip" id="defendValue${item_index}">未维护</span>
                                        <#else>
                                            <span id="defendValue${item_index}">
                                        </#if>
                                    <#else>
                                        <span id="defendValue${item_index}">
                                    </#if>
                                    <#--<span class="mind-layer-tip" id="defendValue${item_index}">
                                    <#if healthIdMap[item.id]?exists><#if !healthIdMap[item.id]>未维护</#if></#if>
                                    </span>-->
                                </li>
                                </#list>
                            </#if>
                        </ul>
                    </td>
                    <td style="height: 300px;padding: 0;">
                        <form id="myForm">
                            <div class="mind-criter-box" >

                            </div>
                        </form>
                    </td>
                </tr>
            </table>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer(1)">
                返回
            </button>
            <button class="btn btn-blue font-14" onclick="save()">确定</button> <#---->
        </div>
    </div>
</div>



<script type="text/javascript">
    var isedit = false;
    function hidelayer(type) {
        if(type = 1){
            if(isedit) {
                var url = '${request.contextPath}/stutotality/healthItem/list';
                $('#showTabList').load(url);
            }
            else{
                $(".mind-layer-ul").find(".healthClass").removeClass("active").first().addClass("active");
            }
        }
        layer.closeAll();
    }

    // 打开编辑标准弹窗
    function editcrier() {
        var healthId='${healthId!}';
        healthItemStandard('',healthId);
        layer.open({
            type: 1,
            shadow: 0.5,
            area: "600px",
            title: "编辑达标标准",
            content: $(".layer-criter-edit"),
            /*yes:function(index,layerDiv){
                alert(123);
            }*/
            cancel: function(index, layero) {
                //layer.close(index);
                hidelayer(1)
                return false;
            }
        });
    }

    function editHealthItem() {
        var url = '${request.contextPath}/stutotality/healthItem/edit';
        $('#showTabList').load(url);
    }
    var index=0;
    function healthItemStandard(that,id) {
       if(that){
           $(".healthClass").removeClass("active");
           $(that).addClass("active");
           index=$(that).attr("data-index");
       }
        $.ajax({
            type:"post",
            url:"${request.contextPath}/stutotality/healthStandard/edit",
            data:{
                "healthId":id
            },
            success:function(data) {
                // var list=data.healthItemByGrade;
                $(".mind-criter-box").html("");
                var mindHtml="";
                for(var i=0;i<data.length;i++){
                    mindHtml+='<div class="mind-criter-item" id="gradeName" ><span>'+data[i].gradeName+' :&nbsp;&nbsp;</span><div>'+
                            '<input type="hidden" name="optionList['+i+'].gradeCode" value="'+data[i].gradeCode+'">'+
                            '<input type="hidden" name="optionList['+i+'].id" value="'+data[i].id+'"/>'+
                        '<input type="text" maxlength="20" id="healthStandardId'+i+'" class="form-control evaluate-control healthStandardClass" name="optionList['+i+'].healthStandard"  value="'+data[i].healthStandard+'" placeholder=""/></div></div> ';
                    mindHtml+='<input type="hidden" name="optionList['+i+'].healthId" value="'+id+'">';
                }
                $(".mind-criter-box").html(mindHtml);
            }
        });

    }

    function save() {
        var check = checkValue("#myForm");
        if(!check){
            return false;
        }
        var ii = layer.load();
        var options= {
            url: "${request.contextPath}/stutotality/healthStandard/doEdit",
            type:"post",
            dataType:"json",
            success: function (data) {
                var jsonO=data;
                layer.close(ii);
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    isedit = true;
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    //debugger;
                    var flag=false;
                    $(".healthStandardClass").each(function() {
                        if($(this).val()){
                            flag=true;
                            $("#defendValue"+index).html("");
                            $("#defendValue"+index).removeClass("mind-layer-tip");
                         }
                     })
                     if(!flag){
                         $("#defendValue"+index).addClass("mind-layer-tip");
                         $("#defendValue"+index).html("未维护");
                     }

                }
            }
        }
        $('#myForm').ajaxSubmit(options);

    }


</script>


