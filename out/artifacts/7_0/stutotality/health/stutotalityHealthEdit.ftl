<div class="evaluate-body col-xs-12">
    <div>
        <button class="btn btn-blue mr10 font-14" onclick="addOrUpdate()">保存</button>
        <button class="btn btn-white font-14" onclick="healthItem()">取消</button>
    </div>

    <div class="evaluate-item">
        <form id="myForm">
        <div>
            <table class="table table-bordered table-striped table-hover mind-table" style="margin-bottom: 0;">
                <thead>
                    <tr>
                        <th style="width: 418px;">项目</th>
                        <th>适用年级</th>
                        <th style="width: 100px;">操作</th>
                    </tr>
                </thead>

                <tbody>
                <#assign ind=0>
                <#assign num=0>
                <#if healthItemList?exists && healthItemList?size gt 0>
                    <#list healthItemList as item>
                    <tr>
                        <td>
                            <input type="hidden" id="healthListId${ind!}" name="healthList[${ind!}].id" value="${item.id!}"/>
                            <input type="hidden" name="healthList[${ind!}].creationTime" <#if item.creationTime?exists>value="${item.creationTime?string('yyyy-MM-dd hh:mm:ss')!}</#if>"/>
                            <input type="hidden" name="healthList[${ind!}].canDeleted" value="${item.canDeleted!}"/>
                            <#--<input type="hidden" name="healthList[${ind!}].orderNumber" value="${item.orderNumber}">-->
                            <input type="text" <#if item.canDeleted?default(0)==0>readonly="readonly"</#if> data-class="healthName" nullable="false" maxlength="40" id="healthListName${ind!}"  name="healthList[${ind!}].healthName" value="${item.healthName!}" placeholder="请输入项目名称" class="form-control evaluate-control" />
                        </td>
                        <td>
                        <#if gradeList?exists && gradeList?size gt 0 && item.healthOptions?exists && item.healthOptions?size gt 0>
                            <#if gradeList?size==item.healthOptions?size>
                                <div class="grade-tip-box healthIdDiv" data-all="1">
                                <span>全年级&nbsp;</span>
                                </div>
                            <#else>
                                <div class="grade-tip-box healthIdDiv" data-all="0">
                                <#list item.healthOptions as option>
                                    <input type="hidden" name="optionList[${num!}].gradeCode" value="${option.gradeCode}" data-healthId="${option.healthId}"/>
                                    <input type="hidden" name="optionList[${num!}].healthId" value="${option.healthId}"/>
                                    <i class="grade-tip" id="${option.gradeCode}" data-id="${option.gradeCode}">${option.gradeName}</i>
                                    <#assign num=num+1>
                                </#list>
                                 </div>
                            </#if>
                        </#if>

                            <a href="javascript:void(0)" class="evaluate-btn-text" onclick="openedit(this,'${item.id}',${ind!})">修改</a>
                        </td>
                        <#assign ind=ind+1>
                        <td>
                            <#if item.canDeleted?default(0)!=0><a href="javascript:void(0)" class="evaluate-btn-text" onclick="delpro(this)">删除</a></#if>
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>

            </table>
            <table class="table table-bordered table-hover mind-table-add">
                <tr>
                    <td class="evaluate-center addDiv" >
                        <a href="javascript:void(0)" class="evaluate-btn-text type-add" onclick="addpro()">+ 新增</a>
                    </td>
                </tr>
            </table>
        </div>
        </form>
    </div>
</div>



<!-- 年级修改 -->
<div class="layer layer-grade-edit">
    <form id="subForm">
        <input type="hidden" id="healthId" name="healthId">
        <input type="hidden" id="healthName" name="healthName">
        <input type="hidden" id="healthIndex" name="healthIndex">
        <div class="layer-content" id="gradeDiv">
            <div class="form-horizontal layer-edit-body layer-grade-body" id="label-box">
                <#if gradeList?exists && gradeList?size gt 0>
                    <#list gradeList as grade>
                        <label class="pos-rel layer-grade-label">
                            <input name="optionList[${grade_index!}].gradeCode" type="checkbox"  class="wp" value="${grade.gradeCode}"/>
                            <span class="lbl">${grade.gradeName}</span>
    <#--                            <input type="hidden" name="optionList[${grade_index!}].gradeCode" value="${grade.gradeCode}"/>-->
                        </label>
                    </#list>
                </#if>
            </div>
            <div class="layer-evaluate-right">
                <div class="btn btn-white mr10 font-14" onclick="hidelayer()">
                    取消
                </div>
<#--                <input type="hidden" id="indexId">-->
                <#--<div class="btn btn-blue font-14" onclick="suredotip()">-->
                <div class="btn btn-blue font-14" onclick="suredotip()">
                    确定
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    function healthItem() {
        var url = '${request.contextPath}/stutotality/healthItem/list';
        $('#showTabList').load(url);
    }
