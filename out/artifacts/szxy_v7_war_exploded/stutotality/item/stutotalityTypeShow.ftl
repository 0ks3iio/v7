<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<div class="evaluate-left-content">
    <div class="evaluate-type-edit">
        <button class="btn btn-white evaluate-100" id="type-edit">评价类别设置</button>
    </div>
    <ul class="evaluate-template-tree" id="typetemplatetree">
        <#list listStutotality as typelist>
            <li  data-total="${typelist.hasStat!}" <#if typelist_index ==0> class="active" </#if> id="${typelist.id!}">${typelist.typeName!}</li>
               <input type="hidden"   id="typeIdHidden" value="${typelist.id!}">
                <input type="hidden" value="${typelist.orderNumber!}">
        </#list>
    </ul>
</div>
<div  id="itemListDiv">

</div>


<div class="layer layer-type-edit">
    <div class="layer-content">
        <form id="typeForm">
        <!-- 评价类别设置 -->
        <input type="hidden" id="acadyear"  name="acadyear" value="${acadyear!}">
        <input type="hidden" id="gradeName"  name="gradeName" value="${gradeName!}">
        <input type="hidden" id="semester" name="semester" value="${semester!}">
        <input type="hidden" id="gradeId" name="gradeId" value="${gradeId!}">
        <div class="form-horizontal layer-edit-body">
            <table class="table table-striped table-hover layer-table-content"
                    style="margin-bottom: 0px;;">
                <thead>
                <tr><th>类别</th><th>排序</th><th>是否计入总分</th><th>操作</th></tr>
                </thead>
                <tbody></tbody>
            </table>
            <table class="table table-hover">
                <tr>
                    <td class="evaluate-center">
                        <a href="javascript:void(0);" class="evaluate-btn-text type-add" id="typeAddId">+ 新增</a>
                    </td>
                </tr>
            </table>
        </div>

        </form>
        <div class="layer-evaluate-right">
            <button class="btn btn-white mr10 font-14" onclick="hidelayer()">
                取消
            </button>
            <button class="btn btn-blue font-14"  onclick="sureaddType()">确定</button>
        </div>
    </div>
</div>
<!-- 新增项目 -->
<form id="itemOptionFormId">
    <div class="layer layer-pro-add">
        <div class="layer-content">
            <div  class="form-horizontal layer-edit-body"
                  style="margin-bottom: 10px;" >
                <div>
                    <span class="layer-evaluate-label">类型：</span>
                    <span class="evaluate-select-item active subjectType"   data-type="1">自定义</span>
                    <span class="evaluate-select-item subjectType" id="showSubjectId"  data-type="2"> 科目</span>
                </div>
                <#-- div开始 -->
                <form id="addItemForm">
                <div class="evaluate-item clearfix">
                    <span class="layer-evaluate-label evaluate-item-left">项目名称：</span>
                    <div class="evaluate-item-right" id="subjectTypeDiv">
                        <div class="addlayer-evaluate-input">
                            <input type="text"  id="itemAddName" nullable="false"  class="form-control evaluate-control" value=""  placeholder="请输入" id="pro-name" />
                        </div>
                    </div>
                </div>

                <div class="evaluate-item clearfix">
                    <span class="layer-evaluate-label evaluate-item-left">项目内容：</span>
                    <div class="evaluate-item-right eva-pro-item">
                        <div class="evaluate-item-min">
                            <div class="addlayer-evaluate-input">
                                <input type="text" nullable="false" id="optionNameId0" name="optionList[0].optionName" class="form-control evaluate-control evaluate-layer-con" value="" placeholder="请输入"/>
                            </div>
                            <div class="addlayer-evaluate-input" style="width: 60px;">
                                <input type="int" max="5"min="1" class="form-control evaluate-control evaluate-layer-num" value="5"/>
                            </div>
                            <div class="addlayer-eva-addicon addlayer-eva-icon" onclick="addmodalpro(this)">
                                <i class="wpfont icon-plus"></i>
                            </div>
                            <div class="addlayer-eva-delicon addlayer-eva-icon" style="display:none;">
                                <i class="sz-icon iconshanchu"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layer-evaluate-right">
                    <div class="btn btn-white mr10 font-14" onclick="hidelayer()">取消</div>
                    <div class="btn btn-blue font-14" onclick="sureaddpro()">确定</div>
                </div>
                <#-- div结束 -->
                </form>
            </div>
        </div>
    </div>
