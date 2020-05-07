<form id="itemsOptionsFormId">
<div class="evaluate-right-content" >
    <div>
        <button class="btn btn-blue mr10 font-14" type="button" onclick="saveItemOptions()">保存</button>
        <button class="btn btn-white font-14" type="button" onclick="showyu()">

            <#if buttonName?default("") == 0>
                评价模板预览
             <#else >
                 评价项目预览
            </#if>
        </button>

        <#if acadyear ?exists>
        <div class="filter-item filter-item-right">
            <span style="color: #999;">名称：</span> ${acadyear!}学年${mcodeSetting.getMcode("DM-XQ",semester?default('1'))}${gradeName!}年级评价项目表
         </div>
        </#if>
    </div>
    <div class="evaluate-item">
        <form id="itemFrom" method="post">
        <table class="table table-bordered table-striped table-hover evaluate-table-maintable" id="maintable">
            <thead><tr> <th>选择</th> <th>项目</th><th>项目内容</th> <th>分值</th><th>操作</th> </tr></thead>
            <tbody>
            <div id="listitemDiv">
                <#assign num = 0>
                <#list listByTypeId as item>
                    <tr data-id="${item_index+1}">
                        <td rowspan="${item.stutotalityItemOptions?size}" class="evaluate-center evaluate-td-1">
                            <label class="pos-rel">
                                <input type="hidden" id="itemIdHidden${item_index}"   name="stutotalityItemList[${item_index}].id" value="${item.id!}"  data-id="itemIdHidden${item_index}"   data-name="stutotalityItemList[${item_index}].id" >
                                <input type="hidden" id="typeIdHidden${item_index}"   name="stutotalityItemList[${item_index}].typeId" value="${item.typeId!}"   data-id="typeIdHidden${item_index}"   data-name="stutotalityItemList[${item_index}].typeId">
                                <input type="hidden" name="stutotalityItemList[${item_index}].orderNumber" value="${item.orderNumber!}" data-name="stutotalityItemList[${item_index}].orderNumber" >
                                <input type="hidden" name="stutotalityItemList[${item_index}].subjectType" value="${item.subjectType!}" data-name="stutotalityItemList[${item_index}].subjectType" >
                                <input type="hidden" name="stutotalityItemList[${item_index}].subjectId" value="${item.subjectId!}" data-name="stutotalityItemList[${item_index}].subjectId" >
                                <input type="hidden" name="stutotalityItemList[${item_index}].creationTime" value="${item.creationTime?string('yyyy-MM-dd hh:mm:ss')!}" data-name="stutotalityItemList[${item_index}].creationTime" >
                                <input  name="course-checkbox"  type="checkbox" value="${item.id}" class="wp" data-id="${item_index+1}" />
                                <span class="lbl"></span>
                            </label>
                        </td>
                        <td rowspan="${item.stutotalityItemOptions?size}" class="evaluate-td-2">
                            <#--<#if item.subjectType?default("")=="2">disabled="disabled"</#if>-->
                            <input type="text"  id="itemNameId${item_index}"   name="stutotalityItemList[${item_index}].itemName" nullable="false" maxlength="100"
                    <#if item.subjectType=="2"> readonly isRead="true" <#else > isRead="false"</#if>   value="${item.itemName!}" placeholder="请输入" class="form-control evaluate-control itemNameClass"/>
                        </td>
                        <#list item.stutotalityItemOptions as option>
                        <#if option_index!=0>
                            <tr data-id="${item_index+1}">
                        </#if>
                            <input type="hidden" class="optionIdHidden" name="optionList[${num}].id" value="${option.id!}">
                            <input type="hidden" class="optionIdHidden" name="optionList[${num}].itemId" value="${option.itemId!}">
                            <td class="evaluate-td-3">
                                <input  type="text" class="form-control evaluate-control " id="optionNameId${num}" name="optionList[${num}].optionName" value="${option.optionName!}" nullable="false" maxlength="100"  placeholder="请输入" />
                            </td>
                            <td class="evaluate-td-4">
                                <input  type="int" class="form-control evaluate-control"  id="optionScodeId${num!}" name="optionList[${num!}].score" nullable="false" max="5" min="5" value="5" placeholder="请输入" />
                            </td>
                            <td>
                                <input class="evaluate-td-5" type="hidden" name="optionList[${num!}].description" value="${option.description!}">
                                <a href="javascript:void(0)" class="evaluate-btn-text" onclick="shownorm(this,'${option.id}')" >评价标准</a>
                                <sapn class="evaluate-btn-line"></sapn>
                                <a href="javascript:void(0)" class="evaluate-btn-text" onclick="addminitem(this,'${option.itemId}')" >新增</a>
                                <sapn class="evaluate-btn-line"></sapn>
                                <a href="javascript:void(0)" class="evaluate-btn-text evaluate-min-del">删除</a><#--onclick="deleteitem('${option.id}','${itemlist.typeId}')"-->
                            </td>
                        </tr>
                        <#assign num =num +1>
                    </#list>
                </#list>
            </div>
            </tbody>
        </table>
        </form>
    </div>
    <div>
        <button class="btn btn-white mr10 font-12" type="button" onclick="addpro()">
            新增项目
        </button>
        <button class="btn btn-white font-12" type="button" onclick="delpro()">
            删除项目
        </button>
    </div>