</script>

<script type="text/javascript">
    if ("ontouchstart" in document.documentElement)
        document.write(
            "<script src='../components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>" +
            "<" +
            "/script>"
        );
</script>

<script type="text/javascript">
    var tipeditdom = null;
    var gradelist = [];
    var index=${lastIndex!};
    $(function() {
        <#if gradeList?exists && gradeList?size gt 0>
        <#list gradeList as grade>
        gradelist[${grade_index}]='${grade.gradeCode!}';
        </#list>
        </#if>
    });
    /*function hidelayer() {
        layer.closeAll();
    }*/

    //打开适用年级弹窗
    function openedit(that,healthId,index) {
        tipeditdom = that;
        //debugger;
        var selectlist = [];
        if ($(that).siblings(".grade-tip-box").attr("data-all") == 1) {
            selectlist = gradelist;
        } else {
            $(that).siblings(".grade-tip-box").find(".grade-tip").each(function(i, v) {
                selectlist.push($(v).attr("data-id"));
            });
        }
        $(".layer-grade-edit").find(".wp").each(function(i, e) {
            $(e)[0].checked = false;
        });
        $(".layer-grade-edit").find(".wp").each(function(i, e) {
            selectlist.forEach(function(v, index) {
                if ($(e)[0].value == v) {
                    $(e)[0].checked = true;
                }
            });
        });
        $("#healthId").val(healthId);
        $("#healthIndex").val(index+1);
        $("#healthName").val($("#healthListName"+index).val());
        layer.open({
            type: 1,
            shadow: 0.5,
            area: "300px",
            content: $(".layer-grade-edit")
        });
    }

    //  删除
    function delpro(that) {
        $(that).parents("tr").remove();
    }

    //修改使用年级
    function suredotip() {
        var layerselectid = [];
        var layerselectstr = [];
        $(".layer-grade-edit").find(".wp").each(function(i, e) {
            if ($(e)[0].checked) {
                layerselectid.push($(e).val());
                layerselectstr.push(
                    $(e).siblings(".lbl").html()
                );
            }
        });
        if (layerselectid.length == gradelist.length) {
            $(tipeditdom).siblings(".grade-tip-box").attr("data-all", "1").html('<span>全年级&nbsp;&nbsp;</span>');
        } else {
            var htmlstr = "";
            layerselectid.forEach(function(v, index) {
                htmlstr += '<i class="grade-tip" data-id="'+v+'">'+layerselectstr[index]+'</i>';
            });
            $(tipeditdom).siblings(".grade-tip-box").attr("data-all", "0").html(htmlstr);
        }
        var healthName=$("#healthName").val();
        if(!healthName){
            layerTipMsg(false, "提示", "请先保存当前项目名称");
            return;
        }
        var options= {
            url: "${request.contextPath}/stutotality/healthItem/updateGradeCode",
            type:"post",
            success: function (data) {
                //debugger;
                var jsonO=JSON.parse(data);
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    if(jsonO.healthId){
                        $("#healthListId"+(index-1)).val(jsonO.healthId);
                    }
                    //$("#healthListId"+index).val(jsonO.healthId);
                    <#--var url = '${request.contextPath}/stutotality/healthItem/edit';-->
                    <#--$('#showTabList').load(url);-->
                }
            }
        }
        $('#subForm').ajaxSubmit(options);
    }

    //新增
    function addpro() {//<input type="hidden" name="healthList['+index+'].orderNumber" value="'+index+'"/>
        $(".mind-table tbody").append('<tr><td>'+
                                    '<input type="hidden" id="healthListId'+index+'" name="healthList['+index+'].id" value=""/>'+
                                    '<input type="text" id="healthListName'+index+'" maxlength="40" nullable="false" name="healthList['+index+'].healthName" class="form-control evaluate-control" value="" placeholder="请输入项目名称"></td>'+
                                  '<td><div class="grade-tip-box" data-all="1"><span>全年级&nbsp;</span></div>'+
                                    '<a href="javascript:void(0)" class="evaluate-btn-text" onclick="openedit(this,null,'+index+')">修改</a></td>'+
                                  '<td><a href="javascript:void(0)" class="evaluate-btn-text" onclick="delpro(this)">删除</a></td></tr>');
        index++;
    }

    function addOrUpdate(){
        var check = checkValue("#myForm");
        if(!check){
            return false;
        }
        var ii = layer.load();
        var options= {
            dataType:"json",
            type:"post",
            url: "${request.contextPath}/stutotality/healthItem/addOrUpdate",
            success: function (data) {
                //var jsonO=JSON.parse(data);
                var jsonO=data;
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
                    return;
                } else {
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    <#--var url = '${request.contextPath}/stutotality/healthItem/edit';
                    $('#showTabList').load(url);-->
                    healthItem();
                }
            }
        }
        $('#myForm').ajaxSubmit(options);
    }
</script>