</form>

<div class="item-showyu">
</div>


<script type="text/javascript">
    function backItemShow () {
        var url = '${request.contextPath}/stutotality/item/index/page?gradeId=${gradeId!}&acadyear=${acadyear!}&semester=${semester!}';
        $('#templateTabShow').load(url);
    }
    var selectid = [];
    $(function() {
        showBreadBack(backItemShow,true,"评价项目");
        itemAfterAdd('${typeId!}');
        $(".evaluate-template-tree li").click(function() {
            itemAfterAdd($(this).attr("id"));

            $(this).siblings().removeClass("active");
            $(this).addClass("active");
        });
        // 评价类别设置
        $("#type-edit").on("click", function() {
            var htmlstr ="";
            $(".evaluate-template-tree li").each(function(i, e) {
                var id = e.id ;    //类别id
                var num = i+1 ;   //排序号
                var myvalue = $(e).html();
                var alltotal = $(e).attr("data-total") == 0 ? "" : "checked";
                htmlstr += '<tr> <td> <input type="text" id="typeNameId'+ i +'" nullable="false" name="stutotalityTypeList['+ i +'].typeName"   class="form-control evaluate-control hiddenClassName typeNameClass" value="'+myvalue +'"  placeholder="请输入"/>'+
                                    ' <input type="hidden" class="hiddenClassId" name="stutotalityTypeList['+ i +'].id" value="'+ id +'">'+
                    '</td><td class="evaluate-updown"> <i class="wpfont icon-arrow-up"></i> <i class="wpfont icon-arrow-down"></i>'+
                    ' </td> <td> <input name="stutotalityTypeList['+ i +'].haveOn" class="wp wp-switch layer-myswitch" type="checkbox"  '+ alltotal+'>'+
                    ' <span class="lbl"> </span> </td> <td> <a href="javascript:void(0);" class="evaluate-btn-text layer-evaluate-del">删除</a></td> </tr>';
            });
            $(".layer-type-edit").find(".layer-table-content tbody").html(htmlstr);
            reorder();
            layer.open({type: 1, shadow: 0.5, area: "500px", title: "评价类别设置", content: $(".layer-type-edit")
            });
        });
        //上移
        $("body").on("click", ".evaluate-updown .icon-arrow-up", function() {
            if ($(this).hasClass("noremove")) {return false;}
            var old=$(this).parents("tr");
            var oldName=old.find(".hiddenClassName").attr("name");
            var oldId=old.find(".hiddenClassId").attr("name");
            var oldLayer=old.find(".layer-myswitch").attr("name");

            var prev = $(this).parents("tr").prev();
            var prevName=prev.find(".hiddenClassName").attr("name");
            var prevId=prev.find(".hiddenClassId").attr("name");
            var prevLayer=prev.find(".layer-myswitch").attr("name");

            old.find(".hiddenClassName").attr("name",prevName);
            old.find(".hiddenClassId").attr("name",prevId);
            old.find(".layer-myswitch").attr("name",prevLayer);
            prev.find(".hiddenClassName").attr("name",oldName);
            prev.find(".hiddenClassId").attr("name",oldId);
            prev.find(".layer-myswitch").attr("name",oldLayer);

            var oldhtml = old.clone();
            $(prev).before(oldhtml);
            $(this).parents("tr").remove();
            reorder();
        });
        //下移
        $("body").on("click", ".evaluate-updown .icon-arrow-down", function() {
            if ($(this).hasClass("noremove")) {return false;}
            var old=$(this).parents("tr");
            var oldName=old.find(".hiddenClassName").attr("name");
            var oldId=old.find(".hiddenClassId").attr("name");
            var oldLayer=old.find(".layer-myswitch").attr("name");

            var next = $(this).parents("tr").next();
            var nextName=next.find(".hiddenClassName").attr("name");
            var nextId=next.find(".hiddenClassId").attr("name");
            var nextLayer=next.find(".layer-myswitch").attr("name");
            old.find(".hiddenClassName").attr("name",nextName);
            old.find(".hiddenClassId").attr("name",nextId);
            old.find(".layer-myswitch").attr("name",nextLayer);
            next.find(".hiddenClassName").attr("name",oldName);
            next.find(".hiddenClassId").attr("name",oldId);
            next.find(".layer-myswitch").attr("name",oldLayer);

            var oldhtml = old.clone();
            $(next).after(oldhtml);
            $(this).parents("tr").remove();
            reorder();
        });
        var hrrr =  $(".evaluate-template-tree li").length;
        // hrrr++;
        //新增类别
        $('#typeAddId').unbind("click").bind("click", function(){
            // $("body").unbind().on("click", ".type-add", function() {
            var htmlstr = '<tr><td> <input type="text" id="typeNameId'+ hrrr +'"  nullable="false" name="stutotalityTypeList['+ hrrr +'].typeName" class="form-control evaluate-control hiddenClassName typeNameClass" value="" placeholder="请输入" /> </td>'+
                ' <input type="hidden" class="hiddenClassId" name="stutotalityTypeList['+ hrrr +'].id" value="">'+
                '<td class="evaluate-updown"> <i class="wpfont icon-arrow-up"></i> <i class="wpfont icon-arrow-down"></i> </td>'+
                '  <td> <input name="stutotalityTypeList['+ hrrr +'].haveOn" class="wp wp-switch layer-myswitch" type="checkbox" > <span class="lbl"> </span> </td>'+
                ' <td> <a href="javascript:void(0);" class="evaluate-btn-text layer-evaluate-del">删除</a> </td> </tr>';
            $(".layer-type-edit").find(".layer-table-content").append(htmlstr);
            hrrr++;
            reorder();
        });

        // 类别删除
        $("body").on("click", ".layer-evaluate-del", function() {
            $(this) .parents("tr") .remove();
            reorder();
        });
    });

    //评价类别设置箭头判断
    function reorder() {
        $(".layer-table-content tbody") .find(".icon-arrow-up") .removeClass("noremove");
        $(".layer-table-content tbody").find(".icon-arrow-down") .removeClass("noremove");
        $(".layer-table-content tbody") .find("tr")  .first() .find(".icon-arrow-up") .addClass("noremove");
        $(".layer-table-content tbody") .find("tr")  .last()  .find(".icon-arrow-down") .addClass("noremove");
    }

    //保存类别
    function sureaddType() {
        var check = checkValue("#typeForm")
        if(!check){
            return false;
        }
        var sametype = 0;
        var res = [];
        $(".typeNameClass").each(function(index, el) {
            var current = el.value;
            current = current.replace(/\s*/g,"");  //去掉所有的空格
            if (res.indexOf(current) === -1) {
                res.push(current)
            }else{
                sametype = 1;
               var id = "#typeNameId"+index;
                layerError($(id),'类别名不能相同');
            }
        });
        if(sametype == 1){
            isSubmit=false;
            return;
        }
        var options= {
            url:"${request.contextPath}/stutotality/type/save",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    successItemTemplate();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        }
        $('#typeForm').ajaxSubmit(options);
    }
    function itemAfterAdd (id) {
        var gradeName = $("#gradeName").val();
        if(id){
            var url = '${request.contextPath}/stutotality/itemtemplate/show?typeId='+id+'&gradeName='+gradeName;
            $('#itemListDiv').load(url);
        }
    }
    function successItemTemplate() {
        var url = '${request.contextPath}/stutotality/typetemplate/show?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}';
        $('#templateTabShow').load(url);
    }
    /*function hidelayer() {
        layer.closeAll();
    }*/
</script>