</div>
</form>
<!-- 确认删除弹窗-->
<div class="layer layer-tip layui-layer-wrap  evaluate-layer-del">
    <div class="layer-content clearfix" style="padding-bottom: 10px;">
        <i class="layer-tip-icon layer-tip-icon-warning flleft"></i>
        <div class="flleft del-layer-content">
            <p class="del-layer-title">确定要删除评价项目吗？</p>
            <p>删除后，数据将不可恢复</p>
        </div>
    </div>
    <div class="layer-evaluate-right">
        <button class="btn btn-white mr10 font-14" onclick="hidelayer()">取消</button>
        <button class="btn btn-blue font-14" onclick="suredelpro()">确定</button>
    </div>
</div>
<!-- 预览 -->
<div class="layer layer-preview">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
            <table class="table table-bordered table-striped table-hover" id="reviewtable">
                <thead>
                <tr><th style="width: 78px;">项目</th>
                    <th style="width: 118px;">项目内容</th>
                    <th>评价标准</th>
                    <th style="width: 78px;">分值</th>
                </tr>
                </thead>
                <tbody> </tbody>
            </table>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                确定
            </button>
        </div>
    </div>
</div>
<!-- 标准设置 -->
<div class="layer layer-norm-edit">
    <div class="layer-content">
        <div class="form-horizontal layer-edit-body">
          <textarea
                  class="layer-norm-area"
                  placeholder="请输入"
                  id="normtext"
          ></textarea>
        </div>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                取消
            </button>
            <button class="btn btn-blue font-14" onclick="editnorm()">
                确定
            </button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        isone();
        <#if hasStat?default(1)!=1>
            $("#showSubjectId").hide();
        <#else >
            $("#showSubjectId").show();
        </#if>

        //新增项目切换类型
        $(".evaluate-select-item").on("click", function() {
            $(this) .siblings(".evaluate-select-item") .removeClass("active");
            $(this).addClass("active");
            var subjectType =  $(this).attr("data-type");
            if(subjectType =="1"){
                $("#subjectTypeDiv").html("");
                $("#subjectTypeDiv").html('<div class="addlayer-evaluate-input">'+
                                '<input type="text"  id="itemAddName" nullable="false"  class="form-control evaluate-control" value=""  placeholder="请输入" id="pro-name" /></div>');
            }else{
                var buttonName='${buttonName!}';
                var acadyear='${acadyear!}';
                var semester='${semester!}';
                var gradeId=$("#gradeId").val();
                if(!gradeId){
                    gradeId="";
                }
                $.ajax({
                    url:"${request.contextPath}/stutotality/subjectShow",
                    data:{"buttonName":buttonName,'acadyear':acadyear,'semester':semester,'gradeId':gradeId},
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        var courseList=data.courseList;
                        $("#subjectTypeDiv").html("");
                        var mindHtml="";
                        mindHtml='<div class="addlayer-evaluate-input"><select id="subjectId" class="form-control evaluate-control">'
                        if(courseList){
                            for(var i=0;i<courseList.length;i++) {
                                mindHtml+= ' <option   value="'+courseList[i].id+'">'+courseList[i].subjectName+'</option>';
                            }
                        }
                        mindHtml+= '</select></div>';
                        $("#subjectTypeDiv").html(mindHtml);
                    }
                })
            }
        });

        //新增项目弹窗删除
        $("body").on("click", ".addlayer-eva-delicon", function() {
            $(this).parents(".evaluate-item-min").remove();
            $('.layer-pro-add').find('.addlayer-eva-addicon').last().css('display', 'inline-block');
            renum();
        });

        //删除小项目
        $("body").on("click", ".evaluate-min-del", function() {
            if (!$(this).hasClass("disabled")) {
                var mynum = 1;
                var newid = $(this).parents("tr").attr("data-id");
                var mystr = "";
                var myst0 = "";
                var myst1 = "";
                var dataId0 = "";
                var dataName0 = "";
                var val0 = "";
                var dataId1 = "";
                var dataName1 = "";
                var val1 = "";
                var dataName2 = "";
                var val2 = "";
                var dataName3 ="";
                var val3 = "";
                var checkId = "";
                var checkVal = "";
                var readonly = "";
                $(this).parents("table").find("tbody tr").each(function(i, e) {
                        if ($(e).attr("data-id") == newid) {
                            var onech = $(e).find(".evaluate-td-1");
                            mystr = $(e).find(".evaluate-td-2").find("input").val();
                            mynum = onech.attr("rowspan") * 1 - 1;
                            mystr0 = $(e).find(".evaluate-td-2").find("input").attr("id");
                            mystr1 = $(e).find(".evaluate-td-2").find("input").attr("name");
                            readonly = $(e).find(".evaluate-td-2").find("input").attr("isread");
                            dataId0 = $(e).find("input").eq(0).attr("id");
                             dataName0 = $(e).find("input").eq(0).attr("name");
                             val0 = $(e).find("input").eq(0).val();
                             dataId1 = $(e).find("input").eq(1).attr("id");
                             dataName1 =$(e).find("input").eq(1).attr("name");
                             val1 = $(e).find("input").eq(1).val();
                            dataName2 = $(e).find("input").eq(2).attr("name");
                             val2 = $(e).find("input").eq(2).val();
                             dataName3 = $(e).find("input").eq(3).attr("name");
                             val3 = $(e).find("input").eq(3).val();
                             checkId = $(e).find("input").eq(4).attr("data-id");
                             checkVal =$(e).find("input").eq(4).val();

                            if (onech.attr("rowspan") > 1) {
                                onech.attr("rowspan", onech.attr("rowspan") * 1 - 1);
                                $(e).find(".evaluate-td-2").attr("rowspan", onech.attr("rowspan"));
                                return false;
                            }
                        }
                    });

                if(readonly == "true"){
                 var isread = "readonly";
                }
                $(this).parents("tr").remove();
                $(".evaluate-right-content").find("table").find("tbody tr").each(function(i, e) {
                        if ($(e).attr("data-id") == newid) {
                            if (
                                !$(e).find(".evaluate-td-1").attr("rowspan") * 1
                            ) {
                                $(e).prepend('<' +
                                    'td rowspan="'+mynum+'" class="evaluate-center evaluate-td-1"> <label class="pos-rel">'+
                                    '<input type="hidden" id="'+ dataId0+'"   name="'+ dataName0+'" value="'+ val0+'"  data-id="'+ dataId0+'"   data-name="'+ dataName0+'">'+
                                '<input type="hidden" id="'+ dataId1+'"   name=id="'+ dataName1+'"  value=id="'+ val1+'"    data-id="'+ dataId1+'"    data-name="'+ dataName1+'" >'+
                                '<input type="hidden" name="'+ dataName2+'"  value="'+ val2+'"  data-name="'+ dataName2+'" >'+
                                '  <input type="hidden" name="'+ dataName3+'"  value="'+ val3+'"   data-name="'+ dataName3+'"   >'+
                                        '<input name="course-checkbox" type="checkbox" class="wp" data-id="'+ checkId+'" value="'+ checkVal+'"/>'+
                                         '<span class="lbl"></span> ' +
                                         '</label>' +
                                    ' </td>'+
                                    '<td rowspan="'+mynum+'" class="evaluate-td-2">' +
                                        '  <input type="text"  id="'+mystr0+'"  '+ isread+' isread = "'+readonly+'"  name="'+mystr1+'" nullable="false" maxlength="100" value="'+mystr+'"  placeholder="请输入" class="form-control evaluate-control"/>'+
                                    '</td>');
                            }
                            return false;
                        }
                    });
            }
            isone();
        });
    });

    //删除项目
    var selectid=[];
    var itemIds='';
    function delpro() {
        selectid = [];
        itemIds='';
        $(".wp").each(function(i, e) {
            if ($(e)[0].checked) {
                selectid.push($(e).attr("data-id"));
                if(itemIds){
                    itemIds+=',';
                }
                itemIds+=$(e).val();
            }
        });
        if (selectid.length == 0) {
            layer.msg("请选择项目", { time: 2000 });
        } else {
            layer.open({
                type: 1,
                shadow: 0.5,
                area: "355px",
                title: "提示",
                content: $(".evaluate-layer-del")
            });
        }
    }
    //确认删除
    function suredelpro(id) {
        if (!id) {
            hidelayer();
            $(".evaluate-right-content").find("table tbody tr").each(function(i, e) {
                for (var i = 0; i < selectid.length; i++) {
                    if (selectid[i] == $(e).attr("data-id")) {
                        $(e).remove();
                    }
                }
            });
            $.ajax({
                url: "${request.contextPath}/stutotality/item/deleteItems",
                data: {'itemIds': itemIds},
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        isSubmit=false;
                        return;
                    }else{
                        layer.closeAll();
                        layer.msg("删除成功", {
                            offset: 't',
                            time: 2000
                        });
                        isSubmit=false;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
    }
    //新增项目
    function addpro() {
        $("#subjectTypeDiv").html("");
        $("#subjectTypeDiv").html('<div class="addlayer-evaluate-input">'+
            '<input type="text"  id="itemAddName"  nullable="false" class="form-control evaluate-control" value=""  placeholder="请输入" id="pro-name" /></div>');
        $(".layer-pro-add").find(".evaluate-select-item").removeClass("active");
        $(".layer-pro-add").find(".evaluate-select-item").first().addClass("active");
        $("#pro-name").val('');
        $(".layer-pro-add").find(".eva-pro-item").html('<div class="evaluate-item-min">'+
            '<div class="addlayer-evaluate-input">'+
            ' <input type="text" name="optionList[0].optionName" id="optionNameId0"  nullable="false" class="form-control evaluate-control evaluate-layer-con " value="" placeholder="请输入"> </div> ' +
            '<div class="addlayer-evaluate-input" style="width: 60px;">'+
            ' <input type="text" name="optionList[0].score" id="optionScoreId0" max="5" min="5"  vtype="int" nullable="false" class="form-control evaluate-control evaluate-layer-num" value="5"></div>'+
            ' <div class="addlayer-eva-addicon addlayer-eva-icon" onclick="addmodalpro(this)"><i class="wpfont icon-plus"></i></div>'+
            ' <div class="addlayer-eva-delicon addlayer-eva-icon" style="display:none;"><i class="sz-icon iconshanchu"></i></div></div>');

        layer.open({
            type: 1,
            shadow: 0.5,
            area: "510px",
            title: "新增项目",
            content: $(".layer-pro-add")
        });
    }
    var optionNum = 1;
    //新增项目弹窗新增
    function addmodalpro(that) {
        //inpit 的data-id.data-name动态的
        $('.layer-pro-add').find('.addlayer-eva-addicon').css('display', 'none');
        $(that).parents(".evaluate-item-min" ).after('<div class="evaluate-item-min">'+
            '<div class="addlayer-evaluate-input">'+
            '<input type="text" name="optionList['+optionNum +'].optionName" id="optionNameId'+optionNum +'" nullable="false" class="form-control evaluate-control evaluate-layer-con"value="" placeholder="请输入" /></div>'+
            '<div class="addlayer-evaluate-input" style="width: 60px;">'+
            '<input type="text" name="optionList['+ optionNum +'].score" id="optionScoreId'+ optionNum +'" vtype="int"  nullable="false" max="5" min="5" class="form-control evaluate-control evaluate-layer-num" value="5" /> </div>'+
            ' <div class="addlayer-eva-addicon addlayer-eva-icon" onclick="addmodalpro(this)" >'+
            ' <i class="wpfont icon-plus"></i></div>'+
            '<div class="addlayer-eva-delicon addlayer-eva-icon">'+
            ' <i class="sz-icon iconshanchu"></i></div></div>');
        renum();
        optionNum++;
    }
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
    //确定新增
    var isSubmit=false;
    function sureaddpro(){
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var check = checkValue('#itemOptionFormId');
        if(!check){
            isSubmit=false;
            return;
        }
        var orderNumber=0;
        <#if listByTypeId?exists && listByTypeId?size gt 0>
        orderNumber=${listByTypeId?size};
        </#if>
        var subjectType =  $(".subjectType.active").attr("data-type");
        var typeId = $(".evaluate-template-tree").find("li.active").attr("id");
        var subjectId="";
        var itemName=$("#itemAddName").val();
        if(!itemName){
            var options=$("#subjectId option:selected");
            subjectId=options.val();
            itemName=options.text();
            var sametype = 0;
            $(".itemNameClass").each(function(index, el) {
                if(el.value == itemName){
                    layerError($("#subjectId"),'项目名不能相同');
                    sametype = 1;
                }
            });
            if(sametype == 1){
                isSubmit=false;
                return;
            }
        }else {
            var sametype = 0;
            $(".itemNameClass").each(function(index, el) {
                itemName =  itemName.replace(/\s*/g,"");
                if(el.value==itemName){
                    layerError($("#itemAddName"),'项目名不能相同');
                    sametype = 1;
                }
            });
            if(sametype == 1){
                isSubmit=false;
                return;
            }
        }
        var options={
            url:"${request.contextPath}/stutotality/item/add",
            dataType:"json",
            type:"post",
            data:{"subjectType":subjectType ,"itemName":itemName,"subjectId":subjectId,"typeId":typeId,'orderNumber':orderNumber},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    isSubmit=false;
                    itemAfterAdd(typeId);
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
        };
        $('#itemOptionFormId').ajaxSubmit(options);
    }

    //保存
    isSubmit=false;
    function saveItemOptions(){
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var check = checkValue('#itemsOptionsFormId');
        if(!check){
            isSubmit=false;
            return;
        }
        var sametype = 0;
        var res = [];
        $(".itemNameClass").each(function(index, el) {
            var current = el.value;
            current = current.replace(/\s*/g,"");  //去掉所有的空格
            if (res.indexOf(current) === -1) {
                res.push(current)
            }else{
                sametype = 1;
                var id = "#itemNameId"+index;
                layerError($(id),'项目名不能相同');
            }
        });
        if(sametype == 1){
            isSubmit=false;
            return;
        }
        var typeId = $(".evaluate-template-tree").find("li.active").attr("id");
        var options={
            url:"${request.contextPath}/stutotality/item/save",
            dataType:"json",
            type:"post",
            data:{"typeId":typeId},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    isSubmit=false;
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
        };
        $('#itemsOptionsFormId').ajaxSubmit(options);

    }
    //新增小项目
    var addminitemNum = ${num!};
    function addminitem(that,itemId) {
        var newid = $(that).parents("tr").attr("data-id");

        var num = 0;
        $(that).parents("table").find("tbody tr").each(function(i, e) {
            if ($(e).attr("data-id") == newid) {
                num++;
            }
        });
        $(that).parents("table").find("tbody tr").each(function(i, e) {
            if ($(e).attr("data-id") == newid) {
                var onech = $(e).find(".evaluate-td-1");
                onech.attr("rowspan", onech.attr("rowspan") * 1 + 1);
                $(e).find(".evaluate-td-2").attr("rowspan", onech.attr("rowspan"));
                return false;
            }
        });
        $(that).parents("tr").after('<tr data-id="'+newid+'"><input type="hidden" class="optionIdHidden" name="optionList['+addminitemNum+'].itemId" value="'+itemId+'">'+
                        '<td class="evaluate-td-3">'+
                        '<input type="text" id="optionNameId'+addminitemNum+'" name="optionList['+addminitemNum+'].optionName" nullable="false" maxlength="100" class="form-control evaluate-control" value="" placeholder="请输入"/> </td>'+
                        '<td class="evaluate-td-4"> <input type="text"  vtype="int"  id="optionScodeId'+addminitemNum+'" name="optionList['+addminitemNum+'].score" nullable="false" max="5" min="5" class="form-control evaluate-control" value="5" placeholder="请输入" /></td>'+
                        '<td> <input class="evaluate-td-5" type="hidden" name="optionList['+addminitemNum+'].description" value=""><a href="javascript:void(0)" class="evaluate-btn-text"onclick="shownorm(this,\''+itemId+'\')" >评价标准</a >'+
                        '<sapn class="evaluate-btn-line"></sapn>'+
                        '<a href="javascript:void(0)" class="evaluate-btn-text" onclick="addminitem(this,\''+itemId+'\')">新增</a>'+
                        '<sapn class="evaluate-btn-line"></sapn>'+
                        '<a href="javascript:void(0)" class="evaluate-btn-text evaluate-min-del">删除</a> </td> </tr>');
        isone();
        addminitemNum++;
    }
    //预览
    function showyu() {
        var htmlstr = "";
        $("#maintable tbody").find("tr").each(function(i, e) {
            var onerowspan = $(e).find(".evaluate-td-2").attr("rowspan");
            var minhtml2 = $(e).find(".evaluate-td-2 input").val();
            var minhtml3 = $(e).find(".evaluate-td-3 input").val();
            var minhtml4 = $(e).find(".evaluate-td-4 input").val();
            var minhtml5 = $(e).find(".evaluate-td-5").val();
            if (onerowspan && onerowspan >= 1) {
                htmlstr += '<tr> <td rowspan="'+onerowspan+'" class="evaluate-td-2">'+minhtml2+' </td>'+
                  '<td class="evaluate-td-3"> '+minhtml3+' </td>'+
                  '<td class="evaluate-td-4"> '+minhtml5+' </td>'+
                  '<td> '+minhtml4+'</td> </tr>';
            } else {
                htmlstr += '<tr><td class="evaluate-td-3">'+minhtml3+'</td>'+
                  '<td class="evaluate-td-4">'+minhtml5+'</td>'+
                  '<td>'+minhtml4+'</td></tr>';
            }
        });
        $("#reviewtable tbody").html(htmlstr);
        layer.open({
            type: 1,
            shadow: 0.5,
            area: "800px",
            title: "预览",
            content: $(".layer-preview")
        });
    }
    //显示评价标准弹窗
    function shownorm(that) {
        normeditdom = that;
        var biaoval = $(that).siblings(".evaluate-td-5").val();
        $(".layer-norm-area").val(biaoval);
        layer.open({
            type: 1,
            shadow: 0.5,
            area: "400px",
            title: "评价标准",
            content: $(".layer-norm-edit")
        });
    }
    //保存评价标准
    function editnorm() {
        var biaoval = $.trim($("#normtext").val());
        if(biaoval && biaoval.length>300){
            layer.tips('评价标准不能超过300个字!', $("#normtext"), {
                time:1000,
                tipsMore: true,
                tips: 2
            });
            return;
        }
        $(normeditdom).siblings(".evaluate-td-5").val(biaoval);
        hidelayer();
        layer.msg("修改成功", {
            offset: "t",
            time: 1000
        });
    }
    //检查项目表格是否只剩一行
    function isone() {
    $(".evaluate-right-content")  .find("table tbody tr") .each(function(i, e) {
        if ( $(e) .find(".evaluate-td-2") .attr("rowspan") == 1 ) {
            $(e) .find(".evaluate-min-del") .addClass("disabled");
        } else {
            $(e).find(".evaluate-min-del").removeClass("disabled");
        }
    });
}
</script>